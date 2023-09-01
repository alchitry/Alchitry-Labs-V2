package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.grammar.LucidParser.*
import com.alchitry.labs.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.AlwaysBlock
import com.alchitry.labs.parsers.lucidv2.types.CustomArg
import com.alchitry.labs.parsers.lucidv2.types.Function
import com.alchitry.labs.parsers.lucidv2.types.TestBlock
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.minBits

/**
 * The job of the BlockParser is to parse out always blocks and check them for errors. The AlwaysEvaluator is
 * responsible for the actual evaluation after the AlwaysParser has done the first pass.
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
    private val localRepeatSignals = mutableMapOf<RepeatStatContext, Signal>()

    private var inTestBlock = false
    private var inFunctionBlock = false

    fun resolveFunction(name: String) = functions[name]

    suspend fun queueEval() {
        alwaysBlocks.values.forEach {
            context.project.queueEvaluation(it)
        }
    }

    private fun enterBlock() {
        dependencies.clear()
        drivenSignals.clear()
        localRepeatSignals.clear()
    }

    override suspend fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        enterBlock()

        if (ctx.firstParentOrNull { it is ModuleContext } == null) {
            context.reportError(ctx, "Always blocks can only be used in modules!")
        }
    }

    override suspend fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlocks[ctx] =
            AlwaysBlock(context, dependencies.toSet(), drivenSignals.toSet(), localRepeatSignals.toMap(), ctx)
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

    override suspend fun enterFunctionBody(ctxBody: FunctionBodyContext) {
        val ctx = ctxBody.parent as FunctionBlockContext
        val nameCtx = ctx.name() ?: return
        if (nameCtx.TYPE_ID() == null) {
            context.reportError(nameCtx, "Function names must start with a lowercase letter.")
            return
        }

        val args = ctx.functionArg().map {
            val itName = it.name() ?: return
            val name = itName.TYPE_ID()?.text
            if (name == null) {
                context.reportError(itName, "Function argument names must start with a lowercase letter.")
                return
            }

            val width = it.signalWidth()?.let { it1 -> context.resolve(it1) } ?: return
            CustomArg(name, width, it.SIGNED() != null)
        }

        val function = Function.Custom(nameCtx.text, args, ctx)

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

        if (assignee.getSignal().hasDriver) {
            context.reportError(
                signalCtx,
                "The signal \"${signalCtx.text}\" already has a driver so it can't be driven by this block."
            )
            return
        }

        drivenSignals.add(assignee.getSignal())

        val exprCtx = ctx.expr() ?: return
        val newValue = context.resolve(exprCtx) ?: return

        if (!assignee.width.canAssign(newValue.width)) {
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
            if (assignee.width.willTruncate(newValue.width)) {
                context.reportWarning(
                    exprCtx,
                    "The expression \"${exprCtx.text}\" is wider than \"${signalCtx.text}\" and will be truncated."
                )
            }
        }
    }

    override suspend fun exitCaseStat(ctx: CaseStatContext) {
        val exprCtx = ctx.expr() ?: return
        val value = context.expr.resolve(exprCtx) ?: return

        if (value !is SimpleValue) {
            context.errorCollector.reportError(exprCtx, "Case statement's value must be a simple value.")
            return
        }

        ctx.caseElem().forEach { caseElemContext ->
            val exprCtx = caseElemContext.expr() ?: return@forEach
            val condition = context.expr.resolve(exprCtx)
            if (condition !is SimpleValue) {
                context.errorCollector.reportError(exprCtx, "Case statement conditions must be simple values.")
                return
            }
            if (!condition.constant) {
                context.errorCollector.reportError(exprCtx, "Case statement conditions must be constant.")
                return
            }
        }
    }

    override suspend fun exitIfStat(ctx: IfStatContext) {
        val exprCtx = ctx.expr() ?: return
        val condition = context.expr.resolve(exprCtx) ?: return

        if (condition !is SimpleValue) {
            context.errorCollector.reportError(exprCtx, "If condition must be a simple value.")
            return
        }
    }

    override suspend fun enterRepeatBlock(ctx: RepeatBlockContext) {
        val repCtx = ctx.parent as RepeatStatContext
        val sigName = repCtx.name()?.text
        val exprCtx = repCtx.expr() ?: return

        val countValue = context.resolve(exprCtx)

        if (countValue !is SimpleValue || !countValue.isNumber()) {
            context.errorCollector.reportError(exprCtx, "Repeat count must be a number!")
            return
        }

        val count = try {
            countValue.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportError(exprCtx, "Repeat count doesn't fit in an integer.")
            return
        }

        if (!inFunctionBlock && !inTestBlock) {
            if (count < 1) {
                context.errorCollector.reportError(exprCtx, "Repeat count must be greater than 0.")
                return
            }

            if (!countValue.constant) {
                context.errorCollector.reportError(exprCtx, "Repeat count must be constant!")
                return
            }
        }

        if (sigName == null)
            return

        val nameCtx = repCtx.name() ?: return

        if (nameCtx.TYPE_ID() == null) {
            context.reportError(nameCtx, "Repeat variable name must start with a lowercase letter.")
            return
        }

        if (context.resolveSignal(sigName) != null) {
            context.reportError(nameCtx, "The name \"$sigName\" is already in use!")
            return
        }

        val sigWidth = (count - 1).toBigInteger().minBits()

        repeatSignals[repCtx] = Signal(
            sigName,
            SignalDirection.Read,
            null,
            BitListValue(
                0, sigWidth,
                constant = false,
                signed = false
            )
        ).also { localRepeatSignals[repCtx] = it }
    }
}