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
            "CLOSED_BRACKET", "BLOCK_COMMENT",
            "COMMENT", "WS", "JUNK"
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
            "\u0004\u0000\u0006\u0035\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u0016\u0008\u0002\u000a\u0002\u000c\u0002\u0019\u0009\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0024\u0008\u0003\u000a\u0003\u000c\u0003\u0027\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0004\u0004\u002c\u0008\u0004\u000b\u0004\u000c\u0004\u002d\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0017\u0000\u0006\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\u0009\u0005\u000b\u0006\u0001\u0000\u0005\u0003\u0000\u0028\u0028\u005b\u005b\u007b\u007b\u0003\u0000\u0029\u0029\u005d\u005d\u007d\u007d\u0002\u0000\u000a\u000a\u000d\u000d\u0002\u0000\u0009\u000a\u0020\u0020\u0005\u0000\u0028\u0029\u005b\u005b\u005d\u005d\u007b\u007b\u007d\u007d\u0037\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\u0009\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0001\u000d\u0001\u0000\u0000\u0000\u0003\u000f\u0001\u0000\u0000\u0000\u0005\u0011\u0001\u0000\u0000\u0000\u0007\u001f\u0001\u0000\u0000\u0000\u0009\u002b\u0001\u0000\u0000\u0000\u000b\u0031\u0001\u0000\u0000\u0000\u000d\u000e\u0007\u0000\u0000\u0000\u000e\u0002\u0001\u0000\u0000\u0000\u000f\u0010\u0007\u0001\u0000\u0000\u0010\u0004\u0001\u0000\u0000\u0000\u0011\u0012\u0005\u002f\u0000\u0000\u0012\u0013\u0005\u002a\u0000\u0000\u0013\u0017\u0001\u0000\u0000\u0000\u0014\u0016\u0009\u0000\u0000\u0000\u0015\u0014\u0001\u0000\u0000\u0000\u0016\u0019\u0001\u0000\u0000\u0000\u0017\u0018\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0018\u001a\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000\u001a\u001b\u0005\u002a\u0000\u0000\u001b\u001c\u0005\u002f\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\u001e\u0006\u0002\u0000\u0000\u001e\u0006\u0001\u0000\u0000\u0000\u001f\u0020\u0005\u002f\u0000\u0000\u0020\u0021\u0005\u002f\u0000\u0000\u0021\u0025\u0001\u0000\u0000\u0000\u0022\u0024\u0008\u0002\u0000\u0000\u0023\u0022\u0001\u0000\u0000\u0000\u0024\u0027\u0001\u0000\u0000\u0000\u0025\u0023\u0001\u0000\u0000\u0000\u0025\u0026\u0001\u0000\u0000\u0000\u0026\u0028\u0001\u0000\u0000\u0000\u0027\u0025\u0001\u0000\u0000\u0000\u0028\u0029\u0006\u0003\u0000\u0000\u0029\u0008\u0001\u0000\u0000\u0000\u002a\u002c\u0007\u0003\u0000\u0000\u002b\u002a\u0001\u0000\u0000\u0000\u002c\u002d\u0001\u0000\u0000\u0000\u002d\u002b\u0001\u0000\u0000\u0000\u002d\u002e\u0001\u0000\u0000\u0000\u002e\u002f\u0001\u0000\u0000\u0000\u002f\u0030\u0006\u0004\u0001\u0000\u0030\u000a\u0001\u0000\u0000\u0000\u0031\u0032\u0008\u0004\u0000\u0000\u0032\u0033\u0001\u0000\u0000\u0000\u0033\u0034\u0006\u0005\u0000\u0000\u0034\u000c\u0001\u0000\u0000\u0000\u0004\u0000\u0017\u0025\u002d\u0002\u0000\u0001\u0000\u0006\u0000\u0000"

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
        BLOCK_COMMENT(3),
        COMMENT(4),
        WS(5),
        JUNK(6)
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
        BLOCK_COMMENT,
        COMMENT,
        WS,
        JUNK
    }


    init {
        this.interpreter = LexerATNSimulator(this, ATN, decisionToDFA as Array<DFA?>, sharedContextCache)
    }

}