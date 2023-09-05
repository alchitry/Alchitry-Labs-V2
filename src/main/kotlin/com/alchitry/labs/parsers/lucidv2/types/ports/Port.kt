package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.EvalQueue
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean
) {
    val isInout: Boolean = direction == SignalDirection.Both

    fun instantiate(parent: SignalParent?, evalQueue: EvalQueue) = when (direction) {
        SignalDirection.Read -> Input(name, evalQueue, parent, width, signed)
        SignalDirection.Write -> Output(name, evalQueue, parent, width, signed)
        SignalDirection.Both -> Inout(name, evalQueue, parent, width, signed)
    }
}