package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.signals.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeListener

class LucidModuleContext(
    val project: ProjectContext,
    val instance: ModuleInstance?,
    val evalContext: Evaluable? = null,
    val errorCollector: ErrorCollector = ErrorCollector(),
    expr: ExprParser? = null,
    bitSelection: BitSelectionParser? = null,
    signal: SignalParser? = null,
    module: LucidModuleParser? = null,
    alwaysParser: AlwaysParser? = null,
    alwaysEvaluator: AlwaysEvaluator? = null,
    signalDriver: SignalDriverParser? = null,
    val localSignalResolver: SignalResolver? = null // Used in tests to simulate a full parse.
) {
    val isEvaluating = evalContext != null

    val expr = expr?.withContext(this) ?: ExprParser(this)
    val bitSelection = bitSelection?.withContext(this) ?: BitSelectionParser(this)
    val signal = signal?.withContext(this) ?: SignalParser(this)
    val module = module?.withContext(this) ?: LucidModuleParser(this)
    val alwaysParser = alwaysParser?.withContext(this) ?: AlwaysParser(this)
    val alwaysEvaluator = alwaysEvaluator?.withContext(this) ?: AlwaysEvaluator(this)
    val signalDriver = signalDriver?.withContext(this) ?: SignalDriverParser(this)

    private val parseListeners = listOf<ParseTreeListener>(
        this.expr,
        this.bitSelection,
        this.signal,
        this.module,
        this.alwaysParser,
        this.signalDriver
    )

    private val evalListeners = listOf<ParseTreeListener>(
        this.expr,
        this.bitSelection,
        this.signal,
        this.alwaysEvaluator
    )

    fun withEvalContext(evalContext: Evaluable) = LucidModuleContext(
        project,
        instance,
        evalContext,
        ErrorCollector(), // give each context its own error collector
        expr,
        bitSelection,
        signal,
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

    fun walk(t: ParseTree, vararg extraListeners: ParseTreeListener) =
        ParseTreeMultiWalker.walk(parseListeners.toMutableList().apply { addAll(extraListeners.toList()) }, t)

    fun evalWalk(t: ParseTree) = ParseTreeMultiWalker.walk(evalListeners, t, WalkerFilter.SkipControlBlocks)

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    fun resolveSignal(name: String): SignalOrParent? {
        localSignalResolver?.resolve(name)?.let { return it }
        signal.resolve(name)?.let { return it }

        return project.resolveSignal(name)
    }
}