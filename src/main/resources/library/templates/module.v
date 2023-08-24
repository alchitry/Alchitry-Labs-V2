module %NAME% (
    input clk,  // clock
    input rst,  // reset
    output out
  );

  /* Combinational Logic */
  always @* begin
    out = 0;
  end
  
  /* Sequential Logic */
  always @(posedge clk) begin
    if (rst) begin
      // Add flip-flop reset values here
    end else begin
      // Add flip-flop q <= d statements here
    end
  end
  
endmodule