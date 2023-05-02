import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SignalParserTest {
    @Test
    fun testDffSimpleDeclaration() {
        val tester = LucidTester("dff testing(.clk(1));")
        tester.dffDec() // parse
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(SignalDirection.Write, dSig.direction)
        assertEquals(SignalDirection.Read, qSig.direction)

        assert(tester.hasNoErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffInit() {
        val tester = LucidTester("dff testing(.clk(1), #INIT(1));")
        tester.dffDec() // parse
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitValue(Bit.B1, constant = false, signed = false), qSig.value)

        assert(tester.hasNoErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffInitTruncate() {
        val tester = LucidTester("dff testing[3](.clk(1), #INIT(15));")
        tester.dffDec() // parse
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitListValue("111",2, constant = false, signed = false), qSig.value)

        assert(tester.hasNoErrors)
        assert(tester.hasWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffInitDimMismatch() {
        val tester = LucidTester("dff testing[3](.clk(1), #INIT({15}));")
        tester.dffDec() // parse
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitListWidth(3), qSig.value.signalWidth)

        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffArray() {
        val tester = LucidTester("dff testing[8][4][2](.clk(1));")
        tester.dffDec() // parse
        assert(tester.hasNoIssues)
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue("00",2, constant = false, signed = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.value)
        assertEquals(initValue, dSig.value)
    }

    @Test
    fun testSignedDffArray() {
        val tester = LucidTester("signed dff testing[8][4][2](.clk(1));")
        tester.dffDec() // parse

        assert(tester.hasNoIssues)

        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue("00",2, signed = true, constant = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.value)
        assertEquals(initValue, dSig.value)


    }

    @Test
    fun testAssignBlockSimple() {
        val tester = LucidTester(".clk(1), .rst(0) { dff test; }")
        tester.assignBlock()
        assert(tester.hasNoIssues)

        val dff = tester.parseContext.signal.resolve("test")

        dff as Dff

        assertEquals(BitValue(Bit.B1, constant = true, signed = false), dff.clk.value)
        assertEquals(BitValue(Bit.B0, constant = true, signed = false), dff.rst?.value)
    }

    @Test
    fun testDoubleAssign() {
        val tester = LucidTester(".clk(1), .rst(0) { dff test(.clk(0)); }")
        tester.assignBlock()
        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDoubleAssignBlocks() {
        val tester = LucidTester(".clk(1) { .clk(0) { dff test; } }")
        tester.assignBlock()
        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffSimpleStruct() {
        val tester = LucidTester("""{
            struct test { a, b[2][3], c[4] }
            dff testing<test>(.clk(1))
            }""".trimIndent())
        tester.moduleBody()
        assert(tester.hasNoIssues)

        val struct = StructType(
            "test", mutableMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", ArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )

        val dff = tester.parseContext.signal.resolve("testing")
        dff as Dff

        assertEquals(StructWidth(struct), dff.q.value.signalWidth)
    }

    @Test
    fun testDffStructArray() {
        val tester = LucidTester("""{
            struct test { a, b[2][3], c[4] }
            dff testing<test>[6](.clk(1))
            }""".trimIndent())
        tester.moduleBody()
        assert(tester.hasNoIssues)

        val struct = StructType(
            "test", mutableMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", ArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )
        val width = ArrayWidth(6, StructWidth(struct))

        val dff = tester.parseContext.signal.resolve("testing")
        dff as Dff

        assertEquals(width, dff.q.value.signalWidth)
    }
}