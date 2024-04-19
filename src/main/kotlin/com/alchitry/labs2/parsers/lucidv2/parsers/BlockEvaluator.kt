package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import com.alchitry.labs2.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs2.parsers.lucidv2.values.minBits

data class BlockEvaluator(
    private val context: LucidBlockContext
) : SuspendLucidBaseListener() {
    fun withContext(context: LucidBlockContext) = copy(context = context)

    private val writtenSignals = mutableSetOf<Signal>()
    private var alwaysBlock: AlwaysBlockContext? = null
    private var testBlock: TestBlockContext? = null

    override suspend fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        check(context.stage == ParseStage.Evaluation) { "The BlockEvaluator should only be used in evaluations!" }
        writtenSignals.clear()
        alwaysBlock = ctx
    }

    override suspend fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        alwaysBlock = null
    }

    override suspend fun enterTestBlock(ctx: TestBlockContext) {
        check(context.stage == ParseStage.Evaluation) { "The BlockEvaluator should only be used in evaluations!" }
        writtenSignals.clear()
        testBlock = ctx
    }

    override suspend fun exitTestBlock(ctx: TestBlockContext) {
        testBlock = null
    }

    suspend fun processWriteQueue() {
        writtenSignals.forEach { it.publish() }
        writtenSignals.clear()
    }

    override suspend fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = ctx.signal()?.let { context.resolve(it) } ?: return
        val newValue = ctx.expr()?.let { context.resolve(it) } ?: return

        assignee.quietWrite(newValue.value, context.evalContext)

        writtenSignals.add(assignee.getSignal())
    }

    override suspend fun exitCaseStat(ctx: CaseStatContext) {
        val value = ctx.expr()?.let { context.expr.resolve(it)?.value } as? SimpleValue ?: return
        ctx.caseElem().forEach { elemCtx ->
            val exprCtx: ExprContext? = elemCtx.expr()
            if (exprCtx == null) { // default case
                context.walk(elemCtx.caseBlock() ?: error("Missing case block!"))
                return
            }
            val condition = context.expr.resolve(exprCtx)?.value as? SimpleValue ?: return
            if ((condition isEqualTo value).bit == Bit.B1) {
                context.walk(elemCtx.caseBlock() ?: error("Missing case block!"))
                return
            }
        }
    }

    override suspend fun exitIfStat(ctx: IfStatContext) {
        val expr = ctx.expr() ?: return
        val condition = context.expr.resolve(expr)?.value as? SimpleValue ?: return
        val truthBit = condition.isTrue().bit

        if (!truthBit.isNumber()) {
            context.notationCollector.reportWarning(expr, "If condition evaluated to $truthBit!")
        }

        if (truthBit == Bit.B1) {
            context.walk(ctx.block() ?: error("Missing if statement block!"))
        } else {
            ctx.elseStat()?.block()?.let { context.walk(it) }
        }
    }

    override suspend fun exitRepeatStat(ctx: RepeatStatContext) {
        val countValue = ctx.expr()?.let { context.expr.resolve(it)?.value } as? SimpleValue ?: return
        val count = countValue.toBigInt()?.toInt() ?: return

        val signalName = ctx.name()?.text
        val width = (count - 1).toBigInteger().minBits()

        val signal =
            signalName?.let {
                Signal(
                    signalName,
                    SignalDirection.Read,
                    null,
                    BitListValue(0, width, false),
                    ExprType.Known
                )
            }

        signal?.let { context.localSignals[it.name] = it }

        repeat(count) {
            signal?.quietWrite(
                BitListValue(
                    value = it,
                    width = signal.width.bitCount ?: error("Repeat signal has an undefined width!"),
                    signed = false
                ),
                context.evalContext
            )
            context.walk(ctx.repeatBlock() ?: error("Missing repeat block!"))
        }

        signal?.let { context.localSignals.remove(it.name) }
    }
}