package basic

import com.alchitry.labs2.parsers.hdl.lucid.parsers.toSourceFile
import com.alchitry.labs2.parsers.hdl.lucid.toVerilog
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitListValue
import com.alchitry.labs2.project.library.ComponentLibrary
import helpers.ProjectTester
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

internal class LucidToVerilogTests {
    @Test
    fun simpleValue() {
        assertEquals(
            "4'ha",
            BitListValue(BigInteger.valueOf(10), signed = false).toVerilog()
        )
        assertEquals(
            "5'sha",
            BitListValue(BigInteger.valueOf(10), signed = true).toVerilog()
        )
        assertEquals(
            "4'b1x01",
            BitListValue(listOf(Bit.B1, Bit.B0, Bit.Bx, Bit.B1), signed = false).toVerilog()
        )
    }

    @Test
    fun simpleConversion() {
        val tester = ProjectTester(
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
                  
                  sig my_sig[2] = 2h1;
                  //const TEST_CONST = 1
                  
                  .clk(clk) {
                    dff ct[8](#INIT(25), .rst(rst))
                    dff ct2[8]
                      #OUT_SIZE(Globals.SIZE[1]) {
                    reset_conditioner cond[2](.in(my_sig))
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

    @Test
    fun binToDecConversion() = runBlocking {
        System.setProperty("app.dir", "../")
        val tester = ProjectTester(
            """
                module alchitry_top (
                    input clk,              // 100MHz clock
                    input rst_n,            // reset button (active low)
                    output led[8],          // 8 user controllable LEDs
                    input usb_rx,            // USB->Serial input
                    output usb_tx,           // USB->Serial output
                    output io_led[3][8],     // LEDs on IO Shield
                    output io_seg[8],        // 7-segment LEDs on IO Shield
                    output io_sel[4],        // Digit select on IO Shield
                    input io_button[5],      // 5 buttons on IO Shield
                    input io_dip[3][8]       // DIP switches on IO Shield
                ) {
                    
                    sig rst                 // reset signal
                    
                    .clk(clk) {
                        // The reset conditioner is used to synchronize the reset signal to the FPGA
                        // clock. This ensures the entire FPGA comes out of reset at the same time.
                        reset_conditioner reset_cond
                        .rst(rst){
                            multi_seven_seg seg(#DIV(${"$"}is_sim() ? 3 : 16)) // 3 for simulation, 16 for hardware
                        }
                    }
                    bin_to_dec bin_to_dec_original(#DIGITS(4))
                    bin_to_dec_v1 bin_to_dec_v1(#DIGITS(4))
                    
                    always {
                        reset_cond.in = ~rst_n  // input raw inverted reset signal
                        rst = reset_cond.out    // conditioned reset
                        
                        led = 8h00             // turn LEDs off
                        
                        usb_tx = usb_rx          // loop serial port
                        
                        bin_to_dec_original.value = 4b0101
                        bin_to_dec_v1.value = 4b0111
                        
                        seg.values={4d4,4d3,4d2,4d1}
                        
                        io_led = 3x{{8h00}}
                        
                        if (io_dip[0][0]){
                            seg.values = bin_to_dec_original.digits
                            io_led[0] = bin_to_dec_original.digits[0]
                            io_led[1] = bin_to_dec_original.digits[1]
                            io_led[2] = bin_to_dec_original.digits[2]
                        }
                        
                        if (io_dip[0][1]){
                            seg.values = bin_to_dec_v1.digits
                            io_led[0] = bin_to_dec_v1.digits[0]
                            io_led[1] = bin_to_dec_v1.digits[1]
                            io_led[2] = bin_to_dec_v1.digits[2]
                        }
                        
                        
                        io_seg = ~seg.seg
                        io_sel = ~seg.sel
                    }
                }
            """.trimIndent().toSourceFile("alchitryTop.luc"),
            """
                module bin_to_dec_v1 #(
                    DIGITS ~ 1 : DIGITS > 0 && DIGITS < 20,           // limited by 64 bit constants in the tools
                    LEADING_ZEROS = 0 : LEADING_ZEROS == 0 || LEADING_ZEROS == 1
                )(
                    input value[${"$"}clog2(${"$"}pow(10, DIGITS))],            // minimum number of bits for DIGITS 
                    output digits[DIGITS][4]                          // decimal output
                ) {
                    
                    sig scale[${"$"}width(value)]
                    sig remainder[${"$"}width(value)]                         // running remainder
                    sig sub_value[${"$"}width(value)]                         // temporary subtraction value
                    sig blank                                          // flag for leading zeros
                    
                    always {
                        repeat(i, DIGITS){
                            digits[i] = d11                                // default to invalid number
                        }
                        
                        remainder = value                                // initialize remainder
                        blank = !LEADING_ZEROS                           // set blank zero flag
                        scale = 0
                        sub_value = 0
                        
                        if (value < ${"$"}pow(10, DIGITS)) {                   // if can be displayed
                            repeat(j, DIGITS, DIGITS-1, -1){// for each digit  
                                scale = ${"$"}pow(10, j)                          // get the scale for the digit
                                
                                if (remainder < scale) {                      // if this digit is 0
                                    if (j != 0 && blank)                        // use 10 for blank
                                        digits[j] = 10
                                    else                                        // or 0 for zero
                                        digits[j] = 0
                                } else {                                      // digit is 1-9
                                    blank = 0                                  // don't blank future zeros
                                    sub_value = 0                              // default to no subtraction
                                    repeat(i, 9, 9, -1){// for each possible value (starting from 9)
                                        
                                        if (remainder < (i+1) * scale) {          // if remainder is less than value
                                            digits[j] = i                          // set digit to this value
                                            sub_value = i * scale                  // set subtraction value
                                        }
                                    }
                                    remainder = remainder - sub_value          // subtract off last digit
                                }
                            }
                        }
                    }
                }
            """.trimIndent().toSourceFile("binToDecV1.luc"),
            """
                module multi_seven_seg #(
                    DIGITS = 4 : DIGITS > 0,
                    DIV = 16 : DIV >= 0
                )(
                    input clk,                // clock
                    input rst,                // reset
                    input values[DIGITS][4],  // values to show
                    output seg[7],            // LED segments
                    output sel[DIGITS]        // Digit select
                ) {
                    
                    // number of bits required to store DIGITS-1
                    const DIGIT_BITS = ${"$"}clog2(DIGITS)
                    
                    .clk(clk), .rst(rst) {
                        counter ctr (#DIV(DIV), #SIZE(DIGIT_BITS), #TOP(DIGITS-1)) 
                    }
                    
                    seven_seg seg_dec                        // segment decoder
                    decoder digit_dec (#WIDTH(DIGIT_BITS)) // digit decoder
                    
                    always {
                        seg_dec.char = values[ctr.value]    // select the value for the active digit
                        seg = seg_dec.segs                  // output the decoded value
                        
                        digit_dec.in = ctr.value           // decode active digit to one-hot
                        sel = digit_dec.out                // output the active digit
                    }
                }
            """.trimIndent().toSourceFile("multiSevenSeg.luc"),
            """
                module seven_seg (
                    input char[4],
                    output segs[7]
                ) {
                    always {
                        case (char) {
                            0: segs = 7b0111111
                            1: segs = 7b0000110
                            2: segs = 7b1011011
                            3: segs = 7b1001111
                            4: segs = 7b1100110
                            5: segs = 7b1101101
                            6: segs = 7b1111101
                            7: segs = 7b0000111
                            8: segs = 7b1111111
                            9: segs = 7b1100111
                            default: segs = 7b0000000
                        }
                    }
                }
            """.trimIndent().toSourceFile("sevenSeg.luc"),
            ComponentLibrary.findByPath("Miscellaneous/counter.luc")!!.content.toSourceFile("counter.luc"),
            ComponentLibrary.findByPath("Conditioning/reset_conditioner.luc")!!.content.toSourceFile("resetConditioner.luc"),
            ComponentLibrary.findByPath("Miscellaneous/decoder.luc")!!.content.toSourceFile("decoder.luc"),
            ComponentLibrary.findByPath("Miscellaneous/bin_to_dec.luc")!!.content.toSourceFile("binToDec.luc"),
        )

        val verilog = tester.getVerilog(allowWarnings = true)
        println(verilog)
    }
}