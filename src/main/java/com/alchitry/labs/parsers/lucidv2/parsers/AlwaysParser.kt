package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.AlwaysBlock
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleWidth
import com.alchitry.labs.parsers.lucidv2.values.minBits

/**
 * The job of the AlwaysParser is to parse out always blocks and check them for errors. The AlwaysEvaluator is
 * responsible for the actual evaluation after the AlwaysParser has done the first pass.
 */
data class AlwaysParser(
    private val context: LucidModuleContext,
    val alwaysBlocks: MutableList<AlwaysBlock> = mutableListOf()
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)

    private val dependencies = mutableSetOf<Signal>()
    private val drivenSignals = mutableSetOf<Signal>()

    suspend fun queueEval() {
        alwaysBlocks.forEach {
            context.project.queueEvaluation(it)
        }
    }

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        dependencies.clear()
        drivenSignals.clear()
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlocks.add(AlwaysBlock(context, dependencies.toList(), drivenSignals.toList(), ctx))
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        context.expr.resolveDependencies(ctx)?.let { dependencies.addAll(it) }
    }

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.signal.resolve(ctx.signal()) ?: return

        if (!assignee.direction.canWrite) {
            context.errorCollector.reportError(ctx.signal(), "The signal ${ctx.signal().text} can't be written to.")
            return
        }
        val newValue = context.expr.resolve(ctx.expr()) ?: return

        if (!assignee.width.canAssign(newValue.signalWidth)) {
            context.errorCollector.reportError(
                ctx.expr(),
                "This expression doesn't match the dimensions of signal ${ctx.signal().text}."
            )
            return
        }

        if (assignee.width.getBitCount() < newValue.signalWidth.getBitCount()) {
            context.errorCollector.reportWarning(
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

    override fun exitRepeatStat(ctx: RepeatStatContext) {
        val signal = context.signal.resolve(ctx.signal()) ?: return
        val countValue = context.expr.resolve(ctx.expr()) ?: return

        if (countValue !is SimpleValue || !countValue.isNumber()) {
            context.errorCollector.reportError(ctx.expr(), "Repeat count must be a number!")
            return
        }

        if (!countValue.constant) {
            context.errorCollector.reportError(ctx.expr(), "Repeat count must be constant!")
            return
        }

        val count = try {
            countValue.toBigInt().intValueExact()
        } catch (e: ArithmeticException) {
            context.errorCollector.reportError(ctx.expr(), "Repeat count doesn't fit in an integer.")
            return
        }

        if (count < 1) {
            context.errorCollector.reportError(ctx.expr(), "Repeat count must be greater than 0.")
            return
        }

        if (signal !is Signal) {
            context.errorCollector.reportError(ctx.signal(), "Repeat signal must be a full signal (no bit selections).")
            return
        }

        val sigWidth = signal.width
        if (sigWidth !is SimpleWidth) {
            context.errorCollector.reportError(ctx.signal(), "Repeat signal must have a simple width.")
            return
        }

        if ((count - 1).toBigInteger().minBits() > sigWidth.size) {
            context.errorCollector.reportError(
                ctx.signal(),
                "The signal isn't wide enough to hold the max value of $count."
            )
            return
        }
    }
}