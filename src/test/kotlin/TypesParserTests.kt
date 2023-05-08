import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.*
import helpers.SimpleLucidTester
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TypesParserTests {
    @Test
    fun testDffSimpleDeclaration() {
        val tester = SimpleLucidTester("dff testing(.clk(1));")
        tester.context.walk(tester.dffDec()) // parse
        val dff = tester.context.types.resolve("testing")

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
    fun testDffArrayClk() {
        val tester = SimpleLucidTester("dff testing(.clk({1}));")
        tester.context.walk(tester.dffDec()) // parse
        tester.context.types.resolve("testing")

        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasErrors)
    }

    @Test
    fun testDffArrayRst() {
        val tester = SimpleLucidTester("dff testing(.clk(1), .rst({1}));")
        tester.context.walk(tester.dffDec()) // parse
        tester.context.types.resolve("testing")

        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasErrors)
    }

    @Test
    fun testDffWideClk() {
        val tester = SimpleLucidTester("dff testing(.clk(2b11));")
        tester.context.walk(tester.dffDec()) // parse
        tester.context.types.resolve("testing")

        assert(tester.hasNoErrors)
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasWarnings)
    }

    @Test
    fun testDffWideRst() {
        val tester = SimpleLucidTester("dff testing(.clk(1), .rst(2b11));")
        tester.context.walk(tester.dffDec()) // parse
        tester.context.types.resolve("testing")

        assert(tester.hasNoErrors)
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasWarnings)
    }

    @Test
    fun testDffInit() {
        val tester = SimpleLucidTester("dff testing(.clk(1), #INIT(1));")
        tester.context.walk(tester.dffDec()) // parse
        val dff = tester.context.types.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitValue(Bit.B1, constant = false, signed = false), qSig.get(tester.context.evalContext))

        assert(tester.hasNoErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffInitTruncate() {
        val tester = SimpleLucidTester("dff testing[3](.clk(1), #INIT(15));")
        tester.context.walk(tester.dffDec()) // parse
        val dff = tester.context.types.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitListValue("111", 2, constant = false, signed = false), qSig.get(tester.context.evalContext))

        assert(tester.hasNoErrors)
        assert(tester.hasWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffInitDimMismatch() {
        val tester = SimpleLucidTester("dff testing[3](.clk(1), #INIT({15}));")
        tester.context.walk(tester.dffDec()) // parse
        val dff = tester.context.types.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitListWidth(3), qSig.get(tester.context.evalContext).signalWidth)

        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffArray() {
        val tester = SimpleLucidTester("dff testing[8][4][2](.clk(1));")
        tester.context.walk(tester.dffDec()) // parse
        assert(tester.hasNoIssues)
        val dff = tester.context.types.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue("00", 2, constant = false, signed = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.get(tester.context.evalContext))
        assertEquals(initValue, dSig.get(tester.context.evalContext))
    }

    @Test
    fun testSignedDffArray() {
        val tester = SimpleLucidTester("signed dff testing[8][4][2](.clk(1));")
        tester.context.walk(tester.dffDec()) // parse

        assert(tester.hasNoIssues)

        val dff = tester.context.types.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue("00", 2, signed = true, constant = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.get(tester.context.evalContext))
        assertEquals(initValue, dSig.get(tester.context.evalContext))


    }

    @Test
    fun testAssignBlockSimple() {
        val tester = SimpleLucidTester(".clk(1), .rst(0) { dff test; }")
        tester.context.walk(tester.assignBlock())
        assert(tester.hasNoIssues)

        val dff = tester.context.types.resolve("test")

        dff as Dff

        assertEquals(BitValue(Bit.B1, constant = true, signed = false), dff.clk.value)
        assertEquals(BitValue(Bit.B0, constant = true, signed = false), dff.rst?.value)
    }

    @Test
    fun testDoubleAssign() {
        val tester = SimpleLucidTester(".clk(1), .rst(0) { dff test(.clk(0)); }")
        tester.context.walk(tester.assignBlock())
        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDoubleAssignBlocks() {
        val tester = SimpleLucidTester(".clk(1) { .clk(0) { dff test; } }")
        tester.context.walk(tester.assignBlock())
        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffSimpleStruct() {
        val tester = SimpleLucidTester(
            """
            module myMod (
            input a
            ){
            struct test { a, b[2][3], c[4] }
            dff testing<test>(.clk(1))
            }
            """.trimIndent()
        )
        tester.context.walk(tester.source())
        assert(tester.hasNoIssues)

        val struct = StructType(
            "test", mutableMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", ArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )

        val dff = tester.context.types.resolve("testing")
        dff as Dff

        assertEquals(StructWidth(struct), dff.q.get(tester.context.evalContext).signalWidth)
    }

    @Test
    fun testDffStructArray() {
        val tester = SimpleLucidTester(
            """
            module myMod (
            input a
            ){
            struct test { a, b[2][3], c[4] };
            dff testing[6]<test>(.clk(1));
            }
            """.trimIndent()
        )
        tester.context.walk(tester.source())
        assert(tester.hasNoIssues)

        val struct = StructType(
            "test", mutableMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", ArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )
        val width = ArrayWidth(6, StructWidth(struct))

        val dff = tester.context.types.resolve("testing")
        dff as Dff

        assertEquals(width, dff.q.get(tester.context.evalContext).signalWidth)
    }

    @Test
    fun testBadParam() {
        val tester = SimpleLucidTester(
            """
            module myMod #(
                MY_PARAM = 2 : MY_PARAM > 3
            )(
                input a,
                output b
            ){
            }
            """.trimIndent()
        )
        tester.context.walk(tester.source())
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasNoWarnings)
        assert(tester.hasErrors)
    }

    @Test
    fun testSimpleSig() {
        val tester = SimpleLucidTester(
            """
            module myMod (
                input a
            ) {
                sig testSig;
            }
            """.trimIndent()
        )
        tester.context.walk(tester.source())
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasNoErrors)
        // TODO: warn about unused signal
        val sig = tester.context.resolveSignal("testSig")
        sig as Signal
        assertEquals(SignalDirection.Both, sig.direction)
        assertEquals(BitValue(Bit.Bu, false, false), sig.get(tester.context.evalContext))
        assertEquals(false, sig.signed)
        assertEquals(null, sig.parent)
        assertEquals(BitWidth, sig.width)
    }

    @Test
    fun testArraySig() {
        val tester = SimpleLucidTester(
            """
            module myMod (
                input a
            ) {
                sig testSig[8];
            }
            """.trimIndent()
        )
        tester.context.walk(tester.source())
        assert(tester.hasNoSyntaxIssues)
        assert(tester.hasNoErrors)
        // TODO: warn about unused signal
        val sig = tester.context.resolveSignal("testSig")
        sig as Signal

        val width = BitListWidth(8)
        assertEquals(SignalDirection.Both, sig.direction)
        assertEquals(width.filledWith(Bit.Bu, false, false), sig.get(tester.context.evalContext))
        assertEquals(false, sig.signed)
        assertEquals(null, sig.parent)
        assertEquals(width, sig.width)
    }
}