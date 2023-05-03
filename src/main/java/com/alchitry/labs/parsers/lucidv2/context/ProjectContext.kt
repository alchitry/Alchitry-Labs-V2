package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.signals.GlobalNamespace
import com.alchitry.labs.parsers.lucidv2.signals.Module
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class ProjectContext {
    val scope = CoroutineScope(Dispatchers.Default)
    private val errors = mutableListOf<String>()
    private val evaluationQueue: MutableSet<Evaluable> = LinkedHashSet()
    private val queueLock = Mutex()

    private val globals = mutableMapOf<String, GlobalNamespace>()
    private val modules = mutableMapOf<String, Module>()

    fun addGlobal(globalNamespace: GlobalNamespace): Boolean =
        globals.putIfAbsent(globalNamespace.name, globalNamespace) == null

    fun addModule(module: Module): Boolean =
        modules.putIfAbsent(module.name, module) == null

    fun parseFile(contents: String): LucidModuleContext {
        val parser = LucidParser(
            CommonTokenStream(
                LucidLexer(
                    CharStreams.fromString(contents)
                ).also { it.removeErrorListeners() })
        )

        val context = LucidModuleContext(this, null)

        (parser.tokenStream.tokenSource as LucidLexer).addErrorListener(context.errorCollector)
        parser.removeErrorListeners()
        parser.addErrorListener(context.errorCollector)

        context.addToParser(parser)

        parser.source()

        return context
    }

    /**
     * Searches all GlobalNamespaces to resolve a signal name.
     */
    fun resolveSignal(name: String): SignalOrParent? = globals[name]

    fun resolveGlobal(name: String) = globals[name]

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