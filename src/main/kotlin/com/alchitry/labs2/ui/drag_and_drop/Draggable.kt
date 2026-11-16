package com.alchitry.labs2.ui.drag_and_drop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A draggable composable that provides a drag handle modifier via the content lambda.
 * Only the element with the [dragHandleModifier] applied will initiate drags.
 * The pointer detection stays on the outer container, so it survives the composable
 * being replaced by a spacer during the drag.
 */
@Composable
fun <T> DragAndDropContext<T>.Draggable(
    item: T,
    onMoved: () -> Unit,
    onDragging: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    waitForSlop: Boolean = true,
    content: @Composable DragAndDropContext<T>.(dragHandleModifier: Modifier) -> Unit
) {
    var dragging by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var position by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current.density
    val animatedSize = remember { Animatable(IntSize.Zero, IntSize.VectorConverter) }
    val animatedOffset = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    var otherDragging by remember { mutableStateOf(false) }

    // Track the drag handle bounds in root coordinates
    var handleBounds by remember { mutableStateOf<Rect?>(null) }

    // Content composable without the handle modifier, used for the drag overlay
    val contentForOverlay: @Composable DragAndDropContext<T>.() -> Unit = {
        content(Modifier)
    }

    DisposableEffect(item) {
        val droppable = object : Droppable<T> {
            override fun onDragStart() {
                if (!dragging)
                    otherDragging = true
            }

            override fun onDragged(offset: Offset, size: IntSize, consumed: Boolean): Boolean {
                return false
            }

            override fun onDropped(item: T) {
            }

            override suspend fun onDroppedStarted(
                dragSize: IntSize,
                dragAlpha: Animatable<Float, AnimationVector1D>,
                dragPosition: Animatable<Offset, AnimationVector2D>,
                dragAnchorOffset: Offset,
                scope: CoroutineScope
            ) {
            }

            override suspend fun onDroppedStarted() {}

            override fun onDropEnd() {
                if (!dragging)
                    otherDragging = false
            }

            override fun getBounds(): Rect {
                throw IllegalStateException("getBounds should never be called for the Draggable!")
            }
        }
        registerDroppable(droppable)
        onDispose {
            removeDroppable(droppable)
        }
    }

    val dragHandleModifier = Modifier.onGloballyPositioned {
        handleBounds = it.boundsInRoot()
    }

    Box(
        Modifier
            .onGloballyPositioned {
                size = it.size
                position = it.positionInRoot()
            }
            .pointerInput(item, otherDragging, enabled) {
                if (!otherDragging && enabled)
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val rootPos = down.position + position
                        val bounds = handleBounds

                        // If the touch started outside the drag handle, don't consume — let children handle it
                        if (bounds != null && !bounds.contains(rootPos)) {
                            return@awaitEachGesture
                        }

                        down.consume()

                        val dragStartPos: Offset = down.position + position

                        val dragPointerId = if (waitForSlop) {
                            awaitTouchSlopOrCancellation(down.id) { change, _ ->
                                change.consume()
                            }?.id ?: return@awaitEachGesture
                        } else {
                            down.id
                        }

                        dragging = true
                        onDragStart(contentForOverlay, dragStartPos, size, position)
                        onDragging(true)
                        scope.launch {
                            animatedSize.snapTo(size)
                            animatedSize.animateTo(IntSize.Zero)
                        }

                        onDragged(dragStartPos)

                        val dragSuccess = drag(dragPointerId) { change ->
                            change.consume()
                            onDragged(change.position + position)
                        }

                        if (dragSuccess) {
                            // onDragEnd
                            val wasMoved = hasDropZone()
                            if (!wasMoved)
                                onDragging(false)
                            scope.launch {
                                if (wasMoved)
                                    animatedSize.snapTo(IntSize.Zero)
                                else {
                                    animatedSize.animateTo(getDraggableSize())
                                }
                            }

                            scope.launch {
                                onDropped(item, position, onMoved)
                                dragging = false
                            }
                        } else {
                            // onDragCancel
                            onDragging(false)
                            scope.launch {
                                animatedSize.snapTo(getDraggableSize())
                                dragging = false
                            }
                            onDragCancel()
                        }
                    }
            }
    ) {
        if (dragging)
            Spacer(
                Modifier
                    .size(animatedSize.value.let { DpSize(it.width.dp, it.height.dp) / density })
            )
        else
            Box(
                Modifier
                    .offset(x = (animatedOffset.value / density).dp)
                    .alpha((1f - animatedOffset.value / size.width.toFloat()).coerceIn(0f, 1f))
            ) {
                content(dragHandleModifier)
            }
    }
}