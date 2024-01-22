// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar;
import com.alchitry.kotlinmultiplatform.asCharArray
import com.alchitry.kotlinmultiplatform.scoped
import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.atn.ATN
import org.antlr.v4.kotlinruntime.atn.ATNDeserializer
import org.antlr.v4.kotlinruntime.atn.ParserATNSimulator
import org.antlr.v4.kotlinruntime.atn.PredictionContextCache
import org.antlr.v4.kotlinruntime.dfa.DFA
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

class AcfParser(input: TokenStream) : Parser(input) {
    // TODO verify version of runtime is compatible

    override val grammarFileName: String
        get() = "Acf.g4"

    override val tokenNames: Array<String?> = AcfParser.tokenNames
    override val ruleNames: Array<String> = AcfParser.ruleNames
    override val atn: ATN = AcfParser.ATN
    override val vocabulary: Vocabulary = AcfParser.VOCABULARY

    enum class Tokens(val id: Int) {
        EOF(-1),
        T__0(1),
        T__1(2),
        T__2(3),
        T__3(4),
        T__4(5),
        PULLUP(6),
        PULLDOWN(7),
        SEMICOLON(8),
        NL(9),
        FREQ_UNIT(10),
        BASIC_NAME(11),
        REAL(12),
        INT(13),
        BLOCK_COMMENT(14),
        COMMENT(15),
        WS(16)
    }

    enum class Rules(val id: Int) {
        RULE_source(0),
        RULE_pin(1),
        RULE_clock(2),
        RULE_name(3),
        RULE_portName(4),
        RULE_pinName(5),
        RULE_frequency(6),
        RULE_arrayIndex(7),
        RULE_number(8),
        RULE_semi(9)
    }

    companion object {
        protected val decisionToDFA: Array<DFA>
        protected val sharedContextCache = PredictionContextCache()

        val ruleNames = arrayOf(
            "source", "pin", "clock", "name", "portName",
            "pinName", "frequency", "arrayIndex", "number",
            "semi"
        )

        private val LITERAL_NAMES: List<String?> = listOf(
            null, "'pin'",
            "'clock'", "'.'",
            "'['", "']'",
            "'pullup'", "'pulldown'",
            "';'"
        )
        private val SYMBOLIC_NAMES: List<String?> = listOf(
            null, null, null,
            null, null, null,
            "PULLUP", "PULLDOWN",
            "SEMICOLON",
            "NL", "FREQ_UNIT",
            "BASIC_NAME",
            "REAL", "INT",
            "BLOCK_COMMENT",
            "COMMENT", "WS"
        )

        val VOCABULARY = VocabularyImpl(LITERAL_NAMES.toTypedArray(), SYMBOLIC_NAMES.toTypedArray())

        val tokenNames: Array<String?> = Array<String?>(SYMBOLIC_NAMES.size) {
            var el = VOCABULARY.getLiteralName(it)
            if (el == null) {
                el = VOCABULARY.getSymbolicName(it)
            }

            if (el == null) {
                el = "<INVALID>"
            }
            el
        }

        private const val serializedATN: String =
            "\u0004\u0001\u0010\u005d\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\u0008\u0007\u0008\u0002\u0009\u0007\u0009\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u0018\u0008\u0000\u000a\u0000\u000c\u0000\u001b\u0009\u0000\u0001\u0000\u0003\u0000\u001e\u0008\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0024\u0008\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u002c\u0008\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0005\u0004\u0035\u0008\u0004\u000a\u0004\u000c\u0004\u0038\u0009\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u003d\u0008\u0004\u000a\u0004\u000c\u0004\u0040\u0009\u0004\u0005\u0004\u0042\u0008\u0004\u000a\u0004\u000c\u0004\u0045\u0009\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0008\u0001\u0008\u0001\u0009\u0001\u0009\u0005\u0009\u0054\u0008\u0009\u000a\u0009\u000c\u0009\u0057\u0009\u0009\u0001\u0009\u0001\u0009\u0003\u0009\u005b\u0008\u0009\u0001\u0009\u0000\u0000\u000a\u0000\u0002\u0004\u0006\u0008\u000a\u000c\u000e\u0010\u0012\u0000\u0003\u0001\u0000\u0006\u0007\u0001\u0000\u000a\u000b\u0001\u0000\u000c\u000d\u005e\u0000\u0019\u0001\u0000\u0000\u0000\u0002\u001f\u0001\u0000\u0000\u0000\u0004\u0027\u0001\u0000\u0000\u0000\u0006\u0030\u0001\u0000\u0000\u0000\u0008\u0032\u0001\u0000\u0000\u0000\u000a\u0046\u0001\u0000\u0000\u0000\u000c\u0048\u0001\u0000\u0000\u0000\u000e\u004b\u0001\u0000\u0000\u0000\u0010\u004f\u0001\u0000\u0000\u0000\u0012\u005a\u0001\u0000\u0000\u0000\u0014\u0018\u0003\u0002\u0001\u0000\u0015\u0018\u0003\u0004\u0002\u0000\u0016\u0018\u0005\u0009\u0000\u0000\u0017\u0014\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0017\u0016\u0001\u0000\u0000\u0000\u0018\u001b\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000\u001a\u001d\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000\u0000\u0000\u001c\u001e\u0005\u0000\u0000\u0001\u001d\u001c\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u0001\u0001\u0000\u0000\u0000\u001f\u0020\u0005\u0001\u0000\u0000\u0020\u0021\u0003\u0008\u0004\u0000\u0021\u0023\u0003\u000a\u0005\u0000\u0022\u0024\u0007\u0000\u0000\u0000\u0023\u0022\u0001\u0000\u0000\u0000\u0023\u0024\u0001\u0000\u0000\u0000\u0024\u0025\u0001\u0000\u0000\u0000\u0025\u0026\u0003\u0012\u0009\u0000\u0026\u0003\u0001\u0000\u0000\u0000\u0027\u0028\u0005\u0002\u0000\u0000\u0028\u0029\u0003\u0008\u0004\u0000\u0029\u002b\u0003\u000a\u0005\u0000\u002a\u002c\u0007\u0000\u0000\u0000\u002b\u002a\u0001\u0000\u0000\u0000\u002b\u002c\u0001\u0000\u0000\u0000\u002c\u002d\u0001\u0000\u0000\u0000\u002d\u002e\u0003\u000c\u0006\u0000\u002e\u002f\u0003\u0012\u0009\u0000\u002f\u0005\u0001\u0000\u0000\u0000\u0030\u0031\u0007\u0001\u0000\u0000\u0031\u0007\u0001\u0000\u0000\u0000\u0032\u0036\u0003\u0006\u0003\u0000\u0033\u0035\u0003\u000e\u0007\u0000\u0034\u0033\u0001\u0000\u0000\u0000\u0035\u0038\u0001\u0000\u0000\u0000\u0036\u0034\u0001\u0000\u0000\u0000\u0036\u0037\u0001\u0000\u0000\u0000\u0037\u0043\u0001\u0000\u0000\u0000\u0038\u0036\u0001\u0000\u0000\u0000\u0039\u003a\u0005\u0003\u0000\u0000\u003a\u003e\u0003\u0006\u0003\u0000\u003b\u003d\u0003\u000e\u0007\u0000\u003c\u003b\u0001\u0000\u0000\u0000\u003d\u0040\u0001\u0000\u0000\u0000\u003e\u003c\u0001\u0000\u0000\u0000\u003e\u003f\u0001\u0000\u0000\u0000\u003f\u0042\u0001\u0000\u0000\u0000\u0040\u003e\u0001\u0000\u0000\u0000\u0041\u0039\u0001\u0000\u0000\u0000\u0042\u0045\u0001\u0000\u0000\u0000\u0043\u0041\u0001\u0000\u0000\u0000\u0043\u0044\u0001\u0000\u0000\u0000\u0044\u0009\u0001\u0000\u0000\u0000\u0045\u0043\u0001\u0000\u0000\u0000\u0046\u0047\u0003\u0006\u0003\u0000\u0047\u000b\u0001\u0000\u0000\u0000\u0048\u0049\u0003\u0010\u0008\u0000\u0049\u004a\u0005\u000a\u0000\u0000\u004a\u000d\u0001\u0000\u0000\u0000\u004b\u004c\u0005\u0004\u0000\u0000\u004c\u004d\u0005\u000d\u0000\u0000\u004d\u004e\u0005\u0005\u0000\u0000\u004e\u000f\u0001\u0000\u0000\u0000\u004f\u0050\u0007\u0002\u0000\u0000\u0050\u0011\u0001\u0000\u0000\u0000\u0051\u005b\u0005\u0009\u0000\u0000\u0052\u0054\u0005\u0009\u0000\u0000\u0053\u0052\u0001\u0000\u0000\u0000\u0054\u0057\u0001\u0000\u0000\u0000\u0055\u0053\u0001\u0000\u0000\u0000\u0055\u0056\u0001\u0000\u0000\u0000\u0056\u0058\u0001\u0000\u0000\u0000\u0057\u0055\u0001\u0000\u0000\u0000\u0058\u005b\u0005\u0008\u0000\u0000\u0059\u005b\u0005\u0000\u0000\u0001\u005a\u0051\u0001\u0000\u0000\u0000\u005a\u0055\u0001\u0000\u0000\u0000\u005a\u0059\u0001\u0000\u0000\u0000\u005b\u0013\u0001\u0000\u0000\u0000\u000a\u0017\u0019\u001d\u0023\u002b\u0036\u003e\u0043\u0055\u005a"

        val ATN = ATNDeserializer().deserialize(serializedATN.asCharArray())
        init {
            decisionToDFA = Array<DFA>(ATN.numberOfDecisions, {
                DFA(ATN.getDecisionState(it)!!, it)
            })


        }
    }

    private val T__0 = Tokens.T__0.id
    private val T__1 = Tokens.T__1.id
    private val T__2 = Tokens.T__2.id
    private val T__3 = Tokens.T__3.id
    private val T__4 = Tokens.T__4.id
    private val PULLUP = Tokens.PULLUP.id
    private val PULLDOWN = Tokens.PULLDOWN.id
    private val SEMICOLON = Tokens.SEMICOLON.id
    private val NL = Tokens.NL.id
    private val FREQ_UNIT = Tokens.FREQ_UNIT.id
    private val BASIC_NAME = Tokens.BASIC_NAME.id
    private val REAL = Tokens.REAL.id
    private val INT = Tokens.INT.id
    private val BLOCK_COMMENT = Tokens.BLOCK_COMMENT.id
    private val COMMENT = Tokens.COMMENT.id
    private val WS = Tokens.WS.id

    /* Named actions */
    init {
        interpreter = ParserATNSimulator(this, ATN, decisionToDFA, sharedContextCache)
    }
    /* Funcs */

    open class SourceContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_source.id
            set(value) {
                throw RuntimeException()
            }

        fun pin(): List<PinContext> = getRuleContexts(PinContext::class)
        fun pin(i: Int): PinContext? = getRuleContext(PinContext::class, i)
        fun clock(): List<ClockContext> = getRuleContexts(ClockContext::class)
        fun clock(i: Int): ClockContext? = getRuleContext(ClockContext::class, i)
        fun NL(): List<TerminalNode> = getTokens(Tokens.NL.id)
        fun NL(i: Int): TerminalNode = getToken(Tokens.NL.id, i) as TerminalNode
        fun EOF(): TerminalNode? = getToken(Tokens.EOF.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterSource(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterSource(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitSource(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitSource(this)
        }
    }

    fun source(): SourceContext {
        var _localctx: SourceContext = SourceContext(context, state)
        enterRule(_localctx, 0, Rules.RULE_source.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 25
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 518L) != 0L)) {
                    scoped {
                        this.state = 23
                        errorHandler.sync(this)
                        when (_input!!.LA(1)) {
                            T__0 ->  /*LL1AltBlock*/ {
                                scoped {
                                    this.state = 20
                                    pin()
                                }
                            }

                            T__1 ->  /*LL1AltBlock*/ {
                                scoped {
                                    this.state = 21
                                    clock()
                                }
                            }

                            NL ->  /*LL1AltBlock*/ {
                                scoped {
                                    this.state = 22
                                    match(NL)
                                }
                            }

                            else -> throw NoViableAltException(this)
                        }
                    }
                    this.state = 27
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 29
                errorHandler.sync(this)
                when (interpreter!!.adaptivePredict(_input!!, 2, context)) {
                    1 -> scoped {
                        this.state = 28
                        match(EOF)
                    }
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

    open class PinContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_pin.id
            set(value) {
                throw RuntimeException()
            }

        fun portName(): PortNameContext? = getRuleContext(PortNameContext::class, 0)
        fun pinName(): PinNameContext? = getRuleContext(PinNameContext::class, 0)
        fun semi(): SemiContext? = getRuleContext(SemiContext::class, 0)
        fun PULLUP(): TerminalNode? = getToken(Tokens.PULLUP.id, 0)
        fun PULLDOWN(): TerminalNode? = getToken(Tokens.PULLDOWN.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterPin(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterPin(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitPin(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitPin(this)
        }
    }

    fun pin(): PinContext {
        var _localctx: PinContext = PinContext(context, state)
        enterRule(_localctx, 2, Rules.RULE_pin.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 31
                match(T__0)
                this.state = 32
                portName()
                this.state = 33
                pinName()
                this.state = 35
                errorHandler.sync(this)
                _la = _input!!.LA(1)
                if (_la == PULLUP || _la == PULLDOWN) {
                    scoped {
                        this.state = 34
                        _la = _input!!.LA(1)
                        if (!(_la == PULLUP || _la == PULLDOWN)) {
                            errorHandler.recoverInline(this)
                        } else {
                            if (_input!!.LA(1) == Tokens.EOF.id) isMatchedEOF = true
                            errorHandler.reportMatch(this)
                            consume()
                        }
                    }
                }

                this.state = 37
                semi()
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

    open class ClockContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_clock.id
            set(value) {
                throw RuntimeException()
            }

        fun portName(): PortNameContext? = getRuleContext(PortNameContext::class, 0)
        fun pinName(): PinNameContext? = getRuleContext(PinNameContext::class, 0)
        fun frequency(): FrequencyContext? = getRuleContext(FrequencyContext::class, 0)
        fun semi(): SemiContext? = getRuleContext(SemiContext::class, 0)
        fun PULLUP(): TerminalNode? = getToken(Tokens.PULLUP.id, 0)
        fun PULLDOWN(): TerminalNode? = getToken(Tokens.PULLDOWN.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterClock(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterClock(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitClock(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitClock(this)
        }
    }

    fun clock(): ClockContext {
        var _localctx: ClockContext = ClockContext(context, state)
        enterRule(_localctx, 4, Rules.RULE_clock.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 39
                match(T__1)
                this.state = 40
                portName()
                this.state = 41
                pinName()
                this.state = 43
                errorHandler.sync(this)
                _la = _input!!.LA(1)
                if (_la == PULLUP || _la == PULLDOWN) {
                    scoped {
                        this.state = 42
                        _la = _input!!.LA(1)
                        if (!(_la == PULLUP || _la == PULLDOWN)) {
                            errorHandler.recoverInline(this)
                        } else {
                            if (_input!!.LA(1) == Tokens.EOF.id) isMatchedEOF = true
                            errorHandler.reportMatch(this)
                            consume()
                        }
                    }
                }

                this.state = 45
                frequency()
                this.state = 46
                semi()
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

    open class NameContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_name.id
            set(value) {
                throw RuntimeException()
            }

        fun BASIC_NAME(): TerminalNode? = getToken(Tokens.BASIC_NAME.id, 0)
        fun FREQ_UNIT(): TerminalNode? = getToken(Tokens.FREQ_UNIT.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterName(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterName(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitName(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitName(this)
        }
    }

    fun name(): NameContext {
        var _localctx: NameContext = NameContext(context, state)
        enterRule(_localctx, 6, Rules.RULE_name.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 48
                _la = _input!!.LA(1)
                if (!(_la == FREQ_UNIT || _la == BASIC_NAME)) {
                    errorHandler.recoverInline(this)
                } else {
                    if (_input!!.LA(1) == Tokens.EOF.id) isMatchedEOF = true
                    errorHandler.reportMatch(this)
                    consume()
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

    open class PortNameContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_portName.id
            set(value) {
                throw RuntimeException()
            }

        fun name(): List<NameContext> = getRuleContexts(NameContext::class)
        fun name(i: Int): NameContext? = getRuleContext(NameContext::class, i)
        fun arrayIndex(): List<ArrayIndexContext> = getRuleContexts(ArrayIndexContext::class)
        fun arrayIndex(i: Int): ArrayIndexContext? = getRuleContext(ArrayIndexContext::class, i)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterPortName(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterPortName(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitPortName(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitPortName(this)
        }
    }

    fun portName(): PortNameContext {
        var _localctx: PortNameContext = PortNameContext(context, state)
        enterRule(_localctx, 8, Rules.RULE_portName.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 50
                name()
                this.state = 54
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while (_la == T__3) {
                    scoped {
                        scoped {
                            this.state = 51
                            arrayIndex()
                        }
                    }
                    this.state = 56
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 67
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while (_la == T__2) {
                    scoped {
                        scoped {
                            this.state = 57
                            match(T__2)
                            this.state = 58
                            name()
                            this.state = 62
                            errorHandler.sync(this);
                            _la = _input!!.LA(1)
                            while (_la == T__3) {
                                scoped {
                                    scoped {
                                        this.state = 59
                                        arrayIndex()
                                    }
                                }
                                this.state = 64
                                errorHandler.sync(this)
                                _la = _input!!.LA(1)
                            }
                        }
                    }
                    this.state = 69
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
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

    open class PinNameContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_pinName.id
            set(value) {
                throw RuntimeException()
            }

        fun name(): NameContext? = getRuleContext(NameContext::class, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterPinName(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterPinName(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitPinName(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitPinName(this)
        }
    }

    fun pinName(): PinNameContext {
        var _localctx: PinNameContext = PinNameContext(context, state)
        enterRule(_localctx, 10, Rules.RULE_pinName.id)
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 70
                name()
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

    open class FrequencyContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_frequency.id
            set(value) {
                throw RuntimeException()
            }

        fun number(): NumberContext? = getRuleContext(NumberContext::class, 0)
        fun FREQ_UNIT(): TerminalNode? = getToken(Tokens.FREQ_UNIT.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterFrequency(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterFrequency(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitFrequency(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitFrequency(this)
        }
    }

    fun frequency(): FrequencyContext {
        var _localctx: FrequencyContext = FrequencyContext(context, state)
        enterRule(_localctx, 12, Rules.RULE_frequency.id)
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 72
                number()
                this.state = 73
                match(FREQ_UNIT)
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

    open class ArrayIndexContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_arrayIndex.id
            set(value) {
                throw RuntimeException()
            }

        fun INT(): TerminalNode? = getToken(Tokens.INT.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterArrayIndex(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterArrayIndex(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitArrayIndex(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitArrayIndex(this)
        }
    }

    fun arrayIndex(): ArrayIndexContext {
        var _localctx: ArrayIndexContext = ArrayIndexContext(context, state)
        enterRule(_localctx, 14, Rules.RULE_arrayIndex.id)
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 75
                match(T__3)
                this.state = 76
                match(INT)
                this.state = 77
                match(T__4)
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

    open class NumberContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_number.id
            set(value) {
                throw RuntimeException()
            }

        fun INT(): TerminalNode? = getToken(Tokens.INT.id, 0)
        fun REAL(): TerminalNode? = getToken(Tokens.REAL.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterNumber(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterNumber(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitNumber(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitNumber(this)
        }
    }

    fun number(): NumberContext {
        var _localctx: NumberContext = NumberContext(context, state)
        enterRule(_localctx, 16, Rules.RULE_number.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 79
                _la = _input!!.LA(1)
                if (!(_la == REAL || _la == INT)) {
                    errorHandler.recoverInline(this)
                } else {
                    if (_input!!.LA(1) == Tokens.EOF.id) isMatchedEOF = true
                    errorHandler.reportMatch(this)
                    consume()
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

    open class SemiContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_semi.id
            set(value) {
                throw RuntimeException()
            }

        fun NL(): List<TerminalNode> = getTokens(Tokens.NL.id)
        fun NL(i: Int): TerminalNode = getToken(Tokens.NL.id, i) as TerminalNode
        fun SEMICOLON(): TerminalNode? = getToken(Tokens.SEMICOLON.id, 0)
        fun EOF(): TerminalNode? = getToken(Tokens.EOF.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.enterSemi(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.enterSemi(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is AcfListener) listener.exitSemi(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendAcfListener) listener.exitSemi(this)
        }
    }

    fun semi(): SemiContext {
        var _localctx: SemiContext = SemiContext(context, state)
        enterRule(_localctx, 18, Rules.RULE_semi.id)
        var _la: Int
        try {
            this.state = 90
            errorHandler.sync(this)
            when (interpreter!!.adaptivePredict(_input!!, 9, context)) {
                1 -> {
                    enterOuterAlt(_localctx, 1)
                    scoped {
                        this.state = 81
                        match(NL)
                    }
                }

                2 -> {
                    enterOuterAlt(_localctx, 2)
                    scoped {
                        scoped {
                            this.state = 85
                            errorHandler.sync(this);
                            _la = _input!!.LA(1)
                            while (_la == NL) {
                                scoped {
                                    scoped {
                                        this.state = 82
                                        match(NL)
                                    }
                                }
                                this.state = 87
                                errorHandler.sync(this)
                                _la = _input!!.LA(1)
                            }
                            this.state = 88
                            match(SEMICOLON)
                        }
                    }
                }

                3 -> {
                    enterOuterAlt(_localctx, 3)
                    scoped {
                        this.state = 89
                        match(EOF)
                    }
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