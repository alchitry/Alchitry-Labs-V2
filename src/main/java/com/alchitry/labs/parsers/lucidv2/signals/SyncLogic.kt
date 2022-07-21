package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.Value

interface SyncLogic {
    val init: Value
    val clkCtx: ExprContext
    val rstCtx: ExprContext?
    var value: Value
}