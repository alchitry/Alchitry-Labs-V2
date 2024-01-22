package com.alchitry.labs2.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.awt.Cursor

enum class ResizePriority {
    EQUAL,
    FIRST,
    SECOND
}

@Composable
fun rememberSashData(
    resizePriority: ResizePriority = ResizePriority.EQUAL, first: Float = 1f, second: Float = 1f,
    minimumSize: Dp = 10.dp
): SashData {
    val minSizePx = LocalDensity.current.run { minimumSize.toPx() }
    return remember(minSizePx) {
        SashData(resizePriority, SashSize(first, second), minSizePx)
    }
}

data class SashSize(
    val first: Float = 1f,
    val second: Float = 1f
) {
    val total: Float = first + second
    val firstWeight: Float = first / total
    val secondWeight: Float = second / total

    val boundFirstWeight: Float get() = if (firstWeight.isNaN()) 0.5f else firstWeight.coerceIn(0.001f, 0.999f)
    val boundSecondWeight: Float get() = if (secondWeight.isNaN()) 0.5f else secondWeight.coerceIn(0.001f, 0.999f)
}

class SashData(
    val resizePriority: ResizePriority = ResizePriority.EQUAL,
    size: SashSize = SashSize(),
    val minimumSize: Float
) {
    var size by mutableStateOf(size)
    var dragSize = size

    fun clampToBounds() {
        if (size.first < minimumSize) {
            size = SashSize(minimumSize, size.total - minimumSize)
        } else if (size.second < minimumSize) {
            size = SashSize(size.total - minimumSize, minimumSize)
        }
    }

    fun resizeFirst(first: Float) {
        size = SashSize(first, size.total - first)
        clampToBounds()
    }

    fun resizeSecond(second: Float) {
        size = SashSize(size.total - second, second)
        clampToBounds()
    }

    fun onResize(newSize: Float) {
        size = when (resizePriority) {
            ResizePriority.EQUAL -> {
                SashSize(newSize * size.boundFirstWeight, newSize * size.boundSecondWeight)
            }

            ResizePriority.FIRST -> SashSize(newSize - size.second, size.second)
            ResizePriority.SECOND -> SashSize(size.first, newSize - size.first)
        }
        clampToBounds()
    }

    fun onDragStart() {
        dragSize = size
    }

    fun onDrag(delta: Float) {
        dragSize = SashSize(dragSize.first + delta, dragSize.second - delta)
        size = dragSize
        clampToBounds()
    }
}


@Composable
fun Sash(
    first: @Composable BoxScope.() -> Unit,
    second: @Composable BoxScope.() -> Unit,
    orientation: Orientation,
    sashData: SashData = rememberSashData(),
    sashSize: Dp = 4.dp,
    onResize: ((SashData) -> Unit)? = null
) {
    when (orientation) {
        Orientation.Vertical -> VSash(first, second, sashData, sashSize, onResize)
        Orientation.Horizontal -> HSash(first, second, sashData, sashSize, onResize)
    }
}

@Composable
fun HSash(
    left: @Composable BoxScope.() -> Unit,
    right: @Composable BoxScope.() -> Unit,
    sashData: SashData = rememberSashData(),
    sashSize: Dp = 4.dp,
    onResize: ((SashData) -> Unit)? = null
) {
    LocalDensity.current.run {
        val sashPx = sashSize.toPx()

        Row(Modifier.onSizeChanged {
            sashData.onResize(it.width - sashPx)
            onResize?.invoke(sashData)
        }) {
            val firstMod =
                if (sashData.resizePriority == ResizePriority.SECOND)
                    Modifier.width(sashData.size.first.toDp())
                else
                    Modifier.weight(sashData.size.boundFirstWeight)
            Box(firstMod.fillMaxSize()) {
                left()
            }

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(sashSize)
                    .draggable(
                        state = rememberDraggableState {
                            sashData.onDrag(it)
                        },
                        orientation = Orientation.Horizontal,
                        onDragStarted = { sashData.onDragStart() },
                        onDragStopped = {
                            onResize?.invoke(sashData)
                        }
                    )
                    .pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
            )

            val secondMod =
                if (sashData.resizePriority == ResizePriority.FIRST)
                    Modifier.width(sashData.size.second.toDp())
                else
                    Modifier.weight(sashData.size.boundSecondWeight)
            Box(secondMod.fillMaxSize()) {
                right()
            }
        }
    }
}

@Composable
fun VSash(
    top: @Composable BoxScope.() -> Unit,
    bottom: @Composable BoxScope.() -> Unit,
    sashData: SashData = rememberSashData(),
    sashSize: Dp = 5.dp,
    onResize: ((SashData) -> Unit)? = null
) {
    LocalDensity.current.run {
        val sashPx = sashSize.toPx()

        Column(Modifier.onSizeChanged {
            sashData.onResize(it.height - sashPx)
            onResize?.invoke(sashData)
        }) {
            val firstMod =
                if (sashData.resizePriority == ResizePriority.SECOND)
                    Modifier.height(sashData.size.first.toDp())
                else
                    Modifier.weight(sashData.size.boundFirstWeight)
            Box(firstMod.fillMaxSize()) {
                top()
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(sashSize)
                    .draggable(
                        state = rememberDraggableState {
                            sashData.onDrag(it)
                        },
                        orientation = Orientation.Vertical,
                        onDragStarted = { sashData.onDragStart() },
                        onDragStopped = {
                            onResize?.invoke(sashData)
                        }
                    )
                    .pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))
            )

            val secondMod =
                if (sashData.resizePriority == ResizePriority.FIRST)
                    Modifier.height(sashData.size.second.toDp())
                else
                    Modifier.weight(sashData.size.boundSecondWeight)
            Box(secondMod.fillMaxSize()) {
                bottom()
            }
        }
    }
}