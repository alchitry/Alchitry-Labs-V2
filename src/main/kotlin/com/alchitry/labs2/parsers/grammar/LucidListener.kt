// Generated from Lucid.g4 by ANTLR 4.13.1
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.ParseTreeListener
import org.antlr.v4.kotlinruntime.tree.SuspendParseTreeListener
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This interface defines a complete listener for a parse tree produced by [LucidParser].
 */
public interface LucidListener : ParseTreeListener {
    /**
     * Enter a parse tree produced by [LucidParser.source].
     *
     * @param ctx The parse tree
     */
    public fun enterSource(ctx: LucidParser.SourceContext)

    /**
     * Exit a parse tree produced by [LucidParser.source].
     *
     * @param ctx The parse tree
     */
    public fun exitSource(ctx: LucidParser.SourceContext)

    /**
     * Enter a parse tree produced by [LucidParser.global].
     *
     * @param ctx The parse tree
     */
    public fun enterGlobal(ctx: LucidParser.GlobalContext)

    /**
     * Exit a parse tree produced by [LucidParser.global].
     *
     * @param ctx The parse tree
     */
    public fun exitGlobal(ctx: LucidParser.GlobalContext)

    /**
     * Enter a parse tree produced by [LucidParser.globalStat].
     *
     * @param ctx The parse tree
     */
    public fun enterGlobalStat(ctx: LucidParser.GlobalStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.globalStat].
     *
     * @param ctx The parse tree
     */
    public fun exitGlobalStat(ctx: LucidParser.GlobalStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.module].
     *
     * @param ctx The parse tree
     */
    public fun enterModule(ctx: LucidParser.ModuleContext)

    /**
     * Exit a parse tree produced by [LucidParser.module].
     *
     * @param ctx The parse tree
     */
    public fun exitModule(ctx: LucidParser.ModuleContext)

    /**
     * Enter a parse tree produced by [LucidParser.testBench].
     *
     * @param ctx The parse tree
     */
    public fun enterTestBench(ctx: LucidParser.TestBenchContext)

    /**
     * Exit a parse tree produced by [LucidParser.testBench].
     *
     * @param ctx The parse tree
     */
    public fun exitTestBench(ctx: LucidParser.TestBenchContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramList].
     *
     * @param ctx The parse tree
     */
    public fun enterParamList(ctx: LucidParser.ParamListContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramList].
     *
     * @param ctx The parse tree
     */
    public fun exitParamList(ctx: LucidParser.ParamListContext)

    /**
     * Enter a parse tree produced by [LucidParser.portList].
     *
     * @param ctx The parse tree
     */
    public fun enterPortList(ctx: LucidParser.PortListContext)

    /**
     * Exit a parse tree produced by [LucidParser.portList].
     *
     * @param ctx The parse tree
     */
    public fun exitPortList(ctx: LucidParser.PortListContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramDec].
     *
     * @param ctx The parse tree
     */
    public fun enterParamDec(ctx: LucidParser.ParamDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramDec].
     *
     * @param ctx The parse tree
     */
    public fun exitParamDec(ctx: LucidParser.ParamDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramDefault].
     *
     * @param ctx The parse tree
     */
    public fun enterParamDefault(ctx: LucidParser.ParamDefaultContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramDefault].
     *
     * @param ctx The parse tree
     */
    public fun exitParamDefault(ctx: LucidParser.ParamDefaultContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramConstraint].
     *
     * @param ctx The parse tree
     */
    public fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramConstraint].
     *
     * @param ctx The parse tree
     */
    public fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext)

    /**
     * Enter a parse tree produced by [LucidParser.portDec].
     *
     * @param ctx The parse tree
     */
    public fun enterPortDec(ctx: LucidParser.PortDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.portDec].
     *
     * @param ctx The parse tree
     */
    public fun exitPortDec(ctx: LucidParser.PortDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.portDirection].
     *
     * @param ctx The parse tree
     */
    public fun enterPortDirection(ctx: LucidParser.PortDirectionContext)

    /**
     * Exit a parse tree produced by [LucidParser.portDirection].
     *
     * @param ctx The parse tree
     */
    public fun exitPortDirection(ctx: LucidParser.PortDirectionContext)

    /**
     * Enter a parse tree produced by [LucidParser.signalWidth].
     *
     * @param ctx The parse tree
     */
    public fun enterSignalWidth(ctx: LucidParser.SignalWidthContext)

    /**
     * Exit a parse tree produced by [LucidParser.signalWidth].
     *
     * @param ctx The parse tree
     */
    public fun exitSignalWidth(ctx: LucidParser.SignalWidthContext)

    /**
     * Enter a parse tree produced by [LucidParser.arraySize].
     *
     * @param ctx The parse tree
     */
    public fun enterArraySize(ctx: LucidParser.ArraySizeContext)

    /**
     * Exit a parse tree produced by [LucidParser.arraySize].
     *
     * @param ctx The parse tree
     */
    public fun exitArraySize(ctx: LucidParser.ArraySizeContext)

    /**
     * Enter a parse tree produced by [LucidParser.structType].
     *
     * @param ctx The parse tree
     */
    public fun enterStructType(ctx: LucidParser.StructTypeContext)

    /**
     * Exit a parse tree produced by [LucidParser.structType].
     *
     * @param ctx The parse tree
     */
    public fun exitStructType(ctx: LucidParser.StructTypeContext)

    /**
     * Enter a parse tree produced by [LucidParser.structMemberConst].
     *
     * @param ctx The parse tree
     */
    public fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext)

    /**
     * Exit a parse tree produced by [LucidParser.structMemberConst].
     *
     * @param ctx The parse tree
     */
    public fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext)

    /**
     * Enter a parse tree produced by [LucidParser.structConst].
     *
     * @param ctx The parse tree
     */
    public fun enterStructConst(ctx: LucidParser.StructConstContext)

    /**
     * Exit a parse tree produced by [LucidParser.structConst].
     *
     * @param ctx The parse tree
     */
    public fun exitStructConst(ctx: LucidParser.StructConstContext)

    /**
     * Enter a parse tree produced by [LucidParser.moduleBody].
     *
     * @param ctx The parse tree
     */
    public fun enterModuleBody(ctx: LucidParser.ModuleBodyContext)

    /**
     * Exit a parse tree produced by [LucidParser.moduleBody].
     *
     * @param ctx The parse tree
     */
    public fun exitModuleBody(ctx: LucidParser.ModuleBodyContext)

    /**
     * Enter a parse tree produced by the `StatConst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatConst(ctx: LucidParser.StatConstContext)

    /**
     * Exit a parse tree produced by the `StatConst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatConst(ctx: LucidParser.StatConstContext)

    /**
     * Enter a parse tree produced by the `StatSig` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatSig(ctx: LucidParser.StatSigContext)

    /**
     * Exit a parse tree produced by the `StatSig` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatSig(ctx: LucidParser.StatSigContext)

    /**
     * Enter a parse tree produced by the `StatEnum` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatEnum(ctx: LucidParser.StatEnumContext)

    /**
     * Exit a parse tree produced by the `StatEnum` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatEnum(ctx: LucidParser.StatEnumContext)

    /**
     * Enter a parse tree produced by the `StatDFF` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatDFF(ctx: LucidParser.StatDFFContext)

    /**
     * Exit a parse tree produced by the `StatDFF` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatDFF(ctx: LucidParser.StatDFFContext)

    /**
     * Enter a parse tree produced by the `StatModuleInst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext)

    /**
     * Exit a parse tree produced by the `StatModuleInst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext)

    /**
     * Enter a parse tree produced by the `StatAssign` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatAssign(ctx: LucidParser.StatAssignContext)

    /**
     * Exit a parse tree produced by the `StatAssign` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatAssign(ctx: LucidParser.StatAssignContext)

    /**
     * Enter a parse tree produced by the `StatAlways` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatAlways(ctx: LucidParser.StatAlwaysContext)

    /**
     * Exit a parse tree produced by the `StatAlways` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatAlways(ctx: LucidParser.StatAlwaysContext)

    /**
     * Enter a parse tree produced by the `StatStruct` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatStruct(ctx: LucidParser.StatStructContext)

    /**
     * Exit a parse tree produced by the `StatStruct` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatStruct(ctx: LucidParser.StatStructContext)

    /**
     * Enter a parse tree produced by the `StatTest` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatTest(ctx: LucidParser.StatTestContext)

    /**
     * Exit a parse tree produced by the `StatTest` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatTest(ctx: LucidParser.StatTestContext)

    /**
     * Enter a parse tree produced by the `StatFunction` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun enterStatFunction(ctx: LucidParser.StatFunctionContext)

    /**
     * Exit a parse tree produced by the `StatFunction` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public fun exitStatFunction(ctx: LucidParser.StatFunctionContext)

    /**
     * Enter a parse tree produced by [LucidParser.constDec].
     *
     * @param ctx The parse tree
     */
    public fun enterConstDec(ctx: LucidParser.ConstDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.constDec].
     *
     * @param ctx The parse tree
     */
    public fun exitConstDec(ctx: LucidParser.ConstDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.assignBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterAssignBlock(ctx: LucidParser.AssignBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.assignBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitAssignBlock(ctx: LucidParser.AssignBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.sigCon].
     *
     * @param ctx The parse tree
     */
    public fun enterSigCon(ctx: LucidParser.SigConContext)

    /**
     * Exit a parse tree produced by [LucidParser.sigCon].
     *
     * @param ctx The parse tree
     */
    public fun exitSigCon(ctx: LucidParser.SigConContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramCon].
     *
     * @param ctx The parse tree
     */
    public fun enterParamCon(ctx: LucidParser.ParamConContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramCon].
     *
     * @param ctx The parse tree
     */
    public fun exitParamCon(ctx: LucidParser.ParamConContext)

    /**
     * Enter a parse tree produced by [LucidParser.sigDec].
     *
     * @param ctx The parse tree
     */
    public fun enterSigDec(ctx: LucidParser.SigDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.sigDec].
     *
     * @param ctx The parse tree
     */
    public fun exitSigDec(ctx: LucidParser.SigDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.dffDec].
     *
     * @param ctx The parse tree
     */
    public fun enterDffDec(ctx: LucidParser.DffDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.dffDec].
     *
     * @param ctx The parse tree
     */
    public fun exitDffDec(ctx: LucidParser.DffDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.enumDec].
     *
     * @param ctx The parse tree
     */
    public fun enterEnumDec(ctx: LucidParser.EnumDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.enumDec].
     *
     * @param ctx The parse tree
     */
    public fun exitEnumDec(ctx: LucidParser.EnumDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.moduleInst].
     *
     * @param ctx The parse tree
     */
    public fun enterModuleInst(ctx: LucidParser.ModuleInstContext)

    /**
     * Exit a parse tree produced by [LucidParser.moduleInst].
     *
     * @param ctx The parse tree
     */
    public fun exitModuleInst(ctx: LucidParser.ModuleInstContext)

    /**
     * Enter a parse tree produced by [LucidParser.instCons].
     *
     * @param ctx The parse tree
     */
    public fun enterInstCons(ctx: LucidParser.InstConsContext)

    /**
     * Exit a parse tree produced by [LucidParser.instCons].
     *
     * @param ctx The parse tree
     */
    public fun exitInstCons(ctx: LucidParser.InstConsContext)

    /**
     * Enter a parse tree produced by [LucidParser.conList].
     *
     * @param ctx The parse tree
     */
    public fun enterConList(ctx: LucidParser.ConListContext)

    /**
     * Exit a parse tree produced by [LucidParser.conList].
     *
     * @param ctx The parse tree
     */
    public fun exitConList(ctx: LucidParser.ConListContext)

    /**
     * Enter a parse tree produced by [LucidParser.connection].
     *
     * @param ctx The parse tree
     */
    public fun enterConnection(ctx: LucidParser.ConnectionContext)

    /**
     * Exit a parse tree produced by [LucidParser.connection].
     *
     * @param ctx The parse tree
     */
    public fun exitConnection(ctx: LucidParser.ConnectionContext)

    /**
     * Enter a parse tree produced by [LucidParser.structMember].
     *
     * @param ctx The parse tree
     */
    public fun enterStructMember(ctx: LucidParser.StructMemberContext)

    /**
     * Exit a parse tree produced by [LucidParser.structMember].
     *
     * @param ctx The parse tree
     */
    public fun exitStructMember(ctx: LucidParser.StructMemberContext)

    /**
     * Enter a parse tree produced by [LucidParser.structDec].
     *
     * @param ctx The parse tree
     */
    public fun enterStructDec(ctx: LucidParser.StructDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.structDec].
     *
     * @param ctx The parse tree
     */
    public fun exitStructDec(ctx: LucidParser.StructDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionArg].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionArg(ctx: LucidParser.FunctionArgContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionArg].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionArg(ctx: LucidParser.FunctionArgContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionBody].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionBody].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext)

    /**
     * Enter a parse tree produced by [LucidParser.testBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterTestBlock(ctx: LucidParser.TestBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.testBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitTestBlock(ctx: LucidParser.TestBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.alwaysBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.alwaysBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)

    /**
     * Enter a parse tree produced by the `AlwaysAssign` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)

    /**
     * Exit a parse tree produced by the `AlwaysAssign` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)

    /**
     * Enter a parse tree produced by the `AlwaysCase` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext)

    /**
     * Exit a parse tree produced by the `AlwaysCase` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext)

    /**
     * Enter a parse tree produced by the `AlwaysIf` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext)

    /**
     * Exit a parse tree produced by the `AlwaysIf` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext)

    /**
     * Enter a parse tree produced by the `AlwaysRepeat` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)

    /**
     * Exit a parse tree produced by the `AlwaysRepeat` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)

    /**
     * Enter a parse tree produced by the `AlwaysFunction` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)

    /**
     * Exit a parse tree produced by the `AlwaysFunction` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)

    /**
     * Enter a parse tree produced by the `AlwaysSignal` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAlwaysSignal(ctx: LucidParser.AlwaysSignalContext)

    /**
     * Exit a parse tree produced by the `AlwaysSignal` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAlwaysSignal(ctx: LucidParser.AlwaysSignalContext)

    /**
     * Enter a parse tree produced by [LucidParser.block].
     *
     * @param ctx The parse tree
     */
    public fun enterBlock(ctx: LucidParser.BlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.block].
     *
     * @param ctx The parse tree
     */
    public fun exitBlock(ctx: LucidParser.BlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.assignStat].
     *
     * @param ctx The parse tree
     */
    public fun enterAssignStat(ctx: LucidParser.AssignStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.assignStat].
     *
     * @param ctx The parse tree
     */
    public fun exitAssignStat(ctx: LucidParser.AssignStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext)

    /**
     * Exit a parse tree produced by [LucidParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext)

    /**
     * Enter a parse tree produced by the `BitSelectorConst` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)

    /**
     * Exit a parse tree produced by the `BitSelectorConst` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)

    /**
     * Enter a parse tree produced by the `BitSelectorFixWidth` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)

    /**
     * Exit a parse tree produced by the `BitSelectorFixWidth` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)

    /**
     * Enter a parse tree produced by [LucidParser.bitSelection].
     *
     * @param ctx The parse tree
     */
    public fun enterBitSelection(ctx: LucidParser.BitSelectionContext)

    /**
     * Exit a parse tree produced by [LucidParser.bitSelection].
     *
     * @param ctx The parse tree
     */
    public fun exitBitSelection(ctx: LucidParser.BitSelectionContext)

    /**
     * Enter a parse tree produced by [LucidParser.signal].
     *
     * @param ctx The parse tree
     */
    public fun enterSignal(ctx: LucidParser.SignalContext)

    /**
     * Exit a parse tree produced by [LucidParser.signal].
     *
     * @param ctx The parse tree
     */
    public fun exitSignal(ctx: LucidParser.SignalContext)

    /**
     * Enter a parse tree produced by [LucidParser.caseStat].
     *
     * @param ctx The parse tree
     */
    public fun enterCaseStat(ctx: LucidParser.CaseStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.caseStat].
     *
     * @param ctx The parse tree
     */
    public fun exitCaseStat(ctx: LucidParser.CaseStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.caseElem].
     *
     * @param ctx The parse tree
     */
    public fun enterCaseElem(ctx: LucidParser.CaseElemContext)

    /**
     * Exit a parse tree produced by [LucidParser.caseElem].
     *
     * @param ctx The parse tree
     */
    public fun exitCaseElem(ctx: LucidParser.CaseElemContext)

    /**
     * Enter a parse tree produced by [LucidParser.caseBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterCaseBlock(ctx: LucidParser.CaseBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.caseBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitCaseBlock(ctx: LucidParser.CaseBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.ifStat].
     *
     * @param ctx The parse tree
     */
    public fun enterIfStat(ctx: LucidParser.IfStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.ifStat].
     *
     * @param ctx The parse tree
     */
    public fun exitIfStat(ctx: LucidParser.IfStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.elseStat].
     *
     * @param ctx The parse tree
     */
    public fun enterElseStat(ctx: LucidParser.ElseStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.elseStat].
     *
     * @param ctx The parse tree
     */
    public fun exitElseStat(ctx: LucidParser.ElseStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.repeatStat].
     *
     * @param ctx The parse tree
     */
    public fun enterRepeatStat(ctx: LucidParser.RepeatStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.repeatStat].
     *
     * @param ctx The parse tree
     */
    public fun exitRepeatStat(ctx: LucidParser.RepeatStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.repeatBlock].
     *
     * @param ctx The parse tree
     */
    public fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.repeatBlock].
     *
     * @param ctx The parse tree
     */
    public fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.function].
     *
     * @param ctx The parse tree
     */
    public fun enterFunction(ctx: LucidParser.FunctionContext)

    /**
     * Exit a parse tree produced by [LucidParser.function].
     *
     * @param ctx The parse tree
     */
    public fun exitFunction(ctx: LucidParser.FunctionContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionExpr].
     *
     * @param ctx The parse tree
     */
    public fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionExpr].
     *
     * @param ctx The parse tree
     */
    public fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext)

    /**
     * Enter a parse tree produced by [LucidParser.number].
     *
     * @param ctx The parse tree
     */
    public fun enterNumber(ctx: LucidParser.NumberContext)

    /**
     * Exit a parse tree produced by [LucidParser.number].
     *
     * @param ctx The parse tree
     */
    public fun exitNumber(ctx: LucidParser.NumberContext)

    /**
     * Enter a parse tree produced by the `ExprTernary` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprTernary(ctx: LucidParser.ExprTernaryContext)

    /**
     * Exit a parse tree produced by the `ExprTernary` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprTernary(ctx: LucidParser.ExprTernaryContext)

    /**
     * Enter a parse tree produced by the `ExprNum` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprNum(ctx: LucidParser.ExprNumContext)

    /**
     * Exit a parse tree produced by the `ExprNum` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprNum(ctx: LucidParser.ExprNumContext)

    /**
     * Enter a parse tree produced by the `ExprConcat` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprConcat(ctx: LucidParser.ExprConcatContext)

    /**
     * Exit a parse tree produced by the `ExprConcat` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprConcat(ctx: LucidParser.ExprConcatContext)

    /**
     * Enter a parse tree produced by the `ExprReduction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprReduction(ctx: LucidParser.ExprReductionContext)

    /**
     * Exit a parse tree produced by the `ExprReduction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprReduction(ctx: LucidParser.ExprReductionContext)

    /**
     * Enter a parse tree produced by the `ExprInvert` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprInvert(ctx: LucidParser.ExprInvertContext)

    /**
     * Exit a parse tree produced by the `ExprInvert` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprInvert(ctx: LucidParser.ExprInvertContext)

    /**
     * Enter a parse tree produced by the `ExprStruct` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprStruct(ctx: LucidParser.ExprStructContext)

    /**
     * Exit a parse tree produced by the `ExprStruct` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprStruct(ctx: LucidParser.ExprStructContext)

    /**
     * Enter a parse tree produced by the `ExprArray` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprArray(ctx: LucidParser.ExprArrayContext)

    /**
     * Exit a parse tree produced by the `ExprArray` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprArray(ctx: LucidParser.ExprArrayContext)

    /**
     * Enter a parse tree produced by the `ExprShift` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprShift(ctx: LucidParser.ExprShiftContext)

    /**
     * Exit a parse tree produced by the `ExprShift` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprShift(ctx: LucidParser.ExprShiftContext)

    /**
     * Enter a parse tree produced by the `ExprAddSub` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext)

    /**
     * Exit a parse tree produced by the `ExprAddSub` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext)

    /**
     * Enter a parse tree produced by the `ExprLogical` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprLogical(ctx: LucidParser.ExprLogicalContext)

    /**
     * Exit a parse tree produced by the `ExprLogical` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprLogical(ctx: LucidParser.ExprLogicalContext)

    /**
     * Enter a parse tree produced by the `ExprNegate` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprNegate(ctx: LucidParser.ExprNegateContext)

    /**
     * Exit a parse tree produced by the `ExprNegate` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprNegate(ctx: LucidParser.ExprNegateContext)

    /**
     * Enter a parse tree produced by the `ExprGroup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprGroup(ctx: LucidParser.ExprGroupContext)

    /**
     * Exit a parse tree produced by the `ExprGroup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprGroup(ctx: LucidParser.ExprGroupContext)

    /**
     * Enter a parse tree produced by the `ExprBitwise` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext)

    /**
     * Exit a parse tree produced by the `ExprBitwise` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext)

    /**
     * Enter a parse tree produced by the `ExprFunction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprFunction(ctx: LucidParser.ExprFunctionContext)

    /**
     * Exit a parse tree produced by the `ExprFunction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprFunction(ctx: LucidParser.ExprFunctionContext)

    /**
     * Enter a parse tree produced by the `ExprCompare` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprCompare(ctx: LucidParser.ExprCompareContext)

    /**
     * Exit a parse tree produced by the `ExprCompare` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprCompare(ctx: LucidParser.ExprCompareContext)

    /**
     * Enter a parse tree produced by the `ExprDup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprDup(ctx: LucidParser.ExprDupContext)

    /**
     * Exit a parse tree produced by the `ExprDup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprDup(ctx: LucidParser.ExprDupContext)

    /**
     * Enter a parse tree produced by the `ExprMultDiv` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext)

    /**
     * Exit a parse tree produced by the `ExprMultDiv` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext)

    /**
     * Enter a parse tree produced by the `ExprSignal` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun enterExprSignal(ctx: LucidParser.ExprSignalContext)

    /**
     * Exit a parse tree produced by the `ExprSignal` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public fun exitExprSignal(ctx: LucidParser.ExprSignalContext)

    /**
     * Enter a parse tree produced by [LucidParser.name].
     *
     * @param ctx The parse tree
     */
    public fun enterName(ctx: LucidParser.NameContext)

    /**
     * Exit a parse tree produced by [LucidParser.name].
     *
     * @param ctx The parse tree
     */
    public fun exitName(ctx: LucidParser.NameContext)

    /**
     * Enter a parse tree produced by [LucidParser.semi].
     *
     * @param ctx The parse tree
     */
    public fun enterSemi(ctx: LucidParser.SemiContext)

    /**
     * Exit a parse tree produced by [LucidParser.semi].
     *
     * @param ctx The parse tree
     */
    public fun exitSemi(ctx: LucidParser.SemiContext)


    override fun asSuspend(): SuspendLucidListener = object: SuspendLucidListener {
        override suspend fun enterSource(ctx: LucidParser.SourceContext) = this@LucidListener.enterSource(ctx)
        override suspend fun exitSource(ctx: LucidParser.SourceContext) = this@LucidListener.exitSource(ctx)

        override suspend fun enterGlobal(ctx: LucidParser.GlobalContext) = this@LucidListener.enterGlobal(ctx)
        override suspend fun exitGlobal(ctx: LucidParser.GlobalContext) = this@LucidListener.exitGlobal(ctx)

        override suspend fun enterGlobalStat(ctx: LucidParser.GlobalStatContext) = this@LucidListener.enterGlobalStat(ctx)
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

        override suspend fun enterParamDefault(ctx: LucidParser.ParamDefaultContext) = this@LucidListener.enterParamDefault(ctx)
        override suspend fun exitParamDefault(ctx: LucidParser.ParamDefaultContext) = this@LucidListener.exitParamDefault(ctx)

        override suspend fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext) = this@LucidListener.enterParamConstraint(ctx)
        override suspend fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext) = this@LucidListener.exitParamConstraint(ctx)

        override suspend fun enterPortDec(ctx: LucidParser.PortDecContext) = this@LucidListener.enterPortDec(ctx)
        override suspend fun exitPortDec(ctx: LucidParser.PortDecContext) = this@LucidListener.exitPortDec(ctx)

        override suspend fun enterPortDirection(ctx: LucidParser.PortDirectionContext) = this@LucidListener.enterPortDirection(ctx)
        override suspend fun exitPortDirection(ctx: LucidParser.PortDirectionContext) = this@LucidListener.exitPortDirection(ctx)

        override suspend fun enterSignalWidth(ctx: LucidParser.SignalWidthContext) = this@LucidListener.enterSignalWidth(ctx)
        override suspend fun exitSignalWidth(ctx: LucidParser.SignalWidthContext) = this@LucidListener.exitSignalWidth(ctx)

        override suspend fun enterArraySize(ctx: LucidParser.ArraySizeContext) = this@LucidListener.enterArraySize(ctx)
        override suspend fun exitArraySize(ctx: LucidParser.ArraySizeContext) = this@LucidListener.exitArraySize(ctx)

        override suspend fun enterStructType(ctx: LucidParser.StructTypeContext) = this@LucidListener.enterStructType(ctx)
        override suspend fun exitStructType(ctx: LucidParser.StructTypeContext) = this@LucidListener.exitStructType(ctx)

        override suspend fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext) = this@LucidListener.enterStructMemberConst(ctx)
        override suspend fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext) = this@LucidListener.exitStructMemberConst(ctx)

        override suspend fun enterStructConst(ctx: LucidParser.StructConstContext) = this@LucidListener.enterStructConst(ctx)
        override suspend fun exitStructConst(ctx: LucidParser.StructConstContext) = this@LucidListener.exitStructConst(ctx)

        override suspend fun enterModuleBody(ctx: LucidParser.ModuleBodyContext) = this@LucidListener.enterModuleBody(ctx)
        override suspend fun exitModuleBody(ctx: LucidParser.ModuleBodyContext) = this@LucidListener.exitModuleBody(ctx)

        override suspend fun enterStatConst(ctx: LucidParser.StatConstContext) = this@LucidListener.enterStatConst(ctx)
        override suspend fun exitStatConst(ctx: LucidParser.StatConstContext) = this@LucidListener.exitStatConst(ctx)

        override suspend fun enterStatSig(ctx: LucidParser.StatSigContext) = this@LucidListener.enterStatSig(ctx)
        override suspend fun exitStatSig(ctx: LucidParser.StatSigContext) = this@LucidListener.exitStatSig(ctx)

        override suspend fun enterStatEnum(ctx: LucidParser.StatEnumContext) = this@LucidListener.enterStatEnum(ctx)
        override suspend fun exitStatEnum(ctx: LucidParser.StatEnumContext) = this@LucidListener.exitStatEnum(ctx)

        override suspend fun enterStatDFF(ctx: LucidParser.StatDFFContext) = this@LucidListener.enterStatDFF(ctx)
        override suspend fun exitStatDFF(ctx: LucidParser.StatDFFContext) = this@LucidListener.exitStatDFF(ctx)

        override suspend fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext) = this@LucidListener.enterStatModuleInst(ctx)
        override suspend fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext) = this@LucidListener.exitStatModuleInst(ctx)

        override suspend fun enterStatAssign(ctx: LucidParser.StatAssignContext) = this@LucidListener.enterStatAssign(ctx)
        override suspend fun exitStatAssign(ctx: LucidParser.StatAssignContext) = this@LucidListener.exitStatAssign(ctx)

        override suspend fun enterStatAlways(ctx: LucidParser.StatAlwaysContext) = this@LucidListener.enterStatAlways(ctx)
        override suspend fun exitStatAlways(ctx: LucidParser.StatAlwaysContext) = this@LucidListener.exitStatAlways(ctx)

        override suspend fun enterStatStruct(ctx: LucidParser.StatStructContext) = this@LucidListener.enterStatStruct(ctx)
        override suspend fun exitStatStruct(ctx: LucidParser.StatStructContext) = this@LucidListener.exitStatStruct(ctx)

        override suspend fun enterStatTest(ctx: LucidParser.StatTestContext) = this@LucidListener.enterStatTest(ctx)
        override suspend fun exitStatTest(ctx: LucidParser.StatTestContext) = this@LucidListener.exitStatTest(ctx)

        override suspend fun enterStatFunction(ctx: LucidParser.StatFunctionContext) = this@LucidListener.enterStatFunction(ctx)
        override suspend fun exitStatFunction(ctx: LucidParser.StatFunctionContext) = this@LucidListener.exitStatFunction(ctx)

        override suspend fun enterConstDec(ctx: LucidParser.ConstDecContext) = this@LucidListener.enterConstDec(ctx)
        override suspend fun exitConstDec(ctx: LucidParser.ConstDecContext) = this@LucidListener.exitConstDec(ctx)

        override suspend fun enterAssignBlock(ctx: LucidParser.AssignBlockContext) = this@LucidListener.enterAssignBlock(ctx)
        override suspend fun exitAssignBlock(ctx: LucidParser.AssignBlockContext) = this@LucidListener.exitAssignBlock(ctx)

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

        override suspend fun enterModuleInst(ctx: LucidParser.ModuleInstContext) = this@LucidListener.enterModuleInst(ctx)
        override suspend fun exitModuleInst(ctx: LucidParser.ModuleInstContext) = this@LucidListener.exitModuleInst(ctx)

        override suspend fun enterInstCons(ctx: LucidParser.InstConsContext) = this@LucidListener.enterInstCons(ctx)
        override suspend fun exitInstCons(ctx: LucidParser.InstConsContext) = this@LucidListener.exitInstCons(ctx)

        override suspend fun enterConList(ctx: LucidParser.ConListContext) = this@LucidListener.enterConList(ctx)
        override suspend fun exitConList(ctx: LucidParser.ConListContext) = this@LucidListener.exitConList(ctx)

        override suspend fun enterConnection(ctx: LucidParser.ConnectionContext) = this@LucidListener.enterConnection(ctx)
        override suspend fun exitConnection(ctx: LucidParser.ConnectionContext) = this@LucidListener.exitConnection(ctx)

        override suspend fun enterStructMember(ctx: LucidParser.StructMemberContext) = this@LucidListener.enterStructMember(ctx)
        override suspend fun exitStructMember(ctx: LucidParser.StructMemberContext) = this@LucidListener.exitStructMember(ctx)

        override suspend fun enterStructDec(ctx: LucidParser.StructDecContext) = this@LucidListener.enterStructDec(ctx)
        override suspend fun exitStructDec(ctx: LucidParser.StructDecContext) = this@LucidListener.exitStructDec(ctx)

        override suspend fun enterFunctionArg(ctx: LucidParser.FunctionArgContext) = this@LucidListener.enterFunctionArg(ctx)
        override suspend fun exitFunctionArg(ctx: LucidParser.FunctionArgContext) = this@LucidListener.exitFunctionArg(ctx)

        override suspend fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext) = this@LucidListener.enterFunctionBlock(ctx)
        override suspend fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext) = this@LucidListener.exitFunctionBlock(ctx)

        override suspend fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext) = this@LucidListener.enterFunctionBody(ctx)
        override suspend fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext) = this@LucidListener.exitFunctionBody(ctx)

        override suspend fun enterTestBlock(ctx: LucidParser.TestBlockContext) = this@LucidListener.enterTestBlock(ctx)
        override suspend fun exitTestBlock(ctx: LucidParser.TestBlockContext) = this@LucidListener.exitTestBlock(ctx)

        override suspend fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) = this@LucidListener.enterAlwaysBlock(ctx)
        override suspend fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) = this@LucidListener.exitAlwaysBlock(ctx)

        override suspend fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) = this@LucidListener.enterAlwaysAssign(ctx)
        override suspend fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) = this@LucidListener.exitAlwaysAssign(ctx)

        override suspend fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext) = this@LucidListener.enterAlwaysCase(ctx)
        override suspend fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext) = this@LucidListener.exitAlwaysCase(ctx)

        override suspend fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext) = this@LucidListener.enterAlwaysIf(ctx)
        override suspend fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext) = this@LucidListener.exitAlwaysIf(ctx)

        override suspend fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) = this@LucidListener.enterAlwaysRepeat(ctx)
        override suspend fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) = this@LucidListener.exitAlwaysRepeat(ctx)

        override suspend fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) = this@LucidListener.enterAlwaysFunction(ctx)
        override suspend fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) = this@LucidListener.exitAlwaysFunction(ctx)

        override suspend fun enterAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) = this@LucidListener.enterAlwaysSignal(ctx)
        override suspend fun exitAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) = this@LucidListener.exitAlwaysSignal(ctx)

        override suspend fun enterBlock(ctx: LucidParser.BlockContext) = this@LucidListener.enterBlock(ctx)
        override suspend fun exitBlock(ctx: LucidParser.BlockContext) = this@LucidListener.exitBlock(ctx)

        override suspend fun enterAssignStat(ctx: LucidParser.AssignStatContext) = this@LucidListener.enterAssignStat(ctx)
        override suspend fun exitAssignStat(ctx: LucidParser.AssignStatContext) = this@LucidListener.exitAssignStat(ctx)

        override suspend fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext) = this@LucidListener.enterArrayIndex(ctx)
        override suspend fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext) = this@LucidListener.exitArrayIndex(ctx)

        override suspend fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) = this@LucidListener.enterBitSelectorConst(ctx)
        override suspend fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) = this@LucidListener.exitBitSelectorConst(ctx)

        override suspend fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) = this@LucidListener.enterBitSelectorFixWidth(ctx)
        override suspend fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) = this@LucidListener.exitBitSelectorFixWidth(ctx)

        override suspend fun enterBitSelection(ctx: LucidParser.BitSelectionContext) = this@LucidListener.enterBitSelection(ctx)
        override suspend fun exitBitSelection(ctx: LucidParser.BitSelectionContext) = this@LucidListener.exitBitSelection(ctx)

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

        override suspend fun enterRepeatStat(ctx: LucidParser.RepeatStatContext) = this@LucidListener.enterRepeatStat(ctx)
        override suspend fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) = this@LucidListener.exitRepeatStat(ctx)

        override suspend fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext) = this@LucidListener.enterRepeatBlock(ctx)
        override suspend fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext) = this@LucidListener.exitRepeatBlock(ctx)

        override suspend fun enterFunction(ctx: LucidParser.FunctionContext) = this@LucidListener.enterFunction(ctx)
        override suspend fun exitFunction(ctx: LucidParser.FunctionContext) = this@LucidListener.exitFunction(ctx)

        override suspend fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext) = this@LucidListener.enterFunctionExpr(ctx)
        override suspend fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext) = this@LucidListener.exitFunctionExpr(ctx)

        override suspend fun enterNumber(ctx: LucidParser.NumberContext) = this@LucidListener.enterNumber(ctx)
        override suspend fun exitNumber(ctx: LucidParser.NumberContext) = this@LucidListener.exitNumber(ctx)

        override suspend fun enterExprTernary(ctx: LucidParser.ExprTernaryContext) = this@LucidListener.enterExprTernary(ctx)
        override suspend fun exitExprTernary(ctx: LucidParser.ExprTernaryContext) = this@LucidListener.exitExprTernary(ctx)

        override suspend fun enterExprNum(ctx: LucidParser.ExprNumContext) = this@LucidListener.enterExprNum(ctx)
        override suspend fun exitExprNum(ctx: LucidParser.ExprNumContext) = this@LucidListener.exitExprNum(ctx)

        override suspend fun enterExprConcat(ctx: LucidParser.ExprConcatContext) = this@LucidListener.enterExprConcat(ctx)
        override suspend fun exitExprConcat(ctx: LucidParser.ExprConcatContext) = this@LucidListener.exitExprConcat(ctx)

        override suspend fun enterExprReduction(ctx: LucidParser.ExprReductionContext) = this@LucidListener.enterExprReduction(ctx)
        override suspend fun exitExprReduction(ctx: LucidParser.ExprReductionContext) = this@LucidListener.exitExprReduction(ctx)

        override suspend fun enterExprInvert(ctx: LucidParser.ExprInvertContext) = this@LucidListener.enterExprInvert(ctx)
        override suspend fun exitExprInvert(ctx: LucidParser.ExprInvertContext) = this@LucidListener.exitExprInvert(ctx)

        override suspend fun enterExprStruct(ctx: LucidParser.ExprStructContext) = this@LucidListener.enterExprStruct(ctx)
        override suspend fun exitExprStruct(ctx: LucidParser.ExprStructContext) = this@LucidListener.exitExprStruct(ctx)

        override suspend fun enterExprArray(ctx: LucidParser.ExprArrayContext) = this@LucidListener.enterExprArray(ctx)
        override suspend fun exitExprArray(ctx: LucidParser.ExprArrayContext) = this@LucidListener.exitExprArray(ctx)

        override suspend fun enterExprShift(ctx: LucidParser.ExprShiftContext) = this@LucidListener.enterExprShift(ctx)
        override suspend fun exitExprShift(ctx: LucidParser.ExprShiftContext) = this@LucidListener.exitExprShift(ctx)

        override suspend fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext) = this@LucidListener.enterExprAddSub(ctx)
        override suspend fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext) = this@LucidListener.exitExprAddSub(ctx)

        override suspend fun enterExprLogical(ctx: LucidParser.ExprLogicalContext) = this@LucidListener.enterExprLogical(ctx)
        override suspend fun exitExprLogical(ctx: LucidParser.ExprLogicalContext) = this@LucidListener.exitExprLogical(ctx)

        override suspend fun enterExprNegate(ctx: LucidParser.ExprNegateContext) = this@LucidListener.enterExprNegate(ctx)
        override suspend fun exitExprNegate(ctx: LucidParser.ExprNegateContext) = this@LucidListener.exitExprNegate(ctx)

        override suspend fun enterExprGroup(ctx: LucidParser.ExprGroupContext) = this@LucidListener.enterExprGroup(ctx)
        override suspend fun exitExprGroup(ctx: LucidParser.ExprGroupContext) = this@LucidListener.exitExprGroup(ctx)

        override suspend fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext) = this@LucidListener.enterExprBitwise(ctx)
        override suspend fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext) = this@LucidListener.exitExprBitwise(ctx)

        override suspend fun enterExprFunction(ctx: LucidParser.ExprFunctionContext) = this@LucidListener.enterExprFunction(ctx)
        override suspend fun exitExprFunction(ctx: LucidParser.ExprFunctionContext) = this@LucidListener.exitExprFunction(ctx)

        override suspend fun enterExprCompare(ctx: LucidParser.ExprCompareContext) = this@LucidListener.enterExprCompare(ctx)
        override suspend fun exitExprCompare(ctx: LucidParser.ExprCompareContext) = this@LucidListener.exitExprCompare(ctx)

        override suspend fun enterExprDup(ctx: LucidParser.ExprDupContext) = this@LucidListener.enterExprDup(ctx)
        override suspend fun exitExprDup(ctx: LucidParser.ExprDupContext) = this@LucidListener.exitExprDup(ctx)

        override suspend fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext) = this@LucidListener.enterExprMultDiv(ctx)
        override suspend fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext) = this@LucidListener.exitExprMultDiv(ctx)

        override suspend fun enterExprSignal(ctx: LucidParser.ExprSignalContext) = this@LucidListener.enterExprSignal(ctx)
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
 * This interface defines a complete suspend listener for a parse tree produced by [LucidParser].
 */
public interface SuspendLucidListener : SuspendParseTreeListener {
    /**
     * Enter a parse tree produced by [LucidParser.source].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSource(ctx: LucidParser.SourceContext)

    /**
     * Exit a parse tree produced by [LucidParser.source].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSource(ctx: LucidParser.SourceContext)

    /**
     * Enter a parse tree produced by [LucidParser.global].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterGlobal(ctx: LucidParser.GlobalContext)

    /**
     * Exit a parse tree produced by [LucidParser.global].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitGlobal(ctx: LucidParser.GlobalContext)

    /**
     * Enter a parse tree produced by [LucidParser.globalStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterGlobalStat(ctx: LucidParser.GlobalStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.globalStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitGlobalStat(ctx: LucidParser.GlobalStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.module].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterModule(ctx: LucidParser.ModuleContext)

    /**
     * Exit a parse tree produced by [LucidParser.module].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitModule(ctx: LucidParser.ModuleContext)

    /**
     * Enter a parse tree produced by [LucidParser.testBench].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterTestBench(ctx: LucidParser.TestBenchContext)

    /**
     * Exit a parse tree produced by [LucidParser.testBench].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitTestBench(ctx: LucidParser.TestBenchContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramList].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterParamList(ctx: LucidParser.ParamListContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramList].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitParamList(ctx: LucidParser.ParamListContext)

    /**
     * Enter a parse tree produced by [LucidParser.portList].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterPortList(ctx: LucidParser.PortListContext)

    /**
     * Exit a parse tree produced by [LucidParser.portList].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitPortList(ctx: LucidParser.PortListContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterParamDec(ctx: LucidParser.ParamDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitParamDec(ctx: LucidParser.ParamDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramDefault].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterParamDefault(ctx: LucidParser.ParamDefaultContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramDefault].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitParamDefault(ctx: LucidParser.ParamDefaultContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramConstraint].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramConstraint].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext)

    /**
     * Enter a parse tree produced by [LucidParser.portDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterPortDec(ctx: LucidParser.PortDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.portDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitPortDec(ctx: LucidParser.PortDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.portDirection].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterPortDirection(ctx: LucidParser.PortDirectionContext)

    /**
     * Exit a parse tree produced by [LucidParser.portDirection].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitPortDirection(ctx: LucidParser.PortDirectionContext)

    /**
     * Enter a parse tree produced by [LucidParser.signalWidth].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSignalWidth(ctx: LucidParser.SignalWidthContext)

    /**
     * Exit a parse tree produced by [LucidParser.signalWidth].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSignalWidth(ctx: LucidParser.SignalWidthContext)

    /**
     * Enter a parse tree produced by [LucidParser.arraySize].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterArraySize(ctx: LucidParser.ArraySizeContext)

    /**
     * Exit a parse tree produced by [LucidParser.arraySize].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitArraySize(ctx: LucidParser.ArraySizeContext)

    /**
     * Enter a parse tree produced by [LucidParser.structType].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStructType(ctx: LucidParser.StructTypeContext)

    /**
     * Exit a parse tree produced by [LucidParser.structType].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStructType(ctx: LucidParser.StructTypeContext)

    /**
     * Enter a parse tree produced by [LucidParser.structMemberConst].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext)

    /**
     * Exit a parse tree produced by [LucidParser.structMemberConst].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext)

    /**
     * Enter a parse tree produced by [LucidParser.structConst].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStructConst(ctx: LucidParser.StructConstContext)

    /**
     * Exit a parse tree produced by [LucidParser.structConst].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStructConst(ctx: LucidParser.StructConstContext)

    /**
     * Enter a parse tree produced by [LucidParser.moduleBody].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterModuleBody(ctx: LucidParser.ModuleBodyContext)

    /**
     * Exit a parse tree produced by [LucidParser.moduleBody].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitModuleBody(ctx: LucidParser.ModuleBodyContext)

    /**
     * Enter a parse tree produced by the `StatConst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatConst(ctx: LucidParser.StatConstContext)

    /**
     * Exit a parse tree produced by the `StatConst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatConst(ctx: LucidParser.StatConstContext)

    /**
     * Enter a parse tree produced by the `StatSig` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatSig(ctx: LucidParser.StatSigContext)

    /**
     * Exit a parse tree produced by the `StatSig` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatSig(ctx: LucidParser.StatSigContext)

    /**
     * Enter a parse tree produced by the `StatEnum` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatEnum(ctx: LucidParser.StatEnumContext)

    /**
     * Exit a parse tree produced by the `StatEnum` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatEnum(ctx: LucidParser.StatEnumContext)

    /**
     * Enter a parse tree produced by the `StatDFF` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatDFF(ctx: LucidParser.StatDFFContext)

    /**
     * Exit a parse tree produced by the `StatDFF` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatDFF(ctx: LucidParser.StatDFFContext)

    /**
     * Enter a parse tree produced by the `StatModuleInst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext)

    /**
     * Exit a parse tree produced by the `StatModuleInst` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext)

    /**
     * Enter a parse tree produced by the `StatAssign` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatAssign(ctx: LucidParser.StatAssignContext)

    /**
     * Exit a parse tree produced by the `StatAssign` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatAssign(ctx: LucidParser.StatAssignContext)

    /**
     * Enter a parse tree produced by the `StatAlways` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatAlways(ctx: LucidParser.StatAlwaysContext)

    /**
     * Exit a parse tree produced by the `StatAlways` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatAlways(ctx: LucidParser.StatAlwaysContext)

    /**
     * Enter a parse tree produced by the `StatStruct` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatStruct(ctx: LucidParser.StatStructContext)

    /**
     * Exit a parse tree produced by the `StatStruct` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatStruct(ctx: LucidParser.StatStructContext)

    /**
     * Enter a parse tree produced by the `StatTest` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatTest(ctx: LucidParser.StatTestContext)

    /**
     * Exit a parse tree produced by the `StatTest` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatTest(ctx: LucidParser.StatTestContext)

    /**
     * Enter a parse tree produced by the `StatFunction` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStatFunction(ctx: LucidParser.StatFunctionContext)

    /**
     * Exit a parse tree produced by the `StatFunction` labeled alternative in [LucidParser.stat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStatFunction(ctx: LucidParser.StatFunctionContext)

    /**
     * Enter a parse tree produced by [LucidParser.constDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterConstDec(ctx: LucidParser.ConstDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.constDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitConstDec(ctx: LucidParser.ConstDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.assignBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAssignBlock(ctx: LucidParser.AssignBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.assignBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAssignBlock(ctx: LucidParser.AssignBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.sigCon].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSigCon(ctx: LucidParser.SigConContext)

    /**
     * Exit a parse tree produced by [LucidParser.sigCon].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSigCon(ctx: LucidParser.SigConContext)

    /**
     * Enter a parse tree produced by [LucidParser.paramCon].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterParamCon(ctx: LucidParser.ParamConContext)

    /**
     * Exit a parse tree produced by [LucidParser.paramCon].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitParamCon(ctx: LucidParser.ParamConContext)

    /**
     * Enter a parse tree produced by [LucidParser.sigDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSigDec(ctx: LucidParser.SigDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.sigDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSigDec(ctx: LucidParser.SigDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.dffDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterDffDec(ctx: LucidParser.DffDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.dffDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitDffDec(ctx: LucidParser.DffDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.enumDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterEnumDec(ctx: LucidParser.EnumDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.enumDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitEnumDec(ctx: LucidParser.EnumDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.moduleInst].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterModuleInst(ctx: LucidParser.ModuleInstContext)

    /**
     * Exit a parse tree produced by [LucidParser.moduleInst].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitModuleInst(ctx: LucidParser.ModuleInstContext)

    /**
     * Enter a parse tree produced by [LucidParser.instCons].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterInstCons(ctx: LucidParser.InstConsContext)

    /**
     * Exit a parse tree produced by [LucidParser.instCons].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitInstCons(ctx: LucidParser.InstConsContext)

    /**
     * Enter a parse tree produced by [LucidParser.conList].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterConList(ctx: LucidParser.ConListContext)

    /**
     * Exit a parse tree produced by [LucidParser.conList].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitConList(ctx: LucidParser.ConListContext)

    /**
     * Enter a parse tree produced by [LucidParser.connection].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterConnection(ctx: LucidParser.ConnectionContext)

    /**
     * Exit a parse tree produced by [LucidParser.connection].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitConnection(ctx: LucidParser.ConnectionContext)

    /**
     * Enter a parse tree produced by [LucidParser.structMember].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStructMember(ctx: LucidParser.StructMemberContext)

    /**
     * Exit a parse tree produced by [LucidParser.structMember].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStructMember(ctx: LucidParser.StructMemberContext)

    /**
     * Enter a parse tree produced by [LucidParser.structDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterStructDec(ctx: LucidParser.StructDecContext)

    /**
     * Exit a parse tree produced by [LucidParser.structDec].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitStructDec(ctx: LucidParser.StructDecContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionArg].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterFunctionArg(ctx: LucidParser.FunctionArgContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionArg].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitFunctionArg(ctx: LucidParser.FunctionArgContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionBody].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionBody].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext)

    /**
     * Enter a parse tree produced by [LucidParser.testBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterTestBlock(ctx: LucidParser.TestBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.testBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitTestBlock(ctx: LucidParser.TestBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.alwaysBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.alwaysBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext)

    /**
     * Enter a parse tree produced by the `AlwaysAssign` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)

    /**
     * Exit a parse tree produced by the `AlwaysAssign` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext)

    /**
     * Enter a parse tree produced by the `AlwaysCase` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext)

    /**
     * Exit a parse tree produced by the `AlwaysCase` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext)

    /**
     * Enter a parse tree produced by the `AlwaysIf` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext)

    /**
     * Exit a parse tree produced by the `AlwaysIf` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext)

    /**
     * Enter a parse tree produced by the `AlwaysRepeat` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)

    /**
     * Exit a parse tree produced by the `AlwaysRepeat` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext)

    /**
     * Enter a parse tree produced by the `AlwaysFunction` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)

    /**
     * Exit a parse tree produced by the `AlwaysFunction` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext)

    /**
     * Enter a parse tree produced by the `AlwaysSignal` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAlwaysSignal(ctx: LucidParser.AlwaysSignalContext)

    /**
     * Exit a parse tree produced by the `AlwaysSignal` labeled alternative in [LucidParser.alwaysStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAlwaysSignal(ctx: LucidParser.AlwaysSignalContext)

    /**
     * Enter a parse tree produced by [LucidParser.block].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterBlock(ctx: LucidParser.BlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.block].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitBlock(ctx: LucidParser.BlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.assignStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterAssignStat(ctx: LucidParser.AssignStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.assignStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitAssignStat(ctx: LucidParser.AssignStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext)

    /**
     * Exit a parse tree produced by [LucidParser.arrayIndex].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext)

    /**
     * Enter a parse tree produced by the `BitSelectorConst` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)

    /**
     * Exit a parse tree produced by the `BitSelectorConst` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext)

    /**
     * Enter a parse tree produced by the `BitSelectorFixWidth` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)

    /**
     * Exit a parse tree produced by the `BitSelectorFixWidth` labeled alternative in [LucidParser.bitSelector].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext)

    /**
     * Enter a parse tree produced by [LucidParser.bitSelection].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterBitSelection(ctx: LucidParser.BitSelectionContext)

    /**
     * Exit a parse tree produced by [LucidParser.bitSelection].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitBitSelection(ctx: LucidParser.BitSelectionContext)

    /**
     * Enter a parse tree produced by [LucidParser.signal].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSignal(ctx: LucidParser.SignalContext)

    /**
     * Exit a parse tree produced by [LucidParser.signal].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSignal(ctx: LucidParser.SignalContext)

    /**
     * Enter a parse tree produced by [LucidParser.caseStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterCaseStat(ctx: LucidParser.CaseStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.caseStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitCaseStat(ctx: LucidParser.CaseStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.caseElem].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterCaseElem(ctx: LucidParser.CaseElemContext)

    /**
     * Exit a parse tree produced by [LucidParser.caseElem].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitCaseElem(ctx: LucidParser.CaseElemContext)

    /**
     * Enter a parse tree produced by [LucidParser.caseBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterCaseBlock(ctx: LucidParser.CaseBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.caseBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitCaseBlock(ctx: LucidParser.CaseBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.ifStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterIfStat(ctx: LucidParser.IfStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.ifStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitIfStat(ctx: LucidParser.IfStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.elseStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterElseStat(ctx: LucidParser.ElseStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.elseStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitElseStat(ctx: LucidParser.ElseStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.repeatStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterRepeatStat(ctx: LucidParser.RepeatStatContext)

    /**
     * Exit a parse tree produced by [LucidParser.repeatStat].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitRepeatStat(ctx: LucidParser.RepeatStatContext)

    /**
     * Enter a parse tree produced by [LucidParser.repeatBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext)

    /**
     * Exit a parse tree produced by [LucidParser.repeatBlock].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext)

    /**
     * Enter a parse tree produced by [LucidParser.function].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterFunction(ctx: LucidParser.FunctionContext)

    /**
     * Exit a parse tree produced by [LucidParser.function].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitFunction(ctx: LucidParser.FunctionContext)

    /**
     * Enter a parse tree produced by [LucidParser.functionExpr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext)

    /**
     * Exit a parse tree produced by [LucidParser.functionExpr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext)

    /**
     * Enter a parse tree produced by [LucidParser.number].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterNumber(ctx: LucidParser.NumberContext)

    /**
     * Exit a parse tree produced by [LucidParser.number].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitNumber(ctx: LucidParser.NumberContext)

    /**
     * Enter a parse tree produced by the `ExprTernary` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprTernary(ctx: LucidParser.ExprTernaryContext)

    /**
     * Exit a parse tree produced by the `ExprTernary` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprTernary(ctx: LucidParser.ExprTernaryContext)

    /**
     * Enter a parse tree produced by the `ExprNum` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprNum(ctx: LucidParser.ExprNumContext)

    /**
     * Exit a parse tree produced by the `ExprNum` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprNum(ctx: LucidParser.ExprNumContext)

    /**
     * Enter a parse tree produced by the `ExprConcat` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprConcat(ctx: LucidParser.ExprConcatContext)

    /**
     * Exit a parse tree produced by the `ExprConcat` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprConcat(ctx: LucidParser.ExprConcatContext)

    /**
     * Enter a parse tree produced by the `ExprReduction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprReduction(ctx: LucidParser.ExprReductionContext)

    /**
     * Exit a parse tree produced by the `ExprReduction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprReduction(ctx: LucidParser.ExprReductionContext)

    /**
     * Enter a parse tree produced by the `ExprInvert` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprInvert(ctx: LucidParser.ExprInvertContext)

    /**
     * Exit a parse tree produced by the `ExprInvert` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprInvert(ctx: LucidParser.ExprInvertContext)

    /**
     * Enter a parse tree produced by the `ExprStruct` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprStruct(ctx: LucidParser.ExprStructContext)

    /**
     * Exit a parse tree produced by the `ExprStruct` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprStruct(ctx: LucidParser.ExprStructContext)

    /**
     * Enter a parse tree produced by the `ExprArray` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprArray(ctx: LucidParser.ExprArrayContext)

    /**
     * Exit a parse tree produced by the `ExprArray` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprArray(ctx: LucidParser.ExprArrayContext)

    /**
     * Enter a parse tree produced by the `ExprShift` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprShift(ctx: LucidParser.ExprShiftContext)

    /**
     * Exit a parse tree produced by the `ExprShift` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprShift(ctx: LucidParser.ExprShiftContext)

    /**
     * Enter a parse tree produced by the `ExprAddSub` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext)

    /**
     * Exit a parse tree produced by the `ExprAddSub` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext)

    /**
     * Enter a parse tree produced by the `ExprLogical` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprLogical(ctx: LucidParser.ExprLogicalContext)

    /**
     * Exit a parse tree produced by the `ExprLogical` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprLogical(ctx: LucidParser.ExprLogicalContext)

    /**
     * Enter a parse tree produced by the `ExprNegate` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprNegate(ctx: LucidParser.ExprNegateContext)

    /**
     * Exit a parse tree produced by the `ExprNegate` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprNegate(ctx: LucidParser.ExprNegateContext)

    /**
     * Enter a parse tree produced by the `ExprGroup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprGroup(ctx: LucidParser.ExprGroupContext)

    /**
     * Exit a parse tree produced by the `ExprGroup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprGroup(ctx: LucidParser.ExprGroupContext)

    /**
     * Enter a parse tree produced by the `ExprBitwise` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext)

    /**
     * Exit a parse tree produced by the `ExprBitwise` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext)

    /**
     * Enter a parse tree produced by the `ExprFunction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprFunction(ctx: LucidParser.ExprFunctionContext)

    /**
     * Exit a parse tree produced by the `ExprFunction` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprFunction(ctx: LucidParser.ExprFunctionContext)

    /**
     * Enter a parse tree produced by the `ExprCompare` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprCompare(ctx: LucidParser.ExprCompareContext)

    /**
     * Exit a parse tree produced by the `ExprCompare` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprCompare(ctx: LucidParser.ExprCompareContext)

    /**
     * Enter a parse tree produced by the `ExprDup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprDup(ctx: LucidParser.ExprDupContext)

    /**
     * Exit a parse tree produced by the `ExprDup` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprDup(ctx: LucidParser.ExprDupContext)

    /**
     * Enter a parse tree produced by the `ExprMultDiv` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext)

    /**
     * Exit a parse tree produced by the `ExprMultDiv` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext)

    /**
     * Enter a parse tree produced by the `ExprSignal` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterExprSignal(ctx: LucidParser.ExprSignalContext)

    /**
     * Exit a parse tree produced by the `ExprSignal` labeled alternative in [LucidParser.expr].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitExprSignal(ctx: LucidParser.ExprSignalContext)

    /**
     * Enter a parse tree produced by [LucidParser.name].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterName(ctx: LucidParser.NameContext)

    /**
     * Exit a parse tree produced by [LucidParser.name].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitName(ctx: LucidParser.NameContext)

    /**
     * Enter a parse tree produced by [LucidParser.semi].
     *
     * @param ctx The parse tree
     */
    public suspend fun enterSemi(ctx: LucidParser.SemiContext)

    /**
     * Exit a parse tree produced by [LucidParser.semi].
     *
     * @param ctx The parse tree
     */
    public suspend fun exitSemi(ctx: LucidParser.SemiContext)

}