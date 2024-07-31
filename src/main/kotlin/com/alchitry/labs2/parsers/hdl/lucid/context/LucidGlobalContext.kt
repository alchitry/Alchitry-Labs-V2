package com.alchitry.labs2.parsers.hdl.lucid.context

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.hdl.lucid.parsers.*
import com.alchitry.labs2.parsers.hdl.types.GlobalNamespace
import com.alchitry.labs2.parsers.hdl.types.SignalOrParent
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.RuleContext

class LucidGlobalContext(
    override val project: ProjectContext,
    override val sourceFile: SourceFile
) : LucidExprContext, ErrorListener by project.notationManager.getCollector(sourceFile) {
    override val evalContext: Evaluable? = null

    private val expr = ExprParser(this)
    private val bitSelection = BitSelectionParser(this)
    private val struct = StructParser(this)
    private val signal = SignalParser(this)
    private val global = GlobalParser(this)
    private val enum = EnumParser(this)
    private val constant = ConstantParser(this)

    private val listeners = listOf(
        this.expr,
        this.bitSelection,
        this.struct,
        this.enum,
        this.constant,
        this.signal,
        this.global
    )

    suspend fun walk(t: ParserRuleContext) = ParseTreeMultiWalker.walk(listeners, t, LucidWalkerFilters.GlobalsOnly)

    override fun resolve(exprCtx: LucidParser.ExprContext) = expr.resolve(exprCtx)

    override fun resolve(signalCtx: LucidParser.SignalContext) = signal.resolve(signalCtx)
    override fun resolve(signalWidthContext: LucidParser.SignalWidthContext) = signal.resolve(signalWidthContext)
    override fun resolve(structTypeContext: LucidParser.StructTypeContext) = struct.resolve(structTypeContext)
    override fun resolve(structDecContext: LucidParser.StructDecContext) = struct.resolve(structDecContext)
    override fun resolve(bitSelectionContext: LucidParser.BitSelectionContext) =
        bitSelection.resolve(bitSelectionContext)

    override fun resolve(enumDecContext: LucidParser.EnumDecContext) = enum.resolve(enumDecContext)
    override fun resolve(constDecContext: LucidParser.ConstDecContext) = constant.resolve(constDecContext)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    override fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent? {
        constant.resolve(ctx, name)?.let { return it }
        enum.resolve(ctx, name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String): GlobalNamespace? = global.resolveGlobal(name)
    override fun inDeadBlock(ctx: RuleContext): Boolean = expr.inDeadBlock(ctx)
}