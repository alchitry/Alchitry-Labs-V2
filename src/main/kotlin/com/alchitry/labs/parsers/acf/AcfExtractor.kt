package com.alchitry.labs.parsers.acf

import com.alchitry.labs.parsers.acf.types.ClockConstraint
import com.alchitry.labs.parsers.acf.types.PinConstraint
import com.alchitry.labs.parsers.acf.types.PinPull
import com.alchitry.labs.parsers.errors.NotationCollector
import com.alchitry.labs.parsers.grammar.AcfBaseListener
import com.alchitry.labs.parsers.grammar.AcfParser
import com.alchitry.labs.parsers.lucidv2.types.*
import com.alchitry.labs.project.Board
import kotlin.math.roundToInt

class AcfExtractor(
    val topModule: ModuleInstance,
    val board: Board,
    val notationCollector: NotationCollector,
) : AcfBaseListener() {
    private val signals = mutableMapOf<AcfParser.PortNameContext, SignalOrSubSignal>()
    val pins: MutableList<PinConstraint> = mutableListOf()
    val clocks: MutableList<ClockConstraint> = mutableListOf()

    override fun exitPortName(ctx: AcfParser.PortNameContext) {
        val name = ctx.name(0)?.text ?: return
        val port = topModule.ports[name]
        if (port == null) {
            notationCollector.reportError(ctx.name(0)!!, "Unknown port name \"$name\"!")
            return
        }
        val children =
            ctx.children?.filter { it is AcfParser.NameContext || it is AcfParser.ArrayIndexContext } ?: emptyList()
        val selectionMap = children.subList(1, children.size).associate {
            when (it) {
                is AcfParser.NameContext -> SignalSelector.Struct(it.text) to it
                is AcfParser.ArrayIndexContext -> SignalSelector.Bits(
                    (it.INT() ?: return).text.toInt(),
                    SelectionContext.Constant
                ) to it

                else -> error("Impossible as everything else should have been filtered out!")
            }
        }
        val sigSelection = selectionMap.keys.toList()

        val selectedSignal = if (sigSelection.isEmpty()) {
            port.external
        } else {
            try {
                port.external.select(sigSelection)
            } catch (e: SignalSelectionException) {
                notationCollector.reportError(selectionMap[e.selector]!!, e.message!!)
                return
            }
        }
        if (selectedSignal.width.bitCount != 1) {
            notationCollector.reportError(ctx, "This signal is wider than a single bit!")
            return
        }
        signals[ctx] = selectedSignal
    }

    override fun exitPin(ctx: AcfParser.PinContext) {
        val signal = signals[ctx.portName() ?: return] ?: return
        val pinName = ctx.pinName()?.text ?: return
        if (board.pinConverter.AcfToFPGAPin(pinName) == null) {
            notationCollector.reportError(ctx.pinName()!!, "Pin \"$pinName\" does not exist on the ${board.name}")
            return
        }
        val pinPull = when {
            ctx.PULLUP() != null -> PinPull.PullUp
            ctx.PULLDOWN() != null -> PinPull.PullDown
            else -> null
        }
        pins.add(PinConstraint(pinName, signal, pinPull))
    }

    override fun exitClock(ctx: AcfParser.ClockContext) {
        val signal = signals[ctx.portName() ?: return] ?: return
        val pinName = ctx.pinName()?.text ?: return
        if (board.pinConverter.AcfToFPGAPin(pinName) == null) {
            notationCollector.reportError(ctx.pinName()!!, "Pin \"$pinName\" does not exist on the ${board.name}")
            return
        }
        val pinPull = when {
            ctx.PULLUP() != null -> PinPull.PullUp
            ctx.PULLDOWN() != null -> PinPull.PullDown
            else -> null
        }
        val frequencyUnit = ctx.frequency()?.FREQ_UNIT()?.text ?: return
        val frequencyScale = when (frequencyUnit.lowercase()) {
            "hz" -> 1
            "khz" -> 1000
            "mhz" -> 1000000
            "ghz" -> 1000000000
            else -> {
                notationCollector.reportError(
                    ctx.frequency()?.FREQ_UNIT()!!,
                    "Unknown frequency unit \"$frequencyUnit\"!"
                )
                return
            }
        }
        val numCtx = ctx.frequency()?.number() ?: return
        val freq = numCtx.text.toDoubleOrNull()
        if (freq == null) {
            notationCollector.reportError(numCtx, "Failed to parse number \"${numCtx.text}\"!")
            return
        }
        val hz = freq * frequencyScale
        clocks.add(ClockConstraint(PinConstraint(pinName, signal, pinPull), hz.roundToInt()))
    }
}