package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
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
    initialValue: Value
) : SignalOrParent, SignalOrSubSignal {
    fun select(selection: SignalSelection) = SubSignal(this, selection)

    private val mutableValueFlow = SynchronizedSharedFlow<Value>()
    val valueFlow: Flow<Value> get() = mutableValueFlow.asFlow()

    override var value: Value = initialValue
        private set

    override val width: SignalWidth = value.signalWidth

    override fun quietSet(v: Value) {
        require(value.canAssign(v)) { "Signal assigned value does not match its size!" }
        val resizedValue = v.resizeToMatch(value.signalWidth)
        value = resizedValue
    }

    suspend fun publishChange() {
        mutableValueFlow.emit(value)
    }

    override suspend fun set(v: Value) {
        quietSet(v)
        publishChange()
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
                context.queueEvaluation(evaluable)
            }
        }
    }
}

class SignalSelectionException(val selector: SignalSelector, message: String) : Exception(message)

data class SubSignal(
    val parent: Signal,
    val selection: SignalSelection
) : SignalOrSubSignal {
    override val value: Value
        get() {
            var v = parent.value
            selection.forEach {
                v = v.select(it)
            }
            return v
        }

    override val width: SignalWidth = value.signalWidth

    override val direction: SignalDirection
        get() = parent.direction

    override fun quietSet(v: Value) {
        require(value.signalWidth.canAssign(v.signalWidth)) {
            "Cannot set value $v to selected subsignal!"
        }
        TODO("Not yet implemented")
    }

    override suspend fun set(v: Value) {
        quietSet(v)
        parent.publishChange()
    }
}

sealed interface SignalOrSubSignal {
    val value: Value
    suspend fun set(v: Value)

    /**
     * Sets the value of this signal without publishing the change.
     */
    fun quietSet(v: Value)
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