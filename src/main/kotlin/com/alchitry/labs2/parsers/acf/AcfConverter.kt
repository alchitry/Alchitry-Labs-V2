package com.alchitry.labs2.parsers.acf

import com.alchitry.hardware.Board
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.ConstraintLang
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.builders.IceCubeBuilder
import com.alchitry.labs2.project.builders.IceStormBuilder
import com.alchitry.labs2.project.builders.projectBuilder
import com.alchitry.labs2.project.files.ConstraintFile

data class NativeConstraint(
    val name: String,
    val language: ConstraintLang,
    val contents: String
)


val Board.acfConverter: AcfConverter
    get() = when (this) {
        Board.AlchitryAu, Board.AlchitryAuPlus, Board.AlchitryAuV2, Board.AlchitryPtV2 -> XilinxConverter
        Board.AlchitryCu, Board.AlchitryCuV2 -> LatticeConverter
    }

sealed interface AcfConverter {
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

val SignalOrSubSignal.nativePortName: String
    get() = when (Project.current?.data?.board?.projectBuilder) {
        is IceStormBuilder, is IceCubeBuilder -> flatFullPortName
        else -> fullPortName
    }


private val SignalOrSubSignal.fullPortName: String
    get() = when (this) {
        is Signal -> name
        is SubSignal -> "${parent.name}${selection.toVerilogSelectors()}"
    }

private val SignalOrSubSignal.flatFullPortName: String
    get() = when (this) {
        is Signal -> name
        is SubSignal -> "${parent.name}[${flatSelectionData.offset}]"
    }