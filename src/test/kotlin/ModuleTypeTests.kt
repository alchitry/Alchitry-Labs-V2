import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.VerilogParser
import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.Module
import com.alchitry.labs2.parsers.hdl.types.Parameter
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitListWidth
import com.alchitry.labs2.parsers.hdl.values.BitWidth
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleTypeTests {

    @Test
    fun basicModuleTest() = runBlocking {
        val test = ProjectTester(
            """
                module myModule #(
                    CLK_FREQ ~ 100000000 : CLK_FREQ > 0,
                    MAX_CT = 100,
                    NEG_TEST = -100 : NEG_TEST < ${"\$"}signed(0)
                )(
                    input clk,
                    signed input rst,
                    output count[8],
                    inout a
                ) {
                }
            """.trimIndent().toSourceFile()
        )

        val tree = test.parseText()
        val module = test.moduleTypeParse(tree).first()



        assertEquals(
            Module(
                "myModule",
                mapOf(
                    "CLK_FREQ" to Parameter(
                        "CLK_FREQ",
                        BitListValue(100000000.toBigInteger()),
                        true,
                        (tree.first().second as LucidParser.SourceContext).module(0)?.paramList()?.paramDec(0)
                            ?.paramConstraint()?.expr()
                    ),
                    "MAX_CT" to Parameter(
                        "MAX_CT",
                        BitListValue(100.toBigInteger()),
                        false,
                        null
                    ),
                    "NEG_TEST" to Parameter(
                        "NEG_TEST",
                        BitListValue((-100).toBigInteger()),
                        false,
                        (tree.first().second as LucidParser.SourceContext).module(0)?.paramList()?.paramDec(2)
                            ?.paramConstraint()?.expr()
                    )
                ),
                mapOf(
                    "clk" to Port("clk", SignalDirection.Read, BitWidth, false, module.ports["clk"]!!.context),
                    "rst" to Port("rst", SignalDirection.Read, BitWidth, true, module.ports["rst"]!!.context),
                    "count" to Port(
                        "count",
                        SignalDirection.Write,
                        BitListWidth(8),
                        false,
                        module.ports["count"]!!.context
                    ),
                    "a" to Port("a", SignalDirection.Both, BitWidth, false, module.ports["a"]!!.context)
                ),
                (tree.first().second as LucidParser.SourceContext).module(0)!!,
                test.files.first()
            ),
            module
        )
    }

    @Test
    fun verilogClkWizModule() = runBlocking {
        val test = ProjectTester(
            """
            module clk_wiz_0(clk_out1, reset, locked, clk_in1)
            /* synthesis syn_black_box black_box_pad_pin="clk_out1,reset,locked,clk_in1" */;
              output clk_out1;
              input reset;
              output locked;
              input clk_in1;
            endmodule
        """.trimIndent().toSourceFile("test.v")
        )

        val tree = test.parseText()
        val module = test.moduleTypeParse(tree).first()
        assertEquals(
            Module(
                "clk_wiz_0",
                mapOf(),
                mapOf(
                    "clk_out1" to Port(
                        "clk_out1",
                        SignalDirection.Write,
                        BitWidth,
                        signed = false,
                        module.ports["clk_out1"]!!.context
                    ),
                    "reset" to Port(
                        "reset",
                        SignalDirection.Read,
                        BitWidth,
                        signed = false,
                        module.ports["reset"]!!.context
                    ),
                    "locked" to Port(
                        "locked",
                        SignalDirection.Write,
                        BitWidth,
                        signed = false,
                        module.ports["locked"]!!.context
                    ),
                    "clk_in1" to Port(
                        "clk_in1",
                        SignalDirection.Read,
                        BitWidth,
                        signed = false,
                        module.ports["clk_in1"]!!.context
                    )
                ),
                (tree.first().second as VerilogParser.Source_textContext).description(0)!!.module_declaration()!!,
                test.files.first()
            ), module
        )
    }
}