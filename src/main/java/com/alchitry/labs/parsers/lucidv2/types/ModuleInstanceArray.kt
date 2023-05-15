package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.context.ProjectContext
import com.alchitry.labs.parsers.lucidv2.signals.Signal
import com.alchitry.labs.parsers.lucidv2.signals.SignalDirection
import com.alchitry.labs.parsers.lucidv2.signals.SignalParent
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector
import com.alchitry.labs.parsers.lucidv2.values.*

sealed interface ModuleInstanceOrArray : SignalParent {
    fun removePort(name: String)
}

class ModuleInstanceArray(
    override val name: String,
    projectContext: ProjectContext,
    override val parent: ModuleInstance?,
    module: Module,
    dimensions: List<Int>,
    paramProvider: (List<Int>) -> Map<String, Value>
) : ModuleInstanceOrArray {
    private val modules: ModuleList

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

    private val externalPorts: MutableMap<String, Signal> = module.ports.mapValues { (_, port) ->
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
                    ModuleInstance(name, projectContext, parent, module, paramProvider(index))
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
            module.ports.forEach { (_, port) ->
                val subSig =
                    inouts[port.name]?.internal?.select(selection)
                        ?: externalPorts[port.name]?.select(selection)
                        ?: error("Missing external port!")
                val modSig = moduleInstance.getSignal(port.name) ?: error("Missing module instance port!")
                if (port.direction.canRead)
                    subSig.connectTo(modSig, projectContext)
                if (port.direction.canWrite)
                    modSig.connectTo(subSig, projectContext)
            }
        }

    }

    override fun getSignal(name: String) = externalPorts[name]
}

sealed interface ListOrModuleInstance
class ModuleList(private val modules: List<ListOrModuleInstance>) : List<ListOrModuleInstance> by modules,
    ListOrModuleInstance {
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