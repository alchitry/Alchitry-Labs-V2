package com.alchitry.labs.ui.code_editor.tooltip

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.*
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
    var job: Job? by mutableStateOf(null)
    var activeToken by mutableStateOf<EditorToken?>(null)
    val isVisible = Animatable(0f)

    fun tokenFromPosition(position: Offset): EditorToken? {
        return codeEditor.offsetToToken(position, true)
    }

    fun startShowing(token: EditorToken) {
        if (activeToken != token)
            hide()
        job?.cancel()
        job = scope.launch {
            delay(750)
            activeToken = token
            isVisible.animateTo(1f)
        }
    }

    fun hide() {
        job?.cancel()
        scope.launch {
            isVisible.animateTo(0f)
            activeToken = null
        }
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
            positionPx = remember { getPopupAnchor() }
        )
}