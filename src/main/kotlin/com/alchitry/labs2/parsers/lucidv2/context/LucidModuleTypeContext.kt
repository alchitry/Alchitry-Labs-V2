package com.alchitry.labs2.parsers.lucidv2.context

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ParseTreeMultiWalker
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.grammar.LucidParser
import com.alchitry.labs2.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs2.parsers.lucidv2.parsers.*
import com.alchitry.labs2.parsers.lucidv2.types.Constant
import com.alchitry.labs2.parsers.lucidv2.types.EnumType
import com.alchitry.labs2.parsers.lucidv2.types.Module
import com.alchitry.labs2.parsers.lucidv2.types.SignalOrParent
import com.alchitry.labs2.parsers.notations.ErrorListener
import com.alchitry.labs2.project.files.SourceFile
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.RuleContext

class LucidModuleTypeContext(
    override val project: ProjectContext,
    override val sourceFile: SourceFile
) : LucidExprContext, ErrorListener by project.notationManager.getCollector(sourceFile) {
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

    suspend fun extract(t: SourceContext): Module? {
        ParseTreeMultiWalker.walk(
            listeners,
            t,
            WalkerFilter.join(WalkerFilter.SkipModuleBodies, WalkerFilter.SkipGlobals)
        )
        return module.module
    }

    override fun resolve(exprCtx: LucidParser.ExprContext) = expr.resolve(exprCtx)

    override fun resolve(signalCtx: LucidParser.SignalContext) = signal.resolve(signalCtx)
    override fun resolve(signalWidthContext: LucidParser.SignalWidthContext) = signal.resolve(signalWidthContext)
    override fun resolve(structTypeContext: LucidParser.StructTypeContext) = struct.resolve(structTypeContext)
    override fun resolve(structDecContext: LucidParser.StructDecContext) = struct.resolve(structDecContext)
    override fun resolve(enumDecContext: LucidParser.EnumDecContext): EnumType? = null
    override fun resolve(constDecContext: LucidParser.ConstDecContext): Constant? = null

    override fun resolve(bitSelectionContext: LucidParser.BitSelectionContext) =
        bitSelection.resolve(bitSelectionContext)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    override fun resolveSignal(ctx: ParserRuleContext, name: String): SignalOrParent? {
        module.resolve(ctx, name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String) = project.resolveGlobal(name)
    override fun inDeadBlock(ctx: RuleContext): Boolean = expr.inDeadBlock(ctx)
}