import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.values.DynamicExpr
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.MutableBitList
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicExprTest {
    @Test
    fun basicDynamicExpr() {
        val test = LucidTester("~test")

        val signal = Signal("test", SignalDirection.Both, null, SimpleValue(MutableBitList("110", 2), false))

        test.parseContext.testingSignalResolver = TestSignalResolver(signal)

        val exprCtx = test.expr()

        val dynamicExpr = DynamicExpr(exprCtx, test.parseContext)

        runBlocking { test.parseContext.processQueue() }

        assertEquals(SimpleValue(MutableBitList("001", 2), false), dynamicExpr.value)

        signal.value = SimpleValue(MutableBitList("011", 2), false)
        runBlocking { test.parseContext.processQueue() }

        assertEquals(SimpleValue(MutableBitList("100", 2), false), dynamicExpr.value)

        signal.value = SimpleValue(MutableBitList("111", 2), false)
        runBlocking { test.parseContext.processQueue() }

        assertEquals(SimpleValue(MutableBitList("000", 2), false), dynamicExpr.value)
    }
}