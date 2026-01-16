package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.alchitry_text_field.tooltip.EditorTooltipArea
import com.alchitry.labs2.ui.dialogs.CopyReadOnlyDialog
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.AlchitryTypography
import java.awt.Cursor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeEditor(
    state: CodeEditorState
) {
    state.textFieldState.clipboardManager = LocalClipboardManager.current

    CopyReadOnlyDialog(state.showReadOnlyDialog, state.file) { state.showReadOnlyDialog = false }

    Box(contentAlignment = Alignment.TopStart) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            with(state.textFieldState.selectionManager) {
                drawLineHighlight()
            }
        }

        Row {
            LazyLayout(
                itemProvider = {
                    object : LazyLayoutItemProvider {
                        override val itemCount: Int
                            get() = state.textFieldState.getLineCount()

                        @Composable
                        override fun Item(index: Int, key: Any) {
                            val notation = state.textFieldState.lineNotationLevel(index)
                            state.textFieldState.redrawTriggerStates.value // trigger redraw on changes TODO: trigger only on notation color change
                            val alpha =
                                if (state.textFieldState.selectionManager.active &&
                                    state.textFieldState.selectionManager.caret.line == index ||
                                    notation != null
                                ) 1f else 0.4f
                            CompositionLocalProvider(
                                LocalContentColor provides (notation?.style?.color
                                    ?: AlchitryColors.current.GutterForeground),
                                LocalTextStyle provides AlchitryTypography.editor
                            ) {
                                // if the index is negative,
                                // this means it is being used
                                // to find the width the gutter should be
                                // the value is the max number of digits,
                                // so by using "8" we should be measuring the widest one
                                // it is offset by -1 so that it will never be -1 as it is used as a flag by Compose
                                val lineNumber = if (index < 0) "8".repeat(-(index + 1)) else (index + 1).toString()
                                val lineActions = state.lineActions?.get(index)

                                val density = LocalDensity.current
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        lineNumber,
                                        textAlign = TextAlign.Right,
                                        modifier = Modifier
                                            .offset( // +1 offset to make text centered
                                                y = state.textFieldState.lineTopOffset.dp + 1.dp
                                            )
                                            .alpha(alpha)
                                    )
                                    if (state.maxLineActions > 0) {
                                        Box(
                                            Modifier.aspectRatio(
                                                state.maxLineActions.toFloat(),
                                                matchHeightConstraintsFirst = true
                                            )
                                        ) {
                                            lineActions?.forEach { action ->
                                                key(action) {
                                                    Box(Modifier.aspectRatio(1f, matchHeightConstraintsFirst = true)) {
                                                        action.content(this)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .scrollable(
                        state.textFieldState.verticalScrollState,
                        Orientation.Vertical,
                        reverseDirection = true
                    ),
                prefetchState = null,
                measurePolicy = fun LazyLayoutMeasureScope.(constraints: Constraints): MeasureResult {
                    with(state) {
                        layoutGutter()
                    }

                    // subscribe to editor changes
                    state.textFieldState.redrawTriggerStates.value

                    var y = -state.textFieldState.verticalScrollState.value

                    val placeables =
                        state.textFieldState.lines.mapIndexedNotNull { lineNum, lineState ->
                            val lineHeight = lineState.lineHeight ?: 0
                            val nextY = y + lineHeight

                            val result = if (nextY >= 0 && y < constraints.maxHeight) {
                                val measured =
                                    measure(lineNum, Constraints.fixed(state.gutterWidth, lineHeight))
                                val yOffset = y + (lineHeight - (measured.maxOfOrNull { it.height } ?: 0)) / 2
                                yOffset to measured
                            } else null

                            y = nextY
                            result
                        }

                    return layout(
                        width = state.gutterWidth,
                        height = constraints.maxHeight
                    ) {
                        placeables.forEach { p ->
                            p.second.forEach {
                                it.place(state.gutterWidth - it.width, p.first)
                            }
                        }
                    }
                }
            )

            Box {
                ContextMenuArea(
                    items = {
                        listOf(
                            ContextMenuItem("Cut") { state.textFieldState.cut() },
                            ContextMenuItem("Copy") { state.textFieldState.copy() },
                            ContextMenuItem("Paste") { state.textFieldState.paste() },
                            ContextMenuItem("Select All") { state.textFieldState.selectionManager.selectAll() },
                            ContextMenuItem("Format") { state.textFieldState.adjustIndents() },
                        )
                    }
                ) {
                    EditorTooltipArea(
                        state = state.textFieldState.tooltipState,
                        tooltip = { notation ->
                            notation.message?.let { Text(it) }
                        }
                    ) {
                        BoxWithConstraints {
                            state.textFieldState.redrawTriggerStates.value
                            with(LocalDensity.current) {
                                state.textFieldState.updateLayout(maxWidth.roundToPx(), maxHeight.roundToPx(), this)
                            }
                            Canvas(
                                modifier = Modifier
                                    .clipToBounds()
                                    .scrollable(
                                        state.textFieldState.verticalScrollState,
                                        Orientation.Vertical,
                                        reverseDirection = true
                                    )
                                    .scrollable(
                                        state.textFieldState.horizontalScrollState,
                                        Orientation.Horizontal,
                                        reverseDirection = true
                                    )
                                    .fillMaxSize()
                                    .pointerHoverIcon(textCursor)
                                    .then(state.textFieldState.keyModifier())
                                    .then(state.textFieldState.tapModifier())

                            ) {
                                with(state.textFieldState) {
                                    draw()
                                }
                            }

                            state.textFieldState.autocomplete?.suggestionOverlay(state.textFieldState)
                        }
                    }
                }
                VerticalScrollbar(
                    rememberScrollbarAdapter(state.textFieldState.verticalScrollState),
                    Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                )
                HorizontalScrollbar(
                    rememberScrollbarAdapter(state.textFieldState.horizontalScrollState),
                    Modifier.align(Alignment.BottomStart).fillMaxWidth()
                        .padding(bottom = 8.dp, start = 8.dp, end = 20.dp),
                )
            }
        }
    }
}


val textCursor = PointerIcon(Cursor(Cursor.TEXT_CURSOR))



