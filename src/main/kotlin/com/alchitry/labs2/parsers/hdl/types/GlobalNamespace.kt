package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.hdl.ExprType

class GlobalNamespace(
    override val name: String,
    constants: Map<String, Constant>,
    val structs: Map<String, StructType>,
    enumList: List<EnumType>
) : SignalParent {
    override val parent: SignalParent? = null

    // convert values to read-only signals
    val constants: Map<String, Signal> =
        constants.mapValues { it.value.value.asSignal(it.key, ExprType.Constant, this) }

    val enums = enumList.associate { it.name to it.copy(parent = this) }

    override fun getSignal(name: String): SignalOrParent? = constants[name] ?: enums[name]
}