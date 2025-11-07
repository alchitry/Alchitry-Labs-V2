/**
    "name": "IDELAYCTRL"
    "description": "IDELAYCTRL for the Artix 7."
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

    This module wraps the Xilinx IDELAYCTRL primitive to expose it to Lucid.

    At least one is required when any IDELAYE2 or ODELAYE2 are used.

    See: https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/IDELAYCTRL
*/
module idelayctrl (
    input refclk, // 200MHz clock
    input rst,    // reset
    output rdy    // delay locked
);
    IDELAYCTRL delayctrl (
        .RDY(rdy),
        .REFCLK(refclk),
        .RST(rst)
    );
endmodule