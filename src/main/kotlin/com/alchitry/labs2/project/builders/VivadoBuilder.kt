package com.alchitry.labs2.project.builders

import com.alchitry.labs2.Env
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.coroutineScope
import java.io.File

data object VivadoBuilder : ProjectBuilder() {
    override suspend fun buildProject(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ) = coroutineScope {
        if (Env.isMac)
            error("Can't build with Vivado on a Mac!")

        val tclScript = project.buildDirectory.resolve("project.tcl")
        tclScript.bufferedWriter().use {
            it.write(generateProjectFile(project, sourceFiles, constraintFiles))
        }

        val vivado = Locations.vivado

        if (vivado == null) {
            Log.printlnError("Couldn't find Vivado!")
            Log.printlnError("Vivado's location must be set in the settings before you can build!")
            return@coroutineScope
        }

        val cmd = listOf(
            vivado.resolve("bin").resolve(if (Env.isWindows) "vivado.bat" else "vivado").absolutePath,
            "-nojournal",
            "-nolog",
            "-mode",
            "batch",
            "-source",
            tclScript.absolutePath
        )

        Log.println("Starting Vivado...", AlchitryColors.current.Info)
        runProcess(cmd, this)
        Log.println("Vivado exited.", AlchitryColors.current.Info)

        Log.println("")
        val binFile =
            project.buildDirectory
                .resolve("vivado")
                .resolve("${project.projectName}.runs")
                .resolve("impl_1")
                .resolve("$topModuleName.bin")
        if (binFile.exists()) {
            binFile.copyTo(project.binFile)
            Log.println("Project built successfully.", AlchitryColors.current.Success)
        } else {
            Log.println(
                "Bin file (${binFile.absolutePath}) could not be found! The build likely failed.",
                AlchitryColors.current.Error
            )
        }
    }

    private fun generateProjectFile(project: Project, sourceFiles: List<File>, constraintFiles: List<File>): String =
        buildString {
            append("set projDir \"").append(getSanitizedPath(project.buildDirectory)).append("/vivado\"").appendLine()
            append("set projName \"").append(project.projectName).append("\"").appendLine()
            appendLine("set topName top")
            append("set device ").append(project.board.fpgaName).appendLine()
            appendLine("if {[file exists \"\$projDir\"]} { file delete -force \"\$projDir\" }")
            appendLine("create_project \$projName \"\$projDir\" -part \$device")
            appendLine("set_property design_mode RTL [get_filesets sources_1]")
            append("set verilogSources [list ").addSpacedList(sourceFiles).appendLine("]")
            appendLine("import_files -fileset [get_filesets sources_1] -force -norecurse \$verilogSources")
            append("set xdcSources [list ").addSpacedList(constraintFiles).appendLine("]")
            appendLine("read_xdc \$xdcSources")
            // TODO Add IP Cores
            appendLine("set_property STEPS.WRITE_BITSTREAM.ARGS.BIN_FILE true [get_runs impl_1]")
            appendLine("update_compile_order -fileset sources_1")
            val cores = Runtime.getRuntime().availableProcessors()
            append("launch_runs -runs synth_1 -jobs ").appendLine(cores.toString())
            appendLine("wait_on_run synth_1")
            append("launch_runs impl_1 -to_step write_bitstream -jobs ").appendLine(cores.toString())
            appendLine("wait_on_run impl_1")
        }


}