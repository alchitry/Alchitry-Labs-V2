package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class Module(
    val name: String,
    val parameters: Map<String, Parameter>,
    val ports: Map<String, Port>
)

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth
)