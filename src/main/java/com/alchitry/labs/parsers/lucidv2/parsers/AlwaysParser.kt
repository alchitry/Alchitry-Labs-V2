package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.AlwaysBlock
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.minBits

/**
 * The job of the AlwaysParser is to parse out always blocks and check them for errors. The AlwaysEvaluator is
 * responsible for the actual evaluation after the AlwaysParser has done the first pass.
 */
data class AlwaysParser(
    private val context: LucidModuleContext,
    val alwaysBlocks: MutableMap<AlwaysBlockContext, AlwaysBlock> = mutableMapOf()
) : LucidBaseListener() {
    private val dependencies = mutableSetOf<Signal>()
    private val drivenSignals = mutableSetOf<Signal>()
    private val previouslyDrivenSignals = mutableSetOf<Signal>()
    val repeatSignals = mutableMapOf<RepeatStatContext, Signal>()

    suspend fun queueEval() {
        alwaysBlocks.values.forEach {
            context.project.queueEvaluation(it)
        }
    }

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        dependencies.clear()
        drivenSignals.clear()
        repeatSignals.clear()
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        previouslyDrivenSignals.addAll(drivenSignals)
        alwaysBlocks[ctx] =
            AlwaysBlock(context, dependencies.toSet(), drivenSignals.toSet(), repeatSignals.toMap(), ctx)
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        context.expr.resolveDependencies(ctx)?.let { dependencies.addAll(it) }
    }

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.resolve(ctx.signal()) ?: return

        if (!assignee.direction.canWrite) {
            context.reportError(ctx.signal(), "The signal ${ctx.signal().text} can't be written to.")
            return
        }

        if (previouslyDrivenSignals.contains(assignee)) {
            context.reportError(
                ctx.signal(),
                "The signal ${ctx.signal().text} already has a driver so it can't be driven by this always block."
            )
            return
        }

        drivenSignals.add(assignee.getSignal())

        val newValue = context.resolve(ctx.expr()) ?: return

        if (!assignee.width.canAssign(newValue.signalWidth)) {
            context.reportError(
                ctx.expr(),
                "This expression doesn't match the dimensions of signal ${ctx.signal().text}."
            )
            return
        }

        if (assignee.width.getBitCount() < newValue.signalWidth.getBitCount()) {
            context.reportWarning(
                ctx.expr(),
                "This expression is wider than ${ctx.signal().text} and will be truncated."
            )
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
        val sigName = repCtx.name().text

        if (repCtx.name().TYPE_ID() == null) {
            context.reportError(repCtx.name(), "Repeat variable name must start with a lowercase letter.")
            return
        }

        if (context.resolveSignal(sigName) != null) {
            context.reportError(repCtx.name(), "The name $sigName is already in use!")
            return
        }

        val countValue = context.resolve(repCtx.expr()) ?: println("Missing countValue from repeat statement!")

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