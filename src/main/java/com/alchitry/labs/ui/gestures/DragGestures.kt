package com.alchitry.labs.ui.gestures

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope

suspend fun PointerInputScope.detectEditorActions(
    onClick: (Offset) -> Unit,
    onDoubleClick: (Offset) -> Unit,
    onDrag: (change: PointerInputChange) -> Unit,
) {
    var lastClick: PointerInputChange? = null
    forEachGesture {
        awaitPointerEventScope {
            val down = awaitFirstDown()
            down.consume()
            val last = lastClick

            lastClick = if (
                last != null &&
                (down.position - last.position).getDistanceSquared() < 100f &&
                down.uptimeMillis - last.uptimeMillis <= viewConfiguration.longPressTimeoutMillis
            ) {
                onDoubleClick(down.position)
                null
            } else {
                onClick(down.position)
                down
            }

            drag(down.id) {
                onDrag(it)
                it.consume()
                lastClick = null
            }
        }
    }
}