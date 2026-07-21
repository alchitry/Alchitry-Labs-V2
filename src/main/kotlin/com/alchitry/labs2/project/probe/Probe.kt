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

        fun buildTree(moduleInstance: ModuleInstance, parent: SignalTree?): SignalTree =
            SignalTree(moduleInstance, parent) { currentTree ->
                val children = mutableListOf<SignalTreeNode>()
                moduleInstance.internal.values.forEach {
                    if (it.direction.canRead) children.add(
                        SignalNode(
                            it,
                            currentTree
                        )
                    )
                }
                moduleInstance.context.types.dffs.values.forEach { children.add(SignalNode(it.q, currentTree)) }
                moduleInstance.context.types.sigs.values.forEach { children.add(SignalNode(it, currentTree)) }
                moduleInstance.context.types.moduleInstances.values.forEach { submoduleInstance ->
                    when (submoduleInstance) {
                        is ModuleInstance -> {
                            submoduleInstance.external.values.forEach {
                                if (it.direction.canRead) children.add(
                                    SignalNode(it, currentTree)
                                )
                            }
                            children.add(buildTree(submoduleInstance, currentTree))
                        }

                        is ModuleInstanceArray -> submoduleInstance.modules.forEach { subSubModule ->
                            subSubModule.external.values.forEach {
                                if (it.direction.canRead) children.add(
                                    SignalNode(
                                        it,
                                        currentTree
                                    )
                                )
                            }
                            children.add(buildTree(subSubModule, currentTree))
                        }
                    }
                }
                return@SignalTree children
            }

        val tree = buildTree(topModule, null)
        println(tree)

        return@withContext tree
    }
}

sealed interface SignalTreeNode

class SignalTree(
    val module: ModuleInstance,
    val parent: SignalTree?,
    childrenBuilder: (SignalTree) -> List<SignalTreeNode>
) : SignalTreeNode {
    val children: List<SignalTreeNode> = childrenBuilder(this)
    override fun toString(): String {
        return "SignalTree(module=$module, children=$children)"
    }
}

class SignalNode(
    val signal: Signal,
    val parent: SignalTree
) : SignalTreeNode {
    override fun toString(): String {
        return "SignalNode(signal=$signal)"
    }
}