package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class Module(
    val name: String,
    val parameters: Map<String, Parameter>,
    val ports: Map<String, Port>,
    val context: ParserRuleContext,
    val sourceFile: SourceFile
) {
    suspend fun convertToVerilog(projectContext: ProjectContext): String? {
        return when (context) {
            is LucidParser.ModuleContext -> {
                val moduleInstance =
                    ModuleInstance(name, projectContext, null, this, mapOf(), mapOf(), mapOf(), ExprEvalMode.Building)
                moduleInstance.initialWalk()
                assert(moduleInstance.context.notationCollector.hasNoErrors) { "Errors can't exist in Lucid code intended to be converted to Verilog!" }
                moduleInstance.context.convertToVerilog()
            }

            is VerilogParser.Module_declarationContext -> error("Verilog modules are already in Verilog!") //sourceFile.readText()
            else -> error("Unknown module context! ${context.javaClass.canonicalName}")
        }
    }
}

