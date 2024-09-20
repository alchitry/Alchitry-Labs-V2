package com.alchitry.labs2.parsers.hdl.verilog.parsers

import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.grammar.VerilogParserBaseListener
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.parsers.hdl.values.DefinedSimpleWidth
import com.alchitry.labs2.parsers.hdl.values.ResolvableSimpleWidth
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import com.alchitry.labs2.parsers.hdl.values.SimpleWidth
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprContext
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class ModuleParser(
    private val context: VerilogExprContext
) : VerilogParserBaseListener(), SignalResolver {
    var modules = mutableListOf<Module>()
    private val parameterSignals: MutableMap<String, Signal> = mutableMapOf()

    private val ports: MutableMap<String, Port> = mutableMapOf()
    private val parameters: MutableMap<String, Parameter> = mutableMapOf()

    override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? =
        parameterSignals[name]

    private fun VerilogParser.Range_Context.bitCount(): Int? {
        val msb = msb_constant_expression()?.constant_expression()?.let { context.resolve(it) } ?: return null
        val lsb = lsb_constant_expression()?.constant_expression()?.let { context.resolve(it) } ?: return null
        if (msb.type != ExprType.Constant || lsb.type != ExprType.Constant) return null

        if (msb.value.width !is SimpleWidth) {
            context.reportError(
                msb_constant_expression() ?: this,
                "MSB \"${msb_constant_expression()?.text}\" must be a number!"
            )
            return null
        }
        if (lsb.value.width !is SimpleWidth) {
            context.reportError(
                lsb_constant_expression() ?: this,
                "LSB \"${lsb_constant_expression()?.text}\" must be a number!"
            )
            return null
        }
        if (!msb.value.isNumber() || !lsb.value.isNumber() || msb.value !is SimpleValue || lsb.value !is SimpleValue)
            return null

        return msb.value.toBigInt()?.minus(lsb.value.toBigInt() ?: return null)?.toInt()?.let { it + 1 }
    }

    override fun exitParam_assignment(ctx: VerilogParser.Param_assignmentContext) {
        val name = ctx.parameter_identifier()?.text ?: return
        val valueCtx = ctx.constant_mintypmax_expression()?.constant_expression(0) ?: return

        if (ctx.constant_mintypmax_expression()!!.constant_expression().size != 1) {
            context.reportError(
                ctx.constant_mintypmax_expression()!!,
                "Only simple constant expressions are supported!"
            )
            return
        }

        val value = context.resolve(valueCtx)

        if (value == null) {
            context.reportError(
                ctx.constant_mintypmax_expression()!!,
                "Failed to resolve expression \"${ctx.constant_mintypmax_expression()?.text}\"."
            )
            return
        }

        parameterSignals[name] = Signal(name, SignalDirection.Read, null, value.value, ExprType.Known)
    }

    override fun exitParameter_declaration(ctx: VerilogParser.Parameter_declarationContext) {
        val typeText = ctx.parameter_type()?.text ?: "integer"
        if (ctx.range_() != null || typeText != "integer") {
            context.reportWarning(
                ctx,
                "Only integer parameters are supported! The parameter \"${ctx.text}\" will be ignored."
            )
            return
        }
        ctx.list_of_param_assignments()?.param_assignment()?.forEach { pCtx ->
            val pName = pCtx.parameter_identifier()?.text ?: return@forEach
            val value = if (pCtx.constant_mintypmax_expression()?.constant_expression()?.size != 1) {
                context.reportWarning(
                    pCtx.constant_mintypmax_expression() ?: pCtx,
                    "Only simple constant expressions are supported. The value \"${pCtx.constant_mintypmax_expression()?.text}\" will be ignored."
                )
                null
            } else {
                context.resolve(pCtx.constant_mintypmax_expression()?.constant_expression(0)!!).also {
                    if (it == null) {
                        context.reportWarning(
                            pCtx.constant_mintypmax_expression() ?: pCtx,
                            "Failed to resolve the expression \"${
                                pCtx.constant_mintypmax_expression()?.constant_expression(0)?.text
                            }\"! It will be ignored."
                        )
                    }
                }
            }

            parameters[pName] = Parameter(pName, value?.value, false, null)
        }
    }

    // TODO: Make ports take parameters into account!
    override fun exitPort_declaration(ctx: VerilogParser.Port_declarationContext) {
        ctx.inout_declaration()?.let { inoutCtx ->
            val signed = inoutCtx.children?.any { it.text == "signed" } ?: false
            val width = inoutCtx.range_()
                ?.let { range -> range.bitCount()?.let { DefinedSimpleWidth(it) } ?: ResolvableSimpleWidth(range) }
                ?: DefinedSimpleWidth(1)

            inoutCtx.list_of_port_identifiers()?.port_identifier()?.forEach { portIdCtx ->
                ports[portIdCtx.text] = Port(portIdCtx.text, SignalDirection.Both, width, signed, inoutCtx)
            }
        }

        ctx.input_declaration()?.let { inputCtx ->
            val signed = inputCtx.children?.any { it.text == "signed" } ?: false
            val width = inputCtx.range_()
                ?.let { range -> range.bitCount()?.let { DefinedSimpleWidth(it) } ?: ResolvableSimpleWidth(range) }
                ?: DefinedSimpleWidth(1)
            inputCtx.list_of_port_identifiers()?.port_identifier()?.forEach { portIdCtx ->
                ports[portIdCtx.text] = Port(portIdCtx.text, SignalDirection.Read, width, signed, inputCtx)
            }
        }

        ctx.output_declaration()?.let { outputCtx ->
            val signed = outputCtx.children?.any { it.text == "signed" } ?: false
            val width = outputCtx.range_()
                ?.let { range -> range.bitCount()?.let { DefinedSimpleWidth(it) } ?: ResolvableSimpleWidth(range) }
                ?: DefinedSimpleWidth(1)
            outputCtx.list_of_port_identifiers()?.port_identifier()?.forEach { portIdCtx ->
                ports[portIdCtx.text] = Port(portIdCtx.text, SignalDirection.Write, width, signed, outputCtx)
            } ?: outputCtx.list_of_variable_port_identifiers()?.var_port_id()?.forEach { varPortIdCtx ->
                val name = varPortIdCtx.port_identifier()?.text ?: return@forEach
                ports[name] = Port(
                    name,
                    SignalDirection.Write,
                    width,
                    signed,
                    outputCtx
                )
            }
        }
    }

    override fun enterModule_declaration(ctx: VerilogParser.Module_declarationContext) {
        parameterSignals.clear()
        parameters.clear()
        ports.clear()
    }

    override fun exitModule_declaration(ctx: VerilogParser.Module_declarationContext) {
        val nameCtx = ctx.module_identifier() ?: return
        val name = nameCtx.text

        modules.add(
            Module(name, parameters, ports, ctx, context.sourceFile).also {
                if (!context.project.addModule(it)) {
                    context.reportError(ctx.module_identifier() ?: ctx, "A module with name $name already exists!")
                }
            }
        )
    }
}