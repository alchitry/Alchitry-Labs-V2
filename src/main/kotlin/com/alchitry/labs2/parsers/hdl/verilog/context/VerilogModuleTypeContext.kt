package com.alchitry.labs2.parsers.hdl.verilog.context

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.WalkerFilter
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.types.Module
import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import com.alchitry.labs2.parsers.hdl.verilog.parsers.ConstantExprParser
import com.alchitry.labs2.parsers.hdl.verilog.parsers.ModuleParser
import com.alchitry.labs2.parsers.hdl.verilog.parsers.VerilogWalkerFilters
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext

class VerilogModuleTypeContext(
    override val project: ProjectContext,
    override val sourceFile: SourceFile
) : VerilogExprContext, ErrorListener by project.notationManager.getCollector(sourceFile) {
    override val evalContext: Evaluable? = null

    override val mode = ExprEvalMode.Default

    private val expr = ConstantExprParser(this)
    private val module = ModuleParser(this)

    private val listeners = listOf(
        expr,
        module
    )


    override fun resolve(exprCtx: VerilogParser.Constant_expressionContext) = expr.resolve(exprCtx)

    override fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent? {
        return module.resolve(ctx, name)
    }

    fun extract(t: ParserRuleContext): List<Module> {
        module.modules.clear()
        ParseTreeMultiWalker.walk(
            listeners,
            t,
            WalkerFilter.join(VerilogWalkerFilters.SkipModuleBodies, VerilogWalkerFilters.ModulesOnly)
        )
        return module.modules
    }

}