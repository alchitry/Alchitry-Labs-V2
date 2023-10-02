import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

internal class LucidToVerilogTests {
    @Test
    fun simpleValue() {
        assertEquals(
            "4'ha",
            BitListValue(BigInteger.valueOf(10), constant = true, signed = false).asVerilog()
        )
        assertEquals(
            "5'sha",
            BitListValue(BigInteger.valueOf(10), constant = true, signed = true).asVerilog()
        )
        assertEquals(
            "4'b1x01",
            BitListValue(listOf(Bit.B1, Bit.B0, Bit.Bx, Bit.B1), constant = true, signed = false).asVerilog()
        )

    }
}