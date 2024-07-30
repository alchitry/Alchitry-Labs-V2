import com.alchitry.labs2.parsers.hdl.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.Module
import com.alchitry.labs2.parsers.hdl.types.Parameter
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.ports.Port
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitListWidth
import com.alchitry.labs2.parsers.hdl.values.BitWidth
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleTypeTests {

    @Test
    fun basicModuleTest() = runBlocking {
        val test = LucidTester(
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
                        tree.first().second.module(0)?.paramList()?.paramDec(0)?.paramConstraint()?.expr()
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
                        tree.first().second.module(0)?.paramList()?.paramDec(2)?.paramConstraint()?.expr()
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
                tree.first().second.module(0)!!,
                test.files.first()
            ),
            module
        )
    }


}