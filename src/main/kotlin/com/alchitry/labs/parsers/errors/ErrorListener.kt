package com.alchitry.labs.parsers.errors

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode

interface ErrorListener {
    fun reportError(node: TerminalNode, message: String)
    fun reportWarning(node: TerminalNode, message: String)
    fun reportDebug(node: TerminalNode, message: String)
    fun reportError(ctx: ParserRuleContext, message: String)
    fun reportWarning(ctx: ParserRuleContext, message: String)
    fun reportDebug(ctx: ParserRuleContext, message: String)
}

val dummyErrorListener = object : ErrorListener {
    override fun reportError(node: TerminalNode, message: String) {
    }

    override fun reportError(ctx: ParserRuleContext, message: String) {
    }

    override fun reportWarning(node: TerminalNode, message: String) {
    }

    override fun reportWarning(ctx: ParserRuleContext, message: String) {
    }

    override fun reportDebug(node: TerminalNode, message: String) {
    }

    override fun reportDebug(ctx: ParserRuleContext, message: String) {
    }
}