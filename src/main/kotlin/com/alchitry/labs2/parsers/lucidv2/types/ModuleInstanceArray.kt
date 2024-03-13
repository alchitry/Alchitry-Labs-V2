package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.signals.snapshot.SnapshotOrParent
import com.alchitry.labs2.parsers.lucidv2.signals.snapshot.SnapshotParent
import com.alchitry.labs2.parsers.lucidv2.signals.snapshot.Snapshotable
import com.alchitry.labs2.parsers.lucidv2.types.ports.Inout
import com.alchitry.labs2.parsers.lucidv2.types.ports.Input
import com.alchitry.labs2.parsers.lucidv2.types.ports.Output
import com.alchitry.labs2.parsers.lucidv2.types.ports.PortInstance
import com.alchitry.labs2.parsers.lucidv2.values.*
import com.alchitry.labs2.parsers.notations.NotationCollector

sealed interface ModuleInstanceOrArray : SignalParent, Snapshotable {
    val internal: Map<String, Signal>
    val external: Map<String, Signal>
}

class ModuleInstanceArray(
    override val name: String,
    project: ProjectContext,
    private val testOrModuleParent: TestOrModuleInstance,
    val notationCollector: NotationCollector,
    module: Module,
    val dimensions: List<Int>,
    paramProvider: (List<Int>) -> Map<String, Value>,
    signalProvider: (List<Int>) -> Map<String, SignalOrSubSignal>
) : ModuleInstanceOrArray {
    override val parent = testOrModuleParent as? ModuleInstance
    val modules: ModuleList

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

    val ports: Map<String, PortInstance>
    override val internal: Map<String, Signal>
    override val external: Map<String, Signal>

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
    fun checkPortDimensions(): Boolean {
        val template = modules.first()
        var match = true
        modules.forEach {
            template.ports.forEach { (name, port) ->
                if (it.ports[name]?.width != port.width) {
                    match = false
                }
            }
        }
        return match
    }

    init {
        require(dimensions.isNotEmpty()) { "Dimensions must not be empty!" }

        fun generateModules(dimensions: List<Int>, index: List<Int>): ListOrModuleInstance {
            val dim = dimensions.first()
            if (dimensions.size == 1) {
                return ModuleList(List(dim) {
                    val curIdx = index.toMutableList().apply { add(it) }
                    ModuleInstance(
                        name = name,
                        project = project,
                        parent = parent,
                        module = module,
                        parameters = paramProvider(curIdx),
                        connections = signalProvider(curIdx),
//                        notationCollector = testOrModuleParent.context.notationCollector.createChild(
//                            "[${
//                                curIdx.joinToString(
//                                    "]["
//                                )
//                            }]"
//                        )
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

        val exampleModule = modules.first()

        ports = exampleModule.ports.mapValues { (_, port) ->
            var width: SignalWidth = port.width
            dimensions.asReversed().forEach {
                width = if (width is BitWidth)
                    BitListWidth(it)
                else
                    DefinedArrayWidth(it, width)
            }
            when (port) {
                is Input -> Input(port.name, project, this, width, port.signed)
                is Output -> Output(port.name, project, this, width, port.signed)
                is Inout -> Inout(port.name, this, width, port.signed)
            }
        }

        internal = ports.mapValues { it.value.internal }
        external = ports
            .filter { !signalProvider(dimensions.map { 0 }).containsKey(it.key) }
            .mapValues { it.value.external }

        modules.forEachIndexed { index: List<Int>, moduleInstance: ModuleInstance ->
            val selection = index.map { SignalSelector.Bits(it, SelectionContext.Constant) }
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
    fun first(): ModuleInstance {
        var current: ListOrModuleInstance = this
        while (true) {
            when (current) {
                is ModuleInstance -> return current
                is ModuleList -> current = current.modules.first()
            }
        }
    }

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
                is ModuleList -> it.forEach(block)
            }
        }
    }

    suspend fun suspendForEach(block: suspend (ModuleInstance) -> Unit) {
        modules.forEach {
            when (it) {
                is ModuleInstance -> block(it)
                is ModuleList -> it.suspendForEach(block)
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