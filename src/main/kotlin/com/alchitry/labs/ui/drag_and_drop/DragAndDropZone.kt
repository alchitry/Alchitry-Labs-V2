package com.alchitry.labs.ui.drag_and_drop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun <T : Any> DragAndDropZone(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    footer: @Composable DragAndDropContext<T>.() -> Unit = {},
    block: @Composable DragAndDropContext<T>.() -> Unit
) {
    var draggingComposable: (@Composable DragAndDropContext<T>.() -> Unit)? by remember {
        mutableStateOf(
            null
        )
    }
    var dragOrigPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    val dragXOffset = remember { Animatable(0f) }
    val dragPosition = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val dragAlpha = remember { Animatable(0.6f) }
    var dragSize by remember { mutableStateOf(IntSize(0, 0)) }
    val dragWidth = remember { Animatable(0f) }
    var position by remember { mutableStateOf(Offset(0f, 0f)) }

    var bounds by remember { mutableStateOf(0f..0f) }
    val scope = rememberCoroutineScope()

    val dndContext = remember {
        object : DragAndDropContext<T> {
            private val droppables = mutableListOf<Droppable<T>>()
            private var activeDroppable: Droppable<T>? = null

            override fun registerDroppable(droppable: Droppable<T>, insertFirst: Boolean) {
                if (droppables.contains(droppable))
                    return
                if (insertFirst)
                    droppables.add(0, droppable)
                else
                    droppables.add(droppable)
            }

            override fun removeDroppable(droppable: Droppable<T>) {
                droppables.remove(droppable)
            }

            override fun onDragStart(
                content: @Composable DragAndDropContext<T>.() -> Unit,
                offset: Offset,
                size: IntSize,
                origPosition: Offset
            ) {
                draggingComposable = content
                dragSize = size
                scope.launch {
                    dragXOffset.snapTo(origPosition.x)
                    dragWidth.snapTo(size.width.toFloat())
                    dragAlpha.snapTo(0.6f)
                }
                scope.launch {
                    dragPosition.snapTo(origPosition + (dragSize / 2).let {
                        Offset(
                            it.width.toFloat(),
                            it.height.toFloat()
                        )
                    })
                    dragPosition.animateTo(offset)
                }
                dragOrigPosition = origPosition
                activeDroppable = null
                droppables.forEach {
                    it.onDragStart()
                }
            }

            override fun onDragged(offset: Offset) {
                val lastDroppable = activeDroppable
                activeDroppable = null
                droppables.forEach {
                    if (it.onDragged(offset, dragSize, activeDroppable != null))
                        activeDroppable = it
                }

                if (lastDroppable != activeDroppable) {
                    val activeBounds = activeDroppable?.getBounds()
                    scope.launch { dragXOffset.animateTo(activeBounds?.left ?: dragOrigPosition.x) }
                    scope.launch { dragWidth.animateTo(activeBounds?.width ?: dragSize.width.toFloat()) }
                }

                scope.launch { dragPosition.snapTo(offset) }
            }

            override fun hasDropZone(): Boolean {
                return activeDroppable != null
            }

            override fun onDragCancel() {
                scope.launch {
                    dragAlpha.animateTo(0f)
                }
                draggingComposable = null
                activeDroppable = null
                droppables.forEach {
                    it.onDropEnd()
                }
            }

            override suspend fun onDropped(item: T, origPosition: Offset, onMoved: () -> Unit) {
                scope.launch {
                    dragOrigPosition = origPosition
                    val active = activeDroppable
                    droppables.forEach {
                        if (it !== activeDroppable)
                            it.onDroppedStarted()
                    }
                    if (active != null) {
                        active.onDroppedStarted(dragSize, dragAlpha, dragPosition, scope)
                    } else {
                        scope.launch {
                            dragAlpha.animateTo(1f)
                        }

                        dragPosition.animateTo(origPosition + (dragSize / 2).let {
                            Offset(
                                it.width.toFloat(),
                                it.height.toFloat()
                            )
                        })
                    }

                    if (activeDroppable != null) {
                        onMoved()
                        activeDroppable?.onDropped(item)
                        activeDroppable = null
                    }

                    scope.launch {
                        dragAlpha.animateTo(0f)
                        draggingComposable = null
                    }

                    droppables.forEach {
                        it.onDropEnd()
                    }
                }.join()
            }

            override fun getDraggableSize(): IntSize {
                return dragSize
            }
        }
    }

    val rootBounds = remember { mutableStateOf(Rect.Zero) }
    Box(
        modifier
            .fillMaxSize()
            .onGloballyPositioned {
                position = it.positionInRoot()
            }
    ) {
        dndContext.run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .onGloballyPositioned {
                            val b = it.boundsInRoot()
                            rootBounds.value = b
                            bounds = b.top..b.bottom
                        }
                        .padding(paddingValues),
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment,
                ) {
                    block()
                }

                footer()
            }
        }

        DragContent(
            position = position,
            dragSize = dragSize,
            dragAlpha = dragAlpha,
            dragPosition = dragPosition,
            onHeightChanged = {
                dragSize = IntSize(dragSize.width, it)
            },
            dragContent = draggingComposable
        )
    }
}

// This is moved to a function so that the main contents don't redraw each frame when drag is active
@Composable
private fun <T : Any> DragContent(
    position: Offset,
    dragSize: IntSize,
    dragAlpha: Animatable<Float, AnimationVector1D>,
    dragPosition: Animatable<Offset, AnimationVector2D>,
    onHeightChanged: (Int) -> Unit,
    dragContent: (@Composable DragAndDropContext<T>.() -> Unit)?
) {
    LocalDensity.current.run {
        val dummyDnDContext = remember { dummyDragAndDropContext<T>() }
        if (dragContent != null)
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = dragAlpha.value
                        clip = true
                    }
                    .offset {
                        (dragPosition.value - position).let {
                            IntOffset(it.x.roundToInt(), it.y.roundToInt()) - (dragSize / 2).let { intSize ->
                                IntOffset(
                                    intSize.width,
                                    intSize.height
                                )
                            }
                        }
                    }
            ) {
                Box(
                    Modifier
                        .width(dragSize.width.toDp())
                        .onGloballyPositioned { if (dragSize.height != it.size.height) onHeightChanged(it.size.height) }
                ) {
                    dummyDnDContext.run {
                        dragContent()
                    }
                }
            }
    }
}


fun <T> dummyDragAndDropContext(): DragAndDropContext<T> {
    return object : DragAndDropContext<T> {
        override fun registerDroppable(droppable: Droppable<T>, insertFirst: Boolean) {
        }

        override fun removeDroppable(droppable: Droppable<T>) {
        }

        override fun onDragStart(
            content: @Composable DragAndDropContext<T>.() -> Unit,
            offset: Offset,
            size: IntSize,
            origPosition: Offset
        ) {
        }

        override fun onDragged(offset: Offset) {
        }

        override fun onDragCancel() {
        }

        override suspend fun onDropped(item: T, origPosition: Offset, onMoved: () -> Unit) {
        }

        override fun hasDropZone(): Boolean {
            return false
        }

        override fun getDraggableSize(): IntSize {
            return IntSize(0, 0)
        }
    }
}