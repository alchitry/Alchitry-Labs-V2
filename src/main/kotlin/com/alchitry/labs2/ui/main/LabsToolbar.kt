package com.alchitry.labs2.ui.main

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
import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.hardware.usb.BoardLoader
import com.alchitry.labs2.hardware.usb.UsbUtil
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.switchActiveWindow
import com.alchitry.labs2.ui.components.ToolbarButton
import com.alchitry.labs2.ui.dialogs.*
import com.alchitry.labs2.ui.menu.*
import com.alchitry.labs2.ui.tabs.BoardSimulationTab
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.AlchitryTheme
import kotlinx.coroutines.*

@Composable
fun LabsToolbar() {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    val project by Project.currentFlow.collectAsState()

    var showProjectDialog by remember { mutableStateOf(false) }
    NewProjectDialog(showProjectDialog) { showProjectDialog = false }

    var showSaveProjectAsDialog by remember { mutableStateOf(false) }
    project?.let {
        SaveAsProjectDialog(it, showSaveProjectAsDialog) { showSaveProjectAsDialog = false }
    }

    Row {
        IconMenu(painterResource("icons/alchitry_icon.svg"), "Menu") {
            MenuItem({ Text("New Project...") }) {
                showProjectDialog = true
            }
            MenuItem({ Text("Open Project...") }) {
                openProjectDialog()
            }
            MenuItem({ Text("Save Project As...") }) {
                showSaveProjectAsDialog = true
            }
            MenuParent(label = { Text("Settings") }) {
                MenuItem({ Text("Set Vivado Location") }) {
                    VivadoLocationDialog()?.let {
                        Log.info("Set Vivado Location: ${it.absolutePath}")
                        Settings.vivadoLocation = it.absolutePath
                    }
                }
                MenuItem({ Text("Set iCEcube2 Location") }) {
                    iCEcubeLocationDialog()?.let {
                        Log.info("Set iCEcube2 Location: ${it.absolutePath}")
                        Settings.iceCubeLocation = it.absolutePath
                    }
                }

                MenuItem({ Text("Set iCEcube2 License Location") }) {
                    iCEcubeLicenseDialog()?.let {
                        if (!it.exists() || !it.isFile) {
                            Log.error("${it.absolutePath} isn't a file!")
                            return@let
                        }
                        Log.info("Set iCEcube2 License Location: ${it.absolutePath}")
                        Settings.iceCubeLicense = it.absolutePath
                    }
                }

                val toolchains = listOf("Yosys (open source)", "iCEcube2 (proprietary)")
                var selectedToolchain by remember { mutableStateOf(if (Settings.useIceCube) toolchains[1] else toolchains[0]) }
                RadioMenuItem(
                    label = { Text("Cu Toolchain") },
                    items = toolchains,
                    labeler = { Text(it) },
                    selected = selectedToolchain,
                ) {
                    selectedToolchain = it
                    Settings.useIceCube = it == toolchains[1]
                }

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

        var showVerilogFileDialog by remember { mutableStateOf(false) }
        VerilogFileDialog(showVerilogFileDialog) { showVerilogFileDialog = false }

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
            MenuItem({ Text("New Verilog Module") }) {
                showVerilogFileDialog = true
            }
        }

        var showComponentLibrary by remember { mutableStateOf(false) }
        ComponentLibraryDialog(showComponentLibrary) { showComponentLibrary = false }
        ToolbarButton(
            icon = painterResource("icons/add_component.svg"),
            description = "Component Library",
            enabled = !running && project != null
        ) { showComponentLibrary = true }
        ToolbarButton(
            icon = painterResource("icons/check.svg"),
            description = "Check for Errors",
            enabled = !running && project != null
        ) { runWithProject { it.check() } }

        ToolbarButton(
            icon = painterResource("icons/debug.svg"),
            description = "Simulate",
            enabled = !running && project != null
        ) {
            runWithProject { project ->
                val context = project.check(simulating = true) ?: return@runWithProject
                val existingTab = Workspace.getTabs().firstOrNull { it is BoardSimulationTab }
                val tabPanel = existingTab?.parent ?: Workspace.activeTabPanel()
                try {
                    tabPanel.addTab(BoardSimulationTab(tabPanel, context))
                    existingTab?.let { it.parent.closeTab(it, false) }
                } catch (e: IllegalStateException) {
                    Log.error(e.message)
                }
            }

        }

        ToolbarButton(
            icon = painterResource("icons/build.svg"),
            description = "Build",
            enabled = !running && project != null
        ) {
            runWithProject { it.build() }
        }

        val board = project?.data?.board
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
            icon = painterResource("icons/load.svg"),
            description = "Load Flash",
            enabled = !running && canLoad
        ) {
            runWithProject {
                if (!it.binFileIsUpToDate()) {
                    Log.info("Bin file is outdated. Building project...")
                    if (!it.build()) {
                        Log.error("Can't load the project because the build failed!")
                        return@runWithProject
                    }
                }
                BoardLoader.load(it.data.board, 0, it.binFile, true)
            }
        }

        if (project?.data?.board?.supportsRamLoading == true) {
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
                        BoardLoader.load(it.data.board, 0, it.binFile, false)
                    }
                },
                icon = painterResource("icons/load_temp.svg"),
                description = "Load RAM",
                enabled = !running && canLoad
            )
        }
        ToolbarButton(
            onClick = {
                runWithProject { BoardLoader.erase(it.data.board, 0) }
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