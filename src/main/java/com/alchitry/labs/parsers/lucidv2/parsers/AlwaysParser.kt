package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.AlwaysBlock
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.minBits

data class AlwaysParser(
    private val context: LucidModuleContext
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)

    private val writtenSignals = mutableSetOf<Signal>()
    private val dependencies = mutableSetOf<Signal>()

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        writtenSignals.clear()
        dependencies.clear()
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        if (!context.isEvaluating) {
            AlwaysBlock(context, dependencies.toList(), ctx)
        }
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        context.expr.resolveDependencies(ctx)?.let { dependencies.addAll(it) }
    }

    suspend fun processWriteQueue() {
        writtenSignals.forEach { it.publish() }
        writtenSignals.clear()
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
        // TODO: Warn about truncation

        assignee.quietSet(newValue, context.evalContext)

        writtenSignals.add(assignee.getSignal())
    }

    override fun exitCaseStat(ctx: CaseStatContext?) {
        // TODO
    }

    override fun exitIfStat(ctx: IfStatContext) {
        // TODO
    }

    override fun enterRepeatStat(ctx: RepeatStatContext) {
        if (context.isEvaluating) {
            val signal = context.signal.resolve(ctx.signal()) ?: return
            if (signal !is SimpleValue) return
            signal.quietSet(
                BitListValue(
                    value = 0,
                    width = signal.bits.size,
                    constant = false,
                    signed = false
                ), context.evalContext
            )
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

        if (signal !is SimpleValue) {
            context.errorCollector.reportError(ctx.signal(), "Repeat signal must be a simple value.")
            return
        }

        val sigBitCount = signal.bits.size

        if ((count - 1).toBigInteger().minBits() > sigBitCount) {
            context.errorCollector.reportError(
                ctx.signal(),
                "The signal isn't wide enough to hold the max value of $count."
            )
            return
        }

        if (context.isEvaluating)
            repeat(count - 1) {
                signal.quietSet(
                    BitListValue(
                        value = it + 1,
                        width = signal.bits.size,
                        constant = false,
                        signed = false
                    ),
                    context.evalContext
                )
                context.walk(ctx.block())
            }
    }
}