package com.alchitry.labs2.project

import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.SourceFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("Project")
sealed interface ProjectData {
    fun upgradeToLatest(): ProjectData1V0

    val version: ProjectVersion
        get() =
            when (this) {
                is ProjectData1V0 -> ProjectData1V0.version
            }

    companion object {
        val latestVersion = ProjectData1V0.version
    }
}

@Serializable
@SerialName("V1.0")
data class ProjectData1V0(
    val projectName: String,
    val board: Board,
    val sourceFiles: Set<SourceFile>,
    val constraintFiles: Set<ConstraintFile>
) : ProjectData {
    override fun upgradeToLatest(): ProjectData1V0 = this

    companion object {
        val version = ProjectVersion(1, 0)
    }
}
