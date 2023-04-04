import com.alchitry.labs.parsers.lucidv2.values.ArrayValue
import com.alchitry.labs.parsers.lucidv2.values.MutableBitList
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class ExprParserTest {
    @Test
    fun testNumbers() {
        var test = LucidTester("5b11011")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("11011", 2, 5)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("hFE01")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("65025", 10, 16)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8hFFF")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("255", 10, 8)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("152")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("152", 10, 8)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("0")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("0", 10, 1)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("20d12")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("12", 10, 20)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testAddition() {
        val test = LucidTester("5b1101 + 4b0010")
        val tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1111", 2, 6)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testSubtraction() {
        val test = LucidTester("5b1101 - 4b0010")
        val tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1011", 2, 6)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testConcat() {
        var test = LucidTester("c{b1101, b0010, 0}")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("110100100", 2, 9)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, b0010, 0}")
        tree = test.expr()
        assertEquals(null, test.exprParser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, {b0010}, {0}}")
        tree = test.expr()
        assertEquals(null, test.exprParser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, {b0010}, {4b0}}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("0000", 2, 4)),
                    SimpleValue(MutableBitList("0010", 2, 4)),
                    SimpleValue(MutableBitList("1101", 2, 4))
                )
            ),
            test.exprParser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testDup() {
        var test = LucidTester("2x{0}")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("00", 2, 2)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8x{2b10}")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1010101010101010", 2, 16)), test.exprParser.values[tree])
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{8}x{2b10}")
        tree = test.expr()
        assertEquals(null, test.exprParser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testArray() {
        var test = LucidTester("{0}")
        var tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("0", 2, 1)),
                )
            ),
            test.exprParser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{{0}}")
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
            test.exprParser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        // values of different sizes = error
        test = LucidTester("{0, 2b10, 2b11}")
        tree = test.expr()
        assertEquals(null, test.exprParser.values[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{b00,b01,b10}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("10", 2, 2)),
                    SimpleValue(MutableBitList("01", 2, 2)),
                    SimpleValue(MutableBitList("00", 2, 2)),
                )
            ),
            test.exprParser.values[tree]
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitSelectorConst() {
        var test = LucidTester("[0:0]")
        var tree = test.bit_selector()
        assertEquals(0..0, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[5:0]")
        tree = test.bit_selector()
        assertEquals(0..5, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[0:5]")
        tree = test.bit_selector()
        assertEquals(null, test.exprParser.bounds[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("[bx0:5]")
        tree = test.bit_selector()
        assertEquals(null, test.exprParser.bounds[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("[[0:5]")
        tree = test.bit_selector()
        assertEquals(null, test.exprParser.bounds[tree])
        assert(test.hasSyntaxIssues)

        test = LucidTester("[1321612161321613216354132465162316516546546516513246:0]")
        tree = test.bit_selector()
        assertEquals(null, test.exprParser.bounds[tree])
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitSelectorFixedWidth() {
        var test = LucidTester("[0+:5]")
        var tree = test.bit_selector()
        assertEquals(0..4, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[5-:5]")
        tree = test.bit_selector()
        assertEquals(1..5, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[5-:2]")
        tree = test.bit_selector()
        assertEquals(4..5, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[5+:0]")
        tree = test.bit_selector()
        assertEquals(null, test.exprParser.bounds[tree])
        assert(test.hasErrors)

        test = LucidTester("[bx+:0]")
        tree = test.bit_selector()
        assertEquals(null, test.exprParser.bounds[tree])
        assert(test.hasErrors)
    }

    @Test
    fun testArrayIndex() {
        var test = LucidTester("[5]")
        var tree = test.array_index()
        assertEquals(5..5, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[0]")
        tree = test.array_index()
        assertEquals(0..0, test.exprParser.bounds[tree])
        assert(test.hasNoIssues)

        test = LucidTester("[bx]")
        tree = test.array_index()
        assertEquals(null, test.exprParser.bounds[tree])
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