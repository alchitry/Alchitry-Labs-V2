package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

data class SubSignal(
    val parent: Signal,
    val selection: SignalSelection
) : SignalOrSubSignal {
    override fun read(evalContext: Evaluable?): Value = parent.read(evalContext).select(selection)

    override val width: SignalWidth = read().signalWidth

    override val direction: SignalDirection
        get() = parent.direction

    override val valueFlow: Flow<Value>
        get() = parent.valueFlow.map { it.select(selection) }.distinctUntilChanged()

    /**
     * Generates the full value for the parent signal with the value v applied to the selected portion of the signal.
     */
    private fun getFullValue(v: Value, evalContext: Evaluable?): Value {
        require(read().signalWidth.canAssign(v.signalWidth)) {
            "Cannot set value $v to selected subsignal!"
        }
        return parent.read(evalContext).write(selection, v.resizeToMatch(width))
    }

    override fun quietWrite(v: Value, evalContext: Evaluable?) =
        parent.quietWrite(getFullValue(v, evalContext), evalContext)

    override suspend fun write(v: Value) = parent.write(getFullValue(v, null))
    override suspend fun publish() = parent.publish()
}