package com.alchitry.labs2.project.probe

import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.ModuleInstanceArray
import com.alchitry.labs2.parsers.hdl.types.Signal
import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Probe(private val project: Project) {
    suspend fun getProbableSignals(): SignalTree = withContext(Dispatchers.Default) {
        val notationManager = NotationManager()
        val context = project.buildContext(notationManager, simulating = false)
        val topModule = context?.top

        if (context == null || topModule == null) {
            error("Failed to parse project!")
        }

        fun buildTree(moduleInstance: ModuleInstance): SignalTree {
            val children = mutableListOf<SignalTreeNode>()
            moduleInstance.internal.values.forEach { if (it.direction.canRead) children.add(SignalNode(it)) }
            moduleInstance.context.types.dffs.values.forEach { children.add(SignalNode(it.q)) }
            moduleInstance.context.types.sigs.values.forEach { children.add(SignalNode(it)) }
            moduleInstance.context.types.moduleInstances.values.forEach { submoduleInstance ->
                when (submoduleInstance) {
                    is ModuleInstance -> {
                        submoduleInstance.external.values.forEach { if (it.direction.canRead) children.add(SignalNode(it)) }
                        children.add(buildTree(submoduleInstance))
                    }

                    is ModuleInstanceArray -> submoduleInstance.modules.forEach { subSubModule ->
                        subSubModule.external.values.forEach { if (it.direction.canRead) children.add(SignalNode(it)) }
                        children.add(buildTree(subSubModule))
                    }
                }
            }
            return SignalTree(moduleInstance, children)
        }

        val tree = buildTree(topModule)
        println(tree)

        return@withContext tree
    }
}

sealed interface SignalTreeNode

data class SignalTree(
    val module: ModuleInstance,
    val children: List<SignalTreeNode>
) : SignalTreeNode

data class SignalNode(
    val signal: Signal
) : SignalTreeNode