package com.alchitry.labs2.ui.drag_and_drop

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.min

enum class DropRegion {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT,
    CENTER
}

@Composable
fun <T> DragAndDropContext<T>.PanelDropContainer(
    modifier: Modifier = Modifier,
    onDropped: (T, DropRegion) -> Unit,
    onDropEnd: () -> Unit,
    content: @Composable () -> Unit
) {
    var hover by remember { mutableStateOf(false) }
    var dragActive by remember { mutableStateOf(false) }
    var bounds by remember { mutableStateOf(Rect.Zero) }
    var region by remember { mutableStateOf<DropRegion?>(null) }

    val droppable = remember {
        object : Droppable<T> {
            override fun onDragStart() {
                region = null
                hover = false
                dragActive = true
            }

            override fun onDragged(offset: Offset, size: IntSize, consumed: Boolean): Boolean {
                hover = !consumed && bounds.contains(offset)

                val leftDist = abs(bounds.left - offset.x) / bounds.width
                val rightDist = abs(bounds.right - offset.x) / bounds.width
                val topDist = abs(bounds.top - offset.y) / bounds.height
                val bottomDist = abs(bounds.bottom - offset.y) / bounds.height

                val minXDist = min(leftDist, rightDist)
                val minYDist = min(topDist, bottomDist)
                val minDist = min(minXDist, minYDist)

                region = when {
                    !hover -> null
                    minXDist > 0.25f && minYDist > 0.25f -> DropRegion.CENTER
                    minDist == leftDist -> DropRegion.LEFT
                    minDist == rightDist -> DropRegion.RIGHT
                    minDist == topDist -> DropRegion.TOP
                    else -> DropRegion.BOTTOM
                }

                return hover
            }

            override suspend fun onDroppedStarted(
                dragSize: IntSize,
                dragAlpha: Animatable<Float, AnimationVector1D>,
                dragPosition: Animatable<Offset, AnimationVector2D>,
                scope: CoroutineScope
            ) {
                scope.launch {
                    dragAlpha.animateTo(0f)
                }
            }

            override suspend fun onDroppedStarted() {
                hover = false
                dragActive = false
                region = null
            }

            override fun onDropped(item: T) {
                onDropped(item, region ?: DropRegion.CENTER)
            }

            override fun onDropEnd() {
                hover = false
                dragActive = false
                region = null
                onDropEnd()
            }

            override fun getBounds(): Rect? {
                return null
            }
        }
    }

    DisposableEffect(droppable) {
        registerDroppable(droppable)
        onDispose {
            removeDroppable(droppable)
        }
    }

    Box(
        modifier
            .onGloballyPositioned {
                bounds = it.boundsInRoot()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
        LocalDensity.current.run {
            when (region) {
                DropRegion.TOP -> Modifier.padding(bottom = (bounds.height / 2).toDp())
                DropRegion.RIGHT -> Modifier.padding(start = (bounds.width / 2).toDp())
                DropRegion.BOTTOM -> Modifier.padding(top = (bounds.height / 2).toDp())
                DropRegion.LEFT -> Modifier.padding(end = (bounds.width / 2).toDp())
                DropRegion.CENTER -> Modifier.padding(0.dp)
                null -> null
            }?.let { paddingMod ->
                Box(
                    paddingMod
                        .matchParentSize()
                        .alpha(0.3f)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}