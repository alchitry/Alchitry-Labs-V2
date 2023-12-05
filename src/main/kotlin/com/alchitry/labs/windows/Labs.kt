package com.alchitry.labs.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import com.alchitry.labs.Settings
import com.alchitry.labs.ui.code_editor.CodeEditor
import com.alchitry.labs.ui.code_editor.rememberCodeEditorState
import com.alchitry.labs.ui.code_editor.styles.lucid.LucidTokenizer
import com.alchitry.labs.ui.components.*
import com.alchitry.labs.ui.main.Console
import com.alchitry.labs.ui.tabs.Tab
import com.alchitry.labs.ui.tabs.TabPanel
import com.alchitry.labs.ui.theme.AlchitryTheme
import com.alchitry.labs.ui.tree.ProjectTree

@Composable
fun ApplicationScope.labsWindow() {
    openWindow(
        "Alchitry Labs",
        Settings.labsWindowState,
        minWidth = 500.dp,
        minHeight = 500.dp,
        onClose = {
            Settings.labsWindowState = it
            Settings.commit()
        }
    ) {
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
                                        val panel = remember {
                                            TabPanel().apply {
                                                for (i in 0..3) {
                                                    addTab(
                                                        object : Tab {
                                                            @Composable
                                                            override fun label() {
                                                                Text("myTestBench $i")
                                                            }

                                                            @Composable
                                                            override fun content() {
                                                                Box(Modifier.layoutId(i)) {
                                                                    println("Active tab $i")
                                                                    val state =
                                                                        rememberCodeEditorState(remember { LucidTokenizer() })
                                                                    LaunchedEffect(i) {
                                                                        println("Launch $i")

                                                                        val text =
                                                                            """
                                                    testBench myTestBench $i {
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
                                                            }

                                                            override fun onClose(): Boolean {
                                                                TODO("Not yet implemented")
                                                            }

                                                        }
                                                    )
                                                }
                                            }
                                        }
                                        panel.content(Modifier.matchParentSize())
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