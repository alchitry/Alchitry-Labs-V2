package com.alchitry.labs2.project

import com.alchitry.labs2.Log
import com.alchitry.labs2.camelToSnakeCase
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.IPCore
import com.alchitry.labs2.project.files.SourceFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
import java.nio.file.Path
import kotlin.io.path.relativeTo


@Serializable
@SerialName("Project")
sealed interface ProjectData {
    fun upgradeToLatest(projectPath: Path): ProjectData1V2

    val version: ProjectVersion
        get() =
            when (this) {
                is ProjectData1V0 -> ProjectData1V0.version
                is ProjectData1V1 -> ProjectData1V1.version
                is ProjectData1V2 -> ProjectData1V2.version
            }

    companion object {
        val latestVersion = ProjectData1V2.version
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
    override fun upgradeToLatest(projectPath: Path): ProjectData1V2 {
        Log.info("Upgrading from project $version to ${ProjectData1V1.version}")
        return ProjectData1V1(projectName, board, sourceFiles, constraintFiles, emptySet()).upgradeToLatest(projectPath)
    }

    companion object {
        val version = ProjectVersion(1, 0)
    }
}

@Serializable
@SerialName("V1.1")
data class ProjectData1V1(
    val projectName: String,
    val board: Board,
    val sourceFiles: Set<SourceFile>,
    val constraintFiles: Set<ConstraintFile>,
    val ipCores: Set<IPCore>
) : ProjectData {
    override fun upgradeToLatest(projectPath: Path): ProjectData1V2 {
        Log.info("Upgrading from project $version to ${ProjectData1V2.version}")
        val camelCaseRegex = Regex("\\b[a-z][A-Za-z0-9]*\\b")
        val newSourceFiles = sourceFiles.map { file ->
            if (file.file is FileProvider.DiskFile) {
                val diskFile = projectPath.resolve(file.file.path).toFile()
                val fileContents = diskFile.readText()
                val newContent = camelCaseRegex.replace(fileContents) {
                    when (val newValue = it.value.camelToSnakeCase()) {
                        "io_seg" -> "io_segment"
                        "io_sel" -> "io_select"
                        else -> newValue
                    }
                }

                val newFileName = diskFile.name.camelToSnakeCase()
                val newFile = File(diskFile.parent, newFileName)
                newFile.writeText(newContent)

                if (newFile != diskFile) {
                    Log.println("Renaming ${diskFile.name} to $newFileName")
                    diskFile.delete()
                    val newPath = newFile.toPath().relativeTo(projectPath)
                    return@map SourceFile(FileProvider.DiskFile(newPath), file.top)
                }
            }
            file
        }.toSet()

        val newConstraintFiles = constraintFiles.map { file ->
            if (file.file is FileProvider.DiskFile) {
                val diskFile = projectPath.resolve(file.file.path).toFile()
                val newContent = camelCaseRegex.replace(diskFile.readText()) {
                    it.value.camelToSnakeCase()
                }
                diskFile.writeText(newContent)
                val newFileName = diskFile.name.camelToSnakeCase()
                if (newFileName != diskFile.name) {
                    Log.println("Renaming ${diskFile.name} to $newFileName")
                    val newFile = File(diskFile.parent, newFileName)
                    diskFile.renameTo(newFile)
                    val newPath = newFile.toPath().relativeTo(projectPath)
                    return@map ConstraintFile(FileProvider.DiskFile(newPath))
                }
            }
            file
        }.toSet()

        return ProjectData1V2(projectName, board, newSourceFiles, newConstraintFiles, ipCores)
    }

    companion object {
        val version = ProjectVersion(1, 1)
    }
}

@Serializable
@SerialName("V1.2")
data class ProjectData1V2(
    val projectName: String,
    val board: Board,
    val sourceFiles: Set<SourceFile>,
    val constraintFiles: Set<ConstraintFile>,
    val ipCores: Set<IPCore>
) : ProjectData {
    override fun upgradeToLatest(projectPath: Path): ProjectData1V2 = this


    companion object {
        val version = ProjectVersion(1, 2)
    }
}