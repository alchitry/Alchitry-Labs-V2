package com.alchitry.labs2.project

import com.alchitry.labs2.JarUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
@SerialName("Template")
data class ProjectTemplate(val name: String, val description: String, val boards: List<Board>, val priority: Int) {
    fun copyTo(projName: String, workspace: File, board: Board): Project {
        val sourceURL = this::class.java.getResource(Locations.lucidProjects + "/" + name)
            ?: error("Failed to find project template \"$name\"")
        return ProjectCreator.clone(sourceURL, "$name.alp", projName, workspace, board)
    }

    companion object {
        fun getTemplates(): List<ProjectTemplate> {
            val templates = mutableListOf<ProjectTemplate>()
            JarUtils.traverserResourceRecursively(Locations.lucidProjects) { relativePath, isDirectory ->
                if (!isDirectory && relativePath.endsWith(".alp")) {
                    val stream = this::class.java.getResourceAsStream("${Locations.lucidProjects}/$relativePath")
                        ?: error("Failed to open stream for: \"${Locations.lucidProjects}/$relativePath\"")
                    val contents = String(stream.readAllBytes())
                    val template = Json.decodeFromString<AlchitryLabsProjectData>(contents).template
                        ?: error("Template JSON missing from template project: $relativePath")
                    templates.add(template)
                }
            }
            return templates.sortedWith(compareBy({ it.priority }, { it.name }))
        }
    }
}