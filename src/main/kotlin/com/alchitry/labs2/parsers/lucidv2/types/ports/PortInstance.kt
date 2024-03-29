package com.alchitry.labs2.parsers.lucidv2.types.ports

import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.values.SignalWidth

sealed interface PortInstance {
    val name: String
    val width: SignalWidth
    val signed: Boolean

    val internal: Signal
    val external: Signal
}