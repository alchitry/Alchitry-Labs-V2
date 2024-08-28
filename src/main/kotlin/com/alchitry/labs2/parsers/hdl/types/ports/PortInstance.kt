package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.SignalWidth

sealed interface PortInstance {
    val name: String
    val width: SignalWidth
    val signed: Boolean

    val internal: Signal
    val external: Signal
}