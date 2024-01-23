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

class IndentDetectorParser(input: TokenStream) : Parser(input) {
    // TODO verify version of runtime is compatible

    override val grammarFileName: String
        get() = "IndentDetector.g4"

    override val tokenNames: Array<String?> = IndentDetectorParser.tokenNames
    override val ruleNames: Array<String> = IndentDetectorParser.ruleNames
    override val atn: ATN = IndentDetectorParser.ATN
    override val vocabulary: Vocabulary = IndentDetectorParser.VOCABULARY

    enum class Tokens(val id: Int) {
        EOF(-1),
        OPEN_BRACKET(1),
        CLOSED_BRACKET(2),
        BLOCK_COMMENT(3),
        COMMENT(4),
        WS(5),
        JUNK(6)
    }

    enum class Rules(val id: Int) {
        RULE_source(0)
    }

    companion object {
        protected val decisionToDFA: Array<DFA>
        protected val sharedContextCache = PredictionContextCache()

        val ruleNames = arrayOf("source")

        private val LITERAL_NAMES: List<String?> = listOf()
        private val SYMBOLIC_NAMES: List<String?> = listOf(
            null, "OPEN_BRACKET",
            "CLOSED_BRACKET",
            "BLOCK_COMMENT",
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
            "\u0004\u0001\u0006\u000c\u0002\u0000\u0007\u0000\u0001\u0000\u0005\u0000\u0004\u0008\u0000\u000a\u0000\u000c\u0000\u0007\u0009\u0000\u0001\u0000\u0003\u0000\u000a\u0008\u0000\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0001\u0001\u0000\u0001\u0002\u000c\u0000\u0005\u0001\u0000\u0000\u0000\u0002\u0004\u0007\u0000\u0000\u0000\u0003\u0002\u0001\u0000\u0000\u0000\u0004\u0007\u0001\u0000\u0000\u0000\u0005\u0003\u0001\u0000\u0000\u0000\u0005\u0006\u0001\u0000\u0000\u0000\u0006\u0009\u0001\u0000\u0000\u0000\u0007\u0005\u0001\u0000\u0000\u0000\u0008\u000a\u0005\u0000\u0000\u0001\u0009\u0008\u0001\u0000\u0000\u0000\u0009\u000a\u0001\u0000\u0000\u0000\u000a\u0001\u0001\u0000\u0000\u0000\u0002\u0005\u0009"

        val ATN = ATNDeserializer().deserialize(serializedATN.asCharArray())
        init {
            decisionToDFA = Array<DFA>(ATN.numberOfDecisions, {
                DFA(ATN.getDecisionState(it)!!, it)
            })


        }
    }

    private val OPEN_BRACKET = Tokens.OPEN_BRACKET.id
    private val CLOSED_BRACKET = Tokens.CLOSED_BRACKET.id
    private val BLOCK_COMMENT = Tokens.BLOCK_COMMENT.id
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

        fun EOF(): TerminalNode? = getToken(Tokens.EOF.id, 0)
        fun OPEN_BRACKET(): List<TerminalNode> = getTokens(Tokens.OPEN_BRACKET.id)
        fun OPEN_BRACKET(i: Int): TerminalNode = getToken(Tokens.OPEN_BRACKET.id, i) as TerminalNode
        fun CLOSED_BRACKET(): List<TerminalNode> = getTokens(Tokens.CLOSED_BRACKET.id)
        fun CLOSED_BRACKET(i: Int): TerminalNode = getToken(Tokens.CLOSED_BRACKET.id, i) as TerminalNode

        constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is IndentDetectorListener) listener.enterSource(this)
        }

        override suspend fun enterRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendIndentDetectorListener) listener.enterSource(this)
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is IndentDetectorListener) listener.exitSource(this)
        }

        override suspend fun exitRule(listener: SuspendParseTreeListener) {
            if (listener is SuspendIndentDetectorListener) listener.exitSource(this)
        }
    }

    fun source(): SourceContext {
        var _localctx: SourceContext = SourceContext(context, state)
        enterRule(_localctx, 0, Rules.RULE_source.id)
        var _la: Int
        try {
            enterOuterAlt(_localctx, 1)
            scoped {
                this.state = 5
                errorHandler.sync(this);
                _la = _input!!.LA(1)
                while (_la == OPEN_BRACKET || _la == CLOSED_BRACKET) {
                    scoped {
                        scoped {
                            this.state = 2
                            _la = _input!!.LA(1)
                            if (!(_la == OPEN_BRACKET || _la == CLOSED_BRACKET)) {
                                errorHandler.recoverInline(this)
                            } else {
                                if (_input!!.LA(1) == Tokens.EOF.id) isMatchedEOF = true
                                errorHandler.reportMatch(this)
                                consume()
                            }
                        }
                    }
                    this.state = 7
                    errorHandler.sync(this)
                    _la = _input!!.LA(1)
                }
                this.state = 9
                errorHandler.sync(this)
                when (interpreter!!.adaptivePredict(_input!!, 1, context)) {
                    1 -> scoped {
                        this.state = 8
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