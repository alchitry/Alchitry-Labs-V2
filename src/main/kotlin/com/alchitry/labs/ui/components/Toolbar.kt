package com.alchitry.labs.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.Log
import com.alchitry.labs.Settings
import com.alchitry.labs.project.Project
import com.alchitry.labs.switchActiveWindow
import com.alchitry.labs.ui.menu.*
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.ui.theme.AlchitryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Toolbar() {
    val scope = rememberCoroutineScope()
    var running by remember { mutableStateOf(false) }
    Row {
        AlchitryMenu {
            MenuItem({ Text("Open Project...") }) {
                Project.openProject()
            }
            MenuItem({ Text("Set Vivado Location") }) {
                // TODO
            }
            MenuItem({ Text("Set iCEcube2 Location") }) {
                // TODO
            }
            MenuItem({ Text("Switch to Alchitry Loader") }) {
                switchActiveWindow(Settings.WindowType.Loader)
            }
        }

        fun runWithProject(block: suspend (Project) -> Unit) {
            val project = Project.current
            if (project == null) {
                Log.println("Project must be open first!", AlchitryColors.current.Error)
                return
            }
            running = true
            scope.launch(Dispatchers.Default) {
                try {
                    block(project)
                } finally {
                    running = false
                }
            }
        }

        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/open.svg"),
            description = "New file"
        )
        ToolbarButton(
            onClick = {
                runWithProject { it.check() }
            },
            icon = painterResource("icons/check.svg"),
            description = "Check for Errors",
            enabled = !running
        )
        ToolbarButton(
            onClick = {
                runWithProject { it.build() }
            },
            icon = painterResource("icons/build.svg"),
            description = "Build",
            enabled = !running
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/simulate.svg"),
            description = "Simulate",
            enabled = !running
        )
    }
}

@Preview
@Composable
fun ToolbarPreview() {
    AlchitryTheme {
        Box(Modifier.size(1000.dp).background(MaterialTheme.colorScheme.background)) {
            Toolbar()
        }
    }
}

@Composable
fun AlchitryMenu(menu: @Composable MenuBarContext.() -> Unit) {
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
            icon = painterResource("icons/alchitry_icon.svg"),
            description = "Menu",
            colorFilter = null
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