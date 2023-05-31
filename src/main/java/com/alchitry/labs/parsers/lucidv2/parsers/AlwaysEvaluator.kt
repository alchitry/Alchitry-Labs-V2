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
    private var alwaysBlock: AlwaysBlockContext? = null

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        check(context.stage == ParseStage.Evaluation) { "The AlwaysEvaluator should only be used in evaluations!" }
        writtenSignals.clear()
        alwaysBlock = ctx
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlock = null
    }

    suspend fun processWriteQueue() {
        writtenSignals.forEach { it.publish() }
        writtenSignals.clear()
    }

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.resolve(ctx.signal()) ?: return
        val newValue = context.resolve(ctx.expr()) ?: return

        assignee.quietWrite(newValue, context.evalContext)

        writtenSignals.add(assignee.getSignal())
    }

    override fun exitCaseStat(ctx: CaseStatContext) {
        val value = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return
        ctx.caseElem().forEach { elemCtx ->
            val exprCtx: ExprContext? = elemCtx.expr()
            if (exprCtx == null) { // default case
                context.walk(elemCtx.caseBlock())
                return
            }
            val condition = context.expr.resolve(exprCtx) as? SimpleValue ?: return
            if ((condition isEqualTo value).bit == Bit.B1) {
                context.walk(elemCtx.caseBlock())
                return
            }
        }
    }

    override fun exitIfStat(ctx: IfStatContext) {
        val condition = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return
        val truthBit = condition.isTrue().bit

        if (!truthBit.isNumber()) {
            context.errorCollector.reportWarning(ctx.expr(), "If condition evaluated to $truthBit!")
        }

        if (truthBit == Bit.B1) {
            context.walk(ctx.block())
        } else {
            ctx.elseStat()?.block()?.let { context.walk(it) }
        }
    }

    override fun exitRepeatStat(ctx: RepeatStatContext) {
        val signal = context.alwaysParser.alwaysBlocks[alwaysBlock]?.repeatSignals?.get(ctx)
        val countValue = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return

        val count = countValue.toBigInt().toInt()

        repeat(count) {
            signal?.quietWrite(
                BitListValue(
                    value = it,
                    width = signal.width.getBitCount() ?: error("Repeat signal has an undefined width!"),
                    constant = false,
                    signed = false
                ),
                context.evalContext
            )
            context.walk(ctx.repeatBlock())
        }
    }
}