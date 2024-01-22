import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.types.Dff
import com.alchitry.labs2.parsers.lucidv2.types.EnumType
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import com.alchitry.labs2.parsers.lucidv2.values.BitValue
import com.alchitry.labs2.project.QueueExhaustionException
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleMultiPassTests {
    @Test
    fun testGlobalUse() = runBlocking {
        val tester = LucidTester(
            """
                global MyGlobal {
                    const ONE = 1
                }
                
                module myModule (
                    input a
                ) {
                
                    dff myDff(.clk(MyGlobal.ONE))
                    
                    always {
                        myDff.d = 0
                        if (myDff.q) {}
                        if (a) {}
                    }
                }
            """.trimIndent().toSourceFile("myGlobal.luc")
        )
        val context = tester.fullParse().context

        val dff = context.resolveSignal("myDff") as Dff

        assertEquals(BitValue(Bit.B1, true, false), dff.clk.value)
    }

    @Test
    fun basicRepeatTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig endValue[16]
                    const REP_CT = 5
                
                    always {
                        if (a) {}
                        endValue = 0
                        repeat(REP_CT, i) {
                            endValue = endValue + i
                        }
                    }
                }
            """.trimIndent().toSourceFile("myModule.luc")
        )
        val context = tester.fullParse().context
        val testSig = context.resolveSignal("endValue") as Signal

        context.initialize()

        assertEquals(BitListValue(1 + 2 + 3 + 4, 16, signed = false, constant = false), testSig.read(null))
    }

    @Test
    fun doubleDriverTest() = runBlocking {
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
                        if (testB) { } // to remove unused signal warning
                        if (a) {}
                        
                        testA = 1
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val context = tester.fullParse().context
        val testSig = context.resolveSignal("testB") as Signal

        context.initialize()

        assertEquals(BitValue(Bit.B1, signed = false, constant = false), testSig.read(null))
    }

    @Test
    fun endlessLoopTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a
                ) {
                    sig testA
                    sig testB
                
                    always {
                        if (a)
                            testB = ~testA
                        else
                            testB = 1
                    }
                    
                    always {
                        testA = testB
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val top = tester.fullParse()

        val context = top.context

        (top.getSignal("a") as Signal).write(BitValue(Bit.B0, false, false))
        context.initialize()
        (top.getSignal("a") as Signal).write(BitValue(Bit.B1, false, false))
        try {
            tester.project.processQueue()
            error("ProcessQueue should've thrown an error!")
        } catch (_: QueueExhaustionException) {

        }
    }

    @Test
    fun enumTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    input a,
                    output b[2]
                ) {
                    enum myFSM {
                        IDLE,
                        INIT,
                        RUN,
                        STOP
                    }
                    
                    sig w[2] = myFSM.WIDTH
                    sig t[2] = ${"$"}widthOf(myFSM.IDLE)
                
                    always {
                        if (a) {}
                        if (w == t) {}
                        b = myFSM.RUN
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val top = tester.fullParse()
        val context = top.context
        val testSig = top.ports["b"]?.external

        context.initialize()

        val enum = EnumType("myFSM", setOf("IDLE", "INIT", "RUN", "STOP"), null)

        assertEquals(BitListValue(2, 2, signed = false, constant = false), testSig?.read())
        assertEquals(enum, context.enum.resolve("myFSM"))

        assertEquals(
            BitListValue(2, 2, constant = false, signed = false),
            (context.resolveSignal("w") as? Signal)?.read()
        )

        assertEquals(
            BitListValue(2, 2, constant = false, signed = false),
            (context.resolveSignal("t") as? Signal)?.read()
        )
    }

    @Test
    fun globalEnumTest() = runBlocking {
        val tester = LucidTester(
            """
                module myModule (
                    output a[2]
                ) {
                    always {
                        a = Enums.myFSM.RUN
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                global Enums {
                    enum myFSM {
                        IDLE,
                        INIT,
                        RUN,
                        STOP
                    }
                }
            """.trimIndent().toSourceFile("globals.luc")
        )
        val top = tester.fullParse()
        val context = top.context
        val testSig = top.ports["a"]?.external as Signal

        context.initialize()

        val enum = EnumType("myFSM", setOf("IDLE", "INIT", "RUN", "STOP"), tester.project.resolveGlobal("Enums"))

        assertEquals(BitListValue(2, 2, signed = false, constant = false), testSig.read())
        assertEquals(enum, tester.project.resolveGlobal("Enums")?.enums?.values?.first())
    }
}