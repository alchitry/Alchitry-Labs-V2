import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.values.DynamicExpr
import com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.values.MutableBitList
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DynamicExprTest {
    @Test
    fun basicDynamicExpr() {
        val test = LucidTester("~test")

        val signal = Signal("test", SignalDirection.Both, null, SimpleValue(MutableBitList("110", 2), false))

        test.parseContext.testingSignalResolver = TestSignalResolver(signal)

        val exprCtx = test.expr()

        val dynamicExpr = DynamicExpr(exprCtx, test.parseContext, SimpleValue(MutableBitList("xxx", 2), false), listOf(signal))

        var count = 0
        runBlocking {
            coroutineScope {
                launch mainJob@{
                    launch {
                        repeat(10) {
                            delay(100)
                            if (!test.parseContext.scope.isActive) {
                                this@coroutineScope.cancel("Error in context scope") // canceling here causes an error
                            }
                        }
                        this@coroutineScope.cancel("Timed out")
                    }
                    launch {
                        dynamicExpr.collect {
                            when (count) {
                                0 -> {
                                    if (!it.isNumber())
                                        return@collect

                                    assertEquals(SimpleValue(MutableBitList("001", 2), false), it)
                                    signal.value = SimpleValue(MutableBitList("011", 2), false)
                                    count++
                                }

                                1 -> {
                                    assertEquals(SimpleValue(MutableBitList("100", 2), false), it)
                                    this@mainJob.cancel() // canceling here passes the test
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}