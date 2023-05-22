package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent

sealed interface PortOrInterfaceInstance {
    val name: String

    val internal: SignalOrParent
    val external: SignalOrParent
}