package com.alchitry.labs.subcommands

import com.alchitry.labs.parsers.errors.ErrorManager
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.runBlocking
import java.io.File

@OptIn(ExperimentalCli::class)
class CheckProject : Subcommand("check", "Check a project for errors") {
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
        val topModule = runBlocking { project.buildContext(errorManager) }

        if (topModule == null) {
            println("Failed to fully parse project!")
        }

        print(errorManager.getReport())
    }
}