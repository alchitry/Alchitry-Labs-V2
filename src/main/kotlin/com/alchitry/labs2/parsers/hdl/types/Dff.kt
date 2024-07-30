package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucidv2.signals.snapshot.SnapshotParent
import com.alchitry.labs2.parsers.hdl.lucidv2.signals.snapshot.Snapshotable
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import com.alchitry.labs2.parsers.hdl.values.Value

data class Dff(
    private val context: ProjectContext,
    override val name: String,
    val init: Value,
    val clk: DynamicExpr,
    val rst: DynamicExpr?,
    val signed: Boolean
) : SignalParent, Snapshotable {
    override val parent: SignalParent? = null
    val d = Signal("d", SignalDirection.Write, this, init, ExprType.Dynamic, signed)
    val q = Signal("q", SignalDirection.Read, this, init, ExprType.Dynamic, signed).also { it.hasDriver = true }
    private var lastClk: Bit? = null

    init {
        clk.addDependant {
            if (!context.initializing)
                context.queue(evaluable)
        }
    }

    suspend fun reset() {
        d.write(init)
        q.write(init)
        lastClk = null
    }

    override fun takeSnapshot(): SnapshotParent {
        return SnapshotParent(name, listOf(d.takeSnapshot(), q.takeSnapshot()))
    }

    override fun getSignal(name: String): Signal? =
        when (name) {
            "d" -> d
            "q" -> q
            else -> null
        }

    // use separate Evaluable instead of extending the interface so equal/hash operations are faster on it
    // since this is a data class
    private val evaluable = Evaluable {
        val clkValue = clk.value
        val rstValue = rst?.value
        require(clkValue is SimpleValue) { "Dff clk was not a SimpleValue!" }
        require(rstValue == null || rstValue is SimpleValue) { "Dff rst was not a SimpleValue!" }
        if (lastClk == Bit.B0 && clkValue.lsb == Bit.B1) { // rising edge
            if ((rstValue as? SimpleValue)?.lsb == Bit.B1) {
                q.write(init)
            } else {
                q.write(d.read())
            }
        } else if (lastClk?.isNumber() == false || !clkValue.isNumber()) {
            q.write(q.width.filledWith(Bit.Bx, q.signed))
        }
        lastClk = clkValue.lsb
    }
}
