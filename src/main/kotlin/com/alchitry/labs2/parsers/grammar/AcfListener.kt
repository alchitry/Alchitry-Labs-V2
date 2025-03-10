// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AcfParser}.
 */
interface AcfListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link AcfParser#source}.
     * @param ctx the parse tree
     */
    fun enterSource(ctx: AcfParser.SourceContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#source}.
     * @param ctx the parse tree
     */
    fun exitSource(ctx: AcfParser.SourceContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#line}.
     * @param ctx the parse tree
     */
    fun enterLine(ctx: AcfParser.LineContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#line}.
     * @param ctx the parse tree
     */
    fun exitLine(ctx: AcfParser.LineContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#pin}.
     * @param ctx the parse tree
     */
    fun enterPin(ctx: AcfParser.PinContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#pin}.
     * @param ctx the parse tree
     */
    fun exitPin(ctx: AcfParser.PinContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#attributeBlock}.
     * @param ctx the parse tree
     */
    fun enterAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#attributeBlock}.
     * @param ctx the parse tree
     */
    fun exitAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#name}.
     * @param ctx the parse tree
     */
    fun enterName(ctx: AcfParser.NameContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#name}.
     * @param ctx the parse tree
     */
    fun exitName(ctx: AcfParser.NameContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#attribute}.
     * @param ctx the parse tree
     */
    fun enterAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#attribute}.
     * @param ctx the parse tree
     */
    fun exitAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#attributeValue}.
     * @param ctx the parse tree
     */
    fun enterAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#attributeValue}.
     * @param ctx the parse tree
     */
    fun exitAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#portName}.
     * @param ctx the parse tree
     */
    fun enterPortName(ctx: AcfParser.PortNameContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#portName}.
     * @param ctx the parse tree
     */
    fun exitPortName(ctx: AcfParser.PortNameContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#pinName}.
     * @param ctx the parse tree
     */
    fun enterPinName(ctx: AcfParser.PinNameContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#pinName}.
     * @param ctx the parse tree
     */
    fun exitPinName(ctx: AcfParser.PinNameContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#frequency}.
     * @param ctx the parse tree
     */
    fun enterFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#frequency}.
     * @param ctx the parse tree
     */
    fun exitFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#arrayIndex}.
     * @param ctx the parse tree
     */
    fun enterArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#arrayIndex}.
     * @param ctx the parse tree
     */
    fun exitArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#number}.
     * @param ctx the parse tree
     */
    fun enterNumber(ctx: AcfParser.NumberContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#number}.
     * @param ctx the parse tree
     */
    fun exitNumber(ctx: AcfParser.NumberContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#semi}.
     * @param ctx the parse tree
     */
    fun enterSemi(ctx: AcfParser.SemiContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#semi}.
     * @param ctx the parse tree
     */
    fun exitSemi(ctx: AcfParser.SemiContext)

    override fun asSuspend(): SuspendAcfListener = object : SuspendAcfListener {
        override suspend fun enterSource(ctx: AcfParser.SourceContext) = this@AcfListener.enterSource(ctx)
        override suspend fun exitSource(ctx: AcfParser.SourceContext) = this@AcfListener.exitSource(ctx)

        override suspend fun enterLine(ctx: AcfParser.LineContext) = this@AcfListener.enterLine(ctx)
        override suspend fun exitLine(ctx: AcfParser.LineContext) = this@AcfListener.exitLine(ctx)

        override suspend fun enterPin(ctx: AcfParser.PinContext) = this@AcfListener.enterPin(ctx)
        override suspend fun exitPin(ctx: AcfParser.PinContext) = this@AcfListener.exitPin(ctx)

        override suspend fun enterAttributeBlock(ctx: AcfParser.AttributeBlockContext) =
            this@AcfListener.enterAttributeBlock(ctx)

        override suspend fun exitAttributeBlock(ctx: AcfParser.AttributeBlockContext) =
            this@AcfListener.exitAttributeBlock(ctx)

        override suspend fun enterName(ctx: AcfParser.NameContext) = this@AcfListener.enterName(ctx)
        override suspend fun exitName(ctx: AcfParser.NameContext) = this@AcfListener.exitName(ctx)

        override suspend fun enterAttribute(ctx: AcfParser.AttributeContext) = this@AcfListener.enterAttribute(ctx)
        override suspend fun exitAttribute(ctx: AcfParser.AttributeContext) = this@AcfListener.exitAttribute(ctx)

        override suspend fun enterAttributeValue(ctx: AcfParser.AttributeValueContext) =
            this@AcfListener.enterAttributeValue(ctx)

        override suspend fun exitAttributeValue(ctx: AcfParser.AttributeValueContext) =
            this@AcfListener.exitAttributeValue(ctx)

        override suspend fun enterPortName(ctx: AcfParser.PortNameContext) = this@AcfListener.enterPortName(ctx)
        override suspend fun exitPortName(ctx: AcfParser.PortNameContext) = this@AcfListener.exitPortName(ctx)

        override suspend fun enterPinName(ctx: AcfParser.PinNameContext) = this@AcfListener.enterPinName(ctx)
        override suspend fun exitPinName(ctx: AcfParser.PinNameContext) = this@AcfListener.exitPinName(ctx)

        override suspend fun enterFrequency(ctx: AcfParser.FrequencyContext) = this@AcfListener.enterFrequency(ctx)
        override suspend fun exitFrequency(ctx: AcfParser.FrequencyContext) = this@AcfListener.exitFrequency(ctx)

        override suspend fun enterArrayIndex(ctx: AcfParser.ArrayIndexContext) = this@AcfListener.enterArrayIndex(ctx)
        override suspend fun exitArrayIndex(ctx: AcfParser.ArrayIndexContext) = this@AcfListener.exitArrayIndex(ctx)

        override suspend fun enterNumber(ctx: AcfParser.NumberContext) = this@AcfListener.enterNumber(ctx)
        override suspend fun exitNumber(ctx: AcfParser.NumberContext) = this@AcfListener.exitNumber(ctx)

        override suspend fun enterSemi(ctx: AcfParser.SemiContext) = this@AcfListener.enterSemi(ctx)
        override suspend fun exitSemi(ctx: AcfParser.SemiContext) = this@AcfListener.exitSemi(ctx)

        override suspend fun enterEveryRule(ctx: ParserRuleContext) = this@AcfListener.enterEveryRule(ctx)
        override suspend fun exitEveryRule(ctx: ParserRuleContext) = this@AcfListener.exitEveryRule(ctx)

        override suspend fun visitErrorNode(node: ErrorNode) = this@AcfListener.visitErrorNode(node)
        override suspend fun visitTerminal(node: TerminalNode) = this@AcfListener.visitTerminal(node)
    }
}

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AcfParser}.
 */
interface SuspendAcfListener : SuspendParseTreeListener {
    /**
     * Enter a parse tree produced by {@link AcfParser#source}.
     * @param ctx the parse tree
     */
    suspend fun enterSource(ctx: AcfParser.SourceContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#source}.
     * @param ctx the parse tree
     */
    suspend fun exitSource(ctx: AcfParser.SourceContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#line}.
     * @param ctx the parse tree
     */
    suspend fun enterLine(ctx: AcfParser.LineContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#line}.
     * @param ctx the parse tree
     */
    suspend fun exitLine(ctx: AcfParser.LineContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#pin}.
     * @param ctx the parse tree
     */
    suspend fun enterPin(ctx: AcfParser.PinContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#pin}.
     * @param ctx the parse tree
     */
    suspend fun exitPin(ctx: AcfParser.PinContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#attributeBlock}.
     * @param ctx the parse tree
     */
    suspend fun enterAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#attributeBlock}.
     * @param ctx the parse tree
     */
    suspend fun exitAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#name}.
     * @param ctx the parse tree
     */
    suspend fun enterName(ctx: AcfParser.NameContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#name}.
     * @param ctx the parse tree
     */
    suspend fun exitName(ctx: AcfParser.NameContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#attribute}.
     * @param ctx the parse tree
     */
    suspend fun enterAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#attribute}.
     * @param ctx the parse tree
     */
    suspend fun exitAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#attributeValue}.
     * @param ctx the parse tree
     */
    suspend fun enterAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#attributeValue}.
     * @param ctx the parse tree
     */
    suspend fun exitAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#portName}.
     * @param ctx the parse tree
     */
    suspend fun enterPortName(ctx: AcfParser.PortNameContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#portName}.
     * @param ctx the parse tree
     */
    suspend fun exitPortName(ctx: AcfParser.PortNameContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#pinName}.
     * @param ctx the parse tree
     */
    suspend fun enterPinName(ctx: AcfParser.PinNameContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#pinName}.
     * @param ctx the parse tree
     */
    suspend fun exitPinName(ctx: AcfParser.PinNameContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#frequency}.
     * @param ctx the parse tree
     */
    suspend fun enterFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#frequency}.
     * @param ctx the parse tree
     */
    suspend fun exitFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#arrayIndex}.
     * @param ctx the parse tree
     */
    suspend fun enterArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#arrayIndex}.
     * @param ctx the parse tree
     */
    suspend fun exitArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#number}.
     * @param ctx the parse tree
     */
    suspend fun enterNumber(ctx: AcfParser.NumberContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#number}.
     * @param ctx the parse tree
     */
    suspend fun exitNumber(ctx: AcfParser.NumberContext)

    /**
     * Enter a parse tree produced by {@link AcfParser#semi}.
     * @param ctx the parse tree
     */
    suspend fun enterSemi(ctx: AcfParser.SemiContext)

    /**
     * Exit a parse tree produced by {@link AcfParser#semi}.
     * @param ctx the parse tree
     */
    suspend fun exitSemi(ctx: AcfParser.SemiContext)
}