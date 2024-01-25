package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.Evaluable
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.values.Value
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

sealed interface SignalOrSubSignal : Measurable {
    /**
     * Reads this signal and returns it's signal. It will return the public value if evalContext
     * is null or doesn't match the quietWrite() evalContext. If the evalContext matches the
     * evalContext provided when writing to quietWrite(), then it will return the private value that
     * was last written.
     */
    fun read(evalContext: Evaluable? = null): Value
    suspend fun write(v: Value)

    val valueFlow: Flow<Value>

    /**
     * Sets the value of this signal without publishing the change. This value will only be accessible when the same
     * Evaluable is passed to the get() function until publish() is called.
     */
    fun quietWrite(v: Value, evalContext: Evaluable?)

    /**
     * Publishes a value written using quietWrite() to anyone listing for new values.
     */
    suspend fun publish()
    val direction: SignalDirection

    fun getSignal(): Signal =
        when (this) {
            is Signal -> this
            is SubSignal -> parent
        }

    /**
     * Connects this signal's value to the provided signal.
     */
    fun connectTo(sig: SignalOrSubSignal, context: ProjectContext) {
        require(sig.width.canAssign(width)) {
            "Cannot assign this signal's value ($width) to the provided signal (${sig.width})!"
        }

        // only check for drivers on full signals since sub-signal assignments will be done multiple times
        if (sig is Signal)
            require(!sig.hasDriver) { "Signal \"${sig.name}\" already has a driver!" }

        sig.getSignal().hasDriver = true
        getSignal().isRead = true

        context.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            sig.write(read())
        }

        val evaluable = Evaluable {
            sig.write(read())
        }

        context.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            valueFlow.collect {
                context.queueEvaluation(evaluable)
            }
        }
    }

}