// Generated from Bracket.g4 by ANTLR 4.13.1
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This class provides an empty implementation of [BracketListener],
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public open class BracketBaseListener : BracketListener {
    /**
     * The default implementation does nothing.
     */
    override fun enterSource(ctx: BracketParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSource(ctx: BracketParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterBlock(ctx: BracketParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitBlock(ctx: BracketParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun visitTerminal(node: TerminalNode) {}

    /**
     * The default implementation does nothing.
     */
    override fun visitErrorNode(node: ErrorNode) {}
}

/**
 * This class provides an empty implementation of Suspend[BracketListener],
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public open class SuspendBracketBaseListener : SuspendBracketListener {
    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSource(ctx: BracketParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSource(ctx: BracketParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterBlock(ctx: BracketParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitBlock(ctx: BracketParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun visitTerminal(node: TerminalNode) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun visitErrorNode(node: ErrorNode) {}
}
