import com.alchitry.labs.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimValue
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestBenchTests {
    @Test
    fun basicTestBenchTest() = runBlocking {
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
            """.trimIndent().toSourceFile(),
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
            """.trimIndent().toSourceFile("counter.luc")
        )

        tester.runTestBenches()
    }

    @Test
    fun basicFunctionTest() = runBlocking {
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
            """.trimIndent().toSourceFile(),
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
            """.trimIndent().toSourceFile("counter.luc")
        )

        val result = tester.runFirstTestBench()

        val countValues = ((result["dut"] as SimParent)["count"] as SimValue)
        assertEquals(101, countValues.size)
        assertEquals(BitListValue(67, 8, false, false), countValues[67])
    }

    @Test
    fun basicFunctionArgTest() = runBlocking {
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
            """.trimIndent().toSourceFile(),
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
            """.trimIndent().toSourceFile("counter.luc")
        )

        tester.runTestBenches()
    }

    @Test
    fun doubleFunctionArgTests() = runBlocking {
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
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(100)
                       
                        ${"$"}assert(dut.count == 100)
                    }
                    
                    test simpleTest2 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(200)
                       
                        ${"$"}assert(dut.count == 200)
                    }
                }
            """.trimIndent().toSourceFile(),
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
            """.trimIndent().toSourceFile("counter.luc")
        )

        tester.runTestBenches()
    }

    @Test
    fun manyFunctionArgTests() = runBlocking {
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
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(100)
                       
                        ${"$"}assert(dut.count == 100)
                    }
                    
                    test simpleTest2 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(150)
                       
                        ${"$"}assert(dut.count == 150)
                    }
                    
                    test simpleTest3 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(50)
                       
                        ${"$"}assert(dut.count == 50)
                    }
                    
                    test simpleTest4 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(120)
                       
                        ${"$"}assert(dut.count == 120)
                    }
                    
                    test simpleTest5 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(200)
                       
                        ${"$"}assert(dut.count == 200)
                    }
                    
                    test simpleTest6 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(100)
                       
                        ${"$"}assert(dut.count == 100)
                    }
                    
                    test simpleTest7 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(150)
                       
                        ${"$"}assert(dut.count == 150)
                    }
                    
                    test simpleTest8 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(50)
                       
                        ${"$"}assert(dut.count == 50)
                    }
                    
                    test simpleTest9 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(120)
                       
                        ${"$"}assert(dut.count == 120)
                    }
                    
                    test simpleTest10 {
                        clk = 0
                        ${"$"}tick()
                        ${"$"}assert(dut.count == 0)
                        ${"$"}tickClock(200)
                       
                        ${"$"}assert(dut.count == 200)
                    }
                }
            """.trimIndent().toSourceFile(),
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
            """.trimIndent().toSourceFile("counter.luc")
        )

        tester.parallelRunTestBenches()
    }
}