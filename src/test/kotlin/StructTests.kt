import com.alchitry.labs.parsers.lucidv2.types.StructMember
import com.alchitry.labs.parsers.lucidv2.types.StructType
import com.alchitry.labs.parsers.lucidv2.values.ArrayWidth
import com.alchitry.labs.parsers.lucidv2.values.BitListWidth
import com.alchitry.labs.parsers.lucidv2.values.BitWidth
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StructTests {

    @Test
    fun basicStructDecTest() = runBlocking {
        val tester = SimpleLucidTester("struct myStruct { a, b[2][3], c[4] };")
        val ctx = tester.parser.structDec().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        val expected = StructType(
            "myStruct", mutableMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", ArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )

        assertEquals(expected, tester.context.resolve(ctx))
    }
}