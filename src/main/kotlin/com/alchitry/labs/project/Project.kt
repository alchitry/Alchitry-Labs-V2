package com.alchitry.labs.project

import com.alchitry.labs.Log
import com.alchitry.labs.PathUtil
import com.alchitry.labs.mainWindow
import com.alchitry.labs.project.files.ConstraintFile
import com.alchitry.labs.project.files.IPCore
import com.alchitry.labs.project.files.SourceFile
import com.alchitry.labs.ui.misc.openFileDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

data class Project(
    val projectName: String,
    val projectFolder: File,
    val board: Board,
    val sourceFiles: Set<SourceFile>,
    val constraintFiles: Set<ConstraintFile>,
    val ipCores: Set<IPCore>
) {
    companion object {
        private val mutableCurrentFlow = MutableStateFlow<Project?>(null)
        val currentFlow = mutableCurrentFlow.asStateFlow()

        var current: Project?
            get() = mutableCurrentFlow.value
            set(value) {
                mutableCurrentFlow.tryEmit(value)
            }

        fun openProject(file: File? = null) {
            try {
                val project =
                    file ?: openFileDialog(mainWindow, "Open Project", listOf(".alp"), allowMultiSelection = false)
                        .firstOrNull() ?: return
                mutableCurrentFlow.tryEmit(openXml(project))
            } catch (e: Exception) {
                Log.showError("Failed to open project: ${e.message}")
            }
        }
    }

    val top = sourceFiles.firstOrNull { it.top } ?: throw Exception("Missing top module!")
    val projectFile = File(PathUtil.assemblePath(projectFolder, "$projectName.alp"))
}