package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent

class GlobalNamespace(
    override val name: String,
    val constants: Map<String, Constant>,
    val structs: Map<String, StructType>,
    enumList: List<EnumType>
) : SignalParent {
    override val parent: SignalParent? = null

    // convert values to read-only signals
    private val constSignals: Map<String, Signal> =
        constants.mapValues { it.value.value.asSignal(it.key, this) }

    val enums = enumList.associate { it.name to it.copy(parent = this) }

    override fun getSignal(name: String): SignalOrParent? = constSignals[name] ?: enums[name]
}