package com.alchitry.labs.project

import com.alchitry.labs.Log
import com.alchitry.labs.PathUtil
import com.alchitry.labs.mainWindow
import com.alchitry.labs.parsers.grammar.LucidLexer
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.ErrorManager
import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidGlobalContext
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleTypeContext
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.types.GlobalNamespace
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.TestBench
import com.alchitry.labs.project.files.ConstraintFile
import com.alchitry.labs.project.files.IPCore
import com.alchitry.labs.project.files.SourceFile
import com.alchitry.labs.ui.misc.openFileDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

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
    val projectFile = File(PathUtil.assemblePath(projectFolder, "$projectName.alp"))

    val scope = CoroutineScope(Dispatchers.Default)

    private val globals = mutableMapOf<String, GlobalNamespace>()
    private val modules = mutableMapOf<String, Module>()
    private val testBenches = mutableMapOf<String, TestBench>()

    private val evaluationQueue = mutableSetOf<Evaluable>()
    private val queueLock = Mutex()

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

    fun parse(errorManger: ErrorManager): ModuleInstance? {
        val trees = sourceFiles.map {
            val parser = LucidParser(
                CommonTokenStream(
                    LucidLexer(
                        CharStreams.fromStream(it.file.inputStream())
                    ).also { it.removeErrorListeners() })
            ).apply {
                (tokenStream.tokenSource as LucidLexer).addErrorListener(errorManger.getCollector(it))
                removeErrorListeners()
                addErrorListener(errorManger.getCollector(it))
            }

            it to parser.source()
        }

        if (!errorManger.hasNoIssues)
            return null

        trees.forEach {
            val globalContext = LucidGlobalContext(this, errorManger.getCollector(it.first))
            globalContext.walk(it.second)
        }

        if (errorManger.hasErrors)
            return null

        val modules = trees.mapNotNull {
            val moduleTypeContext = LucidModuleTypeContext(this, errorManger.getCollector(it.first))
            val module = moduleTypeContext.extract(it.second)

            it.first to (module ?: return@mapNotNull null)
        }

        if (errorManger.hasErrors)
            return null

        val topModule = modules.firstOrNull { it.first == top }?.second

        if (topModule == null) {
            errorManger.getCollector(top)
                .reportError(trees.first { it.first == top }.second, "Top file does not contain a module!")
            return null
        }

        val moduleInstance =
            ModuleInstance(top.name, this, null, topModule, mapOf(), mapOf(), errorManger.getCollector(top))

        moduleInstance.initialWalk()

        if (errorManger.hasErrors)
            return null

        return moduleInstance
    }

    fun getTestBenches(): List<TestBench> {
        return testBenches.values.toList()
    }

    var initializing: Boolean = false
        private set

    suspend fun initialize() {
        initializing = true
        processQueue()
        initializing = false
    }

    suspend fun queueEvaluation(evaluable: Evaluable) {
        queueLock.withLock {
            evaluationQueue.add(evaluable)
        }
    }

    suspend fun processQueue() {
        repeat(1000) {
            val items = queueLock.withLock {
                if (evaluationQueue.isEmpty())
                    return

                evaluationQueue.toList().also {
                    evaluationQueue.clear()
                }
            }
            coroutineScope {
                items.forEach {
                    launch(Dispatchers.Default) {
                        it.evaluate()
                    }
                }
            }
        }
        throw QueueExhaustionException("Failed to resolve a stable state after 1000 iterations. There is likely a dependency loop.")
    }

    fun addGlobal(globalNamespace: GlobalNamespace): Boolean =
        globals.putIfAbsent(globalNamespace.name, globalNamespace) == null

    fun addModule(module: Module): Boolean =
        modules.putIfAbsent(module.name, module) == null

    fun addTestBench(testBench: TestBench): Boolean =
        testBenches.putIfAbsent(testBench.name, testBench) == null

    /**
     * Searches all GlobalNamespaces to resolve a signal name.
     */
    fun resolveSignal(name: String): SignalOrParent? = globals[name]

    fun resolveGlobal(name: String) = globals[name]

    fun resolveModuleType(name: String): Module? = modules[name]
}