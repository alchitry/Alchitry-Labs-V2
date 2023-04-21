package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.onAnyChange
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.resolvers.Evaluable
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
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
): Evaluable {
    private val mutableValueFlow =
        MutableStateFlow(parseContext.expr.resolve(expr) ?: error("Failed to resolve initial value for ${expr.text}"))
    val valueFlow = mutableValueFlow.asStateFlow()

    var value
        get() = mutableValueFlow.value
        set(v) {
            mutableValueFlow.getAndUpdate {
                require(it.signalWidth == v.signalWidth) { "DynamicExpr size has changed?!" }
                v.resizeToMatch(it)
            }
        }

    init {
        val dependencies = parseContext.expr.resolveDependencies(expr) ?: error("Failed to resolve dependencies for ${expr.text}")
        parseContext.scope.launch {
            onAnyChange(dependencies.map { it.valueFlow }) {
                parseContext.queueEvaluation(this@DynamicExpr)
            }
        }
    }

    suspend fun collect(collector: FlowCollector<Value>): Nothing = mutableValueFlow.collect(collector)

    override fun evaluate() {
        ParseTreeWalker.DEFAULT.walk(parseContext.expr, expr)

        val newValue = parseContext.expr.resolve(expr)

        if (parseContext.errorCollector.errors.isNotEmpty()) {
            parseContext.errorCollector.errors.forEach { println(it) }
            error("Failed to parse DynamicValue $expr")
        }

        requireNotNull(newValue) { "Failed to produce a value for DynamicValue $expr" }

        value = newValue
    }
}