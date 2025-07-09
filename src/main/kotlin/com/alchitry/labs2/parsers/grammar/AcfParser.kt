// Generated from Acf.g4 by ANTLR 4.13.1
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.atn.ATN
import org.antlr.v4.kotlinruntime.atn.ATNDeserializer
import org.antlr.v4.kotlinruntime.atn.ParserATNSimulator
import org.antlr.v4.kotlinruntime.atn.PredictionContextCache
import org.antlr.v4.kotlinruntime.dfa.DFA
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

@Suppress(
    // This is required as we are using a custom JsName alias that is not recognized by the IDE.
    // No name clashes will happen tho.
    "JS_NAME_CLASH",
    "UNUSED_VARIABLE",
    "ClassName",
    "FunctionName",
    "LocalVariableName",
    "ConstPropertyName",
    "ConvertSecondaryConstructorToPrimary",
    "CanBeVal",
)
public open class AcfParser(input: TokenStream) : Parser(input) {
    private companion object {
        init {
            RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.runtimeVersion)
        }

        private const val SERIALIZED_ATN: String =
            "\u0004\u0001\u0013\u0081\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\u0008\u0007\u0008\u0002\u0009\u0007\u0009\u0002\u000a\u0007\u000a\u0002\u000b\u0007\u000b\u0002\u000c\u0007\u000c\u0002\u000d\u0007\u000d\u0001\u0000\u0005\u0000\u001e\u0008\u0000\u000a\u0000\u000c\u0000\u0021\u0009\u0000\u0001\u0000\u0003\u0000\u0024\u0008\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u002a\u0008\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0030\u0008\u0002\u000a\u0002\u000c\u0002\u0033\u0009\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u003a\u0008\u0003\u000a\u0003\u000c\u0003\u003d\u0009\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0041\u0008\u0003\u000a\u0003\u000c\u0003\u0044\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\u0055\u0008\u0007\u0001\u0008\u0001\u0008\u0005\u0008\u0059\u0008\u0008\u000a\u0008\u000c\u0008\u005c\u0009\u0008\u0001\u0008\u0001\u0008\u0001\u0008\u0005\u0008\u0061\u0008\u0008\u000a\u0008\u000c\u0008\u0064\u0009\u0008\u0005\u0008\u0066\u0008\u0008\u000a\u0008\u000c\u0008\u0069\u0009\u0008\u0001\u0009\u0001\u0009\u0001\u000a\u0001\u000a\u0001\u000a\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000c\u0001\u000c\u0001\u000d\u0001\u000d\u0005\u000d\u0078\u0008\u000d\u000a\u000d\u000c\u000d\u007b\u0009\u000d\u0001\u000d\u0001\u000d\u0003\u000d\u007f\u0008\u000d\u0001\u000d\u0000\u0000\u000e\u0000\u0002\u0004\u0006\u0008\u000a\u000c\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u0002\u0001\u0000\u000d\u000e\u0001\u0000\u000f\u0010\u0082\u0000\u001f\u0001\u0000\u0000\u0000\u0002\u0029\u0001\u0000\u0000\u0000\u0004\u002b\u0001\u0000\u0000\u0000\u0006\u0036\u0001\u0000\u0000\u0000\u0008\u0047\u0001\u0000\u0000\u0000\u000a\u004a\u0001\u0000\u0000\u0000\u000c\u004c\u0001\u0000\u0000\u0000\u000e\u0054\u0001\u0000\u0000\u0000\u0010\u0056\u0001\u0000\u0000\u0000\u0012\u006a\u0001\u0000\u0000\u0000\u0014\u006c\u0001\u0000\u0000\u0000\u0016\u006f\u0001\u0000\u0000\u0000\u0018\u0073\u0001\u0000\u0000\u0000\u001a\u007e\u0001\u0000\u0000\u0000\u001c\u001e\u0003\u0002\u0001\u0000\u001d\u001c\u0001\u0000\u0000\u0000\u001e\u0021\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000\u001f\u0020\u0001\u0000\u0000\u0000\u0020\u0023\u0001\u0000\u0000\u0000\u0021\u001f\u0001\u0000\u0000\u0000\u0022\u0024\u0005\u0000\u0000\u0001\u0023\u0022\u0001\u0000\u0000\u0000\u0023\u0024\u0001\u0000\u0000\u0000\u0024\u0001\u0001\u0000\u0000\u0000\u0025\u002a\u0003\u0004\u0002\u0000\u0026\u002a\u0003\u0006\u0003\u0000\u0027\u002a\u0003\u0008\u0004\u0000\u0028\u002a\u0005\u000c\u0000\u0000\u0029\u0025\u0001\u0000\u0000\u0000\u0029\u0026\u0001\u0000\u0000\u0000\u0029\u0027\u0001\u0000\u0000\u0000\u0029\u0028\u0001\u0000\u0000\u0000\u002a\u0003\u0001\u0000\u0000\u0000\u002b\u002c\u0005\u0001\u0000\u0000\u002c\u002d\u0003\u0010\u0008\u0000\u002d\u0031\u0003\u0012\u0009\u0000\u002e\u0030\u0003\u000c\u0006\u0000\u002f\u002e\u0001\u0000\u0000\u0000\u0030\u0033\u0001\u0000\u0000\u0000\u0031\u002f\u0001\u0000\u0000\u0000\u0031\u0032\u0001\u0000\u0000\u0000\u0032\u0034\u0001\u0000\u0000\u0000\u0033\u0031\u0001\u0000\u0000\u0000\u0034\u0035\u0003\u001a\u000d\u0000\u0035\u0005\u0001\u0000\u0000\u0000\u0036\u003b\u0003\u000c\u0006\u0000\u0037\u0038\u0005\u0002\u0000\u0000\u0038\u003a\u0003\u000c\u0006\u0000\u0039\u0037\u0001\u0000\u0000\u0000\u003a\u003d\u0001\u0000\u0000\u0000\u003b\u0039\u0001\u0000\u0000\u0000\u003b\u003c\u0001\u0000\u0000\u0000\u003c\u003e\u0001\u0000\u0000\u0000\u003d\u003b\u0001\u0000\u0000\u0000\u003e\u0042\u0005\u0003\u0000\u0000\u003f\u0041\u0003\u0002\u0001\u0000\u0040\u003f\u0001\u0000\u0000\u0000\u0041\u0044\u0001\u0000\u0000\u0000\u0042\u0040\u0001\u0000\u0000\u0000\u0042\u0043\u0001\u0000\u0000\u0000\u0043\u0045\u0001\u0000\u0000\u0000\u0044\u0042\u0001\u0000\u0000\u0000\u0045\u0046\u0005\u0004\u0000\u0000\u0046\u0007\u0001\u0000\u0000\u0000\u0047\u0048\u0005\u000a\u0000\u0000\u0048\u0049\u0005\u0004\u0000\u0000\u0049\u0009\u0001\u0000\u0000\u0000\u004a\u004b\u0007\u0000\u0000\u0000\u004b\u000b\u0001\u0000\u0000\u0000\u004c\u004d\u0005\u000e\u0000\u0000\u004d\u004e\u0005\u0005\u0000\u0000\u004e\u004f\u0003\u000e\u0007\u0000\u004f\u0050\u0005\u0006\u0000\u0000\u0050\u000d\u0001\u0000\u0000\u0000\u0051\u0055\u0005\u000e\u0000\u0000\u0052\u0055\u0003\u0018\u000c\u0000\u0053\u0055\u0003\u0014\u000a\u0000\u0054\u0051\u0001\u0000\u0000\u0000\u0054\u0052\u0001\u0000\u0000\u0000\u0054\u0053\u0001\u0000\u0000\u0000\u0055\u000f\u0001\u0000\u0000\u0000\u0056\u005a\u0003\u000a\u0005\u0000\u0057\u0059\u0003\u0016\u000b\u0000\u0058\u0057\u0001\u0000\u0000\u0000\u0059\u005c\u0001\u0000\u0000\u0000\u005a\u0058\u0001\u0000\u0000\u0000\u005a\u005b\u0001\u0000\u0000\u0000\u005b\u0067\u0001\u0000\u0000\u0000\u005c\u005a\u0001\u0000\u0000\u0000\u005d\u005e\u0005\u0007\u0000\u0000\u005e\u0062\u0003\u000a\u0005\u0000\u005f\u0061\u0003\u0016\u000b\u0000\u0060\u005f\u0001\u0000\u0000\u0000\u0061\u0064\u0001\u0000\u0000\u0000\u0062\u0060\u0001\u0000\u0000\u0000\u0062\u0063\u0001\u0000\u0000\u0000\u0063\u0066\u0001\u0000\u0000\u0000\u0064\u0062\u0001\u0000\u0000\u0000\u0065\u005d\u0001\u0000\u0000\u0000\u0066\u0069\u0001\u0000\u0000\u0000\u0067\u0065\u0001\u0000\u0000\u0000\u0067\u0068\u0001\u0000\u0000\u0000\u0068\u0011\u0001\u0000\u0000\u0000\u0069\u0067\u0001\u0000\u0000\u0000\u006a\u006b\u0003\u000a\u0005\u0000\u006b\u0013\u0001\u0000\u0000\u0000\u006c\u006d\u0003\u0018\u000c\u0000\u006d\u006e\u0005\u000d\u0000\u0000\u006e\u0015\u0001\u0000\u0000\u0000\u006f\u0070\u0005\u0008\u0000\u0000\u0070\u0071\u0005\u0010\u0000\u0000\u0071\u0072\u0005\u0009\u0000\u0000\u0072\u0017\u0001\u0000\u0000\u0000\u0073\u0074\u0007\u0001\u0000\u0000\u0074\u0019\u0001\u0000\u0000\u0000\u0075\u007f\u0005\u000c\u0000\u0000\u0076\u0078\u0005\u000c\u0000\u0000\u0077\u0076\u0001\u0000\u0000\u0000\u0078\u007b\u0001\u0000\u0000\u0000\u0079\u0077\u0001\u0000\u0000\u0000\u0079\u007a\u0001\u0000\u0000\u0000\u007a\u007c\u0001\u0000\u0000\u0000\u007b\u0079\u0001\u0000\u0000\u0000\u007c\u007f\u0005\u000b\u0000\u0000\u007d\u007f\u0005\u0000\u0000\u0001\u007e\u0075\u0001\u0000\u0000\u0000\u007e\u0079\u0001\u0000\u0000\u0000\u007e\u007d\u0001\u0000\u0000\u0000\u007f\u001b\u0001\u0000\u0000\u0000\u000c\u001f\u0023\u0029\u0031\u003b\u0042\u0054\u005a\u0062\u0067\u0079\u007e"

        private val ATN = ATNDeserializer().deserialize(SERIALIZED_ATN.toCharArray())

        private val DECISION_TO_DFA = Array(ATN.numberOfDecisions) {
            DFA(ATN.getDecisionState(it)!!, it)
        }

        private val SHARED_CONTEXT_CACHE = PredictionContextCache()
        private val RULE_NAMES: Array<String> = arrayOf(
            "source", "line", "pin", "attributeBlock", "nativeBlock", "name",
            "attribute", "attributeValue", "portName", "pinName", "frequency",
            "arrayIndex", "number", "semi"
        )

        private val LITERAL_NAMES: Array<String?> = arrayOf(
            null, "'pin'", "','", "'{'", "'}'", "'('", "')'", "'.'", "'['",
            "']'", null, "';'"
        )

        private val SYMBOLIC_NAMES: Array<String?> = arrayOf(
            null, null, null, null, null, null, null, null, null, null,
            "NATIVE_BLOCK", "SEMICOLON", "NL", "FREQ_UNIT", "BASIC_NAME",
            "REAL", "INT", "BLOCK_COMMENT", "COMMENT", "WS"
        )

        private val VOCABULARY = VocabularyImpl(LITERAL_NAMES, SYMBOLIC_NAMES)

        private val TOKEN_NAMES: Array<String> = Array(SYMBOLIC_NAMES.size) {
            VOCABULARY.getLiteralName(it)
                ?: VOCABULARY.getSymbolicName(it)
                ?: "<INVALID>"
        }
    }

    public object Tokens {
        public const val EOF: Int = -1
        public const val T__0: Int = 1
        public const val T__1: Int = 2
        public const val T__2: Int = 3
        public const val T__3: Int = 4
        public const val T__4: Int = 5
        public const val T__5: Int = 6
        public const val T__6: Int = 7
        public const val T__7: Int = 8
        public const val T__8: Int = 9
        public const val NATIVE_BLOCK: Int = 10
        public const val SEMICOLON: Int = 11
        public const val NL: Int = 12
        public const val FREQ_UNIT: Int = 13
        public const val BASIC_NAME: Int = 14
        public const val REAL: Int = 15
        public const val INT: Int = 16
        public const val BLOCK_COMMENT: Int = 17
        public const val COMMENT: Int = 18
        public const val WS: Int = 19
    }

    public object Rules {
        public const val Source: Int = 0
        public const val Line: Int = 1
        public const val Pin: Int = 2
        public const val AttributeBlock: Int = 3
        public const val NativeBlock: Int = 4
        public const val Name: Int = 5
        public const val Attribute: Int = 6
        public const val AttributeValue: Int = 7
        public const val PortName: Int = 8
        public const val PinName: Int = 9
        public const val Frequency: Int = 10
        public const val ArrayIndex: Int = 11
        public const val Number: Int = 12
        public const val Semi: Int = 13
    }

    override var interpreter: ParserATNSimulator =
        @Suppress("LeakingThis")
        ParserATNSimulator(this, ATN, DECISION_TO_DFA, SHARED_CONTEXT_CACHE)

    override val grammarFileName: String =
        "Acf.g4"

    @Deprecated("Use vocabulary instead", replaceWith = ReplaceWith("vocabulary"))
    override val tokenNames: Array<String> =
        TOKEN_NAMES

    override val ruleNames: Array<String> =
        RULE_NAMES

    override val atn: ATN =
        ATN

    override val vocabulary: Vocabulary =
        VOCABULARY

    override val serializedATN: String =
        SERIALIZED_ATN

    /* Named actions */

    /* Funcs */
    public open class SourceContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Source

        public fun line(): List<LineContext> = getRuleContexts(LineContext::class)
        public fun line(i: Int): LineContext? = getRuleContext(LineContext::class, i)
        public fun EOF(): TerminalNode? = getToken(Tokens.EOF, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): SourceContext {
            return SourceContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterSource(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterSource(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitSource(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitSource(this)
            }
        }
    }


    public fun source(): SourceContext {
        var _localctx = SourceContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 0, Rules.Source)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 31
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 21506L) != 0L)) {
                this.state = 28
                line()

                this.state = 33
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 35
            errorHandler.sync(this)

            when (interpreter.adaptivePredict(_input, 1, context)) {
                1 -> {
                    this.state = 34
                    match(Tokens.EOF)

                }
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class LineContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Line

        public fun pin(): PinContext? = getRuleContext(PinContext::class, 0)
        public fun attributeBlock(): AttributeBlockContext? = getRuleContext(AttributeBlockContext::class, 0)
        public fun nativeBlock(): NativeBlockContext? = getRuleContext(NativeBlockContext::class, 0)
        public fun NL(): TerminalNode? = getToken(Tokens.NL, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): LineContext {
            return LineContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterLine(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterLine(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitLine(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitLine(this)
            }
        }
    }


    public fun line(): LineContext {
        var _localctx = LineContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 2, Rules.Line)

        try {
            this.state = 41
            errorHandler.sync(this)

            when (_input.LA(1)) {
                Tokens.T__0 -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 1)
                    this.state = 37
                    pin()

                }

                Tokens.BASIC_NAME -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 2)
                    this.state = 38
                    attributeBlock()

                }

                Tokens.NATIVE_BLOCK -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 3)
                    this.state = 39
                    nativeBlock()

                }

                Tokens.NL -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 4)
                    this.state = 40
                    match(Tokens.NL)

                }
                else -> throw NoViableAltException(this)
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class PinContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Pin

        public fun portName(): PortNameContext = getRuleContext(PortNameContext::class, 0)!!
        public fun pinName(): PinNameContext = getRuleContext(PinNameContext::class, 0)!!
        public fun semi(): SemiContext = getRuleContext(SemiContext::class, 0)!!
        public fun attribute(): List<AttributeContext> = getRuleContexts(AttributeContext::class)
        public fun attribute(i: Int): AttributeContext? = getRuleContext(AttributeContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): PinContext {
            return PinContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterPin(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterPin(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitPin(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitPin(this)
            }
        }
    }


    public fun pin(): PinContext {
        var _localctx = PinContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 4, Rules.Pin)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 43
            match(Tokens.T__0)

            this.state = 44
            portName()

            this.state = 45
            pinName()

            this.state = 49
            errorHandler.sync(this)
            _la = _input.LA(1)

            while (_la == Tokens.BASIC_NAME) {
                this.state = 46
                attribute()

                this.state = 51
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 52
            semi()

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class AttributeBlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.AttributeBlock

        public fun attribute(): List<AttributeContext> = getRuleContexts(AttributeContext::class)
        public fun attribute(i: Int): AttributeContext? = getRuleContext(AttributeContext::class, i)
        public fun line(): List<LineContext> = getRuleContexts(LineContext::class)
        public fun line(i: Int): LineContext? = getRuleContext(LineContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): AttributeBlockContext {
            return AttributeBlockContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterAttributeBlock(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterAttributeBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitAttributeBlock(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitAttributeBlock(this)
            }
        }
    }


    public fun attributeBlock(): AttributeBlockContext {
        var _localctx = AttributeBlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 6, Rules.AttributeBlock)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 54
            attribute()

            this.state = 59
            errorHandler.sync(this)
            _la = _input.LA(1)

            while (_la == Tokens.T__1) {
                this.state = 55
                match(Tokens.T__1)

                this.state = 56
                attribute()

                this.state = 61
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 62
            match(Tokens.T__2)

            this.state = 66
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 21506L) != 0L)) {
                this.state = 63
                line()

                this.state = 68
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 69
            match(Tokens.T__3)

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class NativeBlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.NativeBlock

        public fun NATIVE_BLOCK(): TerminalNode = getToken(Tokens.NATIVE_BLOCK, 0)!!

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): NativeBlockContext {
            return NativeBlockContext().also { it.deepCopyFrom(this) }
        }
        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterNativeBlock(this)
            }
        }
        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterNativeBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitNativeBlock(this)
            }
        }
        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitNativeBlock(this)
            }
        }
    }


    public fun nativeBlock(): NativeBlockContext {
        var _localctx = NativeBlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 8, Rules.NativeBlock)

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 71
            match(Tokens.NATIVE_BLOCK)

            this.state = 72
            match(Tokens.T__3)

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class NameContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Name

        public fun BASIC_NAME(): TerminalNode? = getToken(Tokens.BASIC_NAME, 0)
        public fun FREQ_UNIT(): TerminalNode? = getToken(Tokens.FREQ_UNIT, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): NameContext {
            return NameContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterName(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterName(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitName(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitName(this)
            }
        }
    }


    public fun name(): NameContext {
        var _localctx = NameContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 10, Rules.Name)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 74
            _la = _input.LA(1)

            if (!(_la == Tokens.FREQ_UNIT || _la == Tokens.BASIC_NAME)) {
                errorHandler.recoverInline(this)
            } else {
                if (_input.LA(1) == Tokens.EOF) {
                    isMatchedEOF = true
                }

                errorHandler.reportMatch(this)
                consume()
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class AttributeContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Attribute

        public fun BASIC_NAME(): TerminalNode = getToken(Tokens.BASIC_NAME, 0)!!
        public fun attributeValue(): AttributeValueContext = getRuleContext(AttributeValueContext::class, 0)!!

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): AttributeContext {
            return AttributeContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterAttribute(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterAttribute(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitAttribute(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitAttribute(this)
            }
        }
    }


    public fun attribute(): AttributeContext {
        var _localctx = AttributeContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 12, Rules.Attribute)

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 76
            match(Tokens.BASIC_NAME)

            this.state = 77
            match(Tokens.T__4)

            this.state = 78
            attributeValue()

            this.state = 79
            match(Tokens.T__5)

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class AttributeValueContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.AttributeValue

        public fun BASIC_NAME(): TerminalNode? = getToken(Tokens.BASIC_NAME, 0)
        public fun number(): NumberContext? = getRuleContext(NumberContext::class, 0)
        public fun frequency(): FrequencyContext? = getRuleContext(FrequencyContext::class, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): AttributeValueContext {
            return AttributeValueContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterAttributeValue(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterAttributeValue(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitAttributeValue(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitAttributeValue(this)
            }
        }
    }


    public fun attributeValue(): AttributeValueContext {
        var _localctx = AttributeValueContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 14, Rules.AttributeValue)

        try {
            this.state = 84
            errorHandler.sync(this)

            when (interpreter.adaptivePredict(_input, 6, context)) {
                1 -> {
                    enterOuterAlt(_localctx, 1)
                    this.state = 81
                    match(Tokens.BASIC_NAME)

                }

                2 -> {
                    enterOuterAlt(_localctx, 2)
                    this.state = 82
                    number()

                }

                3 -> {
                    enterOuterAlt(_localctx, 3)
                    this.state = 83
                    frequency()

                }
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class PortNameContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.PortName

        public fun name(): List<NameContext> = getRuleContexts(NameContext::class)
        public fun name(i: Int): NameContext? = getRuleContext(NameContext::class, i)
        public fun arrayIndex(): List<ArrayIndexContext> = getRuleContexts(ArrayIndexContext::class)
        public fun arrayIndex(i: Int): ArrayIndexContext? = getRuleContext(ArrayIndexContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): PortNameContext {
            return PortNameContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterPortName(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterPortName(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitPortName(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitPortName(this)
            }
        }
    }


    public fun portName(): PortNameContext {
        var _localctx = PortNameContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 16, Rules.PortName)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 86
            name()

            this.state = 90
            errorHandler.sync(this)
            _la = _input.LA(1)

            while (_la == Tokens.T__7) {
                this.state = 87
                arrayIndex()

                this.state = 92
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 103
            errorHandler.sync(this)
            _la = _input.LA(1)

            while (_la == Tokens.T__6) {
                this.state = 93
                match(Tokens.T__6)

                this.state = 94
                name()

                this.state = 98
                errorHandler.sync(this)
                _la = _input.LA(1)

                while (_la == Tokens.T__7) {
                    this.state = 95
                    arrayIndex()

                    this.state = 100
                    errorHandler.sync(this)
                    _la = _input.LA(1)
                }
                this.state = 105
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class PinNameContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.PinName

        public fun name(): NameContext = getRuleContext(NameContext::class, 0)!!

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): PinNameContext {
            return PinNameContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterPinName(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterPinName(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitPinName(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitPinName(this)
            }
        }
    }


    public fun pinName(): PinNameContext {
        var _localctx = PinNameContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 18, Rules.PinName)

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 106
            name()

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class FrequencyContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Frequency

        public fun number(): NumberContext = getRuleContext(NumberContext::class, 0)!!
        public fun FREQ_UNIT(): TerminalNode = getToken(Tokens.FREQ_UNIT, 0)!!

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): FrequencyContext {
            return FrequencyContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterFrequency(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterFrequency(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitFrequency(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitFrequency(this)
            }
        }
    }


    public fun frequency(): FrequencyContext {
        var _localctx = FrequencyContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 20, Rules.Frequency)

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 108
            number()

            this.state = 109
            match(Tokens.FREQ_UNIT)

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class ArrayIndexContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.ArrayIndex

        public fun INT(): TerminalNode = getToken(Tokens.INT, 0)!!

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): ArrayIndexContext {
            return ArrayIndexContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterArrayIndex(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterArrayIndex(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitArrayIndex(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitArrayIndex(this)
            }
        }
    }


    public fun arrayIndex(): ArrayIndexContext {
        var _localctx = ArrayIndexContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 22, Rules.ArrayIndex)

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 111
            match(Tokens.T__7)

            this.state = 112
            match(Tokens.INT)

            this.state = 113
            match(Tokens.T__8)

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class NumberContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Number

        public fun INT(): TerminalNode? = getToken(Tokens.INT, 0)
        public fun REAL(): TerminalNode? = getToken(Tokens.REAL, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): NumberContext {
            return NumberContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterNumber(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterNumber(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitNumber(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitNumber(this)
            }
        }
    }


    public fun number(): NumberContext {
        var _localctx = NumberContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 24, Rules.Number)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 115
            _la = _input.LA(1)

            if (!(_la == Tokens.REAL || _la == Tokens.INT)) {
                errorHandler.recoverInline(this)
            } else {
                if (_input.LA(1) == Tokens.EOF) {
                    isMatchedEOF = true
                }

                errorHandler.reportMatch(this)
                consume()
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class SemiContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Semi

        public fun NL(): List<TerminalNode> = getTokens(Tokens.NL)
        public fun NL(i: Int): TerminalNode? = getToken(Tokens.NL, i)
        public fun SEMICOLON(): TerminalNode? = getToken(Tokens.SEMICOLON, 0)
        public fun EOF(): TerminalNode? = getToken(Tokens.EOF, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): SemiContext {
            return SemiContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.enterSemi(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.enterSemi(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) {
                listener.exitSemi(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) {
                listener.exitSemi(this)
            }
        }
    }


    public fun semi(): SemiContext {
        var _localctx = SemiContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 26, Rules.Semi)
        var _la: Int

        try {
            this.state = 126
            errorHandler.sync(this)

            when (interpreter.adaptivePredict(_input, 11, context)) {
                1 -> {
                    enterOuterAlt(_localctx, 1)
                    this.state = 117
                    match(Tokens.NL)

                }

                2 -> {
                    enterOuterAlt(_localctx, 2)
                    this.state = 121
                    errorHandler.sync(this)
                    _la = _input.LA(1)

                    while (_la == Tokens.NL) {
                        this.state = 118
                        match(Tokens.NL)

                        this.state = 123
                        errorHandler.sync(this)
                        _la = _input.LA(1)
                    }
                    this.state = 124
                    match(Tokens.SEMICOLON)

                }

                3 -> {
                    enterOuterAlt(_localctx, 3)
                    this.state = 125
                    match(Tokens.EOF)

                }
            }
        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }
}
