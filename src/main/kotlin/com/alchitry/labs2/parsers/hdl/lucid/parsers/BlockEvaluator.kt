package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.grammar.SuspendLucidBaseListener
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.SimpleValue

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

    override suspend fun exitAlwaysSignal(ctx: AlwaysSignalContext) {
        val sigCtx = ctx.sigDec() ?: return
        val name = sigCtx.name()?.text ?: return
        val signal = context.types.resolveLocalSignal(ctx, name)
        if (signal == null) {
            context.reportError(ctx, "Failed to resolve local signal \"$name\"!")
            return
        }
        val newValue = sigCtx.expr()?.let { context.resolve(it) } ?: return
        signal.quietWrite(newValue.value, context.evalContext)
        writtenSignals.add(signal)
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
        val repeatBlock = context.blockParser.resolveRepeatBlock(ctx) ?: return
        val signal = if (repeatBlock.signal_hidden) null else repeatBlock.createSignal()

        signal?.let { context.localSignals[signal.name] = signal }

        repeatBlock.forEach {
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