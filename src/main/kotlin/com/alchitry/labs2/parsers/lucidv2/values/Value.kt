package com.alchitry.labs2.parsers.lucidv2.values

import com.alchitry.labs2.parsers.lucidv2.types.*

sealed class ValueFormat {
    data object Binary : ValueFormat()
    data object Hex : ValueFormat()
    data object Decimal : ValueFormat()
    data class Fractional(val fractionalBits: Int) : ValueFormat()
}


sealed class Value : Measurable {
    abstract val constant: Boolean
    abstract fun isNumber(): Boolean

    /** Makes a copy of this Value that has the constant flag set as false. */
    abstract fun asMutable(): Value
    abstract fun withSign(signed: Boolean): Value
    abstract fun invert(): Value
    abstract fun isTrue(): BitValue
    abstract infix fun and(other: Value): Value
    abstract infix fun or(other: Value): Value
    abstract infix fun xor(other: Value): Value

    abstract fun toString(format: ValueFormat): String

    /**
     * Returns a same sized Value with B1 where the bits match the given bit and B0 everywhere else.
     */
    abstract fun where(bit: Bit): Value

    /**
     * Replaces all bits in the mask where B1 with bit.
     */
    abstract fun replace(mask: Value, bit: Bit): Value

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
                this[key]?.getBits() ?: List(
                    value.width.bitCount ?: error("Member of a struct has an undefined width!")
                ) { Bit.Bx }
            }

            is UndefinedValue -> List(width.bitCount ?: 1) { Bit.Bx }
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
    fun canAssign(other: Value): Boolean = width.canAssign(other.width)

    fun select(selection: SignalSelection): Value {
        var v = this
        selection.forEach {
            v = v.select(it)
        }
        return v
    }

    fun select(selection: SignalSelector): Value =
        when (selection) {
            is SignalSelector.Bits -> when (this) {
                is ArrayValue -> selectElements(selection)
                is BitListValue -> selectBits(selection)
                is StructValue -> throw SignalSelectionException(selection, "Bit selection can't be used on a struct.")
                is UndefinedValue -> throw SignalSelectionException(
                    selection,
                    "Bit selection can't be used on an undefined value."
                )

                is BitValue -> throw SignalSelectionException(selection, "Bit selection can't be used on a single bit.")
            }

            is SignalSelector.Struct -> when (this) {
                is ArrayValue, is BitListValue -> throw SignalSelectionException(
                    selection,
                    "Arrays don't have struct members."
                )

                is StructValue -> selectMember(selection)
                is UndefinedValue -> throw SignalSelectionException(
                    selection,
                    "Struct selection can't be used on an undefined value."
                )

                is BitValue -> throw SignalSelectionException(selection, "Bits don't have struct members.")
            }
        }

    abstract fun write(selection: List<SignalSelector>, newValue: Value): Value

    fun asSignal(name: String, parent: SignalParent? = null) = Signal(
        name,
        SignalDirection.Read,
        parent,
        this
    ).also { it.hasDriver = true }
}






