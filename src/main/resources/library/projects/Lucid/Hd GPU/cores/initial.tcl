open_project {$CORE_DIR/managed_ip_project/managed_ip_project.xpr}
create_ip -name clk_wiz -vendor xilinx.com -library ip -version 6.0 -module_name clk_wiz_0 -dir {$CORE_DIR}
set_property -dict [list CONFIG.CLKOUT2_USED {true} CONFIG.PRIMARY_PORT {clk_in} CONFIG.CLK_OUT1_PORT {clk_100} CONFIG.CLK_OUT2_PORT {clk_200} CONFIG.CLKOUT2_REQUESTED_OUT_FREQ {200.000} CONFIG.USE_RESET {true} CONFIG.RESET_TYPE {ACTIVE_LOW} CONFIG.MMCM_CLKOUT1_DIVIDE {5} CONFIG.NUM_OUT_CLKS {2} CONFIG.RESET_PORT {resetn} CONFIG.CLKOUT2_JITTER {114.829} CONFIG.CLKOUT2_PHASE_ERROR {98.575}] [get_ips clk_wiz_0]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
catch { config_ip_cache -export [get_ips -all clk_wiz_0] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}]
launch_runs clk_wiz_0_synth_1 -jobs 16
wait_on_run clk_wiz_0_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_0/clk_wiz_0.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet

create_ip -name cordic -vendor xilinx.com -library ip -version 6.0 -module_name cordic -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.Data_Format {SignedFraction} \
  CONFIG.Functional_Selection {Sin_and_Cos} \
  CONFIG.Input_Width {10} \
  CONFIG.Output_Width {10} \
  CONFIG.flow_control {Blocking} \
  CONFIG.out_tready {true} \
] [get_ips cordic]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/cordic/cordic.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/cordic/cordic.xci}]
catch { config_ip_cache -export [get_ips -all cordic] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/cordic/cordic.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/cordic/cordic.xci}]
launch_runs cordic_synth_1 -jobs 16
wait_on_run cordic_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/cordic/cordic.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet

create_ip -name div_gen -vendor xilinx.com -library ip -version 5.1 -module_name div_gen_0 -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.FlowControl {Blocking} \
  CONFIG.OptimizeGoal {Resources} \
  CONFIG.OutTready {true} \
  CONFIG.clocks_per_division {8} \
  CONFIG.dividend_and_quotient_width {35} \
  CONFIG.divisor_has_tuser {true} \
  CONFIG.divisor_tuser_width {4} \
  CONFIG.divisor_width {32} \
  CONFIG.fractional_width {19} \
  CONFIG.latency {62} \
  CONFIG.remainder_type {Fractional} \
] [get_ips div_gen_0]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/div_gen_0/div_gen_0.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/div_gen_0/div_gen_0.xci}]
catch { config_ip_cache -export [get_ips -all div_gen_0] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/div_gen_0/div_gen_0.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/div_gen_0/div_gen_0.xci}]
launch_runs div_gen_0_synth_1 -jobs 16
wait_on_run div_gen_0_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/div_gen_0/div_gen_0.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet


create_ip -name cordic -vendor xilinx.com -library ip -version 6.0 -module_name sqrt_calc -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.Coarse_Rotation {false} \
  CONFIG.Data_Format {UnsignedFraction} \
  CONFIG.Functional_Selection {Square_Root} \
  CONFIG.Input_Width {32} \
  CONFIG.Output_Width {16} \
  CONFIG.cartesian_has_tuser {true} \
  CONFIG.cartesian_tuser_width {16} \
  CONFIG.flow_control {Blocking} \
  CONFIG.out_tready {true} \
] [get_ips sqrt_calc]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/sqrt_calc/sqrt_calc.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/sqrt_calc/sqrt_calc.xci}]
catch { config_ip_cache -export [get_ips -all sqrt_calc] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/sqrt_calc/sqrt_calc.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/sqrt_calc/sqrt_calc.xci}]
launch_runs sqrt_calc_synth_1 -jobs 16
wait_on_run sqrt_calc_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/sqrt_calc/sqrt_calc.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet

create_ip -name div_gen -vendor xilinx.com -library ip -version 5.1 -module_name div_gen_16_16 -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.FlowControl {Blocking} \
  CONFIG.OptimizeGoal {Resources} \
  CONFIG.OutTready {true} \
  CONFIG.clocks_per_division {8} \
  CONFIG.fractional_width {6} \
  CONFIG.latency {30} \
  CONFIG.remainder_type {Fractional} \
] [get_ips div_gen_16_16]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/div_gen_16_16/div_gen_16_16.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/div_gen_16_16/div_gen_16_16.xci}]
catch { config_ip_cache -export [get_ips -all div_gen_16_16] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/div_gen_16_16/div_gen_16_16.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/div_gen_16_16/div_gen_16_16.xci}]
launch_runs div_gen_16_16_synth_1 -jobs 16
wait_on_run div_gen_16_16_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/div_gen_16_16/div_gen_16_16.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet

create_ip -name clk_wiz -vendor xilinx.com -library ip -version 6.0 -module_name clk_wiz_hd -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.CLKOUT1_JITTER {258.703} \
  CONFIG.CLKOUT1_PHASE_ERROR {322.999} \
  CONFIG.CLKOUT1_REQUESTED_OUT_FREQ {371.25} \
  CONFIG.CLKOUT2_JITTER {337.616} \
  CONFIG.CLKOUT2_PHASE_ERROR {322.999} \
  CONFIG.CLKOUT2_REQUESTED_OUT_FREQ {74.25} \
  CONFIG.CLKOUT2_USED {true} \
  CONFIG.CLK_OUT1_PORT {pclkx5} \
  CONFIG.CLK_OUT2_PORT {pclk} \
  CONFIG.MMCM_CLKFBOUT_MULT_F {37.125} \
  CONFIG.MMCM_CLKOUT0_DIVIDE_F {2.000} \
  CONFIG.MMCM_CLKOUT1_DIVIDE {10} \
  CONFIG.MMCM_DIVCLK_DIVIDE {5} \
  CONFIG.NUM_OUT_CLKS {2} \
  CONFIG.PRIMARY_PORT {clk_in_100} \
] [get_ips clk_wiz_hd]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/clk_wiz_hd/clk_wiz_hd.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/clk_wiz_hd/clk_wiz_hd.xci}]
catch { config_ip_cache -export [get_ips -all clk_wiz_hd] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_hd/clk_wiz_hd.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/clk_wiz_hd/clk_wiz_hd.xci}]
launch_runs clk_wiz_hd_synth_1 -jobs 16
wait_on_run clk_wiz_hd_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/clk_wiz_hd/clk_wiz_hd.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet

create_ip -name fifo_generator -vendor xilinx.com -library ip -version 13.2 -module_name fifo_generator_0 -dir {$CORE_DIR}
set_property -dict [list \
  CONFIG.Fifo_Implementation {Independent_Clocks_Block_RAM} \
  CONFIG.Full_Threshold_Assert_Value {450} \
  CONFIG.Full_Threshold_Negate_Value {350} \
  CONFIG.Input_Data_Width {128} \
  CONFIG.Input_Depth {512} \
  CONFIG.Output_Data_Width {16} \
  CONFIG.Performance_Options {First_Word_Fall_Through} \
  CONFIG.Programmable_Full_Type {Multiple_Programmable_Full_Threshold_Constants} \
] [get_ips fifo_generator_0]
generate_target {instantiation_template} [get_files {$CORE_DIR_ESC/fifo_generator_0/fifo_generator_0.xci}]
generate_target all [get_files  {$CORE_DIR_ESC/fifo_generator_0/fifo_generator_0.xci}]
catch { config_ip_cache -export [get_ips -all fifo_generator_0] }
export_ip_user_files -of_objects [get_files {$CORE_DIR_ESC/fifo_generator_0/fifo_generator_0.xci}] -no_script -sync -force -quiet
create_ip_run [get_files -of_objects [get_fileset sources_1] {$CORE_DIR_ESC/fifo_generator_0/fifo_generator_0.xci}]
launch_runs fifo_generator_0_synth_1 -jobs 16
wait_on_run fifo_generator_0_synth_1
export_simulation -of_objects [get_files {$CORE_DIR_ESC/fifo_generator_0/fifo_generator_0.xci}] -directory {$CORE_DIR/ip_user_files/sim_scripts} -ip_user_files_dir {$CORE_DIR/ip_user_files} -ipstatic_source_dir {$CORE_DIR/ip_user_files/ipstatic} -lib_map_path [list {modelsim=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim} {questa=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/questa} {xcelium=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium} {vcs=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs} {riviera=$CORE_DIR/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}] -use_ip_compiled_libs -force -quiet
