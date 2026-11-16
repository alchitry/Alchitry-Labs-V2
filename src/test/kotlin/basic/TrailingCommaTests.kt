package basic

import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.grammar.LucidParser
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import kotlin.test.Test
import kotlin.test.assertTrue

internal class TrailingCommaTests {
    private fun parseSource(source: String): LucidParser {
        val lexer = LucidLexer(CharStreams.fromString(source)).also { it.removeErrorListeners() }
        val parser = LucidParser(CommonTokenStream(lexer))
        return parser
    }

    @Test
    fun testModuleParamListTrailingComma() {
        val src = """
            module myModule #(
                PARAM_A = 1,
                PARAM_B = 2,
            ) (
                input clk,
                output out,
            ) {
                always {}
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.source()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with trailing commas in param and port lists")
    }

    @Test
    fun testModulePortListSingleTrailingComma() {
        val src = """
            module myModule (
                input clk,
            ) {
                always {}
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.source()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with single port trailing comma")
    }

    @Test
    fun testEnumDecTrailingComma() {
        val src = """
            enum MyState {
                STATE_IDLE,
                STATE_RUN,
                STATE_DONE,
            };
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.enumDec()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with trailing comma in enum")
    }

    @Test
    fun testStructDecTrailingComma() {
        val src = """
            struct myStruct {
                a,
                b[8],
            };
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.structDec()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Errors: " + tester.context.notationCollector.getReport())
    }

    @Test
    fun testStructConstTrailingComma() {
        val src = """
            <myStruct>(
                .a(1),
                .b(2),
            )
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.structConst()
        assertTrue(tester.hasNoErrors, "Errors: " + tester.context.notationCollector.getReport())
    }

    @Test
    fun testInstConsTrailingComma() {
        val src = """
            dff myDff(
                .clk(1),
                .rst(1),
            );
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.dffDec()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Errors: " + tester.context.notationCollector.getReport())
    }

    @Test
    fun testAssignBlockConListTrailingComma() {
        val src = """
            .clk(1),
            .rst(1), {
                dff a;
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.assignBlock()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Errors: " + tester.context.notationCollector.getReport())
    }

    @Test
    fun testFunctionBlockTrailingComma() {
        val src = """
            fun myFunc(
                arg1,
                arg2[8],
            ) {
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.functionBlock()
        assertTrue(tester.hasNoErrors, "Errors: " + tester.context.notationCollector.getReport())
    }

    @Test
    fun testRepeatStatTrailingComma() {
        val src = """
            repeat (i, 4,) {
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.repeatStat()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with trailing comma in repeatStat")
    }

    @Test
    fun testFunctionCallTrailingComma() {
        val src = """
            ${'$'}cdiv(8, 4,)
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.expr()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with trailing comma in built-in function call")
    }

    @Test
    fun testExprConcatTrailingComma() {
        val src = """
            c{
                8h12,
                8h34,
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.expr()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with trailing comma in concatenation expression")
    }

    @Test
    fun testExprArrayTrailingComma() {
        val src = """
            {
                8h12,
                8h34,
            }
        """.trimIndent()
        val tester = SimpleLucidTester(src)
        val tree = tester.parser.expr()
        runBlocking { tester.context.walk(tree) }
        assertTrue(tester.hasNoErrors, "Expected no errors with trailing comma in array expression")
    }

    @Test
    fun testSyntaxErrorsInListsNotObfuscated() {
        // Double commas should produce a syntax error at the second comma
        val doubleCommaSrc = """
            module myModule (
                input clk,,
                output out
            ) {
                always {}
            }
        """.trimIndent()
        val tester1 = SimpleLucidTester(doubleCommaSrc)
        val tree1 = tester1.parser.source()
        runBlocking { tester1.context.walk(tree1) }
        assertTrue(tester1.hasErrors, "Expected syntax error for double comma")

        // Missing item before comma
        val missingItemSrc = """
            enum State {
                ,
                STATE_A
            };
        """.trimIndent()
        val tester2 = SimpleLucidTester(missingItemSrc)
        val tree2 = tester2.parser.enumDec()
        runBlocking { tester2.context.walk(tree2) }
        assertTrue(tester2.hasErrors, "Expected syntax error for leading comma in enum")

        // Invalid token inside list
        val invalidTokenSrc = """
            c{ 8h12, +, 8h34 }
        """.trimIndent()
        val tester3 = SimpleLucidTester(invalidTokenSrc)
        val tree3 = tester3.parser.expr()
        runBlocking { tester3.context.walk(tree3) }
        assertTrue(tester3.hasErrors, "Expected syntax error for invalid expression element in concat")
    }
}
