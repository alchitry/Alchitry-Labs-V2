package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.values.Bit
import com.alchitry.labs.parsers.lucidv2.values.Value

class ModuleInstance(
    override val name: String,
    private val project: ProjectContext,
    override val parent: ModuleInstance?,
    val module: Module,
    parameters: Map<String, Value>
) : ModuleInstanceOrArray, ListOrModuleInstance {
    val context = LucidModuleContext(project, this)

    fun checkParameters(): String? {
        val paramErrors = context.checkParameters() ?: return null
        val sb = StringBuilder("Module instance $name parameters failed their constraint checks:")
        paramErrors.forEach { sb.append("\n    ").append(it) }
        return sb.toString()
    }

    fun initialWalk(): String? {
        val walkErrors = context.initialWalk(module.context) ?: return null
        val sb = StringBuilder("Module instance $name contains errors:")
        walkErrors.forEach { sb.append("\n    ").append(it) }
        return sb.toString()
    }

    private val inouts = module.ports.mapNotNull { (_, port) ->
        if (port.direction != SignalDirection.Both)
            null
        else
            Inout(port.name, project, this, port.width, port.signed)
    }.associateBy { it.name }

    private val ports: Map<String, Signal> = module.ports.mapValues { (_, port) ->
        if (port.direction == SignalDirection.Both)
            inouts[port.name]?.internal ?: error("Missing inout for port ${port.name}! This should be impossible!")
        else
            Signal(port.name, port.direction, null, port.width.filledWith(Bit.Bx, false, port.signed), port.signed)
    }

    override fun removePort(name: String) {
        externalPorts.remove(name)
    }

    private val externalPorts: MutableMap<String, Signal> = module.ports.mapValues { (_, port) ->
        if (port.direction == SignalDirection.Both)
            inouts[port.name]?.external ?: error("Missing inout for port ${port.name}! This should be impossible!")
        else
            Signal(
                port.name,
                port.direction.flip(),
                this,
                port.width.filledWith(Bit.Bx, false, port.signed),
                port.signed
            )
    }.toMutableMap()

    // Use the provided parameters or the default value from the module is it is missing
    val parameters = module.parameters.mapValues { (name, param) ->
        Signal(
            name,
            SignalDirection.Read,
            this,
            parameters[name] ?: param.default ?: error("Missing module parameter!")
        )
    }

    init {
        ports.keys.forEach { portName ->
            val internal = ports[portName] ?: error("Missing port $portName!")
            val external = externalPorts[portName] ?: error("Missing port $portName!")

            when (internal.direction) {
                SignalDirection.Read -> external.connectTo(internal, project)
                SignalDirection.Write -> internal.connectTo(external, project)
                SignalDirection.Both -> {}
            }
        }
    }

    fun getInternalSignal(name: String) = ports[name] ?: parameters[name]
    override fun getSignal(name: String) = externalPorts[name]
}
