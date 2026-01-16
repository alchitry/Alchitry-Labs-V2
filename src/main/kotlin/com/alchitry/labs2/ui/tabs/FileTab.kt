package com.alchitry.labs2.ui.tabs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.ui.alchitry_text_field.CodeEditor
import com.alchitry.labs2.ui.alchitry_text_field.CodeEditorState

class FileTab(
    val file: ProjectFile,
    override var parent: TabPanel
) : Tab {
    private val codeEditorState = CodeEditorState(file)

    @Composable
    override fun label() {
        Text(file.name)
    }

    @Composable
    override fun content() {
        codeEditorState.subscribe()
        CodeEditor(codeEditorState)
    }

    override fun onClose(save: Boolean): Boolean {
        if (!file.isReadOnly && save)
            file.file.writeText(codeEditorState.textFieldState.getText())
        return true
    }
}