package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.lucidv2.types.Signal
import com.alchitry.labs.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

class Output(
    override val name: String,
    context: ProjectContext,
    parent: SignalParent?,
    override val width: SignalWidth,
    override val signed: Boolean
) : PortInstance {
    override val internal = Signal(name, SignalDirection.Write, parent, width.filledWith(Bit.Bx, false, signed), signed)
    override val external = Signal(name, SignalDirection.Read, parent, width.filledWith(Bit.Bx, false, signed), signed)

    init {
        internal.connectTo(external, context)
    }
}