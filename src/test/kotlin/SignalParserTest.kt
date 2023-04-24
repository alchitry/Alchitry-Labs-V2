import com.alchitry.labs.parsers.lucidv2.signals.Dff
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
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
        assertEquals(BitListValue(MutableBitList("1",2), constant = false), qSig.value)

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
        assertEquals(BitListValue(MutableBitList("111",2), constant = false), qSig.value)

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
        assertEquals(SimpleWidth(3), qSig.value.signalWidth)

        assert(tester.hasErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testDffArray() {
        val tester = LucidTester("dff testing[8][4][2](.clk(1));")
        tester.dffDec() // parse
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue(MutableBitList("00",2), constant = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.value)
        assertEquals(initValue, dSig.value)

        assert(tester.hasNoErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }

    @Test
    fun testSignedDffArray() {
        val tester = LucidTester("signed dff testing[8][4][2](.clk(1));")
        tester.dffDec() // parse
        val dff = tester.parseContext.signal.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        BitListValue(MutableBitList("00",2, signed = true), constant = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.value)
        assertEquals(initValue, dSig.value)

        assert(tester.hasNoErrors)
        assert(tester.hasNoWarnings)
        assert(tester.hasNoSyntaxIssues)
    }
}