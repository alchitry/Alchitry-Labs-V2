package com.alchitry.labs2.parsers

import com.alchitry.labs2.parsers.hdl.lucidv2.parsers.WalkerFilter
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.*

/**
 * Used to walk through a ParseTree with multiple listeners just like when doing the actual parse.
 */
object ParseTreeMultiWalker {
    suspend fun walk(
        listeners: List<AnyParseTreeListener>,
        t: ParseTree,
        filter: WalkerFilter = WalkerFilter.None,
        ignoreSkip: Boolean = true,
        skipErrorNodes: Boolean = true,
        skipTerminalNodes: Boolean = true
    ) {
        if (!skipErrorNodes && t is ErrorNode) {
            for (listener in listeners) {
                when (listener) {
                    is ParseTreeListener -> listener.visitErrorNode(t)
                    is SuspendParseTreeListener -> listener.visitErrorNode(t)
                }
            }
            return
        }

        if (!skipTerminalNodes && t is TerminalNode) {
            for (listener in listeners) {
                when (listener) {
                    is ParseTreeListener -> listener.visitTerminal(t)
                    is SuspendParseTreeListener -> listener.visitTerminal(t)
                }
            }
            return
        }

        val r = t as? RuleNode ?: return
        if (ignoreSkip || !r.skip) {
            enterRule(listeners, r)
            for (i in 0 until r.childCount) {
                val child = r.getChild(i) ?: continue
                if (!filter.shouldSkip(r, child))
                    walk(listeners, child, filter, ignoreSkip)
            }
            exitRule(listeners, r)
        }
    }

    fun walk(
        listeners: List<ParseTreeListener>,
        t: ParseTree,
        filter: WalkerFilter = WalkerFilter.None,
        ignoreSkip: Boolean = true,
        skipErrorNodes: Boolean = true,
        skipTerminalNodes: Boolean = true
    ) {
        if (!skipErrorNodes && t is ErrorNode) {
            for (listener in listeners) {
                listener.visitErrorNode(t)
            }
            return
        }

        if (!skipTerminalNodes && t is TerminalNode) {
            for (listener in listeners) {
                listener.visitTerminal(t)
            }
            return
        }

        val r = t as? RuleNode ?: return
        if (ignoreSkip || !r.skip) {
            enterRule(listeners, r)
            for (i in 0 until r.childCount) {
                val child = r.getChild(i) ?: continue
                if (!filter.shouldSkip(r, child))
                    walk(listeners, child, filter, ignoreSkip)
            }
            exitRule(listeners, r)
        }
    }


    /**
     * The discovery of a rule node, involves sending two events: the generic [ParseTreeListener.enterEveryRule] and a [RuleContext]-specific event. First we
     * trigger the generic and then the rule specific. We to them in reverse order upon finishing the node.
     */
    private suspend fun enterRule(listeners: List<AnyParseTreeListener>, r: RuleNode) {
        val ctx = r.ruleContext as ParserRuleContext
        for (listener in listeners) {
            when (listener) {
                is ParseTreeListener -> {
                    listener.enterEveryRule(ctx)
                    ctx.enterRule(listener)
                }

                is SuspendParseTreeListener -> {
                    listener.enterEveryRule(ctx)
                    ctx.enterRule(listener)
                }
            }
        }
    }

    private suspend fun exitRule(listeners: List<AnyParseTreeListener>, r: RuleNode) {
        val ctx = r.ruleContext as ParserRuleContext
        for (listener in listeners) {
            when (listener) {
                is ParseTreeListener -> {
                    ctx.exitRule(listener)
                    listener.exitEveryRule(ctx)
                }

                is SuspendParseTreeListener -> {
                    ctx.exitRule(listener)
                    listener.exitEveryRule(ctx)
                }
            }
        }
    }

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