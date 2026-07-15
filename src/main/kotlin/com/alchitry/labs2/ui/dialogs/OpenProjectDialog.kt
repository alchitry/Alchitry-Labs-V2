package com.alchitry.labs2.ui.dialogs

import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.ProjectAlreadyOpenException
import com.alchitry.labs2.windows.mainWindow
import java.io.File

suspend fun openProjectDialog(showLockedDialog: suspend () -> Boolean?): Project? {
    try {
        val projectFile =
            openFileDialog(
                mainWindow,
                "Open Project",
                listOf("alp"),
                allowMultiSelection = false,
                startingDirectory = File(Locations.workspace)
            )?.firstOrNull() ?: return null
        if (projectFile == Project.current?.projectFile)
            return null
        val project = try {
            Project.open(projectFile)
        } catch (_: ProjectAlreadyOpenException) {
            if (showLockedDialog() != true) {
                return null
            }
            Project.open(projectFile, forceOpen = true)
        }
        projectFile.parentFile?.parent?.let { Settings.workspace = it }
        return project
    } catch (e: Exception) {
        Log.showError("Failed to open project: ${e.message}")
        return null
    }
}