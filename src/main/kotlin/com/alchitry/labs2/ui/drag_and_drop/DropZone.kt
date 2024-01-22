package com.alchitry.labs2.ui.drag_and_drop

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

val defaultHoverModifier: Modifier
    @Composable
    get() = Modifier
        .background(MaterialTheme.colorScheme.onSurface.copy(0.1f))

val defaultActiveModifier = Modifier
val defaultInactiveModifier = Modifier

@Composable
fun <T> DragAndDropContext<T>.DropZone(
    hoverModifier: Modifier = defaultHoverModifier,
    activeModifier: Modifier = defaultActiveModifier,
    inactiveModifier: Modifier = defaultInactiveModifier,
    minimumSize: DpSize,
    dropMargin: Dp = 20.dp,
    content: @Composable () -> Unit = {},
    onDropEnd: () -> Unit = {},
    onDropped: (T) -> Unit
) {
    var dragBounds by remember { mutableStateOf(Rect.Zero) }
    var hover by remember { mutableStateOf(false) }
    var dragActive by remember { mutableStateOf(false) }
    var bounds by remember { mutableStateOf(Rect.Zero) }

    val scope = rememberCoroutineScope()

    LocalDensity.current.run {

        val animatedSize = remember { Animatable(IntSize.Zero, IntSize.VectorConverter) }
        val animatedAlpha = remember { Animatable(1f) }

        val droppable = remember {
            object : Droppable<T> {
                override fun onDragStart() {
                    hover = false
                    dragActive = true
                }

                override fun onDragged(offset: Offset, size: IntSize, consumed: Boolean): Boolean {
                    return if (!consumed && dragBounds.contains(offset)) {
                        if (!hover) {
                            hover = true
                            scope.launch {
                                animatedAlpha.animateTo(1f)
                            }
                            scope.launch {
                                animatedSize.animateTo(size)
                            }
                        }
                        true
                    } else {
                        if (hover) {
                            scope.launch {
                                animatedAlpha.animateTo(0f)
                            }
                            scope.launch {
                                animatedSize.animateTo(IntSize.Zero)
                                hover = false
                            }
                        }
                        false
                    }
                }

                override suspend fun onDroppedStarted(
                    dragSize: IntSize,
                    dragAlpha: Animatable<Float, AnimationVector1D>,
                    dragPosition: Animatable<Offset, AnimationVector2D>,
                    scope: CoroutineScope
                ) {
                    dragActive = false
                    scope.launch {
                        animatedAlpha.animateTo(0f)
                    }
                    coroutineScope {
                        launch {
                            dragAlpha.animateTo(1f)
                        }
                        launch {
                            dragPosition.animateTo(
                                Offset(
                                    bounds.left + dragSize.width / 2f,
                                    bounds.top + dragSize.height / 2f
                                )
                            )
                        }
                    }
                }

                override suspend fun onDroppedStarted() {}

                override fun onDropped(item: T) {
                    onDropped(item)
                }

                override fun onDropEnd() {
                    hover = false
                    dragActive = false
                    scope.launch {
                        animatedSize.snapTo(IntSize.Zero)
                    }
                    onDropEnd()
                }

                override fun getBounds(): Rect {
                    return bounds
                }
            }

        }

        DisposableEffect(droppable) {
            registerDroppable(droppable)
            onDispose { removeDroppable(droppable) }
        }


        val dropMarginPx = dropMargin.toPx()

        // Nested boxes are needed to accurately capture the size with padding
        Box(
            Modifier
                .alpha(animatedAlpha.value)
                .onGloballyPositioned {
                    bounds = it.boundsInRoot()
                    dragBounds = Rect(
                        bounds.left - dropMarginPx,
                        bounds.top - dropMarginPx,
                        bounds.right + dropMarginPx,
                        bounds.bottom + dropMarginPx
                    )
                }
        ) {

            Crossfade(targetState = hover to dragActive) {
                val mod = when {
                    it.first -> hoverModifier
                    it.second -> activeModifier
                    else -> inactiveModifier
                }
                Box(
                    Modifier
                        .defaultMinSize(
                            animatedSize.value.width.toDp().coerceAtLeast(minimumSize.width),
                            animatedSize.value.height.toDp().coerceAtLeast(minimumSize.height)
                        )
                        .then(mod),
                    contentAlignment = Alignment.Center
                ) {
                    if (!it.first)
                        content()
                }
            }
        }
    }
}