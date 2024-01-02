package com.alchitry.labs.project

import java.io.File

data class ProjectTemplate(val name: String, val boards: List<Board>) {
    fun copyTo(projName: String, workspace: File, board: Board): Project {
        val sourceURL = this::class.java.getResource(Locations.lucidProjects + "/" + name)
            ?: error("Failed to find project template \"$name\"")
        return ProjectCreator.clone(sourceURL, "$name.alp", projName, workspace, board)
    }
}
