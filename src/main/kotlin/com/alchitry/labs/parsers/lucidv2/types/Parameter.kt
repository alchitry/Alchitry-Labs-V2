package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.Value

data class Parameter(
    val name: String,
    val default: Value?,
    val defaultTestOnly: Boolean,
    val constraint: ExprContext?
)