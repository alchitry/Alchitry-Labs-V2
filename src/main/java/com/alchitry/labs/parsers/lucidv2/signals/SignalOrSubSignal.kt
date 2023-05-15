package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

sealed interface SignalOrSubSignal {
    fun read(evalContext: Evaluable? = null): Value
    suspend fun write(v: Value)

    val valueFlow: Flow<Value>

    /**
     * Sets the value of this signal without publishing the change. This value will only be accessible when the same
     * Evaluable is passed to the get() function until publish() is called.
     */
    fun quietWrite(v: Value, evalContext: Evaluable?)
    suspend fun publish()
    val direction: SignalDirection
    val width: SignalWidth

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
            "Cannot assign this signal's value to the provided signal!"
        }
        context.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            sig.write(read())
        }

        val evaluable = Evaluable { sig.write(read()) }

        context.scope.launch(start = CoroutineStart.UNDISPATCHED) {
            valueFlow.collect {
                context.queueEvaluation(evaluable)
            }
        }
    }
}