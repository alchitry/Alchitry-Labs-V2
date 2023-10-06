package com.alchitry.labs

import androidx.compose.ui.window.application
import com.alchitry.labs.windows.loaderWindow

fun main(args: Array<String>) {

    if (Env.os == Env.OS.Unknown) {
        System.err.println("Warning: OS detection failed!")
    }

    // TODO: open project files from command line

    application {
        loaderWindow()
    }
}
