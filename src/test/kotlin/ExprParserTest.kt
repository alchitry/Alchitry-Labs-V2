import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.lucidv2.ExprParser
import com.alchitry.labs.parsers.lucidv2.values.ArrayValue
import com.alchitry.labs.parsers.lucidv2.values.MutableBitList
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.TerminalNode
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*
import kotlin.test.Test

internal class ExprParserTest {
    private class Tester(text: String) :
        LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(text)
                ).also { it.removeErrorListeners() })
        ) {

        val errorCollector = ErrorCollector()
        val parser: ExprParser

        init {
            (tokenStream.tokenSource as LucidLexer).addErrorListener(errorCollector)
            removeErrorListeners()
            addErrorListener(errorCollector)
            parser = ExprParser(errorCollector)
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

    @Test
    fun testNumbers() {
        var test = Tester("5b11011")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("11011", 2, 5)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("hFE01")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("65025", 10, 16)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("8hFFF")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("255", 10, 8)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("152")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("152", 10, 8)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("0")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("0", 10, 1)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("20d12")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("12", 10, 20)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testAddition() {
        val test = Tester("5b1101 + 4b0010")
        val tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1111", 2, 6)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testSubtraction() {
        val test = Tester("5b1101 - 4b0010")
        val tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1011", 2, 6)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testConcat() {
        var test = Tester("c{b1101, b0010, 0}")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("110100100", 2, 9)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("c{{b1101}, b0010, 0}")
        tree = test.expr()
        assertEquals(null, test.parser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("c{{b1101}, {b0010}, {0}}")
        tree = test.expr()
        assertEquals(null, test.parser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("c{{b1101}, {b0010}, {4b0}}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("0000", 2, 4)),
                    SimpleValue(MutableBitList("0010", 2, 4)),
                    SimpleValue(MutableBitList("1101", 2, 4))
                )
            ),
            test.parser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testDup() {
        var test = Tester("2x{0}")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("00", 2, 2)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("8x{2b10}")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1010101010101010", 2, 16)), test.parser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("{8}x{2b10}")
        tree = test.expr()
        assertEquals(null, test.parser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testArray() {
        var test = Tester("{0}")
        var tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("0", 2, 1)),
                )
            ),
            test.parser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("{{0}}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    ArrayValue(
                        listOf(
                            SimpleValue(MutableBitList("0", 2, 1)),
                        )
                    )
                )
            ),
            test.parser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        // values of different sizes = error
        test = Tester("{0, 2b10, 2b11}")
        tree = test.expr()
        assertEquals(null, test.parser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("{b00,b01,b10}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("10", 2, 2)),
                    SimpleValue(MutableBitList("01", 2, 2)),
                    SimpleValue(MutableBitList("00", 2, 2)),
                )
            ),
            test.parser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitSelectorConst() {
        var test = Tester("[0:0]")
        var tree = test.bit_selector()
        assertEquals(0..0, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[5:0]")
        tree = test.bit_selector()
        assertEquals(0..5, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[0:5]")
        tree = test.bit_selector()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("[bx0:5]")
        tree = test.bit_selector()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = Tester("[[0:5]")
        tree = test.bit_selector()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasSyntaxIssues)

        test = Tester("[1321612161321613216354132465162316516546546516513246:0]")
        tree = test.bit_selector()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitSelectorFixedWidth() {
        var test = Tester("[0+:5]")
        var tree = test.bit_selector()
        assertEquals(0..4, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[5-:5]")
        tree = test.bit_selector()
        assertEquals(1..5, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[5-:2]")
        tree = test.bit_selector()
        assertEquals(4..5, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[5+:0]")
        tree = test.bit_selector()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasErrors)

        test = Tester("[bx+:0]")
        tree = test.bit_selector()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasErrors)
    }

    @Test
    fun testArrayIndex() {
        var test = Tester("[5]")
        var tree = test.array_index()
        assertEquals(5..5, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[0]")
        tree = test.array_index()
        assertEquals(0..0, test.parser.bounds[tree])
        assert(test.hasNoIssues)

        test = Tester("[bx]")
        tree = test.array_index()
        assertEquals(null, test.parser.bounds[tree])
        assert(test.hasErrors)
    }

    @Test
    fun testMultiply() {
        TODO()
    }

    @Test
    fun testDivide() {
        TODO()
    }

    @Test
    fun testShift() {
        TODO()
    }

    @Test
    fun testBitwise() {
        TODO()
    }

    @Test
    fun testReduction() {
        TODO()
    }

    @Test
    fun testCompare() {
        TODO()
    }

    @Test
    fun testLogical() {
        TODO()
    }

    @Test
    fun testTernary() {
        TODO()
    }

    @Test
    fun testInvert() {
        TODO()
    }

    @Test
    fun testNegate() {
        TODO()
    }

    @Test
    fun testFunctions() {
        TODO("Test all functions found in Function.kt")
    }
}