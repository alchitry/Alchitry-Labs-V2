package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector
import com.alchitry.labs.parsers.lucidv2.signals.StructType

data class StructValue(
    val type: StructType,
    private val valueMap: MutableMap<String, Value> = mutableMapOf()
) : Value(), Map<String, Value> by valueMap {
    init {
        valueMap.keys.removeIf { !type.contains(it) }
        type.keys.forEach {
            valueMap.putIfAbsent(it, UndefinedValue(constant))
        }
    }

    override val constant: Boolean
        get() = valueMap.values.all { it.constant }

    fun isComplete(): Boolean {
        return type.keys.all { this[it] !is UndefinedValue }
    }

    fun select(selection: SignalSelector.Struct): Value {
        return valueMap[selection.member] ?: throw SignalSelectionException(selection, "Member $selection is not part of the struct $type")
    }
}