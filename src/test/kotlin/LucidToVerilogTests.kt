import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.BitListValue
import helpers.LucidTester
import kotlinx.coroutines.runBlocking
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

    @Test
    fun simpleConversion() {
        val tester = LucidTester(
            """
            module alchitry_top (
                input clk,              // 100MHz clock
                input rst_n,            // reset button (active low)
                output led [8],         // 8 user controllable LEDs
                input usb_rx,           // USB->Serial input
                output usb_tx           // USB->Serial output
              ) {
              
              sig rst = ~rst_n                 // reset signal
              signed sig clk_n = ~clk
              
              .clk(clk) {
                dff ct[8](#INIT(25), .rst(rst))
                dff ct2[8]
              }
              
              dff ct3[8](.clk(clk_n))
              
              always {
                ct.d = ct.q + 1
                }
              
              always {
                ct2.d = ct2.q + 2
                ct3.d = ct3.q + 3
                
                led = 8h00             // turn LEDs off

                usb_tx = usb_rx        // echo the serial data
              }
            }
        """.trimIndent()
        )

        runBlocking {
            println(tester.getVerilog())
        }
    }
}