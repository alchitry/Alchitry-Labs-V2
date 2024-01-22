package com.alchitry.labs2.ui.drag_and_drop

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

interface DragAndDropContext<T> {
    fun registerDroppable(droppable: Droppable<T>, insertFirst: Boolean = false)
    fun removeDroppable(droppable: Droppable<T>)

    fun onDragStart(
        content: @Composable DragAndDropContext<T>.() -> Unit,
        offset: Offset,
        size: IntSize,
        origPosition: Offset
    )

    fun onDragged(offset: Offset)
    fun onDragCancel()
    suspend fun onDropped(item: T, origPosition: Offset, onMoved: () -> Unit)
    fun hasDropZone(): Boolean
    fun getDraggableSize(): IntSize
}