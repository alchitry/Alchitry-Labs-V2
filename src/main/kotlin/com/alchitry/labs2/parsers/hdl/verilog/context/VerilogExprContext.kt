package com.alchitry.labs2.parsers.hdl.verilog.context

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.WalkerFilter
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.Expr
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.ExprEvaluatorContext
import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import com.alchitry.labs2.parsers.hdl.verilog.parsers.ConstantExprParser
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree

interface VerilogExprContext : ErrorListener, ExprEvaluatorContext<VerilogParser.Constant_expressionContext> {
    val evalContext: Evaluable?
    val project: ProjectContext
    val sourceFile: SourceFile
    fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent?
}

class VerilogExprEval(
    override val project: ProjectContext,
    override val sourceFile: SourceFile,
    override val mode: ExprEvalMode,
    val notationCollector: NotationCollector = project.notationManager.getCollector(sourceFile),
    val signalProvider: (name: String) -> SignalOrParent?,
) : VerilogExprContext, ErrorListener by notationCollector {
    override val evalContext: Evaluable? = null

    private val expr = ConstantExprParser(this)

    private val listeners = listOf(
        expr
    )

    fun walk(t: ParseTree, ignoreSkip: Boolean = false) {
        ParseTreeMultiWalker.walk(
            listeners,
            t,
            WalkerFilter.None,
            ignoreSkip = ignoreSkip
        )
    }

    override fun resolve(exprCtx: VerilogParser.Constant_expressionContext): Expr? {
        return expr.resolve(exprCtx)
    }

    override fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent? {
        return signalProvider(name)
    }
}