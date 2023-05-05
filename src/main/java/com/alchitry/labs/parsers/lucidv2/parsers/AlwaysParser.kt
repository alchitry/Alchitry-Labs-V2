package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.Signal

data class AlwaysParser(
    private val context: LucidModuleContext,
    private val writtenSignals: MutableSet<Signal> = mutableSetOf()
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)

    override fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {
        writtenSignals.clear()
    }

    suspend fun processWriteQueue() {
        writtenSignals.forEach { it.publish() }
        writtenSignals.clear()
    }

    override fun exitAssignStat(ctx: LucidParser.AssignStatContext) {
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
}