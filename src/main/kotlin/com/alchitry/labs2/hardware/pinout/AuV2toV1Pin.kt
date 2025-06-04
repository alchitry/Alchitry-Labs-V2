package com.alchitry.labs2.hardware.pinout

import com.alchitry.labs2.parsers.acf.types.Artix7IoStandard

object AuV2toV1Pin : PinConverter {
    override val boardSide = BoardSide.TOP
    override val version = ConverterVersion.V1
    override val standards = Artix7IoStandard.entries
    override fun acfToPin(name: String): AuV2Pin? =
        V2toV1Adapter.entries.firstOrNull { it.name == name }?.let { AuV2Pin.acfToPin(it.v2Pin) }

    override fun bankToVcco(bank: Int): List<String> = when (bank) {
        0 -> emptyList()
        15 -> listOf("1.35")
        14, 35 -> listOf("3.3")
        34 -> listOf("3.3", "2.5")
        else -> error("Unknown bank: $bank")
    }
}