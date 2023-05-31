package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.AlwaysBlock
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
    val testBlocks: MutableMap<TestBlockContext, TestBlock> = mutableMapOf()
) : LucidBaseListener() {
    private val dependencies = mutableSetOf<Signal>()
    private val drivenSignals = mutableSetOf<Signal>()
    val repeatSignals = mutableMapOf<RepeatStatContext, Signal>()

    private var inTestBlock = false

    suspend fun queueEval() {
        alwaysBlocks.values.forEach {
            context.project.queueEvaluation(it)
        }
    }

    private fun enterBlock() {
        dependencies.clear()
        drivenSignals.clear()
        repeatSignals.clear()
    }

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        enterBlock()

        if (ctx.firstParentOrNull { it is ModuleContext } == null) {
            context.reportError(ctx, "Always blocks can only be used in modules!")
        }
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlocks[ctx] =
            AlwaysBlock(context, dependencies.toSet(), drivenSignals.toSet(), repeatSignals.toMap(), ctx)
    }

    override fun enterTestBlock(ctx: TestBlockContext) {
        inTestBlock = true
        enterBlock()

        if (ctx.firstParentOrNull { it is TestBenchContext } == null) {
            context.reportError(ctx, "Test blocks can only be used in test benches!")
        }
    }

    override fun exitTestBlock(ctx: TestBlockContext) {
        inTestBlock = false
        val name = ctx.name().text

        if (ctx.name().TYPE_ID() == null) {
            context.reportError(ctx.name(), "Test names must start with a lowercase letter.")
            return
        }

        testBlocks[ctx] =
            TestBlock(name, context, dependencies.toSet(), drivenSignals.toSet(), ctx)
    }

    override fun exitAlwaysFunction(ctx: AlwaysFunctionContext) {
        if (!inTestBlock) {
            context.reportError(ctx, "Functions can only be called stand-alone in test blocks.")
        }
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        context.expr.resolveDependencies(ctx)?.let { dependencies.addAll(it) }
    }

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.resolve(ctx.signal()) ?: return

        if (!assignee.direction.canWrite) {
            context.reportError(ctx.signal(), "The signal \"${ctx.signal().text}\" can't be written to.")
            return
        }

        if (assignee.getSignal().hasDriver) {
            context.reportError(
                ctx.signal(),
                "The signal \"${ctx.signal().text}\" already has a driver so it can't be driven by this block."
            )
            return
        }

        drivenSignals.add(assignee.getSignal())

        val newValue = context.resolve(ctx.expr()) ?: return

        if (!assignee.width.canAssign(newValue.width)) {
            context.reportError(
                ctx.expr(),
                "This expression doesn't match the dimensions of signal \"${ctx.signal().text}\"."
            )
            return
        }

        // only warn about truncation for certain types of expressions
        if (
            ctx.expr() is ExprSignalContext ||
            ctx.expr() is ExprStructContext ||
            ctx.expr() is ExprConcatContext ||
            ctx.expr() is ExprDupContext ||
            ctx.expr() is ExprArrayContext
        ) {
            if (assignee.width.willTruncate(newValue.width)) {
                context.reportWarning(
                    ctx.expr(),
                    "This expression is wider than \"${ctx.signal().text}\" and will be truncated."
                )
            }
        }
    }

    override fun exitCaseStat(ctx: CaseStatContext) {
        val value = context.expr.resolve(ctx.expr()) ?: return

        if (value !is SimpleValue) {
            context.errorCollector.reportError(ctx.expr(), "Case statement's value must be a simple value.")
            return
        }

        ctx.caseElem().forEach { caseElemContext ->
            val exprCtx = caseElemContext.expr() ?: return@forEach
            val condition = context.expr.resolve(exprCtx)
            if (condition !is SimpleValue) {
                context.errorCollector.reportError(ctx.expr(), "Case statement conditions must be simple values.")
                return
            }
            if (!condition.constant) {
                context.errorCollector.reportError(ctx.expr(), "Case statement conditions must be constant.")
                return
            }
        }
    }

    override fun exitIfStat(ctx: IfStatContext) {
        val condition = context.expr.resolve(ctx.expr()) ?: return

        if (condition !is SimpleValue) {
            context.errorCollector.reportError(ctx.expr(), "If condition must be a simple value.")
            return
        }
    }

    override fun enterRepeatBlock(ctx: RepeatBlockContext) {
        val repCtx = ctx.getParent() as RepeatStatContext
        val sigName = repCtx.name()?.text

        val countValue = context.resolve(repCtx.expr())

        if (countValue !is SimpleValue || !countValue.isNumber()) {
            context.errorCollector.reportError(repCtx.expr(), "Repeat count must be a number!")
            return
        }

        if (!countValue.constant) {
            context.errorCollector.reportError(repCtx.expr(), "Repeat count must be constant!")
            return
        }

        val count = try {
            countValue.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportError(repCtx.expr(), "Repeat count doesn't fit in an integer.")
            return
        }

        if (count < 1) {
            context.errorCollector.reportError(repCtx.expr(), "Repeat count must be greater than 0.")
            return
        }

        if (sigName == null)
            return

        if (repCtx.name().TYPE_ID() == null) {
            context.reportError(repCtx.name(), "Repeat variable name must start with a lowercase letter.")
            return
        }

        if (context.resolveSignal(sigName) != null) {
            context.reportError(repCtx.name(), "The name \"$sigName\" is already in use!")
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
        )
    }
}