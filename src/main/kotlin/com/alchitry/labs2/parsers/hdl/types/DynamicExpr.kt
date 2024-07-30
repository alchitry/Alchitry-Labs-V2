package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.grammar.LucidParser.ExprContext
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucidv2.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.values.Value
import kotlinx.coroutines.CoroutineStart
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
    context: LucidBlockContext,
    private val widthConstraint: SignalWidth? = null
) : Evaluable, SignalParent {
    override val name = "DynamicExpr(${expr.text})"
    override val parent: SignalParent? = null
    private val context = context.withEvalContext(this, name)

    private val dependants = mutableSetOf<Evaluable>()
    fun addDependant(dependent: Evaluable) {
        dependants.add(dependent)
    }

    fun addDependant(onChange: suspend () -> Unit) = addDependant(Evaluable(onChange))

    fun withContext(context: LucidBlockContext) = DynamicExpr(expr, context, widthConstraint)

    var value: Value = context.expr.resolve(expr)?.value?.let {
        if (widthConstraint != null) {
            if (!widthConstraint.canAssign(it.width))
                error("Specified width is incompatible with the initial value!")
            it.resizeToMatch(widthConstraint)
        } else {
            it
        }
    } ?: error("Failed to resolve initial value for ${expr.text}")
        private set

    val width: SignalWidth = widthConstraint ?: value.width

    init {
        val dependencies =
            context.expr.resolveDependencies(expr) ?: error("Failed to resolve dependencies for ${expr.text}")
        dependencies.forEach { it.markRead() }
        val evaluable = Evaluable { context.project.queue(this@DynamicExpr) }
        dependencies.forEach { it.addDependant(evaluable) }
    }

    override fun getSignal(name: String): Signal = asSignal(name)

    fun asSignal(name: String): Signal {
        return Signal(name, SignalDirection.Read, this, value, ExprType.Dynamic).also { signal ->
            signal.hasDriver = true
            val evaluable = Evaluable { signal.write(value) }
            addDependant {
                context.project.queue(evaluable)
            }
        }
    }

    fun connectTo(signal: SignalOrSubSignal) {
        require(signal.width.canAssign(width)) { "The provided signal's width doesn't match this DynamicExpr width!" }
        if (signal is Signal) {
            require(!signal.hasDriver) { "The signal \"${signal.name}\" already has a driver!" }
            signal.hasDriver = true
        }

        context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            signal.write(value)
        }

        val evaluable = Evaluable { signal.write(value) }
        addDependant {
            context.project.queue(evaluable)
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

    private var lastValue: Value? = null
    override suspend fun evaluate() {
        context.walk(expr)

        val newValue = context.expr.resolve(expr)?.value

        if (context.notationCollector.hasErrors) {
            context.notationCollector.printReport()
            error("Failed to parse DynamicValue $expr")
        }

        requireNotNull(newValue) { "Failed to produce a value for DynamicValue $expr" }

        if (!width.canAssign(newValue.width))
            error("Dynamic expression value attempted to change widths to something incompatible!")

        value = newValue.resizeToMatch(width)
        if (lastValue != value) {
            dependants.forEach { it.evaluate() }
            lastValue = value
        }
    }
}