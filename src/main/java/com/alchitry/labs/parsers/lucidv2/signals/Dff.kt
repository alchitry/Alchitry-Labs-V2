package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.Value

data class Dff(
    override val name: String,
    val init: Value,
    val iob: Boolean = false,
    val clkCtx: ExprContext,
    val rstCtx: ExprContext?
) : Named, SignalParent {
    val d = Signal("d", SignalDirection.Write, init.asMutable())
    val q = Signal("q", SignalDirection.Read, init.asMutable())

    override fun getSignal(name: String): Signal? =
        when (name) {
            "d" -> d
            "q" -> q
            else -> null
        }
}
