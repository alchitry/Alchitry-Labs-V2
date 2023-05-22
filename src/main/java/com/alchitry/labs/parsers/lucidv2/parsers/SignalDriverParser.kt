package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidBaseListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.SubSignal
import com.alchitry.labs.parsers.lucidv2.types.Dff
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstanceOrArray
import com.alchitry.labs.parsers.lucidv2.types.ports.InterfaceSignals
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

    private var expectedDrivers: Set<Signal>? = null

    override fun exitPortDec(ctx: PortDecContext) {
        val port =
            context.resolveSignal(ctx.name().text) as? Signal ?: error("Unresolved port of name \"${ctx.name().text}\"")
        when (port.direction) {
            SignalDirection.Read ->
                if (!port.isRead)
                    context.reportWarning(ctx.name(), "The input \"${port.name}\" is never read.")

            SignalDirection.Write ->
                if (!port.hasDriver)
                    context.reportError(ctx.name(), "The output \"${port.name}\" is never driven.")

            SignalDirection.Both ->
                when {
                    !port.isRead && !port.hasDriver ->
                        context.reportError(ctx.name(), "The inout \"${port.name}\" is not connected.")

                    !port.isRead ->
                        context.reportWarning(ctx.name(), "The inout \"${port.name}\" is never read.")

                    !port.hasDriver ->
                        context.reportError(ctx.name(), "The inout \"${port.name}\" is never driven!")
                }
        }
    }

    override fun exitSigDec(ctx: SigDecContext) {
        val sig =
            context.resolveSignal(ctx.name().text) as? Signal ?: error("Unresolved sig of name ${ctx.name().text}")
        if (!sig.hasDriver && sig.isRead) {
            context.reportError(ctx.name(), "The signal \"${sig.name}\" is read but doesn't have a driver!")
            return
        }
        if (!sig.isRead) {
            context.reportWarning(ctx.name(), "The signal \"${sig.name}\" is never read.")
            return
        }
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val dff = context.resolveSignal(ctx.name().text) as? Dff ?: error("Unresolved dff of name ${ctx.name().text}")
        if (!dff.d.hasDriver) {
            context.reportError(ctx.name(), "The d input of dff \"${dff.name}\" is never driven!")
            return
        }
        if (!dff.q.isRead) {
            context.reportWarning(ctx.name(), "The q output of the dff \"${dff.name}\" is never read.")
            return
        }
    }

    override fun exitModuleInst(ctx: ModuleInstContext) {
        val nameNode = ctx.name(1) ?: return
        val inst = context.resolveSignal(nameNode.text) as? ModuleInstanceOrArray
            ?: error("Unresolved instance of name ${nameNode.text}")

        fun checkSignal(signal: SignalOrParent) {
            when (signal) {
                is Signal -> {
                    when (signal.direction) {
                        SignalDirection.Read -> {
                            if (!signal.isRead) {
                                context.reportWarning(
                                    nameNode,
                                    "Port \"${signal.name}\" of module instance \"${nameNode.text}\" is never read."
                                )
                            }
                        }

                        SignalDirection.Write -> {
                            if (!signal.hasDriver) {
                                context.reportError(
                                    nameNode,
                                    "Port \"${signal.name}\" of module instance \"${nameNode.text}\" is never driven!"
                                )
                            }
                        }

                        SignalDirection.Both -> {
                            when {
                                !signal.isRead && !signal.hasDriver ->
                                    context.reportError(
                                        nameNode,
                                        "Port \"${signal.name}\" of module instance \"${nameNode.text}\" is not connected!"
                                    )

                                !signal.isRead ->
                                    context.reportWarning(
                                        nameNode,
                                        "Port \"${signal.name}\" of module instance \"${nameNode.text}\" is never read."
                                    )

                                !signal.hasDriver ->
                                    context.reportError(
                                        nameNode,
                                        "Port \"${signal.name}\" of module instance \"${nameNode.text}\" is never driven!"
                                    )

                            }
                        }
                    }
                }

                is InterfaceSignals -> signal.signals.values.forEach { checkSignal(it) }
                else -> error("Unexpected port signal type!")
            }
        }
        inst.external.values.forEach { signalOrParent -> checkSignal(signalOrParent) }
    }

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        expectedDrivers = (context.alwaysParser.alwaysBlocks[ctx]?.drivenSignals
            ?: error("Failed to resolve always block!"))
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        val drivenMap = drivenSignals[ctx.block()] ?: error("Missing always block signals!")
        expectedDrivers?.forEach { signal ->
            val driven = drivenMap[signal]
            if (driven == null) {
                context.reportError(
                    ctx,
                    "The signal \"${signal.fullName()}\" was expected to be driven by this always block but it wasn't."
                )
                return@forEach
            }
            if (driven.andReduce().bit != Bit.B1) {
                context.reportError(
                    ctx,
                    "The signal \"${signal.fullName()}\" was only partially driven. Bits marked as 0 weren't driven: $driven"
                )
            }
        }
        expectedDrivers = null
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        val sig = context.resolve(ctx.signal()) ?: return
        val fullSig = sig.getSignal()
        val expected = expectedDrivers ?: return
        if (expected.contains(fullSig)) { // should be driving this signal
            val drivenValue = signalStack.firstNotNullOfOrNull { it[fullSig] }
            if (drivenValue == null) {
                context.reportError(ctx, "The signal \"${fullSig.fullName()}\" can't be read before it is written!")
                return
            }
            val selectedValue = when (sig) {
                is Signal -> drivenValue
                is SubSignal -> drivenValue.select(sig.selection)
            }
            if (selectedValue.andReduce().bit != Bit.B1) {
                context.reportError(
                    ctx,
                    "This portion of the signal \"${fullSig.fullName()}\" can't be read before it is written!"
                )
                return
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
        drivenSignals[ctx.repeatBlock().block()]?.let { signals.putAll(it) }
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