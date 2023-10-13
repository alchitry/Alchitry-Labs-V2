package com.alchitry.labs.project.builders

import com.alchitry.labs.Env
import com.alchitry.labs.Log
import com.alchitry.labs.project.Locations
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.theme.AlchitryColors
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.CharBuffer

object VivadoBuilder {
    suspend fun buildProject(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ) = coroutineScope {
        val tclScript = project.buildDirectory.resolve("project.tcl")
        tclScript.outputStream().bufferedWriter().use {
            it.write(generateProjectFile(project, sourceFiles, constraintFiles))
        }

        val vivado = Locations.vivado

        if (vivado == null) {
            Log.printlnError("Couldn't find Vivado!")
            Log.printlnError("Vivado's location must be set in the settings before you can build!")
            return@coroutineScope
        }

        val cmd = listOf(
            File(vivado).resolve("bin").resolve(if (Env.isWindows) "vivado.bat" else "vivado").absolutePath,
            "-nojournal",
            "-nolog",
            "-mode",
            "batch",
            "-source",
            tclScript.absolutePath
        )

        Log.println("Starting Vivado...", AlchitryColors.current.Info)
        val builder = ProcessBuilder(cmd)
        val process = try {
            withContext(Dispatchers.IO) {
                builder.start()
            }
        } catch (e: Exception) {
            Log.printlnError("Failed to start ${cmd.first()}", e)
            return@coroutineScope
        }

        val inputStreamJob = launch {
            startStreamPrinter(process, process.inputStream, false)
        }
        val errorStreamJob = launch {
            startStreamPrinter(process, process.errorStream, true)
        }

        try {
            while (process.isAlive)
                delay(100)

            inputStreamJob.join()
            errorStreamJob.join()
        } catch (e: CancellationException) {
            process.destroyForcibly()
            throw e
        }

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

    private suspend fun startStreamPrinter(process: Process, s: InputStream, red: Boolean) =
        withContext(Dispatchers.IO) {
            val color = if (red) AlchitryColors.current.Error else null
            val stringBuffer = StringBuffer(512)
            val buffer = CharBuffer.allocate(512)
            InputStreamReader(s).use { stream ->
                try {
                    while (process.isAlive) {
                        while (stream.ready()) {
                            val ct = stream.read(buffer)
                            buffer.flip()
                            if (ct == -1) return@withContext
                            val newText = buffer.toString()
                            stringBuffer.append(newText)
                            if (newText.contains("\n")) {
                                val lines = stringBuffer.toString().split("\\r?\\n".toRegex())
                                lines.forEachIndexed { i, it -> if (i != lines.size - 1) Log.println(it, color) }
                                stringBuffer.delete(0, stringBuffer.length - lines.last().length)
                            }
                        }
                        delay(100)
                    }
                    while (stream.ready()) {
                        val ct = stream.read(buffer)
                        buffer.flip()
                        if (ct == -1) return@withContext
                        Log.print(buffer.toString(), color)
                    }
                } catch (e: IOException) {
                    Log.printlnError("Failed to monitor stream!", e)
                }
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


    private fun getSanitizedPath(f: File): String {
        return getSanitizedPath(f.absolutePath)
    }

    private fun getSanitizedPath(f: String): String {
        return f.replace("\\", "/").replace(" ", "\\ ")
    }

    private fun StringBuilder.addSpacedList(list: Collection<File>): StringBuilder {
        for (s in list) {
            append("\"").append(getSanitizedPath(s.absolutePath)).append("\" ")
        }
        return this
    }
}