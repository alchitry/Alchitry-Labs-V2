package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value

sealed interface SignalOrSubSignal {
    fun read(evalContext: Evaluable? = null): Value
    suspend fun write(v: Value)

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

}