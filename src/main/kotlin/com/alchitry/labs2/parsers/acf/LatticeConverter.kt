package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.parsers.acf.types.ClockConstraint
import com.alchitry.labs2.parsers.acf.types.PinConstraint
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.Languages

class LatticeConverter(override val board: Board) : AcfConverter {
    override suspend fun convert(
        name: String,
        clockConstraints: List<ClockConstraint>,
        pinConstraints: List<PinConstraint>
    ): List<NativeConstraint> {
        val pinConverter = board.pinConverter
        val sdc = buildString {
            clockConstraints.forEachIndexed { index, clock ->
                val portName = clock.pinConstraint.port.fullPortName
                append("# ")
                append(portName)
                append(" => ")
                append(clock.frequency)
                append("Hz\n")

                append("create_clock -name ")
                append(portName.replace("[", "_").replace("]", "_"))
                append("_")
                append(index)
                append(" -period ")
                val nsPeriod = 1000000000.0 / clock.frequency
                append(nsPeriod)
                append(" [get_ports ")
                append(portName)
                appendLine("]")
            }
        }

        val pcf = buildString {
            (clockConstraints.map { it.pinConstraint } + pinConstraints).forEach { pin ->
                append("set_io ")
                append(pin.port.fullPortName)
                append(" ")
                append(pinConverter.AcfToFPGAPin(pin.acfPin))
                appendLine()
            }
        }

        return listOf(NativeConstraint(name, Languages.PCF, pcf), NativeConstraint(name, Languages.SDC, sdc))
    }
}