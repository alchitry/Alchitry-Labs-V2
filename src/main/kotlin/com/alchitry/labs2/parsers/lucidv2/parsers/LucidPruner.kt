package com.alchitry.labs2.parsers.lucidv2.parsers

import com.alchitry.labs2.parsers.grammar.LucidBaseListener
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import org.antlr.v4.kotlinruntime.ParserRuleContext

data class LucidPruner(
    private val context: LucidBlockContext,
) : LucidBaseListener() {

    override fun enterIfStat(ctx: LucidParser.IfStatContext) {
        val value = context.expr.resolve(ctx.expr() ?: return) ?: return
        if (value.constant) {
            val parent = ctx.parent as? ParserRuleContext ?: return
            val children = parent.children ?: return
            val index = children.indexOf(ctx)
            children.removeAt(index)
            val newStatements = if (value.isTrue().bit == Bit.B1) {
                ctx.block()?.alwaysStat()
            } else {
                ctx.elseStat()?.block()?.alwaysStat()
            }
            newStatements?.let { children.addAll(index, it) }
        }
    }

    override fun enterEveryRule(ctx: ParserRuleContext) {
        if (ctx is LucidParser.ExprContext) {
            val value = context.expr.resolve(ctx)
            if (value?.constant == true) {
                ctx.skip = true
            }
        }
    }
}