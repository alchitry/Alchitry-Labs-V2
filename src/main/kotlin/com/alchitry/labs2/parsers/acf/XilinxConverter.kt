package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.firstOfTypeOrNull
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.acf.types.PinAttribute
import com.alchitry.labs2.parsers.acf.types.PinPull
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

        attributes.firstOfTypeOrNull<PinAttribute.Standard>()?.let { standard ->
            append("set_property IOSTANDARD ")
            append(standard.value.name)
            append(" [get_ports {")
            append(portName)
            append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.Pull>()?.let { pull ->
            append("set_property ")
            when (pull.value) {
                PinPull.Up -> append("PULLUP")
                PinPull.Down -> append("PULLDOWN")
                PinPull.Keep -> append("KEEPER")
            }
            append(" true [get_ports {")
            append(portName)
            append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.Drive>()?.let { drive ->
            append("set_property DRIVE ")
            append(drive.value)
            append(" [get_ports {")
            append(portName)
            append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.Slew>()?.let { slew ->
            append("set_property SLEW ")
            append(slew.value.name)
            append(" [get_ports {")
            append(portName)
            append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.DiffTerm>()?.let { diffTerm ->
            append("set_property DIFF_TERM ")
            append(if (diffTerm.value) "TRUE" else "FALSE")
            append(" [get_ports {")
            append(portName)
            append("}]\n")
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

                    constraint.attributes.firstOfTypeOrNull<PinAttribute.Frequency>()?.let { frequency ->
                        val portName = constraint.port.fullPortName
                        val clockName = "${portName.replace("[", "_").replace("]", "_")}_$index"
                        append("# ")
                        append(portName)
                        append(" => ")
                        append(frequency.value)
                        append("Hz\n")

                        append("create_clock -period ")
                        val nsPeriod = 1000000000.0 / frequency.value
                        append(nsPeriod)
                        append(" -name ")
                        append(clockName)
                        append(" -waveform {0.000 ${nsPeriod / 2.0}} [get_ports ")
                        append(portName)
                        append("]\n")
                        append("set_clock_groups -asynchronous -group [")
                        append("get_clocks -include_generated_clocks ")
                        append(clockName)
                        append("]\n")
                    }
                    appendLine()
                }
            }
        )
    )
}