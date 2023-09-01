// Generated from java-escape by ANTLR 4.13.0
package com.alchitry.labs.parsers.grammar

import org.antlr.v4.kotlinruntime.tree.ParseTreeListener

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LucidParser}.
 */
interface LucidListener : ParseTreeListener {
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