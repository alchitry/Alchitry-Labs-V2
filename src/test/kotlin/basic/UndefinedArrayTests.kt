package basic

import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.SignalResolver
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.hdl.types.SignalDirection
import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import com.alchitry.labs2.parsers.hdl.values.*
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UndefinedArrayTests {
    private fun undefinedSignal(name: String, width: SignalWidth = UndefinedArrayWidth(UndefinedSimpleWidth())) =
        Signal(name, SignalDirection.Read, null, UndefinedValue(width), ExprType.Constant)

    private fun testExprWithLocalValues(expr: String, vararg localValues: Signal): SimpleLucidTester {
        val resolver = object : SignalResolver {
            override fun resolve(ctx: ParserRuleContext, name: String): SignalOrParent? {
                return localValues.firstOrNull { it.name == name }
            }
        }
        return SimpleLucidTester(expr, resolver)
    }

    private fun SimpleLucidTester.assertNoIssues() =
        assert(hasNoIssues) { project.notationManager.getReport().toString() }

    @Test
    fun undefinedBitwiseInvertDefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("~SIZE", undefinedSignal("SIZE", UndefinedArrayWidth(BitListWidth(4))))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is UndefinedValue)
        val width = result?.width
        test.assertNoIssues()
        assert(width is UndefinedArrayWidth)
        assertEquals(BitListWidth(4), (width as UndefinedArrayWidth).next)
    }

    @Test
    fun undefinedConcatenation() = runBlocking {
        val test = testExprWithLocalValues("c{{4b1}, SIZE, {4d0}}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue) { result.toString() }
        assertEquals(UndefinedArrayWidth(UndefinedSimpleWidth()), result?.width)
    }

    @Test
    fun undefinedConcatenation2() = runBlocking {
        val test = testExprWithLocalValues("c{SIZE, {4b1}, {4d0, 4d2}}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue)
        assertEquals(UndefinedArrayWidth(UndefinedSimpleWidth()), result?.width)
    }

    @Test
    fun undefinedDuplication() = runBlocking {
        val test = testExprWithLocalValues("4x{SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedArrayWidth) { result?.width.toString() }
    }

    @Test
    fun undefinedArray() = runBlocking {
        val test = testExprWithLocalValues("{SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is ArrayValue && result.elements.all { it is UndefinedValue }) { "Value: $result" }
        val width = result?.width
        test.assertNoIssues()
        assertEquals(DefinedArrayWidth(1, UndefinedArrayWidth(UndefinedSimpleWidth())), width)
    }

    @Test
    fun partiallyUndefinedArray() = runBlocking {
        val test = testExprWithLocalValues("{SIZE, {4b0}}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is ArrayValue) { "Value: $result" }
        result as ArrayValue
        assert(result.elements[1] is UndefinedValue)
        assertEquals(DefinedArrayWidth(1, BitListWidth(4)), result.elements[1].width)
        assertEquals(ArrayValue(listOf(BitListValue(0, 4, signed = false))), result.elements[0])
        assert(result.width.size == 2)
        assertEquals(DefinedArrayWidth(1, BitListWidth(4)), result.width.next)
    }

    @Test
    fun partiallyUndefinedArray2() = runBlocking {
        val test = testExprWithLocalValues("{{4b0}, SIZE}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is ArrayValue) { "Value: $result" }
        result as ArrayValue
        test.assertNoIssues()
        assert(result.elements.size == 2)
        assert(result.elements[0] is UndefinedValue)
        assertEquals(ArrayValue(listOf(BitListValue(0, 4, signed = false))), result.elements[1])
        assert(result.width.size == 2)
        assertEquals(DefinedArrayWidth(1, BitListWidth(4)), result.width.next)
    }

    @Test
    fun undefinedBooleanInvert() = runBlocking {
        val test = testExprWithLocalValues("!SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is UndefinedValue)
        assert(result?.width is BitWidth) { "Width was ${result?.width}" }
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedBitwiseInvert() = runBlocking {
        val test = testExprWithLocalValues("~SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue)
        assertEquals(UndefinedArrayWidth(UndefinedSimpleWidth()), result?.width)
    }

    @Test
    fun undefinedBitwise() = runBlocking {
        val test = testExprWithLocalValues("SIZE & {4b0010}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue) { result.toString() }
        assertEquals(DefinedArrayWidth(1, BitListWidth(4)), result?.width)
    }

    @Test
    fun undefinedBitwise2() = runBlocking {
        val test = testExprWithLocalValues("SIG & SIZE", undefinedSignal("SIZE"), undefinedSignal("SIG"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue)
        assertEquals(UndefinedArrayWidth(UndefinedSimpleWidth()), result?.width)
    }

    @Test
    fun undefinedReduction() = runBlocking {
        val test = testExprWithLocalValues("&SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is UndefinedValue)
        assert(result?.width is BitWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedEqual() = runBlocking {
        val test = testExprWithLocalValues("SIG == SIZE", undefinedSignal("SIZE"), undefinedSignal("SIG"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is UndefinedValue) { "Result $result" }
        assert(result?.width is BitWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedEqual2() = runBlocking {
        val test = testExprWithLocalValues("{4b0101} == SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is UndefinedValue) { "Result $result" }
        assert(result?.width is BitWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedLogical() = runBlocking {
        val test = testExprWithLocalValues("4b0101 || SIZE", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue) { "Result $result" }
        assert(result?.width is BitWidth)
    }

    @Test
    fun undefinedTernary2() = runBlocking {
        val test = testExprWithLocalValues("1 ? SIZE : {4b0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue) { "Result $result" }
        assertEquals(DefinedArrayWidth(1, BitListWidth(4)), result?.width)
    }

    @Test
    fun undefinedTernary3() = runBlocking {
        val test = testExprWithLocalValues("0 ? SIZE : {4b0}", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assertEquals(ArrayValue(listOf(BitListValue(0, 4, signed = false))), result)
        assertEquals(DefinedArrayWidth(1, BitListWidth(4)), result?.width)
        assert(test.hasNoIssues)
    }


    @Test
    fun undefinedReverse() = runBlocking {
        val test = testExprWithLocalValues("\$reverse(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is UndefinedValue)
        assertEquals(UndefinedArrayWidth(UndefinedSimpleWidth()), result?.width)
    }

    @Test
    fun undefinedFlatten() = runBlocking {
        val test = testExprWithLocalValues("\$flatten(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        assert(result is UndefinedValue)
        assert(result?.width is UndefinedSimpleWidth)
        assert(test.hasNoIssues)
    }

    @Test
    fun undefinedWidth() = runBlocking {
        val test = testExprWithLocalValues("\$width(SIZE)", undefinedSignal("SIZE"))
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is ArrayValue)
        assert((result as ArrayValue).elements[0] is UndefinedValue)
        assert(result.elements[1] is UndefinedValue)
        assertEquals(DefinedArrayWidth(2, UndefinedSimpleWidth()), result.width)
    }

    @Test
    fun undefinedWidth2() = runBlocking {
        val test = testExprWithLocalValues(
            "\$width(SIZE)", undefinedSignal(
                "SIZE", UndefinedArrayWidth(
                    DefinedSimpleWidth(10)
                )
            )
        )
        val tree = test.parser.expr().also { test.context.walk(it) }
        val result = test.context.expr.resolve(tree)?.value
        test.assertNoIssues()
        assert(result is ArrayValue)
        assert((result as ArrayValue).elements[0] is UndefinedValue)
        assertEquals(BitListValue(10, signed = false), result.elements[1])
        assertEquals(DefinedArrayWidth(2, BitListWidth(4)), result.width)
    }
}