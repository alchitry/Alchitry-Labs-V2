import com.alchitry.labs2.parsers.BitUtil.widthOfMult
import com.alchitry.labs2.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.types.SignalOrParent
import com.alchitry.labs2.parsers.lucidv2.values.*
import helpers.LucidTester
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UndefinedValueTests {
    @Test
    fun counterModuleNoSize() = runBlocking {
        val tester = LucidTester(
            """
            module counter #(
                SIZE : SIZE > 0, // Width of the output
                DIV = 0  : DIV >= 0, // number of bits to use as divisor
                TOP = 0  : TOP >= 0, // max value, 0 = none

                // direction to count, use 1 for up and 0 for down
                UP = 1 : UP == 1 || UP == 0
            )(
                input clk,
                input rst,
                output value[SIZE]
            ) {

                .clk(clk), .rst(rst) {
                    dff ctr[SIZE+DIV]
                }

                const MAX_VALUE = TOP << DIV // value when maxed out

                always {
                    value = ctr.q[SIZE+DIV-1-:SIZE] // set the output

                    if (UP) { // when this is an up counter
                        ctr.d = ctr.q + 1 // increase
                        if (TOP != 0 && ctr.q == MAX_VALUE) { // reached the top?
                            ctr.d = 0 // reset
                        }
                    } else { // down counter
                        ctr.d = ctr.q - 1 // decrease
                        if (TOP != 0 && ctr.q == 0) { // reached the bottom?
                            ctr.d = MAX_VALUE // reset to top
                        }
                    }
                }
            }
        """.trimIndent().toSourceFile("counter.luc")
        )
        val context = tester.fullParse(testing = true).context
        context.notationCollector.assertNoErrors()
        assertEquals(1, context.notationCollector.getAllWarnings().size)
    }

    private fun undefinedSignal(name: String, width: SignalWidth = UndefinedSimpleWidth()) =
        Signal(name, SignalDirection.Read, null, UndefinedValue(true, width))

    private fun testExprWithLocalValues(expr: String, vararg localValues: Signal): SimpleLucidTester {
        val resolver = object : SignalResolver {
            override fun resolve(name: String): SignalOrParent? {
                return localValues.firstOrNull { it.name == name }
            }
        }
        return SimpleLucidTester(expr, resolver)
    }

    @Test
    fun undefinedBitwiseInvertDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("~SIZE", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assertEquals(BitListWidth(4), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedConcatenation() = runBlocking {
        val test = testExprWithLocalValues("c{4b1, SIZE, 2d0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedConcatenation2() = runBlocking {
        val test = testExprWithLocalValues("c{SIZE, 4b1, 2d0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedConcatDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("c{4b1, SIZE, 2d0}", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assertEquals(BitListWidth(10), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDuplication() = runBlocking {
        val test = testExprWithLocalValues("4x{SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDuplicationAmount() = runBlocking {
        val test = testExprWithLocalValues("SIZEx{4b0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDuplicationDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("4x{SIZE}", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assertEquals(BitListWidth(16), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedArray() = runBlocking {
        val test = testExprWithLocalValues("{SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is ArrayValue && result.elements.all { it is UndefinedValue }) { "Value: $result" }
        val width = result?.width
        assert(width is ArrayWidth && width.size == 1 && width.next is UndefinedSimpleWidth) { "Width: $width" }
        assert(test.hasNoIssues)
    }

    @Test
    fun partiallyUndefinedArray() = runBlocking {
        val test = testExprWithLocalValues("{SIZE, 4b0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is ArrayValue) { "Value: $result" }
        result as ArrayValue
        assert(result.elements[1] is UndefinedValue)
        assertEquals(BitListValue(0, 4, constant = true, signed = false), result.elements[0])
        assert(result.width.size == 2)
        assertEquals(BitListWidth(4), result.width.next)
        assert(test.hasNoIssues)
    }

    @Test
    fun partiallyUndefinedArray2() = runBlocking {
        val test = testExprWithLocalValues("{4b0, SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is ArrayValue) { "Value: $result" }
        result as ArrayValue
        assert(result.elements.size == 2)
        assert(result.elements[0] is UndefinedValue)
        assertEquals(BitListValue(0, 4, constant = true, signed = false), result.elements[1])
        assert(result.width.size == 2)
        assertEquals(BitListWidth(4), result.width.next)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedNegation() = runBlocking {
        val test = testExprWithLocalValues("-SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedNegationDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("-SIZE", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assertEquals(BitListWidth(4), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBooleanInvert() = runBlocking {
        val test = testExprWithLocalValues("!SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is BitWidth) { "Width was ${result?.width}" }
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBitwiseInvert() = runBlocking {
        val test = testExprWithLocalValues("~SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth) { "Width was ${result?.width}" }
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedAddition() = runBlocking {
        val test = testExprWithLocalValues("SIZE + 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedMultiplication() = runBlocking {
        val test = testExprWithLocalValues("SIZE * 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedMultiplicationDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("SIZE * 4b0010", undefinedSignal("SIZE", BitListWidth(3)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assertEquals(BitListWidth(widthOfMult(4, 3)), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDivision() = runBlocking {
        val test = testExprWithLocalValues("SIZE / 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDivision2() = runBlocking {
        val test = testExprWithLocalValues("4b0000 / SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)
        assert(result is UndefinedValue)
        assertEquals(BitListWidth(4), result?.width)
        assert(test.hasNoIssues)
    }

}