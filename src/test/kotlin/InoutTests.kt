import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.types.ports.Inout
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import com.alchitry.labs2.parsers.lucidv2.values.BitListWidth
import com.alchitry.labs2.parsers.notations.NotationManager
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InoutTests {
    @Test
    fun basicInoutTest() {
        val inout = Inout("test", ProjectContext(NotationManager()), null, BitListWidth(8), false)
        runBlocking {
            var ct = 0
            val readJob = launch(start = CoroutineStart.UNDISPATCHED) {
                inout.external.valueFlow.collect {
                    when (ct++) {
                        0 -> assertEquals(BitListValue("zzzz1100", 2, 8, false, false), it)
                        1 -> assertEquals(BitListValue("01011100", 2, 8, false, false), it)
                        2 -> assertEquals(BitListValue("0101x100", 2, 8, false, false), it)
                    }
                }
            }
            inout.internal.write(BitListValue("zzzz1100", 2, 8, false, false))
            assertEquals(BitListValue("zzzz1100", 2, 8, false, false), inout.external.read())
            inout.external.write(BitListValue("0101zzzz", 2, 8, false, false))
            assertEquals(BitListValue("01011100", 2, 8, false, false), inout.internal.read())
            inout.external.write(BitListValue("01010zzz", 2, 8, false, false))
            assertEquals(BitListValue("0101x100", 2, 8, false, false), inout.internal.read())

            // this shouldn't trigger a new value for readJob since this write will produce the same value
            inout.external.write(BitListValue("01011zzz", 2, 8, false, false))
            assertEquals(BitListValue("0101x100", 2, 8, false, false), inout.internal.read())

            readJob.cancel()
            assert(ct == 3)
        }
    }
}