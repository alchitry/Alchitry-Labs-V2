package com.alchitry.labs.parsers.acf

import com.alchitry.labs.hardware.pinout.PinConverter
import com.alchitry.labs.parsers.acf.types.ClockConstraint
import com.alchitry.labs.parsers.acf.types.PinConstraint
import com.alchitry.labs.parsers.acf.types.PinPull
import com.alchitry.labs.parsers.lucidv2.types.Signal
import com.alchitry.labs.parsers.lucidv2.types.SignalOrSubSignal
import com.alchitry.labs.parsers.lucidv2.types.SubSignal

object XdcConverter {
    private val SignalOrSubSignal.fullPortName: String
        get() = when (this) {
            is Signal -> "P_${name}"
            is SubSignal -> "P_${parent.name}[${flatSelectionData.offset}]"
        }

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

    fun convert(
        clockConstraints: List<ClockConstraint>,
        pinConstraints: List<PinConstraint>,
        pinConverter: PinConverter
    ): String = buildString {
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
            append(portName)
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
}