package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.resolvers.Evaluable
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.values.Value
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * This class is designed to represent an expression whose value will change automatically whenever one of the
 * Signals it depends on changes.
 *
 * @param expr is the expression to use to evaluate new values
 * @param context is the context of the original parse
 */
class DynamicExpr(
    val expr: ExprContext,
    private val context: LucidParseContext
) : Evaluable {
    private val mutableValueFlow = SynchronizedSharedFlow<Value>()
    val valueFlow: Flow<Value> get() = mutableValueFlow.asFlow()

    private val collectingFlow = MutableStateFlow(false)

    var value: Value = context.expr.resolve(expr) ?: error("Failed to resolve initial value for ${expr.text}")
        private set

    init {
        val dependencies =
            context.expr.resolveDependencies(expr) ?: error("Failed to resolve dependencies for ${expr.text}")
        context.scope.launch {
            onAnyChange(
                dependencies.map { it.valueFlow },
                onStarted = { collectingFlow.tryEmit(true) }
            ) {
                context.queueEvaluation(this@DynamicExpr)
            }
        }
        context.addEvaluable(this)
    }

    /**
     * Suspends until the evaluator is collecting.
     */
    override suspend fun waitCollecting() {
        collectingFlow.first { true }
    }

    override suspend fun evaluate() {
        context.walk(expr)

        val newValue = context.expr.resolve(expr)

        if (context.errorCollector.errors.isNotEmpty()) {
            context.errorCollector.errors.forEach { println(it) }
            error("Failed to parse DynamicValue $expr")
        }

        requireNotNull(newValue) { "Failed to produce a value for DynamicValue $expr" }

        value = newValue
        mutableValueFlow.emit(newValue)
    }
}