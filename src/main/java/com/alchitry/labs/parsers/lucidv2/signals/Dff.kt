package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

data class Dff(
    private val context: ProjectContext,
    override val name: String,
    val init: Value,
    val clk: DynamicExpr,
    val rst: DynamicExpr?,
    val signed: Boolean
) : SignalParent, Evaluable {
    val d = Signal("d", SignalDirection.Write, this, init.asMutable(), signed)
    val q = Signal("q", SignalDirection.Read, this, init.asMutable(), signed)
    private var lastClk: Bit = clk.value.flatten().lsb

    init {
        context.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            clk.valueFlow.collect {
                context.queueEvaluation(this@Dff)
            }
        }
    }

    override fun getSignal(name: String): Signal? =
        when (name) {
            "d" -> d
            "q" -> q
            else -> null
        }

    override suspend fun evaluate() {
        val clkValue = clk.value
        require(clkValue is SimpleValue) { "Dff clk was not a SimpleValue!" }
        if (lastClk == Bit.B0 && clkValue.lsb == Bit.B1) { // rising edge
            q.set(d.get(null))
        } else if (!lastClk.isNumber() || !clkValue.isNumber()) {
            q.set(q.width.filledWith(Bit.Bx, false, q.signed))
        }
        lastClk = clkValue.lsb
    }
}
