package com.alchitry.labs

import java.util.*

object Env {
    enum class OS { UNKNOWN, WINDOWS, LINUX, MACOS }

    val os = System.getProperty("os.name").lowercase(Locale.getDefault()).let {
        when {
            it.contains("win") -> OS.WINDOWS
            it.contains("nix") || it.contains("nux") || it.contains("aix") -> OS.LINUX
            it.contains("mac") -> OS.MACOS
            else -> OS.UNKNOWN
        }
    }
    val isWindows: Boolean get() = os == OS.WINDOWS
    val isLinux: Boolean get() = os == OS.LINUX
    val isMac: Boolean get() = os == OS.MACOS

    var isIDE: Boolean = false

    val version =
        Properties()
            .apply {
                Env.javaClass.getResourceAsStream("/version.properties")?.let { load(it) }
            }
            .getProperty("version") ?: "Missing Version"
}