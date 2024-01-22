// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IndentDetectorParser}.
 */
interface IndentDetectorListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link IndentDetectorParser#source}.
     * @param ctx the parse tree
     */
    fun enterSource(ctx: IndentDetectorParser.SourceContext)

    /**
     * Exit a parse tree produced by {@link IndentDetectorParser#source}.
     * @param ctx the parse tree
     */
    fun exitSource(ctx: IndentDetectorParser.SourceContext)

    override fun asSuspend(): SuspendIndentDetectorListener = object : SuspendIndentDetectorListener {
        override suspend fun enterSource(ctx: IndentDetectorParser.SourceContext) =
            this@IndentDetectorListener.enterSource(ctx)

        override suspend fun exitSource(ctx: IndentDetectorParser.SourceContext) =
            this@IndentDetectorListener.exitSource(ctx)

        override suspend fun enterEveryRule(ctx: ParserRuleContext) = this@IndentDetectorListener.enterEveryRule(ctx)
        override suspend fun exitEveryRule(ctx: ParserRuleContext) = this@IndentDetectorListener.exitEveryRule(ctx)

        override suspend fun visitErrorNode(node: ErrorNode) = this@IndentDetectorListener.visitErrorNode(node)
        override suspend fun visitTerminal(node: TerminalNode) = this@IndentDetectorListener.visitTerminal(node)
    }
}

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IndentDetectorParser}.
 */
interface SuspendIndentDetectorListener : SuspendParseTreeListener {
    /**
     * Enter a parse tree produced by {@link IndentDetectorParser#source}.
     * @param ctx the parse tree
     */
    suspend fun enterSource(ctx: IndentDetectorParser.SourceContext)

    /**
     * Exit a parse tree produced by {@link IndentDetectorParser#source}.
     * @param ctx the parse tree
     */
    suspend fun exitSource(ctx: IndentDetectorParser.SourceContext)
}