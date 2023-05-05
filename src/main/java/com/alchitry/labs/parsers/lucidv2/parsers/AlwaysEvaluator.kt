package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue

data class AlwaysEvaluator(
    private val context: LucidModuleContext
) : LucidBaseListener() {
    fun withContext(context: LucidModuleContext) = copy(context = context)

    private val writtenSignals = mutableSetOf<Signal>()

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        check(context.isEvaluating) { "The AlwaysEvaluator should only be used in evaluations!" }
        writtenSignals.clear()
    }

    suspend fun processWriteQueue() {
        writtenSignals.forEach { it.publish() }
        writtenSignals.clear()
    }

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.signal.resolve(ctx.signal()) ?: return
        val newValue = context.expr.resolve(ctx.expr()) ?: return

        assignee.quietSet(newValue, context.evalContext)

        writtenSignals.add(assignee.getSignal())
    }

    override fun exitCaseStat(ctx: CaseStatContext) {
        val value = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return
        ctx.caseElem().forEach { elemCtx ->
            val exprCtx: ExprContext? = elemCtx.expr()
            if (exprCtx == null) { // default case
                context.evalWalk(elemCtx.caseBlock())
                return
            }
            val condition = context.expr.resolve(exprCtx) as? SimpleValue ?: return
            if ((condition isEqualTo value).bit == Bit.B1) {
                context.evalWalk(elemCtx.caseBlock())
                return
            }
        }
    }

    override fun exitIfStat(ctx: IfStatContext) {
        val condition = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return
        if (condition.isTrue().bit == Bit.B1) {
            context.evalWalk(ctx.block())
        } else {
            ctx.elseStat()?.block()?.let { context.evalWalk(it) }
        }
    }

    override fun exitRepeatStat(ctx: RepeatStatContext) {
        val signal = context.signal.resolve(ctx.signal()) as? Signal ?: return
        val countValue = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return

        val count = countValue.toBigInt().toInt()

        if (context.isEvaluating)
            repeat(count) {
                signal.quietSet(
                    BitListValue(
                        value = it,
                        width = signal.width.getBitCount(),
                        constant = false,
                        signed = false
                    ),
                    context.evalContext
                )
                context.evalWalk(ctx.block())
            }
    }
}