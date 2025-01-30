/**
    "name": "SerDes DDR 10 to 1"
    "description": "Serializes data with a DDR clock from 10 to 1."
    "supportedBoards" : ["Au", "Au+", "AuV2"]
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

    This module wraps the Xilinx primitives to expose it to Lucid.

    See: https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/OSERDESE2
*/
module serdes_ddr_10_to_1 (
    input ioclk,
    input rst,
    input clk,
    input [9:0] data,
    output iob_out
);
    wire shift1, shift2;

    OSERDESE2 #(
        .DATA_WIDTH(10),
        .DATA_RATE_OQ("DDR"),
        .DATA_RATE_TQ("SDR"),
        .SERDES_MODE("MASTER"),
        .TRISTATE_WIDTH(1)
    ) serdes_master (
        .RST(rst),
        .CLK(ioclk),
        .CLKDIV(clk),
        .D1(data[0]),
        .D2(data[1]),
        .D3(data[2]),
        .D4(data[3]),
        .D5(data[4]),
        .D6(data[5]),
        .D7(data[6]),
        .D8(data[7]),
        .TCE(1),
        .OCE(1),
        .T1(0),
        .T2(0),
        .T3(0),
        .T4(0),
        .OQ(iob_out),
        .SHIFTIN1(shift1),
        .SHIFTIN2(shift2)
    );

    OSERDESE2 #(
        .DATA_WIDTH(10),
        .DATA_RATE_OQ("DDR"),
        .DATA_RATE_TQ("SDR"),
        .SERDES_MODE("SLAVE"),
        .TRISTATE_WIDTH(1)
    ) serdes_slave (
        .RST(rst),
        .CLK(ioclk),
        .CLKDIV(clk),
        .D1(0),
        .D2(0),
        .D3(data[8]),
        .D4(data[9]),
        .D5(0),
        .D6(0),
        .D7(0),
        .D8(0),
        .TCE(1),
        .OCE(1),
        .T1(0),
        .T2(0),
        .T3(0),
        .T4(0),
        .OQ(),
        .SHIFTIN1(0),
        .SHIFTIN2(0),
        .SHIFTOUT1(shift1),
        .SHIFTOUT2(shift2)
    );
endmodule