package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.ProjectCreator
import com.alchitry.labs2.project.ProjectTemplate
import com.alchitry.labs2.switchActiveWindow
import com.alchitry.labs2.windows.mainWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ProjectDialog(mainContent: @Composable BoxScope.() -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        mainContent()

        if (Project.currentFlow.collectAsState().value == null) {
            var show by remember { mutableStateOf(false) }
            NewProjectDialog(show) { show = false }
            Box(
                Modifier.background(Color.Black.copy(alpha = 0.7f)).matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface {
                    Column {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            DialogButton(painterResource("icons/add.svg"), "Create Project") { show = true }
                            DialogButton(painterResource("icons/open.svg"), "Open Project") { openProjectDialog() }
                            DialogButton(
                                painterResource("icons/load.svg"),
                                "Alchitry Loader"
                            ) { switchActiveWindow(Settings.WindowType.Loader) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogButton(painter: Painter, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(30.dp)
    ) {
        Image(painter, label, Modifier.size(75.dp), colorFilter = ColorFilter.tint(LocalContentColor.current))
        Text(label)
    }
}

@Composable
fun NewProjectDialog(visible: Boolean, onClose: () -> Unit) {
    AlchitryDialog(visible, "New Project", onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)

        var projectName by remember { mutableStateOf("") }
        var workspace by remember { mutableStateOf(Locations.workspace) }
        var board by remember { mutableStateOf(Settings.boardType?.let { Board.fromName(it) } ?: Board.AlchitryAuV2) }
        var template by remember { mutableStateOf<ProjectTemplate?>(null) }
        val workspaceFile by derivedStateOf { File(workspace) }

        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                OutlinedTextField(
                    projectName,
                    onValueChange = { projectName = it },
                    isError = projectName.isBlank(),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text("Project Name") }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                OutlinedTextField(
                    workspace,
                    onValueChange = { workspace = it },
                    modifier = Modifier.weight(1f).defaultMinSize(minWidth = 500.dp),
                    singleLine = true,
                    isError = !workspaceFile.isDirectory,
                    label = { Text("Workspace") }
                )
                Button(onClick = {
                    openDirectoryDialog(
                        window = mainWindow,
                        "Select a Workspace",
                        workspaceFile
                    )?.let { newWorkspace ->
                        workspace = newWorkspace.path
                    }
                }) {
                    Text("Browse...")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                BoardSelector(board) { board = it }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                TemplateSelector(template, board) { template = it }
            }
            var loading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            loading = true
                            scope.launch(Dispatchers.IO) {
                                try {
                                    template?.instantiate(projectName, workspaceFile, board)?.let {
                                        Settings.workspace = workspaceFile.absolutePath
                                        Settings.boardType = board.name
                                        onClose()
                                    }
                                } catch (e: IllegalStateException) {
                                    Log.showError("Failed to create project!", e)
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        enabled = projectName.isNotBlank() && workspaceFile.isDirectory && template != null
                    ) {
                        Text("Create Project")
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardSelector(
    board: Board,
    enabled: Boolean = true,
    onBoardChanged: (Board) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            readOnly = true,
            value = board.name,
            onValueChange = {},
            label = { Text("Board") },
            enabled = enabled,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(1000.dp))
        ) {
            Board.All.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onBoardChanged(it)
                        expanded = false
                    })
            }
        }

    }
}

@Composable
private fun TemplateSelector(
    template: ProjectTemplate?,
    board: Board,
    onTemplateChanged: (ProjectTemplate) -> Unit
) {
    var templates by remember { mutableStateOf<List<ProjectTemplate>>(emptyList()) }

    LaunchedEffect(Unit) {
        templates = withContext(Dispatchers.IO) {
            try {
                ProjectTemplate.getTemplates()
            } catch (e: IllegalStateException) {
                Log.showError("Failed to load templates!", e)
                emptyList()
            }
        }
        if (template == null) {
            templates.firstOrNull { it.boards.contains(board) }?.let(onTemplateChanged)
        }
    }

    val validTemplates by derivedStateOf { templates.filter { it.boards.contains(board) } }

    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            readOnly = true,
            value = template?.let { "${it.name} - ${it.description}" } ?: "",
            onValueChange = {},
            label = { Text("Template") },
            isError = templates.isEmpty(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            singleLine = true,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
        )
        SideEffect {
            if (validTemplates.isEmpty())
                expanded = false
        }
        if (validTemplates.isNotEmpty())
            ExposedDropdownMenu(
                expanded,
                onDismissRequest = { expanded = false },
                Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(1000.dp))
            ) {
                validTemplates.forEach {
                    DropdownMenuItem(
                        text = { Text("${it.name} - ${it.description}") },
                        onClick = {
                            onTemplateChanged(it)
                            expanded = false
                        })
                }
            }

    }
}

@Composable
fun SaveAsProjectDialog(project: Project, visible: Boolean, onClose: () -> Unit) {
    AlchitryDialog(visible, "Save Project As", onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)

        var projectName by remember { mutableStateOf("") }
        var workspace by remember { mutableStateOf(File(Locations.workspace)) }

        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                OutlinedTextField(
                    projectName,
                    onValueChange = { projectName = it },
                    isError = projectName.isBlank(),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text("New Project Name") }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                OutlinedTextField(
                    workspace.path,
                    onValueChange = { workspace = File(it) },
                    modifier = Modifier.weight(1f).defaultMinSize(minWidth = 500.dp),
                    singleLine = true,
                    isError = !workspace.isDirectory,
                    label = { Text("Workspace") }
                )
                Button(onClick = {
                    openDirectoryDialog(
                        window = mainWindow,
                        "Select a Workspace",
                        workspace
                    )?.let { newWorkspace ->
                        workspace = newWorkspace
                    }
                }) {
                    Text("Browse...")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                BoardSelector(project.data.board, enabled = false) { }
            }
            var loading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            loading = true
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val newProject = ProjectCreator.clone(
                                        project.path.toUri().toURL(),
                                        project.projectFile.name,
                                        projectName,
                                        workspace,
                                        project.data.board
                                    )
                                    Project.open(newProject)
                                    onClose()
                                } catch (e: IllegalStateException) {
                                    Log.showError("Failed to create project!", e)
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        enabled = projectName.isNotBlank() && workspace.isDirectory
                    ) {
                        Text("Create Project")
                    }
                }
            }
        }
    }
}