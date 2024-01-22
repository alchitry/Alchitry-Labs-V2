package com.alchitry.labs2.ui.tree

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.removeFile
import com.alchitry.labs2.ui.dialogs.AcfFileDialog
import com.alchitry.labs2.ui.dialogs.DeleteFileDialog
import com.alchitry.labs2.ui.dialogs.LucidFileDialog
import com.alchitry.labs2.ui.fillMaxIntrinsic
import com.alchitry.labs2.ui.selection.SingleSelectionContext
import com.alchitry.labs2.ui.tabs.Workspace

@Composable
fun ProjectTree() {
    val project = Project.currentFlow.collectAsState().value ?: return
    val selectionContext = remember { SingleSelectionContext() }

    with(selectionContext) {
        Row(
            Modifier
                .fillMaxWidth()
                //.horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState())
        ) {
            var fileToDelete by remember { mutableStateOf<ProjectFile?>(null) }
            DeleteFileDialog(fileToDelete) { fileToDelete = null }
            Column(Modifier.fillMaxIntrinsic()) {
                var showNewLucidModule by remember { mutableStateOf(false) }
                LucidFileDialog(showNewLucidModule) { showNewLucidModule = false }
                var showNewAlchitryConstraint by remember { mutableStateOf(false) }
                AcfFileDialog(showNewAlchitryConstraint) { showNewAlchitryConstraint = false }
                TreeSection(project.projectName, 0) {
                    ContextMenuDataProvider(
                        items = {
                            listOf(
                                ContextMenuItem("New Lucid Module") {
                                    showNewLucidModule = true
                                }
                            )
                        }
                    ) {
                        TreeSection("Source Files", 1) {
                            project.sourceFiles.sortedBy { it.name }.forEach { file ->
                                key(file) {
                                    val color = project
                                        .notationCollectorFlowForFile(file)
                                        .collectAsState(null).value
                                        ?.getAllNotations()
                                        ?.minByOrNull { it.type.ordinal }
                                        ?.type?.color

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
                                        TreeItem(file.name, 2, color) {
                                            Workspace.openFile(file)
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
                        TreeSection("Constraint Files", 1) {
                            project.constraintFiles.sortedBy { it.name }.forEach { file ->
                                key(file) {
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
                                        TreeItem(file.name, 2) {
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
    }
}