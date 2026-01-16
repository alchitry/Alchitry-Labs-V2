package com.alchitry.labs2.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.alchitry_text_field.AlchitryTextFieldState
import com.alchitry.labs2.ui.alchitry_text_field.TextPosition
import com.alchitry.labs2.ui.alchitry_text_field.textCursor
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.ubuntuMonoFont
import kotlinx.coroutines.launch
import me.tongfei.progressbar.ProgressBarConsumer
import kotlin.math.roundToInt

private data class StyledText(
    val text: String,
    val style: SpanStyle?
)

class Console {
    companion object {
        val main = Console()
        private const val SCALE = 0.08f
    }

    private val state = AlchitryTextFieldState(
        onReplaceText = { _, _ -> true } // read only
    )

    private var activeProgressBar: String? = null

    private fun removeLastLine() {
        val lineCount = state.lines.size
        val lastLine = state.lines.getOrNull(lineCount - 1) ?: return
        val lastLastLine = state.lines.getOrNull(lineCount - 2)
        val startLine = if (lastLastLine == null) lineCount - 1 else lineCount - 2
        val startOffset = lastLastLine?.text?.length ?: 0
        val startPosition = TextPosition(startLine, startOffset)
        val endPosition = TextPosition(lineCount - 1, lastLine.text.length)
        state.replaceText("", startPosition..<endPosition)
    }

    val progressBarConsumer = object : ProgressBarConsumer {
        override fun clear() {
            if (state.lines.lastOrNull()?.text?.text == activeProgressBar) {
                removeLastLine()
            }
            activeProgressBar = null
        }

        override fun accept(rendered: String) {
            // over-write the lines
            val overloadedText = buildString {
                rendered.split("\r").forEach { line ->
                    replace(0, line.length.coerceAtMost(length), line)
                }
            }

            val newLine = buildAnnotatedString {
                val firstSplit = overloadedText.split("\u001b[33m")
                val start = firstSplit.firstOrNull()
                val secondSplit = firstSplit.getOrNull(1)?.split("\u001b[0m")
                val middle = secondSplit?.firstOrNull()
                val end = secondSplit?.getOrNull(1)
                start?.let { append(it) }
                middle?.let {
                    withStyle(
                        SpanStyle(
                            color = AlchitryColors.current.ProgressBar,
                            fontFamily = ubuntuMonoFont
                        )
                    ) { append(it) }
                }
                end?.let { append(it) }
            }

            if (state.lines.isNotEmpty() && (state.lines.last().text.text == activeProgressBar || state.lines.last().text.isBlank())) {
                removeLastLine()
            }

            state.appendText(AnnotatedString("\n" + newLine.text))
            activeProgressBar = newLine.text
        }

        override fun close() {
            activeProgressBar = null
        }

        override fun getMaxRenderedLength(): Int = 80
    }

    private fun AnnotatedString.split(
        vararg delimiters: String,
        ignoreCase: Boolean = false,
        limit: Int = 0
    ): List<AnnotatedString> {
        val rawStrings = this.text.split(delimiters = delimiters, ignoreCase, limit)
        val result = mutableListOf<AnnotatedString>()
        val spans = this.spanStyles
        var currentIndex = 0

        for (rawString in rawStrings) {
            val builder = AnnotatedString.Builder(rawString)

            for (span in spans) {
                if (span.start >= currentIndex || span.end <= currentIndex + rawString.length) {
                    builder.addStyle(span.item, span.start - currentIndex, span.end - currentIndex)
                }
            }
            currentIndex += rawString.length + 1

            result.add(builder.toAnnotatedString())
        }

        return result
    }

    fun append(text: AnnotatedString) {
        state.appendText(text) // TODO: deal with styles
    }

    fun append(text: String, style: SpanStyle? = null) {
        append(buildAnnotatedString {
            if (style != null)
                withStyle(style) {
                    append(text)
                }
            else append(text)
        })
    }

    fun clear() {
        state.clearText()
    }

    @Composable
    fun show(showCursor: Boolean = false) {
        state.subscribe()
        state.clipboardManager = LocalClipboardManager.current

        Box(contentAlignment = Alignment.TopStart) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                state.redrawTriggerStates.value
                with(state.selectionManager) {
                    drawLineHighlight()
                }
            }
            Row {
                Spacer(modifier = Modifier.width(10.dp))
                Box {
                    ContextMenuArea(
                        items = {
                            listOf(
                                ContextMenuItem("Copy") { state.copy() },
                                ContextMenuItem("Select All") { state.selectionManager.selectAll() },
                                ContextMenuItem("Clear All") { clear() },
                            )
                        }
                    ) {
                        BoxWithConstraints {
                            state.redrawTriggerStates.value
                            with(LocalDensity.current) {
                                state.updateLayout(maxWidth.roundToPx(), maxHeight.roundToPx(), this)
                            }

                            Canvas(
                                modifier = Modifier
                                    .clipToBounds()
                                    .scrollable(
                                        state.verticalScrollState,
                                        Orientation.Vertical,
                                        reverseDirection = true
                                    )
                                    .scrollable(
                                        state.horizontalScrollState,
                                        Orientation.Horizontal,
                                        reverseDirection = true
                                    )
                                    .fillMaxSize()
                                    .pointerHoverIcon(textCursor)
                                    .then(state.keyModifier())
                                    .then(state.tapModifier())

                            ) {
                                with(state) {
                                    draw()
                                }
                            }
                        }

                    }
                    VerticalScrollbar(
                        rememberScrollbarAdapter(state.verticalScrollState),
                        Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                            .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                    HorizontalScrollbar(
                        rememberScrollbarAdapter(state.horizontalScrollState),
                        Modifier.align(Alignment.BottomStart).fillMaxWidth()
                            .padding(bottom = 8.dp, start = 8.dp, end = 20.dp),
                    )
                }
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
        first.index / (itemCount.toFloat() - 1).coerceAtLeast(1f) - (first.offset.toFloat() - layoutInfo.viewportStartOffset) / first.size / itemCount
    val endPercent =
        last.index / (itemCount.toFloat() - 1).coerceAtLeast(1f) - (last.size - (layoutInfo.viewportEndOffset.toFloat() - last.offset).coerceIn(
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
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        val offsetLoc = down.position.y
                        val scrollPercent = (offsetLoc / miniTextHeight - scrollAmount.second / 2).coerceIn(0f, 1f)
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
                                val visibleBoxSize = miniTextHeight * scrollAmount.second
                                val change =
                                    (it.positionChange().y / (actualHeight - visibleBoxSize)) * (1f - scrollAmount.second)
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
            measurables[0].measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)) // unconstrained
        miniTextHeight = miniText.height
        actualHeight = miniText.height.coerceIn(constraints.minHeight, constraints.maxHeight)
        val actualWidth = miniText.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        val boxHeight = (miniText.height * visiblePercent).let { if (it.isNaN()) 0f else it }.roundToInt()
        val boxStart = (miniText.height * startPercent).let { if (it.isNaN()) 0f else it }.roundToInt()

        val oversizeOffset =
            ((miniText.height - actualHeight) * (startPercent / (1f - visiblePercent))).let { if (it.isNaN()) 0f else it }
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