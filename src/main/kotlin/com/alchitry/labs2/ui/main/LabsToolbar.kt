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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.hardware.usb.BoardLoader
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.library.VivadoIP
import com.alchitry.labs2.switchActiveWindow
import com.alchitry.labs2.ui.components.ToolbarButton
import com.alchitry.labs2.ui.dialogs.*
import com.alchitry.labs2.ui.menu.*
import com.alchitry.labs2.ui.tabs.BoardSimulationTab
import com.alchitry.labs2.ui.tabs.RegisterInterface
import com.alchitry.labs2.ui.tabs.SerialTerminal
import com.alchitry.labs2.ui.tabs.Workspace
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.AlchitryTheme
import com.alchitry.labs2.windows.LocalLabsState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LabsToolbar() {
    val scope = rememberCoroutineScope()
    val project by Project.currentFlow.collectAsState()
    val running = Project.building

    var showProjectDialog by remember { mutableStateOf(false) }
    NewProjectDialog(showProjectDialog) { showProjectDialog = false }

    var showSaveProjectAsDialog by remember { mutableStateOf(false) }
    project?.let {
        SaveAsProjectDialog(it, showSaveProjectAsDialog) { showSaveProjectAsDialog = false }
    }

    Row {
        val icon = if (Settings.darkTheme) "icons/alchitry_icon.svg" else "icons/alchitry_icon_black.svg"
        IconMenu(painterResource(icon), "Menu", colorFilter = null) {
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

                RadioMenuItem(
                    label = { Text("UI Scale") },
                    items = listOf(0.75f, 0.9f, 1f, 1.1f, 1.25f, 1.5f, 2f, 2.5f, 3f),
                    labeler = { Text("${(it * 100).roundToInt()}%") },
                    selected = Settings.uiScale
                ) {
                    Settings.uiScale = it
                }

                RadioMenuItem(
                    label = { Text("Theme") },
                    items = listOf(false, true),
                    labeler = { Text(if (it) "Dark" else "Light") },
                    selected = Settings.darkTheme
                ) {
                    Settings.darkTheme = it
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
            scope.launch(Dispatchers.Default) {
                Project.withBuildLock {
                    try {
                        block(currentProj)
                    } catch (e: Exception) {
                        if (e is CancellationException)
                            throw e
                        Log.printlnError(e.message)
                    }
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
            MenuItem({ Text("Import Existing File") }) {
                project?.let { importFilesDialog(it) }
            }
        }

        var showComponentLibrary by remember { mutableStateOf(false) }
        ComponentLibraryDialog(showComponentLibrary) { showComponentLibrary = false }

        if (project?.data?.board is Board.XilinxBoard) {
            IconMenu(painterResource("icons/add_component.svg"), "Add Component", enabled = !running) {
                MenuItem({ Text("Component Library") }) {
                    showComponentLibrary = true
                }
                MenuItem({ Text("Vivado IP Catalog") }) {
                    runWithProject {
                        VivadoIP.generateCores(it)
                    }
                }
                if (project?.data?.ipCores?.any { it.name == "mig_7series_0" } == false) {
                    MenuItem({ Text("Generate MIG Core (DDR)") }) {
                        runWithProject {
                            VivadoIP.generateMigCore(it)
                        }
                    }
                }
            }
        } else {
            ToolbarButton(
                icon = painterResource("icons/add_component.svg"),
                description = "Component Library",
                enabled = !running && project != null
            ) { showComponentLibrary = true }
        }
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

        val boards by LocalLabsState.current.detectedBoards
        var attachedToBoard by LocalLabsState.current.attachedToBoard

        val board = project?.data?.board
        val boardDetected = boards.keys.contains(board)

        val canLoad = boardDetected && project != null && !attachedToBoard

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
                try {
                    attachedToBoard = true
                    BoardLoader.load(it.data.board, 0, it.binFile, true)
                } finally {
                    attachedToBoard = false
                }
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
                        try {
                            attachedToBoard = true
                            BoardLoader.load(it.data.board, 0, it.binFile, false)
                        } finally {
                            attachedToBoard = false
                        }
                    }
                },
                icon = painterResource("icons/load_temp.svg"),
                description = "Load RAM",
                enabled = !running && canLoad
            )
        }
        ToolbarButton(
            onClick = {
                runWithProject {
                    try {
                        attachedToBoard = true
                        BoardLoader.erase(it.data.board, 0)
                    } finally {
                        attachedToBoard = false
                    }
                }
            },
            icon = painterResource("icons/erase.svg"),
            description = "Erase",
            enabled = !running && boardDetected && !attachedToBoard
        )
        IconMenu(painterResource("icons/terminal.svg"), "Tools", enabled = !running) {
            MenuItem({ Text("Serial Terminal") }) {
                Workspace.focusOrAddTab { SerialTerminal(it) }
            }
            MenuItem({ Text("Register Interface") }) {
                Workspace.focusOrAddTab { RegisterInterface(it) }
            }
        }
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
    colorFilter: ColorFilter? = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
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
            colorFilter = colorFilter,
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