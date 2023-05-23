package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

sealed interface PortInstance {
    val name: String
    val width: SignalWidth
    val signed: Boolean

    val internal: Signal
    val external: Signal
}