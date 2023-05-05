import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BoundsParserTest {
    @Test
    fun testBitSelectorConst() {
        var test = LucidTester("[0:0]")
        var tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(0..0), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasNoIssues)

        test = LucidTester("[5:0]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(0..5), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasNoIssues)

        test = LucidTester("[0:5]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("[bx0:5]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)

        test = LucidTester("[[0:5]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasSyntaxIssues)

        test = LucidTester("[1321612161321613216354132465162316516546546516513246:0]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasErrors)
        assert(test.hasNoWarnings)
        assert(test.hasNoSyntaxIssues)
    }

    @Test
    fun testBitSelectorFixedWidth() {
        var test = LucidTester("[0+:5]")
        var tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(0..4), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasNoIssues)

        test = LucidTester("[5-:5]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(1..5), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasNoIssues)

        test = LucidTester("[5-:2]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(listOf(4..5), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasNoIssues)

        test = LucidTester("[5+:0]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasErrors)

        test = LucidTester("[bx+:0]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasErrors)
    }

    @Test
    fun testArrayIndex() {
        var test = LucidTester("[5]")
        var tree = test.bitSelection()
        test.context.walk(tree)
        assert(test.hasNoIssues)
        assertEquals(listOf(5..5), test.context.expr.resolve(tree).map{it.range})

        test = LucidTester("[0]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assert(test.hasNoIssues)
        assertEquals(listOf(0..0), test.context.expr.resolve(tree).map{it.range})

        test = LucidTester("[bx]")
        tree = test.bitSelection()
        test.context.walk(tree)
        assertEquals(emptyList<IntRange>(), test.context.expr.resolve(tree).map{it.range})
        assert(test.hasErrors)
    }
}