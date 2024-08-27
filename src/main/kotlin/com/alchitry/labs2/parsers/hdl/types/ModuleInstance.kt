package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.ExprEvalMode
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.lucid.context.LucidBlockContext
import com.alchitry.labs2.parsers.hdl.lucid.signals.snapshot.SnapshotOrParent
import com.alchitry.labs2.parsers.hdl.lucid.signals.snapshot.SnapshotParent
import com.alchitry.labs2.parsers.hdl.values.UndefinedSimpleWidth
import com.alchitry.labs2.parsers.hdl.values.UndefinedValue
import com.alchitry.labs2.parsers.hdl.values.Value
import com.alchitry.labs2.parsers.notations.ErrorListener
import org.antlr.v4.kotlinruntime.ParserRuleContext

class ModuleInstance(
    override val name: String,
    project: ProjectContext,
    override val parent: ModuleInstance?,
    override val module: Module,
    override val paramAssignments: Map<String, DynamicExpr>,
    parameters: Map<String, Value>,
    val connections: Map<String, SignalOrSubSignal>,
    mode: ExprEvalMode = ExprEvalMode.Default
) : ModuleInstanceOrArray, ListOrModuleInstance, TestOrModuleInstance {
    // make a copy of the module context tree, so we can prune it without disrupting others
    val moduleContext = module.context.deepCopy() as ParserRuleContext

    override val context = LucidBlockContext(
        project,
        module.sourceFile,
        this,
        notationCollector = project.notationManager.getCollector(module.sourceFile),
        mode = mode
    )

    override fun takeSnapshot(): SnapshotParent {
        val snapshots = mutableListOf<SnapshotOrParent>()
        snapshots.addAll(context.types.dffs.values.map { it.takeSnapshot() })
        snapshots.addAll(context.types.sigs.values.map { it.takeSnapshot() })
        snapshots.addAll(context.types.moduleInstances.values.map { it.takeSnapshot() })
        snapshots.addAll(internal.values.map { it.takeSnapshot() })
        return SnapshotParent(name, snapshots)
    }

    suspend fun checkParameters(errorListener: ErrorListener = context.notationCollector): Boolean =
        context.checkParameters(errorListener)

    suspend fun initialWalk() = context.initialWalk(moduleContext)

    fun getAllModules(): List<Module> {
        return (context.types.moduleInstances.values.flatMap { instOrArray ->
            when (instOrArray) {
                is ModuleInstance -> instOrArray.getAllModules()
                is ModuleInstanceArray -> instOrArray.getAllInstances().first().getAllModules()
            }
        } + listOf(module)).distinct()
    }

    fun getModuleDependents(): List<Module> {
        return context.types.moduleInstances.values.map { instOrArray ->
            when (instOrArray) {
                is ModuleInstance -> instOrArray.module
                is ModuleInstanceArray -> instOrArray.modules.first().module
            }
        }.distinct()
    }

    // Use the provided parameters or the default value from the module is it is missing
    val parameters = module.parameters.mapValues { (name, param) ->
        Signal(
            name,
            SignalDirection.Read,
            this,
            (parameters[name]
                ?: (if (param.defaultTestOnly && !mode.testing) null else param.default)
                ?: if (mode != ExprEvalMode.Default) UndefinedValue(
                    param.default?.width ?: UndefinedSimpleWidth()
                ) else error("Missing value for parameter \"${name}\" in module \"${this.name}\" of type \"${module.name}\".")),
            ExprType.Fixed
        )
    }

    val ports = module.ports.mapValues { (_, port) ->
        port.instantiate(this, project)
    }

    override val internal: Map<String, Signal> = ports.mapValues { it.value.internal }
    override val external: Map<String, Signal> = ports
        .filter { !connections.contains(it.key) }
        .mapValues { it.value.external }

    init {
        if (mode == ExprEvalMode.Default)
            connections.forEach { (name, sig) ->
                val port = ports[name]?.external ?: error("No matching port for given connection \"$name\"!")
                if (port.direction.canWrite) {
                    if (!sig.width.canAssign(port.width)) {
                        throw ConnectionException(
                            "The width of \"${sig.getSignal().name}\" does not match the width of the port \"${port.name}\".",
                            name
                        )
                    }
                    sig.connectTo(port, project)
                }
                if (port.direction.canRead) {
                    if (!port.width.canAssign(sig.width)) {
                        throw ConnectionException(
                            "The width of \"${sig.getSignal().name}\" does not match the width of the port \"${port.name}\".",
                            name
                        )
                    }
                    port.connectTo(sig, project)
                }
            }
    }


    /**
     * Generates a unique suffix to attach to this instance corresponding to the parameter set.
     */
    private fun generateSuffix(): String {
        if (parameters.isEmpty())
            return ""
        var hash = 0
        // sort the list to make it order independent
        parameters.asIterable().sortedBy { it.key }.forEach { (name, signal) ->
            hash += name.hashCode()
            hash *= 31
            hash += signal.initialValue.hashCode()
            hash *= 31
        }
        return "_${hash.toUInt().toString(16)}"
    }

    fun getInternalSignal(name: String) = internal[name] ?: parameters[name]
    override fun getSignal(name: String) = external[name]
}

class ConnectionException(override val message: String, val port: String) :
    IllegalArgumentException(message)