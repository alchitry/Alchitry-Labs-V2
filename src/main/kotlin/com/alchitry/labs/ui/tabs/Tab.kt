package com.alchitry.labs.ui.tabs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.alchitry.labs.project.files.ProjectFile
import com.alchitry.labs.ui.code_editor.CodeEditor
import com.alchitry.labs.ui.code_editor.CodeEditorState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface Tab {
    @Composable
    fun label()

    @Composable
    fun content()

    fun onClose(): Boolean

    var parent: TabPanel
}

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
        key(this) {
            CodeEditor(codeEditorState)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onClose(): Boolean {
        if (!file.isReadOnly)
            GlobalScope.launch { file.writeText(codeEditorState.getText()) }
        return true
    }

}