package com.alchitry.labs.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.ApplicationScope
import com.alchitry.labs.Settings
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

@Composable
fun ApplicationScope.labsWindow() {
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
}