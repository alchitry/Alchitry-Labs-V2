package com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.parsers.ExprParser
import com.alchitry.labs.parsers.lucidv2.parsers.ParseTreeMultiWalker
import com.alchitry.labs.parsers.lucidv2.parsers.SignalParser
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.antlr.v4.runtime.tree.ParseTree

interface Evaluable {
    suspend fun evaluate()
    suspend fun waitCollecting()
}

class LucidParseContext(val errorCollector: ErrorCollector) {
    val expr = ExprParser(this)
    val signal = SignalParser(this)
    //val struct = StructResolver(er)
    /** Used in tests to simulate a full parse. */
    var testingSignalResolver: SignalResolver? = null

    val scope = CoroutineScope(Dispatchers.Default)
    private val evaluationQueue: MutableSet<Evaluable> = LinkedHashSet()
    private val queueLock = Mutex()

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

    suspend fun queueEvaluation(evaluable: Evaluable) {
        queueLock.withLock {
            evaluationQueue.add(evaluable)
        }
    }

    suspend fun processQueue() {
        repeat(1000) {
            val items = queueLock.withLock {
                if (evaluationQueue.isEmpty())
                    return

                evaluationQueue.toList().also {
                    evaluationQueue.clear()
                }
            }
            coroutineScope {
                items.forEach {
                    launch(Dispatchers.Default) {
                        it.evaluate()
                    }
                }
            }
        }
        error("Failed to clear the queue after 1000 iterations. There is likely a dependency loop.")
    }
}