package com.alchitry.labs.ui.drag_and_drop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun <T> DragAndDropContext<T>.DropContainer(
    modifier: Modifier = Modifier,
    onDropped: (T) -> Unit,
    insertFirst: Boolean = false,
    content: @Composable (dragActive: Boolean, hover: Boolean) -> Unit
) {
    var dragBounds by remember { mutableStateOf(0f..0f) }
    var hover by remember { mutableStateOf(false) }
    var dragActive by remember { mutableStateOf(false) }
    var bounds by remember { mutableStateOf(Rect.Zero) }

    val droppable = remember {
        object : Droppable<T> {
            override fun onDragStart() {
                hover = false
                dragActive = true
            }

            override fun onDragged(y: Float, size: IntSize, consumed: Boolean): Boolean {
                hover = !consumed && dragBounds.contains(y)
                return hover
            }

            override suspend fun onDroppedStarted(
                dragSize: IntSize,
                dragAlpha: Animatable<Float, AnimationVector1D>,
                dragPosition: Animatable<Float, AnimationVector1D>,
                scope: CoroutineScope
            ) {
                scope.launch {
                    dragPosition.animateTo(dragBounds.endInclusive + dragSize.height)
                }
            }

            override suspend fun onDroppedStarted() {
                hover = false
                dragActive = false
            }

            override fun onDropped(item: T) {
                onDropped(item)
            }

            override fun onDropEnd() {
                hover = false
                dragActive = false
            }

            override fun getBounds(): Rect? {
                return null
            }
        }
    }

    DisposableEffect(droppable) {
        registerDroppable(droppable, insertFirst)
        onDispose { removeDroppable(droppable) }
    }

    Box(
        modifier
            .onGloballyPositioned {
                bounds = it.boundsInRoot()
                dragBounds = bounds.top..bounds.bottom
            }
    ) {
        content(dragActive, hover)
    }
}