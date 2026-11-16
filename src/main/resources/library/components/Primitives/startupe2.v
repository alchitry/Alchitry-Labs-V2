/**
    "name": "STARTUPE2"
    "description": "STARTUPE2 primitive on the Artix 7."
    "supportedBoards" : ["Xilinx"]
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

    This module wraps the Xilinx STARTUPE2 primitive to expose it to Lucid.

    See: https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/STARTUPE2
*/

module startupe2 #(
    parameter PROG_USR = "FALSE" // Activate program event security feature. Requires encrypted bitstreams.
)(
    output cfgclk,     // Configuration main clock output
    output cfgmclk,    // Configuration internal oscillator clock output
    output eos,        // Active high output signal indicating the End Of Startup.
    output preq,       // PROGRAM request to fabric output
    input clk,         // User start-up clock input
    input gsr,         // Global Set/Reset input
    input gts,         // Global 3-state input
    input keyclearb,   // Clear AES Decrypter Key input from Battery-Backed RAM (BBRAM)
    input pack,        // PROGRAM acknowledge input
    input usrcclko,    // User CCLK input
    input usrcclkts,   // User CCLK 3-state enable input
    input usrdoneo,    // User DONE pin output control
    input usrdonets    // User DONE 3-state enable output
);

    STARTUPE2 #(
        .PROG_USR(PROG_USR)
    ) startupe2_primitive (
        .CFGCLK(cfgclk),
        .CFGMCLK(cfgmclk),
        .EOS(eos),
        .PREQ(preq),
        .CLK(clk),
        .GSR(gsr),
        .GTS(gts),
        .KEYCLEARB(keyclearb),
        .PACK(pack),
        .USRCCLKO(usrcclko),
        .USRCCLKTS(usrcclkts),
        .USRDONEO(usrdoneo),
        .USRDONETS(usrdonets)
    );

endmodule
