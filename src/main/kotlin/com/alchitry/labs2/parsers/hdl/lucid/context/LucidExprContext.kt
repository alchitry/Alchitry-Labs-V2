package com.alchitry.labs2.parsers.hdl.lucid.context

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser.*
import com.alchitry.labs2.parsers.hdl.ExprEvaluatorContext
import com.alchitry.labs2.parsers.hdl.lucid.parsers.*
import com.alchitry.labs2.parsers.hdl.types.*
import com.alchitry.labs2.parsers.hdl.types.Function
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.parsers.notations.NotationCollector
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.RuleContext
import org.antlr.v4.kotlinruntime.tree.ParseTree

interface LucidExprContext : ErrorListener, ExprEvaluatorContext<ExprContext> {
    fun resolve(bitSelectionContext: BitSelectionContext): List<BitSelection>
    fun resolve(signalCtx: SignalContext): SignalOrSubSignal?
    fun resolve(signalWidthContext: SignalWidthContext): SignalWidth?
    fun resolve(structTypeContext: StructTypeContext): StructType?
    fun resolve(structDecContext: StructDecContext): StructType?
    fun resolve(enumDecContext: EnumDecContext): EnumType?
    fun resolve(constDecContext: ConstDecContext): Constant?
    fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent?
    fun resolveStruct(name: String): StructType?
    fun resolveGlobal(name: String): GlobalNamespace?
    fun resolveFunction(name: String): Function? = Function.builtIn.firstOrNull { it.label == name }
    fun inDeadBlock(ctx: RuleContext): Boolean
    val evalContext: Evaluable?
    val project: ProjectContext
    val sourceFile: SourceFile
}

class LucidExprEval(
    override val project: ProjectContext,
    override val sourceFile: SourceFile,
    val notationCollector: NotationCollector = project.notationManager.getCollector(sourceFile),
    val signalProvider: (name: String) -> SignalOrParent?
) : LucidExprContext, ErrorListener by notationCollector {
    override val evalContext: Evaluable? = null

    private val expr = ExprParser(this)
    private val bitSelection = BitSelectionParser(this)
    private val struct = StructParser(this)
    private val signal = SignalParser(this)
    private val module = ModuleParser(this)

    private val listeners = listOf(
        this.expr,
        this.bitSelection,
        this.struct,
        this.signal,
        this.module
    )

    suspend fun walk(t: ParseTree, ignoreSkip: Boolean = false) {
        ParseTreeMultiWalker.walk(
            listeners,
            t,
            WalkerFilter.None,
            ignoreSkip = ignoreSkip
        )
    }

    override fun resolve(exprCtx: ExprContext) = expr.resolve(exprCtx)

    override fun resolve(signalCtx: SignalContext) = signal.resolve(signalCtx)
    override fun resolve(signalWidthContext: SignalWidthContext) = signal.resolve(signalWidthContext)
    override fun resolve(structTypeContext: StructTypeContext) = struct.resolve(structTypeContext)
    override fun resolve(structDecContext: StructDecContext) = struct.resolve(structDecContext)
    override fun resolve(enumDecContext: EnumDecContext): EnumType? = null
    override fun resolve(constDecContext: ConstDecContext): Constant? = null

    override fun resolve(bitSelectionContext: BitSelectionContext) =
        bitSelection.resolve(bitSelectionContext)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    override fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent? {
        signalProvider(name)?.let { return it }
        module.resolve(ctx, name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String) = project.resolveGlobal(name)
    override fun inDeadBlock(ctx: RuleContext): Boolean = expr.inDeadBlock(ctx)
}
