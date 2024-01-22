package com.alchitry.labs2.project

import com.alchitry.labs2.Env
import com.alchitry.labs2.PathUtil
import com.alchitry.labs2.Settings
import java.io.File
import java.io.FileFilter
import java.nio.file.Paths
import javax.swing.filechooser.FileSystemView

object Locations {
    val library: String = "/library"
    val components: String = "$library/components"
    val project: String = "$library/projects"
    val lucidProjects = "$project/Lucid"
    val verilogProjects = "$project/Verilog"
    val toolsDirectory: File =
        System.getProperty("app.dir")?.let { Paths.get(it).toFile() } ?: error("System property \"app.dir\" isn't set!")
    val binDir = toolsDirectory.resolve("bin")

    val workspace: String = Settings.workspace ?: PathUtil.assemblePath(
        FileSystemView.getFileSystemView().defaultDirectory.path,
        "alchitry"
    )

    /**
     * Returns the [File] of the tool with the given name.
     * @throws [IllegalArgumentException] if the file doesn't exist.
     * @param name the name of the tool to find
     * @return the [File] pointing to the tool
     */
    fun getToolNamed(name: String): File {
        val extension = when (Env.os) {
            Env.OS.Windows -> ".exe"
            Env.OS.Linux, Env.OS.MacOS, Env.OS.Unknown -> ""
        }
        val file = binDir.resolve("$name$extension")
        require(file.exists()) { "Failed to locate $name: ${file.absolutePath}" }
        return file
    }

    val osDir: String
        get() =
            when (Env.os) {
                Env.OS.Windows -> "windows"
                Env.OS.Linux -> "linux"
                Env.OS.MacOS -> "macos"
                else -> throw IllegalStateException("OS type is unknown!")
            }

    object Project {
        const val ipCoresFolder = "cores"
        const val sourceFolder = "source"
        const val constraintFolder = "constraint"
        const val workFolder = "work"
    }

    val vivado: File?
        get() {
            val vivado = Settings.vivadoLocation
            if (vivado != null && File(vivado).exists()) return File(vivado)
            var path = when (Env.os) {
                Env.OS.Windows -> File("C:\\Xilinx\\Vivado")
                Env.OS.Linux -> File("/opt/Xilinx/Vivado")
                Env.OS.Unknown, Env.OS.MacOS -> return null
            }
            if (!path.isDirectory) return null
            val subs = path.listFiles(FileFilter { it.isDirectory })
            subs?.find {
                try {
                    it.name.toFloat()
                    true
                } catch (e: NumberFormatException) {
                    false
                }
            }?.also { path = it }
            return path
        }

    val iceCube2: File?
        get() {
            val iceCube = Settings.iceCubeLocation
            if (iceCube != null && File(iceCube).exists()) return File(iceCube)
            val path = when (Env.os) {
                Env.OS.Windows -> File("C:\\lscc\\iCEcube2.2020.12")
                Env.OS.Linux -> File("~/lscc/iCEcube2.2020.12")
                Env.OS.Unknown, Env.OS.MacOS -> return null
            }
            if (!path.isDirectory) return null
            return path
        }

    val iceCubeLicense: File? =
        Settings.iceCubeLicense?.let { File(it).let { file -> if (file.exists()) file else null } }
}