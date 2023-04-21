package com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.parsers.BitSelectionParser
import com.alchitry.labs.parsers.lucidv2.parsers.ExprParser
import com.alchitry.labs.parsers.lucidv2.parsers.SignalParser
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LucidParseContext(val errorCollector: ErrorCollector) {
    val expr = ExprParser(errorCollector, this)
    val signal = SignalParser(errorCollector, this)
    //val struct = StructResolver(er)
    val scope = CoroutineScope(Dispatchers.Default)
    /** Used in tests to simulate a full parse. */
    var testingSignalResolver: SignalResolver? = null

    fun addToParser(parser: LucidParser) {
        parser.addParseListener(expr)
        parser.addParseListener(signal)
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