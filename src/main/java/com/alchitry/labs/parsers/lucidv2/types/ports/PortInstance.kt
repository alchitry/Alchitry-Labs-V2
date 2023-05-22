package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

sealed interface PortInstance : PortOrInterfaceInstance {
    val width: SignalWidth
    val signed: Boolean

    override val internal: Signal
    override val external: Signal
}