package com.alchitry.labs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.alchitry.labs.windows.labsWindow
import com.alchitry.labs.windows.loaderWindow

private var activeWindow by mutableStateOf(Settings.openWindow)
fun switchActiveWindow(windowType: Settings.WindowType) {
    activeWindow = windowType
    Settings.openWindow = activeWindow
}

fun main(args: Array<String>) {
    if (Env.os == Env.OS.Unknown) {
        System.err.println("Warning: OS detection failed!")
    }

    // TODO: open project files from command line

    application {
        when (activeWindow) {
            Settings.WindowType.Labs -> labsWindow()
            Settings.WindowType.Loader -> loaderWindow()
        }
    }
}
