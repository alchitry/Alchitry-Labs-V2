// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This class provides an empty implementation of {@link BracketListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
open class BracketBaseListener : BracketListener {
    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterSource(ctx: BracketParser.SourceContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitSource(ctx: BracketParser.SourceContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterBlock(ctx: BracketParser.BlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitBlock(ctx: BracketParser.BlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun enterEveryRule(ctx: ParserRuleContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitEveryRule(ctx: ParserRuleContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun visitTerminal(node: TerminalNode) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun visitErrorNode(node: ErrorNode) {}
}

/**
 * This class provides an empty implementation of {@link BracketListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
open class SuspendBracketBaseListener : SuspendBracketListener {
    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterSource(ctx: BracketParser.SourceContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitSource(ctx: BracketParser.SourceContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterBlock(ctx: BracketParser.BlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitBlock(ctx: BracketParser.BlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitParenBlock(ctx: BracketParser.ParenBlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitSquareBlock(ctx: BracketParser.SquareBlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext) {}

    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitCommentBlock(ctx: BracketParser.CommentBlockContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterEveryRule(ctx: ParserRuleContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitEveryRule(ctx: ParserRuleContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun visitTerminal(node: TerminalNode) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun visitErrorNode(node: ErrorNode) {}
}