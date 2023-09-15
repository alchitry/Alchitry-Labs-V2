package com.alchitry.labs.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import com.alchitry.labs.LocalComposeWindow
import com.alchitry.labs.Log
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import com.alchitry.labs.ui.menu.MenuBar
import com.alchitry.labs.ui.menu.MenuItem
import com.alchitry.labs.ui.menu.TopMenuItem
import com.alchitry.labs.ui.misc.openFileDialog

@Composable
fun MainMenuBar() {
    val window = LocalComposeWindow.current
    MenuBar {
        TopMenuItem(label = { Text("File") }) {
            MenuItem(label = { Text("Open Project") }, onClick = { openProject(window) })
        }
    }
}

private fun openProject(window: ComposeWindow) {
    val files = openFileDialog(
        window,
        "Select a project file",
        allowedExtensions = listOf(".alp"),
        allowMultiSelection = false
    )
    val project = files.firstOrNull()?.let {
        try {
            Project.openXml(it)
        } catch (e: Throwable) {
            Log.printlnError("Failed to open project file!", e)
            null
        }
    } ?: return

    Project.current = project
}