// Generated from /home/justin/IdeaProjects/LucidParserV2/src/main/java/com/alchitry/labs/parsers/lucidv2/grammar/Lucid.g4 by ANTLR 4.13.0

package com.alchitry.labs.parsers.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LucidParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
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
     * Visit a parse tree produced by {@link LucidParser#globalStat}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGlobalStat(LucidParser.GlobalStatContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#module}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitModule(LucidParser.ModuleContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#testBench}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTestBench(LucidParser.TestBenchContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#paramList}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParamList(LucidParser.ParamListContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#portList}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPortList(LucidParser.PortListContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#paramDec}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParamDec(LucidParser.ParamDecContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#paramDefault}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParamDefault(LucidParser.ParamDefaultContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#paramConstraint}.
     * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamConstraint(LucidParser.ParamConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortDec(LucidParser.PortDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortDirection(LucidParser.PortDirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignalWidth(LucidParser.SignalWidthContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArraySize(LucidParser.ArraySizeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructType(LucidParser.StructTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructMemberConst(LucidParser.StructMemberConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructConst(LucidParser.StructConstContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleBody(LucidParser.ModuleBodyContext ctx);
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
	 * Visit a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatEnum(LucidParser.StatEnumContext ctx);
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
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatAssign(LucidParser.StatAssignContext ctx);

    /**
     * Visit a parse tree produced by the {@code StatAlways}
     * labeled alternative in {@link LucidParser#stat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatAlways(LucidParser.StatAlwaysContext ctx);

    /**
     * Visit a parse tree produced by the {@code StatStruct}
     * labeled alternative in {@link LucidParser#stat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatStruct(LucidParser.StatStructContext ctx);

    /**
     * Visit a parse tree produced by the {@code StatTest}
     * labeled alternative in {@link LucidParser#stat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatTest(LucidParser.StatTestContext ctx);

    /**
     * Visit a parse tree produced by the {@code StatFunction}
     * labeled alternative in {@link LucidParser#stat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStatFunction(LucidParser.StatFunctionContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#constDec}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstDec(LucidParser.ConstDecContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#assignBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignBlock(LucidParser.AssignBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#sigCon}.
     * @param ctx the parse tree
     * @return the visitor result
	 */
	T visitSigCon(LucidParser.SigConContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamCon(LucidParser.ParamConContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigDec(LucidParser.SigDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDffDec(LucidParser.DffDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDec(LucidParser.EnumDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuleInst(LucidParser.ModuleInstContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#instCons}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInstCons(LucidParser.InstConsContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#conList}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConList(LucidParser.ConListContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#connection}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConnection(LucidParser.ConnectionContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#structMember}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStructMember(LucidParser.StructMemberContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#structDec}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStructDec(LucidParser.StructDecContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#functionArg}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionArg(LucidParser.FunctionArgContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#functionBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionBlock(LucidParser.FunctionBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#testBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTestBlock(LucidParser.TestBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#alwaysBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAlwaysBlock(LucidParser.AlwaysBlockContext ctx);

    /**
     * Visit a parse tree produced by the {@code AlwaysAssign}
     * labeled alternative in {@link LucidParser#alwaysStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAlwaysAssign(LucidParser.AlwaysAssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAlwaysCase(LucidParser.AlwaysCaseContext ctx);

    /**
     * Visit a parse tree produced by the {@code AlwaysIf}
     * labeled alternative in {@link LucidParser#alwaysStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAlwaysIf(LucidParser.AlwaysIfContext ctx);

    /**
     * Visit a parse tree produced by the {@code AlwaysRepeat}
     * labeled alternative in {@link LucidParser#alwaysStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAlwaysRepeat(LucidParser.AlwaysRepeatContext ctx);

    /**
     * Visit a parse tree produced by the {@code AlwaysFunction}
     * labeled alternative in {@link LucidParser#alwaysStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAlwaysFunction(LucidParser.AlwaysFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(LucidParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStat(LucidParser.AssignStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndex(LucidParser.ArrayIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitSelectorConst(LucidParser.BitSelectorConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitSelectorFixWidth(LucidParser.BitSelectorFixWidthContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitSelection(LucidParser.BitSelectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LucidParser#signal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSignal(LucidParser.SignalContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#caseStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCaseStat(LucidParser.CaseStatContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#caseElem}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCaseElem(LucidParser.CaseElemContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#caseBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCaseBlock(LucidParser.CaseBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#ifStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfStat(LucidParser.IfStatContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#elseStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitElseStat(LucidParser.ElseStatContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#repeatStat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRepeatStat(LucidParser.RepeatStatContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#repeatBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRepeatBlock(LucidParser.RepeatBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#function}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunction(LucidParser.FunctionContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#functionExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionExpr(LucidParser.FunctionExprContext ctx);

    /**
     * Visit a parse tree produced by {@link LucidParser#number}.
     *
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