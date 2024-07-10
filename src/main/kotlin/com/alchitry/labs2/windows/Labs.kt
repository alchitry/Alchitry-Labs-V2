package com.alchitry.labs2.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import com.alchitry.labs2.Env
import com.alchitry.labs2.Settings
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.components.ResizePriority
import com.alchitry.labs2.ui.components.Sash
import com.alchitry.labs2.ui.components.WindowDecoration
import com.alchitry.labs2.ui.components.rememberSashData
import com.alchitry.labs2.ui.dialogs.ProjectDialog
import com.alchitry.labs2.ui.main.Console
import com.alchitry.labs2.ui.main.LabsToolbar
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryTheme
import com.alchitry.labs2.ui.tree.ProjectTree
import java.io.File

@Composable
fun ApplicationScope.labsWindow() {
    openWindow(
        "Alchitry Labs",
        Settings.labsWindowState,
        minWidth = 500.dp,
        minHeight = 500.dp,
        onClose = {
            Settings.labsWindowState = it
            Settings.openProject = Project.current?.projectFile?.absolutePath
            Settings.commit()
        }
    ) { state ->
        LaunchedEffect(Unit) {
            Env.mode = Env.Mode.Labs
            Settings.openProject?.let {
                if (Project.current == null) {
                    try {
                        Project.open(File(it))
                    } catch (_: Exception) {
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            var lastProject: Project? = null
            Project.currentFlow.collect { project ->
                if (project?.projectFile != lastProject?.projectFile) {
                    Workspace.closeAll()
                    project?.top?.let { Workspace.openFile(it) }
                }

                lastProject = project
            }
        }

        AlchitryTheme {
            Column {
                WindowDecoration(state) {
                    LabsToolbar()
                }

                ProjectDialog {
                    val focusManger = LocalFocusManager.current
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .pointerInput(Unit) { detectTapGestures { focusManger.clearFocus() } }
                    ) {
                        Column {
                            Sash(
                                first = {
                                    Sash(
                                        first = {
                                            Surface(Modifier.matchParentSize()) {
                                                ProjectTree()
                                            }
                                        },
                                        second = {
                                            Workspace.content()
                                        },
                                        orientation = Orientation.Horizontal,
                                        sashData = rememberSashData(
                                            resizePriority = ResizePriority.SECOND,
                                            first = Settings.fileListWidth.toFloat(),
                                            minimumSize = 100.dp to 100.dp
                                        ),
                                        onResize = { Settings.fileListWidth = it.size.first.toInt() }
                                    )
                                },
                                second = {
                                    Surface(Modifier.matchParentSize()) {
                                        Console.show()
                                    }
                                },
                                orientation = Orientation.Vertical,
                                sashData = rememberSashData(
                                    resizePriority = ResizePriority.FIRST,
                                    second = Settings.consoleHeight.toFloat(),
                                    minimumSize = 200.dp to 10.dp
                                ),
                                onResize = { Settings.consoleHeight = it.size.second.toInt() }
                            )
                        }
                    }
                }
            }
        }
    }
}