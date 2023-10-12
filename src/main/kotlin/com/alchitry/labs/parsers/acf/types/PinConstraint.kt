package com.alchitry.labs.parsers.acf.types

import com.alchitry.labs.parsers.lucidv2.types.SignalOrSubSignal

data class PinConstraint(
    val acfPin: String,
    val port: SignalOrSubSignal,
    val pull: PinPull?
)
