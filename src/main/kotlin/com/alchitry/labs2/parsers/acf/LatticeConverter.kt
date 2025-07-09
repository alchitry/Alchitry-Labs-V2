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
        val pinConstraints = constraints.filterIsInstance<Constraint.PinConstraint>()
        val sdc = buildString {
            pinConstraints.filter { it.attributes.any { attr -> attr is PinAttribute.Frequency } }
                .forEachIndexed { index, clock ->
                    val frequency = clock.attributes.first { it is PinAttribute.Frequency } as PinAttribute.Frequency
                val portName = clock.port.flatFullPortName
                append("# ")
                append(portName)
                append(" => ")
                    append(frequency.value)
                append("Hz\n")

                append("create_clock -name ")
                append(portName.replace("[", "_").replace("]", "_"))
                append("_")
                append(index)
                append(" -period ")
                    val nsPeriod = 1000000000.0 / frequency.value
                append(nsPeriod)
                append(" [get_ports ")
                append(portName)
                appendLine("]")
            }
        }

        val pcf = buildString {
            constraints.forEach { constraint ->
                when (constraint) {
                    is Constraint.PinConstraint -> {
                        append("set_io ")
                        append(constraint.port.flatFullPortName)
                        append(" ")
                        append(constraint.pin.fpgaPin)
                        val pull = constraint.attributes.firstOfTypeOrNull<PinAttribute.Pull>()
                        if (pull != null && pull.value == PinPull.Up) {
                            append(" -pullup yes")
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

        return listOf(NativeConstraint(name, Languages.PCF, pcf), NativeConstraint(name, Languages.SDC, sdc))
    }
}