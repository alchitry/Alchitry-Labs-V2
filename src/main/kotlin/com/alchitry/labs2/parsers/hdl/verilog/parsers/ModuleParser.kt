package com.alchitry.labs2.parsers.hdl.verilog.parsers

import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.grammar.VerilogParserBaseListener
import com.alchitry.labs2.parsers.hdl.types.Module
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprContext

data class ModuleParser(
    private val context: VerilogExprContext
) : VerilogParserBaseListener() {
    var modules = mutableListOf<Module>()

    override fun enterModule_declaration(ctx: VerilogParser.Module_declarationContext) {
        TODO()
    }

    override fun exitModule_declaration(ctx: VerilogParser.Module_declarationContext) {
        val nameCtx = ctx.module_identifier() ?: return
        val name = nameCtx.text


    }
}