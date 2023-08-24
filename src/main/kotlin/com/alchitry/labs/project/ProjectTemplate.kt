package com.alchitry.labs.project

import java.io.File

data class ProjectTemplate(val name: String, val boards: List<Board>) {
    fun copyTo(projName: String, workspace: File, board: Board) {
        val sourceURI = this::class.java.getResource(Locations.lucidProjects + "/" + name)?.toURI()
            ?: error("Failed to find project template \"$name\"")
        val source = File(sourceURI)
        val sourceProjFile = File(source, "$name.alp")
        ProjectCreator.clone(sourceProjFile, projName, workspace, board)
    }
}
