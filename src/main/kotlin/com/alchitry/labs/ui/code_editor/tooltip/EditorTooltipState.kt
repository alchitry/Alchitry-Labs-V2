package com.alchitry.labs.ui.code_editor.tooltip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition
import com.alchitry.labs.ui.code_editor.CodeEditorState
import com.alchitry.labs.ui.code_editor.EditorToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditorTooltipState(
    val scope: CoroutineScope,
    val codeEditor: CodeEditorState,
) {
    var parentBounds by mutableStateOf(Rect.Zero)
    var popupPosition by mutableStateOf(Offset.Zero)
    var cursorPosition by mutableStateOf(Offset.Zero)
    var isVisible by mutableStateOf(false)
    var job: Job? by mutableStateOf(null)
    var activeToken by mutableStateOf<EditorToken?>(null)

    fun tokenFromPosition(position: Offset): EditorToken? {
        return codeEditor.offsetToToken(position, true)
    }

    fun startShowing(token: EditorToken) {
        if (activeToken != token)
            isVisible = false
        activeToken = token
        job?.cancel()
        job = scope.launch {
            delay(750)
            isVisible = true
        }
    }

    fun hide() {
        job?.cancel()
        isVisible = false
        activeToken = null
    }

    fun hideIfNotHovered(globalPosition: Offset) {
        if (!parentBounds.contains(globalPosition)) {
            hide()
        }
    }

    private fun getPopupAnchor(): Offset {
        val token = activeToken ?: return cursorPosition
        val line = token.range.start.line
        val yOffset = codeEditor.offsetAtLineBottom(line)
        return cursorPosition.copy(y = yOffset.toFloat())
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun positionProvider() =
        rememberPopupPositionProviderAtPosition(
            positionPx = getPopupAnchor()
        )
}