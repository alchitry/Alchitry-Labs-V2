package basic

import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.asConstExpr
import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.ArrayValue
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.ProjectTester
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FunctionTests {

    @Test
    fun basicWidthTest() = runBlocking {
        val tester = SimpleLucidTester("\$width(8b1);")
        val ctx = tester.parser.expr().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        assertEquals(
            BitListValue(8, signed = false).asConstExpr(),
            tester.context.resolve(ctx)
        )
    }

    @Test
    fun multiDimWidthTest() = runBlocking {
        val tester = SimpleLucidTester("{\$width({8b1, 8b1},1),\$width({8b1, 8b1},0)}")
        val ctx = tester.parser.expr().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        val v = tester.context.resolve(ctx)
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue(2, 4, signed = false),
                    BitListValue(8, 4, signed = false),
                )
            ).asConstExpr(),
            v
        )
    }

    @Test
    fun reverseTestToVerilog() = runBlocking {
        val test = ProjectTester(
            $$"""
            module alchitry_top #(
                SAMPLE_SIZE=8d32
            )(
                input clk,
                output junk
            ) {
                const META_TEXT = $reverse (c{
                {8h40}, {SAMPLE_SIZE[7:0]}
                })
                
                always {
                    junk = clk + META_TEXT[0]
                }
            }
            """.trimIndent().toSourceFile()
        )

        val tree = test.fullParse(ExprEvalMode.Default)
        val text = tree.context.resolveSignal(tree.moduleContext, "META_TEXT") as? Signal
        assert(test.notationManager.hasNoErrors)
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue(64, 8, false),
                    BitListValue(32, 8, false)
                )
            ), text?.read()
        )
        val test2 = ProjectTester(*test.files)
        val verilog = test2.getVerilog(false)
        println(verilog)
        assert(verilog.isNotEmpty())
    }

    @Test
    fun paramSignalWidth() = runBlocking {
        val test = ProjectTester(
            $$"""
            module alchitry_top #(
                CLK_DIV = 8 : CLK_DIV >= 3
            )(
                input clk,
                output junk
            ) {
                .clk(clk) {
                    dff scl_reg[CLK_DIV]
                }
                const RISING_EDGE = $width(scl_reg.q)x{1b0}
                
                always {
                    scl_reg.d = scl_reg.q + 1
                    junk = RISING_EDGE
                }
            }
            """.trimIndent().toSourceFile()
        )
        val tree = test.fullParse(ExprEvalMode.Default)
        val text = tree.context.resolveSignal(tree.moduleContext, "RISING_EDGE") as? Signal
        assert(test.notationManager.hasNoErrors) { test.notationManager.getReport() }
        assertEquals(BitListValue(0, 8, false), text?.read())
    }
}