package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.types.ports.InterfaceInstance

enum class InterfaceDirection {
    A,
    B;

    fun flip(): InterfaceDirection =
        when (this) {
            A -> B
            B -> A
        }
}

data class Interface(
    override val name: String,
    val ports: Map<String, PortOrInterface>,
    val direction: InterfaceDirection
) : PortOrInterface {
    override val hasInout: Boolean = ports.values.any { it.hasInout }

    override fun instantiate(parent: SignalParent?, project: ProjectContext) =
        InterfaceInstance(name, parent, project, this)

    override fun flip(): Interface = copy(
        ports = ports.mapValues { it.value.flip() },
        direction = direction.flip()
    )

    fun withDirection(direction: InterfaceDirection) =
        if (direction == this.direction) this else flip()

    fun isCompatible(other: Interface): Boolean = this == other.flip()
}

