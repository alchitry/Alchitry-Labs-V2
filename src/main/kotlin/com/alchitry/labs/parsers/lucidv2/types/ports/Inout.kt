package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.Evaluable
import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.lucidv2.types.ProxySignal
import com.alchitry.labs.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class Inout(
    override val name: String,
    context: ProjectContext,
    parent: SignalParent?,
    override val width: SignalWidth,
    override val signed: Boolean
) : PortInstance {
    override val internal = ProxySignal(
        name,
        SignalDirection.Both,
        parent,
        width.filledWith(Bit.Bz, false, signed),
        signed
    ) {
        generateValue(it)
    }

    override val external = ProxySignal(
        name,
        SignalDirection.Both,
        parent,
        width.filledWith(Bit.Bz, false, signed),
        signed
    ) {
        generateValue(it)
    }

    // connect the write flows to the read flows
    init {
        context.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            onAnyChange(internal.writeFlow, external.writeFlow) {
                val newValue = generateValue(null)
                internal.updateRead(newValue)
                external.updateRead(newValue)
            }
        }
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