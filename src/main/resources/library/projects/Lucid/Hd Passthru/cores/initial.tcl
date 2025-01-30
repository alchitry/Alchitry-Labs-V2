open_project {$CORE_DIR/managed_ip_project/managed_ip_project.xpr}
create_ip -name clk_wiz -vendor xilinx.com -library ip -version 6.0 -module_name clk_wiz_0 -dir {$CORE_DIR}
set_property -dict [list CONFIG.CLKOUT2_USED {true} CONFIG.PRIMARY_PORT {clk_in} CONFIG.CLK_OUT1_PORT {clk_100} CONFIG.CLK_OUT2_PORT {clk_200} CONFIG.CLKOUT2_REQUESTED_OUT_FREQ {200.000} CONFIG.USE_RESET {false} CONFIG.MMCM_CLKOUT1_DIVIDE {5} CONFIG.NUM_OUT_CLKS {2} CONFIG.CLKOUT2_JITTER {114.829} CONFIG.CLKOUT2_PHASE_ERROR {98.575}] [get_ips clk_wiz_0]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
catch { config_ip_cache -export [get_ips -all clk_wiz_0] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
launch_runs clk_wiz_0_synth_1 -jobs 8
wait_on_run clk_wiz_0_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet
