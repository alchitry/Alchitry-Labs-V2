import helpers.LucidTester
import org.junit.jupiter.api.Test

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
}