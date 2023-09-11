package com.alchitry.labs.subcommands

import com.alchitry.labs.Log
import com.alchitry.labs.hardware.usb.UsbUtil
import com.alchitry.labs.hardware.usb.ftdi.LatticeSpi
import com.alchitry.labs.hardware.usb.ftdi.XilinxJtag
import com.alchitry.labs.project.Board
import com.alchitry.labs.project.Project
import com.alchitry.labs.project.openXml
import com.alchitry.labs.showHelp
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.coroutines.runBlocking
import java.io.File

@OptIn(ExperimentalCli::class)
class LoadProject : Subcommand("load", "Load a project or .bin file") {
    private val project by option(ArgType.String, "project", "p", "Alchitry project file")
    private val flash by option(ArgType.Boolean, "flash", "f", "Load project to FPGA's flash (persistent)").default(
        false
    )
    private val ram by option(ArgType.Boolean, "ram", "r", "Load project to FPGA's RAM (temporary)").default(false)

    private val list by option(ArgType.Boolean, "list", "l", "List all detected boards").default(false)
    private val device by option(ArgType.Int, "device", "d", "Index of device to load").default(0)

    private val bin by option(ArgType.String, "bin", null, "Bin file to load")
    private val board by option(
        ArgType.Choice(
            Board.All,
            toVariant = { Board.fromName(it) ?: error("Unknown board name $it!") },
            variantToString = { it.alias }
        ),
        "board",
        "b",
        "Board used in the project"
    )

    override fun execute() {
        if (flash && ram) {
            showHelp("Commands flash and ram can't both be specified!")
            return
        }

        if (project != null && bin != null) {
            showHelp("The bin flag can't be set when a project is specified!")
            return
        }

        if (list) {
            val detected = UsbUtil.detectAttachedBoards()

            if (detected.values.sum() == 0) {
                Log.println("No devices detected.")
            }
            Board.All.forEach { b ->
                val count = detected[b]
                if (count == null || count == 0)
                    return@forEach
                Log.println("Detected $count ${b.name}")
            }
            return
        }

        val project = project?.let {
            try {
                Project.openXml(File(it))
            } catch (e: Exception) {
                Log.printlnError("Failed to open project:")
                Log.printlnError("     ${e.message}")
                return@execute
            }
        }

        if (flash || ram) {
            val binFile = project?.binFile ?: bin?.let { File(it) }
            ?: run {
                showHelp("A project or bin file must be specified when loading to the FPGA.")
                return
            }

            val boardType = project?.board ?: board ?: run {
                showHelp("A board must be specified when providing a raw .bin file.")
                return
            }

            UsbUtil.openFtdiDevice(boardType, device).use { ftdi ->
                if (ftdi == null) {
                    Log.printlnError("No board of type ${boardType.name} found!")
                    return
                }
                try {
                    when (boardType) {
                        is Board.XilinxBoard -> {
                            val jtag = XilinxJtag(ftdi)
                            runBlocking {
                                jtag.init()
                                jtag.writeBin(binFile, flash, boardType)
                            }
                        }

                        Board.AlchitryCu -> {
                            if (!flash) {
                                showHelp("The Alchitry Cu doesn't support RAM loading.")
                                return
                            }
                            val spi = LatticeSpi(ftdi)
                            runBlocking {
                                spi.init()
                                spi.writeBin(binFile)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.printlnError("Error: ${e.message}")
                }
            }


        }
    }
}