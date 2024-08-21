package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalOrSubSignal
import com.alchitry.labs2.parsers.hdl.types.SubSignal
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.ConstraintLang
import com.alchitry.labs2.project.files.ConstraintFile

data class NativeConstraint(
    val name: String,
    val language: ConstraintLang,
    val contents: String
)

sealed interface AcfConverter {
    val SignalOrSubSignal.fullPortName: String
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