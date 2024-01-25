package com.alchitry.labs2.project

import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.AcfExtractor
import com.alchitry.labs2.parsers.acf.NativeConstraint
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidTestBenchContext
import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.ValueFormat
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.parsers.notations.NotationType
import com.alchitry.labs2.project.files.*
import com.alchitry.labs2.ui.code_editor.TextPosition
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
    private val ideProjectContextFlow = MutableStateFlow<ProjectContext?>(null)
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
            ideProjectContextFlow.value?.close() // close the old one
            val context = buildContext(notationManager)
            notationManagerFlow.tryEmit(notationManager)
            ideProjectContextFlow.tryEmit(context)
            context?.getTestBenches()?.forEach { testBench ->
                val collector = notationManager.getCollector(testBench.sourceFile)
                testBench.getTestBlocks().forEach forEachTest@{ test ->
                    val line = test.testBlockContext.start?.line ?: return@forEachTest
                    collector.addLineAction(line - 1) {
                        LineActionButton("icons/play.svg", "Run ${test.name}") {
                            // TODO: move this scope somewhere else as this could be canceled while running
                            context.scope.launch {
                                runTest(testBench, test)
                            }
                        }
                    }
                }
            }
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
                    Languages.ACF -> emptyList()
                    Languages.XDC, Languages.SDC, Languages.PCF ->
                        listOf(
                            NativeConstraint(
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
        } + board.acfConverter.convert("alchitry", board, context.getConstraints())

        if (!constraintNotationManager.hasNoMessages) {
            Log.print(constraintNotationManager.getReport(), AlchitryColors.current.Error)
            return@withContext false
        }

        val mergedSdcBuilder = StringBuilder()
        val mergedPcfBuilder = StringBuilder()
        constraints.forEach { nativeConstraint ->
            when (nativeConstraint.language) {
                Languages.PCF -> mergedPcfBuilder.append(nativeConstraint.contents).appendLine()
                Languages.SDC -> mergedSdcBuilder.append(nativeConstraint.contents).appendLine()
                else -> {}
            }
        }

        val mergedConstraints = constraints.filter {
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
            }

            if (notationManager.hasErrors)
                return null

            val topModule = modules.firstOrNull { it.first == top }?.second

            if (topModule == null) {
                notationManager.getCollector(top)
                    .reportError(trees.first { it.first == top }.second, "Top file does not contain a module!")
                return null
            }

            val topModuleInstance =
                ModuleInstance(
                    "alchitryTop",
                    projectContext,
                    null,
                    topModule,
                    mapOf(),
                    mapOf(),
                    //notationManager.getCollector(top)
                )

            topModuleInstance.initialWalk()

            // if modules aren't used but are still in the project, we need to check it outside the initial walk
            // of the top level instance
            val usedModules =
                (topModuleInstance.getAllModules() + projectContext.getTestBenches()
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

            projectContext.top = topModuleInstance

            constraintFiles.sortedBy {
                val prefix = if (it.isLibFile) "a" else "b"
                prefix + it.name
            }.forEach { constraintFile ->
                when (constraintFile.language) {
                    Languages.ACF -> {
                        AcfExtractor.extract(
                            projectContext,
                            constraintFile,
                            topModuleInstance,
                            board,
                            notationManager.getCollector(constraintFile)
                        ) ?: return@forEach
                    }

                    Languages.PCF, Languages.SDC, Languages.XDC -> notationManager.getCollector(constraintFile)
                        .addNotation(
                            Notation(
                                "${constraintFile.language.name} constraints are not supported yet!",
                                TextPosition(1, 1)..TextPosition(1, 1),
                                NotationType.Warning
                            )
                        )
                }
            }

            if (notationManager.hasErrors)
                return null

            val ports = topModule.ports.values.associate {
                it.name to Signal(
                    name = it.name,
                    direction = SignalDirection.Read,
                    parent = null,
                    initialValue = it.width.filledWith(Bit.B0, constant = false, signed = false),
                    signed = false
                )
            }

            projectContext.getConstraints().forEach {
                val port = it.port
                val signal = ports[port.getSignal().name] ?: return@forEach // error should already be caught

                when (port) {
                    is Signal -> signal.write(signal.width.filledWith(Bit.B1, constant = false, signed = false))
                    is SubSignal -> signal.select(port.selection)
                        .apply { write(width.filledWith(Bit.B1, constant = false, signed = false)) }
                }
            }

            ports.values.forEach {
                val constrained = it.read()
                if (constrained.andReduce().bit != Bit.B1) {
                    val portCtx = topModule.ports[it.name]?.context ?: error("Missing context for port \"${it.name}\"!")

                    if (constrained.orReduce().bit == Bit.B1) {
                        notationManager.getCollector(topModule.sourceFile).reportError(
                            portCtx,
                            "Top level port \"${it.name}\" isn't fully constrained. Bits marked as 0 aren't constrained: ${
                                constrained.toString(ValueFormat.Binary)
                            }"
                        )
                    } else {
                        notationManager.getCollector(topModule.sourceFile)
                            .reportError(portCtx, "Top level port \"${it.name}\" isn't constrained.")

                    }
                }
            }

            if (notationManager.hasErrors)
                return null

            success = true
        } finally {
            if (!success)
                projectContext.close()
        }

        return projectContext
    }

}

