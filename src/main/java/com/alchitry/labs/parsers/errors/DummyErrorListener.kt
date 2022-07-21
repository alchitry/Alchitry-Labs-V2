package com.alchitry.labs.parsers.errors

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode

class DummyErrorListener : ErrorListener {
    override fun reportError(node: TerminalNode, message: String) {}
    override fun reportWarning(node: TerminalNode, message: String) {}
    override fun reportDebug(node: TerminalNode, message: String) {}
    override fun reportError(ctx: ParserRuleContext, message: String) {}
    override fun reportWarning(ctx: ParserRuleContext, message: String) {}
    override fun reportDebug(ctx: ParserRuleContext, message: String) {}
}