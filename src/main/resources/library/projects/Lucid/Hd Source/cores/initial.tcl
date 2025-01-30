open_project {$CORE_DIR/managed_ip_project/managed_ip_project.xpr}
create_ip -name clk_wiz -vendor xilinx.com -library ip -version 6.0 -module_name clk_wiz_0 -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.CLKOUT1_JITTER {81.245} \
  CONFIG.CLKOUT1_PHASE_ERROR {85.164} \
  CONFIG.CLKOUT1_REQUESTED_OUT_FREQ {618.75} \
  CONFIG.CLKOUT2_JITTER {108.600} \
  CONFIG.CLKOUT2_PHASE_ERROR {85.164} \
  CONFIG.CLKOUT2_REQUESTED_OUT_FREQ {123.75} \
  CONFIG.CLKOUT2_USED {true} \
  CONFIG.CLK_OUT1_PORT {pclkx5} \
  CONFIG.CLK_OUT2_PORT {pclk} \
  CONFIG.MMCM_CLKFBOUT_MULT_F {12.375} \
  CONFIG.MMCM_CLKOUT0_DIVIDE_F {2.000} \
  CONFIG.MMCM_CLKOUT1_DIVIDE {10} \
  CONFIG.NUM_OUT_CLKS {2} \
  CONFIG.PRIMARY_PORT {clk_in} \
  CONFIG.USE_RESET {false} \
] [get_ips clk_wiz_0]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
catch { config_ip_cache -export [get_ips -all clk_wiz_0] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
launch_runs clk_wiz_0_synth_1 -jobs 8
wait_on_run clk_wiz_0_synth_1
export_simulation -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -use_ip_compiled_libs -force -quiet