package com.alchitry.labs.subcommands

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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import com.alchitry.labs.*
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
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.delay
import java.awt.Dimension
import java.io.File
import javax.swing.ImageIcon

@OptIn(ExperimentalCli::class)
class Labs : Subcommand("labs", "Launch Alchitry Labs GUI") {
    private val project by option(ArgType.String, "project", "p", "Alchitry project file")

    override fun execute() {
        Env.mode = Env.Mode.Labs
        project?.let { Project.openProject(File(it)) }
        openWindow(
            "Alchitry Labs",
            Settings.labsWindowState,
            minWidth = 500,
            minHeight = 500,
            onClose = {
                Settings.labsWindowState = it
                Settings.commit()
            }
        ) {
            LabsWindow()
        }
    }
}

fun openWindow(
    title: String,
    initialWindowState: WindowState,
    packContent: Boolean = false,
    minWidth: Int = 150,
    minHeight: Int = 150,
    onClose: (WindowState) -> Unit,
    body: @Composable () -> Unit
) {
    application {
        val windowState = rememberWindowState(
            placement = initialWindowState.placement,
            size = initialWindowState.size,
            position = initialWindowState.position,
            isMinimized = initialWindowState.isMinimized
        )

        if (isTraySupported)
            Tray(
                icon = painterResource("icons/alchitry_icon.svg"),
                tooltip = title
            )

        Window(
            state = windowState,
            title = "$title - ${Env.version}",
            onCloseRequest = {
                onClose(windowState)
                exitApplication()
            }
        ) {
            var lastMinDimension by remember { mutableStateOf<Dimension?>(null) }

            LaunchedEffect(Unit) {
                window.minimumSize = Dimension(minWidth, minHeight)
                lastMinDimension = null
                delay(10) // need to wait for the window to open then resize it to the size we want
                window.size =
                    Dimension(initialWindowState.size.width.value.toInt(), initialWindowState.size.height.value.toInt())
                this@Window.window.iconImage =
                    ImageIcon(this::class.java.getResource("/icons/icon.png")).image
            }
            SideEffect { mainWindow = this.window }

            CompositionLocalProvider(LocalComposeWindow provides this.window) {
                Layout(content = body) { measurables, constraints ->
                    if (packContent) {
                        val minX = measurables.maxOf { it.minIntrinsicWidth(Int.MAX_VALUE) }
                        val minY = measurables.maxOf { it.minIntrinsicHeight(minX) }

                        val minDim = Dimension(minX.coerceAtLeast(minWidth), minY.coerceAtLeast(minHeight))
                        if (lastMinDimension == null) {
                            lastMinDimension = minDim
                            window.minimumSize = minDim
                        }
                    }

                    val placeables = measurables.map { measurable ->
                        measurable.measure(constraints)
                    }

                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeables.forEach { placeable ->
                            placeable.placeRelative(x = 0, y = 0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabsWindow() {
    CompositionLocalProvider(LocalScale provides 1.0f) {

        AlchitryTheme {
            val focusManger = LocalFocusManager.current
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
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