package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.NameContext
import com.alchitry.labs.parsers.lucidv2.signals.DynamicExpr

data class Connection(val portCtx: NameContext, val value: DynamicExpr) {
    val port: String = portCtx.text
}
data class AssignmentBlock(val signals: List<Connection>, val params: List<Connection>)