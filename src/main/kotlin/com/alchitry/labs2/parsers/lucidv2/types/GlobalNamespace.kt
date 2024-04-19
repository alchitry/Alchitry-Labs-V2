package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.lucidv2.parsers.ExprType

class GlobalNamespace(
    override val name: String,
    val constants: Map<String, Constant>,
    val structs: Map<String, StructType>,
    enumList: List<EnumType>
) : SignalParent {
    override val parent: SignalParent? = null

    // convert values to read-only signals
    private val constSignals: Map<String, Signal> =
        constants.mapValues { it.value.value.asSignal(it.key, ExprType.Constant, this) }

    val enums = enumList.associate { it.name to it.copy(parent = this) }

    override fun getSignal(name: String): SignalOrParent? = constSignals[name] ?: enums[name]
}