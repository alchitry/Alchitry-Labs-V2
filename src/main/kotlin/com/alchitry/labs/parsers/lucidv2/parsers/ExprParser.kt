package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.BigFunctions
import com.alchitry.labs.parsers.BitUtil.widthOfMult
import com.alchitry.labs.parsers.errors.ErrorStrings
import com.alchitry.labs.parsers.errors.WarningStrings
import com.alchitry.labs.parsers.grammar.LucidParser.*
import com.alchitry.labs.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs.parsers.lucidv2.types.Function
import com.alchitry.labs.parsers.lucidv2.types.FunctionArg
import com.alchitry.labs.parsers.lucidv2.types.Signal
import com.alchitry.labs.parsers.lucidv2.types.SubSignal
import com.alchitry.labs.parsers.lucidv2.values.*
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.apache.commons.text.StringEscapeUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.*

/**
 * Provides values for all ExprContext and also provides bit selection ranges for BitSelectionContext through
 * the parent class BitSelectionParser.
 */
data class ExprParser(
    private val context: LucidExprContext,
    private val values: MutableMap<ParseTree, Value> = mutableMapOf(),
    private val dependencies: MutableMap<ParseTree, Set<Signal>> = mutableMapOf()
) : SuspendLucidBaseListener() {
    private var inTestBlock = false
    private var inFunctionBlock = false

    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolve(ctx: ExprContext): Value? = values[ctx]
    fun resolveDependencies(ctx: ExprContext): Set<Signal>? = dependencies[ctx]

    private fun debug(ctx: ParserRuleContext) {
        //context.reportInfo(ctx, values[ctx].toString())
    }

    /**
     * Returns true if the value at this node doesn't need to be recalculated
     */
    private fun canSkip(ctx: ParseTree): Boolean {
        return values[ctx]?.constant == true
    }

    override suspend fun enterTestBlock(ctx: TestBlockContext) {
        inTestBlock = true
    }

    override suspend fun exitTestBlock(ctx: TestBlockContext) {
        inTestBlock = false
    }

    override suspend fun enterFunctionBlock(ctx: FunctionBlockContext) {
        inFunctionBlock = true
    }

    override suspend fun exitFunctionBlock(ctx: FunctionBlockContext) {
        inFunctionBlock = false
    }

    override suspend fun exitBitSelectorFixWidth(ctx: BitSelectorFixWidthContext) {
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override suspend fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override suspend fun exitArrayIndex(ctx: ArrayIndexContext) {
        dependencies[ctx.expr() ?: return]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitNumber(ctx: NumberContext) {
        if (canSkip(ctx)) return
        dependencies[ctx] = emptySet()

        val radix: Int
        val split: List<String>?
        val hex = ctx.HEX()
        val bin = ctx.BIN()
        val dec = ctx.DEC()
        when {
            hex != null -> {
                radix = 16
                split = hex.text.split("h")
            }

            bin != null -> {
                radix = 2
                split = bin.text.split("b")
            }

            dec != null -> {
                radix = 10
                split = dec.text.split("d")
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
            val intCtx = ctx.INT()
            if (intCtx != null) {
                valueString = intCtx.text
            } else { // String
                val stringCtx = ctx.STRING() ?: return
                val str = StringEscapeUtils.unescapeJava(stringCtx.text)
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

    override suspend fun exitParamConstraint(ctx: ParamConstraintContext) {
        if (canSkip(ctx)) return
        val exprCtx = ctx.expr() ?: return

        values[exprCtx]?.let { values[ctx] = it }
        dependencies[exprCtx]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitStructConst(ctx: StructConstContext) {
        if (canSkip(ctx)) return

        val type = ctx.structType()?.let { context.resolve(it) } ?: return

        val members = mutableMapOf<String, Value>()

        ctx.structMemberConst().forEach { memberCtx ->
            val nameCtx = memberCtx.name() ?: return
            val name = nameCtx.text
            val member = type[name]
            if (member == null) {
                context.reportError(nameCtx, "The member $name doesn't belong to the struct ${type.name}.")
                return
            }

            val value = memberCtx.expr()?.let { values[it] } ?: return

            if (!member.width.canAssign(value.width)) {
                context.reportError(nameCtx, "The member $name width does not match this expression.")
                return
            }

            if (member.width.willTruncate(value.width)) {
                context.reportWarning(
                    nameCtx,
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

    override suspend fun exitBitSelection(ctx: BitSelectionContext) {
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.children?.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override suspend fun exitExprSignal(ctx: ExprSignalContext) {
        if (canSkip(ctx)) return
        val signalCtx = ctx.signal() ?: return

        val signal = context.resolve(signalCtx) ?: return

        if (!signal.direction.canRead) {
            context.reportError(signalCtx, "The signal ${signalCtx.text} can't be read!")
            return
        }

        values[ctx] = signal.read(context.evalContext)

        val parentSig = when (signal) {
            is Signal -> signal
            is SubSignal -> signal.parent
        }

        // add dependencies for all signals used in bit selection as well as this signal
        dependencies[ctx] = mutableSetOf(parentSig).apply {
            signalCtx.bitSelection().forEach { child ->
                dependencies[child]?.let { addAll(it) }
            }
        }
    }

    override suspend fun exitExprStruct(ctx: ExprStructContext) {
        if (canSkip(ctx)) return
        val structCtx = ctx.structConst() ?: return
        values[structCtx]?.let { values[ctx] = it }
        dependencies[structCtx]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitExprFunction(ctx: ExprFunctionContext) {
        if (canSkip(ctx)) return
        val functionCtx = ctx.function() ?: return
        values[functionCtx]?.let { values[ctx] = it }
        dependencies[functionCtx]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitExprNum(ctx: ExprNumContext) {
        if (canSkip(ctx)) return
        val numCtx = ctx.number() ?: return
        values[numCtx]?.let { values[ctx] = it }
        dependencies[numCtx]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitExprGroup(ctx: ExprGroupContext) {
        if (canSkip(ctx)) return
        val exprCtx = ctx.expr() ?: return

        values[exprCtx]?.let { values[ctx] = it }
        dependencies[exprCtx]?.let { dependencies[ctx] = it }
    }

    // always returns an unsigned value
    override suspend fun exitExprConcat(ctx: ExprConcatContext) {
        if (canSkip(ctx)) return
        val exprCtx = ctx.expr()
        if (exprCtx.isEmpty()) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        exprCtx.forEach {
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
    override suspend fun exitExprDup(ctx: ExprDupContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val constant = values[exprCtx[1]]?.constant == true

        val dupCount = values[exprCtx[0]] ?: return
        val dupValue = values[exprCtx[1]] ?: return

        if (values[exprCtx[0]]?.constant != true) {
            context.reportError(exprCtx[0], "The expression \"${exprCtx[0].text}\" must be constant")
            return
        }

        val valWidth = dupValue.width

        if (!valWidth.isArray()) {
            context.reportError(exprCtx[0], "Duplication can't be performed on structs")
            return
        }

        if (!dupCount.width.isFlatArray()) {
            context.reportError(exprCtx[0], "The array duplication index must be one dimensional")
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
            context.reportError(exprCtx[0], "The array duplication index must be a number (no x or z values)")
            return
        }

        val dupTimes = try {
            dupCount.toBigInt().intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(exprCtx[0], "Duplication count is too big to fit into an integer!")
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

    override suspend fun exitExprArray(ctx: ExprArrayContext) {
        if (canSkip(ctx)) return
        val exprCtx = ctx.expr()
        if (exprCtx.isEmpty())
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        exprCtx.forEach {
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

    override suspend fun exitExprNegate(ctx: ExprNegateContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr() ?: return

        dependencies[exprCtx]?.let { dependencies[ctx] = it }

        val constant = values[exprCtx]?.constant == true
        val expr = values[exprCtx] ?: return

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

    override suspend fun exitExprInvert(ctx: ExprInvertContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr() ?: return

        val expr = values[exprCtx] ?: return

        dependencies[exprCtx]?.let { dependencies[ctx] = it }

        values[ctx] = if (ctx.getChild(0)?.text == "!") {
            expr.isTrue().not()
        } else { // ~ operator
            expr.invert()
        }
        debug(ctx)
    }

    override suspend fun exitExprAddSub(ctx: ExprAddSubContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 3 || exprCtx.size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if both operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val op1 = values[exprCtx[0]] ?: return
        val op2 = values[exprCtx[1]] ?: return

        val operand = ctx.getChild(1)?.text ?: return

        if (!context.checkFlat(*exprCtx.toTypedArray()) {
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

    override suspend fun exitExprMultDiv(ctx: ExprMultDivContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 3 || exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if both operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val op1 = values[exprCtx[0]] ?: return
        val op2 = values[exprCtx[1]] ?: return

        val multOp = ctx.getChild(1)?.text == "*"

        if (!context.checkFlat(*exprCtx.toTypedArray()) {
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
                context.reportWarning(exprCtx[1], WarningStrings.DIVIDE_NOT_POW_2)
            }

            val width = op1.size
            if (!op1.isNumber() || !op2.isNumber() || op2BigInt == BigInteger.ZERO)
                BitListValue(width, constant, signed) { Bit.Bx }
            else
                BitListValue(op1.toBigInt(signed).divide(op2BigInt), constant, signed, width)
        }
        debug(ctx)
    }

    override suspend fun exitExprShift(ctx: ExprShiftContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 3 || exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if both operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val value = values[exprCtx[0]] ?: return
        val shift = values[exprCtx[1]] ?: return

        val operand = ctx.getChild(1)?.text ?: return

        if (!context.checkFlat(*exprCtx.toTypedArray()) {
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
            context.reportError(exprCtx[1], "Shift amount didn't fit into an integer!")
            return
        }

        values[ctx] = when (operand) {
            ">>" -> value ushr shiftAmount
            ">>>" -> value shr shiftAmount
            "<<" -> value ushl shiftAmount
            "<<<" -> value shl shiftAmount
            else -> {
                context.reportError(
                    ctx.getChild(ParserRuleContext::class, 1) ?: return,
                    "Unknown operator $operand"
                )
                return
            }
        }.let { if (!constant && it.constant) it.asMutable() else it }
        debug(ctx)
    }

    override suspend fun exitExprBitwise(ctx: ExprBitwiseContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 3 || exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val op1 = values[exprCtx[0]] ?: return
        val op2 = values[exprCtx[1]] ?: return

        val operand = ctx.getChild(1)?.text ?: return

        if (!context.checkUndefinedMatchingDims(exprCtx[0], exprCtx[1]) {
                context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (!context.checkFlat(*exprCtx.toTypedArray()) {
                    context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                }) return

            val op1Width = op1.width
            val op2Width = op2.width

            if (op1Width.isDefinedArray() && op2Width.isDefinedArray()) {
                if (op1Width != op2Width) {
                    context.reportError(exprCtx[1], ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    return
                }
                values[ctx] = UndefinedValue(constant, op1Width)
            } else {
                values[ctx] = UndefinedValue(constant)
            }
            return
        }

        if (!context.checkFlatOrMatchingDims(*exprCtx.toTypedArray()) {
                context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        values[ctx] = when (operand) {
            "&" -> op1 and op2
            "|" -> op1 or op2
            "^" -> op1 xor op2
            else -> {
                context.reportError(
                    ctx.getChild(ParserRuleContext::class, 1) ?: return,
                    "Unknown operator $operand"
                )
                return
            }
        }
        debug(ctx)
    }

    override suspend fun exitExprReduction(ctx: ExprReductionContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 2 || exprCtx == null) return

        dependencies[exprCtx]?.let { dependencies[ctx] = it }

        val constant = values[exprCtx]?.constant == true

        val value = values[exprCtx] ?: return

        if (value is UndefinedValue) {
            values[ctx] = UndefinedValue(constant, BitWidth)
            return
        }

        values[ctx] = when (ctx.getChild(0)?.text) {
            "&" -> value.andReduce()
            "|" -> value.orReduce()
            "^" -> value.xorReduce()
            else -> {
                context.reportError(
                    ctx.getChild(ParserRuleContext::class, 0) ?: return,
                    "Unknown operator ${ctx.getChild(0)?.text}"
                )
                return
            }
        }

        debug(ctx)

    }

    override suspend fun exitExprCompare(ctx: ExprCompareContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 3 || exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val op1 = values[exprCtx[0]] ?: return
        val op2 = values[exprCtx[1]] ?: return

        when (val operand = ctx.getChild(1)?.text) {
            "<", ">", "<=", ">=" -> {
                if (!context.checkFlat(*exprCtx.toTypedArray()) {
                        context.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
                    }) return


                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    values[ctx] = UndefinedValue(constant, BitWidth)
                    return
                }

                if (!context.checkSimpleValue(*exprCtx.toTypedArray()) {
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
                if (!context.checkUndefinedMatchingDims(exprCtx[0], exprCtx[1]) {
                        context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    values[ctx] = UndefinedValue(constant, BitWidth)
                    return
                }

                if (!context.checkFlatOrMatchingDims(*exprCtx.toTypedArray()) {
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

    override suspend fun exitExprLogical(ctx: ExprLogicalContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (ctx.childCount != 3 || exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val op1 = values[exprCtx[0]] ?: return
        val op2 = values[exprCtx[1]] ?: return

        val operand = ctx.getChild(1)?.text ?: return

        if (!context.checkFlat(*exprCtx.toTypedArray()) {
                context.reportError(it, ErrorStrings.OP_NOT_NUMBER.format(operand))
            }) return


        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            values[ctx] = UndefinedValue(constant, BitWidth)
            return
        }

        if (!context.checkSimpleValue(*exprCtx.toTypedArray()) {
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

    override suspend fun exitExprTernary(ctx: ExprTernaryContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 3) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        // is constant if all operands are constant
        val constant = exprCtx.none { values[it]?.constant != true }

        val cond = values[exprCtx[0]] ?: return
        val op1 = values[exprCtx[1]] ?: return
        val op2 = values[exprCtx[2]] ?: return

        val op1Width = op1.width
        val op2Width = op2.width

        if (!cond.width.isFlatArray()) {
            context.reportError(exprCtx[0], ErrorStrings.TERN_SELECTOR_MULTI_DIM)
            return
        }

        if (!context.checkFlatOrMatchingDims(exprCtx[1], exprCtx[2]) {
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

    override suspend fun exitFunction(ctx: FunctionContext) {
        if (canSkip(ctx)) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.functionExpr().forEach { c -> c.expr()?.let { expr -> dependencies[expr]?.let { addAll(it) } } }
        }

        // is constant if all operands are constant
        val constant = ctx.functionExpr().all { c -> c.expr()?.let { values[it]?.constant == true } ?: true }

        val functionIdCtx = ctx.FUNCTION_ID() ?: return
        val fid = functionIdCtx.text.substring(1) // remove $ from beginning
        val function = context.resolveFunction(fid)

        if (function == null) {
            context.reportError(functionIdCtx, ErrorStrings.UNKNOWN_FUNCTION.format(fid))
            return
        }

        val args = ctx.functionExpr().map { expr ->
            val exprExpr = expr.expr()
            if (exprExpr != null) {
                FunctionArg.ValueArg(values[exprExpr] ?: return)
            } else if (expr.REAL() != null) {
                expr.REAL()?.text?.toDoubleOrNull().let {
                    if (it == null) {
                        context.reportError(expr, "Failed to parse real number \"${expr.REAL()?.text}\"!")
                        return
                    }
                    FunctionArg.RealArg(it)
                }

            } else {
                return
            }
        }

        if (function.argCount >= 0) {
            if (args.size != function.argCount) {
                context.reportError(
                    functionIdCtx,
                    ErrorStrings.FUNCTION_ARG_COUNT.format(ctx.FUNCTION_ID().toString(), function.argCount)
                )
                return
            }
        } else {
            if (args.size < function.argCount.absoluteValue) {
                context.reportError(
                    functionIdCtx,
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
            context.reportError(functionIdCtx, ErrorStrings.CONST_FUNCTION.format(functionIdCtx.text))
            return
        }

        if (function.testOnly && !(inTestBlock || inFunctionBlock)) {
            context.reportError(
                functionIdCtx,
                "The function \"\$${function.label}\" can only be used in test or function blocks."
            )
            return
        }

        fun checkNoReal(): List<Value>? {
            args.forEachIndexed { index, functionArg ->
                if (functionArg is FunctionArg.RealArg) {
                    context.reportError(
                        ctx.functionExpr(index) ?: ctx,
                        "Arguments for \"\$${function.label}()\" can't be real numbers."
                    )
                    return null
                }
            }
            return args.map { (it as FunctionArg.ValueArg).value }
        }

        when (function) {
            Function.WIDTH -> {
                val valArgs = checkNoReal() ?: return
                val arg = valArgs[0]
                val width = arg.width
                if (!width.isSimpleArray()) {
                    context.reportError(ctx.functionExpr(0) ?: ctx, "The function widthOf() can't be used on structs.")
                    return
                }
                values[ctx] = width.toValue()
            }

            Function.FIXEDPOINT, Function.CFIXEDPOINT, Function.FFIXEDPOINT -> {
                val width = when (val w = args[1]) {
                    is FunctionArg.RealArg -> {
                        context.reportError(ctx.functionExpr(1) ?: ctx, "The width value can't be real number.")
                        return
                    }

                    is FunctionArg.ValueArg -> {
                        if (w.value is UndefinedValue) {
                            values[ctx] = UndefinedValue(true)
                            return
                        }
                        if (w.value !is SimpleValue) {
                            context.reportError(ctx.functionExpr(1) ?: ctx, "The width value must be a simple value.")
                            return
                        }
                        try {
                            w.value.toBigInt().intValueExact()
                        } catch (e: ArithmeticException) {
                            context.reportError(
                                ctx.functionExpr(1) ?: ctx,
                                "The width value doesn't fit into an integer!"
                            )
                            return
                        }
                    }
                }

                val fractionalWidth = when (val w = args[2]) {
                    is FunctionArg.RealArg -> {
                        context.reportError(ctx.functionExpr(2) ?: ctx, "The width value can't be real number.")
                        return
                    }

                    is FunctionArg.ValueArg -> {
                        if (w.value is UndefinedValue) {
                            values[ctx] = UndefinedValue(true, BitListWidth(width))
                            return
                        }
                        if (w.value !is SimpleValue) {
                            context.reportError(ctx.functionExpr(2) ?: ctx, "The width value must be a simple value.")
                            return
                        }
                        try {
                            w.value.toBigInt().intValueExact()
                        } catch (e: ArithmeticException) {
                            context.reportError(
                                ctx.functionExpr(2) ?: ctx,
                                "The width value doesn't fit into an integer!"
                            )
                            return
                        }
                    }
                }

                if (width <= 0) {
                    context.reportError(ctx.functionExpr(1) ?: ctx, "The width value must be greater than 0.")
                    return
                }

                if (fractionalWidth < 0) {
                    context.reportError(
                        ctx.functionExpr(2) ?: ctx,
                        "The fractional width must be greater or equal to 0."
                    )
                }

                if (fractionalWidth > width) {
                    context.reportError(
                        ctx.functionExpr(2) ?: ctx,
                        "The partial width must be less than or equal to the total width."
                    )
                    return
                }

                val value = when (val a = args[0]) {
                    is FunctionArg.RealArg -> a.value
                    is FunctionArg.ValueArg -> {
                        if (a.value is UndefinedValue) {
                            values[ctx] = UndefinedValue(true, BitListWidth(width))
                            return
                        }
                        if (a.value !is SimpleValue) {
                            context.reportError(ctx.functionExpr(0) ?: ctx, "The value must be a simple value.")
                            return
                        }
                        a.value.toBigInt().toDouble().also {
                            if (!it.isFinite()) {
                                context.reportError(ctx.functionExpr(0) ?: ctx, "The value doesn't fit into a double!")
                                return
                            }
                        }
                    }
                }

                val adjustedValue = value * 2.0.pow(fractionalWidth)

                val cValue = ceil(adjustedValue)
                val fValue = floor(adjustedValue)

                val cError = abs(adjustedValue - cValue)
                val fError = abs(adjustedValue - fValue)

                val bigInt = when (function) {
                    Function.FIXEDPOINT -> if (cError < fError) cValue else fValue
                    Function.CFIXEDPOINT -> cValue
                    Function.FFIXEDPOINT -> fValue
                    else -> error("Impossible value for function!")
                }.toBigDecimal().toBigInteger()

                if (bigInt.minBits() > width) {
                    context.reportWarning(
                        ctx.functionExpr(0) ?: ctx,
                        "This value is bigger than can fit in the specified width. It will be truncated!"
                    )
                }

                values[ctx] = BitListValue(bigInt, constant = true, width = width)
            }

            Function.CLOG2 -> {
                val valArgs = checkNoReal() ?: return
                val arg = valArgs[0]
                if (arg !is SimpleValue) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg.toString())
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
                val valArgs = checkNoReal() ?: return
                val arg1 = valArgs[0]
                val arg2 = valArgs[1]
                if (arg1 !is SimpleValue) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg1.toString())
                    )
                    return
                }
                if (arg2 !is SimpleValue) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(1)?.text, arg2.toString())
                    )
                    return
                }
                val b1 = arg1.toBigInt()
                val b2 = arg2.toBigInt()
                try {
                    values[ctx] = b1.pow(b2.intValueExact()).toBitListValue(constant)
                } catch (e: ArithmeticException) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.functionExpr(1)?.text)
                    )
                }
            }

            Function.REVERSE -> {
                val valArgs = checkNoReal() ?: return
                val arg = valArgs[0]
                if (!arg.width.isArray()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NOT_ARRAY.format(ctx.functionExpr(0)?.text)
                    )
                    return
                }
                values[ctx] = arg.reverse()
            }

            Function.FLATTEN -> {
                val valArgs = checkNoReal() ?: return
                if (!valArgs[0].width.isDefined()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.UNKNOWN_WIDTH.format(ctx.functionExpr(0)?.text)
                    )
                    return
                }
                values[ctx] = valArgs[0].flatten()
            }

            Function.BUILD -> {
                val valArgs = checkNoReal() ?: return
                val value = valArgs[0]
                if (value !is BitListValue) {
                    context.reportError(ctx.functionExpr(0) ?: ctx, ErrorStrings.BUILD_MULTI_DIM)
                    return
                }
                for (i in 1 until valArgs.size) {
                    if (!valArgs[i].isNumber() || valArgs[i] !is BitListValue) {
                        context.reportError(
                            ctx.functionExpr(i) ?: ctx,
                            ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(i)?.text, valArgs[i].toString())
                        )
                        return
                    }
                }
                val dims = valArgs.subList(1, valArgs.size).mapIndexed { i, it ->
                    try {
                        (it as BitListValue).toBigInt().intValueExact()
                    } catch (e: ArithmeticException) {
                        context.reportError(
                            ctx.functionExpr(i + 1) ?: ctx,
                            ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.functionExpr(i + 1)?.text)
                        )
                        return
                    }
                }

                dims.forEachIndexed { i, dim ->
                    if (dim < 0) {
                        context.reportError(
                            ctx.functionExpr(i + 1) ?: ctx,
                            ErrorStrings.FUNCTION_ARG_NEG.format(ctx.functionExpr(i + 1)?.text)
                        )
                        return
                    }
                    if (dim == 0) {
                        context.reportError(
                            ctx.functionExpr(i + 1) ?: ctx,
                            ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.functionExpr(i + 1)?.text)
                        )
                        return
                    }
                }
                val factor = dims.foldRight(1L) { dim, acc -> dim * acc }

                if (value.size % factor != 0L) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.ARRAY_NOT_DIVISIBLE.format(ctx.functionExpr(0)?.text)
                    )
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
                val valArgs = checkNoReal() ?: return
                when (val arg = valArgs[0]) {
                    is BitValue -> values[ctx] = arg.copy(signed = true)
                    is BitListValue -> values[ctx] = arg.copy(signed = true)
                    is UndefinedValue -> values[ctx] = arg.copy()
                    else -> context.reportError(ctx.functionExpr(0) ?: ctx, ErrorStrings.SIGNED_MULTI_DIM)
                }
            }

            Function.UNSIGNED -> {
                val valArgs = checkNoReal() ?: return
                when (val arg = valArgs[0]) {
                    is BitValue -> values[ctx] = arg.copy(signed = false)
                    is BitListValue -> values[ctx] = arg.copy(signed = false)
                    is UndefinedValue -> values[ctx] = arg.copy()
                    else -> context.reportError(ctx.functionExpr(0) ?: ctx, ErrorStrings.UNSIGNED_MULTI_DIM)
                }
            }

            Function.CDIV -> {
                val valArgs = checkNoReal() ?: return
                val arg1 = valArgs[0]
                val arg2 = valArgs[1]
                if (arg1 !is SimpleValue) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg1.toString())
                    )
                    return
                }
                if (arg2 !is SimpleValue) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(1)?.text, arg2.toString())
                    )
                    return
                }
                val b1 = arg1.toBigInt()
                val b2 = arg2.toBigInt()

                if (b2 == BigInteger.ZERO) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.functionExpr(1)?.text)
                    )
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
                val valArgs = checkNoReal() ?: return
                val value = valArgs[0]
                val size = valArgs[1]
                if (size.isNumber() && size is BitListValue) {
                    val numBits = try {
                        size.toBigInt().intValueExact()
                    } catch (e: ArithmeticException) {
                        context.reportError(
                            ctx.functionExpr(1) ?: ctx,
                            ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.functionExpr(1)?.text)
                        )
                        return
                    }
                    if (numBits < 0) {
                        context.reportError(
                            ctx.functionExpr(1) ?: ctx,
                            ErrorStrings.FUNCTION_ARG_NEG.format(ctx.functionExpr(1)?.text)
                        )
                        return
                    }
                    if (numBits == 0) {
                        context.reportError(
                            ctx.functionExpr(1) ?: ctx,
                            ErrorStrings.FUNCTION_ARG_ZERO.format(ctx.functionExpr(1)?.text)
                        )
                        return
                    }
                    if (!value.width.isFlatArray()) {
                        context.reportError(
                            ctx.functionExpr(0) ?: ctx,
                            ErrorStrings.FUNCTION_NOT_FLAT.format(functionIdCtx.text)
                        )
                        return
                    }
                    if (value is SimpleValue && value.minBits() > numBits) {
                        context.reportWarning(
                            ctx.functionExpr(0) ?: ctx,
                            ErrorStrings.TRUNC_WARN.format(ctx.functionExpr(1)?.text, size.toString())
                        )
                    }
                    values[ctx] = when (value) {
                        is SimpleValue -> value.asBitListValue().resize(numBits)
                        is UndefinedValue -> value.copy(width = BitListWidth(numBits))
                        else -> error("Previous error checks failed. This shouldn't be reached!")
                    }
                }
            }

            Function.TICK -> if (context is LucidBlockContext) context.tick(shouldSnapshot = true)
            Function.SILENTTICK -> if (context is LucidBlockContext) context.tick(shouldSnapshot = false)
            Function.ASSERT -> {
                if (context !is LucidBlockContext || context.stage != ParseStage.Evaluation)
                    return

                val passed = when (val arg = args[0]) {
                    is FunctionArg.RealArg -> arg.value > 0.0
                    is FunctionArg.ValueArg -> arg.value.isTrue().bit == Bit.B1
                }
                if (!passed) {
                    context.reportError(ctx, "Assert failed: \"${ctx.functionExpr(0)?.text}\"")
                    context.abortTest()
                }
            }

            Function.PRINT -> {
                if (context !is LucidBlockContext)
                    return

                val value = when (val arg = args[0]) {
                    is FunctionArg.RealArg -> arg.value.toString()
                    is FunctionArg.ValueArg -> arg.value.toString()
                }
                context.print("${ctx.functionExpr(0)?.text} = $value")
            }

            is Function.Custom -> {
                if (context !is LucidBlockContext) return
                val valArgs = checkNoReal() ?: return

                function.args.forEachIndexed { index, customArg ->
                    if (!customArg.width.canAssign(valArgs[index].width)) {
                        context.reportError(
                            ctx.functionExpr(index) ?: ctx,
                            "The function parameter \"${customArg.name}\" can't be assigned the value \"${
                                ctx.functionExpr(index)?.text
                            }\"!"
                        )
                        return
                    }
                }

                context.runFunction(function, valArgs)
            }
        }
        debug(ctx)
    }
}