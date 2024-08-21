package com.alchitry.labs2.parsers.hdl.verilog.parsers

import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.grammar.VerilogParserBaseListener
import com.alchitry.labs2.parsers.hdl.types.Module
import com.alchitry.labs2.parsers.hdl.types.Parameter
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.parsers.hdl.values.DefinedSimpleWidth
import com.alchitry.labs2.parsers.hdl.values.SimpleValue
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprContext

data class ModuleParser(
    private val context: VerilogExprContext
) : VerilogParserBaseListener() {
    var modules = mutableListOf<Module>()

    private fun VerilogParser.Range_Context.bitCount(): Int? {
        val msb = msb_constant_expression()?.constant_expression()?.let { context.resolve(it) }?.value ?: return null
        val lsb = lsb_constant_expression()?.constant_expression()?.let { context.resolve(it) }?.value ?: return null
        if (msb !is SimpleValue || !msb.isNumber()) {
            context.reportError(
                msb_constant_expression() ?: this,
                "MSB \"${msb_constant_expression()?.text}\" must be a number!"
            )
            return null
        }
        if (lsb !is SimpleValue || !lsb.isNumber()) {
            context.reportError(
                lsb_constant_expression() ?: this,
                "LSB \"${lsb_constant_expression()?.text}\" must be a number!"
            )
            return null
        }
        return msb.toBigInt()?.minus(lsb.toBigInt() ?: return null)?.toInt()?.let { it + 1 }
    }

    override fun exitModule_declaration(ctx: VerilogParser.Module_declarationContext) {
        val nameCtx = ctx.module_identifier() ?: return
        val name = nameCtx.text

        val params = ctx.module_parameter_port_list()?.parameter_declaration()?.flatMap { paramCtx ->
            val typeText = paramCtx.parameter_type()?.text ?: "integer"
            if (paramCtx.range_() != null || typeText != "integer") {
                context.reportWarning(
                    paramCtx,
                    "Only integer parameters are supported! The parameter \"${paramCtx.text}\" will be ignored."
                )
                return@flatMap emptyList()
            }
            return@flatMap paramCtx.list_of_param_assignments()?.param_assignment()?.mapNotNull { pCtx ->
                val pName = pCtx.parameter_identifier()?.text ?: return@mapNotNull null
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

                Parameter(pName, value?.value, false, null)
            } ?: emptyList()
        }?.associate { it.name to it }

        val ports = ctx.list_of_port_declarations()?.port_declaration()?.flatMap { portDecCtx ->
            portDecCtx.inout_declaration()?.let { inoutCtx ->
                val signed = inoutCtx.children?.any { it.text == "signed" } ?: false
                val width = inoutCtx.range_()?.bitCount() ?: 1
                return@flatMap inoutCtx.list_of_port_identifiers()?.port_identifier()?.map { portIdCtx ->
                    Port(portIdCtx.text, SignalDirection.Both, DefinedSimpleWidth(width), signed, inoutCtx)
                } ?: emptyList()
            }

            portDecCtx.input_declaration()?.let { inputCtx ->
                val signed = inputCtx.children?.any { it.text == "signed" } ?: false
                val width = inputCtx.range_()?.bitCount() ?: 1
                return@flatMap inputCtx.list_of_port_identifiers()?.port_identifier()?.map { portIdCtx ->
                    Port(portIdCtx.text, SignalDirection.Read, DefinedSimpleWidth(width), signed, inputCtx)
                } ?: emptyList()
            }

            portDecCtx.output_declaration()?.let { outputCtx ->
                val signed = outputCtx.children?.any { it.text == "signed" } ?: false
                val width = outputCtx.range_()?.bitCount() ?: 1
                return@flatMap outputCtx.list_of_port_identifiers()?.port_identifier()?.map { portIdCtx ->
                    Port(portIdCtx.text, SignalDirection.Write, DefinedSimpleWidth(width), signed, outputCtx)
                } ?: outputCtx.list_of_variable_port_identifiers()?.var_port_id()?.mapNotNull { varPortIdCtx ->
                    Port(
                        varPortIdCtx.port_identifier()?.text ?: return@mapNotNull null,
                        SignalDirection.Write,
                        DefinedSimpleWidth(width),
                        signed,
                        outputCtx
                    )
                } ?: emptyList()
            }

            return@flatMap emptyList()
        }?.associate { it.name to it }

        modules.add(
            Module(name, params ?: emptyMap(), ports ?: emptyMap(), ctx, context.sourceFile).also {
                if (!context.project.addModule(it)) {
                    context.reportError(ctx.module_identifier() ?: ctx, "A module with name $name already exists!")
                }
            }
        )
    }
}