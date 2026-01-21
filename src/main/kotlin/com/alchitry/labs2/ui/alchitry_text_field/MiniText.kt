package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val MINI_TEXT_SCALE = 0.08f

@Composable
fun ScrollStateMiniScrollBar(
    verticalScrollState: ScrollState,
    horizontalScrollState: ScrollState,
    scale: Float,
    lines: SnapshotStateList<AlchitryLineState>,
    modifier: Modifier = Modifier
) {
    val max = verticalScrollState.maxValue.toFloat().coerceAtLeast(1f)
    val startPercent = verticalScrollState.value.toFloat() / max
    val visiblePercent = (verticalScrollState.viewportSize / (max + verticalScrollState.viewportSize)).coerceIn(0f, 1f)
    val scope = rememberCoroutineScope()

    MiniScrollBar(
        horizontalScrollState = horizontalScrollState,
        scale = scale,
        startPercent = startPercent,
        visiblePercent = visiblePercent,
        onScroll = { it, animate ->
            val p = it.coerceIn(0f, 1f)
            val offset = (verticalScrollState.maxValue * p).roundToInt()
            scope.launch {
                if (animate)
                    verticalScrollState.animateScrollTo(offset)
                else
                    verticalScrollState.scrollTo(offset)
            }
        },
        modifier = modifier,
        content = {
            MiniText(lines, scale)
        }
    )
}

@Composable
fun MiniScrollBar(
    horizontalScrollState: ScrollState,
    scale: Float,
    startPercent: Float,
    visiblePercent: Float,
    onScroll: (Float, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var miniTextHeight by remember { mutableStateOf(0) }
    var actualHeight by remember { mutableStateOf(0) }

    var scrollAmount by remember { mutableStateOf(startPercent to visiblePercent) }

    SideEffect {
        scrollAmount = startPercent to visiblePercent
    }

    Layout(
        modifier = modifier.clipToBounds(),
        content = {
            Box(
                Modifier.pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        val offsetLoc = down.position.y
                        val adjustedHeight = miniTextHeight * (1f - scrollAmount.second)
                        val scrollPercent = (offsetLoc / adjustedHeight - scrollAmount.second / 2).coerceIn(0f, 1f)
                        onScroll(scrollPercent, true)
                    }
                }
            ) {
                content()
            }
            Box(
                Modifier
                    .alpha(0.1f)
                    .background(LocalContentColor.current)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            down.consume()
                            var scrollPercent = scrollAmount.first
                            drag(down.id) {
                                val adjustedHeight = miniTextHeight * (1f - scrollAmount.second)
                                val visibleBoxSize = adjustedHeight * scrollAmount.second
                                val adjustedActualHeight = actualHeight * (1f - scrollAmount.second)
                                val change =
                                    (it.positionChange().y / (adjustedActualHeight - visibleBoxSize)) * (1f - scrollAmount.second)
                                scrollPercent += change
                                onScroll(scrollPercent, false)
                            }
                        }
                    }
            )
        }
    ) { measurables, constraints ->
        require(measurables.size == 2) { "Expected exactly two children composables!" }
        val miniText =
            measurables[0].measure(Constraints())//constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)) // unconstrained
        miniTextHeight = miniText.height
        actualHeight = miniText.height.coerceIn(constraints.minHeight, constraints.maxHeight)
        val actualWidth = miniText.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        val boxHeight = (miniText.height * visiblePercent).let { if (it.isNaN()) 0f else it }.roundToInt()
        val boxStart =
            (miniText.height * startPercent * (1f - visiblePercent)).let { if (it.isNaN()) 0f else it }.roundToInt()

        val oversizeOffset =
            ((miniText.height - actualHeight) * startPercent).let { if (it.isNaN()) 0f else it }
                .roundToInt().coerceAtLeast(0)

        val overlayBox = measurables[1].measure(
            Constraints(
                minWidth = actualWidth.coerceAtLeast(1),
                maxWidth = actualWidth.coerceAtLeast(1),
                minHeight = boxHeight.coerceAtLeast(1),
                maxHeight = boxHeight.coerceAtLeast(1)
            )
        )

        layout(actualWidth, actualHeight) {
            miniText.place((-horizontalScrollState.value * scale).roundToInt(), -oversizeOffset)
            overlayBox.place(0, boxStart - oversizeOffset)
        }
    }
}

// Splitting this into its own function reduces expensive redrawing
@Composable
fun MiniText(
    content: SnapshotStateList<AlchitryLineState>,
    scale: Float
) {
    Column(modifier = Modifier.scaleSize(scale)) {
        content.forEach {
            Text(it.text, style = it.style ?: LocalTextStyle.current)
        }
        Spacer(modifier = Modifier.height(AlchitryTextFieldState.EXTRA_VERT_PADDING))
    }
}

/**
 * Scales both the appearance and layout size by scale.
 */
fun Modifier.scaleSize(scale: Float) = this.layout { measurable, constraints ->
    val placeable = measurable.measure(Constraints())
    val xPadding = (placeable.width - placeable.width * scale) / 2
    val yPadding = (placeable.height - placeable.height * scale) / 2
    val desiredWidth = (placeable.width * scale).roundToInt()
    val desiredHeight = (placeable.height * scale).roundToInt()
    val actualHeight = desiredHeight.coerceIn(constraints.minHeight, constraints.maxHeight)
    val actualWidth = desiredWidth.coerceIn(constraints.minWidth, constraints.maxWidth)

    layout(actualWidth, actualHeight) {
        placeable.place(-xPadding.roundToInt(), -yPadding.roundToInt())
    }
}.scale(scale)