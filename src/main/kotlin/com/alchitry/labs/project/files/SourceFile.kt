package com.alchitry.labs.project.files

import com.alchitry.labs.project.HDL
import com.alchitry.labs.project.Languages
import com.alchitry.labs.ui.code_editor.styles.EditorTokenizer
import com.alchitry.labs.ui.code_editor.styles.lucid.LucidTokenizer

class SourceFile(
    file: FileProvider,
    val top: Boolean = false
) : ProjectFile(file) {
    override val language =
        HDL.fromExt(file.extension) ?: throw Exception("Unsupported file extension \"${file.extension}\"")
    override val editorTokenizer: EditorTokenizer = when (language) {
        Languages.Lucid -> LucidTokenizer
        Languages.Verilog -> TODO()
    }
}
