package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.ConstraintLang
import com.alchitry.labs2.project.files.ConstraintFile

data class NativeConstraint(
    val name: String,
    val language: ConstraintLang,
    val contents: String
)

sealed interface AcfConverter {
    private fun SignalSelection.toVerilogSelectors() = buildString {
        this@toVerilogSelectors.forEach { selector ->
            when (selector) {
                is SignalSelector.Bits -> {
                    check(selector.context is SelectionContext.Constant) { "Constraint selector is not constant!" }
                    append("[${selector.range.first}]")
                }

                is SignalSelector.Struct -> append(".${selector.member}")
            }
        }
    }

    val SignalOrSubSignal.fullPortName: String
        get() = when (this) {
            is Signal -> name
            is SubSignal -> "${parent.name}${selection.toVerilogSelectors()}"
        }

    val SignalOrSubSignal.flatFullPortName: String
        get() = when (this) {
            is Signal -> name
            is SubSignal -> "${parent.name}[${flatSelectionData.offset}]"
        }

    suspend fun convert(
        context: ProjectContext,
        board: Board,
        file: ConstraintFile,
        topModule: ModuleInstance,
        notationCollector: NotationCollector,
    ): List<NativeConstraint>? {
        val extractor = AcfExtractor.extract(context, file, topModule, board, notationCollector) ?: return null
        val name = file.name.split(".").first()
        return convert(name, board, extractor.constraints)
    }

    suspend fun convert(
        name: String,
        board: Board,
        constraints: List<Constraint>
    ): List<NativeConstraint>
}