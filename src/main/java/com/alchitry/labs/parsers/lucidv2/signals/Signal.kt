package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate

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
): SignalOrParent {
    fun select(selection: SignalSelection) = SubSignal(this, selection)

    private val mutableValueFlow = MutableStateFlow(initialValue)
    val valueFlow = mutableValueFlow.asStateFlow()

    var value
        get() = mutableValueFlow.value
        set(v) {
            mutableValueFlow.getAndUpdate {
                check(it.canAssign(v)) { "Signal assigned value does not match its size!" }
                v.resizeToMatch(it.signalWidth)
            }
        }

    suspend fun collect(collector: FlowCollector<Value>): Nothing = mutableValueFlow.collect(collector)
}

class SignalSelectionException(val selector: SignalSelector, message: String): Exception(message)

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
sealed interface SignalParent: SignalOrParent {
    fun getSignal(name: String): Signal?
}