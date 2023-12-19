package com.alchitry.labs.ui.code_editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.alchitry.labs.ui.code_editor.tooltip.EditorTooltipArea
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.ui.theme.AlchitryTypography
import java.awt.Cursor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeEditor(
    state: CodeEditorState
) {
    state.clipboardManager = LocalClipboardManager.current

    Box(contentAlignment = Alignment.TopStart) {
        Canvas(
            modifier = Modifier
                .scrollable(state.scrollState, Orientation.Vertical, reverseDirection = true)
                .fillMaxSize()
        ) {
            with(state.selectionManager) {
                drawLineHighlight()
            }
        }

        Row {

            LazyLayout(
                itemProvider = object : LazyLayoutItemProvider {
                    override val itemCount: Int
                        get() = state.lines.size

                    @Composable
                    override fun Item(index: Int, key: Any) {
                        val notation = state.lineNotationLevel(index)
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
                            val density = LocalDensity.current
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    lineNumber,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                        .offset( // offset to make text centered
                                            y = state.lineTopOffset.dp * density.density
                                        )
                                        .alpha(alpha)
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .scrollable(state.scrollState, Orientation.Vertical, reverseDirection = true)
            ) { constraints ->
                with(state) {
                    layout(constraints)
                }

                var y = -state.scrollState.value

                val placeables =
                    state.lines.mapIndexedNotNull { lineNum, lineState ->
                        val nextY = y + lineState.lineHeight

                        val result = if (nextY > 0 && y < constraints.maxHeight) {
                            val measured =
                                measure(lineNum, Constraints.fixed(state.gutterWidth, lineState.lineHeight))
                            val yOffset = y + (lineState.lineHeight - (measured.maxOf { it.height })) / 2
                            yOffset to measured
                        } else null

                        y = nextY
                        result
                    }

                layout(
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

            val horizontalScroll = rememberScrollState()
            Box(
                Modifier
                    .scrollable(horizontalScroll, Orientation.Horizontal)
                    .padding(start = 10.dp)
            ) {
                state.subscribe(currentRecomposeScope)
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
                }

            }
        }
    }
}


private val textCursor = PointerIcon(Cursor(Cursor.TEXT_CURSOR))



