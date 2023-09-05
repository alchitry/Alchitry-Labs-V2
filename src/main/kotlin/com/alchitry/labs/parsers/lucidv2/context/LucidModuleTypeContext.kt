package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.Evaluable
import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.grammar.LucidParser
import com.alchitry.labs.parsers.grammar.LucidParser.SourceContext
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.types.Constant
import com.alchitry.labs.parsers.lucidv2.types.EnumType
import com.alchitry.labs.parsers.lucidv2.types.Module

class LucidModuleTypeContext(
    override val project: ProjectContext,
    val errorCollector: ErrorCollector
) : LucidExprContext, ErrorListener by errorCollector {
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
    override fun resolveSignal(name: String): SignalOrParent? {
        module.resolve(name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String) = project.resolveGlobal(name)
}