package com.alchitry.labs.ui.code_editor

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UndoManager(private val editor: CodeEditorState, private val selectionManager: SelectionManager) {
    private val undoStack = mutableListOf<EditorSnapshot>()
    private val redoStack = mutableListOf<EditorSnapshot>()
    private var currentSnapshot: EditorSnapshot? = null
    private var snapshotJob: Job? = null

    companion object {
        const val snapshotDelayMs: Long = 250
    }

    fun reset() {
        snapshotJob?.cancel()
        snapshotJob = null
        undoStack.clear()
        redoStack.clear()
        currentSnapshot = getSnapshot()
    }

    fun undo() {
        val newState = undoStack.removeLastOrNull() ?: return
        snapshotJob?.cancel()
        snapshotJob = null
        redoStack.add(currentSnapshot ?: getSnapshot())
        restoreSnapshot(newState)
    }

    fun redo() {
        val newState = redoStack.removeLastOrNull() ?: return
        snapshotJob?.cancel()
        snapshotJob = null
        undoStack.add(currentSnapshot ?: getSnapshot())
        restoreSnapshot(newState)
    }

    private fun restoreSnapshot(snapshot: EditorSnapshot) {
        editor.lines.clear()
        snapshot.lines.forEach { line ->
            editor.lines.add(editor.newLineState(line))
        }
        selectionManager.caret = snapshot.caret
        selectionManager.start = snapshot.selectionStart
        selectionManager.end = snapshot.selectionEnd
        editor.onTextChange()
        currentSnapshot = snapshot
    }

    private fun getSnapshot(): EditorSnapshot {
        snapshotJob?.cancel()
        snapshotJob = null
        currentSnapshot = null
        return editor.getSnapshot()
    }

    fun queueSnapshot() {
        redoStack.clear()

        currentSnapshot?.let {
            currentSnapshot = null
            undoStack.add(
                it.copy(
                    caret = selectionManager.caret,
                    selectionStart = selectionManager.start,
                    selectionEnd = selectionManager.end
                )
            )
        }

        snapshotJob?.cancel()
        snapshotJob = editor.scope.launch {
            delay(snapshotDelayMs)
            currentSnapshot = getSnapshot()
        }
    }
}

