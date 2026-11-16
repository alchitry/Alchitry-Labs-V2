package com.alchitry.labs2.hardware.pinout

import com.alchitry.labs2.parsers.acf.types.Artix7IoStandard

object PtV2toV1Pin : PinConverter {
    override val boardSide = BoardSide.TOP
    override val version = ConverterVersion.V1
    override val standards = Artix7IoStandard.entries
    override fun acfToPin(name: String): PtV2TopPin? =
        V2toV1Adapter.entries.firstOrNull { it.name == name }?.let { PtV2TopPin.acfToPin(it.v2Pin) }

    override fun bankToVcco(bank: Int): List<String> = PtV2TopPin.bankToVcco(bank)
}