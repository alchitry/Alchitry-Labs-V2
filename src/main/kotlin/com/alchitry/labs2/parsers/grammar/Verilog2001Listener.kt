// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs2.parsers.grammar

package com.alchitry.labs.parsers.verilog;


import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Verilog2001Parser}.
 */
interface Verilog2001Listener : ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#config_declaration}.
     * @param ctx the parse tree
     */
    fun enterConfig_declaration(ctx: Verilog2001Parser.Config_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#config_declaration}.
     * @param ctx the parse tree
     */
    fun exitConfig_declaration(ctx: Verilog2001Parser.Config_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#design_statement}.
     * @param ctx the parse tree
     */
    fun enterDesign_statement(ctx: Verilog2001Parser.Design_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#design_statement}.
     * @param ctx the parse tree
     */
    fun exitDesign_statement(ctx: Verilog2001Parser.Design_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#config_rule_statement}.
     * @param ctx the parse tree
     */
    fun enterConfig_rule_statement(ctx: Verilog2001Parser.Config_rule_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#config_rule_statement}.
     * @param ctx the parse tree
     */
    fun exitConfig_rule_statement(ctx: Verilog2001Parser.Config_rule_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#default_clause}.
     * @param ctx the parse tree
     */
    fun enterDefault_clause(ctx: Verilog2001Parser.Default_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#default_clause}.
     * @param ctx the parse tree
     */
    fun exitDefault_clause(ctx: Verilog2001Parser.Default_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inst_clause}.
     * @param ctx the parse tree
     */
    fun enterInst_clause(ctx: Verilog2001Parser.Inst_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inst_clause}.
     * @param ctx the parse tree
     */
    fun exitInst_clause(ctx: Verilog2001Parser.Inst_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inst_name}.
     * @param ctx the parse tree
     */
    fun enterInst_name(ctx: Verilog2001Parser.Inst_nameContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inst_name}.
     * @param ctx the parse tree
     */
    fun exitInst_name(ctx: Verilog2001Parser.Inst_nameContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#liblist_clause}.
     * @param ctx the parse tree
     */
    fun enterLiblist_clause(ctx: Verilog2001Parser.Liblist_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#liblist_clause}.
     * @param ctx the parse tree
     */
    fun exitLiblist_clause(ctx: Verilog2001Parser.Liblist_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cell_clause}.
     * @param ctx the parse tree
     */
    fun enterCell_clause(ctx: Verilog2001Parser.Cell_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cell_clause}.
     * @param ctx the parse tree
     */
    fun exitCell_clause(ctx: Verilog2001Parser.Cell_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#use_clause}.
     * @param ctx the parse tree
     */
    fun enterUse_clause(ctx: Verilog2001Parser.Use_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#use_clause}.
     * @param ctx the parse tree
     */
    fun exitUse_clause(ctx: Verilog2001Parser.Use_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#source_text}.
     * @param ctx the parse tree
     */
    fun enterSource_text(ctx: Verilog2001Parser.Source_textContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#source_text}.
     * @param ctx the parse tree
     */
    fun exitSource_text(ctx: Verilog2001Parser.Source_textContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#description}.
     * @param ctx the parse tree
     */
    fun enterDescription(ctx: Verilog2001Parser.DescriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#description}.
     * @param ctx the parse tree
     */
    fun exitDescription(ctx: Verilog2001Parser.DescriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_declaration}.
     * @param ctx the parse tree
     */
    fun enterModule_declaration(ctx: Verilog2001Parser.Module_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_declaration}.
     * @param ctx the parse tree
     */
    fun exitModule_declaration(ctx: Verilog2001Parser.Module_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_keyword}.
     * @param ctx the parse tree
     */
    fun enterModule_keyword(ctx: Verilog2001Parser.Module_keywordContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_keyword}.
     * @param ctx the parse tree
     */
    fun exitModule_keyword(ctx: Verilog2001Parser.Module_keywordContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    fun enterModule_parameter_port_list(ctx: Verilog2001Parser.Module_parameter_port_listContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    fun exitModule_parameter_port_list(ctx: Verilog2001Parser.Module_parameter_port_listContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
     * @param ctx the parse tree
     */
    fun enterList_of_ports(ctx: Verilog2001Parser.List_of_portsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
     * @param ctx the parse tree
     */
    fun exitList_of_ports(ctx: Verilog2001Parser.List_of_portsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    fun enterList_of_port_declarations(ctx: Verilog2001Parser.List_of_port_declarationsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    fun exitList_of_port_declarations(ctx: Verilog2001Parser.List_of_port_declarationsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port}.
     * @param ctx the parse tree
     */
    fun enterPort(ctx: Verilog2001Parser.PortContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port}.
     * @param ctx the parse tree
     */
    fun exitPort(ctx: Verilog2001Parser.PortContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_expression}.
     * @param ctx the parse tree
     */
    fun enterPort_expression(ctx: Verilog2001Parser.Port_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_expression}.
     * @param ctx the parse tree
     */
    fun exitPort_expression(ctx: Verilog2001Parser.Port_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_reference}.
     * @param ctx the parse tree
     */
    fun enterPort_reference(ctx: Verilog2001Parser.Port_referenceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_reference}.
     * @param ctx the parse tree
     */
    fun exitPort_reference(ctx: Verilog2001Parser.Port_referenceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_declaration}.
     * @param ctx the parse tree
     */
    fun enterPort_declaration(ctx: Verilog2001Parser.Port_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_declaration}.
     * @param ctx the parse tree
     */
    fun exitPort_declaration(ctx: Verilog2001Parser.Port_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_item}.
     * @param ctx the parse tree
     */
    fun enterModule_item(ctx: Verilog2001Parser.Module_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_item}.
     * @param ctx the parse tree
     */
    fun exitModule_item(ctx: Verilog2001Parser.Module_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    fun enterModule_or_generate_item(ctx: Verilog2001Parser.Module_or_generate_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    fun exitModule_or_generate_item(ctx: Verilog2001Parser.Module_or_generate_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#non_port_module_item}.
     * @param ctx the parse tree
     */
    fun enterNon_port_module_item(ctx: Verilog2001Parser.Non_port_module_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#non_port_module_item}.
     * @param ctx the parse tree
     */
    fun exitNon_port_module_item(ctx: Verilog2001Parser.Non_port_module_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterModule_or_generate_item_declaration(ctx: Verilog2001Parser.Module_or_generate_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitModule_or_generate_item_declaration(ctx: Verilog2001Parser.Module_or_generate_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_override}.
     * @param ctx the parse tree
     */
    fun enterParameter_override(ctx: Verilog2001Parser.Parameter_overrideContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_override}.
     * @param ctx the parse tree
     */
    fun exitParameter_override(ctx: Verilog2001Parser.Parameter_overrideContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    fun enterLocal_parameter_declaration(ctx: Verilog2001Parser.Local_parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    fun exitLocal_parameter_declaration(ctx: Verilog2001Parser.Local_parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
     * @param ctx the parse tree
     */
    fun enterParameter_declaration(ctx: Verilog2001Parser.Parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
     * @param ctx the parse tree
     */
    fun exitParameter_declaration(ctx: Verilog2001Parser.Parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
     * @param ctx the parse tree
     */
    fun enterParameter_declaration_(ctx: Verilog2001Parser.Parameter_declaration_Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
     * @param ctx the parse tree
     */
    fun exitParameter_declaration_(ctx: Verilog2001Parser.Parameter_declaration_Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specparam_declaration}.
     * @param ctx the parse tree
     */
    fun enterSpecparam_declaration(ctx: Verilog2001Parser.Specparam_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specparam_declaration}.
     * @param ctx the parse tree
     */
    fun exitSpecparam_declaration(ctx: Verilog2001Parser.Specparam_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inout_declaration}.
     * @param ctx the parse tree
     */
    fun enterInout_declaration(ctx: Verilog2001Parser.Inout_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inout_declaration}.
     * @param ctx the parse tree
     */
    fun exitInout_declaration(ctx: Verilog2001Parser.Inout_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_declaration}.
     * @param ctx the parse tree
     */
    fun enterInput_declaration(ctx: Verilog2001Parser.Input_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_declaration}.
     * @param ctx the parse tree
     */
    fun exitInput_declaration(ctx: Verilog2001Parser.Input_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_declaration}.
     * @param ctx the parse tree
     */
    fun enterOutput_declaration(ctx: Verilog2001Parser.Output_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_declaration}.
     * @param ctx the parse tree
     */
    fun exitOutput_declaration(ctx: Verilog2001Parser.Output_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_declaration}.
     * @param ctx the parse tree
     */
    fun enterEvent_declaration(ctx: Verilog2001Parser.Event_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_declaration}.
     * @param ctx the parse tree
     */
    fun exitEvent_declaration(ctx: Verilog2001Parser.Event_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_declaration}.
     * @param ctx the parse tree
     */
    fun enterGenvar_declaration(ctx: Verilog2001Parser.Genvar_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_declaration}.
     * @param ctx the parse tree
     */
    fun exitGenvar_declaration(ctx: Verilog2001Parser.Genvar_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#integer_declaration}.
     * @param ctx the parse tree
     */
    fun enterInteger_declaration(ctx: Verilog2001Parser.Integer_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#integer_declaration}.
     * @param ctx the parse tree
     */
    fun exitInteger_declaration(ctx: Verilog2001Parser.Integer_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#time_declaration}.
     * @param ctx the parse tree
     */
    fun enterTime_declaration(ctx: Verilog2001Parser.Time_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#time_declaration}.
     * @param ctx the parse tree
     */
    fun exitTime_declaration(ctx: Verilog2001Parser.Time_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#real_declaration}.
     * @param ctx the parse tree
     */
    fun enterReal_declaration(ctx: Verilog2001Parser.Real_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#real_declaration}.
     * @param ctx the parse tree
     */
    fun exitReal_declaration(ctx: Verilog2001Parser.Real_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#realtime_declaration}.
     * @param ctx the parse tree
     */
    fun enterRealtime_declaration(ctx: Verilog2001Parser.Realtime_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#realtime_declaration}.
     * @param ctx the parse tree
     */
    fun exitRealtime_declaration(ctx: Verilog2001Parser.Realtime_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
     * @param ctx the parse tree
     */
    fun enterReg_declaration(ctx: Verilog2001Parser.Reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
     * @param ctx the parse tree
     */
    fun exitReg_declaration(ctx: Verilog2001Parser.Reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_declaration}.
     * @param ctx the parse tree
     */
    fun enterNet_declaration(ctx: Verilog2001Parser.Net_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_declaration}.
     * @param ctx the parse tree
     */
    fun exitNet_declaration(ctx: Verilog2001Parser.Net_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_type}.
     * @param ctx the parse tree
     */
    fun enterNet_type(ctx: Verilog2001Parser.Net_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_type}.
     * @param ctx the parse tree
     */
    fun exitNet_type(ctx: Verilog2001Parser.Net_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_variable_type}.
     * @param ctx the parse tree
     */
    fun enterOutput_variable_type(ctx: Verilog2001Parser.Output_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_variable_type}.
     * @param ctx the parse tree
     */
    fun exitOutput_variable_type(ctx: Verilog2001Parser.Output_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#real_type}.
     * @param ctx the parse tree
     */
    fun enterReal_type(ctx: Verilog2001Parser.Real_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#real_type}.
     * @param ctx the parse tree
     */
    fun exitReal_type(ctx: Verilog2001Parser.Real_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_type}.
     * @param ctx the parse tree
     */
    fun enterVariable_type(ctx: Verilog2001Parser.Variable_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_type}.
     * @param ctx the parse tree
     */
    fun exitVariable_type(ctx: Verilog2001Parser.Variable_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#drive_strength}.
     * @param ctx the parse tree
     */
    fun enterDrive_strength(ctx: Verilog2001Parser.Drive_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#drive_strength}.
     * @param ctx the parse tree
     */
    fun exitDrive_strength(ctx: Verilog2001Parser.Drive_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#strength0}.
     * @param ctx the parse tree
     */
    fun enterStrength0(ctx: Verilog2001Parser.Strength0Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#strength0}.
     * @param ctx the parse tree
     */
    fun exitStrength0(ctx: Verilog2001Parser.Strength0Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#strength1}.
     * @param ctx the parse tree
     */
    fun enterStrength1(ctx: Verilog2001Parser.Strength1Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#strength1}.
     * @param ctx the parse tree
     */
    fun exitStrength1(ctx: Verilog2001Parser.Strength1Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#charge_strength}.
     * @param ctx the parse tree
     */
    fun enterCharge_strength(ctx: Verilog2001Parser.Charge_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#charge_strength}.
     * @param ctx the parse tree
     */
    fun exitCharge_strength(ctx: Verilog2001Parser.Charge_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay3}.
     * @param ctx the parse tree
     */
    fun enterDelay3(ctx: Verilog2001Parser.Delay3Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay3}.
     * @param ctx the parse tree
     */
    fun exitDelay3(ctx: Verilog2001Parser.Delay3Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay2}.
     * @param ctx the parse tree
     */
    fun enterDelay2(ctx: Verilog2001Parser.Delay2Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay2}.
     * @param ctx the parse tree
     */
    fun exitDelay2(ctx: Verilog2001Parser.Delay2Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay_value}.
     * @param ctx the parse tree
     */
    fun enterDelay_value(ctx: Verilog2001Parser.Delay_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay_value}.
     * @param ctx the parse tree
     */
    fun exitDelay_value(ctx: Verilog2001Parser.Delay_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_event_identifiers(ctx: Verilog2001Parser.List_of_event_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_event_identifiers(ctx: Verilog2001Parser.List_of_event_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_net_identifiers(ctx: Verilog2001Parser.List_of_net_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_net_identifiers(ctx: Verilog2001Parser.List_of_net_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_genvar_identifiers(ctx: Verilog2001Parser.List_of_genvar_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_genvar_identifiers(ctx: Verilog2001Parser.List_of_genvar_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_port_identifiers(ctx: Verilog2001Parser.List_of_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_port_identifiers(ctx: Verilog2001Parser.List_of_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_net_decl_assignments(ctx: Verilog2001Parser.List_of_net_decl_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_net_decl_assignments(ctx: Verilog2001Parser.List_of_net_decl_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_param_assignments(ctx: Verilog2001Parser.List_of_param_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_param_assignments(ctx: Verilog2001Parser.List_of_param_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_specparam_assignments(ctx: Verilog2001Parser.List_of_specparam_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_specparam_assignments(ctx: Verilog2001Parser.List_of_specparam_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_real_identifiers(ctx: Verilog2001Parser.List_of_real_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_real_identifiers(ctx: Verilog2001Parser.List_of_real_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_variable_identifiers(ctx: Verilog2001Parser.List_of_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_variable_identifiers(ctx: Verilog2001Parser.List_of_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_variable_port_identifiers(ctx: Verilog2001Parser.List_of_variable_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_variable_port_identifiers(ctx: Verilog2001Parser.List_of_variable_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    fun enterNet_decl_assignment(ctx: Verilog2001Parser.Net_decl_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    fun exitNet_decl_assignment(ctx: Verilog2001Parser.Net_decl_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#param_assignment}.
     * @param ctx the parse tree
     */
    fun enterParam_assignment(ctx: Verilog2001Parser.Param_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#param_assignment}.
     * @param ctx the parse tree
     */
    fun exitParam_assignment(ctx: Verilog2001Parser.Param_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specparam_assignment}.
     * @param ctx the parse tree
     */
    fun enterSpecparam_assignment(ctx: Verilog2001Parser.Specparam_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specparam_assignment}.
     * @param ctx the parse tree
     */
    fun exitSpecparam_assignment(ctx: Verilog2001Parser.Specparam_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    fun enterPulse_control_specparam(ctx: Verilog2001Parser.Pulse_control_specparamContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    fun exitPulse_control_specparam(ctx: Verilog2001Parser.Pulse_control_specparamContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#error_limit_value}.
     * @param ctx the parse tree
     */
    fun enterError_limit_value(ctx: Verilog2001Parser.Error_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#error_limit_value}.
     * @param ctx the parse tree
     */
    fun exitError_limit_value(ctx: Verilog2001Parser.Error_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#reject_limit_value}.
     * @param ctx the parse tree
     */
    fun enterReject_limit_value(ctx: Verilog2001Parser.Reject_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#reject_limit_value}.
     * @param ctx the parse tree
     */
    fun exitReject_limit_value(ctx: Verilog2001Parser.Reject_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#limit_value}.
     * @param ctx the parse tree
     */
    fun enterLimit_value(ctx: Verilog2001Parser.Limit_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#limit_value}.
     * @param ctx the parse tree
     */
    fun exitLimit_value(ctx: Verilog2001Parser.Limit_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#dimension}.
     * @param ctx the parse tree
     */
    fun enterDimension(ctx: Verilog2001Parser.DimensionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#dimension}.
     * @param ctx the parse tree
     */
    fun exitDimension(ctx: Verilog2001Parser.DimensionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#range}.
     * @param ctx the parse tree
     */
    fun enterRange(ctx: Verilog2001Parser.RangeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#range}.
     * @param ctx the parse tree
     */
    fun exitRange(ctx: Verilog2001Parser.RangeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_declaration}.
     * @param ctx the parse tree
     */
    fun enterFunction_declaration(ctx: Verilog2001Parser.Function_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_declaration}.
     * @param ctx the parse tree
     */
    fun exitFunction_declaration(ctx: Verilog2001Parser.Function_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterFunction_item_declaration(ctx: Verilog2001Parser.Function_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitFunction_item_declaration(ctx: Verilog2001Parser.Function_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_port_list}.
     * @param ctx the parse tree
     */
    fun enterFunction_port_list(ctx: Verilog2001Parser.Function_port_listContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_port_list}.
     * @param ctx the parse tree
     */
    fun exitFunction_port_list(ctx: Verilog2001Parser.Function_port_listContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_port}.
     * @param ctx the parse tree
     */
    fun enterFunction_port(ctx: Verilog2001Parser.Function_portContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_port}.
     * @param ctx the parse tree
     */
    fun exitFunction_port(ctx: Verilog2001Parser.Function_portContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#range_or_type}.
     * @param ctx the parse tree
     */
    fun enterRange_or_type(ctx: Verilog2001Parser.Range_or_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#range_or_type}.
     * @param ctx the parse tree
     */
    fun exitRange_or_type(ctx: Verilog2001Parser.Range_or_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_declaration}.
     * @param ctx the parse tree
     */
    fun enterTask_declaration(ctx: Verilog2001Parser.Task_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_declaration}.
     * @param ctx the parse tree
     */
    fun exitTask_declaration(ctx: Verilog2001Parser.Task_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterTask_item_declaration(ctx: Verilog2001Parser.Task_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitTask_item_declaration(ctx: Verilog2001Parser.Task_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_port_list}.
     * @param ctx the parse tree
     */
    fun enterTask_port_list(ctx: Verilog2001Parser.Task_port_listContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_port_list}.
     * @param ctx the parse tree
     */
    fun exitTask_port_list(ctx: Verilog2001Parser.Task_port_listContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_port_item}.
     * @param ctx the parse tree
     */
    fun enterTask_port_item(ctx: Verilog2001Parser.Task_port_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_port_item}.
     * @param ctx the parse tree
     */
    fun exitTask_port_item(ctx: Verilog2001Parser.Task_port_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tf_decl_header}.
     * @param ctx the parse tree
     */
    fun enterTf_decl_header(ctx: Verilog2001Parser.Tf_decl_headerContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tf_decl_header}.
     * @param ctx the parse tree
     */
    fun exitTf_decl_header(ctx: Verilog2001Parser.Tf_decl_headerContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tf_declaration}.
     * @param ctx the parse tree
     */
    fun enterTf_declaration(ctx: Verilog2001Parser.Tf_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tf_declaration}.
     * @param ctx the parse tree
     */
    fun exitTf_declaration(ctx: Verilog2001Parser.Tf_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_port_type}.
     * @param ctx the parse tree
     */
    fun enterTask_port_type(ctx: Verilog2001Parser.Task_port_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_port_type}.
     * @param ctx the parse tree
     */
    fun exitTask_port_type(ctx: Verilog2001Parser.Task_port_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_item_declaration}.
     * @param ctx the parse tree
     */
    fun enterBlock_item_declaration(ctx: Verilog2001Parser.Block_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_item_declaration}.
     * @param ctx the parse tree
     */
    fun exitBlock_item_declaration(ctx: Verilog2001Parser.Block_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_reg_declaration}.
     * @param ctx the parse tree
     */
    fun enterBlock_reg_declaration(ctx: Verilog2001Parser.Block_reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_reg_declaration}.
     * @param ctx the parse tree
     */
    fun exitBlock_reg_declaration(ctx: Verilog2001Parser.Block_reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun enterList_of_block_variable_identifiers(ctx: Verilog2001Parser.List_of_block_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    fun exitList_of_block_variable_identifiers(ctx: Verilog2001Parser.List_of_block_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_variable_type}.
     * @param ctx the parse tree
     */
    fun enterBlock_variable_type(ctx: Verilog2001Parser.Block_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_variable_type}.
     * @param ctx the parse tree
     */
    fun exitBlock_variable_type(ctx: Verilog2001Parser.Block_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#gate_instantiation}.
     * @param ctx the parse tree
     */
    fun enterGate_instantiation(ctx: Verilog2001Parser.Gate_instantiationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#gate_instantiation}.
     * @param ctx the parse tree
     */
    fun exitGate_instantiation(ctx: Verilog2001Parser.Gate_instantiationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterCmos_switch_instance(ctx: Verilog2001Parser.Cmos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitCmos_switch_instance(ctx: Verilog2001Parser.Cmos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterEnable_gate_instance(ctx: Verilog2001Parser.Enable_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitEnable_gate_instance(ctx: Verilog2001Parser.Enable_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterMos_switch_instance(ctx: Verilog2001Parser.Mos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitMos_switch_instance(ctx: Verilog2001Parser.Mos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterN_input_gate_instance(ctx: Verilog2001Parser.N_input_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitN_input_gate_instance(ctx: Verilog2001Parser.N_input_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterN_output_gate_instance(ctx: Verilog2001Parser.N_output_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitN_output_gate_instance(ctx: Verilog2001Parser.N_output_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterPass_switch_instance(ctx: Verilog2001Parser.Pass_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitPass_switch_instance(ctx: Verilog2001Parser.Pass_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    fun enterPass_enable_switch_instance(ctx: Verilog2001Parser.Pass_enable_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    fun exitPass_enable_switch_instance(ctx: Verilog2001Parser.Pass_enable_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterPull_gate_instance(ctx: Verilog2001Parser.Pull_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitPull_gate_instance(ctx: Verilog2001Parser.Pull_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    fun enterName_of_gate_instance(ctx: Verilog2001Parser.Name_of_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    fun exitName_of_gate_instance(ctx: Verilog2001Parser.Name_of_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pulldown_strength}.
     * @param ctx the parse tree
     */
    fun enterPulldown_strength(ctx: Verilog2001Parser.Pulldown_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pulldown_strength}.
     * @param ctx the parse tree
     */
    fun exitPulldown_strength(ctx: Verilog2001Parser.Pulldown_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pullup_strength}.
     * @param ctx the parse tree
     */
    fun enterPullup_strength(ctx: Verilog2001Parser.Pullup_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pullup_strength}.
     * @param ctx the parse tree
     */
    fun exitPullup_strength(ctx: Verilog2001Parser.Pullup_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#enable_terminal}.
     * @param ctx the parse tree
     */
    fun enterEnable_terminal(ctx: Verilog2001Parser.Enable_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#enable_terminal}.
     * @param ctx the parse tree
     */
    fun exitEnable_terminal(ctx: Verilog2001Parser.Enable_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    fun enterNcontrol_terminal(ctx: Verilog2001Parser.Ncontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    fun exitNcontrol_terminal(ctx: Verilog2001Parser.Ncontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    fun enterPcontrol_terminal(ctx: Verilog2001Parser.Pcontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    fun exitPcontrol_terminal(ctx: Verilog2001Parser.Pcontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_terminal}.
     * @param ctx the parse tree
     */
    fun enterInput_terminal(ctx: Verilog2001Parser.Input_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_terminal}.
     * @param ctx the parse tree
     */
    fun exitInput_terminal(ctx: Verilog2001Parser.Input_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inout_terminal}.
     * @param ctx the parse tree
     */
    fun enterInout_terminal(ctx: Verilog2001Parser.Inout_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inout_terminal}.
     * @param ctx the parse tree
     */
    fun exitInout_terminal(ctx: Verilog2001Parser.Inout_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_terminal}.
     * @param ctx the parse tree
     */
    fun enterOutput_terminal(ctx: Verilog2001Parser.Output_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_terminal}.
     * @param ctx the parse tree
     */
    fun exitOutput_terminal(ctx: Verilog2001Parser.Output_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    fun enterCmos_switchtype(ctx: Verilog2001Parser.Cmos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    fun exitCmos_switchtype(ctx: Verilog2001Parser.Cmos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#enable_gatetype}.
     * @param ctx the parse tree
     */
    fun enterEnable_gatetype(ctx: Verilog2001Parser.Enable_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#enable_gatetype}.
     * @param ctx the parse tree
     */
    fun exitEnable_gatetype(ctx: Verilog2001Parser.Enable_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#mos_switchtype}.
     * @param ctx the parse tree
     */
    fun enterMos_switchtype(ctx: Verilog2001Parser.Mos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#mos_switchtype}.
     * @param ctx the parse tree
     */
    fun exitMos_switchtype(ctx: Verilog2001Parser.Mos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    fun enterN_input_gatetype(ctx: Verilog2001Parser.N_input_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    fun exitN_input_gatetype(ctx: Verilog2001Parser.N_input_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    fun enterN_output_gatetype(ctx: Verilog2001Parser.N_output_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    fun exitN_output_gatetype(ctx: Verilog2001Parser.N_output_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    fun enterPass_en_switchtype(ctx: Verilog2001Parser.Pass_en_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    fun exitPass_en_switchtype(ctx: Verilog2001Parser.Pass_en_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_switchtype}.
     * @param ctx the parse tree
     */
    fun enterPass_switchtype(ctx: Verilog2001Parser.Pass_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_switchtype}.
     * @param ctx the parse tree
     */
    fun exitPass_switchtype(ctx: Verilog2001Parser.Pass_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
     * @param ctx the parse tree
     */
    fun enterModule_instantiation(ctx: Verilog2001Parser.Module_instantiationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
     * @param ctx the parse tree
     */
    fun exitModule_instantiation(ctx: Verilog2001Parser.Module_instantiationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    fun enterParameter_value_assignment(ctx: Verilog2001Parser.Parameter_value_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    fun exitParameter_value_assignment(ctx: Verilog2001Parser.Parameter_value_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_parameter_assignments(ctx: Verilog2001Parser.List_of_parameter_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_parameter_assignments(ctx: Verilog2001Parser.List_of_parameter_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun enterOrdered_parameter_assignment(ctx: Verilog2001Parser.Ordered_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun exitOrdered_parameter_assignment(ctx: Verilog2001Parser.Ordered_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun enterNamed_parameter_assignment(ctx: Verilog2001Parser.Named_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    fun exitNamed_parameter_assignment(ctx: Verilog2001Parser.Named_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_instance}.
     * @param ctx the parse tree
     */
    fun enterModule_instance(ctx: Verilog2001Parser.Module_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_instance}.
     * @param ctx the parse tree
     */
    fun exitModule_instance(ctx: Verilog2001Parser.Module_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
     * @param ctx the parse tree
     */
    fun enterName_of_instance(ctx: Verilog2001Parser.Name_of_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
     * @param ctx the parse tree
     */
    fun exitName_of_instance(ctx: Verilog2001Parser.Name_of_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    fun enterList_of_port_connections(ctx: Verilog2001Parser.List_of_port_connectionsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    fun exitList_of_port_connections(ctx: Verilog2001Parser.List_of_port_connectionsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    fun enterOrdered_port_connection(ctx: Verilog2001Parser.Ordered_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    fun exitOrdered_port_connection(ctx: Verilog2001Parser.Ordered_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#named_port_connection}.
     * @param ctx the parse tree
     */
    fun enterNamed_port_connection(ctx: Verilog2001Parser.Named_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#named_port_connection}.
     * @param ctx the parse tree
     */
    fun exitNamed_port_connection(ctx: Verilog2001Parser.Named_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generated_instantiation}.
     * @param ctx the parse tree
     */
    fun enterGenerated_instantiation(ctx: Verilog2001Parser.Generated_instantiationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generated_instantiation}.
     * @param ctx the parse tree
     */
    fun exitGenerated_instantiation(ctx: Verilog2001Parser.Generated_instantiationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_item_or_null}.
     * @param ctx the parse tree
     */
    fun enterGenerate_item_or_null(ctx: Verilog2001Parser.Generate_item_or_nullContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_item_or_null}.
     * @param ctx the parse tree
     */
    fun exitGenerate_item_or_null(ctx: Verilog2001Parser.Generate_item_or_nullContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_item}.
     * @param ctx the parse tree
     */
    fun enterGenerate_item(ctx: Verilog2001Parser.Generate_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_item}.
     * @param ctx the parse tree
     */
    fun exitGenerate_item(ctx: Verilog2001Parser.Generate_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_conditional_statement}.
     * @param ctx the parse tree
     */
    fun enterGenerate_conditional_statement(ctx: Verilog2001Parser.Generate_conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_conditional_statement}.
     * @param ctx the parse tree
     */
    fun exitGenerate_conditional_statement(ctx: Verilog2001Parser.Generate_conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_case_statement}.
     * @param ctx the parse tree
     */
    fun enterGenerate_case_statement(ctx: Verilog2001Parser.Generate_case_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_case_statement}.
     * @param ctx the parse tree
     */
    fun exitGenerate_case_statement(ctx: Verilog2001Parser.Generate_case_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_case_item}.
     * @param ctx the parse tree
     */
    fun enterGenvar_case_item(ctx: Verilog2001Parser.Genvar_case_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_case_item}.
     * @param ctx the parse tree
     */
    fun exitGenvar_case_item(ctx: Verilog2001Parser.Genvar_case_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_loop_statement}.
     * @param ctx the parse tree
     */
    fun enterGenerate_loop_statement(ctx: Verilog2001Parser.Generate_loop_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_loop_statement}.
     * @param ctx the parse tree
     */
    fun exitGenerate_loop_statement(ctx: Verilog2001Parser.Generate_loop_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_assignment}.
     * @param ctx the parse tree
     */
    fun enterGenvar_assignment(ctx: Verilog2001Parser.Genvar_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_assignment}.
     * @param ctx the parse tree
     */
    fun exitGenvar_assignment(ctx: Verilog2001Parser.Genvar_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_block}.
     * @param ctx the parse tree
     */
    fun enterGenerate_block(ctx: Verilog2001Parser.Generate_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_block}.
     * @param ctx the parse tree
     */
    fun exitGenerate_block(ctx: Verilog2001Parser.Generate_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
     * @param ctx the parse tree
     */
    fun enterContinuous_assign(ctx: Verilog2001Parser.Continuous_assignContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
     * @param ctx the parse tree
     */
    fun exitContinuous_assign(ctx: Verilog2001Parser.Continuous_assignContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    fun enterList_of_net_assignments(ctx: Verilog2001Parser.List_of_net_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    fun exitList_of_net_assignments(ctx: Verilog2001Parser.List_of_net_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_assignment}.
     * @param ctx the parse tree
     */
    fun enterNet_assignment(ctx: Verilog2001Parser.Net_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_assignment}.
     * @param ctx the parse tree
     */
    fun exitNet_assignment(ctx: Verilog2001Parser.Net_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#initial_construct}.
     * @param ctx the parse tree
     */
    fun enterInitial_construct(ctx: Verilog2001Parser.Initial_constructContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#initial_construct}.
     * @param ctx the parse tree
     */
    fun exitInitial_construct(ctx: Verilog2001Parser.Initial_constructContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#always_construct}.
     * @param ctx the parse tree
     */
    fun enterAlways_construct(ctx: Verilog2001Parser.Always_constructContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#always_construct}.
     * @param ctx the parse tree
     */
    fun exitAlways_construct(ctx: Verilog2001Parser.Always_constructContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
     * @param ctx the parse tree
     */
    fun enterBlocking_assignment(ctx: Verilog2001Parser.Blocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
     * @param ctx the parse tree
     */
    fun exitBlocking_assignment(ctx: Verilog2001Parser.Blocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    fun enterNonblocking_assignment(ctx: Verilog2001Parser.Nonblocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    fun exitNonblocking_assignment(ctx: Verilog2001Parser.Nonblocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    fun enterProcedural_continuous_assignments(ctx: Verilog2001Parser.Procedural_continuous_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    fun exitProcedural_continuous_assignments(ctx: Verilog2001Parser.Procedural_continuous_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_blocking_assignment}.
     * @param ctx the parse tree
     */
    fun enterFunction_blocking_assignment(ctx: Verilog2001Parser.Function_blocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_blocking_assignment}.
     * @param ctx the parse tree
     */
    fun exitFunction_blocking_assignment(ctx: Verilog2001Parser.Function_blocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_statement_or_null}.
     * @param ctx the parse tree
     */
    fun enterFunction_statement_or_null(ctx: Verilog2001Parser.Function_statement_or_nullContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_statement_or_null}.
     * @param ctx the parse tree
     */
    fun exitFunction_statement_or_null(ctx: Verilog2001Parser.Function_statement_or_nullContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_seq_block}.
     * @param ctx the parse tree
     */
    fun enterFunction_seq_block(ctx: Verilog2001Parser.Function_seq_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_seq_block}.
     * @param ctx the parse tree
     */
    fun exitFunction_seq_block(ctx: Verilog2001Parser.Function_seq_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_assignment}.
     * @param ctx the parse tree
     */
    fun enterVariable_assignment(ctx: Verilog2001Parser.Variable_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_assignment}.
     * @param ctx the parse tree
     */
    fun exitVariable_assignment(ctx: Verilog2001Parser.Variable_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#par_block}.
     * @param ctx the parse tree
     */
    fun enterPar_block(ctx: Verilog2001Parser.Par_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#par_block}.
     * @param ctx the parse tree
     */
    fun exitPar_block(ctx: Verilog2001Parser.Par_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#seq_block}.
     * @param ctx the parse tree
     */
    fun enterSeq_block(ctx: Verilog2001Parser.Seq_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#seq_block}.
     * @param ctx the parse tree
     */
    fun exitSeq_block(ctx: Verilog2001Parser.Seq_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#statement}.
     * @param ctx the parse tree
     */
    fun enterStatement(ctx: Verilog2001Parser.StatementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#statement}.
     * @param ctx the parse tree
     */
    fun exitStatement(ctx: Verilog2001Parser.StatementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#statement_or_null}.
     * @param ctx the parse tree
     */
    fun enterStatement_or_null(ctx: Verilog2001Parser.Statement_or_nullContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#statement_or_null}.
     * @param ctx the parse tree
     */
    fun exitStatement_or_null(ctx: Verilog2001Parser.Statement_or_nullContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_statement}.
     * @param ctx the parse tree
     */
    fun enterFunction_statement(ctx: Verilog2001Parser.Function_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_statement}.
     * @param ctx the parse tree
     */
    fun exitFunction_statement(ctx: Verilog2001Parser.Function_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    fun enterDelay_or_event_control(ctx: Verilog2001Parser.Delay_or_event_controlContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    fun exitDelay_or_event_control(ctx: Verilog2001Parser.Delay_or_event_controlContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay_control}.
     * @param ctx the parse tree
     */
    fun enterDelay_control(ctx: Verilog2001Parser.Delay_controlContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay_control}.
     * @param ctx the parse tree
     */
    fun exitDelay_control(ctx: Verilog2001Parser.Delay_controlContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#disable_statement}.
     * @param ctx the parse tree
     */
    fun enterDisable_statement(ctx: Verilog2001Parser.Disable_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#disable_statement}.
     * @param ctx the parse tree
     */
    fun exitDisable_statement(ctx: Verilog2001Parser.Disable_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_control}.
     * @param ctx the parse tree
     */
    fun enterEvent_control(ctx: Verilog2001Parser.Event_controlContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_control}.
     * @param ctx the parse tree
     */
    fun exitEvent_control(ctx: Verilog2001Parser.Event_controlContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_trigger}.
     * @param ctx the parse tree
     */
    fun enterEvent_trigger(ctx: Verilog2001Parser.Event_triggerContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_trigger}.
     * @param ctx the parse tree
     */
    fun exitEvent_trigger(ctx: Verilog2001Parser.Event_triggerContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_expression}.
     * @param ctx the parse tree
     */
    fun enterEvent_expression(ctx: Verilog2001Parser.Event_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_expression}.
     * @param ctx the parse tree
     */
    fun exitEvent_expression(ctx: Verilog2001Parser.Event_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_primary}.
     * @param ctx the parse tree
     */
    fun enterEvent_primary(ctx: Verilog2001Parser.Event_primaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_primary}.
     * @param ctx the parse tree
     */
    fun exitEvent_primary(ctx: Verilog2001Parser.Event_primaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    fun enterProcedural_timing_control_statement(ctx: Verilog2001Parser.Procedural_timing_control_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    fun exitProcedural_timing_control_statement(ctx: Verilog2001Parser.Procedural_timing_control_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#wait_statement}.
     * @param ctx the parse tree
     */
    fun enterWait_statement(ctx: Verilog2001Parser.Wait_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#wait_statement}.
     * @param ctx the parse tree
     */
    fun exitWait_statement(ctx: Verilog2001Parser.Wait_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
     * @param ctx the parse tree
     */
    fun enterConditional_statement(ctx: Verilog2001Parser.Conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
     * @param ctx the parse tree
     */
    fun exitConditional_statement(ctx: Verilog2001Parser.Conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#if_else_if_statement}.
     * @param ctx the parse tree
     */
    fun enterIf_else_if_statement(ctx: Verilog2001Parser.If_else_if_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#if_else_if_statement}.
     * @param ctx the parse tree
     */
    fun exitIf_else_if_statement(ctx: Verilog2001Parser.If_else_if_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_conditional_statement}.
     * @param ctx the parse tree
     */
    fun enterFunction_conditional_statement(ctx: Verilog2001Parser.Function_conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_conditional_statement}.
     * @param ctx the parse tree
     */
    fun exitFunction_conditional_statement(ctx: Verilog2001Parser.Function_conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_if_else_if_statement}.
     * @param ctx the parse tree
     */
    fun enterFunction_if_else_if_statement(ctx: Verilog2001Parser.Function_if_else_if_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_if_else_if_statement}.
     * @param ctx the parse tree
     */
    fun exitFunction_if_else_if_statement(ctx: Verilog2001Parser.Function_if_else_if_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#case_statement}.
     * @param ctx the parse tree
     */
    fun enterCase_statement(ctx: Verilog2001Parser.Case_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#case_statement}.
     * @param ctx the parse tree
     */
    fun exitCase_statement(ctx: Verilog2001Parser.Case_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#case_item}.
     * @param ctx the parse tree
     */
    fun enterCase_item(ctx: Verilog2001Parser.Case_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#case_item}.
     * @param ctx the parse tree
     */
    fun exitCase_item(ctx: Verilog2001Parser.Case_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_case_statement}.
     * @param ctx the parse tree
     */
    fun enterFunction_case_statement(ctx: Verilog2001Parser.Function_case_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_case_statement}.
     * @param ctx the parse tree
     */
    fun exitFunction_case_statement(ctx: Verilog2001Parser.Function_case_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_case_item}.
     * @param ctx the parse tree
     */
    fun enterFunction_case_item(ctx: Verilog2001Parser.Function_case_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_case_item}.
     * @param ctx the parse tree
     */
    fun exitFunction_case_item(ctx: Verilog2001Parser.Function_case_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_loop_statement}.
     * @param ctx the parse tree
     */
    fun enterFunction_loop_statement(ctx: Verilog2001Parser.Function_loop_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_loop_statement}.
     * @param ctx the parse tree
     */
    fun exitFunction_loop_statement(ctx: Verilog2001Parser.Function_loop_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#loop_statement}.
     * @param ctx the parse tree
     */
    fun enterLoop_statement(ctx: Verilog2001Parser.Loop_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#loop_statement}.
     * @param ctx the parse tree
     */
    fun exitLoop_statement(ctx: Verilog2001Parser.Loop_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_task_enable}.
     * @param ctx the parse tree
     */
    fun enterSystem_task_enable(ctx: Verilog2001Parser.System_task_enableContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_task_enable}.
     * @param ctx the parse tree
     */
    fun exitSystem_task_enable(ctx: Verilog2001Parser.System_task_enableContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_enable}.
     * @param ctx the parse tree
     */
    fun enterTask_enable(ctx: Verilog2001Parser.Task_enableContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_enable}.
     * @param ctx the parse tree
     */
    fun exitTask_enable(ctx: Verilog2001Parser.Task_enableContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_block}.
     * @param ctx the parse tree
     */
    fun enterSpecify_block(ctx: Verilog2001Parser.Specify_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_block}.
     * @param ctx the parse tree
     */
    fun exitSpecify_block(ctx: Verilog2001Parser.Specify_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_item}.
     * @param ctx the parse tree
     */
    fun enterSpecify_item(ctx: Verilog2001Parser.Specify_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_item}.
     * @param ctx the parse tree
     */
    fun exitSpecify_item(ctx: Verilog2001Parser.Specify_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    fun enterPulsestyle_declaration(ctx: Verilog2001Parser.Pulsestyle_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    fun exitPulsestyle_declaration(ctx: Verilog2001Parser.Pulsestyle_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    fun enterShowcancelled_declaration(ctx: Verilog2001Parser.Showcancelled_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    fun exitShowcancelled_declaration(ctx: Verilog2001Parser.Showcancelled_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#path_declaration}.
     * @param ctx the parse tree
     */
    fun enterPath_declaration(ctx: Verilog2001Parser.Path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#path_declaration}.
     * @param ctx the parse tree
     */
    fun exitPath_declaration(ctx: Verilog2001Parser.Path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    fun enterSimple_path_declaration(ctx: Verilog2001Parser.Simple_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    fun exitSimple_path_declaration(ctx: Verilog2001Parser.Simple_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parallel_path_description}.
     * @param ctx the parse tree
     */
    fun enterParallel_path_description(ctx: Verilog2001Parser.Parallel_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parallel_path_description}.
     * @param ctx the parse tree
     */
    fun exitParallel_path_description(ctx: Verilog2001Parser.Parallel_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#full_path_description}.
     * @param ctx the parse tree
     */
    fun enterFull_path_description(ctx: Verilog2001Parser.Full_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#full_path_description}.
     * @param ctx the parse tree
     */
    fun exitFull_path_description(ctx: Verilog2001Parser.Full_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    fun enterList_of_path_inputs(ctx: Verilog2001Parser.List_of_path_inputsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    fun exitList_of_path_inputs(ctx: Verilog2001Parser.List_of_path_inputsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    fun enterList_of_path_outputs(ctx: Verilog2001Parser.List_of_path_outputsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    fun exitList_of_path_outputs(ctx: Verilog2001Parser.List_of_path_outputsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun enterSpecify_input_terminal_descriptor(ctx: Verilog2001Parser.Specify_input_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun exitSpecify_input_terminal_descriptor(ctx: Verilog2001Parser.Specify_input_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun enterSpecify_output_terminal_descriptor(ctx: Verilog2001Parser.Specify_output_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    fun exitSpecify_output_terminal_descriptor(ctx: Verilog2001Parser.Specify_output_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_identifier}.
     * @param ctx the parse tree
     */
    fun enterInput_identifier(ctx: Verilog2001Parser.Input_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_identifier}.
     * @param ctx the parse tree
     */
    fun exitInput_identifier(ctx: Verilog2001Parser.Input_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_identifier}.
     * @param ctx the parse tree
     */
    fun enterOutput_identifier(ctx: Verilog2001Parser.Output_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_identifier}.
     * @param ctx the parse tree
     */
    fun exitOutput_identifier(ctx: Verilog2001Parser.Output_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#path_delay_value}.
     * @param ctx the parse tree
     */
    fun enterPath_delay_value(ctx: Verilog2001Parser.Path_delay_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#path_delay_value}.
     * @param ctx the parse tree
     */
    fun exitPath_delay_value(ctx: Verilog2001Parser.Path_delay_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    fun enterList_of_path_delay_expressions(ctx: Verilog2001Parser.List_of_path_delay_expressionsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    fun exitList_of_path_delay_expressions(ctx: Verilog2001Parser.List_of_path_delay_expressionsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT_path_delay_expression(ctx: Verilog2001Parser.T_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT_path_delay_expression(ctx: Verilog2001Parser.T_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTrise_path_delay_expression(ctx: Verilog2001Parser.Trise_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTrise_path_delay_expression(ctx: Verilog2001Parser.Trise_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTfall_path_delay_expression(ctx: Verilog2001Parser.Tfall_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTfall_path_delay_expression(ctx: Verilog2001Parser.Tfall_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTz_path_delay_expression(ctx: Verilog2001Parser.Tz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTz_path_delay_expression(ctx: Verilog2001Parser.Tz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT01_path_delay_expression(ctx: Verilog2001Parser.T01_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT01_path_delay_expression(ctx: Verilog2001Parser.T01_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT10_path_delay_expression(ctx: Verilog2001Parser.T10_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT10_path_delay_expression(ctx: Verilog2001Parser.T10_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT0z_path_delay_expression(ctx: Verilog2001Parser.T0z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT0z_path_delay_expression(ctx: Verilog2001Parser.T0z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTz1_path_delay_expression(ctx: Verilog2001Parser.Tz1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTz1_path_delay_expression(ctx: Verilog2001Parser.Tz1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT1z_path_delay_expression(ctx: Verilog2001Parser.T1z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT1z_path_delay_expression(ctx: Verilog2001Parser.T1z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTz0_path_delay_expression(ctx: Verilog2001Parser.Tz0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTz0_path_delay_expression(ctx: Verilog2001Parser.Tz0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT0x_path_delay_expression(ctx: Verilog2001Parser.T0x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT0x_path_delay_expression(ctx: Verilog2001Parser.T0x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTx1_path_delay_expression(ctx: Verilog2001Parser.Tx1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTx1_path_delay_expression(ctx: Verilog2001Parser.Tx1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterT1x_path_delay_expression(ctx: Verilog2001Parser.T1x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitT1x_path_delay_expression(ctx: Verilog2001Parser.T1x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTx0_path_delay_expression(ctx: Verilog2001Parser.Tx0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTx0_path_delay_expression(ctx: Verilog2001Parser.Tx0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTxz_path_delay_expression(ctx: Verilog2001Parser.Txz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTxz_path_delay_expression(ctx: Verilog2001Parser.Txz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterTzx_path_delay_expression(ctx: Verilog2001Parser.Tzx_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitTzx_path_delay_expression(ctx: Verilog2001Parser.Tzx_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#path_delay_expression}.
     * @param ctx the parse tree
     */
    fun enterPath_delay_expression(ctx: Verilog2001Parser.Path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#path_delay_expression}.
     * @param ctx the parse tree
     */
    fun exitPath_delay_expression(ctx: Verilog2001Parser.Path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    fun enterEdge_sensitive_path_declaration(ctx: Verilog2001Parser.Edge_sensitive_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    fun exitEdge_sensitive_path_declaration(ctx: Verilog2001Parser.Edge_sensitive_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun enterParallel_edge_sensitive_path_description(ctx: Verilog2001Parser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun exitParallel_edge_sensitive_path_description(ctx: Verilog2001Parser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun enterFull_edge_sensitive_path_description(ctx: Verilog2001Parser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    fun exitFull_edge_sensitive_path_description(ctx: Verilog2001Parser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#data_source_expression}.
     * @param ctx the parse tree
     */
    fun enterData_source_expression(ctx: Verilog2001Parser.Data_source_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#data_source_expression}.
     * @param ctx the parse tree
     */
    fun exitData_source_expression(ctx: Verilog2001Parser.Data_source_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#edge_identifier}.
     * @param ctx the parse tree
     */
    fun enterEdge_identifier(ctx: Verilog2001Parser.Edge_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#edge_identifier}.
     * @param ctx the parse tree
     */
    fun exitEdge_identifier(ctx: Verilog2001Parser.Edge_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    fun enterState_dependent_path_declaration(ctx: Verilog2001Parser.State_dependent_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    fun exitState_dependent_path_declaration(ctx: Verilog2001Parser.State_dependent_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#polarity_operator}.
     * @param ctx the parse tree
     */
    fun enterPolarity_operator(ctx: Verilog2001Parser.Polarity_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#polarity_operator}.
     * @param ctx the parse tree
     */
    fun exitPolarity_operator(ctx: Verilog2001Parser.Polarity_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#checktime_condition}.
     * @param ctx the parse tree
     */
    fun enterChecktime_condition(ctx: Verilog2001Parser.Checktime_conditionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#checktime_condition}.
     * @param ctx the parse tree
     */
    fun exitChecktime_condition(ctx: Verilog2001Parser.Checktime_conditionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delayed_data}.
     * @param ctx the parse tree
     */
    fun enterDelayed_data(ctx: Verilog2001Parser.Delayed_dataContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delayed_data}.
     * @param ctx the parse tree
     */
    fun exitDelayed_data(ctx: Verilog2001Parser.Delayed_dataContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delayed_reference}.
     * @param ctx the parse tree
     */
    fun enterDelayed_reference(ctx: Verilog2001Parser.Delayed_referenceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delayed_reference}.
     * @param ctx the parse tree
     */
    fun exitDelayed_reference(ctx: Verilog2001Parser.Delayed_referenceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#end_edge_offset}.
     * @param ctx the parse tree
     */
    fun enterEnd_edge_offset(ctx: Verilog2001Parser.End_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#end_edge_offset}.
     * @param ctx the parse tree
     */
    fun exitEnd_edge_offset(ctx: Verilog2001Parser.End_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_based_flag}.
     * @param ctx the parse tree
     */
    fun enterEvent_based_flag(ctx: Verilog2001Parser.Event_based_flagContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_based_flag}.
     * @param ctx the parse tree
     */
    fun exitEvent_based_flag(ctx: Verilog2001Parser.Event_based_flagContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#notify_reg}.
     * @param ctx the parse tree
     */
    fun enterNotify_reg(ctx: Verilog2001Parser.Notify_regContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#notify_reg}.
     * @param ctx the parse tree
     */
    fun exitNotify_reg(ctx: Verilog2001Parser.Notify_regContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#remain_active_flag}.
     * @param ctx the parse tree
     */
    fun enterRemain_active_flag(ctx: Verilog2001Parser.Remain_active_flagContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#remain_active_flag}.
     * @param ctx the parse tree
     */
    fun exitRemain_active_flag(ctx: Verilog2001Parser.Remain_active_flagContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#stamptime_condition}.
     * @param ctx the parse tree
     */
    fun enterStamptime_condition(ctx: Verilog2001Parser.Stamptime_conditionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#stamptime_condition}.
     * @param ctx the parse tree
     */
    fun exitStamptime_condition(ctx: Verilog2001Parser.Stamptime_conditionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#start_edge_offset}.
     * @param ctx the parse tree
     */
    fun enterStart_edge_offset(ctx: Verilog2001Parser.Start_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#start_edge_offset}.
     * @param ctx the parse tree
     */
    fun exitStart_edge_offset(ctx: Verilog2001Parser.Start_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#threshold}.
     * @param ctx the parse tree
     */
    fun enterThreshold(ctx: Verilog2001Parser.ThresholdContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#threshold}.
     * @param ctx the parse tree
     */
    fun exitThreshold(ctx: Verilog2001Parser.ThresholdContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#timing_check_limit}.
     * @param ctx the parse tree
     */
    fun enterTiming_check_limit(ctx: Verilog2001Parser.Timing_check_limitContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#timing_check_limit}.
     * @param ctx the parse tree
     */
    fun exitTiming_check_limit(ctx: Verilog2001Parser.Timing_check_limitContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#concatenation}.
     * @param ctx the parse tree
     */
    fun enterConcatenation(ctx: Verilog2001Parser.ConcatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#concatenation}.
     * @param ctx the parse tree
     */
    fun exitConcatenation(ctx: Verilog2001Parser.ConcatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_concatenation}.
     * @param ctx the parse tree
     */
    fun enterConstant_concatenation(ctx: Verilog2001Parser.Constant_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_concatenation}.
     * @param ctx the parse tree
     */
    fun exitConstant_concatenation(ctx: Verilog2001Parser.Constant_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun enterConstant_multiple_concatenation(ctx: Verilog2001Parser.Constant_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun exitConstant_multiple_concatenation(ctx: Verilog2001Parser.Constant_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    fun enterModule_path_concatenation(ctx: Verilog2001Parser.Module_path_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    fun exitModule_path_concatenation(ctx: Verilog2001Parser.Module_path_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun enterModule_path_multiple_concatenation(ctx: Verilog2001Parser.Module_path_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun exitModule_path_multiple_concatenation(ctx: Verilog2001Parser.Module_path_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun enterMultiple_concatenation(ctx: Verilog2001Parser.Multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    fun exitMultiple_concatenation(ctx: Verilog2001Parser.Multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_concatenation}.
     * @param ctx the parse tree
     */
    fun enterNet_concatenation(ctx: Verilog2001Parser.Net_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_concatenation}.
     * @param ctx the parse tree
     */
    fun exitNet_concatenation(ctx: Verilog2001Parser.Net_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_concatenation_value}.
     * @param ctx the parse tree
     */
    fun enterNet_concatenation_value(ctx: Verilog2001Parser.Net_concatenation_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_concatenation_value}.
     * @param ctx the parse tree
     */
    fun exitNet_concatenation_value(ctx: Verilog2001Parser.Net_concatenation_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_concatenation}.
     * @param ctx the parse tree
     */
    fun enterVariable_concatenation(ctx: Verilog2001Parser.Variable_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_concatenation}.
     * @param ctx the parse tree
     */
    fun exitVariable_concatenation(ctx: Verilog2001Parser.Variable_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_concatenation_value}.
     * @param ctx the parse tree
     */
    fun enterVariable_concatenation_value(ctx: Verilog2001Parser.Variable_concatenation_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_concatenation_value}.
     * @param ctx the parse tree
     */
    fun exitVariable_concatenation_value(ctx: Verilog2001Parser.Variable_concatenation_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_function_call}.
     * @param ctx the parse tree
     */
    fun enterConstant_function_call(ctx: Verilog2001Parser.Constant_function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_function_call}.
     * @param ctx the parse tree
     */
    fun exitConstant_function_call(ctx: Verilog2001Parser.Constant_function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_call}.
     * @param ctx the parse tree
     */
    fun enterFunction_call(ctx: Verilog2001Parser.Function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_call}.
     * @param ctx the parse tree
     */
    fun exitFunction_call(ctx: Verilog2001Parser.Function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_function_call}.
     * @param ctx the parse tree
     */
    fun enterSystem_function_call(ctx: Verilog2001Parser.System_function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_function_call}.
     * @param ctx the parse tree
     */
    fun exitSystem_function_call(ctx: Verilog2001Parser.System_function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_function_call}.
     * @param ctx the parse tree
     */
    fun enterGenvar_function_call(ctx: Verilog2001Parser.Genvar_function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_function_call}.
     * @param ctx the parse tree
     */
    fun exitGenvar_function_call(ctx: Verilog2001Parser.Genvar_function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#base_expression}.
     * @param ctx the parse tree
     */
    fun enterBase_expression(ctx: Verilog2001Parser.Base_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#base_expression}.
     * @param ctx the parse tree
     */
    fun exitBase_expression(ctx: Verilog2001Parser.Base_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_base_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_base_expression(ctx: Verilog2001Parser.Constant_base_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_base_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_base_expression(ctx: Verilog2001Parser.Constant_base_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_expression(ctx: Verilog2001Parser.Constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_expression(ctx: Verilog2001Parser.Constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_mintypmax_expression(ctx: Verilog2001Parser.Constant_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_mintypmax_expression(ctx: Verilog2001Parser.Constant_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_range_expression}.
     * @param ctx the parse tree
     */
    fun enterConstant_range_expression(ctx: Verilog2001Parser.Constant_range_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_range_expression}.
     * @param ctx the parse tree
     */
    fun exitConstant_range_expression(ctx: Verilog2001Parser.Constant_range_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterDimension_constant_expression(ctx: Verilog2001Parser.Dimension_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitDimension_constant_expression(ctx: Verilog2001Parser.Dimension_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#expression}.
     * @param ctx the parse tree
     */
    fun enterExpression(ctx: Verilog2001Parser.ExpressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#expression}.
     * @param ctx the parse tree
     */
    fun exitExpression(ctx: Verilog2001Parser.ExpressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#term}.
     * @param ctx the parse tree
     */
    fun enterTerm(ctx: Verilog2001Parser.TermContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#term}.
     * @param ctx the parse tree
     */
    fun exitTerm(ctx: Verilog2001Parser.TermContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterLsb_constant_expression(ctx: Verilog2001Parser.Lsb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitLsb_constant_expression(ctx: Verilog2001Parser.Lsb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun enterMintypmax_expression(ctx: Verilog2001Parser.Mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun exitMintypmax_expression(ctx: Verilog2001Parser.Mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_conditional_expression}.
     * @param ctx the parse tree
     */
    fun enterModule_path_conditional_expression(ctx: Verilog2001Parser.Module_path_conditional_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_conditional_expression}.
     * @param ctx the parse tree
     */
    fun exitModule_path_conditional_expression(ctx: Verilog2001Parser.Module_path_conditional_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_expression}.
     * @param ctx the parse tree
     */
    fun enterModule_path_expression(ctx: Verilog2001Parser.Module_path_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_expression}.
     * @param ctx the parse tree
     */
    fun exitModule_path_expression(ctx: Verilog2001Parser.Module_path_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun enterModule_path_mintypmax_expression(ctx: Verilog2001Parser.Module_path_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    fun exitModule_path_mintypmax_expression(ctx: Verilog2001Parser.Module_path_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterMsb_constant_expression(ctx: Verilog2001Parser.Msb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitMsb_constant_expression(ctx: Verilog2001Parser.Msb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#range_expression}.
     * @param ctx the parse tree
     */
    fun enterRange_expression(ctx: Verilog2001Parser.Range_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#range_expression}.
     * @param ctx the parse tree
     */
    fun exitRange_expression(ctx: Verilog2001Parser.Range_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#width_constant_expression}.
     * @param ctx the parse tree
     */
    fun enterWidth_constant_expression(ctx: Verilog2001Parser.Width_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#width_constant_expression}.
     * @param ctx the parse tree
     */
    fun exitWidth_constant_expression(ctx: Verilog2001Parser.Width_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_primary}.
     * @param ctx the parse tree
     */
    fun enterConstant_primary(ctx: Verilog2001Parser.Constant_primaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_primary}.
     * @param ctx the parse tree
     */
    fun exitConstant_primary(ctx: Verilog2001Parser.Constant_primaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_primary}.
     * @param ctx the parse tree
     */
    fun enterModule_path_primary(ctx: Verilog2001Parser.Module_path_primaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_primary}.
     * @param ctx the parse tree
     */
    fun exitModule_path_primary(ctx: Verilog2001Parser.Module_path_primaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#primary}.
     * @param ctx the parse tree
     */
    fun enterPrimary(ctx: Verilog2001Parser.PrimaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#primary}.
     * @param ctx the parse tree
     */
    fun exitPrimary(ctx: Verilog2001Parser.PrimaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_lvalue}.
     * @param ctx the parse tree
     */
    fun enterNet_lvalue(ctx: Verilog2001Parser.Net_lvalueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_lvalue}.
     * @param ctx the parse tree
     */
    fun exitNet_lvalue(ctx: Verilog2001Parser.Net_lvalueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
     * @param ctx the parse tree
     */
    fun enterVariable_lvalue(ctx: Verilog2001Parser.Variable_lvalueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
     * @param ctx the parse tree
     */
    fun exitVariable_lvalue(ctx: Verilog2001Parser.Variable_lvalueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#unary_operator}.
     * @param ctx the parse tree
     */
    fun enterUnary_operator(ctx: Verilog2001Parser.Unary_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#unary_operator}.
     * @param ctx the parse tree
     */
    fun exitUnary_operator(ctx: Verilog2001Parser.Unary_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#binary_operator}.
     * @param ctx the parse tree
     */
    fun enterBinary_operator(ctx: Verilog2001Parser.Binary_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#binary_operator}.
     * @param ctx the parse tree
     */
    fun exitBinary_operator(ctx: Verilog2001Parser.Binary_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    fun enterUnary_module_path_operator(ctx: Verilog2001Parser.Unary_module_path_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    fun exitUnary_module_path_operator(ctx: Verilog2001Parser.Unary_module_path_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#binary_module_path_operator}.
     * @param ctx the parse tree
     */
    fun enterBinary_module_path_operator(ctx: Verilog2001Parser.Binary_module_path_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#binary_module_path_operator}.
     * @param ctx the parse tree
     */
    fun exitBinary_module_path_operator(ctx: Verilog2001Parser.Binary_module_path_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#number}.
     * @param ctx the parse tree
     */
    fun enterNumber(ctx: Verilog2001Parser.NumberContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#number}.
     * @param ctx the parse tree
     */
    fun exitNumber(ctx: Verilog2001Parser.NumberContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#attribute_instance}.
     * @param ctx the parse tree
     */
    fun enterAttribute_instance(ctx: Verilog2001Parser.Attribute_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#attribute_instance}.
     * @param ctx the parse tree
     */
    fun exitAttribute_instance(ctx: Verilog2001Parser.Attribute_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#attr_spec}.
     * @param ctx the parse tree
     */
    fun enterAttr_spec(ctx: Verilog2001Parser.Attr_specContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#attr_spec}.
     * @param ctx the parse tree
     */
    fun exitAttr_spec(ctx: Verilog2001Parser.Attr_specContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#attr_name}.
     * @param ctx the parse tree
     */
    fun enterAttr_name(ctx: Verilog2001Parser.Attr_nameContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#attr_name}.
     * @param ctx the parse tree
     */
    fun exitAttr_name(ctx: Verilog2001Parser.Attr_nameContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#arrayed_identifier}.
     * @param ctx the parse tree
     */
    fun enterArrayed_identifier(ctx: Verilog2001Parser.Arrayed_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#arrayed_identifier}.
     * @param ctx the parse tree
     */
    fun exitArrayed_identifier(ctx: Verilog2001Parser.Arrayed_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_identifier}.
     * @param ctx the parse tree
     */
    fun enterBlock_identifier(ctx: Verilog2001Parser.Block_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_identifier}.
     * @param ctx the parse tree
     */
    fun exitBlock_identifier(ctx: Verilog2001Parser.Block_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cell_identifier}.
     * @param ctx the parse tree
     */
    fun enterCell_identifier(ctx: Verilog2001Parser.Cell_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cell_identifier}.
     * @param ctx the parse tree
     */
    fun exitCell_identifier(ctx: Verilog2001Parser.Cell_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#config_identifier}.
     * @param ctx the parse tree
     */
    fun enterConfig_identifier(ctx: Verilog2001Parser.Config_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#config_identifier}.
     * @param ctx the parse tree
     */
    fun exitConfig_identifier(ctx: Verilog2001Parser.Config_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#escaped_arrayed_identifier}.
     * @param ctx the parse tree
     */
    fun enterEscaped_arrayed_identifier(ctx: Verilog2001Parser.Escaped_arrayed_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#escaped_arrayed_identifier}.
     * @param ctx the parse tree
     */
    fun exitEscaped_arrayed_identifier(ctx: Verilog2001Parser.Escaped_arrayed_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun enterEscaped_hierarchical_identifier(ctx: Verilog2001Parser.Escaped_hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun exitEscaped_hierarchical_identifier(ctx: Verilog2001Parser.Escaped_hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_identifier}.
     * @param ctx the parse tree
     */
    fun enterEvent_identifier(ctx: Verilog2001Parser.Event_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_identifier}.
     * @param ctx the parse tree
     */
    fun exitEvent_identifier(ctx: Verilog2001Parser.Event_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_identifier}.
     * @param ctx the parse tree
     */
    fun enterFunction_identifier(ctx: Verilog2001Parser.Function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_identifier}.
     * @param ctx the parse tree
     */
    fun exitFunction_identifier(ctx: Verilog2001Parser.Function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterGate_instance_identifier(ctx: Verilog2001Parser.Gate_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitGate_instance_identifier(ctx: Verilog2001Parser.Gate_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    fun enterGenerate_block_identifier(ctx: Verilog2001Parser.Generate_block_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    fun exitGenerate_block_identifier(ctx: Verilog2001Parser.Generate_block_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_function_identifier}.
     * @param ctx the parse tree
     */
    fun enterGenvar_function_identifier(ctx: Verilog2001Parser.Genvar_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_function_identifier}.
     * @param ctx the parse tree
     */
    fun exitGenvar_function_identifier(ctx: Verilog2001Parser.Genvar_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_identifier}.
     * @param ctx the parse tree
     */
    fun enterGenvar_identifier(ctx: Verilog2001Parser.Genvar_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_identifier}.
     * @param ctx the parse tree
     */
    fun exitGenvar_identifier(ctx: Verilog2001Parser.Genvar_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_block_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_block_identifier(ctx: Verilog2001Parser.Hierarchical_block_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_block_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_block_identifier(ctx: Verilog2001Parser.Hierarchical_block_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_event_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_event_identifier(ctx: Verilog2001Parser.Hierarchical_event_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_event_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_event_identifier(ctx: Verilog2001Parser.Hierarchical_event_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_function_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_function_identifier(ctx: Verilog2001Parser.Hierarchical_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_function_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_function_identifier(ctx: Verilog2001Parser.Hierarchical_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_identifier(ctx: Verilog2001Parser.Hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_identifier(ctx: Verilog2001Parser.Hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_net_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_net_identifier(ctx: Verilog2001Parser.Hierarchical_net_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_net_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_net_identifier(ctx: Verilog2001Parser.Hierarchical_net_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_variable_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_variable_identifier(ctx: Verilog2001Parser.Hierarchical_variable_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_variable_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_variable_identifier(ctx: Verilog2001Parser.Hierarchical_variable_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_task_identifier}.
     * @param ctx the parse tree
     */
    fun enterHierarchical_task_identifier(ctx: Verilog2001Parser.Hierarchical_task_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_task_identifier}.
     * @param ctx the parse tree
     */
    fun exitHierarchical_task_identifier(ctx: Verilog2001Parser.Hierarchical_task_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#identifier}.
     * @param ctx the parse tree
     */
    fun enterIdentifier(ctx: Verilog2001Parser.IdentifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#identifier}.
     * @param ctx the parse tree
     */
    fun exitIdentifier(ctx: Verilog2001Parser.IdentifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inout_port_identifier}.
     * @param ctx the parse tree
     */
    fun enterInout_port_identifier(ctx: Verilog2001Parser.Inout_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inout_port_identifier}.
     * @param ctx the parse tree
     */
    fun exitInout_port_identifier(ctx: Verilog2001Parser.Inout_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_port_identifier}.
     * @param ctx the parse tree
     */
    fun enterInput_port_identifier(ctx: Verilog2001Parser.Input_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_port_identifier}.
     * @param ctx the parse tree
     */
    fun exitInput_port_identifier(ctx: Verilog2001Parser.Input_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterInstance_identifier(ctx: Verilog2001Parser.Instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitInstance_identifier(ctx: Verilog2001Parser.Instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#library_identifier}.
     * @param ctx the parse tree
     */
    fun enterLibrary_identifier(ctx: Verilog2001Parser.Library_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#library_identifier}.
     * @param ctx the parse tree
     */
    fun exitLibrary_identifier(ctx: Verilog2001Parser.Library_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#memory_identifier}.
     * @param ctx the parse tree
     */
    fun enterMemory_identifier(ctx: Verilog2001Parser.Memory_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#memory_identifier}.
     * @param ctx the parse tree
     */
    fun exitMemory_identifier(ctx: Verilog2001Parser.Memory_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_identifier}.
     * @param ctx the parse tree
     */
    fun enterModule_identifier(ctx: Verilog2001Parser.Module_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_identifier}.
     * @param ctx the parse tree
     */
    fun exitModule_identifier(ctx: Verilog2001Parser.Module_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterModule_instance_identifier(ctx: Verilog2001Parser.Module_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitModule_instance_identifier(ctx: Verilog2001Parser.Module_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_identifier}.
     * @param ctx the parse tree
     */
    fun enterNet_identifier(ctx: Verilog2001Parser.Net_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_identifier}.
     * @param ctx the parse tree
     */
    fun exitNet_identifier(ctx: Verilog2001Parser.Net_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_port_identifier}.
     * @param ctx the parse tree
     */
    fun enterOutput_port_identifier(ctx: Verilog2001Parser.Output_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_port_identifier}.
     * @param ctx the parse tree
     */
    fun exitOutput_port_identifier(ctx: Verilog2001Parser.Output_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_identifier}.
     * @param ctx the parse tree
     */
    fun enterParameter_identifier(ctx: Verilog2001Parser.Parameter_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_identifier}.
     * @param ctx the parse tree
     */
    fun exitParameter_identifier(ctx: Verilog2001Parser.Parameter_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_identifier}.
     * @param ctx the parse tree
     */
    fun enterPort_identifier(ctx: Verilog2001Parser.Port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_identifier}.
     * @param ctx the parse tree
     */
    fun exitPort_identifier(ctx: Verilog2001Parser.Port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#real_identifier}.
     * @param ctx the parse tree
     */
    fun enterReal_identifier(ctx: Verilog2001Parser.Real_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#real_identifier}.
     * @param ctx the parse tree
     */
    fun exitReal_identifier(ctx: Verilog2001Parser.Real_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_arrayed_identifier}.
     * @param ctx the parse tree
     */
    fun enterSimple_arrayed_identifier(ctx: Verilog2001Parser.Simple_arrayed_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_arrayed_identifier}.
     * @param ctx the parse tree
     */
    fun exitSimple_arrayed_identifier(ctx: Verilog2001Parser.Simple_arrayed_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun enterSimple_hierarchical_identifier(ctx: Verilog2001Parser.Simple_hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    fun exitSimple_hierarchical_identifier(ctx: Verilog2001Parser.Simple_hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specparam_identifier}.
     * @param ctx the parse tree
     */
    fun enterSpecparam_identifier(ctx: Verilog2001Parser.Specparam_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specparam_identifier}.
     * @param ctx the parse tree
     */
    fun exitSpecparam_identifier(ctx: Verilog2001Parser.Specparam_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_function_identifier}.
     * @param ctx the parse tree
     */
    fun enterSystem_function_identifier(ctx: Verilog2001Parser.System_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_function_identifier}.
     * @param ctx the parse tree
     */
    fun exitSystem_function_identifier(ctx: Verilog2001Parser.System_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_task_identifier}.
     * @param ctx the parse tree
     */
    fun enterSystem_task_identifier(ctx: Verilog2001Parser.System_task_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_task_identifier}.
     * @param ctx the parse tree
     */
    fun exitSystem_task_identifier(ctx: Verilog2001Parser.System_task_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_identifier}.
     * @param ctx the parse tree
     */
    fun enterTask_identifier(ctx: Verilog2001Parser.Task_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_identifier}.
     * @param ctx the parse tree
     */
    fun exitTask_identifier(ctx: Verilog2001Parser.Task_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#terminal_identifier}.
     * @param ctx the parse tree
     */
    fun enterTerminal_identifier(ctx: Verilog2001Parser.Terminal_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#terminal_identifier}.
     * @param ctx the parse tree
     */
    fun exitTerminal_identifier(ctx: Verilog2001Parser.Terminal_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#text_macro_identifier}.
     * @param ctx the parse tree
     */
    fun enterText_macro_identifier(ctx: Verilog2001Parser.Text_macro_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#text_macro_identifier}.
     * @param ctx the parse tree
     */
    fun exitText_macro_identifier(ctx: Verilog2001Parser.Text_macro_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    fun enterTopmodule_identifier(ctx: Verilog2001Parser.Topmodule_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    fun exitTopmodule_identifier(ctx: Verilog2001Parser.Topmodule_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#udp_identifier}.
     * @param ctx the parse tree
     */
    fun enterUdp_identifier(ctx: Verilog2001Parser.Udp_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#udp_identifier}.
     * @param ctx the parse tree
     */
    fun exitUdp_identifier(ctx: Verilog2001Parser.Udp_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    fun enterUdp_instance_identifier(ctx: Verilog2001Parser.Udp_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    fun exitUdp_instance_identifier(ctx: Verilog2001Parser.Udp_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_identifier}.
     * @param ctx the parse tree
     */
    fun enterVariable_identifier(ctx: Verilog2001Parser.Variable_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_identifier}.
     * @param ctx the parse tree
     */
    fun exitVariable_identifier(ctx: Verilog2001Parser.Variable_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_branch}.
     * @param ctx the parse tree
     */
    fun enterSimple_hierarchical_branch(ctx: Verilog2001Parser.Simple_hierarchical_branchContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_branch}.
     * @param ctx the parse tree
     */
    fun exitSimple_hierarchical_branch(ctx: Verilog2001Parser.Simple_hierarchical_branchContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_branch}.
     * @param ctx the parse tree
     */
    fun enterEscaped_hierarchical_branch(ctx: Verilog2001Parser.Escaped_hierarchical_branchContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_branch}.
     * @param ctx the parse tree
     */
    fun exitEscaped_hierarchical_branch(ctx: Verilog2001Parser.Escaped_hierarchical_branchContext)

    override fun asSuspend(): SuspendVerilog2001Listener = object : SuspendVerilog2001Listener {
        override suspend fun enterConfig_declaration(ctx: Verilog2001Parser.Config_declarationContext) =
            this@Verilog2001Listener.enterConfig_declaration(ctx)

        override suspend fun exitConfig_declaration(ctx: Verilog2001Parser.Config_declarationContext) =
            this@Verilog2001Listener.exitConfig_declaration(ctx)

        override suspend fun enterDesign_statement(ctx: Verilog2001Parser.Design_statementContext) =
            this@Verilog2001Listener.enterDesign_statement(ctx)

        override suspend fun exitDesign_statement(ctx: Verilog2001Parser.Design_statementContext) =
            this@Verilog2001Listener.exitDesign_statement(ctx)

        override suspend fun enterConfig_rule_statement(ctx: Verilog2001Parser.Config_rule_statementContext) =
            this@Verilog2001Listener.enterConfig_rule_statement(ctx)

        override suspend fun exitConfig_rule_statement(ctx: Verilog2001Parser.Config_rule_statementContext) =
            this@Verilog2001Listener.exitConfig_rule_statement(ctx)

        override suspend fun enterDefault_clause(ctx: Verilog2001Parser.Default_clauseContext) =
            this@Verilog2001Listener.enterDefault_clause(ctx)

        override suspend fun exitDefault_clause(ctx: Verilog2001Parser.Default_clauseContext) =
            this@Verilog2001Listener.exitDefault_clause(ctx)

        override suspend fun enterInst_clause(ctx: Verilog2001Parser.Inst_clauseContext) =
            this@Verilog2001Listener.enterInst_clause(ctx)

        override suspend fun exitInst_clause(ctx: Verilog2001Parser.Inst_clauseContext) =
            this@Verilog2001Listener.exitInst_clause(ctx)

        override suspend fun enterInst_name(ctx: Verilog2001Parser.Inst_nameContext) =
            this@Verilog2001Listener.enterInst_name(ctx)

        override suspend fun exitInst_name(ctx: Verilog2001Parser.Inst_nameContext) =
            this@Verilog2001Listener.exitInst_name(ctx)

        override suspend fun enterLiblist_clause(ctx: Verilog2001Parser.Liblist_clauseContext) =
            this@Verilog2001Listener.enterLiblist_clause(ctx)

        override suspend fun exitLiblist_clause(ctx: Verilog2001Parser.Liblist_clauseContext) =
            this@Verilog2001Listener.exitLiblist_clause(ctx)

        override suspend fun enterCell_clause(ctx: Verilog2001Parser.Cell_clauseContext) =
            this@Verilog2001Listener.enterCell_clause(ctx)

        override suspend fun exitCell_clause(ctx: Verilog2001Parser.Cell_clauseContext) =
            this@Verilog2001Listener.exitCell_clause(ctx)

        override suspend fun enterUse_clause(ctx: Verilog2001Parser.Use_clauseContext) =
            this@Verilog2001Listener.enterUse_clause(ctx)

        override suspend fun exitUse_clause(ctx: Verilog2001Parser.Use_clauseContext) =
            this@Verilog2001Listener.exitUse_clause(ctx)

        override suspend fun enterSource_text(ctx: Verilog2001Parser.Source_textContext) =
            this@Verilog2001Listener.enterSource_text(ctx)

        override suspend fun exitSource_text(ctx: Verilog2001Parser.Source_textContext) =
            this@Verilog2001Listener.exitSource_text(ctx)

        override suspend fun enterDescription(ctx: Verilog2001Parser.DescriptionContext) =
            this@Verilog2001Listener.enterDescription(ctx)

        override suspend fun exitDescription(ctx: Verilog2001Parser.DescriptionContext) =
            this@Verilog2001Listener.exitDescription(ctx)

        override suspend fun enterModule_declaration(ctx: Verilog2001Parser.Module_declarationContext) =
            this@Verilog2001Listener.enterModule_declaration(ctx)

        override suspend fun exitModule_declaration(ctx: Verilog2001Parser.Module_declarationContext) =
            this@Verilog2001Listener.exitModule_declaration(ctx)

        override suspend fun enterModule_keyword(ctx: Verilog2001Parser.Module_keywordContext) =
            this@Verilog2001Listener.enterModule_keyword(ctx)

        override suspend fun exitModule_keyword(ctx: Verilog2001Parser.Module_keywordContext) =
            this@Verilog2001Listener.exitModule_keyword(ctx)

        override suspend fun enterModule_parameter_port_list(ctx: Verilog2001Parser.Module_parameter_port_listContext) =
            this@Verilog2001Listener.enterModule_parameter_port_list(ctx)

        override suspend fun exitModule_parameter_port_list(ctx: Verilog2001Parser.Module_parameter_port_listContext) =
            this@Verilog2001Listener.exitModule_parameter_port_list(ctx)

        override suspend fun enterList_of_ports(ctx: Verilog2001Parser.List_of_portsContext) =
            this@Verilog2001Listener.enterList_of_ports(ctx)

        override suspend fun exitList_of_ports(ctx: Verilog2001Parser.List_of_portsContext) =
            this@Verilog2001Listener.exitList_of_ports(ctx)

        override suspend fun enterList_of_port_declarations(ctx: Verilog2001Parser.List_of_port_declarationsContext) =
            this@Verilog2001Listener.enterList_of_port_declarations(ctx)

        override suspend fun exitList_of_port_declarations(ctx: Verilog2001Parser.List_of_port_declarationsContext) =
            this@Verilog2001Listener.exitList_of_port_declarations(ctx)

        override suspend fun enterPort(ctx: Verilog2001Parser.PortContext) = this@Verilog2001Listener.enterPort(ctx)
        override suspend fun exitPort(ctx: Verilog2001Parser.PortContext) = this@Verilog2001Listener.exitPort(ctx)

        override suspend fun enterPort_expression(ctx: Verilog2001Parser.Port_expressionContext) =
            this@Verilog2001Listener.enterPort_expression(ctx)

        override suspend fun exitPort_expression(ctx: Verilog2001Parser.Port_expressionContext) =
            this@Verilog2001Listener.exitPort_expression(ctx)

        override suspend fun enterPort_reference(ctx: Verilog2001Parser.Port_referenceContext) =
            this@Verilog2001Listener.enterPort_reference(ctx)

        override suspend fun exitPort_reference(ctx: Verilog2001Parser.Port_referenceContext) =
            this@Verilog2001Listener.exitPort_reference(ctx)

        override suspend fun enterPort_declaration(ctx: Verilog2001Parser.Port_declarationContext) =
            this@Verilog2001Listener.enterPort_declaration(ctx)

        override suspend fun exitPort_declaration(ctx: Verilog2001Parser.Port_declarationContext) =
            this@Verilog2001Listener.exitPort_declaration(ctx)

        override suspend fun enterModule_item(ctx: Verilog2001Parser.Module_itemContext) =
            this@Verilog2001Listener.enterModule_item(ctx)

        override suspend fun exitModule_item(ctx: Verilog2001Parser.Module_itemContext) =
            this@Verilog2001Listener.exitModule_item(ctx)

        override suspend fun enterModule_or_generate_item(ctx: Verilog2001Parser.Module_or_generate_itemContext) =
            this@Verilog2001Listener.enterModule_or_generate_item(ctx)

        override suspend fun exitModule_or_generate_item(ctx: Verilog2001Parser.Module_or_generate_itemContext) =
            this@Verilog2001Listener.exitModule_or_generate_item(ctx)

        override suspend fun enterNon_port_module_item(ctx: Verilog2001Parser.Non_port_module_itemContext) =
            this@Verilog2001Listener.enterNon_port_module_item(ctx)

        override suspend fun exitNon_port_module_item(ctx: Verilog2001Parser.Non_port_module_itemContext) =
            this@Verilog2001Listener.exitNon_port_module_item(ctx)

        override suspend fun enterModule_or_generate_item_declaration(ctx: Verilog2001Parser.Module_or_generate_item_declarationContext) =
            this@Verilog2001Listener.enterModule_or_generate_item_declaration(ctx)

        override suspend fun exitModule_or_generate_item_declaration(ctx: Verilog2001Parser.Module_or_generate_item_declarationContext) =
            this@Verilog2001Listener.exitModule_or_generate_item_declaration(ctx)

        override suspend fun enterParameter_override(ctx: Verilog2001Parser.Parameter_overrideContext) =
            this@Verilog2001Listener.enterParameter_override(ctx)

        override suspend fun exitParameter_override(ctx: Verilog2001Parser.Parameter_overrideContext) =
            this@Verilog2001Listener.exitParameter_override(ctx)

        override suspend fun enterLocal_parameter_declaration(ctx: Verilog2001Parser.Local_parameter_declarationContext) =
            this@Verilog2001Listener.enterLocal_parameter_declaration(ctx)

        override suspend fun exitLocal_parameter_declaration(ctx: Verilog2001Parser.Local_parameter_declarationContext) =
            this@Verilog2001Listener.exitLocal_parameter_declaration(ctx)

        override suspend fun enterParameter_declaration(ctx: Verilog2001Parser.Parameter_declarationContext) =
            this@Verilog2001Listener.enterParameter_declaration(ctx)

        override suspend fun exitParameter_declaration(ctx: Verilog2001Parser.Parameter_declarationContext) =
            this@Verilog2001Listener.exitParameter_declaration(ctx)

        override suspend fun enterParameter_declaration_(ctx: Verilog2001Parser.Parameter_declaration_Context) =
            this@Verilog2001Listener.enterParameter_declaration_(ctx)

        override suspend fun exitParameter_declaration_(ctx: Verilog2001Parser.Parameter_declaration_Context) =
            this@Verilog2001Listener.exitParameter_declaration_(ctx)

        override suspend fun enterSpecparam_declaration(ctx: Verilog2001Parser.Specparam_declarationContext) =
            this@Verilog2001Listener.enterSpecparam_declaration(ctx)

        override suspend fun exitSpecparam_declaration(ctx: Verilog2001Parser.Specparam_declarationContext) =
            this@Verilog2001Listener.exitSpecparam_declaration(ctx)

        override suspend fun enterInout_declaration(ctx: Verilog2001Parser.Inout_declarationContext) =
            this@Verilog2001Listener.enterInout_declaration(ctx)

        override suspend fun exitInout_declaration(ctx: Verilog2001Parser.Inout_declarationContext) =
            this@Verilog2001Listener.exitInout_declaration(ctx)

        override suspend fun enterInput_declaration(ctx: Verilog2001Parser.Input_declarationContext) =
            this@Verilog2001Listener.enterInput_declaration(ctx)

        override suspend fun exitInput_declaration(ctx: Verilog2001Parser.Input_declarationContext) =
            this@Verilog2001Listener.exitInput_declaration(ctx)

        override suspend fun enterOutput_declaration(ctx: Verilog2001Parser.Output_declarationContext) =
            this@Verilog2001Listener.enterOutput_declaration(ctx)

        override suspend fun exitOutput_declaration(ctx: Verilog2001Parser.Output_declarationContext) =
            this@Verilog2001Listener.exitOutput_declaration(ctx)

        override suspend fun enterEvent_declaration(ctx: Verilog2001Parser.Event_declarationContext) =
            this@Verilog2001Listener.enterEvent_declaration(ctx)

        override suspend fun exitEvent_declaration(ctx: Verilog2001Parser.Event_declarationContext) =
            this@Verilog2001Listener.exitEvent_declaration(ctx)

        override suspend fun enterGenvar_declaration(ctx: Verilog2001Parser.Genvar_declarationContext) =
            this@Verilog2001Listener.enterGenvar_declaration(ctx)

        override suspend fun exitGenvar_declaration(ctx: Verilog2001Parser.Genvar_declarationContext) =
            this@Verilog2001Listener.exitGenvar_declaration(ctx)

        override suspend fun enterInteger_declaration(ctx: Verilog2001Parser.Integer_declarationContext) =
            this@Verilog2001Listener.enterInteger_declaration(ctx)

        override suspend fun exitInteger_declaration(ctx: Verilog2001Parser.Integer_declarationContext) =
            this@Verilog2001Listener.exitInteger_declaration(ctx)

        override suspend fun enterTime_declaration(ctx: Verilog2001Parser.Time_declarationContext) =
            this@Verilog2001Listener.enterTime_declaration(ctx)

        override suspend fun exitTime_declaration(ctx: Verilog2001Parser.Time_declarationContext) =
            this@Verilog2001Listener.exitTime_declaration(ctx)

        override suspend fun enterReal_declaration(ctx: Verilog2001Parser.Real_declarationContext) =
            this@Verilog2001Listener.enterReal_declaration(ctx)

        override suspend fun exitReal_declaration(ctx: Verilog2001Parser.Real_declarationContext) =
            this@Verilog2001Listener.exitReal_declaration(ctx)

        override suspend fun enterRealtime_declaration(ctx: Verilog2001Parser.Realtime_declarationContext) =
            this@Verilog2001Listener.enterRealtime_declaration(ctx)

        override suspend fun exitRealtime_declaration(ctx: Verilog2001Parser.Realtime_declarationContext) =
            this@Verilog2001Listener.exitRealtime_declaration(ctx)

        override suspend fun enterReg_declaration(ctx: Verilog2001Parser.Reg_declarationContext) =
            this@Verilog2001Listener.enterReg_declaration(ctx)

        override suspend fun exitReg_declaration(ctx: Verilog2001Parser.Reg_declarationContext) =
            this@Verilog2001Listener.exitReg_declaration(ctx)

        override suspend fun enterNet_declaration(ctx: Verilog2001Parser.Net_declarationContext) =
            this@Verilog2001Listener.enterNet_declaration(ctx)

        override suspend fun exitNet_declaration(ctx: Verilog2001Parser.Net_declarationContext) =
            this@Verilog2001Listener.exitNet_declaration(ctx)

        override suspend fun enterNet_type(ctx: Verilog2001Parser.Net_typeContext) =
            this@Verilog2001Listener.enterNet_type(ctx)

        override suspend fun exitNet_type(ctx: Verilog2001Parser.Net_typeContext) =
            this@Verilog2001Listener.exitNet_type(ctx)

        override suspend fun enterOutput_variable_type(ctx: Verilog2001Parser.Output_variable_typeContext) =
            this@Verilog2001Listener.enterOutput_variable_type(ctx)

        override suspend fun exitOutput_variable_type(ctx: Verilog2001Parser.Output_variable_typeContext) =
            this@Verilog2001Listener.exitOutput_variable_type(ctx)

        override suspend fun enterReal_type(ctx: Verilog2001Parser.Real_typeContext) =
            this@Verilog2001Listener.enterReal_type(ctx)

        override suspend fun exitReal_type(ctx: Verilog2001Parser.Real_typeContext) =
            this@Verilog2001Listener.exitReal_type(ctx)

        override suspend fun enterVariable_type(ctx: Verilog2001Parser.Variable_typeContext) =
            this@Verilog2001Listener.enterVariable_type(ctx)

        override suspend fun exitVariable_type(ctx: Verilog2001Parser.Variable_typeContext) =
            this@Verilog2001Listener.exitVariable_type(ctx)

        override suspend fun enterDrive_strength(ctx: Verilog2001Parser.Drive_strengthContext) =
            this@Verilog2001Listener.enterDrive_strength(ctx)

        override suspend fun exitDrive_strength(ctx: Verilog2001Parser.Drive_strengthContext) =
            this@Verilog2001Listener.exitDrive_strength(ctx)

        override suspend fun enterStrength0(ctx: Verilog2001Parser.Strength0Context) =
            this@Verilog2001Listener.enterStrength0(ctx)

        override suspend fun exitStrength0(ctx: Verilog2001Parser.Strength0Context) =
            this@Verilog2001Listener.exitStrength0(ctx)

        override suspend fun enterStrength1(ctx: Verilog2001Parser.Strength1Context) =
            this@Verilog2001Listener.enterStrength1(ctx)

        override suspend fun exitStrength1(ctx: Verilog2001Parser.Strength1Context) =
            this@Verilog2001Listener.exitStrength1(ctx)

        override suspend fun enterCharge_strength(ctx: Verilog2001Parser.Charge_strengthContext) =
            this@Verilog2001Listener.enterCharge_strength(ctx)

        override suspend fun exitCharge_strength(ctx: Verilog2001Parser.Charge_strengthContext) =
            this@Verilog2001Listener.exitCharge_strength(ctx)

        override suspend fun enterDelay3(ctx: Verilog2001Parser.Delay3Context) =
            this@Verilog2001Listener.enterDelay3(ctx)

        override suspend fun exitDelay3(ctx: Verilog2001Parser.Delay3Context) = this@Verilog2001Listener.exitDelay3(ctx)

        override suspend fun enterDelay2(ctx: Verilog2001Parser.Delay2Context) =
            this@Verilog2001Listener.enterDelay2(ctx)

        override suspend fun exitDelay2(ctx: Verilog2001Parser.Delay2Context) = this@Verilog2001Listener.exitDelay2(ctx)

        override suspend fun enterDelay_value(ctx: Verilog2001Parser.Delay_valueContext) =
            this@Verilog2001Listener.enterDelay_value(ctx)

        override suspend fun exitDelay_value(ctx: Verilog2001Parser.Delay_valueContext) =
            this@Verilog2001Listener.exitDelay_value(ctx)

        override suspend fun enterList_of_event_identifiers(ctx: Verilog2001Parser.List_of_event_identifiersContext) =
            this@Verilog2001Listener.enterList_of_event_identifiers(ctx)

        override suspend fun exitList_of_event_identifiers(ctx: Verilog2001Parser.List_of_event_identifiersContext) =
            this@Verilog2001Listener.exitList_of_event_identifiers(ctx)

        override suspend fun enterList_of_net_identifiers(ctx: Verilog2001Parser.List_of_net_identifiersContext) =
            this@Verilog2001Listener.enterList_of_net_identifiers(ctx)

        override suspend fun exitList_of_net_identifiers(ctx: Verilog2001Parser.List_of_net_identifiersContext) =
            this@Verilog2001Listener.exitList_of_net_identifiers(ctx)

        override suspend fun enterList_of_genvar_identifiers(ctx: Verilog2001Parser.List_of_genvar_identifiersContext) =
            this@Verilog2001Listener.enterList_of_genvar_identifiers(ctx)

        override suspend fun exitList_of_genvar_identifiers(ctx: Verilog2001Parser.List_of_genvar_identifiersContext) =
            this@Verilog2001Listener.exitList_of_genvar_identifiers(ctx)

        override suspend fun enterList_of_port_identifiers(ctx: Verilog2001Parser.List_of_port_identifiersContext) =
            this@Verilog2001Listener.enterList_of_port_identifiers(ctx)

        override suspend fun exitList_of_port_identifiers(ctx: Verilog2001Parser.List_of_port_identifiersContext) =
            this@Verilog2001Listener.exitList_of_port_identifiers(ctx)

        override suspend fun enterList_of_net_decl_assignments(ctx: Verilog2001Parser.List_of_net_decl_assignmentsContext) =
            this@Verilog2001Listener.enterList_of_net_decl_assignments(ctx)

        override suspend fun exitList_of_net_decl_assignments(ctx: Verilog2001Parser.List_of_net_decl_assignmentsContext) =
            this@Verilog2001Listener.exitList_of_net_decl_assignments(ctx)

        override suspend fun enterList_of_param_assignments(ctx: Verilog2001Parser.List_of_param_assignmentsContext) =
            this@Verilog2001Listener.enterList_of_param_assignments(ctx)

        override suspend fun exitList_of_param_assignments(ctx: Verilog2001Parser.List_of_param_assignmentsContext) =
            this@Verilog2001Listener.exitList_of_param_assignments(ctx)

        override suspend fun enterList_of_specparam_assignments(ctx: Verilog2001Parser.List_of_specparam_assignmentsContext) =
            this@Verilog2001Listener.enterList_of_specparam_assignments(ctx)

        override suspend fun exitList_of_specparam_assignments(ctx: Verilog2001Parser.List_of_specparam_assignmentsContext) =
            this@Verilog2001Listener.exitList_of_specparam_assignments(ctx)

        override suspend fun enterList_of_real_identifiers(ctx: Verilog2001Parser.List_of_real_identifiersContext) =
            this@Verilog2001Listener.enterList_of_real_identifiers(ctx)

        override suspend fun exitList_of_real_identifiers(ctx: Verilog2001Parser.List_of_real_identifiersContext) =
            this@Verilog2001Listener.exitList_of_real_identifiers(ctx)

        override suspend fun enterList_of_variable_identifiers(ctx: Verilog2001Parser.List_of_variable_identifiersContext) =
            this@Verilog2001Listener.enterList_of_variable_identifiers(ctx)

        override suspend fun exitList_of_variable_identifiers(ctx: Verilog2001Parser.List_of_variable_identifiersContext) =
            this@Verilog2001Listener.exitList_of_variable_identifiers(ctx)

        override suspend fun enterList_of_variable_port_identifiers(ctx: Verilog2001Parser.List_of_variable_port_identifiersContext) =
            this@Verilog2001Listener.enterList_of_variable_port_identifiers(ctx)

        override suspend fun exitList_of_variable_port_identifiers(ctx: Verilog2001Parser.List_of_variable_port_identifiersContext) =
            this@Verilog2001Listener.exitList_of_variable_port_identifiers(ctx)

        override suspend fun enterNet_decl_assignment(ctx: Verilog2001Parser.Net_decl_assignmentContext) =
            this@Verilog2001Listener.enterNet_decl_assignment(ctx)

        override suspend fun exitNet_decl_assignment(ctx: Verilog2001Parser.Net_decl_assignmentContext) =
            this@Verilog2001Listener.exitNet_decl_assignment(ctx)

        override suspend fun enterParam_assignment(ctx: Verilog2001Parser.Param_assignmentContext) =
            this@Verilog2001Listener.enterParam_assignment(ctx)

        override suspend fun exitParam_assignment(ctx: Verilog2001Parser.Param_assignmentContext) =
            this@Verilog2001Listener.exitParam_assignment(ctx)

        override suspend fun enterSpecparam_assignment(ctx: Verilog2001Parser.Specparam_assignmentContext) =
            this@Verilog2001Listener.enterSpecparam_assignment(ctx)

        override suspend fun exitSpecparam_assignment(ctx: Verilog2001Parser.Specparam_assignmentContext) =
            this@Verilog2001Listener.exitSpecparam_assignment(ctx)

        override suspend fun enterPulse_control_specparam(ctx: Verilog2001Parser.Pulse_control_specparamContext) =
            this@Verilog2001Listener.enterPulse_control_specparam(ctx)

        override suspend fun exitPulse_control_specparam(ctx: Verilog2001Parser.Pulse_control_specparamContext) =
            this@Verilog2001Listener.exitPulse_control_specparam(ctx)

        override suspend fun enterError_limit_value(ctx: Verilog2001Parser.Error_limit_valueContext) =
            this@Verilog2001Listener.enterError_limit_value(ctx)

        override suspend fun exitError_limit_value(ctx: Verilog2001Parser.Error_limit_valueContext) =
            this@Verilog2001Listener.exitError_limit_value(ctx)

        override suspend fun enterReject_limit_value(ctx: Verilog2001Parser.Reject_limit_valueContext) =
            this@Verilog2001Listener.enterReject_limit_value(ctx)

        override suspend fun exitReject_limit_value(ctx: Verilog2001Parser.Reject_limit_valueContext) =
            this@Verilog2001Listener.exitReject_limit_value(ctx)

        override suspend fun enterLimit_value(ctx: Verilog2001Parser.Limit_valueContext) =
            this@Verilog2001Listener.enterLimit_value(ctx)

        override suspend fun exitLimit_value(ctx: Verilog2001Parser.Limit_valueContext) =
            this@Verilog2001Listener.exitLimit_value(ctx)

        override suspend fun enterDimension(ctx: Verilog2001Parser.DimensionContext) =
            this@Verilog2001Listener.enterDimension(ctx)

        override suspend fun exitDimension(ctx: Verilog2001Parser.DimensionContext) =
            this@Verilog2001Listener.exitDimension(ctx)

        override suspend fun enterRange(ctx: Verilog2001Parser.RangeContext) = this@Verilog2001Listener.enterRange(ctx)
        override suspend fun exitRange(ctx: Verilog2001Parser.RangeContext) = this@Verilog2001Listener.exitRange(ctx)

        override suspend fun enterFunction_declaration(ctx: Verilog2001Parser.Function_declarationContext) =
            this@Verilog2001Listener.enterFunction_declaration(ctx)

        override suspend fun exitFunction_declaration(ctx: Verilog2001Parser.Function_declarationContext) =
            this@Verilog2001Listener.exitFunction_declaration(ctx)

        override suspend fun enterFunction_item_declaration(ctx: Verilog2001Parser.Function_item_declarationContext) =
            this@Verilog2001Listener.enterFunction_item_declaration(ctx)

        override suspend fun exitFunction_item_declaration(ctx: Verilog2001Parser.Function_item_declarationContext) =
            this@Verilog2001Listener.exitFunction_item_declaration(ctx)

        override suspend fun enterFunction_port_list(ctx: Verilog2001Parser.Function_port_listContext) =
            this@Verilog2001Listener.enterFunction_port_list(ctx)

        override suspend fun exitFunction_port_list(ctx: Verilog2001Parser.Function_port_listContext) =
            this@Verilog2001Listener.exitFunction_port_list(ctx)

        override suspend fun enterFunction_port(ctx: Verilog2001Parser.Function_portContext) =
            this@Verilog2001Listener.enterFunction_port(ctx)

        override suspend fun exitFunction_port(ctx: Verilog2001Parser.Function_portContext) =
            this@Verilog2001Listener.exitFunction_port(ctx)

        override suspend fun enterRange_or_type(ctx: Verilog2001Parser.Range_or_typeContext) =
            this@Verilog2001Listener.enterRange_or_type(ctx)

        override suspend fun exitRange_or_type(ctx: Verilog2001Parser.Range_or_typeContext) =
            this@Verilog2001Listener.exitRange_or_type(ctx)

        override suspend fun enterTask_declaration(ctx: Verilog2001Parser.Task_declarationContext) =
            this@Verilog2001Listener.enterTask_declaration(ctx)

        override suspend fun exitTask_declaration(ctx: Verilog2001Parser.Task_declarationContext) =
            this@Verilog2001Listener.exitTask_declaration(ctx)

        override suspend fun enterTask_item_declaration(ctx: Verilog2001Parser.Task_item_declarationContext) =
            this@Verilog2001Listener.enterTask_item_declaration(ctx)

        override suspend fun exitTask_item_declaration(ctx: Verilog2001Parser.Task_item_declarationContext) =
            this@Verilog2001Listener.exitTask_item_declaration(ctx)

        override suspend fun enterTask_port_list(ctx: Verilog2001Parser.Task_port_listContext) =
            this@Verilog2001Listener.enterTask_port_list(ctx)

        override suspend fun exitTask_port_list(ctx: Verilog2001Parser.Task_port_listContext) =
            this@Verilog2001Listener.exitTask_port_list(ctx)

        override suspend fun enterTask_port_item(ctx: Verilog2001Parser.Task_port_itemContext) =
            this@Verilog2001Listener.enterTask_port_item(ctx)

        override suspend fun exitTask_port_item(ctx: Verilog2001Parser.Task_port_itemContext) =
            this@Verilog2001Listener.exitTask_port_item(ctx)

        override suspend fun enterTf_decl_header(ctx: Verilog2001Parser.Tf_decl_headerContext) =
            this@Verilog2001Listener.enterTf_decl_header(ctx)

        override suspend fun exitTf_decl_header(ctx: Verilog2001Parser.Tf_decl_headerContext) =
            this@Verilog2001Listener.exitTf_decl_header(ctx)

        override suspend fun enterTf_declaration(ctx: Verilog2001Parser.Tf_declarationContext) =
            this@Verilog2001Listener.enterTf_declaration(ctx)

        override suspend fun exitTf_declaration(ctx: Verilog2001Parser.Tf_declarationContext) =
            this@Verilog2001Listener.exitTf_declaration(ctx)

        override suspend fun enterTask_port_type(ctx: Verilog2001Parser.Task_port_typeContext) =
            this@Verilog2001Listener.enterTask_port_type(ctx)

        override suspend fun exitTask_port_type(ctx: Verilog2001Parser.Task_port_typeContext) =
            this@Verilog2001Listener.exitTask_port_type(ctx)

        override suspend fun enterBlock_item_declaration(ctx: Verilog2001Parser.Block_item_declarationContext) =
            this@Verilog2001Listener.enterBlock_item_declaration(ctx)

        override suspend fun exitBlock_item_declaration(ctx: Verilog2001Parser.Block_item_declarationContext) =
            this@Verilog2001Listener.exitBlock_item_declaration(ctx)

        override suspend fun enterBlock_reg_declaration(ctx: Verilog2001Parser.Block_reg_declarationContext) =
            this@Verilog2001Listener.enterBlock_reg_declaration(ctx)

        override suspend fun exitBlock_reg_declaration(ctx: Verilog2001Parser.Block_reg_declarationContext) =
            this@Verilog2001Listener.exitBlock_reg_declaration(ctx)

        override suspend fun enterList_of_block_variable_identifiers(ctx: Verilog2001Parser.List_of_block_variable_identifiersContext) =
            this@Verilog2001Listener.enterList_of_block_variable_identifiers(ctx)

        override suspend fun exitList_of_block_variable_identifiers(ctx: Verilog2001Parser.List_of_block_variable_identifiersContext) =
            this@Verilog2001Listener.exitList_of_block_variable_identifiers(ctx)

        override suspend fun enterBlock_variable_type(ctx: Verilog2001Parser.Block_variable_typeContext) =
            this@Verilog2001Listener.enterBlock_variable_type(ctx)

        override suspend fun exitBlock_variable_type(ctx: Verilog2001Parser.Block_variable_typeContext) =
            this@Verilog2001Listener.exitBlock_variable_type(ctx)

        override suspend fun enterGate_instantiation(ctx: Verilog2001Parser.Gate_instantiationContext) =
            this@Verilog2001Listener.enterGate_instantiation(ctx)

        override suspend fun exitGate_instantiation(ctx: Verilog2001Parser.Gate_instantiationContext) =
            this@Verilog2001Listener.exitGate_instantiation(ctx)

        override suspend fun enterCmos_switch_instance(ctx: Verilog2001Parser.Cmos_switch_instanceContext) =
            this@Verilog2001Listener.enterCmos_switch_instance(ctx)

        override suspend fun exitCmos_switch_instance(ctx: Verilog2001Parser.Cmos_switch_instanceContext) =
            this@Verilog2001Listener.exitCmos_switch_instance(ctx)

        override suspend fun enterEnable_gate_instance(ctx: Verilog2001Parser.Enable_gate_instanceContext) =
            this@Verilog2001Listener.enterEnable_gate_instance(ctx)

        override suspend fun exitEnable_gate_instance(ctx: Verilog2001Parser.Enable_gate_instanceContext) =
            this@Verilog2001Listener.exitEnable_gate_instance(ctx)

        override suspend fun enterMos_switch_instance(ctx: Verilog2001Parser.Mos_switch_instanceContext) =
            this@Verilog2001Listener.enterMos_switch_instance(ctx)

        override suspend fun exitMos_switch_instance(ctx: Verilog2001Parser.Mos_switch_instanceContext) =
            this@Verilog2001Listener.exitMos_switch_instance(ctx)

        override suspend fun enterN_input_gate_instance(ctx: Verilog2001Parser.N_input_gate_instanceContext) =
            this@Verilog2001Listener.enterN_input_gate_instance(ctx)

        override suspend fun exitN_input_gate_instance(ctx: Verilog2001Parser.N_input_gate_instanceContext) =
            this@Verilog2001Listener.exitN_input_gate_instance(ctx)

        override suspend fun enterN_output_gate_instance(ctx: Verilog2001Parser.N_output_gate_instanceContext) =
            this@Verilog2001Listener.enterN_output_gate_instance(ctx)

        override suspend fun exitN_output_gate_instance(ctx: Verilog2001Parser.N_output_gate_instanceContext) =
            this@Verilog2001Listener.exitN_output_gate_instance(ctx)

        override suspend fun enterPass_switch_instance(ctx: Verilog2001Parser.Pass_switch_instanceContext) =
            this@Verilog2001Listener.enterPass_switch_instance(ctx)

        override suspend fun exitPass_switch_instance(ctx: Verilog2001Parser.Pass_switch_instanceContext) =
            this@Verilog2001Listener.exitPass_switch_instance(ctx)

        override suspend fun enterPass_enable_switch_instance(ctx: Verilog2001Parser.Pass_enable_switch_instanceContext) =
            this@Verilog2001Listener.enterPass_enable_switch_instance(ctx)

        override suspend fun exitPass_enable_switch_instance(ctx: Verilog2001Parser.Pass_enable_switch_instanceContext) =
            this@Verilog2001Listener.exitPass_enable_switch_instance(ctx)

        override suspend fun enterPull_gate_instance(ctx: Verilog2001Parser.Pull_gate_instanceContext) =
            this@Verilog2001Listener.enterPull_gate_instance(ctx)

        override suspend fun exitPull_gate_instance(ctx: Verilog2001Parser.Pull_gate_instanceContext) =
            this@Verilog2001Listener.exitPull_gate_instance(ctx)

        override suspend fun enterName_of_gate_instance(ctx: Verilog2001Parser.Name_of_gate_instanceContext) =
            this@Verilog2001Listener.enterName_of_gate_instance(ctx)

        override suspend fun exitName_of_gate_instance(ctx: Verilog2001Parser.Name_of_gate_instanceContext) =
            this@Verilog2001Listener.exitName_of_gate_instance(ctx)

        override suspend fun enterPulldown_strength(ctx: Verilog2001Parser.Pulldown_strengthContext) =
            this@Verilog2001Listener.enterPulldown_strength(ctx)

        override suspend fun exitPulldown_strength(ctx: Verilog2001Parser.Pulldown_strengthContext) =
            this@Verilog2001Listener.exitPulldown_strength(ctx)

        override suspend fun enterPullup_strength(ctx: Verilog2001Parser.Pullup_strengthContext) =
            this@Verilog2001Listener.enterPullup_strength(ctx)

        override suspend fun exitPullup_strength(ctx: Verilog2001Parser.Pullup_strengthContext) =
            this@Verilog2001Listener.exitPullup_strength(ctx)

        override suspend fun enterEnable_terminal(ctx: Verilog2001Parser.Enable_terminalContext) =
            this@Verilog2001Listener.enterEnable_terminal(ctx)

        override suspend fun exitEnable_terminal(ctx: Verilog2001Parser.Enable_terminalContext) =
            this@Verilog2001Listener.exitEnable_terminal(ctx)

        override suspend fun enterNcontrol_terminal(ctx: Verilog2001Parser.Ncontrol_terminalContext) =
            this@Verilog2001Listener.enterNcontrol_terminal(ctx)

        override suspend fun exitNcontrol_terminal(ctx: Verilog2001Parser.Ncontrol_terminalContext) =
            this@Verilog2001Listener.exitNcontrol_terminal(ctx)

        override suspend fun enterPcontrol_terminal(ctx: Verilog2001Parser.Pcontrol_terminalContext) =
            this@Verilog2001Listener.enterPcontrol_terminal(ctx)

        override suspend fun exitPcontrol_terminal(ctx: Verilog2001Parser.Pcontrol_terminalContext) =
            this@Verilog2001Listener.exitPcontrol_terminal(ctx)

        override suspend fun enterInput_terminal(ctx: Verilog2001Parser.Input_terminalContext) =
            this@Verilog2001Listener.enterInput_terminal(ctx)

        override suspend fun exitInput_terminal(ctx: Verilog2001Parser.Input_terminalContext) =
            this@Verilog2001Listener.exitInput_terminal(ctx)

        override suspend fun enterInout_terminal(ctx: Verilog2001Parser.Inout_terminalContext) =
            this@Verilog2001Listener.enterInout_terminal(ctx)

        override suspend fun exitInout_terminal(ctx: Verilog2001Parser.Inout_terminalContext) =
            this@Verilog2001Listener.exitInout_terminal(ctx)

        override suspend fun enterOutput_terminal(ctx: Verilog2001Parser.Output_terminalContext) =
            this@Verilog2001Listener.enterOutput_terminal(ctx)

        override suspend fun exitOutput_terminal(ctx: Verilog2001Parser.Output_terminalContext) =
            this@Verilog2001Listener.exitOutput_terminal(ctx)

        override suspend fun enterCmos_switchtype(ctx: Verilog2001Parser.Cmos_switchtypeContext) =
            this@Verilog2001Listener.enterCmos_switchtype(ctx)

        override suspend fun exitCmos_switchtype(ctx: Verilog2001Parser.Cmos_switchtypeContext) =
            this@Verilog2001Listener.exitCmos_switchtype(ctx)

        override suspend fun enterEnable_gatetype(ctx: Verilog2001Parser.Enable_gatetypeContext) =
            this@Verilog2001Listener.enterEnable_gatetype(ctx)

        override suspend fun exitEnable_gatetype(ctx: Verilog2001Parser.Enable_gatetypeContext) =
            this@Verilog2001Listener.exitEnable_gatetype(ctx)

        override suspend fun enterMos_switchtype(ctx: Verilog2001Parser.Mos_switchtypeContext) =
            this@Verilog2001Listener.enterMos_switchtype(ctx)

        override suspend fun exitMos_switchtype(ctx: Verilog2001Parser.Mos_switchtypeContext) =
            this@Verilog2001Listener.exitMos_switchtype(ctx)

        override suspend fun enterN_input_gatetype(ctx: Verilog2001Parser.N_input_gatetypeContext) =
            this@Verilog2001Listener.enterN_input_gatetype(ctx)

        override suspend fun exitN_input_gatetype(ctx: Verilog2001Parser.N_input_gatetypeContext) =
            this@Verilog2001Listener.exitN_input_gatetype(ctx)

        override suspend fun enterN_output_gatetype(ctx: Verilog2001Parser.N_output_gatetypeContext) =
            this@Verilog2001Listener.enterN_output_gatetype(ctx)

        override suspend fun exitN_output_gatetype(ctx: Verilog2001Parser.N_output_gatetypeContext) =
            this@Verilog2001Listener.exitN_output_gatetype(ctx)

        override suspend fun enterPass_en_switchtype(ctx: Verilog2001Parser.Pass_en_switchtypeContext) =
            this@Verilog2001Listener.enterPass_en_switchtype(ctx)

        override suspend fun exitPass_en_switchtype(ctx: Verilog2001Parser.Pass_en_switchtypeContext) =
            this@Verilog2001Listener.exitPass_en_switchtype(ctx)

        override suspend fun enterPass_switchtype(ctx: Verilog2001Parser.Pass_switchtypeContext) =
            this@Verilog2001Listener.enterPass_switchtype(ctx)

        override suspend fun exitPass_switchtype(ctx: Verilog2001Parser.Pass_switchtypeContext) =
            this@Verilog2001Listener.exitPass_switchtype(ctx)

        override suspend fun enterModule_instantiation(ctx: Verilog2001Parser.Module_instantiationContext) =
            this@Verilog2001Listener.enterModule_instantiation(ctx)

        override suspend fun exitModule_instantiation(ctx: Verilog2001Parser.Module_instantiationContext) =
            this@Verilog2001Listener.exitModule_instantiation(ctx)

        override suspend fun enterParameter_value_assignment(ctx: Verilog2001Parser.Parameter_value_assignmentContext) =
            this@Verilog2001Listener.enterParameter_value_assignment(ctx)

        override suspend fun exitParameter_value_assignment(ctx: Verilog2001Parser.Parameter_value_assignmentContext) =
            this@Verilog2001Listener.exitParameter_value_assignment(ctx)

        override suspend fun enterList_of_parameter_assignments(ctx: Verilog2001Parser.List_of_parameter_assignmentsContext) =
            this@Verilog2001Listener.enterList_of_parameter_assignments(ctx)

        override suspend fun exitList_of_parameter_assignments(ctx: Verilog2001Parser.List_of_parameter_assignmentsContext) =
            this@Verilog2001Listener.exitList_of_parameter_assignments(ctx)

        override suspend fun enterOrdered_parameter_assignment(ctx: Verilog2001Parser.Ordered_parameter_assignmentContext) =
            this@Verilog2001Listener.enterOrdered_parameter_assignment(ctx)

        override suspend fun exitOrdered_parameter_assignment(ctx: Verilog2001Parser.Ordered_parameter_assignmentContext) =
            this@Verilog2001Listener.exitOrdered_parameter_assignment(ctx)

        override suspend fun enterNamed_parameter_assignment(ctx: Verilog2001Parser.Named_parameter_assignmentContext) =
            this@Verilog2001Listener.enterNamed_parameter_assignment(ctx)

        override suspend fun exitNamed_parameter_assignment(ctx: Verilog2001Parser.Named_parameter_assignmentContext) =
            this@Verilog2001Listener.exitNamed_parameter_assignment(ctx)

        override suspend fun enterModule_instance(ctx: Verilog2001Parser.Module_instanceContext) =
            this@Verilog2001Listener.enterModule_instance(ctx)

        override suspend fun exitModule_instance(ctx: Verilog2001Parser.Module_instanceContext) =
            this@Verilog2001Listener.exitModule_instance(ctx)

        override suspend fun enterName_of_instance(ctx: Verilog2001Parser.Name_of_instanceContext) =
            this@Verilog2001Listener.enterName_of_instance(ctx)

        override suspend fun exitName_of_instance(ctx: Verilog2001Parser.Name_of_instanceContext) =
            this@Verilog2001Listener.exitName_of_instance(ctx)

        override suspend fun enterList_of_port_connections(ctx: Verilog2001Parser.List_of_port_connectionsContext) =
            this@Verilog2001Listener.enterList_of_port_connections(ctx)

        override suspend fun exitList_of_port_connections(ctx: Verilog2001Parser.List_of_port_connectionsContext) =
            this@Verilog2001Listener.exitList_of_port_connections(ctx)

        override suspend fun enterOrdered_port_connection(ctx: Verilog2001Parser.Ordered_port_connectionContext) =
            this@Verilog2001Listener.enterOrdered_port_connection(ctx)

        override suspend fun exitOrdered_port_connection(ctx: Verilog2001Parser.Ordered_port_connectionContext) =
            this@Verilog2001Listener.exitOrdered_port_connection(ctx)

        override suspend fun enterNamed_port_connection(ctx: Verilog2001Parser.Named_port_connectionContext) =
            this@Verilog2001Listener.enterNamed_port_connection(ctx)

        override suspend fun exitNamed_port_connection(ctx: Verilog2001Parser.Named_port_connectionContext) =
            this@Verilog2001Listener.exitNamed_port_connection(ctx)

        override suspend fun enterGenerated_instantiation(ctx: Verilog2001Parser.Generated_instantiationContext) =
            this@Verilog2001Listener.enterGenerated_instantiation(ctx)

        override suspend fun exitGenerated_instantiation(ctx: Verilog2001Parser.Generated_instantiationContext) =
            this@Verilog2001Listener.exitGenerated_instantiation(ctx)

        override suspend fun enterGenerate_item_or_null(ctx: Verilog2001Parser.Generate_item_or_nullContext) =
            this@Verilog2001Listener.enterGenerate_item_or_null(ctx)

        override suspend fun exitGenerate_item_or_null(ctx: Verilog2001Parser.Generate_item_or_nullContext) =
            this@Verilog2001Listener.exitGenerate_item_or_null(ctx)

        override suspend fun enterGenerate_item(ctx: Verilog2001Parser.Generate_itemContext) =
            this@Verilog2001Listener.enterGenerate_item(ctx)

        override suspend fun exitGenerate_item(ctx: Verilog2001Parser.Generate_itemContext) =
            this@Verilog2001Listener.exitGenerate_item(ctx)

        override suspend fun enterGenerate_conditional_statement(ctx: Verilog2001Parser.Generate_conditional_statementContext) =
            this@Verilog2001Listener.enterGenerate_conditional_statement(ctx)

        override suspend fun exitGenerate_conditional_statement(ctx: Verilog2001Parser.Generate_conditional_statementContext) =
            this@Verilog2001Listener.exitGenerate_conditional_statement(ctx)

        override suspend fun enterGenerate_case_statement(ctx: Verilog2001Parser.Generate_case_statementContext) =
            this@Verilog2001Listener.enterGenerate_case_statement(ctx)

        override suspend fun exitGenerate_case_statement(ctx: Verilog2001Parser.Generate_case_statementContext) =
            this@Verilog2001Listener.exitGenerate_case_statement(ctx)

        override suspend fun enterGenvar_case_item(ctx: Verilog2001Parser.Genvar_case_itemContext) =
            this@Verilog2001Listener.enterGenvar_case_item(ctx)

        override suspend fun exitGenvar_case_item(ctx: Verilog2001Parser.Genvar_case_itemContext) =
            this@Verilog2001Listener.exitGenvar_case_item(ctx)

        override suspend fun enterGenerate_loop_statement(ctx: Verilog2001Parser.Generate_loop_statementContext) =
            this@Verilog2001Listener.enterGenerate_loop_statement(ctx)

        override suspend fun exitGenerate_loop_statement(ctx: Verilog2001Parser.Generate_loop_statementContext) =
            this@Verilog2001Listener.exitGenerate_loop_statement(ctx)

        override suspend fun enterGenvar_assignment(ctx: Verilog2001Parser.Genvar_assignmentContext) =
            this@Verilog2001Listener.enterGenvar_assignment(ctx)

        override suspend fun exitGenvar_assignment(ctx: Verilog2001Parser.Genvar_assignmentContext) =
            this@Verilog2001Listener.exitGenvar_assignment(ctx)

        override suspend fun enterGenerate_block(ctx: Verilog2001Parser.Generate_blockContext) =
            this@Verilog2001Listener.enterGenerate_block(ctx)

        override suspend fun exitGenerate_block(ctx: Verilog2001Parser.Generate_blockContext) =
            this@Verilog2001Listener.exitGenerate_block(ctx)

        override suspend fun enterContinuous_assign(ctx: Verilog2001Parser.Continuous_assignContext) =
            this@Verilog2001Listener.enterContinuous_assign(ctx)

        override suspend fun exitContinuous_assign(ctx: Verilog2001Parser.Continuous_assignContext) =
            this@Verilog2001Listener.exitContinuous_assign(ctx)

        override suspend fun enterList_of_net_assignments(ctx: Verilog2001Parser.List_of_net_assignmentsContext) =
            this@Verilog2001Listener.enterList_of_net_assignments(ctx)

        override suspend fun exitList_of_net_assignments(ctx: Verilog2001Parser.List_of_net_assignmentsContext) =
            this@Verilog2001Listener.exitList_of_net_assignments(ctx)

        override suspend fun enterNet_assignment(ctx: Verilog2001Parser.Net_assignmentContext) =
            this@Verilog2001Listener.enterNet_assignment(ctx)

        override suspend fun exitNet_assignment(ctx: Verilog2001Parser.Net_assignmentContext) =
            this@Verilog2001Listener.exitNet_assignment(ctx)

        override suspend fun enterInitial_construct(ctx: Verilog2001Parser.Initial_constructContext) =
            this@Verilog2001Listener.enterInitial_construct(ctx)

        override suspend fun exitInitial_construct(ctx: Verilog2001Parser.Initial_constructContext) =
            this@Verilog2001Listener.exitInitial_construct(ctx)

        override suspend fun enterAlways_construct(ctx: Verilog2001Parser.Always_constructContext) =
            this@Verilog2001Listener.enterAlways_construct(ctx)

        override suspend fun exitAlways_construct(ctx: Verilog2001Parser.Always_constructContext) =
            this@Verilog2001Listener.exitAlways_construct(ctx)

        override suspend fun enterBlocking_assignment(ctx: Verilog2001Parser.Blocking_assignmentContext) =
            this@Verilog2001Listener.enterBlocking_assignment(ctx)

        override suspend fun exitBlocking_assignment(ctx: Verilog2001Parser.Blocking_assignmentContext) =
            this@Verilog2001Listener.exitBlocking_assignment(ctx)

        override suspend fun enterNonblocking_assignment(ctx: Verilog2001Parser.Nonblocking_assignmentContext) =
            this@Verilog2001Listener.enterNonblocking_assignment(ctx)

        override suspend fun exitNonblocking_assignment(ctx: Verilog2001Parser.Nonblocking_assignmentContext) =
            this@Verilog2001Listener.exitNonblocking_assignment(ctx)

        override suspend fun enterProcedural_continuous_assignments(ctx: Verilog2001Parser.Procedural_continuous_assignmentsContext) =
            this@Verilog2001Listener.enterProcedural_continuous_assignments(ctx)

        override suspend fun exitProcedural_continuous_assignments(ctx: Verilog2001Parser.Procedural_continuous_assignmentsContext) =
            this@Verilog2001Listener.exitProcedural_continuous_assignments(ctx)

        override suspend fun enterFunction_blocking_assignment(ctx: Verilog2001Parser.Function_blocking_assignmentContext) =
            this@Verilog2001Listener.enterFunction_blocking_assignment(ctx)

        override suspend fun exitFunction_blocking_assignment(ctx: Verilog2001Parser.Function_blocking_assignmentContext) =
            this@Verilog2001Listener.exitFunction_blocking_assignment(ctx)

        override suspend fun enterFunction_statement_or_null(ctx: Verilog2001Parser.Function_statement_or_nullContext) =
            this@Verilog2001Listener.enterFunction_statement_or_null(ctx)

        override suspend fun exitFunction_statement_or_null(ctx: Verilog2001Parser.Function_statement_or_nullContext) =
            this@Verilog2001Listener.exitFunction_statement_or_null(ctx)

        override suspend fun enterFunction_seq_block(ctx: Verilog2001Parser.Function_seq_blockContext) =
            this@Verilog2001Listener.enterFunction_seq_block(ctx)

        override suspend fun exitFunction_seq_block(ctx: Verilog2001Parser.Function_seq_blockContext) =
            this@Verilog2001Listener.exitFunction_seq_block(ctx)

        override suspend fun enterVariable_assignment(ctx: Verilog2001Parser.Variable_assignmentContext) =
            this@Verilog2001Listener.enterVariable_assignment(ctx)

        override suspend fun exitVariable_assignment(ctx: Verilog2001Parser.Variable_assignmentContext) =
            this@Verilog2001Listener.exitVariable_assignment(ctx)

        override suspend fun enterPar_block(ctx: Verilog2001Parser.Par_blockContext) =
            this@Verilog2001Listener.enterPar_block(ctx)

        override suspend fun exitPar_block(ctx: Verilog2001Parser.Par_blockContext) =
            this@Verilog2001Listener.exitPar_block(ctx)

        override suspend fun enterSeq_block(ctx: Verilog2001Parser.Seq_blockContext) =
            this@Verilog2001Listener.enterSeq_block(ctx)

        override suspend fun exitSeq_block(ctx: Verilog2001Parser.Seq_blockContext) =
            this@Verilog2001Listener.exitSeq_block(ctx)

        override suspend fun enterStatement(ctx: Verilog2001Parser.StatementContext) =
            this@Verilog2001Listener.enterStatement(ctx)

        override suspend fun exitStatement(ctx: Verilog2001Parser.StatementContext) =
            this@Verilog2001Listener.exitStatement(ctx)

        override suspend fun enterStatement_or_null(ctx: Verilog2001Parser.Statement_or_nullContext) =
            this@Verilog2001Listener.enterStatement_or_null(ctx)

        override suspend fun exitStatement_or_null(ctx: Verilog2001Parser.Statement_or_nullContext) =
            this@Verilog2001Listener.exitStatement_or_null(ctx)

        override suspend fun enterFunction_statement(ctx: Verilog2001Parser.Function_statementContext) =
            this@Verilog2001Listener.enterFunction_statement(ctx)

        override suspend fun exitFunction_statement(ctx: Verilog2001Parser.Function_statementContext) =
            this@Verilog2001Listener.exitFunction_statement(ctx)

        override suspend fun enterDelay_or_event_control(ctx: Verilog2001Parser.Delay_or_event_controlContext) =
            this@Verilog2001Listener.enterDelay_or_event_control(ctx)

        override suspend fun exitDelay_or_event_control(ctx: Verilog2001Parser.Delay_or_event_controlContext) =
            this@Verilog2001Listener.exitDelay_or_event_control(ctx)

        override suspend fun enterDelay_control(ctx: Verilog2001Parser.Delay_controlContext) =
            this@Verilog2001Listener.enterDelay_control(ctx)

        override suspend fun exitDelay_control(ctx: Verilog2001Parser.Delay_controlContext) =
            this@Verilog2001Listener.exitDelay_control(ctx)

        override suspend fun enterDisable_statement(ctx: Verilog2001Parser.Disable_statementContext) =
            this@Verilog2001Listener.enterDisable_statement(ctx)

        override suspend fun exitDisable_statement(ctx: Verilog2001Parser.Disable_statementContext) =
            this@Verilog2001Listener.exitDisable_statement(ctx)

        override suspend fun enterEvent_control(ctx: Verilog2001Parser.Event_controlContext) =
            this@Verilog2001Listener.enterEvent_control(ctx)

        override suspend fun exitEvent_control(ctx: Verilog2001Parser.Event_controlContext) =
            this@Verilog2001Listener.exitEvent_control(ctx)

        override suspend fun enterEvent_trigger(ctx: Verilog2001Parser.Event_triggerContext) =
            this@Verilog2001Listener.enterEvent_trigger(ctx)

        override suspend fun exitEvent_trigger(ctx: Verilog2001Parser.Event_triggerContext) =
            this@Verilog2001Listener.exitEvent_trigger(ctx)

        override suspend fun enterEvent_expression(ctx: Verilog2001Parser.Event_expressionContext) =
            this@Verilog2001Listener.enterEvent_expression(ctx)

        override suspend fun exitEvent_expression(ctx: Verilog2001Parser.Event_expressionContext) =
            this@Verilog2001Listener.exitEvent_expression(ctx)

        override suspend fun enterEvent_primary(ctx: Verilog2001Parser.Event_primaryContext) =
            this@Verilog2001Listener.enterEvent_primary(ctx)

        override suspend fun exitEvent_primary(ctx: Verilog2001Parser.Event_primaryContext) =
            this@Verilog2001Listener.exitEvent_primary(ctx)

        override suspend fun enterProcedural_timing_control_statement(ctx: Verilog2001Parser.Procedural_timing_control_statementContext) =
            this@Verilog2001Listener.enterProcedural_timing_control_statement(ctx)

        override suspend fun exitProcedural_timing_control_statement(ctx: Verilog2001Parser.Procedural_timing_control_statementContext) =
            this@Verilog2001Listener.exitProcedural_timing_control_statement(ctx)

        override suspend fun enterWait_statement(ctx: Verilog2001Parser.Wait_statementContext) =
            this@Verilog2001Listener.enterWait_statement(ctx)

        override suspend fun exitWait_statement(ctx: Verilog2001Parser.Wait_statementContext) =
            this@Verilog2001Listener.exitWait_statement(ctx)

        override suspend fun enterConditional_statement(ctx: Verilog2001Parser.Conditional_statementContext) =
            this@Verilog2001Listener.enterConditional_statement(ctx)

        override suspend fun exitConditional_statement(ctx: Verilog2001Parser.Conditional_statementContext) =
            this@Verilog2001Listener.exitConditional_statement(ctx)

        override suspend fun enterIf_else_if_statement(ctx: Verilog2001Parser.If_else_if_statementContext) =
            this@Verilog2001Listener.enterIf_else_if_statement(ctx)

        override suspend fun exitIf_else_if_statement(ctx: Verilog2001Parser.If_else_if_statementContext) =
            this@Verilog2001Listener.exitIf_else_if_statement(ctx)

        override suspend fun enterFunction_conditional_statement(ctx: Verilog2001Parser.Function_conditional_statementContext) =
            this@Verilog2001Listener.enterFunction_conditional_statement(ctx)

        override suspend fun exitFunction_conditional_statement(ctx: Verilog2001Parser.Function_conditional_statementContext) =
            this@Verilog2001Listener.exitFunction_conditional_statement(ctx)

        override suspend fun enterFunction_if_else_if_statement(ctx: Verilog2001Parser.Function_if_else_if_statementContext) =
            this@Verilog2001Listener.enterFunction_if_else_if_statement(ctx)

        override suspend fun exitFunction_if_else_if_statement(ctx: Verilog2001Parser.Function_if_else_if_statementContext) =
            this@Verilog2001Listener.exitFunction_if_else_if_statement(ctx)

        override suspend fun enterCase_statement(ctx: Verilog2001Parser.Case_statementContext) =
            this@Verilog2001Listener.enterCase_statement(ctx)

        override suspend fun exitCase_statement(ctx: Verilog2001Parser.Case_statementContext) =
            this@Verilog2001Listener.exitCase_statement(ctx)

        override suspend fun enterCase_item(ctx: Verilog2001Parser.Case_itemContext) =
            this@Verilog2001Listener.enterCase_item(ctx)

        override suspend fun exitCase_item(ctx: Verilog2001Parser.Case_itemContext) =
            this@Verilog2001Listener.exitCase_item(ctx)

        override suspend fun enterFunction_case_statement(ctx: Verilog2001Parser.Function_case_statementContext) =
            this@Verilog2001Listener.enterFunction_case_statement(ctx)

        override suspend fun exitFunction_case_statement(ctx: Verilog2001Parser.Function_case_statementContext) =
            this@Verilog2001Listener.exitFunction_case_statement(ctx)

        override suspend fun enterFunction_case_item(ctx: Verilog2001Parser.Function_case_itemContext) =
            this@Verilog2001Listener.enterFunction_case_item(ctx)

        override suspend fun exitFunction_case_item(ctx: Verilog2001Parser.Function_case_itemContext) =
            this@Verilog2001Listener.exitFunction_case_item(ctx)

        override suspend fun enterFunction_loop_statement(ctx: Verilog2001Parser.Function_loop_statementContext) =
            this@Verilog2001Listener.enterFunction_loop_statement(ctx)

        override suspend fun exitFunction_loop_statement(ctx: Verilog2001Parser.Function_loop_statementContext) =
            this@Verilog2001Listener.exitFunction_loop_statement(ctx)

        override suspend fun enterLoop_statement(ctx: Verilog2001Parser.Loop_statementContext) =
            this@Verilog2001Listener.enterLoop_statement(ctx)

        override suspend fun exitLoop_statement(ctx: Verilog2001Parser.Loop_statementContext) =
            this@Verilog2001Listener.exitLoop_statement(ctx)

        override suspend fun enterSystem_task_enable(ctx: Verilog2001Parser.System_task_enableContext) =
            this@Verilog2001Listener.enterSystem_task_enable(ctx)

        override suspend fun exitSystem_task_enable(ctx: Verilog2001Parser.System_task_enableContext) =
            this@Verilog2001Listener.exitSystem_task_enable(ctx)

        override suspend fun enterTask_enable(ctx: Verilog2001Parser.Task_enableContext) =
            this@Verilog2001Listener.enterTask_enable(ctx)

        override suspend fun exitTask_enable(ctx: Verilog2001Parser.Task_enableContext) =
            this@Verilog2001Listener.exitTask_enable(ctx)

        override suspend fun enterSpecify_block(ctx: Verilog2001Parser.Specify_blockContext) =
            this@Verilog2001Listener.enterSpecify_block(ctx)

        override suspend fun exitSpecify_block(ctx: Verilog2001Parser.Specify_blockContext) =
            this@Verilog2001Listener.exitSpecify_block(ctx)

        override suspend fun enterSpecify_item(ctx: Verilog2001Parser.Specify_itemContext) =
            this@Verilog2001Listener.enterSpecify_item(ctx)

        override suspend fun exitSpecify_item(ctx: Verilog2001Parser.Specify_itemContext) =
            this@Verilog2001Listener.exitSpecify_item(ctx)

        override suspend fun enterPulsestyle_declaration(ctx: Verilog2001Parser.Pulsestyle_declarationContext) =
            this@Verilog2001Listener.enterPulsestyle_declaration(ctx)

        override suspend fun exitPulsestyle_declaration(ctx: Verilog2001Parser.Pulsestyle_declarationContext) =
            this@Verilog2001Listener.exitPulsestyle_declaration(ctx)

        override suspend fun enterShowcancelled_declaration(ctx: Verilog2001Parser.Showcancelled_declarationContext) =
            this@Verilog2001Listener.enterShowcancelled_declaration(ctx)

        override suspend fun exitShowcancelled_declaration(ctx: Verilog2001Parser.Showcancelled_declarationContext) =
            this@Verilog2001Listener.exitShowcancelled_declaration(ctx)

        override suspend fun enterPath_declaration(ctx: Verilog2001Parser.Path_declarationContext) =
            this@Verilog2001Listener.enterPath_declaration(ctx)

        override suspend fun exitPath_declaration(ctx: Verilog2001Parser.Path_declarationContext) =
            this@Verilog2001Listener.exitPath_declaration(ctx)

        override suspend fun enterSimple_path_declaration(ctx: Verilog2001Parser.Simple_path_declarationContext) =
            this@Verilog2001Listener.enterSimple_path_declaration(ctx)

        override suspend fun exitSimple_path_declaration(ctx: Verilog2001Parser.Simple_path_declarationContext) =
            this@Verilog2001Listener.exitSimple_path_declaration(ctx)

        override suspend fun enterParallel_path_description(ctx: Verilog2001Parser.Parallel_path_descriptionContext) =
            this@Verilog2001Listener.enterParallel_path_description(ctx)

        override suspend fun exitParallel_path_description(ctx: Verilog2001Parser.Parallel_path_descriptionContext) =
            this@Verilog2001Listener.exitParallel_path_description(ctx)

        override suspend fun enterFull_path_description(ctx: Verilog2001Parser.Full_path_descriptionContext) =
            this@Verilog2001Listener.enterFull_path_description(ctx)

        override suspend fun exitFull_path_description(ctx: Verilog2001Parser.Full_path_descriptionContext) =
            this@Verilog2001Listener.exitFull_path_description(ctx)

        override suspend fun enterList_of_path_inputs(ctx: Verilog2001Parser.List_of_path_inputsContext) =
            this@Verilog2001Listener.enterList_of_path_inputs(ctx)

        override suspend fun exitList_of_path_inputs(ctx: Verilog2001Parser.List_of_path_inputsContext) =
            this@Verilog2001Listener.exitList_of_path_inputs(ctx)

        override suspend fun enterList_of_path_outputs(ctx: Verilog2001Parser.List_of_path_outputsContext) =
            this@Verilog2001Listener.enterList_of_path_outputs(ctx)

        override suspend fun exitList_of_path_outputs(ctx: Verilog2001Parser.List_of_path_outputsContext) =
            this@Verilog2001Listener.exitList_of_path_outputs(ctx)

        override suspend fun enterSpecify_input_terminal_descriptor(ctx: Verilog2001Parser.Specify_input_terminal_descriptorContext) =
            this@Verilog2001Listener.enterSpecify_input_terminal_descriptor(ctx)

        override suspend fun exitSpecify_input_terminal_descriptor(ctx: Verilog2001Parser.Specify_input_terminal_descriptorContext) =
            this@Verilog2001Listener.exitSpecify_input_terminal_descriptor(ctx)

        override suspend fun enterSpecify_output_terminal_descriptor(ctx: Verilog2001Parser.Specify_output_terminal_descriptorContext) =
            this@Verilog2001Listener.enterSpecify_output_terminal_descriptor(ctx)

        override suspend fun exitSpecify_output_terminal_descriptor(ctx: Verilog2001Parser.Specify_output_terminal_descriptorContext) =
            this@Verilog2001Listener.exitSpecify_output_terminal_descriptor(ctx)

        override suspend fun enterInput_identifier(ctx: Verilog2001Parser.Input_identifierContext) =
            this@Verilog2001Listener.enterInput_identifier(ctx)

        override suspend fun exitInput_identifier(ctx: Verilog2001Parser.Input_identifierContext) =
            this@Verilog2001Listener.exitInput_identifier(ctx)

        override suspend fun enterOutput_identifier(ctx: Verilog2001Parser.Output_identifierContext) =
            this@Verilog2001Listener.enterOutput_identifier(ctx)

        override suspend fun exitOutput_identifier(ctx: Verilog2001Parser.Output_identifierContext) =
            this@Verilog2001Listener.exitOutput_identifier(ctx)

        override suspend fun enterPath_delay_value(ctx: Verilog2001Parser.Path_delay_valueContext) =
            this@Verilog2001Listener.enterPath_delay_value(ctx)

        override suspend fun exitPath_delay_value(ctx: Verilog2001Parser.Path_delay_valueContext) =
            this@Verilog2001Listener.exitPath_delay_value(ctx)

        override suspend fun enterList_of_path_delay_expressions(ctx: Verilog2001Parser.List_of_path_delay_expressionsContext) =
            this@Verilog2001Listener.enterList_of_path_delay_expressions(ctx)

        override suspend fun exitList_of_path_delay_expressions(ctx: Verilog2001Parser.List_of_path_delay_expressionsContext) =
            this@Verilog2001Listener.exitList_of_path_delay_expressions(ctx)

        override suspend fun enterT_path_delay_expression(ctx: Verilog2001Parser.T_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT_path_delay_expression(ctx)

        override suspend fun exitT_path_delay_expression(ctx: Verilog2001Parser.T_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT_path_delay_expression(ctx)

        override suspend fun enterTrise_path_delay_expression(ctx: Verilog2001Parser.Trise_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTrise_path_delay_expression(ctx)

        override suspend fun exitTrise_path_delay_expression(ctx: Verilog2001Parser.Trise_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTrise_path_delay_expression(ctx)

        override suspend fun enterTfall_path_delay_expression(ctx: Verilog2001Parser.Tfall_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTfall_path_delay_expression(ctx)

        override suspend fun exitTfall_path_delay_expression(ctx: Verilog2001Parser.Tfall_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTfall_path_delay_expression(ctx)

        override suspend fun enterTz_path_delay_expression(ctx: Verilog2001Parser.Tz_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTz_path_delay_expression(ctx)

        override suspend fun exitTz_path_delay_expression(ctx: Verilog2001Parser.Tz_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTz_path_delay_expression(ctx)

        override suspend fun enterT01_path_delay_expression(ctx: Verilog2001Parser.T01_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT01_path_delay_expression(ctx)

        override suspend fun exitT01_path_delay_expression(ctx: Verilog2001Parser.T01_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT01_path_delay_expression(ctx)

        override suspend fun enterT10_path_delay_expression(ctx: Verilog2001Parser.T10_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT10_path_delay_expression(ctx)

        override suspend fun exitT10_path_delay_expression(ctx: Verilog2001Parser.T10_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT10_path_delay_expression(ctx)

        override suspend fun enterT0z_path_delay_expression(ctx: Verilog2001Parser.T0z_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT0z_path_delay_expression(ctx)

        override suspend fun exitT0z_path_delay_expression(ctx: Verilog2001Parser.T0z_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT0z_path_delay_expression(ctx)

        override suspend fun enterTz1_path_delay_expression(ctx: Verilog2001Parser.Tz1_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTz1_path_delay_expression(ctx)

        override suspend fun exitTz1_path_delay_expression(ctx: Verilog2001Parser.Tz1_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTz1_path_delay_expression(ctx)

        override suspend fun enterT1z_path_delay_expression(ctx: Verilog2001Parser.T1z_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT1z_path_delay_expression(ctx)

        override suspend fun exitT1z_path_delay_expression(ctx: Verilog2001Parser.T1z_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT1z_path_delay_expression(ctx)

        override suspend fun enterTz0_path_delay_expression(ctx: Verilog2001Parser.Tz0_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTz0_path_delay_expression(ctx)

        override suspend fun exitTz0_path_delay_expression(ctx: Verilog2001Parser.Tz0_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTz0_path_delay_expression(ctx)

        override suspend fun enterT0x_path_delay_expression(ctx: Verilog2001Parser.T0x_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT0x_path_delay_expression(ctx)

        override suspend fun exitT0x_path_delay_expression(ctx: Verilog2001Parser.T0x_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT0x_path_delay_expression(ctx)

        override suspend fun enterTx1_path_delay_expression(ctx: Verilog2001Parser.Tx1_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTx1_path_delay_expression(ctx)

        override suspend fun exitTx1_path_delay_expression(ctx: Verilog2001Parser.Tx1_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTx1_path_delay_expression(ctx)

        override suspend fun enterT1x_path_delay_expression(ctx: Verilog2001Parser.T1x_path_delay_expressionContext) =
            this@Verilog2001Listener.enterT1x_path_delay_expression(ctx)

        override suspend fun exitT1x_path_delay_expression(ctx: Verilog2001Parser.T1x_path_delay_expressionContext) =
            this@Verilog2001Listener.exitT1x_path_delay_expression(ctx)

        override suspend fun enterTx0_path_delay_expression(ctx: Verilog2001Parser.Tx0_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTx0_path_delay_expression(ctx)

        override suspend fun exitTx0_path_delay_expression(ctx: Verilog2001Parser.Tx0_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTx0_path_delay_expression(ctx)

        override suspend fun enterTxz_path_delay_expression(ctx: Verilog2001Parser.Txz_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTxz_path_delay_expression(ctx)

        override suspend fun exitTxz_path_delay_expression(ctx: Verilog2001Parser.Txz_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTxz_path_delay_expression(ctx)

        override suspend fun enterTzx_path_delay_expression(ctx: Verilog2001Parser.Tzx_path_delay_expressionContext) =
            this@Verilog2001Listener.enterTzx_path_delay_expression(ctx)

        override suspend fun exitTzx_path_delay_expression(ctx: Verilog2001Parser.Tzx_path_delay_expressionContext) =
            this@Verilog2001Listener.exitTzx_path_delay_expression(ctx)

        override suspend fun enterPath_delay_expression(ctx: Verilog2001Parser.Path_delay_expressionContext) =
            this@Verilog2001Listener.enterPath_delay_expression(ctx)

        override suspend fun exitPath_delay_expression(ctx: Verilog2001Parser.Path_delay_expressionContext) =
            this@Verilog2001Listener.exitPath_delay_expression(ctx)

        override suspend fun enterEdge_sensitive_path_declaration(ctx: Verilog2001Parser.Edge_sensitive_path_declarationContext) =
            this@Verilog2001Listener.enterEdge_sensitive_path_declaration(ctx)

        override suspend fun exitEdge_sensitive_path_declaration(ctx: Verilog2001Parser.Edge_sensitive_path_declarationContext) =
            this@Verilog2001Listener.exitEdge_sensitive_path_declaration(ctx)

        override suspend fun enterParallel_edge_sensitive_path_description(ctx: Verilog2001Parser.Parallel_edge_sensitive_path_descriptionContext) =
            this@Verilog2001Listener.enterParallel_edge_sensitive_path_description(ctx)

        override suspend fun exitParallel_edge_sensitive_path_description(ctx: Verilog2001Parser.Parallel_edge_sensitive_path_descriptionContext) =
            this@Verilog2001Listener.exitParallel_edge_sensitive_path_description(ctx)

        override suspend fun enterFull_edge_sensitive_path_description(ctx: Verilog2001Parser.Full_edge_sensitive_path_descriptionContext) =
            this@Verilog2001Listener.enterFull_edge_sensitive_path_description(ctx)

        override suspend fun exitFull_edge_sensitive_path_description(ctx: Verilog2001Parser.Full_edge_sensitive_path_descriptionContext) =
            this@Verilog2001Listener.exitFull_edge_sensitive_path_description(ctx)

        override suspend fun enterData_source_expression(ctx: Verilog2001Parser.Data_source_expressionContext) =
            this@Verilog2001Listener.enterData_source_expression(ctx)

        override suspend fun exitData_source_expression(ctx: Verilog2001Parser.Data_source_expressionContext) =
            this@Verilog2001Listener.exitData_source_expression(ctx)

        override suspend fun enterEdge_identifier(ctx: Verilog2001Parser.Edge_identifierContext) =
            this@Verilog2001Listener.enterEdge_identifier(ctx)

        override suspend fun exitEdge_identifier(ctx: Verilog2001Parser.Edge_identifierContext) =
            this@Verilog2001Listener.exitEdge_identifier(ctx)

        override suspend fun enterState_dependent_path_declaration(ctx: Verilog2001Parser.State_dependent_path_declarationContext) =
            this@Verilog2001Listener.enterState_dependent_path_declaration(ctx)

        override suspend fun exitState_dependent_path_declaration(ctx: Verilog2001Parser.State_dependent_path_declarationContext) =
            this@Verilog2001Listener.exitState_dependent_path_declaration(ctx)

        override suspend fun enterPolarity_operator(ctx: Verilog2001Parser.Polarity_operatorContext) =
            this@Verilog2001Listener.enterPolarity_operator(ctx)

        override suspend fun exitPolarity_operator(ctx: Verilog2001Parser.Polarity_operatorContext) =
            this@Verilog2001Listener.exitPolarity_operator(ctx)

        override suspend fun enterChecktime_condition(ctx: Verilog2001Parser.Checktime_conditionContext) =
            this@Verilog2001Listener.enterChecktime_condition(ctx)

        override suspend fun exitChecktime_condition(ctx: Verilog2001Parser.Checktime_conditionContext) =
            this@Verilog2001Listener.exitChecktime_condition(ctx)

        override suspend fun enterDelayed_data(ctx: Verilog2001Parser.Delayed_dataContext) =
            this@Verilog2001Listener.enterDelayed_data(ctx)

        override suspend fun exitDelayed_data(ctx: Verilog2001Parser.Delayed_dataContext) =
            this@Verilog2001Listener.exitDelayed_data(ctx)

        override suspend fun enterDelayed_reference(ctx: Verilog2001Parser.Delayed_referenceContext) =
            this@Verilog2001Listener.enterDelayed_reference(ctx)

        override suspend fun exitDelayed_reference(ctx: Verilog2001Parser.Delayed_referenceContext) =
            this@Verilog2001Listener.exitDelayed_reference(ctx)

        override suspend fun enterEnd_edge_offset(ctx: Verilog2001Parser.End_edge_offsetContext) =
            this@Verilog2001Listener.enterEnd_edge_offset(ctx)

        override suspend fun exitEnd_edge_offset(ctx: Verilog2001Parser.End_edge_offsetContext) =
            this@Verilog2001Listener.exitEnd_edge_offset(ctx)

        override suspend fun enterEvent_based_flag(ctx: Verilog2001Parser.Event_based_flagContext) =
            this@Verilog2001Listener.enterEvent_based_flag(ctx)

        override suspend fun exitEvent_based_flag(ctx: Verilog2001Parser.Event_based_flagContext) =
            this@Verilog2001Listener.exitEvent_based_flag(ctx)

        override suspend fun enterNotify_reg(ctx: Verilog2001Parser.Notify_regContext) =
            this@Verilog2001Listener.enterNotify_reg(ctx)

        override suspend fun exitNotify_reg(ctx: Verilog2001Parser.Notify_regContext) =
            this@Verilog2001Listener.exitNotify_reg(ctx)

        override suspend fun enterRemain_active_flag(ctx: Verilog2001Parser.Remain_active_flagContext) =
            this@Verilog2001Listener.enterRemain_active_flag(ctx)

        override suspend fun exitRemain_active_flag(ctx: Verilog2001Parser.Remain_active_flagContext) =
            this@Verilog2001Listener.exitRemain_active_flag(ctx)

        override suspend fun enterStamptime_condition(ctx: Verilog2001Parser.Stamptime_conditionContext) =
            this@Verilog2001Listener.enterStamptime_condition(ctx)

        override suspend fun exitStamptime_condition(ctx: Verilog2001Parser.Stamptime_conditionContext) =
            this@Verilog2001Listener.exitStamptime_condition(ctx)

        override suspend fun enterStart_edge_offset(ctx: Verilog2001Parser.Start_edge_offsetContext) =
            this@Verilog2001Listener.enterStart_edge_offset(ctx)

        override suspend fun exitStart_edge_offset(ctx: Verilog2001Parser.Start_edge_offsetContext) =
            this@Verilog2001Listener.exitStart_edge_offset(ctx)

        override suspend fun enterThreshold(ctx: Verilog2001Parser.ThresholdContext) =
            this@Verilog2001Listener.enterThreshold(ctx)

        override suspend fun exitThreshold(ctx: Verilog2001Parser.ThresholdContext) =
            this@Verilog2001Listener.exitThreshold(ctx)

        override suspend fun enterTiming_check_limit(ctx: Verilog2001Parser.Timing_check_limitContext) =
            this@Verilog2001Listener.enterTiming_check_limit(ctx)

        override suspend fun exitTiming_check_limit(ctx: Verilog2001Parser.Timing_check_limitContext) =
            this@Verilog2001Listener.exitTiming_check_limit(ctx)

        override suspend fun enterConcatenation(ctx: Verilog2001Parser.ConcatenationContext) =
            this@Verilog2001Listener.enterConcatenation(ctx)

        override suspend fun exitConcatenation(ctx: Verilog2001Parser.ConcatenationContext) =
            this@Verilog2001Listener.exitConcatenation(ctx)

        override suspend fun enterConstant_concatenation(ctx: Verilog2001Parser.Constant_concatenationContext) =
            this@Verilog2001Listener.enterConstant_concatenation(ctx)

        override suspend fun exitConstant_concatenation(ctx: Verilog2001Parser.Constant_concatenationContext) =
            this@Verilog2001Listener.exitConstant_concatenation(ctx)

        override suspend fun enterConstant_multiple_concatenation(ctx: Verilog2001Parser.Constant_multiple_concatenationContext) =
            this@Verilog2001Listener.enterConstant_multiple_concatenation(ctx)

        override suspend fun exitConstant_multiple_concatenation(ctx: Verilog2001Parser.Constant_multiple_concatenationContext) =
            this@Verilog2001Listener.exitConstant_multiple_concatenation(ctx)

        override suspend fun enterModule_path_concatenation(ctx: Verilog2001Parser.Module_path_concatenationContext) =
            this@Verilog2001Listener.enterModule_path_concatenation(ctx)

        override suspend fun exitModule_path_concatenation(ctx: Verilog2001Parser.Module_path_concatenationContext) =
            this@Verilog2001Listener.exitModule_path_concatenation(ctx)

        override suspend fun enterModule_path_multiple_concatenation(ctx: Verilog2001Parser.Module_path_multiple_concatenationContext) =
            this@Verilog2001Listener.enterModule_path_multiple_concatenation(ctx)

        override suspend fun exitModule_path_multiple_concatenation(ctx: Verilog2001Parser.Module_path_multiple_concatenationContext) =
            this@Verilog2001Listener.exitModule_path_multiple_concatenation(ctx)

        override suspend fun enterMultiple_concatenation(ctx: Verilog2001Parser.Multiple_concatenationContext) =
            this@Verilog2001Listener.enterMultiple_concatenation(ctx)

        override suspend fun exitMultiple_concatenation(ctx: Verilog2001Parser.Multiple_concatenationContext) =
            this@Verilog2001Listener.exitMultiple_concatenation(ctx)

        override suspend fun enterNet_concatenation(ctx: Verilog2001Parser.Net_concatenationContext) =
            this@Verilog2001Listener.enterNet_concatenation(ctx)

        override suspend fun exitNet_concatenation(ctx: Verilog2001Parser.Net_concatenationContext) =
            this@Verilog2001Listener.exitNet_concatenation(ctx)

        override suspend fun enterNet_concatenation_value(ctx: Verilog2001Parser.Net_concatenation_valueContext) =
            this@Verilog2001Listener.enterNet_concatenation_value(ctx)

        override suspend fun exitNet_concatenation_value(ctx: Verilog2001Parser.Net_concatenation_valueContext) =
            this@Verilog2001Listener.exitNet_concatenation_value(ctx)

        override suspend fun enterVariable_concatenation(ctx: Verilog2001Parser.Variable_concatenationContext) =
            this@Verilog2001Listener.enterVariable_concatenation(ctx)

        override suspend fun exitVariable_concatenation(ctx: Verilog2001Parser.Variable_concatenationContext) =
            this@Verilog2001Listener.exitVariable_concatenation(ctx)

        override suspend fun enterVariable_concatenation_value(ctx: Verilog2001Parser.Variable_concatenation_valueContext) =
            this@Verilog2001Listener.enterVariable_concatenation_value(ctx)

        override suspend fun exitVariable_concatenation_value(ctx: Verilog2001Parser.Variable_concatenation_valueContext) =
            this@Verilog2001Listener.exitVariable_concatenation_value(ctx)

        override suspend fun enterConstant_function_call(ctx: Verilog2001Parser.Constant_function_callContext) =
            this@Verilog2001Listener.enterConstant_function_call(ctx)

        override suspend fun exitConstant_function_call(ctx: Verilog2001Parser.Constant_function_callContext) =
            this@Verilog2001Listener.exitConstant_function_call(ctx)

        override suspend fun enterFunction_call(ctx: Verilog2001Parser.Function_callContext) =
            this@Verilog2001Listener.enterFunction_call(ctx)

        override suspend fun exitFunction_call(ctx: Verilog2001Parser.Function_callContext) =
            this@Verilog2001Listener.exitFunction_call(ctx)

        override suspend fun enterSystem_function_call(ctx: Verilog2001Parser.System_function_callContext) =
            this@Verilog2001Listener.enterSystem_function_call(ctx)

        override suspend fun exitSystem_function_call(ctx: Verilog2001Parser.System_function_callContext) =
            this@Verilog2001Listener.exitSystem_function_call(ctx)

        override suspend fun enterGenvar_function_call(ctx: Verilog2001Parser.Genvar_function_callContext) =
            this@Verilog2001Listener.enterGenvar_function_call(ctx)

        override suspend fun exitGenvar_function_call(ctx: Verilog2001Parser.Genvar_function_callContext) =
            this@Verilog2001Listener.exitGenvar_function_call(ctx)

        override suspend fun enterBase_expression(ctx: Verilog2001Parser.Base_expressionContext) =
            this@Verilog2001Listener.enterBase_expression(ctx)

        override suspend fun exitBase_expression(ctx: Verilog2001Parser.Base_expressionContext) =
            this@Verilog2001Listener.exitBase_expression(ctx)

        override suspend fun enterConstant_base_expression(ctx: Verilog2001Parser.Constant_base_expressionContext) =
            this@Verilog2001Listener.enterConstant_base_expression(ctx)

        override suspend fun exitConstant_base_expression(ctx: Verilog2001Parser.Constant_base_expressionContext) =
            this@Verilog2001Listener.exitConstant_base_expression(ctx)

        override suspend fun enterConstant_expression(ctx: Verilog2001Parser.Constant_expressionContext) =
            this@Verilog2001Listener.enterConstant_expression(ctx)

        override suspend fun exitConstant_expression(ctx: Verilog2001Parser.Constant_expressionContext) =
            this@Verilog2001Listener.exitConstant_expression(ctx)

        override suspend fun enterConstant_mintypmax_expression(ctx: Verilog2001Parser.Constant_mintypmax_expressionContext) =
            this@Verilog2001Listener.enterConstant_mintypmax_expression(ctx)

        override suspend fun exitConstant_mintypmax_expression(ctx: Verilog2001Parser.Constant_mintypmax_expressionContext) =
            this@Verilog2001Listener.exitConstant_mintypmax_expression(ctx)

        override suspend fun enterConstant_range_expression(ctx: Verilog2001Parser.Constant_range_expressionContext) =
            this@Verilog2001Listener.enterConstant_range_expression(ctx)

        override suspend fun exitConstant_range_expression(ctx: Verilog2001Parser.Constant_range_expressionContext) =
            this@Verilog2001Listener.exitConstant_range_expression(ctx)

        override suspend fun enterDimension_constant_expression(ctx: Verilog2001Parser.Dimension_constant_expressionContext) =
            this@Verilog2001Listener.enterDimension_constant_expression(ctx)

        override suspend fun exitDimension_constant_expression(ctx: Verilog2001Parser.Dimension_constant_expressionContext) =
            this@Verilog2001Listener.exitDimension_constant_expression(ctx)

        override suspend fun enterExpression(ctx: Verilog2001Parser.ExpressionContext) =
            this@Verilog2001Listener.enterExpression(ctx)

        override suspend fun exitExpression(ctx: Verilog2001Parser.ExpressionContext) =
            this@Verilog2001Listener.exitExpression(ctx)

        override suspend fun enterTerm(ctx: Verilog2001Parser.TermContext) = this@Verilog2001Listener.enterTerm(ctx)
        override suspend fun exitTerm(ctx: Verilog2001Parser.TermContext) = this@Verilog2001Listener.exitTerm(ctx)

        override suspend fun enterLsb_constant_expression(ctx: Verilog2001Parser.Lsb_constant_expressionContext) =
            this@Verilog2001Listener.enterLsb_constant_expression(ctx)

        override suspend fun exitLsb_constant_expression(ctx: Verilog2001Parser.Lsb_constant_expressionContext) =
            this@Verilog2001Listener.exitLsb_constant_expression(ctx)

        override suspend fun enterMintypmax_expression(ctx: Verilog2001Parser.Mintypmax_expressionContext) =
            this@Verilog2001Listener.enterMintypmax_expression(ctx)

        override suspend fun exitMintypmax_expression(ctx: Verilog2001Parser.Mintypmax_expressionContext) =
            this@Verilog2001Listener.exitMintypmax_expression(ctx)

        override suspend fun enterModule_path_conditional_expression(ctx: Verilog2001Parser.Module_path_conditional_expressionContext) =
            this@Verilog2001Listener.enterModule_path_conditional_expression(ctx)

        override suspend fun exitModule_path_conditional_expression(ctx: Verilog2001Parser.Module_path_conditional_expressionContext) =
            this@Verilog2001Listener.exitModule_path_conditional_expression(ctx)

        override suspend fun enterModule_path_expression(ctx: Verilog2001Parser.Module_path_expressionContext) =
            this@Verilog2001Listener.enterModule_path_expression(ctx)

        override suspend fun exitModule_path_expression(ctx: Verilog2001Parser.Module_path_expressionContext) =
            this@Verilog2001Listener.exitModule_path_expression(ctx)

        override suspend fun enterModule_path_mintypmax_expression(ctx: Verilog2001Parser.Module_path_mintypmax_expressionContext) =
            this@Verilog2001Listener.enterModule_path_mintypmax_expression(ctx)

        override suspend fun exitModule_path_mintypmax_expression(ctx: Verilog2001Parser.Module_path_mintypmax_expressionContext) =
            this@Verilog2001Listener.exitModule_path_mintypmax_expression(ctx)

        override suspend fun enterMsb_constant_expression(ctx: Verilog2001Parser.Msb_constant_expressionContext) =
            this@Verilog2001Listener.enterMsb_constant_expression(ctx)

        override suspend fun exitMsb_constant_expression(ctx: Verilog2001Parser.Msb_constant_expressionContext) =
            this@Verilog2001Listener.exitMsb_constant_expression(ctx)

        override suspend fun enterRange_expression(ctx: Verilog2001Parser.Range_expressionContext) =
            this@Verilog2001Listener.enterRange_expression(ctx)

        override suspend fun exitRange_expression(ctx: Verilog2001Parser.Range_expressionContext) =
            this@Verilog2001Listener.exitRange_expression(ctx)

        override suspend fun enterWidth_constant_expression(ctx: Verilog2001Parser.Width_constant_expressionContext) =
            this@Verilog2001Listener.enterWidth_constant_expression(ctx)

        override suspend fun exitWidth_constant_expression(ctx: Verilog2001Parser.Width_constant_expressionContext) =
            this@Verilog2001Listener.exitWidth_constant_expression(ctx)

        override suspend fun enterConstant_primary(ctx: Verilog2001Parser.Constant_primaryContext) =
            this@Verilog2001Listener.enterConstant_primary(ctx)

        override suspend fun exitConstant_primary(ctx: Verilog2001Parser.Constant_primaryContext) =
            this@Verilog2001Listener.exitConstant_primary(ctx)

        override suspend fun enterModule_path_primary(ctx: Verilog2001Parser.Module_path_primaryContext) =
            this@Verilog2001Listener.enterModule_path_primary(ctx)

        override suspend fun exitModule_path_primary(ctx: Verilog2001Parser.Module_path_primaryContext) =
            this@Verilog2001Listener.exitModule_path_primary(ctx)

        override suspend fun enterPrimary(ctx: Verilog2001Parser.PrimaryContext) =
            this@Verilog2001Listener.enterPrimary(ctx)

        override suspend fun exitPrimary(ctx: Verilog2001Parser.PrimaryContext) =
            this@Verilog2001Listener.exitPrimary(ctx)

        override suspend fun enterNet_lvalue(ctx: Verilog2001Parser.Net_lvalueContext) =
            this@Verilog2001Listener.enterNet_lvalue(ctx)

        override suspend fun exitNet_lvalue(ctx: Verilog2001Parser.Net_lvalueContext) =
            this@Verilog2001Listener.exitNet_lvalue(ctx)

        override suspend fun enterVariable_lvalue(ctx: Verilog2001Parser.Variable_lvalueContext) =
            this@Verilog2001Listener.enterVariable_lvalue(ctx)

        override suspend fun exitVariable_lvalue(ctx: Verilog2001Parser.Variable_lvalueContext) =
            this@Verilog2001Listener.exitVariable_lvalue(ctx)

        override suspend fun enterUnary_operator(ctx: Verilog2001Parser.Unary_operatorContext) =
            this@Verilog2001Listener.enterUnary_operator(ctx)

        override suspend fun exitUnary_operator(ctx: Verilog2001Parser.Unary_operatorContext) =
            this@Verilog2001Listener.exitUnary_operator(ctx)

        override suspend fun enterBinary_operator(ctx: Verilog2001Parser.Binary_operatorContext) =
            this@Verilog2001Listener.enterBinary_operator(ctx)

        override suspend fun exitBinary_operator(ctx: Verilog2001Parser.Binary_operatorContext) =
            this@Verilog2001Listener.exitBinary_operator(ctx)

        override suspend fun enterUnary_module_path_operator(ctx: Verilog2001Parser.Unary_module_path_operatorContext) =
            this@Verilog2001Listener.enterUnary_module_path_operator(ctx)

        override suspend fun exitUnary_module_path_operator(ctx: Verilog2001Parser.Unary_module_path_operatorContext) =
            this@Verilog2001Listener.exitUnary_module_path_operator(ctx)

        override suspend fun enterBinary_module_path_operator(ctx: Verilog2001Parser.Binary_module_path_operatorContext) =
            this@Verilog2001Listener.enterBinary_module_path_operator(ctx)

        override suspend fun exitBinary_module_path_operator(ctx: Verilog2001Parser.Binary_module_path_operatorContext) =
            this@Verilog2001Listener.exitBinary_module_path_operator(ctx)

        override suspend fun enterNumber(ctx: Verilog2001Parser.NumberContext) =
            this@Verilog2001Listener.enterNumber(ctx)

        override suspend fun exitNumber(ctx: Verilog2001Parser.NumberContext) = this@Verilog2001Listener.exitNumber(ctx)

        override suspend fun enterAttribute_instance(ctx: Verilog2001Parser.Attribute_instanceContext) =
            this@Verilog2001Listener.enterAttribute_instance(ctx)

        override suspend fun exitAttribute_instance(ctx: Verilog2001Parser.Attribute_instanceContext) =
            this@Verilog2001Listener.exitAttribute_instance(ctx)

        override suspend fun enterAttr_spec(ctx: Verilog2001Parser.Attr_specContext) =
            this@Verilog2001Listener.enterAttr_spec(ctx)

        override suspend fun exitAttr_spec(ctx: Verilog2001Parser.Attr_specContext) =
            this@Verilog2001Listener.exitAttr_spec(ctx)

        override suspend fun enterAttr_name(ctx: Verilog2001Parser.Attr_nameContext) =
            this@Verilog2001Listener.enterAttr_name(ctx)

        override suspend fun exitAttr_name(ctx: Verilog2001Parser.Attr_nameContext) =
            this@Verilog2001Listener.exitAttr_name(ctx)

        override suspend fun enterArrayed_identifier(ctx: Verilog2001Parser.Arrayed_identifierContext) =
            this@Verilog2001Listener.enterArrayed_identifier(ctx)

        override suspend fun exitArrayed_identifier(ctx: Verilog2001Parser.Arrayed_identifierContext) =
            this@Verilog2001Listener.exitArrayed_identifier(ctx)

        override suspend fun enterBlock_identifier(ctx: Verilog2001Parser.Block_identifierContext) =
            this@Verilog2001Listener.enterBlock_identifier(ctx)

        override suspend fun exitBlock_identifier(ctx: Verilog2001Parser.Block_identifierContext) =
            this@Verilog2001Listener.exitBlock_identifier(ctx)

        override suspend fun enterCell_identifier(ctx: Verilog2001Parser.Cell_identifierContext) =
            this@Verilog2001Listener.enterCell_identifier(ctx)

        override suspend fun exitCell_identifier(ctx: Verilog2001Parser.Cell_identifierContext) =
            this@Verilog2001Listener.exitCell_identifier(ctx)

        override suspend fun enterConfig_identifier(ctx: Verilog2001Parser.Config_identifierContext) =
            this@Verilog2001Listener.enterConfig_identifier(ctx)

        override suspend fun exitConfig_identifier(ctx: Verilog2001Parser.Config_identifierContext) =
            this@Verilog2001Listener.exitConfig_identifier(ctx)

        override suspend fun enterEscaped_arrayed_identifier(ctx: Verilog2001Parser.Escaped_arrayed_identifierContext) =
            this@Verilog2001Listener.enterEscaped_arrayed_identifier(ctx)

        override suspend fun exitEscaped_arrayed_identifier(ctx: Verilog2001Parser.Escaped_arrayed_identifierContext) =
            this@Verilog2001Listener.exitEscaped_arrayed_identifier(ctx)

        override suspend fun enterEscaped_hierarchical_identifier(ctx: Verilog2001Parser.Escaped_hierarchical_identifierContext) =
            this@Verilog2001Listener.enterEscaped_hierarchical_identifier(ctx)

        override suspend fun exitEscaped_hierarchical_identifier(ctx: Verilog2001Parser.Escaped_hierarchical_identifierContext) =
            this@Verilog2001Listener.exitEscaped_hierarchical_identifier(ctx)

        override suspend fun enterEvent_identifier(ctx: Verilog2001Parser.Event_identifierContext) =
            this@Verilog2001Listener.enterEvent_identifier(ctx)

        override suspend fun exitEvent_identifier(ctx: Verilog2001Parser.Event_identifierContext) =
            this@Verilog2001Listener.exitEvent_identifier(ctx)

        override suspend fun enterFunction_identifier(ctx: Verilog2001Parser.Function_identifierContext) =
            this@Verilog2001Listener.enterFunction_identifier(ctx)

        override suspend fun exitFunction_identifier(ctx: Verilog2001Parser.Function_identifierContext) =
            this@Verilog2001Listener.exitFunction_identifier(ctx)

        override suspend fun enterGate_instance_identifier(ctx: Verilog2001Parser.Gate_instance_identifierContext) =
            this@Verilog2001Listener.enterGate_instance_identifier(ctx)

        override suspend fun exitGate_instance_identifier(ctx: Verilog2001Parser.Gate_instance_identifierContext) =
            this@Verilog2001Listener.exitGate_instance_identifier(ctx)

        override suspend fun enterGenerate_block_identifier(ctx: Verilog2001Parser.Generate_block_identifierContext) =
            this@Verilog2001Listener.enterGenerate_block_identifier(ctx)

        override suspend fun exitGenerate_block_identifier(ctx: Verilog2001Parser.Generate_block_identifierContext) =
            this@Verilog2001Listener.exitGenerate_block_identifier(ctx)

        override suspend fun enterGenvar_function_identifier(ctx: Verilog2001Parser.Genvar_function_identifierContext) =
            this@Verilog2001Listener.enterGenvar_function_identifier(ctx)

        override suspend fun exitGenvar_function_identifier(ctx: Verilog2001Parser.Genvar_function_identifierContext) =
            this@Verilog2001Listener.exitGenvar_function_identifier(ctx)

        override suspend fun enterGenvar_identifier(ctx: Verilog2001Parser.Genvar_identifierContext) =
            this@Verilog2001Listener.enterGenvar_identifier(ctx)

        override suspend fun exitGenvar_identifier(ctx: Verilog2001Parser.Genvar_identifierContext) =
            this@Verilog2001Listener.exitGenvar_identifier(ctx)

        override suspend fun enterHierarchical_block_identifier(ctx: Verilog2001Parser.Hierarchical_block_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_block_identifier(ctx)

        override suspend fun exitHierarchical_block_identifier(ctx: Verilog2001Parser.Hierarchical_block_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_block_identifier(ctx)

        override suspend fun enterHierarchical_event_identifier(ctx: Verilog2001Parser.Hierarchical_event_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_event_identifier(ctx)

        override suspend fun exitHierarchical_event_identifier(ctx: Verilog2001Parser.Hierarchical_event_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_event_identifier(ctx)

        override suspend fun enterHierarchical_function_identifier(ctx: Verilog2001Parser.Hierarchical_function_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_function_identifier(ctx)

        override suspend fun exitHierarchical_function_identifier(ctx: Verilog2001Parser.Hierarchical_function_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_function_identifier(ctx)

        override suspend fun enterHierarchical_identifier(ctx: Verilog2001Parser.Hierarchical_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_identifier(ctx)

        override suspend fun exitHierarchical_identifier(ctx: Verilog2001Parser.Hierarchical_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_identifier(ctx)

        override suspend fun enterHierarchical_net_identifier(ctx: Verilog2001Parser.Hierarchical_net_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_net_identifier(ctx)

        override suspend fun exitHierarchical_net_identifier(ctx: Verilog2001Parser.Hierarchical_net_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_net_identifier(ctx)

        override suspend fun enterHierarchical_variable_identifier(ctx: Verilog2001Parser.Hierarchical_variable_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_variable_identifier(ctx)

        override suspend fun exitHierarchical_variable_identifier(ctx: Verilog2001Parser.Hierarchical_variable_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_variable_identifier(ctx)

        override suspend fun enterHierarchical_task_identifier(ctx: Verilog2001Parser.Hierarchical_task_identifierContext) =
            this@Verilog2001Listener.enterHierarchical_task_identifier(ctx)

        override suspend fun exitHierarchical_task_identifier(ctx: Verilog2001Parser.Hierarchical_task_identifierContext) =
            this@Verilog2001Listener.exitHierarchical_task_identifier(ctx)

        override suspend fun enterIdentifier(ctx: Verilog2001Parser.IdentifierContext) =
            this@Verilog2001Listener.enterIdentifier(ctx)

        override suspend fun exitIdentifier(ctx: Verilog2001Parser.IdentifierContext) =
            this@Verilog2001Listener.exitIdentifier(ctx)

        override suspend fun enterInout_port_identifier(ctx: Verilog2001Parser.Inout_port_identifierContext) =
            this@Verilog2001Listener.enterInout_port_identifier(ctx)

        override suspend fun exitInout_port_identifier(ctx: Verilog2001Parser.Inout_port_identifierContext) =
            this@Verilog2001Listener.exitInout_port_identifier(ctx)

        override suspend fun enterInput_port_identifier(ctx: Verilog2001Parser.Input_port_identifierContext) =
            this@Verilog2001Listener.enterInput_port_identifier(ctx)

        override suspend fun exitInput_port_identifier(ctx: Verilog2001Parser.Input_port_identifierContext) =
            this@Verilog2001Listener.exitInput_port_identifier(ctx)

        override suspend fun enterInstance_identifier(ctx: Verilog2001Parser.Instance_identifierContext) =
            this@Verilog2001Listener.enterInstance_identifier(ctx)

        override suspend fun exitInstance_identifier(ctx: Verilog2001Parser.Instance_identifierContext) =
            this@Verilog2001Listener.exitInstance_identifier(ctx)

        override suspend fun enterLibrary_identifier(ctx: Verilog2001Parser.Library_identifierContext) =
            this@Verilog2001Listener.enterLibrary_identifier(ctx)

        override suspend fun exitLibrary_identifier(ctx: Verilog2001Parser.Library_identifierContext) =
            this@Verilog2001Listener.exitLibrary_identifier(ctx)

        override suspend fun enterMemory_identifier(ctx: Verilog2001Parser.Memory_identifierContext) =
            this@Verilog2001Listener.enterMemory_identifier(ctx)

        override suspend fun exitMemory_identifier(ctx: Verilog2001Parser.Memory_identifierContext) =
            this@Verilog2001Listener.exitMemory_identifier(ctx)

        override suspend fun enterModule_identifier(ctx: Verilog2001Parser.Module_identifierContext) =
            this@Verilog2001Listener.enterModule_identifier(ctx)

        override suspend fun exitModule_identifier(ctx: Verilog2001Parser.Module_identifierContext) =
            this@Verilog2001Listener.exitModule_identifier(ctx)

        override suspend fun enterModule_instance_identifier(ctx: Verilog2001Parser.Module_instance_identifierContext) =
            this@Verilog2001Listener.enterModule_instance_identifier(ctx)

        override suspend fun exitModule_instance_identifier(ctx: Verilog2001Parser.Module_instance_identifierContext) =
            this@Verilog2001Listener.exitModule_instance_identifier(ctx)

        override suspend fun enterNet_identifier(ctx: Verilog2001Parser.Net_identifierContext) =
            this@Verilog2001Listener.enterNet_identifier(ctx)

        override suspend fun exitNet_identifier(ctx: Verilog2001Parser.Net_identifierContext) =
            this@Verilog2001Listener.exitNet_identifier(ctx)

        override suspend fun enterOutput_port_identifier(ctx: Verilog2001Parser.Output_port_identifierContext) =
            this@Verilog2001Listener.enterOutput_port_identifier(ctx)

        override suspend fun exitOutput_port_identifier(ctx: Verilog2001Parser.Output_port_identifierContext) =
            this@Verilog2001Listener.exitOutput_port_identifier(ctx)

        override suspend fun enterParameter_identifier(ctx: Verilog2001Parser.Parameter_identifierContext) =
            this@Verilog2001Listener.enterParameter_identifier(ctx)

        override suspend fun exitParameter_identifier(ctx: Verilog2001Parser.Parameter_identifierContext) =
            this@Verilog2001Listener.exitParameter_identifier(ctx)

        override suspend fun enterPort_identifier(ctx: Verilog2001Parser.Port_identifierContext) =
            this@Verilog2001Listener.enterPort_identifier(ctx)

        override suspend fun exitPort_identifier(ctx: Verilog2001Parser.Port_identifierContext) =
            this@Verilog2001Listener.exitPort_identifier(ctx)

        override suspend fun enterReal_identifier(ctx: Verilog2001Parser.Real_identifierContext) =
            this@Verilog2001Listener.enterReal_identifier(ctx)

        override suspend fun exitReal_identifier(ctx: Verilog2001Parser.Real_identifierContext) =
            this@Verilog2001Listener.exitReal_identifier(ctx)

        override suspend fun enterSimple_arrayed_identifier(ctx: Verilog2001Parser.Simple_arrayed_identifierContext) =
            this@Verilog2001Listener.enterSimple_arrayed_identifier(ctx)

        override suspend fun exitSimple_arrayed_identifier(ctx: Verilog2001Parser.Simple_arrayed_identifierContext) =
            this@Verilog2001Listener.exitSimple_arrayed_identifier(ctx)

        override suspend fun enterSimple_hierarchical_identifier(ctx: Verilog2001Parser.Simple_hierarchical_identifierContext) =
            this@Verilog2001Listener.enterSimple_hierarchical_identifier(ctx)

        override suspend fun exitSimple_hierarchical_identifier(ctx: Verilog2001Parser.Simple_hierarchical_identifierContext) =
            this@Verilog2001Listener.exitSimple_hierarchical_identifier(ctx)

        override suspend fun enterSpecparam_identifier(ctx: Verilog2001Parser.Specparam_identifierContext) =
            this@Verilog2001Listener.enterSpecparam_identifier(ctx)

        override suspend fun exitSpecparam_identifier(ctx: Verilog2001Parser.Specparam_identifierContext) =
            this@Verilog2001Listener.exitSpecparam_identifier(ctx)

        override suspend fun enterSystem_function_identifier(ctx: Verilog2001Parser.System_function_identifierContext) =
            this@Verilog2001Listener.enterSystem_function_identifier(ctx)

        override suspend fun exitSystem_function_identifier(ctx: Verilog2001Parser.System_function_identifierContext) =
            this@Verilog2001Listener.exitSystem_function_identifier(ctx)

        override suspend fun enterSystem_task_identifier(ctx: Verilog2001Parser.System_task_identifierContext) =
            this@Verilog2001Listener.enterSystem_task_identifier(ctx)

        override suspend fun exitSystem_task_identifier(ctx: Verilog2001Parser.System_task_identifierContext) =
            this@Verilog2001Listener.exitSystem_task_identifier(ctx)

        override suspend fun enterTask_identifier(ctx: Verilog2001Parser.Task_identifierContext) =
            this@Verilog2001Listener.enterTask_identifier(ctx)

        override suspend fun exitTask_identifier(ctx: Verilog2001Parser.Task_identifierContext) =
            this@Verilog2001Listener.exitTask_identifier(ctx)

        override suspend fun enterTerminal_identifier(ctx: Verilog2001Parser.Terminal_identifierContext) =
            this@Verilog2001Listener.enterTerminal_identifier(ctx)

        override suspend fun exitTerminal_identifier(ctx: Verilog2001Parser.Terminal_identifierContext) =
            this@Verilog2001Listener.exitTerminal_identifier(ctx)

        override suspend fun enterText_macro_identifier(ctx: Verilog2001Parser.Text_macro_identifierContext) =
            this@Verilog2001Listener.enterText_macro_identifier(ctx)

        override suspend fun exitText_macro_identifier(ctx: Verilog2001Parser.Text_macro_identifierContext) =
            this@Verilog2001Listener.exitText_macro_identifier(ctx)

        override suspend fun enterTopmodule_identifier(ctx: Verilog2001Parser.Topmodule_identifierContext) =
            this@Verilog2001Listener.enterTopmodule_identifier(ctx)

        override suspend fun exitTopmodule_identifier(ctx: Verilog2001Parser.Topmodule_identifierContext) =
            this@Verilog2001Listener.exitTopmodule_identifier(ctx)

        override suspend fun enterUdp_identifier(ctx: Verilog2001Parser.Udp_identifierContext) =
            this@Verilog2001Listener.enterUdp_identifier(ctx)

        override suspend fun exitUdp_identifier(ctx: Verilog2001Parser.Udp_identifierContext) =
            this@Verilog2001Listener.exitUdp_identifier(ctx)

        override suspend fun enterUdp_instance_identifier(ctx: Verilog2001Parser.Udp_instance_identifierContext) =
            this@Verilog2001Listener.enterUdp_instance_identifier(ctx)

        override suspend fun exitUdp_instance_identifier(ctx: Verilog2001Parser.Udp_instance_identifierContext) =
            this@Verilog2001Listener.exitUdp_instance_identifier(ctx)

        override suspend fun enterVariable_identifier(ctx: Verilog2001Parser.Variable_identifierContext) =
            this@Verilog2001Listener.enterVariable_identifier(ctx)

        override suspend fun exitVariable_identifier(ctx: Verilog2001Parser.Variable_identifierContext) =
            this@Verilog2001Listener.exitVariable_identifier(ctx)

        override suspend fun enterSimple_hierarchical_branch(ctx: Verilog2001Parser.Simple_hierarchical_branchContext) =
            this@Verilog2001Listener.enterSimple_hierarchical_branch(ctx)

        override suspend fun exitSimple_hierarchical_branch(ctx: Verilog2001Parser.Simple_hierarchical_branchContext) =
            this@Verilog2001Listener.exitSimple_hierarchical_branch(ctx)

        override suspend fun enterEscaped_hierarchical_branch(ctx: Verilog2001Parser.Escaped_hierarchical_branchContext) =
            this@Verilog2001Listener.enterEscaped_hierarchical_branch(ctx)

        override suspend fun exitEscaped_hierarchical_branch(ctx: Verilog2001Parser.Escaped_hierarchical_branchContext) =
            this@Verilog2001Listener.exitEscaped_hierarchical_branch(ctx)

        override suspend fun enterEveryRule(ctx: ParserRuleContext) = this@Verilog2001Listener.enterEveryRule(ctx)
        override suspend fun exitEveryRule(ctx: ParserRuleContext) = this@Verilog2001Listener.exitEveryRule(ctx)

        override suspend fun visitErrorNode(node: ErrorNode) = this@Verilog2001Listener.visitErrorNode(node)
        override suspend fun visitTerminal(node: TerminalNode) = this@Verilog2001Listener.visitTerminal(node)
    }
}

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Verilog2001Parser}.
 */
interface SuspendVerilog2001Listener : SuspendParseTreeListener {
    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#config_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterConfig_declaration(ctx: Verilog2001Parser.Config_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#config_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitConfig_declaration(ctx: Verilog2001Parser.Config_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#design_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterDesign_statement(ctx: Verilog2001Parser.Design_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#design_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitDesign_statement(ctx: Verilog2001Parser.Design_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#config_rule_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterConfig_rule_statement(ctx: Verilog2001Parser.Config_rule_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#config_rule_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitConfig_rule_statement(ctx: Verilog2001Parser.Config_rule_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#default_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterDefault_clause(ctx: Verilog2001Parser.Default_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#default_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitDefault_clause(ctx: Verilog2001Parser.Default_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inst_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterInst_clause(ctx: Verilog2001Parser.Inst_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inst_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitInst_clause(ctx: Verilog2001Parser.Inst_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inst_name}.
     * @param ctx the parse tree
     */
    suspend fun enterInst_name(ctx: Verilog2001Parser.Inst_nameContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inst_name}.
     * @param ctx the parse tree
     */
    suspend fun exitInst_name(ctx: Verilog2001Parser.Inst_nameContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#liblist_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterLiblist_clause(ctx: Verilog2001Parser.Liblist_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#liblist_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitLiblist_clause(ctx: Verilog2001Parser.Liblist_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cell_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterCell_clause(ctx: Verilog2001Parser.Cell_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cell_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitCell_clause(ctx: Verilog2001Parser.Cell_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#use_clause}.
     * @param ctx the parse tree
     */
    suspend fun enterUse_clause(ctx: Verilog2001Parser.Use_clauseContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#use_clause}.
     * @param ctx the parse tree
     */
    suspend fun exitUse_clause(ctx: Verilog2001Parser.Use_clauseContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#source_text}.
     * @param ctx the parse tree
     */
    suspend fun enterSource_text(ctx: Verilog2001Parser.Source_textContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#source_text}.
     * @param ctx the parse tree
     */
    suspend fun exitSource_text(ctx: Verilog2001Parser.Source_textContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#description}.
     * @param ctx the parse tree
     */
    suspend fun enterDescription(ctx: Verilog2001Parser.DescriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#description}.
     * @param ctx the parse tree
     */
    suspend fun exitDescription(ctx: Verilog2001Parser.DescriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_declaration(ctx: Verilog2001Parser.Module_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_declaration(ctx: Verilog2001Parser.Module_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_keyword}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_keyword(ctx: Verilog2001Parser.Module_keywordContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_keyword}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_keyword(ctx: Verilog2001Parser.Module_keywordContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_parameter_port_list(ctx: Verilog2001Parser.Module_parameter_port_listContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_parameter_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_parameter_port_list(ctx: Verilog2001Parser.Module_parameter_port_listContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_ports(ctx: Verilog2001Parser.List_of_portsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_ports}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_ports(ctx: Verilog2001Parser.List_of_portsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_port_declarations(ctx: Verilog2001Parser.List_of_port_declarationsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_declarations}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_port_declarations(ctx: Verilog2001Parser.List_of_port_declarationsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port}.
     * @param ctx the parse tree
     */
    suspend fun enterPort(ctx: Verilog2001Parser.PortContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port}.
     * @param ctx the parse tree
     */
    suspend fun exitPort(ctx: Verilog2001Parser.PortContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_expression(ctx: Verilog2001Parser.Port_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_expression(ctx: Verilog2001Parser.Port_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_reference}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_reference(ctx: Verilog2001Parser.Port_referenceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_reference}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_reference(ctx: Verilog2001Parser.Port_referenceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_declaration(ctx: Verilog2001Parser.Port_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_declaration(ctx: Verilog2001Parser.Port_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_item}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_item(ctx: Verilog2001Parser.Module_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_item}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_item(ctx: Verilog2001Parser.Module_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_or_generate_item(ctx: Verilog2001Parser.Module_or_generate_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_or_generate_item}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_or_generate_item(ctx: Verilog2001Parser.Module_or_generate_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#non_port_module_item}.
     * @param ctx the parse tree
     */
    suspend fun enterNon_port_module_item(ctx: Verilog2001Parser.Non_port_module_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#non_port_module_item}.
     * @param ctx the parse tree
     */
    suspend fun exitNon_port_module_item(ctx: Verilog2001Parser.Non_port_module_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_or_generate_item_declaration(ctx: Verilog2001Parser.Module_or_generate_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_or_generate_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_or_generate_item_declaration(ctx: Verilog2001Parser.Module_or_generate_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_override}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_override(ctx: Verilog2001Parser.Parameter_overrideContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_override}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_override(ctx: Verilog2001Parser.Parameter_overrideContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterLocal_parameter_declaration(ctx: Verilog2001Parser.Local_parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#local_parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitLocal_parameter_declaration(ctx: Verilog2001Parser.Local_parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_declaration(ctx: Verilog2001Parser.Parameter_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_declaration(ctx: Verilog2001Parser.Parameter_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_declaration_(ctx: Verilog2001Parser.Parameter_declaration_Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_declaration_}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_declaration_(ctx: Verilog2001Parser.Parameter_declaration_Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specparam_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecparam_declaration(ctx: Verilog2001Parser.Specparam_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specparam_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecparam_declaration(ctx: Verilog2001Parser.Specparam_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inout_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterInout_declaration(ctx: Verilog2001Parser.Inout_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inout_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitInout_declaration(ctx: Verilog2001Parser.Inout_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_declaration(ctx: Verilog2001Parser.Input_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_declaration(ctx: Verilog2001Parser.Input_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_declaration(ctx: Verilog2001Parser.Output_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_declaration(ctx: Verilog2001Parser.Output_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_declaration(ctx: Verilog2001Parser.Event_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_declaration(ctx: Verilog2001Parser.Event_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_declaration(ctx: Verilog2001Parser.Genvar_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_declaration(ctx: Verilog2001Parser.Genvar_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#integer_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterInteger_declaration(ctx: Verilog2001Parser.Integer_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#integer_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitInteger_declaration(ctx: Verilog2001Parser.Integer_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#time_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTime_declaration(ctx: Verilog2001Parser.Time_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#time_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTime_declaration(ctx: Verilog2001Parser.Time_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#real_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_declaration(ctx: Verilog2001Parser.Real_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#real_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_declaration(ctx: Verilog2001Parser.Real_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#realtime_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterRealtime_declaration(ctx: Verilog2001Parser.Realtime_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#realtime_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitRealtime_declaration(ctx: Verilog2001Parser.Realtime_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterReg_declaration(ctx: Verilog2001Parser.Reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitReg_declaration(ctx: Verilog2001Parser.Reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_declaration(ctx: Verilog2001Parser.Net_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_declaration(ctx: Verilog2001Parser.Net_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_type}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_type(ctx: Verilog2001Parser.Net_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_type}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_type(ctx: Verilog2001Parser.Net_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_variable_type(ctx: Verilog2001Parser.Output_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_variable_type(ctx: Verilog2001Parser.Output_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#real_type}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_type(ctx: Verilog2001Parser.Real_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#real_type}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_type(ctx: Verilog2001Parser.Real_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_type}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_type(ctx: Verilog2001Parser.Variable_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_type}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_type(ctx: Verilog2001Parser.Variable_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#drive_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterDrive_strength(ctx: Verilog2001Parser.Drive_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#drive_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitDrive_strength(ctx: Verilog2001Parser.Drive_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#strength0}.
     * @param ctx the parse tree
     */
    suspend fun enterStrength0(ctx: Verilog2001Parser.Strength0Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#strength0}.
     * @param ctx the parse tree
     */
    suspend fun exitStrength0(ctx: Verilog2001Parser.Strength0Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#strength1}.
     * @param ctx the parse tree
     */
    suspend fun enterStrength1(ctx: Verilog2001Parser.Strength1Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#strength1}.
     * @param ctx the parse tree
     */
    suspend fun exitStrength1(ctx: Verilog2001Parser.Strength1Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#charge_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterCharge_strength(ctx: Verilog2001Parser.Charge_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#charge_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitCharge_strength(ctx: Verilog2001Parser.Charge_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay3}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay3(ctx: Verilog2001Parser.Delay3Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay3}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay3(ctx: Verilog2001Parser.Delay3Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay2}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay2(ctx: Verilog2001Parser.Delay2Context)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay2}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay2(ctx: Verilog2001Parser.Delay2Context)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay_value}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay_value(ctx: Verilog2001Parser.Delay_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay_value}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay_value(ctx: Verilog2001Parser.Delay_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_event_identifiers(ctx: Verilog2001Parser.List_of_event_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_event_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_event_identifiers(ctx: Verilog2001Parser.List_of_event_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_net_identifiers(ctx: Verilog2001Parser.List_of_net_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_net_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_net_identifiers(ctx: Verilog2001Parser.List_of_net_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_genvar_identifiers(ctx: Verilog2001Parser.List_of_genvar_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_genvar_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_genvar_identifiers(ctx: Verilog2001Parser.List_of_genvar_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_port_identifiers(ctx: Verilog2001Parser.List_of_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_port_identifiers(ctx: Verilog2001Parser.List_of_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_net_decl_assignments(ctx: Verilog2001Parser.List_of_net_decl_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_net_decl_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_net_decl_assignments(ctx: Verilog2001Parser.List_of_net_decl_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_param_assignments(ctx: Verilog2001Parser.List_of_param_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_param_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_param_assignments(ctx: Verilog2001Parser.List_of_param_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_specparam_assignments(ctx: Verilog2001Parser.List_of_specparam_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_specparam_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_specparam_assignments(ctx: Verilog2001Parser.List_of_specparam_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_real_identifiers(ctx: Verilog2001Parser.List_of_real_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_real_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_real_identifiers(ctx: Verilog2001Parser.List_of_real_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_variable_identifiers(ctx: Verilog2001Parser.List_of_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_variable_identifiers(ctx: Verilog2001Parser.List_of_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_variable_port_identifiers(ctx: Verilog2001Parser.List_of_variable_port_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_variable_port_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_variable_port_identifiers(ctx: Verilog2001Parser.List_of_variable_port_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_decl_assignment(ctx: Verilog2001Parser.Net_decl_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_decl_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_decl_assignment(ctx: Verilog2001Parser.Net_decl_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#param_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterParam_assignment(ctx: Verilog2001Parser.Param_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#param_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitParam_assignment(ctx: Verilog2001Parser.Param_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specparam_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecparam_assignment(ctx: Verilog2001Parser.Specparam_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specparam_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecparam_assignment(ctx: Verilog2001Parser.Specparam_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    suspend fun enterPulse_control_specparam(ctx: Verilog2001Parser.Pulse_control_specparamContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pulse_control_specparam}.
     * @param ctx the parse tree
     */
    suspend fun exitPulse_control_specparam(ctx: Verilog2001Parser.Pulse_control_specparamContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#error_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun enterError_limit_value(ctx: Verilog2001Parser.Error_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#error_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun exitError_limit_value(ctx: Verilog2001Parser.Error_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#reject_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun enterReject_limit_value(ctx: Verilog2001Parser.Reject_limit_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#reject_limit_value}.
     * @param ctx the parse tree
     */
    suspend fun exitReject_limit_value(ctx: Verilog2001Parser.Reject_limit_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#limit_value}.
     * @param ctx the parse tree
     */
    suspend fun enterLimit_value(ctx: Verilog2001Parser.Limit_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#limit_value}.
     * @param ctx the parse tree
     */
    suspend fun exitLimit_value(ctx: Verilog2001Parser.Limit_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#dimension}.
     * @param ctx the parse tree
     */
    suspend fun enterDimension(ctx: Verilog2001Parser.DimensionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#dimension}.
     * @param ctx the parse tree
     */
    suspend fun exitDimension(ctx: Verilog2001Parser.DimensionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#range}.
     * @param ctx the parse tree
     */
    suspend fun enterRange(ctx: Verilog2001Parser.RangeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#range}.
     * @param ctx the parse tree
     */
    suspend fun exitRange(ctx: Verilog2001Parser.RangeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_declaration(ctx: Verilog2001Parser.Function_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_declaration(ctx: Verilog2001Parser.Function_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_item_declaration(ctx: Verilog2001Parser.Function_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_item_declaration(ctx: Verilog2001Parser.Function_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_port_list(ctx: Verilog2001Parser.Function_port_listContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_port_list(ctx: Verilog2001Parser.Function_port_listContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_port}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_port(ctx: Verilog2001Parser.Function_portContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_port}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_port(ctx: Verilog2001Parser.Function_portContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#range_or_type}.
     * @param ctx the parse tree
     */
    suspend fun enterRange_or_type(ctx: Verilog2001Parser.Range_or_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#range_or_type}.
     * @param ctx the parse tree
     */
    suspend fun exitRange_or_type(ctx: Verilog2001Parser.Range_or_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_declaration(ctx: Verilog2001Parser.Task_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_declaration(ctx: Verilog2001Parser.Task_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_item_declaration(ctx: Verilog2001Parser.Task_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_item_declaration(ctx: Verilog2001Parser.Task_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_port_list}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_port_list(ctx: Verilog2001Parser.Task_port_listContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_port_list}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_port_list(ctx: Verilog2001Parser.Task_port_listContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_port_item}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_port_item(ctx: Verilog2001Parser.Task_port_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_port_item}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_port_item(ctx: Verilog2001Parser.Task_port_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tf_decl_header}.
     * @param ctx the parse tree
     */
    suspend fun enterTf_decl_header(ctx: Verilog2001Parser.Tf_decl_headerContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tf_decl_header}.
     * @param ctx the parse tree
     */
    suspend fun exitTf_decl_header(ctx: Verilog2001Parser.Tf_decl_headerContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tf_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterTf_declaration(ctx: Verilog2001Parser.Tf_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tf_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitTf_declaration(ctx: Verilog2001Parser.Tf_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_port_type}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_port_type(ctx: Verilog2001Parser.Task_port_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_port_type}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_port_type(ctx: Verilog2001Parser.Task_port_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_item_declaration(ctx: Verilog2001Parser.Block_item_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_item_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_item_declaration(ctx: Verilog2001Parser.Block_item_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_reg_declaration(ctx: Verilog2001Parser.Block_reg_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_reg_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_reg_declaration(ctx: Verilog2001Parser.Block_reg_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_block_variable_identifiers(ctx: Verilog2001Parser.List_of_block_variable_identifiersContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_block_variable_identifiers}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_block_variable_identifiers(ctx: Verilog2001Parser.List_of_block_variable_identifiersContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_variable_type(ctx: Verilog2001Parser.Block_variable_typeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_variable_type}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_variable_type(ctx: Verilog2001Parser.Block_variable_typeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#gate_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun enterGate_instantiation(ctx: Verilog2001Parser.Gate_instantiationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#gate_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun exitGate_instantiation(ctx: Verilog2001Parser.Gate_instantiationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterCmos_switch_instance(ctx: Verilog2001Parser.Cmos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cmos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitCmos_switch_instance(ctx: Verilog2001Parser.Cmos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterEnable_gate_instance(ctx: Verilog2001Parser.Enable_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#enable_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitEnable_gate_instance(ctx: Verilog2001Parser.Enable_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterMos_switch_instance(ctx: Verilog2001Parser.Mos_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#mos_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitMos_switch_instance(ctx: Verilog2001Parser.Mos_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterN_input_gate_instance(ctx: Verilog2001Parser.N_input_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_input_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitN_input_gate_instance(ctx: Verilog2001Parser.N_input_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterN_output_gate_instance(ctx: Verilog2001Parser.N_output_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_output_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitN_output_gate_instance(ctx: Verilog2001Parser.N_output_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_switch_instance(ctx: Verilog2001Parser.Pass_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_switch_instance(ctx: Verilog2001Parser.Pass_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_enable_switch_instance(ctx: Verilog2001Parser.Pass_enable_switch_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_enable_switch_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_enable_switch_instance(ctx: Verilog2001Parser.Pass_enable_switch_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterPull_gate_instance(ctx: Verilog2001Parser.Pull_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pull_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitPull_gate_instance(ctx: Verilog2001Parser.Pull_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterName_of_gate_instance(ctx: Verilog2001Parser.Name_of_gate_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#name_of_gate_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitName_of_gate_instance(ctx: Verilog2001Parser.Name_of_gate_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pulldown_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterPulldown_strength(ctx: Verilog2001Parser.Pulldown_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pulldown_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitPulldown_strength(ctx: Verilog2001Parser.Pulldown_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pullup_strength}.
     * @param ctx the parse tree
     */
    suspend fun enterPullup_strength(ctx: Verilog2001Parser.Pullup_strengthContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pullup_strength}.
     * @param ctx the parse tree
     */
    suspend fun exitPullup_strength(ctx: Verilog2001Parser.Pullup_strengthContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#enable_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterEnable_terminal(ctx: Verilog2001Parser.Enable_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#enable_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitEnable_terminal(ctx: Verilog2001Parser.Enable_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterNcontrol_terminal(ctx: Verilog2001Parser.Ncontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#ncontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitNcontrol_terminal(ctx: Verilog2001Parser.Ncontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterPcontrol_terminal(ctx: Verilog2001Parser.Pcontrol_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pcontrol_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitPcontrol_terminal(ctx: Verilog2001Parser.Pcontrol_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_terminal(ctx: Verilog2001Parser.Input_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_terminal(ctx: Verilog2001Parser.Input_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inout_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterInout_terminal(ctx: Verilog2001Parser.Inout_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inout_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitInout_terminal(ctx: Verilog2001Parser.Inout_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_terminal}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_terminal(ctx: Verilog2001Parser.Output_terminalContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_terminal}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_terminal(ctx: Verilog2001Parser.Output_terminalContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterCmos_switchtype(ctx: Verilog2001Parser.Cmos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cmos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitCmos_switchtype(ctx: Verilog2001Parser.Cmos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#enable_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun enterEnable_gatetype(ctx: Verilog2001Parser.Enable_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#enable_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun exitEnable_gatetype(ctx: Verilog2001Parser.Enable_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#mos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterMos_switchtype(ctx: Verilog2001Parser.Mos_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#mos_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitMos_switchtype(ctx: Verilog2001Parser.Mos_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun enterN_input_gatetype(ctx: Verilog2001Parser.N_input_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_input_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun exitN_input_gatetype(ctx: Verilog2001Parser.N_input_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun enterN_output_gatetype(ctx: Verilog2001Parser.N_output_gatetypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#n_output_gatetype}.
     * @param ctx the parse tree
     */
    suspend fun exitN_output_gatetype(ctx: Verilog2001Parser.N_output_gatetypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_en_switchtype(ctx: Verilog2001Parser.Pass_en_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_en_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_en_switchtype(ctx: Verilog2001Parser.Pass_en_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pass_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun enterPass_switchtype(ctx: Verilog2001Parser.Pass_switchtypeContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pass_switchtype}.
     * @param ctx the parse tree
     */
    suspend fun exitPass_switchtype(ctx: Verilog2001Parser.Pass_switchtypeContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_instantiation(ctx: Verilog2001Parser.Module_instantiationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_instantiation(ctx: Verilog2001Parser.Module_instantiationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_value_assignment(ctx: Verilog2001Parser.Parameter_value_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_value_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_value_assignment(ctx: Verilog2001Parser.Parameter_value_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_parameter_assignments(ctx: Verilog2001Parser.List_of_parameter_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_parameter_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_parameter_assignments(ctx: Verilog2001Parser.List_of_parameter_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterOrdered_parameter_assignment(ctx: Verilog2001Parser.Ordered_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#ordered_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitOrdered_parameter_assignment(ctx: Verilog2001Parser.Ordered_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNamed_parameter_assignment(ctx: Verilog2001Parser.Named_parameter_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#named_parameter_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNamed_parameter_assignment(ctx: Verilog2001Parser.Named_parameter_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_instance(ctx: Verilog2001Parser.Module_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_instance(ctx: Verilog2001Parser.Module_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterName_of_instance(ctx: Verilog2001Parser.Name_of_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#name_of_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitName_of_instance(ctx: Verilog2001Parser.Name_of_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_port_connections(ctx: Verilog2001Parser.List_of_port_connectionsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_port_connections}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_port_connections(ctx: Verilog2001Parser.List_of_port_connectionsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun enterOrdered_port_connection(ctx: Verilog2001Parser.Ordered_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#ordered_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun exitOrdered_port_connection(ctx: Verilog2001Parser.Ordered_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#named_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun enterNamed_port_connection(ctx: Verilog2001Parser.Named_port_connectionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#named_port_connection}.
     * @param ctx the parse tree
     */
    suspend fun exitNamed_port_connection(ctx: Verilog2001Parser.Named_port_connectionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generated_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerated_instantiation(ctx: Verilog2001Parser.Generated_instantiationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generated_instantiation}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerated_instantiation(ctx: Verilog2001Parser.Generated_instantiationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_item_or_null}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_item_or_null(ctx: Verilog2001Parser.Generate_item_or_nullContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_item_or_null}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_item_or_null(ctx: Verilog2001Parser.Generate_item_or_nullContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_item}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_item(ctx: Verilog2001Parser.Generate_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_item}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_item(ctx: Verilog2001Parser.Generate_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_conditional_statement(ctx: Verilog2001Parser.Generate_conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_conditional_statement(ctx: Verilog2001Parser.Generate_conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_case_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_case_statement(ctx: Verilog2001Parser.Generate_case_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_case_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_case_statement(ctx: Verilog2001Parser.Generate_case_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_case_item}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_case_item(ctx: Verilog2001Parser.Genvar_case_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_case_item}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_case_item(ctx: Verilog2001Parser.Genvar_case_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_loop_statement(ctx: Verilog2001Parser.Generate_loop_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_loop_statement(ctx: Verilog2001Parser.Generate_loop_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_assignment(ctx: Verilog2001Parser.Genvar_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_assignment(ctx: Verilog2001Parser.Genvar_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_block}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_block(ctx: Verilog2001Parser.Generate_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_block}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_block(ctx: Verilog2001Parser.Generate_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
     * @param ctx the parse tree
     */
    suspend fun enterContinuous_assign(ctx: Verilog2001Parser.Continuous_assignContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#continuous_assign}.
     * @param ctx the parse tree
     */
    suspend fun exitContinuous_assign(ctx: Verilog2001Parser.Continuous_assignContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_net_assignments(ctx: Verilog2001Parser.List_of_net_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_net_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_net_assignments(ctx: Verilog2001Parser.List_of_net_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_assignment(ctx: Verilog2001Parser.Net_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_assignment(ctx: Verilog2001Parser.Net_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#initial_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterInitial_construct(ctx: Verilog2001Parser.Initial_constructContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#initial_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitInitial_construct(ctx: Verilog2001Parser.Initial_constructContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#always_construct}.
     * @param ctx the parse tree
     */
    suspend fun enterAlways_construct(ctx: Verilog2001Parser.Always_constructContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#always_construct}.
     * @param ctx the parse tree
     */
    suspend fun exitAlways_construct(ctx: Verilog2001Parser.Always_constructContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterBlocking_assignment(ctx: Verilog2001Parser.Blocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#blocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitBlocking_assignment(ctx: Verilog2001Parser.Blocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterNonblocking_assignment(ctx: Verilog2001Parser.Nonblocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#nonblocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitNonblocking_assignment(ctx: Verilog2001Parser.Nonblocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    suspend fun enterProcedural_continuous_assignments(ctx: Verilog2001Parser.Procedural_continuous_assignmentsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#procedural_continuous_assignments}.
     * @param ctx the parse tree
     */
    suspend fun exitProcedural_continuous_assignments(ctx: Verilog2001Parser.Procedural_continuous_assignmentsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_blocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_blocking_assignment(ctx: Verilog2001Parser.Function_blocking_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_blocking_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_blocking_assignment(ctx: Verilog2001Parser.Function_blocking_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_statement_or_null}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_statement_or_null(ctx: Verilog2001Parser.Function_statement_or_nullContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_statement_or_null}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_statement_or_null(ctx: Verilog2001Parser.Function_statement_or_nullContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_seq_block}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_seq_block(ctx: Verilog2001Parser.Function_seq_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_seq_block}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_seq_block(ctx: Verilog2001Parser.Function_seq_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_assignment}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_assignment(ctx: Verilog2001Parser.Variable_assignmentContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_assignment}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_assignment(ctx: Verilog2001Parser.Variable_assignmentContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#par_block}.
     * @param ctx the parse tree
     */
    suspend fun enterPar_block(ctx: Verilog2001Parser.Par_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#par_block}.
     * @param ctx the parse tree
     */
    suspend fun exitPar_block(ctx: Verilog2001Parser.Par_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#seq_block}.
     * @param ctx the parse tree
     */
    suspend fun enterSeq_block(ctx: Verilog2001Parser.Seq_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#seq_block}.
     * @param ctx the parse tree
     */
    suspend fun exitSeq_block(ctx: Verilog2001Parser.Seq_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#statement}.
     * @param ctx the parse tree
     */
    suspend fun enterStatement(ctx: Verilog2001Parser.StatementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#statement}.
     * @param ctx the parse tree
     */
    suspend fun exitStatement(ctx: Verilog2001Parser.StatementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#statement_or_null}.
     * @param ctx the parse tree
     */
    suspend fun enterStatement_or_null(ctx: Verilog2001Parser.Statement_or_nullContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#statement_or_null}.
     * @param ctx the parse tree
     */
    suspend fun exitStatement_or_null(ctx: Verilog2001Parser.Statement_or_nullContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_statement(ctx: Verilog2001Parser.Function_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_statement(ctx: Verilog2001Parser.Function_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay_or_event_control(ctx: Verilog2001Parser.Delay_or_event_controlContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay_or_event_control}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay_or_event_control(ctx: Verilog2001Parser.Delay_or_event_controlContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delay_control}.
     * @param ctx the parse tree
     */
    suspend fun enterDelay_control(ctx: Verilog2001Parser.Delay_controlContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delay_control}.
     * @param ctx the parse tree
     */
    suspend fun exitDelay_control(ctx: Verilog2001Parser.Delay_controlContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#disable_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterDisable_statement(ctx: Verilog2001Parser.Disable_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#disable_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitDisable_statement(ctx: Verilog2001Parser.Disable_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_control}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_control(ctx: Verilog2001Parser.Event_controlContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_control}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_control(ctx: Verilog2001Parser.Event_controlContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_trigger}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_trigger(ctx: Verilog2001Parser.Event_triggerContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_trigger}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_trigger(ctx: Verilog2001Parser.Event_triggerContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_expression(ctx: Verilog2001Parser.Event_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_expression(ctx: Verilog2001Parser.Event_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_primary}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_primary(ctx: Verilog2001Parser.Event_primaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_primary}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_primary(ctx: Verilog2001Parser.Event_primaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterProcedural_timing_control_statement(ctx: Verilog2001Parser.Procedural_timing_control_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#procedural_timing_control_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitProcedural_timing_control_statement(ctx: Verilog2001Parser.Procedural_timing_control_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#wait_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterWait_statement(ctx: Verilog2001Parser.Wait_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#wait_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitWait_statement(ctx: Verilog2001Parser.Wait_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterConditional_statement(ctx: Verilog2001Parser.Conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitConditional_statement(ctx: Verilog2001Parser.Conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#if_else_if_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterIf_else_if_statement(ctx: Verilog2001Parser.If_else_if_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#if_else_if_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitIf_else_if_statement(ctx: Verilog2001Parser.If_else_if_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_conditional_statement(ctx: Verilog2001Parser.Function_conditional_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_conditional_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_conditional_statement(ctx: Verilog2001Parser.Function_conditional_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_if_else_if_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_if_else_if_statement(ctx: Verilog2001Parser.Function_if_else_if_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_if_else_if_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_if_else_if_statement(ctx: Verilog2001Parser.Function_if_else_if_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#case_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterCase_statement(ctx: Verilog2001Parser.Case_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#case_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitCase_statement(ctx: Verilog2001Parser.Case_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#case_item}.
     * @param ctx the parse tree
     */
    suspend fun enterCase_item(ctx: Verilog2001Parser.Case_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#case_item}.
     * @param ctx the parse tree
     */
    suspend fun exitCase_item(ctx: Verilog2001Parser.Case_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_case_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_case_statement(ctx: Verilog2001Parser.Function_case_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_case_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_case_statement(ctx: Verilog2001Parser.Function_case_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_case_item}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_case_item(ctx: Verilog2001Parser.Function_case_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_case_item}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_case_item(ctx: Verilog2001Parser.Function_case_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_loop_statement(ctx: Verilog2001Parser.Function_loop_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_loop_statement(ctx: Verilog2001Parser.Function_loop_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun enterLoop_statement(ctx: Verilog2001Parser.Loop_statementContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#loop_statement}.
     * @param ctx the parse tree
     */
    suspend fun exitLoop_statement(ctx: Verilog2001Parser.Loop_statementContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_task_enable}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_task_enable(ctx: Verilog2001Parser.System_task_enableContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_task_enable}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_task_enable(ctx: Verilog2001Parser.System_task_enableContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_enable}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_enable(ctx: Verilog2001Parser.Task_enableContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_enable}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_enable(ctx: Verilog2001Parser.Task_enableContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_block}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_block(ctx: Verilog2001Parser.Specify_blockContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_block}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_block(ctx: Verilog2001Parser.Specify_blockContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_item}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_item(ctx: Verilog2001Parser.Specify_itemContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_item}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_item(ctx: Verilog2001Parser.Specify_itemContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterPulsestyle_declaration(ctx: Verilog2001Parser.Pulsestyle_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#pulsestyle_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitPulsestyle_declaration(ctx: Verilog2001Parser.Pulsestyle_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterShowcancelled_declaration(ctx: Verilog2001Parser.Showcancelled_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#showcancelled_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitShowcancelled_declaration(ctx: Verilog2001Parser.Showcancelled_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterPath_declaration(ctx: Verilog2001Parser.Path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitPath_declaration(ctx: Verilog2001Parser.Path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterSimple_path_declaration(ctx: Verilog2001Parser.Simple_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitSimple_path_declaration(ctx: Verilog2001Parser.Simple_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parallel_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterParallel_path_description(ctx: Verilog2001Parser.Parallel_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parallel_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitParallel_path_description(ctx: Verilog2001Parser.Parallel_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#full_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterFull_path_description(ctx: Verilog2001Parser.Full_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#full_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitFull_path_description(ctx: Verilog2001Parser.Full_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_path_inputs(ctx: Verilog2001Parser.List_of_path_inputsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_path_inputs}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_path_inputs(ctx: Verilog2001Parser.List_of_path_inputsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_path_outputs(ctx: Verilog2001Parser.List_of_path_outputsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_path_outputs}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_path_outputs(ctx: Verilog2001Parser.List_of_path_outputsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_input_terminal_descriptor(ctx: Verilog2001Parser.Specify_input_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_input_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_input_terminal_descriptor(ctx: Verilog2001Parser.Specify_input_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecify_output_terminal_descriptor(ctx: Verilog2001Parser.Specify_output_terminal_descriptorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specify_output_terminal_descriptor}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecify_output_terminal_descriptor(ctx: Verilog2001Parser.Specify_output_terminal_descriptorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_identifier(ctx: Verilog2001Parser.Input_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_identifier(ctx: Verilog2001Parser.Input_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_identifier(ctx: Verilog2001Parser.Output_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_identifier(ctx: Verilog2001Parser.Output_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#path_delay_value}.
     * @param ctx the parse tree
     */
    suspend fun enterPath_delay_value(ctx: Verilog2001Parser.Path_delay_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#path_delay_value}.
     * @param ctx the parse tree
     */
    suspend fun exitPath_delay_value(ctx: Verilog2001Parser.Path_delay_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    suspend fun enterList_of_path_delay_expressions(ctx: Verilog2001Parser.List_of_path_delay_expressionsContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#list_of_path_delay_expressions}.
     * @param ctx the parse tree
     */
    suspend fun exitList_of_path_delay_expressions(ctx: Verilog2001Parser.List_of_path_delay_expressionsContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT_path_delay_expression(ctx: Verilog2001Parser.T_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT_path_delay_expression(ctx: Verilog2001Parser.T_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTrise_path_delay_expression(ctx: Verilog2001Parser.Trise_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#trise_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTrise_path_delay_expression(ctx: Verilog2001Parser.Trise_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTfall_path_delay_expression(ctx: Verilog2001Parser.Tfall_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tfall_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTfall_path_delay_expression(ctx: Verilog2001Parser.Tfall_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTz_path_delay_expression(ctx: Verilog2001Parser.Tz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTz_path_delay_expression(ctx: Verilog2001Parser.Tz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT01_path_delay_expression(ctx: Verilog2001Parser.T01_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t01_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT01_path_delay_expression(ctx: Verilog2001Parser.T01_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT10_path_delay_expression(ctx: Verilog2001Parser.T10_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t10_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT10_path_delay_expression(ctx: Verilog2001Parser.T10_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT0z_path_delay_expression(ctx: Verilog2001Parser.T0z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t0z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT0z_path_delay_expression(ctx: Verilog2001Parser.T0z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTz1_path_delay_expression(ctx: Verilog2001Parser.Tz1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tz1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTz1_path_delay_expression(ctx: Verilog2001Parser.Tz1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT1z_path_delay_expression(ctx: Verilog2001Parser.T1z_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t1z_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT1z_path_delay_expression(ctx: Verilog2001Parser.T1z_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTz0_path_delay_expression(ctx: Verilog2001Parser.Tz0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tz0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTz0_path_delay_expression(ctx: Verilog2001Parser.Tz0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT0x_path_delay_expression(ctx: Verilog2001Parser.T0x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t0x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT0x_path_delay_expression(ctx: Verilog2001Parser.T0x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTx1_path_delay_expression(ctx: Verilog2001Parser.Tx1_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tx1_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTx1_path_delay_expression(ctx: Verilog2001Parser.Tx1_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterT1x_path_delay_expression(ctx: Verilog2001Parser.T1x_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#t1x_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitT1x_path_delay_expression(ctx: Verilog2001Parser.T1x_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTx0_path_delay_expression(ctx: Verilog2001Parser.Tx0_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tx0_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTx0_path_delay_expression(ctx: Verilog2001Parser.Tx0_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTxz_path_delay_expression(ctx: Verilog2001Parser.Txz_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#txz_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTxz_path_delay_expression(ctx: Verilog2001Parser.Txz_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterTzx_path_delay_expression(ctx: Verilog2001Parser.Tzx_path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#tzx_path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitTzx_path_delay_expression(ctx: Verilog2001Parser.Tzx_path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterPath_delay_expression(ctx: Verilog2001Parser.Path_delay_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#path_delay_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitPath_delay_expression(ctx: Verilog2001Parser.Path_delay_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_sensitive_path_declaration(ctx: Verilog2001Parser.Edge_sensitive_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#edge_sensitive_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_sensitive_path_declaration(ctx: Verilog2001Parser.Edge_sensitive_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterParallel_edge_sensitive_path_description(ctx: Verilog2001Parser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parallel_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitParallel_edge_sensitive_path_description(ctx: Verilog2001Parser.Parallel_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun enterFull_edge_sensitive_path_description(ctx: Verilog2001Parser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#full_edge_sensitive_path_description}.
     * @param ctx the parse tree
     */
    suspend fun exitFull_edge_sensitive_path_description(ctx: Verilog2001Parser.Full_edge_sensitive_path_descriptionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#data_source_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterData_source_expression(ctx: Verilog2001Parser.Data_source_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#data_source_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitData_source_expression(ctx: Verilog2001Parser.Data_source_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#edge_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEdge_identifier(ctx: Verilog2001Parser.Edge_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#edge_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEdge_identifier(ctx: Verilog2001Parser.Edge_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun enterState_dependent_path_declaration(ctx: Verilog2001Parser.State_dependent_path_declarationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#state_dependent_path_declaration}.
     * @param ctx the parse tree
     */
    suspend fun exitState_dependent_path_declaration(ctx: Verilog2001Parser.State_dependent_path_declarationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#polarity_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterPolarity_operator(ctx: Verilog2001Parser.Polarity_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#polarity_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitPolarity_operator(ctx: Verilog2001Parser.Polarity_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#checktime_condition}.
     * @param ctx the parse tree
     */
    suspend fun enterChecktime_condition(ctx: Verilog2001Parser.Checktime_conditionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#checktime_condition}.
     * @param ctx the parse tree
     */
    suspend fun exitChecktime_condition(ctx: Verilog2001Parser.Checktime_conditionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delayed_data}.
     * @param ctx the parse tree
     */
    suspend fun enterDelayed_data(ctx: Verilog2001Parser.Delayed_dataContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delayed_data}.
     * @param ctx the parse tree
     */
    suspend fun exitDelayed_data(ctx: Verilog2001Parser.Delayed_dataContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#delayed_reference}.
     * @param ctx the parse tree
     */
    suspend fun enterDelayed_reference(ctx: Verilog2001Parser.Delayed_referenceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#delayed_reference}.
     * @param ctx the parse tree
     */
    suspend fun exitDelayed_reference(ctx: Verilog2001Parser.Delayed_referenceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#end_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun enterEnd_edge_offset(ctx: Verilog2001Parser.End_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#end_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun exitEnd_edge_offset(ctx: Verilog2001Parser.End_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_based_flag}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_based_flag(ctx: Verilog2001Parser.Event_based_flagContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_based_flag}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_based_flag(ctx: Verilog2001Parser.Event_based_flagContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#notify_reg}.
     * @param ctx the parse tree
     */
    suspend fun enterNotify_reg(ctx: Verilog2001Parser.Notify_regContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#notify_reg}.
     * @param ctx the parse tree
     */
    suspend fun exitNotify_reg(ctx: Verilog2001Parser.Notify_regContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#remain_active_flag}.
     * @param ctx the parse tree
     */
    suspend fun enterRemain_active_flag(ctx: Verilog2001Parser.Remain_active_flagContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#remain_active_flag}.
     * @param ctx the parse tree
     */
    suspend fun exitRemain_active_flag(ctx: Verilog2001Parser.Remain_active_flagContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#stamptime_condition}.
     * @param ctx the parse tree
     */
    suspend fun enterStamptime_condition(ctx: Verilog2001Parser.Stamptime_conditionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#stamptime_condition}.
     * @param ctx the parse tree
     */
    suspend fun exitStamptime_condition(ctx: Verilog2001Parser.Stamptime_conditionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#start_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun enterStart_edge_offset(ctx: Verilog2001Parser.Start_edge_offsetContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#start_edge_offset}.
     * @param ctx the parse tree
     */
    suspend fun exitStart_edge_offset(ctx: Verilog2001Parser.Start_edge_offsetContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#threshold}.
     * @param ctx the parse tree
     */
    suspend fun enterThreshold(ctx: Verilog2001Parser.ThresholdContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#threshold}.
     * @param ctx the parse tree
     */
    suspend fun exitThreshold(ctx: Verilog2001Parser.ThresholdContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#timing_check_limit}.
     * @param ctx the parse tree
     */
    suspend fun enterTiming_check_limit(ctx: Verilog2001Parser.Timing_check_limitContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#timing_check_limit}.
     * @param ctx the parse tree
     */
    suspend fun exitTiming_check_limit(ctx: Verilog2001Parser.Timing_check_limitContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterConcatenation(ctx: Verilog2001Parser.ConcatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitConcatenation(ctx: Verilog2001Parser.ConcatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_concatenation(ctx: Verilog2001Parser.Constant_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_concatenation(ctx: Verilog2001Parser.Constant_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_multiple_concatenation(ctx: Verilog2001Parser.Constant_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_multiple_concatenation(ctx: Verilog2001Parser.Constant_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_concatenation(ctx: Verilog2001Parser.Module_path_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_concatenation(ctx: Verilog2001Parser.Module_path_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_multiple_concatenation(ctx: Verilog2001Parser.Module_path_multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_multiple_concatenation(ctx: Verilog2001Parser.Module_path_multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterMultiple_concatenation(ctx: Verilog2001Parser.Multiple_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#multiple_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitMultiple_concatenation(ctx: Verilog2001Parser.Multiple_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_concatenation(ctx: Verilog2001Parser.Net_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_concatenation(ctx: Verilog2001Parser.Net_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_concatenation_value}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_concatenation_value(ctx: Verilog2001Parser.Net_concatenation_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_concatenation_value}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_concatenation_value(ctx: Verilog2001Parser.Net_concatenation_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_concatenation(ctx: Verilog2001Parser.Variable_concatenationContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_concatenation}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_concatenation(ctx: Verilog2001Parser.Variable_concatenationContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_concatenation_value}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_concatenation_value(ctx: Verilog2001Parser.Variable_concatenation_valueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_concatenation_value}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_concatenation_value(ctx: Verilog2001Parser.Variable_concatenation_valueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_function_call(ctx: Verilog2001Parser.Constant_function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_function_call(ctx: Verilog2001Parser.Constant_function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_call(ctx: Verilog2001Parser.Function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_call(ctx: Verilog2001Parser.Function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_function_call(ctx: Verilog2001Parser.System_function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_function_call(ctx: Verilog2001Parser.System_function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_function_call}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_function_call(ctx: Verilog2001Parser.Genvar_function_callContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_function_call}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_function_call(ctx: Verilog2001Parser.Genvar_function_callContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#base_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterBase_expression(ctx: Verilog2001Parser.Base_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#base_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitBase_expression(ctx: Verilog2001Parser.Base_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_base_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_base_expression(ctx: Verilog2001Parser.Constant_base_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_base_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_base_expression(ctx: Verilog2001Parser.Constant_base_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_expression(ctx: Verilog2001Parser.Constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_expression(ctx: Verilog2001Parser.Constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_mintypmax_expression(ctx: Verilog2001Parser.Constant_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_mintypmax_expression(ctx: Verilog2001Parser.Constant_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_range_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_range_expression(ctx: Verilog2001Parser.Constant_range_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_range_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_range_expression(ctx: Verilog2001Parser.Constant_range_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterDimension_constant_expression(ctx: Verilog2001Parser.Dimension_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#dimension_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitDimension_constant_expression(ctx: Verilog2001Parser.Dimension_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#expression}.
     * @param ctx the parse tree
     */
    suspend fun enterExpression(ctx: Verilog2001Parser.ExpressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#expression}.
     * @param ctx the parse tree
     */
    suspend fun exitExpression(ctx: Verilog2001Parser.ExpressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#term}.
     * @param ctx the parse tree
     */
    suspend fun enterTerm(ctx: Verilog2001Parser.TermContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#term}.
     * @param ctx the parse tree
     */
    suspend fun exitTerm(ctx: Verilog2001Parser.TermContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterLsb_constant_expression(ctx: Verilog2001Parser.Lsb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#lsb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitLsb_constant_expression(ctx: Verilog2001Parser.Lsb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterMintypmax_expression(ctx: Verilog2001Parser.Mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitMintypmax_expression(ctx: Verilog2001Parser.Mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_conditional_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_conditional_expression(ctx: Verilog2001Parser.Module_path_conditional_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_conditional_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_conditional_expression(ctx: Verilog2001Parser.Module_path_conditional_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_expression(ctx: Verilog2001Parser.Module_path_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_expression(ctx: Verilog2001Parser.Module_path_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_mintypmax_expression(ctx: Verilog2001Parser.Module_path_mintypmax_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_mintypmax_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_mintypmax_expression(ctx: Verilog2001Parser.Module_path_mintypmax_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterMsb_constant_expression(ctx: Verilog2001Parser.Msb_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#msb_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitMsb_constant_expression(ctx: Verilog2001Parser.Msb_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#range_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterRange_expression(ctx: Verilog2001Parser.Range_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#range_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitRange_expression(ctx: Verilog2001Parser.Range_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#width_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun enterWidth_constant_expression(ctx: Verilog2001Parser.Width_constant_expressionContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#width_constant_expression}.
     * @param ctx the parse tree
     */
    suspend fun exitWidth_constant_expression(ctx: Verilog2001Parser.Width_constant_expressionContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#constant_primary}.
     * @param ctx the parse tree
     */
    suspend fun enterConstant_primary(ctx: Verilog2001Parser.Constant_primaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#constant_primary}.
     * @param ctx the parse tree
     */
    suspend fun exitConstant_primary(ctx: Verilog2001Parser.Constant_primaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_path_primary}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_path_primary(ctx: Verilog2001Parser.Module_path_primaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_path_primary}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_path_primary(ctx: Verilog2001Parser.Module_path_primaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#primary}.
     * @param ctx the parse tree
     */
    suspend fun enterPrimary(ctx: Verilog2001Parser.PrimaryContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#primary}.
     * @param ctx the parse tree
     */
    suspend fun exitPrimary(ctx: Verilog2001Parser.PrimaryContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_lvalue(ctx: Verilog2001Parser.Net_lvalueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_lvalue(ctx: Verilog2001Parser.Net_lvalueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_lvalue(ctx: Verilog2001Parser.Variable_lvalueContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_lvalue}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_lvalue(ctx: Verilog2001Parser.Variable_lvalueContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#unary_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterUnary_operator(ctx: Verilog2001Parser.Unary_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#unary_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitUnary_operator(ctx: Verilog2001Parser.Unary_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#binary_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterBinary_operator(ctx: Verilog2001Parser.Binary_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#binary_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitBinary_operator(ctx: Verilog2001Parser.Binary_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterUnary_module_path_operator(ctx: Verilog2001Parser.Unary_module_path_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#unary_module_path_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitUnary_module_path_operator(ctx: Verilog2001Parser.Unary_module_path_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#binary_module_path_operator}.
     * @param ctx the parse tree
     */
    suspend fun enterBinary_module_path_operator(ctx: Verilog2001Parser.Binary_module_path_operatorContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#binary_module_path_operator}.
     * @param ctx the parse tree
     */
    suspend fun exitBinary_module_path_operator(ctx: Verilog2001Parser.Binary_module_path_operatorContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#number}.
     * @param ctx the parse tree
     */
    suspend fun enterNumber(ctx: Verilog2001Parser.NumberContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#number}.
     * @param ctx the parse tree
     */
    suspend fun exitNumber(ctx: Verilog2001Parser.NumberContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#attribute_instance}.
     * @param ctx the parse tree
     */
    suspend fun enterAttribute_instance(ctx: Verilog2001Parser.Attribute_instanceContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#attribute_instance}.
     * @param ctx the parse tree
     */
    suspend fun exitAttribute_instance(ctx: Verilog2001Parser.Attribute_instanceContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#attr_spec}.
     * @param ctx the parse tree
     */
    suspend fun enterAttr_spec(ctx: Verilog2001Parser.Attr_specContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#attr_spec}.
     * @param ctx the parse tree
     */
    suspend fun exitAttr_spec(ctx: Verilog2001Parser.Attr_specContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#attr_name}.
     * @param ctx the parse tree
     */
    suspend fun enterAttr_name(ctx: Verilog2001Parser.Attr_nameContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#attr_name}.
     * @param ctx the parse tree
     */
    suspend fun exitAttr_name(ctx: Verilog2001Parser.Attr_nameContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#arrayed_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterArrayed_identifier(ctx: Verilog2001Parser.Arrayed_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#arrayed_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitArrayed_identifier(ctx: Verilog2001Parser.Arrayed_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterBlock_identifier(ctx: Verilog2001Parser.Block_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitBlock_identifier(ctx: Verilog2001Parser.Block_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#cell_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterCell_identifier(ctx: Verilog2001Parser.Cell_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#cell_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitCell_identifier(ctx: Verilog2001Parser.Cell_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#config_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterConfig_identifier(ctx: Verilog2001Parser.Config_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#config_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitConfig_identifier(ctx: Verilog2001Parser.Config_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#escaped_arrayed_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEscaped_arrayed_identifier(ctx: Verilog2001Parser.Escaped_arrayed_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#escaped_arrayed_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEscaped_arrayed_identifier(ctx: Verilog2001Parser.Escaped_arrayed_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEscaped_hierarchical_identifier(ctx: Verilog2001Parser.Escaped_hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEscaped_hierarchical_identifier(ctx: Verilog2001Parser.Escaped_hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#event_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterEvent_identifier(ctx: Verilog2001Parser.Event_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#event_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitEvent_identifier(ctx: Verilog2001Parser.Event_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterFunction_identifier(ctx: Verilog2001Parser.Function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitFunction_identifier(ctx: Verilog2001Parser.Function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGate_instance_identifier(ctx: Verilog2001Parser.Gate_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#gate_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGate_instance_identifier(ctx: Verilog2001Parser.Gate_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGenerate_block_identifier(ctx: Verilog2001Parser.Generate_block_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#generate_block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGenerate_block_identifier(ctx: Verilog2001Parser.Generate_block_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_function_identifier(ctx: Verilog2001Parser.Genvar_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_function_identifier(ctx: Verilog2001Parser.Genvar_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#genvar_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterGenvar_identifier(ctx: Verilog2001Parser.Genvar_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#genvar_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitGenvar_identifier(ctx: Verilog2001Parser.Genvar_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_block_identifier(ctx: Verilog2001Parser.Hierarchical_block_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_block_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_block_identifier(ctx: Verilog2001Parser.Hierarchical_block_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_event_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_event_identifier(ctx: Verilog2001Parser.Hierarchical_event_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_event_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_event_identifier(ctx: Verilog2001Parser.Hierarchical_event_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_function_identifier(ctx: Verilog2001Parser.Hierarchical_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_function_identifier(ctx: Verilog2001Parser.Hierarchical_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_identifier(ctx: Verilog2001Parser.Hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_identifier(ctx: Verilog2001Parser.Hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_net_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_net_identifier(ctx: Verilog2001Parser.Hierarchical_net_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_net_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_net_identifier(ctx: Verilog2001Parser.Hierarchical_net_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_variable_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_variable_identifier(ctx: Verilog2001Parser.Hierarchical_variable_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_variable_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_variable_identifier(ctx: Verilog2001Parser.Hierarchical_variable_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#hierarchical_task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterHierarchical_task_identifier(ctx: Verilog2001Parser.Hierarchical_task_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#hierarchical_task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitHierarchical_task_identifier(ctx: Verilog2001Parser.Hierarchical_task_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterIdentifier(ctx: Verilog2001Parser.IdentifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitIdentifier(ctx: Verilog2001Parser.IdentifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#inout_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInout_port_identifier(ctx: Verilog2001Parser.Inout_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#inout_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInout_port_identifier(ctx: Verilog2001Parser.Inout_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#input_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInput_port_identifier(ctx: Verilog2001Parser.Input_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#input_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInput_port_identifier(ctx: Verilog2001Parser.Input_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterInstance_identifier(ctx: Verilog2001Parser.Instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitInstance_identifier(ctx: Verilog2001Parser.Instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#library_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterLibrary_identifier(ctx: Verilog2001Parser.Library_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#library_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitLibrary_identifier(ctx: Verilog2001Parser.Library_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#memory_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterMemory_identifier(ctx: Verilog2001Parser.Memory_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#memory_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitMemory_identifier(ctx: Verilog2001Parser.Memory_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_identifier(ctx: Verilog2001Parser.Module_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_identifier(ctx: Verilog2001Parser.Module_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterModule_instance_identifier(ctx: Verilog2001Parser.Module_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#module_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitModule_instance_identifier(ctx: Verilog2001Parser.Module_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#net_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterNet_identifier(ctx: Verilog2001Parser.Net_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#net_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitNet_identifier(ctx: Verilog2001Parser.Net_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#output_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterOutput_port_identifier(ctx: Verilog2001Parser.Output_port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#output_port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitOutput_port_identifier(ctx: Verilog2001Parser.Output_port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#parameter_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterParameter_identifier(ctx: Verilog2001Parser.Parameter_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#parameter_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitParameter_identifier(ctx: Verilog2001Parser.Parameter_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterPort_identifier(ctx: Verilog2001Parser.Port_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#port_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitPort_identifier(ctx: Verilog2001Parser.Port_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#real_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterReal_identifier(ctx: Verilog2001Parser.Real_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#real_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitReal_identifier(ctx: Verilog2001Parser.Real_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_arrayed_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSimple_arrayed_identifier(ctx: Verilog2001Parser.Simple_arrayed_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_arrayed_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSimple_arrayed_identifier(ctx: Verilog2001Parser.Simple_arrayed_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSimple_hierarchical_identifier(ctx: Verilog2001Parser.Simple_hierarchical_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSimple_hierarchical_identifier(ctx: Verilog2001Parser.Simple_hierarchical_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#specparam_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSpecparam_identifier(ctx: Verilog2001Parser.Specparam_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#specparam_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSpecparam_identifier(ctx: Verilog2001Parser.Specparam_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_function_identifier(ctx: Verilog2001Parser.System_function_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_function_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_function_identifier(ctx: Verilog2001Parser.System_function_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#system_task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterSystem_task_identifier(ctx: Verilog2001Parser.System_task_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#system_task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitSystem_task_identifier(ctx: Verilog2001Parser.System_task_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterTask_identifier(ctx: Verilog2001Parser.Task_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#task_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitTask_identifier(ctx: Verilog2001Parser.Task_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#terminal_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterTerminal_identifier(ctx: Verilog2001Parser.Terminal_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#terminal_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitTerminal_identifier(ctx: Verilog2001Parser.Terminal_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#text_macro_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterText_macro_identifier(ctx: Verilog2001Parser.Text_macro_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#text_macro_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitText_macro_identifier(ctx: Verilog2001Parser.Text_macro_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterTopmodule_identifier(ctx: Verilog2001Parser.Topmodule_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#topmodule_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitTopmodule_identifier(ctx: Verilog2001Parser.Topmodule_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#udp_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_identifier(ctx: Verilog2001Parser.Udp_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#udp_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_identifier(ctx: Verilog2001Parser.Udp_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterUdp_instance_identifier(ctx: Verilog2001Parser.Udp_instance_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#udp_instance_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitUdp_instance_identifier(ctx: Verilog2001Parser.Udp_instance_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#variable_identifier}.
     * @param ctx the parse tree
     */
    suspend fun enterVariable_identifier(ctx: Verilog2001Parser.Variable_identifierContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#variable_identifier}.
     * @param ctx the parse tree
     */
    suspend fun exitVariable_identifier(ctx: Verilog2001Parser.Variable_identifierContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_branch}.
     * @param ctx the parse tree
     */
    suspend fun enterSimple_hierarchical_branch(ctx: Verilog2001Parser.Simple_hierarchical_branchContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#simple_hierarchical_branch}.
     * @param ctx the parse tree
     */
    suspend fun exitSimple_hierarchical_branch(ctx: Verilog2001Parser.Simple_hierarchical_branchContext)

    /**
     * Enter a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_branch}.
     * @param ctx the parse tree
     */
    suspend fun enterEscaped_hierarchical_branch(ctx: Verilog2001Parser.Escaped_hierarchical_branchContext)

    /**
     * Exit a parse tree produced by {@link Verilog2001Parser#escaped_hierarchical_branch}.
     * @param ctx the parse tree
     */
    suspend fun exitEscaped_hierarchical_branch(ctx: Verilog2001Parser.Escaped_hierarchical_branchContext)
}