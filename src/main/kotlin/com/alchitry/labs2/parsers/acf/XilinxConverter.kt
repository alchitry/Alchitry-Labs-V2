package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.parsers.acf.types.ClockConstraint
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.acf.types.PinPull
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.Languages


data object XilinxConverter : AcfConverter {
    context (StringBuilder)
    private fun Constraint.toXdc() {
        val portName = port.fullPortName
        append("set_property PACKAGE_PIN ")
        append(pin.fpgaPin)
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
        board: Board,
        constraints: List<Constraint>
    ): List<NativeConstraint> = listOf(
        NativeConstraint(
            name,
            Languages.XDC,
            buildString {
                constraints.forEachIndexed { index, constraint ->
                    constraint.toXdc()

                    if (constraint is ClockConstraint) {
                        val portName = constraint.port.fullPortName
                        append("# ")
                        append(portName)
                        append(" => ")
                        append(constraint.frequency)
                        append("Hz\n")

                        append("create_clock -period ")
                        val nsPeriod = 1000000000.0 / constraint.frequency
                        append(nsPeriod)
                        append(" -name ")
                        append(portName.replace("[", "_").replace("]", "_"))
                        append("_")
                        append(index)
                        append(" -waveform {0.000 5.000} [get_ports ")
                        append(portName)
                        append("]\n")
                    }
                }
            }
        )
    )
}