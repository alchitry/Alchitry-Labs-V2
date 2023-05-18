package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.BigFunctions
import com.alchitry.labs.parsers.Util.widthOfMult
import com.alchitry.labs.parsers.errors.ErrorStrings
import com.alchitry.labs.parsers.errors.WarningStrings
import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SubSignal
import com.alchitry.labs.parsers.lucidv2.values.*
import com.alchitry.labs.parsers.lucidv2.values.Function
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import org.apache.commons.text.StringEscapeUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.absoluteValue

/**
 * Provides values for all ExprContext and also provides bit selection ranges for BitSelectionContext through
 * the parent class BitSelectionParser.
 */
data class ExprParser(
    private val context: LucidExprContext,
    private val values: MutableMap<ParseTree, Value> = mutableMapOf(),
    private val dependencies: MutableMap<ParseTree, Set<Signal>> = mutableMapOf()
) : LucidBaseListener() {
    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolve(ctx: ExprContext): Value? = values[ctx]
    fun resolveDependencies(ctx: ExprContext): Set<Signal>? = dependencies[ctx]

    private fun debug(ctx: ParserRuleContext) {
        context.reportDebug(ctx, values[ctx].toString())
    }

    /**
     * Returns true if the value at this node doesn't need to be recalculated
     */
    private fun canSkip(ctx: ParseTree): Boolean {
        val value = values[ctx] ?: return false
        return value.constant
    }

    override fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override fun exitArrayIndex(ctx: ArrayIndexContext) {
        dependencies[ctx.expr()]?.let { dependencies[ctx] = it }
    }

    override fun exitNumber(ctx: NumberContext) {
        if (canSkip(ctx)) return
        dependencies[ctx] = emptySet()

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
        val width: Int? = try {
            split?.first()?.let { if (it.isBlank()) null else it.toInt() }
        } catch (e: NumberFormatException) {
            context.reportError(ctx, "Number width value of ${split?.first()} could not be parsed: ${e.message}")
            return
        }

        if (split == null) {
            if (ctx.INT() != null) {
                valueString = ctx.INT().text
            } else { // String
                val str = StringEscapeUtils.unescapeJava(ctx.STRING().text)
                valueString = str.substring(1, str.length - 1)

                val value: Value
                when {
                    valueString.length > 1 -> {
                        val elements = mutableListOf<Value>()
                        repeat(valueString.length) {
                            elements.add(
                                BitListValue(
                                    valueString[it].code,
                                    8,
                                    constant = true,
                                    signed = false
                                )
                            )
                        }
                        value = ArrayValue(elements)
                    }

                    valueString.length == 1 -> {
                        value = BitListValue(valueString[0].code, 8, constant = true, signed = false)
                    }

                    else -> {
                        value = BitListValue(emptyList<Bit>(), constant = true, signed = false)
                        context.reportError(ctx, ErrorStrings.STRING_CANNOT_BE_EMPTY)
                    }
                }
                values[ctx] = value
                return
            }
        } else {
            valueString = split[1]
        }

        val unbound = BitListValue(valueString, radix, constant = true, signed = false)
        val value = if (width != null) {
            BitListValue(valueString, radix, width = width, constant = true, signed = false)
        } else {
            unbound
        }
        if (value.bits.size < unbound.size) {
            context.reportWarning(
                ctx,
                "The value \"${ctx.text}\" is wider than ${value.bits.size} bits and it will be truncated"
            )
        }

        if (value.width.size == 1)
            values[ctx] = value.getBit(0) // return BitValue instead of BitListValue
        else
            values[ctx] = value
    }

    override fun exitParamConstraint(ctx: ParamConstraintContext) {
        if (canSkip(ctx)) return

        values[ctx.expr()]?.let { values[ctx] = it }
        dependencies[ctx.expr()]?.let { dependencies[ctx] = it }
    }

    override fun exitStructConst(ctx: StructConstContext) {
        if (canSkip(ctx)) return

        val type = context.resolve(ctx.structType()) ?: return

        val members = mutableMapOf<String, Value>()

        ctx.structMemberConst().forEach { memberCtx ->
            val name = memberCtx.name().text
            val member = type[name]
            if (member == null) {
                context.reportError(memberCtx.name(), "The member $name doesn't belong to the struct ${type.name}.")
                return
            }

            val value = values[memberCtx.expr()] ?: return

            if (!member.width.canAssign(value.width)) {
                context.reportError(memberCtx.expr(), "The member $name width does not match this expression.")
                return
            }

            if (value is SimpleValue && value.width.getBitCount() > member.width.getBitCount()) {
                context.reportWarning(
                    memberCtx.expr(),
                    "The member $name has fewer bits than this expression. It will be truncated."
                )
            }

            members[name] = if (value is SimpleValue) value.resizeToMatch(member.width) else value
        }

        val missingKeys = type.keys.toMutableList().apply { removeAll(members.keys) }
        if (missingKeys.isNotEmpty()) {
            context.reportError(
                ctx,
                "The following members are missing from the struct: ${missingKeys.joinToString(", ")}."
            )
            return
        }

        values[ctx] = StructValue(type, members)
    }

    override fun exitBitSelection(ctx: BitSelectionContext) {
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.children.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        if (canSkip(ctx)) return

        val signal = context.resolve(ctx.signal()) ?: return

        if (!signal.direction.canRead) {
            context.reportError(ctx.signal(), "The signal ${ctx.signal().text} can't be read!")
            return
        }

        values[ctx] = signal.read(context.evalContext)

        val parentSig = when (signal) {
            is Signal -> signal
            is SubSignal -> signal.parent
        }

        // add dependencies for all signals used in bit selection as well as this signal
        dependencies[ctx] = mutableSetOf(parentSig).apply {
            ctx.signal().bitSelection().forEach { child ->
                dependencies[child]?.let { addAll(it) }
            }
        }
    }

    override fun exitExprStruct(ctx: ExprStructContext) {
        if (canSkip(ctx)) return
        values[ctx.structConst()]?.let { values[ctx] = it }
        dependencies[ctx.structConst()]?.let { dependencies[ctx] = it }
    }

    override fun exitExprFunction(ctx: ExprFunctionContext) {
        if (canSkip(ctx)) return
        values[ctx.function()]?.let { values[ctx] = it }
        dependencies[ctx.function()]?.let { dependencies[ctx] = it }
    }

    override fun exitExprNum(ctx: ExprNumContext) {
        if (canSkip(ctx)) return
        values[ctx.number()]?.let { values[ctx] = it }
        dependencies[ctx.number()]?.let { dependencies[ctx] = it }
    }

    override fun exitExprGroup(ctx: ExprGroupContext) {
        if (canSkip(ctx)) return
        if (ctx.expr() == null)
            return
        values[ctx.expr()]?.let { values[ctx] = it }
        dependencies[ctx.expr()]?.let { dependencies[ctx] = it }
    }

    // always returns an unsigned value
    override fun exitExprConcat(ctx: ExprConcatContext) {
        if (canSkip(ctx)) return
        if (ctx.expr().isEmpty()) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

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
        val baseSigWidth = base.width
        var error = false

        if (baseSigWidth is StructWidth) {
            context.reportError(operands[0].second, "Concatenation can't be performed on structs")
            return
        }

        // if width is array, value is array or simple
        assert(baseSigWidth.isArray())
        when (base) {
            is ArrayValue -> {
                assert(baseSigWidth is ArrayWidth) { "The ArrayValue has a width that isn't an ArrayWidth" }
                operands.forEach {
                    val sigWidth = it.first.width
                    if (sigWidth !is ArrayWidth || sigWidth.next != (baseSigWidth as ArrayWidth).next) {
                        context.reportError(
                            it.second,
                            "Each element in an array concatenation must have the same dimensions"
                        )
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
                    val sigWidth = it.first.width
                    if (!sigWidth.isFlatArray()) {
                        context.reportError(
                            it.second,
                            "Each element in an array concatenation must have the same dimensions"
                        )
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
                        UndefinedValue(constant, BitListWidth(bitCount))
                    else
                        UndefinedValue(constant)

                    return
                }

                val bits = mutableListOf<Bit>()
                operands.asReversed().forEach { bits.addAll((it.first as SimpleValue).bits) }
                values[ctx] = BitListValue(bits, constant, signed = false)
            }

            else -> {
                error("Value with array width isn't an array or simple value")
            }
        }
    }

    // always returns an unsigned value
    override fun exitExprDup(ctx: ExprDupContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val constant = values[ctx.expr(1)]?.constant == true

        val dupCount = values[ctx.expr(0)] ?: return
        val dupValue = values[ctx.expr(1)] ?: return

        if (values[ctx.expr(0)]?.constant != true) {
            context.reportError(ctx.expr(0), "The expression \"${ctx.expr(0).text}\" must be constant")
            return
        }

        val valWidth = dupValue.width

        if (!valWidth.isArray()) {
            context.reportError(ctx.expr(0), "Duplication can't be performed on structs")
            return
        }

        if (!dupCount.width.isFlatArray()) {
            context.reportError(ctx.expr(0), "The array duplication index must be one dimensional")
            return
        }

        // if the duplication value is undefined we have no idea what the width will be
        if (dupCount is UndefinedValue) {
            values[ctx] = UndefinedValue(constant)
            return
        }

        assert(dupCount is SimpleValue) { "Duplication count is flat array but not a SimpleValue!" }
        dupCount as SimpleValue

        if (!dupCount.isNumber()) {
            context.reportError(ctx.expr(0), "The array duplication index must be a number (no x or z values)")
            return
        }

        val dupTimes = try {
            dupCount.toBigInt().intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(ctx.expr(0), "Duplication count is too big to fit into an integer!")
            return
        }

        if (dupValue is UndefinedValue) {
            values[ctx] = UndefinedValue(
                constant,
                width = when (valWidth) {
                    is ArrayWidth -> ArrayWidth(valWidth.size * dupTimes, valWidth.next)
                    is SimpleWidth -> BitListWidth(valWidth.size * dupTimes)
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
            val bits = mutableListOf<Bit>()
            repeat(dupTimes) {
                bits.addAll(dupValue.bits)
            }
            values[ctx] = BitListValue(bits, constant, dupValue.signed)
        }
        debug(ctx)
    }

    override fun exitExprArray(ctx: ExprArrayContext) {
        if (canSkip(ctx)) return

        if (ctx.expr().isEmpty())
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        ctx.expr().forEach {
            val v = values[it] ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val firstDim = operands[0].first.width

        var error = false
        operands.forEach {
            if (it.first.width != firstDim) {
                error = true
                context.reportError(it.second, ErrorStrings.ARRAY_BUILDING_DIM_MISMATCH)
            }
        }
        if (error) return

        val filteredValues = operands.map {
            val v = if (it.first is BitValue) (it.first as BitValue).asBitListValue() else it.first
            v
        }

        val elements = mutableListOf<Value>()
        filteredValues.asReversed().forEach { elements.add(it) }
        values[ctx] = ArrayValue(elements)
    }

    override fun exitExprNegate(ctx: ExprNegateContext) {
        if (canSkip(ctx)) return

        dependencies[ctx.expr()]?.let { dependencies[ctx] = it }

        val constant = values[ctx.expr()]?.constant == true
        val expr = values[ctx.expr()] ?: return

        if (!expr.width.isFlatArray()) {
            context.reportError(ctx, ErrorStrings.NEG_MULTI_DIM)
            return
        }

        if (expr is UndefinedValue) {
            values[ctx] = UndefinedValue(constant, expr.width)
            return
        }

        assert(expr is SimpleValue) { "Expression assumed to be SimpleValue" }
        expr as SimpleValue

        if (!expr.isNumber()) {
            values[ctx] = BitListValue(size = expr.size + 1, constant, signed = false) { Bit.Bx }
            return
        }

        values[ctx] = BitListValue(expr.toBigInt().negate(), constant, true, expr.size + 1)
        debug(ctx)
    }

    override fun exitExprInvert(ctx: ExprInvertContext) {
        if (canSkip(ctx)) return

        val expr = values[ctx.expr()] ?: return

        dependencies[ctx.expr()]?.let { dependencies[ctx] = it }

        values[ctx] = if (ctx.getChild(0).text == "!") {
            expr.isTrue().not()
        } else { // ~ operator
            expr.invert()
        }
        debug(ctx)
    }

    override fun exitExprAddSub(ctx: ExprAddSubContext) {
        if (canSkip(ctx)) return

        if (ctx.childCount != 3 || ctx.expr().size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if both operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!context.checkFlat(*ctx.expr().toTypedArray()) {
                context.reportError(
                    it,
                    if (operand == "+") ErrorStrings.ADD_MULTI_DIM else ErrorStrings.SUB_MULTI_DIM
                )
            }) return

        val op1Width = op1.width
        val op2Width = op2.width

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (op1Width is SimpleWidth && op2Width is SimpleWidth)
                values[ctx] =
                    UndefinedValue(
                        constant,
                        BitListWidth(op1Width.size.coerceAtLeast(op2Width.size) + 1)
                    )
            else
                values[ctx] = UndefinedValue(constant)
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a simple array. This shouldn't be possible.")

        val width = op1.bits.size.coerceAtLeast(op2.bits.size) + 1

        val signed = op1.signed && op2.signed

        values[ctx] = when {
            !op1.isNumber() || !op2.isNumber() -> BitListValue(width, signed, constant) { Bit.Bx }
            operand == "+" -> BitListValue(
                op1.toBigInt().add(op2.toBigInt()),
                constant,
                signed,
                width
            )

            else -> BitListValue(
                op1.toBigInt().subtract(op2.toBigInt()),
                constant,
                signed,
                width
            )
        }
    }

    override fun exitExprMultDiv(ctx: ExprMultDivContext) {
        if (canSkip(ctx)) return

        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if both operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val multOp = ctx.getChild(1).text == "*"

        if (!context.checkFlat(*ctx.expr().toTypedArray()) {
                context.reportError(
                    it,
                    if (multOp) ErrorStrings.MUL_MULTI_DIM else ErrorStrings.DIV_MULTI_DIM
                )
            }) return

        val op1Width = op1.width
        val op2Width = op2.width

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (op1Width is SimpleWidth && op2Width is SimpleWidth)
                values[ctx] =
                    UndefinedValue(constant, BitListWidth(widthOfMult(op1Width.size, op2Width.size)))
            else
                values[ctx] = UndefinedValue(constant)
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a simple array. This shouldn't be possible.")

        val signed = op1.signed && op2.signed

        values[ctx] = if (multOp) {
            val width = widthOfMult(op1.size, op2.size)
            if (!op1.isNumber() || !op2.isNumber())
                BitListValue(width, constant, signed) { Bit.Bx }
            else
                BitListValue(op1.toBigInt(signed).multiply(op2.toBigInt(signed)), constant, signed, width)
        } else {
            val op2BigInt = op2.toBigInt(signed)

            if (!constant && (!op2.constant || !op2.isPowerOf2())) {
                context.reportWarning(ctx.expr(1), WarningStrings.DIVIDE_NOT_POW_2)
            }

            val width = op1.size
            if (!op1.isNumber() || !op2.isNumber() || op2BigInt == BigInteger.ZERO)
                BitListValue(width, constant, signed) { Bit.Bx }
            else
                BitListValue(op1.toBigInt(signed).divide(op2BigInt), constant, signed, width)
        }
        debug(ctx)
    }

    override fun exitExprShift(ctx: ExprShiftContext) {
        if (canSkip(ctx)) return

        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if both operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val value = values[ctx.expr(0)] ?: return
        val shift = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!context.checkFlat(*ctx.expr().toTypedArray()) {
                context.reportError(it, ErrorStrings.SHIFT_MULTI_DIM)
            }) return

        if (shift is UndefinedValue) {
            values[ctx] = UndefinedValue(constant)
            return
        }

        check(shift is SimpleValue) { "Shift value is flat array but not SimpleValue or UndefinedValue" }

        if (value is UndefinedValue) {
            val vWidth = value.width
            if (vWidth is SimpleWidth) {
                val w = if (operand == "<<" || operand == "<<<") vWidth.size + shift.toBigInt()
                    .toInt() else vWidth.size
                values[ctx] = UndefinedValue(constant, BitListWidth(w))
            } else
                values[ctx] = UndefinedValue(constant)
        }

        check(value is SimpleValue) { "Value is flat array but not SimpleValue or UndefinedValue" }

        val isSigned = value.signed && (operand == ">>>" || operand == "<<<")

        if (!shift.isNumber()) {
            values[ctx] = BitListValue(value.size, constant, isSigned) { Bit.Bx }
            return
        }

        val shiftAmount = try {
            shift.toBigInt().intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(ctx.expr(1), "Shift amount didn't fit into an integer!")
            return
        }

        values[ctx] = when (operand) {
            ">>" -> value ushr shiftAmount
            ">>>" -> value shr shiftAmount
            "<<" -> value ushl shiftAmount
            "<<<" -> value shl shiftAmount
            else -> {
                context.reportError(ctx.getChild(ParserRuleContext::class.java, 1), "Unknown operator $operand")
                return
            }
        }.let { if (!constant && it.constant) it.asMutable() else it }
        debug(ctx)
    }

    override fun exitExprBitwise(ctx: ExprBitwiseContext) {
        if (canSkip(ctx)) return

        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!context.checkUndefinedMatchingDims(ctx.expr(0), ctx.expr(1)) {
                context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (!context.checkFlat(*ctx.expr().toTypedArray()) {
                    context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                }) return

            val op1Width = op1.width
            val op2Width = op2.width

            if (op1Width.isDefinedArray() && op2Width.isDefinedArray()) {
                if (op1Width != op2Width) {
                    context.reportError(ctx.expr(1), ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    return
                }
                values[ctx] = UndefinedValue(constant, op1Width)
            } else {
                values[ctx] = UndefinedValue(constant)
            }
            return
        }

        if (!context.checkFlatOrMatchingDims(*ctx.expr().toTypedArray()) {
                context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        values[ctx] = when (operand) {
            "&" -> op1 and op2
            "|" -> op1 or op2
            "^" -> op1 xor op2
            else -> {
                context.reportError(ctx.getChild(ParserRuleContext::class.java, 1), "Unknown operator $operand")
                return
            }
        }
        debug(ctx)
    }

    override fun exitExprReduction(ctx: ExprReductionContext) {
        if (canSkip(ctx)) return

        if (ctx.childCount != 2 || ctx.expr() == null) return

        dependencies[ctx.expr()]?.let { dependencies[ctx] = it }

        val constant = values[ctx.expr()]?.constant == true

        val value = values[ctx.expr()] ?: return

        if (value is UndefinedValue) {
            values[ctx] = UndefinedValue(constant, BitWidth)
            return
        }

        values[ctx] = when (ctx.getChild(0).text) {
            "&" -> value.andReduce()
            "|" -> value.orReduce()
            "^" -> value.xorReduce()
            else -> {
                context.reportError(
                    ctx.getChild(ParserRuleContext::class.java, 0),
                    "Unknown operator ${ctx.getChild(0).text}"
                )
                return
            }
        }

        debug(ctx)

    }

    override fun exitExprCompare(ctx: ExprCompareContext) {
        if (canSkip(ctx)) return

        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        when (val operand = ctx.getChild(1).text) {
            "<", ">", "<=", ">=" -> {
                if (!context.checkFlat(*ctx.expr().toTypedArray()) {
                        context.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
                    }) return


                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    values[ctx] = UndefinedValue(constant, BitWidth)
                    return
                }

                if (!context.checkSimpleValue(*ctx.expr().toTypedArray()) {
                        context.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
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
                if (!context.checkUndefinedMatchingDims(ctx.expr(0), ctx.expr(1)) {
                        context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    values[ctx] = UndefinedValue(constant, BitWidth)
                    return
                }

                if (!context.checkFlatOrMatchingDims(*ctx.expr().toTypedArray()) {
                        context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
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
        if (canSkip(ctx)) return

        if (ctx.childCount != 3 || ctx.expr().size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val op1 = values[ctx.expr(0)] ?: return
        val op2 = values[ctx.expr(1)] ?: return

        val operand = ctx.getChild(1).text

        if (!context.checkFlat(*ctx.expr().toTypedArray()) {
                context.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
            }) return


        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            values[ctx] = UndefinedValue(constant, BitWidth)
            return
        }

        if (!context.checkSimpleValue(*ctx.expr().toTypedArray()) {
                context.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
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
        if (canSkip(ctx)) return

        if (ctx.expr().size != 3) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val cond = values[ctx.expr(0)] ?: return
        val op1 = values[ctx.expr(1)] ?: return
        val op2 = values[ctx.expr(2)] ?: return

        val op1Width = op1.width
        val op2Width = op2.width

        if (!cond.width.isFlatArray()) {
            context.reportError(ctx.expr(0), ErrorStrings.TERN_SELECTOR_MULTI_DIM)
            return
        }

        if (!context.checkFlatOrMatchingDims(ctx.expr(1), ctx.expr(2)) {
                context.reportError(it, ErrorStrings.OP_TERN_DIM_MISMATCH)
            }) return

        val width = when {
            op1Width == op2Width -> op1Width
            op1Width is SimpleWidth && op2Width is SimpleWidth ->
                BitListWidth(op1Width.size.coerceAtLeast(op2Width.size))

            else -> {
                context.reportError(ctx, ErrorStrings.UNKNOWN_WIDTH.format(ctx))
                return
            }
        }

        if (cond is UndefinedValue) {
            values[ctx] = UndefinedValue(constant, width)
            return
        }

        val value = if (cond.isTrue().lsb == Bit.B1) op1 else op2
        if (value.width != width) {
            if (value !is SimpleValue || width !is SimpleWidth) {
                context.reportError(
                    ctx,
                    "BUG in exitExprTernary! Width of value couldn't be determined after passing checks!"
                )
                return
            }
            values[ctx] = value.asBitListValue().resize(width.size)
        } else {
            values[ctx] = value
        }

        debug(ctx)
    }

    override fun exitFunction(ctx: FunctionContext) {
        if (canSkip(ctx)) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = ctx.expr().none { values[it]?.constant != true }

        val fid = ctx.FUNCTION_ID().text.substring(1) // remove $ from beginning
        val function = Function.values().firstOrNull { it.label == fid }

        if (function == null) {
            context.reportError(ctx.FUNCTION_ID(), ErrorStrings.UNKNOWN_FUNCTION.format(fid))
            return
        }

        val args = ctx.expr().map { values[it] ?: return }

        if (function.argCount >= 0) {
            if (args.size != function.argCount) {
                context.reportError(
                    ctx.FUNCTION_ID(),
                    ErrorStrings.FUNCTION_ARG_COUNT.format(ctx.FUNCTION_ID().toString(), function.argCount)
                )
                return
            }
        } else {
            if (args.size < function.argCount.absoluteValue) {
                context.reportError(
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
            context.reportError(ctx.FUNCTION_ID(), ErrorStrings.CONST_FUNCTION.format(ctx.FUNCTION_ID().text))
            return
        }

        when (function) {
            Function.CLOG2 -> {
                val arg = args[0]
                if (arg !is SimpleValue) {
                    context.reportError(
                        ctx.expr(0),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(0).text, arg.toString())
                    )
                    return
                }
                val bigInt = arg.toBigInt()
                if (bigInt == BigInteger.ZERO) {
                    values[ctx] = BitValue(Bit.B0, constant, false)
                    return
                }
                values[ctx] = BigFunctions.ln(BigDecimal(bigInt), 32)
                    .divide(BigFunctions.LOG2, RoundingMode.HALF_UP)
                    .setScale(0, RoundingMode.CEILING)
                    .toBigInteger()
                    .toBitListValue(constant)
            }

            Function.POWER -> {
                val arg1 = args[0]
                val arg2 = args[1]
                if (arg1 !is SimpleValue) {
                    context.reportError(
                        ctx.expr(0),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(0).text, arg1.toString())
                    )
                    return
                }
                if (arg2 !is SimpleValue) {
                    context.reportError(
                        ctx.expr(1),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(1).text, arg2.toString())
                    )
                    return
                }
                val b1 = arg1.toBigInt()
                val b2 = arg2.toBigInt()
                try {
                    values[ctx] = b1.pow(b2.intValueExact()).toBitListValue(constant)
                } catch (e: ArithmeticException) {
                    context.reportError(ctx.expr(1), ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.expr(1).text))
                }
            }

            Function.REVERSE -> {
                val arg = args[0]
                if (!arg.width.isArray()) {
                    context.reportError(ctx.expr(0), ErrorStrings.FUNCTION_ARG_NOT_ARRAY.format(ctx.expr(0).text))
                    return
                }
                values[ctx] = arg.reverse()
            }

            Function.FLATTEN -> {
                if (!args[0].width.isDefined()) {
                    context.reportError(ctx.expr(0), ErrorStrings.UNKNOWN_WIDTH.format(ctx.expr(0).text))
                    return
                }
                values[ctx] = args[0].flatten()
            }

            Function.BUILD -> {
                val value = args[0]
                if (value !is BitListValue) {
                    context.reportError(ctx.expr(0), ErrorStrings.BUILD_MULTI_DIM)
                    return
                }
                for (i in 1 until args.size) {
                    if (!args[i].isNumber() || args[i] !is BitListValue) {
                        context.reportError(
                            ctx.expr(i),
                            ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(i).text, args[i].toString())
                        )
                        return
                    }
                }
                val dims = args.subList(1, args.size).mapIndexed { i, it ->
                    try {
                        (it as BitListValue).toBigInt().intValueExact()
                    } catch (e: ArithmeticException) {
                        context.reportError(
                            ctx.expr(i + 1),
                            ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.expr(i + 1).text)
                        )
                        return
                    }
                }

                dims.forEachIndexed { i, dim ->
                    if (dim < 0) {
                        context.reportError(
                            ctx.expr(i + 1),
                            ErrorStrings.FUNCTION_ARG_NEG.format(ctx.expr(i + 1).text)
                        )
                        return
                    }
                    if (dim == 0) {
                        context.reportError(
                            ctx.expr(i + 1),
                            ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.expr(i + 1).text)
                        )
                        return
                    }
                }
                val factor = dims.foldRight(1L) { dim, acc -> dim * acc }

                if (value.size % factor != 0L) {
                    context.reportError(ctx.expr(0), ErrorStrings.ARRAY_NOT_DIVISIBLE.format(ctx.expr(0).text))
                    return
                }

                fun buildRecursive(bits: List<Bit>, dims: List<Int>): ArrayValue {
                    val d = dims.last()
                    val vCt = bits.size
                    val step = vCt / d
                    val root = mutableListOf<Value>()
                    check(step * d == vCt) { "Dimensions don't split evenly!" }
                    if (dims.size == 1) {
                        repeat(d) {
                            root.add(BitListValue(bits.subList(step * it, step * it + step), constant, false))
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
                    is BitValue -> values[ctx] = arg.copy(signed = true)
                    is BitListValue -> values[ctx] = arg.copy(signed = true)
                    is UndefinedValue -> values[ctx] = arg.copy()
                    else -> context.reportError(ctx.expr(0), ErrorStrings.SIGNED_MULTI_DIM)
                }
            }

            Function.UNSIGNED -> {
                when (val arg = args[0]) {
                    is BitValue -> values[ctx] = arg.copy(signed = false)
                    is BitListValue -> values[ctx] = arg.copy(signed = false)
                    is UndefinedValue -> values[ctx] = arg.copy()
                    else -> context.reportError(ctx.expr(0), ErrorStrings.UNSIGNED_MULTI_DIM)
                }
            }

            Function.CDIV -> {
                val arg1 = args[0]
                val arg2 = args[1]
                if (arg1 !is SimpleValue) {
                    context.reportError(
                        ctx.expr(0),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(0).text, arg1.toString())
                    )
                    return
                }
                if (arg2 !is SimpleValue) {
                    context.reportError(
                        ctx.expr(1),
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.expr(1).text, arg2.toString())
                    )
                    return
                }
                val b1 = arg1.toBigInt()
                val b2 = arg2.toBigInt()

                if (b2 == BigInteger.ZERO) {
                    context.reportError(ctx.expr(1), ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.expr(1).text))
                    return
                }

                val d1 = BigDecimal(b1, 10)
                val d2 = BigDecimal(b2, 10)
                values[ctx] = d1
                    .divide(d2, RoundingMode.HALF_UP)
                    .setScale(0, RoundingMode.CEILING)
                    .toBigInteger()
                    .toBitListValue(constant)
            }

            Function.RESIZE -> {
                val value = args[0]
                val size = args[1]
                if (size.isNumber() && size is BitListValue) {
                    val numBits = try {
                        size.toBigInt().intValueExact()
                    } catch (e: ArithmeticException) {
                        context.reportError(
                            ctx.expr(1),
                            ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.expr(1).text)
                        )
                        return
                    }
                    if (numBits < 0) {
                        context.reportError(ctx.expr(1), ErrorStrings.FUNCTION_ARG_NEG.format(ctx.expr(1).text))
                        return
                    }
                    if (numBits == 0) {
                        context.reportError(ctx.expr(1), ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.expr(1).text))
                        return
                    }
                    if (!value.width.isFlatArray()) {
                        context.reportError(
                            ctx.expr(0),
                            ErrorStrings.FUNCTION_NOT_FLAT.format(ctx.FUNCTION_ID().text)
                        )
                        return
                    }
                    if (value is SimpleValue && value.minBits() > numBits) {
                        context.reportWarning(
                            ctx.expr(0),
                            ErrorStrings.TRUNC_WARN.format(ctx.expr(1).text, size.toString())
                        )
                    }
                    values[ctx] = when (value) {
                        is SimpleValue -> value.asBitListValue().resize(numBits)
                        is UndefinedValue -> value.copy(width = BitListWidth(numBits))
                        else -> error("Previous error checks failed. This shouldn't be reached!")
                    }
                }
            }
        }
        debug(ctx)
    }
}