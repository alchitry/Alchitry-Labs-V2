package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.types.Function
import com.alchitry.labs2.parsers.lucidv2.values.*
import kotlinx.coroutines.runBlocking
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.TerminalNode


/**
 * This is responsible for checking that all the bits of signals that are expected to be driven are driven.
 */
data class SignalDriverParser(
    private val context: LucidBlockContext
) : LucidBaseListener() {
    private val drivenSignals = mutableMapOf<ParserRuleContext, Map<Signal, Value>>()
    private val signalStack = mutableListOf<MutableMap<Signal, Value>>()
    private val signals: MutableMap<Signal, Value> get() = signalStack.last()

    private var expectedDrivers: Set<Signal>? = null

    fun getDrivenSignals(context: BlockContext): Set<Signal>? = drivenSignals[context]?.keys

    override fun exitPortDec(ctx: PortDecContext) {
        val nameCtx = ctx.name() ?: error("Name missing from port dec!")
        val port =
            context.resolveSignal(ctx, nameCtx.text) as? Signal ?: error("Unresolved port of name \"${nameCtx.text}\"")
        when (port.direction) {
            SignalDirection.Read ->
                if (!port.isRead)
                    context.reportWarning(nameCtx, "The input \"${port.name}\" is never read.")

            SignalDirection.Write ->
                if (!port.hasDriver)
                    context.reportError(nameCtx, "The output \"${port.name}\" is never driven.")

            SignalDirection.Both ->
                when {
                    !port.isRead && !port.hasDriver ->
                        context.reportError(nameCtx, "The inout \"${port.name}\" is not connected.")

                    !port.isRead ->
                        context.reportWarning(nameCtx, "The inout \"${port.name}\" is never read.")

                    !port.hasDriver ->
                        context.reportError(nameCtx, "The inout \"${port.name}\" is never driven!")
                }
        }
    }

    override fun exitConstDec(ctx: ConstDecContext) {
        val nameCtx = ctx.name() ?: error("Name missing from const dec!")
        val sig =
            context.resolveSignal(ctx, nameCtx.text) as? Signal ?: error("Unresolved sig of name ${nameCtx.text}")
        if (!sig.isRead) {
            context.reportWarning(nameCtx, "The constant \"${sig.name}\" is never used.")
            return
        }
    }

    override fun exitSigDec(ctx: SigDecContext) {
        val nameCtx = ctx.name() ?: error("Name missing from sig dec!")
        val sig =
            context.resolveSignal(ctx, nameCtx.text) as? Signal ?: error("Unresolved sig of name ${nameCtx.text}")

        if (ctx.parent is AlwaysSignalContext) {
            if (ctx.expr() != null) {
                signals[sig] = sig.width.filledWith(Bit.B1, signed = false)
            }
            return
        }

        if (!sig.hasDriver && sig.isRead) {
            context.reportError(nameCtx, "The signal \"${sig.name}\" is read but doesn't have a driver!")
            return
        }
        if (!sig.isRead) {
            context.reportWarning(nameCtx, "The signal \"${sig.name}\" is never read.")
            return
        }
    }

    override fun exitDffDec(ctx: DffDecContext) {
        val nameCtx = ctx.name() ?: error("Name missing from dff dec!")
        val dff = context.resolveSignal(ctx, nameCtx.text) as? Dff ?: error("Unresolved dff of name ${nameCtx.text}")
        if (!dff.d.hasDriver) {
            context.reportError(nameCtx, "The d input of dff \"${dff.name}\" is never driven!")
            return
        }
        if (!dff.q.isRead) {
            context.reportWarning(nameCtx, "The q output of the dff \"${dff.name}\" is never read.")
            return
        }
    }

    override fun exitModuleInst(ctx: ModuleInstContext) {
        val nameNode = ctx.name(1) ?: return
        val inst = context.resolveSignal(ctx, nameNode.text) as? ModuleInstanceOrArray
            ?: error("Unresolved instance of name ${nameNode.text}")

        inst.external.values.forEach { signal ->
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
    }

    override fun enterAlwaysBlock(ctx: AlwaysBlockContext) {
        expectedDrivers = (context.blockParser.alwaysBlocks[ctx]?.drivenSignals
            ?: error("Failed to resolve always block!"))
    }

    override fun exitAlwaysBlock(ctx: AlwaysBlockContext) {
        val drivenMap = drivenSignals[ctx.block() ?: error("Block missing from Always block!")]
            ?: error("Missing always block signals!")
        expectedDrivers?.forEach { signal ->
            val driven = drivenMap[signal]
            if (driven == null) {
                context.reportError(
                    ctx.children?.first() as TerminalNode,
                    "The signal \"${signal.fullName()}\" was expected to be driven by this always block but it wasn't."
                )
                return@forEach
            }
            val drivenBit = driven.andReduce().bit
            if (!(driven is UndefinedValue || drivenBit == Bit.B1 || signal.parent is Dff)) {
                context.reportError(
                    ctx.children?.first() as TerminalNode,
                    "The signal \"${signal.fullName()}\" was only partially driven. 1 = driven, x = conditionally driven, 0 = not driven: ${
                        driven.toString(
                            ValueFormat.Binary
                        )
                    }"
                )
            }
        }
        expectedDrivers = null
    }

    override fun exitExprSignal(ctx: ExprSignalContext) {
        val sig = context.resolve(ctx.signal() ?: return) ?: return
        val fullSig = sig.getSignal()
        val expected = expectedDrivers ?: return
        if (expected.contains(fullSig) || fullSig.parent is LocalSignal) { // should be driving this signal
            val drivenValue = signalStack.firstNotNullOfOrNull { it[fullSig] }
            if (drivenValue == null) {
                val functionCtx = ctx.firstParentOrNull { it is FunctionContext }
                // exclude inout as they can be read and written any time
                // exclude reads of the WIDTH function as this doesn't read the signal
                val isInout = fullSig.parent is ModuleInstance && fullSig.direction == SignalDirection.Both
                if (!isInout && (functionCtx !is FunctionContext || functionCtx.FUNCTION_ID()?.text != "$" + Function.WIDTH.label)) {
                    context.reportError(ctx, "The signal \"${fullSig.fullName()}\" can't be read before it is written!")
                }
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
        signalStack.add(mutableMapOf()) // push a new map onto the stack
    }

    private fun stopBlock(ctx: ParserRuleContext) {
        drivenSignals[ctx] = signalStack.removeLast() // pop map from stack
    }

    override fun enterBlock(ctx: BlockContext) {
        if (ctx.parent !is RepeatBlockContext)
            startBlock()
    }

    override fun exitBlock(ctx: BlockContext) {
        if (ctx.parent !is RepeatBlockContext)
            stopBlock(ctx)
    }

    override fun enterCaseBlock(ctx: CaseBlockContext) = startBlock()
    override fun exitCaseBlock(ctx: CaseBlockContext) = stopBlock(ctx)

    override fun exitAssignStat(ctx: AssignStatContext) {
        val assignee = context.resolve(ctx.signal() ?: return) ?: return
        val currentValue = signals[assignee.getSignal()]
            ?: assignee.getSignal().width.filledWith(Bit.B0, signed = false)

        signals[assignee.getSignal()] = when (assignee) {
            is Signal -> assignee.width.filledWith(Bit.B1, signed = false)
            is SubSignal -> currentValue.write(
                assignee.selection,
                assignee.width.filledWith(Bit.B1, signed = false)
            )
        }

        val exprCtx = ctx.expr() ?: return
        val newValue = context.resolve(exprCtx)?.value ?: return
        // runBlocking should be fine since this isn't run during evaluation (multi-thread)
        runBlocking { assignee.write(newValue) }
    }

    override fun exitRepeatStat(ctx: RepeatStatContext) {
        val repeatBlock = context.blockParser.resolveRepeatBlock(ctx) ?: return
        val block = ctx.repeatBlock()?.block() ?: return
        val signal = repeatBlock.createSignal()

        startBlock()

        signal?.let { context.localSignals[it.name] = it }
        runBlocking {
            repeatBlock.forEach {
                signal?.quietWrite(
                    BitListValue(
                        value = it,
                        width = signal.width.bitCount ?: error("Repeat signal has an undefined width!"),
                        signed = false
                    ),
                    context.evalContext
                )
                context.walk(block)
            }
        }
        signal?.let { context.localSignals.remove(it.name) }
        stopBlock(block)
        drivenSignals[ctx.repeatBlock()?.block() ?: return]?.let { drivers ->
            drivers.forEach { driver ->
                val currentValue = signals[driver.key]
                    ?: driver.value.width.filledWith(Bit.B0, signed = false)
                signals[driver.key] = currentValue.or(driver.value)
            }
        }
    }

    private fun combineConditionalAssignments(first: Value, second: Value): Value {
        val fullyDriven = (first and second).replaceBit(Bit.Bx, Bit.B0)
        val conditionallyDriven = (first or second).replaceBit(Bit.B1, Bit.Bx)
        return fullyDriven or conditionallyDriven
    }

    override fun exitIfStat(ctx: IfStatContext) {
        // if statements can't drive a signal if not complete
        val elseBlock = ctx.elseStat()?.block()?.let { drivenSignals[it] ?: error("Missing else block signals!") }
        val ifBlock = drivenSignals[ctx.block() ?: error("Block missing from if statement!")]
            ?: error("Missing if block signals!")


        ifBlock.keys.union(elseBlock?.keys ?: emptySet()).forEach { signal ->
            val ifValue = ifBlock[signal] ?: signal.width.filledWith(Bit.B0, signed = false)
            val elseValue = elseBlock?.get(signal) ?: signal.width.filledWith(Bit.B0, signed = false)
            val currentValue = signals[signal] ?: signal.width.filledWith(Bit.B0, signed = false)
            signals[signal] = currentValue or combineConditionalAssignments(ifValue, elseValue)
        }
    }

    override fun exitCaseStat(ctx: CaseStatContext) {
        // case statements can't drive a signal without a default case
        val forceConditional = ctx.caseElem().none { it.expr() == null }

        val caseMaps =
            ctx.caseElem().map {
                drivenSignals[it.caseBlock() ?: error("Block missing from case element!")]
                    ?: error("Missing case block signals!")
            }

        // for each signal appearing in any block
        caseMaps.flatMap { it.keys }.toSet().forEach { sig ->
            val signalValues =
                caseMaps.map { it[sig] ?: sig.width.filledWith(Bit.B0, signed = false) }
            val currentValue = signals[sig] ?: sig.width.filledWith(Bit.B0, signed = false)
            val drivenValue = signalValues.reduce { acc, value -> combineConditionalAssignments(acc, value) }
            val maskedValue = if (forceConditional) drivenValue.replaceBit(Bit.B1, Bit.Bx) else drivenValue
            signals[sig] = currentValue or maskedValue
        }
    }

}