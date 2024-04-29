import com.alchitry.labs2.parsers.lucidv2.types.*
import com.alchitry.labs2.parsers.lucidv2.values.*
import helpers.SimpleLucidTester
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TypesParserTests {
    @Test
    fun testDffSimpleDeclaration() = runBlocking {
        val tester = SimpleLucidTester("dff testing(.clk(1));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(SignalDirection.Write, dSig.direction)
        assertEquals(SignalDirection.Read, qSig.direction)

        assert(tester.hasNoIssues)
    }

    @Test
    fun testDffArrayClk() = runBlocking {
        val tester = SimpleLucidTester("dff testing(.clk({1}));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        assert(tester.hasNoWarnings)
        assert(tester.hasErrors)
    }

    @Test
    fun testDffArrayRst() = runBlocking {
        val tester = SimpleLucidTester("dff testing(.clk(1), .rst({1}));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        assert(tester.hasNoWarnings)
        assert(tester.hasErrors)
    }

    @Test
    fun testDffWideClk() = runBlocking {
        val tester = SimpleLucidTester("dff testing(.clk(2b11));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        assert(tester.hasNoErrors)
        assert(tester.hasWarnings)
    }

    @Test
    fun testDffWideRst() = runBlocking {
        val tester = SimpleLucidTester("dff testing(.clk(1), .rst(2b11));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        assert(tester.hasNoErrors)
        assert(tester.hasWarnings)
    }

    @Test
    fun testDffInit() = runBlocking {
        val tester = SimpleLucidTester("dff testing(.clk(1), #INIT(1));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitValue(Bit.B1, signed = false), qSig.read(tester.context.evalContext))

        assert(tester.hasNoIssues)
    }

    @Test
    fun testDffInitTruncate() = runBlocking {
        val tester = SimpleLucidTester("dff testing[3](.clk(1), #INIT(15));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitListValue("111", 2, signed = false), qSig.read(tester.context.evalContext))

        assert(tester.hasNoErrors)
        assert(tester.hasWarnings)
    }

    @Test
    fun testDffInitDimMismatch() = runBlocking {
        val tester = SimpleLucidTester("dff testing[3](.clk(1), #INIT({15}));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(BitListWidth(3), qSig.read(tester.context.evalContext).width)

        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
    }

    @Test
    fun testDffArray() = runBlocking {
        val tester = SimpleLucidTester("dff testing[8][4][2](.clk(1));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")
        assert(tester.hasNoIssues)

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue("00", 2, signed = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.read(tester.context.evalContext))
        assertEquals(initValue, dSig.read(tester.context.evalContext))
    }

    @Test
    fun testSignedDffArray() = runBlocking {
        val tester = SimpleLucidTester("signed dff testing[8][4][2](.clk(1));")
        val tree = tester.parser.dffDec()
        tester.context.walk(tree) // parse
        val dff = tester.context.types.resolve(tree, "testing")
        assert(tester.hasNoIssues)

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue("00", 2, signed = true)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.read(tester.context.evalContext))
        assertEquals(initValue, dSig.read(tester.context.evalContext))


    }

    @Test
    fun testAssignBlockSimple() = runBlocking {
        val tester = SimpleLucidTester(".clk(1), .rst(0) { dff myDff; }")
        val tree = tester.parser.assignBlock()
        tester.context.walk(tree)
        assert(tester.hasNoIssues)

        val dff = tester.context.types.resolve(tree, "myDff")

        dff as Dff

        assertEquals(BitValue(Bit.B1, signed = false), dff.clk.value)
        assertEquals(BitValue(Bit.B0, signed = false), dff.rst?.value)
    }

    @Test
    fun testDoubleAssign() = runBlocking {
        val tester = SimpleLucidTester(".clk(1), .rst(0) { dff myDff(.clk(0)); }")
        tester.context.walk(tester.parser.assignBlock())
        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
    }

    @Test
    fun testDoubleAssignBlocks() = runBlocking {
        val tester = SimpleLucidTester(".clk(1) { .clk(0) { dff myDff; } }")
        tester.context.walk(tester.parser.assignBlock())
        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
    }

    @Test
    fun testDffSimpleStruct() = runBlocking {
        val tester = SimpleLucidTester(
            """
            module myMod (
            input a
            ){
            struct myStruct { a, b[2][3], c[4] }
            dff testing<myStruct>(.clk(1))
            }
            """.trimIndent()
        )
        val tree = tester.parser.source()
        tester.context.walk(tree)
        assert(tester.hasNoIssues)

        val struct = StructType(
            "myStruct", linkedMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", DefinedArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )

        val dff = tester.context.types.resolve(tree, "testing")
        dff as Dff

        assertEquals(StructWidth(struct), dff.q.read(tester.context.evalContext).width)
    }

    @Test
    fun testDffStructArray() = runBlocking {
        val tester = SimpleLucidTester(
            """
            module myMod (
            input a
            ){
            struct myStruct { a, b[2][3], c[4] };
            dff testing[6]<myStruct>(.clk(1));
            }
            """.trimIndent()
        )
        val tree = tester.parser.source()
        tester.context.walk(tree)
        assert(tester.hasNoIssues)

        val struct = StructType(
            "myStruct", linkedMapOf(
                "a" to StructMember("a", BitWidth, false),
                "b" to StructMember("b", DefinedArrayWidth(2, BitListWidth(3)), false),
                "c" to StructMember("c", BitListWidth(4), false)
            )
        )
        val width = DefinedArrayWidth(6, StructWidth(struct))

        val dff = tester.context.types.resolve(tree, "testing")
        dff as Dff

        assertEquals(width, dff.q.read(tester.context.evalContext).width)
    }

    @Test
    fun testBadParam() = runBlocking {
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
        tester.context.walk(tester.parser.source())
        assert(tester.hasNoWarnings)
        assert(tester.hasErrors)
    }

    @Test
    fun testSimpleSig() = runBlocking {
        val tester = SimpleLucidTester(
            """
            module myMod (
                input a
            ) {
                sig testSig;
            }
            """.trimIndent()
        )
        val tree = tester.parser.source()
        tester.context.walk(tree)
        assert(tester.hasNoErrors)
        // TODO: warn about unused signal
        val sig = tester.context.resolveSignal(tree, "testSig")
        sig as Signal
        assertEquals(SignalDirection.Both, sig.direction)
        assertEquals(BitValue(Bit.Bx, false), sig.read(tester.context.evalContext))
        assertEquals(false, sig.signed)
        assertEquals(null, sig.parent)
        assertEquals(BitWidth, sig.width)
    }

    @Test
    fun testArraySig() = runBlocking {
        val tester = SimpleLucidTester(
            """
            module myMod (
                input a
            ) {
                sig testSig[8];
            }
            """.trimIndent()
        )
        val tree = tester.parser.source()
        tester.context.walk(tree)
        assert(tester.hasNoErrors)
        // TODO: warn about unused signal
        val sig = tester.context.resolveSignal(tree, "testSig")
        sig as Signal

        val width = BitListWidth(8)
        assertEquals(SignalDirection.Both, sig.direction)
        assertEquals(width.filledWith(Bit.Bx, false), sig.read(tester.context.evalContext))
        assertEquals(false, sig.signed)
        assertEquals(null, sig.parent)
        assertEquals(width, sig.width)
    }
}