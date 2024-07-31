package com.alchitry.labs2.parsers.hdl.verilog.parsers

import com.alchitry.labs2.parsers.grammar.VerilogParser.ExpressionContext
import com.alchitry.labs2.parsers.grammar.VerilogParserBaseListener
import com.alchitry.labs2.parsers.hdl.ExprEvaluator
import com.alchitry.labs2.parsers.hdl.verilog.context.VerilogExprContext

class ExprParser(
    private val context: VerilogExprContext,
    evaluator: ExprEvaluator<ExpressionContext>? = null
) : VerilogParserBaseListener() {
    private val evaluator = evaluator?.withContext(context) ?: ExprEvaluator(context)

    fun resolve(ctx: ExpressionContext) = evaluator.resolve(ctx)
}