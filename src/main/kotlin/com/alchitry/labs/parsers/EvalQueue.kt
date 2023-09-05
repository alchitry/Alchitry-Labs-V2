package com.alchitry.labs.parsers

import com.alchitry.labs.project.QueueExhaustionException
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.Closeable

class EvalQueue : Closeable {
    private val evaluationQueue = mutableSetOf<Evaluable>()
    private val queueLock = Mutex()
    val scope = CoroutineScope(Dispatchers.Default)

    override fun close() {
        scope.cancel("EvalQueue closed.")
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

    var initializing: Boolean = false
        private set

    suspend fun initialize() {
        initializing = true
        processQueue()
        initializing = false
    }
}