package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucidv2.values.Bit
import com.alchitry.labs2.parsers.hdl.lucidv2.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.SignalParent

class Output(
    override val name: String,
    context: ProjectContext,
    parent: SignalParent?,
    override val width: SignalWidth,
    override val signed: Boolean
) : PortInstance {
    override val internal =
        Signal(name, SignalDirection.Write, parent, width.filledWith(Bit.Bx, signed), ExprType.Dynamic, signed)
    override val external =
        Signal(name, SignalDirection.Read, parent, width.filledWith(Bit.Bx, signed), ExprType.Dynamic, signed)

    init {
        if (parent !is ModuleInstance || !parent.testing)
            internal.connectTo(external, context)
    }
}