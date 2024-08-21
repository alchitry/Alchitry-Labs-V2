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

class BracketLexer(val _input: CharStream) : Lexer(_input) {
	// TODO Verify the runtime version is correct

    override val ruleNames: Array<String> = Rules.values().map { it.name }.toTypedArray()

    override val grammarFileName: String = "Bracket.g4"

    override val atn: ATN = BracketLexer.ATN

    override val vocabulary: Vocabulary = BracketLexer.VOCABULARY

	companion object {
        val decisionToDFA: Array<DFA>
		val sharedContextCache = PredictionContextCache()

        private val LITERAL_NAMES: List<String?> = listOf(
            null, "'('", "')'",
            "'['", "']'", "'{'",
            "'}'", "'/*'", "'*/'"
        )
        private val SYMBOLIC_NAMES: List<String?> = listOf(
            null, null, null, null,
            null, null, null, null,
            null, "COMMENT", "WS",
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
            "\u0004\u0000\u000b\u003f\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\u0008\u0007\u0008\u0002\u0009\u0007\u0009\u0002\u000a\u0007\u000a\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0008\u0001\u0008\u0001\u0008\u0001\u0008\u0005\u0008\u002e\u0008\u0008\u000a\u0008\u000c\u0008\u0031\u0009\u0008\u0001\u0008\u0001\u0008\u0001\u0009\u0004\u0009\u0036\u0008\u0009\u000b\u0009\u000c\u0009\u0037\u0001\u0009\u0001\u0009\u0001\u000a\u0001\u000a\u0001\u000a\u0001\u000a\u0000\u0000\u000b\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\u0009\u0005\u000b\u0006\u000d\u0007\u000f\u0008\u0011\u0009\u0013\u000a\u0015\u000b\u0001\u0000\u0003\u0002\u0000\u000a\u000a\u000d\u000d\u0002\u0000\u0009\u000a\u0020\u0020\u0005\u0000\u0028\u0029\u005b\u005b\u005d\u005d\u007b\u007b\u007d\u007d\u0040\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\u0009\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\u000d\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0001\u0017\u0001\u0000\u0000\u0000\u0003\u0019\u0001\u0000\u0000\u0000\u0005\u001b\u0001\u0000\u0000\u0000\u0007\u001d\u0001\u0000\u0000\u0000\u0009\u001f\u0001\u0000\u0000\u0000\u000b\u0021\u0001\u0000\u0000\u0000\u000d\u0023\u0001\u0000\u0000\u0000\u000f\u0026\u0001\u0000\u0000\u0000\u0011\u0029\u0001\u0000\u0000\u0000\u0013\u0035\u0001\u0000\u0000\u0000\u0015\u003b\u0001\u0000\u0000\u0000\u0017\u0018\u0005\u0028\u0000\u0000\u0018\u0002\u0001\u0000\u0000\u0000\u0019\u001a\u0005\u0029\u0000\u0000\u001a\u0004\u0001\u0000\u0000\u0000\u001b\u001c\u0005\u005b\u0000\u0000\u001c\u0006\u0001\u0000\u0000\u0000\u001d\u001e\u0005\u005d\u0000\u0000\u001e\u0008\u0001\u0000\u0000\u0000\u001f\u0020\u0005\u007b\u0000\u0000\u0020\u000a\u0001\u0000\u0000\u0000\u0021\u0022\u0005\u007d\u0000\u0000\u0022\u000c\u0001\u0000\u0000\u0000\u0023\u0024\u0005\u002f\u0000\u0000\u0024\u0025\u0005\u002a\u0000\u0000\u0025\u000e\u0001\u0000\u0000\u0000\u0026\u0027\u0005\u002a\u0000\u0000\u0027\u0028\u0005\u002f\u0000\u0000\u0028\u0010\u0001\u0000\u0000\u0000\u0029\u002a\u0005\u002f\u0000\u0000\u002a\u002b\u0005\u002f\u0000\u0000\u002b\u002f\u0001\u0000\u0000\u0000\u002c\u002e\u0008\u0000\u0000\u0000\u002d\u002c\u0001\u0000\u0000\u0000\u002e\u0031\u0001\u0000\u0000\u0000\u002f\u002d\u0001\u0000\u0000\u0000\u002f\u0030\u0001\u0000\u0000\u0000\u0030\u0032\u0001\u0000\u0000\u0000\u0031\u002f\u0001\u0000\u0000\u0000\u0032\u0033\u0006\u0008\u0000\u0000\u0033\u0012\u0001\u0000\u0000\u0000\u0034\u0036\u0007\u0001\u0000\u0000\u0035\u0034\u0001\u0000\u0000\u0000\u0036\u0037\u0001\u0000\u0000\u0000\u0037\u0035\u0001\u0000\u0000\u0000\u0037\u0038\u0001\u0000\u0000\u0000\u0038\u0039\u0001\u0000\u0000\u0000\u0039\u003a\u0006\u0009\u0001\u0000\u003a\u0014\u0001\u0000\u0000\u0000\u003b\u003c\u0008\u0002\u0000\u0000\u003c\u003d\u0001\u0000\u0000\u0000\u003d\u003e\u0006\u000a\u0000\u0000\u003e\u0016\u0001\u0000\u0000\u0000\u0003\u0000\u002f\u0037\u0002\u0000\u0001\u0000\u0006\u0000\u0000"

		val ATN = ATNDeserializer().deserialize(serializedATN.asCharArray())
		init {
			decisionToDFA = Array<DFA>(ATN.numberOfDecisions, {
				DFA(ATN.getDecisionState(it)!!, it)
			})


		}
	}

    enum class Tokens(val id: Int) {
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

	enum class Channels(val id: Int) {
		DEFAULT_TOKEN_CHANNEL(0),
		HIDDEN(1),
	}

	override val channelNames = Channels.values().map(Channels::name).toTypedArray()

	enum class Modes(val id: Int) {
		DEFAULT_MODE(0),
	}

	enum class Rules {
        T__0,
        T__1,
        T__2,
        T__3,
        T__4,
        T__5,
        T__6,
        T__7,
        COMMENT,
        WS,
        JUNK
	}


	init {
		this.interpreter = LexerATNSimulator(this, ATN, decisionToDFA as Array<DFA?>, sharedContextCache)
	}

}