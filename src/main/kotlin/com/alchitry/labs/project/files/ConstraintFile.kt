package com.alchitry.labs.project.files

import com.alchitry.labs.project.ConstraintLang

data class ConstraintFile(
    override val file: FileProvider
) : ProjectFile {
    val language =
        ConstraintLang.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
}
