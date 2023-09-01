package com.alchitry.labs.parsers.lucidv2.parsers

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.RuleContext
import org.antlr.v4.kotlinruntime.tree.*

/**
 * Used to walk through a ParseTree with multiple listeners just like when doing the actual parse.
 */
object ParseTreeMultiWalker {
    fun walk(listeners: List<ParseTreeListener>, t: ParseTree, filter: WalkerFilter = WalkerFilter.None) {
        if (t is ErrorNode) {
            for (listener in listeners) listener.visitErrorNode(t)
            return
        } else if (t is TerminalNode) {
            for (listener in listeners) listener.visitTerminal(t)
            return
        }
        val r = t as RuleNode
        enterRule(listeners, r)
        for (i in 0 until r.childCount) {
            val child = r.getChild(i) ?: continue
            if (!filter.shouldSkip(r, child))
                walk(listeners, child, filter)
        }
        exitRule(listeners, r)
    }


    /**
     * The discovery of a rule node, involves sending two events: the generic [ParseTreeListener.enterEveryRule] and a [RuleContext]-specific event. First we
     * trigger the generic and then the rule specific. We to them in reverse order upon finishing the node.
     */
    private fun enterRule(listeners: List<ParseTreeListener>, r: RuleNode) {
        val ctx = r.ruleContext as ParserRuleContext
        for (listener in listeners) {
            listener.enterEveryRule(ctx)
            ctx.enterRule(listener)
        }
    }

    private fun exitRule(listeners: List<ParseTreeListener>, r: RuleNode) {
        val ctx = r.ruleContext as ParserRuleContext
        for (listener in listeners) {
            ctx.exitRule(listener)
            listener.exitEveryRule(ctx)
        }
    }
}