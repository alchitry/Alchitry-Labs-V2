// Generated from Lucid.g4 by ANTLR 4.10.1

package com.alchitry.labs.parsers.lucidv2.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LucidParser}.
 */
public interface LucidListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 */
	void enterSource(LucidParser.SourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 */
	void exitSource(LucidParser.SourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 */
	void enterGlobal(LucidParser.GlobalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 */
	void exitGlobal(LucidParser.GlobalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#global_stat}.
	 * @param ctx the parse tree
	 */
	void enterGlobal_stat(LucidParser.Global_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#global_stat}.
	 * @param ctx the parse tree
	 */
	void exitGlobal_stat(LucidParser.Global_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(LucidParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(LucidParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#param_list}.
	 * @param ctx the parse tree
	 */
	void enterParam_list(LucidParser.Param_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#param_list}.
	 * @param ctx the parse tree
	 */
	void exitParam_list(LucidParser.Param_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#port_list}.
	 * @param ctx the parse tree
	 */
	void enterPort_list(LucidParser.Port_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#port_list}.
	 * @param ctx the parse tree
	 */
	void exitPort_list(LucidParser.Port_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#param_dec}.
	 * @param ctx the parse tree
	 */
	void enterParam_dec(LucidParser.Param_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#param_dec}.
	 * @param ctx the parse tree
	 */
	void exitParam_dec(LucidParser.Param_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#port_dec}.
	 * @param ctx the parse tree
	 */
	void enterPort_dec(LucidParser.Port_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#port_dec}.
	 * @param ctx the parse tree
	 */
	void exitPort_dec(LucidParser.Port_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#input_dec}.
	 * @param ctx the parse tree
	 */
	void enterInput_dec(LucidParser.Input_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#input_dec}.
	 * @param ctx the parse tree
	 */
	void exitInput_dec(LucidParser.Input_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#output_dec}.
	 * @param ctx the parse tree
	 */
	void enterOutput_dec(LucidParser.Output_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#output_dec}.
	 * @param ctx the parse tree
	 */
	void exitOutput_dec(LucidParser.Output_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#inout_dec}.
	 * @param ctx the parse tree
	 */
	void enterInout_dec(LucidParser.Inout_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#inout_dec}.
	 * @param ctx the parse tree
	 */
	void exitInout_dec(LucidParser.Inout_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#param_name}.
	 * @param ctx the parse tree
	 */
	void enterParam_name(LucidParser.Param_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#param_name}.
	 * @param ctx the parse tree
	 */
	void exitParam_name(LucidParser.Param_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#param_constraint}.
	 * @param ctx the parse tree
	 */
	void enterParam_constraint(LucidParser.Param_constraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#param_constraint}.
	 * @param ctx the parse tree
	 */
	void exitParam_constraint(LucidParser.Param_constraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#array_size}.
	 * @param ctx the parse tree
	 */
	void enterArray_size(LucidParser.Array_sizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#array_size}.
	 * @param ctx the parse tree
	 */
	void exitArray_size(LucidParser.Array_sizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#struct_type}.
	 * @param ctx the parse tree
	 */
	void enterStruct_type(LucidParser.Struct_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#struct_type}.
	 * @param ctx the parse tree
	 */
	void exitStruct_type(LucidParser.Struct_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#struct_member_const}.
	 * @param ctx the parse tree
	 */
	void enterStruct_member_const(LucidParser.Struct_member_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#struct_member_const}.
	 * @param ctx the parse tree
	 */
	void exitStruct_member_const(LucidParser.Struct_member_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#struct_const}.
	 * @param ctx the parse tree
	 */
	void enterStruct_const(LucidParser.Struct_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#struct_const}.
	 * @param ctx the parse tree
	 */
	void exitStruct_const(LucidParser.Struct_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#module_body}.
	 * @param ctx the parse tree
	 */
	void enterModule_body(LucidParser.Module_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#module_body}.
	 * @param ctx the parse tree
	 */
	void exitModule_body(LucidParser.Module_bodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatConst(LucidParser.StatConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatConst(LucidParser.StatConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatSig(LucidParser.StatSigContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatSig(LucidParser.StatSigContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatFSM}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatFSM(LucidParser.StatFSMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatFSM}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatFSM(LucidParser.StatFSMContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatDFF(LucidParser.StatDFFContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatDFF(LucidParser.StatDFFContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatModuleInst(LucidParser.StatModuleInstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatModuleInst(LucidParser.StatModuleInstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatAssign(LucidParser.StatAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatAssign(LucidParser.StatAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatAlways(LucidParser.StatAlwaysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatAlways(LucidParser.StatAlwaysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatStruct(LucidParser.StatStructContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatStruct(LucidParser.StatStructContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#const_dec}.
	 * @param ctx the parse tree
	 */
	void enterConst_dec(LucidParser.Const_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#const_dec}.
	 * @param ctx the parse tree
	 */
	void exitConst_dec(LucidParser.Const_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#assign_block}.
	 * @param ctx the parse tree
	 */
	void enterAssign_block(LucidParser.Assign_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#assign_block}.
	 * @param ctx the parse tree
	 */
	void exitAssign_block(LucidParser.Assign_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#sig_con}.
	 * @param ctx the parse tree
	 */
	void enterSig_con(LucidParser.Sig_conContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#sig_con}.
	 * @param ctx the parse tree
	 */
	void exitSig_con(LucidParser.Sig_conContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#param_con}.
	 * @param ctx the parse tree
	 */
	void enterParam_con(LucidParser.Param_conContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#param_con}.
	 * @param ctx the parse tree
	 */
	void exitParam_con(LucidParser.Param_conContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#type_dec}.
	 * @param ctx the parse tree
	 */
	void enterType_dec(LucidParser.Type_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#type_dec}.
	 * @param ctx the parse tree
	 */
	void exitType_dec(LucidParser.Type_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#dff_single}.
	 * @param ctx the parse tree
	 */
	void enterDff_single(LucidParser.Dff_singleContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#dff_single}.
	 * @param ctx the parse tree
	 */
	void exitDff_single(LucidParser.Dff_singleContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#sig_dec}.
	 * @param ctx the parse tree
	 */
	void enterSig_dec(LucidParser.Sig_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#sig_dec}.
	 * @param ctx the parse tree
	 */
	void exitSig_dec(LucidParser.Sig_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#dff_dec}.
	 * @param ctx the parse tree
	 */
	void enterDff_dec(LucidParser.Dff_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#dff_dec}.
	 * @param ctx the parse tree
	 */
	void exitDff_dec(LucidParser.Dff_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#fsm_dec}.
	 * @param ctx the parse tree
	 */
	void enterFsm_dec(LucidParser.Fsm_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#fsm_dec}.
	 * @param ctx the parse tree
	 */
	void exitFsm_dec(LucidParser.Fsm_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#fsm_states}.
	 * @param ctx the parse tree
	 */
	void enterFsm_states(LucidParser.Fsm_statesContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#fsm_states}.
	 * @param ctx the parse tree
	 */
	void exitFsm_states(LucidParser.Fsm_statesContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#module_inst}.
	 * @param ctx the parse tree
	 */
	void enterModule_inst(LucidParser.Module_instContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#module_inst}.
	 * @param ctx the parse tree
	 */
	void exitModule_inst(LucidParser.Module_instContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#inst_cons}.
	 * @param ctx the parse tree
	 */
	void enterInst_cons(LucidParser.Inst_consContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#inst_cons}.
	 * @param ctx the parse tree
	 */
	void exitInst_cons(LucidParser.Inst_consContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#con_list}.
	 * @param ctx the parse tree
	 */
	void enterCon_list(LucidParser.Con_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#con_list}.
	 * @param ctx the parse tree
	 */
	void exitCon_list(LucidParser.Con_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 */
	void enterConnection(LucidParser.ConnectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 */
	void exitConnection(LucidParser.ConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#struct_member}.
	 * @param ctx the parse tree
	 */
	void enterStruct_member(LucidParser.Struct_memberContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#struct_member}.
	 * @param ctx the parse tree
	 */
	void exitStruct_member(LucidParser.Struct_memberContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#struct_dec}.
	 * @param ctx the parse tree
	 */
	void enterStruct_dec(LucidParser.Struct_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#struct_dec}.
	 * @param ctx the parse tree
	 */
	void exitStruct_dec(LucidParser.Struct_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#always_block}.
	 * @param ctx the parse tree
	 */
	void enterAlways_block(LucidParser.Always_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#always_block}.
	 * @param ctx the parse tree
	 */
	void exitAlways_block(LucidParser.Always_blockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysStat}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysStat(LucidParser.AlwaysStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysStat}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysStat(LucidParser.AlwaysStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysCase(LucidParser.AlwaysCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysCase(LucidParser.AlwaysCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysIf(LucidParser.AlwaysIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysIf(LucidParser.AlwaysIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysRepeat(LucidParser.AlwaysRepeatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysRepeat(LucidParser.AlwaysRepeatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(LucidParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(LucidParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#assign_stat}.
	 * @param ctx the parse tree
	 */
	void enterAssign_stat(LucidParser.Assign_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#assign_stat}.
	 * @param ctx the parse tree
	 */
	void exitAssign_stat(LucidParser.Assign_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#array_index}.
	 * @param ctx the parse tree
	 */
	void enterArray_index(LucidParser.Array_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#array_index}.
	 * @param ctx the parse tree
	 */
	void exitArray_index(LucidParser.Array_indexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bit_selector}.
	 * @param ctx the parse tree
	 */
	void enterBitSelectorConst(LucidParser.BitSelectorConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bit_selector}.
	 * @param ctx the parse tree
	 */
	void exitBitSelectorConst(LucidParser.BitSelectorConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bit_selector}.
	 * @param ctx the parse tree
	 */
	void enterBitSelectorFixWidth(LucidParser.BitSelectorFixWidthContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bit_selector}.
	 * @param ctx the parse tree
	 */
	void exitBitSelectorFixWidth(LucidParser.BitSelectorFixWidthContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#bit_selection}.
	 * @param ctx the parse tree
	 */
	void enterBit_selection(LucidParser.Bit_selectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#bit_selection}.
	 * @param ctx the parse tree
	 */
	void exitBit_selection(LucidParser.Bit_selectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 */
	void enterSignal(LucidParser.SignalContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 */
	void exitSignal(LucidParser.SignalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#case_stat}.
	 * @param ctx the parse tree
	 */
	void enterCase_stat(LucidParser.Case_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#case_stat}.
	 * @param ctx the parse tree
	 */
	void exitCase_stat(LucidParser.Case_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#case_elem}.
	 * @param ctx the parse tree
	 */
	void enterCase_elem(LucidParser.Case_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#case_elem}.
	 * @param ctx the parse tree
	 */
	void exitCase_elem(LucidParser.Case_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#if_stat}.
	 * @param ctx the parse tree
	 */
	void enterIf_stat(LucidParser.If_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#if_stat}.
	 * @param ctx the parse tree
	 */
	void exitIf_stat(LucidParser.If_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#else_stat}.
	 * @param ctx the parse tree
	 */
	void enterElse_stat(LucidParser.Else_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#else_stat}.
	 * @param ctx the parse tree
	 */
	void exitElse_stat(LucidParser.Else_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#repeat_stat}.
	 * @param ctx the parse tree
	 */
	void enterRepeat_stat(LucidParser.Repeat_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#repeat_stat}.
	 * @param ctx the parse tree
	 */
	void exitRepeat_stat(LucidParser.Repeat_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(LucidParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(LucidParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(LucidParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(LucidParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprTernary(LucidParser.ExprTernaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprTernary(LucidParser.ExprTernaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprNum(LucidParser.ExprNumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprNum(LucidParser.ExprNumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprConcat(LucidParser.ExprConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprConcat(LucidParser.ExprConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprReduction(LucidParser.ExprReductionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprReduction(LucidParser.ExprReductionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprInvert(LucidParser.ExprInvertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprInvert(LucidParser.ExprInvertContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprStruct(LucidParser.ExprStructContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprStruct(LucidParser.ExprStructContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprArray(LucidParser.ExprArrayContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprArray(LucidParser.ExprArrayContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprShift(LucidParser.ExprShiftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprShift(LucidParser.ExprShiftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprAddSub(LucidParser.ExprAddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprAddSub(LucidParser.ExprAddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprLogical(LucidParser.ExprLogicalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprLogical(LucidParser.ExprLogicalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprNegate(LucidParser.ExprNegateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprNegate(LucidParser.ExprNegateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprGroup(LucidParser.ExprGroupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprGroup(LucidParser.ExprGroupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprBitwise(LucidParser.ExprBitwiseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprBitwise(LucidParser.ExprBitwiseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprFunction(LucidParser.ExprFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprFunction(LucidParser.ExprFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprCompare(LucidParser.ExprCompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprCompare(LucidParser.ExprCompareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprDup(LucidParser.ExprDupContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprDup(LucidParser.ExprDupContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprMultDiv(LucidParser.ExprMultDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprMultDiv(LucidParser.ExprMultDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprSignal(LucidParser.ExprSignalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprSignal(LucidParser.ExprSignalContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(LucidParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(LucidParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 */
	void enterSemi(LucidParser.SemiContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 */
	void exitSemi(LucidParser.SemiContext ctx);
}