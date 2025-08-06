package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.firstOfTypeOrNull
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.hardware.pinout.BoardSide
import com.alchitry.labs2.hardware.pinout.PinConverter
import com.alchitry.labs2.joinToOrString
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.*
import com.alchitry.labs2.parsers.grammar.AcfBaseListener
import com.alchitry.labs2.parsers.grammar.AcfLexer
import com.alchitry.labs2.parsers.grammar.AcfParser
import com.alchitry.labs2.parsers.grammar.AcfParser.*
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.parsers.notations.NotationType
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.ui.code_editor.textPositionAtOffset
import org.antlr.v4.kotlinruntime.CharStreams
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
        val acfNativeRegex = Regex("""\b(?:acf_pin|acf_port)\s*\([^)]+\)""")

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
                    (tokenStream.tokenSource as? AcfLexer)?.addErrorListener(notationCollector)
                        ?: error("TokenSource was not an AcfLexer!")
                    removeErrorListeners()
                    addErrorListener(notationCollector)
                }

            val tree = parser.source()

            if (notationCollector.hasErrors) {
                return null
            }

            val extractor = AcfExtractor(context, topModule, board, notationCollector)
            ParseTreeWalker.DEFAULT.walk(extractor, tree)

            if (notationCollector.hasErrors) {
                return null
            }

            return extractor
        }
    }

    override fun enterSource(ctx: SourceContext) {
        blockAttributes.clear()
    }

    override fun exitNativeBlock(ctx: NativeBlockContext) {
        val offsetIndex = ctx.text.indexOf("{")
        if (offsetIndex == -1) {
            return
        }
        val nativeText = ctx.text.substring(offsetIndex + 1, ctx.text.length - 1)
        val convertedText = acfNativeRegex.replace(nativeText) { match ->
            val startIdx = match.value.indexOf("(") + 1
            val endIdx = match.value.lastIndexOf(")")
            require(startIdx != -1 && endIdx != -1) { "Failed to find () in ACF native block match!" }
            val body = match.value.substring(startIdx, endIdx)
            when {
                match.value.startsWith("acf_port") -> {
                    val offset =
                        ctx.NATIVE_BLOCK()?.symbol?.textPositionAtOffset(startIdx + match.range.first + offsetIndex + 1)
                            ?: return@replace ""
                    notationCollector.withOffset(offset) {
                        val parser = AcfParser(
                            CommonTokenStream(
                                AcfLexer(
                                    CharStreams.fromString(body, "acf_port")
                                ).also { it.removeErrorListeners() }
                            )
                        ).apply {
                            (tokenStream.tokenSource as? AcfLexer)?.addErrorListener(notationCollector)
                                ?: error("TokenSource was not an AcfLexer!")
                            removeErrorListeners()
                            addErrorListener(notationCollector)
                        }
                        val tree = parser.portName()
                        if (notationCollector.hasErrors) {
                            return@withOffset ""
                        }
                        ParseTreeWalker.DEFAULT.walk(this, tree)
                        val signal = signals[tree]
                        val port = signal?.flatFullPortName
                        if (port == null) {
                            notationCollector.reportError(
                                tree,
                                "Unknown port name \"${tree.name(0)?.text}\"."
                            )
                            return@withOffset ""
                        }
                        port
                    }
                }

                match.value.startsWith("acf_pin") -> {
                    val bodyOffset = startIdx + match.range.first + offsetIndex + 1
                    val startPosition =
                        ctx.NATIVE_BLOCK()?.symbol?.textPositionAtOffset(bodyOffset) ?: return@replace ""
                    val endPosition =
                        ctx.NATIVE_BLOCK()?.symbol?.textPositionAtOffset(bodyOffset + body.length) ?: return@replace ""
                    val converter = currentConverter(blockAttributes.flatMap { it.values })
                    if (converter == null) {
                        notationCollector.addNotation(
                            Notation(
                                "Failed to resolve a pin converter!",
                                startPosition..endPosition,
                                NotationType.Error
                            )
                        )
                        return@replace ""
                    }
                    val pin = converter.acfToPin(body)
                    if (pin == null) {
                        notationCollector.addNotation(
                            Notation(
                                "ACF pin \"$body\" not found!",
                                startPosition..endPosition,
                                NotationType.Error
                            )
                        )
                        ""
                    } else {
                        pin.fpgaPin
                    }
                }

                else -> error("Match doesn't start with acf_port or acf_pin!")
            }
        }.trim()
        Constraint.NativeBlockConstraint(convertedText, ctx).also {
            context.addConstraint(it)
            constraints.add(it)
        }
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

    private fun currentConverter(attributes: Collection<PinAttribute> = blockAttributes.flatMap { it.values }): PinConverter? {
        val version = attributes.firstOfTypeOrNull<PinAttribute.Pinout>()?.value ?: board.pinConverters.first().version
        val side = attributes.firstOfTypeOrNull<PinAttribute.Side>()?.value ?: BoardSide.TOP
        return board.pinConverters.firstOrNull { it.version == version && it.boardSide == side }
    }

    private fun parseAttribute(ctx: AttributeContext): PinAttribute? {
        val name = ctx.BASIC_NAME()?.text ?: return null
        val valueText = ctx.attributeValue()?.text ?: return null
        when (name) {
            "SIDE" -> {
                val sides = board.pinConverters.map { it.boardSide }.distinct()
                sides.firstOrNull { it.name == valueText }?.let {
                    return PinAttribute.Side(it)
                }
                notationCollector.reportError(
                    ctx,
                    "Invalid SIDE value \"$valueText\". Expected ${sides.joinToOrString { "\"${it.name}\"" }}."
                )
                return null
            }

            "PINOUT" -> {
                board.pinConverters.firstOrNull { it.version.name == valueText }?.let {
                    return PinAttribute.Pinout(it.version)
                }
                notationCollector.reportError(
                    ctx,
                    "Invalid PINOUT value \"$valueText\". Expected ${board.pinConverters.joinToOrString { "\"${it.version.name}\"" }}."
                )
                return null
            }

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
                val converter = currentConverter() ?: board.pinConverters.first()
                val standard = converter.standards.firstOrNull { it.name == valueText }
                if (standard == null) {
                    notationCollector.reportError(
                        ctx.attributeValue() ?: ctx,
                        "Invalid STANDARD value \"$valueText\". Expected ${
                            converter.standards.map { "\"${it.name}\"" }.joinToOrString()
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
                            PinSlew.entries.map { "\"${it.name}\"" }.joinToOrString()
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

            "DIFF_TERM" -> {
                val value = when (valueText) {
                    "TRUE" -> true
                    "FALSE" -> false
                    else -> {
                        notationCollector.reportError(
                            ctx.attributeValue() ?: ctx,
                            "Invalid DIFF_TERM value \"$valueText\". Expected TRUE or FALSE."
                        )
                        return null
                    }
                }
                return PinAttribute.DiffTerm(value)
            }

            else -> {
                notationCollector.reportError(ctx, "Unknown attribute \"$name\".")
                return null
            }
        }
    }

    override fun enterAttributeBlock(ctx: AttributeBlockContext) {
        val attributeMap = mutableMapOf<AttributeContext, PinAttribute>()
        blockAttributes.add(attributeMap)
        ctx.attribute().forEach { attrCtx ->
            val attr = parseAttribute(attrCtx) ?: return@forEach
            if (blockAttributes.any { block -> block.any { it.value.name == attr.name } }) {
                notationCollector.reportError(ctx, "The attribute \"${attr.name}\" has already been defined.")
            }
            attributeMap[attrCtx] = attr
        }
    }

    override fun exitAttributeBlock(ctx: AttributeBlockContext) {
        blockAttributes.removeLast()
    }

    private fun addConstraint(
        pinConstraint: Constraint.PinConstraint,
        pinContext: ParserRuleContext,
        portContext: ParserRuleContext
    ) {
        when (context.addConstraint(pinConstraint)) {
            ProjectContext.AddConstraintResult.Success -> constraints.add(pinConstraint)
            ProjectContext.AddConstraintResult.PinTaken -> notationCollector.reportError(
                pinContext,
                "The pin \"${pinConstraint.pin.name}\" has already been connected!"
            )

            ProjectContext.AddConstraintResult.PortTaken -> notationCollector.reportError(
                portContext,
                "The port \"${pinConstraint.port}\" has already been connected!"
            )
        }
    }

    override fun exitPortName(ctx: PortNameContext) {
        val name = ctx.name(0)?.text ?: return
        val port = topModule.ports[name] ?: return
        val children =
            ctx.children?.filter { it is NameContext || it is ArrayIndexContext } ?: emptyList()
        val selectionMap = children.subList(1, children.size).map {
            when (it) {
                is NameContext -> SignalSelector.Struct(it.text) to it
                is ArrayIndexContext -> SignalSelector.Bits(
                    (it.INT() ?: return).text.toInt()
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
        val signal = signals[ctx.portName() ?: return]
        if (signal == null) {
            notationCollector.reportWarning(
                ctx.portName() ?: ctx,
                "Unknown port name \"${ctx.portName()?.name(0)?.text}\". This constraint will be ignored."
            )
            return
        }
        val pinName = ctx.pinName()?.text ?: return

        val attributes = mutableMapOf<AttributeContext, PinAttribute>()
        blockAttributes.forEach { attributes.putAll(it) }
        ctx.attribute().forEach { attributeContext ->
            parseAttribute(attributeContext)?.let { attr ->
                if (attributes.any { it.value.name == attr.name }) {
                    notationCollector.reportError(
                        attributeContext,
                        "The attribute \"${attr.name}\" has already been defined."
                    )
                }
                attributes[attributeContext] = attr
            }
        }

        val converter = currentConverter(attributes.values)
        if (converter == null) {
            val version =
                attributes.values.firstOfTypeOrNull<PinAttribute.Pinout>()?.value ?: board.pinConverters.first().version
            val side = attributes.values.firstOfTypeOrNull<PinAttribute.Side>()?.value ?: BoardSide.TOP
            notationCollector.reportError(
                ctx.pinName() ?: ctx,
                "Failed to find a compatible pin converter for pinout $version on side $side of the board ${board.name}."
            )
            return
        }
        val pin = converter.acfToPin(pinName)
        if (pin == null) {
            notationCollector.reportError(
                ctx.pinName() ?: ctx,
                "Pin \"$pinName\" does not exist in pinout ${converter.version.name} on side ${converter.boardSide.name} of the ${board.name}."
            )
            return
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

        val bankVccos = converter.bankToVcco(pin.bank)
        val filteredVcco = context.getConstraints().filterIsInstance<Constraint.PinConstraint>().firstNotNullOfOrNull {
            if (pin.bank != it.pin.bank)
                return@firstNotNullOfOrNull null
            val pinStd =
                it.attributes.firstOfTypeOrNull<PinAttribute.Standard>()?.value ?: return@firstNotNullOfOrNull null
            val diffTerm =
                it.attributes.firstOfTypeOrNull<PinAttribute.DiffTerm>()?.value ?: false
            if (it.port.direction != SignalDirection.Write && (pinStd.inputAnyVcco && !diffTerm))
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
                        bankVccos.joinToOrString()
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
                        supportedDrives.joinToOrString()
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
                        supportedSlews.joinToOrString()
                    }."
                )
                return
            }
        }

        val diffTerm = attributes.firstOfType<PinAttribute.DiffTerm>()
        if (diffTerm != null) {
            if (!ioStandard.diffTerm) {
                notationCollector.reportError(
                    diffTerm.first,
                    "The STANDARD \"${ioStandard.name}\" does not support differential termination."
                )
                return
            }
            if (diffTerm.second.value && filteredVcco != null && ioStandard.vcco != filteredVcco) {
                notationCollector.reportError(
                    diffTerm.first,
                    "Differential termination requires that VCCO be set to ${ioStandard.vcco} but VCCO is ${filteredVcco}."
                )
                return
            }
        }

        addConstraint(
            Constraint.PinConstraint(pin, signal, ctx, attributes.values.toList()),
            ctx.pinName() ?: ctx,
            ctx.portName() ?: ctx
        )
    }


}