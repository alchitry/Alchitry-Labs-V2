package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.Util.widthOfMult
import com.alchitry.labs.parsers.lucidv2.resolvers.ExprResolver
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidResolver
import com.alchitry.labs.parsers.BigFunctions
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.errors.ErrorStrings
import com.alchitry.labs.parsers.errors.WarningStrings
import com.alchitry.labs.parsers.errors.dummyErrorListener
import com.alchitry.labs.parsers.lucidv2.*
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.values.*
import com.alchitry.labs.parsers.lucidv2.values.Function
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import org.apache.commons.text.StringEscapeUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.absoluteValue


class ExprParser(
    private val errorListener: ErrorListener = dummyErrorListener,
    private val resolver: LucidResolver
) : LucidBaseListener(), ExprResolver {
    private val values = mutableMapOf<ParseTree, Value>()

    init {
        resolver.expr = this
    }

    override fun resolve(ctx: ExprContext): Value? {
        return values[ctx]
    }

    private fun debug(ctx: ParserRuleContext) {
        errorListener.reportDebug(ctx, values[ctx].toString())
    }

    override fun exitSignal(ctx: SignalContext) {
        // TODO: Implement signals
    }

    override fun exitNumber(ctx: NumberContext) {
        val radix: Int
        val split: List<String>?
        when {
            ctx.HEX() != null -> {
                radix = 16
                split = ctx.HEX().text.split("h")
            }

            ctx.BIN() != null -> {
                radix = 2
                split = ctx.BIN().text.split("b")
            }

            ctx.DEC() != null -> {
                radix = 10
                split = ctx.DEC().text.split("d")
            }

            else -> {
                radix = 10
                split = null
            }
        }

        val valueString: String
        var width: MutableBitList? = null

        if (split != null) {
            valueString = split[1]
            if (split[0].isNotBlank()) {
                width = MutableBitList(split[0])
                if (!width.isNumber()) {
                    errorListener.reportError(ctx, ErrorStrings.NUM_WIDTH_NAN)
                    return
                }
            }
        } else {
            if (ctx.INT() != null) {
                valueString = ctx.INT().text
            } else {
                // String
                val str = StringEscapeUtils.unescapeJava(ctx.STRING().text)
                valueString = str.substring(1, str.length - 1)

                val value: Value
                when {
                    valueString.length > 1 -> {
                        val elements = mutableListOf<Value>()
                        repeat(valueString.length) {
                            elements.add(
                                SimpleValue(
                                    MutableBitList(
                                        valueString[it].code.toLong(),
                                        8
                                    ),
                                    constant = true
                                )
                            )
                        }
                        value = ArrayValue(elements)
                    }

                    valueString.length == 1 -> {
                        value = SimpleValue(MutableBitList(valueString[0].code.toLong(), 8), constant = true)
                    }

                    else -> {
                        value = SimpleValue(MutableBitList(), constant = true)
                        errorListener.reportError(ctx, ErrorStrings.STRING_CANNOT_BE_EMPTY)
                    }
                }
                values[ctx] = value
                return
            }
        }

        val unbound = MutableBitList(valueString, radix)
        val value = if (width != null) {
            SimpleValue(MutableBitList(valueString, radix, width.toBigInt().intValueExact()), constant = true)
        } else {
            SimpleValue(unbound, constant = true)
        }
        if (value.bits.size < unbound.size) {
            errorListener.reportWarning(ctx, String.format(ErrorStrings.VALUE_TOO_BIG, ctx.text, value.bits.size))
        }
        values[ctx] = value
    }

    override fun exitParamConstraint(ctx: ParamConstraintContext) {
        values[ctx.expr()]?.let { values[ctx] = it }
    }

    override fun exitStructConst(ctx: StructConstContext) {
        // TODO Struct constants
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        values[ctx.signal()]?.let { values[ctx] = it }
    }

    override fun exitExprStruct(ctx: ExprStructContext) {
        values[ctx.structConst()]?.let { values[ctx] = it }
    }

    override fun exitExprFunction(ctx: ExprFunctionContext) {
        values[ctx.function()]?.let { values[ctx] = it }
    }

    override fun exitExprNum(ctx: ExprNumContext) {
        values[ctx.number()]?.let { values[ctx] = it }
    }

    override fun exitExprGroup(ctx: ExprGroupContext) {
        if (ctx.expr() == null)
            return
        values[ctx.expr()]?.let { values[ctx] = it }
    }

    // always returns an unsigned value
    override fun exitExprConcat(ctx: ExprConcatContext) {
        if (ctx.expr().isEmpty())
            return

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        ctx.expr().forEach {
            val v = values[it] ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val base = operands[0].first
        val baseSigWidth = base.signalWidth
        var error = false

        if (baseSigWidth is StructWidth) {
            errorListener.reportError(operands[0].second, ErrorStrings.ARRAY_CONCAT_STRUCT)
            return
        }

        // if width is array, value is array or simple
        assert(baseSigWidth.isArray())
        when (base) {
            is ArrayValue -> {
                assert(baseSigWidth is ArrayWidth) { "The ArrayValue has a width that isn't an ArrayWidth" }
                operands.forEach {
                    val sigWidth = it.first.signalWidth
                    if (sigWidth !is ArrayWidth || sigWidth.next != (baseSigWidth as ArrayWidth).next) {
                        errorListener.reportError(it.second, ErrorStrings.ARRAY_CONCAT_DIM_MISMATCH)
                        error = true
                    }
                }
                if (error) return

                val valueList = mutableListOf<Value>()
                operands.asReversed().forEach { valueList.addAll((it.first as ArrayValue).elements) }
                values[ctx] = ArrayValue(valueList)
            }

            is SimpleValue, is UndefinedValue -> {
                var bitCount = 0
                var definedWidth = true

                operands.forEach {
                    val sigWidth = it.first.signalWidth
                    if (!sigWidth.isFlatArray()) {
                        errorListener.reportError(it.second, ErrorStrings.ARRAY_CONCAT_DIM_MISMATCH)
                        error = true
                    }
                    if (sigWidth is SimpleWidth)
                        bitCount += sigWidth.size
                    else // UndefinedWidth
                        definedWidth = false
                }

                if (error) return

                if (operands.any { it.first is UndefinedValue }) {
                    values[ctx] = if (definedWidth)
                        UndefinedValue(ctx.text, constant, SimpleWidth(bitCount))
                    else
                        UndefinedValue(ctx.text, constant)

                    return
                }

                val bits = MutableBitList()
                operands.asReversed().forEach { bits.addAll((it.first as SimpleValue).bits) }
                values[ctx] = SimpleValue(bits, constant)
            }

            else -> {
                error("Value with array width isn't an array or simple value")
            }
        }
    }

    // always returns an unsigned value
    override fun exitExprDup(ctx: ExprDupContext) {
        if (ctx.expr().size != 2)
            return

        val constant = values[ctx.expr(1)]?.constant == true

        val dupCount = values[ctx.expr(0)] ?: return
        val dupValue = values[ctx.expr(1)] ?: return

        if (values[ctx.expr(0)]?.constant != true) {
            errorListener.reportError(ctx.expr(0), ErrorStrings.EXPR_NOT_CONSTANT.format(ctx.expr(0).text))
            return
        }

        val valWidth = dupValue.signalWidth

        if (!valWidth.isArray()) {
            errorListener.reportError(ctx.expr(0), ErrorStrings.ARRAY_DUP_STRUCT)
            return
        }

        if (!dupCount.signalWidth.isFlatArray()) {
            errorListener.reportError(ctx.expr(0), ErrorStrings.ARRAY_DUP_INDEX_MULTI_DIM)
            return
        }

        // if the duplication value is undefined we have no idea what the width will be
        if (dupCount is UndefinedValue) {
            values[ctx] = UndefinedValue(ctx.text, constant)
            return
        }

        assert(dupCount is SimpleValue) { "Duplication count is flat array but not a SimpleValue!" }
        dupCount as SimpleValue

        if (!dupCount.bits.isNumber()) {
            errorListener.reportError(ctx.expr(0), ErrorStrings.ARRAY_DUP_INDEX_NAN)
            return
        }

        val dupTimes = dupCount.bits.toBigInt().toInt()

        if (dupValue is UndefinedValue) {
            values[ctx] = UndefinedValue(
                ctx.text,
                constant,
                width = when (valWidth) {
                    is ArrayWidth -> ArrayWidth(valWidth.size * dupTimes, valWidth.next)
                    is SimpleWidth -> SimpleWidth(valWidth.size * dupTimes)
                    else -> UndefinedSimpleWidth
                }
            )
            return
        }

        if (dupValue is ArrayValue) {
            val elements = mutableListOf<Value>()
            repeat(dupTimes) {
                elements.addAll(dupValue.elements)
            }
            values[ctx] = ArrayValue(elements)
        } else if (dupValue is SimpleValue) {
            val bits = MutableBitList(dupValue.signed)
            repeat(dupTimes) {
                bits.addAll(dupValue.bits)
            }
            values[ctx] = SimpleValue(bits, constant)
        }
        debug(ctx)
    }

    override fun exitExprArray(ctx: ExprArrayContext) {
        if (ctx.expr().isEmpty())
            return

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        ctx.expr().forEach {
            val v = values[it] ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val firstDim = operands[0].first.signalWidth

        var error = false
        operands.forEach {
            if (it.first.signalWidth != firstDim) {
                error = true
                errorListener.reportError(it.second, ErrorStrings.ARRAY_BUILDING_DIM_MISMATCH)
            }
        }
        if (error) return

        val elements = mutableListOf<Value>()
        operands.asReversed().forEach { elements.add(it.first) }
        values[ctx] = ArrayValue(elements)
    }

    override fun exitExprNegate(ctx: ExprNegateContext) {
        val constant = values[ctx.expr()]?.constant == true
        val expr = values[ctx.expr()] ?: return

        if (!expr.signalWidth.isFlatArray()) {
            errorListener.reportError(ctx, ErrorStrings.NEG_MULTI_DIM)
            return
        }

        if (expr is UndefinedValue) {
            values[ctx] = UndefinedValue(ctx.text, constant, expr.width)
            return
        }

        assert(expr is SimpleValue) { "Expression assumed to be SimpleValue" }
        expr as SimpleValue

        if (!expr.bits.isNumber()) {
            values[ctx] = SimpleValue(MutableBitList(true, expr.size+1) { BitValue.Bx }, constant)
            return
        }

        values[ctx] = SimpleValue(MutableBitList(expr.bits.toBigInt().negate(), true, expr.size+1), constant)
        debug(ctx)
    }

    override fun exitExprInvert(ctx: ExprInvertContext) {
        val expr = values[ctx.expr()] ?: return

        values[ctx] = if (ctx.getChild(0).text == "!") {
            expr.not()
        } else { // ~ operator
            expr.invert()
        }
        debug(ctx)
    }

    override fun exitExprAddSub(ctx: ExprAddSubContext) {
        if (ctx.childCount != 3 || ctx.expr().size != 2)
            return

        // is constant if both operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!checkFlat(*ctx.expr().toTypedArray()) {
                errorListener.reportError(
                    it,
                    if (operand == "+") ErrorStrings.ADD_MULTI_DIM else ErrorStrings.SUB_MULTI_DIM
                )
            }) return

        val op1Width = op1.signalWidth
        val op2Width = op2.signalWidth

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (op1Width is SimpleWidth && op2Width is SimpleWidth)
                values[ctx] =
                    UndefinedValue(
                        ctx.text,
                        constant,
                        SimpleWidth(op1Width.size.coerceAtLeast(op2Width.size) + 1)
                    )
            else
                values[ctx] = UndefinedValue(ctx.text, constant)
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a simple array. This shouldn't be possible.")

        val width = op1.bits.size.coerceAtLeast(op2.bits.size) + 1

        val signed = op1.signed && op2.signed

        values[ctx] = when {
            !op1.isNumber() || !op2.isNumber() -> SimpleValue(MutableBitList(BitValue.Bx, width, signed), constant)
            operand == "+" -> SimpleValue(
                MutableBitList(op1.bits.toBigInt().add(op2.bits.toBigInt()), signed, width),
                constant
            )

            else -> SimpleValue(
                MutableBitList(op1.bits.toBigInt().subtract(op2.bits.toBigInt()), signed, width),
                constant
            )
        }
    }

    override fun exitExprMultDiv(ctx: ExprMultDivContext) {
        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        // is constant if both operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val multOp = ctx.getChild(1).text == "*"

        if (!checkFlat(*ctx.expr().toTypedArray()) {
                errorListener.reportError(
                    it,
                    if (multOp) ErrorStrings.MUL_MULTI_DIM else ErrorStrings.DIV_MULTI_DIM
                )
            }) return

        val op1Width = op1.signalWidth
        val op2Width = op2.signalWidth

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (op1Width is SimpleWidth && op2Width is SimpleWidth)
                values[ctx] =
                    UndefinedValue(ctx.text, constant, SimpleWidth(widthOfMult(op1Width.size, op2Width.size)))
            else
                values[ctx] = UndefinedValue(ctx.text, constant)
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a simple array. This shouldn't be possible.")

        val signed = op1.signed && op2.signed
        val op1Bits = MutableBitList(signed, op1.bits)
        val op2Bits = MutableBitList(signed, op2.bits)

        values[ctx] = if (multOp) {
            val width = widthOfMult(op1Bits.size, op2Bits.size)
            if (!op1.isNumber() || !op2.isNumber())
                SimpleValue(MutableBitList(BitValue.Bx, width, signed), constant)
            else
                SimpleValue(MutableBitList(op1Bits.toBigInt().multiply(op2Bits.toBigInt()), signed, width), constant)
        } else {
            val op2BigInt = op2Bits.toBigInt()

            if (!constant && (!op2.constant || !op2Bits.isPowerOf2())) {
                errorListener.reportWarning(ctx.expr(1), WarningStrings.DIVIDE_NOT_POW_2)
            }

            val width = op1Bits.size
            if (!op1.isNumber() || !op2.isNumber() || op2BigInt == BigInteger.ZERO)
                SimpleValue(MutableBitList(BitValue.Bx, width, signed), constant)
            else
                SimpleValue(MutableBitList(op1Bits.toBigInt().divide(op2BigInt), signed, width), constant)
        }
        debug(ctx)
    }

    override fun exitExprShift(ctx: ExprShiftContext) {
        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        // is constant if both operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val value = values[ctx.expr(0)] ?: return
        val shift = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!checkFlat(*ctx.expr().toTypedArray()) {
                errorListener.reportError(it, ErrorStrings.SHIFT_MULTI_DIM)
            }) return

        if (shift is UndefinedValue) {
            values[ctx] = UndefinedValue(ctx.text, constant)
            return
        }

        check(shift is SimpleValue) { "Shift value is flat array but not SimpleValue or UndefinedValue" }

        if (value is UndefinedValue) {
            val vWidth = value.signalWidth
            if (vWidth is SimpleWidth) {
                val w = if (operand == "<<" || operand == "<<<") vWidth.size + shift.bits.toBigInt()
                    .toInt() else vWidth.size
                values[ctx] = UndefinedValue(ctx.text, constant, SimpleWidth(w))
            } else
                values[ctx] = UndefinedValue(ctx.text, constant)
        }

        check(value is SimpleValue) { "Value is flat array but not SimpleValue or UndefinedValue" }

        val isSigned = value.signed && (operand == ">>>" || operand == "<<<")

        if (!shift.bits.isNumber()) {
            values[ctx] = SimpleValue(MutableBitList(isSigned, value.size) { BitValue.Bx }, constant)
            return
        }

        val shiftAmount = shift.bits.toBigInt().toInt()

        values[ctx] = when (operand) {
            ">>" -> SimpleValue(value.bits ushr shiftAmount, constant)
            ">>>" -> SimpleValue(value.bits shr shiftAmount, constant)
            "<<" -> SimpleValue(value.bits ushl shiftAmount, constant)
            "<<<" -> SimpleValue(value.bits shl shiftAmount, constant)
            else -> {
                errorListener.reportError(ctx.getChild(ParserRuleContext::class.java, 1), "Unknown operator $operand")
                return
            }
        }
        debug(ctx)
    }

    override fun exitExprBitwise(ctx: ExprBitwiseContext) {
        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!checkUndefinedMatchingDims(ctx.expr(0), ctx.expr(1)) {
                errorListener.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (!checkFlat(*ctx.expr().toTypedArray()) {
                    errorListener.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                }) return

            val op1Width = op1.signalWidth
            val op2Width = op2.signalWidth

            if (op1Width.isDefinedArray() && op2Width.isDefinedArray()) {
                if (op1Width != op2Width) {
                    errorListener.reportError(ctx.expr(1), ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    return
                }
                values[ctx] = UndefinedValue(ctx.text, constant, op1Width)
            } else {
                values[ctx] = UndefinedValue(ctx.text, constant)
            }
            return
        }

        if (!checkFlatOrMatchingDims(*ctx.expr().toTypedArray()) {
                errorListener.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        values[ctx] = when (operand) {
            "&" -> op1 and op2
            "|" -> op1 or op2
            "^" -> op1 xor op2
            else -> {
                errorListener.reportError(ctx.getChild(ParserRuleContext::class.java, 1), "Unknown operator $operand")
                return
            }
        }
        debug(ctx)
    }

    override fun exitExprReduction(ctx: ExprReductionContext) {
        if (ctx.childCount != 2 || ctx.expr() == null) return

        val constant = values[ctx.expr()]?.constant == true

        val value = values[ctx.expr()] ?: return

        if (value is UndefinedValue) {
            values[ctx] = UndefinedValue(ctx.text, constant, SimpleWidth(1))
            return
        }

        values[ctx] = when (ctx.getChild(0).text) {
            "&" -> value.andReduce()
            "|" -> value.orReduce()
            "^" -> value.xorReduce()
            else -> {
                errorListener.reportError(
                    ctx.getChild(ParserRuleContext::class.java, 0),
                    "Unknown operator ${ctx.getChild(0).text}"
                )
                return
            }
        }

        debug(ctx)

    }

    override fun exitExprCompare(ctx: ExprCompareContext) {
        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        when (val operand = ctx.getChild(1).text) {
            "<", ">", "<=", ">=" -> {
                if (!checkFlat(*ctx.expr().toTypedArray()) {
                        errorListener.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
                    }) return


                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    values[ctx] = UndefinedValue(ctx.text,constant, SimpleWidth(1))
                    return
                }

                if (!checkSimpleValue(*ctx.expr().toTypedArray()) {
                        errorListener.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
                    }) return

                op1 as SimpleValue
                op2 as SimpleValue

                values[ctx] = when (operand) {
                    "<" -> op1 isLessThan op2
                    ">" -> op1 isGreaterThan op2
                    "<=" -> op1 isLessOrEqualTo op2
                    ">=" -> op1 isGreaterOrEqualTo op2
                    else -> throw IllegalStateException()
                }
            }

            "==", "!=" -> {
                if (!checkUndefinedMatchingDims(ctx.expr(0), ctx.expr(1)) {
                        errorListener.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    values[ctx] = UndefinedValue(ctx.text,constant, SimpleWidth(1))
                    return
                }

                if (!checkFlatOrMatchingDims(*ctx.expr().toTypedArray()) {
                        errorListener.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    }) return

                values[ctx] = when (operand) {
                    "==" -> op1 isEqualTo op2
                    "!=" -> op1 isNotEqualTo op2
                    else -> throw IllegalStateException()
                }
            }
        }

        debug(ctx)
    }

    override fun exitExprLogical(ctx: ExprLogicalContext) {
        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!checkFlat(*ctx.expr().toTypedArray()) {
                errorListener.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
            }) return


        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            values[ctx] = UndefinedValue(ctx.text, constant, SimpleWidth(1))
            return
        }

        if (!checkSimpleValue(*ctx.expr().toTypedArray()) {
                errorListener.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
            }) return

        op1 as SimpleValue
        op2 as SimpleValue

        values[ctx] = when (operand) {
            "||" -> op1.isTrue() or op2.isTrue()
            "&&" -> op1.isTrue() and op2.isTrue()
            else -> throw IllegalStateException()
        }

        debug(ctx)
    }

    override fun exitExprTernary(ctx: ExprTernaryContext) {
        if (ctx.expr().size != 3) return

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val cond = values[ctx.expr(0)] ?: return
        val op1 = values[ctx.expr(1)] ?: return
        val op2 = values[ctx.expr(2)] ?: return

        val op1Width = op1.signalWidth
        val op2Width = op2.signalWidth

        if (!cond.signalWidth.isFlatArray()) {
            errorListener.reportError(ctx.expr(0), ErrorStrings.TERN_SELECTOR_MULTI_DIM)
            return
        }

        if (!checkFlatOrMatchingDims(ctx.expr(1), ctx.expr(2)) {
                errorListener.reportError(it, ErrorStrings.OP_TERN_DIM_MISMATCH)
            }) return

        val width = when {
            op1Width == op2Width -> op1Width
            op1Width is SimpleWidth && op2Width is SimpleWidth ->
                SimpleWidth(op1Width.size.coerceAtLeast(op2Width.size))

            else -> {
                errorListener.reportError(ctx, ErrorStrings.UNKNOWN_WIDTH.format(ctx))
                return
            }
        }

        if (cond is UndefinedValue) {
            values[ctx] = UndefinedValue(ctx.text, constant, width)
            return
        }

        val value = if (cond.isTrue().lsb == BitValue.B1) op1 else op2
        if (value.signalWidth != width) {
            if (value !is SimpleValue || width !is SimpleWidth) {
                errorListener.reportError(
                    ctx,
                    "BUG in exitExprTernary! Width of value couldn't be determined after passing checks!"
                )
                return
            }
            values[ctx] = value.resize(width.size)
        } else {
            values[ctx] = value
        }

        debug(ctx)
    }

    override fun exitFunction(ctx: FunctionContext) {
        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val fid = ctx.FUNCTION_ID().text.substring(1) // remove $ from beginning
        val function = Function.values().firstOrNull { it.label == fid }

        if (function == null) {
            errorListener.reportError(ctx.FUNCTION_ID(), ErrorStrings.UNKNOWN_FUNCTION.format(fid))
            return
        }

        val args = ctx.expr().map { values[it] ?: return }

        if (function.argCount >= 0) {
            if (args.size != function.argCount) {
                errorListener.reportError(
                    ctx.FUNCTION_ID(),
                    ErrorStrings.FUNCTION_ARG_COUNT.format(ctx.FUNCTION_ID().toString(), function.argCount)
                )
                return
            }
        } else {
            if (args.size < function.argCount.absoluteValue) {
                errorListener.reportError(
                    ctx.FUNCTION_ID(),
                    String.format(
                        ErrorStrings.FUNCTION_MIN_ARG_COUNT,
                        ctx.FUNCTION_ID(),
                        function.argCount.absoluteValue
                    )
                )
                return
            }
        }

        if (function.constOnly && !constant) {
            errorListener.reportError(ctx.FUNCTION_ID(), ErrorStrings.CONST_FUNCTION.format(ctx.FUNCTION_ID().text))
            return
        }

        when (function) {
            Function.CLOG2 -> {
                val arg = args[0]
                if (arg !is SimpleValue) {
                    errorListener.reportError(
                        ctx.expr(0),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(0).text, arg.toString())
                    )
                    return
                }
                val bigInt = arg.toBigInt()
                if (bigInt == BigInteger.ZERO) {
                    values[ctx] = SimpleValue(MutableBitList(BitValue.B0), constant)
                    return
                }
                values[ctx] = BigFunctions.ln(BigDecimal(bigInt), 32)
                    .divide(BigFunctions.LOG2, RoundingMode.HALF_UP)
                    .setScale(0, RoundingMode.CEILING)
                    .toBigInteger()
                    .toValue(constant)
            }

            Function.POWER -> {
                val arg1 = args[0]
                val arg2 = args[1]
                if (arg1 !is SimpleValue) {
                    errorListener.reportError(
                        ctx.expr(0),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(0).text, arg1.toString())
                    )
                    return
                }
                if (arg2 !is SimpleValue) {
                    errorListener.reportError(
                        ctx.expr(1),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(1).text, arg2.toString())
                    )
                    return
                }
                val b1 = arg1.toBigInt()
                val b2 = arg2.toBigInt()
                try {
                    values[ctx] = b1.pow(b2.intValueExact()).toValue(constant)
                } catch (e: ArithmeticException) {
                    errorListener.reportError(ctx.expr(1), ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.expr(1).text))
                }
            }

            Function.REVERSE -> {
                val arg = args[0]
                if (!arg.signalWidth.isArray()) {
                    errorListener.reportError(ctx.expr(0), ErrorStrings.FUNCTION_ARG_NOT_ARRAY.format(ctx.expr(0).text))
                    return
                }
                values[ctx] = arg.reverse()
            }

            Function.FLATTEN -> {
                if (!args[0].signalWidth.isDefined()) {
                    errorListener.reportError(ctx.expr(0), ErrorStrings.UNKNOWN_WIDTH.format(ctx.expr(0).text))
                    return
                }
                values[ctx] = args[0].flatten()
            }

            Function.BUILD -> {
                val value = args[0]
                if (value !is SimpleValue) {
                    errorListener.reportError(ctx.expr(0), ErrorStrings.BUILD_MULTI_DIM)
                    return
                }
                for (i in 1 until args.size) {
                    if (!args[i].isNumber() || args[i] !is SimpleValue) {
                        errorListener.reportError(
                            ctx.expr(i),
                            ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(i).text, args[i].toString())
                        )
                        return
                    }
                }
                val dims = args.subList(1, args.size).mapIndexed { i, it ->
                    try {
                        (it as SimpleValue).toBigInt().intValueExact()
                    } catch (e: ArithmeticException) {
                        errorListener.reportError(
                            ctx.expr(i + 1),
                            ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.expr(i + 1).text)
                        )
                        return
                    }
                }

                dims.forEachIndexed { i, dim ->
                    if (dim < 0) {
                        errorListener.reportError(
                            ctx.expr(i + 1),
                            ErrorStrings.FUNCTION_ARG_NEG.format(ctx.expr(i + 1).text)
                        )
                        return
                    }
                    if (dim == 0) {
                        errorListener.reportError(
                            ctx.expr(i + 1),
                            ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.expr(i + 1).text)
                        )
                        return
                    }
                }
                val factor = dims.foldRight(1L) { dim, acc -> dim * acc }

                if (value.size % factor != 0L) {
                    errorListener.reportError(ctx.expr(0), ErrorStrings.ARRAY_NOT_DIVISIBLE.format(ctx.expr(0).text))
                    return
                }

                fun buildRecursive(bits: BitList, dims: List<Int>): ArrayValue {
                    val d = dims.last()
                    val vCt = bits.size
                    val step = vCt / d
                    val root = mutableListOf<Value>()
                    check(step * d == vCt) { "Dimensions don't split evenly!" }
                    if (dims.size == 1) {
                        repeat(d) {
                            root.add(SimpleValue(bits.subList(step * it, step * it + step), constant))
                        }
                    } else {
                        repeat(d) {
                            root.add(
                                buildRecursive(
                                    bits.subList(step * it, step * it + step),
                                    dims.subList(0, dims.size - 1)
                                )
                            )
                        }
                    }
                    return ArrayValue(root)
                }

                values[ctx] = buildRecursive(value.bits, dims)
            }

            Function.SIGNED -> {
                when (val arg = args[0]) {
                    is SimpleValue -> values[ctx] = SimpleValue(MutableBitList(true, arg), constant)
                    is UndefinedValue -> values[ctx] = arg.copy()
                    else -> errorListener.reportError(ctx.expr(0), ErrorStrings.SIGNED_MULTI_DIM)
                }
            }

            Function.UNSIGNED -> {
                when (val arg = args[0]) {
                    is SimpleValue -> values[ctx] = SimpleValue(MutableBitList(false, arg), constant)
                    is UndefinedValue -> values[ctx] = arg.copy()
                    else -> errorListener.reportError(ctx.expr(0), ErrorStrings.UNSIGNED_MULTI_DIM)
                }
            }

            Function.CDIV -> {
                val arg1 = args[0]
                val arg2 = args[1]
                if (arg1 !is SimpleValue) {
                    errorListener.reportError(
                        ctx.expr(0),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(0).text, arg1.toString())
                    )
                    return
                }
                if (arg2 !is SimpleValue) {
                    errorListener.reportError(
                        ctx.expr(1),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(1).text, arg2.toString())
                    )
                    return
                }
                val b1 = arg1.toBigInt()
                val b2 = arg2.toBigInt()

                if (b2 == BigInteger.ZERO) {
                    errorListener.reportError(ctx.expr(1), ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.expr(1).text))
                    return
                }

                val d1 = BigDecimal(b1, 10)
                val d2 = BigDecimal(b2, 10)
                values[ctx] = d1
                    .divide(d2, RoundingMode.HALF_UP)
                    .setScale(0, RoundingMode.CEILING)
                    .toBigInteger()
                    .toValue(constant)
            }

            Function.RESIZE -> {
                val value = args[0]
                val size = args[1]
                if (size.isNumber() && size is SimpleValue) {
                    val numBits = try {
                        size.toBigInt().intValueExact()
                    } catch (e: ArithmeticException) {
                        errorListener.reportError(
                            ctx.expr(1),
                            ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.expr(1).text)
                        )
                        return
                    }
                    if (numBits < 0) {
                        errorListener.reportError(ctx.expr(1), ErrorStrings.FUNCTION_ARG_NEG.format(ctx.expr(1).text))
                        return
                    }
                    if (numBits == 0) {
                        errorListener.reportError(ctx.expr(1), ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.expr(1).text))
                        return
                    }
                    if (!value.signalWidth.isFlatArray()) {
                        errorListener.reportError(
                            ctx.expr(0),
                            ErrorStrings.FUNCTION_NOT_FLAT.format(ctx.FUNCTION_ID().text)
                        )
                        return
                    }
                    if (value is SimpleValue && value.bits.minBits() > numBits) {
                        errorListener.reportWarning(
                            ctx.expr(0),
                            ErrorStrings.TRUNC_WARN.format(ctx.expr(1).text, size.toString())
                        )
                    }
                    values[ctx] = when (value) {
                        is SimpleValue -> value.resize(numBits)
                        is UndefinedValue -> value.copy(width = SimpleWidth(numBits))
                        else -> error("Previous error checks failed. This shouldn't be reached!")
                    }
                }
            }
        }
        debug(ctx)
    }
}