package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.types.Function
import com.alchitry.labs2.parsers.lucidv2.values.*

/**
 * The job of the BlockParser is to parse out always blocks and check them for errors.
 * The BlockEvaluator is responsible for the actual evaluation after the BlockParser has done the first pass.
 */
data class BlockParser(
    private val context: LucidBlockContext,
    val alwaysBlocks: MutableMap<AlwaysBlockContext, AlwaysBlock> = mutableMapOf(),
    val testBlocks: MutableMap<TestBlockContext, TestBlock> = mutableMapOf(),
    val repeatSignals: MutableMap<RepeatStatContext, Signal> = mutableMapOf(),
    val functions: MutableMap<String, Function> = mutableMapOf()
) : SuspendLucidBaseListener() {
    private val dependencies = mutableSetOf<Signal>()
    private val drivenSignals = mutableSetOf<Signal>()
    private val repeatBlocks = mutableMapOf<RepeatStatContext, RepeatBlock>()

    private var inTestBlock = false
    private var inFunctionBlock = false

    fun resolveFunction(name: String) = functions[name]
    fun resolveRepeatBlock(block: RepeatStatContext) = repeatBlocks[block]

    fun queueEval() {
        alwaysBlocks.values.forEach {
            context.project.queue(it)
        }
    }

    private fun enterBlock() {
        dependencies.clear()
        drivenSignals.clear()
    }

    override suspend fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        enterBlock()

        if (ctx.firstParentOrNull { it is ModuleContext } == null) {
            context.reportError(ctx, "Always blocks can only be used in modules!")
        }
    }

    override suspend fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlocks[ctx] =
            AlwaysBlock(context, dependencies.toSet(), drivenSignals.toSet(), ctx)
    }

    override suspend fun enterTestBlock(ctx: TestBlockContext) {
        inTestBlock = true
        enterBlock()

        if (ctx.firstParentOrNull { it is TestBenchContext } == null) {
            context.reportError(ctx, "Test blocks can only be used in test benches!")
        }
    }

    override suspend fun exitTestBlock(ctx: TestBlockContext) {
        inTestBlock = false
        val nameCtx = ctx.name() ?: return
        val name = nameCtx.text

        if (nameCtx.TYPE_ID() == null) {
            context.reportError(nameCtx, "Test names must start with a lowercase letter.")
            return
        }

        if (testBlocks.values.any { it.name == name }) {
            context.reportError(nameCtx, "Test name \"$name\" is already in use.")
            return
        }

        testBlocks[ctx] =
            TestBlock(name, context, dependencies.toSet(), drivenSignals.toSet(), ctx)
    }

    override suspend fun enterFunctionBlock(ctx: FunctionBlockContext) {
        inFunctionBlock = true
        enterBlock()

        if (ctx.firstParentOrNull { it is TestBenchContext } == null) {
            context.reportError(ctx, "Test blocks can only be used in test benches!")
        }
    }

    override suspend fun enterFunctionBody(ctx: FunctionBodyContext) {
        val functionCtx = ctx.parent as FunctionBlockContext
        val nameCtx = functionCtx.name() ?: return
        if (nameCtx.TYPE_ID() == null) {
            context.reportError(nameCtx, "Function names must start with a lowercase letter.")
            return
        }

        val args = functionCtx.functionArg().map {
            val itName = it.name() ?: return
            val name = itName.TYPE_ID()?.text
            if (name == null) {
                context.reportError(itName, "Function argument names must start with a lowercase letter.")
                return
            }

            val width = it.signalWidth()?.let { it1 -> context.resolve(it1) } ?: return
            CustomArg(name, width, it.SIGNED() != null)
        }

        val function = Function.Custom(nameCtx.text, args, functionCtx)

        if (
            Function.builtIn.any { it.label == function.label } ||
            functions.putIfAbsent(function.label, function) != null
        ) {
            context.reportError(nameCtx, "The function name \"${function.label}\" is already in use.")
            return
        }
    }

    override suspend fun exitFunctionBlock(ctx: FunctionBlockContext) {
        inFunctionBlock = false
    }

    override suspend fun exitAlwaysFunction(ctx: AlwaysFunctionContext) {
        if (!inTestBlock && !inFunctionBlock) {
            context.reportError(ctx, "Functions can only be called in test or function blocks.")
        }
    }

    override suspend fun exitExprSignal(ctx: ExprSignalContext) {
        context.expr.resolveDependencies(ctx)?.let { dependencies.addAll(it) }
    }

    override suspend fun exitAssignStat(ctx: AssignStatContext) {
        val signalCtx = ctx.signal() ?: return
        val assignee = context.resolve(signalCtx) ?: return

        if (!assignee.direction.canWrite) {
            context.reportError(signalCtx, "The signal \"${signalCtx.text}\" can't be written to.")
            return
        }

        if (!inFunctionBlock && !inTestBlock && assignee.getSignal().hasDriver) {
            context.reportError(
                signalCtx,
                "The signal \"${signalCtx.text}\" already has a driver so it can't be driven by this block."
            )
            return
        }

        if (assignee.getSignal().parent !is LocalSignal) {
            drivenSignals.add(assignee.getSignal())
        }

        val exprCtx = ctx.expr() ?: return
        val newValue = context.resolve(exprCtx) ?: return

        if (!assignee.width.canAssign(newValue.value.width)) {
            context.reportError(
                exprCtx,
                "The expression \"${exprCtx.text}\" doesn't match the dimensions of signal \"${signalCtx.text}\"."
            )
            return
        }

        // only warn about truncation for certain types of expressions
        if (
            exprCtx is ExprSignalContext ||
            exprCtx is ExprStructContext ||
            exprCtx is ExprConcatContext ||
            exprCtx is ExprDupContext ||
            exprCtx is ExprArrayContext ||
            exprCtx is ExprNumContext
        ) {
            if (assignee.width.willTruncate(newValue.value.width)) {
                context.reportWarning(
                    exprCtx,
                    "The expression \"${exprCtx.text}\" is wider than \"${signalCtx.text}\" and will be truncated."
                )
            }
        }

        context.expr.setAssignWidth(ctx, assignee.width)

        assignee.write(newValue.value)
    }

    override suspend fun exitCaseStat(ctx: CaseStatContext) {
        val exprCtx = ctx.expr() ?: return
        val value = context.expr.resolve(exprCtx) ?: return

        if (value.value !is SimpleValue) {
            context.notationCollector.reportError(exprCtx, "Case statement's value must be a simple value.")
            return
        }

        ctx.caseElem().forEach { caseElemContext ->
            val caseExprCtx = caseElemContext.expr() ?: return@forEach
            val condition = context.expr.resolve(caseExprCtx)
            if (condition?.value !is SimpleValue) {
                context.notationCollector.reportError(caseExprCtx, "Case statement conditions must be simple values.")
                return
            }
            if (condition.type != ExprType.Constant) {
                context.notationCollector.reportError(caseExprCtx, "Case statement conditions must be constant.")
                return
            }
        }
    }

    override suspend fun exitIfStat(ctx: IfStatContext) {
        val exprCtx = ctx.expr() ?: return
        val condition = context.expr.resolve(exprCtx) ?: return

        if (condition.value !is SimpleValue && condition.value !is UndefinedValue) {
            context.notationCollector.reportError(exprCtx, "If condition must be a simple value.")
            return
        }
    }

    override suspend fun enterRepeatBlock(ctx: RepeatBlockContext) {
        val repCtx = ctx.parent as RepeatStatContext

        val allExprCtx = repCtx.expr()

        if (!(1..4).contains(allExprCtx.size)) {
            context.notationCollector.reportError(
                repCtx,
                "Repeat requires 1-4 arguments, ${allExprCtx.size} were given."
            )
            return
        }

        val first = allExprCtx.first()

        val firstUnresolved = context.resolve(first) == null

        val sigName =
            if (firstUnresolved && first is ExprSignalContext && first.signal()?.name()?.size == 1 && first.signal()
                    ?.bitSelection()?.size == 0
            ) {
                first.text
            } else null

        if (firstUnresolved && sigName == null) {
            context.reportError(first, "Failed to determine if this is a number or loop variable name.")
            return
        }

        val exprCtx = allExprCtx.subList(if (sigName != null) 1 else 0, allExprCtx.size)
        val exprList = exprCtx.map { context.resolve(it) }

        val hiddenSignalName = sigName ?: "r_${ctx.hashCode()}" // create signal for verilog conversion
        val repeatBlock = RepeatBlock(
            context,
            hiddenSignalName,
            sigName == null,
            exprCtx[0],
            exprCtx.getOrNull(1),
            exprCtx.getOrNull(2),
            repCtx
        )

        if (exprList.isEmpty()) {
            context.notationCollector.reportError(repCtx, "Repeat count was missing.")
            return
        }

        val countValue = exprList[0]

        if (countValue?.value?.width !is SimpleWidth) {
            context.notationCollector.reportError(exprCtx[0], "Repeat count must be a number!")
            return
        }

        val count = try {
            (countValue.value as? SimpleValue)?.toBigInt()?.intValueExact() ?: 0
        } catch (e: ArithmeticException) {
            context.notationCollector.reportError(exprCtx[0], "Repeat count doesn't fit in an integer.")
            return
        }

        val dependencies = context.expr.resolveDependencies(exprCtx[0])
        val known = countValue.type.known || dependencies?.all { it.type.known } != false

        if (!inFunctionBlock && !inTestBlock) {
            if (count < 0) {
                context.notationCollector.reportError(exprCtx[0], "Repeat count must be greater than or equal to 0.")
                return
            }

            if (!known) {
                context.notationCollector.reportError(exprCtx[0], "Repeat count must be constant!")
                return
            }

            if (repeatBlock.step == 0) {
                context.reportError(exprCtx[2], "Repeat step size can not be 0!")
                return
            }
        }

        if (sigName != null) {
            val nameCtx = (allExprCtx[0] as? ExprSignalContext)?.signal()?.name(0) ?: return

            if (nameCtx.TYPE_ID() == null) {
                context.reportError(nameCtx, "Repeat variable name must start with a lowercase letter.")
                return
            }

            if (context.resolveSignal(ctx, sigName) != null) {
                context.reportError(nameCtx, "The name \"$sigName\" is already in use!")
                return
            }
        }

        repeatSignals[repCtx] = repeatBlock.createSignal()
        repeatBlocks[repCtx] = repeatBlock
    }
}

data class RepeatBlock(
    val context: LucidBlockContext,
    val signal: String,
    val signal_hidden: Boolean,
    val countExprCtx: ExprContext,
    val startExprCtx: ExprContext?,
    val stepExprCtx: ExprContext?,
    val repeatStatCtx: RepeatStatContext
) {
    private val countExpr: Expr? get() = context.resolve(countExprCtx)
    private val startExpr: Expr? get() = startExprCtx?.let { context.resolve(it) }
    private val stepExpr: Expr? get() = stepExprCtx?.let { context.resolve(it) }

    val count: Int get() = (countExpr?.value as? SimpleValue)?.toBigInt()?.intValueExact() ?: 0
    val start: Int get() = (startExpr?.value as? SimpleValue)?.toBigInt()?.intValueExact() ?: 0
    val step: Int get() = (stepExpr?.value as? SimpleValue)?.toBigInt()?.intValueExact() ?: 1

    inline fun forEach(block: (Int) -> Unit) {
        var idx = start
        repeat(count) {
            block(idx)
            idx += step
        }
    }

    fun createSignal(): Signal {
        return RepeatSignal(signal, BitListValue(start, minBits, false), repeatStatCtx).signal
    }

    val minBits: Int
        get() = start.toBigInteger().minBits().coerceAtLeast((start + step * (count - 1)).toBigInteger().minBits())
}
