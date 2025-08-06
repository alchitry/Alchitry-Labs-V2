package com.alchitry.labs2.ui.code_editor.tooltip

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.ui.code_editor.CodeEditorState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class TooltipProvider<T>(val codeEditor: CodeEditorState) {
    var parentBounds by mutableStateOf(Rect.Zero)
    var popupPosition by mutableStateOf(Offset.Zero)
    var cursorPosition by mutableStateOf(Offset.Zero)
    var job: Job? by mutableStateOf(null)
    var activeToken by mutableStateOf<T?>(null)
    val isVisible = Animatable(0f)

    abstract fun tokenFromPosition(position: Offset): T?

    fun startShowing(token: T) {
        if (activeToken != token)
            hide()
        job?.cancel()
        job = codeEditor.uiScope?.launch {
            delay(750)
            activeToken = token
            isVisible.animateTo(1f)
        }
    }

    fun hide() {
        job?.cancel()
        codeEditor.uiScope?.launch {
            isVisible.animateTo(0f)
            activeToken = null
        }
    }

    fun hideIfNotHovered(globalPosition: Offset) {
        if (!parentBounds.contains(globalPosition)) {
            hide()
        }
    }

    abstract fun getPopupAnchor(): Offset

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun positionProvider() =
        rememberPopupPositionProviderAtPosition(
            positionPx = remember { getPopupAnchor() }
        )
}

class NotationTooltipProvider(
    codeEditor: CodeEditorState,
) : TooltipProvider<Notation>(codeEditor) {

    override fun tokenFromPosition(position: Offset): Notation? {
        return codeEditor.notations?.firstOrNull { it.range.contains(codeEditor.screenOffsetToTextPosition(position)) }
            ?.let { if (it.message == null) null else it }
    }

    override fun getPopupAnchor(): Offset {
        val token = activeToken ?: return cursorPosition
        val line = token.range.start.line
        val yOffset = codeEditor.offsetAtLineBottom(line)
        return cursorPosition.copy(y = yOffset.toFloat() - codeEditor.verticalScrollState.value)
    }
}