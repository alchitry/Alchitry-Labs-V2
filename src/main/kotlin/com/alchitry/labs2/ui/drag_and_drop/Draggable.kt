package com.alchitry.labs2.ui.drag_and_drop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun <T> DragAndDropContext<T>.Draggable(
    item: T,
    onMoved: () -> Unit,
    onDragging: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    content: @Composable DragAndDropContext<T>.() -> Unit
) {
    var dragging by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var position by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current.density
    val animatedSize = remember { Animatable(IntSize.Zero, IntSize.VectorConverter) }
    val animatedOffset = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    var otherDragging by remember { mutableStateOf(false) }

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

    Box(
        Modifier
            .zIndex(1f)
            .onGloballyPositioned {
                size = it.size
                position = it.positionInRoot()
            }
            .pointerInput(item, otherDragging, enabled) { // maybe need height in the list
                if (!otherDragging && enabled)
                    detectDragGestures(
                        onDragStart = {
                            dragging = true
                            onDragStart(content, it + position, size, position)
                            onDragging(true)
                            scope.launch {
                                animatedSize.snapTo(size)
                                animatedSize.animateTo(IntSize.Zero)
                            }
                        },
                        onDragEnd = {
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
                        },
                        onDragCancel = {
                            onDragging(false)
                            scope.launch {
                                animatedSize.snapTo(getDraggableSize())
                                dragging = false
                            }
                            onDragCancel()
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            onDragged(change.position + position)
                        }
                    )
            }
//            .draggable(
//                enabled = enabled && !otherDragging,
//                startDragImmediately = false,
//                orientation = Orientation.Horizontal,
//                onDragStarted = {
//                    scope.launch { animatedOffset.snapTo(0f) }
//                },
//                onDragStopped = { velocity ->
//                    // Velocity is in pixels per second, but we deal in percentage offsets, so we
//                    // need to scale the velocity to match
//                    scope.launch {
//                        val spec = exponentialDecay<Float>(
//                            frictionMultiplier = 4f,
//                            absVelocityThreshold = density * 10f
//                        )
//                        val target = spec.calculateTargetValue(animatedOffset.value, velocity)
//                        if (target.absoluteValue <= 0.8f * size.width) {
//                            animatedOffset.animateDecay(velocity, spec)
//                            animatedOffset.animateTo(0f)
//                        } else {
//                            animatedOffset.animateTo(size.width.toFloat() * animatedOffset.targetValue.sign)
//                            dragging = true
//                            animatedHeight.snapTo(size.height.toFloat())
//                            animatedHeight.animateTo(0f)
//
//                            onMoved()
//                        }
//                    }
//                },
//                state = rememberDraggableState { dragAmount ->
//                    scope.launch {
//                        animatedOffset.snapTo(animatedOffset.targetValue + dragAmount)
//                    }
//                }
//            )
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
                content()
            }
    }
}