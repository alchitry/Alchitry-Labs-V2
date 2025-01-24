package com.alchitry.labs2.project.library

import com.alchitry.labs2.Log
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.builders.ProjectBuilder
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.IPCore
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.project.replaceIpCores
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

object VivadoIP {
    private val Project.coresProjectFile
        get() = ipCoreDirectory.resolve("managed_ip_project").resolve("managed_ip_project.xpr")

    private suspend fun createCoresProject(project: Project) {
        val tclScript = project.ipCoreDirectory.resolve("project.tcl")
        tclScript.writeText(buildString {
            val coresPath = project.ipCoreDirectory.absolutePathString().replace("\\", "/")
            append("cd \"")
            append(coresPath)
            append("\"")
            append(System.lineSeparator())
            append("create_project managed_ip_project \"")
            append(coresPath)
            append("/managed_ip_project\" -part ")
            append(project.data.board.fpgaName)
            append(" -ip")
        })
        runTclScript(tclScript)
    }

    suspend fun runTclScript(tclScript: Path) = coroutineScope {
        val vivado = Locations.vivadoBin
        if (vivado == null) {
            Log.error("Couldn't find Vivado!")
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
            tclScript.absolutePathString()
        )
        Log.info(cmd.joinToString(" "))
        ProjectBuilder.runProcess(cmd, tclScript.parent.toFile(), this)
    }

    private suspend fun openCoresProject(project: Project, coresProject: Path) = coroutineScope {
        val vivado = Locations.vivadoBin
        if (vivado == null) {
            Log.error("Couldn't find Vivado!")
            Log.showError("Vivado's location must be set in the settings before you can build!")
            return@coroutineScope
        }
        val cmd = listOf(
            vivado.absolutePath,
            "-nojournal",
            "-nolog",
            coresProject.absolutePathString()
        )
        Log.info(cmd.joinToString(" "))
        ProjectBuilder.runProcess(cmd, project.ipCoreDirectory.toFile(), this)
    }

    suspend fun createCoreProjectIfNeeded(project: Project) {
        val coresDirectory = project.ipCoreDirectory
        if (!coresDirectory.exists())
            coresDirectory.createDirectory()

        val coresProject = project.coresProjectFile

        if (!coresProject.exists()) {
            Log.info("No IP core project detected. Creating one...")
            createCoresProject(project)
        }
    }

    suspend fun generateCores(project: Project) = coroutineScope {
        try {
            createCoreProjectIfNeeded(project)
            Log.info("Launching Vivado...")
            openCoresProject(project, project.coresProjectFile)
            Log.info("Vivado exited...")

            checkForNewCores(project)
            Log.success("Done.")
        } catch (e: IOException) {
            Log.error("Exception while generating cores!", e)
        }
    }

    private fun findPattern(dir: File, regex: Regex, isFile: Boolean): File? {
        dir.listFiles()?.forEach { stub ->
            if (isFile == stub.isFile && stub.name.matches(regex)) {
                return stub
            }
            if (stub.isDirectory) {
                val rStub = findPattern(stub, regex, isFile)
                if (rStub != null) return rStub
            }
        }
        return null
    }

    fun checkForNewCores(project: Project) {
        val blackList = listOf("managed_ip_project", ".Xil", "ip_user_files", "project.tcl", "mig_ip.tcl")
        Log.info("Looking for cores...")
        val cores = project.ipCoreDirectory.toFile().listFiles()?.mapNotNull { dir ->
            if (blackList.contains(dir.name) || !dir.isDirectory) return@mapNotNull null
            Log.println("Looking in ${dir.name}...")
            val coreName = dir.name
            val coreFiles = mutableSetOf<FileProvider.DiskFile>()
            val xciFile = findPattern(dir, Regex(".*\\.xci"), true)
            if (xciFile != null) {
                Log.println("  Found core .xci file!")
                coreFiles.add(FileProvider.DiskFile(xciFile.toPath().relativeTo(project.path)))
                val foundStub = findPattern(dir, Regex(".*_stub\\.v"), true)
                val stub = if (foundStub != null) {
                    Log.println("  Found stub file!")
                    SourceFile(FileProvider.DiskFile(foundStub.toPath().relativeTo(project.path)))
                } else {
                    Log.error("  Could not find stub file! Did you let synthesis finish before closing Vivado?")
                    null
                }
                return@mapNotNull IPCore(coreName, stub, coreFiles)
            }
            Log.error("  Failed to find .xci file!")
            return@mapNotNull null
        }

        project.replaceIpCores(cores?.toSet() ?: emptySet())
    }

    suspend fun deleteCore(project: Project, core: IPCore) {
        try {
            val tclScript = project.ipCoreDirectory.resolve("delete_core.tcl")
            tclScript.writeText(buildString {
                val nl = System.lineSeparator()
                val fileList = core.files.joinToString(" ", "{", "}") {
                    "{${project.path.resolve(it.path).absolutePathString().replace("\\", "/")}}"
                }
                append("open_project {${project.coresProjectFile}}")
                append(nl)
                append("export_ip_user_files -of_objects  [get_files $fileList] -no_script -reset -force -quiet")
                append(nl)
                append("remove_files ")
                if (core.name.startsWith("mig"))
                    append("-fileset ${core.name} ")
                append(fileList)
                append(nl)
                append(
                    "file delete -force {${
                        project.ipCoreDirectory.resolve(core.name).absolutePathString().replace("\\", "/")
                    }}"
                )
                append(nl)
            })
            Log.info("Deleting IP core ${core.name}...")
            runTclScript(tclScript)
            tclScript.deleteIfExists()
            checkForNewCores(project)
            Log.success("Done.")
        } catch (e: IOException) {
            Log.error("Exception while deleting core.", e)
        }
    }

    suspend fun generateMigCore(project: Project) {
        try {
            val migCompFile = when (project.data.board) {
                Board.AlchitryAu -> "mig.prj"
                Board.AlchitryAuPlus -> "mig_plus.prj"
                Board.AlchitryAuV2 -> "mig_v2.prj"
                Board.AlchitryCu, Board.AlchitryCuV2 -> error("Can't generate MIG core for Cu!")
            }
            val migCompFilePath = "${Locations.cores}/${migCompFile}"
            val migProjectContents =
                this::class.java.getResourceAsStream(migCompFilePath)
                    ?.let { String(it.readAllBytes()) } ?: error("Failed to read MIG project file: $migCompFilePath")

            val migProjectFile = project.ipCoreDirectory.resolve("managed_ip_project").resolve("mig.prj")
            migProjectFile.createParentDirectories()
            migProjectFile.writeText(migProjectContents)
            val migPrjFile = "../managed_ip_project/mig.prj"
            val coresFolder = project.ipCoreDirectory.absolutePathString().replace("\\", "/")
            val xciFile =
                project.ipCoreDirectory.resolve("mig_7series_0").resolve("mig_7series_0.xci").absolutePathString()
                    .replace("\\", "/")
                    .replace(" ", "\\ ")

            val tclScript = project.ipCoreDirectory.resolve("mig_ip.tcl")

            createCoreProjectIfNeeded(project)

            tclScript.writeText(buildString {
                val nl = System.lineSeparator()
                append("open_project {${project.coresProjectFile.absolutePathString().replace("\\", "/")}}")
                append(nl)
                append("create_ip -name mig_7series -vendor xilinx.com -library ip -module_name mig_7series_0 -dir {$coresFolder}")
                append(nl)
                append("set_property -dict [list CONFIG.XML_INPUT_FILE {$migPrjFile} CONFIG.RESET_BOARD_INTERFACE ")
                append("{Custom} CONFIG.MIG_DONT_TOUCH_PARAM {Custom} CONFIG.BOARD_MIG_PARAM {Custom}] [get_ips mig_7series_0]")
                append(nl)
                append("generate_target all [get_files {$xciFile}]")
                append(nl)
                append("catch { config_ip_cache -export [get_ips -all mig_7series_0] }")
                append(nl)
                append("export_ip_user_files -of_objects [get_files {$xciFile}] -no_script -sync -force -quiet")
                append(nl)
                append("create_ip_run [get_files -of_objects [get_fileset sources_1] {$xciFile}]")
                append(nl)
                append("launch_runs -jobs 16 mig_7series_0_synth_1")
                append(nl)
                append("wait_on_run mig_7series_0_synth_1")
                append(nl)
                append("export_simulation -of_objects [get_files {$xciFile}]")
                append(" -directory {$coresFolder/ip_user_files/sim_scripts} -ip_user_files_dir {$coresFolder/ip_user_files}")
                append(" -ipstatic_source_dir {$coresFolder/ip_user_files/ipstatic}")
                append(" -lib_map_path [list {modelsim=$coresFolder/managed_ip_project/managed_ip_project.cache/compile_simlib/modelsim}")
                append(" {questa=$coresFolder/managed_ip_project/managed_ip_project.cache/compile_simlib/questa}")
                append(" {ies=$coresFolder/managed_ip_project/managed_ip_project.cache/compile_simlib/ies}")
                append(" {xcelium=$coresFolder/managed_ip_project/managed_ip_project.cache/compile_simlib/xcelium}")
                append(" {vcs=$coresFolder/managed_ip_project/managed_ip_project.cache/compile_simlib/vcs}")
                append(" {riviera=$coresFolder/managed_ip_project/managed_ip_project.cache/compile_simlib/riviera}]")
                append(" -use_ip_compiled_libs -force -quiet")
                append(nl)
            })

            Log.info("Running TCL script...")
            runTclScript(tclScript)
            checkForNewCores(project)
            Log.success("Done.")
        } catch (e: IOException) {
            Log.error("Exception while generating MIG core.", e)
        }
    }
}