package com.alchitry.labs.project

import com.alchitry.labs.JarUtils
import org.jdom2.Document
import org.jdom2.JDOMException
import org.jdom2.input.SAXBuilder
import java.io.File
import java.net.URL

object ProjectCreator {
    fun getTemplates(): List<ProjectTemplate> {
        val projectsResource = this::class.java.getResourceAsStream("${Locations.lucidProjects}/projects.xml")
            ?: error("Failed to load projects.xml")
        val builder = SAXBuilder()
        val document: Document = try {
            builder.build(projectsResource) as Document
        } catch (e: JDOMException) {
            error(e.message ?: "Failed to parse projects.xml")
        }
        val projectsXml = document.rootElement

        return projectsXml.children.map { project ->
            if (project.name != Tags.project)
                error("Projects child was not a project!")

            val name = project.getAttribute(Tags.Attributes.name)?.value ?: error("Project name was missing!")
            val boards = project.children.map { board ->
                if (board.name != Tags.board)
                    error("Expected board tag but found ${board.name}")
                Board.fromName(board.textNormalize) ?: error("Unknown board type \"${board.textNormalize}\"")
            }
            ProjectTemplate(name, boards)
        }
    }

    fun clone(sourceFolder: URL, sourceName: String, projName: String, workspace: File, board: Board? = null) {
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

        val project = Project.openXml(newProjFile)
        project.copy(projectName = projName, board = board ?: project.board).saveXML(newProjFile)
    }
}

