import com.alchitry.labs2.parsers.BitUtil.widthOfMult
import com.alchitry.labs2.parsers.lucidv2.context.SignalResolver
import com.alchitry.labs2.parsers.lucidv2.parsers.ExprType
import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.values.*
import helpers.LucidTester
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
                    dff testArray[SIZE][8]
                }

                const MAX_VALUE = TOP << DIV // value when maxed out

                always {
                    value = ctr.q[SIZE+DIV-1-:SIZE] // set the output
                    
                    testArray.d[0] = testArray.q[0] + 1

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
        Signal(name, SignalDirection.Read, null, UndefinedValue(width), ExprType.Constant)

    private fun testExprWithLocalValues(expr: String, vararg localValues: Signal): SimpleLucidTester {
        val resolver = object : SignalResolver {
            override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? {
                return localValues.firstOrNull { it.name == name }
            }
        }
        return SimpleLucidTester(expr, resolver)
    }

    @Test
    fun undefinedSignals() = runBlocking {
        val signal =
            Signal(
                "testSig",
                SignalDirection.Both,
                null,
                UndefinedValue(UndefinedArrayWidth(BitListWidth(8))),
                ExprType.Dynamic
            )
        assertEquals(UndefinedValue(UndefinedArrayWidth(BitListWidth(8))), signal.read())

        val subSignal = signal.select(listOf(SignalSelector.Bits(2..2, SelectionContext.Constant)))
        assertEquals(UndefinedValue(DefinedSimpleWidth(8)), subSignal.read())

        subSignal.write(BitListValue(2, 4, signed = false))
        assertEquals(UndefinedValue(DefinedSimpleWidth(8)), subSignal.read())

        // check that writing to the entire signal replaces the UndefinedValue with a defined one
        val fullValue = ArrayValue(listOf(BitListValue(2, 8, signed = false)))
        signal.write(fullValue)
        assertEquals(fullValue, signal.read())
    }

    @Test
    fun undefinedBitwiseInvertDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("~SIZE", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(4), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedConcatenation() = runBlocking {
        val test = testExprWithLocalValues("c{4b1, SIZE, 2d0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedConcatenation2() = runBlocking {
        val test = testExprWithLocalValues("c{SIZE, 4b1, 2d0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedConcatDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("c{4b1, SIZE, 2d0}", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(10), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDuplication() = runBlocking {
        val test = testExprWithLocalValues("4x{SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDuplicationAmount() = runBlocking {
        val test = testExprWithLocalValues("SIZEx{4b0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDuplicationDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("4x{SIZE}", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(16), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedArray() = runBlocking {
        val test = testExprWithLocalValues("{SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is ArrayValue && result.elements.all { it is UndefinedValue }) { "Value: $result" }
        val width = result?.width
        assert(width is DefinedArrayWidth && width.size == 1 && width.next is UndefinedSimpleWidth) { "Width: $width" }
        assert(test.hasNoIssues)
    }

    @Test
    fun partiallyUndefinedArray() = runBlocking {
        val test = testExprWithLocalValues("{SIZE, 4b0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is ArrayValue) { "Value: $result" }
        result as ArrayValue
        assert(result.elements[1] is UndefinedValue)
        assertEquals(BitListValue(0, 4, signed = false), result.elements[0])
        assert(result.width.size == 2)
        assertEquals(BitListWidth(4), result.width.next)
        assert(test.hasNoIssues)
    }

    @Test
    fun partiallyUndefinedArray2() = runBlocking {
        val test = testExprWithLocalValues("{4b0, SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is ArrayValue) { "Value: $result" }
        result as ArrayValue
        assert(result.elements.size == 2)
        assert(result.elements[0] is UndefinedValue)
        assertEquals(BitListValue(0, 4, signed = false), result.elements[1])
        assert(result.width.size == 2)
        assertEquals(BitListWidth(4), result.width.next)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedNegation() = runBlocking {
        val test = testExprWithLocalValues("-SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedNegationDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("-SIZE", undefinedSignal("SIZE", BitListWidth(4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(4), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBooleanInvert() = runBlocking {
        val test = testExprWithLocalValues("!SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<BitWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBitwiseInvert() = runBlocking {
        val test = testExprWithLocalValues("~SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedAddition() = runBlocking {
        val test = testExprWithLocalValues("SIZE + 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedMultiplication() = runBlocking {
        val test = testExprWithLocalValues("SIZE * 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedMultiplicationDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("SIZE * 4b0010", undefinedSignal("SIZE", BitListWidth(3)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(widthOfMult(4, 3)), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDivision() = runBlocking {
        val test = testExprWithLocalValues("SIZE / 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedDivision2() = runBlocking {
        val test = testExprWithLocalValues("4b0000 / SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(4), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedShift() = runBlocking {
        val test = testExprWithLocalValues("SIZE << 2", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedShift2() = runBlocking {
        val test = testExprWithLocalValues("2 <<< SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedShiftDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("SIZE <<< 2", undefinedSignal("SIZE", BitListWidth(5)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(7), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBitwise() = runBlocking {
        val test = testExprWithLocalValues("SIZE & 4b0010", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(4), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBitwise2() = runBlocking {
        val test = testExprWithLocalValues("SIG & SIZE", undefinedSignal("SIZE"), undefinedSignal("SIG"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBitwise2DefinedWidth() = runBlocking {
        val test = testExprWithLocalValues(
            "SIG & SIZE",
            undefinedSignal("SIZE", BitListWidth(4)),
            undefinedSignal("SIG", BitListWidth(6))
        )
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(6), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedReduction() = runBlocking {
        val test = testExprWithLocalValues("&SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<BitWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedEqual() = runBlocking {
        val test = testExprWithLocalValues("SIG == SIZE", undefinedSignal("SIZE"), undefinedSignal("SIG"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<BitWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedEqual2() = runBlocking {
        val test = testExprWithLocalValues("4b0101 == SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<BitWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedCompare() = runBlocking {
        val test = testExprWithLocalValues("4b0101 <= SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<BitWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedLogical() = runBlocking {
        val test = testExprWithLocalValues("4b0101 || SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<BitWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedTernary() = runBlocking {
        val test = testExprWithLocalValues("SIZE ? 4b1 : 4b0", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(4), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedTernary2() = runBlocking {
        val test = testExprWithLocalValues("1 ? SIZE : 4b0", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(4), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedTernary3() = runBlocking {
        val test = testExprWithLocalValues("0 ? SIZE : 4b0", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertEquals(BitListValue(0, 4, signed = false), result)
        assertEquals(BitListWidth(4), result?.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedClog2() = runBlocking {
        val test = testExprWithLocalValues("${'$'}clog2(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedPow() = runBlocking {
        val test = testExprWithLocalValues("${'$'}pow(2,SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedPow2() = runBlocking {
        val test = testExprWithLocalValues("${'$'}pow(SIZE,2)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedReverse() = runBlocking {
        val test = testExprWithLocalValues("${'$'}reverse(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedFlatten() = runBlocking {
        val test = testExprWithLocalValues("${'$'}flatten(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedFlattenDefinedWidth() = runBlocking {
        val test =
            testExprWithLocalValues(
                "${'$'}flatten(SIZE)",
                undefinedSignal("SIZE", DefinedArrayWidth(2, BitListWidth(4)))
            )
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(8), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBuild() = runBlocking {
        val test = testExprWithLocalValues("${'$'}build(SIZE, 3, 2)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        val width = result.width
        assert(
            width is DefinedArrayWidth &&
                    width.size == 3 &&
                    width.next is DefinedArrayWidth &&
                    (width.next as DefinedArrayWidth).size == 2 &&
                    (width.next as DefinedArrayWidth).next is UndefinedSimpleWidth
        )
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBuildDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("${'$'}build(SIZE, 3, 2)", undefinedSignal("SIZE", BitListWidth(3 * 2 * 4)))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(DefinedArrayWidth(3, DefinedArrayWidth(2, BitListWidth(4))), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBuild2() = runBlocking {
        val test = testExprWithLocalValues("${'$'}build(b111100001111, SIZE, 2)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        val width = result.width
        assert(
            width is UndefinedArrayWidth &&
                    width.next is DefinedArrayWidth &&
                    (width.next as DefinedArrayWidth).size == 2 &&
                    (width.next as DefinedArrayWidth).next is UndefinedSimpleWidth
        )
        assert(test.hasNoErrors)
    }

    @Test
    fun undefinedSigned() = runBlocking {
        val test = testExprWithLocalValues("${'$'}signed(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedUnsigned() = runBlocking {
        val test = testExprWithLocalValues("${'$'}unsigned(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedCdiv() = runBlocking {
        val test = testExprWithLocalValues("${'$'}cdiv(SIZE, 2)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedResize() = runBlocking {
        val test = testExprWithLocalValues("${'$'}resize(SIZE, 8)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertEquals(BitListWidth(8), result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("${'$'}width(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedFixedPoint() = runBlocking {
        val test = testExprWithLocalValues("${'$'}fixedPoint(3.145, SIZE, 4)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedCFixedPoint() = runBlocking {
        val test = testExprWithLocalValues("${'$'}cFixedPoint(3.145, SIZE, 4)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedFFixedPoint() = runBlocking {
        val test = testExprWithLocalValues("${'$'}fFixedPoint(3.145, SIZE, 4)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertIs<UndefinedValue>(result)
        assertIs<UndefinedSimpleWidth>(result.width)
        assert(test.hasNoIssues)
    }
}