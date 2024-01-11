package com.alchitry.labs.parsers

import com.alchitry.labs.Log
import com.alchitry.labs.parsers.lucidv2.types.*
import com.alchitry.labs.parsers.notations.NotationManager
import com.alchitry.labs.project.QueueExhaustionException
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.Closeable

class ProjectContext(val notationManager: NotationManager) : Closeable {
    var top: ModuleInstance? = null
    private val globals = mutableMapOf<String, GlobalNamespace>()
    private val modules = mutableMapOf<String, Module>()
    private val testBenches = mutableMapOf<String, TestBench>()
    val scope = CoroutineScope(Dispatchers.Default)

    private val evaluationQueue = mutableSetOf<Evaluable>()
    private val queueLock = Mutex()

    var initializing: Boolean = false
        private set

    suspend fun initialize() {
        initializing = true
        processQueue()
        initializing = false
    }

    suspend fun clearQueue() {
        queueLock.withLock {
            evaluationQueue.clear()
        }
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

    suspend fun convertToVerilog(): Map<String, String?> {
        val topInstance = top ?: error("Top level module instance missing!")
        val instances = mutableMapOf<String, ModuleInstance>()
        fun add(instance: ModuleInstance) {
            instances[instance.parameterizedModuleName] = instance
            instance.context.types.moduleInstances.values.forEach { moduleInstanceOrArray ->
                when (moduleInstanceOrArray) {
                    is ModuleInstance -> add(moduleInstanceOrArray)
                    is ModuleInstanceArray -> moduleInstanceOrArray.modules.forEach { add(it) }
                }
            }
        }
        add(topInstance)
        return instances.mapValues {
            try {
                it.value.context.convertToVerilog() ?: error("Missing verilog for ${it.key}")
            } catch (e: Exception) {
                Log.printlnError("Error while converting to Verilog! This is a bug!", e)
                null
            }
        }
    }

    override fun close() {
        scope.cancel("ProjectContext closed.")
    }

    fun getTestBenches(): List<TestBench> {
        return testBenches.values.toList()
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