import com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.values.ArrayWidth
import com.alchitry.labs.parsers.lucidv2.values.SimpleWidth
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StructTest {

    @Test
    fun basicStructDecTest() {
        val tester = LucidTester("struct test { a, b[2][3], c[4] };")
        val ctx = tester.structDec()

        assert(tester.hasNoIssues)

        val expected = StructType("test", mutableMapOf(
            "a" to SimpleWidth(1),
            "b" to ArrayWidth(2, SimpleWidth(3)),
            "c" to SimpleWidth(4)
        ))

        assertEquals(expected, tester.parseContext.signal.resolveStructType("test"))
    }
}