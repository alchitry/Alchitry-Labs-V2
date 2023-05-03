package com.alchitry.labs.parsers.lucidv2.signals

data class ModuleInstance(
    val module: Module,
    val parameters: Map<String, Signal>,
    val ports: Map<String, Signal>
)
