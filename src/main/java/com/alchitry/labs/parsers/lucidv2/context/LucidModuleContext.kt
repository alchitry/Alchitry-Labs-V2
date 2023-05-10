package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.errors.ErrorListener
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.*
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.signals.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeListener

class LucidModuleContext(
    override val project: ProjectContext,
    var stage: ParseStage = ParseStage.ModuleInternals,
    val instance: ModuleInstance,
    override val evalContext: Evaluable? = null,
    val errorCollector: ErrorCollector = ErrorCollector(),
    expr: ExprParser? = null,
    bitSelection: BitSelectionParser? = null,
    types: TypesParser? = null,
    struct: StructParser? = null,
    signal: SignalParser? = null,
    alwaysParser: AlwaysParser? = null,
    alwaysEvaluator: AlwaysEvaluator? = null,
    signalDriver: SignalDriverParser? = null,
    val localSignalResolver: SignalResolver? = null // Used in tests to simulate a full parse.
) : LucidExprContext, ErrorListener by errorCollector {

    // These will be run multiple times during evaluation, so they need their context updated
    val expr = expr?.withContext(this) ?: ExprParser(this)
    val bitSelection = bitSelection?.withContext(this) ?: BitSelectionParser(this)
    val signal = signal?.withContext(this) ?: SignalParser(this)
    val alwaysEvaluator = alwaysEvaluator?.withContext(this) ?: AlwaysEvaluator(this)

    // These won't be re-run during evaluation, so they don't need their context updated
    val types = types ?: TypesParser(this)
    val struct = struct ?: StructParser(this)
    val alwaysParser = alwaysParser ?: AlwaysParser(this)
    val signalDriver = signalDriver ?: SignalDriverParser(this)

    private fun getListeners() = when (stage) {
        ParseStage.ModuleInternals -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.struct,
            this.types,
            this.alwaysParser,
            this.signal
        )

        ParseStage.Drivers -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.signalDriver
        )

        ParseStage.Evaluation -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.alwaysEvaluator
        )

        ParseStage.ErrorCheck -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.struct,
            this.signal,
            this.types,
            this.alwaysParser
        )
    }

    private fun getFilter(): WalkerFilter = when (stage) {
        ParseStage.ModuleInternals -> WalkerFilter.SkipGlobals
        ParseStage.Drivers -> WalkerFilter.SkipGlobals
        ParseStage.Evaluation -> WalkerFilter.join(WalkerFilter.SkipControlBlocks, WalkerFilter.SkipGlobals)
        ParseStage.ErrorCheck -> WalkerFilter.SkipGlobals
    }

    fun withEvalContext(evalContext: Evaluable) = LucidModuleContext(
        project,
        ParseStage.Evaluation,
        instance,
        evalContext,
        ErrorCollector(), // give each context its own error collector
        expr,
        bitSelection,
        types,
        struct,
        signal,
        alwaysParser,
        alwaysEvaluator,
        signalDriver,
        localSignalResolver
    )

    /**
     * Queues always blocks for an initial evaluation.
     */
    suspend fun queueEval() {
        alwaysParser.queueEval()
    }

    fun walk(t: ParseTree, vararg extraListeners: ParseTreeListener) =
        ParseTreeMultiWalker.walk(
            getListeners().toMutableList().apply { addAll(extraListeners.toList()) },
            t,
            getFilter()
        )


    override fun resolve(exprCtx: ExprContext) = expr.resolve(exprCtx)

    override fun resolve(signalCtx: SignalContext) = signal.resolve(signalCtx)
    override fun resolve(signalWidthContext: SignalWidthContext) = signal.resolve(signalWidthContext)
    override fun resolve(structTypeContext: StructTypeContext) = struct.resolve(structTypeContext)
    override fun resolve(structDecContext: StructDecContext) = struct.resolve(structDecContext)
    override fun resolve(bitSelectionContext: BitSelectionContext) = bitSelection.resolve(bitSelectionContext)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    override fun resolveSignal(name: String): SignalOrParent? {
        localSignalResolver?.resolve(name)?.let { return it }
        types.resolve(name)?.let { return it }

        instance.getInternalSignal(name)?.let { return it }

        return project.resolveSignal(name)
    }

    override fun resolveStruct(name: String) = struct.resolveStruct(name)

    override fun resolveGlobal(name: String) = project.resolveGlobal(name)
}