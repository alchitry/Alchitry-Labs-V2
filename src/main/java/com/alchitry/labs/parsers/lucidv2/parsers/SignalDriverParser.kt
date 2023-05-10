package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SubSignal
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.Value
import org.antlr.v4.runtime.tree.ParseTree


/**
 * This is responsible for checking that all the bits of signals that are expected to be driven are driven.
 */
data class SignalDriverParser(
    private val context: LucidModuleContext
) : LucidBaseListener() {
    private val drivenSignals = mutableMapOf<ParseTree, Map<Signal, Value>>()
    private val signalStack = mutableListOf<MutableMap<Signal, Value>>()
    private val signals: MutableMap<Signal, Value> get() = signalStack.last()

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        val drivenMap = drivenSignals[ctx.block()] ?: error("Missing always block signals!")
        val expected = context.alwaysParser.alwaysBlocks[ctx]?.drivenSignals ?: error("Failed to resolve always block!")
        expected.forEach { signal ->
            val driven = drivenMap[signal]
            if (driven == null) {
                context.reportError(
                    ctx,
                    "The signal ${signal.name} was expected to be driven by this always block but it wasn't."
                )
                return@forEach
            }
            if (driven.andReduce().bit != Bit.B1) {
                context.reportError(
                    ctx,
                    "The signal ${signal.name} was only partially driven. Bits marked as 0 weren't driven: $driven"
                )
            }
        }
    }

    private fun startBlock() {
        signalStack.add(mutableMapOf()) // push new map onto the stack
    }

    private fun stopBlock(ctx: ParseTree) {
        val signals = signalStack.removeLast() // pop map from stack
        drivenSignals[ctx] = signals
    }

    override fun enterBlock(ctx: BlockContext) = startBlock()
    override fun exitBlock(ctx: BlockContext) = stopBlock(ctx)
    override fun enterCaseBlock(ctx: CaseBlockContext) = startBlock()
    override fun exitCaseBlock(ctx: CaseBlockContext) = stopBlock(ctx)

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.resolve(ctx.signal()) ?: return
        val currentValue = signals[assignee.getSignal()]
            ?: assignee.getSignal().width.filledWith(Bit.B0, constant = false, signed = false)

        signals[assignee.getSignal()] = when (assignee) {
            is Signal -> assignee.width.filledWith(Bit.B1, constant = false, signed = false)
            is SubSignal -> currentValue.write(
                assignee.selection,
                assignee.width.filledWith(Bit.B1, constant = false, signed = false)
            )
        }
    }

    override fun exitRepeatStat(ctx: RepeatStatContext) {
        drivenSignals[ctx.block()]?.let { signals.putAll(it) }
    }

    override fun exitIfStat(ctx: IfStatContext) {
        // if statements can't drive a signal if not complete
        val elseBlock = drivenSignals[ctx.elseStat()?.block() ?: return] ?: error("Missing else block signals!")
        val ifBlock = drivenSignals[ctx.block()] ?: error("Missing if block signals!")

        ifBlock.keys.intersect(elseBlock.keys).forEach { signal ->
            val ifValue = ifBlock[signal]!!
            val elseValue = elseBlock[signal]!!
            val currentValue = signals[signal] ?: signal.width.filledWith(Bit.B0, constant = false, signed = false)
            signals[signal] = currentValue or (ifValue and elseValue)
        }
    }

    override fun exitCaseStat(ctx: CaseStatContext) {
        // case statements can't drive a signal without a default case
        if (ctx.caseElem().none { it.expr() == null })
            return

        val caseMaps =
            ctx.caseElem().map { drivenSignals[it.caseBlock()] ?: error("Missing case block signals!") }

        // for each signal appearing in all blocks
        caseMaps.flatMap { it.keys }.toSet().forEach { sig ->
            val signalValues = caseMaps.map { it[sig]!! }
            val currentValue = signals[sig] ?: sig.width.filledWith(Bit.B0, constant = false, signed = false)
            val fullyDrivenValue = signalValues.reduce { acc, value -> acc and value }
            signals[sig] = currentValue or fullyDrivenValue
        }
    }

}