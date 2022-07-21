package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.StructType

sealed class SignalWidth {
    /**
     * Returns true if this is a 1D array
     */
    fun isFlatArray(): Boolean {
        return this is SimpleWidth || this is UndefinedSimpleWidth
    }

    /**
     *  Returns true if this is JUST an array (no structs)
     */
    fun isSimpleArray(): Boolean {
        return when (this) {
            is ArrayWidth -> next.isSimpleArray()
            is SimpleWidth -> true
            is UndefinedSimpleWidth -> true
            is StructWidth -> false
        }
    }

    /**
     *  Returns true if this is JUST an array (no structs) and defined
     */
    fun isDefinedSimpleArray(): Boolean {
        return (this is SimpleWidth || (this is ArrayWidth && next.isDefinedSimpleArray()))
    }

    /**
     * Returns true if this is an array. It may be an array of anything, including structs.
     */
    fun isArray(): Boolean {
        return this is ArrayWidth || this is UndefinedSimpleWidth || this is SimpleWidth
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
            is SimpleWidth -> true
            is ArrayWidth -> next.isDefined()
            is StructWidth -> type.values.all { it.width.isDefined() }
            is UndefinedSimpleWidth -> false
        }
    }

    fun getDimensions(): List<Int> {
        require(isDefinedSimpleArray()) { "getDimensions can only be called on arrays" }
        val dims = mutableListOf<Int>()
        var array: SignalWidth = this
        while (true) {
            if (array is ArrayWidth) {
                dims.add(array.size)
                array = array.next
            } else if (array is SimpleWidth) {
                dims.add(array.size)
                break
            }
        }
        return dims
    }

    fun getBitCount(): Int {
        return when (this) {
            is ArrayWidth -> size * next.getBitCount()
            is StructWidth -> type.values.sumOf { it.width.getBitCount() }
            UndefinedSimpleWidth -> error("getBitCount() can't be used when width isn't well defined")
            is SimpleWidth -> size
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SignalWidth)
            return false

        return when (other) {
            is ArrayWidth -> this is ArrayWidth && other.size == this.size && other.next == this.next
            is StructWidth -> this is StructWidth && other.type == this.type
            is SimpleWidth -> this is SimpleWidth && other.size == this.size
            is UndefinedSimpleWidth -> false
        }
    }

    fun shallowEquals(other: SignalWidth): Boolean {
        return when (other) {
            is ArrayWidth -> this is ArrayWidth && other.size == this.size
            else -> other == this
        }
    }
}

data class SimpleWidth(
    val size: Int
) : SignalWidth()

data class ArrayWidth(
    val size: Int,
    val next: SignalWidth
) : SignalWidth()

data class StructWidth(
    val type: StructType
) : SignalWidth()

object UndefinedSimpleWidth : SignalWidth()