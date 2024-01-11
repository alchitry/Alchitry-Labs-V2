package com.alchitry.labs.parsers.notations

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.TerminalNode

interface ErrorListener {
    fun reportError(node: TerminalNode, message: String)
    fun reportWarning(node: TerminalNode, message: String)
    fun reportInfo(node: TerminalNode, message: String)
    fun reportError(ctx: ParserRuleContext, message: String)
    fun reportWarning(ctx: ParserRuleContext, message: String)
    fun reportInfo(ctx: ParserRuleContext, message: String)
}

