package com.alchitry.labs.project

import com.alchitry.labs.Env

object Locations {
    val library: String = "/library"
    val components: String = "$library/components"

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
}