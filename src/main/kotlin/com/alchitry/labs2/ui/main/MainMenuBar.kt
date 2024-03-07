package com.alchitry.labs2.ui.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.dialogs.openFileDialog
import com.alchitry.labs2.ui.menu.MenuBar
import com.alchitry.labs2.ui.menu.MenuItem
import com.alchitry.labs2.ui.menu.TopMenuItem
import com.alchitry.labs2.windows.LocalComposeWindow

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
            Project.open(it)
        } catch (e: Throwable) {
            Log.printlnError("Failed to open project file!", e)
            null
        }
    } ?: return
    Project.open(project)
}