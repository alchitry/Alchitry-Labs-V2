package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.probe.SignalNode
import com.alchitry.labs2.project.probe.SignalTree
import com.alchitry.labs2.ui.selection.Selectable
import com.alchitry.labs2.ui.selection.SingleSelectionContext
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.tree.TreeItem
import com.alchitry.labs2.ui.tree.TreeSection

@Composable
fun ProbeSignalSelectorDialog(visible: Boolean, onClose: (SignalTree?) -> Unit) {
    AlchitryDialog(visible, "Inspector Signal Selection", resizable = true, onClose = { onClose(null) }) {
        var fullSignalTree: SignalTree? by remember { mutableStateOf(null) }
        val project by Project.currentFlow.collectAsState()
        LaunchedEffect(project) {
            fullSignalTree = project?.probe?.getProbableSignals()
        }

        val selectionContext = remember { SingleSelectionContext<String>() }
        val selectedSignals = remember { mutableStateListOf<SignalNode>() }

        with(selectionContext) {
            val treeScrollState = rememberScrollState()
            Box(Modifier.size(700.dp, 500.dp)) {
                Column(Modifier.height(IntrinsicSize.Min).verticalScroll(treeScrollState)) {
                    val fullSignalTree = fullSignalTree
                    if (fullSignalTree == null) {
                        CircularProgressIndicator()
                        return@Column
                    }

                    @Composable
                    fun addBranch(tree: SignalTree, indentLevel: Int) {
                        TreeSection(
                            {
                                Text(tree.module.name)
                                Text(" - ", Modifier.alpha(0.5f))
                                Text(tree.module.module.name, Modifier.alpha(0.5f))
                            },
                            indentLevel,
                            remember { Selectable("Root") },
                            initiallyExpanded = indentLevel == 0
                        ) {
                            tree.children.sortedBy { child ->
                                when (child) {
                                    is SignalNode -> child.listName
                                    is SignalTree -> child.module.name
                                }
                            }.forEach { child ->
                                when (child) {
                                    is SignalNode -> TreeItem(
                                        {
                                            Text(child.listName)
                                            Text(" ${child.signal.width.prettyPrint()}", Modifier.alpha(0.5f))
                                        },
                                        indentLevel + 1,
                                        remember { Selectable("Root") },
                                        modifier = if (selectedSignals.contains(child)) Modifier.background(
                                            AlchitryColors.current.ProbeSelection
                                        ) else Modifier
                                    ) {
                                        if (selectedSignals.contains(child)) {
                                            selectedSignals.remove(child)
                                        } else {
                                            selectedSignals.add(child)
                                        }
                                    }

                                    is SignalTree -> addBranch(child, indentLevel + 1)
                                }
                            }
                        }
                    }

                    addBranch(fullSignalTree, 0)
                }
                VerticalScrollbar(
                    rememberScrollbarAdapter(treeScrollState),
                    Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                        .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                )
            }
        }

    }
}

private fun SplitTree(tree: SignalTree): Pair<List<SignalTree>, List<SignalNode>> {
    val trees = mutableListOf<SignalTree>()
    val nodes = mutableListOf<SignalNode>()
    tree.children.forEach {
        when (it) {
            is SignalNode -> nodes.add(it)
            is SignalTree -> trees.add(it)
        }
    }
    return trees.sortedBy { it.module.name } to nodes.sortedBy { it.listName }
}

private val SignalNode.listName: String
    get() {
        if (signal.parent == parent.module)
            return signal.name
        return signal.parent?.let { "${it.name}.${signal.name}" } ?: signal.name
    }
