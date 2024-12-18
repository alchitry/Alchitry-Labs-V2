package com.alchitry.labs2.project

import com.alchitry.labs2.JarUtils
import com.alchitry.labs2.hardware.Board
import java.io.File
import java.net.URL

object ProjectCreator {
    fun clone(sourceFolder: URL, sourceName: String, projName: String, workspace: File, board: Board? = null): Project {
        val destination = File(workspace, projName)
        if (destination.exists())
            error("The destination ${destination.path} already exists")

        if (!workspace.canWrite())
            error("Can't write to the directory $workspace")

        if (!destination.mkdir()) {
            error("Failed to make project directory ${destination.path}")
        }

        if (!JarUtils.copyResourcesRecursively(sourceFolder, destination)) {
            error("Failed to copy project")
        }

        val projFile = File(destination, sourceName)
        if (!projFile.exists()) {
            error("Failed to find copied project file ${projFile.path}")
        }

        val newProjFile = File(destination, "$projName.alp")
        if (!projFile.renameTo(newProjFile)) {
            error("Failed to rename project file")
        }

        val project = Project.load(newProjFile)
        return project.copy(data = project.data.copy(projectName = projName, board = board ?: project.data.board))
            .apply { save() }
    }
}

