package com.alchitry.labs2

import java.util.*

object Env {
    enum class OS { Unknown, Windows, Linux, MacOS }
    enum class Mode { Labs, Loader, CLI }
    enum class Release { Beta, Stable, Unknown }

    var mode: Env.Mode = Mode.CLI

    val os = System.getProperty("os.name").lowercase(Locale.getDefault()).let {
        when {
            it.startsWith("Win", ignoreCase = true) -> OS.Windows
            it.startsWith("Linux", ignoreCase = true) -> OS.Linux
            it.equals("Mac OS X", ignoreCase = true) -> OS.MacOS
            else -> OS.Unknown
        }
    }
    val isWindows: Boolean get() = os == OS.Windows
    val isLinux: Boolean get() = os == OS.Linux
    val isMac: Boolean get() = os == OS.MacOS

    val isCLI: Boolean get() = mode == Mode.CLI
    val isLabs: Boolean get() = mode == Mode.Labs
    val isLoader: Boolean get() = mode == Mode.Loader

    val release = when (System.getProperty("app.release.channel", "unknown")) {
        "stable" -> Release.Stable
        "beta" -> Release.Beta
        else -> Release.Unknown
    }

    val isStable = release == Release.Stable
    val isBeta = release == Release.Beta

    val version =
        (Properties()
            .apply {
                Env.javaClass.getResourceAsStream("/version.properties")?.let { load(it) }
            }
            .getProperty("version") ?: "Missing Version") + if (isBeta) " BETA" else ""
}