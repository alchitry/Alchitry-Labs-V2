package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs2.parsers.hdl.*
import com.alchitry.labs2.parsers.hdl.lucid.context.ContextState
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprContext
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.Function
import com.alchitry.labs2.parsers.hdl.values.*
import com.alchitry.labs2.parsers.notations.ErrorStrings
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationType
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
class ExprParser(
    private val context: LucidExprContext,
    evaluator: ExprEvaluator<ExprContext>? = null
) : SuspendLucidBaseListener() {
    private val evaluator: ExprEvaluator<ExprContext> = evaluator?.withContext(context) ?: ExprEvaluator(context)
    val functions = mutableMapOf<FunctionContext, Function>()
    private var inTestBlock = false
    private var inFunctionBlock = false

    fun withContext(context: LucidExprContext) = ExprParser(context, evaluator)

    fun resolve(ctx: ExprContext): Expr? = evaluator.resolve(ctx)
    fun resolveDependencies(ctx: ExprContext): Set<Signal>? = evaluator.resolveDependencies(ctx)
    fun setAssignWidth(ctx: ParseTree, width: SignalWidth) = evaluator.setAssignWidth(ctx, width)
    fun getContextState(ctx: RuleContext): ContextState = evaluator.getContextState(ctx)

    override suspend fun enterBlock(ctx: BlockContext) {
        when (val parent = ctx.parent) {
            is RepeatBlockContext -> {
                val exprContexts = (parent.parent as? RepeatStatContext)?.expr() ?: return
                val expr = context.resolve(
                    if (exprContexts.size > 1) exprContexts[1] else exprContexts.firstOrNull() ?: return
                ) ?: return
                evaluator.setBlockState(ctx, expr.value.isEqualTo(Bit.B0.toBitValue()).bit == Bit.B1, expr.type.known)
            }

            is IfStatContext -> {
                val expr = context.resolve(parent.expr() ?: return) ?: return
                evaluator.setBlockState(ctx, expr.value.isTrue().invert().bit == Bit.B1, expr.type.known)
            }

            is ElseStatContext -> {
                val expr = context.resolve((parent.parent as? IfStatContext)?.expr() ?: return) ?: return
                evaluator.setBlockState(ctx, expr.value.isTrue().bit == Bit.B1, expr.type.known)
            }
        }
    }

    override suspend fun enterCaseBlock(ctx: CaseBlockContext) {
        val parent = ctx.parent as CaseElemContext
        val caseExprCtx = parent.expr()
        val expr = context.resolve((parent.parent as CaseStatContext).expr() ?: return) ?: return

        if (caseExprCtx != null) {
            val caseExpr = context.resolve(caseExprCtx) ?: return
            evaluator.setBlockState(ctx, expr.value.isEqualTo(caseExpr.value).invert().bit == Bit.B1, expr.type.known)
            return
        }

        // default case
        val cases = (parent.parent as CaseStatContext).caseElem()
        val index = cases.indexOf(parent)
        if (index < 0) error("Case isn't inside its parent!")
        evaluator.setBlockState(
            ctx,
            cases.subList(0, index).any { evaluator.getContextState(it) == ContextState.ACTIVE },
            expr.type.known
        )
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
        evaluator.copyDependencies(ctx, ctx.expr())
    }

    override suspend fun exitBitSelectorConst(ctx: BitSelectorConstContext) {
        evaluator.copyDependencies(ctx, ctx.expr())
    }

    override suspend fun exitArrayIndex(ctx: ArrayIndexContext) {
        evaluator.copyDependencies(ctx, ctx.expr())
    }

    override suspend fun exitNumber(ctx: NumberContext) {
        ctx.skip = true
        if (evaluator.canSkip(ctx)) return
        evaluator.setDependencies(ctx, emptySet())

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
                valueString = str.substring(1, str.length - 1).reversed()

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
                evaluator.setExpr(ctx, value.asConstExpr())
                return
            }
        } else {
            valueString = split[1]
        }

        val cleanValueString = valueString.replace("_", "")

        if (cleanValueString.isBlank())
            return

        val unbound = BitListValue(cleanValueString, radix, signed = false)
        val value = if (width != null) {
            BitListValue(cleanValueString, radix, width = width, signed = false)
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
            evaluator.setExpr(ctx, value.getBit(0).asConstExpr()) // return BitValue instead of BitListValue
        else
            evaluator.setExpr(ctx, value.asConstExpr())
    }

    override suspend fun exitParamConstraint(ctx: ParamConstraintContext) {
        if (evaluator.canSkip(ctx)) return
        evaluator.copyExpr(ctx, ctx.expr())
        evaluator.copyDependencies(ctx, ctx.expr())
    }

    override suspend fun exitStructConst(ctx: StructConstContext) {
        ctx.skip = true
        if (evaluator.canSkip(ctx)) return

        evaluator.setWidthFence(ctx)

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

            val value = memberCtx.expr()?.let { evaluator.resolve(it)?.value } ?: return

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

        evaluator.setExpr(ctx, StructValue(type, members).asConstExpr())
    }

    override suspend fun exitBitSelection(ctx: BitSelectionContext) {
        evaluator.setWidthFence(ctx)
        evaluator.copyDependencies(ctx, ctx.children)
    }

    override suspend fun exitRepeatStat(ctx: RepeatStatContext) {
        evaluator.setWidthFence(ctx)
    }

    override suspend fun exitExprSignal(ctx: ExprSignalContext) {
        //if (canSkip(ctx)) return
        val signalCtx = ctx.signal() ?: return

        val signal = context.resolve(signalCtx) ?: return

        val finalWidth = evaluator.getFinalWidth(ctx)

        if (!signal.direction.canRead) {
            val functionCtx = ctx.firstParentOrNull { it is FunctionContext }
            val sigCtx = ctx.firstParentOrNull { it is SigConContext }
            // exclude reads of the WIDTH function as this doesn't read the signal
            if ((functionCtx !is FunctionContext || functionCtx.FUNCTION_ID()?.text != "$" + Function.WIDTH.label) && sigCtx !is SigConContext) {
                context.reportError(signalCtx, "The signal \"${signalCtx.text}\" can't be read!")
                return
            }
        }

        signal.getSignal().markRead()

        // check the bit selectors to see if they are constant or not
        val bitSelectionType = evaluator.getExprType(signalCtx.bitSelection().flatMap { bitSelection ->
            context.resolve(bitSelection).flatMap {
                when (it.selectionCtx) {
                    is SelectionContext.Constant -> emptyList()
                    is SelectionContext.DownTo -> listOf(it.selectionCtx.stop)
                    is SelectionContext.Fixed -> listOf(it.selectionCtx.start, it.selectionCtx.stop)
                    is SelectionContext.Single -> listOf(it.selectionCtx.bit)
                    is SelectionContext.UpTo -> listOf(it.selectionCtx.start)
                }
            }
        }.mapNotNull { evaluator.resolve(it)?.type })

        val type = evaluator.getExprType(listOf(signal.type, bitSelectionType))

        val value = if (context.mode == ExprEvalMode.Building && signal.type == ExprType.Fixed) {
            // outermost dimension is undefined for parameters
            val newWidth = when (val width = signal.width) {
                is SimpleWidth -> UndefinedSimpleWidth()
                is ArrayWidth -> UndefinedArrayWidth(width.next)
                else -> width
            }
            UndefinedValue(newWidth)
        } else {
            signal.read(context.evalContext)
        }
        val resizedValue = if (finalWidth != null) value.resizeToMatch(finalWidth) else value

        evaluator.setExpr(ctx, Expr(resizedValue, type))

        // add dependencies for all signals used in bit selection as well as this signal
        if (evaluator.resolveDependencies(ctx) == null) {
            val parentSig = when (signal) {
                is Signal -> signal
                is SubSignal -> signal.parent
            }

            evaluator.setDependencies(ctx, mutableSetOf(parentSig).apply {
                signalCtx.bitSelection().forEach { child ->
                    evaluator.resolveDependencies(child)?.let { addAll(it) }
                }
            })
        }
    }

    override suspend fun exitExprStruct(ctx: ExprStructContext) = evaluator.passThrough(ctx, ctx.structConst())
    override suspend fun exitExprFunction(ctx: ExprFunctionContext) = evaluator.passThrough(ctx, ctx.function())
    override suspend fun exitExprNum(ctx: ExprNumContext) = evaluator.passThrough(ctx, ctx.number())
    override suspend fun exitExprGroup(ctx: ExprGroupContext) = evaluator.passThrough(ctx, ctx.expr())
    override suspend fun exitExprConcat(ctx: ExprConcatContext) = evaluator.concatenate(ctx, ctx.expr())
    override suspend fun exitExprDup(ctx: ExprDupContext) = evaluator.duplicate(ctx, ctx.expr())
    override suspend fun exitExprArray(ctx: ExprArrayContext) = evaluator.buildArray(ctx, ctx.expr())
    override suspend fun exitExprNegate(ctx: ExprNegateContext) = evaluator.negate(ctx, ctx.expr())

    override suspend fun exitExprInvert(ctx: ExprInvertContext) {
        evaluator.invert(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprAddSub(ctx: ExprAddSubContext) {
        evaluator.addOrSubtract(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprMultDiv(ctx: ExprMultDivContext) {
        evaluator.multiplyOrDivide(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprShift(ctx: ExprShiftContext) {
        evaluator.shift(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprBitwise(ctx: ExprBitwiseContext) {
        evaluator.bitwise(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprReduction(ctx: ExprReductionContext) {
        evaluator.reduction(ctx, ctx.expr(), ctx.getChild(0)?.text ?: return)
    }

    override suspend fun exitExprCompare(ctx: ExprCompareContext) {
        evaluator.comparison(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprLogical(ctx: ExprLogicalContext) {
        evaluator.logical(ctx, ctx.expr(), getOperator(ctx) ?: return)
    }

    override suspend fun exitExprTernary(ctx: ExprTernaryContext) = evaluator.ternary(ctx, ctx.expr())

    override suspend fun exitFunction(ctx: FunctionContext) {
        if (evaluator.canSkip(ctx)) return

        evaluator.setWidthFence(ctx)

        evaluator.setDependencies(ctx, mutableSetOf<Signal>().apply {
            ctx.functionExpr()
                .forEach { c -> c.expr()?.let { expr -> evaluator.resolveDependencies(expr)?.let { addAll(it) } } }
        })

        val type = evaluator.getExprType(
            ctx.functionExpr().map { c -> c.expr()?.let { evaluator.resolve(it)?.type } ?: ExprType.Constant })

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
                evaluator.setExpr(ctx, sig.width.toValue().asConstExpr())
                return
            }
        }

        val args = functionExprCtxs.map { expr ->
            val exprExpr = expr.expr()
            if (exprExpr != null) {
                FunctionArg.ValueArg(evaluator.resolve(exprExpr)?.value ?: return)
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
            Function.IS_SIMULATION -> {
                val bit = if (context.project.simulating) Bit.B1 else Bit.B0
                evaluator.setExpr(ctx, bit.toBitValue().asConstExpr())
            }

            Function.WIDTH -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                if (valArgs.size > 2) {
                    context.reportError(ctx.functionExpr(2) ?: ctx, "\$width accepts at most two arguments.")
                    return
                }
                val dimArg = valArgs.getOrNull(1)
                if (dimArg != null) {
                    val dimType = evaluator.resolve(functionExprCtxs[1])?.type
                        ?: error("Failed to get dimension argument type even though it should exist!")
                    if (dimType != ExprType.Constant) {
                        context.reportError(
                            ctx.functionExpr(1) ?: ctx,
                            "The dimension argument must be a fixed constant."
                        )
                        return
                    }
                }
                val width = arg.width
                if (!width.isSimpleArray()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The function \"$${Function.WIDTH.label}()\" can't be used on structs."
                    )
                    return
                }
                val widthType = if (width.isDefined()) ExprType.Constant else ExprType.Fixed
                val widthValue = width.toValue()
                if (dimArg == null && widthValue is ArrayValue) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The value \"${ctx.functionExpr(0)?.text}\" has a multidimensional width but a dimension was not specified."
                    )
                    return
                }
                val value = if (dimArg != null) {
                    if (!dimArg.isNumber()) {
                        context.reportError(ctx.functionExpr(1) ?: ctx, "The dimension argument must be a number.")
                        return
                    }
                    val dimInt = (dimArg as SimpleValue).toBigInt()!!.toInt()

                    if (widthValue !is ArrayValue) {
                        if (dimInt != 0) {
                            context.reportError(
                                ctx.functionExpr(1) ?: ctx,
                                "The dimension index \"$dimInt\" is outside the range for \"${ctx.functionExpr(0)?.text}\"."
                            )
                            return
                        }
                        widthValue
                    } else {
                        widthValue.elements.getOrElse(dimInt) {
                            context.reportError(
                                ctx.functionExpr(1) ?: ctx,
                                "The dimension index \"$dimInt\" is outside the range for \"${ctx.functionExpr(0)?.text}\"."
                            )
                            return
                        }
                    }
                } else widthValue

                evaluator.setExpr(ctx, value.asExpr(widthType))
            }

            Function.FIXEDPOINT, Function.CFIXEDPOINT, Function.FFIXEDPOINT -> {
                if (!(evaluator.resolve(ctx.functionExpr(1)?.expr() ?: return)?.type ?: return).fixed) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx,
                        "The width (second) argument of \"\$${function.label}()\" must be constant."
                    )
                }
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
                            evaluator.setExpr(ctx, UndefinedValue(UndefinedSimpleWidth()).asConstExpr())
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
                            evaluator.setExpr(ctx, UndefinedValue(BitListWidth(width)).asConstExpr())
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
                            evaluator.setExpr(ctx, UndefinedValue(BitListWidth(width)).asConstExpr())
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
                    Function.FIXEDPOINT -> if (fError < cError) fValue else cValue
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

                evaluator.setExpr(ctx, BitListValue(bigInt, width = width).asExpr(type))
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
                    evaluator.setExpr(ctx, UndefinedValue(UndefinedSimpleWidth()).asExpr(type))
                    return
                }
                if (!arg.isNumber()) {
                    context.reportError(
                        ctx.functionExpr(0) ?: ctx,
                        "The argument \"${ctx.functionExpr(0)?.text}\" with value \"$arg\" must be a number."
                    )
                    return
                }
                val clog2 = Function.CLOG2.compute(arg)
                if (clog2 == null) {
                    context.reportError(ctx, "Failed to compute clog2 for value: $arg")
                    return
                }
                evaluator.setExpr(ctx, clog2.asExpr(type))
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
                    evaluator.setExpr(ctx, UndefinedValue(UndefinedSimpleWidth()).asExpr(type))
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
                    evaluator.setExpr(ctx, b1.pow(b2.intValueExact()).toBitListValue().asExpr(type))
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

                if (type != ExprType.Constant) {
                    if (ctx.functionExpr(0)?.expr() !is ExprSignalContext) {
                        context.reportError(
                            ctx.functionExpr(0) ?: ctx,
                            "\$reverse() can only be used on constant expressions or signals."
                        )
                        return
                    }
                }

                evaluator.setExpr(ctx, arg.reverse().asExpr(type))
            }

            Function.FLATTEN -> {
                val valArgs = checkOnlyValues() ?: return
                evaluator.setExpr(
                    ctx, if (valArgs[0].width.isDefined()) {
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
                        UndefinedValue(UndefinedSimpleWidth())
                    }.asExpr(type)
                )
            }

            Function.BUILD -> {
                val valArgs = checkOnlyValues() ?: return
                val value = valArgs[0]
                val dimArgs = valArgs.subList(1, valArgs.size)
                if (!value.width.isSimple()) {
                    context.reportError(ctx.functionExpr(0) ?: ctx, "Only single dimensional arrays can be built")
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
                    evaluator.setExpr(ctx, UndefinedValue(width).asExpr(type))
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
                    evaluator.setExpr(ctx, UndefinedValue(width).asExpr(type))
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

                evaluator.setExpr(ctx, buildRecursive(value.bits, dims).asExpr(type))
            }

            Function.SIGNED -> {
                val valArgs = checkOnlyValues() ?: return
                val arg = valArgs[0]
                if (!arg.width.isSimple()) {
                    context.reportError(ctx.functionExpr(0) ?: ctx, "Only single dimensional values can use \$signed()")
                }
                when (arg) {
                    is BitValue -> evaluator.setExpr(ctx, arg.copy(signed = true).asExpr(type))
                    is BitListValue -> evaluator.setExpr(ctx, arg.copy(signed = true).asExpr(type))
                    is UndefinedValue -> evaluator.setExpr(ctx, arg.copy().asExpr(type))
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
                    is BitValue -> evaluator.setExpr(ctx, arg.copy(signed = false).asExpr(type))
                    is BitListValue -> evaluator.setExpr(ctx, arg.copy(signed = false).asExpr(type))
                    is UndefinedValue -> evaluator.setExpr(ctx, arg.copy().asExpr(type))
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
                    evaluator.setExpr(ctx, UndefinedValue(UndefinedSimpleWidth()).asExpr(type))
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
                evaluator.setExpr(
                    ctx, d1
                        .divide(d2, RoundingMode.HALF_UP)
                        .setScale(0, RoundingMode.CEILING)
                        .toBigInteger()
                        .toBitListValue()
                        .asExpr(type)
                )
            }

            Function.RESIZE -> {
                val valArgs = checkOnlyValues() ?: return
                val value = valArgs[0]
                val size = valArgs[1]
                if (evaluator.resolve(ctx.functionExpr(1)?.expr() ?: return)?.type?.fixed == false) {
                    context.reportError(
                        ctx.functionExpr(1) ?: ctx, "The size (second) argument of \$resize() must be a constant."
                    )
                }
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
                    evaluator.setExpr(
                        ctx, when (value) {
                            is SimpleValue -> value.asBitListValue().resize(numBits)
                            is UndefinedValue -> value.copy(width = DefinedSimpleWidth(numBits))
                            else -> error("Previous error checks failed. This shouldn't be reached!")
                        }.asExpr(type)
                    )
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