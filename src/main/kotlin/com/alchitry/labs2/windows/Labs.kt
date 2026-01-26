package com.alchitry.labs2.windows

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
import com.alchitry.labs2.Env
import com.alchitry.labs2.Settings
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.hardware.usb.UsbUtil
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.alchitry_text_field.Console
import com.alchitry.labs2.ui.components.ResizePriority
import com.alchitry.labs2.ui.components.Sash
import com.alchitry.labs2.ui.components.WindowDecoration
import com.alchitry.labs2.ui.components.rememberSashData
import com.alchitry.labs2.ui.dialogs.ProjectDialog
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.toolbars.LabsToolbar
import com.alchitry.labs2.ui.tree.ProjectTree
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jline.utils.Log
import java.io.File

val LocalLabsState = compositionLocalOf<LabsState> { error("No LabsState provided!") }

data class LabsState(
    val detectedBoards: MutableState<Map<Board, Int>> = mutableStateOf(emptyMap()),
    val attachedToBoard: MutableState<Boolean> = mutableStateOf(false),
)

@Composable
fun ApplicationScope.labsWindow(projectPath: String?) {
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
        val labsState = remember { LabsState() }
        LaunchedEffect(labsState.attachedToBoard.value) {
            if (labsState.attachedToBoard.value) {
                return@LaunchedEffect
            }
            withContext(Dispatchers.IO) {
                while (isActive) {
                    UsbUtil.lock.withLock {
                        labsState.detectedBoards.value = UsbUtil.detectAttachedBoards()
                    }
                    delay(1000)
                }
            }
        }

        LaunchedEffect(Unit) {
            Env.mode = Env.Mode.Labs
            (projectPath ?: Settings.openProject)?.let {
                if (Project.current == null) {
                    try {
                        Project.open(File(it))
                    } catch (e: Exception) {
                        if (projectPath != null) {
                            Log.error("Failed to open project file $it")
                            Log.error(e.message ?: "")
                        }
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

        CompositionLocalProvider(LocalLabsState provides labsState) {
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
                                        Console.main.show()
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