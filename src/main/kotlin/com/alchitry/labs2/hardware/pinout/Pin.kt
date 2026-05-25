package com.alchitry.labs2.hardware.pinout

import com.alchitry.hardware.Board
import com.alchitry.labs2.parsers.acf.types.IoStandard

enum class ConverterVersion {
    V1, V2, PT_ALPHA
}

enum class BoardSide {
    TOP,
    BOTTOM
}

val Board.pinConverters: List<PinConverter>
    get() = when (this) {
        Board.AlchitryAu, Board.AlchitryAuPlus -> listOf(AuPin)
        Board.AlchitryAuV2 -> listOf(AuV2Pin, AuV2toV1Pin)
        Board.AlchitryCu -> listOf(CuPin)
        Board.AlchitryCuV2 -> listOf(CuV2Pin, CuV2toV1Pin)
        Board.AlchitryPtV2 -> listOf(PtV2TopPin, PtV2BottomPin, PtV2AlphaTopPin)
    }

interface PinConverter {
    val boardSide: BoardSide
    val version: ConverterVersion
    fun acfToPin(name: String): Pin?
    val standards: List<IoStandard>

    fun bankToVcco(bank: Int): List<String>
}

interface Pin {
    val name: String
    val fpgaPin: String
    val bank: Int
}

sealed interface PinAttributes {
    val name: String

    object Pull : PinAttributes {
        override val name = "PULL"

    }
}
