package com.alchitry.labs.subcommands

import com.alchitry.labs.hardware.usb.UsbDevice
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import com.alchitry.labs.showHelp
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import java.io.File

@OptIn(ExperimentalCli::class)
class LoadProject : Subcommand("load", "Load a project or .bin file") {
    private val project by option(ArgType.String, "project", "p", "Alchitry project file")
    private val flash by option(ArgType.Boolean, "flash", "f", "Load project to FPGA's flash (persistent)").default(
        false
    )
    private val ram by option(ArgType.Boolean, "ram", "r", "Load project to FPGA's RAM (temporary)").default(false)
    private val bin by option(ArgType.String, "bin", "b", "Bin file to load")
    private val list by option(ArgType.Boolean, "list", "l", "List all detected boards").default(false)
    private val board by option(ArgType.Int, "device", "d", "Index of device to load").default(0)

    override fun execute() {
        if (flash && ram) {
            showHelp("Commands flash and ram can't both be specified!")
            return
        }

        if (list) {
            val devices = UsbDevice.usbFindAll(Board.All)
            if (devices.isEmpty()) {
                println("No devices detected.")
                return
            }
            Board.All.forEach { b ->
                val boards = devices.filter { it.board == b }
                if (boards.isEmpty())
                    return@forEach
                println("Detected ${boards.size} ${b.name}")
            }

            UsbDevice.entryListFree(devices)
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


        if (flash || ram) {
            if (project == null && bin == null) {
                showHelp("A project or bin file must be specified when loading to the FPGA.")
                return
            }

            showHelp("Loading isn't implemented yet!")
        }
    }
}