package com.alchitry.labs2.ui.tree

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.painterResource
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.IPCore
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.library.VivadoIP
import com.alchitry.labs2.project.removeFile
import com.alchitry.labs2.ui.dialogs.AcfFileDialog
import com.alchitry.labs2.ui.dialogs.DeleteCoreDialog
import com.alchitry.labs2.ui.dialogs.DeleteFileDialog
import com.alchitry.labs2.ui.dialogs.LucidFileDialog
import com.alchitry.labs2.ui.selection.Selectable
import com.alchitry.labs2.ui.selection.SingleSelectionContext
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.launch

@Composable
fun ProjectTree() {
    val project = Project.currentFlow.collectAsState().value ?: return
    val selectionContext = remember { SingleSelectionContext<String>() }
    val scope = rememberCoroutineScope()

    with(selectionContext) {
        val horizontalScrollState = rememberScrollState()
        val verticalScrollState = rememberScrollState()
        Box {
            Box(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
                    .verticalScroll(verticalScrollState)
            ) {
                var fileToDelete by remember { mutableStateOf<ProjectFile?>(null) }
                DeleteFileDialog(fileToDelete) { fileToDelete = null }

                var coreToDelete by remember { mutableStateOf<IPCore?>(null) }
                DeleteCoreDialog(coreToDelete, scope) { coreToDelete = null }

                Column(Modifier.padding(end = 24.dp)) {
                    var showNewLucidModule by remember { mutableStateOf(false) }
                    LucidFileDialog(showNewLucidModule) { showNewLucidModule = false }
                    var showNewAlchitryConstraint by remember { mutableStateOf(false) }
                    AcfFileDialog(showNewAlchitryConstraint) { showNewAlchitryConstraint = false }
                    TreeSection({
                        Text(project.data.projectName, maxLines = 1)
                        Text(" - ", Modifier.alpha(0.5f), maxLines = 1)
                        Text(
                            project.projectFile.absolutePath,
                            Modifier.alpha(0.5f),
                            overflow = TextOverflow.StartEllipsis,
                            maxLines = 1
                        )
                    }, 0, remember { Selectable("Root") }) {
                        ContextMenuDataProvider(
                            items = {
                                listOf(
                                    ContextMenuItem("New Lucid Module") {
                                        showNewLucidModule = true
                                    }
                                )
                            }
                        ) {
                            TreeSection({ Text("Source Files") }, 1, remember { Selectable("Source Files") }) {
                                project.data.sourceFiles.sortedBy { it.name }.forEach { file ->
                                    key(file) {
                                        val color = project
                                            .notationCollectorFlowForFile(file)
                                            .collectAsState(null).value
                                            ?.getAllNotations()
                                            ?.minByOrNull { it.type.priority }
                                            ?.type?.style?.color

                                        ContextMenuArea(
                                            items = {
                                                when {
                                                    project.top == file -> emptyList()
                                                    file.isLibFile || file.isReadOnly -> listOf(
                                                        ContextMenuItem("Remove ${file.name}") {
                                                            project.removeFile(file, false)
                                                        }
                                                    )

                                                    file.file is FileProvider.DiskFile -> listOf(
                                                        ContextMenuItem("Delete ${file.name}") {
                                                            fileToDelete = file
                                                        }
                                                    )

                                                    else -> emptyList()
                                                }
                                            }
                                        ) {
                                            TreeItem(
                                                file.name,
                                                2,
                                                remember { Selectable(file.name) },
                                                color,
                                                if (file.isLibFile) painterResource("icons/component.svg") else null
                                            ) {
                                                Workspace.openFile(file)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (project.data.ipCores.isNotEmpty()) {
                            ContextMenuDataProvider(
                                items = {
                                    listOf(
                                        ContextMenuItem("Vivado IP Catalog") {
                                            if (Project.building) {
                                                Log.warn("Something is already running. Can't open Vivado IP Catalog.")
                                                return@ContextMenuItem
                                            }
                                            scope.launch {
                                                Project.withBuildLock {
                                                    VivadoIP.generateCores(project)
                                                }
                                            }
                                        }
                                    )
                                }
                            ) {
                                TreeSection({ Text("IP Cores") }, 1, remember { Selectable("IP Cores") }) {
                                    project.data.ipCores.sortedBy { it.name }.forEach { core ->
                                        key(core) {
                                            val color = if (core.stub == null) AlchitryColors.current.Error else null
                                            ContextMenuArea(
                                                items = {
                                                    listOf(
                                                        ContextMenuItem("Delete ${core.name}") {
                                                            if (Project.building) {
                                                                Log.warn("Something is already running. Can't delete IP core.")
                                                                return@ContextMenuItem
                                                            }
                                                            coreToDelete = core
                                                        }
                                                    )
                                                }
                                            ) {
                                                TreeItem(
                                                    core.name,
                                                    2,
                                                    remember { Selectable(core.name) },
                                                    color,
                                                    painterResource("icons/component.svg")
                                                ) {
                                                    core.stub?.let {
                                                        Workspace.openFile(it)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ContextMenuDataProvider(
                            items = {
                                listOf(
                                    ContextMenuItem("New Alchitry Constraint") {
                                        showNewAlchitryConstraint = true
                                    }
                                )
                            }
                        ) {
                            TreeSection({ Text("Constraint Files") }, 1, remember { Selectable("Constraint Files") }) {
                                project.data.constraintFiles.sortedBy { it.name }.forEach { file ->
                                    key(file) {
                                        val color = project
                                            .notationCollectorFlowForFile(file)
                                            .collectAsState(null).value
                                            ?.getAllNotations()
                                            ?.minByOrNull { it.type.priority }
                                            ?.type?.style?.color
                                        ContextMenuArea(
                                            items = {
                                                when {
                                                    file.isLibFile || file.isReadOnly -> listOf(
                                                        ContextMenuItem("Remove ${file.name}") {
                                                            project.removeFile(file, false)
                                                        }
                                                    )

                                                    file.file is FileProvider.DiskFile -> listOf(
                                                        ContextMenuItem("Delete ${file.name}") {
                                                            fileToDelete = file
                                                        }
                                                    )

                                                    else -> emptyList()
                                                }
                                            }
                                        ) {
                                            TreeItem(
                                                file.name,
                                                2,
                                                remember { Selectable(file.name) },
                                                color,
                                                if (file.isLibFile) painterResource("icons/component.svg") else null
                                            ) {
                                                Workspace.openFile(file)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalScrollbar(
                rememberScrollbarAdapter(horizontalScrollState),
                Modifier.align(Alignment.BottomStart).fillMaxWidth()
                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            )

            VerticalScrollbar(
                rememberScrollbarAdapter(verticalScrollState),
                Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                    .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
            )
        }
    }
}