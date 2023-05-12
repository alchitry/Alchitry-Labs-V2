package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.SynchronizedSharedFlow
import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.flow.Flow

/**
 * A signal whose reads are delegated to a proxy. This is used by inout ports on modules since the read value
 * depends on two different signal values.
 */
class ProxySignal(
    name: String,
    direction: SignalDirection,
    parent: SignalParent?,
    initialValue: Value,
    signed: Boolean = initialValue is SimpleValue && initialValue.signed,
    private val onRead: (evalContext: Evaluable?) -> Value
) : Signal(name, direction, parent, initialValue, signed) {

    override fun read(evalContext: Evaluable?): Value = onRead(evalContext)
    fun baseRead(evalContext: Evaluable?) = super.read(evalContext)

    private val mutableReadFlow = SynchronizedSharedFlow<Value>()
    override val valueFlow: Flow<Value> = mutableReadFlow.asFlow()
    val writeFlow = super.valueFlow

    suspend fun updateRead(value: Value = read()) {
        mutableReadFlow.emit(value)
    }
}