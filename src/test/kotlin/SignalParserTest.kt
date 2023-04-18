import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.SimpleWidth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class SignalParserTest {
    @Test
    fun testDffSimpleDeclaration() {
        val tester = LucidTester("dff testing")
        tester.dffDec() // parse
        val dSig = tester.sigParser.resolve("testing.d")
        val qSig = tester.sigParser.resolve("testing.q")
        assertNotNull(dSig, "DFF d signal was null!")
        assertNotNull(qSig, "DFF q signal was null!")
    }
}