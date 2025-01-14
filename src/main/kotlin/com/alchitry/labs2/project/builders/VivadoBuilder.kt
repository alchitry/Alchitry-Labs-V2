package com.alchitry.labs2.project.builders

import com.alchitry.labs2.Env
import com.alchitry.labs2.Log
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.io.path.exists
import kotlin.io.path.readText

data object VivadoBuilder : ProjectBuilder() {
    override suspend fun buildProject(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ) = coroutineScope {
        if (Env.isMac)
            error("Can't build with Vivado on a Mac!")

        val propsFile = project.buildDirectory.resolve("constraint").resolve("au_props.xdc").toFile()
        val spiBusWidth = when (project.data.board) {
            Board.AlchitryAu, Board.AlchitryAuPlus -> 2
            Board.AlchitryAuV2 -> 4
            Board.AlchitryCu, Board.AlchitryCuV2 -> error("Cu isn't supported by the VivadoBuilder")
        }
        propsFile.writeText(
            """
            set_property BITSTREAM.GENERAL.COMPRESS TRUE [current_design]
            set_property BITSTREAM.CONFIG.CONFIGRATE 66 [current_design]
            set_property CONFIG_VOLTAGE 3.3 [current_design]
            set_property CFGBVS VCCO [current_design]
            set_property BITSTREAM.CONFIG.SPI_32BIT_ADDR NO [current_design]
            set_property BITSTREAM.CONFIG.SPI_BUSWIDTH $spiBusWidth [current_design]
            set_property BITSTREAM.CONFIG.SPI_FALL_EDGE YES [current_design]
        """.trimIndent()
        )

        val tclScript = project.buildDirectory.resolve("project.tcl").toFile()
        tclScript.bufferedWriter().use { bufferedWriter ->
            bufferedWriter.write(
                generateProjectFile(
                    project,
                    sourceFiles + project.data.ipCores.flatMap { core -> core.files.map { it.file } },
                    constraintFiles.toMutableList().apply { add(propsFile) })
            )
        }

        val vivado = Locations.vivadoBin

        if (vivado == null) {
            Log.printlnError("Couldn't find Vivado!")
            Log.showError("Vivado's location must be set in the settings before you can build!")
            return@coroutineScope
        }

        val cmd = listOf(
            vivado.absolutePath,
            "-nojournal",
            "-nolog",
            "-mode",
            "batch",
            "-source",
            tclScript.absolutePath
        )

        Log.info("Starting Vivado...")
        runProcess(cmd, this)
        Log.info("Vivado exited.")

        Log.println("")
        val binFile =
            project.buildDirectory
                .resolve("vivado")
                .resolve("${project.data.projectName}.runs")
                .resolve("impl_1")
                .resolve("$topModuleName.bin").toFile()
        if (binFile.exists()) {
            binFile.copyTo(project.binFile)

            when (didTimingPass(project)) {
                true -> Log.success("Project built successfully.")
                false -> Log.warn("Project built but failed to meet timing.")
                null -> Log.warn("Project built but timing was unchecked.")
            }

        } else {
            Log.error(
                "Bin file (${binFile.absolutePath}) could not be found! The build likely failed."
            )
        }
    }

    private suspend fun didTimingPass(project: Project): Boolean? = withContext(Dispatchers.IO) {
        val timingReport = project.buildDirectory
            .resolve("vivado")
            .resolve("${project.data.projectName}.runs")
            .resolve("impl_1")
            .resolve("alchitry_top_timing_summary_routed.rpt")
        if (!timingReport.exists()) {
            Log.warn("The timing report could not be located! Checked: $timingReport")
            return@withContext null
        }

        return@withContext timingReport.readText().contains("All user specified timing constraints are met.")
    }


    private fun generateProjectFile(project: Project, sourceFiles: List<File>, constraintFiles: List<File>): String =
        buildString {
            append("set projDir \"").append(getSanitizedPath(project.buildDirectory)).append("/vivado\"").appendLine()
            append("set projName \"").append(project.data.projectName).append("\"").appendLine()
            appendLine("set topName top")
            append("set device ").append(project.data.board.fpgaName).appendLine()
            appendLine("if {[file exists \"\$projDir\"]} { file delete -force \"\$projDir\" }")
            appendLine("create_project \$projName \"\$projDir\" -part \$device")
            appendLine("set_property design_mode RTL [get_filesets sources_1]")
            append("set verilogSources [list ").addSpacedList(sourceFiles).appendLine("]")
            appendLine("import_files -fileset [get_filesets sources_1] -force -norecurse \$verilogSources")
            append("set xdcSources [list ").addSpacedList(constraintFiles).appendLine("]")
            appendLine("read_xdc \$xdcSources")
            appendLine("set_property STEPS.WRITE_BITSTREAM.ARGS.BIN_FILE true [get_runs impl_1]")
            appendLine("update_compile_order -fileset sources_1")
            val cores = Runtime.getRuntime().availableProcessors()
            append("launch_runs -runs synth_1 -jobs ").appendLine(cores.toString())
            appendLine("wait_on_run synth_1")
            append("launch_runs impl_1 -to_step write_bitstream -jobs ").appendLine(cores.toString())
            appendLine("wait_on_run impl_1")
        }


}