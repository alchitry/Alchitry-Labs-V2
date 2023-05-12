package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.Value

data class GlobalNamespace(
    override val name: String,
    val constants: Map<String, Value>,
    val structs: Map<String, StructType>
) : SignalParent {
    override val parent: SignalParent? = null

    // convert values to read-only signals
    private val constSignals: Map<String, Signal> =
        constants.mapValues { Signal(it.key, SignalDirection.Read, this, it.value) }

    override fun getSignal(name: String): Signal? = constSignals[name]
}