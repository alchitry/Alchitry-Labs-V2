package com.alchitry.labs2.parsers.acf

import com.alchitry.labs2.parsers.acf.types.ClockConstraint
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.Languages

data object LatticeConverter : AcfConverter {
    override suspend fun convert(
        name: String,
        board: Board,
        constraints: List<Constraint>
    ): List<NativeConstraint> {
        val sdc = buildString {
            constraints.mapNotNull { it as? ClockConstraint }.forEachIndexed { index, clock ->
                val portName = clock.port.flatFullPortName
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
            constraints.forEach { constraint ->
                append("set_io ")
                append(constraint.port.flatFullPortName)
                append(" ")
                append(constraint.pin.fpgaPin)
                appendLine()
            }
        }

        return listOf(NativeConstraint(name, Languages.PCF, pcf), NativeConstraint(name, Languages.SDC, sdc))
    }
}