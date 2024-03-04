package com.alchitry.labs2.ui.code_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.round

data class HighlightAnnotation(
    val range: ClosedRange<TextPosition>,
    val color: Color
) {
    context(DrawScope)
    fun draw(lineState: CodeLineState) {
        require(range.start.line == range.endInclusive.line) {
            "When drawing on a single line the range must be only a single line."
        }
        if (range.isEmpty())
            return

        val layout = lineState.layoutResult ?: return

        val xStart = round(
            layout.getHorizontalPosition(
                range.start.offset.coerceIn(0, lineState.text.length),
                usePrimaryDirection = true
            )
        )

        val xEnd = round(
            layout.getHorizontalPosition(
                range.endInclusive.offset.coerceIn(0, lineState.text.length),
                usePrimaryDirection = true
            )
        )

        drawRect(color, Offset(xStart, 0f), Size(xEnd - xStart, (lineState.lineHeight ?: 0).toFloat()))
    }

    context(DrawScope)
    fun draw(editorState: CodeEditorState) {
        if (range.isEmpty())
            return

        val firstPos = range.start
        val secondPos = range.endInclusive

        val bounds = Rect(
            0f,
            editorState.offsetAtLineTop(firstPos.line).toFloat(),
            size.width,
            editorState.offsetAtLineBottom(secondPos.line).toFloat()
        )

        if (!editorState.getWindow().overlaps(bounds)) {
            return
        }

        translate(top = -editorState.scrollState.value.toFloat()) {
            // first line (might be partial)
            val start = with(editorState) { firstPos.getTopOffset() }
            val end = if (secondPos.line == firstPos.line) {
                with(editorState) { secondPos.getBottomOffset() }
            } else {
                Offset(size.width, start.y + (editorState.lineHeight(firstPos.line) ?: 0).toFloat())
            }

            val firstRect = Rect(topLeft = start, bottomRight = end)

            drawRect(
                color,
                topLeft = firstRect.topLeft,
                size = firstRect.size
            )


            // main bulk rect between partial selections
            if (secondPos.line - firstPos.line > 1) {
                val top = editorState.offsetAtLineTop(firstPos.line + 1)
                val bottom = editorState.offsetAtLineBottom(secondPos.line - 1)

                val bulkRect = Rect(0f, top.toFloat(), size.width, bottom.toFloat())
                drawRect(
                    color,
                    topLeft = bulkRect.topLeft,
                    size = bulkRect.size
                )
            }

            // final partial selection line
            if (secondPos.line != firstPos.line) {
                val top = Offset(0f, editorState.offsetAtLineTop(secondPos.line).toFloat())
                val bottom = with(editorState) { secondPos.getBottomOffset() }
                val finalRect = Rect(topLeft = top, bottomRight = bottom)
                drawRect(
                    color,
                    topLeft = finalRect.topLeft,
                    size = finalRect.size
                )
            }
        }
    }
}