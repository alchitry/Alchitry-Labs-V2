package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.signals.SignalSelection
import com.alchitry.labs.parsers.lucidv2.values.Value

enum class SignalDirection {
    Read,
    Write,
    Both;

    val canRead: Boolean get() = this != Write
    val canWrite: Boolean get() = this != Read
}

data class Signal(
    val name: String, // includes namespace or module name
    val direction: SignalDirection,
    val value: Value
) {
    fun select(selection: SignalSelection) = SubSignal(this, selection)
}

data class SubSignal(
    val signal: Signal,
    val selection: SignalSelection
)