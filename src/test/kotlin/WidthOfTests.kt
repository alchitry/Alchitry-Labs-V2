import com.alchitry.labs2.parsers.hdl.asConstExpr
import com.alchitry.labs2.parsers.hdl.values.ArrayValue
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WidthOfTests {

    @Test
    fun basicWidthTest() = runBlocking {
        val tester = SimpleLucidTester("\$width(8b1);")
        val ctx = tester.parser.expr().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        assertEquals(
            BitListValue(8, signed = false).asConstExpr(),
            tester.context.resolve(ctx)
        )
    }

    @Test
    fun multiDimWidthTest() = runBlocking {
        val tester = SimpleLucidTester("\$width({8b1, 8b1});")
        val ctx = tester.parser.expr().also { tester.context.walk(it) }

        assert(tester.hasNoIssues)

        val v = tester.context.resolve(ctx)
        assertEquals(
            ArrayValue(
                listOf(
                    BitListValue(2, 4, signed = false),
                    BitListValue(8, 4, signed = false),
                )
            ).asConstExpr(),
            v
        )
    }
}