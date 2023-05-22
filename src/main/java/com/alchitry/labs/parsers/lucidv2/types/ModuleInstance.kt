package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.context.LucidModuleContext
import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent
import com.alchitry.labs.parsers.lucidv2.signals.SignalOrSubSignal
import com.alchitry.labs.parsers.lucidv2.types.ports.Inout
import com.alchitry.labs.parsers.lucidv2.types.ports.Input
import com.alchitry.labs.parsers.lucidv2.types.ports.InterfaceInstance
import com.alchitry.labs.parsers.lucidv2.types.ports.Output
import com.alchitry.labs.parsers.lucidv2.values.Value

class ModuleInstance(
    override val name: String,
    private val project: ProjectContext,
    override val parent: ModuleInstance?,
    val module: Module,
    parameters: Map<String, Value>,
    connections: Map<String, SignalOrSubSignal>,
    errorCollector: ErrorCollector = ErrorCollector()
) : ModuleInstanceOrArray, ListOrModuleInstance {
    val context = LucidModuleContext(project, this, errorCollector = errorCollector)

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

    val ports = module.ports.mapValues { (_, port) ->
        port.instantiate(this, project)
    }

    override val internal: Map<String, SignalOrParent> = ports.mapValues { it.value.internal }
    override val external: Map<String, SignalOrParent> = ports
        .filter { !connections.contains(it.key) }
        .mapValues { it.value.external }

    init {
        connections.forEach { (name, sig) ->
            when (val port = ports[name] ?: error("No matching port for given connection \"$name\"!")) {
                is InterfaceInstance -> TODO()
                is Inout -> {
                    port.external.connectTo(sig, project)
                    sig.connectTo(port.external, project)
                }

                is Input -> sig.connectTo(port.external, project)
                is Output -> port.external.connectTo(sig, project)
            }
        }
    }

    // Use the provided parameters or the default value from the module is it is missing
    val parameters = module.parameters.mapValues { (name, param) ->
        Signal(
            name,
            SignalDirection.Read,
            this,
            parameters[name] ?: param.default ?: error("Missing module parameter!")
        )
    }

    fun getInternalSignal(name: String) = internal[name] ?: parameters[name]
    override fun getSignal(name: String) = external[name]
}
