package com.alchitry.labs2.ui.tabs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.ui.code_editor.CodeEditor
import com.alchitry.labs2.ui.code_editor.CodeEditorState

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
        CodeEditor(codeEditorState)
    }

    override fun onClose(save: Boolean): Boolean {
        if (!file.isReadOnly && save)
            file.file.writeText(codeEditorState.getText())
        return true
    }
}