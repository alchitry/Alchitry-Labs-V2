package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.Value

data class GlobalNamespace(
    override val name: String,
    val constants: Map<String, Value>,
    val structs: Map<String, StructType>
) : SignalParent {
    // convert values to read-only signals
    private val constSignals: Map<String, Signal> =
        constants.mapValues { Signal(it.key, SignalDirection.Read, this, it.value) }

    override fun getSignal(name: String): Signal? = constSignals[name]
}