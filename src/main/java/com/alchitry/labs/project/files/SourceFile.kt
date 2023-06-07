package com.alchitry.labs.project.files

import com.alchitry.labs.project.HDL
import java.io.File

data class SourceFile(
    override val file: File,
    val top: Boolean = false
) : ProjectFile {
    override val name: String = file.name
    val language = HDL.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
}
