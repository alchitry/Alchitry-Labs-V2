import com.alchitry.labs.parsers.Util
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.ArrayValue
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitValue
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class ExprParserTest {
    @Test
    fun testNumbers() {
        var test = LucidTester("5b11011")
        var tree = test.expr()
        assertEquals(BitListValue("11011", 2, 5, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("hFE01")
        tree = test.expr()
        assertEquals(
            BitListValue("65025", 10, 16, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8hFFF")
        tree = test.expr()
        assertEquals(BitListValue("255", 10, 8, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("152")
        tree = test.expr()
        assertEquals(BitListValue("152", 10, 8, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("0")
        tree = test.expr()
        assertEquals(BitValue(Bit.B0, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("1")
        tree = test.expr()
        assertEquals(BitValue(Bit.B1, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("20d12")
        tree = test.expr()
        assertEquals(BitListValue("12", 10, 20, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testAddition() {
        val test = LucidTester("5b1101 + 4b0010")
        val tree = test.expr()
        assertEquals(BitListValue("1111", 2, 6, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testSubtraction() {
        val test = LucidTester("5b1101 - 4b0010")
        val tree = test.expr()
        assertEquals(BitListValue("1011", 2, 6, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testConcat() {
        var test = LucidTester("c{b1101, b0010, 0}")
        var tree = test.expr()
        assertEquals(
            BitListValue("110100100", 2, 9, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, b0010, 0}")
        tree = test.expr()
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, {b0010}, {0}}")
        tree = test.expr()
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("c{{b1101}, {b0010}, {4b0}}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("0000", 2, 4, constant = true, signed = false),
                    BitListValue("0010", 2, 4, constant = true, signed = false),
                    BitListValue("1101", 2, 4, constant = true, signed = false)
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testDup() {
        var test = LucidTester("2x{0}")
        var tree = test.expr()
        assertEquals(BitListValue("00", 2, 2, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8x{2b10}")
        tree = test.expr()
        assertEquals(
            BitListValue("1010101010101010", 2, 16, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{8}x{2b10}")
        tree = test.expr()
        assertEquals(null, test.context.expr.resolve(tree))
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
                    BitListValue("0", 2, 1, constant = true, signed = false),
                )
            ),
            test.context.expr.resolve(tree)
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
                            BitListValue("0", 2, 1, constant = true, signed = false),
                        )
                    )
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        // values of different sizes = error
        test = LucidTester("{0, 2b10, 2b11}")
        tree = test.expr()
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{b00,b01,b10}")
        tree = test.expr()
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("10", 2, 2, constant = true, signed = false),
                    BitListValue("01", 2, 2, constant = true, signed = false),
                    BitListValue("00", 2, 2, constant = true, signed = false),
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testMultiply() {
        var test = LucidTester("20 * 40")
        var tree = test.expr()

        assertEquals(
            BitListValue(
                "800",
                10,
                Util.widthOfMult(Util.minWidthNum(20), Util.minWidthNum(40)),
                constant = true,
                signed = false
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("20 * {40}")
        test.expr()

        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("{20} * {40}")
        test.expr()

        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$signed(20) * 40")
        tree = test.expr()

        assertEquals(
            BitListValue(
                "800",
                10,
                Util.widthOfMult(Util.minWidthNum(20), Util.minWidthNum(40)),
                constant = true,
                signed = false
            ),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("-20 * -40")
        tree = test.expr()

        assertEquals(
            BitListValue(
                "800",
                10,
                Util.widthOfMult(Util.minWidthNum(20) + 1, Util.minWidthNum(40) + 1),
                constant = true,
                signed = true
            ),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testDivide() {
        var test = LucidTester("40 / 8")
        var tree = test.expr()

        assertEquals(
            BitListValue("5", 10, Util.minWidthNum(40), constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("40 / 5")
        tree = test.expr()

        assertEquals(
            BitListValue("8", 10, Util.minWidthNum(40), constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        // TODO: Check for warning with non-constant non-power 2 divisor
    }

    @Test
    fun testShift() {
        var test = LucidTester("40 >> 3")
        var tree = test.expr()

        assertEquals(
            BitListValue("5", 10, Util.minWidthNum(40), constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("-8 >> 2")
        tree = test.expr()

        assertEquals(
            BitListValue("00110", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("-8 >>> 2")
        tree = test.expr()

        assertEquals(
            BitListValue("11110", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8 << 1")
        tree = test.expr()

        assertEquals(
            BitListValue("16", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("-8 << 1")
        tree = test.expr()

        assertEquals(
            BitListValue("110000", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("8 <<< 1")
        tree = test.expr()

        assertEquals(
            BitListValue("16", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("-8 <<< 1")
        tree = test.expr()

        assertEquals(
            BitListValue("110000", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitwise() {
        var test = LucidTester("b1101 & b1001")
        var tree = test.expr()

        assertEquals(
            BitListValue("1001", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("b001101 & b1001")
        tree = test.expr()

        assertEquals(
            BitListValue("001001", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$signed(b001101) & \$signed(b1001)")
        tree = test.expr()

        assertEquals(
            BitListValue("001001", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("b1101 | b1001")
        tree = test.expr()

        assertEquals(
            BitListValue("1101", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("b101101 | b1010")
        tree = test.expr()

        assertEquals(
            BitListValue("101111", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$signed(b001101) | \$signed(b1001)")
        tree = test.expr()

        assertEquals(
            BitListValue("111101", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("b1101 ^ b1001")
        tree = test.expr()

        assertEquals(
            BitListValue("0100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("b001101 ^ b1001")
        tree = test.expr()

        assertEquals(
            BitListValue("000100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$signed(b001101) ^ \$signed(b1001)")
        tree = test.expr()

        assertEquals(
            BitListValue("110100", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testReduction() {
        var test = LucidTester("|b1001")
        var tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("|b0000")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("&b1001")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("&b1111")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("&b1x01")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.Bx, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("^b1001")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("^b1011")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testCompare() {
        var test = LucidTester("10 < 4")
        var tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("10 > 4")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("4 >= 10")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("10 >= 10")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("10 <= 4")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("10 <= 10")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testLogical() {
        var test = LucidTester("10 || 0")
        var tree = test.expr()
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = LucidTester("0 || 0")
        tree = test.expr()
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = LucidTester("10 && 0")
        tree = test.expr()
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = LucidTester("10 && 4")
        tree = test.expr()
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
    }

    @Test
    fun testTernary() {
        var test = LucidTester("10 ? 1 : 2")
        var tree = test.expr()
        assert(test.hasNoIssues)

        assertEquals(
            BitListValue("1", 2, 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = LucidTester("10b0 ? 1 : 2")
        tree = test.expr()
        assert(test.hasNoIssues)

        assertEquals(
            BitListValue("2", 10, 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
    }

    @Test
    fun testInvert() {
        var test = LucidTester("!10")
        var tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("!0")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("~b101")
        tree = test.expr()

        assertEquals(
            BitListValue("010", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testNegate() {
        var test = LucidTester("-20")
        var tree = test.expr()

        assertEquals(
            BitListValue("101100", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("--20")
        tree = test.expr()

        assertEquals(
            BitListValue("20", 10, Util.minWidthNum(20) + 2, signed = true, constant = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testFunctions() {
        var test = LucidTester("\$signed(20)")
        var tree = test.expr()

        assertEquals(
            BitListValue("20", 10, width = Util.minWidthNum(20), signed = true, constant = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$signed(-20)")
        tree = test.expr()

        assertEquals(
            BitListValue("101100", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$clog2(7)")
        tree = test.expr()

        assertEquals(
            BitListValue("3", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$clog2(0)")
        tree = test.expr()

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$clog2(1)")
        tree = test.expr()

        assertEquals(
            BitListValue("0", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$clog2(129)")
        tree = test.expr()

        assertEquals(
            BitListValue("8", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$pow(3,0)")
        tree = test.expr()

        assertEquals(
            BitListValue("1", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$pow(2,4)")
        tree = test.expr()

        assertEquals(
            BitListValue("16", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$reverse(b1100)")
        tree = test.expr()

        assertEquals(
            BitListValue("0011", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$reverse({b1100, b0011})")
        tree = test.expr()

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("1100", 2, constant = true, signed = false),
                    BitListValue("0011", 2, constant = true, signed = false)
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$flatten(b1100)")
        tree = test.expr()

        assertEquals(
            BitListValue("1100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$flatten({b1100, b0011})")
        tree = test.expr()

        assertEquals(
            BitListValue("11000011", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        // TODO: Test flatten for structs

        test = LucidTester("\$build(b1100, 2)")
        tree = test.expr()

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("00", 2, constant = true, signed = false),
                    BitListValue("11", 2, constant = true, signed = false)
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$build(b11001001, 2, 2)")
        tree = test.expr()

        assertEquals(
            ArrayValue(
                listOf(
                    ArrayValue(
                        listOf(
                            BitListValue("01", 2, constant = true, signed = false),
                            BitListValue("10", 2, constant = true, signed = false)
                        )
                    ),
                    ArrayValue(
                        listOf(
                            BitListValue("00", 2, constant = true, signed = false),
                            BitListValue("11", 2, constant = true, signed = false)
                        )
                    )
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$unsigned(20)")
        tree = test.expr()

        assertEquals(
            BitListValue("20", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$unsigned(\$signed(-20))")
        tree = test.expr()

        assertEquals(
            BitListValue("101100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$cdiv(8, 3)")
        tree = test.expr()

        assertEquals(
            BitListValue("3", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$cdiv(9, 3)")
        tree = test.expr()

        assertEquals(
            BitListValue("3", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$cdiv(10, 3)")
        tree = test.expr()

        assertEquals(
            BitListValue("4", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$resize(8, 3)")
        tree = test.expr()

        assertEquals(
            BitListValue("8", 10, 3, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasWarnings) // should warn about truncation
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("\$resize(1, 3)")
        tree = test.expr()

        assertEquals(
            BitListValue("1", 10, 3, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun simpleSignalTest() {
        val test = LucidTester("test[2]")
        val signal = Signal("test", SignalDirection.Both, null, BitListValue("110", 2, constant = false, signed = false))
        test.context.testingSignalResolver = TestSignalResolver(signal)
        val exprCtx = test.expr()

        assert(test.hasNoIssues)

        assertEquals(BitValue(Bit.B1, constant = false, signed = false), test.context.expr.resolve(exprCtx))
    }

    @Test
    fun rangeSignalTest() {
        val test = LucidTester("test[2:1]")
        val signal = Signal("test", SignalDirection.Both, null, BitListValue("1010", 2, constant = false, signed = false))
        test.context.testingSignalResolver = TestSignalResolver(signal)
        val exprCtx = test.expr()

        assert(test.hasNoIssues)

        assertEquals(BitListValue("01", 2, constant = false, signed = false), test.context.expr.resolve(exprCtx))
    }

    @Test
    fun rangeOutOfBoundsSignalTest() {
        val test = LucidTester("test[9:1]")
        val signal = Signal("test", SignalDirection.Both, null, BitListValue("1010", 2, constant = false, signed = false))
        test.context.testingSignalResolver = TestSignalResolver(signal)
        test.expr()

        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }
}