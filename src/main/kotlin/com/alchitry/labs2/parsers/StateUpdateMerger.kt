package com.alchitry.labs2.parsers

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * This version of MutableSharedFlow is designed to wait for all collectors to finish processing the value before
 * returning from emit()
 */
class SynchronizedSharedFlow<T:Any> {
    private val mutableFlow = MutableSharedFlow<T?>()

    fun asFlow(): Flow<T> = mutableFlow.filterNotNull().distinctUntilChanged()

    suspend fun collect(collector: FlowCollector<T>) = asFlow().collect(collector)

    suspend fun emit(value: T) {
        mutableFlow.emit(value)
        mutableFlow.emit(null)
    }
}

/**
 * This takes multiple StateFlows and merges their updates into one call.
 * @param dependencies a list of StateFlows to merge
 * @param onChange a callback to trigger on any update to dependencies. This is guaranteed to not be called concurrently.
 */
suspend fun onAnyChange(dependencies: List<Flow<*>>, onChange: suspend () -> Unit) =
    coroutineScope {
        val changeFlow = SynchronizedSharedFlow<Int>()
        val ct = AtomicInteger(0)
        dependencies.forEach {
            launch(start = CoroutineStart.UNDISPATCHED) {
                it.collect {
                    changeFlow.emit(ct.getAndIncrement())
                }
            }
        }
        changeFlow.collect {
            onChange()
        }
    }

suspend fun onAnyChange(vararg dependencies: Flow<*>, onChange: suspend () -> Unit) =
    onAnyChange(dependencies.toList(), onChange)