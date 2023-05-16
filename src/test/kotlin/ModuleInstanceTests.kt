import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.types.ModuleInstanceArray
import com.alchitry.labs.parsers.lucidv2.values.*
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleInstanceTests {
    @Test
    fun basicTwoModules() {
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
        assertEquals(SignalDirection.Read, myModInst.getSignal("a")?.direction)
    }

    @Test
    fun basicModuleArray() {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk
                ) {
                    .clk(clk) {
                        testModule myMod[8]
                    }
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
        assertEquals(ArrayWidth(8, BitListWidth(5)), myModInst.getSignal("a")?.width)
        assertEquals(SignalDirection.Read, myModInst.getSignal("a")?.direction)
    }

    @Test
    fun counterTest() {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk,
                    output count[8]
                ) {
                    dff counter[8] (.clk(clk))
                    sig fullSum[9]
                    always {
                        fullSum = counter.q + 1
                        counter.d = fullSum[7:0]
                        count = counter.q
                    }
                }
            """.trimIndent()
        )

        val project = tester.project

        val top = tester.fullParse()

        val clk = top.getSignal("clk") as Signal
        val count = top.getSignal("count") as Signal

        val b0 = BitValue(Bit.B0, false, false)
        val b1 = BitValue(Bit.B1, false, false)

        runBlocking {
            top.context.initialize()
            project.processQueue()

            (0..255).forEach {
                assertEquals(BitListValue(it, 8, false, false), count.read())

                clk.write(b0)
                project.processQueue()
                clk.write(b1)
                project.processQueue()
            }
            assertEquals(BitListValue(0, 8, false, false), count.read())
        }
    }

    @Test
    fun counterParamTest() {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk,
                    output count[8]
                ) {
                    .clk(clk) {
                        testModule myMod (#INC(2))
                    }
                   
                    always {
                        count = myMod.count
                    }
                }
            """.trimIndent(),
            """
                module testModule #(
                    INC
                )(
                    input clk,
                    output count[8]
                ) {
                    dff counter[8] (.clk(clk))
                    sig fullSum[9]
                    
                    always {
                        fullSum = counter.q + INC
                        counter.d = fullSum[7:0]
                        count = counter.q
                    }
                }
            """.trimIndent()
        )

        val project = tester.project

        val top = tester.fullParse()

        val clk = top.getSignal("clk") as Signal
        val count = top.getSignal("count") as Signal

        val b0 = BitValue(Bit.B0, false, false)
        val b1 = BitValue(Bit.B1, false, false)

        runBlocking {
            top.context.initialize()
            project.processQueue()

            (0..127).forEach {
                assertEquals(BitListValue(it * 2, 8, false, false), count.read())

                clk.write(b0)
                project.processQueue()
                clk.write(b1)
                project.processQueue()
            }
            assertEquals(BitListValue(0, 8, false, false), count.read())
        }
    }
}