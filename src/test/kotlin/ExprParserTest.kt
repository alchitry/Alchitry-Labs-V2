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
        assertEquals(SimpleValue(MutableBitList("11011", 2, 5), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("hFE01")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("65025", 10, 16), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8hFFF")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("255", 10, 8), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("152")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("152", 10, 8), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("0")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("0", 10, 1), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("20d12")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("12", 10, 20), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testAddition() {
        val test = LucidTester("5b1101 + 4b0010")
        val tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1111", 2, 6), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testSubtraction() {
        val test = LucidTester("5b1101 - 4b0010")
        val tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1011", 2, 6), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testConcat() {
        var test = LucidTester("c{b1101, b0010, 0}")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("110100100", 2, 9), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, b0010, 0}")
        tree = test.expr()
        assertEquals(null, test.exprParser.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, {b0010}, {0}}")
        tree = test.expr()
        assertEquals(null, test.exprParser.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, {b0010}, {4b0}}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("0000", 2, 4), true),
                    SimpleValue(MutableBitList("0010", 2, 4), true),
                    SimpleValue(MutableBitList("1101", 2, 4), true)
                )
            ),
            test.exprParser.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testDup() {
        var test = LucidTester("2x{0}")
        var tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("00", 2, 2), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8x{2b10}")
        tree = test.expr()
        assertEquals(SimpleValue(MutableBitList("1010101010101010", 2, 16), true), test.exprParser.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{8}x{2b10}")
        tree = test.expr()
        assertEquals(null, test.exprParser.resolve(tree))
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
                    SimpleValue(MutableBitList("0", 2, 1), true),
                )
            ),
            test.exprParser.resolve(tree)
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
                            SimpleValue(MutableBitList("0", 2, 1), true),
                        )
                    )
                )
            ),
            test.exprParser.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        // values of different sizes = error
        test = LucidTester("{0, 2b10, 2b11}")
        tree = test.expr()
        assertEquals(null, test.exprParser.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{b00,b01,b10}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    SimpleValue(MutableBitList("10", 2, 2), true),
                    SimpleValue(MutableBitList("01", 2, 2), true),
                    SimpleValue(MutableBitList("00", 2, 2), true),
                )
            ),
            test.exprParser.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
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