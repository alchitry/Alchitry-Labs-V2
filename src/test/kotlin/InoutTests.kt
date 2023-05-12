import com.alchitry.labs.parsers.lucidv2.types.Inout
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import com.alchitry.labs.parsers.lucidv2.values.BitListWidth
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InoutTests {
    @Test
    fun basicInoutTest() {
        val inout = Inout("test", null, BitListWidth(8), false)
        runBlocking {
            inout.internal.write(BitListValue("zzzz1100", 2, 8, false, false))
            assertEquals(BitListValue("zzzz1100", 2, 8, false, false), inout.external.read())
            inout.external.write(BitListValue("0101zzzz", 2, 8, false, false))
            assertEquals(BitListValue("01011100", 2, 8, false, false), inout.internal.read())
            inout.external.write(BitListValue("01010zzz", 2, 8, false, false))
            assertEquals(BitListValue("0101x100", 2, 8, false, false), inout.internal.read())
        }
    }
}