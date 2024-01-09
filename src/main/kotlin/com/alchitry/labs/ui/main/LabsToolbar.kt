package com.alchitry.labs.ui.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.Log
import com.alchitry.labs.Settings
import com.alchitry.labs.hardware.usb.BoardLoader
import com.alchitry.labs.hardware.usb.UsbUtil
import com.alchitry.labs.project.Project
import com.alchitry.labs.switchActiveWindow
import com.alchitry.labs.ui.components.ToolbarButton
import com.alchitry.labs.ui.dialogs.AcfFileDialog
import com.alchitry.labs.ui.dialogs.LucidFileDialog
import com.alchitry.labs.ui.dialogs.LucidTestBenchDialog
import com.alchitry.labs.ui.dialogs.NewProjectDialog
import com.alchitry.labs.ui.menu.*
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.ui.theme.AlchitryTheme
import kotlinx.coroutines.*

@Composable
fun LabsToolbar() {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    val project by Project.currentFlow.collectAsState()
    var showProjectDialog by remember { mutableStateOf(false) }
    NewProjectDialog(showProjectDialog) { showProjectDialog = false }
    Row {
        IconMenu(painterResource("icons/alchitry_icon.svg"), "Menu") {
            MenuItem({ Text("New Project...") }) {
                showProjectDialog = true
            }
            MenuItem({ Text("Open Project...") }) {
                Project.open()
            }
            MenuItem({ Text("Set Vivado Location") }) {
                Log.warn("Not implemented yet!") // TODO
            }
            MenuItem({ Text("Set iCEcube2 Location") }) {
                Log.warn("Not implemented yet!") // TODO
            }
            MenuItem({ Text("Switch to Alchitry Loader") }) {
                switchActiveWindow(Settings.WindowType.Loader)
            }
        }

        fun runWithProject(block: suspend (Project) -> Unit) {
            val currentProj = Project.current
            if (currentProj == null) {
                Log.println("Project must be open first!", AlchitryColors.current.Error)
                return
            }
            running = true
            scope.launch(Dispatchers.Default) {
                try {
                    block(currentProj)
                } catch (e: Exception) {
                    if (e is CancellationException)
                        throw e
                    Log.printlnError(e.message)
                } finally {
                    running = false
                }
            }
        }

        var showLucidFileDialog by remember { mutableStateOf(false) }
        LucidFileDialog(showLucidFileDialog) { showLucidFileDialog = false }

        var showLucidTestBenchDialog by remember { mutableStateOf(false) }
        LucidTestBenchDialog(showLucidTestBenchDialog) { showLucidTestBenchDialog = false }

        var showACFFileDialog by remember { mutableStateOf(false) }
        AcfFileDialog(showACFFileDialog) { showACFFileDialog = false }

        IconMenu(painterResource("icons/open.svg"), "New File", enabled = project != null) {
            MenuItem({ Text("New Lucid Module") }) {
                showLucidFileDialog = true
            }
            MenuItem({ Text("New Lucid Test Bench") }) {
                showLucidTestBenchDialog = true
            }
            MenuItem({ Text("New Alchitry Constraints") }) {
                showACFFileDialog = true
            }
        }
        ToolbarButton(
            onClick = {
                runWithProject { it.check() }
            },
            icon = painterResource("icons/check.svg"),
            description = "Check for Errors",
            enabled = !running && project != null
        )

        ToolbarButton(
            onClick = {
                Log.warn("Not implemented yet!") // TODO
            },
            icon = painterResource("icons/debug.svg"),
            description = "Simulate",
            enabled = !running && project != null
        )
        ToolbarButton(
            onClick = {
                runWithProject { it.build() }
            },
            icon = painterResource("icons/build.svg"),
            description = "Build",
            enabled = !running && project != null
        )

        val board = project?.board
        var boardDetected by remember { mutableStateOf(false) }
        LaunchedEffect(board, running) {
            if (board == null || running)
                return@LaunchedEffect

            while (isActive) {
                val boards = UsbUtil.detectAttachedBoards()
                boardDetected = boards.keys.contains(board)
                delay(1000)
            }
        }

        val canLoad = boardDetected && project != null

        ToolbarButton(
            onClick = {
                runWithProject {
                    if (!it.binFileIsUpToDate()) {
                        Log.info("Bin file is outdated. Building project...")
                        if (!it.build()) {
                            Log.error("Can't load the project because the build failed!")
                            return@runWithProject
                        }
                    }
                    BoardLoader.load(it.board, 0, it.binFile, true)
                }
            },
            icon = painterResource("icons/load.svg"),
            description = "Load Flash",
            enabled = !running && canLoad
        )
        if (project?.board?.supportsRamLoading == true) {
            ToolbarButton(
                onClick = {
                    runWithProject {
                        if (!it.binFileIsUpToDate()) {
                            Log.info("Bin file is outdated. Building project...")
                            if (!it.build()) {
                                Log.error("Can't load the project because the build failed!")
                                return@runWithProject
                            }
                        }
                        BoardLoader.load(it.board, 0, it.binFile, false)
                    }
                },
                icon = painterResource("icons/load_temp.svg"),
                description = "Load RAM",
                enabled = !running && canLoad
            )
        }
        ToolbarButton(
            onClick = {
                runWithProject { BoardLoader.erase(it.board, 0) }
            },
            icon = painterResource("icons/erase.svg"),
            description = "Erase",
            enabled = !running && boardDetected
        )
    }
}

@Preview
@Composable
fun ToolbarPreview() {
    AlchitryTheme {
        Box(Modifier.size(1000.dp).background(MaterialTheme.colorScheme.background)) {
            LabsToolbar()
        }
    }
}

@Composable
fun IconMenu(
    iconPainter: Painter,
    description: String,
    enabled: Boolean = true,
    menu: @Composable MenuBarContext.() -> Unit
) {
    MenuBar {
        val menuBarItem = remember { MenuBarItem() }
        val active = focused.value === menuBarItem && isActive.value

        val subFocus = remember { mutableStateOf<MenuBarItem?>(null) }
        val menuBarContext = remember {
            object : MenuBarContext {
                override val isActive = this@MenuBar.isActive
                override val focused = subFocus
                override fun dismiss() {
                    this@MenuBar.dismiss()
                }

                override fun requestFocus(item: MenuBarItem) {
                    subFocus.value = item
                }
            }
        }
        ToolbarButton(
            onClick = {
                requestFocus(menuBarItem)
            },
            icon = iconPainter,
            description = description,
            colorFilter = null,
            enabled = enabled
        )
        with(menuBarContext) {
            MenuBarDropdown(
                expanded = active,
                onDismissRequest = { dismiss() },
            ) {
                menu()
            }
        }
    }
}