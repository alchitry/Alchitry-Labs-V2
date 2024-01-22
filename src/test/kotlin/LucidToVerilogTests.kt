import com.alchitry.labs2.parsers.lucidv2.parsers.toSourceFile
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
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
                global Globals {
                    const SIZE = 4
                }
                module alchitry_top (
                    input clk,              // 100MHz clock
                    input rst_n,            // reset button (active low)
                    output led [8],         // 8 user controllable LEDs
                    input usb_rx,           // USB->Serial input
                    output usb_tx           // USB->Serial output
                  ) {
                  
                  sig rst = ~rst_n                 // reset signal
                  signed sig clk_n = ~clk
                  
                  sig mySig[2] = 2h1;
                  //const TEST_CONST = 1
                  
                  .clk(clk) {
                    dff ct[8](#INIT(25), .rst(rst))
                    dff ct2[8]
                      #OUT_SIZE(Globals.SIZE[1]) {
                    reset_conditioner cond[2](.in(mySig))
                    }
                    
                  }
                  
                  dff ct3[8](.clk(clk_n))
                  
                  always {
                    ct.d = ct.q + 1
                    }
                  
                  always {
                    ct2.d = ct2.q + 2 + Globals.SIZE[ct2.q]
                    ct3.d = ct3.q + 3 + cond.out[1];
                    
                    led = 8h00             // turn LEDs off
    
                    usb_tx = usb_rx        // echo the serial data
                  }
                }
            """.trimIndent().toSourceFile(),
            """
                module reset_conditioner #(
                    STAGES = 4 : STAGES > 1, // number of stages
                    OUT_SIZE ~ 2 : OUT_SIZE >= 0
                  )(
                    input clk,  // clock
                    input in,   // async reset
                    output out[2]  // sync reset
                  ) {
                  
                  dff stage[STAGES] (.clk(clk), .rst(in), #INIT(STAGESx{1}));

                  always {
                    stage.d = c{stage.q[STAGES-2:0],0};
                    out = stage.q[STAGES-1] + OUT_SIZE;
                  }
                }

            """.trimIndent().toSourceFile("reset.luc")
        )

        runBlocking {
            println(tester.getVerilog())
        }
    }
}