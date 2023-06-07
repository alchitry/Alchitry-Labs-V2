package com.alchitry.labs.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun rememberSashData(resizePriority: ResizePriority = ResizePriority.EQUAL, first: Float = 1f, second: Float = 1f) =
    remember {
        SashData(resizePriority, mutableStateOf(SashSize(first, second)))
    }

data class SashSize(
    val first: Float = 1f,
    val second: Float = 1f
) {
    val firstWeight: Float get() = first / (first + second)
    val secondWeight: Float get() = second / (first + second)

    val boundFirstWeight: Float get() = firstWeight.coerceIn(0.001f, 0.999f)
    val boundSecondWeight: Float get() = secondWeight.coerceIn(0.001f, 0.999f)
}

data class SashData(
    val resizePriority: ResizePriority = ResizePriority.EQUAL,
    val size: MutableState<SashSize> = mutableStateOf(SashSize()),
) {
    fun clampToBounds() {
        val totalSize = size.value.first + size.value.second
        if (size.value.first < 1f) {
            size.value = SashSize(1f, totalSize - 1f)
        } else if (size.value.second < 1f) {
            size.value = SashSize(totalSize - 1f, 1f)
        }
    }

    fun onResize(newSize: Float) {
        size.value = when (resizePriority) {
            ResizePriority.EQUAL -> SashSize(newSize * size.value.second, newSize - size.value.first)
            ResizePriority.FIRST -> SashSize(newSize - size.value.second, size.value.second)
            ResizePriority.SECOND -> SashSize(size.value.first, newSize - size.value.first)
        }
    }

    fun onDrag(delta: Float) {
        size.value = SashSize(size.value.first + delta, size.value.second - delta)
    }
}


@Composable
fun Sash(
    first: @Composable BoxScope.() -> Unit,
    second: @Composable BoxScope.() -> Unit,
    orientation: Orientation,
    sashData: SashData = rememberSashData(),
    sashSize: Dp = 5.dp,
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
    sashSize: Dp = 5.dp,
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
                    Modifier.width(sashData.size.value.first.toDp())
                else
                    Modifier.weight(sashData.size.value.boundFirstWeight)
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
                        onDragStopped = {
                            sashData.clampToBounds()
                            onResize?.invoke(sashData)
                        }
                    )
                    .pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
            )

            val secondMod =
                if (sashData.resizePriority == ResizePriority.FIRST)
                    Modifier.width(sashData.size.value.second.toDp())
                else
                    Modifier.weight(sashData.size.value.boundSecondWeight)
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
                    Modifier.height(sashData.size.value.first.toDp())
                else
                    Modifier.weight(sashData.size.value.boundFirstWeight)
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
                        onDragStarted = {
                            sashData.clampToBounds()
                        },
                        onDragStopped = {
                            sashData.clampToBounds()
                            onResize?.invoke(sashData)
                        }
                    )
                    .pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))
            )

            val secondMod =
                if (sashData.resizePriority == ResizePriority.FIRST)
                    Modifier.height(sashData.size.value.second.toDp())
                else
                    Modifier.weight(sashData.size.value.boundSecondWeight)
            Box(secondMod.fillMaxSize()) {
                bottom()
            }
        }
    }
}