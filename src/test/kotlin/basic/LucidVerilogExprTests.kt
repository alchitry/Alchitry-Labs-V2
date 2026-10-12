package basic

import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.toVerilog
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.*
import helpers.SimpleLucidTester
import helpers.TestSignalResolver
import helpers.convertLucidExprToVerilog
import helpers.evalVerilogExpr
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LucidVerilogExprTests {

    private fun assertLucidMatchesVerilog(
        lucidExpr: String,
        vararg signals: Signal,
        extraVerilogContext: String = ""
    ) = runBlocking {
        val resolver = if (signals.isEmpty()) null else TestSignalResolver(*signals)
        val tester = SimpleLucidTester(lucidExpr, localSignalResolver = resolver)
        val exprCtx = tester.parser.expr()

        if (tester.parser.tokenStream.LA(1) != com.alchitry.labs2.parsers.grammar.LucidParser.Tokens.EOF) {
            error("Errors resolving Lucid expression \"$lucidExpr\":\nUnparsed trailing tokens remaining")
        }

        tester.context.walk(exprCtx)

        if (tester.context.notationCollector.hasErrors) {
            val errors =
                tester.context.notationCollector.getAllErrors().joinToString("\n") { it.message ?: it.toString() }
            error("Errors resolving Lucid expression \"$lucidExpr\":\n$errors")
        }

        val lucidValue = tester.context.expr.resolve(exprCtx)?.value
            ?: error("No value resolved for Lucid expression \"$lucidExpr\"")

        val verilogExpr = convertLucidExprToVerilog(lucidExpr, resolver)

        val svContext = buildString {
            if (extraVerilogContext.isNotBlank()) {
                appendLine(extraVerilogContext)
            }
            for (sig in signals) {
                val sigVal = sig.read(null)
                val isSigned = (sigVal as? BitListValue)?.signed == true
                val width: Int = sig.width.bitCount ?: 1
                val signedStr = if (isSigned) "signed " else ""
                val verilogVal = sigVal.toVerilog()
                appendLine("    localparam $signedStr[${width - 1}:0] ${sig.name} = $verilogVal;")
            }
        }

        val verilogValue = evalVerilogExpr(verilogExpr, svContext)

        assertEquals(
            normalizeValue(lucidValue),
            normalizeValue(verilogValue),
            "Mismatch for Lucid expr: \"$lucidExpr\" -> Verilog: \"$verilogExpr\""
        )
    }

    private fun normalizeValue(value: Value): Value {
        return when (value) {
            is BitValue -> BitListValue(listOf(value.bit), value.signed)
            is ArrayValue -> ArrayValue(value.elements.map { normalizeValue(it) })
            else -> value
        }
    }

    @Test
    fun testConstantLiterals() {
        assertLucidMatchesVerilog("4b1010")
        assertLucidMatchesVerilog("8b11001100")
        assertLucidMatchesVerilog("1b1")
        assertLucidMatchesVerilog("1b0")
        assertLucidMatchesVerilog("8hA5")
        assertLucidMatchesVerilog("16hDEAD")
        assertLucidMatchesVerilog("4hF")
        assertLucidMatchesVerilog("32h12345678")
        assertLucidMatchesVerilog("4d12")
        assertLucidMatchesVerilog("8d250")
        assertLucidMatchesVerilog("0")
        assertLucidMatchesVerilog("1")
        assertLucidMatchesVerilog("42")
        assertLucidMatchesVerilog("\"A\"")
        assertLucidMatchesVerilog($$"$flatten(\"Hi\")")
    }

    @Test
    fun testConstantArithmetic() {
        assertLucidMatchesVerilog("10 + 5")
        assertLucidMatchesVerilog("8h12 + 8h34")
        assertLucidMatchesVerilog("16h1234 + 16h5678")
        assertLucidMatchesVerilog("8h12 + 4h5")
        assertLucidMatchesVerilog("4hA + 8h20")
        assertLucidMatchesVerilog("16h1234 + 8h56")
        assertLucidMatchesVerilog("8h12 + 16h3456")
        assertLucidMatchesVerilog("10 - 5")
        assertLucidMatchesVerilog("8h50 - 8h20")
        assertLucidMatchesVerilog("8h50 - 4h2")
        assertLucidMatchesVerilog("16h1000 - 8h20")
        assertLucidMatchesVerilog("10 * 5")
        assertLucidMatchesVerilog("8h12 * 8h04")
        assertLucidMatchesVerilog("4h5 * 8h04")
        assertLucidMatchesVerilog("8h04 * 4h5")
        assertLucidMatchesVerilog("100 / 5")
        assertLucidMatchesVerilog("8h64 / 8h08")
        assertLucidMatchesVerilog("8h64 / 4h8")
        assertLucidMatchesVerilog("16h0064 / 4h8")
        assertLucidMatchesVerilog("-8d10")
        assertLucidMatchesVerilog("(8h10 + 8h20) * 8h02")
        assertLucidMatchesVerilog("(8h10 + 4h5) * 4h2")
        assertLucidMatchesVerilog("8hFF + 8h01")
        assertLucidMatchesVerilog("8hFF + 4h1")
        assertLucidMatchesVerilog("16hFFFF + 16h0001")
        assertLucidMatchesVerilog("16hFFFF + 8h01")
    }

    @Test
    fun testConstantBitwise() {
        assertLucidMatchesVerilog("~4b1010")
        assertLucidMatchesVerilog("~8hAA")
        assertLucidMatchesVerilog("4b1100 & 4b1010")
        assertLucidMatchesVerilog("8hF0 & 8h55")
        assertLucidMatchesVerilog("8hF0 & 4h5")
        assertLucidMatchesVerilog("4h5 & 8hF0")
        assertLucidMatchesVerilog("4b1100 & 8b00001010")
        assertLucidMatchesVerilog("4b1100 | 4b1010")
        assertLucidMatchesVerilog("8hF0 | 8h55")
        assertLucidMatchesVerilog("8hF0 | 4h5")
        assertLucidMatchesVerilog("4hA | 8h55")
        assertLucidMatchesVerilog("4b1100 ^ 4b1010")
        assertLucidMatchesVerilog("8hFF ^ 8hAA")
        assertLucidMatchesVerilog("8hFF ^ 4hA")
        assertLucidMatchesVerilog("4hA ^ 8hFF")
        assertLucidMatchesVerilog("4b1100 ^~ 4b1010")
        assertLucidMatchesVerilog("8hAA ^~ 8h55")
        assertLucidMatchesVerilog("8hAA ^~ 4h5")
        assertLucidMatchesVerilog("4h5 ^~ 8hAA")
        assertLucidMatchesVerilog("(8hAA & 8h0F) | (8h55 & 8hF0)")
        assertLucidMatchesVerilog("(8hAA & 4hF) | (4h5 & 8hF0)")
    }

    @Test
    fun testConstantLogical() {
        assertLucidMatchesVerilog("!1b1")
        assertLucidMatchesVerilog("!1b0")
        assertLucidMatchesVerilog("!0")
        assertLucidMatchesVerilog("!1")
        assertLucidMatchesVerilog("1b1 && 1b1")
        assertLucidMatchesVerilog("1b1 && 1b0")
        assertLucidMatchesVerilog("1b0 || 1b1")
        assertLucidMatchesVerilog("1b0 || 1b0")
        assertLucidMatchesVerilog("!1b0 && (1b1 || 1b0)")
    }

    @Test
    fun testConstantComparisons() {
        assertLucidMatchesVerilog("8h12 == 8h12")
        assertLucidMatchesVerilog("8h12 == 8h34")
        assertLucidMatchesVerilog("8h12 != 8h34")
        assertLucidMatchesVerilog("8h12 != 8h12")
        assertLucidMatchesVerilog("8h05 == 4h5")
        assertLucidMatchesVerilog("8h12 == 4h2")
        assertLucidMatchesVerilog("8h05 != 4h5")
        assertLucidMatchesVerilog("8h12 != 4h5")
        assertLucidMatchesVerilog("8h10 < 8h20")
        assertLucidMatchesVerilog("8h20 < 8h10")
        assertLucidMatchesVerilog("8h10 < 4h5")
        assertLucidMatchesVerilog("4h5 < 8h10")
        assertLucidMatchesVerilog("8h10 <= 8h10")
        assertLucidMatchesVerilog("8h10 <= 8h20")
        assertLucidMatchesVerilog("8h05 <= 4h5")
        assertLucidMatchesVerilog("4h5 <= 8h10")
        assertLucidMatchesVerilog("8h20 > 8h10")
        assertLucidMatchesVerilog("8h10 > 8h20")
        assertLucidMatchesVerilog("8h10 > 4h5")
        assertLucidMatchesVerilog("4h5 > 8h10")
        assertLucidMatchesVerilog("8h20 >= 8h20")
        assertLucidMatchesVerilog("8h10 >= 8h20")
        assertLucidMatchesVerilog("8h05 >= 4h5")
        assertLucidMatchesVerilog("4h5 >= 8h05")
    }

    @Test
    fun testConstantShifts() {
        assertLucidMatchesVerilog("8h01 << 3")
        assertLucidMatchesVerilog("8h80 >> 3")
        assertLucidMatchesVerilog("8hF0 >> 4")
        assertLucidMatchesVerilog("8h0F << 4")
        assertLucidMatchesVerilog("8h80 >>> 2")
        assertLucidMatchesVerilog("16h8000 >>> 4")
        assertLucidMatchesVerilog("8h01 << 4h3")
        assertLucidMatchesVerilog("8h80 >> 4h3")
        assertLucidMatchesVerilog("8hF0 >> 4h4")
        assertLucidMatchesVerilog("4hF << 8h04")
        assertLucidMatchesVerilog("8h80 >>> 4h2")
        assertLucidMatchesVerilog("16h8000 >>> 8h04")
    }

    @Test
    fun testConstantConcatAndDup() {
        assertLucidMatchesVerilog("c{4b1010, 4b0101}")
        assertLucidMatchesVerilog("c{8hAB, 8hCD, 8hEF}")
        assertLucidMatchesVerilog("4x{2b10}")
        assertLucidMatchesVerilog("2x{4hA}")
        assertLucidMatchesVerilog("8x{1b1}")
        assertLucidMatchesVerilog("c{2x{2b01}, 4x{1b0}}")
        assertLucidMatchesVerilog("c{4b1111, 2x{2b00}}")
    }

    @Test
    fun testConstantBitSelection() {
        val constA = Signal(
            "CONST_A",
            SignalDirection.Both,
            null,
            BitListValue("10100101", 2, width = 8, signed = false),
            ExprType.Constant
        )
        assertLucidMatchesVerilog("CONST_A[0]", constA)
        assertLucidMatchesVerilog("CONST_A[7]", constA)
        assertLucidMatchesVerilog("CONST_A[3:0]", constA)
        assertLucidMatchesVerilog("CONST_A[7:4]", constA)
        assertLucidMatchesVerilog("CONST_A[5:2]", constA)
    }

    @Test
    fun testInvalidConstantLiteralBitSelectionFails() {
        org.junit.jupiter.api.assertThrows<IllegalStateException> {
            assertLucidMatchesVerilog("8hA5[0]")
        }
        org.junit.jupiter.api.assertThrows<IllegalStateException> {
            assertLucidMatchesVerilog("c{4hA, 4h5}[3:0]")
        }
    }

    @Test
    fun testConstantTernary() {
        assertLucidMatchesVerilog("1b1 ? 8hAA : 8h55")
        assertLucidMatchesVerilog("1b0 ? 8hAA : 8h55")
        assertLucidMatchesVerilog("1b1 ? 8hAA : 4h5")
        assertLucidMatchesVerilog("1b0 ? 8hAA : 4h5")
        assertLucidMatchesVerilog("1b1 ? 4hA : 8h55")
        assertLucidMatchesVerilog("1b0 ? 4hA : 8h55")
        assertLucidMatchesVerilog("(8h10 > 8h05) ? 8hFF : 8h00")
        assertLucidMatchesVerilog("(8h05 > 8h10) ? 8hFF : 8h00")
        assertLucidMatchesVerilog("(8h10 > 4h5) ? 8hFF : 4h0")
        assertLucidMatchesVerilog("(4h5 > 8h10) ? 4hF : 8h00")
        assertLucidMatchesVerilog("1b0 ? 8h11 : (1b1 ? 8h22 : 8h33)")
        assertLucidMatchesVerilog("1b0 ? 4h1 : (1b1 ? 8h22 : 4h3)")
    }

    @Test
    fun testConstantFunctions() {
        assertLucidMatchesVerilog($$"$reverse(8b10110001)")
        assertLucidMatchesVerilog($$"$reverse(4hA)")
        assertLucidMatchesVerilog($$"$flatten({8h12, 8h34})")
        assertLucidMatchesVerilog($$"$clog2(8)")
        assertLucidMatchesVerilog($$"$clog2(9)")
        assertLucidMatchesVerilog($$"$clog2(1)")
        assertLucidMatchesVerilog($$"$clog2(16)")
        assertLucidMatchesVerilog($$"$clog2(100)")
        assertLucidMatchesVerilog($$"$pow(2, 8)")
        assertLucidMatchesVerilog($$"$pow(3, 4)")
        assertLucidMatchesVerilog($$"$pow(2, 0)")
        assertLucidMatchesVerilog($$"$signed(8hFE)")
        assertLucidMatchesVerilog($$"$unsigned(8hFE)")
        assertLucidMatchesVerilog($$"$width(8hA5)")
        assertLucidMatchesVerilog($$"$width(16h1234)")
        assertLucidMatchesVerilog($$"$width(4b1010)")
    }

    @Test
    fun testDynamicArithmetic() {
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("00101010", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigB = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("00010101", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigA4 = Signal(
            "a4",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = false),
            ExprType.Dynamic
        )
        val sigB4 = Signal(
            "b4",
            SignalDirection.Both,
            null,
            BitListValue("1010", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("a + b", sigA, sigB)
        assertLucidMatchesVerilog("a + a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 + a", sigA4, sigA)
        assertLucidMatchesVerilog("a - b", sigA, sigB)
        assertLucidMatchesVerilog("a - a4", sigA, sigA4)
        assertLucidMatchesVerilog("a * b", sigA, sigB)
        assertLucidMatchesVerilog("a * a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 * a", sigA4, sigA)
        assertLucidMatchesVerilog("a / b", sigA, sigB)
        assertLucidMatchesVerilog("a / a4", sigA, sigA4)
        assertLucidMatchesVerilog("-a", sigA)
        assertLucidMatchesVerilog("-a4", sigA4)
        assertLucidMatchesVerilog("(a + b) * 8h02", sigA, sigB)
        assertLucidMatchesVerilog("(a + a4) * 4h2", sigA, sigA4)
        assertLucidMatchesVerilog("(a4 + b4) * 8h02", sigA4, sigB4)
    }

    @Test
    fun testDynamicBitwise() {
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("11001100", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigB = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("10101010", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigA4 = Signal(
            "a4",
            SignalDirection.Both,
            null,
            BitListValue("1010", 2, width = 4, signed = false),
            ExprType.Dynamic
        )
        val sigB4 = Signal(
            "b4",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("~a", sigA)
        assertLucidMatchesVerilog("~a4", sigA4)
        assertLucidMatchesVerilog("a & b", sigA, sigB)
        assertLucidMatchesVerilog("a & a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 & a", sigA4, sigA)
        assertLucidMatchesVerilog("a | b", sigA, sigB)
        assertLucidMatchesVerilog("a | a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 | a", sigA4, sigA)
        assertLucidMatchesVerilog("a ^ b", sigA, sigB)
        assertLucidMatchesVerilog("a ^ a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 ^ a", sigA4, sigA)
        assertLucidMatchesVerilog("a ^~ b", sigA, sigB)
        assertLucidMatchesVerilog("a ^~ a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 ^~ a", sigA4, sigA)
        assertLucidMatchesVerilog("(a & 8h0F) | (b & 8hF0)", sigA, sigB)
        assertLucidMatchesVerilog("(a & 4hF) | (a4 & 8hF0)", sigA, sigA4)
        assertLucidMatchesVerilog("~(a & ~b)", sigA, sigB)
        assertLucidMatchesVerilog("~(a & ~a4)", sigA, sigA4)
    }

    @Test
    fun testDynamicLogicalAndRelational() {
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("00001010", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigB = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("00010100", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigC = Signal(
            "c",
            SignalDirection.Both,
            null,
            BitListValue("00001010", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigA4 = Signal(
            "a4",
            SignalDirection.Both,
            null,
            BitListValue("1010", 2, width = 4, signed = false),
            ExprType.Dynamic
        )
        val sigB4 = Signal(
            "b4",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("a == b", sigA, sigB)
        assertLucidMatchesVerilog("a == c", sigA, sigC)
        assertLucidMatchesVerilog("a == a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 == a", sigA4, sigA)
        assertLucidMatchesVerilog("b == a4", sigB, sigA4)
        assertLucidMatchesVerilog("a != b", sigA, sigB)
        assertLucidMatchesVerilog("a != c", sigA, sigC)
        assertLucidMatchesVerilog("a != a4", sigA, sigA4)
        assertLucidMatchesVerilog("b != a4", sigB, sigA4)
        assertLucidMatchesVerilog("a < b", sigA, sigB)
        assertLucidMatchesVerilog("b < a", sigA, sigB)
        assertLucidMatchesVerilog("a4 < b", sigA4, sigB)
        assertLucidMatchesVerilog("b < a4", sigB, sigA4)
        assertLucidMatchesVerilog("a <= c", sigA, sigC)
        assertLucidMatchesVerilog("a <= b", sigA, sigB)
        assertLucidMatchesVerilog("a <= a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 <= a", sigA4, sigA)
        assertLucidMatchesVerilog("a4 <= b", sigA4, sigB)
        assertLucidMatchesVerilog("b > a", sigA, sigB)
        assertLucidMatchesVerilog("a > b", sigA, sigB)
        assertLucidMatchesVerilog("b > a4", sigB, sigA4)
        assertLucidMatchesVerilog("a4 > b", sigA4, sigB)
        assertLucidMatchesVerilog("a >= c", sigA, sigC)
        assertLucidMatchesVerilog("a >= b", sigA, sigB)
        assertLucidMatchesVerilog("a >= a4", sigA, sigA4)
        assertLucidMatchesVerilog("a4 >= a", sigA4, sigA)
        assertLucidMatchesVerilog("(a < b) && (a == c)", sigA, sigB, sigC)
        assertLucidMatchesVerilog("(a4 < b) && (a == a4)", sigA4, sigB, sigA)
        assertLucidMatchesVerilog("(a > b) || (a == c)", sigA, sigB, sigC)
        assertLucidMatchesVerilog("(b > a4) || (a4 == b4)", sigB, sigA4, sigB4)
    }

    @Test
    fun testDynamicShifts() {
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("00001111", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigShift4 = Signal(
            "shift4",
            SignalDirection.Both,
            null,
            BitListValue("0010", 2, width = 4, signed = false),
            ExprType.Dynamic
        )
        val sigShift8 = Signal(
            "shift8",
            SignalDirection.Both,
            null,
            BitListValue("00000010", 2, width = 8, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("a << 2", sigA)
        assertLucidMatchesVerilog("a >> 2", sigA)
        assertLucidMatchesVerilog("a << 4", sigA)
        assertLucidMatchesVerilog("a >> 4", sigA)
        assertLucidMatchesVerilog("a << 4h2", sigA)
        assertLucidMatchesVerilog("a << 4h4", sigA)
        assertLucidMatchesVerilog("a << 8h02", sigA)
        assertLucidMatchesVerilog("a << 8h04", sigA)
        assertLucidMatchesVerilog("a >> shift4", sigA, sigShift4)
        assertLucidMatchesVerilog("a >> shift8", sigA, sigShift8)
    }

    @Test
    fun testDynamicSignedOperations() {
        val sigSignedA = Signal(
            "sa",
            SignalDirection.Both,
            null,
            BitListValue("11110110", 2, width = 8, signed = true),
            ExprType.Dynamic
        )
        val sigSignedB = Signal(
            "sb",
            SignalDirection.Both,
            null,
            BitListValue("00000101", 2, width = 8, signed = true),
            ExprType.Dynamic
        )
        val sigSignedA4 = Signal(
            "sa4",
            SignalDirection.Both,
            null,
            BitListValue("1010", 2, width = 4, signed = true),
            ExprType.Dynamic
        ) // -6
        val sigSignedB4 = Signal(
            "sb4",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = true),
            ExprType.Dynamic
        ) // 5

        assertLucidMatchesVerilog("sa + sb", sigSignedA, sigSignedB)
        assertLucidMatchesVerilog("sa + sb4", sigSignedA, sigSignedB4)
        assertLucidMatchesVerilog("sa4 + sb", sigSignedA4, sigSignedB)
        assertLucidMatchesVerilog("sa - sb", sigSignedA, sigSignedB)
        assertLucidMatchesVerilog("sa - sb4", sigSignedA, sigSignedB4)
        assertLucidMatchesVerilog("sb - sa4", sigSignedB, sigSignedA4)
        assertLucidMatchesVerilog("sa * sb", sigSignedA, sigSignedB)
        assertLucidMatchesVerilog("sa * sb4", sigSignedA, sigSignedB4)
        assertLucidMatchesVerilog("sa4 * sb", sigSignedA4, sigSignedB)
        assertLucidMatchesVerilog("sa < sb", sigSignedA, sigSignedB)
        assertLucidMatchesVerilog("sa4 < sb", sigSignedA4, sigSignedB)
        assertLucidMatchesVerilog("sa < sb4", sigSignedA, sigSignedB4)
        assertLucidMatchesVerilog("sa > sb", sigSignedA, sigSignedB)
        assertLucidMatchesVerilog("sb > sa4", sigSignedB, sigSignedA4)
        assertLucidMatchesVerilog(
            "sa4 == sigSignedA4_alias",
            sigSignedA4,
            Signal(
                "sigSignedA4_alias",
                SignalDirection.Both,
                null,
                BitListValue("1010", 2, width = 4, signed = true),
                ExprType.Dynamic
            )
        )
        assertLucidMatchesVerilog("sa4 != sb", sigSignedA4, sigSignedB)
        assertLucidMatchesVerilog("sa >>> 2", sigSignedA)
        assertLucidMatchesVerilog("sa >>> 1", sigSignedA)
        assertLucidMatchesVerilog("sa >>> sb4", sigSignedA, sigSignedB4)
        assertLucidMatchesVerilog("-sa", sigSignedA)
        assertLucidMatchesVerilog("-sa4", sigSignedA4)
    }

    @Test
    fun testDynamicConcatAndSelection() {
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("1010", 2, width = 4, signed = false),
            ExprType.Dynamic
        )
        val sigB = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("c{a, b}", sigA, sigB)
        assertLucidMatchesVerilog("c{a, 4hF, b}", sigA, sigB)
        assertLucidMatchesVerilog("2x{a}", sigA)
        assertLucidMatchesVerilog("4x{a[0]}", sigA)
        assertLucidMatchesVerilog("a[0]", sigA)
        assertLucidMatchesVerilog("a[3]", sigA)
        assertLucidMatchesVerilog("a[2:1]", sigA)
        assertLucidMatchesVerilog("c{a[3:2], b[1:0]}", sigA, sigB)
    }

    @Test
    fun testDynamicTernary() {
        val sel =
            Signal("sel", SignalDirection.Both, null, BitListValue("1", 2, width = 1, signed = false), ExprType.Dynamic)
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("11110000", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigB = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("00001111", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigA4 = Signal(
            "a4",
            SignalDirection.Both,
            null,
            BitListValue("1010", 2, width = 4, signed = false),
            ExprType.Dynamic
        )
        val sigB4 = Signal(
            "b4",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("sel ? a : b", sel, sigA, sigB)
        assertLucidMatchesVerilog("!sel ? a : b", sel, sigA, sigB)
        assertLucidMatchesVerilog("sel ? a4 : b", sel, sigA4, sigB)
        assertLucidMatchesVerilog("!sel ? a4 : b", sel, sigA4, sigB)
        assertLucidMatchesVerilog("sel ? a : b4", sel, sigA, sigB4)
        assertLucidMatchesVerilog("!sel ? a : b4", sel, sigA, sigB4)
        assertLucidMatchesVerilog("(a > b) ? a : b", sigA, sigB)
        assertLucidMatchesVerilog("(a > a4) ? a : a4", sigA, sigA4)
        assertLucidMatchesVerilog("(a4 > a) ? a4 : a", sigA4, sigA)
    }

    @Test
    fun testComplexExpressions() {
        val sigA = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("10110010", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigB = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("01001101", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigC = Signal(
            "c",
            SignalDirection.Both,
            null,
            BitListValue("00000111", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val sigD4 = Signal(
            "d4",
            SignalDirection.Both,
            null,
            BitListValue("0101", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        assertLucidMatchesVerilog("((a & 8h0F) << 4) | ((a & 8hF0) >> 4)", sigA)
        assertLucidMatchesVerilog("((a & 4hF) << 4) | ((a & 8hF0) >> d4)", sigA, sigD4)
        assertLucidMatchesVerilog("(a + b) ^ c", sigA, sigB, sigC)
        assertLucidMatchesVerilog("(a + d4) ^ c", sigA, sigD4, sigC)
        assertLucidMatchesVerilog("c{a[7:4] + b[7:4], a[3:0] ^ b[3:0]}", sigA, sigB)
        assertLucidMatchesVerilog("c{a[7:4] + d4, a[3:0] ^ d4}", sigA, sigD4)
    }

    @Test
    fun testSignedMixedComparisons() {
        val posA = Signal(
            "posA",
            SignalDirection.Both,
            null,
            BitListValue("00001111", 2, width = 8, signed = true),
            ExprType.Dynamic
        )
        val negB = Signal(
            "negB",
            SignalDirection.Both,
            null,
            BitListValue("11110000", 2, width = 8, signed = true),
            ExprType.Dynamic
        )
        val posA4 = Signal(
            "posA4",
            SignalDirection.Both,
            null,
            BitListValue("0111", 2, width = 4, signed = true),
            ExprType.Dynamic
        ) // +7
        val negB4 = Signal(
            "negB4",
            SignalDirection.Both,
            null,
            BitListValue("1000", 2, width = 4, signed = true),
            ExprType.Dynamic
        ) // -8
        val posA8 = Signal(
            "posA8",
            SignalDirection.Both,
            null,
            BitListValue("00000111", 2, width = 8, signed = true),
            ExprType.Dynamic
        ) // +7

        assertLucidMatchesVerilog("posA > negB", posA, negB)
        assertLucidMatchesVerilog("negB < posA", posA, negB)
        assertLucidMatchesVerilog("posA >= negB", posA, negB)
        assertLucidMatchesVerilog("negB <= posA", posA, negB)
        assertLucidMatchesVerilog("negB != posA", posA, negB)
        assertLucidMatchesVerilog("negB == negB", negB)

        assertLucidMatchesVerilog("posA4 > negB", posA4, negB)
        assertLucidMatchesVerilog("negB < posA4", negB, posA4)
        assertLucidMatchesVerilog("posA4 >= negB", posA4, negB)
        assertLucidMatchesVerilog("negB <= posA4", negB, posA4)
        assertLucidMatchesVerilog("posA4 == posA8", posA4, posA8)
        assertLucidMatchesVerilog("posA4 != posA", posA4, posA)
        assertLucidMatchesVerilog("negB4 > negB", negB4, negB)
        assertLucidMatchesVerilog("negB < negB4", negB, negB4)
    }

    @Test
    fun testALUOperations() {
        val op =
            Signal("op", SignalDirection.Both, null, BitListValue("00", 2, width = 2, signed = false), ExprType.Dynamic)

        val a = Signal(
            "a",
            SignalDirection.Both,
            null,
            BitListValue("00111100", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val b = Signal(
            "b",
            SignalDirection.Both,
            null,
            BitListValue("00001111", 2, width = 8, signed = false),
            ExprType.Dynamic
        )
        val b4 = Signal(
            "b4",
            SignalDirection.Both,
            null,
            BitListValue("1111", 2, width = 4, signed = false),
            ExprType.Dynamic
        )

        val aluExpr = "op == 2b00 ? (a + b) : (op == 2b01 ? (a - b) : (op == 2b10 ? (a & b) : (a | b)))"
        assertLucidMatchesVerilog(aluExpr, op, a, b)

        val aluMixedExpr = "op == 2b00 ? (a + b4) : (op == 2b01 ? (a - b4) : (op == 2b10 ? (a & b4) : (a | b4)))"
        assertLucidMatchesVerilog(aluMixedExpr, op, a, b4)
    }

    private fun assertKnownExprWidthMatchesVerilog(
        lucidExpr: String,
        vararg signals: Signal,
        extraVerilogContext: String = ""
    ) = runBlocking {
        val concreteResolver = if (signals.isEmpty()) null else TestSignalResolver(*signals)
        val expectedTester = SimpleLucidTester(lucidExpr, localSignalResolver = concreteResolver)
        val expectedExprCtx = expectedTester.parser.expr()

        if (expectedTester.parser.tokenStream.LA(1) != com.alchitry.labs2.parsers.grammar.LucidParser.Tokens.EOF) {
            error("Errors resolving expected Lucid expression \"$lucidExpr\":\nUnparsed trailing tokens remaining")
        }

        expectedTester.context.walk(expectedExprCtx)

        if (expectedTester.context.notationCollector.hasErrors) {
            val errors =
                expectedTester.context.notationCollector.getAllErrors()
                    .joinToString("\n") { it.message ?: it.toString() }
            error("Errors resolving expected Lucid expression \"$lucidExpr\":\n$errors")
        }

        val expectedLucidValue = expectedTester.context.expr.resolve(expectedExprCtx)?.value
            ?: error("No value resolved for expected Lucid expression \"$lucidExpr\"")

        val expectedWidth = expectedLucidValue.width.bitCount
            ?: error("Expected Lucid value for \"$lucidExpr\" has no bit count: $expectedLucidValue")

        val unknownSignals = signals.map { sig ->
            Signal(
                sig.name,
                sig.direction,
                null,
                UndefinedValue(UndefinedSimpleWidth()),
                ExprType.Known
            )
        }.toTypedArray()

        val widthLucidExpr = $$"$width($$lucidExpr)"
        val resolver = if (unknownSignals.isEmpty()) null else TestSignalResolver(*unknownSignals)
        val tester = SimpleLucidTester(widthLucidExpr, localSignalResolver = resolver)
        val exprCtx = tester.parser.expr()

        if (tester.parser.tokenStream.LA(1) != com.alchitry.labs2.parsers.grammar.LucidParser.Tokens.EOF) {
            error("Errors resolving Lucid expression \"$widthLucidExpr\":\nUnparsed trailing tokens remaining")
        }

        tester.context.walk(exprCtx)

        if (tester.context.notationCollector.hasErrors) {
            val errors =
                tester.context.notationCollector.getAllErrors().joinToString("\n") { it.message ?: it.toString() }
            error("Errors resolving Lucid expression \"$widthLucidExpr\":\n$errors")
        }

        val lucidValue = tester.context.expr.resolve(exprCtx)?.value
            ?: error("No value resolved for Lucid expression \"$widthLucidExpr\"")

        when (lucidValue) {
            is UndefinedValue -> assertEquals(
                UndefinedSimpleWidth(),
                lucidValue.width,
                "Expected UndefinedValue with UndefinedSimpleWidth for Lucid expression \"$widthLucidExpr\""
            )

            is SimpleValue -> assertEquals(
                expectedWidth,
                lucidValue.toBigInt()?.toInt(),
                "Lucid value mismatch for Lucid expression \"$widthLucidExpr\""
            )

            else -> error("Unexpected Lucid value type: $lucidValue")
        }

        val verilogExpr = convertLucidExprToVerilog(widthLucidExpr, resolver)

        val svContext = buildString {
            if (extraVerilogContext.isNotBlank()) {
                appendLine(extraVerilogContext)
            }
            for (sig in signals) {
                val sigVal = sig.read(null)
                val isSigned = (sigVal as? BitListValue)?.signed == true
                val width: Int = sig.width.bitCount ?: 1
                val signedStr = if (isSigned) "signed " else ""
                val verilogVal = sigVal.toVerilog()
                appendLine("    localparam $signedStr[${width - 1}:0] ${sig.name} = $verilogVal;")
            }
        }

        val verilogValue = evalVerilogExpr(verilogExpr, svContext)

        val actualWidth = (verilogValue as? SimpleValue)?.toBigInt()?.toInt()
            ?: error("Verilog expression \"$verilogExpr\" did not evaluate to a simple numeric value, got: $verilogValue")

        assertEquals(
            expectedWidth,
            actualWidth,
            "Width mismatch for Lucid expr: \"$widthLucidExpr\" -> Verilog: \"$verilogExpr\""
        )
    }

    @Test
    fun testUndefinedWidthKnownExpressions() {
        val paramA = Signal(
            "PARAM_A",
            SignalDirection.Both,
            null,
            BitListValue("10100101", 2, width = 8, signed = false),
            ExprType.Constant
        )
        val paramB = Signal(
            "PARAM_B",
            SignalDirection.Both,
            null,
            BitListValue("0011", 2, width = 4, signed = false),
            ExprType.Constant
        )
        val paramC = Signal(
            "PARAM_C",
            SignalDirection.Both,
            null,
            BitListValue("0010", 2, width = 4, signed = false),
            ExprType.Constant
        )

        // Single parameter widths
        assertKnownExprWidthMatchesVerilog("PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_B", paramB)

        // Arithmetic operations
        assertKnownExprWidthMatchesVerilog("PARAM_A + PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A + PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A + 8h12", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A + 4h5", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_B + 8h12", paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A - PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A - 8h10", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A * PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A * PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A * 4h2", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A / PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A / 4h2", paramA)
        assertKnownExprWidthMatchesVerilog("-PARAM_A", paramA)

        // Bitwise operations
        assertKnownExprWidthMatchesVerilog("~PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A & PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A | PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A ^ PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A ^~ PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A & 8h0F", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A | 8hF0", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A ^ 8hAA", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_B ^ 4hF", paramB)
        assertKnownExprWidthMatchesVerilog("~(PARAM_A & ~PARAM_B)", paramA, paramB)

        // Shifts
        assertKnownExprWidthMatchesVerilog("PARAM_A << 2", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A << 4", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A >> 2", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A >> 4", paramA)
        assertKnownExprWidthMatchesVerilog("PARAM_A >> PARAM_B", paramA, paramB)

        // Concatenation and Duplication
        assertKnownExprWidthMatchesVerilog("c{PARAM_A, PARAM_B}", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("c{PARAM_A, 4hF, PARAM_B}", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("c{PARAM_A, 8hAA}", paramA)
        assertKnownExprWidthMatchesVerilog("2x{PARAM_A}", paramA)
        assertKnownExprWidthMatchesVerilog("3x{PARAM_B}", paramB)

        // Ternary
        assertKnownExprWidthMatchesVerilog("PARAM_A ? PARAM_A : PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_B ? PARAM_A : 8h55", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A == 8h00 ? PARAM_A : PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A > PARAM_B ? PARAM_A : 8h00", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_B > 0 ? PARAM_B : 4h0", paramB)

        // Logical, Relational, and Reduction
        assertKnownExprWidthMatchesVerilog("PARAM_A == PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A != PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A < PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A > PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A <= PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A >= PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A && PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("PARAM_A || PARAM_B", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("!PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("&PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("|PARAM_A", paramA)
        assertKnownExprWidthMatchesVerilog("^PARAM_A", paramA)

        // Functions
        assertKnownExprWidthMatchesVerilog($$"$signed(PARAM_A)", paramA)
        assertKnownExprWidthMatchesVerilog($$"$unsigned(PARAM_A)", paramA)
        assertKnownExprWidthMatchesVerilog($$"$flatten(PARAM_A)", paramA)
        assertKnownExprWidthMatchesVerilog($$"$resize(PARAM_A, 16)", paramA)
        assertKnownExprWidthMatchesVerilog($$"$resize(PARAM_B, 32)", paramB)

        // Complex expressions
        assertKnownExprWidthMatchesVerilog("((PARAM_A & 8h0F) << 4) | ((PARAM_A & 8hF0) >> 4)", paramA)
        assertKnownExprWidthMatchesVerilog("(PARAM_A + PARAM_B) ^ 9h1FF", paramA, paramB)
        assertKnownExprWidthMatchesVerilog("(PARAM_A + PARAM_B) * PARAM_C", paramA, paramB, paramC)
    }
}
