package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.types.ProxySignal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.SignalParent
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.values.Value

class Inout(
    override val name: String,
    parent: SignalParent?,
    override val width: SignalWidth,
    override val signed: Boolean
) : PortInstance {
    override val internal = ProxySignal(
        name,
        SignalDirection.Both,
        parent,
        width.filledWith(Bit.Bz, signed),
        ExprType.Dynamic,
        signed
    ) {
        generateValue(it)
    }

    override val external = ProxySignal(
        name,
        SignalDirection.Both,
        parent,
        width.filledWith(Bit.Bz, signed),
        ExprType.Dynamic,
        signed
    ) {
        internal.baseRead(it)
    }

    var lastValue: Value? = null
    // connect the write flows to the read flows
    init {
        val evaluable = Evaluable {
            val current = generateValue(null)
            if (current != lastValue) {
                lastValue = current
                internal.updateRead()
                external.updateRead()
            }
        }
        internal.addWriteDependant(evaluable)
        external.addWriteDependant(evaluable)
    }

    /**
     * Combines the values of internal and external to produce a value where floating portions remain z and
     * over driven portions become x.
     */
    private fun generateValue(evaluable: Evaluable?): Value {
        val wr = internal.baseRead(evaluable)
        val extWr = external.baseRead(evaluable)
        val en = wr.where(Bit.Bz).invert()
        val extEn = extWr.where(Bit.Bz).invert()

        val doubleDrive = en and extEn
        val floating = en.invert() and extEn.invert()
        val validBits = (floating or doubleDrive).invert()
        val setBits = ((wr and en) or (extWr and extEn)) and validBits
        val clearBits = ((wr.invert() and en) or (extWr.invert() and extEn)) and validBits
        return (width.filledWith(Bit.Bx, signed) or setBits and clearBits.invert()).replace(floating, Bit.Bz)
    }
}