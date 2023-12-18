package com.alchitry.labs.parsers.acf

import com.alchitry.labs.parsers.acf.types.ClockConstraint
import com.alchitry.labs.parsers.acf.types.PinConstraint
import com.alchitry.labs.parsers.errors.NotationCollector
import com.alchitry.labs.parsers.grammar.AcfLexer
import com.alchitry.labs.parsers.grammar.AcfParser
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.Signal
import com.alchitry.labs.parsers.lucidv2.types.SignalOrSubSignal
import com.alchitry.labs.parsers.lucidv2.types.SubSignal
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.ConstraintLang
import com.alchitry.labs.project.files.ConstraintFile
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

    suspend fun convert(
        file: ConstraintFile,
        topModule: ModuleInstance,
        notationCollector: NotationCollector,
    ): List<NativeConstraint>? {
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

        val extractor = AcfExtractor(topModule, board, notationCollector)
        ParseTreeWalker.walk(extractor, tree)
        val name = file.name.split(".").first()
        return convert(name, extractor.clocks, extractor.pins)
    }

    suspend fun convert(
        name: String,
        clockConstraints: List<ClockConstraint>,
        pinConstraints: List<PinConstraint>
    ): List<NativeConstraint>
}