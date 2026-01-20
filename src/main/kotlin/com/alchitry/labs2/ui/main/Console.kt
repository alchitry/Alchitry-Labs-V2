package com.alchitry.labs2.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.key.KeyEvent
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
import com.alchitry.labs2.ui.alchitry_text_field.AlchitryLineState
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

class Console(onKeyEvent: (KeyEvent) -> Boolean = { false }) {
    companion object {
        val main = Console()
        private const val SCALE = 0.08f
    }

    private val state = AlchitryTextFieldState(
        onKeyEvent = onKeyEvent,
        onReplaceText = { _, _ -> true } // read only
    )

    private var activeProgressBar: String? = null

    private fun clearLastLine() {
        val lineCount = state.lines.size
        val lastLine = state.lines.getOrNull(lineCount - 1) ?: return
        val lineNumber = lineCount - 1
        val startPosition = TextPosition(lineNumber, 0)
        val endPosition = TextPosition(lineNumber, lastLine.text.length)
        state.forceReplaceText("", startPosition..<endPosition, false)
        state.notations.removeIf { it.range.endInclusive.line == lineNumber }
    }

    val progressBarConsumer = object : ProgressBarConsumer {
        override fun clear() {
            if (state.lines.lastOrNull()?.text?.text == activeProgressBar) {
                clearLastLine()
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
                clearLastLine()
            }

            state.appendText(newLine, false)
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
        state.queueAppendText(text) // TODO: deal with styles
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
    fun show() {
        state.subscribe()
        state.clipboardManager = LocalClipboardManager.current

        Row {
            Box(Modifier.fillMaxWidth(1f - SCALE)) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    state.redrawTriggerStates.value
                    with(state.selectionManager) {
                        drawLineHighlight()
                    }
                }
                ContextMenuArea(
                    items = {
                        listOf(
                            ContextMenuItem("Copy") { state.copy() },
                            ContextMenuItem("Select All") { state.selectionManager.selectAll() },
                            ContextMenuItem("Clear All") { clear() },
                        )
                    }
                ) {
                    BoxWithConstraints(Modifier.padding(start = 10.dp)) {
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
                HorizontalScrollbar(
                    rememberScrollbarAdapter(state.horizontalScrollState),
                    Modifier.align(Alignment.BottomStart).fillMaxWidth()
                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                )
            }

            ScrollStateMiniScrollBar(
                state.verticalScrollState,
                state.horizontalScrollState,
                SCALE,
                state.lines,
                Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ScrollStateMiniScrollBar(
    verticalScrollState: ScrollState,
    horizontalScrollState: ScrollState,
    scale: Float,
    lines: SnapshotStateList<AlchitryLineState>,
    modifier: Modifier = Modifier
) {
    val max = (verticalScrollState.maxValue + verticalScrollState.viewportSize).toFloat().coerceAtLeast(1f)
    val startPercent = verticalScrollState.value.toFloat() / max
    val visiblePercent = verticalScrollState.viewportSize.toFloat() / max
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
            measurables[0].measure(Constraints())//constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity)) // unconstrained
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