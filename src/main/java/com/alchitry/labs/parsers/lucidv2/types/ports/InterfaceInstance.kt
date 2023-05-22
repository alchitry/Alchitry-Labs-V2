package com.alchitry.labs.parsers.lucidv2.types.ports

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.types.Interface


class InterfaceInstance(
    override val name: String,
    override val parent: SignalParent?,
    project: ProjectContext,
    val type: Interface,
) : PortOrInterfaceInstance, SignalParent {
    private val ports = type.ports.mapValues { it.value.instantiate(this, project) }

    override val internal = InterfaceSignals(
        name,
        parent,
        ports.mapValues { it.value.internal }
    )

    override val external = InterfaceSignals(
        name,
        parent,
        ports.mapValues { it.value.external }
    )

    override fun getSignal(name: String): SignalOrParent? {
        error("getSignal() shouldn't be called on InterfaceInstance!")
    }
}

class InterfaceSignals(
    override val name: String,
    override val parent: SignalParent?,
    val signals: Map<String, SignalOrParent>
) : SignalParent {
    override fun getSignal(name: String): SignalOrParent? = signals[name]
}