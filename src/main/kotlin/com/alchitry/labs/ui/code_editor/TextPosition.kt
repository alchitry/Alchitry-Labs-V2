package com.alchitry.labs.ui.code_editor

import androidx.compose.ui.geometry.Offset
import kotlin.math.round

data class TextPosition(val line: Int, val offset: Int) : Comparable<TextPosition> {
    override fun compareTo(other: TextPosition): Int {
        val lineDiff = line - other.line
        if (lineDiff != 0) return lineDiff
        return offset - other.offset
    }

    /**
     * Returns a TextPosition that is the closest valid position for the
     * provided text.
     */
    fun coerceInRange(lines: List<CodeLineState>): TextPosition {
        if (lines.isEmpty())
            return TextPosition(0, 0)
        val newLine = line.coerceIn(0, lines.size - 1)
        val newOffset = when {
            newLine > line -> 0
            newLine < line -> lines.last().text.length
            else -> offset.coerceIn(0, lines[newLine].text.length)
        }
        return TextPosition(newLine, newOffset)
    }

    context(CodeEditorState)
    fun getTopOffset(): Offset {
        val lineState = lines.getOrNull(line)
        val layout = lineState?.layoutResult
        val xOffset = round(
            layout?.getHorizontalPosition(
                offset.coerceIn(0, lineState.text.length),
                usePrimaryDirection = true
            ) ?: 0f
        )
        val yOffset = offsetAtLineTop(line).toFloat()
        return Offset(xOffset, yOffset)
    }

    context(CodeEditorState)
    fun getBottomOffset(): Offset {
        return getTopOffset().let {
            it.copy(
                y = it.y + (lineHeight(line)).toFloat()
            )
        }
    }
}
