import com.alchitry.labs2.parsers.BitUtil
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.values.ArrayValue
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import com.alchitry.labs2.parsers.lucidv2.values.BitValue
import helpers.SimpleLucidTester
import helpers.TestSignalResolver
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class ExprParserTests {
    @Test
    fun testNumbers() = runBlocking {
        var test = SimpleLucidTester("5b11011")
        var tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("11011", 2, 5, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = SimpleLucidTester("hFE01")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(
            BitListValue("65025", 10, 16, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8hFFF")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("255", 10, 8, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoErrors)
        assert(test.hasWarnings)

        test = SimpleLucidTester("152")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("152", 10, 8, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = SimpleLucidTester("0")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitValue(Bit.B0, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = SimpleLucidTester("1")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitValue(Bit.B1, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = SimpleLucidTester("20d12")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("12", 10, 20, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)
    }

    @Test
    fun testMinBits() {
        assertEquals(1, BitListValue("1", 16, constant = true, signed = false).minimumBits())
        assertEquals(1, BitListValue("0", 16, constant = true, signed = false).minimumBits())
        assertEquals(4, BitListValue("F", 16, constant = true, signed = false).minimumBits())
        assertEquals(1, BitListValue("11", 2, constant = true, signed = true).minimumBits())
        assertEquals(3, BitListValue("101", 2, constant = true, signed = true).minimumBits())
        assertEquals(3, BitListValue("011", 2, constant = true, signed = true).minimumBits())
        assertEquals(4, BitListValue("0101", 2, constant = true, signed = true).minimumBits())
        assertEquals(3, BitListValue("0011", 2, constant = true, signed = true).minimumBits())
        assertEquals(4, BitListValue("00101", 2, constant = true, signed = true).minimumBits())
    }

    @Test
    fun testAddition() = runBlocking {
        val test = SimpleLucidTester("5b1101 + 4b0010")
        val tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("1111", 2, 6, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)
    }

    @Test
    fun testSubtraction() = runBlocking {
        val test = SimpleLucidTester("5b1101 - 4b0010")
        val tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("1011", 2, 6, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)
    }

    @Test
    fun testSignedSubtraction() = runBlocking {
        val test = SimpleLucidTester("${"$"}signed(16d10) - ${"$"}signed(8d50)")
        val tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(
            BitListValue("11111111111011000", 2, 17, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testConcat() = runBlocking {
        var test = SimpleLucidTester("c{b1101, b0010, 0}")
        var tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(
            BitListValue("110100100", 2, 9, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("c{{b1101}, b0010, 0}")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("c{{b1101}, {b0010}, {0}}")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("c{{b1101}, {b0010}, {4b0}}")
        tree = test.parser.expr().also { test.context.walk(it) }
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
        assert(test.hasNoIssues)
    }

    @Test
    fun testDup() = runBlocking {
        var test = SimpleLucidTester("2x{0}")
        var tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(BitListValue("00", 2, 2, constant = true, signed = false), test.context.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8x{2b10}")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(
            BitListValue("1010101010101010", 2, 16, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("{8}x{2b10}")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
    }

    @Test
    fun testArray() = runBlocking {
        var test = SimpleLucidTester("{0}")
        var tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("0", 2, 1, constant = true, signed = false),
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("{{0}}")
        tree = test.parser.expr().also { test.context.walk(it) }
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
        assert(test.hasNoIssues)

        // values of different sizes = error
        test = SimpleLucidTester("{0, 2b10, 2b11}")
        tree = test.parser.expr().also { test.context.walk(it) }
        assertEquals(null, test.context.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("{b00,b01,b10}")
        tree = test.parser.expr().also { test.context.walk(it) }
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
        assert(test.hasNoIssues)
    }

    @Test
    fun testMultiply() = runBlocking {
        var test = SimpleLucidTester("20 * 40")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue(
                "800",
                10,
                BitUtil.widthOfMult(BitUtil.minWidthNum(20), BitUtil.minWidthNum(40)),
                constant = true,
                signed = false
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("20 * {40}")
        test.parser.expr().also { test.context.walk(it) }

        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("{20} * {40}")
        test.parser.expr().also { test.context.walk(it) }

        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("\$signed(20) * 40")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue(
                "800",
                10,
                BitUtil.widthOfMult(BitUtil.minWidthNum(20), BitUtil.minWidthNum(40)),
                constant = true,
                signed = false
            ),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("-20 * -40")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue(
                "800",
                10,
                BitUtil.widthOfMult(BitUtil.minWidthNum(20) + 1, BitUtil.minWidthNum(40) + 1),
                constant = true,
                signed = true
            ),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)
    }

    @Test
    fun testDivide() = runBlocking {
        var test = SimpleLucidTester("40 / 8")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("5", 10, BitUtil.minWidthNum(40), constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("40 / 5")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("8", 10, BitUtil.minWidthNum(40), constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        // TODO: Check for warning with non-constant non-power 2 divisor
    }

    @Test
    fun testNegativeShift() = runBlocking {
        val test = SimpleLucidTester("-8 >> 2")
        val tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("00110", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testShift() = runBlocking {
        var test = SimpleLucidTester("40 >> 3")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("5", 10, BitUtil.minWidthNum(40), constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8\n >> 2")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("00110", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8 >>> 2")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("11110", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8 << 1")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("16", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8 << 1")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("110000", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8 <<< 1")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("16", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8 <<< 1")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("110000", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testBitwise() = runBlocking {
        var test = SimpleLucidTester("b1101 & b1001")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("1001", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b001101 & b1001")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("001001", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(b001101) & \$signed(b1001)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("001001", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b1101 | b1001")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("1101", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b101101 | b1010")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("101111", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(b001101) | \$signed(b1001)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("111101", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b1101 ^ b1001")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("0100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b001101 ^ b1001")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("000100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(b001101) ^ \$signed(b1001)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("110100", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testReduction() = runBlocking {
        var test = SimpleLucidTester("|b1001")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("|b0000")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("&b1001")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("&b1111")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("&b1x11")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.Bx, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("^b1001")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("^b1011")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testCompare() = runBlocking {
        var test = SimpleLucidTester("10 < 4")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 > 4")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("4 >= 10")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 >= 10")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 <= 4")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 <= 10")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testLogical() = runBlocking {
        var test = SimpleLucidTester("10 || 0")
        var tree = test.parser.expr().also { test.context.walk(it) }
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = SimpleLucidTester("0 || 0")
        tree = test.parser.expr().also { test.context.walk(it) }
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = SimpleLucidTester("10 && 0")
        tree = test.parser.expr().also { test.context.walk(it) }
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = SimpleLucidTester("10 && 4")
        tree = test.parser.expr().also { test.context.walk(it) }
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
    }

    @Test
    fun testTernary() = runBlocking {
        var test = SimpleLucidTester("10 ? 1 : 2")
        var tree = test.parser.expr().also { test.context.walk(it) }
        assert(test.hasNoIssues)

        assertEquals(
            BitListValue("1", 2, 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        test = SimpleLucidTester("10b0 ? 1 : 2")
        tree = test.parser.expr().also { test.context.walk(it) }
        assert(test.hasNoIssues)

        assertEquals(
            BitListValue("2", 10, 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
    }

    @Test
    fun testInvert() = runBlocking {
        var test = SimpleLucidTester("!10")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("!0")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B1, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("~b101")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("010", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testNegate() = runBlocking {
        var test = SimpleLucidTester("-20")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("101100", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("--20")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("20", 10, BitUtil.minWidthNum(20) + 2, signed = true, constant = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)
    }

    @Test
    fun testFunctions() = runBlocking {
        var test = SimpleLucidTester("\$signed(20)")
        var tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("20", 10, width = BitUtil.minWidthNum(20), signed = true, constant = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(-20)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("101100", 2, constant = true, signed = true),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(7)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("3", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(0)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitValue(Bit.B0, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(1)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("0", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(129)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("8", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$pow(3,0)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("1", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$pow(2,4)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("16", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$reverse(b1100)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("0011", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$reverse({b1100, b0011})")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("1100", 2, constant = true, signed = false),
                    BitListValue("0011", 2, constant = true, signed = false)
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$flatten(b1100)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("1100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$flatten({b1100, b0011})")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("11000011", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        // TODO: Test flatten for structs

        test = SimpleLucidTester("\$build(b1100, 2)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("00", 2, constant = true, signed = false),
                    BitListValue("11", 2, constant = true, signed = false)
                )
            ),
            test.context.expr.resolve(tree)
        )
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$build(b11001001, 2, 2)")
        tree = test.parser.expr().also { test.context.walk(it) }

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
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$unsigned(20)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("20", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$unsigned(\$signed(-20))")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("101100", 2, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$cdiv(8, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("3", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$cdiv(9, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("3", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$cdiv(10, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("4", 10, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$resize(8, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("8", 10, 3, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoErrors)
        assert(test.hasWarnings) // should warn about truncation

        test = SimpleLucidTester("\$resize(1, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }

        assertEquals(
            BitListValue("1", 10, 3, constant = true, signed = false),
            test.context.expr.resolve(tree)
        )

        assert(test.hasNoIssues)
    }

    @Test
    fun simpleSignalTest() = runBlocking {
        val signal =
            Signal("mySig", SignalDirection.Both, null, BitListValue("110", 2, constant = false, signed = false))
        val test =
            SimpleLucidTester("mySig[2]", localSignalResolver = TestSignalResolver(signal))
        val exprCtx = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        assertEquals(BitValue(Bit.B1, constant = false, signed = false), test.context.expr.resolve(exprCtx))
    }

    @Test
    fun rangeSignalTest() = runBlocking {
        val signal =
            Signal("mySig", SignalDirection.Both, null, BitListValue("1010", 2, constant = false, signed = false))
        val test =
            SimpleLucidTester("mySig[2:1]", localSignalResolver = TestSignalResolver(signal))
        val exprCtx = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        assertEquals(BitListValue("01", 2, constant = false, signed = false), test.context.expr.resolve(exprCtx))
    }

    @Test
    fun rangeOutOfBoundsSignalTest() = runBlocking {
        val signal =
            Signal("mySig", SignalDirection.Both, null, BitListValue("1010", 2, constant = false, signed = false))
        val test =
            SimpleLucidTester("mySig[9:1]", localSignalResolver = TestSignalResolver(signal))
        test.parser.expr().also { test.context.walk(it) }

        assert(test.hasErrors)
        assert(test.hasNoWarnings)
    }

    @Test
    fun fixedPointTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fixedPoint(3.14, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(50, 8, true, false),
            value
        )
    }

    @Test
    fun fixedPointIntTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fixedPoint(10, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(160, 8, true, false),
            value
        )
    }

    @Test
    fun fixedPointOverflowTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fixedPoint(300.14, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoErrors)
        assert(test.hasWarnings)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(194, 8, true, false),
            value
        )
    }

    @Test
    fun cFixedPointTest() = runBlocking {
        val test =
            SimpleLucidTester("\$cFixedPoint(3.14, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(51, 8, true, false),
            value
        )
    }

    @Test
    fun fFixedPointTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fFixedPoint(3.16, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(50, 8, true, false),
            value
        )
    }
}