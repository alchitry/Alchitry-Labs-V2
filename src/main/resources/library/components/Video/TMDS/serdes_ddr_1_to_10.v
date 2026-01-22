/**
    "name": "SerDes DDR 1 to 10"
    "description": "Deserializes data with a DDR clock from 1 to 10."
    "supportedBoards" : ["Au", "Au+", "AuV2", "PtV2"]
**/
/******************************************************************************

    The MIT License (MIT)

    Copyright (c) 2026 Alchitry

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

    See:
        https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/IBUFDS
        https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/IDELAYE2
        https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/ISERDESE2
*/
module serdes_ddr_1_to_10 (
    input rst,         // reset
    input clk,         // slow clock
    input clkx5,       // 5x frequency of clk
    input data_p,      // input data
    input data_n,
    input bitslip,     // 1 = bitslip
    input [4:0] delay, // delay value to load
    input load_delay,  // 1 = load delay from delay input
    output [9:0] data  // output data
);

    wire d;
    IBUFDS bufds (.I(data_p), .IB(data_n), .O(d));
    wire ddly;

    IDELAYE2
    # (
        .CINVCTRL_SEL("FALSE"),
        .DELAY_SRC("IDATAIN"),
        .HIGH_PERFORMANCE_MODE("FALSE"),
        .IDELAY_TYPE("VAR_LOAD"),
        .IDELAY_VALUE(0),
        .REFCLK_FREQUENCY(200.0),
        .PIPE_SEL("FALSE"),
        .SIGNAL_PATTERN("DATA"))
    idelaye2_bus
    (
        .DATAOUT(ddly),
        .DATAIN(1'b0),
        .C(clk),
        .CE(1'b0),
        .INC(1'b0),
        .IDATAIN(d),
        .LD(load_delay),
        .REGRST(1'b0),
        .LDPIPEEN(1'b0),
        .CNTVALUEIN(delay),
        .CNTVALUEOUT(),
        .CINVCTRL(1'b0)
    );

    wire shift1;
    wire shift2;

    ISERDESE2 #(
        .DATA_RATE("DDR"),
        .DATA_WIDTH(10),
        .INTERFACE_TYPE("NETWORKING"),
        .DYN_CLKDIV_INV_EN("FALSE"),
        .DYN_CLK_INV_EN("FALSE"),
        .NUM_CE(2),
        .OFB_USED("FALSE"),
        .IOBDELAY("IFD"),
        .SERDES_MODE("MASTER")
    ) serdes_master (
        .D(0),
        .DDLY(ddly),
        .CLK(clkx5),
        .CLKB(!clkx5),
        .CE1(1),
        .CE2(1),
        .RST(rst),
        .CLKDIV(clk),
        .CLKDIVP(0),
        .OCLK(0),
        .OCLKB(0),
        .BITSLIP(bitslip),
        .SHIFTIN1(0),
        .SHIFTIN2(0),
        .SHIFTOUT1(shift1),
        .SHIFTOUT2(shift2),
        .OFB(0),
        .DYNCLKDIVSEL(0),
        .DYNCLKSEL(0),
        .Q1(data[9]),
        .Q2(data[8]),
        .Q3(data[7]),
        .Q4(data[6]),
        .Q5(data[5]),
        .Q6(data[4]),
        .Q7(data[3]),
        .Q8(data[2]),
        .O()
    );

    ISERDESE2 #(
        .DATA_RATE("DDR"),
        .DATA_WIDTH(10),
        .INTERFACE_TYPE("NETWORKING"),
        .DYN_CLKDIV_INV_EN("FALSE"),
        .DYN_CLK_INV_EN("FALSE"),
        .NUM_CE(2),
        .OFB_USED("FALSE"),
        .IOBDELAY("IFD"),
        .SERDES_MODE("SLAVE")
    ) serdes_slave (
        .D(0),
        .DDLY(0),
        .CLK(clkx5),
        .CLKB(!clkx5),
        .CE1(1),
        .CE2(1),
        .RST(rst),
        .CLKDIV(clk),
        .CLKDIVP(0),
        .OCLK(0),
        .OCLKB(0),
        .BITSLIP(bitslip),
        .SHIFTIN1(shift1),
        .SHIFTIN2(shift2),
        .SHIFTOUT1(),
        .SHIFTOUT2(),
        .OFB(0),
        .DYNCLKDIVSEL(0),
        .DYNCLKSEL(0),
        .Q1(),
        .Q2(),
        .Q3(data[1]),
        .Q4(data[0]),
        .Q5(),
        .Q6(),
        .Q7(),
        .Q8(),
        .O()
    );

endmodule