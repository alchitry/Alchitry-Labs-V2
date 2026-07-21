package com.alchitry.labs2.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.painterResource
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.probe.SignalNode
import com.alchitry.labs2.project.probe.SignalTree
import com.alchitry.labs2.project.probe.SignalTreeNode
import com.alchitry.labs2.ui.components.Sash
import com.alchitry.labs2.ui.components.ScrollableBox
import com.alchitry.labs2.ui.components.TextTooltipArea
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


        val selectedSignals = remember { mutableStateListOf<SignalNode>() }

        Column(Modifier.size(700.dp, 500.dp).background(MaterialTheme.colorScheme.background)) {
            Box(Modifier.weight(1f)) {
                Sash(
                    first = {
                        val selectionContext = remember { SingleSelectionContext<SignalTreeNode>() }
                        with(selectionContext) {
                            ScrollableBox(Modifier.background(MaterialTheme.colorScheme.surface)) {
                                Column(Modifier.height(IntrinsicSize.Max)) {
                                    Text(
                                        "Project Tree",
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier.padding(10.dp)
                                    )
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
                                            remember { Selectable(tree) },
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
                                                            Text(
                                                                " ${child.signal.width.prettyPrint()}",
                                                                Modifier.alpha(0.5f)
                                                            )
                                                        },
                                                        indentLevel + 1,
                                                        remember { Selectable(child) },
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
                            }
                        }

                    },
                    second = {
                        val selectionContext = remember { SingleSelectionContext<SignalTreeNode>() }
                        with(selectionContext) {
                            ScrollableBox(Modifier.background(MaterialTheme.colorScheme.surface)) {
                                Column(
                                    Modifier.height(IntrinsicSize.Min).fillMaxWidth()
                                ) {
                                    Text(
                                        "Selected Signals",
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                    val fullNames =
                                        selectedSignals.map { it to it.fullName }.sortedBy { it.second }
                                    fullNames.forEach {
                                        key(it.first) {
                                            TreeItem(
                                                {
                                                    Text(it.second)
                                                    Text(
                                                        " ${it.first.signal.width.prettyPrint()}",
                                                        Modifier.alpha(0.5f)
                                                    )
                                                },
                                                1,
                                                remember { Selectable(it.first) }
                                            ) {
                                                selectedSignals.remove(it.first)
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    },
                    orientation = Orientation.Horizontal
                )
            }
            Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
                Row {
                    var textFieldValue by remember { mutableStateOf(TextFieldValue("256")) }
                    TextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            if (newValue.text.all { it.isDigit() }) {
                                textFieldValue = newValue
                            }
                        },
                        label = { Text("Max Sample Depth") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            val number = textFieldValue.text.toIntOrNull()
                            val powerOfTwo = number != null && number > 0 && number.countOneBits() == 1
                            AnimatedVisibility(!powerOfTwo && number != null, enter = fadeIn(), exit = fadeOut()) {
                                TextTooltipArea(
                                    "The number $number is not a power of 2 and will be rounded up to ${
                                        Integer.highestOneBit(
                                            number ?: 1
                                        ) shl 1
                                    }"
                                ) {
                                    Icon(
                                        painterResource("icons/warning.svg"),
                                        contentDescription = "Warning",
                                        tint = AlchitryColors.current.Warning
                                    )
                                }
                            }
                        },
                        isError = textFieldValue.text.toIntOrNull() == null
                    )
                }
            }
        }
    }
}


private val SignalNode.listName: String
    get() {
        if (signal.parent == parent.module)
            return signal.name
        return signal.parent?.let { "${it.name}.${signal.name}" } ?: signal.name
    }

private val SignalNode.fullName: String
    get() = buildString {
        val moduleNames = mutableListOf<String>()
        var currentParent = parent
        moduleNames.add(currentParent.module.name)
        while (currentParent.parent != null) {
            currentParent = currentParent.parent
            moduleNames.add(currentParent.module.name)
        }
        append(moduleNames.asReversed().joinToString("/"))
        append("/")
        append(listName)
    }