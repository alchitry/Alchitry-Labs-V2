package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.parsers.*
import com.alchitry.labs.parsers.lucidv2.signals.ModuleInstance
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import kotlinx.coroutines.sync.Mutex
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeListener

class LucidModuleContext(val project: ProjectContext, val instance: ModuleInstance?) {
    val errorCollector = ErrorCollector()
    val expr = ExprParser(this)
    val signal = SignalParser(this)
    val module = LucidModuleParser(this)
    val always = AlwaysParser(this)

    /**
     * This lock is used to ensure only one part of the module is evaluated at any time. By using a module wide
     * lock, we can still get parallelism between modules.
     *
     * This lock is needed to prevent the reading of signals as they are being updated inside an always block.
     *
     * It could be possible to remove the need for this if every always block instance was sandboxed with its own
     * LucidModuleContext.
     */
    private val moduleEvalLock = Mutex()

    /** Used in tests to simulate a full parse. */
    var localSignalResolver: SignalResolver? = null

    private val listeners = listOf<ParseTreeListener>(
        expr,
        signal,
        module,
        always
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

    suspend fun queueEvaluation(evaluable: Evaluable) {
        project.queueEvaluation(evaluable, moduleEvalLock)
    }
}