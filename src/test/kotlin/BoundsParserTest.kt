import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BoundsParserTest {
    @Test
    fun testBitSelectorConst() {
        var test = LucidTester("[0:0]")
        var tree = test.bitSelection()
        assertEquals(listOf(0..0), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[5:0]")
        tree = test.bitSelection()
        assertEquals(listOf(0..5), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[0:5]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("[bx0:5]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("[[0:5]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasSyntaxIssues)

        test = LucidTester("[1321612161321613216354132465162316516546546516513246:0]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitSelectorFixedWidth() {
        var test = LucidTester("[0+:5]")
        var tree = test.bitSelection()
        assertEquals(listOf(0..4), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[5-:5]")
        tree = test.bitSelection()
        assertEquals(listOf(1..5), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[5-:2]")
        tree = test.bitSelection()
        assertEquals(listOf(4..5), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[5+:0]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasErrors)

        test = LucidTester("[bx+:0]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasErrors)
    }

    @Test
    fun testArrayIndex() {
        var test = LucidTester("[5]")
        var tree = test.bitSelection()
        assertEquals(listOf(5..5), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[0]")
        tree = test.bitSelection()
        assertEquals(listOf(0..0), test.parseContext.expr.resolve(tree))
        assert(test.hasNoIssues)

        test = LucidTester("[bx]")
        tree = test.bitSelection()
        assertEquals(emptyList<IntRange>(), test.parseContext.expr.resolve(tree))
        assert(test.hasErrors)
    }
}