package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.hdl.lucidv2.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.types.Signal

sealed interface PortInstance {
    val name: String
    val width: SignalWidth
    val signed: Boolean

    val internal: Signal
    val external: Signal
}