package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.ClockConstraint
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.acf.types.PinConstraint
import com.alchitry.labs2.parsers.acf.types.PinPull
import com.alchitry.labs2.parsers.grammar.AcfBaseListener
import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.parsers.grammar.AcfParser
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.files.ConstraintFile
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker
import kotlin.math.roundToInt

class AcfExtractor(
    val context: ProjectContext,
    val topModule: ModuleInstance,
    val board: Board,
    val notationCollector: NotationCollector,
) : AcfBaseListener() {
    private val signals = mutableMapOf<AcfParser.PortNameContext, SignalOrSubSignal>()
    val constraints: MutableList<Constraint> = mutableListOf()

    companion object {
        suspend fun extract(
            context: ProjectContext,
            file: ConstraintFile,
            topModule: ModuleInstance,
            board: Board,
            notationCollector: NotationCollector,
        ): AcfExtractor? {
            val parser =
                AcfParser(
                    CommonTokenStream(
                        AcfLexer(
                            file.toCharStream()
                        ).also { it.removeErrorListeners() }
                    )
                ).apply {
                    (tokenStream?.tokenSource as? AcfLexer)?.addErrorListener(notationCollector)
                        ?: error("TokenSource was not an AcfLexer!")
                    removeErrorListeners()
                    addErrorListener(notationCollector)
                }

            val tree = parser.source()

            if (notationCollector.hasErrors) {
                return null
            }

            val extractor = AcfExtractor(context, topModule, board, notationCollector)
            ParseTreeWalker.walk(extractor, tree)

            if (notationCollector.hasErrors) {
                return null
            }

            return extractor
        }
    }

    private fun addConstraint(constraint: Constraint, pinContext: ParserRuleContext, portContext: ParserRuleContext) {
        when (context.addConstraint(constraint)) {
            ProjectContext.AddConstraintResult.Success -> constraints.add(constraint)
            ProjectContext.AddConstraintResult.PinTaken -> notationCollector.reportError(
                pinContext,
                "The pin \"${constraint.pin.name}\" has already been connected!"
            )

            ProjectContext.AddConstraintResult.PortTaken -> notationCollector.reportError(
                portContext,
                "The port \"${constraint.port}\" has already been connected!"
            )
        }
    }

    override fun exitPortName(ctx: AcfParser.PortNameContext) {
        val name = ctx.name(0)?.text ?: return
        val port = topModule.ports[name]
        if (port == null) {
            notationCollector.reportWarning(
                ctx.name(0)!!,
                "Unknown port name \"$name\". This constraint will be ignored."
            )
            return
        }
        val children =
            ctx.children?.filter { it is AcfParser.NameContext || it is AcfParser.ArrayIndexContext } ?: emptyList()
        val selectionMap = children.subList(1, children.size).map {
            when (it) {
                is AcfParser.NameContext -> SignalSelector.Struct(it.text) to it
                is AcfParser.ArrayIndexContext -> SignalSelector.Bits(
                    (it.INT() ?: return).text.toInt(),
                    SelectionContext.Constant
                ) to it

                else -> error("Impossible as everything else should have been filtered out!")
            }
        }
        val sigSelection = selectionMap.map { it.first }

        val selectedSignal = if (sigSelection.isEmpty()) {
            port.external
        } else {
            try {
                port.external.select(sigSelection)
            } catch (e: SignalSelectionException) {
                notationCollector.reportError(
                    selectionMap.firstOrNull { it.first === e.selector }?.second ?: ctx,
                    e.message!!
                )
                return
            }
        }
        if (selectedSignal.width.bitCount != 1) {
            notationCollector.reportError(
                ctx, "The signal \"${children.joinToString("") { it.text }}\" is wider than a single bit!"
            )
            return
        }
        signals[ctx] = selectedSignal
    }

    override fun exitPin(ctx: AcfParser.PinContext) {
        val signal = signals[ctx.portName() ?: return] ?: return
        val pinName = ctx.pinName()?.text ?: return
        val pin = board.pinConverter.acfToPin(pinName)
        if (pin == null) {
            notationCollector.reportError(ctx.pinName()!!, "Pin \"$pinName\" does not exist on the ${board.name}")
            return
        }
        val pinPull = when {
            ctx.PULLUP() != null -> PinPull.PullUp
            ctx.PULLDOWN() != null -> PinPull.PullDown
            else -> null
        }
        addConstraint(PinConstraint(pin, signal, pinPull, ctx), ctx.pinName() ?: ctx, ctx.portName() ?: ctx)
    }

    override fun exitClock(ctx: AcfParser.ClockContext) {
        val signal = signals[ctx.portName() ?: return] ?: return
        val pinName = ctx.pinName()?.text ?: return
        val pin = board.pinConverter.acfToPin(pinName)
        if (pin == null) {
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
        addConstraint(
            ClockConstraint(pin, signal, pinPull, hz.roundToInt(), ctx),
            ctx.pinName() ?: ctx,
            ctx.portName() ?: ctx
        )
    }
}