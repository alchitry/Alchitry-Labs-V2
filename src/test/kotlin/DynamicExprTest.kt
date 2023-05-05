import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.signals.DynamicExpr
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitValue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicExprTest {
    @Test
    fun basicDynamicExpr() {
        repeat(30) { // repeat to attempt to check for race conditions
            val signal =
                Signal("test", SignalDirection.Both, null, BitListValue("110", 2, constant = false, signed = false))

            val test = LucidTester("~test", localSignalResolver = TestSignalResolver(signal))

            val exprCtx = test.expr()

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
        val evaluable = Evaluable {  }
        val evaluable2 = Evaluable {  }
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
}