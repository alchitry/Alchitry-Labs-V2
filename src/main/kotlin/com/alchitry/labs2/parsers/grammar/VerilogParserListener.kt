// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link VerilogParser}.
 */
interface VerilogParserListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link VerilogParser#library_text}.
     * @param ctx the parse tree
     */
    fun enterLibrary_text(ctx: VerilogParser.Library_textContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_text}.
     * @param ctx the parse tree
     */
    fun exitLibrary_text(ctx: VerilogParser.Library_textContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_description}.
     * @param ctx the parse tree
     */
    fun enterLibrary_description(ctx: VerilogParser.Library_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_description}.
     * @param ctx the parse tree
     */
    fun exitLibrary_description(ctx: VerilogParser.Library_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_declaration}.
     * @param ctx the parse tree
     */
    fun enterLibrary_declaration(ctx: VerilogParser.Library_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_declaration}.
     * @param ctx the parse tree
     */
    fun exitLibrary_declaration(ctx: VerilogParser.Library_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_incdir}.
     * @param ctx the parse tree
     */
    fun enterLibrary_incdir(ctx: VerilogParser.Library_incdirContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_incdir}.
     * @param ctx the parse tree
     */
    fun exitLibrary_incdir(ctx: VerilogParser.Library_incdirContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#include_statement}.
     * @param ctx the parse tree
     */
    fun enterInclude_statement(ctx: VerilogParser.Include_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#include_statement}.
     * @param ctx the parse tree
     */
    fun exitInclude_statement(ctx: VerilogParser.Include_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#file_path_spec}.
     * @param ctx the parse tree
     */
    fun enterFile_path_spec(ctx: VerilogParser.File_path_specContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#file_path_spec}.
     * @param ctx the parse tree
     */
    fun exitFile_path_spec(ctx: VerilogParser.File_path_specContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#source_text}.
     * @param ctx the parse tree
     */
    fun enterSource_text(ctx: VerilogParser.Source_textContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#source_text}.
     * @param ctx the parse tree
     */
    fun exitSource_text(ctx: VerilogParser.Source_textContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#description}.
     * @param ctx the parse tree
     */
    fun enterDescription(ctx: VerilogParser.DescriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#description}.
     * @param ctx the parse tree
     */
    fun exitDescription(ctx: VerilogParser.DescriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_declaration}.
     * @param ctx the parse tree
     */
    fun enterModule_declaration(ctx: VerilogParser.Module_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_declaration}.
     * @param ctx the parse tree
     */
    fun exitModule_declaration(ctx: VerilogParser.Module_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_keyword}.
     * @param ctx the parse tree
     */
    fun enterModule_keyword(ctx: VerilogParser.Module_keywordContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_keyword}.
     * @param ctx the parse tree
     */
    fun exitModule_keyword(ctx: VerilogParser.Module_keywordContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    fun enterModule_parameter_port_list(ctx: VerilogParser.Module_parameter_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    fun exitModule_parameter_port_list(ctx: VerilogParser.Module_parameter_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    fun enterList_of_port_declarations(ctx: VerilogParser.List_of_port_declarationsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    fun exitList_of_port_declarations(ctx: VerilogParser.List_of_port_declarationsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port}.
     * @param ctx the parse tree
     */
    fun enterPort(ctx: VerilogParser.PortContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port}.
     * @param ctx the parse tree
     */
    fun exitPort(ctx: VerilogParser.PortContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_implicit}.
     * @param ctx the parse tree
     */
    fun enterPort_implicit(ctx: VerilogParser.Port_implicitContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_implicit}.
     * @param ctx the parse tree
     */
    fun exitPort_implicit(ctx: VerilogParser.Port_implicitContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_explicit}.
     * @param ctx the parse tree
     */
    fun enterPort_explicit(ctx: VerilogParser.Port_explicitContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_explicit}.
     * @param ctx the parse tree
     */
    fun exitPort_explicit(ctx: VerilogParser.Port_explicitContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_expression}.
     * @param ctx the parse tree
     */
    fun enterPort_expression(ctx: VerilogParser.Port_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_expression}.
     * @param ctx the parse tree
     */
    fun exitPort_expression(ctx: VerilogParser.Port_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_reference}.
     * @param ctx the parse tree
     */
    fun enterPort_reference(ctx: VerilogParser.Port_referenceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_reference}.
     * @param ctx the parse tree
     */
    fun exitPort_reference(ctx: VerilogParser.Port_referenceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_declaration}.
     * @param ctx the parse tree
     */
    fun enterPort_declaration(ctx: VerilogParser.Port_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_declaration}.
     * @param ctx the parse tree
     */
    fun exitPort_declaration(ctx: VerilogParser.Port_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_item}.
     * @param ctx the parse tree
     */
    fun enterModule_item(ctx: VerilogParser.Module_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_item}.
     * @param ctx the parse tree
     */
    fun exitModule_item(ctx: VerilogParser.Module_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    fun enterModule_or_generate_item(ctx: VerilogParser.Module_or_generate_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    fun exitModule_or_generate_item(ctx: VerilogParser.Module_or_generate_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterModule_or_generate_item_declaration(ctx: VerilogParser.Module_or_generate_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitModule_or_generate_item_declaration(ctx: VerilogParser.Module_or_generate_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_override}.
     * @param ctx the parse tree
     */
    fun enterParameter_override(ctx: VerilogParser.Parameter_overrideContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_override}.
     * @param ctx the parse tree
     */
    fun exitParameter_override(ctx: VerilogParser.Parameter_overrideContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#config_declaration}.
     * @param ctx the parse tree
     */
    fun enterConfig_declaration(ctx: VerilogParser.Config_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#config_declaration}.
     * @param ctx the parse tree
     */
    fun exitConfig_declaration(ctx: VerilogParser.Config_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#design_statement}.
     * @param ctx the parse tree
     */
    fun enterDesign_statement(ctx: VerilogParser.Design_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#design_statement}.
     * @param ctx the parse tree
     */
    fun exitDesign_statement(ctx: VerilogParser.Design_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#design_statement_item}.
     * @param ctx the parse tree
     */
    fun enterDesign_statement_item(ctx: VerilogParser.Design_statement_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#design_statement_item}.
     * @param ctx the parse tree
     */
    fun exitDesign_statement_item(ctx: VerilogParser.Design_statement_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#config_rule_statement}.
     * @param ctx the parse tree
     */
    fun enterConfig_rule_statement(ctx: VerilogParser.Config_rule_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#config_rule_statement}.
     * @param ctx the parse tree
     */
    fun exitConfig_rule_statement(ctx: VerilogParser.Config_rule_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#default_clause}.
     * @param ctx the parse tree
     */
    fun enterDefault_clause(ctx: VerilogParser.Default_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#default_clause}.
     * @param ctx the parse tree
     */
    fun exitDefault_clause(ctx: VerilogParser.Default_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inst_clause}.
     * @param ctx the parse tree
     */
    fun enterInst_clause(ctx: VerilogParser.Inst_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inst_clause}.
     * @param ctx the parse tree
     */
    fun exitInst_clause(ctx: VerilogParser.Inst_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inst_name}.
     * @param ctx the parse tree
     */
    fun enterInst_name(ctx: VerilogParser.Inst_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inst_name}.
     * @param ctx the parse tree
     */
    fun exitInst_name(ctx: VerilogParser.Inst_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cell_clause}.
     * @param ctx the parse tree
     */
    fun enterCell_clause(ctx: VerilogParser.Cell_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cell_clause}.
     * @param ctx the parse tree
     */
    fun exitCell_clause(ctx: VerilogParser.Cell_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#liblist_clause}.
     * @param ctx the parse tree
     */
    fun enterLiblist_clause(ctx: VerilogParser.Liblist_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#liblist_clause}.
     * @param ctx the parse tree
     */
    fun exitLiblist_clause(ctx: VerilogParser.Liblist_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#use_clause}.
     * @param ctx the parse tree
     */
    fun enterUse_clause(ctx: VerilogParser.Use_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#use_clause}.
     * @param ctx the parse tree
     */
    fun exitUse_clause(ctx: VerilogParser.Use_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    fun enterLocal_parameter_declaration(ctx: VerilogParser.Local_parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    fun exitLocal_parameter_declaration(ctx: VerilogParser.Local_parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_declaration}.
     * @param ctx the parse tree
     */
    fun enterParameter_declaration(ctx: VerilogParser.Parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_declaration}.
     * @param ctx the parse tree
     */
    fun exitParameter_declaration(ctx: VerilogParser.Parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specparam_declaration}.
     * @param ctx the parse tree
     */
    fun enterSpecparam_declaration(ctx: VerilogParser.Specparam_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specparam_declaration}.
     * @param ctx the parse tree
     */
    fun exitSpecparam_declaration(ctx: VerilogParser.Specparam_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_type}.
     * @param ctx the parse tree
     */
    fun enterParameter_type(ctx: VerilogParser.Parameter_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_type}.
     * @param ctx the parse tree
     */
    fun exitParameter_type(ctx: VerilogParser.Parameter_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inout_declaration}.
     * @param ctx the parse tree
     */
    fun enterInout_declaration(ctx: VerilogParser.Inout_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inout_declaration}.
     * @param ctx the parse tree
     */
    fun exitInout_declaration(ctx: VerilogParser.Inout_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_declaration}.
     * @param ctx the parse tree
     */
    fun enterInput_declaration(ctx: VerilogParser.Input_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_declaration}.
     * @param ctx the parse tree
     */
    fun exitInput_declaration(ctx: VerilogParser.Input_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_declaration}.
     * @param ctx the parse tree
     */
    fun enterOutput_declaration(ctx: VerilogParser.Output_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_declaration}.
     * @param ctx the parse tree
     */
    fun exitOutput_declaration(ctx: VerilogParser.Output_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_declaration}.
     * @param ctx the parse tree
     */
    fun enterEvent_declaration(ctx: VerilogParser.Event_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_declaration}.
     * @param ctx the parse tree
     */
    fun exitEvent_declaration(ctx: VerilogParser.Event_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#integer_declaration}.
     * @param ctx the parse tree
     */
    fun enterInteger_declaration(ctx: VerilogParser.Integer_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#integer_declaration}.
     * @param ctx the parse tree
     */
    fun exitInteger_declaration(ctx: VerilogParser.Integer_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_declaration}.
     * @param ctx the parse tree
     */
    fun enterNet_declaration(ctx: VerilogParser.Net_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_declaration}.
     * @param ctx the parse tree
     */
    fun exitNet_declaration(ctx: VerilogParser.Net_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_declaration}.
     * @param ctx the parse tree
     */
    fun enterReal_declaration(ctx: VerilogParser.Real_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_declaration}.
     * @param ctx the parse tree
     */
    fun exitReal_declaration(ctx: VerilogParser.Real_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#realtime_declaration}.
     * @param ctx the parse tree
     */
    fun enterRealtime_declaration(ctx: VerilogParser.Realtime_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#realtime_declaration}.
     * @param ctx the parse tree
     */
    fun exitRealtime_declaration(ctx: VerilogParser.Realtime_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#reg_declaration}.
     * @param ctx the parse tree
     */
    fun enterReg_declaration(ctx: VerilogParser.Reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#reg_declaration}.
     * @param ctx the parse tree
     */
    fun exitReg_declaration(ctx: VerilogParser.Reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#time_declaration}.
     * @param ctx the parse tree
     */
    fun enterTime_declaration(ctx: VerilogParser.Time_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#time_declaration}.
     * @param ctx the parse tree
     */
    fun exitTime_declaration(ctx: VerilogParser.Time_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_type}.
     * @param ctx the parse tree
     */
    fun enterNet_type(ctx: VerilogParser.Net_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_type}.
     * @param ctx the parse tree
     */
    fun exitNet_type(ctx: VerilogParser.Net_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_variable_type}.
     * @param ctx the parse tree
     */
    fun enterOutput_variable_type(ctx: VerilogParser.Output_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_variable_type}.
     * @param ctx the parse tree
     */
    fun exitOutput_variable_type(ctx: VerilogParser.Output_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_type}.
     * @param ctx the parse tree
     */
    fun enterReal_type(ctx: VerilogParser.Real_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_type}.
     * @param ctx the parse tree
     */
    fun exitReal_type(ctx: VerilogParser.Real_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_type}.
     * @param ctx the parse tree
     */
    fun enterVariable_type(ctx: VerilogParser.Variable_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_type}.
     * @param ctx the parse tree
     */
    fun exitVariable_type(ctx: VerilogParser.Variable_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#drive_strength}.
     * @param ctx the parse tree
     */
    fun enterDrive_strength(ctx: VerilogParser.Drive_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#drive_strength}.
     * @param ctx the parse tree
     */
    fun exitDrive_strength(ctx: VerilogParser.Drive_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#strength0}.
     * @param ctx the parse tree
     */
    fun enterStrength0(ctx: VerilogParser.Strength0Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#strength0}.
     * @param ctx the parse tree
     */
    fun exitStrength0(ctx: VerilogParser.Strength0Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#strength1}.
     * @param ctx the parse tree
     */
    fun enterStrength1(ctx: VerilogParser.Strength1Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#strength1}.
     * @param ctx the parse tree
     */
    fun exitStrength1(ctx: VerilogParser.Strength1Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#charge_strength}.
     * @param ctx the parse tree
     */
    fun enterCharge_strength(ctx: VerilogParser.Charge_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#charge_strength}.
     * @param ctx the parse tree
     */
    fun exitCharge_strength(ctx: VerilogParser.Charge_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay3}.
     * @param ctx the parse tree
     */
    fun enterDelay3(ctx: VerilogParser.Delay3Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay3}.
     * @param ctx the parse tree
     */
    fun exitDelay3(ctx: VerilogParser.Delay3Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay2}.
     * @param ctx the parse tree
     */
    fun enterDelay2(ctx: VerilogParser.Delay2Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay2}.
     * @param ctx the parse tree
     */
    fun exitDelay2(ctx: VerilogParser.Delay2Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay_value}.
     * @param ctx the parse tree
     */
    fun enterDelay_value(ctx: VerilogParser.Delay_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay_value}.
     * @param ctx the parse tree
     */
    fun exitDelay_value(ctx: VerilogParser.Delay_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_defparam_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_defparam_assignments(ctx: VerilogParser.List_of_defparam_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_defparam_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_defparam_assignments(ctx: VerilogParser.List_of_defparam_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_event_identifiers(ctx: VerilogParser.List_of_event_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_event_identifiers(ctx: VerilogParser.List_of_event_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_id}.
     * @param ctx the parse tree
     */
    fun enterEvent_id(ctx: VerilogParser.Event_idContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_id}.
     * @param ctx the parse tree
     */
    fun exitEvent_id(ctx: VerilogParser.Event_idContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_net_decl_assignments(ctx: VerilogParser.List_of_net_decl_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_net_decl_assignments(ctx: VerilogParser.List_of_net_decl_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_net_identifiers(ctx: VerilogParser.List_of_net_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_net_identifiers(ctx: VerilogParser.List_of_net_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_id}.
     * @param ctx the parse tree
     */
    fun enterNet_id(ctx: VerilogParser.Net_idContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_id}.
     * @param ctx the parse tree
     */
    fun exitNet_id(ctx: VerilogParser.Net_idContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_param_assignments(ctx: VerilogParser.List_of_param_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_param_assignments(ctx: VerilogParser.List_of_param_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_port_identifiers(ctx: VerilogParser.List_of_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_port_identifiers(ctx: VerilogParser.List_of_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_real_identifiers(ctx: VerilogParser.List_of_real_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_real_identifiers(ctx: VerilogParser.List_of_real_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_specparam_assignments(ctx: VerilogParser.List_of_specparam_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_specparam_assignments(ctx: VerilogParser.List_of_specparam_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_variable_identifiers(ctx: VerilogParser.List_of_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_variable_identifiers(ctx: VerilogParser.List_of_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_variable_port_identifiers(ctx: VerilogParser.List_of_variable_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_variable_port_identifiers(ctx: VerilogParser.List_of_variable_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#var_port_id}.
     * @param ctx the parse tree
     */
    fun enterVar_port_id(ctx: VerilogParser.Var_port_idContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#var_port_id}.
     * @param ctx the parse tree
     */
    fun exitVar_port_id(ctx: VerilogParser.Var_port_idContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#defparam_assignment}.
     * @param ctx the parse tree
     */
    fun enterDefparam_assignment(ctx: VerilogParser.Defparam_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#defparam_assignment}.
     * @param ctx the parse tree
     */
    fun exitDefparam_assignment(ctx: VerilogParser.Defparam_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    fun enterNet_decl_assignment(ctx: VerilogParser.Net_decl_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    fun exitNet_decl_assignment(ctx: VerilogParser.Net_decl_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#param_assignment}.
     * @param ctx the parse tree
     */
    fun enterParam_assignment(ctx: VerilogParser.Param_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#param_assignment}.
     * @param ctx the parse tree
     */
    fun exitParam_assignment(ctx: VerilogParser.Param_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specparam_assignment}.
     * @param ctx the parse tree
     */
    fun enterSpecparam_assignment(ctx: VerilogParser.Specparam_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specparam_assignment}.
     * @param ctx the parse tree
     */
    fun exitSpecparam_assignment(ctx: VerilogParser.Specparam_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    fun enterPulse_control_specparam(ctx: VerilogParser.Pulse_control_specparamContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    fun exitPulse_control_specparam(ctx: VerilogParser.Pulse_control_specparamContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#error_limit_value}.
     * @param ctx the parse tree
     */
    fun enterError_limit_value(ctx: VerilogParser.Error_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#error_limit_value}.
     * @param ctx the parse tree
     */
    fun exitError_limit_value(ctx: VerilogParser.Error_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#reject_limit_value}.
     * @param ctx the parse tree
     */
    fun enterReject_limit_value(ctx: VerilogParser.Reject_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#reject_limit_value}.
     * @param ctx the parse tree
     */
    fun exitReject_limit_value(ctx: VerilogParser.Reject_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#limit_value}.
     * @param ctx the parse tree
     */
    fun enterLimit_value(ctx: VerilogParser.Limit_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#limit_value}.
     * @param ctx the parse tree
     */
    fun exitLimit_value(ctx: VerilogParser.Limit_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#dimension}.
     * @param ctx the parse tree
     */
    fun enterDimension(ctx: VerilogParser.DimensionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#dimension}.
     * @param ctx the parse tree
     */
    fun exitDimension(ctx: VerilogParser.DimensionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#range_}.
     * @param ctx the parse tree
     */
    fun enterRange_(ctx: VerilogParser.Range_Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#range_}.
     * @param ctx the parse tree
     */
    fun exitRange_(ctx: VerilogParser.Range_Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_declaration}.
     * @param ctx the parse tree
     */
    fun enterFunction_declaration(ctx: VerilogParser.Function_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_declaration}.
     * @param ctx the parse tree
     */
    fun exitFunction_declaration(ctx: VerilogParser.Function_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterFunction_item_declaration(ctx: VerilogParser.Function_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitFunction_item_declaration(ctx: VerilogParser.Function_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_port_list}.
     * @param ctx the parse tree
     */
    fun enterFunction_port_list(ctx: VerilogParser.Function_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_port_list}.
     * @param ctx the parse tree
     */
    fun exitFunction_port_list(ctx: VerilogParser.Function_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#func_port_item}.
     * @param ctx the parse tree
     */
    fun enterFunc_port_item(ctx: VerilogParser.Func_port_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#func_port_item}.
     * @param ctx the parse tree
     */
    fun exitFunc_port_item(ctx: VerilogParser.Func_port_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_range_or_type}.
     * @param ctx the parse tree
     */
    fun enterFunction_range_or_type(ctx: VerilogParser.Function_range_or_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_range_or_type}.
     * @param ctx the parse tree
     */
    fun exitFunction_range_or_type(ctx: VerilogParser.Function_range_or_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_declaration}.
     * @param ctx the parse tree
     */
    fun enterTask_declaration(ctx: VerilogParser.Task_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_declaration}.
     * @param ctx the parse tree
     */
    fun exitTask_declaration(ctx: VerilogParser.Task_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterTask_item_declaration(ctx: VerilogParser.Task_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitTask_item_declaration(ctx: VerilogParser.Task_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_port_list}.
     * @param ctx the parse tree
     */
    fun enterTask_port_list(ctx: VerilogParser.Task_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_port_list}.
     * @param ctx the parse tree
     */
    fun exitTask_port_list(ctx: VerilogParser.Task_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_port_item}.
     * @param ctx the parse tree
     */
    fun enterTask_port_item(ctx: VerilogParser.Task_port_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_port_item}.
     * @param ctx the parse tree
     */
    fun exitTask_port_item(ctx: VerilogParser.Task_port_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tf_input_declaration}.
     * @param ctx the parse tree
     */
    fun enterTf_input_declaration(ctx: VerilogParser.Tf_input_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tf_input_declaration}.
     * @param ctx the parse tree
     */
    fun exitTf_input_declaration(ctx: VerilogParser.Tf_input_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tf_output_declaration}.
     * @param ctx the parse tree
     */
    fun enterTf_output_declaration(ctx: VerilogParser.Tf_output_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tf_output_declaration}.
     * @param ctx the parse tree
     */
    fun exitTf_output_declaration(ctx: VerilogParser.Tf_output_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tf_inout_declaration}.
     * @param ctx the parse tree
     */
    fun enterTf_inout_declaration(ctx: VerilogParser.Tf_inout_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tf_inout_declaration}.
     * @param ctx the parse tree
     */
    fun exitTf_inout_declaration(ctx: VerilogParser.Tf_inout_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_port_type}.
     * @param ctx the parse tree
     */
    fun enterTask_port_type(ctx: VerilogParser.Task_port_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_port_type}.
     * @param ctx the parse tree
     */
    fun exitTask_port_type(ctx: VerilogParser.Task_port_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterBlock_item_declaration(ctx: VerilogParser.Block_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitBlock_item_declaration(ctx: VerilogParser.Block_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_block_variable_identifiers(ctx: VerilogParser.List_of_block_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_block_variable_identifiers(ctx: VerilogParser.List_of_block_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_block_real_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_block_real_identifiers(ctx: VerilogParser.List_of_block_real_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_block_real_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_block_real_identifiers(ctx: VerilogParser.List_of_block_real_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_variable_type}.
     * @param ctx the parse tree
     */
    fun enterBlock_variable_type(ctx: VerilogParser.Block_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_variable_type}.
     * @param ctx the parse tree
     */
    fun exitBlock_variable_type(ctx: VerilogParser.Block_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_real_type}.
     * @param ctx the parse tree
     */
    fun enterBlock_real_type(ctx: VerilogParser.Block_real_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_real_type}.
     * @param ctx the parse tree
     */
    fun exitBlock_real_type(ctx: VerilogParser.Block_real_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#gate_instantiation}.
     * @param ctx the parse tree
     */
    fun enterGate_instantiation(ctx: VerilogParser.Gate_instantiationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#gate_instantiation}.
     * @param ctx the parse tree
     */
    fun exitGate_instantiation(ctx: VerilogParser.Gate_instantiationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterCmos_switch_instance(ctx: VerilogParser.Cmos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitCmos_switch_instance(ctx: VerilogParser.Cmos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterEnable_gate_instance(ctx: VerilogParser.Enable_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitEnable_gate_instance(ctx: VerilogParser.Enable_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterMos_switch_instance(ctx: VerilogParser.Mos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitMos_switch_instance(ctx: VerilogParser.Mos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterN_input_gate_instance(ctx: VerilogParser.N_input_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitN_input_gate_instance(ctx: VerilogParser.N_input_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterN_output_gate_instance(ctx: VerilogParser.N_output_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitN_output_gate_instance(ctx: VerilogParser.N_output_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterPass_switch_instance(ctx: VerilogParser.Pass_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitPass_switch_instance(ctx: VerilogParser.Pass_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterPass_enable_switch_instance(ctx: VerilogParser.Pass_enable_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitPass_enable_switch_instance(ctx: VerilogParser.Pass_enable_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterPull_gate_instance(ctx: VerilogParser.Pull_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitPull_gate_instance(ctx: VerilogParser.Pull_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterName_of_gate_instance(ctx: VerilogParser.Name_of_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitName_of_gate_instance(ctx: VerilogParser.Name_of_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pulldown_strength}.
     * @param ctx the parse tree
     */
    fun enterPulldown_strength(ctx: VerilogParser.Pulldown_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pulldown_strength}.
     * @param ctx the parse tree
     */
    fun exitPulldown_strength(ctx: VerilogParser.Pulldown_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pullup_strength}.
     * @param ctx the parse tree
     */
    fun enterPullup_strength(ctx: VerilogParser.Pullup_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pullup_strength}.
     * @param ctx the parse tree
     */
    fun exitPullup_strength(ctx: VerilogParser.Pullup_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#enable_terminal}.
     * @param ctx the parse tree
     */
    fun enterEnable_terminal(ctx: VerilogParser.Enable_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#enable_terminal}.
     * @param ctx the parse tree
     */
    fun exitEnable_terminal(ctx: VerilogParser.Enable_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inout_terminal}.
     * @param ctx the parse tree
     */
    fun enterInout_terminal(ctx: VerilogParser.Inout_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inout_terminal}.
     * @param ctx the parse tree
     */
    fun exitInout_terminal(ctx: VerilogParser.Inout_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_terminal}.
     * @param ctx the parse tree
     */
    fun enterInput_terminal(ctx: VerilogParser.Input_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_terminal}.
     * @param ctx the parse tree
     */
    fun exitInput_terminal(ctx: VerilogParser.Input_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    fun enterNcontrol_terminal(ctx: VerilogParser.Ncontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    fun exitNcontrol_terminal(ctx: VerilogParser.Ncontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_terminal}.
     * @param ctx the parse tree
     */
    fun enterOutput_terminal(ctx: VerilogParser.Output_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_terminal}.
     * @param ctx the parse tree
     */
    fun exitOutput_terminal(ctx: VerilogParser.Output_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    fun enterPcontrol_terminal(ctx: VerilogParser.Pcontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    fun exitPcontrol_terminal(ctx: VerilogParser.Pcontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    fun enterCmos_switchtype(ctx: VerilogParser.Cmos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    fun exitCmos_switchtype(ctx: VerilogParser.Cmos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#enable_gatetype}.
     * @param ctx the parse tree
     */
    fun enterEnable_gatetype(ctx: VerilogParser.Enable_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#enable_gatetype}.
     * @param ctx the parse tree
     */
    fun exitEnable_gatetype(ctx: VerilogParser.Enable_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#mos_switchtype}.
     * @param ctx the parse tree
     */
    fun enterMos_switchtype(ctx: VerilogParser.Mos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#mos_switchtype}.
     * @param ctx the parse tree
     */
    fun exitMos_switchtype(ctx: VerilogParser.Mos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    fun enterN_input_gatetype(ctx: VerilogParser.N_input_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    fun exitN_input_gatetype(ctx: VerilogParser.N_input_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    fun enterN_output_gatetype(ctx: VerilogParser.N_output_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    fun exitN_output_gatetype(ctx: VerilogParser.N_output_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    fun enterPass_en_switchtype(ctx: VerilogParser.Pass_en_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    fun exitPass_en_switchtype(ctx: VerilogParser.Pass_en_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_switchtype}.
     * @param ctx the parse tree
     */
    fun enterPass_switchtype(ctx: VerilogParser.Pass_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_switchtype}.
     * @param ctx the parse tree
     */
    fun exitPass_switchtype(ctx: VerilogParser.Pass_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_instantiation}.
     * @param ctx the parse tree
     */
    fun enterModule_instantiation(ctx: VerilogParser.Module_instantiationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_instantiation}.
     * @param ctx the parse tree
     */
    fun exitModule_instantiation(ctx: VerilogParser.Module_instantiationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    fun enterParameter_value_assignment(ctx: VerilogParser.Parameter_value_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    fun exitParameter_value_assignment(ctx: VerilogParser.Parameter_value_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_parameter_assignments(ctx: VerilogParser.List_of_parameter_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_parameter_assignments(ctx: VerilogParser.List_of_parameter_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun enterOrdered_parameter_assignment(ctx: VerilogParser.Ordered_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun exitOrdered_parameter_assignment(ctx: VerilogParser.Ordered_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun enterNamed_parameter_assignment(ctx: VerilogParser.Named_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun exitNamed_parameter_assignment(ctx: VerilogParser.Named_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_instance}.
     * @param ctx the parse tree
     */
    fun enterModule_instance(ctx: VerilogParser.Module_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_instance}.
     * @param ctx the parse tree
     */
    fun exitModule_instance(ctx: VerilogParser.Module_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#name_of_module_instance}.
     * @param ctx the parse tree
     */
    fun enterName_of_module_instance(ctx: VerilogParser.Name_of_module_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#name_of_module_instance}.
     * @param ctx the parse tree
     */
    fun exitName_of_module_instance(ctx: VerilogParser.Name_of_module_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    fun enterList_of_port_connections(ctx: VerilogParser.List_of_port_connectionsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    fun exitList_of_port_connections(ctx: VerilogParser.List_of_port_connectionsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    fun enterOrdered_port_connection(ctx: VerilogParser.Ordered_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    fun exitOrdered_port_connection(ctx: VerilogParser.Ordered_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#named_port_connection}.
     * @param ctx the parse tree
     */
    fun enterNamed_port_connection(ctx: VerilogParser.Named_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#named_port_connection}.
     * @param ctx the parse tree
     */
    fun exitNamed_port_connection(ctx: VerilogParser.Named_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_region}.
     * @param ctx the parse tree
     */
    fun enterGenerate_region(ctx: VerilogParser.Generate_regionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_region}.
     * @param ctx the parse tree
     */
    fun exitGenerate_region(ctx: VerilogParser.Generate_regionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_declaration}.
     * @param ctx the parse tree
     */
    fun enterGenvar_declaration(ctx: VerilogParser.Genvar_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_declaration}.
     * @param ctx the parse tree
     */
    fun exitGenvar_declaration(ctx: VerilogParser.Genvar_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_genvar_identifiers(ctx: VerilogParser.List_of_genvar_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_genvar_identifiers(ctx: VerilogParser.List_of_genvar_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#loop_generate_construct}.
     * @param ctx the parse tree
     */
    fun enterLoop_generate_construct(ctx: VerilogParser.Loop_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#loop_generate_construct}.
     * @param ctx the parse tree
     */
    fun exitLoop_generate_construct(ctx: VerilogParser.Loop_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_initialization}.
     * @param ctx the parse tree
     */
    fun enterGenvar_initialization(ctx: VerilogParser.Genvar_initializationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_initialization}.
     * @param ctx the parse tree
     */
    fun exitGenvar_initialization(ctx: VerilogParser.Genvar_initializationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_expression}.
     * @param ctx the parse tree
     */
    fun enterGenvar_expression(ctx: VerilogParser.Genvar_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_expression}.
     * @param ctx the parse tree
     */
    fun exitGenvar_expression(ctx: VerilogParser.Genvar_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_iteration}.
     * @param ctx the parse tree
     */
    fun enterGenvar_iteration(ctx: VerilogParser.Genvar_iterationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_iteration}.
     * @param ctx the parse tree
     */
    fun exitGenvar_iteration(ctx: VerilogParser.Genvar_iterationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#conditional_generate_construct}.
     * @param ctx the parse tree
     */
    fun enterConditional_generate_construct(ctx: VerilogParser.Conditional_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#conditional_generate_construct}.
     * @param ctx the parse tree
     */
    fun exitConditional_generate_construct(ctx: VerilogParser.Conditional_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#if_generate_construct}.
     * @param ctx the parse tree
     */
    fun enterIf_generate_construct(ctx: VerilogParser.If_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#if_generate_construct}.
     * @param ctx the parse tree
     */
    fun exitIf_generate_construct(ctx: VerilogParser.If_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_generate_construct}.
     * @param ctx the parse tree
     */
    fun enterCase_generate_construct(ctx: VerilogParser.Case_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_generate_construct}.
     * @param ctx the parse tree
     */
    fun exitCase_generate_construct(ctx: VerilogParser.Case_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_generate_item}.
     * @param ctx the parse tree
     */
    fun enterCase_generate_item(ctx: VerilogParser.Case_generate_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_generate_item}.
     * @param ctx the parse tree
     */
    fun exitCase_generate_item(ctx: VerilogParser.Case_generate_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block}.
     * @param ctx the parse tree
     */
    fun enterGenerate_block(ctx: VerilogParser.Generate_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block}.
     * @param ctx the parse tree
     */
    fun exitGenerate_block(ctx: VerilogParser.Generate_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block_name}.
     * @param ctx the parse tree
     */
    fun enterGenerate_block_name(ctx: VerilogParser.Generate_block_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block_name}.
     * @param ctx the parse tree
     */
    fun exitGenerate_block_name(ctx: VerilogParser.Generate_block_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block_or_null}.
     * @param ctx the parse tree
     */
    fun enterGenerate_block_or_null(ctx: VerilogParser.Generate_block_or_nullContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block_or_null}.
     * @param ctx the parse tree
     */
    fun exitGenerate_block_or_null(ctx: VerilogParser.Generate_block_or_nullContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_declaration}.
     * @param ctx the parse tree
     */
    fun enterUdp_declaration(ctx: VerilogParser.Udp_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_declaration}.
     * @param ctx the parse tree
     */
    fun exitUdp_declaration(ctx: VerilogParser.Udp_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_port_list}.
     * @param ctx the parse tree
     */
    fun enterUdp_port_list(ctx: VerilogParser.Udp_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_port_list}.
     * @param ctx the parse tree
     */
    fun exitUdp_port_list(ctx: VerilogParser.Udp_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_declaration_port_list}.
     * @param ctx the parse tree
     */
    fun enterUdp_declaration_port_list(ctx: VerilogParser.Udp_declaration_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_declaration_port_list}.
     * @param ctx the parse tree
     */
    fun exitUdp_declaration_port_list(ctx: VerilogParser.Udp_declaration_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_port_declaration}.
     * @param ctx the parse tree
     */
    fun enterUdp_port_declaration(ctx: VerilogParser.Udp_port_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_port_declaration}.
     * @param ctx the parse tree
     */
    fun exitUdp_port_declaration(ctx: VerilogParser.Udp_port_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_output_declaration}.
     * @param ctx the parse tree
     */
    fun enterUdp_output_declaration(ctx: VerilogParser.Udp_output_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_output_declaration}.
     * @param ctx the parse tree
     */
    fun exitUdp_output_declaration(ctx: VerilogParser.Udp_output_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_input_declaration}.
     * @param ctx the parse tree
     */
    fun enterUdp_input_declaration(ctx: VerilogParser.Udp_input_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_input_declaration}.
     * @param ctx the parse tree
     */
    fun exitUdp_input_declaration(ctx: VerilogParser.Udp_input_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_reg_declaration}.
     * @param ctx the parse tree
     */
    fun enterUdp_reg_declaration(ctx: VerilogParser.Udp_reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_reg_declaration}.
     * @param ctx the parse tree
     */
    fun exitUdp_reg_declaration(ctx: VerilogParser.Udp_reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_body}.
     * @param ctx the parse tree
     */
    fun enterUdp_body(ctx: VerilogParser.Udp_bodyContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_body}.
     * @param ctx the parse tree
     */
    fun exitUdp_body(ctx: VerilogParser.Udp_bodyContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#combinational_body}.
     * @param ctx the parse tree
     */
    fun enterCombinational_body(ctx: VerilogParser.Combinational_bodyContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#combinational_body}.
     * @param ctx the parse tree
     */
    fun exitCombinational_body(ctx: VerilogParser.Combinational_bodyContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#combinational_entry}.
     * @param ctx the parse tree
     */
    fun enterCombinational_entry(ctx: VerilogParser.Combinational_entryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#combinational_entry}.
     * @param ctx the parse tree
     */
    fun exitCombinational_entry(ctx: VerilogParser.Combinational_entryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sequential_body}.
     * @param ctx the parse tree
     */
    fun enterSequential_body(ctx: VerilogParser.Sequential_bodyContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sequential_body}.
     * @param ctx the parse tree
     */
    fun exitSequential_body(ctx: VerilogParser.Sequential_bodyContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_initial_statement}.
     * @param ctx the parse tree
     */
    fun enterUdp_initial_statement(ctx: VerilogParser.Udp_initial_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_initial_statement}.
     * @param ctx the parse tree
     */
    fun exitUdp_initial_statement(ctx: VerilogParser.Udp_initial_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#init_val}.
     * @param ctx the parse tree
     */
    fun enterInit_val(ctx: VerilogParser.Init_valContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#init_val}.
     * @param ctx the parse tree
     */
    fun exitInit_val(ctx: VerilogParser.Init_valContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sequential_entry}.
     * @param ctx the parse tree
     */
    fun enterSequential_entry(ctx: VerilogParser.Sequential_entryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sequential_entry}.
     * @param ctx the parse tree
     */
    fun exitSequential_entry(ctx: VerilogParser.Sequential_entryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#seq_input_list}.
     * @param ctx the parse tree
     */
    fun enterSeq_input_list(ctx: VerilogParser.Seq_input_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#seq_input_list}.
     * @param ctx the parse tree
     */
    fun exitSeq_input_list(ctx: VerilogParser.Seq_input_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#level_input_list}.
     * @param ctx the parse tree
     */
    fun enterLevel_input_list(ctx: VerilogParser.Level_input_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#level_input_list}.
     * @param ctx the parse tree
     */
    fun exitLevel_input_list(ctx: VerilogParser.Level_input_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_input_list}.
     * @param ctx the parse tree
     */
    fun enterEdge_input_list(ctx: VerilogParser.Edge_input_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_input_list}.
     * @param ctx the parse tree
     */
    fun exitEdge_input_list(ctx: VerilogParser.Edge_input_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_indicator}.
     * @param ctx the parse tree
     */
    fun enterEdge_indicator(ctx: VerilogParser.Edge_indicatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_indicator}.
     * @param ctx the parse tree
     */
    fun exitEdge_indicator(ctx: VerilogParser.Edge_indicatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#current_state}.
     * @param ctx the parse tree
     */
    fun enterCurrent_state(ctx: VerilogParser.Current_stateContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#current_state}.
     * @param ctx the parse tree
     */
    fun exitCurrent_state(ctx: VerilogParser.Current_stateContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#next_state}.
     * @param ctx the parse tree
     */
    fun enterNext_state(ctx: VerilogParser.Next_stateContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#next_state}.
     * @param ctx the parse tree
     */
    fun exitNext_state(ctx: VerilogParser.Next_stateContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_symbol}.
     * @param ctx the parse tree
     */
    fun enterOutput_symbol(ctx: VerilogParser.Output_symbolContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_symbol}.
     * @param ctx the parse tree
     */
    fun exitOutput_symbol(ctx: VerilogParser.Output_symbolContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#level_symbol}.
     * @param ctx the parse tree
     */
    fun enterLevel_symbol(ctx: VerilogParser.Level_symbolContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#level_symbol}.
     * @param ctx the parse tree
     */
    fun exitLevel_symbol(ctx: VerilogParser.Level_symbolContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_symbol}.
     * @param ctx the parse tree
     */
    fun enterEdge_symbol(ctx: VerilogParser.Edge_symbolContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_symbol}.
     * @param ctx the parse tree
     */
    fun exitEdge_symbol(ctx: VerilogParser.Edge_symbolContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_instantiation}.
     * @param ctx the parse tree
     */
    fun enterUdp_instantiation(ctx: VerilogParser.Udp_instantiationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_instantiation}.
     * @param ctx the parse tree
     */
    fun exitUdp_instantiation(ctx: VerilogParser.Udp_instantiationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_instance}.
     * @param ctx the parse tree
     */
    fun enterUdp_instance(ctx: VerilogParser.Udp_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_instance}.
     * @param ctx the parse tree
     */
    fun exitUdp_instance(ctx: VerilogParser.Udp_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#name_of_udp_instance}.
     * @param ctx the parse tree
     */
    fun enterName_of_udp_instance(ctx: VerilogParser.Name_of_udp_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#name_of_udp_instance}.
     * @param ctx the parse tree
     */
    fun exitName_of_udp_instance(ctx: VerilogParser.Name_of_udp_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#continuous_assign}.
     * @param ctx the parse tree
     */
    fun enterContinuous_assign(ctx: VerilogParser.Continuous_assignContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#continuous_assign}.
     * @param ctx the parse tree
     */
    fun exitContinuous_assign(ctx: VerilogParser.Continuous_assignContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_net_assignments(ctx: VerilogParser.List_of_net_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_net_assignments(ctx: VerilogParser.List_of_net_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_assignment}.
     * @param ctx the parse tree
     */
    fun enterNet_assignment(ctx: VerilogParser.Net_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_assignment}.
     * @param ctx the parse tree
     */
    fun exitNet_assignment(ctx: VerilogParser.Net_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#initial_construct}.
     * @param ctx the parse tree
     */
    fun enterInitial_construct(ctx: VerilogParser.Initial_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#initial_construct}.
     * @param ctx the parse tree
     */
    fun exitInitial_construct(ctx: VerilogParser.Initial_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#always_construct}.
     * @param ctx the parse tree
     */
    fun enterAlways_construct(ctx: VerilogParser.Always_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#always_construct}.
     * @param ctx the parse tree
     */
    fun exitAlways_construct(ctx: VerilogParser.Always_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#blocking_assignment}.
     * @param ctx the parse tree
     */
    fun enterBlocking_assignment(ctx: VerilogParser.Blocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#blocking_assignment}.
     * @param ctx the parse tree
     */
    fun exitBlocking_assignment(ctx: VerilogParser.Blocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    fun enterNonblocking_assignment(ctx: VerilogParser.Nonblocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    fun exitNonblocking_assignment(ctx: VerilogParser.Nonblocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    fun enterProcedural_continuous_assignments(ctx: VerilogParser.Procedural_continuous_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    fun exitProcedural_continuous_assignments(ctx: VerilogParser.Procedural_continuous_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_assignment}.
     * @param ctx the parse tree
     */
    fun enterVariable_assignment(ctx: VerilogParser.Variable_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_assignment}.
     * @param ctx the parse tree
     */
    fun exitVariable_assignment(ctx: VerilogParser.Variable_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#par_block}.
     * @param ctx the parse tree
     */
    fun enterPar_block(ctx: VerilogParser.Par_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#par_block}.
     * @param ctx the parse tree
     */
    fun exitPar_block(ctx: VerilogParser.Par_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_name}.
     * @param ctx the parse tree
     */
    fun enterBlock_name(ctx: VerilogParser.Block_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_name}.
     * @param ctx the parse tree
     */
    fun exitBlock_name(ctx: VerilogParser.Block_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#seq_block}.
     * @param ctx the parse tree
     */
    fun enterSeq_block(ctx: VerilogParser.Seq_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#seq_block}.
     * @param ctx the parse tree
     */
    fun exitSeq_block(ctx: VerilogParser.Seq_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#statement}.
     * @param ctx the parse tree
     */
    fun enterStatement(ctx: VerilogParser.StatementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#statement}.
     * @param ctx the parse tree
     */
    fun exitStatement(ctx: VerilogParser.StatementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#statement_or_null}.
     * @param ctx the parse tree
     */
    fun enterStatement_or_null(ctx: VerilogParser.Statement_or_nullContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#statement_or_null}.
     * @param ctx the parse tree
     */
    fun exitStatement_or_null(ctx: VerilogParser.Statement_or_nullContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_statement}.
     * @param ctx the parse tree
     */
    fun enterFunction_statement(ctx: VerilogParser.Function_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_statement}.
     * @param ctx the parse tree
     */
    fun exitFunction_statement(ctx: VerilogParser.Function_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay_control}.
     * @param ctx the parse tree
     */
    fun enterDelay_control(ctx: VerilogParser.Delay_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay_control}.
     * @param ctx the parse tree
     */
    fun exitDelay_control(ctx: VerilogParser.Delay_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    fun enterDelay_or_event_control(ctx: VerilogParser.Delay_or_event_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    fun exitDelay_or_event_control(ctx: VerilogParser.Delay_or_event_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#disable_statement}.
     * @param ctx the parse tree
     */
    fun enterDisable_statement(ctx: VerilogParser.Disable_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#disable_statement}.
     * @param ctx the parse tree
     */
    fun exitDisable_statement(ctx: VerilogParser.Disable_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_control}.
     * @param ctx the parse tree
     */
    fun enterEvent_control(ctx: VerilogParser.Event_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_control}.
     * @param ctx the parse tree
     */
    fun exitEvent_control(ctx: VerilogParser.Event_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_trigger}.
     * @param ctx the parse tree
     */
    fun enterEvent_trigger(ctx: VerilogParser.Event_triggerContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_trigger}.
     * @param ctx the parse tree
     */
    fun exitEvent_trigger(ctx: VerilogParser.Event_triggerContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_expression}.
     * @param ctx the parse tree
     */
    fun enterEvent_expression(ctx: VerilogParser.Event_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_expression}.
     * @param ctx the parse tree
     */
    fun exitEvent_expression(ctx: VerilogParser.Event_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#procedural_timing_control}.
     * @param ctx the parse tree
     */
    fun enterProcedural_timing_control(ctx: VerilogParser.Procedural_timing_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#procedural_timing_control}.
     * @param ctx the parse tree
     */
    fun exitProcedural_timing_control(ctx: VerilogParser.Procedural_timing_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    fun enterProcedural_timing_control_statement(ctx: VerilogParser.Procedural_timing_control_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    fun exitProcedural_timing_control_statement(ctx: VerilogParser.Procedural_timing_control_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#wait_statement}.
     * @param ctx the parse tree
     */
    fun enterWait_statement(ctx: VerilogParser.Wait_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#wait_statement}.
     * @param ctx the parse tree
     */
    fun exitWait_statement(ctx: VerilogParser.Wait_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#conditional_statement}.
     * @param ctx the parse tree
     */
    fun enterConditional_statement(ctx: VerilogParser.Conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#conditional_statement}.
     * @param ctx the parse tree
     */
    fun exitConditional_statement(ctx: VerilogParser.Conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_statement}.
     * @param ctx the parse tree
     */
    fun enterCase_statement(ctx: VerilogParser.Case_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_statement}.
     * @param ctx the parse tree
     */
    fun exitCase_statement(ctx: VerilogParser.Case_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_item}.
     * @param ctx the parse tree
     */
    fun enterCase_item(ctx: VerilogParser.Case_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_item}.
     * @param ctx the parse tree
     */
    fun exitCase_item(ctx: VerilogParser.Case_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#loop_statement}.
     * @param ctx the parse tree
     */
    fun enterLoop_statement(ctx: VerilogParser.Loop_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#loop_statement}.
     * @param ctx the parse tree
     */
    fun exitLoop_statement(ctx: VerilogParser.Loop_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_task_enable}.
     * @param ctx the parse tree
     */
    fun enterSystem_task_enable(ctx: VerilogParser.System_task_enableContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_task_enable}.
     * @param ctx the parse tree
     */
    fun exitSystem_task_enable(ctx: VerilogParser.System_task_enableContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sys_task_en_port_list}.
     * @param ctx the parse tree
     */
    fun enterSys_task_en_port_list(ctx: VerilogParser.Sys_task_en_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sys_task_en_port_list}.
     * @param ctx the parse tree
     */
    fun exitSys_task_en_port_list(ctx: VerilogParser.Sys_task_en_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sys_task_en_port_item}.
     * @param ctx the parse tree
     */
    fun enterSys_task_en_port_item(ctx: VerilogParser.Sys_task_en_port_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sys_task_en_port_item}.
     * @param ctx the parse tree
     */
    fun exitSys_task_en_port_item(ctx: VerilogParser.Sys_task_en_port_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_enable}.
     * @param ctx the parse tree
     */
    fun enterTask_enable(ctx: VerilogParser.Task_enableContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_enable}.
     * @param ctx the parse tree
     */
    fun exitTask_enable(ctx: VerilogParser.Task_enableContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_en_port_list}.
     * @param ctx the parse tree
     */
    fun enterTask_en_port_list(ctx: VerilogParser.Task_en_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_en_port_list}.
     * @param ctx the parse tree
     */
    fun exitTask_en_port_list(ctx: VerilogParser.Task_en_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_block}.
     * @param ctx the parse tree
     */
    fun enterSpecify_block(ctx: VerilogParser.Specify_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_block}.
     * @param ctx the parse tree
     */
    fun exitSpecify_block(ctx: VerilogParser.Specify_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_item}.
     * @param ctx the parse tree
     */
    fun enterSpecify_item(ctx: VerilogParser.Specify_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_item}.
     * @param ctx the parse tree
     */
    fun exitSpecify_item(ctx: VerilogParser.Specify_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    fun enterPulsestyle_declaration(ctx: VerilogParser.Pulsestyle_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    fun exitPulsestyle_declaration(ctx: VerilogParser.Pulsestyle_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    fun enterShowcancelled_declaration(ctx: VerilogParser.Showcancelled_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    fun exitShowcancelled_declaration(ctx: VerilogParser.Showcancelled_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#path_declaration}.
     * @param ctx the parse tree
     */
    fun enterPath_declaration(ctx: VerilogParser.Path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#path_declaration}.
     * @param ctx the parse tree
     */
    fun exitPath_declaration(ctx: VerilogParser.Path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    fun enterSimple_path_declaration(ctx: VerilogParser.Simple_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    fun exitSimple_path_declaration(ctx: VerilogParser.Simple_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parallel_path_description}.
     * @param ctx the parse tree
     */
    fun enterParallel_path_description(ctx: VerilogParser.Parallel_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parallel_path_description}.
     * @param ctx the parse tree
     */
    fun exitParallel_path_description(ctx: VerilogParser.Parallel_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#full_path_description}.
     * @param ctx the parse tree
     */
    fun enterFull_path_description(ctx: VerilogParser.Full_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#full_path_description}.
     * @param ctx the parse tree
     */
    fun exitFull_path_description(ctx: VerilogParser.Full_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    fun enterList_of_path_inputs(ctx: VerilogParser.List_of_path_inputsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    fun exitList_of_path_inputs(ctx: VerilogParser.List_of_path_inputsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    fun enterList_of_path_outputs(ctx: VerilogParser.List_of_path_outputsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    fun exitList_of_path_outputs(ctx: VerilogParser.List_of_path_outputsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun enterSpecify_input_terminal_descriptor(ctx: VerilogParser.Specify_input_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun exitSpecify_input_terminal_descriptor(ctx: VerilogParser.Specify_input_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun enterSpecify_output_terminal_descriptor(ctx: VerilogParser.Specify_output_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun exitSpecify_output_terminal_descriptor(ctx: VerilogParser.Specify_output_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_identifier}.
     * @param ctx the parse tree
     */
    fun enterInput_identifier(ctx: VerilogParser.Input_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_identifier}.
     * @param ctx the parse tree
     */
    fun exitInput_identifier(ctx: VerilogParser.Input_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_identifier}.
     * @param ctx the parse tree
     */
    fun enterOutput_identifier(ctx: VerilogParser.Output_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_identifier}.
     * @param ctx the parse tree
     */
    fun exitOutput_identifier(ctx: VerilogParser.Output_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#path_delay_value}.
     * @param ctx the parse tree
     */
    fun enterPath_delay_value(ctx: VerilogParser.Path_delay_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#path_delay_value}.
     * @param ctx the parse tree
     */
    fun exitPath_delay_value(ctx: VerilogParser.Path_delay_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    fun enterList_of_path_delay_expressions(ctx: VerilogParser.List_of_path_delay_expressionsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    fun exitList_of_path_delay_expressions(ctx: VerilogParser.List_of_path_delay_expressionsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT_path_delay_expression(ctx: VerilogParser.T_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT_path_delay_expression(ctx: VerilogParser.T_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTrise_path_delay_expression(ctx: VerilogParser.Trise_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTrise_path_delay_expression(ctx: VerilogParser.Trise_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTfall_path_delay_expression(ctx: VerilogParser.Tfall_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTfall_path_delay_expression(ctx: VerilogParser.Tfall_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTz_path_delay_expression(ctx: VerilogParser.Tz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTz_path_delay_expression(ctx: VerilogParser.Tz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT01_path_delay_expression(ctx: VerilogParser.T01_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT01_path_delay_expression(ctx: VerilogParser.T01_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT10_path_delay_expression(ctx: VerilogParser.T10_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT10_path_delay_expression(ctx: VerilogParser.T10_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT0z_path_delay_expression(ctx: VerilogParser.T0z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT0z_path_delay_expression(ctx: VerilogParser.T0z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTz1_path_delay_expression(ctx: VerilogParser.Tz1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTz1_path_delay_expression(ctx: VerilogParser.Tz1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT1z_path_delay_expression(ctx: VerilogParser.T1z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT1z_path_delay_expression(ctx: VerilogParser.T1z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTz0_path_delay_expression(ctx: VerilogParser.Tz0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTz0_path_delay_expression(ctx: VerilogParser.Tz0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT0x_path_delay_expression(ctx: VerilogParser.T0x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT0x_path_delay_expression(ctx: VerilogParser.T0x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTx1_path_delay_expression(ctx: VerilogParser.Tx1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTx1_path_delay_expression(ctx: VerilogParser.Tx1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT1x_path_delay_expression(ctx: VerilogParser.T1x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT1x_path_delay_expression(ctx: VerilogParser.T1x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTx0_path_delay_expression(ctx: VerilogParser.Tx0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTx0_path_delay_expression(ctx: VerilogParser.Tx0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTxz_path_delay_expression(ctx: VerilogParser.Txz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTxz_path_delay_expression(ctx: VerilogParser.Txz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTzx_path_delay_expression(ctx: VerilogParser.Tzx_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTzx_path_delay_expression(ctx: VerilogParser.Tzx_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterPath_delay_expression(ctx: VerilogParser.Path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitPath_delay_expression(ctx: VerilogParser.Path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    fun enterEdge_sensitive_path_declaration(ctx: VerilogParser.Edge_sensitive_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    fun exitEdge_sensitive_path_declaration(ctx: VerilogParser.Edge_sensitive_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun enterParallel_edge_sensitive_path_description(ctx: VerilogParser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun exitParallel_edge_sensitive_path_description(ctx: VerilogParser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun enterFull_edge_sensitive_path_description(ctx: VerilogParser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun exitFull_edge_sensitive_path_description(ctx: VerilogParser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#data_source_expression}.
     * @param ctx the parse tree
     */
    fun enterData_source_expression(ctx: VerilogParser.Data_source_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#data_source_expression}.
     * @param ctx the parse tree
     */
    fun exitData_source_expression(ctx: VerilogParser.Data_source_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_identifier}.
     * @param ctx the parse tree
     */
    fun enterEdge_identifier(ctx: VerilogParser.Edge_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_identifier}.
     * @param ctx the parse tree
     */
    fun exitEdge_identifier(ctx: VerilogParser.Edge_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    fun enterState_dependent_path_declaration(ctx: VerilogParser.State_dependent_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    fun exitState_dependent_path_declaration(ctx: VerilogParser.State_dependent_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#polarity_operator}.
     * @param ctx the parse tree
     */
    fun enterPolarity_operator(ctx: VerilogParser.Polarity_operatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#polarity_operator}.
     * @param ctx the parse tree
     */
    fun exitPolarity_operator(ctx: VerilogParser.Polarity_operatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_timing_check}.
     * @param ctx the parse tree
     */
    fun enterSystem_timing_check(ctx: VerilogParser.System_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_timing_check}.
     * @param ctx the parse tree
     */
    fun exitSystem_timing_check(ctx: VerilogParser.System_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#setup_timing_check}.
     * @param ctx the parse tree
     */
    fun enterSetup_timing_check(ctx: VerilogParser.Setup_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#setup_timing_check}.
     * @param ctx the parse tree
     */
    fun exitSetup_timing_check(ctx: VerilogParser.Setup_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#notifier_opt}.
     * @param ctx the parse tree
     */
    fun enterNotifier_opt(ctx: VerilogParser.Notifier_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#notifier_opt}.
     * @param ctx the parse tree
     */
    fun exitNotifier_opt(ctx: VerilogParser.Notifier_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hold_timing_check}.
     * @param ctx the parse tree
     */
    fun enterHold_timing_check(ctx: VerilogParser.Hold_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hold_timing_check}.
     * @param ctx the parse tree
     */
    fun exitHold_timing_check(ctx: VerilogParser.Hold_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#setuphold_timing_check}.
     * @param ctx the parse tree
     */
    fun enterSetuphold_timing_check(ctx: VerilogParser.Setuphold_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#setuphold_timing_check}.
     * @param ctx the parse tree
     */
    fun exitSetuphold_timing_check(ctx: VerilogParser.Setuphold_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_opt}.
     * @param ctx the parse tree
     */
    fun enterTiming_check_opt(ctx: VerilogParser.Timing_check_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_opt}.
     * @param ctx the parse tree
     */
    fun exitTiming_check_opt(ctx: VerilogParser.Timing_check_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#stamptime_cond_opt}.
     * @param ctx the parse tree
     */
    fun enterStamptime_cond_opt(ctx: VerilogParser.Stamptime_cond_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#stamptime_cond_opt}.
     * @param ctx the parse tree
     */
    fun exitStamptime_cond_opt(ctx: VerilogParser.Stamptime_cond_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#checktime_cond_opt}.
     * @param ctx the parse tree
     */
    fun enterChecktime_cond_opt(ctx: VerilogParser.Checktime_cond_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#checktime_cond_opt}.
     * @param ctx the parse tree
     */
    fun exitChecktime_cond_opt(ctx: VerilogParser.Checktime_cond_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_ref_opt}.
     * @param ctx the parse tree
     */
    fun enterDelayed_ref_opt(ctx: VerilogParser.Delayed_ref_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_ref_opt}.
     * @param ctx the parse tree
     */
    fun exitDelayed_ref_opt(ctx: VerilogParser.Delayed_ref_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_data_opt}.
     * @param ctx the parse tree
     */
    fun enterDelayed_data_opt(ctx: VerilogParser.Delayed_data_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_data_opt}.
     * @param ctx the parse tree
     */
    fun exitDelayed_data_opt(ctx: VerilogParser.Delayed_data_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#recovery_timing_check}.
     * @param ctx the parse tree
     */
    fun enterRecovery_timing_check(ctx: VerilogParser.Recovery_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#recovery_timing_check}.
     * @param ctx the parse tree
     */
    fun exitRecovery_timing_check(ctx: VerilogParser.Recovery_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#removal_timing_check}.
     * @param ctx the parse tree
     */
    fun enterRemoval_timing_check(ctx: VerilogParser.Removal_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#removal_timing_check}.
     * @param ctx the parse tree
     */
    fun exitRemoval_timing_check(ctx: VerilogParser.Removal_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#recrem_timing_check}.
     * @param ctx the parse tree
     */
    fun enterRecrem_timing_check(ctx: VerilogParser.Recrem_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#recrem_timing_check}.
     * @param ctx the parse tree
     */
    fun exitRecrem_timing_check(ctx: VerilogParser.Recrem_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#skew_timing_check}.
     * @param ctx the parse tree
     */
    fun enterSkew_timing_check(ctx: VerilogParser.Skew_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#skew_timing_check}.
     * @param ctx the parse tree
     */
    fun exitSkew_timing_check(ctx: VerilogParser.Skew_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timeskew_timing_check}.
     * @param ctx the parse tree
     */
    fun enterTimeskew_timing_check(ctx: VerilogParser.Timeskew_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timeskew_timing_check}.
     * @param ctx the parse tree
     */
    fun exitTimeskew_timing_check(ctx: VerilogParser.Timeskew_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#skew_timing_check_opt}.
     * @param ctx the parse tree
     */
    fun enterSkew_timing_check_opt(ctx: VerilogParser.Skew_timing_check_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#skew_timing_check_opt}.
     * @param ctx the parse tree
     */
    fun exitSkew_timing_check_opt(ctx: VerilogParser.Skew_timing_check_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_based_flag_opt}.
     * @param ctx the parse tree
     */
    fun enterEvent_based_flag_opt(ctx: VerilogParser.Event_based_flag_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_based_flag_opt}.
     * @param ctx the parse tree
     */
    fun exitEvent_based_flag_opt(ctx: VerilogParser.Event_based_flag_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#remain_active_flag_opt}.
     * @param ctx the parse tree
     */
    fun enterRemain_active_flag_opt(ctx: VerilogParser.Remain_active_flag_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#remain_active_flag_opt}.
     * @param ctx the parse tree
     */
    fun exitRemain_active_flag_opt(ctx: VerilogParser.Remain_active_flag_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#fullskew_timing_check}.
     * @param ctx the parse tree
     */
    fun enterFullskew_timing_check(ctx: VerilogParser.Fullskew_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#fullskew_timing_check}.
     * @param ctx the parse tree
     */
    fun exitFullskew_timing_check(ctx: VerilogParser.Fullskew_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#period_timing_check}.
     * @param ctx the parse tree
     */
    fun enterPeriod_timing_check(ctx: VerilogParser.Period_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#period_timing_check}.
     * @param ctx the parse tree
     */
    fun exitPeriod_timing_check(ctx: VerilogParser.Period_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#width_timing_check}.
     * @param ctx the parse tree
     */
    fun enterWidth_timing_check(ctx: VerilogParser.Width_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#width_timing_check}.
     * @param ctx the parse tree
     */
    fun exitWidth_timing_check(ctx: VerilogParser.Width_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#threshold_opt}.
     * @param ctx the parse tree
     */
    fun enterThreshold_opt(ctx: VerilogParser.Threshold_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#threshold_opt}.
     * @param ctx the parse tree
     */
    fun exitThreshold_opt(ctx: VerilogParser.Threshold_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#nochange_timing_check}.
     * @param ctx the parse tree
     */
    fun enterNochange_timing_check(ctx: VerilogParser.Nochange_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#nochange_timing_check}.
     * @param ctx the parse tree
     */
    fun exitNochange_timing_check(ctx: VerilogParser.Nochange_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#checktime_condition}.
     * @param ctx the parse tree
     */
    fun enterChecktime_condition(ctx: VerilogParser.Checktime_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#checktime_condition}.
     * @param ctx the parse tree
     */
    fun exitChecktime_condition(ctx: VerilogParser.Checktime_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#controlled_reference_event}.
     * @param ctx the parse tree
     */
    fun enterControlled_reference_event(ctx: VerilogParser.Controlled_reference_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#controlled_reference_event}.
     * @param ctx the parse tree
     */
    fun exitControlled_reference_event(ctx: VerilogParser.Controlled_reference_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#data_event}.
     * @param ctx the parse tree
     */
    fun enterData_event(ctx: VerilogParser.Data_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#data_event}.
     * @param ctx the parse tree
     */
    fun exitData_event(ctx: VerilogParser.Data_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_data}.
     * @param ctx the parse tree
     */
    fun enterDelayed_data(ctx: VerilogParser.Delayed_dataContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_data}.
     * @param ctx the parse tree
     */
    fun exitDelayed_data(ctx: VerilogParser.Delayed_dataContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_reference}.
     * @param ctx the parse tree
     */
    fun enterDelayed_reference(ctx: VerilogParser.Delayed_referenceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_reference}.
     * @param ctx the parse tree
     */
    fun exitDelayed_reference(ctx: VerilogParser.Delayed_referenceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#end_edge_offset}.
     * @param ctx the parse tree
     */
    fun enterEnd_edge_offset(ctx: VerilogParser.End_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#end_edge_offset}.
     * @param ctx the parse tree
     */
    fun exitEnd_edge_offset(ctx: VerilogParser.End_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_based_flag}.
     * @param ctx the parse tree
     */
    fun enterEvent_based_flag(ctx: VerilogParser.Event_based_flagContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_based_flag}.
     * @param ctx the parse tree
     */
    fun exitEvent_based_flag(ctx: VerilogParser.Event_based_flagContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#notifier}.
     * @param ctx the parse tree
     */
    fun enterNotifier(ctx: VerilogParser.NotifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#notifier}.
     * @param ctx the parse tree
     */
    fun exitNotifier(ctx: VerilogParser.NotifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#reference_event}.
     * @param ctx the parse tree
     */
    fun enterReference_event(ctx: VerilogParser.Reference_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#reference_event}.
     * @param ctx the parse tree
     */
    fun exitReference_event(ctx: VerilogParser.Reference_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#remain_active_flag}.
     * @param ctx the parse tree
     */
    fun enterRemain_active_flag(ctx: VerilogParser.Remain_active_flagContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#remain_active_flag}.
     * @param ctx the parse tree
     */
    fun exitRemain_active_flag(ctx: VerilogParser.Remain_active_flagContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#stamptime_condition}.
     * @param ctx the parse tree
     */
    fun enterStamptime_condition(ctx: VerilogParser.Stamptime_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#stamptime_condition}.
     * @param ctx the parse tree
     */
    fun exitStamptime_condition(ctx: VerilogParser.Stamptime_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#start_edge_offset}.
     * @param ctx the parse tree
     */
    fun enterStart_edge_offset(ctx: VerilogParser.Start_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#start_edge_offset}.
     * @param ctx the parse tree
     */
    fun exitStart_edge_offset(ctx: VerilogParser.Start_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#threshold}.
     * @param ctx the parse tree
     */
    fun enterThreshold(ctx: VerilogParser.ThresholdContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#threshold}.
     * @param ctx the parse tree
     */
    fun exitThreshold(ctx: VerilogParser.ThresholdContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_limit}.
     * @param ctx the parse tree
     */
    fun enterTiming_check_limit(ctx: VerilogParser.Timing_check_limitContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_limit}.
     * @param ctx the parse tree
     */
    fun exitTiming_check_limit(ctx: VerilogParser.Timing_check_limitContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_event}.
     * @param ctx the parse tree
     */
    fun enterTiming_check_event(ctx: VerilogParser.Timing_check_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_event}.
     * @param ctx the parse tree
     */
    fun exitTiming_check_event(ctx: VerilogParser.Timing_check_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#controlled_timing_check_event}.
     * @param ctx the parse tree
     */
    fun enterControlled_timing_check_event(ctx: VerilogParser.Controlled_timing_check_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#controlled_timing_check_event}.
     * @param ctx the parse tree
     */
    fun exitControlled_timing_check_event(ctx: VerilogParser.Controlled_timing_check_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_event_control}.
     * @param ctx the parse tree
     */
    fun enterTiming_check_event_control(ctx: VerilogParser.Timing_check_event_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_event_control}.
     * @param ctx the parse tree
     */
    fun exitTiming_check_event_control(ctx: VerilogParser.Timing_check_event_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun enterSpecify_terminal_descriptor(ctx: VerilogParser.Specify_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun exitSpecify_terminal_descriptor(ctx: VerilogParser.Specify_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_control_specifier}.
     * @param ctx the parse tree
     */
    fun enterEdge_control_specifier(ctx: VerilogParser.Edge_control_specifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_control_specifier}.
     * @param ctx the parse tree
     */
    fun exitEdge_control_specifier(ctx: VerilogParser.Edge_control_specifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_descriptor}.
     * @param ctx the parse tree
     */
    fun enterEdge_descriptor(ctx: VerilogParser.Edge_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_descriptor}.
     * @param ctx the parse tree
     */
    fun exitEdge_descriptor(ctx: VerilogParser.Edge_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_condition}.
     * @param ctx the parse tree
     */
    fun enterTiming_check_condition(ctx: VerilogParser.Timing_check_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_condition}.
     * @param ctx the parse tree
     */
    fun exitTiming_check_condition(ctx: VerilogParser.Timing_check_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#scalar_timing_check_condition}.
     * @param ctx the parse tree
     */
    fun enterScalar_timing_check_condition(ctx: VerilogParser.Scalar_timing_check_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#scalar_timing_check_condition}.
     * @param ctx the parse tree
     */
    fun exitScalar_timing_check_condition(ctx: VerilogParser.Scalar_timing_check_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#scalar_constant}.
     * @param ctx the parse tree
     */
    fun enterScalar_constant(ctx: VerilogParser.Scalar_constantContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#scalar_constant}.
     * @param ctx the parse tree
     */
    fun exitScalar_constant(ctx: VerilogParser.Scalar_constantContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#concatenation}.
     * @param ctx the parse tree
     */
    fun enterConcatenation(ctx: VerilogParser.ConcatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#concatenation}.
     * @param ctx the parse tree
     */
    fun exitConcatenation(ctx: VerilogParser.ConcatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_concatenation}.
     * @param ctx the parse tree
     */
    fun enterConstant_concatenation(ctx: VerilogParser.Constant_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_concatenation}.
     * @param ctx the parse tree
     */
    fun exitConstant_concatenation(ctx: VerilogParser.Constant_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun enterConstant_multiple_concatenation(ctx: VerilogParser.Constant_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun exitConstant_multiple_concatenation(ctx: VerilogParser.Constant_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    fun enterModule_path_concatenation(ctx: VerilogParser.Module_path_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    fun exitModule_path_concatenation(ctx: VerilogParser.Module_path_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun enterModule_path_multiple_concatenation(ctx: VerilogParser.Module_path_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun exitModule_path_multiple_concatenation(ctx: VerilogParser.Module_path_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun enterMultiple_concatenation(ctx: VerilogParser.Multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun exitMultiple_concatenation(ctx: VerilogParser.Multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_function_call}.
     * @param ctx the parse tree
     */
    fun enterConstant_function_call(ctx: VerilogParser.Constant_function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_function_call}.
     * @param ctx the parse tree
     */
    fun exitConstant_function_call(ctx: VerilogParser.Constant_function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_system_function_call}.
     * @param ctx the parse tree
     */
    fun enterConstant_system_function_call(ctx: VerilogParser.Constant_system_function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_system_function_call}.
     * @param ctx the parse tree
     */
    fun exitConstant_system_function_call(ctx: VerilogParser.Constant_system_function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_call}.
     * @param ctx the parse tree
     */
    fun enterFunction_call(ctx: VerilogParser.Function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_call}.
     * @param ctx the parse tree
     */
    fun exitFunction_call(ctx: VerilogParser.Function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_function_call}.
     * @param ctx the parse tree
     */
    fun enterSystem_function_call(ctx: VerilogParser.System_function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_function_call}.
     * @param ctx the parse tree
     */
    fun exitSystem_function_call(ctx: VerilogParser.System_function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sys_func_call_port_list}.
     * @param ctx the parse tree
     */
    fun enterSys_func_call_port_list(ctx: VerilogParser.Sys_func_call_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sys_func_call_port_list}.
     * @param ctx the parse tree
     */
    fun exitSys_func_call_port_list(ctx: VerilogParser.Sys_func_call_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#base_expression}.
     * @param ctx the parse tree
     */
    fun enterBase_expression(ctx: VerilogParser.Base_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#base_expression}.
     * @param ctx the parse tree
     */
    fun exitBase_expression(ctx: VerilogParser.Base_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_base_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_base_expression(ctx: VerilogParser.Constant_base_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_base_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_base_expression(ctx: VerilogParser.Constant_base_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_expression(ctx: VerilogParser.Constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_expression(ctx: VerilogParser.Constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_mintypmax_expression(ctx: VerilogParser.Constant_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_mintypmax_expression(ctx: VerilogParser.Constant_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_range_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_range_expression(ctx: VerilogParser.Constant_range_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_range_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_range_expression(ctx: VerilogParser.Constant_range_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterDimension_constant_expression(ctx: VerilogParser.Dimension_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitDimension_constant_expression(ctx: VerilogParser.Dimension_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#expression}.
     * @param ctx the parse tree
     */
    fun enterExpression(ctx: VerilogParser.ExpressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#expression}.
     * @param ctx the parse tree
     */
    fun exitExpression(ctx: VerilogParser.ExpressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterLsb_constant_expression(ctx: VerilogParser.Lsb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitLsb_constant_expression(ctx: VerilogParser.Lsb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun enterMintypmax_expression(ctx: VerilogParser.Mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun exitMintypmax_expression(ctx: VerilogParser.Mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_expression}.
     * @param ctx the parse tree
     */
    fun enterModule_path_expression(ctx: VerilogParser.Module_path_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_expression}.
     * @param ctx the parse tree
     */
    fun exitModule_path_expression(ctx: VerilogParser.Module_path_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun enterModule_path_mintypmax_expression(ctx: VerilogParser.Module_path_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun exitModule_path_mintypmax_expression(ctx: VerilogParser.Module_path_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterMsb_constant_expression(ctx: VerilogParser.Msb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitMsb_constant_expression(ctx: VerilogParser.Msb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#range_expression}.
     * @param ctx the parse tree
     */
    fun enterRange_expression(ctx: VerilogParser.Range_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#range_expression}.
     * @param ctx the parse tree
     */
    fun exitRange_expression(ctx: VerilogParser.Range_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#width_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterWidth_constant_expression(ctx: VerilogParser.Width_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#width_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitWidth_constant_expression(ctx: VerilogParser.Width_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_primary}.
     * @param ctx the parse tree
     */
    fun enterConstant_primary(ctx: VerilogParser.Constant_primaryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_primary}.
     * @param ctx the parse tree
     */
    fun exitConstant_primary(ctx: VerilogParser.Constant_primaryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_primary}.
     * @param ctx the parse tree
     */
    fun enterModule_path_primary(ctx: VerilogParser.Module_path_primaryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_primary}.
     * @param ctx the parse tree
     */
    fun exitModule_path_primary(ctx: VerilogParser.Module_path_primaryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#primary}.
     * @param ctx the parse tree
     */
    fun enterPrimary(ctx: VerilogParser.PrimaryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#primary}.
     * @param ctx the parse tree
     */
    fun exitPrimary(ctx: VerilogParser.PrimaryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#select_}.
     * @param ctx the parse tree
     */
    fun enterSelect_(ctx: VerilogParser.Select_Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#select_}.
     * @param ctx the parse tree
     */
    fun exitSelect_(ctx: VerilogParser.Select_Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#bit_select}.
     * @param ctx the parse tree
     */
    fun enterBit_select(ctx: VerilogParser.Bit_selectContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#bit_select}.
     * @param ctx the parse tree
     */
    fun exitBit_select(ctx: VerilogParser.Bit_selectContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_lvalue}.
     * @param ctx the parse tree
     */
    fun enterNet_lvalue(ctx: VerilogParser.Net_lvalueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_lvalue}.
     * @param ctx the parse tree
     */
    fun exitNet_lvalue(ctx: VerilogParser.Net_lvalueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#const_select}.
     * @param ctx the parse tree
     */
    fun enterConst_select(ctx: VerilogParser.Const_selectContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#const_select}.
     * @param ctx the parse tree
     */
    fun exitConst_select(ctx: VerilogParser.Const_selectContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#const_bit_select}.
     * @param ctx the parse tree
     */
    fun enterConst_bit_select(ctx: VerilogParser.Const_bit_selectContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#const_bit_select}.
     * @param ctx the parse tree
     */
    fun exitConst_bit_select(ctx: VerilogParser.Const_bit_selectContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_lvalue}.
     * @param ctx the parse tree
     */
    fun enterVariable_lvalue(ctx: VerilogParser.Variable_lvalueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_lvalue}.
     * @param ctx the parse tree
     */
    fun exitVariable_lvalue(ctx: VerilogParser.Variable_lvalueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#unary_operator}.
     * @param ctx the parse tree
     */
    fun enterUnary_operator(ctx: VerilogParser.Unary_operatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#unary_operator}.
     * @param ctx the parse tree
     */
    fun exitUnary_operator(ctx: VerilogParser.Unary_operatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    fun enterUnary_module_path_operator(ctx: VerilogParser.Unary_module_path_operatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    fun exitUnary_module_path_operator(ctx: VerilogParser.Unary_module_path_operatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#number}.
     * @param ctx the parse tree
     */
    fun enterNumber(ctx: VerilogParser.NumberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#number}.
     * @param ctx the parse tree
     */
    fun exitNumber(ctx: VerilogParser.NumberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_number}.
     * @param ctx the parse tree
     */
    fun enterReal_number(ctx: VerilogParser.Real_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_number}.
     * @param ctx the parse tree
     */
    fun exitReal_number(ctx: VerilogParser.Real_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#decimal_number}.
     * @param ctx the parse tree
     */
    fun enterDecimal_number(ctx: VerilogParser.Decimal_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#decimal_number}.
     * @param ctx the parse tree
     */
    fun exitDecimal_number(ctx: VerilogParser.Decimal_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#binary_number}.
     * @param ctx the parse tree
     */
    fun enterBinary_number(ctx: VerilogParser.Binary_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#binary_number}.
     * @param ctx the parse tree
     */
    fun exitBinary_number(ctx: VerilogParser.Binary_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#octal_number}.
     * @param ctx the parse tree
     */
    fun enterOctal_number(ctx: VerilogParser.Octal_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#octal_number}.
     * @param ctx the parse tree
     */
    fun exitOctal_number(ctx: VerilogParser.Octal_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hex_number}.
     * @param ctx the parse tree
     */
    fun enterHex_number(ctx: VerilogParser.Hex_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hex_number}.
     * @param ctx the parse tree
     */
    fun exitHex_number(ctx: VerilogParser.Hex_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#size}.
     * @param ctx the parse tree
     */
    fun enterSize(ctx: VerilogParser.SizeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#size}.
     * @param ctx the parse tree
     */
    fun exitSize(ctx: VerilogParser.SizeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#fixed_point_number}.
     * @param ctx the parse tree
     */
    fun enterFixed_point_number(ctx: VerilogParser.Fixed_point_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#fixed_point_number}.
     * @param ctx the parse tree
     */
    fun exitFixed_point_number(ctx: VerilogParser.Fixed_point_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#exponential_number}.
     * @param ctx the parse tree
     */
    fun enterExponential_number(ctx: VerilogParser.Exponential_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#exponential_number}.
     * @param ctx the parse tree
     */
    fun exitExponential_number(ctx: VerilogParser.Exponential_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#unsigned_number}.
     * @param ctx the parse tree
     */
    fun enterUnsigned_number(ctx: VerilogParser.Unsigned_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#unsigned_number}.
     * @param ctx the parse tree
     */
    fun exitUnsigned_number(ctx: VerilogParser.Unsigned_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#decimal_value}.
     * @param ctx the parse tree
     */
    fun enterDecimal_value(ctx: VerilogParser.Decimal_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#decimal_value}.
     * @param ctx the parse tree
     */
    fun exitDecimal_value(ctx: VerilogParser.Decimal_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#binary_value}.
     * @param ctx the parse tree
     */
    fun enterBinary_value(ctx: VerilogParser.Binary_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#binary_value}.
     * @param ctx the parse tree
     */
    fun exitBinary_value(ctx: VerilogParser.Binary_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#octal_value}.
     * @param ctx the parse tree
     */
    fun enterOctal_value(ctx: VerilogParser.Octal_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#octal_value}.
     * @param ctx the parse tree
     */
    fun exitOctal_value(ctx: VerilogParser.Octal_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hex_value}.
     * @param ctx the parse tree
     */
    fun enterHex_value(ctx: VerilogParser.Hex_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hex_value}.
     * @param ctx the parse tree
     */
    fun exitHex_value(ctx: VerilogParser.Hex_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#decimal_base}.
     * @param ctx the parse tree
     */
    fun enterDecimal_base(ctx: VerilogParser.Decimal_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#decimal_base}.
     * @param ctx the parse tree
     */
    fun exitDecimal_base(ctx: VerilogParser.Decimal_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#binary_base}.
     * @param ctx the parse tree
     */
    fun enterBinary_base(ctx: VerilogParser.Binary_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#binary_base}.
     * @param ctx the parse tree
     */
    fun exitBinary_base(ctx: VerilogParser.Binary_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#octal_base}.
     * @param ctx the parse tree
     */
    fun enterOctal_base(ctx: VerilogParser.Octal_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#octal_base}.
     * @param ctx the parse tree
     */
    fun exitOctal_base(ctx: VerilogParser.Octal_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hex_base}.
     * @param ctx the parse tree
     */
    fun enterHex_base(ctx: VerilogParser.Hex_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hex_base}.
     * @param ctx the parse tree
     */
    fun exitHex_base(ctx: VerilogParser.Hex_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#string_}.
     * @param ctx the parse tree
     */
    fun enterString_(ctx: VerilogParser.String_Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#string_}.
     * @param ctx the parse tree
     */
    fun exitString_(ctx: VerilogParser.String_Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#attribute_instance}.
     * @param ctx the parse tree
     */
    fun enterAttribute_instance(ctx: VerilogParser.Attribute_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#attribute_instance}.
     * @param ctx the parse tree
     */
    fun exitAttribute_instance(ctx: VerilogParser.Attribute_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#attr_spec}.
     * @param ctx the parse tree
     */
    fun enterAttr_spec(ctx: VerilogParser.Attr_specContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#attr_spec}.
     * @param ctx the parse tree
     */
    fun exitAttr_spec(ctx: VerilogParser.Attr_specContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#attr_name}.
     * @param ctx the parse tree
     */
    fun enterAttr_name(ctx: VerilogParser.Attr_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#attr_name}.
     * @param ctx the parse tree
     */
    fun exitAttr_name(ctx: VerilogParser.Attr_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_identifier}.
     * @param ctx the parse tree
     */
    fun enterBlock_identifier(ctx: VerilogParser.Block_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_identifier}.
     * @param ctx the parse tree
     */
    fun exitBlock_identifier(ctx: VerilogParser.Block_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cell_identifier}.
     * @param ctx the parse tree
     */
    fun enterCell_identifier(ctx: VerilogParser.Cell_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cell_identifier}.
     * @param ctx the parse tree
     */
    fun exitCell_identifier(ctx: VerilogParser.Cell_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#config_identifier}.
     * @param ctx the parse tree
     */
    fun enterConfig_identifier(ctx: VerilogParser.Config_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#config_identifier}.
     * @param ctx the parse tree
     */
    fun exitConfig_identifier(ctx: VerilogParser.Config_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#escaped_identifier}.
     * @param ctx the parse tree
     */
    fun enterEscaped_identifier(ctx: VerilogParser.Escaped_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#escaped_identifier}.
     * @param ctx the parse tree
     */
    fun exitEscaped_identifier(ctx: VerilogParser.Escaped_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_identifier}.
     * @param ctx the parse tree
     */
    fun enterEvent_identifier(ctx: VerilogParser.Event_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_identifier}.
     * @param ctx the parse tree
     */
    fun exitEvent_identifier(ctx: VerilogParser.Event_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_identifier}.
     * @param ctx the parse tree
     */
    fun enterFunction_identifier(ctx: VerilogParser.Function_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_identifier}.
     * @param ctx the parse tree
     */
    fun exitFunction_identifier(ctx: VerilogParser.Function_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterGate_instance_identifier(ctx: VerilogParser.Gate_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitGate_instance_identifier(ctx: VerilogParser.Gate_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    fun enterGenerate_block_identifier(ctx: VerilogParser.Generate_block_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    fun exitGenerate_block_identifier(ctx: VerilogParser.Generate_block_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_identifier}.
     * @param ctx the parse tree
     */
    fun enterGenvar_identifier(ctx: VerilogParser.Genvar_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_identifier}.
     * @param ctx the parse tree
     */
    fun exitGenvar_identifier(ctx: VerilogParser.Genvar_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_identifier(ctx: VerilogParser.Hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_identifier(ctx: VerilogParser.Hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hier_ref}.
     * @param ctx the parse tree
     */
    fun enterHier_ref(ctx: VerilogParser.Hier_refContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hier_ref}.
     * @param ctx the parse tree
     */
    fun exitHier_ref(ctx: VerilogParser.Hier_refContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#identifier}.
     * @param ctx the parse tree
     */
    fun enterIdentifier(ctx: VerilogParser.IdentifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#identifier}.
     * @param ctx the parse tree
     */
    fun exitIdentifier(ctx: VerilogParser.IdentifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_port_identifier}.
     * @param ctx the parse tree
     */
    fun enterInput_port_identifier(ctx: VerilogParser.Input_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_port_identifier}.
     * @param ctx the parse tree
     */
    fun exitInput_port_identifier(ctx: VerilogParser.Input_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterInstance_identifier(ctx: VerilogParser.Instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitInstance_identifier(ctx: VerilogParser.Instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_identifier}.
     * @param ctx the parse tree
     */
    fun enterLibrary_identifier(ctx: VerilogParser.Library_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_identifier}.
     * @param ctx the parse tree
     */
    fun exitLibrary_identifier(ctx: VerilogParser.Library_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_identifier}.
     * @param ctx the parse tree
     */
    fun enterModule_identifier(ctx: VerilogParser.Module_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_identifier}.
     * @param ctx the parse tree
     */
    fun exitModule_identifier(ctx: VerilogParser.Module_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterModule_instance_identifier(ctx: VerilogParser.Module_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitModule_instance_identifier(ctx: VerilogParser.Module_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_identifier}.
     * @param ctx the parse tree
     */
    fun enterNet_identifier(ctx: VerilogParser.Net_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_identifier}.
     * @param ctx the parse tree
     */
    fun exitNet_identifier(ctx: VerilogParser.Net_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_port_identifier}.
     * @param ctx the parse tree
     */
    fun enterOutput_port_identifier(ctx: VerilogParser.Output_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_port_identifier}.
     * @param ctx the parse tree
     */
    fun exitOutput_port_identifier(ctx: VerilogParser.Output_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_identifier}.
     * @param ctx the parse tree
     */
    fun enterParameter_identifier(ctx: VerilogParser.Parameter_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_identifier}.
     * @param ctx the parse tree
     */
    fun exitParameter_identifier(ctx: VerilogParser.Parameter_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_identifier}.
     * @param ctx the parse tree
     */
    fun enterPort_identifier(ctx: VerilogParser.Port_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_identifier}.
     * @param ctx the parse tree
     */
    fun exitPort_identifier(ctx: VerilogParser.Port_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_identifier}.
     * @param ctx the parse tree
     */
    fun enterReal_identifier(ctx: VerilogParser.Real_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_identifier}.
     * @param ctx the parse tree
     */
    fun exitReal_identifier(ctx: VerilogParser.Real_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#simple_identifier}.
     * @param ctx the parse tree
     */
    fun enterSimple_identifier(ctx: VerilogParser.Simple_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#simple_identifier}.
     * @param ctx the parse tree
     */
    fun exitSimple_identifier(ctx: VerilogParser.Simple_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specparam_identifier}.
     * @param ctx the parse tree
     */
    fun enterSpecparam_identifier(ctx: VerilogParser.Specparam_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specparam_identifier}.
     * @param ctx the parse tree
     */
    fun exitSpecparam_identifier(ctx: VerilogParser.Specparam_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_function_identifier}.
     * @param ctx the parse tree
     */
    fun enterSystem_function_identifier(ctx: VerilogParser.System_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_function_identifier}.
     * @param ctx the parse tree
     */
    fun exitSystem_function_identifier(ctx: VerilogParser.System_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_task_identifier}.
     * @param ctx the parse tree
     */
    fun enterSystem_task_identifier(ctx: VerilogParser.System_task_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_task_identifier}.
     * @param ctx the parse tree
     */
    fun exitSystem_task_identifier(ctx: VerilogParser.System_task_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_identifier}.
     * @param ctx the parse tree
     */
    fun enterTask_identifier(ctx: VerilogParser.Task_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_identifier}.
     * @param ctx the parse tree
     */
    fun exitTask_identifier(ctx: VerilogParser.Task_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#terminal_identifier}.
     * @param ctx the parse tree
     */
    fun enterTerminal_identifier(ctx: VerilogParser.Terminal_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#terminal_identifier}.
     * @param ctx the parse tree
     */
    fun exitTerminal_identifier(ctx: VerilogParser.Terminal_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    fun enterTopmodule_identifier(ctx: VerilogParser.Topmodule_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    fun exitTopmodule_identifier(ctx: VerilogParser.Topmodule_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_identifier}.
     * @param ctx the parse tree
     */
    fun enterUdp_identifier(ctx: VerilogParser.Udp_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_identifier}.
     * @param ctx the parse tree
     */
    fun exitUdp_identifier(ctx: VerilogParser.Udp_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterUdp_instance_identifier(ctx: VerilogParser.Udp_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitUdp_instance_identifier(ctx: VerilogParser.Udp_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_identifier}.
     * @param ctx the parse tree
     */
    fun enterVariable_identifier(ctx: VerilogParser.Variable_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_identifier}.
     * @param ctx the parse tree
     */
    fun exitVariable_identifier(ctx: VerilogParser.Variable_identifierContext)

    override fun asSuspend(): SuspendVerilogParserListener = object : SuspendVerilogParserListener {
        override suspend fun enterLibrary_text(ctx: VerilogParser.Library_textContext) =
            this@VerilogParserListener.enterLibrary_text(ctx)

        override suspend fun exitLibrary_text(ctx: VerilogParser.Library_textContext) =
            this@VerilogParserListener.exitLibrary_text(ctx)

        override suspend fun enterLibrary_description(ctx: VerilogParser.Library_descriptionContext) =
            this@VerilogParserListener.enterLibrary_description(ctx)

        override suspend fun exitLibrary_description(ctx: VerilogParser.Library_descriptionContext) =
            this@VerilogParserListener.exitLibrary_description(ctx)

        override suspend fun enterLibrary_declaration(ctx: VerilogParser.Library_declarationContext) =
            this@VerilogParserListener.enterLibrary_declaration(ctx)

        override suspend fun exitLibrary_declaration(ctx: VerilogParser.Library_declarationContext) =
            this@VerilogParserListener.exitLibrary_declaration(ctx)

        override suspend fun enterLibrary_incdir(ctx: VerilogParser.Library_incdirContext) =
            this@VerilogParserListener.enterLibrary_incdir(ctx)

        override suspend fun exitLibrary_incdir(ctx: VerilogParser.Library_incdirContext) =
            this@VerilogParserListener.exitLibrary_incdir(ctx)

        override suspend fun enterInclude_statement(ctx: VerilogParser.Include_statementContext) =
            this@VerilogParserListener.enterInclude_statement(ctx)

        override suspend fun exitInclude_statement(ctx: VerilogParser.Include_statementContext) =
            this@VerilogParserListener.exitInclude_statement(ctx)

        override suspend fun enterFile_path_spec(ctx: VerilogParser.File_path_specContext) =
            this@VerilogParserListener.enterFile_path_spec(ctx)

        override suspend fun exitFile_path_spec(ctx: VerilogParser.File_path_specContext) =
            this@VerilogParserListener.exitFile_path_spec(ctx)

        override suspend fun enterSource_text(ctx: VerilogParser.Source_textContext) =
            this@VerilogParserListener.enterSource_text(ctx)

        override suspend fun exitSource_text(ctx: VerilogParser.Source_textContext) =
            this@VerilogParserListener.exitSource_text(ctx)

        override suspend fun enterDescription(ctx: VerilogParser.DescriptionContext) =
            this@VerilogParserListener.enterDescription(ctx)

        override suspend fun exitDescription(ctx: VerilogParser.DescriptionContext) =
            this@VerilogParserListener.exitDescription(ctx)

        override suspend fun enterModule_declaration(ctx: VerilogParser.Module_declarationContext) =
            this@VerilogParserListener.enterModule_declaration(ctx)

        override suspend fun exitModule_declaration(ctx: VerilogParser.Module_declarationContext) =
            this@VerilogParserListener.exitModule_declaration(ctx)

        override suspend fun enterModule_keyword(ctx: VerilogParser.Module_keywordContext) =
            this@VerilogParserListener.enterModule_keyword(ctx)

        override suspend fun exitModule_keyword(ctx: VerilogParser.Module_keywordContext) =
            this@VerilogParserListener.exitModule_keyword(ctx)

        override suspend fun enterModule_parameter_port_list(ctx: VerilogParser.Module_parameter_port_listContext) =
            this@VerilogParserListener.enterModule_parameter_port_list(ctx)

        override suspend fun exitModule_parameter_port_list(ctx: VerilogParser.Module_parameter_port_listContext) =
            this@VerilogParserListener.exitModule_parameter_port_list(ctx)

        override suspend fun enterList_of_port_declarations(ctx: VerilogParser.List_of_port_declarationsContext) =
            this@VerilogParserListener.enterList_of_port_declarations(ctx)

        override suspend fun exitList_of_port_declarations(ctx: VerilogParser.List_of_port_declarationsContext) =
            this@VerilogParserListener.exitList_of_port_declarations(ctx)

        override suspend fun enterPort(ctx: VerilogParser.PortContext) = this@VerilogParserListener.enterPort(ctx)
        override suspend fun exitPort(ctx: VerilogParser.PortContext) = this@VerilogParserListener.exitPort(ctx)

        override suspend fun enterPort_implicit(ctx: VerilogParser.Port_implicitContext) =
            this@VerilogParserListener.enterPort_implicit(ctx)

        override suspend fun exitPort_implicit(ctx: VerilogParser.Port_implicitContext) =
            this@VerilogParserListener.exitPort_implicit(ctx)

        override suspend fun enterPort_explicit(ctx: VerilogParser.Port_explicitContext) =
            this@VerilogParserListener.enterPort_explicit(ctx)

        override suspend fun exitPort_explicit(ctx: VerilogParser.Port_explicitContext) =
            this@VerilogParserListener.exitPort_explicit(ctx)

        override suspend fun enterPort_expression(ctx: VerilogParser.Port_expressionContext) =
            this@VerilogParserListener.enterPort_expression(ctx)

        override suspend fun exitPort_expression(ctx: VerilogParser.Port_expressionContext) =
            this@VerilogParserListener.exitPort_expression(ctx)

        override suspend fun enterPort_reference(ctx: VerilogParser.Port_referenceContext) =
            this@VerilogParserListener.enterPort_reference(ctx)

        override suspend fun exitPort_reference(ctx: VerilogParser.Port_referenceContext) =
            this@VerilogParserListener.exitPort_reference(ctx)

        override suspend fun enterPort_declaration(ctx: VerilogParser.Port_declarationContext) =
            this@VerilogParserListener.enterPort_declaration(ctx)

        override suspend fun exitPort_declaration(ctx: VerilogParser.Port_declarationContext) =
            this@VerilogParserListener.exitPort_declaration(ctx)

        override suspend fun enterModule_item(ctx: VerilogParser.Module_itemContext) =
            this@VerilogParserListener.enterModule_item(ctx)

        override suspend fun exitModule_item(ctx: VerilogParser.Module_itemContext) =
            this@VerilogParserListener.exitModule_item(ctx)

        override suspend fun enterModule_or_generate_item(ctx: VerilogParser.Module_or_generate_itemContext) =
            this@VerilogParserListener.enterModule_or_generate_item(ctx)

        override suspend fun exitModule_or_generate_item(ctx: VerilogParser.Module_or_generate_itemContext) =
            this@VerilogParserListener.exitModule_or_generate_item(ctx)

        override suspend fun enterModule_or_generate_item_declaration(ctx: VerilogParser.Module_or_generate_item_declarationContext) =
            this@VerilogParserListener.enterModule_or_generate_item_declaration(ctx)

        override suspend fun exitModule_or_generate_item_declaration(ctx: VerilogParser.Module_or_generate_item_declarationContext) =
            this@VerilogParserListener.exitModule_or_generate_item_declaration(ctx)

        override suspend fun enterParameter_override(ctx: VerilogParser.Parameter_overrideContext) =
            this@VerilogParserListener.enterParameter_override(ctx)

        override suspend fun exitParameter_override(ctx: VerilogParser.Parameter_overrideContext) =
            this@VerilogParserListener.exitParameter_override(ctx)

        override suspend fun enterConfig_declaration(ctx: VerilogParser.Config_declarationContext) =
            this@VerilogParserListener.enterConfig_declaration(ctx)

        override suspend fun exitConfig_declaration(ctx: VerilogParser.Config_declarationContext) =
            this@VerilogParserListener.exitConfig_declaration(ctx)

        override suspend fun enterDesign_statement(ctx: VerilogParser.Design_statementContext) =
            this@VerilogParserListener.enterDesign_statement(ctx)

        override suspend fun exitDesign_statement(ctx: VerilogParser.Design_statementContext) =
            this@VerilogParserListener.exitDesign_statement(ctx)

        override suspend fun enterDesign_statement_item(ctx: VerilogParser.Design_statement_itemContext) =
            this@VerilogParserListener.enterDesign_statement_item(ctx)

        override suspend fun exitDesign_statement_item(ctx: VerilogParser.Design_statement_itemContext) =
            this@VerilogParserListener.exitDesign_statement_item(ctx)

        override suspend fun enterConfig_rule_statement(ctx: VerilogParser.Config_rule_statementContext) =
            this@VerilogParserListener.enterConfig_rule_statement(ctx)

        override suspend fun exitConfig_rule_statement(ctx: VerilogParser.Config_rule_statementContext) =
            this@VerilogParserListener.exitConfig_rule_statement(ctx)

        override suspend fun enterDefault_clause(ctx: VerilogParser.Default_clauseContext) =
            this@VerilogParserListener.enterDefault_clause(ctx)

        override suspend fun exitDefault_clause(ctx: VerilogParser.Default_clauseContext) =
            this@VerilogParserListener.exitDefault_clause(ctx)

        override suspend fun enterInst_clause(ctx: VerilogParser.Inst_clauseContext) =
            this@VerilogParserListener.enterInst_clause(ctx)

        override suspend fun exitInst_clause(ctx: VerilogParser.Inst_clauseContext) =
            this@VerilogParserListener.exitInst_clause(ctx)

        override suspend fun enterInst_name(ctx: VerilogParser.Inst_nameContext) =
            this@VerilogParserListener.enterInst_name(ctx)

        override suspend fun exitInst_name(ctx: VerilogParser.Inst_nameContext) =
            this@VerilogParserListener.exitInst_name(ctx)

        override suspend fun enterCell_clause(ctx: VerilogParser.Cell_clauseContext) =
            this@VerilogParserListener.enterCell_clause(ctx)

        override suspend fun exitCell_clause(ctx: VerilogParser.Cell_clauseContext) =
            this@VerilogParserListener.exitCell_clause(ctx)

        override suspend fun enterLiblist_clause(ctx: VerilogParser.Liblist_clauseContext) =
            this@VerilogParserListener.enterLiblist_clause(ctx)

        override suspend fun exitLiblist_clause(ctx: VerilogParser.Liblist_clauseContext) =
            this@VerilogParserListener.exitLiblist_clause(ctx)

        override suspend fun enterUse_clause(ctx: VerilogParser.Use_clauseContext) =
            this@VerilogParserListener.enterUse_clause(ctx)

        override suspend fun exitUse_clause(ctx: VerilogParser.Use_clauseContext) =
            this@VerilogParserListener.exitUse_clause(ctx)

        override suspend fun enterLocal_parameter_declaration(ctx: VerilogParser.Local_parameter_declarationContext) =
            this@VerilogParserListener.enterLocal_parameter_declaration(ctx)

        override suspend fun exitLocal_parameter_declaration(ctx: VerilogParser.Local_parameter_declarationContext) =
            this@VerilogParserListener.exitLocal_parameter_declaration(ctx)

        override suspend fun enterParameter_declaration(ctx: VerilogParser.Parameter_declarationContext) =
            this@VerilogParserListener.enterParameter_declaration(ctx)

        override suspend fun exitParameter_declaration(ctx: VerilogParser.Parameter_declarationContext) =
            this@VerilogParserListener.exitParameter_declaration(ctx)

        override suspend fun enterSpecparam_declaration(ctx: VerilogParser.Specparam_declarationContext) =
            this@VerilogParserListener.enterSpecparam_declaration(ctx)

        override suspend fun exitSpecparam_declaration(ctx: VerilogParser.Specparam_declarationContext) =
            this@VerilogParserListener.exitSpecparam_declaration(ctx)

        override suspend fun enterParameter_type(ctx: VerilogParser.Parameter_typeContext) =
            this@VerilogParserListener.enterParameter_type(ctx)

        override suspend fun exitParameter_type(ctx: VerilogParser.Parameter_typeContext) =
            this@VerilogParserListener.exitParameter_type(ctx)

        override suspend fun enterInout_declaration(ctx: VerilogParser.Inout_declarationContext) =
            this@VerilogParserListener.enterInout_declaration(ctx)

        override suspend fun exitInout_declaration(ctx: VerilogParser.Inout_declarationContext) =
            this@VerilogParserListener.exitInout_declaration(ctx)

        override suspend fun enterInput_declaration(ctx: VerilogParser.Input_declarationContext) =
            this@VerilogParserListener.enterInput_declaration(ctx)

        override suspend fun exitInput_declaration(ctx: VerilogParser.Input_declarationContext) =
            this@VerilogParserListener.exitInput_declaration(ctx)

        override suspend fun enterOutput_declaration(ctx: VerilogParser.Output_declarationContext) =
            this@VerilogParserListener.enterOutput_declaration(ctx)

        override suspend fun exitOutput_declaration(ctx: VerilogParser.Output_declarationContext) =
            this@VerilogParserListener.exitOutput_declaration(ctx)

        override suspend fun enterEvent_declaration(ctx: VerilogParser.Event_declarationContext) =
            this@VerilogParserListener.enterEvent_declaration(ctx)

        override suspend fun exitEvent_declaration(ctx: VerilogParser.Event_declarationContext) =
            this@VerilogParserListener.exitEvent_declaration(ctx)

        override suspend fun enterInteger_declaration(ctx: VerilogParser.Integer_declarationContext) =
            this@VerilogParserListener.enterInteger_declaration(ctx)

        override suspend fun exitInteger_declaration(ctx: VerilogParser.Integer_declarationContext) =
            this@VerilogParserListener.exitInteger_declaration(ctx)

        override suspend fun enterNet_declaration(ctx: VerilogParser.Net_declarationContext) =
            this@VerilogParserListener.enterNet_declaration(ctx)

        override suspend fun exitNet_declaration(ctx: VerilogParser.Net_declarationContext) =
            this@VerilogParserListener.exitNet_declaration(ctx)

        override suspend fun enterReal_declaration(ctx: VerilogParser.Real_declarationContext) =
            this@VerilogParserListener.enterReal_declaration(ctx)

        override suspend fun exitReal_declaration(ctx: VerilogParser.Real_declarationContext) =
            this@VerilogParserListener.exitReal_declaration(ctx)

        override suspend fun enterRealtime_declaration(ctx: VerilogParser.Realtime_declarationContext) =
            this@VerilogParserListener.enterRealtime_declaration(ctx)

        override suspend fun exitRealtime_declaration(ctx: VerilogParser.Realtime_declarationContext) =
            this@VerilogParserListener.exitRealtime_declaration(ctx)

        override suspend fun enterReg_declaration(ctx: VerilogParser.Reg_declarationContext) =
            this@VerilogParserListener.enterReg_declaration(ctx)

        override suspend fun exitReg_declaration(ctx: VerilogParser.Reg_declarationContext) =
            this@VerilogParserListener.exitReg_declaration(ctx)

        override suspend fun enterTime_declaration(ctx: VerilogParser.Time_declarationContext) =
            this@VerilogParserListener.enterTime_declaration(ctx)

        override suspend fun exitTime_declaration(ctx: VerilogParser.Time_declarationContext) =
            this@VerilogParserListener.exitTime_declaration(ctx)

        override suspend fun enterNet_type(ctx: VerilogParser.Net_typeContext) =
            this@VerilogParserListener.enterNet_type(ctx)

        override suspend fun exitNet_type(ctx: VerilogParser.Net_typeContext) =
            this@VerilogParserListener.exitNet_type(ctx)

        override suspend fun enterOutput_variable_type(ctx: VerilogParser.Output_variable_typeContext) =
            this@VerilogParserListener.enterOutput_variable_type(ctx)

        override suspend fun exitOutput_variable_type(ctx: VerilogParser.Output_variable_typeContext) =
            this@VerilogParserListener.exitOutput_variable_type(ctx)

        override suspend fun enterReal_type(ctx: VerilogParser.Real_typeContext) =
            this@VerilogParserListener.enterReal_type(ctx)

        override suspend fun exitReal_type(ctx: VerilogParser.Real_typeContext) =
            this@VerilogParserListener.exitReal_type(ctx)

        override suspend fun enterVariable_type(ctx: VerilogParser.Variable_typeContext) =
            this@VerilogParserListener.enterVariable_type(ctx)

        override suspend fun exitVariable_type(ctx: VerilogParser.Variable_typeContext) =
            this@VerilogParserListener.exitVariable_type(ctx)

        override suspend fun enterDrive_strength(ctx: VerilogParser.Drive_strengthContext) =
            this@VerilogParserListener.enterDrive_strength(ctx)

        override suspend fun exitDrive_strength(ctx: VerilogParser.Drive_strengthContext) =
            this@VerilogParserListener.exitDrive_strength(ctx)

        override suspend fun enterStrength0(ctx: VerilogParser.Strength0Context) =
            this@VerilogParserListener.enterStrength0(ctx)

        override suspend fun exitStrength0(ctx: VerilogParser.Strength0Context) =
            this@VerilogParserListener.exitStrength0(ctx)

        override suspend fun enterStrength1(ctx: VerilogParser.Strength1Context) =
            this@VerilogParserListener.enterStrength1(ctx)

        override suspend fun exitStrength1(ctx: VerilogParser.Strength1Context) =
            this@VerilogParserListener.exitStrength1(ctx)

        override suspend fun enterCharge_strength(ctx: VerilogParser.Charge_strengthContext) =
            this@VerilogParserListener.enterCharge_strength(ctx)

        override suspend fun exitCharge_strength(ctx: VerilogParser.Charge_strengthContext) =
            this@VerilogParserListener.exitCharge_strength(ctx)

        override suspend fun enterDelay3(ctx: VerilogParser.Delay3Context) = this@VerilogParserListener.enterDelay3(ctx)
        override suspend fun exitDelay3(ctx: VerilogParser.Delay3Context) = this@VerilogParserListener.exitDelay3(ctx)

        override suspend fun enterDelay2(ctx: VerilogParser.Delay2Context) = this@VerilogParserListener.enterDelay2(ctx)
        override suspend fun exitDelay2(ctx: VerilogParser.Delay2Context) = this@VerilogParserListener.exitDelay2(ctx)

        override suspend fun enterDelay_value(ctx: VerilogParser.Delay_valueContext) =
            this@VerilogParserListener.enterDelay_value(ctx)

        override suspend fun exitDelay_value(ctx: VerilogParser.Delay_valueContext) =
            this@VerilogParserListener.exitDelay_value(ctx)

        override suspend fun enterList_of_defparam_assignments(ctx: VerilogParser.List_of_defparam_assignmentsContext) =
            this@VerilogParserListener.enterList_of_defparam_assignments(ctx)

        override suspend fun exitList_of_defparam_assignments(ctx: VerilogParser.List_of_defparam_assignmentsContext) =
            this@VerilogParserListener.exitList_of_defparam_assignments(ctx)

        override suspend fun enterList_of_event_identifiers(ctx: VerilogParser.List_of_event_identifiersContext) =
            this@VerilogParserListener.enterList_of_event_identifiers(ctx)

        override suspend fun exitList_of_event_identifiers(ctx: VerilogParser.List_of_event_identifiersContext) =
            this@VerilogParserListener.exitList_of_event_identifiers(ctx)

        override suspend fun enterEvent_id(ctx: VerilogParser.Event_idContext) =
            this@VerilogParserListener.enterEvent_id(ctx)

        override suspend fun exitEvent_id(ctx: VerilogParser.Event_idContext) =
            this@VerilogParserListener.exitEvent_id(ctx)

        override suspend fun enterList_of_net_decl_assignments(ctx: VerilogParser.List_of_net_decl_assignmentsContext) =
            this@VerilogParserListener.enterList_of_net_decl_assignments(ctx)

        override suspend fun exitList_of_net_decl_assignments(ctx: VerilogParser.List_of_net_decl_assignmentsContext) =
            this@VerilogParserListener.exitList_of_net_decl_assignments(ctx)

        override suspend fun enterList_of_net_identifiers(ctx: VerilogParser.List_of_net_identifiersContext) =
            this@VerilogParserListener.enterList_of_net_identifiers(ctx)

        override suspend fun exitList_of_net_identifiers(ctx: VerilogParser.List_of_net_identifiersContext) =
            this@VerilogParserListener.exitList_of_net_identifiers(ctx)

        override suspend fun enterNet_id(ctx: VerilogParser.Net_idContext) = this@VerilogParserListener.enterNet_id(ctx)
        override suspend fun exitNet_id(ctx: VerilogParser.Net_idContext) = this@VerilogParserListener.exitNet_id(ctx)

        override suspend fun enterList_of_param_assignments(ctx: VerilogParser.List_of_param_assignmentsContext) =
            this@VerilogParserListener.enterList_of_param_assignments(ctx)

        override suspend fun exitList_of_param_assignments(ctx: VerilogParser.List_of_param_assignmentsContext) =
            this@VerilogParserListener.exitList_of_param_assignments(ctx)

        override suspend fun enterList_of_port_identifiers(ctx: VerilogParser.List_of_port_identifiersContext) =
            this@VerilogParserListener.enterList_of_port_identifiers(ctx)

        override suspend fun exitList_of_port_identifiers(ctx: VerilogParser.List_of_port_identifiersContext) =
            this@VerilogParserListener.exitList_of_port_identifiers(ctx)

        override suspend fun enterList_of_real_identifiers(ctx: VerilogParser.List_of_real_identifiersContext) =
            this@VerilogParserListener.enterList_of_real_identifiers(ctx)

        override suspend fun exitList_of_real_identifiers(ctx: VerilogParser.List_of_real_identifiersContext) =
            this@VerilogParserListener.exitList_of_real_identifiers(ctx)

        override suspend fun enterList_of_specparam_assignments(ctx: VerilogParser.List_of_specparam_assignmentsContext) =
            this@VerilogParserListener.enterList_of_specparam_assignments(ctx)

        override suspend fun exitList_of_specparam_assignments(ctx: VerilogParser.List_of_specparam_assignmentsContext) =
            this@VerilogParserListener.exitList_of_specparam_assignments(ctx)

        override suspend fun enterList_of_variable_identifiers(ctx: VerilogParser.List_of_variable_identifiersContext) =
            this@VerilogParserListener.enterList_of_variable_identifiers(ctx)

        override suspend fun exitList_of_variable_identifiers(ctx: VerilogParser.List_of_variable_identifiersContext) =
            this@VerilogParserListener.exitList_of_variable_identifiers(ctx)

        override suspend fun enterList_of_variable_port_identifiers(ctx: VerilogParser.List_of_variable_port_identifiersContext) =
            this@VerilogParserListener.enterList_of_variable_port_identifiers(ctx)

        override suspend fun exitList_of_variable_port_identifiers(ctx: VerilogParser.List_of_variable_port_identifiersContext) =
            this@VerilogParserListener.exitList_of_variable_port_identifiers(ctx)

        override suspend fun enterVar_port_id(ctx: VerilogParser.Var_port_idContext) =
            this@VerilogParserListener.enterVar_port_id(ctx)

        override suspend fun exitVar_port_id(ctx: VerilogParser.Var_port_idContext) =
            this@VerilogParserListener.exitVar_port_id(ctx)

        override suspend fun enterDefparam_assignment(ctx: VerilogParser.Defparam_assignmentContext) =
            this@VerilogParserListener.enterDefparam_assignment(ctx)

        override suspend fun exitDefparam_assignment(ctx: VerilogParser.Defparam_assignmentContext) =
            this@VerilogParserListener.exitDefparam_assignment(ctx)

        override suspend fun enterNet_decl_assignment(ctx: VerilogParser.Net_decl_assignmentContext) =
            this@VerilogParserListener.enterNet_decl_assignment(ctx)

        override suspend fun exitNet_decl_assignment(ctx: VerilogParser.Net_decl_assignmentContext) =
            this@VerilogParserListener.exitNet_decl_assignment(ctx)

        override suspend fun enterParam_assignment(ctx: VerilogParser.Param_assignmentContext) =
            this@VerilogParserListener.enterParam_assignment(ctx)

        override suspend fun exitParam_assignment(ctx: VerilogParser.Param_assignmentContext) =
            this@VerilogParserListener.exitParam_assignment(ctx)

        override suspend fun enterSpecparam_assignment(ctx: VerilogParser.Specparam_assignmentContext) =
            this@VerilogParserListener.enterSpecparam_assignment(ctx)

        override suspend fun exitSpecparam_assignment(ctx: VerilogParser.Specparam_assignmentContext) =
            this@VerilogParserListener.exitSpecparam_assignment(ctx)

        override suspend fun enterPulse_control_specparam(ctx: VerilogParser.Pulse_control_specparamContext) =
            this@VerilogParserListener.enterPulse_control_specparam(ctx)

        override suspend fun exitPulse_control_specparam(ctx: VerilogParser.Pulse_control_specparamContext) =
            this@VerilogParserListener.exitPulse_control_specparam(ctx)

        override suspend fun enterError_limit_value(ctx: VerilogParser.Error_limit_valueContext) =
            this@VerilogParserListener.enterError_limit_value(ctx)

        override suspend fun exitError_limit_value(ctx: VerilogParser.Error_limit_valueContext) =
            this@VerilogParserListener.exitError_limit_value(ctx)

        override suspend fun enterReject_limit_value(ctx: VerilogParser.Reject_limit_valueContext) =
            this@VerilogParserListener.enterReject_limit_value(ctx)

        override suspend fun exitReject_limit_value(ctx: VerilogParser.Reject_limit_valueContext) =
            this@VerilogParserListener.exitReject_limit_value(ctx)

        override suspend fun enterLimit_value(ctx: VerilogParser.Limit_valueContext) =
            this@VerilogParserListener.enterLimit_value(ctx)

        override suspend fun exitLimit_value(ctx: VerilogParser.Limit_valueContext) =
            this@VerilogParserListener.exitLimit_value(ctx)

        override suspend fun enterDimension(ctx: VerilogParser.DimensionContext) =
            this@VerilogParserListener.enterDimension(ctx)

        override suspend fun exitDimension(ctx: VerilogParser.DimensionContext) =
            this@VerilogParserListener.exitDimension(ctx)

        override suspend fun enterRange_(ctx: VerilogParser.Range_Context) = this@VerilogParserListener.enterRange_(ctx)
        override suspend fun exitRange_(ctx: VerilogParser.Range_Context) = this@VerilogParserListener.exitRange_(ctx)

        override suspend fun enterFunction_declaration(ctx: VerilogParser.Function_declarationContext) =
            this@VerilogParserListener.enterFunction_declaration(ctx)

        override suspend fun exitFunction_declaration(ctx: VerilogParser.Function_declarationContext) =
            this@VerilogParserListener.exitFunction_declaration(ctx)

        override suspend fun enterFunction_item_declaration(ctx: VerilogParser.Function_item_declarationContext) =
            this@VerilogParserListener.enterFunction_item_declaration(ctx)

        override suspend fun exitFunction_item_declaration(ctx: VerilogParser.Function_item_declarationContext) =
            this@VerilogParserListener.exitFunction_item_declaration(ctx)

        override suspend fun enterFunction_port_list(ctx: VerilogParser.Function_port_listContext) =
            this@VerilogParserListener.enterFunction_port_list(ctx)

        override suspend fun exitFunction_port_list(ctx: VerilogParser.Function_port_listContext) =
            this@VerilogParserListener.exitFunction_port_list(ctx)

        override suspend fun enterFunc_port_item(ctx: VerilogParser.Func_port_itemContext) =
            this@VerilogParserListener.enterFunc_port_item(ctx)

        override suspend fun exitFunc_port_item(ctx: VerilogParser.Func_port_itemContext) =
            this@VerilogParserListener.exitFunc_port_item(ctx)

        override suspend fun enterFunction_range_or_type(ctx: VerilogParser.Function_range_or_typeContext) =
            this@VerilogParserListener.enterFunction_range_or_type(ctx)

        override suspend fun exitFunction_range_or_type(ctx: VerilogParser.Function_range_or_typeContext) =
            this@VerilogParserListener.exitFunction_range_or_type(ctx)

        override suspend fun enterTask_declaration(ctx: VerilogParser.Task_declarationContext) =
            this@VerilogParserListener.enterTask_declaration(ctx)

        override suspend fun exitTask_declaration(ctx: VerilogParser.Task_declarationContext) =
            this@VerilogParserListener.exitTask_declaration(ctx)

        override suspend fun enterTask_item_declaration(ctx: VerilogParser.Task_item_declarationContext) =
            this@VerilogParserListener.enterTask_item_declaration(ctx)

        override suspend fun exitTask_item_declaration(ctx: VerilogParser.Task_item_declarationContext) =
            this@VerilogParserListener.exitTask_item_declaration(ctx)

        override suspend fun enterTask_port_list(ctx: VerilogParser.Task_port_listContext) =
            this@VerilogParserListener.enterTask_port_list(ctx)

        override suspend fun exitTask_port_list(ctx: VerilogParser.Task_port_listContext) =
            this@VerilogParserListener.exitTask_port_list(ctx)

        override suspend fun enterTask_port_item(ctx: VerilogParser.Task_port_itemContext) =
            this@VerilogParserListener.enterTask_port_item(ctx)

        override suspend fun exitTask_port_item(ctx: VerilogParser.Task_port_itemContext) =
            this@VerilogParserListener.exitTask_port_item(ctx)

        override suspend fun enterTf_input_declaration(ctx: VerilogParser.Tf_input_declarationContext) =
            this@VerilogParserListener.enterTf_input_declaration(ctx)

        override suspend fun exitTf_input_declaration(ctx: VerilogParser.Tf_input_declarationContext) =
            this@VerilogParserListener.exitTf_input_declaration(ctx)

        override suspend fun enterTf_output_declaration(ctx: VerilogParser.Tf_output_declarationContext) =
            this@VerilogParserListener.enterTf_output_declaration(ctx)

        override suspend fun exitTf_output_declaration(ctx: VerilogParser.Tf_output_declarationContext) =
            this@VerilogParserListener.exitTf_output_declaration(ctx)

        override suspend fun enterTf_inout_declaration(ctx: VerilogParser.Tf_inout_declarationContext) =
            this@VerilogParserListener.enterTf_inout_declaration(ctx)

        override suspend fun exitTf_inout_declaration(ctx: VerilogParser.Tf_inout_declarationContext) =
            this@VerilogParserListener.exitTf_inout_declaration(ctx)

        override suspend fun enterTask_port_type(ctx: VerilogParser.Task_port_typeContext) =
            this@VerilogParserListener.enterTask_port_type(ctx)

        override suspend fun exitTask_port_type(ctx: VerilogParser.Task_port_typeContext) =
            this@VerilogParserListener.exitTask_port_type(ctx)

        override suspend fun enterBlock_item_declaration(ctx: VerilogParser.Block_item_declarationContext) =
            this@VerilogParserListener.enterBlock_item_declaration(ctx)

        override suspend fun exitBlock_item_declaration(ctx: VerilogParser.Block_item_declarationContext) =
            this@VerilogParserListener.exitBlock_item_declaration(ctx)

        override suspend fun enterList_of_block_variable_identifiers(ctx: VerilogParser.List_of_block_variable_identifiersContext) =
            this@VerilogParserListener.enterList_of_block_variable_identifiers(ctx)

        override suspend fun exitList_of_block_variable_identifiers(ctx: VerilogParser.List_of_block_variable_identifiersContext) =
            this@VerilogParserListener.exitList_of_block_variable_identifiers(ctx)

        override suspend fun enterList_of_block_real_identifiers(ctx: VerilogParser.List_of_block_real_identifiersContext) =
            this@VerilogParserListener.enterList_of_block_real_identifiers(ctx)

        override suspend fun exitList_of_block_real_identifiers(ctx: VerilogParser.List_of_block_real_identifiersContext) =
            this@VerilogParserListener.exitList_of_block_real_identifiers(ctx)

        override suspend fun enterBlock_variable_type(ctx: VerilogParser.Block_variable_typeContext) =
            this@VerilogParserListener.enterBlock_variable_type(ctx)

        override suspend fun exitBlock_variable_type(ctx: VerilogParser.Block_variable_typeContext) =
            this@VerilogParserListener.exitBlock_variable_type(ctx)

        override suspend fun enterBlock_real_type(ctx: VerilogParser.Block_real_typeContext) =
            this@VerilogParserListener.enterBlock_real_type(ctx)

        override suspend fun exitBlock_real_type(ctx: VerilogParser.Block_real_typeContext) =
            this@VerilogParserListener.exitBlock_real_type(ctx)

        override suspend fun enterGate_instantiation(ctx: VerilogParser.Gate_instantiationContext) =
            this@VerilogParserListener.enterGate_instantiation(ctx)

        override suspend fun exitGate_instantiation(ctx: VerilogParser.Gate_instantiationContext) =
            this@VerilogParserListener.exitGate_instantiation(ctx)

        override suspend fun enterCmos_switch_instance(ctx: VerilogParser.Cmos_switch_instanceContext) =
            this@VerilogParserListener.enterCmos_switch_instance(ctx)

        override suspend fun exitCmos_switch_instance(ctx: VerilogParser.Cmos_switch_instanceContext) =
            this@VerilogParserListener.exitCmos_switch_instance(ctx)

        override suspend fun enterEnable_gate_instance(ctx: VerilogParser.Enable_gate_instanceContext) =
            this@VerilogParserListener.enterEnable_gate_instance(ctx)

        override suspend fun exitEnable_gate_instance(ctx: VerilogParser.Enable_gate_instanceContext) =
            this@VerilogParserListener.exitEnable_gate_instance(ctx)

        override suspend fun enterMos_switch_instance(ctx: VerilogParser.Mos_switch_instanceContext) =
            this@VerilogParserListener.enterMos_switch_instance(ctx)

        override suspend fun exitMos_switch_instance(ctx: VerilogParser.Mos_switch_instanceContext) =
            this@VerilogParserListener.exitMos_switch_instance(ctx)

        override suspend fun enterN_input_gate_instance(ctx: VerilogParser.N_input_gate_instanceContext) =
            this@VerilogParserListener.enterN_input_gate_instance(ctx)

        override suspend fun exitN_input_gate_instance(ctx: VerilogParser.N_input_gate_instanceContext) =
            this@VerilogParserListener.exitN_input_gate_instance(ctx)

        override suspend fun enterN_output_gate_instance(ctx: VerilogParser.N_output_gate_instanceContext) =
            this@VerilogParserListener.enterN_output_gate_instance(ctx)

        override suspend fun exitN_output_gate_instance(ctx: VerilogParser.N_output_gate_instanceContext) =
            this@VerilogParserListener.exitN_output_gate_instance(ctx)

        override suspend fun enterPass_switch_instance(ctx: VerilogParser.Pass_switch_instanceContext) =
            this@VerilogParserListener.enterPass_switch_instance(ctx)

        override suspend fun exitPass_switch_instance(ctx: VerilogParser.Pass_switch_instanceContext) =
            this@VerilogParserListener.exitPass_switch_instance(ctx)

        override suspend fun enterPass_enable_switch_instance(ctx: VerilogParser.Pass_enable_switch_instanceContext) =
            this@VerilogParserListener.enterPass_enable_switch_instance(ctx)

        override suspend fun exitPass_enable_switch_instance(ctx: VerilogParser.Pass_enable_switch_instanceContext) =
            this@VerilogParserListener.exitPass_enable_switch_instance(ctx)

        override suspend fun enterPull_gate_instance(ctx: VerilogParser.Pull_gate_instanceContext) =
            this@VerilogParserListener.enterPull_gate_instance(ctx)

        override suspend fun exitPull_gate_instance(ctx: VerilogParser.Pull_gate_instanceContext) =
            this@VerilogParserListener.exitPull_gate_instance(ctx)

        override suspend fun enterName_of_gate_instance(ctx: VerilogParser.Name_of_gate_instanceContext) =
            this@VerilogParserListener.enterName_of_gate_instance(ctx)

        override suspend fun exitName_of_gate_instance(ctx: VerilogParser.Name_of_gate_instanceContext) =
            this@VerilogParserListener.exitName_of_gate_instance(ctx)

        override suspend fun enterPulldown_strength(ctx: VerilogParser.Pulldown_strengthContext) =
            this@VerilogParserListener.enterPulldown_strength(ctx)

        override suspend fun exitPulldown_strength(ctx: VerilogParser.Pulldown_strengthContext) =
            this@VerilogParserListener.exitPulldown_strength(ctx)

        override suspend fun enterPullup_strength(ctx: VerilogParser.Pullup_strengthContext) =
            this@VerilogParserListener.enterPullup_strength(ctx)

        override suspend fun exitPullup_strength(ctx: VerilogParser.Pullup_strengthContext) =
            this@VerilogParserListener.exitPullup_strength(ctx)

        override suspend fun enterEnable_terminal(ctx: VerilogParser.Enable_terminalContext) =
            this@VerilogParserListener.enterEnable_terminal(ctx)

        override suspend fun exitEnable_terminal(ctx: VerilogParser.Enable_terminalContext) =
            this@VerilogParserListener.exitEnable_terminal(ctx)

        override suspend fun enterInout_terminal(ctx: VerilogParser.Inout_terminalContext) =
            this@VerilogParserListener.enterInout_terminal(ctx)

        override suspend fun exitInout_terminal(ctx: VerilogParser.Inout_terminalContext) =
            this@VerilogParserListener.exitInout_terminal(ctx)

        override suspend fun enterInput_terminal(ctx: VerilogParser.Input_terminalContext) =
            this@VerilogParserListener.enterInput_terminal(ctx)

        override suspend fun exitInput_terminal(ctx: VerilogParser.Input_terminalContext) =
            this@VerilogParserListener.exitInput_terminal(ctx)

        override suspend fun enterNcontrol_terminal(ctx: VerilogParser.Ncontrol_terminalContext) =
            this@VerilogParserListener.enterNcontrol_terminal(ctx)

        override suspend fun exitNcontrol_terminal(ctx: VerilogParser.Ncontrol_terminalContext) =
            this@VerilogParserListener.exitNcontrol_terminal(ctx)

        override suspend fun enterOutput_terminal(ctx: VerilogParser.Output_terminalContext) =
            this@VerilogParserListener.enterOutput_terminal(ctx)

        override suspend fun exitOutput_terminal(ctx: VerilogParser.Output_terminalContext) =
            this@VerilogParserListener.exitOutput_terminal(ctx)

        override suspend fun enterPcontrol_terminal(ctx: VerilogParser.Pcontrol_terminalContext) =
            this@VerilogParserListener.enterPcontrol_terminal(ctx)

        override suspend fun exitPcontrol_terminal(ctx: VerilogParser.Pcontrol_terminalContext) =
            this@VerilogParserListener.exitPcontrol_terminal(ctx)

        override suspend fun enterCmos_switchtype(ctx: VerilogParser.Cmos_switchtypeContext) =
            this@VerilogParserListener.enterCmos_switchtype(ctx)

        override suspend fun exitCmos_switchtype(ctx: VerilogParser.Cmos_switchtypeContext) =
            this@VerilogParserListener.exitCmos_switchtype(ctx)

        override suspend fun enterEnable_gatetype(ctx: VerilogParser.Enable_gatetypeContext) =
            this@VerilogParserListener.enterEnable_gatetype(ctx)

        override suspend fun exitEnable_gatetype(ctx: VerilogParser.Enable_gatetypeContext) =
            this@VerilogParserListener.exitEnable_gatetype(ctx)

        override suspend fun enterMos_switchtype(ctx: VerilogParser.Mos_switchtypeContext) =
            this@VerilogParserListener.enterMos_switchtype(ctx)

        override suspend fun exitMos_switchtype(ctx: VerilogParser.Mos_switchtypeContext) =
            this@VerilogParserListener.exitMos_switchtype(ctx)

        override suspend fun enterN_input_gatetype(ctx: VerilogParser.N_input_gatetypeContext) =
            this@VerilogParserListener.enterN_input_gatetype(ctx)

        override suspend fun exitN_input_gatetype(ctx: VerilogParser.N_input_gatetypeContext) =
            this@VerilogParserListener.exitN_input_gatetype(ctx)

        override suspend fun enterN_output_gatetype(ctx: VerilogParser.N_output_gatetypeContext) =
            this@VerilogParserListener.enterN_output_gatetype(ctx)

        override suspend fun exitN_output_gatetype(ctx: VerilogParser.N_output_gatetypeContext) =
            this@VerilogParserListener.exitN_output_gatetype(ctx)

        override suspend fun enterPass_en_switchtype(ctx: VerilogParser.Pass_en_switchtypeContext) =
            this@VerilogParserListener.enterPass_en_switchtype(ctx)

        override suspend fun exitPass_en_switchtype(ctx: VerilogParser.Pass_en_switchtypeContext) =
            this@VerilogParserListener.exitPass_en_switchtype(ctx)

        override suspend fun enterPass_switchtype(ctx: VerilogParser.Pass_switchtypeContext) =
            this@VerilogParserListener.enterPass_switchtype(ctx)

        override suspend fun exitPass_switchtype(ctx: VerilogParser.Pass_switchtypeContext) =
            this@VerilogParserListener.exitPass_switchtype(ctx)

        override suspend fun enterModule_instantiation(ctx: VerilogParser.Module_instantiationContext) =
            this@VerilogParserListener.enterModule_instantiation(ctx)

        override suspend fun exitModule_instantiation(ctx: VerilogParser.Module_instantiationContext) =
            this@VerilogParserListener.exitModule_instantiation(ctx)

        override suspend fun enterParameter_value_assignment(ctx: VerilogParser.Parameter_value_assignmentContext) =
            this@VerilogParserListener.enterParameter_value_assignment(ctx)

        override suspend fun exitParameter_value_assignment(ctx: VerilogParser.Parameter_value_assignmentContext) =
            this@VerilogParserListener.exitParameter_value_assignment(ctx)

        override suspend fun enterList_of_parameter_assignments(ctx: VerilogParser.List_of_parameter_assignmentsContext) =
            this@VerilogParserListener.enterList_of_parameter_assignments(ctx)

        override suspend fun exitList_of_parameter_assignments(ctx: VerilogParser.List_of_parameter_assignmentsContext) =
            this@VerilogParserListener.exitList_of_parameter_assignments(ctx)

        override suspend fun enterOrdered_parameter_assignment(ctx: VerilogParser.Ordered_parameter_assignmentContext) =
            this@VerilogParserListener.enterOrdered_parameter_assignment(ctx)

        override suspend fun exitOrdered_parameter_assignment(ctx: VerilogParser.Ordered_parameter_assignmentContext) =
            this@VerilogParserListener.exitOrdered_parameter_assignment(ctx)

        override suspend fun enterNamed_parameter_assignment(ctx: VerilogParser.Named_parameter_assignmentContext) =
            this@VerilogParserListener.enterNamed_parameter_assignment(ctx)

        override suspend fun exitNamed_parameter_assignment(ctx: VerilogParser.Named_parameter_assignmentContext) =
            this@VerilogParserListener.exitNamed_parameter_assignment(ctx)

        override suspend fun enterModule_instance(ctx: VerilogParser.Module_instanceContext) =
            this@VerilogParserListener.enterModule_instance(ctx)

        override suspend fun exitModule_instance(ctx: VerilogParser.Module_instanceContext) =
            this@VerilogParserListener.exitModule_instance(ctx)

        override suspend fun enterName_of_module_instance(ctx: VerilogParser.Name_of_module_instanceContext) =
            this@VerilogParserListener.enterName_of_module_instance(ctx)

        override suspend fun exitName_of_module_instance(ctx: VerilogParser.Name_of_module_instanceContext) =
            this@VerilogParserListener.exitName_of_module_instance(ctx)

        override suspend fun enterList_of_port_connections(ctx: VerilogParser.List_of_port_connectionsContext) =
            this@VerilogParserListener.enterList_of_port_connections(ctx)

        override suspend fun exitList_of_port_connections(ctx: VerilogParser.List_of_port_connectionsContext) =
            this@VerilogParserListener.exitList_of_port_connections(ctx)

        override suspend fun enterOrdered_port_connection(ctx: VerilogParser.Ordered_port_connectionContext) =
            this@VerilogParserListener.enterOrdered_port_connection(ctx)

        override suspend fun exitOrdered_port_connection(ctx: VerilogParser.Ordered_port_connectionContext) =
            this@VerilogParserListener.exitOrdered_port_connection(ctx)

        override suspend fun enterNamed_port_connection(ctx: VerilogParser.Named_port_connectionContext) =
            this@VerilogParserListener.enterNamed_port_connection(ctx)

        override suspend fun exitNamed_port_connection(ctx: VerilogParser.Named_port_connectionContext) =
            this@VerilogParserListener.exitNamed_port_connection(ctx)

        override suspend fun enterGenerate_region(ctx: VerilogParser.Generate_regionContext) =
            this@VerilogParserListener.enterGenerate_region(ctx)

        override suspend fun exitGenerate_region(ctx: VerilogParser.Generate_regionContext) =
            this@VerilogParserListener.exitGenerate_region(ctx)

        override suspend fun enterGenvar_declaration(ctx: VerilogParser.Genvar_declarationContext) =
            this@VerilogParserListener.enterGenvar_declaration(ctx)

        override suspend fun exitGenvar_declaration(ctx: VerilogParser.Genvar_declarationContext) =
            this@VerilogParserListener.exitGenvar_declaration(ctx)

        override suspend fun enterList_of_genvar_identifiers(ctx: VerilogParser.List_of_genvar_identifiersContext) =
            this@VerilogParserListener.enterList_of_genvar_identifiers(ctx)

        override suspend fun exitList_of_genvar_identifiers(ctx: VerilogParser.List_of_genvar_identifiersContext) =
            this@VerilogParserListener.exitList_of_genvar_identifiers(ctx)

        override suspend fun enterLoop_generate_construct(ctx: VerilogParser.Loop_generate_constructContext) =
            this@VerilogParserListener.enterLoop_generate_construct(ctx)

        override suspend fun exitLoop_generate_construct(ctx: VerilogParser.Loop_generate_constructContext) =
            this@VerilogParserListener.exitLoop_generate_construct(ctx)

        override suspend fun enterGenvar_initialization(ctx: VerilogParser.Genvar_initializationContext) =
            this@VerilogParserListener.enterGenvar_initialization(ctx)

        override suspend fun exitGenvar_initialization(ctx: VerilogParser.Genvar_initializationContext) =
            this@VerilogParserListener.exitGenvar_initialization(ctx)

        override suspend fun enterGenvar_expression(ctx: VerilogParser.Genvar_expressionContext) =
            this@VerilogParserListener.enterGenvar_expression(ctx)

        override suspend fun exitGenvar_expression(ctx: VerilogParser.Genvar_expressionContext) =
            this@VerilogParserListener.exitGenvar_expression(ctx)

        override suspend fun enterGenvar_iteration(ctx: VerilogParser.Genvar_iterationContext) =
            this@VerilogParserListener.enterGenvar_iteration(ctx)

        override suspend fun exitGenvar_iteration(ctx: VerilogParser.Genvar_iterationContext) =
            this@VerilogParserListener.exitGenvar_iteration(ctx)

        override suspend fun enterConditional_generate_construct(ctx: VerilogParser.Conditional_generate_constructContext) =
            this@VerilogParserListener.enterConditional_generate_construct(ctx)

        override suspend fun exitConditional_generate_construct(ctx: VerilogParser.Conditional_generate_constructContext) =
            this@VerilogParserListener.exitConditional_generate_construct(ctx)

        override suspend fun enterIf_generate_construct(ctx: VerilogParser.If_generate_constructContext) =
            this@VerilogParserListener.enterIf_generate_construct(ctx)

        override suspend fun exitIf_generate_construct(ctx: VerilogParser.If_generate_constructContext) =
            this@VerilogParserListener.exitIf_generate_construct(ctx)

        override suspend fun enterCase_generate_construct(ctx: VerilogParser.Case_generate_constructContext) =
            this@VerilogParserListener.enterCase_generate_construct(ctx)

        override suspend fun exitCase_generate_construct(ctx: VerilogParser.Case_generate_constructContext) =
            this@VerilogParserListener.exitCase_generate_construct(ctx)

        override suspend fun enterCase_generate_item(ctx: VerilogParser.Case_generate_itemContext) =
            this@VerilogParserListener.enterCase_generate_item(ctx)

        override suspend fun exitCase_generate_item(ctx: VerilogParser.Case_generate_itemContext) =
            this@VerilogParserListener.exitCase_generate_item(ctx)

        override suspend fun enterGenerate_block(ctx: VerilogParser.Generate_blockContext) =
            this@VerilogParserListener.enterGenerate_block(ctx)

        override suspend fun exitGenerate_block(ctx: VerilogParser.Generate_blockContext) =
            this@VerilogParserListener.exitGenerate_block(ctx)

        override suspend fun enterGenerate_block_name(ctx: VerilogParser.Generate_block_nameContext) =
            this@VerilogParserListener.enterGenerate_block_name(ctx)

        override suspend fun exitGenerate_block_name(ctx: VerilogParser.Generate_block_nameContext) =
            this@VerilogParserListener.exitGenerate_block_name(ctx)

        override suspend fun enterGenerate_block_or_null(ctx: VerilogParser.Generate_block_or_nullContext) =
            this@VerilogParserListener.enterGenerate_block_or_null(ctx)

        override suspend fun exitGenerate_block_or_null(ctx: VerilogParser.Generate_block_or_nullContext) =
            this@VerilogParserListener.exitGenerate_block_or_null(ctx)

        override suspend fun enterUdp_declaration(ctx: VerilogParser.Udp_declarationContext) =
            this@VerilogParserListener.enterUdp_declaration(ctx)

        override suspend fun exitUdp_declaration(ctx: VerilogParser.Udp_declarationContext) =
            this@VerilogParserListener.exitUdp_declaration(ctx)

        override suspend fun enterUdp_port_list(ctx: VerilogParser.Udp_port_listContext) =
            this@VerilogParserListener.enterUdp_port_list(ctx)

        override suspend fun exitUdp_port_list(ctx: VerilogParser.Udp_port_listContext) =
            this@VerilogParserListener.exitUdp_port_list(ctx)

        override suspend fun enterUdp_declaration_port_list(ctx: VerilogParser.Udp_declaration_port_listContext) =
            this@VerilogParserListener.enterUdp_declaration_port_list(ctx)

        override suspend fun exitUdp_declaration_port_list(ctx: VerilogParser.Udp_declaration_port_listContext) =
            this@VerilogParserListener.exitUdp_declaration_port_list(ctx)

        override suspend fun enterUdp_port_declaration(ctx: VerilogParser.Udp_port_declarationContext) =
            this@VerilogParserListener.enterUdp_port_declaration(ctx)

        override suspend fun exitUdp_port_declaration(ctx: VerilogParser.Udp_port_declarationContext) =
            this@VerilogParserListener.exitUdp_port_declaration(ctx)

        override suspend fun enterUdp_output_declaration(ctx: VerilogParser.Udp_output_declarationContext) =
            this@VerilogParserListener.enterUdp_output_declaration(ctx)

        override suspend fun exitUdp_output_declaration(ctx: VerilogParser.Udp_output_declarationContext) =
            this@VerilogParserListener.exitUdp_output_declaration(ctx)

        override suspend fun enterUdp_input_declaration(ctx: VerilogParser.Udp_input_declarationContext) =
            this@VerilogParserListener.enterUdp_input_declaration(ctx)

        override suspend fun exitUdp_input_declaration(ctx: VerilogParser.Udp_input_declarationContext) =
            this@VerilogParserListener.exitUdp_input_declaration(ctx)

        override suspend fun enterUdp_reg_declaration(ctx: VerilogParser.Udp_reg_declarationContext) =
            this@VerilogParserListener.enterUdp_reg_declaration(ctx)

        override suspend fun exitUdp_reg_declaration(ctx: VerilogParser.Udp_reg_declarationContext) =
            this@VerilogParserListener.exitUdp_reg_declaration(ctx)

        override suspend fun enterUdp_body(ctx: VerilogParser.Udp_bodyContext) =
            this@VerilogParserListener.enterUdp_body(ctx)

        override suspend fun exitUdp_body(ctx: VerilogParser.Udp_bodyContext) =
            this@VerilogParserListener.exitUdp_body(ctx)

        override suspend fun enterCombinational_body(ctx: VerilogParser.Combinational_bodyContext) =
            this@VerilogParserListener.enterCombinational_body(ctx)

        override suspend fun exitCombinational_body(ctx: VerilogParser.Combinational_bodyContext) =
            this@VerilogParserListener.exitCombinational_body(ctx)

        override suspend fun enterCombinational_entry(ctx: VerilogParser.Combinational_entryContext) =
            this@VerilogParserListener.enterCombinational_entry(ctx)

        override suspend fun exitCombinational_entry(ctx: VerilogParser.Combinational_entryContext) =
            this@VerilogParserListener.exitCombinational_entry(ctx)

        override suspend fun enterSequential_body(ctx: VerilogParser.Sequential_bodyContext) =
            this@VerilogParserListener.enterSequential_body(ctx)

        override suspend fun exitSequential_body(ctx: VerilogParser.Sequential_bodyContext) =
            this@VerilogParserListener.exitSequential_body(ctx)

        override suspend fun enterUdp_initial_statement(ctx: VerilogParser.Udp_initial_statementContext) =
            this@VerilogParserListener.enterUdp_initial_statement(ctx)

        override suspend fun exitUdp_initial_statement(ctx: VerilogParser.Udp_initial_statementContext) =
            this@VerilogParserListener.exitUdp_initial_statement(ctx)

        override suspend fun enterInit_val(ctx: VerilogParser.Init_valContext) =
            this@VerilogParserListener.enterInit_val(ctx)

        override suspend fun exitInit_val(ctx: VerilogParser.Init_valContext) =
            this@VerilogParserListener.exitInit_val(ctx)

        override suspend fun enterSequential_entry(ctx: VerilogParser.Sequential_entryContext) =
            this@VerilogParserListener.enterSequential_entry(ctx)

        override suspend fun exitSequential_entry(ctx: VerilogParser.Sequential_entryContext) =
            this@VerilogParserListener.exitSequential_entry(ctx)

        override suspend fun enterSeq_input_list(ctx: VerilogParser.Seq_input_listContext) =
            this@VerilogParserListener.enterSeq_input_list(ctx)

        override suspend fun exitSeq_input_list(ctx: VerilogParser.Seq_input_listContext) =
            this@VerilogParserListener.exitSeq_input_list(ctx)

        override suspend fun enterLevel_input_list(ctx: VerilogParser.Level_input_listContext) =
            this@VerilogParserListener.enterLevel_input_list(ctx)

        override suspend fun exitLevel_input_list(ctx: VerilogParser.Level_input_listContext) =
            this@VerilogParserListener.exitLevel_input_list(ctx)

        override suspend fun enterEdge_input_list(ctx: VerilogParser.Edge_input_listContext) =
            this@VerilogParserListener.enterEdge_input_list(ctx)

        override suspend fun exitEdge_input_list(ctx: VerilogParser.Edge_input_listContext) =
            this@VerilogParserListener.exitEdge_input_list(ctx)

        override suspend fun enterEdge_indicator(ctx: VerilogParser.Edge_indicatorContext) =
            this@VerilogParserListener.enterEdge_indicator(ctx)

        override suspend fun exitEdge_indicator(ctx: VerilogParser.Edge_indicatorContext) =
            this@VerilogParserListener.exitEdge_indicator(ctx)

        override suspend fun enterCurrent_state(ctx: VerilogParser.Current_stateContext) =
            this@VerilogParserListener.enterCurrent_state(ctx)

        override suspend fun exitCurrent_state(ctx: VerilogParser.Current_stateContext) =
            this@VerilogParserListener.exitCurrent_state(ctx)

        override suspend fun enterNext_state(ctx: VerilogParser.Next_stateContext) =
            this@VerilogParserListener.enterNext_state(ctx)

        override suspend fun exitNext_state(ctx: VerilogParser.Next_stateContext) =
            this@VerilogParserListener.exitNext_state(ctx)

        override suspend fun enterOutput_symbol(ctx: VerilogParser.Output_symbolContext) =
            this@VerilogParserListener.enterOutput_symbol(ctx)

        override suspend fun exitOutput_symbol(ctx: VerilogParser.Output_symbolContext) =
            this@VerilogParserListener.exitOutput_symbol(ctx)

        override suspend fun enterLevel_symbol(ctx: VerilogParser.Level_symbolContext) =
            this@VerilogParserListener.enterLevel_symbol(ctx)

        override suspend fun exitLevel_symbol(ctx: VerilogParser.Level_symbolContext) =
            this@VerilogParserListener.exitLevel_symbol(ctx)

        override suspend fun enterEdge_symbol(ctx: VerilogParser.Edge_symbolContext) =
            this@VerilogParserListener.enterEdge_symbol(ctx)

        override suspend fun exitEdge_symbol(ctx: VerilogParser.Edge_symbolContext) =
            this@VerilogParserListener.exitEdge_symbol(ctx)

        override suspend fun enterUdp_instantiation(ctx: VerilogParser.Udp_instantiationContext) =
            this@VerilogParserListener.enterUdp_instantiation(ctx)

        override suspend fun exitUdp_instantiation(ctx: VerilogParser.Udp_instantiationContext) =
            this@VerilogParserListener.exitUdp_instantiation(ctx)

        override suspend fun enterUdp_instance(ctx: VerilogParser.Udp_instanceContext) =
            this@VerilogParserListener.enterUdp_instance(ctx)

        override suspend fun exitUdp_instance(ctx: VerilogParser.Udp_instanceContext) =
            this@VerilogParserListener.exitUdp_instance(ctx)

        override suspend fun enterName_of_udp_instance(ctx: VerilogParser.Name_of_udp_instanceContext) =
            this@VerilogParserListener.enterName_of_udp_instance(ctx)

        override suspend fun exitName_of_udp_instance(ctx: VerilogParser.Name_of_udp_instanceContext) =
            this@VerilogParserListener.exitName_of_udp_instance(ctx)

        override suspend fun enterContinuous_assign(ctx: VerilogParser.Continuous_assignContext) =
            this@VerilogParserListener.enterContinuous_assign(ctx)

        override suspend fun exitContinuous_assign(ctx: VerilogParser.Continuous_assignContext) =
            this@VerilogParserListener.exitContinuous_assign(ctx)

        override suspend fun enterList_of_net_assignments(ctx: VerilogParser.List_of_net_assignmentsContext) =
            this@VerilogParserListener.enterList_of_net_assignments(ctx)

        override suspend fun exitList_of_net_assignments(ctx: VerilogParser.List_of_net_assignmentsContext) =
            this@VerilogParserListener.exitList_of_net_assignments(ctx)

        override suspend fun enterNet_assignment(ctx: VerilogParser.Net_assignmentContext) =
            this@VerilogParserListener.enterNet_assignment(ctx)

        override suspend fun exitNet_assignment(ctx: VerilogParser.Net_assignmentContext) =
            this@VerilogParserListener.exitNet_assignment(ctx)

        override suspend fun enterInitial_construct(ctx: VerilogParser.Initial_constructContext) =
            this@VerilogParserListener.enterInitial_construct(ctx)

        override suspend fun exitInitial_construct(ctx: VerilogParser.Initial_constructContext) =
            this@VerilogParserListener.exitInitial_construct(ctx)

        override suspend fun enterAlways_construct(ctx: VerilogParser.Always_constructContext) =
            this@VerilogParserListener.enterAlways_construct(ctx)

        override suspend fun exitAlways_construct(ctx: VerilogParser.Always_constructContext) =
            this@VerilogParserListener.exitAlways_construct(ctx)

        override suspend fun enterBlocking_assignment(ctx: VerilogParser.Blocking_assignmentContext) =
            this@VerilogParserListener.enterBlocking_assignment(ctx)

        override suspend fun exitBlocking_assignment(ctx: VerilogParser.Blocking_assignmentContext) =
            this@VerilogParserListener.exitBlocking_assignment(ctx)

        override suspend fun enterNonblocking_assignment(ctx: VerilogParser.Nonblocking_assignmentContext) =
            this@VerilogParserListener.enterNonblocking_assignment(ctx)

        override suspend fun exitNonblocking_assignment(ctx: VerilogParser.Nonblocking_assignmentContext) =
            this@VerilogParserListener.exitNonblocking_assignment(ctx)

        override suspend fun enterProcedural_continuous_assignments(ctx: VerilogParser.Procedural_continuous_assignmentsContext) =
            this@VerilogParserListener.enterProcedural_continuous_assignments(ctx)

        override suspend fun exitProcedural_continuous_assignments(ctx: VerilogParser.Procedural_continuous_assignmentsContext) =
            this@VerilogParserListener.exitProcedural_continuous_assignments(ctx)

        override suspend fun enterVariable_assignment(ctx: VerilogParser.Variable_assignmentContext) =
            this@VerilogParserListener.enterVariable_assignment(ctx)

        override suspend fun exitVariable_assignment(ctx: VerilogParser.Variable_assignmentContext) =
            this@VerilogParserListener.exitVariable_assignment(ctx)

        override suspend fun enterPar_block(ctx: VerilogParser.Par_blockContext) =
            this@VerilogParserListener.enterPar_block(ctx)

        override suspend fun exitPar_block(ctx: VerilogParser.Par_blockContext) =
            this@VerilogParserListener.exitPar_block(ctx)

        override suspend fun enterBlock_name(ctx: VerilogParser.Block_nameContext) =
            this@VerilogParserListener.enterBlock_name(ctx)

        override suspend fun exitBlock_name(ctx: VerilogParser.Block_nameContext) =
            this@VerilogParserListener.exitBlock_name(ctx)

        override suspend fun enterSeq_block(ctx: VerilogParser.Seq_blockContext) =
            this@VerilogParserListener.enterSeq_block(ctx)

        override suspend fun exitSeq_block(ctx: VerilogParser.Seq_blockContext) =
            this@VerilogParserListener.exitSeq_block(ctx)

        override suspend fun enterStatement(ctx: VerilogParser.StatementContext) =
            this@VerilogParserListener.enterStatement(ctx)

        override suspend fun exitStatement(ctx: VerilogParser.StatementContext) =
            this@VerilogParserListener.exitStatement(ctx)

        override suspend fun enterStatement_or_null(ctx: VerilogParser.Statement_or_nullContext) =
            this@VerilogParserListener.enterStatement_or_null(ctx)

        override suspend fun exitStatement_or_null(ctx: VerilogParser.Statement_or_nullContext) =
            this@VerilogParserListener.exitStatement_or_null(ctx)

        override suspend fun enterFunction_statement(ctx: VerilogParser.Function_statementContext) =
            this@VerilogParserListener.enterFunction_statement(ctx)

        override suspend fun exitFunction_statement(ctx: VerilogParser.Function_statementContext) =
            this@VerilogParserListener.exitFunction_statement(ctx)

        override suspend fun enterDelay_control(ctx: VerilogParser.Delay_controlContext) =
            this@VerilogParserListener.enterDelay_control(ctx)

        override suspend fun exitDelay_control(ctx: VerilogParser.Delay_controlContext) =
            this@VerilogParserListener.exitDelay_control(ctx)

        override suspend fun enterDelay_or_event_control(ctx: VerilogParser.Delay_or_event_controlContext) =
            this@VerilogParserListener.enterDelay_or_event_control(ctx)

        override suspend fun exitDelay_or_event_control(ctx: VerilogParser.Delay_or_event_controlContext) =
            this@VerilogParserListener.exitDelay_or_event_control(ctx)

        override suspend fun enterDisable_statement(ctx: VerilogParser.Disable_statementContext) =
            this@VerilogParserListener.enterDisable_statement(ctx)

        override suspend fun exitDisable_statement(ctx: VerilogParser.Disable_statementContext) =
            this@VerilogParserListener.exitDisable_statement(ctx)

        override suspend fun enterEvent_control(ctx: VerilogParser.Event_controlContext) =
            this@VerilogParserListener.enterEvent_control(ctx)

        override suspend fun exitEvent_control(ctx: VerilogParser.Event_controlContext) =
            this@VerilogParserListener.exitEvent_control(ctx)

        override suspend fun enterEvent_trigger(ctx: VerilogParser.Event_triggerContext) =
            this@VerilogParserListener.enterEvent_trigger(ctx)

        override suspend fun exitEvent_trigger(ctx: VerilogParser.Event_triggerContext) =
            this@VerilogParserListener.exitEvent_trigger(ctx)

        override suspend fun enterEvent_expression(ctx: VerilogParser.Event_expressionContext) =
            this@VerilogParserListener.enterEvent_expression(ctx)

        override suspend fun exitEvent_expression(ctx: VerilogParser.Event_expressionContext) =
            this@VerilogParserListener.exitEvent_expression(ctx)

        override suspend fun enterProcedural_timing_control(ctx: VerilogParser.Procedural_timing_controlContext) =
            this@VerilogParserListener.enterProcedural_timing_control(ctx)

        override suspend fun exitProcedural_timing_control(ctx: VerilogParser.Procedural_timing_controlContext) =
            this@VerilogParserListener.exitProcedural_timing_control(ctx)

        override suspend fun enterProcedural_timing_control_statement(ctx: VerilogParser.Procedural_timing_control_statementContext) =
            this@VerilogParserListener.enterProcedural_timing_control_statement(ctx)

        override suspend fun exitProcedural_timing_control_statement(ctx: VerilogParser.Procedural_timing_control_statementContext) =
            this@VerilogParserListener.exitProcedural_timing_control_statement(ctx)

        override suspend fun enterWait_statement(ctx: VerilogParser.Wait_statementContext) =
            this@VerilogParserListener.enterWait_statement(ctx)

        override suspend fun exitWait_statement(ctx: VerilogParser.Wait_statementContext) =
            this@VerilogParserListener.exitWait_statement(ctx)

        override suspend fun enterConditional_statement(ctx: VerilogParser.Conditional_statementContext) =
            this@VerilogParserListener.enterConditional_statement(ctx)

        override suspend fun exitConditional_statement(ctx: VerilogParser.Conditional_statementContext) =
            this@VerilogParserListener.exitConditional_statement(ctx)

        override suspend fun enterCase_statement(ctx: VerilogParser.Case_statementContext) =
            this@VerilogParserListener.enterCase_statement(ctx)

        override suspend fun exitCase_statement(ctx: VerilogParser.Case_statementContext) =
            this@VerilogParserListener.exitCase_statement(ctx)

        override suspend fun enterCase_item(ctx: VerilogParser.Case_itemContext) =
            this@VerilogParserListener.enterCase_item(ctx)

        override suspend fun exitCase_item(ctx: VerilogParser.Case_itemContext) =
            this@VerilogParserListener.exitCase_item(ctx)

        override suspend fun enterLoop_statement(ctx: VerilogParser.Loop_statementContext) =
            this@VerilogParserListener.enterLoop_statement(ctx)

        override suspend fun exitLoop_statement(ctx: VerilogParser.Loop_statementContext) =
            this@VerilogParserListener.exitLoop_statement(ctx)

        override suspend fun enterSystem_task_enable(ctx: VerilogParser.System_task_enableContext) =
            this@VerilogParserListener.enterSystem_task_enable(ctx)

        override suspend fun exitSystem_task_enable(ctx: VerilogParser.System_task_enableContext) =
            this@VerilogParserListener.exitSystem_task_enable(ctx)

        override suspend fun enterSys_task_en_port_list(ctx: VerilogParser.Sys_task_en_port_listContext) =
            this@VerilogParserListener.enterSys_task_en_port_list(ctx)

        override suspend fun exitSys_task_en_port_list(ctx: VerilogParser.Sys_task_en_port_listContext) =
            this@VerilogParserListener.exitSys_task_en_port_list(ctx)

        override suspend fun enterSys_task_en_port_item(ctx: VerilogParser.Sys_task_en_port_itemContext) =
            this@VerilogParserListener.enterSys_task_en_port_item(ctx)

        override suspend fun exitSys_task_en_port_item(ctx: VerilogParser.Sys_task_en_port_itemContext) =
            this@VerilogParserListener.exitSys_task_en_port_item(ctx)

        override suspend fun enterTask_enable(ctx: VerilogParser.Task_enableContext) =
            this@VerilogParserListener.enterTask_enable(ctx)

        override suspend fun exitTask_enable(ctx: VerilogParser.Task_enableContext) =
            this@VerilogParserListener.exitTask_enable(ctx)

        override suspend fun enterTask_en_port_list(ctx: VerilogParser.Task_en_port_listContext) =
            this@VerilogParserListener.enterTask_en_port_list(ctx)

        override suspend fun exitTask_en_port_list(ctx: VerilogParser.Task_en_port_listContext) =
            this@VerilogParserListener.exitTask_en_port_list(ctx)

        override suspend fun enterSpecify_block(ctx: VerilogParser.Specify_blockContext) =
            this@VerilogParserListener.enterSpecify_block(ctx)

        override suspend fun exitSpecify_block(ctx: VerilogParser.Specify_blockContext) =
            this@VerilogParserListener.exitSpecify_block(ctx)

        override suspend fun enterSpecify_item(ctx: VerilogParser.Specify_itemContext) =
            this@VerilogParserListener.enterSpecify_item(ctx)

        override suspend fun exitSpecify_item(ctx: VerilogParser.Specify_itemContext) =
            this@VerilogParserListener.exitSpecify_item(ctx)

        override suspend fun enterPulsestyle_declaration(ctx: VerilogParser.Pulsestyle_declarationContext) =
            this@VerilogParserListener.enterPulsestyle_declaration(ctx)

        override suspend fun exitPulsestyle_declaration(ctx: VerilogParser.Pulsestyle_declarationContext) =
            this@VerilogParserListener.exitPulsestyle_declaration(ctx)

        override suspend fun enterShowcancelled_declaration(ctx: VerilogParser.Showcancelled_declarationContext) =
            this@VerilogParserListener.enterShowcancelled_declaration(ctx)

        override suspend fun exitShowcancelled_declaration(ctx: VerilogParser.Showcancelled_declarationContext) =
            this@VerilogParserListener.exitShowcancelled_declaration(ctx)

        override suspend fun enterPath_declaration(ctx: VerilogParser.Path_declarationContext) =
            this@VerilogParserListener.enterPath_declaration(ctx)

        override suspend fun exitPath_declaration(ctx: VerilogParser.Path_declarationContext) =
            this@VerilogParserListener.exitPath_declaration(ctx)

        override suspend fun enterSimple_path_declaration(ctx: VerilogParser.Simple_path_declarationContext) =
            this@VerilogParserListener.enterSimple_path_declaration(ctx)

        override suspend fun exitSimple_path_declaration(ctx: VerilogParser.Simple_path_declarationContext) =
            this@VerilogParserListener.exitSimple_path_declaration(ctx)

        override suspend fun enterParallel_path_description(ctx: VerilogParser.Parallel_path_descriptionContext) =
            this@VerilogParserListener.enterParallel_path_description(ctx)

        override suspend fun exitParallel_path_description(ctx: VerilogParser.Parallel_path_descriptionContext) =
            this@VerilogParserListener.exitParallel_path_description(ctx)

        override suspend fun enterFull_path_description(ctx: VerilogParser.Full_path_descriptionContext) =
            this@VerilogParserListener.enterFull_path_description(ctx)

        override suspend fun exitFull_path_description(ctx: VerilogParser.Full_path_descriptionContext) =
            this@VerilogParserListener.exitFull_path_description(ctx)

        override suspend fun enterList_of_path_inputs(ctx: VerilogParser.List_of_path_inputsContext) =
            this@VerilogParserListener.enterList_of_path_inputs(ctx)

        override suspend fun exitList_of_path_inputs(ctx: VerilogParser.List_of_path_inputsContext) =
            this@VerilogParserListener.exitList_of_path_inputs(ctx)

        override suspend fun enterList_of_path_outputs(ctx: VerilogParser.List_of_path_outputsContext) =
            this@VerilogParserListener.enterList_of_path_outputs(ctx)

        override suspend fun exitList_of_path_outputs(ctx: VerilogParser.List_of_path_outputsContext) =
            this@VerilogParserListener.exitList_of_path_outputs(ctx)

        override suspend fun enterSpecify_input_terminal_descriptor(ctx: VerilogParser.Specify_input_terminal_descriptorContext) =
            this@VerilogParserListener.enterSpecify_input_terminal_descriptor(ctx)

        override suspend fun exitSpecify_input_terminal_descriptor(ctx: VerilogParser.Specify_input_terminal_descriptorContext) =
            this@VerilogParserListener.exitSpecify_input_terminal_descriptor(ctx)

        override suspend fun enterSpecify_output_terminal_descriptor(ctx: VerilogParser.Specify_output_terminal_descriptorContext) =
            this@VerilogParserListener.enterSpecify_output_terminal_descriptor(ctx)

        override suspend fun exitSpecify_output_terminal_descriptor(ctx: VerilogParser.Specify_output_terminal_descriptorContext) =
            this@VerilogParserListener.exitSpecify_output_terminal_descriptor(ctx)

        override suspend fun enterInput_identifier(ctx: VerilogParser.Input_identifierContext) =
            this@VerilogParserListener.enterInput_identifier(ctx)

        override suspend fun exitInput_identifier(ctx: VerilogParser.Input_identifierContext) =
            this@VerilogParserListener.exitInput_identifier(ctx)

        override suspend fun enterOutput_identifier(ctx: VerilogParser.Output_identifierContext) =
            this@VerilogParserListener.enterOutput_identifier(ctx)

        override suspend fun exitOutput_identifier(ctx: VerilogParser.Output_identifierContext) =
            this@VerilogParserListener.exitOutput_identifier(ctx)

        override suspend fun enterPath_delay_value(ctx: VerilogParser.Path_delay_valueContext) =
            this@VerilogParserListener.enterPath_delay_value(ctx)

        override suspend fun exitPath_delay_value(ctx: VerilogParser.Path_delay_valueContext) =
            this@VerilogParserListener.exitPath_delay_value(ctx)

        override suspend fun enterList_of_path_delay_expressions(ctx: VerilogParser.List_of_path_delay_expressionsContext) =
            this@VerilogParserListener.enterList_of_path_delay_expressions(ctx)

        override suspend fun exitList_of_path_delay_expressions(ctx: VerilogParser.List_of_path_delay_expressionsContext) =
            this@VerilogParserListener.exitList_of_path_delay_expressions(ctx)

        override suspend fun enterT_path_delay_expression(ctx: VerilogParser.T_path_delay_expressionContext) =
            this@VerilogParserListener.enterT_path_delay_expression(ctx)

        override suspend fun exitT_path_delay_expression(ctx: VerilogParser.T_path_delay_expressionContext) =
            this@VerilogParserListener.exitT_path_delay_expression(ctx)

        override suspend fun enterTrise_path_delay_expression(ctx: VerilogParser.Trise_path_delay_expressionContext) =
            this@VerilogParserListener.enterTrise_path_delay_expression(ctx)

        override suspend fun exitTrise_path_delay_expression(ctx: VerilogParser.Trise_path_delay_expressionContext) =
            this@VerilogParserListener.exitTrise_path_delay_expression(ctx)

        override suspend fun enterTfall_path_delay_expression(ctx: VerilogParser.Tfall_path_delay_expressionContext) =
            this@VerilogParserListener.enterTfall_path_delay_expression(ctx)

        override suspend fun exitTfall_path_delay_expression(ctx: VerilogParser.Tfall_path_delay_expressionContext) =
            this@VerilogParserListener.exitTfall_path_delay_expression(ctx)

        override suspend fun enterTz_path_delay_expression(ctx: VerilogParser.Tz_path_delay_expressionContext) =
            this@VerilogParserListener.enterTz_path_delay_expression(ctx)

        override suspend fun exitTz_path_delay_expression(ctx: VerilogParser.Tz_path_delay_expressionContext) =
            this@VerilogParserListener.exitTz_path_delay_expression(ctx)

        override suspend fun enterT01_path_delay_expression(ctx: VerilogParser.T01_path_delay_expressionContext) =
            this@VerilogParserListener.enterT01_path_delay_expression(ctx)

        override suspend fun exitT01_path_delay_expression(ctx: VerilogParser.T01_path_delay_expressionContext) =
            this@VerilogParserListener.exitT01_path_delay_expression(ctx)

        override suspend fun enterT10_path_delay_expression(ctx: VerilogParser.T10_path_delay_expressionContext) =
            this@VerilogParserListener.enterT10_path_delay_expression(ctx)

        override suspend fun exitT10_path_delay_expression(ctx: VerilogParser.T10_path_delay_expressionContext) =
            this@VerilogParserListener.exitT10_path_delay_expression(ctx)

        override suspend fun enterT0z_path_delay_expression(ctx: VerilogParser.T0z_path_delay_expressionContext) =
            this@VerilogParserListener.enterT0z_path_delay_expression(ctx)

        override suspend fun exitT0z_path_delay_expression(ctx: VerilogParser.T0z_path_delay_expressionContext) =
            this@VerilogParserListener.exitT0z_path_delay_expression(ctx)

        override suspend fun enterTz1_path_delay_expression(ctx: VerilogParser.Tz1_path_delay_expressionContext) =
            this@VerilogParserListener.enterTz1_path_delay_expression(ctx)

        override suspend fun exitTz1_path_delay_expression(ctx: VerilogParser.Tz1_path_delay_expressionContext) =
            this@VerilogParserListener.exitTz1_path_delay_expression(ctx)

        override suspend fun enterT1z_path_delay_expression(ctx: VerilogParser.T1z_path_delay_expressionContext) =
            this@VerilogParserListener.enterT1z_path_delay_expression(ctx)

        override suspend fun exitT1z_path_delay_expression(ctx: VerilogParser.T1z_path_delay_expressionContext) =
            this@VerilogParserListener.exitT1z_path_delay_expression(ctx)

        override suspend fun enterTz0_path_delay_expression(ctx: VerilogParser.Tz0_path_delay_expressionContext) =
            this@VerilogParserListener.enterTz0_path_delay_expression(ctx)

        override suspend fun exitTz0_path_delay_expression(ctx: VerilogParser.Tz0_path_delay_expressionContext) =
            this@VerilogParserListener.exitTz0_path_delay_expression(ctx)

        override suspend fun enterT0x_path_delay_expression(ctx: VerilogParser.T0x_path_delay_expressionContext) =
            this@VerilogParserListener.enterT0x_path_delay_expression(ctx)

        override suspend fun exitT0x_path_delay_expression(ctx: VerilogParser.T0x_path_delay_expressionContext) =
            this@VerilogParserListener.exitT0x_path_delay_expression(ctx)

        override suspend fun enterTx1_path_delay_expression(ctx: VerilogParser.Tx1_path_delay_expressionContext) =
            this@VerilogParserListener.enterTx1_path_delay_expression(ctx)

        override suspend fun exitTx1_path_delay_expression(ctx: VerilogParser.Tx1_path_delay_expressionContext) =
            this@VerilogParserListener.exitTx1_path_delay_expression(ctx)

        override suspend fun enterT1x_path_delay_expression(ctx: VerilogParser.T1x_path_delay_expressionContext) =
            this@VerilogParserListener.enterT1x_path_delay_expression(ctx)

        override suspend fun exitT1x_path_delay_expression(ctx: VerilogParser.T1x_path_delay_expressionContext) =
            this@VerilogParserListener.exitT1x_path_delay_expression(ctx)

        override suspend fun enterTx0_path_delay_expression(ctx: VerilogParser.Tx0_path_delay_expressionContext) =
            this@VerilogParserListener.enterTx0_path_delay_expression(ctx)

        override suspend fun exitTx0_path_delay_expression(ctx: VerilogParser.Tx0_path_delay_expressionContext) =
            this@VerilogParserListener.exitTx0_path_delay_expression(ctx)

        override suspend fun enterTxz_path_delay_expression(ctx: VerilogParser.Txz_path_delay_expressionContext) =
            this@VerilogParserListener.enterTxz_path_delay_expression(ctx)

        override suspend fun exitTxz_path_delay_expression(ctx: VerilogParser.Txz_path_delay_expressionContext) =
            this@VerilogParserListener.exitTxz_path_delay_expression(ctx)

        override suspend fun enterTzx_path_delay_expression(ctx: VerilogParser.Tzx_path_delay_expressionContext) =
            this@VerilogParserListener.enterTzx_path_delay_expression(ctx)

        override suspend fun exitTzx_path_delay_expression(ctx: VerilogParser.Tzx_path_delay_expressionContext) =
            this@VerilogParserListener.exitTzx_path_delay_expression(ctx)

        override suspend fun enterPath_delay_expression(ctx: VerilogParser.Path_delay_expressionContext) =
            this@VerilogParserListener.enterPath_delay_expression(ctx)

        override suspend fun exitPath_delay_expression(ctx: VerilogParser.Path_delay_expressionContext) =
            this@VerilogParserListener.exitPath_delay_expression(ctx)

        override suspend fun enterEdge_sensitive_path_declaration(ctx: VerilogParser.Edge_sensitive_path_declarationContext) =
            this@VerilogParserListener.enterEdge_sensitive_path_declaration(ctx)

        override suspend fun exitEdge_sensitive_path_declaration(ctx: VerilogParser.Edge_sensitive_path_declarationContext) =
            this@VerilogParserListener.exitEdge_sensitive_path_declaration(ctx)

        override suspend fun enterParallel_edge_sensitive_path_description(ctx: VerilogParser.Parallel_edge_sensitive_path_descriptionContext) =
            this@VerilogParserListener.enterParallel_edge_sensitive_path_description(ctx)

        override suspend fun exitParallel_edge_sensitive_path_description(ctx: VerilogParser.Parallel_edge_sensitive_path_descriptionContext) =
            this@VerilogParserListener.exitParallel_edge_sensitive_path_description(ctx)

        override suspend fun enterFull_edge_sensitive_path_description(ctx: VerilogParser.Full_edge_sensitive_path_descriptionContext) =
            this@VerilogParserListener.enterFull_edge_sensitive_path_description(ctx)

        override suspend fun exitFull_edge_sensitive_path_description(ctx: VerilogParser.Full_edge_sensitive_path_descriptionContext) =
            this@VerilogParserListener.exitFull_edge_sensitive_path_description(ctx)

        override suspend fun enterData_source_expression(ctx: VerilogParser.Data_source_expressionContext) =
            this@VerilogParserListener.enterData_source_expression(ctx)

        override suspend fun exitData_source_expression(ctx: VerilogParser.Data_source_expressionContext) =
            this@VerilogParserListener.exitData_source_expression(ctx)

        override suspend fun enterEdge_identifier(ctx: VerilogParser.Edge_identifierContext) =
            this@VerilogParserListener.enterEdge_identifier(ctx)

        override suspend fun exitEdge_identifier(ctx: VerilogParser.Edge_identifierContext) =
            this@VerilogParserListener.exitEdge_identifier(ctx)

        override suspend fun enterState_dependent_path_declaration(ctx: VerilogParser.State_dependent_path_declarationContext) =
            this@VerilogParserListener.enterState_dependent_path_declaration(ctx)

        override suspend fun exitState_dependent_path_declaration(ctx: VerilogParser.State_dependent_path_declarationContext) =
            this@VerilogParserListener.exitState_dependent_path_declaration(ctx)

        override suspend fun enterPolarity_operator(ctx: VerilogParser.Polarity_operatorContext) =
            this@VerilogParserListener.enterPolarity_operator(ctx)

        override suspend fun exitPolarity_operator(ctx: VerilogParser.Polarity_operatorContext) =
            this@VerilogParserListener.exitPolarity_operator(ctx)

        override suspend fun enterSystem_timing_check(ctx: VerilogParser.System_timing_checkContext) =
            this@VerilogParserListener.enterSystem_timing_check(ctx)

        override suspend fun exitSystem_timing_check(ctx: VerilogParser.System_timing_checkContext) =
            this@VerilogParserListener.exitSystem_timing_check(ctx)

        override suspend fun enterSetup_timing_check(ctx: VerilogParser.Setup_timing_checkContext) =
            this@VerilogParserListener.enterSetup_timing_check(ctx)

        override suspend fun exitSetup_timing_check(ctx: VerilogParser.Setup_timing_checkContext) =
            this@VerilogParserListener.exitSetup_timing_check(ctx)

        override suspend fun enterNotifier_opt(ctx: VerilogParser.Notifier_optContext) =
            this@VerilogParserListener.enterNotifier_opt(ctx)

        override suspend fun exitNotifier_opt(ctx: VerilogParser.Notifier_optContext) =
            this@VerilogParserListener.exitNotifier_opt(ctx)

        override suspend fun enterHold_timing_check(ctx: VerilogParser.Hold_timing_checkContext) =
            this@VerilogParserListener.enterHold_timing_check(ctx)

        override suspend fun exitHold_timing_check(ctx: VerilogParser.Hold_timing_checkContext) =
            this@VerilogParserListener.exitHold_timing_check(ctx)

        override suspend fun enterSetuphold_timing_check(ctx: VerilogParser.Setuphold_timing_checkContext) =
            this@VerilogParserListener.enterSetuphold_timing_check(ctx)

        override suspend fun exitSetuphold_timing_check(ctx: VerilogParser.Setuphold_timing_checkContext) =
            this@VerilogParserListener.exitSetuphold_timing_check(ctx)

        override suspend fun enterTiming_check_opt(ctx: VerilogParser.Timing_check_optContext) =
            this@VerilogParserListener.enterTiming_check_opt(ctx)

        override suspend fun exitTiming_check_opt(ctx: VerilogParser.Timing_check_optContext) =
            this@VerilogParserListener.exitTiming_check_opt(ctx)

        override suspend fun enterStamptime_cond_opt(ctx: VerilogParser.Stamptime_cond_optContext) =
            this@VerilogParserListener.enterStamptime_cond_opt(ctx)

        override suspend fun exitStamptime_cond_opt(ctx: VerilogParser.Stamptime_cond_optContext) =
            this@VerilogParserListener.exitStamptime_cond_opt(ctx)

        override suspend fun enterChecktime_cond_opt(ctx: VerilogParser.Checktime_cond_optContext) =
            this@VerilogParserListener.enterChecktime_cond_opt(ctx)

        override suspend fun exitChecktime_cond_opt(ctx: VerilogParser.Checktime_cond_optContext) =
            this@VerilogParserListener.exitChecktime_cond_opt(ctx)

        override suspend fun enterDelayed_ref_opt(ctx: VerilogParser.Delayed_ref_optContext) =
            this@VerilogParserListener.enterDelayed_ref_opt(ctx)

        override suspend fun exitDelayed_ref_opt(ctx: VerilogParser.Delayed_ref_optContext) =
            this@VerilogParserListener.exitDelayed_ref_opt(ctx)

        override suspend fun enterDelayed_data_opt(ctx: VerilogParser.Delayed_data_optContext) =
            this@VerilogParserListener.enterDelayed_data_opt(ctx)

        override suspend fun exitDelayed_data_opt(ctx: VerilogParser.Delayed_data_optContext) =
            this@VerilogParserListener.exitDelayed_data_opt(ctx)

        override suspend fun enterRecovery_timing_check(ctx: VerilogParser.Recovery_timing_checkContext) =
            this@VerilogParserListener.enterRecovery_timing_check(ctx)

        override suspend fun exitRecovery_timing_check(ctx: VerilogParser.Recovery_timing_checkContext) =
            this@VerilogParserListener.exitRecovery_timing_check(ctx)

        override suspend fun enterRemoval_timing_check(ctx: VerilogParser.Removal_timing_checkContext) =
            this@VerilogParserListener.enterRemoval_timing_check(ctx)

        override suspend fun exitRemoval_timing_check(ctx: VerilogParser.Removal_timing_checkContext) =
            this@VerilogParserListener.exitRemoval_timing_check(ctx)

        override suspend fun enterRecrem_timing_check(ctx: VerilogParser.Recrem_timing_checkContext) =
            this@VerilogParserListener.enterRecrem_timing_check(ctx)

        override suspend fun exitRecrem_timing_check(ctx: VerilogParser.Recrem_timing_checkContext) =
            this@VerilogParserListener.exitRecrem_timing_check(ctx)

        override suspend fun enterSkew_timing_check(ctx: VerilogParser.Skew_timing_checkContext) =
            this@VerilogParserListener.enterSkew_timing_check(ctx)

        override suspend fun exitSkew_timing_check(ctx: VerilogParser.Skew_timing_checkContext) =
            this@VerilogParserListener.exitSkew_timing_check(ctx)

        override suspend fun enterTimeskew_timing_check(ctx: VerilogParser.Timeskew_timing_checkContext) =
            this@VerilogParserListener.enterTimeskew_timing_check(ctx)

        override suspend fun exitTimeskew_timing_check(ctx: VerilogParser.Timeskew_timing_checkContext) =
            this@VerilogParserListener.exitTimeskew_timing_check(ctx)

        override suspend fun enterSkew_timing_check_opt(ctx: VerilogParser.Skew_timing_check_optContext) =
            this@VerilogParserListener.enterSkew_timing_check_opt(ctx)

        override suspend fun exitSkew_timing_check_opt(ctx: VerilogParser.Skew_timing_check_optContext) =
            this@VerilogParserListener.exitSkew_timing_check_opt(ctx)

        override suspend fun enterEvent_based_flag_opt(ctx: VerilogParser.Event_based_flag_optContext) =
            this@VerilogParserListener.enterEvent_based_flag_opt(ctx)

        override suspend fun exitEvent_based_flag_opt(ctx: VerilogParser.Event_based_flag_optContext) =
            this@VerilogParserListener.exitEvent_based_flag_opt(ctx)

        override suspend fun enterRemain_active_flag_opt(ctx: VerilogParser.Remain_active_flag_optContext) =
            this@VerilogParserListener.enterRemain_active_flag_opt(ctx)

        override suspend fun exitRemain_active_flag_opt(ctx: VerilogParser.Remain_active_flag_optContext) =
            this@VerilogParserListener.exitRemain_active_flag_opt(ctx)

        override suspend fun enterFullskew_timing_check(ctx: VerilogParser.Fullskew_timing_checkContext) =
            this@VerilogParserListener.enterFullskew_timing_check(ctx)

        override suspend fun exitFullskew_timing_check(ctx: VerilogParser.Fullskew_timing_checkContext) =
            this@VerilogParserListener.exitFullskew_timing_check(ctx)

        override suspend fun enterPeriod_timing_check(ctx: VerilogParser.Period_timing_checkContext) =
            this@VerilogParserListener.enterPeriod_timing_check(ctx)

        override suspend fun exitPeriod_timing_check(ctx: VerilogParser.Period_timing_checkContext) =
            this@VerilogParserListener.exitPeriod_timing_check(ctx)

        override suspend fun enterWidth_timing_check(ctx: VerilogParser.Width_timing_checkContext) =
            this@VerilogParserListener.enterWidth_timing_check(ctx)

        override suspend fun exitWidth_timing_check(ctx: VerilogParser.Width_timing_checkContext) =
            this@VerilogParserListener.exitWidth_timing_check(ctx)

        override suspend fun enterThreshold_opt(ctx: VerilogParser.Threshold_optContext) =
            this@VerilogParserListener.enterThreshold_opt(ctx)

        override suspend fun exitThreshold_opt(ctx: VerilogParser.Threshold_optContext) =
            this@VerilogParserListener.exitThreshold_opt(ctx)

        override suspend fun enterNochange_timing_check(ctx: VerilogParser.Nochange_timing_checkContext) =
            this@VerilogParserListener.enterNochange_timing_check(ctx)

        override suspend fun exitNochange_timing_check(ctx: VerilogParser.Nochange_timing_checkContext) =
            this@VerilogParserListener.exitNochange_timing_check(ctx)

        override suspend fun enterChecktime_condition(ctx: VerilogParser.Checktime_conditionContext) =
            this@VerilogParserListener.enterChecktime_condition(ctx)

        override suspend fun exitChecktime_condition(ctx: VerilogParser.Checktime_conditionContext) =
            this@VerilogParserListener.exitChecktime_condition(ctx)

        override suspend fun enterControlled_reference_event(ctx: VerilogParser.Controlled_reference_eventContext) =
            this@VerilogParserListener.enterControlled_reference_event(ctx)

        override suspend fun exitControlled_reference_event(ctx: VerilogParser.Controlled_reference_eventContext) =
            this@VerilogParserListener.exitControlled_reference_event(ctx)

        override suspend fun enterData_event(ctx: VerilogParser.Data_eventContext) =
            this@VerilogParserListener.enterData_event(ctx)

        override suspend fun exitData_event(ctx: VerilogParser.Data_eventContext) =
            this@VerilogParserListener.exitData_event(ctx)

        override suspend fun enterDelayed_data(ctx: VerilogParser.Delayed_dataContext) =
            this@VerilogParserListener.enterDelayed_data(ctx)

        override suspend fun exitDelayed_data(ctx: VerilogParser.Delayed_dataContext) =
            this@VerilogParserListener.exitDelayed_data(ctx)

        override suspend fun enterDelayed_reference(ctx: VerilogParser.Delayed_referenceContext) =
            this@VerilogParserListener.enterDelayed_reference(ctx)

        override suspend fun exitDelayed_reference(ctx: VerilogParser.Delayed_referenceContext) =
            this@VerilogParserListener.exitDelayed_reference(ctx)

        override suspend fun enterEnd_edge_offset(ctx: VerilogParser.End_edge_offsetContext) =
            this@VerilogParserListener.enterEnd_edge_offset(ctx)

        override suspend fun exitEnd_edge_offset(ctx: VerilogParser.End_edge_offsetContext) =
            this@VerilogParserListener.exitEnd_edge_offset(ctx)

        override suspend fun enterEvent_based_flag(ctx: VerilogParser.Event_based_flagContext) =
            this@VerilogParserListener.enterEvent_based_flag(ctx)

        override suspend fun exitEvent_based_flag(ctx: VerilogParser.Event_based_flagContext) =
            this@VerilogParserListener.exitEvent_based_flag(ctx)

        override suspend fun enterNotifier(ctx: VerilogParser.NotifierContext) =
            this@VerilogParserListener.enterNotifier(ctx)

        override suspend fun exitNotifier(ctx: VerilogParser.NotifierContext) =
            this@VerilogParserListener.exitNotifier(ctx)

        override suspend fun enterReference_event(ctx: VerilogParser.Reference_eventContext) =
            this@VerilogParserListener.enterReference_event(ctx)

        override suspend fun exitReference_event(ctx: VerilogParser.Reference_eventContext) =
            this@VerilogParserListener.exitReference_event(ctx)

        override suspend fun enterRemain_active_flag(ctx: VerilogParser.Remain_active_flagContext) =
            this@VerilogParserListener.enterRemain_active_flag(ctx)

        override suspend fun exitRemain_active_flag(ctx: VerilogParser.Remain_active_flagContext) =
            this@VerilogParserListener.exitRemain_active_flag(ctx)

        override suspend fun enterStamptime_condition(ctx: VerilogParser.Stamptime_conditionContext) =
            this@VerilogParserListener.enterStamptime_condition(ctx)

        override suspend fun exitStamptime_condition(ctx: VerilogParser.Stamptime_conditionContext) =
            this@VerilogParserListener.exitStamptime_condition(ctx)

        override suspend fun enterStart_edge_offset(ctx: VerilogParser.Start_edge_offsetContext) =
            this@VerilogParserListener.enterStart_edge_offset(ctx)

        override suspend fun exitStart_edge_offset(ctx: VerilogParser.Start_edge_offsetContext) =
            this@VerilogParserListener.exitStart_edge_offset(ctx)

        override suspend fun enterThreshold(ctx: VerilogParser.ThresholdContext) =
            this@VerilogParserListener.enterThreshold(ctx)

        override suspend fun exitThreshold(ctx: VerilogParser.ThresholdContext) =
            this@VerilogParserListener.exitThreshold(ctx)

        override suspend fun enterTiming_check_limit(ctx: VerilogParser.Timing_check_limitContext) =
            this@VerilogParserListener.enterTiming_check_limit(ctx)

        override suspend fun exitTiming_check_limit(ctx: VerilogParser.Timing_check_limitContext) =
            this@VerilogParserListener.exitTiming_check_limit(ctx)

        override suspend fun enterTiming_check_event(ctx: VerilogParser.Timing_check_eventContext) =
            this@VerilogParserListener.enterTiming_check_event(ctx)

        override suspend fun exitTiming_check_event(ctx: VerilogParser.Timing_check_eventContext) =
            this@VerilogParserListener.exitTiming_check_event(ctx)

        override suspend fun enterControlled_timing_check_event(ctx: VerilogParser.Controlled_timing_check_eventContext) =
            this@VerilogParserListener.enterControlled_timing_check_event(ctx)

        override suspend fun exitControlled_timing_check_event(ctx: VerilogParser.Controlled_timing_check_eventContext) =
            this@VerilogParserListener.exitControlled_timing_check_event(ctx)

        override suspend fun enterTiming_check_event_control(ctx: VerilogParser.Timing_check_event_controlContext) =
            this@VerilogParserListener.enterTiming_check_event_control(ctx)

        override suspend fun exitTiming_check_event_control(ctx: VerilogParser.Timing_check_event_controlContext) =
            this@VerilogParserListener.exitTiming_check_event_control(ctx)

        override suspend fun enterSpecify_terminal_descriptor(ctx: VerilogParser.Specify_terminal_descriptorContext) =
            this@VerilogParserListener.enterSpecify_terminal_descriptor(ctx)

        override suspend fun exitSpecify_terminal_descriptor(ctx: VerilogParser.Specify_terminal_descriptorContext) =
            this@VerilogParserListener.exitSpecify_terminal_descriptor(ctx)

        override suspend fun enterEdge_control_specifier(ctx: VerilogParser.Edge_control_specifierContext) =
            this@VerilogParserListener.enterEdge_control_specifier(ctx)

        override suspend fun exitEdge_control_specifier(ctx: VerilogParser.Edge_control_specifierContext) =
            this@VerilogParserListener.exitEdge_control_specifier(ctx)

        override suspend fun enterEdge_descriptor(ctx: VerilogParser.Edge_descriptorContext) =
            this@VerilogParserListener.enterEdge_descriptor(ctx)

        override suspend fun exitEdge_descriptor(ctx: VerilogParser.Edge_descriptorContext) =
            this@VerilogParserListener.exitEdge_descriptor(ctx)

        override suspend fun enterTiming_check_condition(ctx: VerilogParser.Timing_check_conditionContext) =
            this@VerilogParserListener.enterTiming_check_condition(ctx)

        override suspend fun exitTiming_check_condition(ctx: VerilogParser.Timing_check_conditionContext) =
            this@VerilogParserListener.exitTiming_check_condition(ctx)

        override suspend fun enterScalar_timing_check_condition(ctx: VerilogParser.Scalar_timing_check_conditionContext) =
            this@VerilogParserListener.enterScalar_timing_check_condition(ctx)

        override suspend fun exitScalar_timing_check_condition(ctx: VerilogParser.Scalar_timing_check_conditionContext) =
            this@VerilogParserListener.exitScalar_timing_check_condition(ctx)

        override suspend fun enterScalar_constant(ctx: VerilogParser.Scalar_constantContext) =
            this@VerilogParserListener.enterScalar_constant(ctx)

        override suspend fun exitScalar_constant(ctx: VerilogParser.Scalar_constantContext) =
            this@VerilogParserListener.exitScalar_constant(ctx)

        override suspend fun enterConcatenation(ctx: VerilogParser.ConcatenationContext) =
            this@VerilogParserListener.enterConcatenation(ctx)

        override suspend fun exitConcatenation(ctx: VerilogParser.ConcatenationContext) =
            this@VerilogParserListener.exitConcatenation(ctx)

        override suspend fun enterConstant_concatenation(ctx: VerilogParser.Constant_concatenationContext) =
            this@VerilogParserListener.enterConstant_concatenation(ctx)

        override suspend fun exitConstant_concatenation(ctx: VerilogParser.Constant_concatenationContext) =
            this@VerilogParserListener.exitConstant_concatenation(ctx)

        override suspend fun enterConstant_multiple_concatenation(ctx: VerilogParser.Constant_multiple_concatenationContext) =
            this@VerilogParserListener.enterConstant_multiple_concatenation(ctx)

        override suspend fun exitConstant_multiple_concatenation(ctx: VerilogParser.Constant_multiple_concatenationContext) =
            this@VerilogParserListener.exitConstant_multiple_concatenation(ctx)

        override suspend fun enterModule_path_concatenation(ctx: VerilogParser.Module_path_concatenationContext) =
            this@VerilogParserListener.enterModule_path_concatenation(ctx)

        override suspend fun exitModule_path_concatenation(ctx: VerilogParser.Module_path_concatenationContext) =
            this@VerilogParserListener.exitModule_path_concatenation(ctx)

        override suspend fun enterModule_path_multiple_concatenation(ctx: VerilogParser.Module_path_multiple_concatenationContext) =
            this@VerilogParserListener.enterModule_path_multiple_concatenation(ctx)

        override suspend fun exitModule_path_multiple_concatenation(ctx: VerilogParser.Module_path_multiple_concatenationContext) =
            this@VerilogParserListener.exitModule_path_multiple_concatenation(ctx)

        override suspend fun enterMultiple_concatenation(ctx: VerilogParser.Multiple_concatenationContext) =
            this@VerilogParserListener.enterMultiple_concatenation(ctx)

        override suspend fun exitMultiple_concatenation(ctx: VerilogParser.Multiple_concatenationContext) =
            this@VerilogParserListener.exitMultiple_concatenation(ctx)

        override suspend fun enterConstant_function_call(ctx: VerilogParser.Constant_function_callContext) =
            this@VerilogParserListener.enterConstant_function_call(ctx)

        override suspend fun exitConstant_function_call(ctx: VerilogParser.Constant_function_callContext) =
            this@VerilogParserListener.exitConstant_function_call(ctx)

        override suspend fun enterConstant_system_function_call(ctx: VerilogParser.Constant_system_function_callContext) =
            this@VerilogParserListener.enterConstant_system_function_call(ctx)

        override suspend fun exitConstant_system_function_call(ctx: VerilogParser.Constant_system_function_callContext) =
            this@VerilogParserListener.exitConstant_system_function_call(ctx)

        override suspend fun enterFunction_call(ctx: VerilogParser.Function_callContext) =
            this@VerilogParserListener.enterFunction_call(ctx)

        override suspend fun exitFunction_call(ctx: VerilogParser.Function_callContext) =
            this@VerilogParserListener.exitFunction_call(ctx)

        override suspend fun enterSystem_function_call(ctx: VerilogParser.System_function_callContext) =
            this@VerilogParserListener.enterSystem_function_call(ctx)

        override suspend fun exitSystem_function_call(ctx: VerilogParser.System_function_callContext) =
            this@VerilogParserListener.exitSystem_function_call(ctx)

        override suspend fun enterSys_func_call_port_list(ctx: VerilogParser.Sys_func_call_port_listContext) =
            this@VerilogParserListener.enterSys_func_call_port_list(ctx)

        override suspend fun exitSys_func_call_port_list(ctx: VerilogParser.Sys_func_call_port_listContext) =
            this@VerilogParserListener.exitSys_func_call_port_list(ctx)

        override suspend fun enterBase_expression(ctx: VerilogParser.Base_expressionContext) =
            this@VerilogParserListener.enterBase_expression(ctx)

        override suspend fun exitBase_expression(ctx: VerilogParser.Base_expressionContext) =
            this@VerilogParserListener.exitBase_expression(ctx)

        override suspend fun enterConstant_base_expression(ctx: VerilogParser.Constant_base_expressionContext) =
            this@VerilogParserListener.enterConstant_base_expression(ctx)

        override suspend fun exitConstant_base_expression(ctx: VerilogParser.Constant_base_expressionContext) =
            this@VerilogParserListener.exitConstant_base_expression(ctx)

        override suspend fun enterConstant_expression(ctx: VerilogParser.Constant_expressionContext) =
            this@VerilogParserListener.enterConstant_expression(ctx)

        override suspend fun exitConstant_expression(ctx: VerilogParser.Constant_expressionContext) =
            this@VerilogParserListener.exitConstant_expression(ctx)

        override suspend fun enterConstant_mintypmax_expression(ctx: VerilogParser.Constant_mintypmax_expressionContext) =
            this@VerilogParserListener.enterConstant_mintypmax_expression(ctx)

        override suspend fun exitConstant_mintypmax_expression(ctx: VerilogParser.Constant_mintypmax_expressionContext) =
            this@VerilogParserListener.exitConstant_mintypmax_expression(ctx)

        override suspend fun enterConstant_range_expression(ctx: VerilogParser.Constant_range_expressionContext) =
            this@VerilogParserListener.enterConstant_range_expression(ctx)

        override suspend fun exitConstant_range_expression(ctx: VerilogParser.Constant_range_expressionContext) =
            this@VerilogParserListener.exitConstant_range_expression(ctx)

        override suspend fun enterDimension_constant_expression(ctx: VerilogParser.Dimension_constant_expressionContext) =
            this@VerilogParserListener.enterDimension_constant_expression(ctx)

        override suspend fun exitDimension_constant_expression(ctx: VerilogParser.Dimension_constant_expressionContext) =
            this@VerilogParserListener.exitDimension_constant_expression(ctx)

        override suspend fun enterExpression(ctx: VerilogParser.ExpressionContext) =
            this@VerilogParserListener.enterExpression(ctx)

        override suspend fun exitExpression(ctx: VerilogParser.ExpressionContext) =
            this@VerilogParserListener.exitExpression(ctx)

        override suspend fun enterLsb_constant_expression(ctx: VerilogParser.Lsb_constant_expressionContext) =
            this@VerilogParserListener.enterLsb_constant_expression(ctx)

        override suspend fun exitLsb_constant_expression(ctx: VerilogParser.Lsb_constant_expressionContext) =
            this@VerilogParserListener.exitLsb_constant_expression(ctx)

        override suspend fun enterMintypmax_expression(ctx: VerilogParser.Mintypmax_expressionContext) =
            this@VerilogParserListener.enterMintypmax_expression(ctx)

        override suspend fun exitMintypmax_expression(ctx: VerilogParser.Mintypmax_expressionContext) =
            this@VerilogParserListener.exitMintypmax_expression(ctx)

        override suspend fun enterModule_path_expression(ctx: VerilogParser.Module_path_expressionContext) =
            this@VerilogParserListener.enterModule_path_expression(ctx)

        override suspend fun exitModule_path_expression(ctx: VerilogParser.Module_path_expressionContext) =
            this@VerilogParserListener.exitModule_path_expression(ctx)

        override suspend fun enterModule_path_mintypmax_expression(ctx: VerilogParser.Module_path_mintypmax_expressionContext) =
            this@VerilogParserListener.enterModule_path_mintypmax_expression(ctx)

        override suspend fun exitModule_path_mintypmax_expression(ctx: VerilogParser.Module_path_mintypmax_expressionContext) =
            this@VerilogParserListener.exitModule_path_mintypmax_expression(ctx)

        override suspend fun enterMsb_constant_expression(ctx: VerilogParser.Msb_constant_expressionContext) =
            this@VerilogParserListener.enterMsb_constant_expression(ctx)

        override suspend fun exitMsb_constant_expression(ctx: VerilogParser.Msb_constant_expressionContext) =
            this@VerilogParserListener.exitMsb_constant_expression(ctx)

        override suspend fun enterRange_expression(ctx: VerilogParser.Range_expressionContext) =
            this@VerilogParserListener.enterRange_expression(ctx)

        override suspend fun exitRange_expression(ctx: VerilogParser.Range_expressionContext) =
            this@VerilogParserListener.exitRange_expression(ctx)

        override suspend fun enterWidth_constant_expression(ctx: VerilogParser.Width_constant_expressionContext) =
            this@VerilogParserListener.enterWidth_constant_expression(ctx)

        override suspend fun exitWidth_constant_expression(ctx: VerilogParser.Width_constant_expressionContext) =
            this@VerilogParserListener.exitWidth_constant_expression(ctx)

        override suspend fun enterConstant_primary(ctx: VerilogParser.Constant_primaryContext) =
            this@VerilogParserListener.enterConstant_primary(ctx)

        override suspend fun exitConstant_primary(ctx: VerilogParser.Constant_primaryContext) =
            this@VerilogParserListener.exitConstant_primary(ctx)

        override suspend fun enterModule_path_primary(ctx: VerilogParser.Module_path_primaryContext) =
            this@VerilogParserListener.enterModule_path_primary(ctx)

        override suspend fun exitModule_path_primary(ctx: VerilogParser.Module_path_primaryContext) =
            this@VerilogParserListener.exitModule_path_primary(ctx)

        override suspend fun enterPrimary(ctx: VerilogParser.PrimaryContext) =
            this@VerilogParserListener.enterPrimary(ctx)

        override suspend fun exitPrimary(ctx: VerilogParser.PrimaryContext) =
            this@VerilogParserListener.exitPrimary(ctx)

        override suspend fun enterSelect_(ctx: VerilogParser.Select_Context) =
            this@VerilogParserListener.enterSelect_(ctx)

        override suspend fun exitSelect_(ctx: VerilogParser.Select_Context) =
            this@VerilogParserListener.exitSelect_(ctx)

        override suspend fun enterBit_select(ctx: VerilogParser.Bit_selectContext) =
            this@VerilogParserListener.enterBit_select(ctx)

        override suspend fun exitBit_select(ctx: VerilogParser.Bit_selectContext) =
            this@VerilogParserListener.exitBit_select(ctx)

        override suspend fun enterNet_lvalue(ctx: VerilogParser.Net_lvalueContext) =
            this@VerilogParserListener.enterNet_lvalue(ctx)

        override suspend fun exitNet_lvalue(ctx: VerilogParser.Net_lvalueContext) =
            this@VerilogParserListener.exitNet_lvalue(ctx)

        override suspend fun enterConst_select(ctx: VerilogParser.Const_selectContext) =
            this@VerilogParserListener.enterConst_select(ctx)

        override suspend fun exitConst_select(ctx: VerilogParser.Const_selectContext) =
            this@VerilogParserListener.exitConst_select(ctx)

        override suspend fun enterConst_bit_select(ctx: VerilogParser.Const_bit_selectContext) =
            this@VerilogParserListener.enterConst_bit_select(ctx)

        override suspend fun exitConst_bit_select(ctx: VerilogParser.Const_bit_selectContext) =
            this@VerilogParserListener.exitConst_bit_select(ctx)

        override suspend fun enterVariable_lvalue(ctx: VerilogParser.Variable_lvalueContext) =
            this@VerilogParserListener.enterVariable_lvalue(ctx)

        override suspend fun exitVariable_lvalue(ctx: VerilogParser.Variable_lvalueContext) =
            this@VerilogParserListener.exitVariable_lvalue(ctx)

        override suspend fun enterUnary_operator(ctx: VerilogParser.Unary_operatorContext) =
            this@VerilogParserListener.enterUnary_operator(ctx)

        override suspend fun exitUnary_operator(ctx: VerilogParser.Unary_operatorContext) =
            this@VerilogParserListener.exitUnary_operator(ctx)

        override suspend fun enterUnary_module_path_operator(ctx: VerilogParser.Unary_module_path_operatorContext) =
            this@VerilogParserListener.enterUnary_module_path_operator(ctx)

        override suspend fun exitUnary_module_path_operator(ctx: VerilogParser.Unary_module_path_operatorContext) =
            this@VerilogParserListener.exitUnary_module_path_operator(ctx)

        override suspend fun enterNumber(ctx: VerilogParser.NumberContext) = this@VerilogParserListener.enterNumber(ctx)
        override suspend fun exitNumber(ctx: VerilogParser.NumberContext) = this@VerilogParserListener.exitNumber(ctx)

        override suspend fun enterReal_number(ctx: VerilogParser.Real_numberContext) =
            this@VerilogParserListener.enterReal_number(ctx)

        override suspend fun exitReal_number(ctx: VerilogParser.Real_numberContext) =
            this@VerilogParserListener.exitReal_number(ctx)

        override suspend fun enterDecimal_number(ctx: VerilogParser.Decimal_numberContext) =
            this@VerilogParserListener.enterDecimal_number(ctx)

        override suspend fun exitDecimal_number(ctx: VerilogParser.Decimal_numberContext) =
            this@VerilogParserListener.exitDecimal_number(ctx)

        override suspend fun enterBinary_number(ctx: VerilogParser.Binary_numberContext) =
            this@VerilogParserListener.enterBinary_number(ctx)

        override suspend fun exitBinary_number(ctx: VerilogParser.Binary_numberContext) =
            this@VerilogParserListener.exitBinary_number(ctx)

        override suspend fun enterOctal_number(ctx: VerilogParser.Octal_numberContext) =
            this@VerilogParserListener.enterOctal_number(ctx)

        override suspend fun exitOctal_number(ctx: VerilogParser.Octal_numberContext) =
            this@VerilogParserListener.exitOctal_number(ctx)

        override suspend fun enterHex_number(ctx: VerilogParser.Hex_numberContext) =
            this@VerilogParserListener.enterHex_number(ctx)

        override suspend fun exitHex_number(ctx: VerilogParser.Hex_numberContext) =
            this@VerilogParserListener.exitHex_number(ctx)

        override suspend fun enterSize(ctx: VerilogParser.SizeContext) = this@VerilogParserListener.enterSize(ctx)
        override suspend fun exitSize(ctx: VerilogParser.SizeContext) = this@VerilogParserListener.exitSize(ctx)

        override suspend fun enterFixed_point_number(ctx: VerilogParser.Fixed_point_numberContext) =
            this@VerilogParserListener.enterFixed_point_number(ctx)

        override suspend fun exitFixed_point_number(ctx: VerilogParser.Fixed_point_numberContext) =
            this@VerilogParserListener.exitFixed_point_number(ctx)

        override suspend fun enterExponential_number(ctx: VerilogParser.Exponential_numberContext) =
            this@VerilogParserListener.enterExponential_number(ctx)

        override suspend fun exitExponential_number(ctx: VerilogParser.Exponential_numberContext) =
            this@VerilogParserListener.exitExponential_number(ctx)

        override suspend fun enterUnsigned_number(ctx: VerilogParser.Unsigned_numberContext) =
            this@VerilogParserListener.enterUnsigned_number(ctx)

        override suspend fun exitUnsigned_number(ctx: VerilogParser.Unsigned_numberContext) =
            this@VerilogParserListener.exitUnsigned_number(ctx)

        override suspend fun enterDecimal_value(ctx: VerilogParser.Decimal_valueContext) =
            this@VerilogParserListener.enterDecimal_value(ctx)

        override suspend fun exitDecimal_value(ctx: VerilogParser.Decimal_valueContext) =
            this@VerilogParserListener.exitDecimal_value(ctx)

        override suspend fun enterBinary_value(ctx: VerilogParser.Binary_valueContext) =
            this@VerilogParserListener.enterBinary_value(ctx)

        override suspend fun exitBinary_value(ctx: VerilogParser.Binary_valueContext) =
            this@VerilogParserListener.exitBinary_value(ctx)

        override suspend fun enterOctal_value(ctx: VerilogParser.Octal_valueContext) =
            this@VerilogParserListener.enterOctal_value(ctx)

        override suspend fun exitOctal_value(ctx: VerilogParser.Octal_valueContext) =
            this@VerilogParserListener.exitOctal_value(ctx)

        override suspend fun enterHex_value(ctx: VerilogParser.Hex_valueContext) =
            this@VerilogParserListener.enterHex_value(ctx)

        override suspend fun exitHex_value(ctx: VerilogParser.Hex_valueContext) =
            this@VerilogParserListener.exitHex_value(ctx)

        override suspend fun enterDecimal_base(ctx: VerilogParser.Decimal_baseContext) =
            this@VerilogParserListener.enterDecimal_base(ctx)

        override suspend fun exitDecimal_base(ctx: VerilogParser.Decimal_baseContext) =
            this@VerilogParserListener.exitDecimal_base(ctx)

        override suspend fun enterBinary_base(ctx: VerilogParser.Binary_baseContext) =
            this@VerilogParserListener.enterBinary_base(ctx)

        override suspend fun exitBinary_base(ctx: VerilogParser.Binary_baseContext) =
            this@VerilogParserListener.exitBinary_base(ctx)

        override suspend fun enterOctal_base(ctx: VerilogParser.Octal_baseContext) =
            this@VerilogParserListener.enterOctal_base(ctx)

        override suspend fun exitOctal_base(ctx: VerilogParser.Octal_baseContext) =
            this@VerilogParserListener.exitOctal_base(ctx)

        override suspend fun enterHex_base(ctx: VerilogParser.Hex_baseContext) =
            this@VerilogParserListener.enterHex_base(ctx)

        override suspend fun exitHex_base(ctx: VerilogParser.Hex_baseContext) =
            this@VerilogParserListener.exitHex_base(ctx)

        override suspend fun enterString_(ctx: VerilogParser.String_Context) =
            this@VerilogParserListener.enterString_(ctx)

        override suspend fun exitString_(ctx: VerilogParser.String_Context) =
            this@VerilogParserListener.exitString_(ctx)

        override suspend fun enterAttribute_instance(ctx: VerilogParser.Attribute_instanceContext) =
            this@VerilogParserListener.enterAttribute_instance(ctx)

        override suspend fun exitAttribute_instance(ctx: VerilogParser.Attribute_instanceContext) =
            this@VerilogParserListener.exitAttribute_instance(ctx)

        override suspend fun enterAttr_spec(ctx: VerilogParser.Attr_specContext) =
            this@VerilogParserListener.enterAttr_spec(ctx)

        override suspend fun exitAttr_spec(ctx: VerilogParser.Attr_specContext) =
            this@VerilogParserListener.exitAttr_spec(ctx)

        override suspend fun enterAttr_name(ctx: VerilogParser.Attr_nameContext) =
            this@VerilogParserListener.enterAttr_name(ctx)

        override suspend fun exitAttr_name(ctx: VerilogParser.Attr_nameContext) =
            this@VerilogParserListener.exitAttr_name(ctx)

        override suspend fun enterBlock_identifier(ctx: VerilogParser.Block_identifierContext) =
            this@VerilogParserListener.enterBlock_identifier(ctx)

        override suspend fun exitBlock_identifier(ctx: VerilogParser.Block_identifierContext) =
            this@VerilogParserListener.exitBlock_identifier(ctx)

        override suspend fun enterCell_identifier(ctx: VerilogParser.Cell_identifierContext) =
            this@VerilogParserListener.enterCell_identifier(ctx)

        override suspend fun exitCell_identifier(ctx: VerilogParser.Cell_identifierContext) =
            this@VerilogParserListener.exitCell_identifier(ctx)

        override suspend fun enterConfig_identifier(ctx: VerilogParser.Config_identifierContext) =
            this@VerilogParserListener.enterConfig_identifier(ctx)

        override suspend fun exitConfig_identifier(ctx: VerilogParser.Config_identifierContext) =
            this@VerilogParserListener.exitConfig_identifier(ctx)

        override suspend fun enterEscaped_identifier(ctx: VerilogParser.Escaped_identifierContext) =
            this@VerilogParserListener.enterEscaped_identifier(ctx)

        override suspend fun exitEscaped_identifier(ctx: VerilogParser.Escaped_identifierContext) =
            this@VerilogParserListener.exitEscaped_identifier(ctx)

        override suspend fun enterEvent_identifier(ctx: VerilogParser.Event_identifierContext) =
            this@VerilogParserListener.enterEvent_identifier(ctx)

        override suspend fun exitEvent_identifier(ctx: VerilogParser.Event_identifierContext) =
            this@VerilogParserListener.exitEvent_identifier(ctx)

        override suspend fun enterFunction_identifier(ctx: VerilogParser.Function_identifierContext) =
            this@VerilogParserListener.enterFunction_identifier(ctx)

        override suspend fun exitFunction_identifier(ctx: VerilogParser.Function_identifierContext) =
            this@VerilogParserListener.exitFunction_identifier(ctx)

        override suspend fun enterGate_instance_identifier(ctx: VerilogParser.Gate_instance_identifierContext) =
            this@VerilogParserListener.enterGate_instance_identifier(ctx)

        override suspend fun exitGate_instance_identifier(ctx: VerilogParser.Gate_instance_identifierContext) =
            this@VerilogParserListener.exitGate_instance_identifier(ctx)

        override suspend fun enterGenerate_block_identifier(ctx: VerilogParser.Generate_block_identifierContext) =
            this@VerilogParserListener.enterGenerate_block_identifier(ctx)

        override suspend fun exitGenerate_block_identifier(ctx: VerilogParser.Generate_block_identifierContext) =
            this@VerilogParserListener.exitGenerate_block_identifier(ctx)

        override suspend fun enterGenvar_identifier(ctx: VerilogParser.Genvar_identifierContext) =
            this@VerilogParserListener.enterGenvar_identifier(ctx)

        override suspend fun exitGenvar_identifier(ctx: VerilogParser.Genvar_identifierContext) =
            this@VerilogParserListener.exitGenvar_identifier(ctx)

        override suspend fun enterHierarchical_identifier(ctx: VerilogParser.Hierarchical_identifierContext) =
            this@VerilogParserListener.enterHierarchical_identifier(ctx)

        override suspend fun exitHierarchical_identifier(ctx: VerilogParser.Hierarchical_identifierContext) =
            this@VerilogParserListener.exitHierarchical_identifier(ctx)

        override suspend fun enterHier_ref(ctx: VerilogParser.Hier_refContext) =
            this@VerilogParserListener.enterHier_ref(ctx)

        override suspend fun exitHier_ref(ctx: VerilogParser.Hier_refContext) =
            this@VerilogParserListener.exitHier_ref(ctx)

        override suspend fun enterIdentifier(ctx: VerilogParser.IdentifierContext) =
            this@VerilogParserListener.enterIdentifier(ctx)

        override suspend fun exitIdentifier(ctx: VerilogParser.IdentifierContext) =
            this@VerilogParserListener.exitIdentifier(ctx)

        override suspend fun enterInput_port_identifier(ctx: VerilogParser.Input_port_identifierContext) =
            this@VerilogParserListener.enterInput_port_identifier(ctx)

        override suspend fun exitInput_port_identifier(ctx: VerilogParser.Input_port_identifierContext) =
            this@VerilogParserListener.exitInput_port_identifier(ctx)

        override suspend fun enterInstance_identifier(ctx: VerilogParser.Instance_identifierContext) =
            this@VerilogParserListener.enterInstance_identifier(ctx)

        override suspend fun exitInstance_identifier(ctx: VerilogParser.Instance_identifierContext) =
            this@VerilogParserListener.exitInstance_identifier(ctx)

        override suspend fun enterLibrary_identifier(ctx: VerilogParser.Library_identifierContext) =
            this@VerilogParserListener.enterLibrary_identifier(ctx)

        override suspend fun exitLibrary_identifier(ctx: VerilogParser.Library_identifierContext) =
            this@VerilogParserListener.exitLibrary_identifier(ctx)

        override suspend fun enterModule_identifier(ctx: VerilogParser.Module_identifierContext) =
            this@VerilogParserListener.enterModule_identifier(ctx)

        override suspend fun exitModule_identifier(ctx: VerilogParser.Module_identifierContext) =
            this@VerilogParserListener.exitModule_identifier(ctx)

        override suspend fun enterModule_instance_identifier(ctx: VerilogParser.Module_instance_identifierContext) =
            this@VerilogParserListener.enterModule_instance_identifier(ctx)

        override suspend fun exitModule_instance_identifier(ctx: VerilogParser.Module_instance_identifierContext) =
            this@VerilogParserListener.exitModule_instance_identifier(ctx)

        override suspend fun enterNet_identifier(ctx: VerilogParser.Net_identifierContext) =
            this@VerilogParserListener.enterNet_identifier(ctx)

        override suspend fun exitNet_identifier(ctx: VerilogParser.Net_identifierContext) =
            this@VerilogParserListener.exitNet_identifier(ctx)

        override suspend fun enterOutput_port_identifier(ctx: VerilogParser.Output_port_identifierContext) =
            this@VerilogParserListener.enterOutput_port_identifier(ctx)

        override suspend fun exitOutput_port_identifier(ctx: VerilogParser.Output_port_identifierContext) =
            this@VerilogParserListener.exitOutput_port_identifier(ctx)

        override suspend fun enterParameter_identifier(ctx: VerilogParser.Parameter_identifierContext) =
            this@VerilogParserListener.enterParameter_identifier(ctx)

        override suspend fun exitParameter_identifier(ctx: VerilogParser.Parameter_identifierContext) =
            this@VerilogParserListener.exitParameter_identifier(ctx)

        override suspend fun enterPort_identifier(ctx: VerilogParser.Port_identifierContext) =
            this@VerilogParserListener.enterPort_identifier(ctx)

        override suspend fun exitPort_identifier(ctx: VerilogParser.Port_identifierContext) =
            this@VerilogParserListener.exitPort_identifier(ctx)

        override suspend fun enterReal_identifier(ctx: VerilogParser.Real_identifierContext) =
            this@VerilogParserListener.enterReal_identifier(ctx)

        override suspend fun exitReal_identifier(ctx: VerilogParser.Real_identifierContext) =
            this@VerilogParserListener.exitReal_identifier(ctx)

        override suspend fun enterSimple_identifier(ctx: VerilogParser.Simple_identifierContext) =
            this@VerilogParserListener.enterSimple_identifier(ctx)

        override suspend fun exitSimple_identifier(ctx: VerilogParser.Simple_identifierContext) =
            this@VerilogParserListener.exitSimple_identifier(ctx)

        override suspend fun enterSpecparam_identifier(ctx: VerilogParser.Specparam_identifierContext) =
            this@VerilogParserListener.enterSpecparam_identifier(ctx)

        override suspend fun exitSpecparam_identifier(ctx: VerilogParser.Specparam_identifierContext) =
            this@VerilogParserListener.exitSpecparam_identifier(ctx)

        override suspend fun enterSystem_function_identifier(ctx: VerilogParser.System_function_identifierContext) =
            this@VerilogParserListener.enterSystem_function_identifier(ctx)

        override suspend fun exitSystem_function_identifier(ctx: VerilogParser.System_function_identifierContext) =
            this@VerilogParserListener.exitSystem_function_identifier(ctx)

        override suspend fun enterSystem_task_identifier(ctx: VerilogParser.System_task_identifierContext) =
            this@VerilogParserListener.enterSystem_task_identifier(ctx)

        override suspend fun exitSystem_task_identifier(ctx: VerilogParser.System_task_identifierContext) =
            this@VerilogParserListener.exitSystem_task_identifier(ctx)

        override suspend fun enterTask_identifier(ctx: VerilogParser.Task_identifierContext) =
            this@VerilogParserListener.enterTask_identifier(ctx)

        override suspend fun exitTask_identifier(ctx: VerilogParser.Task_identifierContext) =
            this@VerilogParserListener.exitTask_identifier(ctx)

        override suspend fun enterTerminal_identifier(ctx: VerilogParser.Terminal_identifierContext) =
            this@VerilogParserListener.enterTerminal_identifier(ctx)

        override suspend fun exitTerminal_identifier(ctx: VerilogParser.Terminal_identifierContext) =
            this@VerilogParserListener.exitTerminal_identifier(ctx)

        override suspend fun enterTopmodule_identifier(ctx: VerilogParser.Topmodule_identifierContext) =
            this@VerilogParserListener.enterTopmodule_identifier(ctx)

        override suspend fun exitTopmodule_identifier(ctx: VerilogParser.Topmodule_identifierContext) =
            this@VerilogParserListener.exitTopmodule_identifier(ctx)

        override suspend fun enterUdp_identifier(ctx: VerilogParser.Udp_identifierContext) =
            this@VerilogParserListener.enterUdp_identifier(ctx)

        override suspend fun exitUdp_identifier(ctx: VerilogParser.Udp_identifierContext) =
            this@VerilogParserListener.exitUdp_identifier(ctx)

        override suspend fun enterUdp_instance_identifier(ctx: VerilogParser.Udp_instance_identifierContext) =
            this@VerilogParserListener.enterUdp_instance_identifier(ctx)

        override suspend fun exitUdp_instance_identifier(ctx: VerilogParser.Udp_instance_identifierContext) =
            this@VerilogParserListener.exitUdp_instance_identifier(ctx)

        override suspend fun enterVariable_identifier(ctx: VerilogParser.Variable_identifierContext) =
            this@VerilogParserListener.enterVariable_identifier(ctx)

        override suspend fun exitVariable_identifier(ctx: VerilogParser.Variable_identifierContext) =
            this@VerilogParserListener.exitVariable_identifier(ctx)

        override suspend fun enterEveryRule(ctx: ParserRuleContext) = this@VerilogParserListener.enterEveryRule(ctx)
        override suspend fun exitEveryRule(ctx: ParserRuleContext) = this@VerilogParserListener.exitEveryRule(ctx)

        override suspend fun visitErrorNode(node: ErrorNode) = this@VerilogParserListener.visitErrorNode(node)
        override suspend fun visitTerminal(node: TerminalNode) = this@VerilogParserListener.visitTerminal(node)
    }
}

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link VerilogParser}.
 */
interface SuspendVerilogParserListener : SuspendParseTreeListener {
    /**
     * Enter a parse tree produced by {@link VerilogParser#library_text}.
     * @param ctx the parse tree
     */
    suspend fun enterLibrary_text(ctx: VerilogParser.Library_textContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_text}.
     * @param ctx the parse tree
     */
    suspend fun exitLibrary_text(ctx: VerilogParser.Library_textContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_description}.
     * @param ctx the parse tree
     */
    suspend fun enterLibrary_description(ctx: VerilogParser.Library_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_description}.
     * @param ctx the parse tree
     */
    suspend fun exitLibrary_description(ctx: VerilogParser.Library_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterLibrary_declaration(ctx: VerilogParser.Library_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitLibrary_declaration(ctx: VerilogParser.Library_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_incdir}.
     * @param ctx the parse tree
     */
    suspend fun enterLibrary_incdir(ctx: VerilogParser.Library_incdirContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_incdir}.
     * @param ctx the parse tree
     */
    suspend fun exitLibrary_incdir(ctx: VerilogParser.Library_incdirContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#include_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterInclude_statement(ctx: VerilogParser.Include_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#include_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitInclude_statement(ctx: VerilogParser.Include_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#file_path_spec}.
     * @param ctx the parse tree
     */
    suspend fun enterFile_path_spec(ctx: VerilogParser.File_path_specContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#file_path_spec}.
     * @param ctx the parse tree
     */
    suspend fun exitFile_path_spec(ctx: VerilogParser.File_path_specContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#source_text}.
     * @param ctx the parse tree
     */
    suspend fun enterSource_text(ctx: VerilogParser.Source_textContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#source_text}.
     * @param ctx the parse tree
     */
    suspend fun exitSource_text(ctx: VerilogParser.Source_textContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#description}.
     * @param ctx the parse tree
     */
    suspend fun enterDescription(ctx: VerilogParser.DescriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#description}.
     * @param ctx the parse tree
     */
    suspend fun exitDescription(ctx: VerilogParser.DescriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_declaration(ctx: VerilogParser.Module_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_declaration(ctx: VerilogParser.Module_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_keyword}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_keyword(ctx: VerilogParser.Module_keywordContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_keyword}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_keyword(ctx: VerilogParser.Module_keywordContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_parameter_port_list(ctx: VerilogParser.Module_parameter_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_parameter_port_list(ctx: VerilogParser.Module_parameter_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_port_declarations(ctx: VerilogParser.List_of_port_declarationsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_port_declarations(ctx: VerilogParser.List_of_port_declarationsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port}.
     * @param ctx the parse tree
     */
    suspend fun enterPort(ctx: VerilogParser.PortContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port}.
     * @param ctx the parse tree
     */
    suspend fun exitPort(ctx: VerilogParser.PortContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_implicit}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_implicit(ctx: VerilogParser.Port_implicitContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_implicit}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_implicit(ctx: VerilogParser.Port_implicitContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_explicit}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_explicit(ctx: VerilogParser.Port_explicitContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_explicit}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_explicit(ctx: VerilogParser.Port_explicitContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_expression(ctx: VerilogParser.Port_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_expression(ctx: VerilogParser.Port_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_reference}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_reference(ctx: VerilogParser.Port_referenceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_reference}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_reference(ctx: VerilogParser.Port_referenceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_declaration(ctx: VerilogParser.Port_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_declaration(ctx: VerilogParser.Port_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_item}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_item(ctx: VerilogParser.Module_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_item}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_item(ctx: VerilogParser.Module_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_or_generate_item(ctx: VerilogParser.Module_or_generate_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_or_generate_item(ctx: VerilogParser.Module_or_generate_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_or_generate_item_declaration(ctx: VerilogParser.Module_or_generate_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_or_generate_item_declaration(ctx: VerilogParser.Module_or_generate_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_override}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_override(ctx: VerilogParser.Parameter_overrideContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_override}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_override(ctx: VerilogParser.Parameter_overrideContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#config_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterConfig_declaration(ctx: VerilogParser.Config_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#config_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitConfig_declaration(ctx: VerilogParser.Config_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#design_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterDesign_statement(ctx: VerilogParser.Design_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#design_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitDesign_statement(ctx: VerilogParser.Design_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#design_statement_item}.
     * @param ctx the parse tree
     */
    suspend fun enterDesign_statement_item(ctx: VerilogParser.Design_statement_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#design_statement_item}.
     * @param ctx the parse tree
     */
    suspend fun exitDesign_statement_item(ctx: VerilogParser.Design_statement_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#config_rule_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterConfig_rule_statement(ctx: VerilogParser.Config_rule_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#config_rule_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitConfig_rule_statement(ctx: VerilogParser.Config_rule_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#default_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterDefault_clause(ctx: VerilogParser.Default_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#default_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitDefault_clause(ctx: VerilogParser.Default_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inst_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterInst_clause(ctx: VerilogParser.Inst_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inst_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitInst_clause(ctx: VerilogParser.Inst_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inst_name}.
     * @param ctx the parse tree
     */
    suspend fun enterInst_name(ctx: VerilogParser.Inst_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inst_name}.
     * @param ctx the parse tree
     */
    suspend fun exitInst_name(ctx: VerilogParser.Inst_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cell_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterCell_clause(ctx: VerilogParser.Cell_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cell_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitCell_clause(ctx: VerilogParser.Cell_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#liblist_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterLiblist_clause(ctx: VerilogParser.Liblist_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#liblist_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitLiblist_clause(ctx: VerilogParser.Liblist_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#use_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterUse_clause(ctx: VerilogParser.Use_clauseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#use_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitUse_clause(ctx: VerilogParser.Use_clauseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterLocal_parameter_declaration(ctx: VerilogParser.Local_parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitLocal_parameter_declaration(ctx: VerilogParser.Local_parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_declaration(ctx: VerilogParser.Parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_declaration(ctx: VerilogParser.Parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specparam_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecparam_declaration(ctx: VerilogParser.Specparam_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specparam_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecparam_declaration(ctx: VerilogParser.Specparam_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_type}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_type(ctx: VerilogParser.Parameter_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_type}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_type(ctx: VerilogParser.Parameter_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inout_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterInout_declaration(ctx: VerilogParser.Inout_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inout_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitInout_declaration(ctx: VerilogParser.Inout_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_declaration(ctx: VerilogParser.Input_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_declaration(ctx: VerilogParser.Input_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_declaration(ctx: VerilogParser.Output_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_declaration(ctx: VerilogParser.Output_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_declaration(ctx: VerilogParser.Event_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_declaration(ctx: VerilogParser.Event_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#integer_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterInteger_declaration(ctx: VerilogParser.Integer_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#integer_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitInteger_declaration(ctx: VerilogParser.Integer_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_declaration(ctx: VerilogParser.Net_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_declaration(ctx: VerilogParser.Net_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_declaration(ctx: VerilogParser.Real_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_declaration(ctx: VerilogParser.Real_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#realtime_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterRealtime_declaration(ctx: VerilogParser.Realtime_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#realtime_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitRealtime_declaration(ctx: VerilogParser.Realtime_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterReg_declaration(ctx: VerilogParser.Reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitReg_declaration(ctx: VerilogParser.Reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#time_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTime_declaration(ctx: VerilogParser.Time_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#time_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTime_declaration(ctx: VerilogParser.Time_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_type}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_type(ctx: VerilogParser.Net_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_type}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_type(ctx: VerilogParser.Net_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_variable_type(ctx: VerilogParser.Output_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_variable_type(ctx: VerilogParser.Output_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_type}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_type(ctx: VerilogParser.Real_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_type}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_type(ctx: VerilogParser.Real_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_type}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_type(ctx: VerilogParser.Variable_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_type}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_type(ctx: VerilogParser.Variable_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#drive_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterDrive_strength(ctx: VerilogParser.Drive_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#drive_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitDrive_strength(ctx: VerilogParser.Drive_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#strength0}.
     * @param ctx the parse tree
     */
    suspend fun enterStrength0(ctx: VerilogParser.Strength0Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#strength0}.
     * @param ctx the parse tree
     */
    suspend fun exitStrength0(ctx: VerilogParser.Strength0Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#strength1}.
     * @param ctx the parse tree
     */
    suspend fun enterStrength1(ctx: VerilogParser.Strength1Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#strength1}.
     * @param ctx the parse tree
     */
    suspend fun exitStrength1(ctx: VerilogParser.Strength1Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#charge_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterCharge_strength(ctx: VerilogParser.Charge_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#charge_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitCharge_strength(ctx: VerilogParser.Charge_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay3}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay3(ctx: VerilogParser.Delay3Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay3}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay3(ctx: VerilogParser.Delay3Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay2}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay2(ctx: VerilogParser.Delay2Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay2}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay2(ctx: VerilogParser.Delay2Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay_value}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay_value(ctx: VerilogParser.Delay_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay_value}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay_value(ctx: VerilogParser.Delay_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_defparam_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_defparam_assignments(ctx: VerilogParser.List_of_defparam_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_defparam_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_defparam_assignments(ctx: VerilogParser.List_of_defparam_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_event_identifiers(ctx: VerilogParser.List_of_event_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_event_identifiers(ctx: VerilogParser.List_of_event_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_id}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_id(ctx: VerilogParser.Event_idContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_id}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_id(ctx: VerilogParser.Event_idContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_net_decl_assignments(ctx: VerilogParser.List_of_net_decl_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_net_decl_assignments(ctx: VerilogParser.List_of_net_decl_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_net_identifiers(ctx: VerilogParser.List_of_net_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_net_identifiers(ctx: VerilogParser.List_of_net_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_id}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_id(ctx: VerilogParser.Net_idContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_id}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_id(ctx: VerilogParser.Net_idContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_param_assignments(ctx: VerilogParser.List_of_param_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_param_assignments(ctx: VerilogParser.List_of_param_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_port_identifiers(ctx: VerilogParser.List_of_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_port_identifiers(ctx: VerilogParser.List_of_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_real_identifiers(ctx: VerilogParser.List_of_real_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_real_identifiers(ctx: VerilogParser.List_of_real_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_specparam_assignments(ctx: VerilogParser.List_of_specparam_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_specparam_assignments(ctx: VerilogParser.List_of_specparam_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_variable_identifiers(ctx: VerilogParser.List_of_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_variable_identifiers(ctx: VerilogParser.List_of_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_variable_port_identifiers(ctx: VerilogParser.List_of_variable_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_variable_port_identifiers(ctx: VerilogParser.List_of_variable_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#var_port_id}.
     * @param ctx the parse tree
     */
    suspend fun enterVar_port_id(ctx: VerilogParser.Var_port_idContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#var_port_id}.
     * @param ctx the parse tree
     */
    suspend fun exitVar_port_id(ctx: VerilogParser.Var_port_idContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#defparam_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterDefparam_assignment(ctx: VerilogParser.Defparam_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#defparam_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitDefparam_assignment(ctx: VerilogParser.Defparam_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_decl_assignment(ctx: VerilogParser.Net_decl_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_decl_assignment(ctx: VerilogParser.Net_decl_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#param_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterParam_assignment(ctx: VerilogParser.Param_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#param_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitParam_assignment(ctx: VerilogParser.Param_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specparam_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecparam_assignment(ctx: VerilogParser.Specparam_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specparam_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecparam_assignment(ctx: VerilogParser.Specparam_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    suspend fun enterPulse_control_specparam(ctx: VerilogParser.Pulse_control_specparamContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    suspend fun exitPulse_control_specparam(ctx: VerilogParser.Pulse_control_specparamContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#error_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun enterError_limit_value(ctx: VerilogParser.Error_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#error_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun exitError_limit_value(ctx: VerilogParser.Error_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#reject_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun enterReject_limit_value(ctx: VerilogParser.Reject_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#reject_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun exitReject_limit_value(ctx: VerilogParser.Reject_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#limit_value}.
     * @param ctx the parse tree
     */
    suspend fun enterLimit_value(ctx: VerilogParser.Limit_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#limit_value}.
     * @param ctx the parse tree
     */
    suspend fun exitLimit_value(ctx: VerilogParser.Limit_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#dimension}.
     * @param ctx the parse tree
     */
    suspend fun enterDimension(ctx: VerilogParser.DimensionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#dimension}.
     * @param ctx the parse tree
     */
    suspend fun exitDimension(ctx: VerilogParser.DimensionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#range_}.
     * @param ctx the parse tree
     */
    suspend fun enterRange_(ctx: VerilogParser.Range_Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#range_}.
     * @param ctx the parse tree
     */
    suspend fun exitRange_(ctx: VerilogParser.Range_Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_declaration(ctx: VerilogParser.Function_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_declaration(ctx: VerilogParser.Function_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_item_declaration(ctx: VerilogParser.Function_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_item_declaration(ctx: VerilogParser.Function_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_port_list(ctx: VerilogParser.Function_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_port_list(ctx: VerilogParser.Function_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#func_port_item}.
     * @param ctx the parse tree
     */
    suspend fun enterFunc_port_item(ctx: VerilogParser.Func_port_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#func_port_item}.
     * @param ctx the parse tree
     */
    suspend fun exitFunc_port_item(ctx: VerilogParser.Func_port_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_range_or_type}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_range_or_type(ctx: VerilogParser.Function_range_or_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_range_or_type}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_range_or_type(ctx: VerilogParser.Function_range_or_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_declaration(ctx: VerilogParser.Task_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_declaration(ctx: VerilogParser.Task_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_item_declaration(ctx: VerilogParser.Task_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_item_declaration(ctx: VerilogParser.Task_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_port_list(ctx: VerilogParser.Task_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_port_list(ctx: VerilogParser.Task_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_port_item}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_port_item(ctx: VerilogParser.Task_port_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_port_item}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_port_item(ctx: VerilogParser.Task_port_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tf_input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTf_input_declaration(ctx: VerilogParser.Tf_input_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tf_input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTf_input_declaration(ctx: VerilogParser.Tf_input_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tf_output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTf_output_declaration(ctx: VerilogParser.Tf_output_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tf_output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTf_output_declaration(ctx: VerilogParser.Tf_output_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tf_inout_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTf_inout_declaration(ctx: VerilogParser.Tf_inout_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tf_inout_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTf_inout_declaration(ctx: VerilogParser.Tf_inout_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_port_type}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_port_type(ctx: VerilogParser.Task_port_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_port_type}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_port_type(ctx: VerilogParser.Task_port_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_item_declaration(ctx: VerilogParser.Block_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_item_declaration(ctx: VerilogParser.Block_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_block_variable_identifiers(ctx: VerilogParser.List_of_block_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_block_variable_identifiers(ctx: VerilogParser.List_of_block_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_block_real_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_block_real_identifiers(ctx: VerilogParser.List_of_block_real_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_block_real_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_block_real_identifiers(ctx: VerilogParser.List_of_block_real_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_variable_type(ctx: VerilogParser.Block_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_variable_type(ctx: VerilogParser.Block_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_real_type}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_real_type(ctx: VerilogParser.Block_real_typeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_real_type}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_real_type(ctx: VerilogParser.Block_real_typeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#gate_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun enterGate_instantiation(ctx: VerilogParser.Gate_instantiationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#gate_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun exitGate_instantiation(ctx: VerilogParser.Gate_instantiationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterCmos_switch_instance(ctx: VerilogParser.Cmos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitCmos_switch_instance(ctx: VerilogParser.Cmos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterEnable_gate_instance(ctx: VerilogParser.Enable_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitEnable_gate_instance(ctx: VerilogParser.Enable_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterMos_switch_instance(ctx: VerilogParser.Mos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitMos_switch_instance(ctx: VerilogParser.Mos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterN_input_gate_instance(ctx: VerilogParser.N_input_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitN_input_gate_instance(ctx: VerilogParser.N_input_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterN_output_gate_instance(ctx: VerilogParser.N_output_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitN_output_gate_instance(ctx: VerilogParser.N_output_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_switch_instance(ctx: VerilogParser.Pass_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_switch_instance(ctx: VerilogParser.Pass_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_enable_switch_instance(ctx: VerilogParser.Pass_enable_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_enable_switch_instance(ctx: VerilogParser.Pass_enable_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterPull_gate_instance(ctx: VerilogParser.Pull_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitPull_gate_instance(ctx: VerilogParser.Pull_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterName_of_gate_instance(ctx: VerilogParser.Name_of_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitName_of_gate_instance(ctx: VerilogParser.Name_of_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pulldown_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterPulldown_strength(ctx: VerilogParser.Pulldown_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pulldown_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitPulldown_strength(ctx: VerilogParser.Pulldown_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pullup_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterPullup_strength(ctx: VerilogParser.Pullup_strengthContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pullup_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitPullup_strength(ctx: VerilogParser.Pullup_strengthContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#enable_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterEnable_terminal(ctx: VerilogParser.Enable_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#enable_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitEnable_terminal(ctx: VerilogParser.Enable_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#inout_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterInout_terminal(ctx: VerilogParser.Inout_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#inout_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitInout_terminal(ctx: VerilogParser.Inout_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_terminal(ctx: VerilogParser.Input_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_terminal(ctx: VerilogParser.Input_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterNcontrol_terminal(ctx: VerilogParser.Ncontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitNcontrol_terminal(ctx: VerilogParser.Ncontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_terminal(ctx: VerilogParser.Output_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_terminal(ctx: VerilogParser.Output_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterPcontrol_terminal(ctx: VerilogParser.Pcontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitPcontrol_terminal(ctx: VerilogParser.Pcontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterCmos_switchtype(ctx: VerilogParser.Cmos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitCmos_switchtype(ctx: VerilogParser.Cmos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#enable_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun enterEnable_gatetype(ctx: VerilogParser.Enable_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#enable_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun exitEnable_gatetype(ctx: VerilogParser.Enable_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#mos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterMos_switchtype(ctx: VerilogParser.Mos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#mos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitMos_switchtype(ctx: VerilogParser.Mos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun enterN_input_gatetype(ctx: VerilogParser.N_input_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun exitN_input_gatetype(ctx: VerilogParser.N_input_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun enterN_output_gatetype(ctx: VerilogParser.N_output_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun exitN_output_gatetype(ctx: VerilogParser.N_output_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_en_switchtype(ctx: VerilogParser.Pass_en_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_en_switchtype(ctx: VerilogParser.Pass_en_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pass_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_switchtype(ctx: VerilogParser.Pass_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pass_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_switchtype(ctx: VerilogParser.Pass_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_instantiation(ctx: VerilogParser.Module_instantiationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_instantiation(ctx: VerilogParser.Module_instantiationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_value_assignment(ctx: VerilogParser.Parameter_value_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_value_assignment(ctx: VerilogParser.Parameter_value_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_parameter_assignments(ctx: VerilogParser.List_of_parameter_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_parameter_assignments(ctx: VerilogParser.List_of_parameter_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterOrdered_parameter_assignment(ctx: VerilogParser.Ordered_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitOrdered_parameter_assignment(ctx: VerilogParser.Ordered_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNamed_parameter_assignment(ctx: VerilogParser.Named_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNamed_parameter_assignment(ctx: VerilogParser.Named_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_instance(ctx: VerilogParser.Module_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_instance(ctx: VerilogParser.Module_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#name_of_module_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterName_of_module_instance(ctx: VerilogParser.Name_of_module_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#name_of_module_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitName_of_module_instance(ctx: VerilogParser.Name_of_module_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_port_connections(ctx: VerilogParser.List_of_port_connectionsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_port_connections(ctx: VerilogParser.List_of_port_connectionsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun enterOrdered_port_connection(ctx: VerilogParser.Ordered_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun exitOrdered_port_connection(ctx: VerilogParser.Ordered_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#named_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun enterNamed_port_connection(ctx: VerilogParser.Named_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#named_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun exitNamed_port_connection(ctx: VerilogParser.Named_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_region}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_region(ctx: VerilogParser.Generate_regionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_region}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_region(ctx: VerilogParser.Generate_regionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_declaration(ctx: VerilogParser.Genvar_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_declaration(ctx: VerilogParser.Genvar_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_genvar_identifiers(ctx: VerilogParser.List_of_genvar_identifiersContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_genvar_identifiers(ctx: VerilogParser.List_of_genvar_identifiersContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#loop_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterLoop_generate_construct(ctx: VerilogParser.Loop_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#loop_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitLoop_generate_construct(ctx: VerilogParser.Loop_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_initialization}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_initialization(ctx: VerilogParser.Genvar_initializationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_initialization}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_initialization(ctx: VerilogParser.Genvar_initializationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_expression(ctx: VerilogParser.Genvar_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_expression(ctx: VerilogParser.Genvar_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_iteration}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_iteration(ctx: VerilogParser.Genvar_iterationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_iteration}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_iteration(ctx: VerilogParser.Genvar_iterationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#conditional_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterConditional_generate_construct(ctx: VerilogParser.Conditional_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#conditional_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitConditional_generate_construct(ctx: VerilogParser.Conditional_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#if_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterIf_generate_construct(ctx: VerilogParser.If_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#if_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitIf_generate_construct(ctx: VerilogParser.If_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterCase_generate_construct(ctx: VerilogParser.Case_generate_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_generate_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitCase_generate_construct(ctx: VerilogParser.Case_generate_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_generate_item}.
     * @param ctx the parse tree
     */
    suspend fun enterCase_generate_item(ctx: VerilogParser.Case_generate_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_generate_item}.
     * @param ctx the parse tree
     */
    suspend fun exitCase_generate_item(ctx: VerilogParser.Case_generate_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_block(ctx: VerilogParser.Generate_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_block(ctx: VerilogParser.Generate_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block_name}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_block_name(ctx: VerilogParser.Generate_block_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block_name}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_block_name(ctx: VerilogParser.Generate_block_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block_or_null}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_block_or_null(ctx: VerilogParser.Generate_block_or_nullContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block_or_null}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_block_or_null(ctx: VerilogParser.Generate_block_or_nullContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_declaration(ctx: VerilogParser.Udp_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_declaration(ctx: VerilogParser.Udp_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_port_list(ctx: VerilogParser.Udp_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_port_list(ctx: VerilogParser.Udp_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_declaration_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_declaration_port_list(ctx: VerilogParser.Udp_declaration_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_declaration_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_declaration_port_list(ctx: VerilogParser.Udp_declaration_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_port_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_port_declaration(ctx: VerilogParser.Udp_port_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_port_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_port_declaration(ctx: VerilogParser.Udp_port_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_output_declaration(ctx: VerilogParser.Udp_output_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_output_declaration(ctx: VerilogParser.Udp_output_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_input_declaration(ctx: VerilogParser.Udp_input_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_input_declaration(ctx: VerilogParser.Udp_input_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_reg_declaration(ctx: VerilogParser.Udp_reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_reg_declaration(ctx: VerilogParser.Udp_reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_body}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_body(ctx: VerilogParser.Udp_bodyContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_body}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_body(ctx: VerilogParser.Udp_bodyContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#combinational_body}.
     * @param ctx the parse tree
     */
    suspend fun enterCombinational_body(ctx: VerilogParser.Combinational_bodyContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#combinational_body}.
     * @param ctx the parse tree
     */
    suspend fun exitCombinational_body(ctx: VerilogParser.Combinational_bodyContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#combinational_entry}.
     * @param ctx the parse tree
     */
    suspend fun enterCombinational_entry(ctx: VerilogParser.Combinational_entryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#combinational_entry}.
     * @param ctx the parse tree
     */
    suspend fun exitCombinational_entry(ctx: VerilogParser.Combinational_entryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sequential_body}.
     * @param ctx the parse tree
     */
    suspend fun enterSequential_body(ctx: VerilogParser.Sequential_bodyContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sequential_body}.
     * @param ctx the parse tree
     */
    suspend fun exitSequential_body(ctx: VerilogParser.Sequential_bodyContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_initial_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_initial_statement(ctx: VerilogParser.Udp_initial_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_initial_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_initial_statement(ctx: VerilogParser.Udp_initial_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#init_val}.
     * @param ctx the parse tree
     */
    suspend fun enterInit_val(ctx: VerilogParser.Init_valContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#init_val}.
     * @param ctx the parse tree
     */
    suspend fun exitInit_val(ctx: VerilogParser.Init_valContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sequential_entry}.
     * @param ctx the parse tree
     */
    suspend fun enterSequential_entry(ctx: VerilogParser.Sequential_entryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sequential_entry}.
     * @param ctx the parse tree
     */
    suspend fun exitSequential_entry(ctx: VerilogParser.Sequential_entryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#seq_input_list}.
     * @param ctx the parse tree
     */
    suspend fun enterSeq_input_list(ctx: VerilogParser.Seq_input_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#seq_input_list}.
     * @param ctx the parse tree
     */
    suspend fun exitSeq_input_list(ctx: VerilogParser.Seq_input_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#level_input_list}.
     * @param ctx the parse tree
     */
    suspend fun enterLevel_input_list(ctx: VerilogParser.Level_input_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#level_input_list}.
     * @param ctx the parse tree
     */
    suspend fun exitLevel_input_list(ctx: VerilogParser.Level_input_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_input_list}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_input_list(ctx: VerilogParser.Edge_input_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_input_list}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_input_list(ctx: VerilogParser.Edge_input_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_indicator}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_indicator(ctx: VerilogParser.Edge_indicatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_indicator}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_indicator(ctx: VerilogParser.Edge_indicatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#current_state}.
     * @param ctx the parse tree
     */
    suspend fun enterCurrent_state(ctx: VerilogParser.Current_stateContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#current_state}.
     * @param ctx the parse tree
     */
    suspend fun exitCurrent_state(ctx: VerilogParser.Current_stateContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#next_state}.
     * @param ctx the parse tree
     */
    suspend fun enterNext_state(ctx: VerilogParser.Next_stateContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#next_state}.
     * @param ctx the parse tree
     */
    suspend fun exitNext_state(ctx: VerilogParser.Next_stateContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_symbol}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_symbol(ctx: VerilogParser.Output_symbolContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_symbol}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_symbol(ctx: VerilogParser.Output_symbolContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#level_symbol}.
     * @param ctx the parse tree
     */
    suspend fun enterLevel_symbol(ctx: VerilogParser.Level_symbolContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#level_symbol}.
     * @param ctx the parse tree
     */
    suspend fun exitLevel_symbol(ctx: VerilogParser.Level_symbolContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_symbol}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_symbol(ctx: VerilogParser.Edge_symbolContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_symbol}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_symbol(ctx: VerilogParser.Edge_symbolContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_instantiation(ctx: VerilogParser.Udp_instantiationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_instantiation(ctx: VerilogParser.Udp_instantiationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_instance(ctx: VerilogParser.Udp_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_instance(ctx: VerilogParser.Udp_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#name_of_udp_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterName_of_udp_instance(ctx: VerilogParser.Name_of_udp_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#name_of_udp_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitName_of_udp_instance(ctx: VerilogParser.Name_of_udp_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#continuous_assign}.
     * @param ctx the parse tree
     */
    suspend fun enterContinuous_assign(ctx: VerilogParser.Continuous_assignContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#continuous_assign}.
     * @param ctx the parse tree
     */
    suspend fun exitContinuous_assign(ctx: VerilogParser.Continuous_assignContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_net_assignments(ctx: VerilogParser.List_of_net_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_net_assignments(ctx: VerilogParser.List_of_net_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_assignment(ctx: VerilogParser.Net_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_assignment(ctx: VerilogParser.Net_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#initial_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterInitial_construct(ctx: VerilogParser.Initial_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#initial_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitInitial_construct(ctx: VerilogParser.Initial_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#always_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterAlways_construct(ctx: VerilogParser.Always_constructContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#always_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitAlways_construct(ctx: VerilogParser.Always_constructContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#blocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterBlocking_assignment(ctx: VerilogParser.Blocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#blocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitBlocking_assignment(ctx: VerilogParser.Blocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNonblocking_assignment(ctx: VerilogParser.Nonblocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNonblocking_assignment(ctx: VerilogParser.Nonblocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterProcedural_continuous_assignments(ctx: VerilogParser.Procedural_continuous_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitProcedural_continuous_assignments(ctx: VerilogParser.Procedural_continuous_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_assignment(ctx: VerilogParser.Variable_assignmentContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_assignment(ctx: VerilogParser.Variable_assignmentContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#par_block}.
     * @param ctx the parse tree
     */
    suspend fun enterPar_block(ctx: VerilogParser.Par_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#par_block}.
     * @param ctx the parse tree
     */
    suspend fun exitPar_block(ctx: VerilogParser.Par_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_name}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_name(ctx: VerilogParser.Block_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_name}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_name(ctx: VerilogParser.Block_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#seq_block}.
     * @param ctx the parse tree
     */
    suspend fun enterSeq_block(ctx: VerilogParser.Seq_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#seq_block}.
     * @param ctx the parse tree
     */
    suspend fun exitSeq_block(ctx: VerilogParser.Seq_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#statement}.
     * @param ctx the parse tree
     */
    suspend fun enterStatement(ctx: VerilogParser.StatementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#statement}.
     * @param ctx the parse tree
     */
    suspend fun exitStatement(ctx: VerilogParser.StatementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#statement_or_null}.
     * @param ctx the parse tree
     */
    suspend fun enterStatement_or_null(ctx: VerilogParser.Statement_or_nullContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#statement_or_null}.
     * @param ctx the parse tree
     */
    suspend fun exitStatement_or_null(ctx: VerilogParser.Statement_or_nullContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_statement(ctx: VerilogParser.Function_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_statement(ctx: VerilogParser.Function_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay_control}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay_control(ctx: VerilogParser.Delay_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay_control}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay_control(ctx: VerilogParser.Delay_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay_or_event_control(ctx: VerilogParser.Delay_or_event_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay_or_event_control(ctx: VerilogParser.Delay_or_event_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#disable_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterDisable_statement(ctx: VerilogParser.Disable_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#disable_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitDisable_statement(ctx: VerilogParser.Disable_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_control}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_control(ctx: VerilogParser.Event_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_control}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_control(ctx: VerilogParser.Event_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_trigger}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_trigger(ctx: VerilogParser.Event_triggerContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_trigger}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_trigger(ctx: VerilogParser.Event_triggerContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_expression(ctx: VerilogParser.Event_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_expression(ctx: VerilogParser.Event_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#procedural_timing_control}.
     * @param ctx the parse tree
     */
    suspend fun enterProcedural_timing_control(ctx: VerilogParser.Procedural_timing_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#procedural_timing_control}.
     * @param ctx the parse tree
     */
    suspend fun exitProcedural_timing_control(ctx: VerilogParser.Procedural_timing_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterProcedural_timing_control_statement(ctx: VerilogParser.Procedural_timing_control_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitProcedural_timing_control_statement(ctx: VerilogParser.Procedural_timing_control_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#wait_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterWait_statement(ctx: VerilogParser.Wait_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#wait_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitWait_statement(ctx: VerilogParser.Wait_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterConditional_statement(ctx: VerilogParser.Conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitConditional_statement(ctx: VerilogParser.Conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterCase_statement(ctx: VerilogParser.Case_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitCase_statement(ctx: VerilogParser.Case_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#case_item}.
     * @param ctx the parse tree
     */
    suspend fun enterCase_item(ctx: VerilogParser.Case_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#case_item}.
     * @param ctx the parse tree
     */
    suspend fun exitCase_item(ctx: VerilogParser.Case_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterLoop_statement(ctx: VerilogParser.Loop_statementContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitLoop_statement(ctx: VerilogParser.Loop_statementContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_task_enable}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_task_enable(ctx: VerilogParser.System_task_enableContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_task_enable}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_task_enable(ctx: VerilogParser.System_task_enableContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sys_task_en_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterSys_task_en_port_list(ctx: VerilogParser.Sys_task_en_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sys_task_en_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitSys_task_en_port_list(ctx: VerilogParser.Sys_task_en_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sys_task_en_port_item}.
     * @param ctx the parse tree
     */
    suspend fun enterSys_task_en_port_item(ctx: VerilogParser.Sys_task_en_port_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sys_task_en_port_item}.
     * @param ctx the parse tree
     */
    suspend fun exitSys_task_en_port_item(ctx: VerilogParser.Sys_task_en_port_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_enable}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_enable(ctx: VerilogParser.Task_enableContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_enable}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_enable(ctx: VerilogParser.Task_enableContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_en_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_en_port_list(ctx: VerilogParser.Task_en_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_en_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_en_port_list(ctx: VerilogParser.Task_en_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_block}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_block(ctx: VerilogParser.Specify_blockContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_block}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_block(ctx: VerilogParser.Specify_blockContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_item}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_item(ctx: VerilogParser.Specify_itemContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_item}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_item(ctx: VerilogParser.Specify_itemContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterPulsestyle_declaration(ctx: VerilogParser.Pulsestyle_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitPulsestyle_declaration(ctx: VerilogParser.Pulsestyle_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterShowcancelled_declaration(ctx: VerilogParser.Showcancelled_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitShowcancelled_declaration(ctx: VerilogParser.Showcancelled_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterPath_declaration(ctx: VerilogParser.Path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitPath_declaration(ctx: VerilogParser.Path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterSimple_path_declaration(ctx: VerilogParser.Simple_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitSimple_path_declaration(ctx: VerilogParser.Simple_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parallel_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterParallel_path_description(ctx: VerilogParser.Parallel_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parallel_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitParallel_path_description(ctx: VerilogParser.Parallel_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#full_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterFull_path_description(ctx: VerilogParser.Full_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#full_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitFull_path_description(ctx: VerilogParser.Full_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_path_inputs(ctx: VerilogParser.List_of_path_inputsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_path_inputs(ctx: VerilogParser.List_of_path_inputsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_path_outputs(ctx: VerilogParser.List_of_path_outputsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_path_outputs(ctx: VerilogParser.List_of_path_outputsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_input_terminal_descriptor(ctx: VerilogParser.Specify_input_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_input_terminal_descriptor(ctx: VerilogParser.Specify_input_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_output_terminal_descriptor(ctx: VerilogParser.Specify_output_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_output_terminal_descriptor(ctx: VerilogParser.Specify_output_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_identifier(ctx: VerilogParser.Input_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_identifier(ctx: VerilogParser.Input_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_identifier(ctx: VerilogParser.Output_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_identifier(ctx: VerilogParser.Output_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#path_delay_value}.
     * @param ctx the parse tree
     */
    suspend fun enterPath_delay_value(ctx: VerilogParser.Path_delay_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#path_delay_value}.
     * @param ctx the parse tree
     */
    suspend fun exitPath_delay_value(ctx: VerilogParser.Path_delay_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_path_delay_expressions(ctx: VerilogParser.List_of_path_delay_expressionsContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_path_delay_expressions(ctx: VerilogParser.List_of_path_delay_expressionsContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT_path_delay_expression(ctx: VerilogParser.T_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT_path_delay_expression(ctx: VerilogParser.T_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTrise_path_delay_expression(ctx: VerilogParser.Trise_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTrise_path_delay_expression(ctx: VerilogParser.Trise_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTfall_path_delay_expression(ctx: VerilogParser.Tfall_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTfall_path_delay_expression(ctx: VerilogParser.Tfall_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTz_path_delay_expression(ctx: VerilogParser.Tz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTz_path_delay_expression(ctx: VerilogParser.Tz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT01_path_delay_expression(ctx: VerilogParser.T01_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT01_path_delay_expression(ctx: VerilogParser.T01_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT10_path_delay_expression(ctx: VerilogParser.T10_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT10_path_delay_expression(ctx: VerilogParser.T10_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT0z_path_delay_expression(ctx: VerilogParser.T0z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT0z_path_delay_expression(ctx: VerilogParser.T0z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTz1_path_delay_expression(ctx: VerilogParser.Tz1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTz1_path_delay_expression(ctx: VerilogParser.Tz1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT1z_path_delay_expression(ctx: VerilogParser.T1z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT1z_path_delay_expression(ctx: VerilogParser.T1z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTz0_path_delay_expression(ctx: VerilogParser.Tz0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTz0_path_delay_expression(ctx: VerilogParser.Tz0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT0x_path_delay_expression(ctx: VerilogParser.T0x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT0x_path_delay_expression(ctx: VerilogParser.T0x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTx1_path_delay_expression(ctx: VerilogParser.Tx1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTx1_path_delay_expression(ctx: VerilogParser.Tx1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT1x_path_delay_expression(ctx: VerilogParser.T1x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT1x_path_delay_expression(ctx: VerilogParser.T1x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTx0_path_delay_expression(ctx: VerilogParser.Tx0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTx0_path_delay_expression(ctx: VerilogParser.Tx0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTxz_path_delay_expression(ctx: VerilogParser.Txz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTxz_path_delay_expression(ctx: VerilogParser.Txz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTzx_path_delay_expression(ctx: VerilogParser.Tzx_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTzx_path_delay_expression(ctx: VerilogParser.Tzx_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterPath_delay_expression(ctx: VerilogParser.Path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitPath_delay_expression(ctx: VerilogParser.Path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_sensitive_path_declaration(ctx: VerilogParser.Edge_sensitive_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_sensitive_path_declaration(ctx: VerilogParser.Edge_sensitive_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterParallel_edge_sensitive_path_description(ctx: VerilogParser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitParallel_edge_sensitive_path_description(ctx: VerilogParser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterFull_edge_sensitive_path_description(ctx: VerilogParser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitFull_edge_sensitive_path_description(ctx: VerilogParser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#data_source_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterData_source_expression(ctx: VerilogParser.Data_source_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#data_source_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitData_source_expression(ctx: VerilogParser.Data_source_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_identifier(ctx: VerilogParser.Edge_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_identifier(ctx: VerilogParser.Edge_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterState_dependent_path_declaration(ctx: VerilogParser.State_dependent_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitState_dependent_path_declaration(ctx: VerilogParser.State_dependent_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#polarity_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterPolarity_operator(ctx: VerilogParser.Polarity_operatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#polarity_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitPolarity_operator(ctx: VerilogParser.Polarity_operatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_timing_check(ctx: VerilogParser.System_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_timing_check(ctx: VerilogParser.System_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#setup_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterSetup_timing_check(ctx: VerilogParser.Setup_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#setup_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitSetup_timing_check(ctx: VerilogParser.Setup_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#notifier_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterNotifier_opt(ctx: VerilogParser.Notifier_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#notifier_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitNotifier_opt(ctx: VerilogParser.Notifier_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hold_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterHold_timing_check(ctx: VerilogParser.Hold_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hold_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitHold_timing_check(ctx: VerilogParser.Hold_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#setuphold_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterSetuphold_timing_check(ctx: VerilogParser.Setuphold_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#setuphold_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitSetuphold_timing_check(ctx: VerilogParser.Setuphold_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterTiming_check_opt(ctx: VerilogParser.Timing_check_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitTiming_check_opt(ctx: VerilogParser.Timing_check_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#stamptime_cond_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterStamptime_cond_opt(ctx: VerilogParser.Stamptime_cond_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#stamptime_cond_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitStamptime_cond_opt(ctx: VerilogParser.Stamptime_cond_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#checktime_cond_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterChecktime_cond_opt(ctx: VerilogParser.Checktime_cond_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#checktime_cond_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitChecktime_cond_opt(ctx: VerilogParser.Checktime_cond_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_ref_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterDelayed_ref_opt(ctx: VerilogParser.Delayed_ref_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_ref_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitDelayed_ref_opt(ctx: VerilogParser.Delayed_ref_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_data_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterDelayed_data_opt(ctx: VerilogParser.Delayed_data_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_data_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitDelayed_data_opt(ctx: VerilogParser.Delayed_data_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#recovery_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterRecovery_timing_check(ctx: VerilogParser.Recovery_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#recovery_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitRecovery_timing_check(ctx: VerilogParser.Recovery_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#removal_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterRemoval_timing_check(ctx: VerilogParser.Removal_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#removal_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitRemoval_timing_check(ctx: VerilogParser.Removal_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#recrem_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterRecrem_timing_check(ctx: VerilogParser.Recrem_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#recrem_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitRecrem_timing_check(ctx: VerilogParser.Recrem_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#skew_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterSkew_timing_check(ctx: VerilogParser.Skew_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#skew_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitSkew_timing_check(ctx: VerilogParser.Skew_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timeskew_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterTimeskew_timing_check(ctx: VerilogParser.Timeskew_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timeskew_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitTimeskew_timing_check(ctx: VerilogParser.Timeskew_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#skew_timing_check_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterSkew_timing_check_opt(ctx: VerilogParser.Skew_timing_check_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#skew_timing_check_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitSkew_timing_check_opt(ctx: VerilogParser.Skew_timing_check_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_based_flag_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_based_flag_opt(ctx: VerilogParser.Event_based_flag_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_based_flag_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_based_flag_opt(ctx: VerilogParser.Event_based_flag_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#remain_active_flag_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterRemain_active_flag_opt(ctx: VerilogParser.Remain_active_flag_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#remain_active_flag_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitRemain_active_flag_opt(ctx: VerilogParser.Remain_active_flag_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#fullskew_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterFullskew_timing_check(ctx: VerilogParser.Fullskew_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#fullskew_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitFullskew_timing_check(ctx: VerilogParser.Fullskew_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#period_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterPeriod_timing_check(ctx: VerilogParser.Period_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#period_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitPeriod_timing_check(ctx: VerilogParser.Period_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#width_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterWidth_timing_check(ctx: VerilogParser.Width_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#width_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitWidth_timing_check(ctx: VerilogParser.Width_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#threshold_opt}.
     * @param ctx the parse tree
     */
    suspend fun enterThreshold_opt(ctx: VerilogParser.Threshold_optContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#threshold_opt}.
     * @param ctx the parse tree
     */
    suspend fun exitThreshold_opt(ctx: VerilogParser.Threshold_optContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#nochange_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun enterNochange_timing_check(ctx: VerilogParser.Nochange_timing_checkContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#nochange_timing_check}.
     * @param ctx the parse tree
     */
    suspend fun exitNochange_timing_check(ctx: VerilogParser.Nochange_timing_checkContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#checktime_condition}.
     * @param ctx the parse tree
     */
    suspend fun enterChecktime_condition(ctx: VerilogParser.Checktime_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#checktime_condition}.
     * @param ctx the parse tree
     */
    suspend fun exitChecktime_condition(ctx: VerilogParser.Checktime_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#controlled_reference_event}.
     * @param ctx the parse tree
     */
    suspend fun enterControlled_reference_event(ctx: VerilogParser.Controlled_reference_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#controlled_reference_event}.
     * @param ctx the parse tree
     */
    suspend fun exitControlled_reference_event(ctx: VerilogParser.Controlled_reference_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#data_event}.
     * @param ctx the parse tree
     */
    suspend fun enterData_event(ctx: VerilogParser.Data_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#data_event}.
     * @param ctx the parse tree
     */
    suspend fun exitData_event(ctx: VerilogParser.Data_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_data}.
     * @param ctx the parse tree
     */
    suspend fun enterDelayed_data(ctx: VerilogParser.Delayed_dataContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_data}.
     * @param ctx the parse tree
     */
    suspend fun exitDelayed_data(ctx: VerilogParser.Delayed_dataContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#delayed_reference}.
     * @param ctx the parse tree
     */
    suspend fun enterDelayed_reference(ctx: VerilogParser.Delayed_referenceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#delayed_reference}.
     * @param ctx the parse tree
     */
    suspend fun exitDelayed_reference(ctx: VerilogParser.Delayed_referenceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#end_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun enterEnd_edge_offset(ctx: VerilogParser.End_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#end_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun exitEnd_edge_offset(ctx: VerilogParser.End_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_based_flag}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_based_flag(ctx: VerilogParser.Event_based_flagContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_based_flag}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_based_flag(ctx: VerilogParser.Event_based_flagContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#notifier}.
     * @param ctx the parse tree
     */
    suspend fun enterNotifier(ctx: VerilogParser.NotifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#notifier}.
     * @param ctx the parse tree
     */
    suspend fun exitNotifier(ctx: VerilogParser.NotifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#reference_event}.
     * @param ctx the parse tree
     */
    suspend fun enterReference_event(ctx: VerilogParser.Reference_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#reference_event}.
     * @param ctx the parse tree
     */
    suspend fun exitReference_event(ctx: VerilogParser.Reference_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#remain_active_flag}.
     * @param ctx the parse tree
     */
    suspend fun enterRemain_active_flag(ctx: VerilogParser.Remain_active_flagContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#remain_active_flag}.
     * @param ctx the parse tree
     */
    suspend fun exitRemain_active_flag(ctx: VerilogParser.Remain_active_flagContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#stamptime_condition}.
     * @param ctx the parse tree
     */
    suspend fun enterStamptime_condition(ctx: VerilogParser.Stamptime_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#stamptime_condition}.
     * @param ctx the parse tree
     */
    suspend fun exitStamptime_condition(ctx: VerilogParser.Stamptime_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#start_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun enterStart_edge_offset(ctx: VerilogParser.Start_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#start_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun exitStart_edge_offset(ctx: VerilogParser.Start_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#threshold}.
     * @param ctx the parse tree
     */
    suspend fun enterThreshold(ctx: VerilogParser.ThresholdContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#threshold}.
     * @param ctx the parse tree
     */
    suspend fun exitThreshold(ctx: VerilogParser.ThresholdContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_limit}.
     * @param ctx the parse tree
     */
    suspend fun enterTiming_check_limit(ctx: VerilogParser.Timing_check_limitContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_limit}.
     * @param ctx the parse tree
     */
    suspend fun exitTiming_check_limit(ctx: VerilogParser.Timing_check_limitContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_event}.
     * @param ctx the parse tree
     */
    suspend fun enterTiming_check_event(ctx: VerilogParser.Timing_check_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_event}.
     * @param ctx the parse tree
     */
    suspend fun exitTiming_check_event(ctx: VerilogParser.Timing_check_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#controlled_timing_check_event}.
     * @param ctx the parse tree
     */
    suspend fun enterControlled_timing_check_event(ctx: VerilogParser.Controlled_timing_check_eventContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#controlled_timing_check_event}.
     * @param ctx the parse tree
     */
    suspend fun exitControlled_timing_check_event(ctx: VerilogParser.Controlled_timing_check_eventContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_event_control}.
     * @param ctx the parse tree
     */
    suspend fun enterTiming_check_event_control(ctx: VerilogParser.Timing_check_event_controlContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_event_control}.
     * @param ctx the parse tree
     */
    suspend fun exitTiming_check_event_control(ctx: VerilogParser.Timing_check_event_controlContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specify_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_terminal_descriptor(ctx: VerilogParser.Specify_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specify_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_terminal_descriptor(ctx: VerilogParser.Specify_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_control_specifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_control_specifier(ctx: VerilogParser.Edge_control_specifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_control_specifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_control_specifier(ctx: VerilogParser.Edge_control_specifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#edge_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_descriptor(ctx: VerilogParser.Edge_descriptorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#edge_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_descriptor(ctx: VerilogParser.Edge_descriptorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#timing_check_condition}.
     * @param ctx the parse tree
     */
    suspend fun enterTiming_check_condition(ctx: VerilogParser.Timing_check_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#timing_check_condition}.
     * @param ctx the parse tree
     */
    suspend fun exitTiming_check_condition(ctx: VerilogParser.Timing_check_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#scalar_timing_check_condition}.
     * @param ctx the parse tree
     */
    suspend fun enterScalar_timing_check_condition(ctx: VerilogParser.Scalar_timing_check_conditionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#scalar_timing_check_condition}.
     * @param ctx the parse tree
     */
    suspend fun exitScalar_timing_check_condition(ctx: VerilogParser.Scalar_timing_check_conditionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#scalar_constant}.
     * @param ctx the parse tree
     */
    suspend fun enterScalar_constant(ctx: VerilogParser.Scalar_constantContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#scalar_constant}.
     * @param ctx the parse tree
     */
    suspend fun exitScalar_constant(ctx: VerilogParser.Scalar_constantContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterConcatenation(ctx: VerilogParser.ConcatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitConcatenation(ctx: VerilogParser.ConcatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_concatenation(ctx: VerilogParser.Constant_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_concatenation(ctx: VerilogParser.Constant_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_multiple_concatenation(ctx: VerilogParser.Constant_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_multiple_concatenation(ctx: VerilogParser.Constant_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_concatenation(ctx: VerilogParser.Module_path_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_concatenation(ctx: VerilogParser.Module_path_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_multiple_concatenation(ctx: VerilogParser.Module_path_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_multiple_concatenation(ctx: VerilogParser.Module_path_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterMultiple_concatenation(ctx: VerilogParser.Multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitMultiple_concatenation(ctx: VerilogParser.Multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_function_call(ctx: VerilogParser.Constant_function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_function_call(ctx: VerilogParser.Constant_function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_system_function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_system_function_call(ctx: VerilogParser.Constant_system_function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_system_function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_system_function_call(ctx: VerilogParser.Constant_system_function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_call(ctx: VerilogParser.Function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_call(ctx: VerilogParser.Function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_function_call(ctx: VerilogParser.System_function_callContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_function_call(ctx: VerilogParser.System_function_callContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#sys_func_call_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterSys_func_call_port_list(ctx: VerilogParser.Sys_func_call_port_listContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#sys_func_call_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitSys_func_call_port_list(ctx: VerilogParser.Sys_func_call_port_listContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#base_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterBase_expression(ctx: VerilogParser.Base_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#base_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitBase_expression(ctx: VerilogParser.Base_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_base_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_base_expression(ctx: VerilogParser.Constant_base_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_base_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_base_expression(ctx: VerilogParser.Constant_base_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_expression(ctx: VerilogParser.Constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_expression(ctx: VerilogParser.Constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_mintypmax_expression(ctx: VerilogParser.Constant_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_mintypmax_expression(ctx: VerilogParser.Constant_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_range_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_range_expression(ctx: VerilogParser.Constant_range_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_range_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_range_expression(ctx: VerilogParser.Constant_range_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterDimension_constant_expression(ctx: VerilogParser.Dimension_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitDimension_constant_expression(ctx: VerilogParser.Dimension_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#expression}.
     * @param ctx the parse tree
     */
    suspend fun enterExpression(ctx: VerilogParser.ExpressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#expression}.
     * @param ctx the parse tree
     */
    suspend fun exitExpression(ctx: VerilogParser.ExpressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterLsb_constant_expression(ctx: VerilogParser.Lsb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitLsb_constant_expression(ctx: VerilogParser.Lsb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterMintypmax_expression(ctx: VerilogParser.Mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitMintypmax_expression(ctx: VerilogParser.Mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_expression(ctx: VerilogParser.Module_path_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_expression(ctx: VerilogParser.Module_path_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_mintypmax_expression(ctx: VerilogParser.Module_path_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_mintypmax_expression(ctx: VerilogParser.Module_path_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterMsb_constant_expression(ctx: VerilogParser.Msb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitMsb_constant_expression(ctx: VerilogParser.Msb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#range_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterRange_expression(ctx: VerilogParser.Range_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#range_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitRange_expression(ctx: VerilogParser.Range_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#width_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterWidth_constant_expression(ctx: VerilogParser.Width_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#width_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitWidth_constant_expression(ctx: VerilogParser.Width_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#constant_primary}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_primary(ctx: VerilogParser.Constant_primaryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#constant_primary}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_primary(ctx: VerilogParser.Constant_primaryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_path_primary}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_primary(ctx: VerilogParser.Module_path_primaryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_path_primary}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_primary(ctx: VerilogParser.Module_path_primaryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#primary}.
     * @param ctx the parse tree
     */
    suspend fun enterPrimary(ctx: VerilogParser.PrimaryContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#primary}.
     * @param ctx the parse tree
     */
    suspend fun exitPrimary(ctx: VerilogParser.PrimaryContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#select_}.
     * @param ctx the parse tree
     */
    suspend fun enterSelect_(ctx: VerilogParser.Select_Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#select_}.
     * @param ctx the parse tree
     */
    suspend fun exitSelect_(ctx: VerilogParser.Select_Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#bit_select}.
     * @param ctx the parse tree
     */
    suspend fun enterBit_select(ctx: VerilogParser.Bit_selectContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#bit_select}.
     * @param ctx the parse tree
     */
    suspend fun exitBit_select(ctx: VerilogParser.Bit_selectContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_lvalue(ctx: VerilogParser.Net_lvalueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_lvalue(ctx: VerilogParser.Net_lvalueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#const_select}.
     * @param ctx the parse tree
     */
    suspend fun enterConst_select(ctx: VerilogParser.Const_selectContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#const_select}.
     * @param ctx the parse tree
     */
    suspend fun exitConst_select(ctx: VerilogParser.Const_selectContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#const_bit_select}.
     * @param ctx the parse tree
     */
    suspend fun enterConst_bit_select(ctx: VerilogParser.Const_bit_selectContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#const_bit_select}.
     * @param ctx the parse tree
     */
    suspend fun exitConst_bit_select(ctx: VerilogParser.Const_bit_selectContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_lvalue(ctx: VerilogParser.Variable_lvalueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_lvalue(ctx: VerilogParser.Variable_lvalueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#unary_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterUnary_operator(ctx: VerilogParser.Unary_operatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#unary_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitUnary_operator(ctx: VerilogParser.Unary_operatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterUnary_module_path_operator(ctx: VerilogParser.Unary_module_path_operatorContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitUnary_module_path_operator(ctx: VerilogParser.Unary_module_path_operatorContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#number}.
     * @param ctx the parse tree
     */
    suspend fun enterNumber(ctx: VerilogParser.NumberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#number}.
     * @param ctx the parse tree
     */
    suspend fun exitNumber(ctx: VerilogParser.NumberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_number}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_number(ctx: VerilogParser.Real_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_number}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_number(ctx: VerilogParser.Real_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#decimal_number}.
     * @param ctx the parse tree
     */
    suspend fun enterDecimal_number(ctx: VerilogParser.Decimal_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#decimal_number}.
     * @param ctx the parse tree
     */
    suspend fun exitDecimal_number(ctx: VerilogParser.Decimal_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#binary_number}.
     * @param ctx the parse tree
     */
    suspend fun enterBinary_number(ctx: VerilogParser.Binary_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#binary_number}.
     * @param ctx the parse tree
     */
    suspend fun exitBinary_number(ctx: VerilogParser.Binary_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#octal_number}.
     * @param ctx the parse tree
     */
    suspend fun enterOctal_number(ctx: VerilogParser.Octal_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#octal_number}.
     * @param ctx the parse tree
     */
    suspend fun exitOctal_number(ctx: VerilogParser.Octal_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hex_number}.
     * @param ctx the parse tree
     */
    suspend fun enterHex_number(ctx: VerilogParser.Hex_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hex_number}.
     * @param ctx the parse tree
     */
    suspend fun exitHex_number(ctx: VerilogParser.Hex_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#size}.
     * @param ctx the parse tree
     */
    suspend fun enterSize(ctx: VerilogParser.SizeContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#size}.
     * @param ctx the parse tree
     */
    suspend fun exitSize(ctx: VerilogParser.SizeContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#fixed_point_number}.
     * @param ctx the parse tree
     */
    suspend fun enterFixed_point_number(ctx: VerilogParser.Fixed_point_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#fixed_point_number}.
     * @param ctx the parse tree
     */
    suspend fun exitFixed_point_number(ctx: VerilogParser.Fixed_point_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#exponential_number}.
     * @param ctx the parse tree
     */
    suspend fun enterExponential_number(ctx: VerilogParser.Exponential_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#exponential_number}.
     * @param ctx the parse tree
     */
    suspend fun exitExponential_number(ctx: VerilogParser.Exponential_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#unsigned_number}.
     * @param ctx the parse tree
     */
    suspend fun enterUnsigned_number(ctx: VerilogParser.Unsigned_numberContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#unsigned_number}.
     * @param ctx the parse tree
     */
    suspend fun exitUnsigned_number(ctx: VerilogParser.Unsigned_numberContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#decimal_value}.
     * @param ctx the parse tree
     */
    suspend fun enterDecimal_value(ctx: VerilogParser.Decimal_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#decimal_value}.
     * @param ctx the parse tree
     */
    suspend fun exitDecimal_value(ctx: VerilogParser.Decimal_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#binary_value}.
     * @param ctx the parse tree
     */
    suspend fun enterBinary_value(ctx: VerilogParser.Binary_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#binary_value}.
     * @param ctx the parse tree
     */
    suspend fun exitBinary_value(ctx: VerilogParser.Binary_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#octal_value}.
     * @param ctx the parse tree
     */
    suspend fun enterOctal_value(ctx: VerilogParser.Octal_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#octal_value}.
     * @param ctx the parse tree
     */
    suspend fun exitOctal_value(ctx: VerilogParser.Octal_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hex_value}.
     * @param ctx the parse tree
     */
    suspend fun enterHex_value(ctx: VerilogParser.Hex_valueContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hex_value}.
     * @param ctx the parse tree
     */
    suspend fun exitHex_value(ctx: VerilogParser.Hex_valueContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#decimal_base}.
     * @param ctx the parse tree
     */
    suspend fun enterDecimal_base(ctx: VerilogParser.Decimal_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#decimal_base}.
     * @param ctx the parse tree
     */
    suspend fun exitDecimal_base(ctx: VerilogParser.Decimal_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#binary_base}.
     * @param ctx the parse tree
     */
    suspend fun enterBinary_base(ctx: VerilogParser.Binary_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#binary_base}.
     * @param ctx the parse tree
     */
    suspend fun exitBinary_base(ctx: VerilogParser.Binary_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#octal_base}.
     * @param ctx the parse tree
     */
    suspend fun enterOctal_base(ctx: VerilogParser.Octal_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#octal_base}.
     * @param ctx the parse tree
     */
    suspend fun exitOctal_base(ctx: VerilogParser.Octal_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hex_base}.
     * @param ctx the parse tree
     */
    suspend fun enterHex_base(ctx: VerilogParser.Hex_baseContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hex_base}.
     * @param ctx the parse tree
     */
    suspend fun exitHex_base(ctx: VerilogParser.Hex_baseContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#string_}.
     * @param ctx the parse tree
     */
    suspend fun enterString_(ctx: VerilogParser.String_Context)

    /**
     * Exit a parse tree produced by {@link VerilogParser#string_}.
     * @param ctx the parse tree
     */
    suspend fun exitString_(ctx: VerilogParser.String_Context)

    /**
     * Enter a parse tree produced by {@link VerilogParser#attribute_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterAttribute_instance(ctx: VerilogParser.Attribute_instanceContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#attribute_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitAttribute_instance(ctx: VerilogParser.Attribute_instanceContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#attr_spec}.
     * @param ctx the parse tree
     */
    suspend fun enterAttr_spec(ctx: VerilogParser.Attr_specContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#attr_spec}.
     * @param ctx the parse tree
     */
    suspend fun exitAttr_spec(ctx: VerilogParser.Attr_specContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#attr_name}.
     * @param ctx the parse tree
     */
    suspend fun enterAttr_name(ctx: VerilogParser.Attr_nameContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#attr_name}.
     * @param ctx the parse tree
     */
    suspend fun exitAttr_name(ctx: VerilogParser.Attr_nameContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_identifier(ctx: VerilogParser.Block_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_identifier(ctx: VerilogParser.Block_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#cell_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterCell_identifier(ctx: VerilogParser.Cell_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#cell_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitCell_identifier(ctx: VerilogParser.Cell_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#config_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterConfig_identifier(ctx: VerilogParser.Config_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#config_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitConfig_identifier(ctx: VerilogParser.Config_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#escaped_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEscaped_identifier(ctx: VerilogParser.Escaped_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#escaped_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEscaped_identifier(ctx: VerilogParser.Escaped_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#event_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_identifier(ctx: VerilogParser.Event_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#event_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_identifier(ctx: VerilogParser.Event_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_identifier(ctx: VerilogParser.Function_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_identifier(ctx: VerilogParser.Function_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGate_instance_identifier(ctx: VerilogParser.Gate_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGate_instance_identifier(ctx: VerilogParser.Gate_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_block_identifier(ctx: VerilogParser.Generate_block_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_block_identifier(ctx: VerilogParser.Generate_block_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#genvar_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_identifier(ctx: VerilogParser.Genvar_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#genvar_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_identifier(ctx: VerilogParser.Genvar_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_identifier(ctx: VerilogParser.Hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_identifier(ctx: VerilogParser.Hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#hier_ref}.
     * @param ctx the parse tree
     */
    suspend fun enterHier_ref(ctx: VerilogParser.Hier_refContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#hier_ref}.
     * @param ctx the parse tree
     */
    suspend fun exitHier_ref(ctx: VerilogParser.Hier_refContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterIdentifier(ctx: VerilogParser.IdentifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitIdentifier(ctx: VerilogParser.IdentifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#input_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_port_identifier(ctx: VerilogParser.Input_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#input_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_port_identifier(ctx: VerilogParser.Input_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInstance_identifier(ctx: VerilogParser.Instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInstance_identifier(ctx: VerilogParser.Instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#library_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterLibrary_identifier(ctx: VerilogParser.Library_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#library_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitLibrary_identifier(ctx: VerilogParser.Library_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_identifier(ctx: VerilogParser.Module_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_identifier(ctx: VerilogParser.Module_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_instance_identifier(ctx: VerilogParser.Module_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_instance_identifier(ctx: VerilogParser.Module_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#net_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_identifier(ctx: VerilogParser.Net_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#net_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_identifier(ctx: VerilogParser.Net_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#output_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_port_identifier(ctx: VerilogParser.Output_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#output_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_port_identifier(ctx: VerilogParser.Output_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#parameter_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_identifier(ctx: VerilogParser.Parameter_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#parameter_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_identifier(ctx: VerilogParser.Parameter_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_identifier(ctx: VerilogParser.Port_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_identifier(ctx: VerilogParser.Port_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#real_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_identifier(ctx: VerilogParser.Real_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#real_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_identifier(ctx: VerilogParser.Real_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#simple_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSimple_identifier(ctx: VerilogParser.Simple_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#simple_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSimple_identifier(ctx: VerilogParser.Simple_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#specparam_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecparam_identifier(ctx: VerilogParser.Specparam_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#specparam_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecparam_identifier(ctx: VerilogParser.Specparam_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_function_identifier(ctx: VerilogParser.System_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_function_identifier(ctx: VerilogParser.System_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#system_task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_task_identifier(ctx: VerilogParser.System_task_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#system_task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_task_identifier(ctx: VerilogParser.System_task_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_identifier(ctx: VerilogParser.Task_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_identifier(ctx: VerilogParser.Task_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#terminal_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterTerminal_identifier(ctx: VerilogParser.Terminal_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#terminal_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitTerminal_identifier(ctx: VerilogParser.Terminal_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterTopmodule_identifier(ctx: VerilogParser.Topmodule_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitTopmodule_identifier(ctx: VerilogParser.Topmodule_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_identifier(ctx: VerilogParser.Udp_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_identifier(ctx: VerilogParser.Udp_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_instance_identifier(ctx: VerilogParser.Udp_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_instance_identifier(ctx: VerilogParser.Udp_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link VerilogParser#variable_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_identifier(ctx: VerilogParser.Variable_identifierContext)

    /**
     * Exit a parse tree produced by {@link VerilogParser#variable_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_identifier(ctx: VerilogParser.Variable_identifierContext)
}