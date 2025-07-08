// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BracketParser}.
 */
interface BracketListener : ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BracketParser#source}.
	 * @param ctx the parse tree
	 */
	fun enterSource(ctx: BracketParser.SourceContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#source}.
	 * @param ctx the parse tree
	 */
	fun exitSource(ctx: BracketParser.SourceContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#block}.
	 * @param ctx the parse tree
	 */
	fun enterBlock(ctx: BracketParser.BlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#block}.
	 * @param ctx the parse tree
	 */
	fun exitBlock(ctx: BracketParser.BlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#parenBlock}.
	 * @param ctx the parse tree
	 */
	fun enterParenBlock(ctx: BracketParser.ParenBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#parenBlock}.
	 * @param ctx the parse tree
	 */
	fun exitParenBlock(ctx: BracketParser.ParenBlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#squareBlock}.
	 * @param ctx the parse tree
	 */
	fun enterSquareBlock(ctx: BracketParser.SquareBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#squareBlock}.
	 * @param ctx the parse tree
	 */
	fun exitSquareBlock(ctx: BracketParser.SquareBlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#curlyBlock}.
	 * @param ctx the parse tree
	 */
	fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#curlyBlock}.
	 * @param ctx the parse tree
	 */
	fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#commentBlock}.
	 * @param ctx the parse tree
	 */
	fun enterCommentBlock(ctx: BracketParser.CommentBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#commentBlock}.
	 * @param ctx the parse tree
	 */
	fun exitCommentBlock(ctx: BracketParser.CommentBlockContext)

    override fun asSuspend(): SuspendBracketListener = object : SuspendBracketListener {
        override suspend fun enterSource(ctx: BracketParser.SourceContext) = this@BracketListener.enterSource(ctx)
        override suspend fun exitSource(ctx: BracketParser.SourceContext) = this@BracketListener.exitSource(ctx)

        override suspend fun enterBlock(ctx: BracketParser.BlockContext) = this@BracketListener.enterBlock(ctx)
        override suspend fun exitBlock(ctx: BracketParser.BlockContext) = this@BracketListener.exitBlock(ctx)

        override suspend fun enterParenBlock(ctx: BracketParser.ParenBlockContext) =
            this@BracketListener.enterParenBlock(ctx)

        override suspend fun exitParenBlock(ctx: BracketParser.ParenBlockContext) =
            this@BracketListener.exitParenBlock(ctx)

        override suspend fun enterSquareBlock(ctx: BracketParser.SquareBlockContext) =
            this@BracketListener.enterSquareBlock(ctx)

        override suspend fun exitSquareBlock(ctx: BracketParser.SquareBlockContext) =
            this@BracketListener.exitSquareBlock(ctx)

        override suspend fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext) =
            this@BracketListener.enterCurlyBlock(ctx)

        override suspend fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext) =
            this@BracketListener.exitCurlyBlock(ctx)

        override suspend fun enterCommentBlock(ctx: BracketParser.CommentBlockContext) =
            this@BracketListener.enterCommentBlock(ctx)

        override suspend fun exitCommentBlock(ctx: BracketParser.CommentBlockContext) =
            this@BracketListener.exitCommentBlock(ctx)

        override suspend fun enterEveryRule(ctx: ParserRuleContext) = this@BracketListener.enterEveryRule(ctx)
        override suspend fun exitEveryRule(ctx: ParserRuleContext) = this@BracketListener.exitEveryRule(ctx)

        override suspend fun visitErrorNode(node: ErrorNode) = this@BracketListener.visitErrorNode(node)
        override suspend fun visitTerminal(node: TerminalNode) = this@BracketListener.visitTerminal(node)
    }
}

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BracketParser}.
 */
interface SuspendBracketListener : SuspendParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BracketParser#source}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSource(ctx: BracketParser.SourceContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#source}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSource(ctx: BracketParser.SourceContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#block}.
	 * @param ctx the parse tree
	 */
	suspend fun enterBlock(ctx: BracketParser.BlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#block}.
	 * @param ctx the parse tree
	 */
	suspend fun exitBlock(ctx: BracketParser.BlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#parenBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterParenBlock(ctx: BracketParser.ParenBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#parenBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitParenBlock(ctx: BracketParser.ParenBlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#squareBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSquareBlock(ctx: BracketParser.SquareBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#squareBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSquareBlock(ctx: BracketParser.SquareBlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#curlyBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterCurlyBlock(ctx: BracketParser.CurlyBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#curlyBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitCurlyBlock(ctx: BracketParser.CurlyBlockContext)
	/**
	 * Enter a parse tree produced by {@link BracketParser#commentBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterCommentBlock(ctx: BracketParser.CommentBlockContext)
	/**
	 * Exit a parse tree produced by {@link BracketParser#commentBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitCommentBlock(ctx: BracketParser.CommentBlockContext)
}