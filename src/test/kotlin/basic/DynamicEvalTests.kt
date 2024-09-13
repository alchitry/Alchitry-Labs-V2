package basic

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.types.Dff
import com.alchitry.labs2.parsers.hdl.types.DynamicExpr
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.parsers.hdl.values.BitValue
import helpers.SimpleLucidTester
import helpers.TestSignalResolver
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicEvalTests {
    @Test
    fun basicDynamicExpr() = runBlocking {
        repeat(30) { // repeat to attempt to check for race conditions
            val signal =
                Signal("mySig", SignalDirection.Both, null, BitListValue("110", 2, signed = false), ExprType.Dynamic)

            val test =
                SimpleLucidTester(
                    "~mySig",
                    localSignalResolver = TestSignalResolver(signal)
                )

            val exprCtx = test.parser.expr()
            test.context.walk(exprCtx)

            val dynamicExpr = DynamicExpr(exprCtx, test.context)

            assertEquals(BitListValue("001", 2, signed = false), dynamicExpr.value)

            signal.write(BitListValue("011", 2, signed = false))
            test.context.project.processQueue()

            assertEquals(BitListValue("100", 2, signed = false), dynamicExpr.value)

            signal.write(BitListValue("111", 2, signed = false))
            test.context.project.processQueue()

            assertEquals(BitListValue("000", 2, signed = false), dynamicExpr.value)
        }
    }

    @Test
    fun dffTest() = runBlocking {
        val b0 = BitValue(Bit.B0, false)
        val b1 = BitValue(Bit.B1, false)

        val clk = Signal("clk", SignalDirection.Read, null, b0, ExprType.Dynamic, false)

        val test =
            SimpleLucidTester(
                "clk",
                localSignalResolver = TestSignalResolver(clk)
            )

        val exprCtx = test.parser.expr()
        test.context.walk(exprCtx)
        val clkExpr = DynamicExpr(exprCtx, test.context)

        val dff = Dff(test.project, "myDff", b0, clkExpr, null, false, null)


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

    @Test
    fun signalContextTest() = runBlocking {
        val evaluable = Evaluable { }
        val evaluable2 = Evaluable { }
        val sig = Signal("test", SignalDirection.Both, null, BitValue(Bit.B1, false), ExprType.Dynamic, false)
        sig.quietWrite(BitValue(Bit.B0, false), evaluable)

        assertEquals(BitValue(Bit.B1, false), sig.read(null))
        assertEquals(BitValue(Bit.B1, false), sig.read(evaluable2))
        assertEquals(BitValue(Bit.B0, false), sig.read(evaluable))

        runBlocking {
            sig.publish()
        }

        assertEquals(BitValue(Bit.B0, false), sig.read(null))
        assertEquals(BitValue(Bit.B0, false), sig.read(evaluable2))
        assertEquals(BitValue(Bit.B0, false), sig.read(evaluable))
    }

    @Test
    fun basicAlwaysEvalTest() = runBlocking {
        val sig1 =
            Signal("sig1", SignalDirection.Write, null, BitValue(Bit.B0, signed = false), ExprType.Dynamic, false)
        val sig2 = Signal("sig2", SignalDirection.Read, null, BitValue(Bit.B1, signed = false), ExprType.Dynamic, false)

        val tester = SimpleLucidTester(
            """
                module myMod () {
                    always {
                        sig1 = sig2
                    }
                }
            """.trimIndent(),
            TestSignalResolver(sig1, sig2)
        )

        tester.context.walk(tester.parser.module())
        assert(tester.hasNoIssues)

        runBlocking {
            tester.context.initialize()
        }

        assertEquals(BitValue(Bit.B1, signed = false), sig1.read(null))
        assertEquals(BitValue(Bit.B1, signed = false), sig2.read(null))

        runBlocking {
            sig2.write(BitValue(Bit.B0, signed = false))
            assertEquals(BitValue(Bit.B1, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, signed = false), sig1.read(null))

            sig2.write(BitValue(Bit.Bx, signed = false))
            assertEquals(BitValue(Bit.B0, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.Bx, signed = false), sig1.read(null))
        }
    }

    @Test
    fun testIfStatement() = runBlocking {
        val sig1 =
            Signal("sig1", SignalDirection.Write, null, BitValue(Bit.B0, signed = false), ExprType.Dynamic, false)
        val sig2 = Signal("sig2", SignalDirection.Read, null, BitValue(Bit.B1, signed = false), ExprType.Dynamic, false)
        val tester = SimpleLucidTester(
            """
                module myMod () {
                    always {
                        if (sig2) 
                            sig1 = 1
                        else
                            sig1 = 0
                    }
                }
            """.trimIndent(),
            TestSignalResolver(sig1, sig2)
        )

        tester.context.walk(tester.parser.module())
        assert(tester.hasNoIssues)

        runBlocking {
            tester.context.initialize()
        }

        val alwaysBlock = tester.context.blockParser.alwaysBlocks.values.first()

        assertEquals(BitValue(Bit.B1, signed = false), sig1.read(null))
        assertEquals(BitValue(Bit.B1, signed = false), sig2.read(null))

        runBlocking {
            sig2.write(BitValue(Bit.B0, signed = false))
            assertEquals(BitValue(Bit.B1, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, signed = false), sig1.read(null))

            assert(alwaysBlock.context.notationCollector.hasNoMessages)

            sig2.write(BitValue(Bit.Bx, signed = false))
            assertEquals(BitValue(Bit.B0, signed = false), sig1.read(null))

            tester.project.processQueue()
            assertEquals(BitValue(Bit.B0, signed = false), sig1.read(null))

            assert(alwaysBlock.context.notationCollector.hasWarnings) // warn about Bx value in if statement
        }
    }
}