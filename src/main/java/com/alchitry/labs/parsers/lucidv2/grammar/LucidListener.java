// Generated from /home/justin/IdeaProjects/LucidParserV2/src/main/java/com/alchitry/labs/parsers/lucidv2/grammar/Lucid.g4 by ANTLR 4.12.0

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
	 * Enter a parse tree produced by {@link LucidParser#globalStat}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStat(LucidParser.GlobalStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#globalStat}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStat(LucidParser.GlobalStatContext ctx);
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
	 * Enter a parse tree produced by {@link LucidParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(LucidParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(LucidParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#portList}.
	 * @param ctx the parse tree
	 */
	void enterPortList(LucidParser.PortListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#portList}.
	 * @param ctx the parse tree
	 */
	void exitPortList(LucidParser.PortListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramDec}.
	 * @param ctx the parse tree
	 */
	void enterParamDec(LucidParser.ParamDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramDec}.
	 * @param ctx the parse tree
	 */
	void exitParamDec(LucidParser.ParamDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramDefault}.
	 * @param ctx the parse tree
	 */
	void enterParamDefault(LucidParser.ParamDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramDefault}.
	 * @param ctx the parse tree
	 */
	void exitParamDefault(LucidParser.ParamDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramConstraint}.
	 * @param ctx the parse tree
	 */
	void enterParamConstraint(LucidParser.ParamConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramConstraint}.
	 * @param ctx the parse tree
	 */
	void exitParamConstraint(LucidParser.ParamConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 */
	void enterPortDec(LucidParser.PortDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#portDec}.
	 * @param ctx the parse tree
	 */
	void exitPortDec(LucidParser.PortDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 */
	void enterPortDirection(LucidParser.PortDirectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#portDirection}.
	 * @param ctx the parse tree
	 */
	void exitPortDirection(LucidParser.PortDirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 */
	void enterSignalWidth(LucidParser.SignalWidthContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#signalWidth}.
	 * @param ctx the parse tree
	 */
	void exitSignalWidth(LucidParser.SignalWidthContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 */
	void enterArraySize(LucidParser.ArraySizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#arraySize}.
	 * @param ctx the parse tree
	 */
	void exitArraySize(LucidParser.ArraySizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 */
	void enterStructType(LucidParser.StructTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#structType}.
	 * @param ctx the parse tree
	 */
	void exitStructType(LucidParser.StructTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 */
	void enterStructMemberConst(LucidParser.StructMemberConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#structMemberConst}.
	 * @param ctx the parse tree
	 */
	void exitStructMemberConst(LucidParser.StructMemberConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 */
	void enterStructConst(LucidParser.StructConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#structConst}.
	 * @param ctx the parse tree
	 */
	void exitStructConst(LucidParser.StructConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 */
	void enterModuleBody(LucidParser.ModuleBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#moduleBody}.
	 * @param ctx the parse tree
	 */
	void exitModuleBody(LucidParser.ModuleBodyContext ctx);
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
	 * Enter a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStatEnum(LucidParser.StatEnumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StatEnum}
	 * labeled alternative in {@link LucidParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStatEnum(LucidParser.StatEnumContext ctx);
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
	 * Enter a parse tree produced by {@link LucidParser#constDec}.
	 * @param ctx the parse tree
	 */
	void enterConstDec(LucidParser.ConstDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#constDec}.
	 * @param ctx the parse tree
	 */
	void exitConstDec(LucidParser.ConstDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#assignBlock}.
	 * @param ctx the parse tree
	 */
	void enterAssignBlock(LucidParser.AssignBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#assignBlock}.
	 * @param ctx the parse tree
	 */
	void exitAssignBlock(LucidParser.AssignBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#sigCon}.
	 * @param ctx the parse tree
	 */
	void enterSigCon(LucidParser.SigConContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#sigCon}.
	 * @param ctx the parse tree
	 */
	void exitSigCon(LucidParser.SigConContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 */
	void enterParamCon(LucidParser.ParamConContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#paramCon}.
	 * @param ctx the parse tree
	 */
	void exitParamCon(LucidParser.ParamConContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 */
	void enterSigDec(LucidParser.SigDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#sigDec}.
	 * @param ctx the parse tree
	 */
	void exitSigDec(LucidParser.SigDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 */
	void enterDffDec(LucidParser.DffDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#dffDec}.
	 * @param ctx the parse tree
	 */
	void exitDffDec(LucidParser.DffDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 */
	void enterEnumDec(LucidParser.EnumDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#enumDec}.
	 * @param ctx the parse tree
	 */
	void exitEnumDec(LucidParser.EnumDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	void enterModuleInst(LucidParser.ModuleInstContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#moduleInst}.
	 * @param ctx the parse tree
	 */
	void exitModuleInst(LucidParser.ModuleInstContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#instCons}.
	 * @param ctx the parse tree
	 */
	void enterInstCons(LucidParser.InstConsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#instCons}.
	 * @param ctx the parse tree
	 */
	void exitInstCons(LucidParser.InstConsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#conList}.
	 * @param ctx the parse tree
	 */
	void enterConList(LucidParser.ConListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#conList}.
	 * @param ctx the parse tree
	 */
	void exitConList(LucidParser.ConListContext ctx);
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
	 * Enter a parse tree produced by {@link LucidParser#structMember}.
	 * @param ctx the parse tree
	 */
	void enterStructMember(LucidParser.StructMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#structMember}.
	 * @param ctx the parse tree
	 */
	void exitStructMember(LucidParser.StructMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#structDec}.
	 * @param ctx the parse tree
	 */
	void enterStructDec(LucidParser.StructDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#structDec}.
	 * @param ctx the parse tree
	 */
	void exitStructDec(LucidParser.StructDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#alwaysBlock}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysBlock(LucidParser.AlwaysBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#alwaysBlock}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysBlock(LucidParser.AlwaysBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysAssign}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysAssign(LucidParser.AlwaysAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysAssign}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysAssign(LucidParser.AlwaysAssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysCase(LucidParser.AlwaysCaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysCase}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysCase(LucidParser.AlwaysCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysIf(LucidParser.AlwaysIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysIf}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void exitAlwaysIf(LucidParser.AlwaysIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
	 * @param ctx the parse tree
	 */
	void enterAlwaysRepeat(LucidParser.AlwaysRepeatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AlwaysRepeat}
	 * labeled alternative in {@link LucidParser#alwaysStat}.
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
	 * Enter a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 */
	void enterAssignStat(LucidParser.AssignStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#assignStat}.
	 * @param ctx the parse tree
	 */
	void exitAssignStat(LucidParser.AssignStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndex(LucidParser.ArrayIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#arrayIndex}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndex(LucidParser.ArrayIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	void enterBitSelectorConst(LucidParser.BitSelectorConstContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitSelectorConst}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	void exitBitSelectorConst(LucidParser.BitSelectorConstContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	void enterBitSelectorFixWidth(LucidParser.BitSelectorFixWidthContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitSelectorFixWidth}
	 * labeled alternative in {@link LucidParser#bitSelector}.
	 * @param ctx the parse tree
	 */
	void exitBitSelectorFixWidth(LucidParser.BitSelectorFixWidthContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 */
	void enterBitSelection(LucidParser.BitSelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#bitSelection}.
	 * @param ctx the parse tree
	 */
	void exitBitSelection(LucidParser.BitSelectionContext ctx);
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
	 * Enter a parse tree produced by {@link LucidParser#caseStat}.
	 * @param ctx the parse tree
	 */
	void enterCaseStat(LucidParser.CaseStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseStat}.
	 * @param ctx the parse tree
	 */
	void exitCaseStat(LucidParser.CaseStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseElem}.
	 * @param ctx the parse tree
	 */
	void enterCaseElem(LucidParser.CaseElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseElem}.
	 * @param ctx the parse tree
	 */
	void exitCaseElem(LucidParser.CaseElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#caseBlock}.
	 * @param ctx the parse tree
	 */
	void enterCaseBlock(LucidParser.CaseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#caseBlock}.
	 * @param ctx the parse tree
	 */
	void exitCaseBlock(LucidParser.CaseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LucidParser#ifStat}.
	 * @param ctx the parse tree
	 */
	void enterIfStat(LucidParser.IfStatContext ctx);
	/**
	 * Exit a parse tree produced by {@link LucidParser#ifStat}.
	 * @param ctx the parse tree
	 */
	void exitIfStat(LucidParser.IfStatContext ctx);
	/**
     * Enter a parse tree produced by {@link LucidParser#elseStat}.
     * @param ctx the parse tree
     */
    void enterElseStat(LucidParser.ElseStatContext ctx);

    /**
     * Exit a parse tree produced by {@link LucidParser#elseStat}.
     *
     * @param ctx the parse tree
     */
    void exitElseStat(LucidParser.ElseStatContext ctx);

    /**
     * Enter a parse tree produced by {@link LucidParser#repeatStat}.
     *
     * @param ctx the parse tree
     */
    void enterRepeatStat(LucidParser.RepeatStatContext ctx);

    /**
     * Exit a parse tree produced by {@link LucidParser#repeatStat}.
     *
     * @param ctx the parse tree
     */
    void exitRepeatStat(LucidParser.RepeatStatContext ctx);

    /**
     * Enter a parse tree produced by {@link LucidParser#repeatBlock}.
     *
     * @param ctx the parse tree
     */
    void enterRepeatBlock(LucidParser.RepeatBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link LucidParser#repeatBlock}.
     *
     * @param ctx the parse tree
     */
    void exitRepeatBlock(LucidParser.RepeatBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link LucidParser#function}.
     *
     * @param ctx the parse tree
     */
    void enterFunction(LucidParser.FunctionContext ctx);

    /**
     * Exit a parse tree produced by {@link LucidParser#function}.
     *
     * @param ctx the parse tree
     */
    void exitFunction(LucidParser.FunctionContext ctx);

    /**
     * Enter a parse tree produced by {@link LucidParser#number}.
     *
     * @param ctx the parse tree
     */
    void enterNumber(LucidParser.NumberContext ctx);

    /**
     * Exit a parse tree produced by {@link LucidParser#number}.
     *
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