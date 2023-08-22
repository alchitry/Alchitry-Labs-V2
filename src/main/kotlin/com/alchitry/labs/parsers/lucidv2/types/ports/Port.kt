package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs.project.Project

data class Port(
    val name: String,
    val direction: SignalDirection,
    val width: SignalWidth,
    val signed: Boolean
) {
    val isInout: Boolean = direction == SignalDirection.Both

    fun instantiate(parent: SignalParent?, project: Project) = when (direction) {
        SignalDirection.Read -> Input(name, project, parent, width, signed)
        SignalDirection.Write -> Output(name, project, parent, width, signed)
        SignalDirection.Both -> Inout(name, project, parent, width, signed)
    }
}