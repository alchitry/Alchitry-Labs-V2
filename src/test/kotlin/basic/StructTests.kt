package basic

import com.alchitry.labs2.parsers.hdl.types.StructMember
import com.alchitry.labs2.parsers.hdl.types.StructType
import com.alchitry.labs2.parsers.hdl.values.BitListWidth
import com.alchitry.labs2.parsers.hdl.values.BitWidth
import com.alchitry.labs2.parsers.hdl.values.DefinedArrayWidth
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
            "myStruct", linkedMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", DefinedArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            ),
            null
        )

        assertEquals(expected, tester.context.resolve(ctx))
    }

    @Test
    fun structOffsetTest() {
        val struct = StructType(
            "myStruct", linkedMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", DefinedArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            ),
            null
        )

        assertEquals(0, struct.offsetOf("a"))
        assertEquals(1, struct.offsetOf("b"))
        assertEquals(1 + 6, struct.offsetOf("c"))
    }
}