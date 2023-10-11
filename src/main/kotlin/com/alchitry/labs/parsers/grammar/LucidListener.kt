// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LucidParser}.
 */
interface LucidListener : ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 */
	fun enterSource(ctx: LucidParser.SourceContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 */
	fun exitSource(ctx: LucidParser.SourceContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 */
	fun enterGlobal(ctx: LucidParser.GlobalContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 */
	fun exitGlobal(ctx: LucidParser.GlobalContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#globalStat}.
	 * @param ctx the parse tree
	 */
	fun enterGlobalStat(ctx: LucidParser.GlobalStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#globalStat}.
	 * @param ctx the parse tree
	 */
	fun exitGlobalStat(ctx: LucidParser.GlobalStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 */
	fun enterModule(ctx: LucidParser.ModuleContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 */
	fun exitModule(ctx: LucidParser.ModuleContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#testBench}.
	 * @param ctx the parse tree
	 */
	fun enterTestBench(ctx: LucidParser.TestBenchContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#testBench}.
	 * @param ctx the parse tree
	 */
	fun exitTestBench(ctx: LucidParser.TestBenchContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramList}.
	 * @param ctx the parse tree
	 */
	fun enterParamList(ctx: LucidParser.ParamListContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramList}.
	 * @param ctx the parse tree
	 */
	fun exitParamList(ctx: LucidParser.ParamListContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#portList}.
	 * @param ctx the parse tree
	 */
	fun enterPortList(ctx: LucidParser.PortListContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#portList}.
	 * @param ctx the parse tree
	 */
	fun exitPortList(ctx: LucidParser.PortListContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramDec}.
	 * @param ctx the parse tree
	 */
	fun enterParamDec(ctx: LucidParser.ParamDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramDec}.
	 * @param ctx the parse tree
	 */
	fun exitParamDec(ctx: LucidParser.ParamDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramDefault}.
	 * @param ctx the parse tree
	 */
	fun enterParamDefault(ctx: LucidParser.ParamDefaultContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramDefault}.
	 * @param ctx the parse tree
	 */
	fun exitParamDefault(ctx: LucidParser.ParamDefaultContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramConstraint}.
	 * @param ctx the parse tree
	 */
	fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramConstraint}.
	 * @param ctx the parse tree
	 */
	fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 */
	fun enterPortDec(ctx: LucidParser.PortDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 */
	fun exitPortDec(ctx: LucidParser.PortDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 */
	fun enterPortDirection(ctx: LucidParser.PortDirectionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 */
	fun exitPortDirection(ctx: LucidParser.PortDirectionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 */
	fun enterSignalWidth(ctx: LucidParser.SignalWidthContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 */
	fun exitSignalWidth(ctx: LucidParser.SignalWidthContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 */
	fun enterArraySize(ctx: LucidParser.ArraySizeContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 */
	fun exitArraySize(ctx: LucidParser.ArraySizeContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 */
	fun enterStructType(ctx: LucidParser.StructTypeContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 */
	fun exitStructType(ctx: LucidParser.StructTypeContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 */
	fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 */
	fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 */
	fun enterStructConst(ctx: LucidParser.StructConstContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 */
	fun exitStructConst(ctx: LucidParser.StructConstContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 */
	fun enterModuleBody(ctx: LucidParser.ModuleBodyContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 */
	fun exitModuleBody(ctx: LucidParser.ModuleBodyContext)
	/**
	 * Enter a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatConst(ctx: LucidParser.StatConstContext)
	/**
	 * Exit a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatConst(ctx: LucidParser.StatConstContext)
	/**
	 * Enter a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatSig(ctx: LucidParser.StatSigContext)
	/**
	 * Exit a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatSig(ctx: LucidParser.StatSigContext)
	/**
	 * Enter a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatEnum(ctx: LucidParser.StatEnumContext)
	/**
	 * Exit a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatEnum(ctx: LucidParser.StatEnumContext)
	/**
	 * Enter a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatDFF(ctx: LucidParser.StatDFFContext)
	/**
	 * Exit a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatDFF(ctx: LucidParser.StatDFFContext)
	/**
	 * Enter a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext)
	/**
	 * Exit a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext)
	/**
	 * Enter a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatAssign(ctx: LucidParser.StatAssignContext)
	/**
	 * Exit a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatAssign(ctx: LucidParser.StatAssignContext)
	/**
	 * Enter a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatAlways(ctx: LucidParser.StatAlwaysContext)
	/**
	 * Exit a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatAlways(ctx: LucidParser.StatAlwaysContext)
	/**
	 * Enter a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatStruct(ctx: LucidParser.StatStructContext)
	/**
	 * Exit a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatStruct(ctx: LucidParser.StatStructContext)
	/**
	 * Enter a parse tree produced by the {@code StatTest}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatTest(ctx: LucidParser.StatTestContext)
	/**
	 * Exit a parse tree produced by the {@code StatTest}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatTest(ctx: LucidParser.StatTestContext)
	/**
	 * Enter a parse tree produced by the {@code StatFunction}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun enterStatFunction(ctx: LucidParser.StatFunctionContext)
	/**
	 * Exit a parse tree produced by the {@code StatFunction}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	fun exitStatFunction(ctx: LucidParser.StatFunctionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#constDec}.
	 * @param ctx the parse tree
	 */
	fun enterConstDec(ctx: LucidParser.ConstDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#constDec}.
	 * @param ctx the parse tree
	 */
	fun exitConstDec(ctx: LucidParser.ConstDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#assignBlock}.
	 * @param ctx the parse tree
	 */
	fun enterAssignBlock(ctx: LucidParser.AssignBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#assignBlock}.
	 * @param ctx the parse tree
	 */
	fun exitAssignBlock(ctx: LucidParser.AssignBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#sigCon}.
	 * @param ctx the parse tree
	 */
	fun enterSigCon(ctx: LucidParser.SigConContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#sigCon}.
	 * @param ctx the parse tree
	 */
	fun exitSigCon(ctx: LucidParser.SigConContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 */
	fun enterParamCon(ctx: LucidParser.ParamConContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 */
	fun exitParamCon(ctx: LucidParser.ParamConContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 */
	fun enterSigDec(ctx: LucidParser.SigDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 */
	fun exitSigDec(ctx: LucidParser.SigDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 */
	fun enterDffDec(ctx: LucidParser.DffDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 */
	fun exitDffDec(ctx: LucidParser.DffDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 */
	fun enterEnumDec(ctx: LucidParser.EnumDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 */
	fun exitEnumDec(ctx: LucidParser.EnumDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	fun enterModuleInst(ctx: LucidParser.ModuleInstContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	fun exitModuleInst(ctx: LucidParser.ModuleInstContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#instCons}.
	 * @param ctx the parse tree
	 */
	fun enterInstCons(ctx: LucidParser.InstConsContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#instCons}.
	 * @param ctx the parse tree
	 */
	fun exitInstCons(ctx: LucidParser.InstConsContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#conList}.
	 * @param ctx the parse tree
	 */
	fun enterConList(ctx: LucidParser.ConListContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#conList}.
	 * @param ctx the parse tree
	 */
	fun exitConList(ctx: LucidParser.ConListContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 */
	fun enterConnection(ctx: LucidParser.ConnectionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 */
	fun exitConnection(ctx: LucidParser.ConnectionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structMember}.
	 * @param ctx the parse tree
	 */
	fun enterStructMember(ctx: LucidParser.StructMemberContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structMember}.
	 * @param ctx the parse tree
	 */
	fun exitStructMember(ctx: LucidParser.StructMemberContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structDec}.
	 * @param ctx the parse tree
	 */
	fun enterStructDec(ctx: LucidParser.StructDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structDec}.
	 * @param ctx the parse tree
	 */
	fun exitStructDec(ctx: LucidParser.StructDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionArg}.
	 * @param ctx the parse tree
	 */
	fun enterFunctionArg(ctx: LucidParser.FunctionArgContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionArg}.
	 * @param ctx the parse tree
	 */
	fun exitFunctionArg(ctx: LucidParser.FunctionArgContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionBlock}.
	 * @param ctx the parse tree
	 */
	fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionBlock}.
	 * @param ctx the parse tree
	 */
	fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionBody}.
	 * @param ctx the parse tree
	 */
	fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionBody}.
	 * @param ctx the parse tree
	 */
	fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#testBlock}.
	 * @param ctx the parse tree
	 */
	fun enterTestBlock(ctx: LucidParser.TestBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#testBlock}.
	 * @param ctx the parse tree
	 */
	fun exitTestBlock(ctx: LucidParser.TestBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#alwaysBlock}.
	 * @param ctx the parse tree
	 */
	fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#alwaysBlock}.
	 * @param ctx the parse tree
	 */
	fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysAssign}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysAssign}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysFunction}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysFunction}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 */
	fun enterBlock(ctx: LucidParser.BlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 */
	fun exitBlock(ctx: LucidParser.BlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 */
	fun enterAssignStat(ctx: LucidParser.AssignStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 */
	fun exitAssignStat(ctx: LucidParser.AssignStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext)
	/**
	 * Enter a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)
	/**
	 * Exit a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)
	/**
	 * Enter a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)
	/**
	 * Exit a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 */
	fun enterBitSelection(ctx: LucidParser.BitSelectionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 */
	fun exitBitSelection(ctx: LucidParser.BitSelectionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 */
	fun enterSignal(ctx: LucidParser.SignalContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 */
	fun exitSignal(ctx: LucidParser.SignalContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseStat}.
	 * @param ctx the parse tree
	 */
	fun enterCaseStat(ctx: LucidParser.CaseStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseStat}.
	 * @param ctx the parse tree
	 */
	fun exitCaseStat(ctx: LucidParser.CaseStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseElem}.
	 * @param ctx the parse tree
	 */
	fun enterCaseElem(ctx: LucidParser.CaseElemContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseElem}.
	 * @param ctx the parse tree
	 */
	fun exitCaseElem(ctx: LucidParser.CaseElemContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseBlock}.
	 * @param ctx the parse tree
	 */
	fun enterCaseBlock(ctx: LucidParser.CaseBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseBlock}.
	 * @param ctx the parse tree
	 */
	fun exitCaseBlock(ctx: LucidParser.CaseBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#ifStat}.
	 * @param ctx the parse tree
	 */
	fun enterIfStat(ctx: LucidParser.IfStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#ifStat}.
	 * @param ctx the parse tree
	 */
	fun exitIfStat(ctx: LucidParser.IfStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#elseStat}.
	 * @param ctx the parse tree
	 */
	fun enterElseStat(ctx: LucidParser.ElseStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#elseStat}.
	 * @param ctx the parse tree
	 */
	fun exitElseStat(ctx: LucidParser.ElseStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#repeatStat}.
	 * @param ctx the parse tree
	 */
	fun enterRepeatStat(ctx: LucidParser.RepeatStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#repeatStat}.
	 * @param ctx the parse tree
	 */
	fun exitRepeatStat(ctx: LucidParser.RepeatStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#repeatBlock}.
	 * @param ctx the parse tree
	 */
	fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#repeatBlock}.
	 * @param ctx the parse tree
	 */
	fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 */
	fun enterFunction(ctx: LucidParser.FunctionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 */
	fun exitFunction(ctx: LucidParser.FunctionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionExpr}.
	 * @param ctx the parse tree
	 */
	fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionExpr}.
	 * @param ctx the parse tree
	 */
	fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 */
	fun enterNumber(ctx: LucidParser.NumberContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 */
	fun exitNumber(ctx: LucidParser.NumberContext)
	/**
	 * Enter a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprTernary(ctx: LucidParser.ExprTernaryContext)
	/**
	 * Exit a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprTernary(ctx: LucidParser.ExprTernaryContext)
	/**
	 * Enter a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprNum(ctx: LucidParser.ExprNumContext)
	/**
	 * Exit a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprNum(ctx: LucidParser.ExprNumContext)
	/**
	 * Enter a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprConcat(ctx: LucidParser.ExprConcatContext)
	/**
	 * Exit a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprConcat(ctx: LucidParser.ExprConcatContext)
	/**
	 * Enter a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprReduction(ctx: LucidParser.ExprReductionContext)
	/**
	 * Exit a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprReduction(ctx: LucidParser.ExprReductionContext)
	/**
	 * Enter a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprInvert(ctx: LucidParser.ExprInvertContext)
	/**
	 * Exit a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprInvert(ctx: LucidParser.ExprInvertContext)
	/**
	 * Enter a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprStruct(ctx: LucidParser.ExprStructContext)
	/**
	 * Exit a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprStruct(ctx: LucidParser.ExprStructContext)
	/**
	 * Enter a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprArray(ctx: LucidParser.ExprArrayContext)
	/**
	 * Exit a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprArray(ctx: LucidParser.ExprArrayContext)
	/**
	 * Enter a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprShift(ctx: LucidParser.ExprShiftContext)
	/**
	 * Exit a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprShift(ctx: LucidParser.ExprShiftContext)
	/**
	 * Enter a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext)
	/**
	 * Exit a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext)
	/**
	 * Enter a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprLogical(ctx: LucidParser.ExprLogicalContext)
	/**
	 * Exit a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprLogical(ctx: LucidParser.ExprLogicalContext)
	/**
	 * Enter a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprNegate(ctx: LucidParser.ExprNegateContext)
	/**
	 * Exit a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprNegate(ctx: LucidParser.ExprNegateContext)
	/**
	 * Enter a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprGroup(ctx: LucidParser.ExprGroupContext)
	/**
	 * Exit a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprGroup(ctx: LucidParser.ExprGroupContext)
	/**
	 * Enter a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext)
	/**
	 * Exit a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext)
	/**
	 * Enter a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprFunction(ctx: LucidParser.ExprFunctionContext)
	/**
	 * Exit a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprFunction(ctx: LucidParser.ExprFunctionContext)
	/**
	 * Enter a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprCompare(ctx: LucidParser.ExprCompareContext)
	/**
	 * Exit a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprCompare(ctx: LucidParser.ExprCompareContext)
	/**
	 * Enter a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprDup(ctx: LucidParser.ExprDupContext)
	/**
	 * Exit a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprDup(ctx: LucidParser.ExprDupContext)
	/**
	 * Enter a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext)
	/**
	 * Exit a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext)
	/**
	 * Enter a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun enterExprSignal(ctx: LucidParser.ExprSignalContext)
	/**
	 * Exit a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	fun exitExprSignal(ctx: LucidParser.ExprSignalContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 */
	fun enterName(ctx: LucidParser.NameContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 */
	fun exitName(ctx: LucidParser.NameContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 */
	fun enterSemi(ctx: LucidParser.SemiContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 */
	fun exitSemi(ctx: LucidParser.SemiContext)

	override fun asSuspend(): SuspendLucidListener = object : SuspendLucidListener {
		override suspend fun enterSource(ctx: LucidParser.SourceContext) = this@LucidListener.enterSource(ctx)
		override suspend fun exitSource(ctx: LucidParser.SourceContext) = this@LucidListener.exitSource(ctx)

		override suspend fun enterGlobal(ctx: LucidParser.GlobalContext) = this@LucidListener.enterGlobal(ctx)
		override suspend fun exitGlobal(ctx: LucidParser.GlobalContext) = this@LucidListener.exitGlobal(ctx)

		override suspend fun enterGlobalStat(ctx: LucidParser.GlobalStatContext) =
			this@LucidListener.enterGlobalStat(ctx)

		override suspend fun exitGlobalStat(ctx: LucidParser.GlobalStatContext) = this@LucidListener.exitGlobalStat(ctx)

		override suspend fun enterModule(ctx: LucidParser.ModuleContext) = this@LucidListener.enterModule(ctx)
		override suspend fun exitModule(ctx: LucidParser.ModuleContext) = this@LucidListener.exitModule(ctx)

		override suspend fun enterTestBench(ctx: LucidParser.TestBenchContext) = this@LucidListener.enterTestBench(ctx)
		override suspend fun exitTestBench(ctx: LucidParser.TestBenchContext) = this@LucidListener.exitTestBench(ctx)

		override suspend fun enterParamList(ctx: LucidParser.ParamListContext) = this@LucidListener.enterParamList(ctx)
		override suspend fun exitParamList(ctx: LucidParser.ParamListContext) = this@LucidListener.exitParamList(ctx)

		override suspend fun enterPortList(ctx: LucidParser.PortListContext) = this@LucidListener.enterPortList(ctx)
		override suspend fun exitPortList(ctx: LucidParser.PortListContext) = this@LucidListener.exitPortList(ctx)

		override suspend fun enterParamDec(ctx: LucidParser.ParamDecContext) = this@LucidListener.enterParamDec(ctx)
		override suspend fun exitParamDec(ctx: LucidParser.ParamDecContext) = this@LucidListener.exitParamDec(ctx)

		override suspend fun enterParamDefault(ctx: LucidParser.ParamDefaultContext) =
			this@LucidListener.enterParamDefault(ctx)

		override suspend fun exitParamDefault(ctx: LucidParser.ParamDefaultContext) =
			this@LucidListener.exitParamDefault(ctx)

		override suspend fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext) =
			this@LucidListener.enterParamConstraint(ctx)

		override suspend fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext) =
			this@LucidListener.exitParamConstraint(ctx)

		override suspend fun enterPortDec(ctx: LucidParser.PortDecContext) = this@LucidListener.enterPortDec(ctx)
		override suspend fun exitPortDec(ctx: LucidParser.PortDecContext) = this@LucidListener.exitPortDec(ctx)

		override suspend fun enterPortDirection(ctx: LucidParser.PortDirectionContext) =
			this@LucidListener.enterPortDirection(ctx)

		override suspend fun exitPortDirection(ctx: LucidParser.PortDirectionContext) =
			this@LucidListener.exitPortDirection(ctx)

		override suspend fun enterSignalWidth(ctx: LucidParser.SignalWidthContext) =
			this@LucidListener.enterSignalWidth(ctx)

		override suspend fun exitSignalWidth(ctx: LucidParser.SignalWidthContext) =
			this@LucidListener.exitSignalWidth(ctx)

		override suspend fun enterArraySize(ctx: LucidParser.ArraySizeContext) = this@LucidListener.enterArraySize(ctx)
		override suspend fun exitArraySize(ctx: LucidParser.ArraySizeContext) = this@LucidListener.exitArraySize(ctx)

		override suspend fun enterStructType(ctx: LucidParser.StructTypeContext) =
			this@LucidListener.enterStructType(ctx)

		override suspend fun exitStructType(ctx: LucidParser.StructTypeContext) = this@LucidListener.exitStructType(ctx)

		override suspend fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext) =
			this@LucidListener.enterStructMemberConst(ctx)

		override suspend fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext) =
			this@LucidListener.exitStructMemberConst(ctx)

		override suspend fun enterStructConst(ctx: LucidParser.StructConstContext) =
			this@LucidListener.enterStructConst(ctx)

		override suspend fun exitStructConst(ctx: LucidParser.StructConstContext) =
			this@LucidListener.exitStructConst(ctx)

		override suspend fun enterModuleBody(ctx: LucidParser.ModuleBodyContext) =
			this@LucidListener.enterModuleBody(ctx)

		override suspend fun exitModuleBody(ctx: LucidParser.ModuleBodyContext) = this@LucidListener.exitModuleBody(ctx)

		override suspend fun enterStatConst(ctx: LucidParser.StatConstContext) = this@LucidListener.enterStatConst(ctx)
		override suspend fun exitStatConst(ctx: LucidParser.StatConstContext) = this@LucidListener.exitStatConst(ctx)

		override suspend fun enterStatSig(ctx: LucidParser.StatSigContext) = this@LucidListener.enterStatSig(ctx)
		override suspend fun exitStatSig(ctx: LucidParser.StatSigContext) = this@LucidListener.exitStatSig(ctx)

		override suspend fun enterStatEnum(ctx: LucidParser.StatEnumContext) = this@LucidListener.enterStatEnum(ctx)
		override suspend fun exitStatEnum(ctx: LucidParser.StatEnumContext) = this@LucidListener.exitStatEnum(ctx)

		override suspend fun enterStatDFF(ctx: LucidParser.StatDFFContext) = this@LucidListener.enterStatDFF(ctx)
		override suspend fun exitStatDFF(ctx: LucidParser.StatDFFContext) = this@LucidListener.exitStatDFF(ctx)

		override suspend fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext) =
			this@LucidListener.enterStatModuleInst(ctx)

		override suspend fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext) =
			this@LucidListener.exitStatModuleInst(ctx)

		override suspend fun enterStatAssign(ctx: LucidParser.StatAssignContext) =
			this@LucidListener.enterStatAssign(ctx)

		override suspend fun exitStatAssign(ctx: LucidParser.StatAssignContext) = this@LucidListener.exitStatAssign(ctx)

		override suspend fun enterStatAlways(ctx: LucidParser.StatAlwaysContext) =
			this@LucidListener.enterStatAlways(ctx)

		override suspend fun exitStatAlways(ctx: LucidParser.StatAlwaysContext) = this@LucidListener.exitStatAlways(ctx)

		override suspend fun enterStatStruct(ctx: LucidParser.StatStructContext) =
			this@LucidListener.enterStatStruct(ctx)

		override suspend fun exitStatStruct(ctx: LucidParser.StatStructContext) = this@LucidListener.exitStatStruct(ctx)

		override suspend fun enterStatTest(ctx: LucidParser.StatTestContext) = this@LucidListener.enterStatTest(ctx)
		override suspend fun exitStatTest(ctx: LucidParser.StatTestContext) = this@LucidListener.exitStatTest(ctx)

		override suspend fun enterStatFunction(ctx: LucidParser.StatFunctionContext) =
			this@LucidListener.enterStatFunction(ctx)

		override suspend fun exitStatFunction(ctx: LucidParser.StatFunctionContext) =
			this@LucidListener.exitStatFunction(ctx)

		override suspend fun enterConstDec(ctx: LucidParser.ConstDecContext) = this@LucidListener.enterConstDec(ctx)
		override suspend fun exitConstDec(ctx: LucidParser.ConstDecContext) = this@LucidListener.exitConstDec(ctx)

		override suspend fun enterAssignBlock(ctx: LucidParser.AssignBlockContext) =
			this@LucidListener.enterAssignBlock(ctx)

		override suspend fun exitAssignBlock(ctx: LucidParser.AssignBlockContext) =
			this@LucidListener.exitAssignBlock(ctx)

		override suspend fun enterSigCon(ctx: LucidParser.SigConContext) = this@LucidListener.enterSigCon(ctx)
		override suspend fun exitSigCon(ctx: LucidParser.SigConContext) = this@LucidListener.exitSigCon(ctx)

		override suspend fun enterParamCon(ctx: LucidParser.ParamConContext) = this@LucidListener.enterParamCon(ctx)
		override suspend fun exitParamCon(ctx: LucidParser.ParamConContext) = this@LucidListener.exitParamCon(ctx)

		override suspend fun enterSigDec(ctx: LucidParser.SigDecContext) = this@LucidListener.enterSigDec(ctx)
		override suspend fun exitSigDec(ctx: LucidParser.SigDecContext) = this@LucidListener.exitSigDec(ctx)

		override suspend fun enterDffDec(ctx: LucidParser.DffDecContext) = this@LucidListener.enterDffDec(ctx)
		override suspend fun exitDffDec(ctx: LucidParser.DffDecContext) = this@LucidListener.exitDffDec(ctx)

		override suspend fun enterEnumDec(ctx: LucidParser.EnumDecContext) = this@LucidListener.enterEnumDec(ctx)
		override suspend fun exitEnumDec(ctx: LucidParser.EnumDecContext) = this@LucidListener.exitEnumDec(ctx)

		override suspend fun enterModuleInst(ctx: LucidParser.ModuleInstContext) =
			this@LucidListener.enterModuleInst(ctx)

		override suspend fun exitModuleInst(ctx: LucidParser.ModuleInstContext) = this@LucidListener.exitModuleInst(ctx)

		override suspend fun enterInstCons(ctx: LucidParser.InstConsContext) = this@LucidListener.enterInstCons(ctx)
		override suspend fun exitInstCons(ctx: LucidParser.InstConsContext) = this@LucidListener.exitInstCons(ctx)

		override suspend fun enterConList(ctx: LucidParser.ConListContext) = this@LucidListener.enterConList(ctx)
		override suspend fun exitConList(ctx: LucidParser.ConListContext) = this@LucidListener.exitConList(ctx)

		override suspend fun enterConnection(ctx: LucidParser.ConnectionContext) =
			this@LucidListener.enterConnection(ctx)

		override suspend fun exitConnection(ctx: LucidParser.ConnectionContext) = this@LucidListener.exitConnection(ctx)

		override suspend fun enterStructMember(ctx: LucidParser.StructMemberContext) =
			this@LucidListener.enterStructMember(ctx)

		override suspend fun exitStructMember(ctx: LucidParser.StructMemberContext) =
			this@LucidListener.exitStructMember(ctx)

		override suspend fun enterStructDec(ctx: LucidParser.StructDecContext) = this@LucidListener.enterStructDec(ctx)
		override suspend fun exitStructDec(ctx: LucidParser.StructDecContext) = this@LucidListener.exitStructDec(ctx)

		override suspend fun enterFunctionArg(ctx: LucidParser.FunctionArgContext) =
			this@LucidListener.enterFunctionArg(ctx)

		override suspend fun exitFunctionArg(ctx: LucidParser.FunctionArgContext) =
			this@LucidListener.exitFunctionArg(ctx)

		override suspend fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext) =
			this@LucidListener.enterFunctionBlock(ctx)

		override suspend fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext) =
			this@LucidListener.exitFunctionBlock(ctx)

		override suspend fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext) =
			this@LucidListener.enterFunctionBody(ctx)

		override suspend fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext) =
			this@LucidListener.exitFunctionBody(ctx)

		override suspend fun enterTestBlock(ctx: LucidParser.TestBlockContext) = this@LucidListener.enterTestBlock(ctx)
		override suspend fun exitTestBlock(ctx: LucidParser.TestBlockContext) = this@LucidListener.exitTestBlock(ctx)

		override suspend fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) =
			this@LucidListener.enterAlwaysBlock(ctx)

		override suspend fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) =
			this@LucidListener.exitAlwaysBlock(ctx)

		override suspend fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) =
			this@LucidListener.enterAlwaysAssign(ctx)

		override suspend fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) =
			this@LucidListener.exitAlwaysAssign(ctx)

		override suspend fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext) =
			this@LucidListener.enterAlwaysCase(ctx)

		override suspend fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext) = this@LucidListener.exitAlwaysCase(ctx)

		override suspend fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext) = this@LucidListener.enterAlwaysIf(ctx)
		override suspend fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext) = this@LucidListener.exitAlwaysIf(ctx)

		override suspend fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) =
			this@LucidListener.enterAlwaysRepeat(ctx)

		override suspend fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) =
			this@LucidListener.exitAlwaysRepeat(ctx)

		override suspend fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) =
			this@LucidListener.enterAlwaysFunction(ctx)

		override suspend fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) =
			this@LucidListener.exitAlwaysFunction(ctx)

		override suspend fun enterBlock(ctx: LucidParser.BlockContext) = this@LucidListener.enterBlock(ctx)
		override suspend fun exitBlock(ctx: LucidParser.BlockContext) = this@LucidListener.exitBlock(ctx)

		override suspend fun enterAssignStat(ctx: LucidParser.AssignStatContext) =
			this@LucidListener.enterAssignStat(ctx)

		override suspend fun exitAssignStat(ctx: LucidParser.AssignStatContext) = this@LucidListener.exitAssignStat(ctx)

		override suspend fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext) =
			this@LucidListener.enterArrayIndex(ctx)

		override suspend fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext) = this@LucidListener.exitArrayIndex(ctx)

		override suspend fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) =
			this@LucidListener.enterBitSelectorConst(ctx)

		override suspend fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) =
			this@LucidListener.exitBitSelectorConst(ctx)

		override suspend fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) =
			this@LucidListener.enterBitSelectorFixWidth(ctx)

		override suspend fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) =
			this@LucidListener.exitBitSelectorFixWidth(ctx)

		override suspend fun enterBitSelection(ctx: LucidParser.BitSelectionContext) =
			this@LucidListener.enterBitSelection(ctx)

		override suspend fun exitBitSelection(ctx: LucidParser.BitSelectionContext) =
			this@LucidListener.exitBitSelection(ctx)

		override suspend fun enterSignal(ctx: LucidParser.SignalContext) = this@LucidListener.enterSignal(ctx)
		override suspend fun exitSignal(ctx: LucidParser.SignalContext) = this@LucidListener.exitSignal(ctx)

		override suspend fun enterCaseStat(ctx: LucidParser.CaseStatContext) = this@LucidListener.enterCaseStat(ctx)
		override suspend fun exitCaseStat(ctx: LucidParser.CaseStatContext) = this@LucidListener.exitCaseStat(ctx)

		override suspend fun enterCaseElem(ctx: LucidParser.CaseElemContext) = this@LucidListener.enterCaseElem(ctx)
		override suspend fun exitCaseElem(ctx: LucidParser.CaseElemContext) = this@LucidListener.exitCaseElem(ctx)

		override suspend fun enterCaseBlock(ctx: LucidParser.CaseBlockContext) = this@LucidListener.enterCaseBlock(ctx)
		override suspend fun exitCaseBlock(ctx: LucidParser.CaseBlockContext) = this@LucidListener.exitCaseBlock(ctx)

		override suspend fun enterIfStat(ctx: LucidParser.IfStatContext) = this@LucidListener.enterIfStat(ctx)
		override suspend fun exitIfStat(ctx: LucidParser.IfStatContext) = this@LucidListener.exitIfStat(ctx)

		override suspend fun enterElseStat(ctx: LucidParser.ElseStatContext) = this@LucidListener.enterElseStat(ctx)
		override suspend fun exitElseStat(ctx: LucidParser.ElseStatContext) = this@LucidListener.exitElseStat(ctx)

		override suspend fun enterRepeatStat(ctx: LucidParser.RepeatStatContext) =
			this@LucidListener.enterRepeatStat(ctx)

		override suspend fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) = this@LucidListener.exitRepeatStat(ctx)

		override suspend fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext) =
			this@LucidListener.enterRepeatBlock(ctx)

		override suspend fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext) =
			this@LucidListener.exitRepeatBlock(ctx)

		override suspend fun enterFunction(ctx: LucidParser.FunctionContext) = this@LucidListener.enterFunction(ctx)
		override suspend fun exitFunction(ctx: LucidParser.FunctionContext) = this@LucidListener.exitFunction(ctx)

		override suspend fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext) =
			this@LucidListener.enterFunctionExpr(ctx)

		override suspend fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext) =
			this@LucidListener.exitFunctionExpr(ctx)

		override suspend fun enterNumber(ctx: LucidParser.NumberContext) = this@LucidListener.enterNumber(ctx)
		override suspend fun exitNumber(ctx: LucidParser.NumberContext) = this@LucidListener.exitNumber(ctx)

		override suspend fun enterExprTernary(ctx: LucidParser.ExprTernaryContext) =
			this@LucidListener.enterExprTernary(ctx)

		override suspend fun exitExprTernary(ctx: LucidParser.ExprTernaryContext) =
			this@LucidListener.exitExprTernary(ctx)

		override suspend fun enterExprNum(ctx: LucidParser.ExprNumContext) = this@LucidListener.enterExprNum(ctx)
		override suspend fun exitExprNum(ctx: LucidParser.ExprNumContext) = this@LucidListener.exitExprNum(ctx)

		override suspend fun enterExprConcat(ctx: LucidParser.ExprConcatContext) =
			this@LucidListener.enterExprConcat(ctx)

		override suspend fun exitExprConcat(ctx: LucidParser.ExprConcatContext) = this@LucidListener.exitExprConcat(ctx)

		override suspend fun enterExprReduction(ctx: LucidParser.ExprReductionContext) =
			this@LucidListener.enterExprReduction(ctx)

		override suspend fun exitExprReduction(ctx: LucidParser.ExprReductionContext) =
			this@LucidListener.exitExprReduction(ctx)

		override suspend fun enterExprInvert(ctx: LucidParser.ExprInvertContext) =
			this@LucidListener.enterExprInvert(ctx)

		override suspend fun exitExprInvert(ctx: LucidParser.ExprInvertContext) = this@LucidListener.exitExprInvert(ctx)

		override suspend fun enterExprStruct(ctx: LucidParser.ExprStructContext) =
			this@LucidListener.enterExprStruct(ctx)

		override suspend fun exitExprStruct(ctx: LucidParser.ExprStructContext) = this@LucidListener.exitExprStruct(ctx)

		override suspend fun enterExprArray(ctx: LucidParser.ExprArrayContext) = this@LucidListener.enterExprArray(ctx)
		override suspend fun exitExprArray(ctx: LucidParser.ExprArrayContext) = this@LucidListener.exitExprArray(ctx)

		override suspend fun enterExprShift(ctx: LucidParser.ExprShiftContext) = this@LucidListener.enterExprShift(ctx)
		override suspend fun exitExprShift(ctx: LucidParser.ExprShiftContext) = this@LucidListener.exitExprShift(ctx)

		override suspend fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext) =
			this@LucidListener.enterExprAddSub(ctx)

		override suspend fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext) = this@LucidListener.exitExprAddSub(ctx)

		override suspend fun enterExprLogical(ctx: LucidParser.ExprLogicalContext) =
			this@LucidListener.enterExprLogical(ctx)

		override suspend fun exitExprLogical(ctx: LucidParser.ExprLogicalContext) =
			this@LucidListener.exitExprLogical(ctx)

		override suspend fun enterExprNegate(ctx: LucidParser.ExprNegateContext) =
			this@LucidListener.enterExprNegate(ctx)

		override suspend fun exitExprNegate(ctx: LucidParser.ExprNegateContext) = this@LucidListener.exitExprNegate(ctx)

		override suspend fun enterExprGroup(ctx: LucidParser.ExprGroupContext) = this@LucidListener.enterExprGroup(ctx)
		override suspend fun exitExprGroup(ctx: LucidParser.ExprGroupContext) = this@LucidListener.exitExprGroup(ctx)

		override suspend fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext) =
			this@LucidListener.enterExprBitwise(ctx)

		override suspend fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext) =
			this@LucidListener.exitExprBitwise(ctx)

		override suspend fun enterExprFunction(ctx: LucidParser.ExprFunctionContext) =
			this@LucidListener.enterExprFunction(ctx)

		override suspend fun exitExprFunction(ctx: LucidParser.ExprFunctionContext) =
			this@LucidListener.exitExprFunction(ctx)

		override suspend fun enterExprCompare(ctx: LucidParser.ExprCompareContext) =
			this@LucidListener.enterExprCompare(ctx)

		override suspend fun exitExprCompare(ctx: LucidParser.ExprCompareContext) =
			this@LucidListener.exitExprCompare(ctx)

		override suspend fun enterExprDup(ctx: LucidParser.ExprDupContext) = this@LucidListener.enterExprDup(ctx)
		override suspend fun exitExprDup(ctx: LucidParser.ExprDupContext) = this@LucidListener.exitExprDup(ctx)

		override suspend fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext) =
			this@LucidListener.enterExprMultDiv(ctx)

		override suspend fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext) =
			this@LucidListener.exitExprMultDiv(ctx)

		override suspend fun enterExprSignal(ctx: LucidParser.ExprSignalContext) =
			this@LucidListener.enterExprSignal(ctx)

		override suspend fun exitExprSignal(ctx: LucidParser.ExprSignalContext) = this@LucidListener.exitExprSignal(ctx)

		override suspend fun enterName(ctx: LucidParser.NameContext) = this@LucidListener.enterName(ctx)
		override suspend fun exitName(ctx: LucidParser.NameContext) = this@LucidListener.exitName(ctx)

		override suspend fun enterSemi(ctx: LucidParser.SemiContext) = this@LucidListener.enterSemi(ctx)
		override suspend fun exitSemi(ctx: LucidParser.SemiContext) = this@LucidListener.exitSemi(ctx)

		override suspend fun enterEveryRule(ctx: ParserRuleContext) = this@LucidListener.enterEveryRule(ctx)
		override suspend fun exitEveryRule(ctx: ParserRuleContext) = this@LucidListener.exitEveryRule(ctx)

		override suspend fun visitErrorNode(node: ErrorNode) = this@LucidListener.visitErrorNode(node)
		override suspend fun visitTerminal(node: TerminalNode) = this@LucidListener.visitTerminal(node)
	}
}

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LucidParser}.
 */
interface SuspendLucidListener : SuspendParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSource(ctx: LucidParser.SourceContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#source}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSource(ctx: LucidParser.SourceContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 */
	suspend fun enterGlobal(ctx: LucidParser.GlobalContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#global}.
	 * @param ctx the parse tree
	 */
	suspend fun exitGlobal(ctx: LucidParser.GlobalContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#globalStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterGlobalStat(ctx: LucidParser.GlobalStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#globalStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitGlobalStat(ctx: LucidParser.GlobalStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 */
	suspend fun enterModule(ctx: LucidParser.ModuleContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#module}.
	 * @param ctx the parse tree
	 */
	suspend fun exitModule(ctx: LucidParser.ModuleContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#testBench}.
	 * @param ctx the parse tree
	 */
	suspend fun enterTestBench(ctx: LucidParser.TestBenchContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#testBench}.
	 * @param ctx the parse tree
	 */
	suspend fun exitTestBench(ctx: LucidParser.TestBenchContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramList}.
	 * @param ctx the parse tree
	 */
	suspend fun enterParamList(ctx: LucidParser.ParamListContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramList}.
	 * @param ctx the parse tree
	 */
	suspend fun exitParamList(ctx: LucidParser.ParamListContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#portList}.
	 * @param ctx the parse tree
	 */
	suspend fun enterPortList(ctx: LucidParser.PortListContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#portList}.
	 * @param ctx the parse tree
	 */
	suspend fun exitPortList(ctx: LucidParser.PortListContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterParamDec(ctx: LucidParser.ParamDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitParamDec(ctx: LucidParser.ParamDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramDefault}.
	 * @param ctx the parse tree
	 */
	suspend fun enterParamDefault(ctx: LucidParser.ParamDefaultContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramDefault}.
	 * @param ctx the parse tree
	 */
	suspend fun exitParamDefault(ctx: LucidParser.ParamDefaultContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramConstraint}.
	 * @param ctx the parse tree
	 */
	suspend fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramConstraint}.
	 * @param ctx the parse tree
	 */
	suspend fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterPortDec(ctx: LucidParser.PortDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitPortDec(ctx: LucidParser.PortDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 */
	suspend fun enterPortDirection(ctx: LucidParser.PortDirectionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 */
	suspend fun exitPortDirection(ctx: LucidParser.PortDirectionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSignalWidth(ctx: LucidParser.SignalWidthContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSignalWidth(ctx: LucidParser.SignalWidthContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 */
	suspend fun enterArraySize(ctx: LucidParser.ArraySizeContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 */
	suspend fun exitArraySize(ctx: LucidParser.ArraySizeContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStructType(ctx: LucidParser.StructTypeContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStructType(ctx: LucidParser.StructTypeContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStructConst(ctx: LucidParser.StructConstContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStructConst(ctx: LucidParser.StructConstContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 */
	suspend fun enterModuleBody(ctx: LucidParser.ModuleBodyContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 */
	suspend fun exitModuleBody(ctx: LucidParser.ModuleBodyContext)
	/**
	 * Enter a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatConst(ctx: LucidParser.StatConstContext)
	/**
	 * Exit a parse tree produced by the {@code StatConst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatConst(ctx: LucidParser.StatConstContext)
	/**
	 * Enter a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatSig(ctx: LucidParser.StatSigContext)
	/**
	 * Exit a parse tree produced by the {@code StatSig}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatSig(ctx: LucidParser.StatSigContext)
	/**
	 * Enter a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatEnum(ctx: LucidParser.StatEnumContext)
	/**
	 * Exit a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatEnum(ctx: LucidParser.StatEnumContext)
	/**
	 * Enter a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatDFF(ctx: LucidParser.StatDFFContext)
	/**
	 * Exit a parse tree produced by the {@code StatDFF}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatDFF(ctx: LucidParser.StatDFFContext)
	/**
	 * Enter a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext)
	/**
	 * Exit a parse tree produced by the {@code StatModuleInst}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext)
	/**
	 * Enter a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatAssign(ctx: LucidParser.StatAssignContext)
	/**
	 * Exit a parse tree produced by the {@code StatAssign}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatAssign(ctx: LucidParser.StatAssignContext)
	/**
	 * Enter a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatAlways(ctx: LucidParser.StatAlwaysContext)
	/**
	 * Exit a parse tree produced by the {@code StatAlways}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatAlways(ctx: LucidParser.StatAlwaysContext)
	/**
	 * Enter a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatStruct(ctx: LucidParser.StatStructContext)
	/**
	 * Exit a parse tree produced by the {@code StatStruct}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatStruct(ctx: LucidParser.StatStructContext)
	/**
	 * Enter a parse tree produced by the {@code StatTest}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatTest(ctx: LucidParser.StatTestContext)
	/**
	 * Exit a parse tree produced by the {@code StatTest}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatTest(ctx: LucidParser.StatTestContext)
	/**
	 * Enter a parse tree produced by the {@code StatFunction}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStatFunction(ctx: LucidParser.StatFunctionContext)
	/**
	 * Exit a parse tree produced by the {@code StatFunction}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStatFunction(ctx: LucidParser.StatFunctionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#constDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterConstDec(ctx: LucidParser.ConstDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#constDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitConstDec(ctx: LucidParser.ConstDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#assignBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAssignBlock(ctx: LucidParser.AssignBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#assignBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAssignBlock(ctx: LucidParser.AssignBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#sigCon}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSigCon(ctx: LucidParser.SigConContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#sigCon}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSigCon(ctx: LucidParser.SigConContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 */
	suspend fun enterParamCon(ctx: LucidParser.ParamConContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 */
	suspend fun exitParamCon(ctx: LucidParser.ParamConContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSigDec(ctx: LucidParser.SigDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSigDec(ctx: LucidParser.SigDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterDffDec(ctx: LucidParser.DffDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitDffDec(ctx: LucidParser.DffDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterEnumDec(ctx: LucidParser.EnumDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitEnumDec(ctx: LucidParser.EnumDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	suspend fun enterModuleInst(ctx: LucidParser.ModuleInstContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	suspend fun exitModuleInst(ctx: LucidParser.ModuleInstContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#instCons}.
	 * @param ctx the parse tree
	 */
	suspend fun enterInstCons(ctx: LucidParser.InstConsContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#instCons}.
	 * @param ctx the parse tree
	 */
	suspend fun exitInstCons(ctx: LucidParser.InstConsContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#conList}.
	 * @param ctx the parse tree
	 */
	suspend fun enterConList(ctx: LucidParser.ConListContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#conList}.
	 * @param ctx the parse tree
	 */
	suspend fun exitConList(ctx: LucidParser.ConListContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 */
	suspend fun enterConnection(ctx: LucidParser.ConnectionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#connection}.
	 * @param ctx the parse tree
	 */
	suspend fun exitConnection(ctx: LucidParser.ConnectionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structMember}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStructMember(ctx: LucidParser.StructMemberContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structMember}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStructMember(ctx: LucidParser.StructMemberContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#structDec}.
	 * @param ctx the parse tree
	 */
	suspend fun enterStructDec(ctx: LucidParser.StructDecContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#structDec}.
	 * @param ctx the parse tree
	 */
	suspend fun exitStructDec(ctx: LucidParser.StructDecContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionArg}.
	 * @param ctx the parse tree
	 */
	suspend fun enterFunctionArg(ctx: LucidParser.FunctionArgContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionArg}.
	 * @param ctx the parse tree
	 */
	suspend fun exitFunctionArg(ctx: LucidParser.FunctionArgContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionBody}.
	 * @param ctx the parse tree
	 */
	suspend fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionBody}.
	 * @param ctx the parse tree
	 */
	suspend fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#testBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterTestBlock(ctx: LucidParser.TestBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#testBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitTestBlock(ctx: LucidParser.TestBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#alwaysBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#alwaysBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysAssign}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysAssign}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)
	/**
	 * Enter a parse tree produced by the {@code AlwaysFunction}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)
	/**
	 * Exit a parse tree produced by the {@code AlwaysFunction}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 */
	suspend fun enterBlock(ctx: LucidParser.BlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 */
	suspend fun exitBlock(ctx: LucidParser.BlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterAssignStat(ctx: LucidParser.AssignStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitAssignStat(ctx: LucidParser.AssignStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	suspend fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	suspend fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext)
	/**
	 * Enter a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	suspend fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)
	/**
	 * Exit a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	suspend fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)
	/**
	 * Enter a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	suspend fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)
	/**
	 * Exit a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	suspend fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 */
	suspend fun enterBitSelection(ctx: LucidParser.BitSelectionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 */
	suspend fun exitBitSelection(ctx: LucidParser.BitSelectionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSignal(ctx: LucidParser.SignalContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#signal}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSignal(ctx: LucidParser.SignalContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterCaseStat(ctx: LucidParser.CaseStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitCaseStat(ctx: LucidParser.CaseStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseElem}.
	 * @param ctx the parse tree
	 */
	suspend fun enterCaseElem(ctx: LucidParser.CaseElemContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseElem}.
	 * @param ctx the parse tree
	 */
	suspend fun exitCaseElem(ctx: LucidParser.CaseElemContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterCaseBlock(ctx: LucidParser.CaseBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitCaseBlock(ctx: LucidParser.CaseBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#ifStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterIfStat(ctx: LucidParser.IfStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#ifStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitIfStat(ctx: LucidParser.IfStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#elseStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterElseStat(ctx: LucidParser.ElseStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#elseStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitElseStat(ctx: LucidParser.ElseStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#repeatStat}.
	 * @param ctx the parse tree
	 */
	suspend fun enterRepeatStat(ctx: LucidParser.RepeatStatContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#repeatStat}.
	 * @param ctx the parse tree
	 */
	suspend fun exitRepeatStat(ctx: LucidParser.RepeatStatContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#repeatBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#repeatBlock}.
	 * @param ctx the parse tree
	 */
	suspend fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 */
	suspend fun enterFunction(ctx: LucidParser.FunctionContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#function}.
	 * @param ctx the parse tree
	 */
	suspend fun exitFunction(ctx: LucidParser.FunctionContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#functionExpr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#functionExpr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 */
	suspend fun enterNumber(ctx: LucidParser.NumberContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#number}.
	 * @param ctx the parse tree
	 */
	suspend fun exitNumber(ctx: LucidParser.NumberContext)
	/**
	 * Enter a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprTernary(ctx: LucidParser.ExprTernaryContext)
	/**
	 * Exit a parse tree produced by the {@code ExprTernary}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprTernary(ctx: LucidParser.ExprTernaryContext)
	/**
	 * Enter a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprNum(ctx: LucidParser.ExprNumContext)
	/**
	 * Exit a parse tree produced by the {@code ExprNum}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprNum(ctx: LucidParser.ExprNumContext)
	/**
	 * Enter a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprConcat(ctx: LucidParser.ExprConcatContext)
	/**
	 * Exit a parse tree produced by the {@code ExprConcat}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprConcat(ctx: LucidParser.ExprConcatContext)
	/**
	 * Enter a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprReduction(ctx: LucidParser.ExprReductionContext)
	/**
	 * Exit a parse tree produced by the {@code ExprReduction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprReduction(ctx: LucidParser.ExprReductionContext)
	/**
	 * Enter a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprInvert(ctx: LucidParser.ExprInvertContext)
	/**
	 * Exit a parse tree produced by the {@code ExprInvert}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprInvert(ctx: LucidParser.ExprInvertContext)
	/**
	 * Enter a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprStruct(ctx: LucidParser.ExprStructContext)
	/**
	 * Exit a parse tree produced by the {@code ExprStruct}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprStruct(ctx: LucidParser.ExprStructContext)
	/**
	 * Enter a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprArray(ctx: LucidParser.ExprArrayContext)
	/**
	 * Exit a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprArray(ctx: LucidParser.ExprArrayContext)
	/**
	 * Enter a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprShift(ctx: LucidParser.ExprShiftContext)
	/**
	 * Exit a parse tree produced by the {@code ExprShift}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprShift(ctx: LucidParser.ExprShiftContext)
	/**
	 * Enter a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext)
	/**
	 * Exit a parse tree produced by the {@code ExprAddSub}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext)
	/**
	 * Enter a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprLogical(ctx: LucidParser.ExprLogicalContext)
	/**
	 * Exit a parse tree produced by the {@code ExprLogical}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprLogical(ctx: LucidParser.ExprLogicalContext)
	/**
	 * Enter a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprNegate(ctx: LucidParser.ExprNegateContext)
	/**
	 * Exit a parse tree produced by the {@code ExprNegate}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprNegate(ctx: LucidParser.ExprNegateContext)
	/**
	 * Enter a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprGroup(ctx: LucidParser.ExprGroupContext)
	/**
	 * Exit a parse tree produced by the {@code ExprGroup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprGroup(ctx: LucidParser.ExprGroupContext)
	/**
	 * Enter a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext)
	/**
	 * Exit a parse tree produced by the {@code ExprBitwise}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext)
	/**
	 * Enter a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprFunction(ctx: LucidParser.ExprFunctionContext)
	/**
	 * Exit a parse tree produced by the {@code ExprFunction}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprFunction(ctx: LucidParser.ExprFunctionContext)
	/**
	 * Enter a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprCompare(ctx: LucidParser.ExprCompareContext)
	/**
	 * Exit a parse tree produced by the {@code ExprCompare}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprCompare(ctx: LucidParser.ExprCompareContext)
	/**
	 * Enter a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprDup(ctx: LucidParser.ExprDupContext)
	/**
	 * Exit a parse tree produced by the {@code ExprDup}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprDup(ctx: LucidParser.ExprDupContext)
	/**
	 * Enter a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext)
	/**
	 * Exit a parse tree produced by the {@code ExprMultDiv}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext)
	/**
	 * Enter a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun enterExprSignal(ctx: LucidParser.ExprSignalContext)
	/**
	 * Exit a parse tree produced by the {@code ExprSignal}
	 * labeled alternative in {@link LucidParser#expr}.
	 * @param ctx the parse tree
	 */
	suspend fun exitExprSignal(ctx: LucidParser.ExprSignalContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 */
	suspend fun enterName(ctx: LucidParser.NameContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#name}.
	 * @param ctx the parse tree
	 */
	suspend fun exitName(ctx: LucidParser.NameContext)
	/**
	 * Enter a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 */
	suspend fun enterSemi(ctx: LucidParser.SemiContext)
	/**
	 * Exit a parse tree produced by {@link LucidParser#semi}.
	 * @param ctx the parse tree
	 */
	suspend fun exitSemi(ctx: LucidParser.SemiContext)
}