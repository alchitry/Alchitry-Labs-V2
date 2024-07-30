package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.grammar.LucidParser.ExprContext
import com.alchitry.labs2.parsers.hdl.lucidv2.values.Value

data class Parameter(
    val name: String,
    val default: Value?,
    val defaultTestOnly: Boolean,
    val constraint: ExprContext?
)
