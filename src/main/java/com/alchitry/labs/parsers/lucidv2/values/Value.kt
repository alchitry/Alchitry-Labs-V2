package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector

sealed class Value {
    abstract val constant: Boolean
    abstract fun isNumber(): Boolean
    /** Makes a copy of this Value that has the constant flag set as false. */
    abstract fun asMutable(): Value
    abstract val signalWidth: SignalWidth
    abstract fun invert(): Value
    abstract fun isTrue(): BitValue
    abstract infix fun and(other: Value): Value
    abstract infix fun or(other: Value): Value
    abstract infix fun xor(other: Value): Value

    /** Resizes this Value to be the same width as the other value.
     *  It may truncate values if other is smaller than this value.
     */
    abstract fun resizeToMatch(newWidth: SignalWidth): Value
    abstract fun reverse(): Value

    infix fun isNotEqualTo(other: Value): BitValue {
        return xor(other).orReduce()
    }

    infix fun isEqualTo(other: Value): BitValue {
        return isNotEqualTo(other).invert()
    }

    private fun getBits(): List<Bit> {
        return when (this) {
            is ArrayValue -> elements.flatMap { it.getBits() }
            is SimpleValue -> bits
            is StructValue -> type.flatMap { (key, value) ->
                this[key]?.getBits() ?: List(value.width.getBitCount()) { Bit.Bx }
            }
            is UndefinedValue -> List(width.getBitCount()) { Bit.Bx }
        }
    }

    fun andReduce(): BitValue {
        return getBits().reduce { a, b -> a and b }.toBitValue(constant)
    }

    fun orReduce(): BitValue {
        return getBits().reduce { a, b -> a or b }.toBitValue(constant)
    }

    fun xorReduce(): BitValue {
        return getBits().reduce { a, b -> a xor b }.toBitValue(constant)
    }

    fun flatten(): BitListValue = BitListValue(getBits(), constant, false)

    /** Returns true if the other value can be scaled to match this value. */
    fun canAssign(other: Value): Boolean = signalWidth.canAssign(other.signalWidth)

    fun select(selection: SignalSelector): Value =
        when (selection) {
            is SignalSelector.Bits -> when (this) {
                is ArrayValue -> selectElements(selection)
                is BitListValue -> selectBits(selection)
                is StructValue -> throw SignalSelectionException(selection, "Bit selection can't be used on a struct.")
                is UndefinedValue -> TODO()
                is BitValue -> throw SignalSelectionException(selection, "Bit selection can't be used on a single bit.")
            }

            is SignalSelector.Struct -> when (this) {
                is ArrayValue, is BitListValue -> throw SignalSelectionException(
                    selection,
                    "Arrays don't have struct members."
                )

                is StructValue -> selectMember(selection)
                is UndefinedValue -> TODO()
                is BitValue -> throw SignalSelectionException(selection, "Bits don't have struct members.")
            }
        }
}






