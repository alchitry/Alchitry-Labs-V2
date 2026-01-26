package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Settings
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.ubuntuMonoFont
import me.tongfei.progressbar.ProgressBarConsumer

class Console(onKeyEvent: (KeyEvent) -> Boolean = { false }) {
    companion object {
        val main = Console()
    }

    private val state = AlchitryTextFieldState(
        onKeyEvent = onKeyEvent,
        isReadOnly = true,
        onReplaceText = { _, _ -> true } // prevent writes
    )

    private var activeProgressBar: String? = null

    private fun clearLastLine() {
        val lineCount = state.lines.size
        val lastLine = state.lines.getOrNull(lineCount - 1) ?: return
        val lineNumber = lineCount - 1
        val startPosition = TextPosition(lineNumber, 0)
        val endPosition = TextPosition(lineNumber, lastLine.text.length)
        state.forceReplaceText("", startPosition..<endPosition, false)
        state.notations.removeIf { it.range.endExclusive.line == lineNumber }
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
                            color = AlchitryColors.Companion.current.ProgressBar,
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

        Column {
            state.searchAndReplaceState.drawBar()
            Row(Modifier.fillMaxWidth().clipToBounds()) {
                val width =
                    if (Settings.consoleScrollBarStyle == Settings.ScrollBarStyle.MiniText) 1f - MINI_TEXT_SCALE else 1f
                Box(Modifier.Companion.fillMaxWidth(width)) {
                    Canvas(
                        modifier = Modifier.Companion.fillMaxSize()
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
                        BoxWithConstraints(Modifier.Companion.padding(start = 10.dp)) {
                            state.redrawTriggerStates.value
                            with(LocalDensity.current) {
                                state.updateLayout(maxWidth.roundToPx(), maxHeight.roundToPx(), this)
                            }

                            Canvas(
                                modifier = Modifier.Companion
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
                        Modifier.Companion.align(Alignment.Companion.BottomStart).fillMaxWidth()
                            .padding(
                                bottom = 8.dp,
                                start = 8.dp,
                                end = if (Settings.consoleScrollBarStyle == Settings.ScrollBarStyle.Minimal) 24.dp else 8.dp
                            ),
                    )
                    if (Settings.consoleScrollBarStyle == Settings.ScrollBarStyle.Minimal) {
                        VerticalScrollbar(
                            rememberScrollbarAdapter(state.verticalScrollState),
                            Modifier.Companion.align(Alignment.Companion.CenterEnd).fillMaxHeight()
                                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                        )
                    }
                }

                if (Settings.consoleScrollBarStyle == Settings.ScrollBarStyle.MiniText) {
                    ScrollStateMiniScrollBar(
                        state.verticalScrollState,
                        state.horizontalScrollState,
                        MINI_TEXT_SCALE,
                        state.lines,
                        Modifier.Companion.fillMaxWidth()
                    )
                }
            }
        }
    }
}