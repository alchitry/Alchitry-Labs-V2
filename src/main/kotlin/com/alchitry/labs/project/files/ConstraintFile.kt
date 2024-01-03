package com.alchitry.labs.project.files

import com.alchitry.labs.project.ConstraintLang

class ConstraintFile(
    file: FileProvider
) : ProjectFile(file) {
    override val language =
        ConstraintLang.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
}
