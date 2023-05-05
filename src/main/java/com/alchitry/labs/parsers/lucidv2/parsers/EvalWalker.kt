package com.alchitry.labs.parsers.lucidv2.parsers

import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.RuleContext
import org.antlr.v4.runtime.tree.*

/**
 * Used to walk through an AlwaysBlockContext and skips control block children.
 */
object EvalWalker {
    fun walk(listeners: List<ParseTreeListener>, t: ParseTree) {
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
            val child = r.getChild(i)
            // skip blocks so that the AlwaysEvaluator can walk only the correct ones
            if ((child is BlockContext || child is CaseBlockContext) && r !is AlwaysBlockContext)
                continue
            walk(listeners, child)
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