package com.alchitry.labs2.ui.dialogs

import com.alchitry.labs2.Log
import com.alchitry.labs2.project.*
import com.alchitry.labs2.project.files.ConstraintFile
import com.alchitry.labs2.project.files.FileProvider
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.windows.mainWindow
import java.io.File
import kotlin.io.path.relativeTo

fun importFilesDialog(project: Project) {
    val supportedExtensions = Language.all.map { ".${it.extension}" }
    try {
        val files = openFileDialog(
            mainWindow,
            "Import Files",
            supportedExtensions,
            allowMultiSelection = true,
            startingDirectory = File(Locations.workspace)
        )
        val sourceFiles = project.data.sourceFiles.toMutableSet()
        val constraintFiles = project.data.constraintFiles.toMutableSet()
        files.forEach { file ->
            val language = Language.fromExt(file.extension)
            if (language == null) {
                Log.showError("Unsupported extension: ${file.name}")
                return@forEach
            }
            val directory = when (language) {
                is ConstraintLang -> project.constraintDirectory
                is HDL -> project.sourceDirectory
            }
            val newFilePath = directory.resolve(file.name)
            try {
                file.copyTo(newFilePath.toFile())
            } catch (e: FileAlreadyExistsException) {
                Log.showError("File already exists in the project: ${file.name}")
                return@forEach
            }
            when (language) {
                is ConstraintLang ->
                    constraintFiles.add(ConstraintFile(FileProvider.DiskFile(newFilePath.relativeTo(project.path))))

                is HDL ->
                    sourceFiles.add(SourceFile(FileProvider.DiskFile(newFilePath.relativeTo(project.path))))
            }
        }
        val newProject =
            project.copy(data = project.data.copy(sourceFiles = sourceFiles, constraintFiles = constraintFiles))
        newProject.save()
        Project.open(newProject)
    } catch (e: Exception) {
        Log.showError("Exception while importing files: ${e.message}")
    }
}