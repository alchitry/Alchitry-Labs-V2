import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.types.Dff
import com.alchitry.labs2.parsers.hdl.types.EnumType
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitValue
import com.alchitry.labs2.project.QueueExhaustionException
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ModuleMultiPassTests {
    @Test
    fun testGlobalUse() = runBlocking {
        val tester = ProjectTester(
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
        val top = tester.fullParse()
        tester.assertNoIssues()

        val dff = top.context.resolveSignal(top.moduleContext, "myDff") as Dff

        assertEquals(BitValue(Bit.B1, false), dff.clk.value)
    }

    @Test
    fun basicRepeatTest() = runBlocking {
        val tester = ProjectTester(
            """
                module myModule (
                    input a
                ) {
                    sig endValue[16]
                    const REP_CT = 5
                
                    always {
                        if (a) {}
                        endValue = 0
                        repeat(i, REP_CT) {
                            endValue = endValue + i
                        }
                    }
                }
            """.trimIndent().toSourceFile("myModule.luc")
        )
        val top = tester.fullParse()
        tester.assertNoIssues()
        val testSig = top.context.resolveSignal(top.moduleContext, "endValue") as Signal

        top.context.initialize()

        assertEquals(BitListValue(1 + 2 + 3 + 4, 16, signed = false), testSig.read(null))
    }

    @Test
    fun doubleDriverTest() = runBlocking {
        val tester = ProjectTester(
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
        val top = tester.fullParse()
        tester.assertNoIssues()
        val context = top.context
        val testSig = context.resolveSignal(top.moduleContext, "testB") as Signal

        context.initialize()

        assertEquals(BitValue(Bit.B1, signed = false), testSig.read(null))
    }

    @Test
    fun endlessLoopTest() = runBlocking {
        val tester = ProjectTester(
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
        tester.assertNoIssues()

        val context = top.context

        (top.getSignal("a") as Signal).write(BitValue(Bit.B0, false))
        context.initialize()
        (top.getSignal("a") as Signal).write(BitValue(Bit.B1, false))
        try {
            tester.project.processQueue()
            error("ProcessQueue should've thrown an error!")
        } catch (_: QueueExhaustionException) {

        }
    }

    @Test
    fun enumTest() = runBlocking {
        val tester = ProjectTester(
            """
                module myModule (
                    input a,
                    output b[2]
                ) {
                    enum MyFsm {
                        IDLE,
                        INIT,
                        RUN,
                        STOP
                    }
                    
                    sig w[2] = ${"$"}width(MyFsm)
                    sig t[2] = ${"$"}width(MyFsm.IDLE)
                
                    always {
                        if (a) {}
                        if (w == t) {}
                        b = MyFsm.RUN
                    }
                }
            """.trimIndent().toSourceFile()
        )
        val top = tester.fullParse()
        tester.assertNoIssues()
        val context = top.context
        val testSig = top.ports["b"]?.external

        context.initialize()

        val enum = EnumType("MyFsm", setOf("IDLE", "INIT", "RUN", "STOP"), null)

        assert(tester.notationManager.hasNoErrors) { tester.notationManager.getReport() }

        assertEquals(BitListValue(2, 2, signed = false), testSig?.read())
        assertEquals(enum, context.enum.resolve(top.moduleContext, "MyFsm"))

        assertEquals(
            BitListValue(2, 2, signed = false),
            (context.resolveSignal(top.moduleContext, "w") as? Signal)?.read()
        )

        assertEquals(
            BitListValue(2, 2, signed = false),
            (context.resolveSignal(top.moduleContext, "t") as? Signal)?.read()
        )
    }

    @Test
    fun globalEnumTest() = runBlocking {
        val tester = ProjectTester(
            """
                module myModule (
                    output a[2]
                ) {
                    always {
                        a = Enums.MyFsm.RUN
                    }
                }
            """.trimIndent().toSourceFile(),
            """
                global Enums {
                    enum MyFsm {
                        IDLE,
                        INIT,
                        RUN,
                        STOP
                    }
                }
            """.trimIndent().toSourceFile("globals.luc")
        )
        val top = tester.fullParse()
        tester.assertNoIssues()
        val context = top.context
        val testSig = top.ports["a"]?.external as Signal

        context.initialize()

        val enum = EnumType("MyFsm", setOf("IDLE", "INIT", "RUN", "STOP"), tester.project.resolveGlobal("Enums"))

        assertEquals(BitListValue(2, 2, signed = false), testSig.read())
        assertEquals(enum, tester.project.resolveGlobal("Enums")?.enums?.values?.first())
    }
}