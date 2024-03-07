package com.alchitry.labs2.ui.dialogs

import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.windows.mainWindow
import java.io.File

fun openProjectDialog(): Project? {
    try {
        val projectFile =
            openFileDialog(
                mainWindow,
                "Open Project",
                listOf(".alp"),
                allowMultiSelection = false,
                startingDirectory = Settings.workspace?.let { File(it) }
            ).firstOrNull() ?: return null
        val project = Project.open(projectFile)
        return project
    } catch (e: Exception) {
        Log.showError("Failed to open project: ${e.message}")
        return null
    }
}