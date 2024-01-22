package com.alchitry.labs2.project

import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.NativeConstraint
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.TestBench
import com.alchitry.labs2.parsers.lucidv2.types.TestBlock
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.project.files.*
import com.alchitry.labs2.ui.code_editor.line_actions.LineActionButton
import com.alchitry.labs2.ui.misc.openFileDialog
import com.alchitry.labs2.ui.tabs.SimulationResultTab
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.windows.mainWindow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import java.io.File
import kotlin.io.path.createDirectories
import kotlin.math.max

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
    val sourceDirectory = projectFolder.resolve("source")
    val constraintDirectory = projectFolder.resolve("constraint")
    val binFile = buildDirectory.resolve("${board.binName}.bin")
    private val notationManagerFlow = MutableStateFlow<NotationManager?>(null)
    val scope = CoroutineScope(Dispatchers.Default)

    fun binFileIsUpToDate(): Boolean = binFile.lastModified() >= lastModified() && binFile.lastModified() > 0L
    fun lastModified(): Long {
        return max(
            max(
                sourceFiles.mapNotNull { (it.file as? FileProvider.DiskFile)?.file?.lastModified() }.maxOrNull() ?: 0L,
                constraintFiles.mapNotNull { (it.file as? FileProvider.DiskFile)?.file?.lastModified() }.maxOrNull()
                    ?: 0L
            ),
            ipCores.maxOfOrNull { it.files.maxOfOrNull { f -> f.lastModified() } ?: 0 } ?: 0L
        )
    }

    fun notationCollectorFlowForFile(projectFile: ProjectFile): Flow<NotationCollector?> =
        notationManagerFlow.map { it?.getCollector(projectFile) }

    fun currentNotationCollectorForFile(projectFile: ProjectFile): NotationCollector? =
        notationManagerFlow.value?.getCollector(projectFile)

    companion object {
        private val mutableCurrentFlow = MutableStateFlow<Project?>(null)
        val currentFlow = mutableCurrentFlow.asStateFlow()

        var current: Project?
            get() = mutableCurrentFlow.value
            set(value) {
                mutableCurrentFlow.tryEmit(value)
            }

        fun open(project: Project): Project {
            if (current != null)
                close()
            project.queueNotationsUpdate()
            mutableCurrentFlow.tryEmit(project)
            Settings.openProject = project.projectFile.absolutePath
            return project
        }

        fun open(file: File? = null): Project? {
            try {
                val projectFile =
                    file ?: openFileDialog(
                        mainWindow,
                        "Open Project",
                        listOf(".alp"),
                        allowMultiSelection = false,
                        startingDirectory = Settings.workspace?.let { File(it) })
                        .firstOrNull() ?: return null
                val project = openXml(projectFile)
                return open(project)
            } catch (e: Exception) {
                Log.showError("Failed to open project: ${e.message}")
                return null
            }
        }

        fun close() {
            mutableCurrentFlow.tryEmit(null)
        }
    }

    fun queueNotationsUpdate() {
        scope.launch {
            //notationManagerFlow.tryEmit(null)
            val notationManager = NotationManager()
            buildContext(notationManager)
            notationManagerFlow.tryEmit(notationManager)
        }
    }

    private suspend fun runTest(testBench: TestBench, test: TestBlock) {
        Log.println("Running ${testBench.name}.${test.name}...")
        val result = testBench.runTest(test.name)
        if (result == null) {
            Log.warn("Simulation resulted in no snapshots. Make sure you call ${"$"}tick() if this wasn't intentional.")
        } else {
            Workspace.activeTabPanel().apply {
                addTab(
                    SimulationResultTab("${testBench.name}.${test.name}", result, this)
                )
            }
        }
        Log.success("Done.")
        Log.println()
    }

    suspend fun runAllTestBenches() = withContext(Dispatchers.Default) {
        check()?.use { context ->
            context.getTestBenches().forEach { testBench ->
                testBench.getTestBlocks().forEach { testBlock ->
                    runTest(testBench, testBlock)
                }
            }
        }
    }

    suspend fun check(): ProjectContext? {
        val notationManager = NotationManager()
        val context = buildContext(notationManager)
        val topModule = context?.top
        if (context == null || topModule == null) {
            Log.print(notationManager.getReport())
            return null
        }

        Log.print(notationManager.getReport())
        return context
    }

    suspend fun build(): Boolean = withContext(Dispatchers.Default) {
        val context = check() ?: return@withContext false
        val topModule = context.top ?: return@withContext false

        val sourceFiles = try {
            context.convertToVerilog()
        } catch (e: Exception) {
            Log.printlnError("Failed to convert source files to Verilog. This should be considered a bug!", e)
            return@withContext false
        }

        val sourceDir = buildDirectory.resolve("source")

        try {
            buildDirectory.deleteRecursively()
        } catch (e: Exception) {
            Log.printlnError("Failed to delete build directory!", e)
            return@withContext false
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
            return@withContext false
        }

        val constraintNotationManager = NotationManager()
        val constraints = try {
            constraintFiles.flatMap { file ->
                when (file.language) {
                    Languages.ACF -> {
                        val files =
                            board.acfConverter.convert(file, topModule, constraintNotationManager.getCollector(file))
                        files?.map { file.name to it } ?: listOf(file.name to null)
                    }

                    Languages.XDC, Languages.SDC, Languages.PCF ->
                        listOf(
                            file.name to NativeConstraint(
                                file.name.split(".").first(),
                                file.language,
                                file.readText()
                            )
                        )
                }
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to get constraint files!", e)
            return@withContext false
        }

        if (!constraintNotationManager.hasNoMessages) {
            Log.print(constraintNotationManager.getReport(), AlchitryColors.current.Error)
            return@withContext false
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
            return@withContext false
        }

        board.projectBuilder.buildProject(
            this@Project,
            topModule.parameterizedModuleName,
            vSourceFiles,
            constraintFiles
        )

        return@withContext true
    }

    suspend fun parse(errorManger: NotationManager): List<Pair<SourceFile, LucidParser.SourceContext>>? {
        val trees = coroutineScope {
            sourceFiles
                .map { file -> file to errorManger.getCollector(file) } // do this first to avoid race conditions
                .map { (file, collector) ->
                    async {
                        val parser = LucidParser(
                            CommonTokenStream(
                                LucidLexer(
                                    CharStreams.fromString(
                                        file.readText(),
                                        file.name
                                    )
                                ).apply {
                                    removeErrorListeners()
                                    addErrorListener(collector)
                                })
                        ).apply {
                            removeErrorListeners()
                            addErrorListener(collector)
                        }

                        file to parser.source()
                    }
                }.awaitAll()
        }
        if (!errorManger.hasNoMessages)
            return null
        return trees
    }

    suspend fun buildContext(
        notationManager: NotationManager,
        parsedTrees: List<Pair<SourceFile, LucidParser.SourceContext>>? = null
    ): ProjectContext? {
        val projectContext = ProjectContext(notationManager)
        var success = false
        val trees = parsedTrees ?: parse(notationManager)

        try {
            if (trees == null || notationManager.hasErrors)
                return null

            trees.forEach {
                LucidGlobalContext(projectContext, it.first).walk(it.second)
            }

            if (notationManager.hasErrors)
                return null

            val modules = trees.mapNotNull {
                val moduleTypeContext = LucidModuleTypeContext(projectContext, it.first)
                val module = moduleTypeContext.extract(it.second)

                it.first to (module ?: return@mapNotNull null)
            }

            if (notationManager.hasErrors)
                return null

            trees.forEach {
                LucidTestBenchContext(projectContext, it.first).walk(it.second)
            }

            if (notationManager.hasErrors)
                return null

            projectContext.getTestBenches().forEach { testBench ->
                testBench.initialWalk()
                val collector = notationManager.getCollector(testBench.sourceFile)
                testBench.getTestBlocks().forEach forEachTest@{ test ->
                    val line = test.testBlockContext.start?.line ?: return@forEachTest
                    collector.addLineAction(line - 1) {
                        LineActionButton("icons/play.svg", "Run ${test.name}") {
                            projectContext.scope.launch {
                                runTest(testBench, test)
                            }
                        }
                    }
                }
            }

            if (notationManager.hasErrors)
                return null

            val topModule = modules.firstOrNull { it.first == top }?.second

            if (topModule == null) {
                notationManager.getCollector(top)
                    .reportError(trees.first { it.first == top }.second, "Top file does not contain a module!")
                return null
            }

            val moduleInstance =
                ModuleInstance(
                    "alchitryTop",
                    projectContext,
                    null,
                    topModule,
                    mapOf(),
                    mapOf(),
                    //notationManager.getCollector(top)
                )

            moduleInstance.initialWalk()

            // if modules aren't used but are still in the project, we need to check it outside the initial walk
            // of the top level instance
            val usedModules =
                (moduleInstance.getAllModules() + projectContext.getTestBenches()
                    .flatMap { it.getAllModules() }).distinct()
            val uncheckedModules = modules.map { it.second }.toMutableList().apply {
                removeAll(usedModules)
            }

            uncheckedModules.forEach { module ->
                projectContext.notationManager.getCollector(module.sourceFile)
                    .reportWarning(
                        module.context.name() ?: module.context,
                        "The module \"${module.name}\" is not used."
                    )
            }

            while (uncheckedModules.isNotEmpty()) {
                val mod = uncheckedModules.first() // get the next one to check
                ModuleInstance(
                    mod.name,
                    projectContext,
                    null,
                    mod,
                    mapOf(),
                    mapOf(),
                    // notationManager.getCollector(mod.sourceFile)
                ).apply {
                    initialWalk()
                    uncheckedModules.removeAll(getAllModules()) // remove this one plus any checked as its dependants
                }
            }

            if (notationManager.hasErrors)
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

