import com.alchitry.labs.parsers.lucidv2.signals.Dff
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitValue
import helpers.LucidModuleTester
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleMultiPassTests {

    @Test
    fun testGlobalUse() {
        val tester = LucidModuleTester(
            """
                global MyGlobal {
                    const ONE = 1
                }
                
                module myModule (
                    input a
                ) {
                
                    dff test(.clk(MyGlobal.ONE))
                
                }
            """.trimIndent()
        )
        tester.fullParse()

        val dff = tester.context.resolveSignal("test") as Dff

        assertEquals(BitValue(Bit.B1, true, false), dff.clk.value)
    }
}