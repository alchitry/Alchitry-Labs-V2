package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.relativeTo

@Composable
fun CopyReadOnlyDialog(
    show: Boolean,
    file: ProjectFile,
    onClose: () -> Unit
) {
    AlchitryDialog(show, "Create Editable Copy", onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)
        Column(
            Modifier.padding(10.dp).requiredWidthIn(min = 300.dp),
            verticalArrangement = spacedBy,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("The file ${file.name} is a read-only file. Create a local editable copy?")
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                var loading by remember { mutableStateOf(false) }
                Spacer(Modifier.weight(1f))
                Button(onClick = onClose, colors = AlchitryColors.current.cancelButton, enabled = !loading) {
                    Text("Cancel")
                }
                val scope = rememberCoroutineScope()
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            loading = true
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val project = Project.current ?: return@launch
                                    val constraints = project.data.constraintFiles.toMutableSet()
                                    val sources = project.data.sourceFiles.toMutableSet()
                                    var newFile: ProjectFile? = null
                                    when (file) {
                                        is ConstraintFile -> {
                                            constraints.remove(file)
                                            val newFullPath = project.constraintDirectory.resolve(file.name)
                                            if (newFullPath.exists()) {
                                                Log.error("The file ${newFullPath.name} already exists!")
                                                return@launch
                                            }
                                            newFullPath.toFile().writeText(file.readText())
                                            newFile =
                                                ConstraintFile(FileProvider.DiskFile(newFullPath.relativeTo(project.path))).also {
                                                    constraints.add(it)
                                                }
                                        }

                                        is SourceFile -> {
                                            sources.remove(file)
                                            val newFullPath = project.sourceDirectory.resolve(file.name)
                                            if (newFullPath.exists()) {
                                                Log.error("The file ${newFullPath.name} already exists!")
                                                return@launch
                                            }
                                            newFullPath.toFile().writeText(file.readText())
                                            newFile =
                                                SourceFile(FileProvider.DiskFile(newFullPath.relativeTo(project.path))).also {
                                                    sources.add(it)
                                                }
                                        }
                                    }
                                    val newProject = project.copy(
                                        data = project.data.copy(
                                            sourceFiles = sources,
                                            constraintFiles = constraints
                                        )
                                    )
                                    Project.open(newProject)
                                    newProject.save()
                                    Workspace.openFile(newFile)
                                    onClose()
                                } catch (e: IllegalStateException) {
                                    Log.showError("Failed to create copy:", e)
                                } finally {
                                    loading = false
                                }
                            }
                        }
                    ) {
                        Text("Create Copy")
                    }
                }
            }
        }
    }
}