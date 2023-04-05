// Generated from /home/justin/IdeaProjects/LucidParserV2/src/main/java/com/alchitry/labs/parsers/lucidv2/grammar/Lucid.g4 by ANTLR 4.12.0

package com.alchitry.labs.parsers.lucidv2.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LucidParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LucidVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSource(LucidParser.SourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal(LucidParser.GlobalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#global_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobal_stat(LucidParser.Global_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule(LucidParser.ModuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#param_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_list(LucidParser.Param_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#port_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort_list(LucidParser.Port_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#param_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_dec(LucidParser.Param_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#port_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort_dec(LucidParser.Port_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#input_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput_dec(LucidParser.Input_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#output_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput_dec(LucidParser.Output_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#inout_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInout_dec(LucidParser.Inout_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#param_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_name(LucidParser.Param_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#param_constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_constraint(LucidParser.Param_constraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#array_size}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_size(LucidParser.Array_sizeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#struct_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_type(LucidParser.Struct_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#struct_member_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_member_const(LucidParser.Struct_member_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#struct_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_const(LucidParser.Struct_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#module_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_body(LucidParser.Module_bodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatConst(LucidParser.StatConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatSig(LucidParser.StatSigContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatFSM}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatFSM(LucidParser.StatFSMContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatDFF(LucidParser.StatDFFContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatModuleInst(LucidParser.StatModuleInstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatAssign(LucidParser.StatAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatAlways(LucidParser.StatAlwaysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatStruct(LucidParser.StatStructContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#const_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst_dec(LucidParser.Const_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#assign_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_block(LucidParser.Assign_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#sig_con}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSig_con(LucidParser.Sig_conContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#param_con}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_con(LucidParser.Param_conContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#type_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_dec(LucidParser.Type_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#dff_single}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDff_single(LucidParser.Dff_singleContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#sig_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSig_dec(LucidParser.Sig_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#dff_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDff_dec(LucidParser.Dff_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#fsm_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFsm_dec(LucidParser.Fsm_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#fsm_states}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFsm_states(LucidParser.Fsm_statesContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#module_inst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModule_inst(LucidParser.Module_instContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#inst_cons}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInst_cons(LucidParser.Inst_consContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#con_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCon_list(LucidParser.Con_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnection(LucidParser.ConnectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#struct_member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_member(LucidParser.Struct_memberContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#struct_dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_dec(LucidParser.Struct_decContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#always_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlways_block(LucidParser.Always_blockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlwaysStat}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlwaysStat(LucidParser.AlwaysStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlwaysCase(LucidParser.AlwaysCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlwaysIf(LucidParser.AlwaysIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#always_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlwaysRepeat(LucidParser.AlwaysRepeatContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(LucidParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#assign_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_stat(LucidParser.Assign_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#array_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_index(LucidParser.Array_indexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bit_selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitSelectorConst(LucidParser.BitSelectorConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bit_selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitSelectorFixWidth(LucidParser.BitSelectorFixWidthContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#bit_selection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBit_selection(LucidParser.Bit_selectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignal(LucidParser.SignalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#case_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_stat(LucidParser.Case_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#case_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase_elem(LucidParser.Case_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#if_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stat(LucidParser.If_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#else_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_stat(LucidParser.Else_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#repeat_stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRepeat_stat(LucidParser.Repeat_statContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(LucidParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(LucidParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTernary(LucidParser.ExprTernaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNum(LucidParser.ExprNumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprConcat(LucidParser.ExprConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprReduction(LucidParser.ExprReductionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprInvert(LucidParser.ExprInvertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStruct(LucidParser.ExprStructContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprArray(LucidParser.ExprArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprShift(LucidParser.ExprShiftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAddSub(LucidParser.ExprAddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprLogical(LucidParser.ExprLogicalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNegate(LucidParser.ExprNegateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprGroup(LucidParser.ExprGroupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprBitwise(LucidParser.ExprBitwiseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFunction(LucidParser.ExprFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCompare(LucidParser.ExprCompareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprDup(LucidParser.ExprDupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprMultDiv(LucidParser.ExprMultDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSignal(LucidParser.ExprSignalContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(LucidParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemi(LucidParser.SemiContext ctx);
}