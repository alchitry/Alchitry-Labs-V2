package com.alchitry.labs.project.builders

import com.alchitry.labs.Env
import com.alchitry.labs.Log
import com.alchitry.labs.project.Locations
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.theme.AlchitryColors
import kotlinx.coroutines.coroutineScope
import java.io.File

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

        val synProjectFile = project.buildDirectory.resolve(SYN_PROJECT_FILE)
        synProjectFile.bufferedWriter().use {
            it.write(generateProjectFile(topModuleName, sourceFiles, constraintFiles))
        }

        val tclScript = project.buildDirectory.resolve(TCL_SCRIPT)
        tclScript.bufferedWriter().use {
            it.write(generateTclScript(project, topModuleName, constraintFiles))
        }

        val bashScript = project.buildDirectory.resolve(if (Env.isWindows) "build.cmd" else "build.sh")
        bashScript.bufferedWriter().use {
            it.write(generateBashScript(project, iceCube, iceCubeLicense))
        }

        val cmd = mutableListOf<String>()
        if (Env.isLinux)
            cmd.add("bash")
        cmd.add(bashScript.absolutePath)

        Log.println("Starting iCEcube2...", AlchitryColors.current.Info)
        runProcess(cmd, this)
        Log.println("iCEcube2 exited.", AlchitryColors.current.Info)
        Log.println("")

        val binFile =
            project.buildDirectory
                .resolve("$IMP_DIR/sbt/outputs/bitmap/${topModuleName}_bitmap.bin")
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

    private fun generateBashScript(project: Project, iceCube: File, license: File): String = buildString {
        val export = when (Env.os) {
            Env.OS.Windows -> "SET"
            Env.OS.Linux -> "export"
            else -> error("Unsupported OS: ${Env.os}")
        }

        val synpwrapDir = iceCube.resolve("sbt_backend/bin/${if (Env.isWindows) "win32" else "linux"}/opt/synpwrap")
        val extension = when (Env.os) {
            Env.OS.Windows -> ".exe"
            Env.OS.Linux, Env.OS.MacOS, Env.OS.Unknown -> ""
        }
        if (Env.isLinux) {
            appendLine("#!/bin/bash")
            append(export).append(" LD_LIBRARY_PATH=")
                .append(synpwrapDir.absolutePath)
                .appendLine(":\$LD_LIBRARY_PATH")
        }

        append(export).append(" LM_LICENSE_FILE=").appendLine(license.absolutePath)
        append(export).append(" SYNPLIFY_PATH=").appendLine(iceCube.resolve("synpbase").absolutePath)
        append(export).append(" SBT_DIR=").appendLine(iceCube.resolve("sbt_backend").absolutePath)

        append(synpwrapDir.resolve("synpwrap$extension")).append(" -prj \"")
            .append(project.buildDirectory.resolve(SYN_PROJECT_FILE).absolutePath)
            .appendLine("\"")

        val tclKit = Locations.getToolNamed("tclkit-8.5.17").absolutePath

        append("\"").append(tclKit).append("\" \"").append(project.buildDirectory.resolve(TCL_SCRIPT).absolutePath)
            .appendLine("\"")
    }

    private fun generateTclScript(project: Project, topModuleName: String, constraintFiles: List<File>): String =
        buildString {
            appendLine("#!/usr/bin/tclsh8.4")
            appendLine()
            appendLine("set device iCE40HX8K-CB132")
            append("set top_module ").appendLine(topModuleName)
            append("set proj_dir \"").append(getSanitizedPath(project.buildDirectory)).appendLine("\"")
            append("set output_dir \"").append(IMP_DIR).appendLine("\"")
            append("set edif_file \"").append(topModuleName).appendLine("\"")
            append("set tool_options \":edifpaarser -y \\\"")
            constraintFiles.forEach { file ->
                if (file.extension == "pcf")
                    append(getSanitizedPath(file)).append(" ")
            }
            appendLine("\\\"\"")
            appendLine("set sbt_root \$::env(SBT_DIR)")
            appendLine("append sbt_tcl \$sbt_root \"/tcl/sbt_backend_synpl.tcl\"")
            appendLine("source \$sbt_tcl")
            appendLine("run_sbt_backend_auto \$device \$top_module \$proj_dir \$output_dir \$tool_options \$edif_file")
            appendLine("exit")
        }

    private fun generateProjectFile(
        topModuleName: String,
        sourceFiles: List<File>,
        constraintFiles: List<File>
    ): String =
        buildString {
            appendLine("#project files")
            sourceFiles.forEach { file ->
                append("add_file -verilog -lib work \"")
                append(getSanitizedPath(file))
                appendLine("\"")
            }
            constraintFiles.forEach { file ->
                if (file.extension == "sdc") {
                    append("add_file -constraint -lib work \"")
                    append(getSanitizedPath(file))
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

            appendLine("project -result_format \"edif\"")
            append("project -result_file ./").append(IMP_DIR).append("/").append(topModuleName).appendLine(".edf")
            append("project -log_file ./").append(IMP_DIR).append("/").append(topModuleName).appendLine(".srr")

            append("impl -active ").appendLine(IMP_DIR)
            appendLine("project -run synthesis -clean")
        }
}