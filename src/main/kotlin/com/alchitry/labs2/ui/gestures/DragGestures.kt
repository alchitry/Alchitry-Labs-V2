package com.alchitry.labs2.ui.gestures

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers

suspend fun PointerInputScope.detectEditorActions(
    onClick: (Offset, PointerKeyboardModifiers) -> Unit,
    onDoubleClick: (Offset) -> Unit,
    onTripleClick: (Offset) -> Unit,
    onDrag: (change: PointerInputChange) -> Unit,
) {
    var lastClick: PointerInputChange? = null
    var clickCount = 0
    awaitEachGesture {
        val down = awaitFirstDown()
        down.consume()
        val last = lastClick

        lastClick = if (
            last != null &&
            (down.position - last.position).getDistanceSquared() < 100f &&
            down.uptimeMillis - last.uptimeMillis <= viewConfiguration.longPressTimeoutMillis
        ) {
            if (clickCount == 1) {
                onDoubleClick(down.position)
                clickCount = 2
                down
            } else {
                onTripleClick(down.position)
                clickCount = 0
                null
            }
        } else {
            onClick(down.position, currentEvent.keyboardModifiers)
            clickCount = 1
            down
        }

        drag(down.id) {
            onDrag(it)
            it.consume()
            lastClick = null
        }
    }
}