package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

enum class SignalDirection {
    Read,
    Write,
    Both;

    val canRead: Boolean get() = this != Write
    val canWrite: Boolean get() = this != Read
}

class Signal(
    override val name: String, // includes namespace or module name
    val direction: SignalDirection,
    val parent: SignalParent?,
    initialValue: Value
) : SignalOrParent {
    fun select(selection: SignalSelection) = SubSignal(this, selection)

    private val mutableValueFlow = SynchronizedSharedFlow<Value>()
    val valueFlow: Flow<Value> get() = mutableValueFlow.asFlow()

    var value: Value = initialValue
        private set

    private val updateLock = Mutex()

    suspend fun set(newValue: Value) {
        updateLock.withLock {
            check(value.canAssign(newValue)) { "Signal assigned value does not match its size!" }
            val resizedValue = newValue.resizeToMatch(value.signalWidth)
            value = resizedValue
            mutableValueFlow.emit(resizedValue)
        }
    }
}

class SignalSelectionException(val selector: SignalSelector, message: String) : Exception(message)

data class SubSignal(
    val signal: Signal,
    val selection: SignalSelection
) {
    var value: Value
        get() {
            var v = signal.value
            selection.forEach {
                v = v.select(it)
            }
            return v
        }
        set(value) = TODO()
}

sealed interface SignalOrParent {
    val name: String
}

sealed interface SignalParent : SignalOrParent {
    fun getSignal(name: String): Signal?
}