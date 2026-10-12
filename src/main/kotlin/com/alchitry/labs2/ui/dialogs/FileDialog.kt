package com.alchitry.labs2.ui.dialogs

import androidx.compose.ui.awt.ComposeWindow
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.*
import java.io.File

suspend fun openFileDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    allowMultiSelection: Boolean = true,
    startingDirectory: File? = null
): Set<File>? {
    val dialogSettings = FileKitDialogSettings(
        title = title,
        parent = FileKitDialogParent.awt(window)
    )
    return if (allowMultiSelection) {
        FileKit.openFilePicker(
            type = FileKitType.File(allowedExtensions),
            mode = FileKitMode.Multiple(),
            directory = startingDirectory?.let { PlatformFile(it) },
            dialogSettings = dialogSettings
        )?.map { it.file }?.toSet()
    } else {
        FileKit.openFilePicker(
            type = FileKitType.File(allowedExtensions),
            mode = FileKitMode.Single,
            directory = startingDirectory?.let { PlatformFile(it) },
            dialogSettings = dialogSettings
        )?.let { result ->
            setOf(result.file)
        }
    }
}

suspend fun openDirectoryDialog(
    window: ComposeWindow,
    title: String,
    startingDirectory: File? = null
): File? {
    return FileKit.openDirectoryPicker(
        directory = startingDirectory?.let { PlatformFile(it) },
        dialogSettings = FileKitDialogSettings(
            title = title,
            parent = FileKitDialogParent.awt(window)
        )
    )?.file
}