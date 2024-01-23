// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar;

import com.alchitry.kotlinmultiplatform.asCharArray
import com.alchitry.kotlinmultiplatform.scoped
import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.atn.ATN
import org.antlr.v4.kotlinruntime.atn.ATN.Companion.INVALID_ALT_NUMBER
import org.antlr.v4.kotlinruntime.atn.ATNDeserializer
import org.antlr.v4.kotlinruntime.atn.ParserATNSimulator
import org.antlr.v4.kotlinruntime.atn.PredictionContextCache
import org.antlr.v4.kotlinruntime.dfa.DFA
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

class BracketParser(input: TokenStream) : Parser(input) {
    // TODO verify version of runtime is compatible

    override val grammarFileName: String
        get() = "Bracket.g4"

    override val tokenNames: Array<String?> = BracketParser.tokenNames
    override val ruleNames: Array<String> = BracketParser.ruleNames
    override val atn: ATN = BracketParser.ATN
    override val vocabulary: Vocabulary = BracketParser.VOCABULARY

    enum class Tokens(val id: Int) {
        EOF(-1),
        T__0(1),
        T__1(2),
        T__2(3),
        T__3(4),
        T__4(5),
        T__5(6),
        T__6(7),
        T__7(8),
        COMMENT(9),
        WS(10),
        JUNK(11)
    }

    enum class Rules(val id: Int) {
        RULE_source(0),
        RULE_block(1),
        RULE_parenBlock(2),
        RULE_squareBlock(3),
        RULE_curlyBlock(4),
        RULE_commentBlock(5)
    }

    companion object {
        protected val decisionToDFA: Array<DFA>
        protected val sharedContextCache = PredictionContextCache()

        val ruleNames = arrayOf(
            "source", "block", "parenBlock", "squareBlock",
            "curlyBlock", "commentBlock"
        )

        private val LITERAL_NAMES: List<String?> = listOf(
            null, "'('", "')'",
            "'['", "']'",
            "'{'", "'}'",
            "'/*'", "'*/'"
        )
        private val SYMBOLIC_NAMES: List<String?> = listOf(
            null, null, null,
            null, null, null,
            null, null, null,
            "COMMENT", "WS",
            "JUNK"
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
            "\u0004\u0001\u000b\u0040\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0001\u0000\u0005\u0000\u000e\u0008\u0000\u000a\u0000\u000c\u0000\u0011\u0009\u0000\u0001\u0000\u0003\u0000\u0014\u0008\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u001a\u0008\u0001\u0001\u0002\u0001\u0002\u0005\u0002\u001e\u0008\u0002\u000a\u0002\u000c\u0002\u0021\u0009\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0005\u0003\u0027\u0008\u0003\u000a\u0003\u000c\u0003\u002a\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0005\u0004\u0030\u0008\u0004\u000a\u0004\u000c\u0004\u0033\u0009\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0005\u0005\u0039\u0008\u0005\u000a\u0005\u000c\u0005\u003c\u0009\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u003a\u0000\u0006\u0000\u0002\u0004\u0006\u0008\u000a\u0000\u0000\u0042\u0000\u000f\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004\u001b\u0001\u0000\u0000\u0000\u0006\u0024\u0001\u0000\u0000\u0000\u0008\u002d\u0001\u0000\u0000\u0000\u000a\u0036\u0001\u0000\u0000\u0000\u000c\u000e\u0003\u0002\u0001\u0000\u000d\u000c\u0001\u0000\u0000\u0000\u000e\u0011\u0001\u0000\u0000\u0000\u000f\u000d\u0001\u0000\u0000\u0000\u000f\u0010\u0001\u0000\u0000\u0000\u0010\u0013\u0001\u0000\u0000\u0000\u0011\u000f\u0001\u0000\u0000\u0000\u0012\u0014\u0005\u0000\u0000\u0001\u0013\u0012\u0001\u0000\u0000\u0000\u0013\u0014\u0001\u0000\u0000\u0000\u0014\u0001\u0001\u0000\u0000\u0000\u0015\u001a\u0003\u0004\u0002\u0000\u0016\u001a\u0003\u0006\u0003\u0000\u0017\u001a\u0003\u0008\u0004\u0000\u0018\u001a\u0003\u000a\u0005\u0000\u0019\u0015\u0001\u0000\u0000\u0000\u0019\u0016\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u0018\u0001\u0000\u0000\u0000\u001a\u0003\u0001\u0000\u0000\u0000\u001b\u001f\u0005\u0001\u0000\u0000\u001c\u001e\u0003\u0002\u0001\u0000\u001d\u001c\u0001\u0000\u0000\u0000\u001e\u0021\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000\u001f\u0020\u0001\u0000\u0000\u0000\u0020\u0022\u0001\u0000\u0000\u0000\u0021\u001f\u0001\u0000\u0000\u0000\u0022\u0023\u0005\u0002\u0000\u0000\u0023\u0005\u0001\u0000\u0000\u0000\u0024\u0028\u0005\u0003\u0000\u0000\u0025\u0027\u0003\u0002\u0001\u0000\u0026\u0025\u0001\u0000\u0000\u0000\u0027\u002a\u0001\u0000\u0000\u0000\u0028\u0026\u0001\u0000\u0000\u0000\u0028\u0029\u0001\u0000\u0000\u0000\u0029\u002b\u0001\u0000\u0000\u0000\u002a\u0028\u0001\u0000\u0000\u0000\u002b\u002c\u0005\u0004\u0000\u0000\u002c\u0007\u0001\u0000\u0000\u0000\u002d\u0031\u0005\u0005\u0000\u0000\u002e\u0030\u0003\u0002\u0001\u0000\u002f\u002e\u0001\u0000\u0000\u0000\u0030\u0033\u0001\u0000\u0000\u0000\u0031\u002f\u0001\u0000\u0000\u0000\u0031\u0032\u0001\u0000\u0000\u0000\u0032\u0034\u0001\u0000\u0000\u0000\u0033\u0031\u0001\u0000\u0000\u0000\u0034\u0035\u0005\u0006\u0000\u0000\u0035\u0009\u0001\u0000\u0000\u0000\u0036\u003a\u0005\u0007\u0000\u0000\u0037\u0039\u0009\u0000\u0000\u0000\u0038\u0037\u0001\u0000\u0000\u0000\u0039\u003c\u0001\u0000\u0000\u0000\u003a\u003b\u0001\u0000\u0000\u0000\u003a\u0038\u0001\u0000\u0000\u0000\u003b\u003d\u0001\u0000\u0000\u0000\u003c\u003a\u0001\u0000\u0000\u0000\u003d\u003e\u0005\u0008\u0000\u0000\u003e\u000b\u0001\u0000\u0000\u0000\u0007\u000f\u0013\u0019\u001f\u0028\u0031\u003a"

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
    private val T__5 = Tokens.T__5.id
    private val T__6 = Tokens.T__6.id
    private val T__7 = Tokens.T__7.id
    private val COMMENT = Tokens.COMMENT.id
    private val WS = Tokens.WS.id
    private val JUNK = Tokens.JUNK.id

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

        fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)
        fun EOF(): TerminalNode? = getToken(Tokens.EOF.id, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.enterSource(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.enterSource(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.exitSource(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.exitSource(this)
        }
    }

    fun source(): SourceContext {
        var _localctx: SourceContext = SourceContext(context, state)
        enterRule(_localctx, 0, Rules.RULE_source.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 15
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                    scoped {
                        scoped {
                            this.state = 12
                            block()
                        }
                    }
                    this.state = 17
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 19
                errorHandler.sync(this)
                when (interpreter!!.adaptivePredict(_input!!, 1, context)) {
                    1 -> scoped {
                        this.state = 18
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

    open class BlockContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_block.id
            set(value) {
                throw RuntimeException()
            }

        fun parenBlock(): ParenBlockContext? = getRuleContext(ParenBlockContext::class, 0)
        fun squareBlock(): SquareBlockContext? = getRuleContext(SquareBlockContext::class, 0)
        fun curlyBlock(): CurlyBlockContext? = getRuleContext(CurlyBlockContext::class, 0)
        fun commentBlock(): CommentBlockContext? = getRuleContext(CommentBlockContext::class, 0)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.enterBlock(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.enterBlock(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.exitBlock(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.exitBlock(this)
        }
    }

    fun block(): BlockContext {
        var _localctx: BlockContext = BlockContext(context, state)
        enterRule(_localctx, 2, Rules.RULE_block.id)
        try {
            this.state = 25
            errorHandler.sync(this)
            when (_input!!.LA(1)) {
                T__0 ->  /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 1)
                    scoped {
                        this.state = 21
                        parenBlock()
                    }
                }

                T__2 ->  /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 2)
                    scoped {
                        this.state = 22
                        squareBlock()
                    }
                }

                T__4 ->  /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 3)
                    scoped {
                        this.state = 23
                        curlyBlock()
                    }
                }

                T__6 ->  /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 4)
                    scoped {
                        this.state = 24
                        commentBlock()
                    }
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

    open class ParenBlockContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_parenBlock.id
            set(value) {
                throw RuntimeException()
            }

        fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.enterParenBlock(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.enterParenBlock(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.exitParenBlock(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.exitParenBlock(this)
        }
    }

    fun parenBlock(): ParenBlockContext {
        var _localctx: ParenBlockContext = ParenBlockContext(context, state)
        enterRule(_localctx, 4, Rules.RULE_parenBlock.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 27
                match(T__0)
                this.state = 31
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                    scoped {
                        scoped {
                            this.state = 28
                            block()
                        }
                    }
                    this.state = 33
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 34
                match(T__1)
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

    open class SquareBlockContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_squareBlock.id
            set(value) {
                throw RuntimeException()
            }

        fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.enterSquareBlock(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.enterSquareBlock(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.exitSquareBlock(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.exitSquareBlock(this)
        }
    }

    fun squareBlock(): SquareBlockContext {
        var _localctx: SquareBlockContext = SquareBlockContext(context, state)
        enterRule(_localctx, 6, Rules.RULE_squareBlock.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 36
                match(T__2)
                this.state = 40
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                    scoped {
                        scoped {
                            this.state = 37
                            block()
                        }
                    }
                    this.state = 42
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 43
                match(T__3)
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

    open class CurlyBlockContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_curlyBlock.id
            set(value) {
                throw RuntimeException()
            }

        fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.enterCurlyBlock(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.enterCurlyBlock(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.exitCurlyBlock(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.exitCurlyBlock(this)
        }
    }

    fun curlyBlock(): CurlyBlockContext {
        var _localctx: CurlyBlockContext = CurlyBlockContext(context, state)
        enterRule(_localctx, 8, Rules.RULE_curlyBlock.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 45
                match(T__4)
                this.state = 49
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                    scoped {
                        scoped {
                            this.state = 46
                            block()
                        }
                    }
                    this.state = 51
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 52
                match(T__5)
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

    open class CommentBlockContext : ParserRuleContext {
        override var ruleIndex: Int
            get() = Rules.RULE_commentBlock.id
            set(value) {
                throw RuntimeException()
            }

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.enterCommentBlock(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.enterCommentBlock(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) listener.exitCommentBlock(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) listener.exitCommentBlock(this)
        }
    }

    fun commentBlock(): CommentBlockContext {
        var _localctx: CommentBlockContext = CommentBlockContext(context, state)
        enterRule(_localctx, 10, Rules.RULE_commentBlock.id)
        try {
            var _alt: Int
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 54
                match(T__6)
                this.state = 58
                errorHandler.sync(this)
                _alt = interpreter!!.adaptivePredict(_input!!, 6, context)
                while (_alt != 1 && _alt != INVALID_ALT_NUMBER) {
                    if (_alt == 1 + 1) {
                        scoped {
                            scoped {
                                this.state = 55
                                matchWildcard()
                            }
                        }
                    }
                    this.state = 60
                    errorHandler.sync(this)
                    _alt = interpreter!!.adaptivePredict(_input!!, 6, context)
                }
                this.state = 61
                match(T__7)
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