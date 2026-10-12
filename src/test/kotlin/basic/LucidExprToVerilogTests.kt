package basic

import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.evalVerilogExpr
import helpers.lucidExprToVerilog
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LucidExprToVerilogTests {
    @Test
    fun testConstantLiterals() {
        assertEquals("4'ha", lucidExprToVerilog("4b1010"))
        assertEquals("8'hff", lucidExprToVerilog("8hFF"))
        assertEquals("8'h98", lucidExprToVerilog("152"))
        assertEquals("1'h0", lucidExprToVerilog("0"))
        assertEquals("1'h1", lucidExprToVerilog("1"))
    }

    @Test
    fun testConstantOperations() {
        assertEquals("5'hf", lucidExprToVerilog("10 + 5"))
        assertEquals("4'h5", lucidExprToVerilog("4b1111 ^ 4b1010"))
        assertEquals("12'h50", lucidExprToVerilog("8h05 << 4"))
    }

    @Test
    fun testDynamicSignals() {
        val sigA = Signal("a", SignalDirection.Both, null, BitListValue("0000", 2, signed = false), ExprType.Dynamic)
        val sigB = Signal("b", SignalDirection.Both, null, BitListValue("0000", 2, signed = false), ExprType.Dynamic)

        assertEquals("a", lucidExprToVerilog("a", sigA))
        assertEquals("4'(~a)", lucidExprToVerilog("~a", sigA))
        assertEquals(
            "5'(a + b)",
            lucidExprToVerilog("a + b", sigA, sigB)
        )
    }

    @Test
    fun testConcatenationAndArrays() {
        val sigA = Signal("a", SignalDirection.Both, null, BitListValue("0000", 2, signed = false), ExprType.Dynamic)
        val sigB = Signal("b", SignalDirection.Both, null, BitListValue("0000", 2, signed = false), ExprType.Dynamic)

        assertEquals("{a, b}", lucidExprToVerilog("c{a, b}", sigA, sigB))
        assertEquals("{a, b}", lucidExprToVerilog("{a, b}", sigA, sigB))
    }

    @Test
    fun testAliases() {
        assertEquals("4'ha", lucidExprToVerilog("4b1010"))
    }

    @Test
    fun testIntegrationWithVerilogExprEvaluator() {
        val verilogCode = lucidExprToVerilog("4b1111 ^ 4b1010")
        val evaluated = evalVerilogExpr(verilogCode)
        assertEquals(BitListValue("0101", radix = 2, width = 4, signed = false), evaluated)
    }
}
