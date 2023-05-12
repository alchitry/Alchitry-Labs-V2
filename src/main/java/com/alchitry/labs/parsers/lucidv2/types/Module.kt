package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ModuleContext
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class Module(
    val name: String,
    val parameters: Map<String, Parameter>,
    val ports: Map<String, Port>,
    val context: ModuleContext
)

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean
)