package com.alchitry.labs2.ui.dialogs

import com.alchitry.labs2.project.Locations
import com.alchitry.labs2.windows.mainWindow
import java.io.File

fun VivadoLocationDialog(): File? {
    return openDirectoryDialog(
        mainWindow,
        "Vivado Location",
        startingDirectory = Locations.vivado
    )
}

fun iCEcubeLocationDialog(): File? {
    return openDirectoryDialog(
        mainWindow,
        "iCEcube2 Location",
        startingDirectory = Locations.iceCube2
    )
}

fun iCEcubeLicenseDialog(): File? {
    return openFileDialog(
        window = mainWindow,
        title = "iCEcube2 License",
        allowedExtensions = listOf(".dat"),
        allowMultiSelection = false,
        startingDirectory = Locations.iceCubeLicense?.parentFile,
    ).firstOrNull()
}