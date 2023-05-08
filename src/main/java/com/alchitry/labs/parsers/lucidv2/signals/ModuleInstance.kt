package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.values.Bit

class ModuleInstance(
    private val context: ProjectContext,
    override val name: String,
    val module: Module,
    parameters: Map<String, Signal>
) : SignalParent {
    val ports: Map<String, Signal> = module.ports.mapValues { (_, port) ->
        Signal(port.name, port.direction, this, port.width.filledWith(Bit.Bu, false, port.signed), port.signed)
    }

    val externalPorts: Map<String, Signal> = module.ports.mapValues { (_, port) ->
        Signal(port.name, port.direction.flip(), this, port.width.filledWith(Bit.Bu, false, port.signed), port.signed)
    }

    // Use the provided parameters or the default value from the module is it is missing
    val parameters = module.parameters.mapValues { (name, param) ->
        parameters[name] ?: Signal(
            name,
            SignalDirection.Read,
            this,
            param.default ?: error("Missing module parameter!")
        )
    }

    init {
        ports.keys.forEach { portName ->
            val internal = ports[portName] ?: error("Missing port $portName!")
            val external = ports[portName] ?: error("Missing port $portName!")

            when (internal.direction) {
                SignalDirection.Read -> external.connect(internal, context)
                SignalDirection.Write -> internal.connect(external, context)
                else -> error("Bidirectional signals not supported on module ports!")
            }
        }
    }

    fun getInternalSignal(name: String) = ports[name] ?: parameters[name]
    override fun getSignal(name: String) = externalPorts[name]
}
