import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.signals.Module
import com.alchitry.labs.parsers.lucidv2.signals.Parameter
import com.alchitry.labs.parsers.lucidv2.signals.Port
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitListWidth
import com.alchitry.labs.parsers.lucidv2.values.BitWidth
import helpers.LucidModuleTester
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleTypeTests {

    @Test
    fun basicModuleTest() {
        val test = LucidModuleTester(
            """
                module myModule #(
                    CLK_FREQ : CLK_FREQ > 0,
                    MAX_CT = 100,
                    NEG_TEST = -100 : NEG_TEST < ${"\$"}signed(0)
                )(
                    input clk,
                    signed input rst,
                    output count[8],
                    inout test
                ) {
                }
            """.trimIndent()
        )

        val errorCollector = ErrorCollector()
        val tree = test.parseText(errorCollector)
        val module = test.moduleTypeParse(errorCollector, tree)

        assertEquals(
            Module(
                "myModule",
                mapOf(
                    "CLK_FREQ" to Parameter(
                        "CLK_FREQ",
                        null,
                        tree.module(0).paramList().paramDec(0).paramConstraint().expr()
                    ),
                    "MAX_CT" to Parameter(
                        "MAX_CT",
                        BitListValue(100.toBigInteger(), true),
                        null
                    ),
                    "NEG_TEST" to Parameter(
                        "NEG_TEST",
                        BitListValue((-100).toBigInteger(), true),
                        tree.module(0).paramList().paramDec(2).paramConstraint().expr()
                    )
                ),
                mapOf(
                    "clk" to Port("clk", SignalDirection.Read, BitWidth, false),
                    "rst" to Port("rst", SignalDirection.Read, BitWidth, true),
                    "count" to Port("count", SignalDirection.Write, BitListWidth(8), false),
                    "test" to Port("test", SignalDirection.Both, BitWidth, false)
                )
            ),
            module
        )
    }


}