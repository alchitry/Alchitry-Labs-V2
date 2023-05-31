package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.types.GlobalNamespace
import com.alchitry.labs.parsers.lucidv2.types.Module
import com.alchitry.labs.parsers.lucidv2.types.TestBench
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class QueueExhaustionException(message: String) : IllegalStateException(message)

class ProjectContext {
    val scope = CoroutineScope(Dispatchers.Default)

    private val globals = mutableMapOf<String, GlobalNamespace>()
    private val modules = mutableMapOf<String, Module>()
    private val testBenches = mutableMapOf<String, TestBench>()

    private val evaluationQueue = mutableSetOf<Evaluable>()
    private val queueLock = Mutex()

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