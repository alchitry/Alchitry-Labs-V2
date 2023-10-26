package com.alchitry.labs.parsers.acf

import com.alchitry.labs.parsers.acf.types.ClockConstraint
import com.alchitry.labs.parsers.acf.types.PinConstraint
import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.parsers.grammar.AcfLexer
import com.alchitry.labs.parsers.grammar.AcfParser
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.Signal
import com.alchitry.labs.parsers.lucidv2.types.SignalOrSubSignal
import com.alchitry.labs.parsers.lucidv2.types.SubSignal
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.ConstraintLang
import com.alchitry.labs.project.files.ConstraintFile
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker

data class NativeConstraint(
    val name: String,
    val language: ConstraintLang,
    val contents: String
)

sealed interface AcfConverter {
    val SignalOrSubSignal.fullPortName: String
        get() = when (this) {
            is Signal -> "P_${name}"
            is SubSignal -> "P_${parent.name}[${flatSelectionData.offset}]"
        }

    val board: Board

    fun convert(
        file: ConstraintFile,
        topModule: ModuleInstance,
        errorCollector: ErrorCollector,
    ): List<NativeConstraint>? {
        val parser =
            AcfParser(
                CommonTokenStream(
                    AcfLexer(
                        CharStreams.fromStream(file.file.inputStream())
                    ).also { it.removeErrorListeners() }
                )
            ).apply {
                (tokenStream?.tokenSource as? AcfLexer)?.addErrorListener(errorCollector)
                    ?: error("TokenSource was not an AcfLexer!")
                removeErrorListeners()
                addErrorListener(errorCollector)
            }

        val tree = parser.source()

        if (errorCollector.hasErrors) {
            return null
        }

        val extractor = AcfExtractor(topModule, board, errorCollector)
        ParseTreeWalker.walk(extractor, tree)
        val name = file.name.split(".").first()
        return convert(name, extractor.clocks, extractor.pins)
    }

    fun convert(
        name: String,
        clockConstraints: List<ClockConstraint>,
        pinConstraints: List<PinConstraint>
    ): List<NativeConstraint>
}