package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.AlwaysBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.AlwaysBlock
import com.alchitry.labs.parsers.lucidv2.signals.Signal

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

    override fun exitExprSignal(ctx: LucidParser.ExprSignalContext) {
        context.expr.resolveDependencies(ctx)?.let {dependencies.addAll(it) }
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