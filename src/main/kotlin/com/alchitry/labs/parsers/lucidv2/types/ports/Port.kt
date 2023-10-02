package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs.parsers.lucidv2.types.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean
) {
    val isInout: Boolean = direction == SignalDirection.Both

    fun instantiate(parent: SignalParent?, context: ProjectContext) = when (direction) {
        SignalDirection.Read -> Input(name, context, parent, width, signed)
        SignalDirection.Write -> Output(name, context, parent, width, signed)
        SignalDirection.Both -> Inout(name, context, parent, width, signed)
    }
}