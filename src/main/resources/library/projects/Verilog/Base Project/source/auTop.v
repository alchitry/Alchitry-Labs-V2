module au_top(
    input clk,              // 100MHz clock
    input rst_n,            // reset button (active low)
    output [7:0] led,       // 8 user controllable LEDs
    input usbRx,           // USB->Serial input
    output usbTx           // USB->Serial output
    );
    
    wire rst;
    
    // The reset conditioner is used to synchronize the reset signal to the FPGA
    // clock. This ensures the entire FPGA comes out of reset at the same time.
    resetConditioner resetConditioner(.clk(clk), .in(!rst_n), .out(rst));
    
    assign led = 8'h00;      // turn LEDs off

    assign usbTx = usbRx;    // echo the serial data
    
endmodule