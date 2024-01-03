package com.alchitry.labs.project.files

import com.alchitry.labs.project.HDL

class SourceFile(
    file: FileProvider,
    val top: Boolean = false
) : ProjectFile(file) {
    override val language =
        HDL.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
}
