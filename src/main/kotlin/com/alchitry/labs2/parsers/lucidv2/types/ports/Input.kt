package com.alchitry.labs2.parsers.lucidv2.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.parsers.ExprType
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.types.SignalParent
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.SignalWidth

class Input(
    override val name: String,
    context: ProjectContext,
    parent: SignalParent?,
    override val width: SignalWidth,
    override val signed: Boolean
) : PortInstance {
    override val internal =
        Signal(name, SignalDirection.Read, parent, width.filledWith(Bit.Bx, signed), ExprType.Dynamic, signed)
    override val external =
        Signal(name, SignalDirection.Write, parent, width.filledWith(Bit.Bx, signed), ExprType.Dynamic, signed)

    init {
        if (parent !is ModuleInstance || !parent.testing)
        external.connectTo(internal, context)
    }
}