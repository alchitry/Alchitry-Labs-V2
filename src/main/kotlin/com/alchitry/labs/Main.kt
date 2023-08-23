package com.alchitry.labs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.alchitry.labs.parsers.lucidv2.ErrorManager
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.code_editor.CodeEditor
import com.alchitry.labs.ui.code_editor.rememberCodeEditorState
import com.alchitry.labs.ui.code_editor.styles.lucid.LucidTokenizer
import com.alchitry.labs.ui.components.ResizePriority
import com.alchitry.labs.ui.components.Sash
import com.alchitry.labs.ui.components.Toolbar
import com.alchitry.labs.ui.components.rememberSashData
import com.alchitry.labs.ui.main.Console
import com.alchitry.labs.ui.theme.AlchitryTheme
import com.alchitry.labs.ui.tree.ProjectTree
import kotlinx.cli.*
import java.io.File
import kotlin.reflect.full.declaredFunctions

val LocalScale = compositionLocalOf { 1.0f }

@Composable
@Preview
fun App() {
    CompositionLocalProvider(LocalScale provides 1.0f) {
        AlchitryTheme {
            val focusManger = LocalFocusManager.current
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .pointerInput(Unit) { detectTapGestures { focusManger.clearFocus() } }
            ) {
                Column {
                    Toolbar()
                    Sash(
                        first = {
                            Sash(
                                first = {
                                    Surface(Modifier.matchParentSize()) {
                                        ProjectTree()
                                    }
                                },
                                second = {
                                    Surface(Modifier.matchParentSize()) {
                                        val state = rememberCodeEditorState(remember { LucidTokenizer() })
                                        LaunchedEffect(state) {

                                            val text =
                                                """
                                                    testBench myTestBench {
                                                        sig clk
                                                        
                                                        counter dut (.clk(clk))
                                                        
                                                        test simpleTest {
                                                            repeat(100, i) {
                                                                clk = 0
                                                                ${"$"}tick()
                                                                ${"$"}assert(dut.count == i)
                                                                clk = 1
                                                                ${"$"}tick()
                                                            }
                                                            ${"$"}assert(dut.count == 100)
                                                        }
                                                    }
                                                """.trimIndent()

                                            state.setText(text)
                                        }
                                        CodeEditor(state)
                                    }
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

fun ApplicationScope.close(windowState: WindowState) {
    Settings.windowHeight = windowState.size.height.value.toInt()
    Settings.windowWidth = windowState.size.width.value.toInt()
    Settings.maximized = windowState.placement == WindowPlacement.Maximized
    Settings.commit()
    exitApplication()
}

val LocalComposeWindow = compositionLocalOf<ComposeWindow> { error("No ComposeWindow set!") }
lateinit var mainWindow: ComposeWindow

fun ArgParser.showHelp(error: String? = null) {
    error?.let { System.err.println("Error: $it") }
    ArgParser::class.declaredFunctions.find { it.name == "makeUsage" }?.let {
        System.err.println(it.call(this))
    }
}

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    if (Env.os == Env.OS.UNKNOWN) {
        System.err.println("Warning: OS detection failed!")
    }

    val parser = ArgParser("Alchitry Labs", strictSubcommandOptionsOrder = true)

    class Cli : Subcommand("cli", "Command Line Interface") {
        val project by option(ArgType.String, "project", "p", "Alchitry project file")
        val check by option(ArgType.Boolean, "check", "c", "Check project for errors without building").default(false)
        val build by option(ArgType.Boolean, "build", "b", "Build project").default(false)
        val flash by option(ArgType.Boolean, "flash", "f", "Load project to FPGA's flash (persistent)").default(false)
        val ram by option(ArgType.Boolean, "ram", "r", "Load project to FPGA's RAM (temporary)").default(false)
        val bin by option(ArgType.String, "bin", null, "Bin file to load")
        val list by option(ArgType.Boolean, "list", "l", "List all detected boards").default(false)
        val board by option(ArgType.Int, "device", "d", "Index of device to load").default(0)

        override fun execute() {
            if (flash && ram) {
                showHelp("Commands flash and ram can't both be specified!")
                return
            }

            if (check && build) {
                println("Warning: Command check is ignored when command build is specified.")
            }

            if (build || check && project == null) {
                showHelp("A project file must be specified when commands check or build are specified.")
                return
            }

            if (!check && !build && !flash && !ram && !list) {
                showHelp("At least one command (check, build, flash, ram, list) must be specified!")
                return
            }

            val project = project?.let {
                try {
                    Project.openProject(File(it))
                } catch (e: Exception) {
                    System.err.println("Failed to open project: ${e.message}")
                    return
                }
            }

            if (check && !build) {
                if (project == null) {
                    showHelp("A project file must be specified when command check is specified.")
                    return
                }

                val errorManager = ErrorManager()
                val topModule = project.parse(errorManager)

                if (topModule == null) {
                    println("Failed to fully parse project!")
                }

                print(errorManager.getReport())
            }

            if (build) {
                if (project == null) {
                    showHelp("A project file must be specified when command build is specified.")
                    return
                }
                showHelp("Not yet implemented!")
            }

            if (flash || ram) {
                if (project == null && bin == null) {
                    showHelp("A project or bin file must be specified when loading to the FPGA.")
                    return
                }

                showHelp("Loading isn't implemented yet!")
            }
        }
    }

    class Labs : Subcommand("labs", "Launch Alchitry Labs GUI") {
        val project by option(ArgType.String, "project", "p", "Alchitry project file")

        override fun execute() {
            project?.let { Project.openProject(File(it)) }

            application {
                val windowState = rememberWindowState(
                    placement = if (Settings.maximized) WindowPlacement.Maximized else WindowPlacement.Floating,
                    size = DpSize(Settings.windowWidth.dp, Settings.windowHeight.dp)
                )

                Window(
                    state = windowState,
                    title = "Alchitry Labs - ${Env.version}",
                    onCloseRequest = { close(windowState) }
                ) {
                    SideEffect { mainWindow = this.window }
                    CompositionLocalProvider(LocalComposeWindow provides this.window) {
                        this.window
                        App()
                    }
                }
            }
        }
    }

    class Loader : Subcommand("loader", "Launch Alchitry Loader GUI") {

        override fun execute() {
            showHelp("Not yet implemented!")
        }
    }

    parser.subcommands(Cli(), Labs(), Loader())

    parser.parse(args)

    if (args.isEmpty()) {
        parser.showHelp("A subcommand must be specified!")
    }
}
