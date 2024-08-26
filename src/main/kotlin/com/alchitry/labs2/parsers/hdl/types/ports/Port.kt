package com.alchitry.labs2.parsers.hdl.types.ports

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidExprEval
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprEval
import com.alchitry.labs2.project.Languages
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
            val resolvedWidth = when (parent.context.sourceFile.language) {
                Languages.Lucid -> {
                    val eval = LucidExprEval(
                        context,
                        parent.context.sourceFile,
                        ExprEvalMode.Default,
                        parent.context.notationCollector.createChild("PortEval")
                    ) {
                        parent.parameters[it]
                    }
                    width.resolve(lucidEval = eval, verilogEval = null)
                }

                Languages.Verilog -> {
                    val eval = VerilogExprEval(
                        context,
                        parent.context.sourceFile,
                        ExprEvalMode.Default,
                        parent.context.notationCollector.createChild("PortEval")
                    ) {
                        parent.parameters[it]
                    }
                    width.resolve(lucidEval = null, verilogEval = eval)
                }
            }

            if (resolvedWidth.isDefined()) {
                resolvedWidth
            } else {
                if (parent.context.mode == ExprEvalMode.Default && parent.parent?.context?.mode != ExprEvalMode.Building)
                    error("Failed to fully resolve width: $width. Resolved: $resolvedWidth")
                width
            }
        }
        return when (direction) {
            SignalDirection.Read -> Input(name, context, parent, instWidth, signed)
            SignalDirection.Write -> Output(name, context, parent, instWidth, signed)
            SignalDirection.Both -> Inout(name, parent, instWidth, signed)
        }
    }
}