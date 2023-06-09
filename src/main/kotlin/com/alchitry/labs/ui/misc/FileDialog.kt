package com.alchitry.labs.ui.misc

import androidx.compose.ui.awt.ComposeWindow
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.awt.FileDialog
import java.awt.Point
import java.io.File

fun openFileDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    allowMultiSelection: Boolean = true
): Set<File> {
    val scope = CoroutineScope(Dispatchers.Swing)
    return FileDialog(window, title, FileDialog.LOAD).apply {
        isMultipleMode = allowMultiSelection

        // windows
        file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

        // linux
        setFilenameFilter { _, name ->
            allowedExtensions.any {
                name.endsWith(it)
            }
        }

        val xOffset = ((window.size.width - 1000) / 2).coerceAtLeast(0)
        val yOffset = ((window.size.height - 1000) / 2).coerceAtLeast(0)

        location = Point(xOffset + window.location.x, yOffset + window.location.y)

        // not sure why, but sometimes it ends up behind the main window
        // this ensures it comes forward
        scope.launch {
            delay(100)
            requestFocus()
        }

        isVisible = true
    }.files.toSet().also { scope.cancel() }
}