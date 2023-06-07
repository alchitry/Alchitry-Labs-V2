package com.alchitry.labs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.alchitry.labs.ui.code_editor.CodeEditor
import com.alchitry.labs.ui.code_editor.rememberCodeEditorState
import com.alchitry.labs.ui.components.ResizePriority
import com.alchitry.labs.ui.components.Sash
import com.alchitry.labs.ui.components.Toolbar
import com.alchitry.labs.ui.components.rememberSashData
import com.alchitry.labs.ui.laguages.LucidTokenizer
import com.alchitry.labs.ui.main.Console
import com.alchitry.labs.ui.main.MainMenuBar
import com.alchitry.labs.ui.theme.AlchitryTheme
import java.io.File

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
                    MainMenuBar()
                    Toolbar()
                    LazyColumn { }
                    Sash(
                        first = {
                            Sash(
                                first = {
                                    Surface(Modifier.matchParentSize()) {
                                        Text("PLACEHOLDER FOR TREE")
                                    }
                                },
                                second = {
                                    Surface(Modifier.matchParentSize()) {
                                        val state = rememberCodeEditorState(remember { LucidTokenizer() })
                                        LaunchedEffect(state) {

                                            val text =
                                                File("/home/justin/alchitry/Au Main Test/source/au_top.luc").readText()

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

fun main() {
    Env.os = Env.OS.LINUX
    Env.isIDE = true

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
            CompositionLocalProvider(LocalComposeWindow provides this.window) {
                this.window
                App()
            }
        }
    }
}
