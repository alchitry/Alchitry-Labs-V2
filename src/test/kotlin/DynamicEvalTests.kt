import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.signals.DynamicExpr
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.Dff
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitValue
import helpers.SimpleLucidTester
import helpers.TestSignalResolver
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicEvalTests {
    @Test
    fun basicDynamicExpr() {
        repeat(30) { // repeat to attempt to check for race conditions
            val signal =
                Signal("mySig", SignalDirection.Both, null, BitListValue("110", 2, constant = false, signed = false))

            val test =
                SimpleLucidTester(
                    "~mySig",
                    localSignalResolver = TestSignalResolver(signal)
                )

            val exprCtx = test.expr()
            test.context.walk(exprCtx)

            val dynamicExpr = DynamicExpr(exprCtx, test.context)

            assertEquals(BitListValue("001", 2, constant = false, signed = false), dynamicExpr.value)

            runBlocking {
                signal.write(BitListValue("011", 2, constant = false, signed = false))
                test.context.project.processQueue()
            }

            assertEquals(BitListValue("100", 2, constant = false, signed = false), dynamicExpr.value)

            runBlocking {
                signal.write(BitListValue("111", 2, constant = false, signed = false))
                test.context.project.processQueue()
            }

            assertEquals(BitListValue("000", 2, constant = false, signed = false), dynamicExpr.value)
        }
    }

    @Test
    fun dffTest() {
        val b0 = BitValue(Bit.B0, false, false)
        val b1 = BitValue(Bit.B1, false, false)

        val clk = Signal("clk", SignalDirection.Read, null, b0, false)

        val test =
            SimpleLucidTester(
                "clk",
                localSignalResolver = TestSignalResolver(clk)
            )

        val exprCtx = test.expr()
        test.context.walk(exprCtx)
        val clkExpr = DynamicExpr(exprCtx, test.context)

        val dff = Dff(test.project, "myDff", b0, clkExpr, null, false)

        runBlocking {
            clk.write(b0)
            test.project.processQueue()
            dff.d.write(b1)
            clk.write(b1)
            test.project.processQueue()
            assertEquals(b1, dff.q.read())

            clk.write(b0)
            dff.d.write(b0)
            test.project.processQueue()
            assertEquals(b1, dff.q.read())

            clk.write(b1)
            test.project.processQueue()
            assertEquals(b0, dff.q.read())
        }
    }

    @Test
    fun signalContextTest() {
        val evaluable = Evaluable { }
        val evaluable2 = Evaluable { }
        val sig = Signal("test", SignalDirection.Both, null, BitValue(Bit.B1, false, false), false)
        sig.quietWrite(BitValue(Bit.B0, false, false), evaluable)

        assertEquals(BitValue(Bit.B1, false, false), sig.read(null))
        assertEquals(BitValue(Bit.B1, false, false), sig.read(evaluable2))
        assertEquals(BitValue(Bit.B0, false, false), sig.read(evaluable))

        runBlocking {
            sig.publish()
        }

        assertEquals(BitValue(Bit.B0, false, false), sig.read(null))
        assertEquals(BitValue(Bit.B0, false, false), sig.read(evaluable2))
        assertEquals(BitValue(Bit.B0, false, false), sig.read(evaluable))
    }

    @Test
    fun basicAlwaysEvalTest() {
        val sig1 =
            Signal("sig1", SignalDirection.Write, null, BitValue(Bit.B0, constant = false, signed = false), false)
        val sig2 = Signal("sig2", SignalDirection.Read, null, BitValue(Bit.B1, constant = false, signed = false), false)

        val tester = SimpleLucidTester(
            """
            always {
                sig1 = sig2
            }
            """.trimIndent(),
            TestSignalResolver(sig1, sig2)
        )

        tester.context.walk(tester.alwaysBlock())
        assert(tester.hasNoIssues)

        runBlocking {
            tester.context.initialize()
        }

        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.read(null))
        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig2.read(null))

        runBlocking {
            sig2.write(BitValue(Bit.B0, constant = false, signed = false))
            assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.read(null))

            sig2.write(BitValue(Bit.Bx, constant = false, signed = false))
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.Bx, constant = false, signed = false), sig1.read(null))
        }
    }

    @Test
    fun testIfStatement() {
        val sig1 =
            Signal("sig1", SignalDirection.Write, null, BitValue(Bit.B0, constant = false, signed = false), false)
        val sig2 = Signal("sig2", SignalDirection.Read, null, BitValue(Bit.B1, constant = false, signed = false), false)
        val tester = SimpleLucidTester(
            """
            always {
                if (sig2) 
                    sig1 = 1
                else
                    sig1 = 0
            }
            """.trimIndent(),
            TestSignalResolver(sig1, sig2)
        )

        tester.context.walk(tester.alwaysBlock())
        assert(tester.hasNoIssues)

        runBlocking {
            tester.context.initialize()
        }

        val alwaysBlock = tester.context.alwaysParser.alwaysBlocks.values.first()

        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.read(null))
        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig2.read(null))

        runBlocking {
            sig2.write(BitValue(Bit.B0, constant = false, signed = false))
            assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.read(null))

            assert(alwaysBlock.context.errorCollector.hasNoIssues)

            sig2.write(BitValue(Bit.Bx, constant = false, signed = false))
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.read(null))

            assert(alwaysBlock.context.errorCollector.hasWarnings) // warn about Bx value in if statement
        }
    }
}