package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.asSingleLine
import com.alchitry.labs2.parsers.BigFunctions
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.lucidv2.context.LucidExprContext
import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.types.Function
import com.alchitry.labs2.parsers.lucidv2.values.*
import com.alchitry.labs2.parsers.notations.ErrorStrings
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationType
import com.alchitry.labs2.parsers.notations.WarningStrings
import com.alchitry.labs2.parsers.parents
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.RuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.TerminalNode
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
    private val exprs: MutableMap<ParseTree, Expr> = mutableMapOf(),
    private val assignWidths: MutableMap<ParseTree, SignalWidth> = mutableMapOf(),
    private val widthFence: MutableMap<ParseTree, Boolean> = mutableMapOf(),
    private val dependencies: MutableMap<ParseTree, Set<Signal>> = mutableMapOf(),
    private val deadBlocks: MutableMap<RuleContext, Boolean> = mutableMapOf()
) : SuspendLucidBaseListener() {
    private var inTestBlock = false
    private var inFunctionBlock = false
    val functions = mutableMapOf<FunctionContext, Function>()

    fun withContext(context: LucidExprContext) = copy(context = context)

    fun resolve(ctx: ExprContext): Expr? = exprs[ctx]
    fun resolveDependencies(ctx: ExprContext): Set<Signal>? = dependencies[ctx]

    fun setAssignWidth(ctx: ParseTree, width: SignalWidth) {
        assignWidths[ctx] = width
    }

    private val finalWidthCache: MutableMap<ParseTree, SignalWidth> = mutableMapOf()

    private fun getFinalWidth(ctx: RuleContext): SignalWidth? {
        finalWidthCache[ctx]?.let { return it }

        if (widthFence[ctx] == true)
            return exprs[ctx]?.value?.width

        val parents = ctx.parents
        val fenceIdx = parents.indexOfFirst { widthFence[it] == true }
        val trimmed = if (fenceIdx >= 0) parents.subList(0, fenceIdx) else parents

        val assignWidth = trimmed.asReversed().firstNotNullOfOrNull { assignWidths[it] }
        val exprWidth = trimmed.asReversed().firstNotNullOfOrNull { exprs[it] }?.value?.width

        if (exprWidth != null) {
            if (assignWidth != null) {
                if (assignWidth is SimpleWidth && exprWidth is SimpleWidth) {
                    val w = assignWidth.mergeWith(exprWidth)
                    finalWidthCache[ctx] = w
                    return w
                }
                finalWidthCache[ctx] = assignWidth
                return assignWidth
            }
            finalWidthCache[ctx] = exprWidth
            return exprWidth
        }

        exprs[ctx]?.value?.width?.let {
            finalWidthCache[ctx] = it
            return it
        }
        return null
    }

    fun inDeadBlock(ctx: RuleContext): Boolean {
        var current: RuleContext? = ctx
        while (current != null) {
            if (deadBlocks[current] == true)
                return true
            current = current.parent
        }
        return false
    }

    override suspend fun enterBlock(ctx: BlockContext) {
        when (val parent = ctx.parent) {
            is IfStatContext -> {
                val expr = context.resolve(parent.expr() ?: return) ?: return
                if (!expr.type.known) return
                deadBlocks[ctx] = expr.value.isTrue().invert().bit == Bit.B1
            }

            is ElseStatContext -> {
                val expr = context.resolve((parent.parent as? IfStatContext)?.expr() ?: return) ?: return
                if (!expr.type.known) return
                deadBlocks[ctx] = expr.value.isTrue().bit == Bit.B1
            }
        }
    }

    override suspend fun enterCaseBlock(ctx: CaseBlockContext) {
        val parent = ctx.parent as CaseElemContext
        val caseExprCtx = parent.expr()
        val expr = context.resolve((parent.parent as CaseStatContext).expr() ?: return) ?: return
        if (!expr.type.known) return

        if (caseExprCtx != null) {
            val caseExpr = context.resolve(caseExprCtx) ?: return
            deadBlocks[ctx] = expr.value.isEqualTo(caseExpr.value).invert().bit == Bit.B1
            return
        }

        // default case
        val cases = (parent.parent as CaseStatContext).caseElem()
        val index = cases.indexOf(parent)
        if (index < 0) error("Case isn't inside its parent!")
        deadBlocks[ctx] = cases.subList(0, index).any { deadBlocks[it] != true }
    }

    /**
     * Returns true if the value at this node doesn't need to be recalculated
     */
    private fun canSkip(ctx: ParseTree): Boolean {
        val expr = exprs[ctx] ?: return false
        if (expr.type == ExprType.Constant && expr.value !is UndefinedValue) { // TODO: remove undefined check?
            ctx.skip = true
            return true
        }
        return false
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
        if (dependencies[ctx] != null) return
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override suspend fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        if (dependencies[ctx] != null) return
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.expr().forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override suspend fun exitArrayIndex(ctx: ArrayIndexContext) {
        if (dependencies[ctx] != null) return
        dependencies[ctx.expr() ?: return]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitNumber(ctx: NumberContext) {
        ctx.skip = true
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
                                    signed = false
                                )
                            )
                        }
                        value = ArrayValue(elements)
                    }

                    valueString.length == 1 -> {
                        value = BitListValue(valueString[0].code, 8, signed = false)
                    }

                    else -> {
                        value = BitListValue(0, 8, signed = false)
                        context.reportError(ctx, "String constants cannot be empty.")
                    }
                }
                exprs[ctx] = value.asConstExpr()
                return
            }
        } else {
            valueString = split[1]
        }

        val unbound = BitListValue(valueString, radix, signed = false)
        val value = if (width != null) {
            BitListValue(valueString, radix, width = width, signed = false)
        } else {
            unbound
        }
        if (value.bits.size < unbound.minimumBits()) {
            context.reportWarning(
                ctx,
                "The value \"${ctx.text}\" is wider than ${value.bits.size} bits and it will be truncated"
            )
        }

        if (value.width.size == 1)
            exprs[ctx] = value.getBit(0).asConstExpr() // return BitValue instead of BitListValue
        else
            exprs[ctx] = value.asConstExpr()
    }

    override suspend fun exitParamConstraint(ctx: ParamConstraintContext) {
        if (canSkip(ctx)) return
        val exprCtx = ctx.expr() ?: return

        exprs[exprCtx]?.let { exprs[ctx] = it }
        dependencies[exprCtx]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitStructConst(ctx: StructConstContext) {
        ctx.skip = true
        if (canSkip(ctx)) return

        widthFence[ctx] = true

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

            val value = memberCtx.expr()?.let { exprs[it]?.value } ?: return

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

            members[name] =
                if (value is SimpleValue || value is UndefinedValue) value.resizeToMatch(member.width) else value
        }

        val missingKeys = type.keys.toMutableList().apply { removeAll(members.keys) }
        if (missingKeys.isNotEmpty()) {
            context.reportError(
                ctx,
                "The following members are missing from the struct: ${missingKeys.joinToString(", ")}."
            )
            return
        }

        exprs[ctx] = StructValue(type, members).asConstExpr()
    }

    override suspend fun exitBitSelection(ctx: BitSelectionContext) {
        widthFence[ctx] = true
        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.children?.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }
    }

    override suspend fun exitRepeatStat(ctx: RepeatStatContext) {
        widthFence[ctx] = true
    }

    override suspend fun exitExprSignal(ctx: ExprSignalContext) {
        //if (canSkip(ctx)) return
        val signalCtx = ctx.signal() ?: return

        val signal = context.resolve(signalCtx) ?: return

        val finalWidth = getFinalWidth(ctx)

        if (!signal.direction.canRead) {
            val functionCtx = ctx.firstParentOrNull { it is FunctionContext }
            // exclude reads of the WIDTH function as this doesn't read the signal
            if (functionCtx !is FunctionContext || functionCtx.FUNCTION_ID()?.text != "$" + Function.WIDTH.label) {
                context.reportError(signalCtx, "The signal \"${signalCtx.text}\" can't be read!")
                return
            }
        }

        signal.getSignal().markRead()

        // check the bit selectors to see if they are constant or not
        val bitSelectionType = getExprType(signalCtx.bitSelection().flatMap { bitSelection ->
            context.resolve(bitSelection).flatMap {
                when (it.selectionCtx) {
                    is SelectionContext.Constant -> emptyList()
                    is SelectionContext.DownTo -> listOf(it.selectionCtx.stop)
                    is SelectionContext.Fixed -> listOf(it.selectionCtx.start, it.selectionCtx.stop)
                    is SelectionContext.Single -> listOf(it.selectionCtx.bit)
                    is SelectionContext.UpTo -> listOf(it.selectionCtx.start)
                }
            }
        }.mapNotNull { exprs[it]?.type })

        val type = getExprType(listOf(signal.type, bitSelectionType))

        val value = signal.read(context.evalContext)
        val resizedValue = if (finalWidth != null) value.resizeToMatch(finalWidth) else value

        exprs[ctx] = Expr(resizedValue, type)

        // add dependencies for all signals used in bit selection as well as this signal
        if (dependencies[ctx] == null) {
            val parentSig = when (signal) {
                is Signal -> signal
                is SubSignal -> signal.parent
            }

            dependencies[ctx] = mutableSetOf(parentSig).apply {
                signalCtx.bitSelection().forEach { child ->
                    dependencies[child]?.let { addAll(it) }
                }
            }
        }
    }

    private fun passThrough(ctx: ExprContext, child: ParseTree?) {
        if (canSkip(ctx)) return
        child ?: return
        exprs[child]?.let { exprs[ctx] = it }
        dependencies[child]?.let { dependencies[ctx] = it }
    }

    override suspend fun exitExprStruct(ctx: ExprStructContext) = passThrough(ctx, ctx.structConst())
    override suspend fun exitExprFunction(ctx: ExprFunctionContext) = passThrough(ctx, ctx.function())
    override suspend fun exitExprNum(ctx: ExprNumContext) = passThrough(ctx, ctx.number())
    override suspend fun exitExprGroup(ctx: ExprGroupContext) = passThrough(ctx, ctx.expr())

    private fun SignalWidth.canConcat(other: SignalWidth): Boolean = when (this) {
        is ArrayWidth -> other is ArrayWidth && next.isCompatibleWith(other.next)
        is SimpleWidth -> other is SimpleWidth
        is StructWidth -> false
    }

    private fun getExprType(exprCtx: List<ExprContext>): ExprType? =
        getExprType(exprCtx.map { exprs[it]?.type ?: return null })

    private fun getExprType(dependants: Collection<ExprType>): ExprType {
        return ExprType.entries[dependants.minOfOrNull { it.ordinal } ?: return ExprType.Constant]
    }
    // always returns an unsigned value
    override suspend fun exitExprConcat(ctx: ExprConcatContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        val exprCtx = ctx.expr()
        if (exprCtx.isEmpty()) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val operands = mutableListOf<Pair<Value, ParserRuleContext>>()
        exprCtx.forEach {
            val v = exprs[it]?.value ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val baseSigWidth = operands.map { it.first.width }.firstMostDefined()
        var error = false

        if (!baseSigWidth.isArrayOrSimple()) {
            context.reportError(operands[0].second, "Concatenation can't be performed on structs")
            return
        }

        var dimSize = 0
        var definedWidth = true

        operands.forEach {
            val sigWidth = it.first.width
            if (!sigWidth.canConcat(baseSigWidth)) {
                context.reportError(
                    it.second,
                    "Each element in an array concatenation must have the same dimensions"
                )
                error = true
            }
            when (sigWidth) {
                is UndefinedWidth -> definedWidth = false
                is DefinedArrayWidth -> dimSize += sigWidth.size
                is DefinedSimpleWidth -> dimSize += sigWidth.size
                is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
            }
        }

        if (error) return

        if (operands.any { it.first is UndefinedValue }) {
            val width = if (definedWidth) {
                when (baseSigWidth) {
                    is SimpleWidth -> BitListWidth(dimSize)
                    is ArrayWidth -> DefinedArrayWidth(dimSize, baseSigWidth.next)
                    is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
                }
            } else {
                when (baseSigWidth) {
                    is SimpleWidth -> UndefinedSimpleWidth()
                    is ArrayWidth -> UndefinedArrayWidth(baseSigWidth.next)
                    is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
                }
            }
            exprs[ctx] = UndefinedValue(width).asExpr(type)
            return
        }

        exprs[ctx] = when (baseSigWidth) {
            is SimpleWidth -> {
                val bits = mutableListOf<Bit>()
                operands.asReversed().forEach { bits.addAll((it.first as SimpleValue).bits) }
                BitListValue(bits, signed = false).asExpr(type)
            }

            is ArrayWidth -> {
                val valueList = mutableListOf<Value>()
                operands.asReversed().forEach { valueList.addAll((it.first as ArrayValue).elements) }
                ArrayValue(valueList).asExpr(type)
            }

            is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
        }
    }

    // always returns an unsigned value
    override suspend fun exitExprDup(ctx: ExprDupContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }


        val dupCountExpr = exprs[exprCtx[0]] ?: return
        val dupValueExpr = exprs[exprCtx[1]] ?: return

        if (!dupCountExpr.type.known) {
            context.reportError(exprCtx[0], "The expression \"${exprCtx[0].text.asSingleLine()}\" must be constant.")
            return
        }

        val valWidth = dupValueExpr.value.width

        if (!valWidth.isArrayOrSimple()) {
            context.reportError(exprCtx[0], "Duplication can't be performed on structs.")
            return
        }

        if (dupCountExpr.value.width !is SimpleWidth) {
            context.reportError(exprCtx[0], "The array duplication index must be one dimensional.")
            return
        }

        // if the duplication value is undefined, we have no idea what the width will be
        if (dupCountExpr.value is UndefinedValue) {
            exprs[ctx] = UndefinedValue().asExpr(dupValueExpr.type)
            return
        }

        assert(dupCountExpr.value is SimpleValue) { "Duplication count is flat array but not a SimpleValue!" }
        dupCountExpr.value as SimpleValue

        if (!dupCountExpr.value.isNumber()) {
            context.reportError(exprCtx[0], "The array duplication index must be a number (no x or z values).")
            return
        }

        val dupTimes = try {
            dupCountExpr.value.toBigInt()!!.intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(exprCtx[0], "Duplication count is too big to fit into an integer!")
            return
        }

        if (dupValueExpr.value is UndefinedValue) {
            exprs[ctx] = UndefinedValue(
                width = when (valWidth) {
                    is DefinedArrayWidth -> DefinedArrayWidth(valWidth.size * dupTimes, valWidth.next)
                    is DefinedSimpleWidth -> BitListWidth(valWidth.size * dupTimes)
                    is UndefinedSimpleWidth -> valWidth
                    is UndefinedArrayWidth -> valWidth
                    is StructWidth -> error("[BUG] Width was StructWidth. Should be impossible.")
                }
            ).asExpr(dupValueExpr.type)
            return
        }

        if (dupTimes <= 0) {
            context.reportError(exprCtx[0], "Duplication count must be greater than 0.")
            return
        }

        if (dupValueExpr.value is ArrayValue) {
            val elements = mutableListOf<Value>()
            repeat(dupTimes) {
                elements.addAll(dupValueExpr.value.elements)
            }
            exprs[ctx] = ArrayValue(elements).asExpr(dupValueExpr.type)
        } else if (dupValueExpr.value is SimpleValue) {
            val bits = mutableListOf<Bit>()
            repeat(dupTimes) {
                bits.addAll(dupValueExpr.value.bits)
            }
            exprs[ctx] = BitListValue(bits, dupValueExpr.value.signed).asExpr(dupValueExpr.type)
        }
    }

    override suspend fun exitExprArray(ctx: ExprArrayContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        val exprCtx = ctx.expr()
        if (exprCtx.isEmpty())
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val operands = mutableListOf<Pair<Expr, ParserRuleContext>>()
        exprCtx.forEach {
            val v = exprs[it] ?: return
            operands.add(Pair(v, it))
        }

        if (operands.isEmpty())
            return

        val filteredValues = operands.map {
            val v = (it.first.value as? BitValue)?.asBitListValue()?.asExpr(it.first.type) ?: it.first
            v
        }

        val arrayDim = filteredValues.map { it.value.width }.firstMostDefined()

        val errors = operands.map {
            // check if the widths match, or at least one is an UndefinedSimpleWidth, and they're both simple
            if (!it.first.value.width.isCompatibleWith(arrayDim)) {
                context.reportError(it.second, "Each element in an array builder must have the same dimensions")
                true
            } else {
                false
            }
        }

        exprs[ctx] = ArrayValue(filteredValues.mapIndexed { index, expr ->
            if (errors[index] || expr.value.width is UndefinedWidth)
                UndefinedValue(arrayDim)
            else
                expr.value
        }.asReversed()).asExpr(getExprType(filteredValues.map { it.type }))
    }

    override suspend fun exitExprNegate(ctx: ExprNegateContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr() ?: return

        dependencies[exprCtx]?.let { dependencies[ctx] = it }

        val expr = exprs[exprCtx] ?: return

        if (!expr.value.width.isSimple()) {
            context.reportError(ctx, "Only single dimensional arrays can be negated")
            return
        }

        if (expr.value is UndefinedValue) {
            exprs[ctx] = UndefinedValue(expr.value.width).asExpr(expr.type)
            return
        }

        assert(expr.value is SimpleValue) { "Expression assumed to be SimpleValue" }
        expr.value as SimpleValue

        if (!expr.value.isNumber()) {
            exprs[ctx] = BitListValue(size = expr.value.size + 1, signed = false) { Bit.Bx }.asExpr(expr.type)
            return
        }

        exprs[ctx] = BitListValue(expr.value.toBigInt()!!.negate(), true, expr.value.size + 1).asExpr(expr.type)
    }

    override suspend fun exitExprInvert(ctx: ExprInvertContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr() ?: return

        val expr = exprs[exprCtx] ?: return

        dependencies[exprCtx]?.let { dependencies[ctx] = it }

        exprs[ctx] = if (ctx.getChild(0)?.text == "!") {
            if (expr.value is UndefinedValue)
                UndefinedValue(BitWidth).asExpr(expr.type)
            else
                expr.value.isTrue().not().asExpr(expr.type)
        } else { // ~ operator
            expr.value.invert().asExpr(expr.type)
        }
    }

    override suspend fun exitExprAddSub(ctx: ExprAddSubContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2)
            return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val op1 = exprs[exprCtx[0]]?.value ?: return
        val op2 = exprs[exprCtx[1]]?.value ?: return

        val operand = getOperator(ctx) ?: return

        if (!context.checkSimpleWidth(*exprCtx.toTypedArray()) {
                context.reportError(
                    it,
                    "Only single dimensional arrays can be ${if (operand == "+") "added" else "subtracted"}"
                )
            }) return

        val op1Width = op1.width
        val op2Width = op2.width

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            if (op1Width is DefinedSimpleWidth && op2Width is DefinedSimpleWidth)
                exprs[ctx] =
                    UndefinedValue(
                        BitListWidth(op1Width.size.coerceAtLeast(op2Width.size) + 1)
                    ).asExpr(type)
            else
                exprs[ctx] = UndefinedValue().asExpr(type)
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a SimpleValue. This shouldn't be possible.")

        val width = op1.bits.size.coerceAtLeast(op2.bits.size) + 1

        val signed = op1.signed && op2.signed

        exprs[ctx] = when {
            !op1.isNumber() || !op2.isNumber() -> BitListValue(
                size = width,
                signed = signed
            ) { Bit.Bx }.asExpr(type)

            operand == "+" -> BitListValue(
                bigInt = op1.toBigInt()!!.add(op2.toBigInt()),
                signed = signed,
                width = width
            ).asExpr(type)

            else -> BitListValue(
                bigInt = op1.toBigInt()!!.subtract(op2.toBigInt()),
                signed = signed,
                width = width
            ).asExpr(type)
        }
    }

    override suspend fun exitExprMultDiv(ctx: ExprMultDivContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val op1 = exprs[exprCtx[0]]?.value ?: return
        val op2Expr = exprs[exprCtx[1]] ?: return
        val op2 = op2Expr.value

        val operand = getOperator(ctx) ?: return

        val multOp = operand == "*"

        if (!context.checkSimpleWidth(*exprCtx.toTypedArray()) {
                context.reportError(
                    it,
                    "Only single dimensional arrays can be ${if (multOp) "multiplied" else "divided"}"
                )
            }) return

        val op1Width = op1.width
        val op2Width = op2.width

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            exprs[ctx] = when {
                multOp && op1Width is DefinedSimpleWidth && op2Width is DefinedSimpleWidth -> {
                    val finalWidth = getFinalWidth(ctx)
                    val mulWidth = op1Width.size + op2Width.size
                    val width = finalWidth?.let { it.bitCount?.coerceAtLeast(mulWidth) } ?: mulWidth
                    UndefinedValue(BitListWidth(width)).asExpr(type)
                }

                !multOp && op1Width is DefinedSimpleWidth -> UndefinedValue(BitListWidth(op1Width.size)).asExpr(type)
                else -> UndefinedValue().asExpr(type)
            }
            return
        }

        if (op1 !is SimpleValue || op2 !is SimpleValue)
            error("One (or both) of the operands isn't a simple array. This shouldn't be possible.")

        val signed = op1.signed && op2.signed
        op1Width as DefinedSimpleWidth
        op2Width as DefinedSimpleWidth

        exprs[ctx] = (if (multOp) {
            val finalWidth = getFinalWidth(ctx)
            val mulWidth = op1Width.size + op2Width.size
            val width = finalWidth?.let { it.bitCount?.coerceAtLeast(mulWidth) } ?: mulWidth
            if (!op1.isNumber() || !op2.isNumber())
                BitListValue(size = width, signed = signed) { Bit.Bx }
            else
                BitListValue(
                    bigInt = op1.toBigInt(signed)!!.multiply(op2.toBigInt(signed)),
                    signed = signed,
                    width = width
                )
        } else {
            val op2BigInt = op2.toBigInt(signed)!!

            if (!type.known && (!op2Expr.type.known || !op2.isPowerOf2())) {
                context.reportWarning(exprCtx[1], WarningStrings.DIVIDE_NOT_POW_2)
            }

            val width = op1.size
            if (!op1.isNumber() || !op2.isNumber() || op2BigInt == BigInteger.ZERO)
                BitListValue(size = width, signed = signed) { Bit.Bx }
            else
                BitListValue(
                    bigInt = op1.toBigInt(signed)!!.divide(op2BigInt),
                    signed = signed,
                    width = width
                )
        }).asExpr(type)
    }

    override suspend fun exitExprShift(ctx: ExprShiftContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val value = exprs[exprCtx[0]]?.value ?: return
        val shift = exprs[exprCtx[1]]?.value ?: return

        val operand = getOperator(ctx) ?: return

        if (!context.checkSimpleWidth(*exprCtx.toTypedArray()) {
                context.reportError(it, ErrorStrings.SHIFT_MULTI_DIM)
            }) return

        if (shift is UndefinedValue) {
            exprs[ctx] = UndefinedValue().asExpr(type)
            return
        }

        check(shift is SimpleValue) { "Shift value has simple width but is not SimpleValue or UndefinedValue" }

        if (value is UndefinedValue) {
            val vWidth = value.width
            exprs[ctx] = if (vWidth is DefinedSimpleWidth && shift.isNumber()) {
                val w = if (operand == "<<" || operand == "<<<") vWidth.size + shift.toBigInt()!!
                    .toInt() else vWidth.size
                UndefinedValue(BitListWidth(w)).asExpr(type)
            } else {
                UndefinedValue().asExpr(type)
            }
            return
        }

        check(value is SimpleValue) { "Value is flat array but not SimpleValue or UndefinedValue" }

        val isSigned = value.signed && (operand == ">>>" || operand == "<<<")

        if (!shift.isNumber()) {
            exprs[ctx] = BitListValue(value.size, isSigned) { Bit.Bx }.asExpr(type)
            return
        }

        val shiftAmount = try {
            shift.toBigInt()!!.intValueExact()
        } catch (e: java.lang.ArithmeticException) {
            context.reportError(exprCtx[1], "Shift amount didn't fit into an integer!")
            return
        }

        exprs[ctx] = when (operand) {
            ">>" -> value ushr shiftAmount
            ">>>" -> value shr shiftAmount
            "<<" -> value ushl shiftAmount
            "<<<" -> value shl shiftAmount
            else -> {
                context.reportError(ctx, "Unknown operator $operand")
                return
            }
        }.asExpr(type)
    }

    /**
     * Merges two compatible widths into one replacing [UndefinedWidth] with defined ones where possible.
     */
    private fun SignalWidth.mergeWith(other: SignalWidth): SignalWidth {
        require(this.canAssign(other)) { "mergeWith() can only be used on assignable widths!" }
        return when (this) {
            is DefinedArrayWidth -> copy(next = next.mergeWith((other as ArrayWidth).next))
            is BitWidth -> when (other) {
                is BitWidth, is UndefinedSimpleWidth -> BitWidth
                is BitListWidth -> other
                else -> error("[BUG] Impossible case if compatible!")
            }

            is BitListWidth -> BitListWidth(size.coerceAtLeast(other.bitCount ?: 0))
            is StructWidth -> this
            is UndefinedArrayWidth -> when (other) {
                is DefinedArrayWidth -> other.copy(next = next.mergeWith(other.next))
                is UndefinedArrayWidth -> UndefinedArrayWidth(next.mergeWith(other.next))
                else -> error("[BUG] Impossible case if compatible!")
            }

            is UndefinedSimpleWidth -> when (other) {
                is BitWidth, is BitListWidth, is UndefinedSimpleWidth -> other
                else -> error("[BUG] Impossible case if compatible!")
            }
        }
    }

    override suspend fun exitExprBitwise(ctx: ExprBitwiseContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val op1 = exprs[exprCtx[0]]?.value ?: return
        val op2 = exprs[exprCtx[1]]?.value ?: return

        val operand = getOperator(ctx) ?: return

        if (!context.checkSimpleOrCompatible(exprCtx[0], exprCtx[1]) {
                context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
            }) return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            exprs[ctx] = UndefinedValue(op1.width.mergeWith(op2.width)).asExpr(type)
            return
        }

        exprs[ctx] = when (operand) {
            "&" -> op1 and op2
            "|" -> op1 or op2
            "^" -> op1 xor op2
            else -> {
                context.reportError(ctx, "Unknown operator $operand")
                return
            }
        }.asExpr(type)
    }

    override suspend fun exitExprReduction(ctx: ExprReductionContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        val exprCtx = ctx.expr() ?: return

        dependencies[exprCtx]?.let { dependencies[ctx] = it }

        val expr = exprs[exprCtx] ?: return
        val value = expr.value

        if (value is UndefinedValue) {
            exprs[ctx] = UndefinedValue(BitWidth).asExpr(expr.type)
            return
        }

        exprs[ctx] = when (ctx.getChild(0)?.text) {
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
        }.asExpr(expr.type)
    }

    override suspend fun exitExprCompare(ctx: ExprCompareContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val op1 = exprs[exprCtx[0]]?.value ?: return
        val op2 = exprs[exprCtx[1]]?.value ?: return

        val operand = getOperator(ctx) ?: return

        when (operand) {
            "<", ">", "<=", ">=" -> {
                if (!context.checkSimpleWidth(*exprCtx.toTypedArray()) {
                        context.reportError(it, "The operator \"$operand\" can only be used on simple values.")
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    exprs[ctx] = UndefinedValue(BitWidth).asExpr(type)
                    return
                }

                op1 as SimpleValue
                op2 as SimpleValue

                exprs[ctx] = when (operand) {
                    "<" -> op1 isLessThan op2
                    ">" -> op1 isGreaterThan op2
                    "<=" -> op1 isLessOrEqualTo op2
                    ">=" -> op1 isGreaterOrEqualTo op2
                    else -> error("Unknown operand!")
                }.asExpr(type)
            }

            "==", "!=" -> {
                if (!context.checkSimpleOrCompatible(*exprCtx.toTypedArray()) {
                        context.reportError(it, ErrorStrings.OP_DIM_MISMATCH.format(operand))
                    }) return

                if (op1 is UndefinedValue || op2 is UndefinedValue) {
                    exprs[ctx] = UndefinedValue(BitWidth).asExpr(type)
                    return
                }

                exprs[ctx] = when (operand) {
                    "==" -> op1 isEqualTo op2
                    "!=" -> op1 isNotEqualTo op2
                    else -> error("Unknown operand!")
                }.asExpr(type)
            }
        }
    }

    override suspend fun exitExprLogical(ctx: ExprLogicalContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        val exprCtx = ctx.expr()

        if (exprCtx.size != 2) return

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val op1 = exprs[exprCtx[0]]?.value ?: return
        val op2 = exprs[exprCtx[1]]?.value ?: return

        val operand = getOperator(ctx) ?: return

        if (op1 is UndefinedValue || op2 is UndefinedValue) {
            exprs[ctx] = UndefinedValue(BitWidth).asExpr(type)
            return
        }

        op1 as SimpleValue
        op2 as SimpleValue

        exprs[ctx] = when (operand) {
            "||" -> op1.isTrue() or op2.isTrue()
            "&&" -> op1.isTrue() and op2.isTrue()
            else -> error("Unknown operand!")
        }.asExpr(type)
    }

    override suspend fun exitExprTernary(ctx: ExprTernaryContext) {
        if (canSkip(ctx)) return

        val exprCtx = ctx.expr()

        if (exprCtx.size != 3) return

        widthFence[exprCtx[0]] = true

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            exprCtx.forEach { c -> dependencies[c]?.let { addAll(it) } }
        }

        val type = getExprType(exprCtx) ?: return

        val cond = exprs[exprCtx[0]]?.value ?: return
        val op1 = exprs[exprCtx[1]]?.value ?: return
        val op2 = exprs[exprCtx[2]]?.value ?: return

        val op1Width = op1.width
        val op2Width = op2.width

        if (!cond.width.isSimple()) {
            context.reportError(exprCtx[0], ErrorStrings.TERN_SELECTOR_MULTI_DIM)
            return
        }

        if (!context.checkSimpleOrCompatible(exprCtx[1], exprCtx[2]) {
                context.reportError(it, ErrorStrings.OP_TERN_DIM_MISMATCH)
            }) return

        val width = op1Width.mergeWith(op2Width)

        if (cond is UndefinedValue) {
            exprs[ctx] = UndefinedValue(width).asExpr(type)
            return
        }

        val value = if (cond.isTrue().lsb == Bit.B1) op1 else op2
        if (value.width != width) {
            exprs[ctx] = value.resizeToMatch(width).asExpr(type)
        } else {
            exprs[ctx] = value.asExpr(type)
        }
    }

    override suspend fun exitFunction(ctx: FunctionContext) {
        if (canSkip(ctx)) return

        widthFence[ctx] = true

        dependencies[ctx] = mutableSetOf<Signal>().apply {
            ctx.functionExpr().forEach { c -> c.expr()?.let { expr -> dependencies[expr]?.let { addAll(it) } } }
        }

        val type = getExprType(ctx.functionExpr().map { c -> c.expr()?.let { exprs[it]?.type } ?: ExprType.Constant })

        val functionIdCtx = ctx.FUNCTION_ID() ?: return
        val fid = functionIdCtx.text.substring(1) // remove $ from beginning
        val function = context.resolveFunction(fid)

        if (function == null) {
            context.reportError(functionIdCtx, ErrorStrings.UNKNOWN_FUNCTION.format(fid))
            return
        }

        functions[ctx] = function

        val functionExprCtxs = ctx.functionExpr()

        if (function.argCount >= 0) {
            if (functionExprCtxs.size != function.argCount) {
                context.reportError(
                    functionIdCtx,
                    ErrorStrings.FUNCTION_ARG_COUNT.format(ctx.FUNCTION_ID().toString(), function.argCount)
                )
                return
            }
        } else {
            if (functionExprCtxs.size < function.argCount.absoluteValue) {
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

        if (function == Function.WIDTH) {
            val argCtx = functionExprCtxs.first()
            val sig = context.resolveSignal(ctx, argCtx.text)
            if (sig is EnumType) {
                exprs[ctx] = sig.width.toValue().asConstExpr()
                return
            }
        }

        val args = functionExprCtxs.map { expr ->
            val exprExpr = expr.expr()
            if (exprExpr != null) {
                FunctionArg.ValueArg(exprs[exprExpr]?.value ?: return)
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

        if (function.exprType.ordinal > type.ordinal) {
            context.reportError(functionIdCtx, ErrorStrings.CONST_FUNCTION.format(functionIdCtx.text))
            return
        }

        if (function.testOnly && !(inTestBlock || inFunctionBlock)) {
            context.reportError(
                functionIdCtx,
                "The function \"$${function.label}()\" can only be used in test or function blocks."
            )
            return
        }

        fun checkOnlyValues(): List<Value>? {
            args.forEachIndexed { index, functionArg ->
                if (functionArg is FunctionArg.RealArg) {
                    context.reportError(
                        ctx.functionExpr(index) ?: ctx,
                        "Arguments for \"$${function.label}()\" can't be real numbers."
                    )
                    return null
                }
            }
            return args.map { (it as FunctionArg.ValueArg).value }
        }

        when (function) {
            Function.WIDTH -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                val width = arg.width
                if (!width.isSimpleArray()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The function \"$${Function.WIDTH.label}()\" can't be used on structs."
                    )
                    return
                }
                exprs[ctx] = width.toValue().asConstExpr()
            }

            Function.FIXEDPOINT, Function.CFIXEDPOINT, Function.FFIXEDPOINT -> {
                val width = when (val w = args[1]) {
                    is FunctionArg.RealArg -> {
                        context.reportError(ctx.functionExpr(1) ?: ctx, "The width value can't be real number.")
                        return
                    }

                    is FunctionArg.ValueArg -> {
                        if (!w.value.width.isSimple()) {
                            context.reportError(ctx.functionExpr(1) ?: ctx, "The width value must be a simple value.")
                            return
                        }
                        if (w.value is UndefinedValue) {
                            exprs[ctx] = UndefinedValue().asConstExpr()
                            return
                        }
                        w.value as SimpleValue // implied by width.isSimple and !is UndefinedValue
                        if (!w.value.isNumber()) {
                            context.reportError(ctx.functionExpr(1) ?: ctx, "The width value must be a number.")
                            return
                        }
                        try {
                            w.value.toBigInt()!!.intValueExact()
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
                            exprs[ctx] = UndefinedValue(BitListWidth(width)).asConstExpr()
                            return
                        }
                        if (w.value !is SimpleValue) {
                            context.reportError(ctx.functionExpr(2) ?: ctx, "The width value must be a simple value.")
                            return
                        }
                        if (!w.value.isNumber()) {
                            context.reportError(ctx.functionExpr(2) ?: ctx, "The width value must be a number.")
                            return
                        }
                        try {
                            w.value.toBigInt()!!.intValueExact()
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
                            exprs[ctx] = UndefinedValue(BitListWidth(width)).asConstExpr()
                            return
                        }
                        if (a.value !is SimpleValue) {
                            context.reportError(ctx.functionExpr(0) ?: ctx, "The value must be a simple value.")
                            return
                        }
                        if (!a.value.isNumber()) {
                            context.reportError(ctx.functionExpr(1) ?: ctx, "The value must be a number.")
                            return
                        }
                        a.value.toBigInt()!!.toDouble().also {
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

                exprs[ctx] = BitListValue(bigInt, width = width).asConstExpr()
            }

            Function.CLOG2 -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                if (!arg.width.isSimple()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The argument \"${ctx.functionExpr(0)?.text}\" with value \"$arg\" must be a number."
                    )
                    return
                }
                if (arg is UndefinedValue) {
                    exprs[ctx] = UndefinedValue().asExpr(type)
                    return
                }
                if (!arg.isNumber()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The argument \"${ctx.functionExpr(0)?.text}\" with value \"$arg\" must be a number."
                    )
                    return
                }
                arg as SimpleValue
                val bigInt = arg.toBigInt()
                if (bigInt == BigInteger.ZERO) {
                    exprs[ctx] = BitValue(Bit.B0, false).asExpr(type)
                    return
                }
                exprs[ctx] = BigFunctions.ln(BigDecimal(bigInt), 32)
                    .divide(BigFunctions.LOG2, RoundingMode.HALF_UP)
                    .setScale(0, RoundingMode.CEILING)
                    .toBigInteger()
                    .toBitListValue()
                    .asExpr(type)
            }

            Function.POWER -> {
                val valArgs = checkOnlyValues() ?: return
                val arg1 = valArgs[0]
                val arg2 = valArgs[1]
                if (!arg1.width.isSimple()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg1.toString())
                    )
                    return
                }
                if (!arg2.width.isSimple()) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(1)?.text, arg2.toString())
                    )
                    return
                }

                if (arg1 is UndefinedValue || arg2 is UndefinedValue) {
                    exprs[ctx] = UndefinedValue().asExpr(type)
                    return
                }

                if (!arg1.isNumber()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg1.toString())
                    )
                    return
                }
                if (!arg2.isNumber()) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(1)?.text, arg2.toString())
                    )
                    return
                }

                arg1 as SimpleValue
                arg2 as SimpleValue

                val b1 = arg1.toBigInt()!!
                val b2 = arg2.toBigInt()!!
                try {
                    exprs[ctx] = b1.pow(b2.intValueExact()).toBitListValue().asExpr(type)
                } catch (e: ArithmeticException) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.functionExpr(1)?.text)
                    )
                }
            }

            Function.REVERSE -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                if (!arg.width.isArrayOrSimple()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NOT_ARRAY.format(ctx.functionExpr(0)?.text)
                    )
                    return
                }
                exprs[ctx] = arg.reverse().asExpr(type)
            }

            Function.FLATTEN -> {
                val valArgs = checkOnlyValues() ?: return
                exprs[ctx] = if (valArgs[0].width.isDefined()) {
                    if (valArgs[0] is UndefinedValue) {
                        UndefinedValue(
                            DefinedSimpleWidth(
                                valArgs[0].width.bitCount ?: error("Failed to get bit count. Should be impossible.")
                            )
                        )
                    } else {
                        valArgs[0].flatten()
                    }
                } else {
                    UndefinedValue()
                }.asExpr(type)
            }

            Function.BUILD -> {
                val valArgs = checkOnlyValues() ?: return
                val value = valArgs[0]
                val dimArgs = valArgs.subList(1, valArgs.size)
                if (!value.width.isSimple()) {
                    context.reportError(ctx.functionExpr(0) ?: ctx, ErrorStrings.BUILD_MULTI_DIM)
                    return
                }
                for (i in 1 until valArgs.size) {
                    if (valArgs[i] !is UndefinedValue && (!valArgs[i].isNumber() || valArgs[i] !is SimpleValue)) {
                        context.reportError(
                            ctx.functionExpr(i) ?: ctx,
                            ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(i)?.text, valArgs[i].toString())
                        )
                        return
                    }
                }
                val dims = dimArgs.mapIndexed { i, it ->
                    if (it is UndefinedValue) {
                        null
                    } else {
                        try {
                            val bigInt = (it as SimpleValue).toBigInt()
                            if (bigInt == null) {
                                context.reportError(
                                    ctx.functionExpr(i + 1) ?: ctx,
                                    "The value \"$it\" is not a number."
                                )
                                return
                            }
                            bigInt.intValueExact()
                        } catch (e: ArithmeticException) {
                            context.reportError(
                                ctx.functionExpr(i + 1) ?: ctx,
                                ErrorStrings.VALUE_BIGGER_THAN_INT.format(ctx.functionExpr(i + 1)?.text)
                            )
                            return
                        }
                    }
                }

                dims.forEachIndexed { i, dim ->
                    if (dim == null)
                        return@forEachIndexed
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

                if (dimArgs.any { it is UndefinedValue }) {
                    var width: SignalWidth = UndefinedSimpleWidth()
                    dims.asReversed().forEach { d ->
                        width = d?.let { DefinedArrayWidth(it, width) } ?: UndefinedArrayWidth(width)
                    }
                    exprs[ctx] = UndefinedValue(width).asExpr(type)
                    return
                }

                @Suppress("UNCHECKED_CAST")
                dims as List<Int>

                val factor = dims.foldRight(1L) { dim, acc -> dim * acc }

                if (value is UndefinedValue) {
                    val bitCount = value.width.bitCount
                    var width: SignalWidth = if (bitCount != null) {
                        if (bitCount % factor != 0L) {
                            context.reportError(
                                ctx.functionExpr(0) ?: ctx,
                                ErrorStrings.ARRAY_NOT_DIVISIBLE.format(ctx.functionExpr(0)?.text)
                            )
                            return
                        }
                        BitListWidth((bitCount / factor).toInt())
                    } else {
                        UndefinedSimpleWidth()
                    }
                    dims.asReversed().forEach { d ->
                        width = DefinedArrayWidth(d, width)
                    }
                    exprs[ctx] = UndefinedValue(width).asExpr(type)
                    return
                }
                value as SimpleValue

                if (value.size % factor != 0L) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.ARRAY_NOT_DIVISIBLE.format(ctx.functionExpr(0)?.text)
                    )
                    return
                }

                fun buildRecursive(bits: List<Bit>, dims: List<Int>): ArrayValue {
                    val d = dims.first()
                    val vCt = bits.size
                    val step = vCt / d
                    val root = mutableListOf<Value>()
                    check(step * d == vCt) { "Dimensions don't split evenly!" }
                    if (dims.size == 1) {
                        repeat(d) {
                            root.add(BitListValue(bits.subList(step * it, step * it + step), false))
                        }
                    } else {
                        repeat(d) {
                            root.add(
                                buildRecursive(
                                    bits.subList(step * it, step * it + step),
                                    dims.subList(1, dims.size)
                                )
                            )
                        }
                    }
                    return ArrayValue(root)
                }

                exprs[ctx] = buildRecursive(value.bits, dims).asExpr(type)
            }

            Function.SIGNED -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                if (!arg.width.isSimple()) {
                    context.reportError(ctx.functionExpr(0) ?: ctx, "Only single dimensional values can use \$signed()")
                }
                when (arg) {
                    is BitValue -> exprs[ctx] = arg.copy(signed = true).asExpr(type)
                    is BitListValue -> exprs[ctx] = arg.copy(signed = true).asExpr(type)
                    is UndefinedValue -> exprs[ctx] = arg.copy().asExpr(type)
                    else -> {}
                }
            }

            Function.UNSIGNED -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                if (!arg.width.isSimple()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "Only single dimensional values can use \$unsigned()"
                    )
                }
                when (arg) {
                    is BitValue -> exprs[ctx] = arg.copy(signed = false).asExpr(type)
                    is BitListValue -> exprs[ctx] = arg.copy(signed = false).asExpr(type)
                    is UndefinedValue -> exprs[ctx] = arg.copy().asExpr(type)
                    else -> {}
                }
            }

            Function.CDIV -> {
                val valArgs = checkOnlyValues() ?: return
                val arg1 = valArgs[0]
                val arg2 = valArgs[1]
                if (!arg1.width.isSimple()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg1.toString())
                    )
                    return
                }
                if (!arg2.width.isSimple()) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(1)?.text, arg2.toString())
                    )
                    return
                }

                if (arg1 is UndefinedValue || arg2 is UndefinedValue) {
                    exprs[ctx] = UndefinedValue().asExpr(type)
                    return
                }

                if (!arg1.isNumber() || arg1 !is SimpleValue) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        ErrorStrings.FUNCTION_ARG_NAN.format(ctx.functionExpr(0)?.text, arg1.toString())
                    )
                    return
                }

                if (!arg2.isNumber() || arg2 !is SimpleValue) {
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
                exprs[ctx] = d1
                    .divide(d2, RoundingMode.HALF_UP)
                    .setScale(0, RoundingMode.CEILING)
                    .toBigInteger()
                    .toBitListValue()
                    .asExpr(type)
            }

            Function.RESIZE -> {
                val valArgs = checkOnlyValues() ?: return
                val value = valArgs[0]
                val size = valArgs[1]
                if (size.isNumber() && size is BitListValue) {
                    val numBits = try {
                        size.toBigInt()!!.intValueExact()
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
                    if (!value.width.isSimple()) {
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
                    exprs[ctx] = when (value) {
                        is SimpleValue -> value.asBitListValue().resize(numBits)
                        is UndefinedValue -> value.copy(width = DefinedSimpleWidth(numBits))
                        else -> error("Previous error checks failed. This shouldn't be reached!")
                    }.asExpr(type)
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
                    val notation = Notation.from(
                        ctx,
                        "Assert failed: \"${ctx.functionExpr(0)?.text}\"",
                        NotationType.Error
                    )
                    context.reportError(ctx, "Assert failed: \"${ctx.functionExpr(0)?.text}\"")
                    context.printError(notation.toString())
                    context.abortTest()
                }
            }

            Function.PRINT -> {
                if (context !is LucidBlockContext)
                    return

                val string = (ctx.functionExpr(0)?.expr() as? ExprNumContext)?.number()?.STRING()?.let {
                    it.text.substring(1, it.text.length - 1)
                }

                if (args.size == 1) {
                    if (string != null) {
                        context.print(string)
                        return
                    }

                    context.print("${ctx.functionExpr(0)?.text} = ${args[0]}")

                    return
                }

                if (string == null) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The first argument to print must be a string when more than one argument is provided."
                    )
                    return
                }

                val regex = Regex("(%d)|(%h)|(%b)|(%\\d*?f)")
                val matches = regex.findAll(string).toList()

                if (matches.size != args.size - 1) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "Unexpected number of arguments given the format string. Expected ${matches.size + 1} but found ${args.size}."
                    )
                    return
                }

                context.print(buildString {
                    var lastIdx = 0
                    matches.forEachIndexed { idx, match ->
                        append(string.subSequence(lastIdx, match.range.first))
                        lastIdx = match.range.endInclusive + 1
                        val value = when (val arg = args[idx + 1]) {
                            is FunctionArg.RealArg -> when {
                                match.groups[1] != null -> { // decimal
                                    arg.value.roundToLong().toString()
                                }

                                match.groups[2] != null -> { // hex
                                    arg.value.roundToLong().toString(16)
                                }

                                match.groups[3] != null -> { // binary
                                    arg.value.roundToLong().toString(2)
                                }

                                match.groups[4] != null -> { // fractional
                                    if (match.groups[4]!!.value.length > 2) {
                                        context.reportWarning(
                                            ctx.functionExpr(idx + 1) ?: ctx,
                                            "The number portion of the format flag %f is ignored for real numbers."
                                        )
                                    }
                                    arg.value.toString()
                                }

                                else -> error("Unknown format!")
                            }

                            is FunctionArg.ValueArg -> {
                                val format = when {
                                    match.groups[1] != null -> ValueFormat.Decimal
                                    match.groups[2] != null -> ValueFormat.Hex
                                    match.groups[3] != null -> ValueFormat.Binary
                                    match.groups[4] != null -> { // fractional
                                        val fractional =
                                            match.groups[4]!!.value.run { substring(1, length - 1) }.toIntOrNull() ?: 0
                                        ValueFormat.Fractional(fractional)
                                    }

                                    else -> error("Unknown format!")
                                }
                                arg.value.toString(format)
                            }
                        }
                        append(value)
                    }
                    append(string.subSequence(lastIdx, string.length))
                })
            }

            is Function.Custom -> {
                if (context !is LucidBlockContext) return
                val valArgs = checkOnlyValues() ?: return

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
    }

    companion object {
        fun getOperator(ctx: ExprContext): String? {
            return ctx.children?.firstOrNull { it is TerminalNode && it.symbol?.type != Tokens.NL.id }?.text
        }
    }
}