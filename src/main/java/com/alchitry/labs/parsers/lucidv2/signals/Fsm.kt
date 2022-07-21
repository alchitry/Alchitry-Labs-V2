package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.Value

data class Fsm(
    override val name: String,
    override val init: Value,
    override val clkCtx: ExprContext,
    override val rstCtx: ExprContext?,
    val states: List<Constant>
) : Named, SyncLogic {
    override var value: Value = init
        set(value) {
            check(init.signalWidth == value.signalWidth) { "Fsm assigned value does not match its size!" }
            field = value
        }
}