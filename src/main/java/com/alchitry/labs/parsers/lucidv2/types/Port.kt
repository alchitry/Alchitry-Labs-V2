package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.types.ports.Inout
import com.alchitry.labs.parsers.lucidv2.types.ports.Input
import com.alchitry.labs.parsers.lucidv2.types.ports.Output
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean
) {
    val isInout: Boolean = direction == SignalDirection.Both

    fun instantiate(parent: SignalParent?, project: ProjectContext) = when (direction) {
        SignalDirection.Read -> Input(name, project, parent, width, signed)
        SignalDirection.Write -> Output(name, project, parent, width, signed)
        SignalDirection.Both -> Inout(name, project, parent, width, signed)
    }
}