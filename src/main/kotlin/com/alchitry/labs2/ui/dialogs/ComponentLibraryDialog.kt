package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.addComponents
import com.alchitry.labs2.project.files.Component
import com.alchitry.labs2.project.library.ComponentLibrary
import com.alchitry.labs2.project.library.LibrarySection
import com.alchitry.labs2.ui.components.ResizePriority
import com.alchitry.labs2.ui.components.Sash
import com.alchitry.labs2.ui.components.rememberSashData
import com.alchitry.labs2.ui.selection.Selectable
import com.alchitry.labs2.ui.selection.SelectionContext
import com.alchitry.labs2.ui.selection.SingleSelectionContext
import com.alchitry.labs2.ui.tree.CheckBoxTreeItem
import com.alchitry.labs2.ui.tree.TreeSection

private val LocalSelectedComponents = compositionLocalOf<SnapshotStateList<Component>> { error("Not provided!") }

@Composable
fun ComponentLibraryDialog(visible: Boolean, onClose: () -> Unit) {
    AlchitryDialog(visible, "Component Library", resizable = true, onClose = onClose) {
        val selectedComponents = remember { mutableStateListOf<Component>() }
        val selectionContext = remember { SingleSelectionContext<Component?>() }
        CompositionLocalProvider(LocalSelectedComponents provides selectedComponents) {
            Column(Modifier.defaultMinSize(800.dp, 500.dp).background(MaterialTheme.colorScheme.background)) {
                val library = ComponentLibrary.library
                Box(Modifier.weight(1f)) {
                    Sash(
                        first = {
                            Surface(Modifier.matchParentSize()) {
                                Column(Modifier.verticalScroll(rememberScrollState())) {
                                    with(selectionContext) {
                                        LibrarySectionTree(library, 0)
                                    }
                                }
                            }
                        },
                        second = {
                            Surface(Modifier.matchParentSize()) {
                                Column(Modifier.padding(10.dp)) {
                                    val selected = selectionContext.selected?.item
                                    if (selected != null) {
                                        Text(selected.componentName, style = MaterialTheme.typography.titleLarge)
                                        Text(selected.description, Modifier.padding(top = 20.dp))
                                        if (selected.dependencies.isNotEmpty()) {
                                            Text("Dependencies", Modifier.padding(top = 20.dp))
                                            selected.dependencies.forEach {
                                                Text("• ${it.componentName}", Modifier.padding(start = 10.dp))
                                            }
                                        }
                                    }
                                    Spacer(Modifier.weight(1f))
                                    Row(
                                        Modifier.fillMaxWidth().padding(5.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            {
                                                val components =
                                                    (selectedComponents.flatMap { it.allDependencies() } + selectedComponents).distinct()
                                                Project.current?.addComponents(components)
                                                onClose()
                                            },
                                            enabled = selectedComponents.isNotEmpty()
                                        ) {
                                            Text("Add Components")
                                        }
                                    }
                                }
                            }
                        },
                        Orientation.Horizontal,
                        sashData = rememberSashData(
                            resizePriority = ResizePriority.SECOND,
                            minimumSize = 250.dp to 250.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectionContext<Component?>.LibrarySectionTree(
    section: LibrarySection,
    indentLevel: Int
) {
    val selected = LocalSelectedComponents.current
    section.components.forEach { component ->
        val alreadyInProject = Project.current?.components?.contains(component) == true
        CheckBoxTreeItem(
            component.componentName,
            indentLevel,
            remember { Selectable(component) },
            checked = selected.contains(component) || alreadyInProject,
            enabled = !alreadyInProject
        ) {
            if (it) {
                selected.add(component)
            } else {
                selected.remove(component)
            }
        }
    }
    section.subSections.forEach { (name, subSection) ->
        TreeSection(name, indentLevel, remember { Selectable(null) }, false) {
            LibrarySectionTree(subSection, indentLevel + 1)
        }
    }
}