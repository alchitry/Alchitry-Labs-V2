package com.alchitry.labs.project

import com.alchitry.labs.Env
import com.alchitry.labs.Log
import com.alchitry.labs.PathUtil
import com.alchitry.labs.main
import java.io.File
import java.net.URISyntaxException
import java.net.URL

object Locations {
    val progDir: File?
    val progPrefix: String
    val library: File
    val base: File
    val components: File
    val firmware: File
    val tools: File
    val bin: File
    val etc: File
    val lib: File
    val resources: File
    val templateDir: File

    val osDir: String
        get() =
            when (Env.os) {
                Env.OS.WINDOWS -> "windows"
                Env.OS.LINUX -> "linux"
                Env.OS.MACOS -> "macos"
                else -> throw IllegalStateException("OS type is unknown!")
            }

    object Project {
        const val ipCoresFolder = "cores"
        const val sourceFolder = "source"
        const val constraintFolder = "constraint"
        const val workFolder = "work"
    }


    init {
        var prog: File? = null
        try {
            var uri = ::main::class.java.protectionDomain.codeSource.location.toURI()
            if (Env.isWindows && uri.authority != null && uri.authority.isNotEmpty()) {
                // Hack for UNC Path
                uri = URL("file://" + uri.toString().substring("file:".length)).toURI()
            }
            prog = File(uri)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.printError("Could not detect program directory!", e)
        }

        prog = prog?.parentFile

        progDir = if (prog != null && !PathUtil.assembleFile(prog.path, "lib").exists())
            prog.parentFile
        else
            prog

        progPrefix = if (Env.isIDE) "" else progDir?.path ?: ""

        library = PathUtil.assembleFile(progPrefix, "library")
        base = PathUtil.assembleFile(library, "base")
        components = PathUtil.assembleFile(library, "ui")
        firmware = PathUtil.assembleFile(library, "firmware")
        tools = PathUtil.assembleFile(progPrefix + "tools")
        bin = PathUtil.assembleFile(tools, osDir, "bin")
        etc = PathUtil.assembleFile(tools, "etc")
        lib = PathUtil.assembleFile(tools, osDir, "lib")
        resources = PathUtil.assembleFile(progPrefix + "res")
        templateDir = PathUtil.assembleFile(base, "templates")
    }
}