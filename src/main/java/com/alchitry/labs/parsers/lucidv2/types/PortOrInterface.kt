package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.types.ports.PortOrInterfaceInstance

sealed interface PortOrInterface {
    val name: String

    val hasInout: Boolean

    fun instantiate(parent: SignalParent?, project: ProjectContext): PortOrInterfaceInstance
    fun flip(): PortOrInterface
}