// Generated from Lucid.g4 by ANTLR 4.13.1
package com.alchitry.labs2.parsers.grammar

import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.tree.ErrorNode
import org.antlr.v4.kotlinruntime.tree.TerminalNode

/**
 * This class provides an empty implementation of [LucidListener],
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public open class LucidBaseListener : LucidListener {
    /**
     * The default implementation does nothing.
     */
    override fun enterSource(ctx: LucidParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSource(ctx: LucidParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterGlobal(ctx: LucidParser.GlobalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitGlobal(ctx: LucidParser.GlobalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterGlobalStat(ctx: LucidParser.GlobalStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitGlobalStat(ctx: LucidParser.GlobalStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterModule(ctx: LucidParser.ModuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitModule(ctx: LucidParser.ModuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterTestBench(ctx: LucidParser.TestBenchContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitTestBench(ctx: LucidParser.TestBenchContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterParamList(ctx: LucidParser.ParamListContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitParamList(ctx: LucidParser.ParamListContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterPortList(ctx: LucidParser.PortListContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitPortList(ctx: LucidParser.PortListContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterParamDec(ctx: LucidParser.ParamDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitParamDec(ctx: LucidParser.ParamDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterParamDefault(ctx: LucidParser.ParamDefaultContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitParamDefault(ctx: LucidParser.ParamDefaultContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterPortDec(ctx: LucidParser.PortDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitPortDec(ctx: LucidParser.PortDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterPortDirection(ctx: LucidParser.PortDirectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitPortDirection(ctx: LucidParser.PortDirectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterSignalWidth(ctx: LucidParser.SignalWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSignalWidth(ctx: LucidParser.SignalWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterArraySize(ctx: LucidParser.ArraySizeContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitArraySize(ctx: LucidParser.ArraySizeContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStructType(ctx: LucidParser.StructTypeContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStructType(ctx: LucidParser.StructTypeContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStructConst(ctx: LucidParser.StructConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStructConst(ctx: LucidParser.StructConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterModuleBody(ctx: LucidParser.ModuleBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitModuleBody(ctx: LucidParser.ModuleBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatConst(ctx: LucidParser.StatConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatConst(ctx: LucidParser.StatConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatSig(ctx: LucidParser.StatSigContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatSig(ctx: LucidParser.StatSigContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatEnum(ctx: LucidParser.StatEnumContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatEnum(ctx: LucidParser.StatEnumContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatDFF(ctx: LucidParser.StatDFFContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatDFF(ctx: LucidParser.StatDFFContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatAssign(ctx: LucidParser.StatAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatAssign(ctx: LucidParser.StatAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatAlways(ctx: LucidParser.StatAlwaysContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatAlways(ctx: LucidParser.StatAlwaysContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatStruct(ctx: LucidParser.StatStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatStruct(ctx: LucidParser.StatStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatTest(ctx: LucidParser.StatTestContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatTest(ctx: LucidParser.StatTestContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStatFunction(ctx: LucidParser.StatFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStatFunction(ctx: LucidParser.StatFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterConstDec(ctx: LucidParser.ConstDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitConstDec(ctx: LucidParser.ConstDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAssignBlock(ctx: LucidParser.AssignBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAssignBlock(ctx: LucidParser.AssignBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterSigCon(ctx: LucidParser.SigConContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSigCon(ctx: LucidParser.SigConContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterParamCon(ctx: LucidParser.ParamConContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitParamCon(ctx: LucidParser.ParamConContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterSigDec(ctx: LucidParser.SigDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSigDec(ctx: LucidParser.SigDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterDffDec(ctx: LucidParser.DffDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitDffDec(ctx: LucidParser.DffDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterEnumDec(ctx: LucidParser.EnumDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitEnumDec(ctx: LucidParser.EnumDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterModuleInst(ctx: LucidParser.ModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitModuleInst(ctx: LucidParser.ModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterInstCons(ctx: LucidParser.InstConsContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitInstCons(ctx: LucidParser.InstConsContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterConList(ctx: LucidParser.ConListContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitConList(ctx: LucidParser.ConListContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterConnection(ctx: LucidParser.ConnectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitConnection(ctx: LucidParser.ConnectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStructMember(ctx: LucidParser.StructMemberContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStructMember(ctx: LucidParser.StructMemberContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterStructDec(ctx: LucidParser.StructDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitStructDec(ctx: LucidParser.StructDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterFunctionArg(ctx: LucidParser.FunctionArgContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitFunctionArg(ctx: LucidParser.FunctionArgContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterTestBlock(ctx: LucidParser.TestBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitTestBlock(ctx: LucidParser.TestBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterBlock(ctx: LucidParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitBlock(ctx: LucidParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterAssignStat(ctx: LucidParser.AssignStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitAssignStat(ctx: LucidParser.AssignStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterBitSelection(ctx: LucidParser.BitSelectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitBitSelection(ctx: LucidParser.BitSelectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterSignal(ctx: LucidParser.SignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSignal(ctx: LucidParser.SignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterCaseStat(ctx: LucidParser.CaseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitCaseStat(ctx: LucidParser.CaseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterCaseElem(ctx: LucidParser.CaseElemContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitCaseElem(ctx: LucidParser.CaseElemContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterCaseBlock(ctx: LucidParser.CaseBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitCaseBlock(ctx: LucidParser.CaseBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterCaseJunk(ctx: LucidParser.CaseJunkContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitCaseJunk(ctx: LucidParser.CaseJunkContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterIfStat(ctx: LucidParser.IfStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitIfStat(ctx: LucidParser.IfStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterElseStat(ctx: LucidParser.ElseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitElseStat(ctx: LucidParser.ElseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterRepeatStat(ctx: LucidParser.RepeatStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterFunction(ctx: LucidParser.FunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitFunction(ctx: LucidParser.FunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterNumber(ctx: LucidParser.NumberContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitNumber(ctx: LucidParser.NumberContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprTernary(ctx: LucidParser.ExprTernaryContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprTernary(ctx: LucidParser.ExprTernaryContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprNum(ctx: LucidParser.ExprNumContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprNum(ctx: LucidParser.ExprNumContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprConcat(ctx: LucidParser.ExprConcatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprConcat(ctx: LucidParser.ExprConcatContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprReduction(ctx: LucidParser.ExprReductionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprReduction(ctx: LucidParser.ExprReductionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprInvert(ctx: LucidParser.ExprInvertContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprInvert(ctx: LucidParser.ExprInvertContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprStruct(ctx: LucidParser.ExprStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprStruct(ctx: LucidParser.ExprStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprArray(ctx: LucidParser.ExprArrayContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprArray(ctx: LucidParser.ExprArrayContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprShift(ctx: LucidParser.ExprShiftContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprShift(ctx: LucidParser.ExprShiftContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprLogical(ctx: LucidParser.ExprLogicalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprLogical(ctx: LucidParser.ExprLogicalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprNegate(ctx: LucidParser.ExprNegateContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprNegate(ctx: LucidParser.ExprNegateContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprGroup(ctx: LucidParser.ExprGroupContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprGroup(ctx: LucidParser.ExprGroupContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprFunction(ctx: LucidParser.ExprFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprFunction(ctx: LucidParser.ExprFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprCompare(ctx: LucidParser.ExprCompareContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprCompare(ctx: LucidParser.ExprCompareContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprDup(ctx: LucidParser.ExprDupContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprDup(ctx: LucidParser.ExprDupContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterExprSignal(ctx: LucidParser.ExprSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitExprSignal(ctx: LucidParser.ExprSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterName(ctx: LucidParser.NameContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitName(ctx: LucidParser.NameContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterSemi(ctx: LucidParser.SemiContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitSemi(ctx: LucidParser.SemiContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun enterEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun exitEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override fun visitTerminal(node: TerminalNode) {}

    /**
     * The default implementation does nothing.
     */
    override fun visitErrorNode(node: ErrorNode) {}
}

/**
 * This class provides an empty implementation of Suspend[LucidListener],
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public open class SuspendLucidBaseListener : SuspendLucidListener {
    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSource(ctx: LucidParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSource(ctx: LucidParser.SourceContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterGlobal(ctx: LucidParser.GlobalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitGlobal(ctx: LucidParser.GlobalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterGlobalStat(ctx: LucidParser.GlobalStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitGlobalStat(ctx: LucidParser.GlobalStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterModule(ctx: LucidParser.ModuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitModule(ctx: LucidParser.ModuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterTestBench(ctx: LucidParser.TestBenchContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitTestBench(ctx: LucidParser.TestBenchContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterParamList(ctx: LucidParser.ParamListContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitParamList(ctx: LucidParser.ParamListContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterPortList(ctx: LucidParser.PortListContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitPortList(ctx: LucidParser.PortListContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterParamDec(ctx: LucidParser.ParamDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitParamDec(ctx: LucidParser.ParamDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterParamDefault(ctx: LucidParser.ParamDefaultContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitParamDefault(ctx: LucidParser.ParamDefaultContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterParamConstraint(ctx: LucidParser.ParamConstraintContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitParamConstraint(ctx: LucidParser.ParamConstraintContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterPortDec(ctx: LucidParser.PortDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitPortDec(ctx: LucidParser.PortDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterPortDirection(ctx: LucidParser.PortDirectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitPortDirection(ctx: LucidParser.PortDirectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSignalWidth(ctx: LucidParser.SignalWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSignalWidth(ctx: LucidParser.SignalWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterArraySize(ctx: LucidParser.ArraySizeContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitArraySize(ctx: LucidParser.ArraySizeContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStructType(ctx: LucidParser.StructTypeContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStructType(ctx: LucidParser.StructTypeContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStructMemberConst(ctx: LucidParser.StructMemberConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStructMemberConst(ctx: LucidParser.StructMemberConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStructConst(ctx: LucidParser.StructConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStructConst(ctx: LucidParser.StructConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterModuleBody(ctx: LucidParser.ModuleBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitModuleBody(ctx: LucidParser.ModuleBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatConst(ctx: LucidParser.StatConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatConst(ctx: LucidParser.StatConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatSig(ctx: LucidParser.StatSigContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatSig(ctx: LucidParser.StatSigContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatEnum(ctx: LucidParser.StatEnumContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatEnum(ctx: LucidParser.StatEnumContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatDFF(ctx: LucidParser.StatDFFContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatDFF(ctx: LucidParser.StatDFFContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatModuleInst(ctx: LucidParser.StatModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatModuleInst(ctx: LucidParser.StatModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatAssign(ctx: LucidParser.StatAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatAssign(ctx: LucidParser.StatAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatAlways(ctx: LucidParser.StatAlwaysContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatAlways(ctx: LucidParser.StatAlwaysContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatStruct(ctx: LucidParser.StatStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatStruct(ctx: LucidParser.StatStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatTest(ctx: LucidParser.StatTestContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatTest(ctx: LucidParser.StatTestContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStatFunction(ctx: LucidParser.StatFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStatFunction(ctx: LucidParser.StatFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterConstDec(ctx: LucidParser.ConstDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitConstDec(ctx: LucidParser.ConstDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAssignBlock(ctx: LucidParser.AssignBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAssignBlock(ctx: LucidParser.AssignBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSigCon(ctx: LucidParser.SigConContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSigCon(ctx: LucidParser.SigConContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterParamCon(ctx: LucidParser.ParamConContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitParamCon(ctx: LucidParser.ParamConContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSigDec(ctx: LucidParser.SigDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSigDec(ctx: LucidParser.SigDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterDffDec(ctx: LucidParser.DffDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitDffDec(ctx: LucidParser.DffDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterEnumDec(ctx: LucidParser.EnumDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitEnumDec(ctx: LucidParser.EnumDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterModuleInst(ctx: LucidParser.ModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitModuleInst(ctx: LucidParser.ModuleInstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterInstCons(ctx: LucidParser.InstConsContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitInstCons(ctx: LucidParser.InstConsContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterConList(ctx: LucidParser.ConListContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitConList(ctx: LucidParser.ConListContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterConnection(ctx: LucidParser.ConnectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitConnection(ctx: LucidParser.ConnectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStructMember(ctx: LucidParser.StructMemberContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStructMember(ctx: LucidParser.StructMemberContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterStructDec(ctx: LucidParser.StructDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitStructDec(ctx: LucidParser.StructDecContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterFunctionArg(ctx: LucidParser.FunctionArgContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitFunctionArg(ctx: LucidParser.FunctionArgContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterFunctionBlock(ctx: LucidParser.FunctionBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitFunctionBlock(ctx: LucidParser.FunctionBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterFunctionBody(ctx: LucidParser.FunctionBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitFunctionBody(ctx: LucidParser.FunctionBodyContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterTestBlock(ctx: LucidParser.TestBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitTestBlock(ctx: LucidParser.TestBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysBlock(ctx: LucidParser.AlwaysBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysAssign(ctx: LucidParser.AlwaysAssignContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysCase(ctx: LucidParser.AlwaysCaseContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysCase(ctx: LucidParser.AlwaysCaseContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysIf(ctx: LucidParser.AlwaysIfContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysIf(ctx: LucidParser.AlwaysIfContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysRepeat(ctx: LucidParser.AlwaysRepeatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysFunction(ctx: LucidParser.AlwaysFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAlwaysSignal(ctx: LucidParser.AlwaysSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterBlock(ctx: LucidParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitBlock(ctx: LucidParser.BlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterAssignStat(ctx: LucidParser.AssignStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitAssignStat(ctx: LucidParser.AssignStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterArrayIndex(ctx: LucidParser.ArrayIndexContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitArrayIndex(ctx: LucidParser.ArrayIndexContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitBitSelectorConst(ctx: LucidParser.BitSelectorConstContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitBitSelectorFixWidth(ctx: LucidParser.BitSelectorFixWidthContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterBitSelection(ctx: LucidParser.BitSelectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitBitSelection(ctx: LucidParser.BitSelectionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSignal(ctx: LucidParser.SignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSignal(ctx: LucidParser.SignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterCaseStat(ctx: LucidParser.CaseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitCaseStat(ctx: LucidParser.CaseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterCaseElem(ctx: LucidParser.CaseElemContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitCaseElem(ctx: LucidParser.CaseElemContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterCaseBlock(ctx: LucidParser.CaseBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitCaseBlock(ctx: LucidParser.CaseBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterCaseJunk(ctx: LucidParser.CaseJunkContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitCaseJunk(ctx: LucidParser.CaseJunkContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterIfStat(ctx: LucidParser.IfStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitIfStat(ctx: LucidParser.IfStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterElseStat(ctx: LucidParser.ElseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitElseStat(ctx: LucidParser.ElseStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterRepeatStat(ctx: LucidParser.RepeatStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitRepeatStat(ctx: LucidParser.RepeatStatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterRepeatBlock(ctx: LucidParser.RepeatBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitRepeatBlock(ctx: LucidParser.RepeatBlockContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterFunction(ctx: LucidParser.FunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitFunction(ctx: LucidParser.FunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterFunctionExpr(ctx: LucidParser.FunctionExprContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitFunctionExpr(ctx: LucidParser.FunctionExprContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterNumber(ctx: LucidParser.NumberContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitNumber(ctx: LucidParser.NumberContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprTernary(ctx: LucidParser.ExprTernaryContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprTernary(ctx: LucidParser.ExprTernaryContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprNum(ctx: LucidParser.ExprNumContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprNum(ctx: LucidParser.ExprNumContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprConcat(ctx: LucidParser.ExprConcatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprConcat(ctx: LucidParser.ExprConcatContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprReduction(ctx: LucidParser.ExprReductionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprReduction(ctx: LucidParser.ExprReductionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprInvert(ctx: LucidParser.ExprInvertContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprInvert(ctx: LucidParser.ExprInvertContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprStruct(ctx: LucidParser.ExprStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprStruct(ctx: LucidParser.ExprStructContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprArray(ctx: LucidParser.ExprArrayContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprArray(ctx: LucidParser.ExprArrayContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprShift(ctx: LucidParser.ExprShiftContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprShift(ctx: LucidParser.ExprShiftContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprAddSub(ctx: LucidParser.ExprAddSubContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprAddSub(ctx: LucidParser.ExprAddSubContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprLogical(ctx: LucidParser.ExprLogicalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprLogical(ctx: LucidParser.ExprLogicalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprNegate(ctx: LucidParser.ExprNegateContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprNegate(ctx: LucidParser.ExprNegateContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprGroup(ctx: LucidParser.ExprGroupContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprGroup(ctx: LucidParser.ExprGroupContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprBitwise(ctx: LucidParser.ExprBitwiseContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprBitwise(ctx: LucidParser.ExprBitwiseContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprFunction(ctx: LucidParser.ExprFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprFunction(ctx: LucidParser.ExprFunctionContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprCompare(ctx: LucidParser.ExprCompareContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprCompare(ctx: LucidParser.ExprCompareContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprDup(ctx: LucidParser.ExprDupContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprDup(ctx: LucidParser.ExprDupContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprMultDiv(ctx: LucidParser.ExprMultDivContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprMultDiv(ctx: LucidParser.ExprMultDivContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterExprSignal(ctx: LucidParser.ExprSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitExprSignal(ctx: LucidParser.ExprSignalContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterName(ctx: LucidParser.NameContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitName(ctx: LucidParser.NameContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterSemi(ctx: LucidParser.SemiContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitSemi(ctx: LucidParser.SemiContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun enterEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun exitEveryRule(ctx: ParserRuleContext) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun visitTerminal(node: TerminalNode) {}

    /**
     * The default implementation does nothing.
     */
    override suspend fun visitErrorNode(node: ErrorNode) {}
}
