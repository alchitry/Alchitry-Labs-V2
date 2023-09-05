package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.ProjectContext
import com.alchitry.labs.parsers.errors.ErrorCollector
import com.alchitry.labs.parsers.lucidv2.signals.*
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SnapshotOrParent
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SnapshotParent
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.Snapshotable
import com.alchitry.labs.parsers.lucidv2.types.ports.Inout
import com.alchitry.labs.parsers.lucidv2.types.ports.Input
import com.alchitry.labs.parsers.lucidv2.types.ports.Output
import com.alchitry.labs.parsers.lucidv2.values.*

sealed interface ModuleInstanceOrArray : SignalParent, Snapshotable {
    val internal: Map<String, Signal>
    val external: Map<String, Signal>
}

class ModuleInstanceArray(
    override val name: String,
    project: ProjectContext,
    private val testOrModuleParent: TestOrModuleInstance,
    val errorCollector: ErrorCollector,
    module: Module,
    dimensions: List<Int>,
    paramProvider: (List<Int>) -> Map<String, Value>,
    signalProvider: (List<Int>) -> Map<String, SignalOrSubSignal>
) : ModuleInstanceOrArray {
    override val parent = testOrModuleParent as? ModuleInstance
    private val modules: ModuleList

    override fun takeSnapshot(): SnapshotParent {
        val snapshots = mutableListOf<SnapshotOrParent>()
        modules.forEachIndexed { index, moduleInstance ->
            val indexedName = index.joinToString(prefix = "[", postfix = "]", separator = "][")
            snapshots.add(moduleInstance.takeSnapshot().copy(name = indexedName))
        }
        return SnapshotParent(name, snapshots)
    }

    fun getAllInstances(): List<ModuleInstance> {
        val instances = mutableListOf<ModuleInstance>()
        modules.forEach {
            instances.add(it)
        }
        return instances
    }

    private val ports = module.ports.mapValues { (_, port) ->
        var width: SignalWidth = port.width
        dimensions.asReversed().forEach {
            width = if (width is BitWidth)
                BitListWidth(it)
            else
                ArrayWidth(it, width)
        }
        when (port.direction) {
            SignalDirection.Read -> Input(port.name, project, this, width, port.signed)
            SignalDirection.Write -> Output(port.name, project, this, width, port.signed)
            SignalDirection.Both -> Inout(port.name, project, this, width, port.signed)
        }
    }

    override val internal: Map<String, Signal> = ports.mapValues { it.value.internal }
    override val external = ports
        .filter { !signalProvider(dimensions.map { 0 }).containsKey(it.key) }
        .mapValues { it.value.external }

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

    suspend fun checkAllParameters(): Boolean = modules.suspendMap { it.checkParameters() }.all { it }
    suspend fun initialWalkAll(): Boolean = modules.suspendMap { it.initialWalk() }.all { it }

    init {
        require(dimensions.isNotEmpty()) { "Dimensions must not be empty!" }

        fun generateModules(dimensions: List<Int>, index: List<Int>): ListOrModuleInstance {
            val dim = dimensions.first()
            if (dimensions.size == 1) {
                return ModuleList(List(dim) {
                    ModuleInstance(
                        name,
                        project,
                        parent,
                        module,
                        paramProvider(index),
                        signalProvider(index),
                        testOrModuleParent.context.errorCollector.createChild("[${index.joinToString("][")}]")
                    )
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
            moduleInstance.external.forEach { (name, port) ->
                val subSig = internal[name]?.select(selection) ?: error("Missing port \"$name\"!")
                if (port.direction.canWrite)
                    subSig.connectTo(port, project)
                if (port.direction.canRead)
                    port.connectTo(subSig, project)
            }
        }

    }

    override fun getSignal(name: String) = external[name]
}

sealed interface ListOrModuleInstance
class ModuleList(private val modules: List<ListOrModuleInstance>) : ListOrModuleInstance {
    fun <T> map(block: (ModuleInstance) -> T): List<T> {
        val list = mutableListOf<T>()
        forEach {
            list.add(block(it))
        }
        return list
    }

    suspend fun <T> suspendMap(block: suspend (ModuleInstance) -> T): List<T> {
        val list = mutableListOf<T>()
        suspendForEach {
            list.add(block(it))
        }
        return list
    }

    fun forEach(block: (ModuleInstance) -> Unit) {
        modules.forEach {
            when (it) {
                is ModuleInstance -> block(it)
                is ModuleList -> forEach(block)
            }
        }
    }

    suspend fun suspendForEach(block: suspend (ModuleInstance) -> Unit) {
        modules.forEach {
            when (it) {
                is ModuleInstance -> block(it)
                is ModuleList -> suspendForEach(block)
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