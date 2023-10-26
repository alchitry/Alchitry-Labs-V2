package com.alchitry.labs.project.builders

import com.alchitry.labs.Log
import com.alchitry.labs.project.Project
import java.io.File

data object IceCubeBuilder : ProjectBuilder() {
    override suspend fun buildProject(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ) {
        Log.printlnError("IceCube builder isn't implemented yet!")
    }
}