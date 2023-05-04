package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.grammar.LucidParser.ExprContext
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value
import com.alchitry.labs.parsers.onAnyChange
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
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
    private val context: LucidModuleContext,
    widthConstraint: SignalWidth? = null
) : Evaluable {
    private val mutableValueFlow = SynchronizedSharedFlow<Value>()
    val valueFlow: Flow<Value> get() = mutableValueFlow.asFlow()

    var value: Value = context.expr.resolve(expr)?.let {
        if (widthConstraint != null) {
            if (!widthConstraint.canAssign(it.signalWidth))
                error("Specified width is incompatible with the initial value!")
            it.resizeToMatch(widthConstraint)
        } else {
            it
        }
    } ?: error("Failed to resolve initial value for ${expr.text}")
        private set

    val width: SignalWidth = widthConstraint ?: value.signalWidth

    init {
        val dependencies =
            context.expr.resolveDependencies(expr) ?: error("Failed to resolve dependencies for ${expr.text}")
        context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            onAnyChange(dependencies.map { it.valueFlow }) {
                context.queueEvaluation(this@DynamicExpr)
            }
        }
    }

    fun asSignal(name: String): Signal {
        return Signal(name, SignalDirection.Read, null, value).also { signal ->
            val evaluable = Evaluable { signal.set(value) }
            context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
                valueFlow.collect {
                    context.queueEvaluation(evaluable)
                }
            }
        }
    }

    /**
     * Returns a DynamicExpr of the same expression but with a different width constraint.
     */
    fun constrain(width: SignalWidth): DynamicExpr {
        if (this.width == width)
            return this
        return DynamicExpr(expr, context, width)
    }

    override suspend fun evaluate() {
        context.walk(expr)

        val newValue = context.expr.resolve(expr)

        if (context.errorCollector.errors.isNotEmpty()) {
            context.errorCollector.errors.forEach { println(it) }
            error("Failed to parse DynamicValue $expr")
        }

        requireNotNull(newValue) { "Failed to produce a value for DynamicValue $expr" }

        if (!width.canAssign(newValue.signalWidth))
            error("Dynamic expression value attempted to change widths to something incompatible!")

        value = newValue.resizeToMatch(width)
        mutableValueFlow.emit(newValue)
    }
}