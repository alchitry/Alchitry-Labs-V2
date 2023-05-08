import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.signals.DynamicExpr
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitValue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicEvalTests {
    @Test
    fun basicDynamicExpr() {
        repeat(30) { // repeat to attempt to check for race conditions
            val signal =
                Signal("test", SignalDirection.Both, null, BitListValue("110", 2, constant = false, signed = false))

            val test =
                LucidTester("~test", stage = ParseStage.AlwaysIO, localSignalResolver = TestSignalResolver(signal))

            val exprCtx = test.expr()
            test.context.walk(exprCtx)

            val dynamicExpr = DynamicExpr(exprCtx, test.context)

            assertEquals(BitListValue("001", 2, constant = false, signed = false), dynamicExpr.value)

            runBlocking {
                signal.set(BitListValue("011", 2, constant = false, signed = false))
                test.context.project.processQueue()
            }

            assertEquals(BitListValue("100", 2, constant = false, signed = false), dynamicExpr.value)

            runBlocking {
                signal.set(BitListValue("111", 2, constant = false, signed = false))
                test.context.project.processQueue()
            }

            assertEquals(BitListValue("000", 2, constant = false, signed = false), dynamicExpr.value)
        }
    }

    @Test
    fun signalContextTest() {
        val evaluable = Evaluable { }
        val evaluable2 = Evaluable { }
        val sig = Signal("test", SignalDirection.Both, null, BitValue(Bit.B1, false, false), false)
        sig.quietSet(BitValue(Bit.B0, false, false), evaluable)

        assertEquals(BitValue(Bit.B1, false, false), sig.get(null))
        assertEquals(BitValue(Bit.B1, false, false), sig.get(evaluable2))
        assertEquals(BitValue(Bit.B0, false, false), sig.get(evaluable))

        runBlocking {
            sig.publish()
        }

        assertEquals(BitValue(Bit.B0, false, false), sig.get(null))
        assertEquals(BitValue(Bit.B0, false, false), sig.get(evaluable2))
        assertEquals(BitValue(Bit.B0, false, false), sig.get(evaluable))
    }

    @Test
    fun basicAlwaysEvalTest() {
        val sig1 = Signal("sig1", SignalDirection.Write, null, BitValue(Bit.B0, constant = false, signed = false), false)
        val sig2 = Signal("sig2", SignalDirection.Read, null, BitValue(Bit.B1, constant = false, signed = false), false)
        val tester = LucidTester(
            """
            module testMod (
                input a
            ) {
                always {
                    sig1 = sig2
                }
            }
        """.trimIndent(),
            stage = ParseStage.AlwaysIO,
            TestSignalResolver(sig1, sig2)
        )

        tester.context.walk(tester.source())
        assert(tester.hasNoIssues)

        runBlocking {
            tester.context.queueEval()
            tester.project.processQueue()
        }

        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.get(null))
        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig2.get(null))

        runBlocking {
            sig2.set(BitValue(Bit.B0, constant = false, signed = false))
            assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.get(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.get(null))

            sig2.set(BitValue(Bit.Bx, constant = false, signed = false))
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.get(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.Bx, constant = false, signed = false), sig1.get(null))
        }
    }

    @Test
    fun testIfStatement() {
        val sig1 =
            Signal("sig1", SignalDirection.Write, null, BitValue(Bit.B0, constant = false, signed = false), false)
        val sig2 = Signal("sig2", SignalDirection.Read, null, BitValue(Bit.B1, constant = false, signed = false), false)
        val tester = LucidTester(
            """
            module testMod (
                input a
            ) {
                always {
                    if (sig2) 
                        sig1 = 1
                    else
                        sig1 = 0
                }
            }
        """.trimIndent(),
            stage = ParseStage.AlwaysIO,
            TestSignalResolver(sig1, sig2)
        )

        tester.context.walk(tester.source())
        assert(tester.hasNoIssues)

        runBlocking {
            tester.context.queueEval()
            tester.project.processQueue()
        }

        val alwaysBlock = tester.context.alwaysParser.alwaysBlocks.first()

        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.get(null))
        assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig2.get(null))

        runBlocking {
            sig2.set(BitValue(Bit.B0, constant = false, signed = false))
            assertEquals(BitValue(Bit.B1, constant = false, signed = false), sig1.get(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.get(null))

            assert(alwaysBlock.context.errorCollector.hasNoIssues)

            sig2.set(BitValue(Bit.Bx, constant = false, signed = false))
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.get(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, constant = false, signed = false), sig1.get(null))

            assert(alwaysBlock.context.errorCollector.hasWarnings) // warn about Bx value in if statement
        }
    }
}