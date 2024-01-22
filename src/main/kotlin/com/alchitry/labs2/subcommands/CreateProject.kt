package com.alchitry.labs2.subcommands

import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.ProjectCreator
import com.alchitry.labs2.showHelp
import kotlinx.cli.*
import java.io.File

@OptIn(ExperimentalCli::class)
class CreateProject : Subcommand("new", "Create a new project") {
    private val projName by argument(ArgType.String, "name", "New project's name").optional()
    private val listTemplates by option(ArgType.Boolean, "list", "l", "List project templates").default(false)
    private val board by option(
        ArgType.Choice(
            Board.All,
            toVariant = { Board.fromName(it) ?: error("Unknown board name $it!") },
            variantToString = { it.name }
        ),
        "board",
        "b",
        "Board used in the project"
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
            return
        }

        val projName = projName;
        if (projName == null) {
            showHelp("New project name must be specified!")
            return
        }

        val workspace = File(workspace)
        if (!workspace.exists()) {
            showHelp("The workspace ${workspace.path} does not exist!")
            return
        }

        val board = board
        if (board == null) {
            showHelp("A board must be specified to create a new project!")
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
            e.printStackTrace()
            return
        }

        println("Project created successfully!")
    }
}