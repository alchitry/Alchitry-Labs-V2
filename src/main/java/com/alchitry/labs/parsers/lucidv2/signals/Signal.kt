package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

enum class SignalDirection {
    Read,
    Write,
    Both;

    val canRead: Boolean get() = this != Write
    val canWrite: Boolean get() = this != Read
}

class Signal(
    override val name: String, // includes namespace or module name
    override val direction: SignalDirection,
    val parent: SignalParent?,
    initialValue: Value,
    val signed: Boolean = initialValue is SimpleValue && initialValue.signed
) : SignalOrParent, SignalOrSubSignal {
    fun select(selection: SignalSelection) = SubSignal(this, selection)

    private val mutableValueFlow = SynchronizedSharedFlow<Value>()
    val valueFlow: Flow<Value> get() = mutableValueFlow.asFlow()

    private var setEvalContext: Evaluable? = null
    private var nextValue: Value? = null
    private var value: Value = initialValue.withSign(signed)

    override fun get(evalContext: Evaluable?): Value {
        if (evalContext === setEvalContext)
            return nextValue ?: value
        return value
    }

    override val width: SignalWidth = value.signalWidth

    override fun quietSet(v: Value, evalContext: Evaluable?) {
        require(setEvalContext == null || evalContext === setEvalContext) { "Attempted to set signal from two different evalContext!" }
        setEvalContext = evalContext
        quietSet(v)
    }

    private fun quietSet(v: Value) {
        require(value.canAssign(v)) { "Signal assigned value does not match its size!" }
        val resizedValue = v.resizeToMatch(value.signalWidth)
        nextValue = resizedValue.withSign(signed)
    }

    override suspend fun publish() {
        nextValue?.let {
            value = it
            mutableValueFlow.emit(it)
        }
        nextValue = null
        setEvalContext = null
    }

    override suspend fun set(v: Value) {
        quietSet(v)
        publish()
    }

    /**
     * Connects this signal's value to the provided signal.
     */
    fun connect(sig: Signal, context: LucidModuleContext) {
        require(sig.value.signalWidth.canAssign(value.signalWidth)) {
            "Cannot assign this signal's value to the provided signal!"
        }
        context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            sig.set(value)
        }

        val evaluable = Evaluable { sig.set(value) }

        context.project.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            valueFlow.collect {
                context.project.queueEvaluation(evaluable)
            }
        }
    }
}

class SignalSelectionException(val selector: SignalSelector, message: String) : Exception(message)

data class SubSignal(
    val parent: Signal,
    val selection: SignalSelection
) : SignalOrSubSignal {
    override fun get(evalContext: Evaluable?): Value {
        var v = parent.get(evalContext)
        selection.forEach {
            v = v.select(it)
        }
        return v
    }

    override val width: SignalWidth = get().signalWidth

    override val direction: SignalDirection
        get() = parent.direction

    /**
     * Generates the full value for the parent signal with the value v applied to the selected portion of the signal.
     */
    private fun getFullValue(v: Value): Value {
        require(get().signalWidth.canAssign(v.signalWidth)) {
            "Cannot set value $v to selected subsignal!"
        }
        TODO("Not yet implemented")
    }

    override fun quietSet(v: Value, evalContext: Evaluable?) = parent.quietSet(getFullValue(v), evalContext)
    override suspend fun set(v: Value) = parent.set(getFullValue(v))
    override suspend fun publish() = parent.publish()
}

sealed interface SignalOrSubSignal {
    fun get(evalContext: Evaluable? = null): Value
    suspend fun set(v: Value)

    /**
     * Sets the value of this signal without publishing the change. This value will only be accessible when the same
     * Evaluable is passed to the get() function until publish() is called.
     */
    fun quietSet(v: Value, evalContext: Evaluable?)
    suspend fun publish()
    val direction: SignalDirection
    val width: SignalWidth

    fun getSignal(): Signal =
        when (this) {
            is Signal -> this
            is SubSignal -> parent
        }

}

/**
 * A union of the interface SignalParent and Signal class
 */
sealed interface SignalOrParent {
    val name: String
}

/**
 * A SignalParent is something that holds signals. For example a Dff has signals "d" and "q".
 */
sealed interface SignalParent : SignalOrParent {
    /**
     * Retrieves the child signal from this parent.
     * @param name the name of the signal to retrieve
     * @return the signal or null if it couldn't be found
     */
    fun getSignal(name: String): Signal?
}