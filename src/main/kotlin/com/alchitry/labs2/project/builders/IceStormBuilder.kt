package com.alchitry.labs2.project.builders

import com.alchitry.labs2.Env
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Languages
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.coroutineScope
import java.io.File
import kotlin.io.path.absolutePathString


data class ClockConstraint(
    val port: String,
    val frequency: Double // in MHz
)

data object IceStormBuilder : ProjectBuilder() {
    /**
     * Parse the sdc file for clock constraints that can be passed to nextpnr
     */
    private fun getClockConstraints(sdcFile: File): List<ClockConstraint> {
        val periodRegex = Regex("-period ([0-9.]+)")
        val portRegex = Regex("\\[get_ports (.+?)]")
        return sdcFile.inputStream().bufferedReader().readLines().mapNotNull {
            val period = periodRegex.find(it)?.groupValues?.lastOrNull() ?: return@mapNotNull null
            val port = portRegex.find(it)?.groupValues?.lastOrNull() ?: return@mapNotNull null
            val frequency = 1000000000.0 / period.toDouble() / 1000000.0
            ClockConstraint(port, frequency)
        }
    }

    override suspend fun buildProject(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ) = coroutineScope {
        val ossPrefix = "oss-cad-suite"
        val yosys = Locations.getToolNamed("$ossPrefix/bin/yosys").absolutePath
        val nextpnr = Locations.getToolNamed("$ossPrefix/bin/nextpnr-ice40").absolutePath
        val icepack = Locations.getToolNamed("$ossPrefix/bin/icepack").absolutePath
        val sv2v = Locations.getToolNamed("sv2v").absolutePath

        val ossFolder = Locations.toolsDirectory.resolve(ossPrefix)

        val environment: Map<String, String>? = when {
            Env.isWindows -> {
                val binPath = ossFolder.resolve("bin").absolutePath
                val libPath = ossFolder.resolve("lib").absolutePath
                mapOf(
                    "PATH" to "$binPath;$libPath;${System.getenv("PATH")}",
                    "PYTHON_EXECUTABLE" to ossFolder.resolve("lib/python3.exe").absolutePath
                )
            }

            Env.isMac || Env.isLinux -> {
                val binPath = ossFolder.resolve("bin").absolutePath
                val pyBinPath = ossFolder.resolve("py3bin").absolutePath
                mapOf(
                    "VIRTUAL_ENV" to ossFolder.absolutePath,
                    "PATH" to "$binPath:$pyBinPath:${System.getenv("PATH")}",
                )
            }

            else -> null
        }
        val envUnset: List<String> = listOf("PYTHONHOME")
        val runDirectory = project.buildDirectory.toFile()

        val jsonFile = project.buildDirectory.resolve("alchitry.json")

        val sv2vCommand = mutableListOf(
            sv2v,
            "-w",
            "adjacent"
        )
        sv2vCommand.addAll(sourceFiles.filter { it.extension == "sv" }.map { it.absolutePath })

        val sv2sStatus = runProcess(sv2vCommand, runDirectory, this)
        if (sv2sStatus != 0) {
            Log.printlnError("sv2v exited with status: $sv2sStatus")
            return@coroutineScope
        }

        val verilogSource = sourceFiles.map { it.parentFile.resolve("${it.nameWithoutExtension}.v") }

        val cmdFile = project.buildDirectory.resolve("yosys.cmd")
        cmdFile.toFile().writeText("synth_ice40 -json \"${jsonFile.absolutePathString()}\" -top $topModuleName")

        val yosysCmd = mutableListOf(
            yosys,
            "-s",
            cmdFile.absolutePathString(),
        )
        yosysCmd.addAll(verilogSource.map { it.absolutePath })

        Log.println("Starting yosys...", AlchitryColors.current.Info)
        val yosysStatus = runProcess(
            yosysCmd,
            workingDirectory = runDirectory,
            scope = this,
            env = environment,
            envRemove = envUnset
        )

        if (yosysStatus != 0) {
            Log.printlnError("Yosys exited with status: $yosysStatus")
            return@coroutineScope
        }

        val pcf = constraintFiles.firstOrNull { it.extension == Languages.PCF.extension }
            ?: error("Missing PCF constraint file!")

        val ascFile = project.buildDirectory.resolve("alchitry.asc")

        val sdcFile = constraintFiles.firstOrNull { it.extension == Languages.SDC.extension }
        val clockConstraints = sdcFile?.let { getClockConstraints(it) }

        val pythonConstraintFile = project.buildDirectory.resolve("clocks.py").toFile()

        pythonConstraintFile.writeText(
            buildString {
                clockConstraints?.forEach {
                    append("ctx.addClock(\"")
                    append(it.port)
                    append("\", ")
                    append(it.frequency.toString())
                    appendLine(")")
                }
            }
        )

        val nextpnrCmd = listOf(
            nextpnr,
            "--hx8k",
            "--package",
            "cb132",
            "--pre-pack",
            pythonConstraintFile.absolutePath,
            "--json",
            jsonFile.absolutePathString(),
            "--pcf",
            pcf.absolutePath,
            "--asc",
            ascFile.absolutePathString(),
            "--opt-timing",
            "--tmg-ripup"
        )

        Log.println("Starting nextpnr...", AlchitryColors.current.Info)
        val nextpnrStatus =
            runProcess(
                nextpnrCmd,
                runDirectory,
                this,
                env = environment,
                envRemove = envUnset,
                errorsRed = false
            )

        if (nextpnrStatus != 0) {
            Log.printlnError("Nextpnr exited with status: $nextpnrStatus")
            return@coroutineScope
        }

        val icepackCmd = listOf(
            icepack,
            ascFile.absolutePathString(),
            project.binFile.absolutePath
        )

        Log.println("Starting icepack...", AlchitryColors.current.Info)
        val icepackStatus = runProcess(
            icepackCmd,
            runDirectory,
            this,
            env = environment,
            envRemove = envUnset
        )

        if (icepackStatus != 0) {
            Log.printlnError("Icepack exited with status: $icepackStatus")
            return@coroutineScope
        }

        Log.println("")

        if (project.binFile.exists()) {
            Log.println("Project built successfully.", AlchitryColors.current.Success)
        } else {
            Log.println(
                "Bin file (${project.binFile.absolutePath}) could not be found! The build likely failed.",
                AlchitryColors.current.Error
            )
        }
    }
}