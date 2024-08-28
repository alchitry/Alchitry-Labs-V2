package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.SignalParent
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.SignalWidth

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
        if (parent !is ModuleInstance || parent.context.mode == ExprEvalMode.Default) {
            external.connectTo(internal, context)
        }
    }
}