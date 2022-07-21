package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.Value

data class Dff(
    override val name: String,
    override val init: Value,
    val iob: Boolean = false,
    override val clkCtx: ExprContext,
    override val rstCtx: ExprContext?
) : Named, SyncLogic {
    override var value: Value = init
        set(value) {
            check(init.signalWidth == value.signalWidth) { "Dff assigned value does not match its size!" }
            field = value
        }
}