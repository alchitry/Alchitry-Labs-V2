package com.alchitry.labs2.ui.drag_and_drop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.CoroutineScope

interface Droppable<T> {
    fun onDragStart()
    fun onDragged(offset: Offset, size: IntSize, consumed: Boolean): Boolean
    suspend fun onDroppedStarted(
        dragSize: IntSize,
        dragAlpha: Animatable<Float, AnimationVector1D>,
        dragPosition: Animatable<Offset, AnimationVector2D>,
        scope: CoroutineScope
    )

    suspend fun onDroppedStarted() // called for zones the drop didn't land in

    fun onDropped(item: T)
    fun onDropEnd()
    fun getBounds(): Rect?
}