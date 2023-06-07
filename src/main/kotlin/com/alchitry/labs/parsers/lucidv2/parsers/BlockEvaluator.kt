package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.grammar.LucidBaseListener
import com.alchitry.labs.parsers.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.minBits

data class BlockEvaluator(
    private val context: LucidBlockContext
) : LucidBaseListener() {
    fun withContext(context: LucidBlockContext) = copy(context = context)

    private val writtenSignals = mutableSetOf<Signal>()
    private var alwaysBlock: AlwaysBlockContext? = null
    private var testBlock: TestBlockContext? = null

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        check(context.stage == ParseStage.Evaluation) { "The BlockEvaluator should only be used in evaluations!" }
        writtenSignals.clear()
        alwaysBlock = ctx
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlock = null
    }

    override fun enterTestBlock(ctx: TestBlockContext) {
        check(context.stage == ParseStage.Evaluation) { "The BlockEvaluator should only be used in evaluations!" }
        writtenSignals.clear()
        testBlock = ctx
    }

    override fun exitTestBlock(ctx: TestBlockContext) {
        testBlock = null
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
        val countValue = context.expr.resolve(ctx.expr()) as? SimpleValue ?: return
        val count = countValue.toBigInt().toInt()

        val signalName = ctx.name()?.text
        val width = (count - 1).toBigInteger().minBits()

        val signal =
            signalName?.let {
                Signal(
                    signalName,
                    SignalDirection.Read,
                    null,
                    BitListValue(0, width, false, false)
                )
            }

        signal?.let { context.localSignals[it.name] = it }

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

        signal?.let { context.localSignals.remove(it.name) }
    }
}