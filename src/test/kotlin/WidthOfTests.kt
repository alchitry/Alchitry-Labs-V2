import com.alchitry.labs.parsers.lucidv2.values.ArrayValue
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import helpers.SimpleLucidTester
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WidthOfTests {

    @Test
    fun basicWidthTest() {
        val tester = SimpleLucidTester("\$widthOf(8b1);")
        val ctx = tester.parser.expr().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        assertEquals(
            BitListValue(8, constant = true, signed = false),
            tester.context.resolve(ctx)
        )
    }

    @Test
    fun multiDimWidthTest() {
        val tester = SimpleLucidTester("\$widthOf({8b1, 8b1});")
        val ctx = tester.parser.expr().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue(2, 4, constant = true, signed = false),
                    BitListValue(8, 4, constant = true, signed = false),
                )
            ),
            tester.context.resolve(ctx)
        )
    }
}