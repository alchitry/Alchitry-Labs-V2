// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import com.alchitry.kotlinmultiplatform.asCharArray
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.Lexer
import org.antlr.v4.kotlinruntime.Vocabulary
import org.antlr.v4.kotlinruntime.VocabularyImpl
import org.antlr.v4.kotlinruntime.atn.ATN
import org.antlr.v4.kotlinruntime.atn.ATNDeserializer
import org.antlr.v4.kotlinruntime.atn.LexerATNSimulator
import org.antlr.v4.kotlinruntime.atn.PredictionContextCache
import org.antlr.v4.kotlinruntime.dfa.DFA

class IndentDetectorLexer(val _input: CharStream) : Lexer(_input) {
    // TODO Verify the runtime version is correct

    override val ruleNames: Array<String> = Rules.values().map { it.name }.toTypedArray()

    override val grammarFileName: String = "IndentDetector.g4"

    override val atn: ATN = IndentDetectorLexer.ATN

    override val vocabulary: Vocabulary = IndentDetectorLexer.VOCABULARY

    companion object {
        val decisionToDFA: Array<DFA>
        val sharedContextCache = PredictionContextCache()

        private val LITERAL_NAMES: List<String?> = listOf()
        private val SYMBOLIC_NAMES: List<String?> = listOf(
            null, "OPEN_BRACKET",
            "CLOSED_BRACKET", "JUNK",
            "BLOCK_COMMENT", "COMMENT",
            "WS"
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
            "\u0004\u0000\u0006\u0038\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0004\u0002\u0013\u0008\u0002\u000b\u0002\u000c\u0002\u0014\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u001d\u0008\u0003\u000a\u0003\u000c\u0003\u0020\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u002b\u0008\u0004\u000a\u0004\u000c\u0004\u002e\u0009\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0004\u0005\u0033\u0008\u0005\u000b\u0005\u000c\u0005\u0034\u0001\u0005\u0001\u0005\u0001\u001e\u0000\u0006\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\u0009\u0005\u000b\u0006\u0001\u0000\u0005\u0003\u0000\u0028\u0028\u005b\u005b\u007b\u007b\u0003\u0000\u0029\u0029\u005d\u005d\u007d\u007d\u0005\u0000\u0028\u0029\u005b\u005b\u005d\u005d\u007b\u007b\u007d\u007d\u0002\u0000\u000a\u000a\u000d\u000d\u0002\u0000\u0009\u000a\u0020\u0020\u003b\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\u0009\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0001\u000d\u0001\u0000\u0000\u0000\u0003\u000f\u0001\u0000\u0000\u0000\u0005\u0012\u0001\u0000\u0000\u0000\u0007\u0018\u0001\u0000\u0000\u0000\u0009\u0026\u0001\u0000\u0000\u0000\u000b\u0032\u0001\u0000\u0000\u0000\u000d\u000e\u0007\u0000\u0000\u0000\u000e\u0002\u0001\u0000\u0000\u0000\u000f\u0010\u0007\u0001\u0000\u0000\u0010\u0004\u0001\u0000\u0000\u0000\u0011\u0013\u0008\u0002\u0000\u0000\u0012\u0011\u0001\u0000\u0000\u0000\u0013\u0014\u0001\u0000\u0000\u0000\u0014\u0012\u0001\u0000\u0000\u0000\u0014\u0015\u0001\u0000\u0000\u0000\u0015\u0016\u0001\u0000\u0000\u0000\u0016\u0017\u0006\u0002\u0000\u0000\u0017\u0006\u0001\u0000\u0000\u0000\u0018\u0019\u0005\u002f\u0000\u0000\u0019\u001a\u0005\u002a\u0000\u0000\u001a\u001e\u0001\u0000\u0000\u0000\u001b\u001d\u0009\u0000\u0000\u0000\u001c\u001b\u0001\u0000\u0000\u0000\u001d\u0020\u0001\u0000\u0000\u0000\u001e\u001f\u0001\u0000\u0000\u0000\u001e\u001c\u0001\u0000\u0000\u0000\u001f\u0021\u0001\u0000\u0000\u0000\u0020\u001e\u0001\u0000\u0000\u0000\u0021\u0022\u0005\u002a\u0000\u0000\u0022\u0023\u0005\u002f\u0000\u0000\u0023\u0024\u0001\u0000\u0000\u0000\u0024\u0025\u0006\u0003\u0000\u0000\u0025\u0008\u0001\u0000\u0000\u0000\u0026\u0027\u0005\u002f\u0000\u0000\u0027\u0028\u0005\u002f\u0000\u0000\u0028\u002c\u0001\u0000\u0000\u0000\u0029\u002b\u0008\u0003\u0000\u0000\u002a\u0029\u0001\u0000\u0000\u0000\u002b\u002e\u0001\u0000\u0000\u0000\u002c\u002a\u0001\u0000\u0000\u0000\u002c\u002d\u0001\u0000\u0000\u0000\u002d\u002f\u0001\u0000\u0000\u0000\u002e\u002c\u0001\u0000\u0000\u0000\u002f\u0030\u0006\u0004\u0000\u0000\u0030\u000a\u0001\u0000\u0000\u0000\u0031\u0033\u0007\u0004\u0000\u0000\u0032\u0031\u0001\u0000\u0000\u0000\u0033\u0034\u0001\u0000\u0000\u0000\u0034\u0032\u0001\u0000\u0000\u0000\u0034\u0035\u0001\u0000\u0000\u0000\u0035\u0036\u0001\u0000\u0000\u0000\u0036\u0037\u0006\u0005\u0001\u0000\u0037\u000c\u0001\u0000\u0000\u0000\u0005\u0000\u0014\u001e\u002c\u0034\u0002\u0000\u0001\u0000\u0006\u0000\u0000"

        val ATN = ATNDeserializer().deserialize(serializedATN.asCharArray())

        init {
            decisionToDFA = Array<DFA>(ATN.numberOfDecisions, {
                DFA(ATN.getDecisionState(it)!!, it)
            })


        }
    }

    enum class Tokens(val id: Int) {
        OPEN_BRACKET(1),
        CLOSED_BRACKET(2),
        JUNK(3),
        BLOCK_COMMENT(4),
        COMMENT(5),
        WS(6)
    }

    enum class Channels(val id: Int) {
        DEFAULT_TOKEN_CHANNEL(0),
        HIDDEN(1),
    }

    override val channelNames = Channels.values().map(Channels::name).toTypedArray()

    enum class Modes(val id: Int) {
        DEFAULT_MODE(0),
    }

    enum class Rules {
        OPEN_BRACKET,
        CLOSED_BRACKET,
        JUNK,
        BLOCK_COMMENT,
        COMMENT,
        WS
    }


    init {
        this.interpreter = LexerATNSimulator(this, ATN, decisionToDFA as Array<DFA?>, sharedContextCache)
    }

}