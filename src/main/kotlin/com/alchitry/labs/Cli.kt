package com.alchitry.labs

import com.alchitry.labs.parsers.lucidv2.ErrorManager
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import java.io.File

@OptIn(ExperimentalCli::class)
class Cli : Subcommand("cli", "Command Line Interface") {
    private val project by option(ArgType.String, "project", "p", "Alchitry project file")
    private val check by option(ArgType.Boolean, "check", "c", "Check project for errors without building").default(
        false
    )
    private val build by option(ArgType.Boolean, "build", "b", "Build project").default(false)
    private val flash by option(ArgType.Boolean, "flash", "f", "Load project to FPGA's flash (persistent)").default(
        false
    )
    private val ram by option(ArgType.Boolean, "ram", "r", "Load project to FPGA's RAM (temporary)").default(false)
    private val bin by option(ArgType.String, "bin", null, "Bin file to load")
    private val list by option(ArgType.Boolean, "list", "l", "List all detected boards").default(false)
    private val board by option(ArgType.Int, "device", "d", "Index of device to load").default(0)

    override fun execute() {
        if (flash && ram) {
            showHelp("Commands flash and ram can't both be specified!")
            return
        }

        if (check && build) {
            println("Warning: Command check is ignored when command build is specified.")
        }

        if (build || check && project == null) {
            showHelp("A project file must be specified when commands check or build are specified.")
            return
        }

        if (!check && !build && !flash && !ram && !list) {
            showHelp("At least one command (check, build, flash, ram, list) must be specified!")
            return
        }

        val project = project?.let {
            try {
                Project.openXml(File(it))
            } catch (e: Exception) {
                System.err.println("Failed to open project:")
                System.err.println("     ${e.message}")
                return@execute
            }
        }

        if (check && !build) {
            if (project == null) {
                showHelp("A project file must be specified when command check is specified.")
                return
            }

            val errorManager = ErrorManager()
            val topModule = project.parse(errorManager)

            if (topModule == null) {
                println("Failed to fully parse project!")
            }

            print(errorManager.getReport())
        }

        if (build) {
            if (project == null) {
                showHelp("A project file must be specified when command build is specified.")
                return
            }
            showHelp("Not yet implemented!")
        }

        if (flash || ram) {
            if (project == null && bin == null) {
                showHelp("A project or bin file must be specified when loading to the FPGA.")
                return
            }

            showHelp("Loading isn't implemented yet!")
        }
    }
}