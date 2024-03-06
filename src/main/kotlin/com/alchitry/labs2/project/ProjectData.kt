package com.alchitry.labs2.project

import com.alchitry.labs2.project.files.SourceFile
import kotlinx.serialization.Serializable

@Serializable
data class ProjectData(
    val projectName: String,
    val board: Board,
    val sourceFiles: Set<SourceFile>
)
