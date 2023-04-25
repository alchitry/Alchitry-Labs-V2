package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.resolvers.Evaluable
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.antlr.v4.runtime.tree.ParseTreeWalker

/**
 * This class is designed to represent an expression whose value will change automatically whenever one of the
 * Signals it depends on changes.
 *
 * @param expr is the expression to use to evaluate new values
 * @param parseContext is the context of the original parse
 */
class DynamicExpr(
    val expr: ExprContext,
    private val parseContext: LucidParseContext
) : Evaluable {
    private val mutableValueFlow = SynchronizedSharedFlow<Value>()
    val valueFlow: Flow<Value> get() = mutableValueFlow.asFlow()

    private val collectingFlow = MutableStateFlow(false)

    var value: Value = parseContext.expr.resolve(expr) ?: error("Failed to resolve initial value for ${expr.text}")
        private set

    init {
        val dependencies =
            parseContext.expr.resolveDependencies(expr) ?: error("Failed to resolve dependencies for ${expr.text}")
        parseContext.scope.launch {
            onAnyChange(
                dependencies.map { it.valueFlow },
                onStarted = { collectingFlow.tryEmit(true) }
            ) {
                parseContext.queueEvaluation(this@DynamicExpr)
            }
        }
        runBlocking { collectingFlow.first { true } } // TODO: Look into a more elegant solution to waiting for collection to start
    }

    override suspend fun evaluate() {
        ParseTreeWalker.DEFAULT.walk(parseContext.expr, expr)

        val newValue = parseContext.expr.resolve(expr)

        if (parseContext.errorCollector.errors.isNotEmpty()) {
            parseContext.errorCollector.errors.forEach { println(it) }
            error("Failed to parse DynamicValue $expr")
        }

        requireNotNull(newValue) { "Failed to produce a value for DynamicValue $expr" }

        value = newValue
        mutableValueFlow.emit(newValue)
    }
}