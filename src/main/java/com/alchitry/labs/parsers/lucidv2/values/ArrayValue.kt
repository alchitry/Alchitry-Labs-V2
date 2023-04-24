package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

data class ArrayValue(
    val elements: List<Value>
) : Value(), List<Value> by elements {
    override val constant: Boolean
        get() = all { it.constant }

    fun select(selection: SignalSelector.Bits): Value {
        try {
            val newElements = elements.subList(selection.range.first, selection.range.last + 1)
            if (newElements.size == 1)
                return newElements.first()
            return copy(elements = newElements)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(selection, "Selection $selection is outside the bounds of [${elements.size-1}:0]")
        }
    }
}