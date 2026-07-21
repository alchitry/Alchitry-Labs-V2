/**
    "name": "BSCANE2"
    "description": "Xilinx JTAG Boundary Scan Primitive."
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

    This module wraps the Xilinx BSCANE2 primitive to expose it to Lucid.

    See: https://docs.amd.com/r/en-US/ug953-vivado-7series-libraries/BSCANE2
*/

module bscane2 #(
    parameter JTAG_CHAIN = 1 // Value for USER command. Accepts 1-4
)(
    output capture, // CAPTURE output from TAP controller.
    output drck,    // Gated TCK output. When SEL is asserted, DRCK toggles when CAPTURE or SHIFT are asserted.
    output reset,   // Reset output for TAP controller.
    output runtest, // Output asserted when TAP controller is in Run Test/Idle state.
    output sel,     // USER instruction active output.
    output shift,   // SHIFT output from TAP controller.
    output tck,     // Test Clock output. Fabric connection to TAP Clock pin.
    output tdi,     // Test Data Input (TDI) output from TAP controller.
    input tdo,      // Test Mode Select output. Fabric connection to TAP.
    output tms,     // UPDATE output from TAP controller
    output update   // Test Data Output (TDO) input for USER function.
);

    BSCANE2 #(.JTAG_CHAIN(JTAG_CHAIN)) bscane2_primitive (
       .CAPTURE(capture),
       .DRCK(drck),
       .RESET(reset),
       .RUNTEST(runtest),
       .SEL(sel),
       .SHIFT(shift),
       .TCK(tck),
       .TDI(tdi),
       .TMS(tms),
       .UPDATE(update),
       .TDO(tdo)
    );

endmodule