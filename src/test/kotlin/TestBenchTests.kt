import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimValue
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestBenchTests {
    @Test
    fun basicTestBenchTest() {
        val tester = LucidTester(
            """
                testBench myTestBench {
                    sig clk
                    
                    counter dut (.clk(clk))
                    
                    test simpleTest {
                        repeat(100, i) {
                            clk = 0
                            ${"$"}tick()
                            ${"$"}assert(dut.count == i)
                            clk = 1
                            ${"$"}tick()
                        }
                        ${"$"}assert(dut.count == 100)
                    }
                }
            """.trimIndent(),
            """
                module counter (
                    input clk,
                    output count[8]
                ) {
                    dff counter[8] (.clk(clk))
                    
                    always {
                        counter.d = counter.q + 1
                        count = counter.q
                    }
                }
            """.trimIndent()
        )

        tester.runTestBenches()
    }

    @Test
    fun basicFunctionTest() {
        val tester = LucidTester(
            """
                testBench myTestBench {
                    sig clk
                    
                    counter dut (.clk(clk))
                    
                    fun tickClock() {
                        clk = 1
                        ${"$"}silentTick()
                        clk = 0
                        ${"$"}tick()
                    }
                    
                    test simpleTest {
                        clk = 0
                        ${"$"}tick()
                        
                        repeat(100, i) {
                            ${"$"}assert(dut.count == i)
                            ${"$"}tickClock()
                        }
                        ${"$"}assert(dut.count == 100)
                    }
                }
            """.trimIndent(),
            """
                module counter (
                    input clk,
                    output count[8]
                ) {
                    dff counter[8] (.clk(clk))
                    
                    always {
                        counter.d = counter.q + 1
                        count = counter.q
                    }
                }
            """.trimIndent()
        )

        val result = tester.runFirstTestBench()

        val countValues = ((result["dut"] as SimParent)["count"] as SimValue)
        assertEquals(101, countValues.size)
        assertEquals(BitListValue(67, 8, false, false), countValues[67])
    }

    @Test
    fun basicFunctionArgTest() {
        val tester = LucidTester(
            """
                testBench myTestBench {
                    sig clk
                    
                    counter dut (.clk(clk))
                    
                    fun tickClock(times[32]) {
                        repeat(times) {
                            clk = 1
                            ${"$"}tick()
                            clk = 0
                            ${"$"}tick()
                        }
                    }
                    
                    test simpleTest {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}tickClock(100)
                       
                        ${"$"}assert(dut.count == 100)
                    }
                }
            """.trimIndent(),
            """
                module counter (
                    input clk,
                    output count[8]
                ) {
                    dff counter[8] (.clk(clk))
                    
                    always {
                        counter.d = counter.q + 1
                        count = counter.q
                    }
                }
            """.trimIndent()
        )

        tester.runTestBenches()
    }
}