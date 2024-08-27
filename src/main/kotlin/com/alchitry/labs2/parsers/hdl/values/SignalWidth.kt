package com.alchitry.labs2.parsers.hdl.values

import com.alchitry.labs2.parsers.BitUtil
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprEval
import com.alchitry.labs2.parsers.hdl.types.StructType
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprEval
import kotlinx.coroutines.runBlocking
import org.antlr.v4.kotlinruntime.ParserRuleContext
import java.math.BigInteger
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
            is ResolvableSimpleWidth<*> -> true
            is ResolvableArrayWidth<*> -> next.isResolvable()
            is UndefinedWidth -> false
            is DefinedSimpleWidth -> true
            is StructWidth -> isDefined() // structs should be defined or never resolved
            is DefinedArrayWidth -> next.isResolvable()
        }
    }

    /**
     * Returns true if the SignalWidth is compatible with a string.
     */
    fun isStringLike(): Boolean {
        return when (this) {
            is ArrayWidth -> (this.next as? DefinedSimpleWidth)?.size == 8
            else -> false
        }
    }

    fun resolve(lucidEval: LucidExprEval?, verilogEval: VerilogExprEval?): SignalWidth {
        return when (this) {
            is DefinedSimpleWidth -> this
            is ResolvableWidth<*> -> {
                val value = when (val context = context) {
                    is LucidParser.ExprContext -> {
                        requireNotNull(lucidEval) { "Lucid expression found but Lucid evaluator not provided!" }
                        runBlocking { lucidEval.walk(context, ignoreSkip = true) }
                        lucidEval.resolve(context)?.value
                            ?: error("Failed to resolve width: $this!")
                    }

                    is VerilogParser.Constant_expressionContext -> {
                        requireNotNull(verilogEval) { "Verilog expression found but Verilog evaluator not provided!" }
                        verilogEval.walk(context, ignoreSkip = true)
                        verilogEval.resolve(context)?.value
                            ?: error("Failed to resolve width: $this!")
                    }

                    is VerilogParser.Range_Context -> {
                        requireNotNull(verilogEval) { "Verilog expression found but Verilog evaluator not provided!" }
                        verilogEval.walk(context, ignoreSkip = true)
                        val msb = context.msb_constant_expression()?.constant_expression()
                            ?: error("Missing MSB expression context!")
                        val lsb = context.lsb_constant_expression()?.constant_expression()
                            ?: error("Missing LSB expression context!")
                        val msbValue =
                            verilogEval.resolve(msb)?.value ?: error("Failed to resolve MSB of width: $this!")
                        val lsbValue =
                            verilogEval.resolve(lsb)?.value ?: error("Failed to resolve LSB of width: $this!")
                        if (msbValue !is SimpleValue || lsbValue !is SimpleValue || !msbValue.isNumber() || !lsbValue.isNumber())
                            return UndefinedSimpleWidth()
                        BitListValue(
                            bigInt = msbValue.toBigInt()!!.subtract(lsbValue.toBigInt()!!).plus(BigInteger.ONE)
                        )
                    }

                    else -> error("Unsupported context: ${context.javaClass.canonicalName}")
                }
                val size = (value as? SimpleValue)?.toBigInt()?.intValueExact()
                if (size != null)
                    when (this) {
                        is ResolvableArrayWidth<*> -> DefinedArrayWidth(size, next.resolve(lucidEval, verilogEval))
                        is ResolvableSimpleWidth<*> -> BitListWidth(size)
                    }
                else
                    when (this) {
                        is ResolvableArrayWidth<*> -> UndefinedArrayWidth(next.resolve(lucidEval, verilogEval))
                        is ResolvableSimpleWidth<*> -> UndefinedSimpleWidth()
                    }
            }

            is DefinedArrayWidth -> DefinedArrayWidth(size, next.resolve(lucidEval, verilogEval))
            is StructWidth -> StructWidth(type.copy(members = type.mapValuesTo(LinkedHashMap()) {
                it.value.copy(width = it.value.width.resolve(lucidEval, verilogEval))
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
            val d = dims.first() ?: return UndefinedValue()
            return BitListValue(d, signed = false)
        }

        val width = BitUtil.minWidthNum(dims.maxOf { it ?: -1 }.coerceAtLeast(1))

        return ArrayValue(dims.map {
            if (it == null) UndefinedValue() else
                BitListValue(it, width, signed = false)
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
     * @param signed if the resulting Value should be signed. Structs with explicit signs will override this.
     */
    abstract fun filledWith(bit: Bit, signed: Boolean): Value
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
    override fun filledWith(bit: Bit, signed: Boolean): BitValue =
        BitValue(bit, signed)
}

data class BitListWidth(
    override val size: Int
) : DefinedSimpleWidth() {
    override fun filledWith(bit: Bit, signed: Boolean): BitListValue =
        BitListValue(size, signed) { bit }
}

sealed interface ArrayWidth {
    val next: SignalWidth
}

data class DefinedArrayWidth(
    val size: Int,
    override val next: SignalWidth
) : SignalWidth(), ArrayWidth {
    override fun filledWith(bit: Bit, signed: Boolean): ArrayValue =
        ArrayValue(List(size) { next.filledWith(bit, signed) })
}

data class StructWidth(
    val type: StructType
) : SignalWidth() {
    override fun filledWith(bit: Bit, signed: Boolean): StructValue =
        StructValue(type, type.mapValues { it.value.width.filledWith(bit, it.value.signed) })
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

    override fun filledWith(bit: Bit, signed: Boolean): UndefinedValue =
        UndefinedValue(this)

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

open class UndefinedArrayWidth(override val next: SignalWidth) : UndefinedWidth(), ArrayWidth {
    override fun filledWith(bit: Bit, signed: Boolean) =
        UndefinedValue(this)

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

sealed interface ResolvableWidth<T : ParserRuleContext> {
    val context: T
}

class ResolvableSimpleWidth<T : ParserRuleContext>(override val context: T) : UndefinedSimpleWidth(),
    ResolvableWidth<T> {
    override fun toString(): String {
        return "ResolvableSimpleWidth(context=${context.text})"
    }
}

class ResolvableArrayWidth<T : ParserRuleContext>(override val context: T, next: SignalWidth) :
    UndefinedArrayWidth(next), ResolvableWidth<T> {
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