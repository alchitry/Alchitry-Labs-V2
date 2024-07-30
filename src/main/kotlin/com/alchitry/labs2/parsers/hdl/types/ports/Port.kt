package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.lucidv2.context.LucidExprEval
import com.alchitry.labs2.parsers.hdl.lucidv2.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.SignalDirection

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean,
    val context: LucidParser.PortDecContext
) {
    val isInout: Boolean = direction == SignalDirection.Both

    fun instantiate(parent: ModuleInstance, context: ProjectContext): PortInstance {
        val instWidth = if (width.isDefined()) width else {
            require(width.isResolvable()) { "Width is not resolvable: $width" }
            val eval = LucidExprEval(
                context,
                parent.context.sourceFile,
                parent.context.notationCollector.createChild("PortEval")
            ) {
                return@LucidExprEval parent.parameters[it]
            }
            width.resolve(eval).also {
                if (!parent.testing)
                    check(it.isDefined()) { "Failed to fully resolve width: $width" }
            }
        }
        return when (direction) {
            SignalDirection.Read -> Input(name, context, parent, instWidth, signed)
            SignalDirection.Write -> Output(name, context, parent, instWidth, signed)
            SignalDirection.Both -> Inout(name, parent, instWidth, signed)
        }
    }
}