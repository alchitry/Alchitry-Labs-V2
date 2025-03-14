package com.alchitry.labs2.parsers.acf.types

import com.alchitry.labs2.hardware.pinout.Pin
import com.alchitry.labs2.hardware.pinout.PinConverter
import com.alchitry.labs2.parsers.hdl.types.SignalOrSubSignal
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class Constraint(
    val pin: Pin,
    val port: SignalOrSubSignal,
    val context: ParserRuleContext,
    val attributes: List<PinAttribute>
)

sealed interface PinAttribute {
    val name: String

    data class Pinout(val value: PinConverter) : PinAttribute {
        override val name: String = "PINOUT"
    }

    data class Pull(val value: PinPull) : PinAttribute {
        override val name: String = "PULL"
    }

    data class Standard(val value: IoStandard) : PinAttribute {
        override val name: String = "STANDARD"
    }

    data class Drive(val value: Int) : PinAttribute {
        override val name: String = "DRIVE"
    }

    data class Slew(val value: PinSlew) : PinAttribute {
        override val name: String = "SLEW"
    }

    data class Frequency(val value: Int) : PinAttribute {
        override val name: String = "FREQUENCY"
    }

    data class DiffTerm(val value: Boolean) : PinAttribute {
        override val name: String = "DIFF_TERM"
    }
}

