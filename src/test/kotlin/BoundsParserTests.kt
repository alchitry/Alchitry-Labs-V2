import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BoundsParserTests {
    @Test
    fun testBitSelectorConst() = runBlocking {
        var test = SimpleLucidTester("[0:0]")
        var tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(0..0), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasNoIssues)

        test = SimpleLucidTester("[5:0]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(0..5), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasNoIssues)

        // flipped indices
        test = SimpleLucidTester("[0:5]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        // NaN
        test = SimpleLucidTester("[bx0:5]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasErrors)
        assert(test.hasNoWarnings)

        // syntax error
        test = SimpleLucidTester("[[0:5]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasErrors)

        // too big
        test = SimpleLucidTester("[1321612161321613216354132465162316516546546516513246:0]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
    }

    @Test
    fun testBitSelectorFixedWidth() = runBlocking {
        var test = SimpleLucidTester("[0+:5]")
        var tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(0..4), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasNoIssues)

        test = SimpleLucidTester("[5-:5]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(1..5), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasNoIssues)

        test = SimpleLucidTester("[5-:2]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(4..5), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasNoIssues)

        test = SimpleLucidTester("[5+:0]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasErrors)

        test = SimpleLucidTester("[bx+:0]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.bitSelection.resolve(tree).map { it.range })
        assert(test.hasErrors)

        test = SimpleLucidTester("[bx+:1]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(true), test.context.bitSelection.resolve(tree).map { it.undefined })
        assert(test.hasNoIssues)
    }

    @Test
    fun testArrayIndex() = runBlocking {
        var test = SimpleLucidTester("[5]")
        var tree = test.parser.bitSelection()
        test.context.walk(tree)
        assert(test.hasNoIssues)
        assertEquals(listOf(5..5), test.context.bitSelection.resolve(tree).map { it.range })

        test = SimpleLucidTester("[0]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assert(test.hasNoIssues)
        assertEquals(listOf(0..0), test.context.bitSelection.resolve(tree).map { it.range })

        test = SimpleLucidTester("[bx]")
        tree = test.parser.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(true), test.context.bitSelection.resolve(tree).map { it.undefined })
        assert(test.hasNoIssues)
    }
}