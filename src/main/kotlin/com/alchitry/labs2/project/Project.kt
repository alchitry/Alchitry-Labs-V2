package com.alchitry.labs2.project

import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.AcfExtractor
import com.alchitry.labs2.parsers.acf.NativeConstraint
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.VerilogLexer
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidGlobalContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidModuleTypeContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidTestBenchContext
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.ValueFormat
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogModuleTypeContext
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.parsers.notations.NotationType
import com.alchitry.labs2.project.files.Component
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.ui.code_editor.TextPosition
import com.alchitry.labs2.ui.code_editor.line_actions.LineActionButton
import com.alchitry.labs2.ui.tabs.SimulationResultTab
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.math.max

class QueueExhaustionException(message: String) : IllegalStateException(message)


data class Project(
    val path: Path,
    val data: ProjectData1V0
) {
    val top: SourceFile get() = data.sourceFiles.firstOrNull { it.top } ?: throw Exception("Missing top module!")
    val projectFile: File = path.resolve("${data.projectName}.alp").toFile()
    val buildDirectory: Path = path.resolve("build")
    val sourceDirectory: Path = path.resolve("source")
    val constraintDirectory: Path = path.resolve("constraint")
    val binFile: File = buildDirectory.resolve("${data.board.binName}.bin").toFile()
    private val mutableProjectContextFlow = MutableStateFlow<ProjectContext?>(null)
    private val notationManagerFlow = MutableStateFlow<NotationManager?>(null)
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val components: List<Component> =
        (data.sourceFiles + data.constraintFiles).mapNotNull { if (it.file is Component) it.file else null }

    private val mutableGlobalMapFlow = MutableStateFlow<Map<String, GlobalNamespace>>(emptyMap())
    val globalMapFlow = mutableGlobalMapFlow.asStateFlow()

    private val mutableModuleMapFlow = MutableStateFlow<Map<SourceFile, List<Module>>>(emptyMap())
    val moduleMapFlow = mutableModuleMapFlow.asStateFlow()

    fun binFileIsUpToDate(): Boolean = binFile.lastModified() >= lastModified() && binFile.lastModified() > 0L
    fun lastModified(): Long {
        return max(
            data.sourceFiles.mapNotNull { (it.file as? FileProvider.DiskFile)?.file?.lastModified() }.maxOrNull()
                ?: 0L,
            data.constraintFiles.mapNotNull { (it.file as? FileProvider.DiskFile)?.file?.lastModified() }
                .maxOrNull() ?: 0L
        )
    }

    fun notationCollectorFlowForFile(projectFile: ProjectFile): Flow<NotationCollector?> =
        notationManagerFlow.map { it?.getCollector(projectFile) }

    fun currentNotationCollectorForFile(projectFile: ProjectFile): NotationCollector? =
        notationManagerFlow.value?.getCollector(projectFile)

    fun save() = saveAs(projectFile)

    fun saveAs(file: File) {
        val alpData = AlchitryLabsProjectData(data)
        val text = json.encodeToString(AlchitryLabsProjectData.serializer(), alpData)
        file.writeText(text)
    }

    companion object {
        private val json = Json {
            prettyPrint = true
        }
        private val mutableCurrentFlow = MutableStateFlow<Project?>(null)
        val currentFlow = mutableCurrentFlow.asStateFlow()

        val current: Project?
            get() = mutableCurrentFlow.value

        fun open(file: File): Project = open(load(file))

        fun open(project: Project): Project {
            if (project.projectFile != current?.projectFile)
                close()
            mutableCurrentFlow.tryEmit(project)
            Settings.openProject = project.projectFile.absolutePath
            project.queueNotationsUpdate()
            return project
        }

        private fun validateProjectFile(file: File): Boolean {
            if (!file.exists()) {
                error("The file ${file.path} does not exist!")
            }

            if (!file.isFile || file.extension != "alp") {
                error("The file ${file.path} is not an Alchitry Labs project file (.alp)!")
            }
            return true
        }

        fun load(file: File, json: Json = this.json): Project {
            validateProjectFile(file)
            if (isXmlProject(file)) {
                val projectData = openXml(file)
                return Project(file.parentFile.toPath(), projectData)
            }
            val alpData = json.decodeFromString(AlchitryLabsProjectData.serializer(), file.readText())
            return Project(file.parentFile.toPath(), alpData.project.upgradeToLatest())
        }

        fun close() {
            Workspace.closeAll()
            mutableCurrentFlow.tryEmit(null)
        }

        suspend fun parseLucidFile(
            file: SourceFile,
            notationCollector: NotationCollector
        ): LucidParser.SourceContext {
            return LucidParser(
                CommonTokenStream(
                    LucidLexer(
                        CharStreams.fromString(
                            file.readText(),
                            file.name
                        )
                    ).apply {
                        removeErrorListeners()
                        addErrorListener(notationCollector)
                    })
            ).apply {
                removeErrorListeners()
                addErrorListener(notationCollector)
            }.source()
        }

        suspend fun parseVerilogFile(
            file: SourceFile,
            notationCollector: NotationCollector
        ): VerilogParser.Source_textContext {
            return VerilogParser(
                CommonTokenStream(
                    VerilogLexer(
                        CharStreams.fromString(
                            file.readText(),
                            file.name
                        )
                    ).apply {
                        removeErrorListeners()
                        addErrorListener(notationCollector)
                    })
            ).apply {
                removeErrorListeners()
                addErrorListener(notationCollector)
            }.source_text()
        }

        suspend fun parseAll(
            sourceFiles: Collection<SourceFile>,
            notationManager: NotationManager
        ): List<Pair<SourceFile, ParserRuleContext>>? {
            val trees = coroutineScope {
                sourceFiles
                    .map { file ->
                        val collector = notationManager.getCollector(file)
                        async {
                            file to when (file.language) {
                                Languages.Lucid -> parseLucidFile(file, collector)
                                Languages.Verilog -> parseVerilogFile(file, collector)
                            }
                        }
                    }.awaitAll()
            }
            if (!notationManager.hasNoMessages)
                return null
            return trees
        }
    }

    fun queueNotationsUpdate() {
        scope.launch {
            //notationManagerFlow.tryEmit(null)
            val notationManager = NotationManager()
            mutableProjectContextFlow.value?.close() // close the old one
            val context = buildContext(notationManager)
            notationManagerFlow.tryEmit(notationManager)
            mutableProjectContextFlow.tryEmit(context)
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
        }.invokeOnCompletion { cause ->
            if (cause != null && cause !is CancellationException) {
                Log.exception(cause)
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

    suspend fun check(simulating: Boolean = false): ProjectContext? {
        val notationManager = NotationManager()
        val context = buildContext(notationManager, simulating = simulating)
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
            buildDirectory.toFile().deleteRecursively()
        } catch (e: Exception) {
            Log.printlnError("Failed to delete build directory!", e)
            return@withContext false
        }

        val vSourceFiles: List<File>
        try {
            sourceDir.createDirectories()
            vSourceFiles = sourceFiles.map { (name, contents) ->
                val file = sourceDir.resolve("$name.sv").toFile()
                file.outputStream().bufferedWriter().use {
                    it.write(contents ?: error("Missing contents for file $name"))
                }
                file
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to write source files to ${sourceDir.absolutePathString()}", e)
            return@withContext false
        }

        val constraintNotationManager = NotationManager()

        val constraints = try {
            data.constraintFiles.flatMap { file ->
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
        } + data.board.acfConverter.convert("alchitry", data.board, context.getConstraints())

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
            constraintDir.createDirectories()
            constraintFiles = mergedConstraints.map { nativeConstraint ->
                val file =
                    constraintDir.resolve("${nativeConstraint.name}.${nativeConstraint.language.extension}").toFile()
                file.outputStream().bufferedWriter().use {
                    it.write(nativeConstraint.contents)
                }
                file
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to write constraint files to ${constraintDir.absolutePathString()}", e)
            return@withContext false
        }

        data.board.projectBuilder.buildProject(
            this@Project,
            topModule.module.name,
            vSourceFiles,
            constraintFiles
        )

        return@withContext true
    }

    suspend fun getTypesForLucidFile(file: SourceFile): ProjectContext {
        val modulesMap = moduleMapFlow.value
        val globals = globalMapFlow.value
        val notationManager = NotationManager()
        ProjectContext(notationManager).use { projectContext ->
            globals.forEach { (_, global) -> projectContext.addGlobal(global) }
            modulesMap.forEach { (_, modules) -> modules.forEach { module -> projectContext.addModule(module) } }

            val tree = parseLucidFile(file, notationManager.getCollector(file))
            LucidTestBenchContext(projectContext, file).walk(tree)

            (modulesMap[file]
                ?: LucidModuleTypeContext(projectContext, file).extract(tree)).firstOrNull()?.let { module ->
                projectContext.top = ModuleInstance(
                    "testingTop",
                    projectContext,
                    null,
                    module,
                    mapOf(),
                    mapOf(),
                    testing = true
                ).apply { initialWalk() }
            }

            return projectContext
        }
    }

    suspend fun buildContext(
        notationManager: NotationManager,
        parsedTrees: List<Pair<SourceFile, LucidParser.SourceContext>>? = null,
        simulating: Boolean = false
    ): ProjectContext? {
        val projectContext = ProjectContext(notationManager, simulating)
        var success = false
        val trees = parsedTrees ?: parseAll(data.sourceFiles, notationManager)

        try {
            if (trees == null || notationManager.hasErrors)
                return null

            trees.forEach {
                when (it.first.language) {
                    Languages.Lucid -> LucidGlobalContext(projectContext, it.first).walk(it.second)
                    Languages.Verilog -> {} // No globals in Verilog
                }
            }

            mutableGlobalMapFlow.tryEmit(projectContext.getGlobals())

            if (notationManager.hasErrors)
                return null

            val modulesMap = trees.associate {
                it.first to when (it.first.language) {
                    Languages.Lucid -> LucidModuleTypeContext(projectContext, it.first).extract(it.second)
                    Languages.Verilog -> VerilogModuleTypeContext(projectContext, it.first).extract(it.second)
                }
            }

            mutableModuleMapFlow.tryEmit(modulesMap)

            val modules = modulesMap.flatMap { (file, list) ->
                list.map { module ->
                    file to module
                }
            }

            if (notationManager.hasErrors)
                return null

            trees.forEach {
                when (it.first.language) {
                    Languages.Lucid -> LucidTestBenchContext(projectContext, it.first).walk(it.second)
                    Languages.Verilog -> {} // No test benches for Verilog
                }
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
                    testing = true
                    // notationManager.getCollector(mod.sourceFile)
                ).apply {
                    initialWalk()
                    uncheckedModules.removeAll(getAllModules()) // remove this one plus any checked as its dependants
                }
            }

            if (notationManager.hasErrors)
                return null

            projectContext.top = topModuleInstance

            data.constraintFiles.sortedBy {
                val prefix = if (it.isLibFile) "a" else "b"
                prefix + it.name
            }.forEach { constraintFile ->
                when (constraintFile.language) {
                    Languages.ACF -> {
                        AcfExtractor.extract(
                            projectContext,
                            constraintFile,
                            topModuleInstance,
                            data.board,
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
                    initialValue = it.width.filledWith(Bit.B0, signed = false),
                    type = ExprType.Dynamic,
                    signed = false
                )
            }

            projectContext.getConstraints().forEach {
                val port = it.port
                val signal = ports[port.getSignal().name] ?: return@forEach // error should already be caught

                when (port) {
                    is Signal -> signal.write(signal.width.filledWith(Bit.B1, signed = false))
                    is SubSignal -> signal.select(port.selection)
                        .apply { write(width.filledWith(Bit.B1, signed = false)) }
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

