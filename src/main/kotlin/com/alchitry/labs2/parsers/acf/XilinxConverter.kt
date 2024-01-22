package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.hardware.pinout.PinConverter
import com.alchitry.labs2.parsers.acf.types.ClockConstraint
import com.alchitry.labs2.parsers.acf.types.PinConstraint
import com.alchitry.labs2.parsers.acf.types.PinPull
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.Languages


class XilinxConverter(override val board: Board) : AcfConverter {
    context (StringBuilder)
    private fun PinConstraint.toXdc(pinConverter: PinConverter) {
        val portName = port.fullPortName
        append("set_property PACKAGE_PIN ")
        append(pinConverter.AcfToFPGAPin(acfPin))
        append(" [get_ports {")
        append(portName)
        append("}]\n")

        append("set_property IOSTANDARD LVCMOS33 [get_ports {")
        append(portName)
        append("}]\n")

        when (pull) {
            PinPull.PullUp -> {
                append("set_property PULLUP true [get_ports {")
                append(portName)
                append("}]\n")
            }

            PinPull.PullDown -> {
                append("set_property PULLDOWN true [get_ports {")
                append(portName)
                append("}]\n")
            }

            null -> {}
        }
    }

    override suspend fun convert(
        name: String,
        clockConstraints: List<ClockConstraint>,
        pinConstraints: List<PinConstraint>
    ): List<NativeConstraint> = listOf(
        NativeConstraint(
            name,
            Languages.XDC,
            buildString {
                val pinConverter = board.pinConverter
                clockConstraints.forEachIndexed { index, clock ->
                    clock.pinConstraint.toXdc(pinConverter)
                    val portName = clock.pinConstraint.port.fullPortName
                    append("# ")
                    append(portName)
                    append(" => ")
                    append(clock.frequency)
                    append("Hz\n")

                    append("create_clock -period ")
                    val nsPeriod = 1000000000.0 / clock.frequency
                    append(nsPeriod)
                    append(" -name ")
                    append(portName.replace("[", "_").replace("]", "_"))
                    append("_")
                    append(index)
                    append(" -waveform {0.000 5.000} [get_ports ")
                    append(portName)
                    append("]\n")
                }
                pinConstraints.forEach { pin ->
                    pin.toXdc(pinConverter)
                }
            }
        )
    )
}