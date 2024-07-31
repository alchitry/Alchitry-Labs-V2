package com.alchitry.labs2.parsers.hdl.lucid.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.values.Bit
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class LucidPruner(
    private val context: LucidBlockContext,
) : LucidBaseListener() {

    override fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext) {
        val ifCtx = ctx.ifStat() ?: return
        val expr = context.expr.resolve(ifCtx.expr() ?: return) ?: return
        if (expr.type == ExprType.Constant) {
            val parent = ctx.parent as? ParserRuleContext ?: return
            val children = parent.children ?: return
            val index = children.indexOf(ctx)
            children.removeAt(index)
            val newStatements = if (expr.value.isTrue().bit == Bit.B1) {
                ifCtx.block()?.alwaysStat()
            } else {
                ifCtx.elseStat()?.block()?.alwaysStat()
            }
            newStatements?.let { children.addAll(index, it) }
        }
    }

    override fun enterSemi(ctx: LucidParser.SemiContext) {
        ctx.skip = true
    }

    override fun enterName(ctx: LucidParser.NameContext) {
        ctx.skip = true
    }

    override fun enterEveryRule(ctx: ParserRuleContext) {
        if (ctx is LucidParser.ExprContext) {
            val value = context.expr.resolve(ctx)
            if (value?.type == ExprType.Constant) {
                ctx.skip = true
            }
        }
    }
}