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
        T__5(6),
        T__6(7),
        T__7(8),
        T__8(9),
		SEMICOLON(10),
		NL(11),
		FREQ_UNIT(12),
		BASIC_NAME(13),
		REAL(14),
		INT(15),
		BLOCK_COMMENT(16),
		COMMENT(17),
		WS(18)
    }

    enum class Rules(val id: Int) {
        RULE_source(0),
        RULE_line(1),
        RULE_pin(2),
        RULE_attributeBlock(3),
        RULE_name(4),
        RULE_attribute(5),
        RULE_attributeValue(6),
        RULE_portName(7),
        RULE_pinName(8),
        RULE_frequency(9),
        RULE_arrayIndex(10),
        RULE_number(11),
        RULE_semi(12)
    }

	companion object {
		protected val decisionToDFA: Array<DFA>
		protected val sharedContextCache = PredictionContextCache()

		val ruleNames = arrayOf(
			"source", "line", "pin", "attributeBlock",
			"name", "attribute", "attributeValue", "portName",
			"pinName", "frequency", "arrayIndex", "number",
			"semi"
		)

		private val LITERAL_NAMES: List<String?> = listOf(
			null, "'pin'",
			"','", "'{'",
			"'}'", "'('",
			"')'", "'.'",
			"'['", "']'",
			"';'"
		)
		private val SYMBOLIC_NAMES: List<String?> = listOf(
			null, null, null,
			null, null, null,
			null, null, null,
			null, "SEMICOLON",
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
			"\u0004\u0001\u0012\u007b\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\u0008\u0007\u0008\u0002\u0009\u0007\u0009\u0002\u000a\u0007\u000a\u0002\u000b\u0007\u000b\u0002\u000c\u0007\u000c\u0001\u0000\u0005\u0000\u001c\u0008\u0000\u000a\u0000\u000c\u0000\u001f\u0009\u0000\u0001\u0000\u0003\u0000\u0022\u0008\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0027\u0008\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u002d\u0008\u0002\u000a\u0002\u000c\u0002\u0030\u0009\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0037\u0008\u0003\u000a\u0003\u000c\u0003\u003a\u0009\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u003e\u0008\u0003\u000a\u0003\u000c\u0003\u0041\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u004f\u0008\u0006\u0001\u0007\u0001\u0007\u0005\u0007\u0053\u0008\u0007\u000a\u0007\u000c\u0007\u0056\u0009\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u005b\u0008\u0007\u000a\u0007\u000c\u0007\u005e\u0009\u0007\u0005\u0007\u0060\u0008\u0007\u000a\u0007\u000c\u0007\u0063\u0009\u0007\u0001\u0008\u0001\u0008\u0001\u0009\u0001\u0009\u0001\u0009\u0001\u000a\u0001\u000a\u0001\u000a\u0001\u000a\u0001\u000b\u0001\u000b\u0001\u000c\u0001\u000c\u0005\u000c\u0072\u0008\u000c\u000a\u000c\u000c\u000c\u0075\u0009\u000c\u0001\u000c\u0001\u000c\u0003\u000c\u0079\u0008\u000c\u0001\u000c\u0000\u0000\u000d\u0000\u0002\u0004\u0006\u0008\u000a\u000c\u000e\u0010\u0012\u0014\u0016\u0018\u0000\u0002\u0001\u0000\u000c\u000d\u0001\u0000\u000e\u000f\u007c\u0000\u001d\u0001\u0000\u0000\u0000\u0002\u0026\u0001\u0000\u0000\u0000\u0004\u0028\u0001\u0000\u0000\u0000\u0006\u0033\u0001\u0000\u0000\u0000\u0008\u0044\u0001\u0000\u0000\u0000\u000a\u0046\u0001\u0000\u0000\u0000\u000c\u004e\u0001\u0000\u0000\u0000\u000e\u0050\u0001\u0000\u0000\u0000\u0010\u0064\u0001\u0000\u0000\u0000\u0012\u0066\u0001\u0000\u0000\u0000\u0014\u0069\u0001\u0000\u0000\u0000\u0016\u006d\u0001\u0000\u0000\u0000\u0018\u0078\u0001\u0000\u0000\u0000\u001a\u001c\u0003\u0002\u0001\u0000\u001b\u001a\u0001\u0000\u0000\u0000\u001c\u001f\u0001\u0000\u0000\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u0021\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000\u0020\u0022\u0005\u0000\u0000\u0001\u0021\u0020\u0001\u0000\u0000\u0000\u0021\u0022\u0001\u0000\u0000\u0000\u0022\u0001\u0001\u0000\u0000\u0000\u0023\u0027\u0003\u0004\u0002\u0000\u0024\u0027\u0003\u0006\u0003\u0000\u0025\u0027\u0005\u000b\u0000\u0000\u0026\u0023\u0001\u0000\u0000\u0000\u0026\u0024\u0001\u0000\u0000\u0000\u0026\u0025\u0001\u0000\u0000\u0000\u0027\u0003\u0001\u0000\u0000\u0000\u0028\u0029\u0005\u0001\u0000\u0000\u0029\u002a\u0003\u000e\u0007\u0000\u002a\u002e\u0003\u0010\u0008\u0000\u002b\u002d\u0003\u000a\u0005\u0000\u002c\u002b\u0001\u0000\u0000\u0000\u002d\u0030\u0001\u0000\u0000\u0000\u002e\u002c\u0001\u0000\u0000\u0000\u002e\u002f\u0001\u0000\u0000\u0000\u002f\u0031\u0001\u0000\u0000\u0000\u0030\u002e\u0001\u0000\u0000\u0000\u0031\u0032\u0003\u0018\u000c\u0000\u0032\u0005\u0001\u0000\u0000\u0000\u0033\u0038\u0003\u000a\u0005\u0000\u0034\u0035\u0005\u0002\u0000\u0000\u0035\u0037\u0003\u000a\u0005\u0000\u0036\u0034\u0001\u0000\u0000\u0000\u0037\u003a\u0001\u0000\u0000\u0000\u0038\u0036\u0001\u0000\u0000\u0000\u0038\u0039\u0001\u0000\u0000\u0000\u0039\u003b\u0001\u0000\u0000\u0000\u003a\u0038\u0001\u0000\u0000\u0000\u003b\u003f\u0005\u0003\u0000\u0000\u003c\u003e\u0003\u0002\u0001\u0000\u003d\u003c\u0001\u0000\u0000\u0000\u003e\u0041\u0001\u0000\u0000\u0000\u003f\u003d\u0001\u0000\u0000\u0000\u003f\u0040\u0001\u0000\u0000\u0000\u0040\u0042\u0001\u0000\u0000\u0000\u0041\u003f\u0001\u0000\u0000\u0000\u0042\u0043\u0005\u0004\u0000\u0000\u0043\u0007\u0001\u0000\u0000\u0000\u0044\u0045\u0007\u0000\u0000\u0000\u0045\u0009\u0001\u0000\u0000\u0000\u0046\u0047\u0005\u000d\u0000\u0000\u0047\u0048\u0005\u0005\u0000\u0000\u0048\u0049\u0003\u000c\u0006\u0000\u0049\u004a\u0005\u0006\u0000\u0000\u004a\u000b\u0001\u0000\u0000\u0000\u004b\u004f\u0005\u000d\u0000\u0000\u004c\u004f\u0003\u0016\u000b\u0000\u004d\u004f\u0003\u0012\u0009\u0000\u004e\u004b\u0001\u0000\u0000\u0000\u004e\u004c\u0001\u0000\u0000\u0000\u004e\u004d\u0001\u0000\u0000\u0000\u004f\u000d\u0001\u0000\u0000\u0000\u0050\u0054\u0003\u0008\u0004\u0000\u0051\u0053\u0003\u0014\u000a\u0000\u0052\u0051\u0001\u0000\u0000\u0000\u0053\u0056\u0001\u0000\u0000\u0000\u0054\u0052\u0001\u0000\u0000\u0000\u0054\u0055\u0001\u0000\u0000\u0000\u0055\u0061\u0001\u0000\u0000\u0000\u0056\u0054\u0001\u0000\u0000\u0000\u0057\u0058\u0005\u0007\u0000\u0000\u0058\u005c\u0003\u0008\u0004\u0000\u0059\u005b\u0003\u0014\u000a\u0000\u005a\u0059\u0001\u0000\u0000\u0000\u005b\u005e\u0001\u0000\u0000\u0000\u005c\u005a\u0001\u0000\u0000\u0000\u005c\u005d\u0001\u0000\u0000\u0000\u005d\u0060\u0001\u0000\u0000\u0000\u005e\u005c\u0001\u0000\u0000\u0000\u005f\u0057\u0001\u0000\u0000\u0000\u0060\u0063\u0001\u0000\u0000\u0000\u0061\u005f\u0001\u0000\u0000\u0000\u0061\u0062\u0001\u0000\u0000\u0000\u0062\u000f\u0001\u0000\u0000\u0000\u0063\u0061\u0001\u0000\u0000\u0000\u0064\u0065\u0003\u0008\u0004\u0000\u0065\u0011\u0001\u0000\u0000\u0000\u0066\u0067\u0003\u0016\u000b\u0000\u0067\u0068\u0005\u000c\u0000\u0000\u0068\u0013\u0001\u0000\u0000\u0000\u0069\u006a\u0005\u0008\u0000\u0000\u006a\u006b\u0005\u000f\u0000\u0000\u006b\u006c\u0005\u0009\u0000\u0000\u006c\u0015\u0001\u0000\u0000\u0000\u006d\u006e\u0007\u0001\u0000\u0000\u006e\u0017\u0001\u0000\u0000\u0000\u006f\u0079\u0005\u000b\u0000\u0000\u0070\u0072\u0005\u000b\u0000\u0000\u0071\u0070\u0001\u0000\u0000\u0000\u0072\u0075\u0001\u0000\u0000\u0000\u0073\u0071\u0001\u0000\u0000\u0000\u0073\u0074\u0001\u0000\u0000\u0000\u0074\u0076\u0001\u0000\u0000\u0000\u0075\u0073\u0001\u0000\u0000\u0000\u0076\u0079\u0005\u000a\u0000\u0000\u0077\u0079\u0005\u0000\u0000\u0001\u0078\u006f\u0001\u0000\u0000\u0000\u0078\u0073\u0001\u0000\u0000\u0000\u0078\u0077\u0001\u0000\u0000\u0000\u0079\u0019\u0001\u0000\u0000\u0000\u000c\u001d\u0021\u0026\u002e\u0038\u003f\u004e\u0054\u005c\u0061\u0073\u0078"

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
    private val T__8 = Tokens.T__8.id
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

		fun line(): List<LineContext> = getRuleContexts(LineContext::class)
		fun line(i: Int): LineContext? = getRuleContext(LineContext::class, i)
		fun EOF(): TerminalNode? = getToken(Tokens.EOF.id, 0)

		constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
		}

		constructor() : super() {}

		override fun deepCopy(): SourceContext {
			return SourceContext().also {
				deepCopyInto(it)
			}
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
				this.state = 29
				errorHandler.sync(this);
				_la = _input!!.LA(1)
				while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 10242L) != 0L)) {
					scoped {
						scoped {
							this.state = 26
							line()
						}
					}
					this.state = 31
					errorHandler.sync(this)
					_la = _input!!.LA(1)
				}
				this.state = 33
				errorHandler.sync(this)
				when (interpreter!!.adaptivePredict(_input!!, 1, context)) {
					1 -> scoped {
						this.state = 32
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

	open class LineContext : ParserRuleContext {
		override var ruleIndex: Int
			get() = Rules.RULE_line.id
			set(value) {
				throw RuntimeException()
			}

		fun pin(): PinContext? = getRuleContext(PinContext::class, 0)
		fun attributeBlock(): AttributeBlockContext? = getRuleContext(AttributeBlockContext::class, 0)
		fun NL(): TerminalNode? = getToken(Tokens.NL.id, 0)

		constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
		}

		constructor() : super() {}

		override fun deepCopy(): LineContext {
			return LineContext().also {
				deepCopyInto(it)
			}
		}

		override fun enterRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.enterLine(this)
		}

		override suspend fun enterRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.enterLine(this)
		}

		override fun exitRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.exitLine(this)
		}

		override suspend fun exitRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.exitLine(this)
		}
	}

	fun line(): LineContext {
		var _localctx: LineContext = LineContext(context, state)
		enterRule(_localctx, 2, Rules.RULE_line.id)
		try {
			this.state = 38
			errorHandler.sync(this)
			when (_input!!.LA(1)) {
				T__0 ->  /*LL1AltBlock*/ {
					enterOuterAlt(_localctx, 1)
					scoped {
						this.state = 35
						pin()
					}
				}

				BASIC_NAME ->  /*LL1AltBlock*/ {
					enterOuterAlt(_localctx, 2)
					scoped {
						this.state = 36
						attributeBlock()
					}
				}

				NL ->  /*LL1AltBlock*/ {
					enterOuterAlt(_localctx, 3)
					scoped {
						this.state = 37
						match(NL)
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

	open class PinContext : ParserRuleContext {
		override var ruleIndex: Int
			get() = Rules.RULE_pin.id
			set(value) {
				throw RuntimeException()
			}

		fun portName(): PortNameContext? = getRuleContext(PortNameContext::class, 0)
		fun pinName(): PinNameContext? = getRuleContext(PinNameContext::class, 0)
		fun semi(): SemiContext? = getRuleContext(SemiContext::class, 0)
		fun attribute(): List<AttributeContext> = getRuleContexts(AttributeContext::class)
		fun attribute(i: Int): AttributeContext? = getRuleContext(AttributeContext::class, i)

		constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
		}

		constructor() : super() {}

		override fun deepCopy(): PinContext {
			return PinContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 4, Rules.RULE_pin.id)
		var _la: Int
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 40
				match(T__0)
				this.state = 41
				portName()
				this.state = 42
				pinName()
				this.state = 46
				errorHandler.sync(this);
				_la = _input!!.LA(1)
				while (_la == BASIC_NAME) {
					scoped {
						scoped {
							this.state = 43
							attribute()
						}
					}
					this.state = 48
					errorHandler.sync(this)
					_la = _input!!.LA(1)
				}
				this.state = 49
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

	open class AttributeBlockContext : ParserRuleContext {
		override var ruleIndex: Int
			get() = Rules.RULE_attributeBlock.id
			set(value) {
				throw RuntimeException()
			}

		fun attribute(): List<AttributeContext> = getRuleContexts(AttributeContext::class)
		fun attribute(i: Int): AttributeContext? = getRuleContext(AttributeContext::class, i)
		fun line(): List<LineContext> = getRuleContexts(LineContext::class)
		fun line(i: Int): LineContext? = getRuleContext(LineContext::class, i)

		constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
		}

		constructor() : super() {}

		override fun deepCopy(): AttributeBlockContext {
			return AttributeBlockContext().also {
				deepCopyInto(it)
			}
		}

		override fun enterRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.enterAttributeBlock(this)
		}

		override suspend fun enterRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.enterAttributeBlock(this)
		}

		override fun exitRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.exitAttributeBlock(this)
		}

		override suspend fun exitRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.exitAttributeBlock(this)
		}
	}

	fun attributeBlock(): AttributeBlockContext {
		var _localctx: AttributeBlockContext = AttributeBlockContext(context, state)
		enterRule(_localctx, 6, Rules.RULE_attributeBlock.id)
		var _la: Int
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 51
				attribute()
				this.state = 56
				errorHandler.sync(this);
				_la = _input!!.LA(1)
				while (_la == T__1) {
					scoped {
						scoped {
							this.state = 52
							match(T__1)
							this.state = 53
							attribute()
						}
					}
					this.state = 58
					errorHandler.sync(this)
					_la = _input!!.LA(1)
				}
				this.state = 59
				match(T__2)
				this.state = 63
				errorHandler.sync(this);
				_la = _input!!.LA(1)
				while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 10242L) != 0L)) {
					scoped {
						scoped {
							this.state = 60
							line()
						}
					}
					this.state = 65
					errorHandler.sync(this)
					_la = _input!!.LA(1)
				}
				this.state = 66
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

		constructor() : super() {}

		override fun deepCopy(): NameContext {
			return NameContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 8, Rules.RULE_name.id)
		var _la: Int
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 68
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

	open class AttributeContext : ParserRuleContext {
		override var ruleIndex: Int
			get() = Rules.RULE_attribute.id
			set(value) {
				throw RuntimeException()
			}

		fun BASIC_NAME(): TerminalNode? = getToken(Tokens.BASIC_NAME.id, 0)
		fun attributeValue(): AttributeValueContext? = getRuleContext(AttributeValueContext::class, 0)

		constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
		}

		constructor() : super() {}

		override fun deepCopy(): AttributeContext {
			return AttributeContext().also {
				deepCopyInto(it)
			}
		}

		override fun enterRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.enterAttribute(this)
		}

		override suspend fun enterRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.enterAttribute(this)
		}

		override fun exitRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.exitAttribute(this)
		}

		override suspend fun exitRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.exitAttribute(this)
		}
	}

	fun attribute(): AttributeContext {
		var _localctx: AttributeContext = AttributeContext(context, state)
		enterRule(_localctx, 10, Rules.RULE_attribute.id)
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 70
				match(BASIC_NAME)
				this.state = 71
				match(T__4)
				this.state = 72
				attributeValue()
				this.state = 73
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

	open class AttributeValueContext : ParserRuleContext {
		override var ruleIndex: Int
			get() = Rules.RULE_attributeValue.id
			set(value) {
				throw RuntimeException()
			}

		fun BASIC_NAME(): TerminalNode? = getToken(Tokens.BASIC_NAME.id, 0)
		fun number(): NumberContext? = getRuleContext(NumberContext::class, 0)
		fun frequency(): FrequencyContext? = getRuleContext(FrequencyContext::class, 0)

		constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
		}

		constructor() : super() {}

		override fun deepCopy(): AttributeValueContext {
			return AttributeValueContext().also {
				deepCopyInto(it)
			}
		}

		override fun enterRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.enterAttributeValue(this)
		}

		override suspend fun enterRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.enterAttributeValue(this)
		}

		override fun exitRule(listener: ParseTreeListener) {
			if (listener is AcfListener) listener.exitAttributeValue(this)
		}

		override suspend fun exitRule(listener: SuspendParseTreeListener) {
			if (listener is SuspendAcfListener) listener.exitAttributeValue(this)
		}
	}

	fun attributeValue(): AttributeValueContext {
		var _localctx: AttributeValueContext = AttributeValueContext(context, state)
		enterRule(_localctx, 12, Rules.RULE_attributeValue.id)
		try {
			this.state = 78
			errorHandler.sync(this)
			when (interpreter!!.adaptivePredict(_input!!, 6, context)) {
				1 -> {
					enterOuterAlt(_localctx, 1)
					scoped {
						this.state = 75
						match(BASIC_NAME)
					}
				}

				2 -> {
					enterOuterAlt(_localctx, 2)
					scoped {
						this.state = 76
						number()
					}
				}

				3 -> {
					enterOuterAlt(_localctx, 3)
					scoped {
						this.state = 77
						frequency()
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

		constructor() : super() {}

		override fun deepCopy(): PortNameContext {
			return PortNameContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 14, Rules.RULE_portName.id)
		var _la: Int
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 80
				name()
				this.state = 84
				errorHandler.sync(this);
				_la = _input!!.LA(1)
				while (_la == T__7) {
					scoped {
						scoped {
							this.state = 81
							arrayIndex()
						}
					}
					this.state = 86
					errorHandler.sync(this)
					_la = _input!!.LA(1)
				}
				this.state = 97
				errorHandler.sync(this);
				_la = _input!!.LA(1)
				while (_la == T__6) {
					scoped {
						scoped {
							this.state = 87
							match(T__6)
							this.state = 88
							name()
							this.state = 92
							errorHandler.sync(this);
							_la = _input!!.LA(1)
							while (_la == T__7) {
								scoped {
									scoped {
										this.state = 89
										arrayIndex()
									}
								}
								this.state = 94
								errorHandler.sync(this)
								_la = _input!!.LA(1)
							}
						}
					}
					this.state = 99
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

		constructor() : super() {}

		override fun deepCopy(): PinNameContext {
			return PinNameContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 16, Rules.RULE_pinName.id)
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 100
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

		constructor() : super() {}

		override fun deepCopy(): FrequencyContext {
			return FrequencyContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 18, Rules.RULE_frequency.id)
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 102
				number()
				this.state = 103
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

		constructor() : super() {}

		override fun deepCopy(): ArrayIndexContext {
			return ArrayIndexContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 20, Rules.RULE_arrayIndex.id)
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 105
				match(T__7)
				this.state = 106
				match(INT)
				this.state = 107
				match(T__8)
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

		constructor() : super() {}

		override fun deepCopy(): NumberContext {
			return NumberContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 22, Rules.RULE_number.id)
		var _la: Int
		try {
			enterOuterAlt(_localctx, 1)
			scoped {
				this.state = 109
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

		constructor() : super() {}

		override fun deepCopy(): SemiContext {
			return SemiContext().also {
				deepCopyInto(it)
			}
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
		enterRule(_localctx, 24, Rules.RULE_semi.id)
		var _la: Int
		try {
			this.state = 120
			errorHandler.sync(this)
			when (interpreter!!.adaptivePredict(_input!!, 11, context)) {
				1 -> {
					enterOuterAlt(_localctx, 1)
					scoped {
						this.state = 111
						match(NL)
					}
				}

				2 -> {
					enterOuterAlt(_localctx, 2)
					scoped {
						scoped {
							this.state = 115
							errorHandler.sync(this);
							_la = _input!!.LA(1)
							while (_la == NL) {
								scoped {
									scoped {
										this.state = 112
										match(NL)
									}
								}
								this.state = 117
								errorHandler.sync(this)
								_la = _input!!.LA(1)
							}
							this.state = 118
							match(SEMICOLON)
						}
					}
				}

				3 -> {
					enterOuterAlt(_localctx, 3)
					scoped {
						this.state = 119
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