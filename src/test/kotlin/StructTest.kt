import com.alchitry.labs.parsers.lucidv2.parsers.ParseStage
import com.alchitry.labs.parsers.lucidv2.signals.StructMember
import com.alchitry.labs.parsers.lucidv2.signals.StructType
import com.alchitry.labs.parsers.lucidv2.values.ArrayWidth
import com.alchitry.labs.parsers.lucidv2.values.BitListWidth
import com.alchitry.labs.parsers.lucidv2.values.BitWidth
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StructTest {

    @Test
    fun basicStructDecTest() {
        val tester = LucidTester("struct test { a, b[2][3], c[4] };", ParseStage.Modules)
        val ctx = tester.structDec().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        val expected = StructType(
            "test", mutableMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", ArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )

        assertEquals(expected, tester.context.signal.resolveStructType(ctx))
    }
}