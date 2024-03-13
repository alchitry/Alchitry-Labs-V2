package com.alchitry.labs2.parsers.lucidv2.values

import com.alchitry.labs2.parsers.BitUtil
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidExprEval
import com.alchitry.labs2.parsers.lucidv2.types.StructType
import kotlinx.coroutines.runBlocking
import kotlin.contracts.contract

sealed class SignalWidth {
    /**
     *  Returns true if this is JUST an array (no structs).
     *  This differs from [isArrayOrSimple] in that it recursively checks arrays.
     */
    fun isSimpleArray(): Boolean {
        contract {
            returns(true) implies (this@SignalWidth is SimpleWidth || this@SignalWidth is ArrayWidth)
            returns(false) implies (this@SignalWidth is StructWidth)
        }
        return when (this) {
            is ArrayWidth -> next.isSimpleArray()
            is SimpleWidth -> true
            is StructWidth -> false
        }
    }

    /**
     * Returns true if this is an array. It may be an array of anything, including structs.
     */
    fun isArrayOrSimple(): Boolean {
        contract {
            returns(true) implies (this@SignalWidth is SimpleWidth || this@SignalWidth is ArrayWidth)
            returns(false) implies (this@SignalWidth is StructWidth)
        }
        return when (this) {
            is ArrayWidth, is SimpleWidth -> true
            is StructWidth -> false
        }
    }

    /**
     * Returns true if this is a simple width. It may be undefined.
     */
    fun isSimple(): Boolean {
        contract {
            returns(true) implies (this@SignalWidth is SimpleWidth)
            returns(false) implies (this@SignalWidth is StructWidth || this@SignalWidth is ArrayWidth)
        }
        return when (this) {
            is SimpleWidth -> true
            is StructWidth, is ArrayWidth -> false
        }
    }

    /**
     * Returns true if the width is well-defined.
     */
    fun isDefined(): Boolean {
        contract {
            returns(true) implies (this@SignalWidth !is UndefinedWidth)
        }
        return when (this) {
            is DefinedSimpleWidth -> true
            is DefinedArrayWidth -> next.isDefined()
            is StructWidth -> type.values.all { it.width.isDefined() }
            is UndefinedWidth -> false
        }
    }

    /**
     * This checks if two widths should be considered equal in size.
     * It takes into account [UndefinedWidth] and assumes these are always
     * compatible with their corresponding defined types.
     *
     * @param other The SignalWidth object to compare compatibility with.
     * @return true if the current SignalWidth is compatible with the given SignalWidth, false otherwise.
     */
    fun isCompatibleWith(other: SignalWidth): Boolean {
        return when (this) {
            is DefinedArrayWidth -> when (other) {
                is UndefinedArrayWidth -> next.isCompatibleWith(other.next)
                is DefinedArrayWidth -> size == other.size && next.isCompatibleWith(other.next)
                else -> false
            }

            is DefinedSimpleWidth -> when (other) {
                is DefinedSimpleWidth -> this.size == other.size
                is UndefinedSimpleWidth -> true
                else -> false
            }

            is StructWidth -> this == other
            is UndefinedArrayWidth -> when (other) {
                is ArrayWidth -> next.isCompatibleWith(other.next)
                else -> false
            }

            is UndefinedSimpleWidth -> other is SimpleWidth
        }
    }

    /**
     * Returns true if the width is well-defined or resolvable.
     */
    fun isResolvable(): Boolean {
        return when (this) {
            is ResolvableSimpleWidth -> true
            is ResolvableArrayWidth -> next.isResolvable()
            is UndefinedWidth -> false
            is DefinedSimpleWidth -> true
            is StructWidth -> isDefined() // structs should be defined or never resolved
            is DefinedArrayWidth -> next.isResolvable()
        }
    }

    fun resolve(exprEval: LucidExprEval): SignalWidth {
        return when (this) {
            is DefinedSimpleWidth -> this
            is ResolvableWidth -> {
                runBlocking { exprEval.walk(context, ignoreSkip = true) }
                val value = exprEval.resolve(context) ?: error("Failed to resolve width: $this!")
                val size = (value as? SimpleValue)?.toBigInt()?.intValueExact()
                if (size != null)
                    when (this) {
                        is ResolvableArrayWidth -> DefinedArrayWidth(size, next)
                        is ResolvableSimpleWidth -> BitListWidth(size)
                    }
                else
                    when (this) {
                        is ResolvableArrayWidth -> UndefinedArrayWidth(next)
                        is ResolvableSimpleWidth -> UndefinedSimpleWidth()
                    }
            }

            is DefinedArrayWidth -> DefinedArrayWidth(size, next.resolve(exprEval))
            is StructWidth -> StructWidth(type.copy(members = type.mapValuesTo(LinkedHashMap()) {
                it.value.copy(
                    width = it.value.width.resolve(
                        exprEval
                    )
                )
            }))

            is UndefinedWidth -> error("UndefinedWidth can't be resolved!")
        }
    }

    /**
     * Returns a Value whose elements match the dimensions of this width.
     */
    fun toValue(): Value {
        require(isSimpleArray()) { "toValue() can only be called on arrays" }
        val dims = mutableListOf<Int?>()
        var array: SignalWidth = this
        while (true) {
            when (array) {
                is DefinedArrayWidth -> {
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
                is UndefinedSimpleWidth -> {
                    dims.add(null)
                    break
                }

                is UndefinedArrayWidth -> {
                    dims.add(null)
                    array = array.next
                }
            }
        }

        check(dims.isNotEmpty()) { "Failed to get any dimensions for this width!" }

        if (dims.size == 1) {
            val d = dims.first() ?: return UndefinedValue(true)
            return BitListValue(d, constant = true, signed = false)
        }

        val width = BitUtil.minWidthNum(dims.maxOf { it ?: -1 }.coerceAtLeast(1))

        return ArrayValue(dims.map {
            if (it == null) UndefinedValue(true) else
                BitListValue(it, width, constant = true, signed = false)
        })
    }


    val bitCount: Int?
        get() {
            return when (this) {
                is BitWidth -> 1
                is DefinedArrayWidth -> size * (next.bitCount ?: return null)
                is StructWidth -> type.values.sumOf { it.width.bitCount ?: return null }
                is UndefinedSimpleWidth, is UndefinedArrayWidth -> null
                is DefinedSimpleWidth -> size
            }
        }

    /** Returns true if the other width can be scaled to match this width. */
    fun canAssign(other: SignalWidth): Boolean =
        when {
            this.isSimple() -> other.isSimple()
            else -> this.isCompatibleWith(other)
        }

    /** Returns true if bits of other will be dropped during an assignment. */
    fun willTruncate(other: SignalWidth): Boolean {
        return when (this) {
            is DefinedArrayWidth, is StructWidth, is UndefinedArrayWidth -> false
            BitWidth, is DefinedSimpleWidth, is UndefinedSimpleWidth ->
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


sealed interface SimpleWidth
/**
 * Creates a [DefinedSimpleWidth] with the given number of bits.
 * If bits == 1, then it returns [BitWidth].
 * Otherwise, it returns a [BitListWidth].
 */
fun DefinedSimpleWidth(bits: Int): DefinedSimpleWidth {
    if (bits == 1) {
        return BitWidth
    }
    return BitListWidth(bits)
}

sealed class DefinedSimpleWidth : SignalWidth(), SimpleWidth {
    abstract val size: Int

    override fun equals(other: Any?): Boolean {
        return other is DefinedSimpleWidth && size == other.size
    }

    override fun hashCode(): Int {
        return size.hashCode()
    }
}

data object BitWidth : DefinedSimpleWidth() {
    override val size = 1
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): BitValue =
        BitValue(bit, constant, signed)
}

data class BitListWidth(
    override val size: Int
) : DefinedSimpleWidth() {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): BitListValue =
        BitListValue(size, constant, signed) { bit }
}

sealed interface ArrayWidth {
    val next: SignalWidth
}

data class DefinedArrayWidth(
    val size: Int,
    override val next: SignalWidth
) : SignalWidth(), ArrayWidth {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): ArrayValue =
        ArrayValue(List(size) { next.filledWith(bit, constant, signed) })
}

data class StructWidth(
    val type: StructType
) : SignalWidth() {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): StructValue =
        StructValue(type, type.mapValues { it.value.width.filledWith(bit, constant, it.value.signed) })
}


sealed class UndefinedWidth : SignalWidth()
/**
 * An undefined 1D width.
 */
open class UndefinedSimpleWidth : UndefinedWidth(), SimpleWidth {
    override fun equals(other: Any?): Boolean {
        return other is UndefinedSimpleWidth
    }

    override fun toString(): String {
        return "UndefinedSimpleWidth"
    }

    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean): UndefinedValue =
        UndefinedValue(constant, this)

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

open class UndefinedArrayWidth(override val next: SignalWidth) : UndefinedWidth(), ArrayWidth {
    override fun filledWith(bit: Bit, constant: Boolean, signed: Boolean) =
        UndefinedValue(constant, this)

    override fun equals(other: Any?): Boolean {
        return other is UndefinedArrayWidth && next == this.next
    }

    override fun hashCode(): Int {
        return next.hashCode()
    }

    override fun toString(): String {
        return "UndefinedArrayWidth(next=$next)"
    }
}

sealed interface ResolvableWidth {
    val context: LucidParser.ExprContext
}

class ResolvableSimpleWidth(override val context: LucidParser.ExprContext) : UndefinedSimpleWidth(), ResolvableWidth {
    override fun toString(): String {
        return "ResolvableSimpleWidth(context=${context.text})"
    }
}

class ResolvableArrayWidth(override val context: LucidParser.ExprContext, next: SignalWidth) :
    UndefinedArrayWidth(next), ResolvableWidth {
    override fun toString(): String {
        return "ResolvableArrayWidth(context=${context.text}, next=${next})"
    }
}

/**
 * Returns the first well-defined [SignalWidth] or the first best defined [SignalWidth]
 * if none are well-defined.
 */
fun Collection<SignalWidth>.firstMostDefined() =
    firstOrNull { it.isDefined() }
        ?: firstOrNull { it !is UndefinedWidth }
        ?: first()