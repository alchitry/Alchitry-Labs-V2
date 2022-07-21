package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.ArrayWidth
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.SimpleWidth
import com.alchitry.labs.parsers.lucidv2.values.StructWidth

sealed class SignalType {
    abstract val width: SignalWidth
}

data class SimpleType(
    override val width: SimpleWidth
) : SignalType()

data class ArrayType(
    val size: Int,
    val type: SignalType
) : SignalType() {
    override val width: SignalWidth = ArrayWidth(size, type.width)
}

data class StructType(
    val fullName: String, // includes namespace
    private val members: MutableMap<String, SignalType> = mutableMapOf()
) : SignalType(), MutableMap<String, SignalType> by members {
    override val width: SignalWidth = StructWidth(this)
    val name: String = fullName.split(".").last()
}