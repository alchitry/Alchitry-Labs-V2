package com.alchitry.labs

object Env {
    enum class OS { UNKNOWN, WINDOWS, LINUX, MACOS }

    var os = OS.UNKNOWN
    val isWindows: Boolean get() = os == OS.WINDOWS
    val isLinux: Boolean get() = os == OS.LINUX
    val isMac: Boolean get() = os == OS.MACOS
    var isIDE: Boolean = false
    var isGUI: Boolean = true
}