package com.alchitry.labs2.project.builders

import com.alchitry.labs2.Env
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.io.path.exists
import kotlin.io.path.readText

data object IceCubeBuilder : ProjectBuilder() {
    private val IMP_DIR = "alchitry_imp"
    private val SYN_PROJECT_FILE = "alchitry_syn.prj"
    private val TCL_SCRIPT = "iCEcube2_flow.tcl"

    override suspend fun buildProject(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ) = coroutineScope {
        if (Env.isMac)
            error("Can't build with iCEcube2 on a Mac!")

        val iceCube = Locations.iceCube2
        val iceCubeLicense = Locations.iceCubeLicense

        if (iceCube == null) {
            Log.printlnError("Couldn't find iCEcube2!")
            Log.printlnError("iCEcube2's location must be set in the settings before you can build!")
            return@coroutineScope
        }

        if (iceCubeLicense == null) {
            Log.printlnError("Couldn't find license file for iCEcube2!")
            Log.printlnError("iCEcube2's license file's location must be set in the settings menu before you can build!")
            return@coroutineScope
        }

        val synProjectFile = project.buildDirectory.resolve(SYN_PROJECT_FILE).toFile()
        synProjectFile.bufferedWriter().use {
            it.write(generateProjectFile(project, topModuleName, sourceFiles, constraintFiles))
        }

        val tclScript = project.buildDirectory.resolve(TCL_SCRIPT).toFile()
        tclScript.bufferedWriter().use {
            it.write(generateTclScript(project, topModuleName, constraintFiles))
        }

        val synpwrapDir = iceCube.resolve("sbt_backend/bin/${if (Env.isWindows) "win32" else "linux"}/opt/synpwrap")
        val envVars = getEnvVars(iceCube, iceCubeLicense, synpwrapDir)

        val synCmd = mutableListOf<String>()
        val extension = when (Env.os) {
            Env.OS.Windows -> ".exe"
            Env.OS.Linux, Env.OS.MacOS, Env.OS.Unknown -> ""
        }
        synCmd.add(synpwrapDir.resolve("synpwrap$extension").absolutePath)
        synCmd.add("-prj")
        synCmd.add(synProjectFile.absolutePath)

        val tclCmd = mutableListOf<String>()
        tclCmd.add(Locations.getToolNamed("tclkit-8.5.17").absolutePath)
        tclCmd.add(tclScript.absolutePath)

        Log.println("Starting iCEcube2...", AlchitryColors.current.Info)
        runProcess(synCmd, scope = this, env = envVars, directory = project.buildDirectory.toFile())
        runProcess(tclCmd, scope = this, env = envVars, directory = project.buildDirectory.toFile())
        Log.println("iCEcube2 exited.", AlchitryColors.current.Info)
        Log.println("")

        val binFile =
            project.buildDirectory
                .resolve("$IMP_DIR/sbt/outputs/bitmap/${topModuleName}_bitmap.bin").toFile()
        if (binFile.exists()) {
            binFile.copyTo(project.binFile)
            when (didTimingPass(project)) {
                true -> Log.success("Project built successfully.")
                false -> Log.warn("Project built but failed to meet timing.")
                null -> Log.warn("Project built but timing was unchecked.")
            }
        } else {
            Log.println(
                "Bin file (${binFile.absolutePath}) could not be found! The build likely failed.",
                AlchitryColors.current.Error
            )
        }
    }

    private suspend fun didTimingPass(project: Project): Boolean? = withContext(Dispatchers.IO) {
        val timingReport = project.buildDirectory
            .resolve(IMP_DIR)
            .resolve("alchitry_top_cck.rpt")
        if (!timingReport.exists()) {
            Log.warn("The timing report could not be located! Checked: $timingReport")
            return@withContext null
        }

        return@withContext timingReport.readText().contains("Found 0 issues in 0 out of")
    }

    private fun getEnvVars(iceCube: File, license: File, synpwrapDir: File): Map<String, String> {
        val env = mutableMapOf<String, String>()
        env["LM_LICENSE_FILE"] = license.absolutePath
        env["SYNPLIFY_PATH"] = iceCube.resolve("synpbase").absolutePath
        env["SBT_DIR"] = iceCube.resolve("sbt_backend").absolutePath
        if (Env.isLinux) {
            env["LD_LIBRARY_PATH"] =
                "${synpwrapDir.absolutePath}${System.getenv("LD_LIBRARY_PATH")?.let { ":$it" } ?: ""}"
        }
        return env
    }

    private fun generateTclScript(project: Project, topModuleName: String, constraintFiles: List<File>): String =
        buildString {
            appendLine("#!/usr/bin/tclsh8.4")
            appendLine()
            appendLine("set device iCE40HX8K-CB132")
            append("set top_module ").appendLine(topModuleName)
            append("set proj_dir \"").append(".").appendLine("\"")
            append("set output_dir \"").append(IMP_DIR).appendLine("\"")
            append("set edif_file \"").append(topModuleName).appendLine("\"")
            append("set tool_options \":edifparser -y \\\"")
            constraintFiles.forEach { file ->
                if (file.extension == "pcf")
                    append(getRelativeSanitizedPath(file, project)).append(" ")
            }
            appendLine("\\\"\"")
            appendLine("set sbt_root \$::env(SBT_DIR)")
            appendLine("append sbt_tcl \$sbt_root \"/tcl/sbt_backend_synpl.tcl\"")
            appendLine("source \$sbt_tcl")
            appendLine("run_sbt_backend_auto \$device \$top_module \$proj_dir \$output_dir \$tool_options \$edif_file")
            appendLine("exit")
        }

    private fun generateProjectFile(
        project: Project,
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ): String =
        buildString {
            appendLine("#project files")
            sourceFiles.asReversed().forEach { file -> // reversed as the tools look for the top module last...
                append("add_file -verilog -lib work \"")
                append(getRelativeSanitizedPath(file, project))
                appendLine("\"")
            }
            constraintFiles.forEach { file ->
                if (file.extension == "sdc") {
                    append("add_file -constraint -lib work \"")
                    append(getRelativeSanitizedPath(file, project))
                    appendLine("\"")
                }
            }

            appendLine("#options")
            append("impl -add ").append(IMP_DIR).appendLine(" -type fpga")
            appendLine("set_option -vlog_std v2001")
            appendLine("set_option -project_relative_includes 1")
            appendLine("set_option -technology SBTiCE40")
            appendLine("set_option -part iCE40HX8K")
            appendLine("set_option -package CB132")
            appendLine("set_option -speed_grade")
            appendLine("set_option -part_companion \"\"")
            appendLine("set_option -frequency auto")
            appendLine("set_option -write_verilog 0")
            appendLine("set_option -write_vhdl 0")
            appendLine("set_option -maxfan 10000")
            appendLine("set_option -disable_io_insertion 0")
            appendLine("set_option -pipe 1")
            appendLine("set_option -retiming 0")
            appendLine("set_option -update_models_cp 0")
            appendLine("set_option -fixgatedclocks 2")
            appendLine("set_option -fixgeneratedclocks 0")
            appendLine("set_option -popfeed 0")
            appendLine("set_option -constprop 0")
            appendLine("set_option -createhierarchy 0")
            appendLine("set_option -symbolic_fsm_compiler 1")
            appendLine("set_option -compiler_compatible 0")
            appendLine("set_option -resource_sharing 1")
            appendLine("set_option -write_apr_constraint 1")
            appendLine("set_option -top-module $topModuleName")

            appendLine("project -result_format \"edif\"")
            append("project -result_file ./").append(IMP_DIR).append("/").append(topModuleName).appendLine(".edf")
            append("project -log_file ./").append(IMP_DIR).append("/").append(topModuleName).appendLine(".srr")

            append("impl -active ").appendLine(IMP_DIR)
            appendLine("project -run synthesis -clean")
        }
}