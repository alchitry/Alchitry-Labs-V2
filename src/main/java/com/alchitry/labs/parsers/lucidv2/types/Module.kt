package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ModuleContext
import com.alchitry.labs.parsers.lucidv2.types.ports.Port

data class Module(
    val name: String,
    val parameters: Map<String, Parameter>,
    val ports: Map<String, Port>,
    val context: ModuleContext
)

