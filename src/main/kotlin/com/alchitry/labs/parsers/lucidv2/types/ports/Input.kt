package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.project.Project

class Input(
    override val name: String,
    project: Project,
    parent: SignalParent?,
    override val width: SignalWidth,
    override val signed: Boolean
) : PortInstance {
    override val internal = Signal(name, SignalDirection.Read, parent, width.filledWith(Bit.Bx, false, signed), signed)
    override val external = Signal(name, SignalDirection.Write, parent, width.filledWith(Bit.Bx, false, signed), signed)

    init {
        external.connectTo(internal, project)
    }
}