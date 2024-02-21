import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.values.*
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleInstanceTests {
    @Test
    fun basicTwoModules() = runBlocking {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk
                ) {
                
                    testModule myMod (.clk(clk))
                    
                    always {
                        if (myMod.a) { } // to remove unused signal warning
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module testModule (
                    input clk,
                    output a
                ) {
                    always  {
                        a = clk
                    }
                }
            """.trimIndent().toSourceFile("testModule.luc")
        )

        val top = tester.fullParse()
        val myModInst = top.context.types.resolve("myMod") as ModuleInstance
        assertEquals(SignalDirection.Read, myModInst.getSignal("a")?.direction)
    }

    @Test
    fun basicModuleArray() = runBlocking {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk
                ) {
                    .clk(clk) {
                        testModule myMod[8]
                    }
                    
                    always {
                        if (myMod.a[0]) { } // to remove unused signal warning
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module testModule (
                    input clk,
                    output a[5]
                ) {
                    always  {
                        a = clk
                    }
                }
            """.trimIndent().toSourceFile("testModule.luc")
        )

        val top = tester.fullParse()
        val myModInst = top.context.types.resolve("myMod") as ModuleInstanceArray
        assertEquals(ArrayWidth(8, BitListWidth(5)), myModInst.getSignal("a")?.width)
        assertEquals(SignalDirection.Read, myModInst.getSignal("a")?.direction)
    }

    @Test
    fun counterTest() = runBlocking {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk,
                    output count[8]
                ) {
                    dff counter[8] (.clk(clk))
                    always {
                        counter.d = counter.q + 1
                        count = counter.q
                    }
                }
            """.trimIndent().toSourceFile()
        )

        val top = tester.fullParse()

        val clk = top.getSignal("clk") as Signal
        val count = top.getSignal("count") as Signal

        val b0 = BitValue(Bit.B0, false, false)
        val b1 = BitValue(Bit.B1, false, false)

        runBlocking {
            top.context.initialize()

            (0..255).forEach {
                assertEquals(BitListValue(it, 8, false, false), count.read())

                clk.write(b0)
                tester.project.processQueue()
                clk.write(b1)
                tester.project.processQueue()
            }
            assertEquals(BitListValue(0, 8, false, false), count.read())
        }
    }

    @Test
    fun complexParamTest() = runBlocking {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk,
                    output count[8]
                ) {
                    decoder myMod (#WIDTH(3))
                    
                    always {
                        myMod.in = 3
                        count = myMod.out
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module decoder #(
                    WIDTH ~ 4 : WIDTH > 0
                )(
                    input in[WIDTH],
                    output out[c{1, WIDTHx{1b0}}]
                ) {
                    always {
                        out = 0
                        out[in] = 1
                    }
                }
            """.trimIndent().toSourceFile("testModule.luc")
        )

        val top = tester.fullParse()

        val clk = top.getSignal("clk") as Signal
        val count = top.getSignal("count") as Signal

        val b0 = BitValue(Bit.B0, false, false)
        val b1 = BitValue(Bit.B1, false, false)


        top.context.initialize()

        clk.write(b0)
        tester.project.processQueue()
        clk.write(b1)
        tester.project.processQueue()

        assertEquals(BitListValue(8, 8, false, false), count.read())
    }

    @Test
    fun counterParamTest() = runBlocking {
        val tester = LucidTester(
            """
                module alchitryTop (
                    input clk,
                    output count[8]
                ) {
                    .clk(clk) {
                        testModule myMod (#INC(2), #CTR_SIZE(8))
                    }
                   
                    always {
                        count = myMod.count
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                module testModule #(
                    INC ~ 1,
                    CTR_SIZE ~ 4
                )(
                    input clk,
                    output count[CTR_SIZE]
                ) {
                    dff counter[8] (.clk(clk))
                    
                    always {
                        counter.d = counter.q + INC
                        count = counter.q
                    }
                }
            """.trimIndent().toSourceFile("testModule.luc")
        )

        val top = tester.fullParse()

        val clk = top.getSignal("clk") as Signal
        val count = top.getSignal("count") as Signal

        val b0 = BitValue(Bit.B0, false, false)
        val b1 = BitValue(Bit.B1, false, false)


        top.context.initialize()

        (0..127).forEach {
            assertEquals(BitListValue(it * 2, 8, false, false), count.read())

            clk.write(b0)
            tester.project.processQueue()
            clk.write(b1)
            tester.project.processQueue()
        }
        assertEquals(BitListValue(0, 8, false, false), count.read())
    }
}