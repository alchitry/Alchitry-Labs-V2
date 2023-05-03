package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.parsers.ExprParser
import com.alchitry.labs.parsers.lucidv2.parsers.ParseTreeMultiWalker
import com.alchitry.labs.parsers.lucidv2.parsers.SignalParser
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import org.antlr.v4.runtime.tree.ParseTree

class LucidModuleContext(val project: ProjectContext) {
    val errorCollector = ErrorCollector()
    val expr = ExprParser(this)
    val signal = SignalParser(this)
    //val struct = StructResolver(er)
    /** Used in tests to simulate a full parse. */
    var testingSignalResolver: SignalResolver? = null

    private val listeners = listOf(
        expr,
        signal
    )

    private val evaluables = mutableListOf<Evaluable>()
    fun addEvaluable(evaluable: Evaluable) = evaluables.add(evaluable)
    suspend fun waitCollecting() { evaluables.forEach { it.waitCollecting() }}

    fun walk(t: ParseTree) = ParseTreeMultiWalker.walk(listeners, t)

    fun addToParser(parser: LucidParser) {
        listeners.forEach { parser.addParseListener(it) }
    }

    /**
     * Searches all SignalParsers to resolve a signal name.
     */
    fun resolveSignal(name: String): SignalOrParent? {
        testingSignalResolver?.resolve(name)?.let { return it }
        signal.resolve(name)?.let { return it }

        return null
    }
}