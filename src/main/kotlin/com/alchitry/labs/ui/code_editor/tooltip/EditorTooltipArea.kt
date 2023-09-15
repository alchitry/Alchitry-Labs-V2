package com.alchitry.labs.ui.code_editor.tooltip

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.window.Popup
import com.alchitry.labs.ui.code_editor.EditorToken
import com.alchitry.labs.ui.components.AlchitryToolTipContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorTooltipArea(
    tooltip: @Composable (EditorToken) -> Unit,
    state: EditorTooltipState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .onGloballyPositioned { state.parentBounds = it.boundsInWindow() }
            .onPointerEvent(PointerEventType.Enter, PointerEventPass.Initial) {
                state.cursorPosition = it.position
                val token = state.tokenFromPosition(it.position) ?: return@onPointerEvent
                state.startShowing(token)
            }
            .onPointerEvent(PointerEventType.Move, PointerEventPass.Initial) {
                if (state.cursorPosition == it.position)
                    return@onPointerEvent
                val token = state.tokenFromPosition(it.position)
                state.cursorPosition = it.position

                if (state.activeToken == null && token != null) {
                    state.startShowing(token)
                } else if (state.activeToken != token) {
                    state.hide()
                    if (token != null)
                        state.startShowing(token)
                } else if (state.isVisible.value > 0f) {
                    state.hideIfNotHovered(state.parentBounds.topLeft + it.position)
                }
            }
            .onPointerEvent(PointerEventType.Exit, PointerEventPass.Initial) {
                state.hideIfNotHovered(state.parentBounds.topLeft + it.position)
            }
            .pointerInput(Unit) {
                detectDown {
                    state.hide()
                }
            }
    ) {
        content()
        if (state.isVisible.value > 0f) {
            Popup(
                popupPositionProvider = state.positionProvider(),
                onDismissRequest = { state.hide() }
            ) {
                AlchitryToolTipContent(
                    Modifier
                        .onGloballyPositioned { state.popupPosition = it.positionInWindow() }
                        .onPointerEvent(PointerEventType.Move) {
                            state.hideIfNotHovered(state.popupPosition + it.position)
                        }
                        .onPointerEvent(PointerEventType.Exit) {
                            state.hideIfNotHovered(state.popupPosition + it.position)
                        }
                        .alpha(state.isVisible.value)
                ) {
                    SelectionContainer {
                        state.activeToken?.let { tooltip(it) }
                    }
                }
            }
        }
    }
}

private val PointerEvent.position get() = changes.first().position

private fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    pass: PointerEventPass = PointerEventPass.Main,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
) = pointerInput(eventType, pass, onEvent) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent(pass)
            if (event.type == eventType) {
                onEvent(event)
            }
        }
    }
}

private suspend fun PointerInputScope.detectDown(onDown: (Offset) -> Unit) {
    while (true) {
        awaitPointerEventScope {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val down = event.changes.find { it.changedToDown() }
            if (down != null) {
                onDown(down.position)
            }
        }
    }
}
