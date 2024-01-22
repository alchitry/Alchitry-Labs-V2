package com.alchitry.labs2.subcommands

import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.ProjectCreator
import com.alchitry.labs2.showHelp
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import java.io.File

@OptIn(ExperimentalCli::class)
class CloneProject : Subcommand("clone", "Clone an existing project") {
    private val project by argument(ArgType.String, "project", "Source project file")
    private val projName by argument(ArgType.String, "name", "New project name")

    private val workspace by option(
        ArgType.String,
        "workspace",
        "w",
        "Workspace (parent directory) to create the project in"
    ).default(Locations.workspace)

    override fun execute() {
        val workspace = File(workspace)
        if (!workspace.exists()) {
            showHelp("The workspace ${workspace.path} does not exist!")
            return
        }

        val source = File(project)
        if (!source.exists()) {
            showHelp("The source project file ${source.path} does not exist!")
            return
        }

        if (!source.isFile || source.extension != "alp") {
            showHelp("The source project file ${source.path} is not an Alchitry Labs project file (.alp)!")
            return
        }

        val parent = source.parentFile?.toURI()?.toURL()
        if (parent == null) {
            println("Failed to get parent folder for $source")
            return
        }

        try {
            ProjectCreator.clone(parent, source.name, projName, workspace)
        } catch (e: Exception) {
            println("Failed to clone project:")
            println("    ${e.message}")
            return
        }

        println("Project cloned successfully!")

    }
}