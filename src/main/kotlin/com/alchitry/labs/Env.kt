package com.alchitry.labs

import java.util.*

object Env {
    enum class OS { UNKNOWN, WINDOWS, LINUX, MACOS }

    var os = OS.UNKNOWN
    val isWindows: Boolean get() = os == OS.WINDOWS
    val isLinux: Boolean get() = os == OS.LINUX
    val isMac: Boolean get() = os == OS.MACOS
    var isIDE: Boolean = false
    var isGUI: Boolean = true

    val version =
        Properties()
            .apply {
                Env.javaClass.getResourceAsStream("/version.properties")?.let { load(it) }
            }
            .getProperty("version") ?: "Missing Version"
}