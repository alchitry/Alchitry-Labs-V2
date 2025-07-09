// Generated from Acf.g4 by ANTLR 4.13.1
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by [AcfParser].
 */
public interface AcfListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by [AcfParser.source].
     *
     * @param ctx The parse tree
     */
    public fun enterSource(ctx: AcfParser.SourceContext)

    /**
     * Exit a parse tree produced by [AcfParser.source].
     *
     * @param ctx The parse tree
     */
    public fun exitSource(ctx: AcfParser.SourceContext)

    /**
     * Enter a parse tree produced by [AcfParser.line].
     *
     * @param ctx The parse tree
     */
    public fun enterLine(ctx: AcfParser.LineContext)

    /**
     * Exit a parse tree produced by [AcfParser.line].
     *
     * @param ctx The parse tree
     */
    public fun exitLine(ctx: AcfParser.LineContext)

    /**
     * Enter a parse tree produced by [AcfParser.pin].
     *
     * @param ctx The parse tree
     */
    public fun enterPin(ctx: AcfParser.PinContext)

    /**
     * Exit a parse tree produced by [AcfParser.pin].
     *
     * @param ctx The parse tree
     */
    public fun exitPin(ctx: AcfParser.PinContext)

    /**
     * Enter a parse tree produced by [AcfParser.attributeBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Exit a parse tree produced by [AcfParser.attributeBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Enter a parse tree produced by [AcfParser.nativeBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterNativeBlock(ctx: AcfParser.NativeBlockContext)

    /**
     * Exit a parse tree produced by [AcfParser.nativeBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitNativeBlock(ctx: AcfParser.NativeBlockContext)

    /**
     * Enter a parse tree produced by [AcfParser.name].
     *
     * @param ctx The parse tree
     */
    public fun enterName(ctx: AcfParser.NameContext)

    /**
     * Exit a parse tree produced by [AcfParser.name].
     *
     * @param ctx The parse tree
     */
    public fun exitName(ctx: AcfParser.NameContext)

    /**
     * Enter a parse tree produced by [AcfParser.attribute].
     *
     * @param ctx The parse tree
     */
    public fun enterAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Exit a parse tree produced by [AcfParser.attribute].
     *
     * @param ctx The parse tree
     */
    public fun exitAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Enter a parse tree produced by [AcfParser.attributeValue].
     *
     * @param ctx The parse tree
     */
    public fun enterAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Exit a parse tree produced by [AcfParser.attributeValue].
     *
     * @param ctx The parse tree
     */
    public fun exitAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Enter a parse tree produced by [AcfParser.portName].
     *
     * @param ctx The parse tree
     */
    public fun enterPortName(ctx: AcfParser.PortNameContext)

    /**
     * Exit a parse tree produced by [AcfParser.portName].
     *
     * @param ctx The parse tree
     */
    public fun exitPortName(ctx: AcfParser.PortNameContext)

    /**
     * Enter a parse tree produced by [AcfParser.pinName].
     *
     * @param ctx The parse tree
     */
    public fun enterPinName(ctx: AcfParser.PinNameContext)

    /**
     * Exit a parse tree produced by [AcfParser.pinName].
     *
     * @param ctx The parse tree
     */
    public fun exitPinName(ctx: AcfParser.PinNameContext)

    /**
     * Enter a parse tree produced by [AcfParser.frequency].
     *
     * @param ctx The parse tree
     */
    public fun enterFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Exit a parse tree produced by [AcfParser.frequency].
     *
     * @param ctx The parse tree
     */
    public fun exitFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Enter a parse tree produced by [AcfParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public fun enterArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Exit a parse tree produced by [AcfParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public fun exitArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Enter a parse tree produced by [AcfParser.number].
     *
     * @param ctx The parse tree
     */
    public fun enterNumber(ctx: AcfParser.NumberContext)

    /**
     * Exit a parse tree produced by [AcfParser.number].
     *
     * @param ctx The parse tree
     */
    public fun exitNumber(ctx: AcfParser.NumberContext)

    /**
     * Enter a parse tree produced by [AcfParser.semi].
     *
     * @param ctx The parse tree
     */
    public fun enterSemi(ctx: AcfParser.SemiContext)

    /**
     * Exit a parse tree produced by [AcfParser.semi].
     *
     * @param ctx The parse tree
     */
    public fun exitSemi(ctx: AcfParser.SemiContext)


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

        override suspend fun enterNativeBlock(ctx: AcfParser.NativeBlockContext) =
            this@AcfListener.enterNativeBlock(ctx)
        override suspend fun exitNativeBlock(ctx: AcfParser.NativeBlockContext) = this@AcfListener.exitNativeBlock(ctx)

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
 * This interface defines a complete suspend listener for a parse tree produced by [AcfParser].
 */
public interface SuspendAcfListener : SuspendParseTreeListener {
    /**
     * Enter a parse tree produced by [AcfParser.source].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSource(ctx: AcfParser.SourceContext)

    /**
     * Exit a parse tree produced by [AcfParser.source].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSource(ctx: AcfParser.SourceContext)

    /**
     * Enter a parse tree produced by [AcfParser.line].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterLine(ctx: AcfParser.LineContext)

    /**
     * Exit a parse tree produced by [AcfParser.line].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitLine(ctx: AcfParser.LineContext)

    /**
     * Enter a parse tree produced by [AcfParser.pin].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterPin(ctx: AcfParser.PinContext)

    /**
     * Exit a parse tree produced by [AcfParser.pin].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitPin(ctx: AcfParser.PinContext)

    /**
     * Enter a parse tree produced by [AcfParser.attributeBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Exit a parse tree produced by [AcfParser.attributeBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAttributeBlock(ctx: AcfParser.AttributeBlockContext)

    /**
     * Enter a parse tree produced by [AcfParser.nativeBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterNativeBlock(ctx: AcfParser.NativeBlockContext)

    /**
     * Exit a parse tree produced by [AcfParser.nativeBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitNativeBlock(ctx: AcfParser.NativeBlockContext)

    /**
     * Enter a parse tree produced by [AcfParser.name].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterName(ctx: AcfParser.NameContext)

    /**
     * Exit a parse tree produced by [AcfParser.name].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitName(ctx: AcfParser.NameContext)

    /**
     * Enter a parse tree produced by [AcfParser.attribute].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Exit a parse tree produced by [AcfParser.attribute].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAttribute(ctx: AcfParser.AttributeContext)

    /**
     * Enter a parse tree produced by [AcfParser.attributeValue].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Exit a parse tree produced by [AcfParser.attributeValue].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAttributeValue(ctx: AcfParser.AttributeValueContext)

    /**
     * Enter a parse tree produced by [AcfParser.portName].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterPortName(ctx: AcfParser.PortNameContext)

    /**
     * Exit a parse tree produced by [AcfParser.portName].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitPortName(ctx: AcfParser.PortNameContext)

    /**
     * Enter a parse tree produced by [AcfParser.pinName].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterPinName(ctx: AcfParser.PinNameContext)

    /**
     * Exit a parse tree produced by [AcfParser.pinName].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitPinName(ctx: AcfParser.PinNameContext)

    /**
     * Enter a parse tree produced by [AcfParser.frequency].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Exit a parse tree produced by [AcfParser.frequency].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitFrequency(ctx: AcfParser.FrequencyContext)

    /**
     * Enter a parse tree produced by [AcfParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Exit a parse tree produced by [AcfParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitArrayIndex(ctx: AcfParser.ArrayIndexContext)

    /**
     * Enter a parse tree produced by [AcfParser.number].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterNumber(ctx: AcfParser.NumberContext)

    /**
     * Exit a parse tree produced by [AcfParser.number].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitNumber(ctx: AcfParser.NumberContext)

    /**
     * Enter a parse tree produced by [AcfParser.semi].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSemi(ctx: AcfParser.SemiContext)

    /**
     * Exit a parse tree produced by [AcfParser.semi].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSemi(ctx: AcfParser.SemiContext)

}