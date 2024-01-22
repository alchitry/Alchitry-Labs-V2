// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This class provides an empty implementation of {@link IndentDetectorListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
open class IndentDetectorBaseListener : IndentDetectorListener {
    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override fun enterSource(ctx: IndentDetectorParser.SourceContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override fun exitSource(ctx: IndentDetectorParser.SourceContext) {}

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
 * This class provides an empty implementation of {@link IndentDetectorListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
open class SuspendIndentDetectorBaseListener : SuspendIndentDetectorListener {
    /**
     * {@inheritDoc}
     *f
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun enterSource(ctx: IndentDetectorParser.SourceContext) {}

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     */
    override suspend fun exitSource(ctx: IndentDetectorParser.SourceContext) {}

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