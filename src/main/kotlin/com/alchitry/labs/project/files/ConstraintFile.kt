package com.alchitry.labs.project.files

import com.alchitry.labs.project.ConstraintLang
import java.io.File

data class ConstraintFile(
    override val file: File
) : ProjectFile {
    override val name: String = file.name
    val language =
        ConstraintLang.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
}
