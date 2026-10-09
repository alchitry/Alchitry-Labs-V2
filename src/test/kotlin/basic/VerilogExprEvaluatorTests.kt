package basic

import com.alchitry.labs2.parsers.hdl.values.ArrayValue
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.evalVerilogExpr
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class VerilogExprEvaluatorTests {
    @Test
    fun testBasicLiterals() {
        assertEquals(BitListValue(BigInteger.valueOf(5), signed = true, width = 32), evalVerilogExpr("5"))
        assertEquals(BitListValue(BigInteger.valueOf(-5), signed = true, width = 32), evalVerilogExpr("-5"))
        assertEquals(BitListValue("1010", radix = 2, width = 4, signed = false), evalVerilogExpr("4'b1010"))
        assertEquals(BitListValue("1010", radix = 2, width = 4, signed = true), evalVerilogExpr("4'sb1010"))
        assertEquals(BitListValue("ff", radix = 16, width = 8, signed = false), evalVerilogExpr("8'hff"))
        assertEquals(BitListValue("10", radix = 10, width = 8, signed = false), evalVerilogExpr("8'd10"))
        assertEquals(BitListValue("77", radix = 8, width = 6, signed = false), evalVerilogExpr("6'o77"))
    }

    @Test
    fun testOperations() {
        assertEquals(BitListValue(BigInteger.valueOf(15), signed = true, width = 32), evalVerilogExpr("10 + 5"))
        assertEquals(BitListValue(BigInteger.valueOf(50), signed = true, width = 32), evalVerilogExpr("10 * 5"))
        assertEquals(BitListValue("0101", radix = 2, width = 4, signed = false), evalVerilogExpr("4'b1111 ^ 4'b1010"))
        assertEquals(BitListValue("1000", radix = 2, width = 4, signed = false), evalVerilogExpr("4'b0001 << 3"))
    }

    @Test
    fun testXZValues() {
        assertEquals(
            BitListValue("10xz", radix = 2, width = 4, signed = false),
            evalVerilogExpr("4'b10xz")
        )
    }

    @Test
    fun testArrays() {
        val context = "typedef logic [7:0] byte_t [2];"
        val result = evalVerilogExpr("byte_t'{8'h12, 8'h34}", extraContext = context)
        val expected = ArrayValue(
            listOf(
                BitListValue("12", radix = 16, width = 8, signed = false),
                BitListValue("34", radix = 16, width = 8, signed = false)
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun testExtraContext() {
        val context = """
            typedef struct packed {
                logic [7:0] a;
                logic [7:0] b;
            } pair_t;
            localparam pair_t MY_PAIR = '{8'hAA, 8'h55};
        """.trimIndent()
        val result = evalVerilogExpr("MY_PAIR.a + MY_PAIR.b", extraContext = context)
        assertEquals(BitListValue(BigInteger.valueOf(0xFF), signed = false, width = 8), result)
    }
}
