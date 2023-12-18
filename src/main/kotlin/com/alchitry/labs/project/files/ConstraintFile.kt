package com.alchitry.labs.project.files

import com.alchitry.labs.project.ConstraintLang
import com.alchitry.labs.ui.code_editor.styles.EditorTokenizer

class ConstraintFile(
    file: FileProvider
) : ProjectFile(file) {
    override val language =
        ConstraintLang.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")

    override val editorTokenizer: EditorTokenizer
        get() = TODO("Not yet implemented")
}
