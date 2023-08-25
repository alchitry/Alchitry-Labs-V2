package com.alchitry.labs.project.files

sealed interface ProjectFile {
    val file: FileProvider

    val name: String get() = file.name
    val isLibFile: Boolean get() = file is FileProvider.ResourceFile
}