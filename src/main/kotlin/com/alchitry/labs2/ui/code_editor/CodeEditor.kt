package com.alchitry.labs2.ui.code_editor

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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.code_editor.tooltip.EditorTooltipArea
import com.alchitry.labs2.ui.dialogs.CopyReadOnlyDialog
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.AlchitryTypography
import java.awt.Cursor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeEditor(
    state: CodeEditorState
) {
    state.clipboardManager = LocalClipboardManager.current

    CopyReadOnlyDialog(state.showReadOnlyDialog, state.file) { state.showReadOnlyDialog = false }

    Box(contentAlignment = Alignment.TopStart) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            with(state.selectionManager) {
                drawLineHighlight()
            }
        }

        Row {
            LazyLayout(
                itemProvider = {
                    object : LazyLayoutItemProvider {
                        override val itemCount: Int
                            get() = state.lines.size

                        @Composable
                        override fun Item(index: Int, key: Any) {
                            val notation = state.lineNotationLevel(index)
                            state.redrawTriggerStates.value // trigger redraw on changes TODO: trigger only on notation color change
                            val alpha =
                                if (state.selectionManager.active && state.selectionManager.caret.line == index || notation != null) 1f else 0.4f
                            CompositionLocalProvider(
                                LocalContentColor provides (notation?.color ?: AlchitryColors.current.GutterForeground),
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
                                                y = state.lineTopOffset.dp * density.density + 1.dp
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
                    .scrollable(state.scrollState, Orientation.Vertical, reverseDirection = true),
                prefetchState = null,
                measurePolicy = fun LazyLayoutMeasureScope.(constraints: Constraints): MeasureResult {
                    with(state) {
                        layout(constraints)
                    }

                    // subscribe to editor changes
                    state.redrawTriggerStates.value

                    var y = -state.scrollState.value

                    val placeables =
                        state.lines.mapIndexedNotNull { lineNum, lineState ->
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

            val horizontalScroll = rememberScrollState()
            Box(
                Modifier
                    .scrollable(horizontalScroll, Orientation.Horizontal)
            ) {
                ContextMenuArea(
                    items = {
                        listOf(
                            ContextMenuItem("Cut") { state.cut() },
                            ContextMenuItem("Copy") { state.copy() },
                            ContextMenuItem("Paste") { state.paste() },
                            ContextMenuItem("Select All") { state.selectionManager.selectAll() },
                            ContextMenuItem("Format") { state.adjustIndents() },
                        )
                    }
                ) {
                    EditorTooltipArea(
                        state = state.tooltipState,
                        tooltip = { notation ->
                            notation.message?.let { Text(it) }
                        }
                    ) {
                        Canvas(
                            modifier = Modifier
                                .scrollable(state.scrollState, Orientation.Vertical, reverseDirection = true)
                                .fillMaxSize()
                                .pointerHoverIcon(textCursor)
                                .then(state.keyModifier())
                                .then(state.tapModifier())

                        ) {
                            with(state) {
                                draw()
                            }
                        }
                        state.autocomplete?.suggestionOverlay()
                    }
                }
            }
        }
    }
}


private val textCursor = PointerIcon(Cursor(Cursor.TEXT_CURSOR))



