package com.alchitry.labs2.hardware.pinout

import com.alchitry.labs2.parsers.acf.types.IoStandard

enum class ConverterVersion {
    V1, V2
}

interface PinConverter {
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
