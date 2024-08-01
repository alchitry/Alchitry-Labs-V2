package com.alchitry.labs2.parsers.hdl.verilog.context

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.ExprEvaluatorContext
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.project.files.SourceFile

interface VerilogExprContext : ErrorListener, ExprEvaluatorContext<VerilogParser.Constant_expressionContext> {
    val evalContext: Evaluable?
    val project: ProjectContext
    val sourceFile: SourceFile
}