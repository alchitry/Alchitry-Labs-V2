package com.alchitry.labs

import com.alchitry.labs.project.Board
import com.alchitry.labs.project.Locations
import com.alchitry.labs.project.ProjectCreator
import kotlinx.cli.*
import java.io.File

@OptIn(ExperimentalCli::class)
class CreateProject : Subcommand("proj", "Create Project") {
    private val listTemplates by option(ArgType.Boolean, "list", "l", "List project templates").default(false)
    private val create by option(ArgType.Boolean, "new", "n", "Create a new project").default(false)
    private val clone by option(ArgType.String, "clone", "c", "Clone an existing project")
    private val projName by option(ArgType.String, "name", "p", "Project name").required()
    private val board by option(
        ArgType.String, "board", "b", "Board used in the project (${
            Board::class.allSealedObjects().joinToString(", ") { it.name }
        })"
    )
    private val templateIndex by option(ArgType.Int, "template", "t", "Template to copy for a new project").default(0)
    private val workspace by option(
        ArgType.String,
        "workspace",
        "w",
        "Workspace (parent directory) to create the project in"
    ).default(Locations.workspace)

    override fun execute() {
        fun printTemplates(board: Board? = null) {
            val templates = ProjectCreator.getTemplates()
            val boards = board?.let { listOf(board) } ?: Board::class.allSealedObjects()
            boards.forEach { b ->
                println("${b.name} Project Templates:")
                templates.filter { it.boards.contains(b) }.forEachIndexed { index, template ->
                    println("    [$index] ${template.name}")
                }
                println()
            }
        }

        if (listTemplates) {
            printTemplates()
        }

        if (create && clone != null) {
            showHelp("Create and clone can't both be specified.")
            return
        }

        if (!create && clone == null && !listTemplates) {
            showHelp("At least one command (create, clone, list) must be specified.")
            return
        }

        val workspace = File(workspace)
        if (!workspace.exists()) {
            showHelp("The workspace ${workspace.path} does not exist!")
            return
        }

        if (create) {
            val boardName = board
            if (boardName == null) {
                showHelp("A board must be specified to create a new project!")
                return
            }
            val board = Board.fromName(boardName)
            if (board == null) {
                showHelp("The board name \"$boardName\" was not recognized.")
                return
            }

            val template = ProjectCreator.getTemplates().filter { it.boards.contains(board) }.getOrNull(templateIndex)

            if (template == null) {
                println("Invalid template index $templateIndex!")
                printTemplates(board)
                return
            }

            try {
                template.copyTo(projName, workspace, board)
            } catch (e: Exception) {
                println("Failed to create project:")
                println("    ${e.message}")
                return
            }

            println("Project created successfully!")
        }

        val clone = clone
        if (clone != null) {
            if (board != null) {
                showHelp("The board flag can't be used when cloning a project!")
                return
            }

            val source = File(clone)
            if (!source.exists()) {
                showHelp("The source project file ${source.path} does not exist!")
                return
            }

            try {
                ProjectCreator.clone(source, projName, workspace)
            } catch (e: Exception) {
                println("Failed to clone project:")
                println("    ${e.message}")
                return
            }

            println("Project cloned successfully!")
        }
    }
}