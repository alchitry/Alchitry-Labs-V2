package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.BitUtil
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.types.StructType

sealed class SignalWidth {
    /**
     *  Returns true if this is JUST an array (no structs)
     */
    fun isSimpleArray(): Boolean {
        return when (this) {
            is ArrayWidth -> next.isSimpleArray()
            is SimpleWidth, BitWidth -> true
            is UndefinedWidth -> false
            is StructWidth -> false
        }
    }

    /**
     *  Returns true if this is JUST an array (no structs) and defined
     */
    fun isDefinedSimpleArray(): Boolean {
        return (this is SimpleWidth || this is BitWidth || (this is ArrayWidth && next.isDefinedSimpleArray()))
    }

    /**
     * Returns true if this is an array. It may be an array of anything, including structs.
     */
    fun isArray(): Boolean {
        return this is ArrayWidth || this is SimpleWidth
    }

    /**
     * Returns true if this is a defined array. It may be an array of anything, including structs.
     */
    fun isDefinedArray(): Boolean {
        return this is ArrayWidth || this is SimpleWidth
    }

    /**
     * Returns true if the width is well-defined.
     */
    fun isDefined(): Boolean {
        return when (this) {
            is SimpleWidth, BitWidth -> true
            is ArrayWidth -> next.isDefined()
            is StructWidth -> type.values.all { it.width.isDefined() }
            is UndefinedWidth -> false
        }
    }

    /**
     * Returns a Value whose elements match the dimensions of this width.
     */
    fun toValue(): Value {
        require(isDefinedSimpleArray()) { "toValue() can only be called on arrays" }
        val dims = mutableListOf<Int>()
        var array: SignalWidth = this
        while (true) {
            when (array) {
                is ArrayWidth -> {
                    dims.add(array.size)
                    array = array.next
                }

                is BitListWidth -> {
                    dims.add(array.size)
                    break
                }

                is BitWidth -> {
                    check(dims.isEmpty()) { "Found a BitWidth inside an array!" }
                    dims.add(1)
                    break
                }

                is StructWidth -> error("toValue() doesn't work on structs!")
                is UndefinedWidth -> {
                    dims.add(-1)
                    break
                }
            }
        }

        check(dims.isNotEmpty()) { "Failed to get any dimensions for this width!" }

        if (dims.size == 1) {
            return BitListValue(dims.first(), constant = true, signed = false)
        }

        val width = BitUtil.minWidthNum(dims.max())

        return ArrayValue(dims.map { BitListValue(it, width, constant = true, signed = false) })
    }


    val bitCount: Int?
        get() {
            return when (this) {
                is BitWidth -> 1
                is ArrayWidth -> size * (next.bitCount ?: return null)
                is StructWidth -> type.values.sumOf { it.width.bitCount ?: return null }
                is UndefinedWidth -> null
                is SimpleWidth -> size
            }
        }

    /** Returns true if the other width can be scaled to match this width. */
    fun canAssign(other: SignalWidth): Boolean =
        when (this) {
            is ArrayWidth, is StructWidth -> this == other
            BitWidth, is SimpleWidth -> other is BitWidth || other is SimpleWidth
            is UndefinedWidth -> false
        }

    /** Returns true if bits of other will be dropped during an assignment. */
    fun willTruncate(other: SignalWidth): Boolean {
        return when (this) {
            is ArrayWidth, is StructWidth -> false
            BitWidth, is SimpleWidth, is UndefinedWidth ->
                (bitCount ?: return false) < (other.bitCount ?: return false)
        }
    }

    /**
     * Returns a value of this width filled with bit.
     * @param bit is the value to fill the Value with
     * @param constant if the resulting Value should be marked as constant
     * @param signed if the resulting Value should be signed. This will be overwritten by structs that have explicit signs.
     */
    abstract fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): Value
}

sealed class SimpleWidth : SignalWidth() {
    abstract val size: Int

    override fun equals(other: Any?): Boolean {
        if (other is SimpleWidth)
            return size == other.size

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return size
    }
}

data object BitWidth : SimpleWidth() {
    override val size = 1
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): BitValue =
        BitValue(bit, constant, signed)
}

data class BitListWidth(
    override val size: Int
) : SimpleWidth() {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): BitListValue =
        BitListValue(size, constant, signed) { bit }
}

data class ArrayWidth(
    val size: Int,
    val next: SignalWidth
) : SignalWidth() {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): ArrayValue =
        ArrayValue(List(size) { next.filledWith(bit, constant, signed) })
}

data class StructWidth(
    val type: StructType
) : SignalWidth() {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): StructValue =
        StructValue(type, type.mapValues { it.value.width.filledWith(bit, constant, it.value.signed) })
}

open class UndefinedWidth : SignalWidth() {
    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun toString(): String {
        return "UndefinedWidth"
    }

    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): UndefinedValue =
        UndefinedValue(constant, this)

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class ResolvableWidth(val context: LucidParser.SignalWidthContext) : UndefinedWidth() {
    override fun toString(): String {
        return "ResolvableWidth(${context.text})"
    }

    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): UndefinedValue =
        UndefinedValue(constant, this)
}