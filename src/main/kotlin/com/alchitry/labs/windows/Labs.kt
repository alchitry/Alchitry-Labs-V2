package com.alchitry.labs.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import com.alchitry.labs.Env
import com.alchitry.labs.Settings
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.components.*
import com.alchitry.labs.ui.main.Console
import com.alchitry.labs.ui.theme.AlchitryTheme
import com.alchitry.labs.ui.tree.ProjectTree
import com.alchitry.labs.workspace.Workspace
import java.io.File

val LocalWorkspace = staticCompositionLocalOf<Workspace> { error("Workspace not set!") }

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
    ) {
        LaunchedEffect(Unit) {
            Env.mode = Env.Mode.Labs
            Settings.openProject?.let {
                if (Project.current == null) {
                    Project.openProject(File(it))
                }
            }
        }

        val workspace = remember { Workspace() }
        CompositionLocalProvider(LocalWorkspace provides workspace) {
            AlchitryTheme {
                Column {
                    WindowDecoration {
                        Toolbar()
                    }

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
                                            workspace.tabManager.content()
                                        },
                                        orientation = Orientation.Horizontal,
                                        sashData = rememberSashData(
                                            resizePriority = ResizePriority.SECOND,
                                            first = Settings.fileListWidth.toFloat()
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
                                    second = Settings.consoleHeight.toFloat()
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