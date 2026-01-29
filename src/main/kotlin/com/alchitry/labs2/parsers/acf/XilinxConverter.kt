package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.firstOfTypeOrNull
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.acf.types.PinAttribute
import com.alchitry.labs2.parsers.acf.types.PinPull
import com.alchitry.labs2.project.Languages


data object XilinxConverter : AcfConverter {
    context (builder: StringBuilder)
    private fun Constraint.PinConstraint.toXdc() {
        val portName = port.nativePortName
        builder.append("set_property PACKAGE_PIN ")
        builder.append(pin.fpgaPin)
        builder.append(" [get_ports {")
        builder.append(portName)
        builder.append("}]\n")

        attributes.firstOfTypeOrNull<PinAttribute.Standard>()?.let { standard ->
            builder.append("set_property IOSTANDARD ")
            builder.append(standard.value.name)
            builder.append(" [get_ports {")
            builder.append(portName)
            builder.append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.Pull>()?.let { pull ->
            builder.append("set_property ")
            when (pull.value) {
                PinPull.Up -> builder.append("PULLUP")
                PinPull.Down -> builder.append("PULLDOWN")
                PinPull.Keep -> builder.append("KEEPER")
            }
            builder.append(" true [get_ports {")
            builder.append(portName)
            builder.append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.Drive>()?.let { drive ->
            builder.append("set_property DRIVE ")
            builder.append(drive.value)
            builder.append(" [get_ports {")
            builder.append(portName)
            builder.append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.Slew>()?.let { slew ->
            builder.append("set_property SLEW ")
            builder.append(slew.value.name)
            builder.append(" [get_ports {")
            builder.append(portName)
            builder.append("}]\n")
        }

        attributes.firstOfTypeOrNull<PinAttribute.DiffTerm>()?.let { diffTerm ->
            builder.append("set_property DIFF_TERM ")
            builder.append(if (diffTerm.value) "TRUE" else "FALSE")
            builder.append(" [get_ports {")
            builder.append(portName)
            builder.append("}]\n")
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
                    when (constraint) {
                        is Constraint.PinConstraint -> {
                            constraint.toXdc()

                            constraint.attributes.firstOfTypeOrNull<PinAttribute.Frequency>()?.let { frequency ->
                                val portName = constraint.port.nativePortName
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

                        is Constraint.NativeBlockConstraint -> {
                            append(constraint.block)
                            appendLine()
                        }
                    }

                }
            }
        )
    )
}