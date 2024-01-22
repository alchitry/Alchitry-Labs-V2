package com.alchitry.labs2.parsers.acf.types

import com.alchitry.labs2.parsers.lucidv2.types.SignalOrSubSignal

data class PinConstraint(
    val acfPin: String,
    val port: SignalOrSubSignal,
    val pull: PinPull?
)
