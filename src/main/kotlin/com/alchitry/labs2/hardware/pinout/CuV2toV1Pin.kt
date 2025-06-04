package com.alchitry.labs2.hardware.pinout

import com.alchitry.labs2.parsers.acf.types.Ice40IoStandard

object CuV2toV1Pin : PinConverter {
    override val boardSide = BoardSide.TOP
    override val version = ConverterVersion.V1
    override val standards = Ice40IoStandard.entries
    override fun acfToPin(name: String): CuV2Pin? =
        V2toV1Adapter.entries.firstOrNull { it.name == name }?.let { CuV2Pin.acfToPin(it.v2Pin) }

    override fun bankToVcco(bank: Int): List<String> = when (bank) {
        0, 1, 2, 3 -> listOf("3.3")
        else -> error("Unknown bank: $bank")
    }
}