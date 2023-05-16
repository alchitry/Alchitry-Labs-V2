package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.values.*

sealed interface ModuleInstanceOrArray : SignalParent {
    fun removePort(name: String)
    val ports: Map<String, Signal>
}

class ModuleInstanceArray(
    override val name: String,
    projectContext: ProjectContext,
    override val parent: ModuleInstance?,
    module: Module,
    dimensions: List<Int>,
    paramProvider: (List<Int>) -> Map<String, Value>,
    signalProvider: (List<Int>) -> Map<String, SignalOrSubSignal>
) : ModuleInstanceOrArray {
    private val modules: ModuleList

    fun getAllInstances(): List<ModuleInstance> {
        val instances = mutableListOf<ModuleInstance>()
        modules.forEach {
            instances.add(it)
        }
        return instances
    }

    private val inouts = module.ports.mapNotNull { (_, port) ->
        if (port.direction != SignalDirection.Both) {
            null
        } else {
            var width: SignalWidth = port.width
            dimensions.asReversed().forEach {
                width = ArrayWidth(it, width)
            }
            Inout(port.name, projectContext, this, width, port.signed)
        }
    }.associateBy { it.name }

    override fun removePort(name: String) {
        externalPorts.remove(name)
    }

    override val ports get() = externalPorts.toMap()
    private val externalPorts: MutableMap<String, Signal> =
        module.ports.filter { !signalProvider(dimensions.map { 0 }).containsKey(it.key) }.mapValues { (_, port) ->
            if (port.direction == SignalDirection.Both) {
                inouts[port.name]?.external ?: error("Missing inout for port ${port.name}! This should be impossible!")
            } else {
                var width: SignalWidth = port.width
                dimensions.asReversed().forEach {
                    width = if (width is BitWidth)
                        BitListWidth(it)
                    else
                        ArrayWidth(it, width)
                }
                Signal(
                    port.name,
                    port.direction.flip(),
                    this,
                    width.filledWith(Bit.Bx, false, port.signed),
                    port.signed
                )
            }
        }.toMutableMap()

    private fun collectErrorsFor(block: (ModuleInstance) -> String?): String? {
        val sb = StringBuilder()
        modules.forEachIndexed { idx: List<Int>, moduleInstance: ModuleInstance ->
            block(moduleInstance)?.let {
                if (sb.isNotEmpty())
                    sb.append("\n")
                sb.append("Index ${idx.joinToString(prefix = "[", postfix = "]", separator = "][")}: ")
                sb.append(it)
            }
        }
        if (sb.isNotEmpty())
            return sb.toString()
        return null
    }

    fun checkAllParameters(): String? = collectErrorsFor { it.checkParameters() }
    fun initialWalkAll(): String? = collectErrorsFor { it.initialWalk() }

    init {
        require(dimensions.isNotEmpty()) { "Dimensions must not be empty!" }

        fun generateModules(dimensions: List<Int>, index: List<Int>): ListOrModuleInstance {
            val dim = dimensions.first()
            if (dimensions.size == 1) {
                return ModuleList(List(dim) {
                    ModuleInstance(name, projectContext, parent, module, paramProvider(index), signalProvider(index))
                })
            }
            val subDim = dimensions.subList(1, dimensions.size)

            return ModuleList(List(dim) {
                val nextIdx = index.toMutableList().apply { add(it) }
                generateModules(subDim, nextIdx)
            })
        }

        modules = generateModules(dimensions, emptyList()) as ModuleList

        modules.forEachIndexed { index: List<Int>, moduleInstance: ModuleInstance ->
            val selection = index.map { SignalSelector.Bits(it) }
            moduleInstance.ports.forEach { (name, port) ->
                val subSig =
                    inouts[name]?.internal?.select(selection)
                        ?: externalPorts[name]?.select(selection)
                        ?: error("Missing external port!")
                if (port.direction.canRead)
                    subSig.connectTo(port, projectContext)
                if (port.direction.canWrite)
                    port.connectTo(subSig, projectContext)
            }
        }

    }

    override fun getSignal(name: String) = externalPorts[name]
}

sealed interface ListOrModuleInstance
class ModuleList(private val modules: List<ListOrModuleInstance>) : ListOrModuleInstance {
    fun forEach(block: (ModuleInstance) -> Unit) {
        modules.forEach {
            when (it) {
                is ModuleInstance -> block(it)
                is ModuleList -> forEach(block)
            }
        }
    }

    private fun forEachIndexedRecurse(index: List<Int>, block: (List<Int>, ModuleInstance) -> Unit) {
        val newIdx = index.toMutableList().apply { add(0) }
        modules.forEachIndexed { i, listOrModuleInstance ->
            newIdx[newIdx.lastIndex] = i
            when (listOrModuleInstance) {
                is ModuleInstance -> block(newIdx, listOrModuleInstance)
                is ModuleList -> listOrModuleInstance.forEachIndexedRecurse(newIdx, block)
            }
        }
    }

    fun forEachIndexed(block: (List<Int>, ModuleInstance) -> Unit) {
        forEachIndexedRecurse(emptyList(), block)
    }
}