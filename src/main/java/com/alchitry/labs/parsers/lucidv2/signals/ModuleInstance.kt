package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.Bit

data class ModuleInstance(
    override val name: String,
    val module: Module,
    val parameters: Map<String, Signal>
) : SignalParent {
    val ports: Map<String, Signal> = module.ports.mapValues { (_, port) ->
        Signal(port.name, port.direction, this, port.width.filledWith(Bit.Bu, false, port.signed), port.signed)
    }

    override fun getSignal(name: String) = ports[name]
}
