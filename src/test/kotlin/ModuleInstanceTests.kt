import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstanceArray
import com.alchitry.labs.parsers.lucidv2.values.ArrayWidth
import com.alchitry.labs.parsers.lucidv2.values.BitListWidth
import com.alchitry.labs.parsers.lucidv2.values.BitWidth
import helpers.LucidTester
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleInstanceTests {
    @Test
    fun BasicTwoModules() {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk
                ) {
                
                    testModule myMod (.clk(clk))
                
                }
            """.trimIndent(),
            """
                module testModule (
                    input clk,
                    output a
                ) {
                    always  {
                        a = clk
                    }
                }
            """.trimIndent()
        )

        val top = tester.fullParse()
        val myModInst = top.context.types.resolve("myMod") as ModuleInstance
        assertEquals(BitWidth, myModInst.getSignal("clk")?.width)
        assertEquals(SignalDirection.Write, myModInst.getSignal("clk")?.direction)
        assertEquals(SignalDirection.Read, myModInst.getSignal("a")?.direction)
    }

    @Test
    fun BasicModuleArray() {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk
                ) {
                
                    testModule myMod[8] (.clk(clk))
                
                }
            """.trimIndent(),
            """
                module testModule (
                    input clk,
                    output a[5]
                ) {
                    always  {
                        a = clk
                    }
                }
            """.trimIndent()
        )

        val top = tester.fullParse()
        val myModInst = top.context.types.resolve("myMod") as ModuleInstanceArray
        assertEquals(BitListWidth(8), myModInst.getSignal("clk")?.width)
        assertEquals(SignalDirection.Write, myModInst.getSignal("clk")?.direction)
        assertEquals(ArrayWidth(8, BitListWidth(5)), myModInst.getSignal("a")?.width)
        assertEquals(SignalDirection.Read, myModInst.getSignal("a")?.direction)
    }
}