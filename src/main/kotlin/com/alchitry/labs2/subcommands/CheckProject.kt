package com.alchitry.labs2.subcommands

import com.alchitry.labs2.parsers.notations.NotationManager
import com.alchitry.labs2.project.Project
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
            Project.open(File(project))
        } catch (e: Exception) {
            System.err.println("Failed to open project:")
            System.err.println("     ${e.message}")
            return
        }

        val notationManager = NotationManager()
        val topModule = runBlocking { project.buildContext(notationManager) }

        if (topModule == null) {
            println("Failed to fully parse project!")
        }

        print(notationManager.getReport())
    }
}