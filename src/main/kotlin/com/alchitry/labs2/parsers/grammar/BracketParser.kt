// Generated from Bracket.g4 by ANTLR 4.13.1
package com.alchitry.labs2.parsers.grammar

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
public open class BracketParser(input: TokenStream) : Parser(input) {
    private companion object {
        init {
            RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.runtimeVersion)
        }

        private const val SERIALIZED_ATN: String =
            "\u0004\u0001\u000b\u0040\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0001\u0000\u0005\u0000\u000e\u0008\u0000\u000a\u0000\u000c\u0000\u0011\u0009\u0000\u0001\u0000\u0003\u0000\u0014\u0008\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u001a\u0008\u0001\u0001\u0002\u0001\u0002\u0005\u0002\u001e\u0008\u0002\u000a\u0002\u000c\u0002\u0021\u0009\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0005\u0003\u0027\u0008\u0003\u000a\u0003\u000c\u0003\u002a\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0005\u0004\u0030\u0008\u0004\u000a\u0004\u000c\u0004\u0033\u0009\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0005\u0005\u0039\u0008\u0005\u000a\u0005\u000c\u0005\u003c\u0009\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u003a\u0000\u0006\u0000\u0002\u0004\u0006\u0008\u000a\u0000\u0000\u0042\u0000\u000f\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004\u001b\u0001\u0000\u0000\u0000\u0006\u0024\u0001\u0000\u0000\u0000\u0008\u002d\u0001\u0000\u0000\u0000\u000a\u0036\u0001\u0000\u0000\u0000\u000c\u000e\u0003\u0002\u0001\u0000\u000d\u000c\u0001\u0000\u0000\u0000\u000e\u0011\u0001\u0000\u0000\u0000\u000f\u000d\u0001\u0000\u0000\u0000\u000f\u0010\u0001\u0000\u0000\u0000\u0010\u0013\u0001\u0000\u0000\u0000\u0011\u000f\u0001\u0000\u0000\u0000\u0012\u0014\u0005\u0000\u0000\u0001\u0013\u0012\u0001\u0000\u0000\u0000\u0013\u0014\u0001\u0000\u0000\u0000\u0014\u0001\u0001\u0000\u0000\u0000\u0015\u001a\u0003\u0004\u0002\u0000\u0016\u001a\u0003\u0006\u0003\u0000\u0017\u001a\u0003\u0008\u0004\u0000\u0018\u001a\u0003\u000a\u0005\u0000\u0019\u0015\u0001\u0000\u0000\u0000\u0019\u0016\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u0019\u0018\u0001\u0000\u0000\u0000\u001a\u0003\u0001\u0000\u0000\u0000\u001b\u001f\u0005\u0001\u0000\u0000\u001c\u001e\u0003\u0002\u0001\u0000\u001d\u001c\u0001\u0000\u0000\u0000\u001e\u0021\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000\u001f\u0020\u0001\u0000\u0000\u0000\u0020\u0022\u0001\u0000\u0000\u0000\u0021\u001f\u0001\u0000\u0000\u0000\u0022\u0023\u0005\u0002\u0000\u0000\u0023\u0005\u0001\u0000\u0000\u0000\u0024\u0028\u0005\u0003\u0000\u0000\u0025\u0027\u0003\u0002\u0001\u0000\u0026\u0025\u0001\u0000\u0000\u0000\u0027\u002a\u0001\u0000\u0000\u0000\u0028\u0026\u0001\u0000\u0000\u0000\u0028\u0029\u0001\u0000\u0000\u0000\u0029\u002b\u0001\u0000\u0000\u0000\u002a\u0028\u0001\u0000\u0000\u0000\u002b\u002c\u0005\u0004\u0000\u0000\u002c\u0007\u0001\u0000\u0000\u0000\u002d\u0031\u0005\u0005\u0000\u0000\u002e\u0030\u0003\u0002\u0001\u0000\u002f\u002e\u0001\u0000\u0000\u0000\u0030\u0033\u0001\u0000\u0000\u0000\u0031\u002f\u0001\u0000\u0000\u0000\u0031\u0032\u0001\u0000\u0000\u0000\u0032\u0034\u0001\u0000\u0000\u0000\u0033\u0031\u0001\u0000\u0000\u0000\u0034\u0035\u0005\u0006\u0000\u0000\u0035\u0009\u0001\u0000\u0000\u0000\u0036\u003a\u0005\u0007\u0000\u0000\u0037\u0039\u0009\u0000\u0000\u0000\u0038\u0037\u0001\u0000\u0000\u0000\u0039\u003c\u0001\u0000\u0000\u0000\u003a\u003b\u0001\u0000\u0000\u0000\u003a\u0038\u0001\u0000\u0000\u0000\u003b\u003d\u0001\u0000\u0000\u0000\u003c\u003a\u0001\u0000\u0000\u0000\u003d\u003e\u0005\u0008\u0000\u0000\u003e\u000b\u0001\u0000\u0000\u0000\u0007\u000f\u0013\u0019\u001f\u0028\u0031\u003a"

        private val ATN = ATNDeserializer().deserialize(SERIALIZED_ATN.toCharArray())

        private val DECISION_TO_DFA = Array(ATN.numberOfDecisions) {
            DFA(ATN.getDecisionState(it)!!, it)
        }

        private val SHARED_CONTEXT_CACHE = PredictionContextCache()
        private val RULE_NAMES: Array<String> = arrayOf(
            "source", "block", "parenBlock", "squareBlock", "curlyBlock",
            "commentBlock"
        )

        private val LITERAL_NAMES: Array<String?> = arrayOf(
            null, "'('", "')'", "'['", "']'", "'{'", "'}'", "'/*'", "'*/'"
        )

        private val SYMBOLIC_NAMES: Array<String?> = arrayOf(
            null, null, null, null, null, null, null, null, null, "COMMENT",
            "WS", "JUNK"
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
        public const val COMMENT: Int = 9
        public const val WS: Int = 10
        public const val JUNK: Int = 11
    }

    public object Rules {
        public const val Source: Int = 0
        public const val Block: Int = 1
        public const val ParenBlock: Int = 2
        public const val SquareBlock: Int = 3
        public const val CurlyBlock: Int = 4
        public const val CommentBlock: Int = 5
    }

    override var interpreter: ParserATNSimulator =
        @Suppress("LeakingThis")
        ParserATNSimulator(this, ATN, DECISION_TO_DFA, SHARED_CONTEXT_CACHE)

    override val grammarFileName: String =
        "Bracket.g4"

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

        public fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        public fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)
        public fun EOF(): TerminalNode? = getToken(Tokens.EOF, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): SourceContext {
            return SourceContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.enterSource(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.enterSource(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.exitSource(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
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
            this.state = 15
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                this.state = 12
                block()

                this.state = 17
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 19
            errorHandler.sync(this)

            when (interpreter.adaptivePredict(_input, 1, context)) {
                1 -> {
                    this.state = 18
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

    public open class BlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Block

        public fun parenBlock(): ParenBlockContext? = getRuleContext(ParenBlockContext::class, 0)
        public fun squareBlock(): SquareBlockContext? = getRuleContext(SquareBlockContext::class, 0)
        public fun curlyBlock(): CurlyBlockContext? = getRuleContext(CurlyBlockContext::class, 0)
        public fun commentBlock(): CommentBlockContext? = getRuleContext(CommentBlockContext::class, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): BlockContext {
            return BlockContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.enterBlock(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.enterBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.exitBlock(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.exitBlock(this)
            }
        }
    }


    public fun block(): BlockContext {
        var _localctx = BlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 2, Rules.Block)

        try {
            this.state = 25
            errorHandler.sync(this)

            when (_input.LA(1)) {
                Tokens.T__0 -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 1)
                    this.state = 21
                    parenBlock()

                }

                Tokens.T__2 -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 2)
                    this.state = 22
                    squareBlock()

                }

                Tokens.T__4 -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 3)
                    this.state = 23
                    curlyBlock()

                }

                Tokens.T__6 -> /*LL1AltBlock*/ {
                    enterOuterAlt(_localctx, 4)
                    this.state = 24
                    commentBlock()

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

    public open class ParenBlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.ParenBlock

        public fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        public fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): ParenBlockContext {
            return ParenBlockContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.enterParenBlock(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.enterParenBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.exitParenBlock(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.exitParenBlock(this)
            }
        }
    }


    public fun parenBlock(): ParenBlockContext {
        var _localctx = ParenBlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 4, Rules.ParenBlock)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 27
            match(Tokens.T__0)

            this.state = 31
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                this.state = 28
                block()

                this.state = 33
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 34
            match(Tokens.T__1)

        } catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        } finally {
            exitRule()
        }

        return _localctx
    }

    public open class SquareBlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.SquareBlock

        public fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        public fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): SquareBlockContext {
            return SquareBlockContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.enterSquareBlock(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.enterSquareBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.exitSquareBlock(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.exitSquareBlock(this)
            }
        }
    }


    public fun squareBlock(): SquareBlockContext {
        var _localctx = SquareBlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 6, Rules.SquareBlock)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 36
            match(Tokens.T__2)

            this.state = 40
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                this.state = 37
                block()

                this.state = 42
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 43
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

    public open class CurlyBlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.CurlyBlock

        public fun block(): List<BlockContext> = getRuleContexts(BlockContext::class)
        public fun block(i: Int): BlockContext? = getRuleContext(BlockContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): CurlyBlockContext {
            return CurlyBlockContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.enterCurlyBlock(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.enterCurlyBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.exitCurlyBlock(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.exitCurlyBlock(this)
            }
        }
    }


    public fun curlyBlock(): CurlyBlockContext {
        var _localctx = CurlyBlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 8, Rules.CurlyBlock)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 45
            match(Tokens.T__4)

            this.state = 49
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 170L) != 0L)) {
                this.state = 46
                block()

                this.state = 51
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 52
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

    public open class CommentBlockContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.CommentBlock


        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        public constructor() : super()

        override fun deepCopy(): CommentBlockContext {
            return CommentBlockContext().also { it.deepCopyFrom(this) }
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.enterCommentBlock(this)
            }
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.enterCommentBlock(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is BracketListener) {
                listener.exitCommentBlock(this)
            }
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendBracketListener) {
                listener.exitCommentBlock(this)
            }
        }
    }


    public fun commentBlock(): CommentBlockContext {
        var _localctx = CommentBlockContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 10, Rules.CommentBlock)

        try {
            var _alt: Int
            enterOuterAlt(_localctx, 1)
            this.state = 54
            match(Tokens.T__6)

            this.state = 58
            errorHandler.sync(this)
            _alt = interpreter.adaptivePredict(_input, 6, context)

            while (_alt != 1 && _alt != INVALID_ALT_NUMBER) {
                if (_alt == 1 + 1) {
                    this.state = 55
                    matchWildcard()

                }

                this.state = 60
                errorHandler.sync(this)
                _alt = interpreter.adaptivePredict(_input, 6, context)
            }
            this.state = 61
            match(Tokens.T__7)

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
