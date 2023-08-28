package com.alchitry.labs.subcommands

import com.alchitry.labs.parsers.errors.ErrorManager
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import com.alchitry.labs.showHelp
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import java.io.File

@OptIn(ExperimentalCli::class)
class SimulateProject : Subcommand("sim", "Simulate a project") {
    private val project by argument(ArgType.String, "project", "Alchitry project file")

    override fun execute() {
        val project = try {
            Project.openXml(File(project))
        } catch (e: Exception) {
            System.err.println("Failed to open project:")
            System.err.println("     ${e.message}")
            return
        }

        val errorManager = ErrorManager()
        val topModule = project.parse(errorManager)

        if (topModule == null) {
            println("Failed to fully parse project!")
            print(errorManager.getReport())
            return
        }

        showHelp("Not implemented yet!")
    }
}