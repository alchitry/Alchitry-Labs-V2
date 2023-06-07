package com.alchitry.labs.ui.code_editor

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
        val newLine = line.coerceIn(0, lines.size - 1)
        val newOffset = when {
            newLine > line -> 0
            newLine < line -> lines.last().text.length
            else -> offset.coerceIn(0, lines[newLine].text.length)
        }
        return TextPosition(newLine, newOffset)
    }
}
