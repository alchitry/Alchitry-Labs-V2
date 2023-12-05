package com.alchitry.labs.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.menu.*
import com.alchitry.labs.ui.theme.AlchitryTheme

@Composable
fun Toolbar() {
    Row {
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
                    MenuItem({ Text("Open Project...") }) {
                        Project.openProject()
                    }
                    MenuItem({ Text("Set Vivado Location") }) {
                        // TODO
                    }
                    MenuItem({ Text("Set iCEcube2 Location") }) {
                        // TODO
                    }
                }
            }
        }

        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/open.svg"),
            description = "New file"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/save.svg"),
            description = "Save"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/check.svg"),
            description = "Check for Errors"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/build.svg"),
            description = "Build"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/simulate.svg"),
            description = "Simulate"
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