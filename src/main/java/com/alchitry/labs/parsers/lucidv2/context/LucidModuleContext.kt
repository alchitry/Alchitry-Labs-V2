package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
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
    signal: SignalParser? = null,
    module: LucidModuleParser? = null,
    always: AlwaysParser? = null,
    val localSignalResolver: SignalResolver? = null // Used in tests to simulate a full parse.
) {
    val expr = expr?.withContext(this) ?: ExprParser(this)
    val signal = signal?.withContext(this) ?: SignalParser(this)
    val module = module?.withContext(this) ?: LucidModuleParser(this)
    val always = always?.withContext(this) ?: AlwaysParser(this)

    private val listeners = listOf<ParseTreeListener>(
        this.expr,
        this.signal,
        this.module,
        this.always
    )

    fun withEvalContext(evalContext: Evaluable) = LucidModuleContext(
        project,
        instance,
        evalContext,
        ErrorCollector(), // give each context its own error collector
        expr,
        signal,
        module,
        always,
        localSignalResolver
    )

    fun walk(t: ParseTree, vararg extraListeners: ParseTreeListener) =
        ParseTreeMultiWalker.walk(listeners.toMutableList().apply { addAll(extraListeners.toList()) }, t)

    fun addToParser(parser: LucidParser) {
        listeners.forEach { parser.addParseListener(it) }
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