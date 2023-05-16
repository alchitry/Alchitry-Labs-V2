import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.types.Dff
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleMultiPassTests {
    @Test
    fun testGlobalUse() {
        val tester = LucidTester(
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
        val context = tester.fullParse().context

        val dff = context.resolveSignal("test") as Dff

        assertEquals(BitValue(Bit.B1, true, false), dff.clk.value)
    }

    @Test
    fun basicRepeatTest() {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig endValue[16]
                
                    always {
                        endValue = 0
                        repeat(i, 5) {
                            endValue = endValue[14:0] + i
                        }
                    }
                }
            """.trimIndent()
        )
        val context = tester.fullParse().context
        val testSig = context.resolveSignal("endValue") as Signal

        runBlocking {
            context.initialize()
            tester.project.processQueue()
        }

        assertEquals(BitListValue(1 + 2 + 3 + 4, 16, signed = false, constant = false), testSig.read(null))
    }

    @Test
    fun doubleDriverTest() {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig testA
                    sig testB
                
                    always {
                        testB = testA
                    }
                    
                    always {
                        testA = 1
                    }
                }
            """.trimIndent()
        )
        val context = tester.fullParse().context
        val testSig = context.resolveSignal("testB") as Signal

        runBlocking {
            context.initialize()
            tester.project.processQueue()
        }

        assertEquals(BitValue(Bit.B1, signed = false, constant = false), testSig.read(null))
    }
}