import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.DynamicExpr
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicExprTest {
    @Test
    fun basicDynamicExpr() {
        val test = LucidTester("~test")

        val signal = Signal("test", SignalDirection.Both, null, BitListValue("110", 2, constant = false, signed = false))

        test.parseContext.testingSignalResolver = TestSignalResolver(signal)

        val exprCtx = test.expr()

        val dynamicExpr = DynamicExpr(exprCtx, test.parseContext)

        assertEquals(BitListValue("001", 2, constant = false, signed = false), dynamicExpr.value)

        runBlocking {
            signal.set(BitListValue("011", 2, constant = false, signed = false))
            test.parseContext.processQueue()
        }

        assertEquals(BitListValue("100", 2, constant = false, signed = false), dynamicExpr.value)

        runBlocking {
            signal.set(BitListValue("111", 2, constant = false, signed = false))
            test.parseContext.processQueue()
        }

        assertEquals(BitListValue("000", 2, constant = false, signed = false), dynamicExpr.value)
    }
}