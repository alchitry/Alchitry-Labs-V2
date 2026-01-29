package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.firstOfTypeOrNull
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.acf.types.PinAttribute
import com.alchitry.labs2.parsers.acf.types.PinPull
import com.alchitry.labs2.project.Languages

data object LatticeConverter : AcfConverter {
    override suspend fun convert(
        name: String,
        board: Board,
        constraints: List<Constraint>
    ): List<NativeConstraint> {
        var index = 0
        val sdc = buildString {
            constraints.forEach { constraint ->
                when (constraint) {
                    is Constraint.PinConstraint -> {
                        if (constraint.attributes.any { attr -> attr is PinAttribute.Frequency }) {
                            val frequency =
                                constraint.attributes.first { it is PinAttribute.Frequency } as PinAttribute.Frequency
                            val portName = constraint.port.nativePortName
                            append("# ")
                            append(portName)
                            append(" => ")
                            append(frequency.value)
                            append("Hz\n")

                            append("create_clock -name ")
                            append(portName.replace("[", "_").replace("]", "_"))
                            append("_")
                            append(index++)
                            append(" -period ")
                            val nsPeriod = 1000000000.0 / frequency.value
                            append(nsPeriod)
                            append(" [get_ports ")
                            append(portName)
                            appendLine("]")
                        }
                    }

                    is Constraint.NativeBlockConstraint -> {
                        append(constraint.block)
                        appendLine()
                    }
                }
            }
        }

        val pcf = buildString {
            constraints.filterIsInstance<Constraint.PinConstraint>().forEach { constraint ->
                append("set_io ")
                append(constraint.port.nativePortName)
                append(" ")
                append(constraint.pin.fpgaPin)
                val pull = constraint.attributes.firstOfTypeOrNull<PinAttribute.Pull>()
                if (pull != null && pull.value == PinPull.Up) {
                    append(" -pullup yes")
                }
                appendLine()
            }
        }

        return listOf(NativeConstraint(name, Languages.PCF, pcf), NativeConstraint(name, Languages.SDC, sdc))
    }
}