package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.signals.ProxySignal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value

class Inout(
    val name: String,
    parent: ModuleInstance?,
    val width: SignalWidth,
    val signed: Boolean
) {
    val internal = ProxySignal(
        name,
        SignalDirection.Both,
        null, // internal signal has no parent
        width.filledWith(Bit.Bz, false, signed),
        signed
    ) {
        generateValue(it)
    }

    val external = ProxySignal(
        name,
        SignalDirection.Both,
        parent, // external signal parent is the moduleInstance
        width.filledWith(Bit.Bz, false, signed),
        signed
    ) {
        generateValue(it)
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
        return (width.filledWith(Bit.Bx, false, signed) or setBits and clearBits.invert()).replace(floating, Bit.Bz)
    }
}