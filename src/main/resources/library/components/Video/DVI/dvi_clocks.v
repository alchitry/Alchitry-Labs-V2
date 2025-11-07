/**
    "name": "DVI Clocks"
    "description": "Uses a PLLE2_BASE to generate x1 and x5 clocks for DVI modules."
    "supportedBoards" : ["Au", "Au+", "AuV2", "PtV2"]
    **/
/******************************************************************************

    The MIT License (MIT)

    Copyright (c) 2025 Alchitry

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

    *****************************************************************************

    Generates x1 and x5 clocks to be used with DVI modules.

    See: https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/PLLE2_BASE
         https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/BUFG
         https://docs.amd.com/v/u/en-US/ds181_Artix_7_Data_Sheet
*/
module dvi_clocks #(
    parameter CLK_FREQ = 75000000
)(
    input clk,
    input rst,
    output pclk,
    output pclkx5,
    output locked
);

    wire clkfbin;
    wire clkfbout;
    wire clk0, clk1, clk2;

    localparam CLK_MULT = (160000000 - 1) / CLK_FREQ + 1; // 800MHz <= 5 * CLK_MULT * CLK_FREQ <= 1600MHz

    PLLE2_BASE #(
        .CLKIN1_PERIOD(1000000000/CLK_FREQ), // nano seconds
        .CLKFBOUT_MULT(5 * CLK_MULT),
        .CLKOUT0_DIVIDE(1 * CLK_MULT),
        .CLKOUT1_DIVIDE(5 * CLK_MULT),
        // phase shift the clocks -45 degree with respect to CLKOUT0 to help with timing
        .CLKFBOUT_PHASE(45.0 / 5.0 * 5.0 * CLK_MULT) // must be multiple of (45 / CLKFBOUT_MULT)
    ) pll (
        .CLKFBIN(clkfbin),
        .CLKIN1(clk),
        .RST(rst),
        .PWRDWN(0),
        .CLKFBOUT(clkfbout),
        .CLKOUT0(clk0),
        .CLKOUT1(clk1),
        .LOCKED(locked)
    );

    BUFG clkfb_buf(.I(clkfbout), .O(clkfbin));
    BUFG pclk_buf(.I(clk1), .O(pclk));
    BUFG pclkx5_buf(.I(clk0), .O(pclkx5));

endmodule