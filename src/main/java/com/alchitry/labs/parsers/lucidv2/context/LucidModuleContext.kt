package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.signals.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeListener

class LucidModuleContext(
    val project: ProjectContext,
    stage: ParseStage = ParseStage.Modules,
    val instance: ModuleInstance?,
    val evalContext: Evaluable? = null,
    val errorCollector: ErrorCollector = ErrorCollector(),
    expr: ExprParser? = null,
    bitSelection: BitSelectionParser? = null,
    signal: SignalParser? = null,
    global: GlobalParser? = null,
    module: ModuleParser? = null,
    alwaysParser: AlwaysParser? = null,
    alwaysEvaluator: AlwaysEvaluator? = null,
    signalDriver: SignalDriverParser? = null,
    val localSignalResolver: SignalResolver? = null // Used in tests to simulate a full parse.
) {
    val isInstantiated = instance != null

    var stage: ParseStage = stage

    val expr = expr?.withContext(this) ?: ExprParser(this)
    val bitSelection = bitSelection?.withContext(this) ?: BitSelectionParser(this)
    val signal = signal?.withContext(this) ?: SignalParser(this)
    val global = global?.withContext(this) ?: GlobalParser(this)
    val module = module?.withContext(this) ?: ModuleParser(this)
    val alwaysParser = alwaysParser?.withContext(this) ?: AlwaysParser(this)
    val alwaysEvaluator = alwaysEvaluator?.withContext(this) ?: AlwaysEvaluator(this)
    val signalDriver = signalDriver?.withContext(this) ?: SignalDriverParser(this)

    private fun getListeners() = when (stage) {
        ParseStage.Globals -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.global
        )

        ParseStage.Modules -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.module
        )

        ParseStage.ModuleInternals -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.alwaysParser,
            this.signalDriver
        )

        ParseStage.Drivers -> listOf<ParseTreeListener>(
            this.expr,
            this.bitSelection,
            this.signal,
            this.alwaysParser,
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
            this.signal,
            this.module,
            this.alwaysParser,
            this.signalDriver
        )
    }

    private fun getFilter(): WalkerFilter = when (stage) {
        ParseStage.Globals -> WalkerFilter.GlobalsOnly
        ParseStage.Modules -> WalkerFilter.join(WalkerFilter.SkipModuleBodies, WalkerFilter.SkipGlobals)
        ParseStage.ModuleInternals -> WalkerFilter.SkipGlobals
        ParseStage.Drivers -> WalkerFilter.SkipGlobals
        ParseStage.Evaluation -> WalkerFilter.join(WalkerFilter.SkipControlBlocks, WalkerFilter.SkipGlobals)
        ParseStage.ErrorCheck -> WalkerFilter.None
    }

    fun instantiate(name: String, parameters: Map<String, Signal>): LucidModuleContext {
        val moduleType = module.module ?: error("Tried to instantiate a module with a null module type!")

        val instance = ModuleInstance(name, moduleType, parameters)

        return LucidModuleContext(
            project,
            ParseStage.ModuleInternals,
            instance
        )
    }

    fun withEvalContext(evalContext: Evaluable) = LucidModuleContext(
        project,
        ParseStage.Evaluation,
        instance,
        evalContext,
        ErrorCollector(), // give each context its own error collector
        expr,
        bitSelection,
        signal,
        global,
        module,
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

    fun walk(t: ParseTree, vararg extraListeners: ParseTreeListener) {
        when (stage) {
            ParseStage.ModuleInternals -> checkNotNull(instance) { "ModuleInternals pass requires instantiation!" }
            ParseStage.Drivers -> checkNotNull(instance) { "Drivers pass requires instantiation!" }
            ParseStage.Evaluation -> checkNotNull(instance) { "Evaluation pass requires instantiation!" }
            else -> {}
        }

        return ParseTreeMultiWalker.walk(
            getListeners().toMutableList().apply { addAll(extraListeners.toList()) },
            t,
            getFilter()
        )
    }

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    fun resolveSignal(name: String): SignalOrParent? {
        localSignalResolver?.resolve(name)?.let { return it }
        signal.resolve(name)?.let { return it }

        return project.resolveSignal(name)
    }
}