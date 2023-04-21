package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.onAnyChange
import com.alchitry.labs.parsers.lucidv2.grammar.LucidLexer
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.parsers.BitSelectionParser
import com.alchitry.labs.parsers.lucidv2.parsers.ExprParser
import com.alchitry.labs.parsers.lucidv2.resolvers.LucidParseContext
import com.alchitry.labs.parsers.lucidv2.resolvers.SignalResolver
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

/**
 * This class is designed to represent an expression whose value will change automatically whenever one of the
 * Signals it depends on changes.
 *
 * @param expr is the expression to use to evaluate new values
 * @param parseContext is the context of the original parse
 * @param initialValue is the initial value to set
 * @param dependencies is the list of Signals whose change will cause a reevaluation of this expression
 */
class DynamicExpr(
    private val expr: ExprContext,
    private val parseContext: LucidParseContext,
    initialValue: Value,
    dependencies: List<Signal>
) {
    private val mutableValueFlow = MutableStateFlow(initialValue)
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
        parseContext.scope.launch {
            onAnyChange(dependencies.map { it.valueFlow }) {
                evaluate()
            }
        }
    }

    suspend fun collect(collector: FlowCollector<Value>): Nothing = mutableValueFlow.collect(collector)

    private fun evaluate() {
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