import com.alchitry.labs.parsers.lucidv2.ExprParser
import com.alchitry.labs.parsers.lucidv2.SignalParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.ArrayType
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SimpleType
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.SimpleWidth
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class SignalParserTest {
    @Test
    fun testDffSimpleDeclaration() {
        val tester = Tester("dff testing;")
        tester.dff_dec() // parse
        val dSig = tester.parser.resolve("testing.d")
        val qSig = tester.parser.resolve("testing.q")
        assertNotNull(dSig, "DFF d signal was null!")
        assertNotNull(qSig, "DFF q signal was null!")

        assertEquals(SimpleType(SimpleWidth(1)), dSig.type)
        assertEquals(SimpleType(SimpleWidth(1)), qSig.type)
        assertEquals(SignalDirection.Write, dSig.direction)
        assertEquals(SignalDirection.Read, qSig.direction)
    }

    private class Tester(text: String) :
        LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(text)
                ).also { it.removeErrorListeners() })
        ) {

        val errorCollector = ErrorCollector()
        val parser: SignalParser

        init {
            (tokenStream.tokenSource as LucidLexer).addErrorListener(errorCollector)
            removeErrorListeners()
            addErrorListener(errorCollector)
            parser = SignalParser(errorCollector)
            addParseListener(parser)
        }

        val hasNoIssues = hasNoErrors && hasNoWarnings && hasNoSyntaxIssues

        val hasNoErrors: Boolean get() = errorCollector.errors.isEmpty()
        val hasNoWarnings: Boolean get() = errorCollector.warnings.isEmpty()
        val hasNoSyntaxIssues: Boolean get() = errorCollector.syntaxIssues.isEmpty()
        val hasErrors: Boolean get() = errorCollector.errors.isNotEmpty()
        val hasWarnings: Boolean get() = errorCollector.warnings.isNotEmpty()
        val hasSyntaxIssues: Boolean get() = errorCollector.syntaxIssues.isNotEmpty()
    }
}