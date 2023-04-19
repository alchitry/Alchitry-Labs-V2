package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelection
import com.alchitry.labs.parsers.lucidv2.values.Value

enum class SignalDirection {
    Read,
    Write,
    Both;

    val canRead: Boolean get() = this != Write
    val canWrite: Boolean get() = this != Read
}

class Signal(
    val name: String, // includes namespace or module name
    val direction: SignalDirection,
    initialValue: Value
): SignalOrParent {
    fun select(selection: SignalSelection) = SubSignal(this, selection)

    var value: Value = initialValue
        set(v) {
            // TODO: Resize v if possible before complaining
            check(field.signalWidth == v.signalWidth) { "Signal assigned value does not match its size!" }
            field = v
        }
}

data class SubSignal(
    val signal: Signal,
    val selection: SignalSelection
)

sealed interface SignalOrParent
sealed interface SignalParent: SignalOrParent {
    fun getSignal(name: String): Signal?
}