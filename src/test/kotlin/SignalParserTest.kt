import com.alchitry.labs.parsers.lucidv2.signals.Dff
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.ArrayValue
import com.alchitry.labs.parsers.lucidv2.values.MutableBitList
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.SimpleWidth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class SignalParserTest {
    @Test
    fun testDffSimpleDeclaration() {
        val tester = LucidTester("dff testing(.clk(1))")
        tester.dffDec() // parse
        val dff = tester.sigParser.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        dSig as Signal
        qSig as Signal
        assertEquals(SignalDirection.Write, dSig.direction)
        assertEquals(SignalDirection.Read, qSig.direction)
    }

    @Test
    fun testDffArray() {
        val tester = LucidTester("dff testing[8][4][2](.clk(1))")
        tester.dffDec() // parse
        val dff = tester.sigParser.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        SimpleValue(MutableBitList("00",2), constant = false)
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
        val tester = LucidTester("signed dff testing[8][4][2](.clk(1))")
        tester.dffDec() // parse
        val dff = tester.sigParser.resolve("testing")

        dff as Dff

        val dSig = dff.getSignal("d")
        val qSig = dff.getSignal("q")

        val initValue = ArrayValue(
            List(8) {
                ArrayValue(
                    List(4) {
                        SimpleValue(MutableBitList("00",2, signed = true), constant = false)
                    }
                )
            }
        )

        dSig as Signal
        qSig as Signal
        assertEquals(initValue, qSig.value)
        assertEquals(initValue, dSig.value)
    }
}