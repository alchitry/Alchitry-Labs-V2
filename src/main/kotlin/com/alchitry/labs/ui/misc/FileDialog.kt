package com.alchitry.labs.ui.misc

import androidx.compose.ui.awt.ComposeWindow
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import li.flor.nativejfilechooser.NativeJFileChooser
import java.awt.FileDialog
import java.awt.Point
import java.io.File
import javax.swing.JFileChooser

fun openFileDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    allowMultiSelection: Boolean = true,
    startingDirectory: File? = null
): Set<File> {
    val scope = CoroutineScope(Dispatchers.Swing)
    return FileDialog(window, title, FileDialog.LOAD).apply {
        isMultipleMode = allowMultiSelection

        startingDirectory?.let {
            directory = it.path
        }

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

fun openDirectoryDialog(
    window: ComposeWindow,
    title: String,
    startingDirectory: File? = null
): File? {
    val scope = CoroutineScope(Dispatchers.Swing)
    val chooser = NativeJFileChooser(startingDirectory)
    chooser.dialogTitle = title
    chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    scope.launch {
        delay(100)
        chooser.requestFocus()
    }
    chooser.showOpenDialog(window)
    return chooser.selectedFile
}