package com.alchitry.labs.project

import com.alchitry.labs.Log
import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.acf.NativeConstraint
import com.alchitry.labs.parsers.errors.ErrorManager
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.project.files.ConstraintFile
import com.alchitry.labs.project.files.IPCore
import com.alchitry.labs.project.files.SourceFile
import com.alchitry.labs.ui.misc.openFileDialog
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.windows.mainWindow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import java.io.File
import kotlin.io.path.createDirectories

class QueueExhaustionException(message: String) : IllegalStateException(message)

data class Project(
    val projectName: String,
    val projectFolder: File,
    val board: Board,
    val sourceFiles: Set<SourceFile>,
    val constraintFiles: Set<ConstraintFile>,
    val ipCores: Set<IPCore>
) {
    val top: SourceFile get() = sourceFiles.firstOrNull { it.top } ?: throw Exception("Missing top module!")
    val projectFile = projectFolder.resolve("$projectName.alp")
    val buildDirectory = projectFolder.resolve("build")
    val binFile = buildDirectory.resolve("${board.binName}.bin")

    companion object {
        private val mutableCurrentFlow = MutableStateFlow<Project?>(null)
        val currentFlow = mutableCurrentFlow.asStateFlow()

        var current: Project?
            get() = mutableCurrentFlow.value
            set(value) {
                mutableCurrentFlow.tryEmit(value)
            }

        fun openProject(file: File? = null): Project? {
            try {
                val projectFile =
                    file ?: openFileDialog(mainWindow, "Open Project", listOf(".alp"), allowMultiSelection = false)
                        .firstOrNull() ?: return null
                val project = openXml(projectFile)
                mutableCurrentFlow.tryEmit(project)
                return project
            } catch (e: Exception) {
                Log.showError("Failed to open project: ${e.message}")
                return null
            }
        }
    }

    suspend fun check(): ProjectContext? {
        val errorManger = ErrorManager()
        val context = buildContext(errorManger)
        val topModule = context?.top
        if (context == null || topModule == null) {
            Log.printlnError("Failed to build project context!")
            Log.print(errorManger.getReport())
            return null
        }

        Log.print(errorManger.getReport())
        return context
    }

    suspend fun build(): Boolean {
        val context = check() ?: return false
        val topModule = context.top ?: return false

        val sourceFiles = try {
            context.convertToVerilog()
        } catch (e: Exception) {
            Log.printlnError("Failed to convert source files to Verilog. This should be considered a bug!", e)
            return false
        }

        val sourceDir = buildDirectory.resolve("source")

        try {
            buildDirectory.deleteRecursively()
        } catch (e: Exception) {
            Log.printlnError("Failed to delete build directory!", e)
            return false
        }

        val vSourceFiles: List<File>
        try {
            sourceDir.toPath().createDirectories()
            vSourceFiles = sourceFiles.map { (name, contents) ->
                val file = sourceDir.resolve("$name.v")
                file.outputStream().bufferedWriter().use {
                    it.write(contents ?: error("Missing contents for file $name"))
                }
                file
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to write source files to ${sourceDir.absolutePath}", e)
            return false
        }

        val constraintErrorManager = ErrorManager()
        val constraints = try {
            constraintFiles.flatMap { file ->
                when (file.language) {
                    Languages.ACF -> {
                        val files =
                            board.acfConverter.convert(file, topModule, constraintErrorManager.getCollector(file))
                        files?.map { file.name to it } ?: listOf(file.name to null)
                    }

                    Languages.XDC, Languages.SDC, Languages.PCF ->
                        listOf(
                            file.name to NativeConstraint(
                                file.name.split(".").first(),
                                file.language,
                                file.file.inputStream().bufferedReader().readText()
                            )
                        )
                }
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to get constraint files!", e)
            return false
        }

        if (!constraintErrorManager.hasNoMessages) {
            Log.print(constraintErrorManager.getReport(), AlchitryColors.current.Error)
            return false
        }

        val checkedConstraints = constraints.map {
            it.second ?: error("Missing contents for file ${it.first}")
        }

        val mergedSdcBuilder = StringBuilder()
        val mergedPcfBuilder = StringBuilder()
        checkedConstraints.forEach { nativeConstraint ->
            when (nativeConstraint.language) {
                Languages.PCF -> mergedPcfBuilder.append(nativeConstraint.contents).appendLine()
                Languages.SDC -> mergedSdcBuilder.append(nativeConstraint.contents).appendLine()
                else -> {}
            }
        }

        val mergedConstraints = checkedConstraints.filter {
            when (it.language) {
                Languages.PCF, Languages.SDC -> false
                else -> true
            }
        }.toMutableList()

        if (mergedPcfBuilder.isNotBlank()) {
            mergedConstraints.add(
                NativeConstraint(
                    "merged_constraints",
                    Languages.PCF,
                    mergedPcfBuilder.toString()
                )
            )
        }

        if (mergedSdcBuilder.isNotBlank()) {
            mergedConstraints.add(
                NativeConstraint(
                    "merged_constraints",
                    Languages.SDC,
                    mergedSdcBuilder.toString()
                )
            )
        }

        val constraintFiles: List<File>
        val constraintDir = buildDirectory.resolve("constraint")
        try {
            constraintDir.toPath().createDirectories()
            constraintFiles = mergedConstraints.map { nativeConstraint ->
                val file = constraintDir.resolve("${nativeConstraint.name}.${nativeConstraint.language.extension}")
                file.outputStream().bufferedWriter().use {
                    it.write(nativeConstraint.contents)
                }
                file
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to write constraint files to ${constraintDir.absolutePath}", e)
            return false
        }

        board.projectBuilder.buildProject(this, topModule.parameterizedModuleName, vSourceFiles, constraintFiles)

        return true
    }

    fun parse(errorManger: ErrorManager): List<Pair<SourceFile, LucidParser.SourceContext>>? {
        val trees = sourceFiles.map { file ->
            val parser = LucidParser(
                CommonTokenStream(
                    LucidLexer(
                        CharStreams.fromStream(file.file.inputStream())
                    ).also { it.removeErrorListeners() })
            ).apply {
                (tokenStream?.tokenSource as? LucidLexer)?.addErrorListener(errorManger.getCollector(file))
                    ?: error("TokenSource was not a LucidLexer!")
                removeErrorListeners()
                addErrorListener(errorManger.getCollector(file))
            }

            file to parser.source()
        }
        if (!errorManger.hasNoMessages)
            return null
        return trees
    }

    suspend fun buildContext(
        errorManger: ErrorManager,
        trees: List<Pair<SourceFile, LucidParser.SourceContext>>? = parse(errorManger)
    ): ProjectContext? {
        val projectContext = ProjectContext()
        var success = false

        try {
            if (trees == null)
                return null

            trees.forEach {
                val globalContext = LucidGlobalContext(projectContext, errorManger.getCollector(it.first))
                globalContext.walk(it.second)
            }

            if (errorManger.hasErrors)
                return null

            val modules = trees.mapNotNull {
                val moduleTypeContext = LucidModuleTypeContext(projectContext, errorManger.getCollector(it.first))
                val module = moduleTypeContext.extract(it.second)

                it.first to (module ?: return@mapNotNull null)
            }

            if (errorManger.hasErrors)
                return null

            trees.forEach {
                val testBenchContext = LucidTestBenchContext(projectContext, errorManger.getCollector(it.first))
                testBenchContext.walk(it.second)
            }

            if (errorManger.hasErrors)
                return null

            projectContext.getTestBenches().forEach { it.initialWalk() }

            if (errorManger.hasErrors)
                return null

            val topModule = modules.firstOrNull { it.first == top }?.second

            if (topModule == null) {
                errorManger.getCollector(top)
                    .reportError(trees.first { it.first == top }.second, "Top file does not contain a module!")
                return null
            }

            val moduleInstance =
                ModuleInstance(
                    top.name,
                    projectContext,
                    null,
                    topModule,
                    mapOf(),
                    mapOf(),
                    errorManger.getCollector(top)
                )

            moduleInstance.initialWalk()

            if (errorManger.hasErrors)
                return null

            projectContext.top = moduleInstance
            success = true
        } finally {
            if (!success)
                projectContext.close()
        }

        return projectContext
    }
}