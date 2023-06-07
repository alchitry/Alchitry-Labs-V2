package com.alchitry.labs.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private data class StyledText(
    val text: String,
    val style: SpanStyle?
)

object Console {
    private val content = mutableStateListOf<AnnotatedString>()
    private var scrollToIdx by mutableStateOf(-1)
    private const val scale = 0.08f

    fun append(text: String, style: SpanStyle? = null) {
        content.add(buildAnnotatedString {
            if (style != null)
                withStyle(style) {
                    append(text)
                }
            else append(text)
        })
        scrollToIdx = content.size - 3
    }

    fun clear() {
        content.clear()
    }

    @Composable
    fun show() {
        val lazyListState = rememberLazyListState()
        LaunchedEffect(scrollToIdx) {
            if (scrollToIdx >= 0) {
                lazyListState.animateScrollToItem(scrollToIdx)
                scrollToIdx = -1
            }
        }
        Box(Modifier.fillMaxSize()) {
            SelectionContainer {
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.fillMaxWidth(1f - scale)
                ) {
                    items(content) {
                        Text(it)
                    }
                }
            }

            LazyListMiniScrollBar(lazyListState, Modifier.fillMaxWidth(scale).align(Alignment.TopEnd)) {
                MiniText(content, scale)
            }
        }
    }
}

@Composable
fun LazyListMiniScrollBar(
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val layoutInfo = lazyListState.layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo

    if (visibleItems.isEmpty()) return

    val first = visibleItems.first()
    val last = visibleItems.last()
    val itemCount = layoutInfo.totalItemsCount
    val startPercent =
        first.index / (itemCount.toFloat() - 1) - (first.offset.toFloat() - layoutInfo.viewportStartOffset) / first.size / itemCount
    val endPercent =
        last.index / (itemCount.toFloat() - 1) - (last.size - (layoutInfo.viewportEndOffset.toFloat() - last.offset).coerceIn(
            0f,
            last.size.toFloat()
        )) / last.size / itemCount
    val visiblePercent = endPercent - startPercent

    val scope = rememberCoroutineScope()

    MiniScrollBar(
        startPercent = startPercent,
        visiblePercent = visiblePercent,
        onScroll = { it, animate ->
            val p = it.coerceIn(0f, 1f)
            val itemCt = lazyListState.layoutInfo.totalItemsCount
            val index = (itemCt * p).toInt().coerceIn(0, itemCt - 1)
            val offset = ((itemCt * p) % 1 * first.size).toInt()
            scope.launch {
                if (animate)
                    lazyListState.animateScrollToItem(index, offset)
                else
                    lazyListState.scrollToItem(index, offset)
            }
        },
        modifier = modifier,
        content = content
    )
}

@Composable
fun MiniScrollBar(
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
        modifier = modifier,
        content = {
            Box(
                Modifier.pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitFirstDown()
                            down.consume()
                            val offsetLoc = down.position.y
                            val scrollPercent = (offsetLoc / miniTextHeight - scrollAmount.second / 2).coerceIn(0f, 1f)
                            onScroll(scrollPercent, true)
                        }
                    }
                }
            ) {
                content()
            }
            Box(
                Modifier
                    .alpha(0.1f)
                    .background(Color.White)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        forEachGesture {
                            awaitPointerEventScope {
                                val down = awaitFirstDown()
                                down.consume()
                                var scrollPercent = scrollAmount.first
                                drag(down.id) {
                                    val visibleBoxSize = miniTextHeight * scrollAmount.second
                                    val change =
                                        (it.positionChange().y / (actualHeight - visibleBoxSize)) * (1f - scrollAmount.second)
                                    scrollPercent += change
                                    onScroll(scrollPercent, false)
                                }
                            }
                        }
                    }
            )
        }
    ) { measurables, constraints ->
        require(measurables.size == 2) { "Expected exactly two children composables!" }
        val miniText =
            measurables[0].measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)) // unconstrained
        miniTextHeight = miniText.height
        actualHeight = miniText.height.coerceIn(constraints.minHeight, constraints.maxHeight)
        val actualWidth = miniText.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        val boxHeight = (miniText.height * visiblePercent).roundToInt()
        val boxStart = (miniText.height * startPercent).roundToInt()

        val oversizeOffset =
            ((miniText.height - actualHeight) * (startPercent / (1f - visiblePercent))).roundToInt().coerceAtLeast(0)

        val overlayBox = measurables[1].measure(
            Constraints(
                minWidth = actualWidth,
                maxWidth = actualWidth,
                minHeight = boxHeight,
                maxHeight = boxHeight
            )
        )

        layout(actualWidth, actualHeight) {
            miniText.place(0, -oversizeOffset)
            overlayBox.place(0, boxStart - oversizeOffset)
        }
    }
}

// Splitting this into its own function reduces expensive redrawing
@Composable
fun MiniText(
    content: SnapshotStateList<AnnotatedString>,
    scale: Float
) {
    Column(modifier = Modifier.scaleSize(scale)) {
        content.forEach {
            Text(it)
        }
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