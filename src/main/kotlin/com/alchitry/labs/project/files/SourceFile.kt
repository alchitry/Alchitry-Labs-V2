package com.alchitry.labs.project.files

import com.alchitry.labs.project.HDL

data class SourceFile(
    override val file: FileProvider,
    val top: Boolean = false
) : ProjectFile {
    val language = HDL.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
}
