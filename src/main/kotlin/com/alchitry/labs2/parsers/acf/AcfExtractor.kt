package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.firstOfTypeOrNull
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.joinToOrList
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.*
import com.alchitry.labs2.parsers.grammar.AcfBaseListener
import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.parsers.grammar.AcfParser
import com.alchitry.labs2.parsers.grammar.AcfParser.*
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.notations.NotationCollector
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
    private val signals = mutableMapOf<PortNameContext, SignalOrSubSignal>()
    val constraints: MutableList<Constraint> = mutableListOf()

    private val blockAttributes = mutableListOf<Map<AttributeContext, PinAttribute>>()

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

    override fun enterSource(ctx: SourceContext) {
        blockAttributes.clear()
    }

    private fun parseFrequency(ctx: FrequencyContext): Int? {
        val frequencyUnit = ctx.FREQ_UNIT()?.text ?: return null
        val frequencyScale = when (frequencyUnit.lowercase()) {
            "hz" -> 1
            "khz" -> 1000
            "mhz" -> 1000000
            "ghz" -> 1000000000
            else -> {
                notationCollector.reportError(
                    ctx.FREQ_UNIT()!!,
                    "Unknown frequency unit \"$frequencyUnit\"!"
                )
                return null
            }
        }
        val numCtx = ctx.number() ?: return null
        val freq = numCtx.text.toDoubleOrNull()
        if (freq == null) {
            notationCollector.reportError(numCtx, "Failed to parse number \"${numCtx.text}\"!")
            return null
        }
        val hz = freq * frequencyScale
        return hz.roundToInt()
    }

    private fun parseAttribute(ctx: AttributeContext): PinAttribute? {
        val name = ctx.BASIC_NAME()?.text ?: return null
        val valueText = ctx.attributeValue()?.text ?: return null
        when (name) {
            "PULL" -> return when (valueText) {
                "UP" -> PinAttribute.Pull(PinPull.Up)
                "DOWN" -> PinAttribute.Pull(PinPull.Down)
                "KEEP" -> PinAttribute.Pull(PinPull.Keep)
                else -> {
                    notationCollector.reportError(
                        ctx.attributeValue() ?: ctx,
                        "Invalid PULL value \"$valueText\". Expected \"UP\", \"DOWN\", or \"KEEP\"."
                    )
                    null
                }
            }


            "STANDARD" -> {
                val standard = board.pinConverter.standards.firstOrNull { it.name == valueText }
                if (standard == null) {
                    notationCollector.reportError(
                        ctx.attributeValue() ?: ctx,
                        "Invalid STANDARD value \"$valueText\". Expected ${
                            board.pinConverter.standards.map { "\"${it.name}\"" }.joinToOrList()
                        }."
                    )
                    return null
                }
                return PinAttribute.Standard(standard)
            }

            "DRIVE" -> {
                val value = valueText.toIntOrNull()
                if (value == null) {
                    notationCollector.reportError(
                        ctx.attributeValue() ?: ctx,
                        "Invalid DRIVE value \"$valueText\". Expected an integer."
                    )
                    return null
                }
                return PinAttribute.Drive(value)
            }

            "SLEW" -> {
                val slew = PinSlew.entries.firstOrNull { it.name == valueText }
                if (slew == null) {
                    notationCollector.reportError(
                        ctx.attributeValue() ?: ctx,
                        "Invalid SLEW value \"$valueText\". Expected ${
                            PinSlew.entries.map { "\"${it.name}\"" }.joinToOrList()
                        }."
                    )
                    return null
                }
                return PinAttribute.Slew(slew)
            }

            "FREQUENCY" -> {
                val frequency = ctx.attributeValue()?.frequency()?.let(::parseFrequency)
                if (frequency == null) {
                    notationCollector.reportError(
                        ctx.attributeValue() ?: ctx,
                        "Invalid FREQUENCY value \"$valueText\". Expected a number followed by a frequency unit (e.g. \"100MHz\")."
                    )
                    return null
                }
                return PinAttribute.Frequency(frequency)
            }

            else -> {
                notationCollector.reportError(ctx, "Unknown attribute \"$name\".")
                return null
            }
        }
    }

    override fun enterAttributeBlock(ctx: AttributeBlockContext) {
        val attributeMap = ctx.attribute().mapNotNull { it to (parseAttribute(it) ?: return@mapNotNull null) }.toMap()
        attributeMap.forEach { (ctx, attr) ->
            if (blockAttributes.any { block -> block.any { it.value.name == attr.name } } || attributeMap.any { it.value.name == attr.name && it.value !== attr }) {
                notationCollector.reportError(ctx, "The attribute \"${attr.name}\" has already been defined.")
            }
        }
        blockAttributes.add(attributeMap)
    }

    override fun exitAttributeBlock(ctx: AttributeBlockContext) {
        blockAttributes.removeLast()
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

    override fun exitPortName(ctx: PortNameContext) {
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
            ctx.children?.filter { it is NameContext || it is ArrayIndexContext } ?: emptyList()
        val selectionMap = children.subList(1, children.size).map {
            when (it) {
                is NameContext -> SignalSelector.Struct(it.text) to it
                is ArrayIndexContext -> SignalSelector.Bits(
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

    private inline fun <reified T : PinAttribute> Map<AttributeContext, PinAttribute>.firstOfType(): Pair<AttributeContext, T>? {
        return firstNotNullOfOrNull { if (it.value is T) it.key to it.value as T else null }
    }

    override fun exitPin(ctx: PinContext) {
        val signal = signals[ctx.portName() ?: return] ?: return
        val pinName = ctx.pinName()?.text ?: return
        val pin = board.pinConverter.acfToPin(pinName)
        if (pin == null) {
            notationCollector.reportError(ctx.pinName()!!, "Pin \"$pinName\" does not exist on the ${board.name}")
            return
        }
        val attributes = mutableMapOf<AttributeContext, PinAttribute>()
        blockAttributes.forEach { attributes.putAll(it) }
        ctx.attribute().forEach {
            parseAttribute(it)?.let { attr ->
                if (attributes.any { it.value.name == attr.name }) {
                    notationCollector.reportError(it, "The attribute \"${attr.name}\" has already been defined.")
                }
                attributes[it] = attr
            }
        }


        val standard = attributes.firstOfType<PinAttribute.Standard>()
        if (standard == null) {
            notationCollector.reportError(
                ctx,
                "The signal \"${ctx.portName()?.text}\" is missing the STANDARD attribute."
            )
            return
        }

        val ioStandard = standard.second.value

        val pinDirection = when (signal.direction) {
            SignalDirection.Read -> PinDirection.OUTPUT
            SignalDirection.Write -> PinDirection.INPUT
            SignalDirection.Both -> PinDirection.INOUT
        }
        if (!ioStandard.directions.contains(pinDirection)) {
            notationCollector.reportError(
                ctx,
                "The STANDARD \"${ioStandard.name}\" does not support the direction \"${pinDirection.name}\"."
            )
            return
        }

        val bankVccos = board.pinConverter.bankToVcco(pin.bank)
        val filteredVcco = context.getConstraints().firstNotNullOfOrNull {
            if (pin.bank != it.pin.bank)
                return@firstNotNullOfOrNull null
            val pinStd =
                it.attributes.firstOfTypeOrNull<PinAttribute.Standard>()?.value ?: return@firstNotNullOfOrNull null
            if (it.port.direction != SignalDirection.Write && pinStd.inputAnyVcco)
                return@firstNotNullOfOrNull null
            pinStd.vcco
        }

        val vcco = if (pinDirection == PinDirection.INPUT && ioStandard.inputAnyVcco) null else ioStandard.vcco
        if (vcco != null) {
            if (filteredVcco != null && filteredVcco != vcco) {
                val reason = if (bankVccos.size > 1) {
                    "requires $filteredVcco from previous constraints"
                } else {
                    "has $filteredVcco"
                }
                notationCollector.reportError(
                    ctx,
                    "The STANDARD \"${ioStandard.name}\" requires a VCCO of $vcco but bank ${pin.bank} $reason."
                )
                return
            }
            if (!bankVccos.contains(vcco)) {
                notationCollector.reportError(
                    ctx,
                    "The STANDARD \"${ioStandard.name}\" requires a VCCO of $vcco but bank ${pin.bank} has ${
                        bankVccos.joinToOrList()
                    }."
                )
                return
            }
        }

        val pull = attributes.firstOfType<PinAttribute.Pull>()
        if (pull != null && board is Board.AlchitryCu && pull.second.value != PinPull.Up) {
            notationCollector.reportError(pull.first, "The Alchitry Cu board only supports PULL(UP).")
            return
        }

        val drive = attributes.firstOfType<PinAttribute.Drive>()
        if (drive != null) {
            val supportedDrives = ioStandard.drive
            if (supportedDrives == null) {
                notationCollector.reportError(
                    drive.first,
                    "The STANDARD \"${ioStandard.name}\" does not support drive values."
                )
                return
            }
            if (!supportedDrives.contains(drive.second.value)) {
                notationCollector.reportError(
                    drive.first,
                    "The STANDARD \"${ioStandard.name}\" does not support the drive value \"${drive.second.value}\". Expected ${
                        supportedDrives.joinToOrList()
                    }."
                )
                return
            }
        }

        val slew = attributes.firstOfType<PinAttribute.Slew>()
        if (slew != null) {
            val supportedSlews = ioStandard.slew
            if (supportedSlews == null) {
                notationCollector.reportError(
                    slew.first,
                    "The STANDARD \"${ioStandard.name}\" does not support slew values."
                )
                return
            }
            if (!supportedSlews.contains(slew.second.value)) {
                notationCollector.reportError(
                    slew.first,
                    "The STANDARD \"${ioStandard.name}\" does not support the slew value \"${slew.second.value}\". Expected ${
                        supportedSlews.joinToOrList()
                    }."
                )
                return
            }
        }

        addConstraint(
            Constraint(pin, signal, ctx, attributes.values.toList()),
            ctx.pinName() ?: ctx,
            ctx.portName() ?: ctx
        )
    }


}