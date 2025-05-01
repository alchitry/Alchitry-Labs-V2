package basic

import com.alchitry.labs2.parsers.BitUtil
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.asConstExpr
import com.alchitry.labs2.parsers.hdl.asDynamicExpr
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.*
import helpers.SimpleLucidTester
import helpers.TestSignalResolver
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ExprParserTests {
    @Test
    fun testNumbers() = runBlocking {
        var test = SimpleLucidTester("5b11011")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("11011", 2, 5, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("hFE01")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(
            BitListValue("65025", 10, 16, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8hFFF")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("255", 10, 8, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoErrors)
        assert(test.hasWarnings)

        test = SimpleLucidTester("152")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("152", 10, 8, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("0")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(BitValue(Bit.B0, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("1")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(BitValue(Bit.B1, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("20d12")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("12", 10, 20, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testXExtension() = runBlocking {
        val test = SimpleLucidTester("12hFX")
        val tree = test.parser.expr().also { test.context.walk(it) }
        val expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("0FX", 16, 12, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testStrings() = runBlocking {
        val test = SimpleLucidTester("\"Hi\"")
        val tree = test.parser.expr().also { test.context.walk(it) }

        val test2 = SimpleLucidTester("{8h48, 8h69}") // H = 8h48, i = 8h69
        val tree2 = test2.parser.expr().also { test2.context.walk(it) }

        val expected = test2.context.expr.resolve(tree2)
        val string = test.context.expr.resolve(tree)
        assertEquals(expected, string)
        assert(test.hasNoIssues)
    }

    @Test
    fun testMinBits() {
        assertEquals(1, BitListValue("1", 16, signed = false).minimumBits())
        assertEquals(1, BitListValue("0", 16, signed = false).minimumBits())
        assertEquals(4, BitListValue("F", 16, signed = false).minimumBits())
        assertEquals(1, BitListValue("11", 2, signed = true).minimumBits())
        assertEquals(3, BitListValue("101", 2, signed = true).minimumBits())
        assertEquals(3, BitListValue("011", 2, signed = true).minimumBits())
        assertEquals(4, BitListValue("0101", 2, signed = true).minimumBits())
        assertEquals(3, BitListValue("0011", 2, signed = true).minimumBits())
        assertEquals(4, BitListValue("00101", 2, signed = true).minimumBits())
    }

    @Test
    fun testAddition() = runBlocking {
        val test = SimpleLucidTester("5b1101 + 4b0010")
        val tree = test.parser.expr().also { test.context.walk(it) }
        val expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("1111", 2, 6, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testSubtraction() = runBlocking {
        val test = SimpleLucidTester("5b1101 - 4b0010")
        val tree = test.parser.expr().also { test.context.walk(it) }
        val expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("1011", 2, 6, signed = false), expr.value)
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testSignedSubtraction() = runBlocking {
        val test = SimpleLucidTester("${"$"}signed(16d10) - ${"$"}signed(8d50)")
        val tree = test.parser.expr().also { test.context.walk(it) }
        val expr = test.context.expr.resolve(tree)!!
        assertEquals(
            BitListValue("11111111111011000", 2, 17, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testConcat() = runBlocking {
        var test = SimpleLucidTester("c{b1101, b0010, 0}")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)
        assertEquals(
            BitListValue("110100100", 2, 9, signed = false),
            expr?.value
        )
        assertEquals(ExprType.Constant, expr?.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("c{{b1101}, b0010, 0}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)
        assertEquals(null, expr?.value)
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("c{{b1101}, {b0010}, {0}}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)
        assertEquals(null, expr?.value)
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("c{{b1101}, {b0010}, {4b0}}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("0000", 2, 4, signed = false),
                    BitListValue("0010", 2, 4, signed = false),
                    BitListValue("1101", 2, 4, signed = false)
                )
            ),
            expr.value
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun testDup() = runBlocking {
        var test = SimpleLucidTester("2x{0}")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!
        assertEquals(BitListValue("00", 2, 2, signed = false), expr.value)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8x{2b10}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(
            BitListValue("1010101010101010", 2, 16, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("{8}x{2b10}")
        tree = test.parser.expr().also { test.context.walk(it) }
        val exprNullable = test.context.expr.resolve(tree)
        assertEquals(null, exprNullable?.value)
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
    }

    @Test
    fun testArray() = runBlocking {
        var test = SimpleLucidTester("{0}")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("0", 2, 1, signed = false),
                )
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("{{0}}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(
            ArrayValue(
                listOf(
                    ArrayValue(
                        listOf(
                            BitListValue("0", 2, 1, signed = false),
                        )
                    )
                )
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        // values of different sizes = error
        test = SimpleLucidTester("{0, 2b10, 2b11}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        val result = expr.value
        assert(result is ArrayValue)
        result as ArrayValue
        assertEquals(3, result.width.size)
        assertEquals(BitListValue(0, 1, signed = false), result.elements[2])
        assertEquals(ExprType.Constant, expr.type)
        assert(result.elements[1] is UndefinedValue)
        assert(result.elements[0] is UndefinedValue)
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        test = SimpleLucidTester("{b00,b01,b10}")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("10", 2, 2, signed = false),
                    BitListValue("01", 2, 2, signed = false),
                    BitListValue("00", 2, 2, signed = false),
                )
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testMultiply() = runBlocking {
        var test = SimpleLucidTester("20 * 40")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue(
                "800",
                10,
                BitUtil.minWidthNum(20) + BitUtil.minWidthNum(40),
                signed = false
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
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
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue(
                "800",
                10,
                BitUtil.minWidthNum(20) + BitUtil.minWidthNum(40),
                signed = false
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("-20 * -40")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue(
                "800",
                10,
                BitUtil.minWidthNum(20) + 1 + BitUtil.minWidthNum(40) + 1,
                signed = true
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)
    }

    @Test
    fun testDivide() = runBlocking {
        var test = SimpleLucidTester("40 / 8")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("5", 10, BitUtil.minWidthNum(40), signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("40 / 5")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("8", 10, BitUtil.minWidthNum(40), signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        // TODO: Check for warning with non-constant non-power 2 divisor
    }

    @Test
    fun testNegativeShift() = runBlocking {
        val test = SimpleLucidTester("-8 >> 2")
        val tree = test.parser.expr().also { test.context.walk(it) }
        val expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("0010", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testShift() = runBlocking {
        var test = SimpleLucidTester("40 >> 3")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("5", 10, BitUtil.minWidthNum(40), signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8\n >> 2")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("0010", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8 >>> 2")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("1110", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8 << 1")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("16", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8 << 1")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("10000", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("8 <<< 1")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("16", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("-8 <<< 1")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("10000", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testBitwise() = runBlocking {
        var test = SimpleLucidTester("b1101 & b1001")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("1001", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b001101 & b1001")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("001001", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(b001101) & \$signed(b1001)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("001001", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b1101 | b1001")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("1101", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b101101 | b1010")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("101111", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(b001101) | \$signed(b1001)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("111101", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b1101 ^ b1001")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("0100", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("b001101 ^ b1001")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("000100", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(b001101) ^ \$signed(b1001)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("110100", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testReduction() = runBlocking {
        var test = SimpleLucidTester("|b1001")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("|b0000")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("&b1001")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("&b1111")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("&b1x11")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.Bx, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("^b1001")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("^b1011")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testCompare() = runBlocking {
        var test = SimpleLucidTester("10 < 4")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 > 4")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("4 >= 10")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 >= 10")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 <= 4")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("10 <= 10")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testLogical() = runBlocking {
        var test = SimpleLucidTester("10 || 0")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        test = SimpleLucidTester("0 || 0")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        test = SimpleLucidTester("10 && 0")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        test = SimpleLucidTester("10 && 4")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assert(test.hasNoIssues)

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
    }

    @Test
    fun testTernary() = runBlocking {
        var test = SimpleLucidTester("10 ? 1 : 2")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!
        assert(test.hasNoIssues)

        assertEquals(
            BitListValue("1", 2, 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        test = SimpleLucidTester("10b0 ? 1 : 2")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!
        assert(test.hasNoIssues)

        assertEquals(
            BitListValue("2", 10, 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
    }

    @Test
    fun testInvert() = runBlocking {
        var test = SimpleLucidTester("!10")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("!0")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B1, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("~b101")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("010", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)
    }

    @Test
    fun testNegate() = runBlocking {
        var test = SimpleLucidTester("-20")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("101100", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("--20")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("20", 10, BitUtil.minWidthNum(20) + 1, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)
    }

    @Test
    fun testFunctions() = runBlocking {
        var test = SimpleLucidTester("\$signed(20)")
        var tree = test.parser.expr().also { test.context.walk(it) }
        var expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("20", 10, width = BitUtil.minWidthNum(20), signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$signed(-20)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("101100", 2, signed = true),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(7)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("3", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(8)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("3", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(9)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("4", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(0)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitValue(Bit.B0, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(1)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("0", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$clog2(129)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("8", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$pow(3,0)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("1", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$pow(2,4)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("16", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$reverse(b1100)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("0011", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$reverse({b1100, b0011})")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("1100", 2, signed = false),
                    BitListValue("0011", 2, signed = false)
                )
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$flatten(b1100)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("1100", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$flatten({b1100, b0011})")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("11000011", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        // TODO: Test flatten for structs

        test = SimpleLucidTester("\$build(b111000, 2)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue("000", 2, signed = false),
                    BitListValue("111", 2, signed = false)
                )
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$build(b11001001, 2, 2)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            ArrayValue(
                listOf(
                    ArrayValue(
                        listOf(
                            BitListValue("01", 2, signed = false),
                            BitListValue("10", 2, signed = false)
                        )
                    ),
                    ArrayValue(
                        listOf(
                            BitListValue("00", 2, signed = false),
                            BitListValue("11", 2, signed = false)
                        )
                    )
                )
            ),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)
        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$unsigned(20)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("20", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$unsigned(\$signed(-20))")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("101100", 2, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$cdiv(8, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("3", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$cdiv(9, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("3", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$cdiv(10, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("4", 10, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)

        test = SimpleLucidTester("\$resize(8, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("8", 10, 3, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoErrors)
        assert(test.hasNoWarnings) // should not warn about truncation

        test = SimpleLucidTester("\$resize(1, 3)")
        tree = test.parser.expr().also { test.context.walk(it) }
        expr = test.context.expr.resolve(tree)!!

        assertEquals(
            BitListValue("1", 10, 3, signed = false),
            expr.value
        )
        assertEquals(ExprType.Constant, expr.type)

        assert(test.hasNoIssues)
    }

    @Test
    fun simpleSignalTest() = runBlocking {
        val signal =
            Signal("mySig", SignalDirection.Both, null, BitListValue("110", 2, signed = false), ExprType.Dynamic)
        val test =
            SimpleLucidTester("mySig[2]", localSignalResolver = TestSignalResolver(signal))
        val exprCtx = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        assertEquals(BitValue(Bit.B1, signed = false).asDynamicExpr(), test.context.expr.resolve(exprCtx))
    }

    @Test
    fun rangeSignalTest() = runBlocking {
        val signal =
            Signal("mySig", SignalDirection.Both, null, BitListValue("1010", 2, signed = false), ExprType.Dynamic)
        val test =
            SimpleLucidTester("mySig[2:1]", localSignalResolver = TestSignalResolver(signal))
        val exprCtx = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        assertEquals(BitListValue("01", 2, signed = false).asDynamicExpr(), test.context.expr.resolve(exprCtx))
    }

    @Test
    fun rangeOutOfBoundsSignalTest() = runBlocking {
        val signal =
            Signal("mySig", SignalDirection.Both, null, BitListValue("1010", 2, signed = false), ExprType.Dynamic)
        val test =
            SimpleLucidTester("mySig[9:1]", localSignalResolver = TestSignalResolver(signal))
        test.parser.expr().also { test.context.walk(it) }

        assert(test.hasErrors)
        assert(test.hasNoWarnings)
    }

    @Test
    fun fixedPointTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fixed_point(3.14, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(50, 8, false).asConstExpr(),
            value
        )
    }

    @Test
    fun fixedPointIntTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fixed_point(10, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(160, 8, false).asConstExpr(),
            value
        )
    }

    @Test
    fun fixedPointOverflowTest() = runBlocking {
        val test =
            SimpleLucidTester("\$fixed_point(300.14, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoErrors)
        assert(test.hasWarnings)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(194, 8, false).asConstExpr(),
            value
        )
    }

    @Test
    fun cFixedPointTest() = runBlocking {
        val test =
            SimpleLucidTester("\$c_fixed_point(3.14, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(51, 8, false).asConstExpr(),
            value
        )
    }

    @Test
    fun fFixedPointTest() = runBlocking {
        val test =
            SimpleLucidTester("\$f_fixed_point(3.16, 8, 4)")
        val expr = test.parser.expr().also { test.context.walk(it) }

        assert(test.hasNoIssues)

        val value = test.context.resolve(expr)

        assertEquals(
            BitListValue(50, 8, false).asConstExpr(),
            value
        )
    }
}