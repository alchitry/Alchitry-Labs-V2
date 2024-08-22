package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprEval
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean,
    val context: ParserRuleContext
) {
    val isInout: Boolean = direction == SignalDirection.Both

    init {
        check(width.isResolvable()) {
            "Unresolvable!"
        }
    }

    fun instantiate(parent: ModuleInstance, context: ProjectContext): PortInstance {
        require(width.isResolvable()) { "Width is not resolvable: $width" }
        val instWidth = if (width.isDefined() || parent.context.mode == ExprEvalMode.Building) width else {
            val eval = LucidExprEval(
                context,
                parent.context.sourceFile,
                ExprEvalMode.Default,
                parent.context.notationCollector.createChild("PortEval")
            ) {
                return@LucidExprEval parent.parameters[it]
            }
            width.resolve(eval).also {
                if (parent.context.mode == ExprEvalMode.Default)
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