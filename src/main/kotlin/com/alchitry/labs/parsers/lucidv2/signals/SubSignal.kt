package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.Evaluable
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.parsers.lucidv2.values.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.withLock

data class SubSignal(
    val parent: Signal,
    val selection: SignalSelection
) : SignalOrSubSignal {
    override fun read(evalContext: Evaluable?): Value = parent.read(evalContext).select(selection)

    override val width: SignalWidth = read().width

    override val direction: SignalDirection
        get() = parent.direction

    override val valueFlow: Flow<Value>
        get() = parent.valueFlow.map { it.select(selection) }.distinctUntilChanged()

    /**
     * Generates the full value for the parent signal with the value v applied to the selected portion of the signal.
     */
    private fun getFullValue(v: Value, evalContext: Evaluable?): Value {
        require(width.canAssign(v.width)) {
            "Cannot set value $v to selected subsignal!"
        }
        return parent.read(evalContext).write(selection, v.resizeToMatch(width))
    }

    // this performs a read-modify-write just like write() does, but we can assume it will always be from the same thread
    // so no need for synchronization
    override fun quietWrite(v: Value, evalContext: Evaluable?) =
        parent.quietWrite(getFullValue(v, evalContext), evalContext)

    // write performs a read-modify-write cycle on the underling signal so the subSignalLock is required to prevent
    // corruption as this could be called from multiple threads
    override suspend fun write(v: Value) = parent.subSignalLock.withLock { parent.write(getFullValue(v, null)) }

    // this also shouldn't need synchronization because we will either use write() or quietWrite()/publish() exclusively
    // we should never be mixing calls to write() with calls to publish()
    override suspend fun publish() = parent.publish()
}