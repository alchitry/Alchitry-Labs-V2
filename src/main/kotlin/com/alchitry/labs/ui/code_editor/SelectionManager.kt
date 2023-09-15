package com.alchitry.labs.ui.code_editor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toOffset
import com.alchitry.labs.ui.theme.AlchitryColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SelectionManager(
    private val editorState: CodeEditorState,
    private val cursorColor: Color,
    private val selectionColor: Color,
    private val scope: CoroutineScope,
    private val onChange: () -> Unit
) {
    var active = false
    private var showCursor = true
    var start = TextPosition(0, 0)
    var end = TextPosition(0, 0)
    var caret = TextPosition(0, 0)
        set(v) {
            if (!hasSelection) {
                start = v
                end = v
            }
            field = v
            active = true
            showCaret()
            launchBlinkCursor()
            editorState.onCaretChanged()
        }

    val firstPosition: TextPosition get() = start.coerceAtMost(end)
    val secondPosition: TextPosition get() = start.coerceAtLeast(end)
    val hasSelection: Boolean get() = start != end

    val selectedRange: OpenEndRange<TextPosition>
        get() =
            if (hasSelection) firstPosition..<secondPosition else caret..<caret

    inner class CaretMovement(val old: TextPosition, val new: TextPosition) {
        fun selectMovement() {
            val min = old.coerceAtMost(new)
            val max = old.coerceAtLeast(new)
            val range = min..<max
            if (hasSelection && (range.contains(firstPosition) || selectedRange.contains(min))) {
                excludeMovement()
            } else {
                includeMovement()
            }
        }

        private fun includeMovement() {
            val positions = listOf(start, end, old, new).sorted()
            start = positions[0]
            end = positions[3]
        }

        private fun excludeMovement() {
            if (new > old) { // moved right
                start = new
                end = secondPosition
            } else {
                start = firstPosition
                end = new
            }
        }
    }

    fun selectAll() {
        start = TextPosition(0, 0)
        end = TextPosition(editorState.lines.size - 1, editorState.lines.last().text.length)
        onChange()
    }

    fun selectRange(range: ClosedRange<TextPosition>) {
        start = range.start
        end = range.endInclusive
        onChange()
    }

    fun getSelectedText(): AnnotatedString = AnnotatedString.Builder().apply {
        val first = firstPosition
        val last = secondPosition

        if (first.line == last.line) {
            append(editorState.lines[first.line].text.subSequence(first.offset, last.offset))
            return@apply
        }

        append(
            editorState.lines[first.line].text.subSequence(
                first.offset,
                editorState.lines[first.line].text.length
            )
        )
        append("\n")

        ((first.line + 1)..<last.line).forEach {
            append(editorState.lines[it].text.text)
            append("\n")
        }

        append(editorState.lines[last.line].text.subSequence(0, last.offset))
    }.toAnnotatedString()

    fun selectLine(line: Int) {
        val adjLine = line.coerceIn(0, editorState.lines.size)
        val nextLine = (adjLine + 1).coerceAtMost(editorState.lines.size)
        start = TextPosition(adjLine, 0)
        end = if (nextLine == adjLine)
            TextPosition(adjLine, editorState.lines[adjLine].text.length)
        else
            TextPosition(nextLine, 0)
        onChange()
    }

    fun selectCurrentLine() {
        selectLine(caret.line)
    }

    fun clearSelection() {
        start = caret
        end = caret
        onChange()
    }

    fun onFocusLost() {
        active = false
        blinkJob?.cancel()
        onChange()
    }

    fun showCaret() {
        editorState.showLine(caret.line)
    }

    fun moveCaret(position: TextPosition): CaretMovement {
        val prevCaret = caret
        caret = position
        return CaretMovement(prevCaret, position)
    }

    fun moveUp(): CaretMovement {
        if (!active) return CaretMovement(caret, caret)

        val newPosition = if (caret.line > 0)
            caret.copy(line = caret.line - 1)
        else
            TextPosition(0, 0)

        return moveCaret(newPosition)
    }

    fun moveDown(): CaretMovement {
        if (!active) return CaretMovement(caret, caret)

        val newPosition = if (caret.line < editorState.lines.size - 1)
            caret.copy(line = caret.line + 1)
        else
            TextPosition(editorState.lines.size - 1, editorState.lines.last().text.length)

        return moveCaret(newPosition)
    }

    fun moveRight(): CaretMovement {
        if (!active) return CaretMovement(caret, caret)

        val newPosition = with(editorState) {
            caret.getNext()
        }

        return moveCaret(newPosition)
    }

    fun moveLeft(): CaretMovement {
        if (!active) return CaretMovement(caret, caret)

        val newPosition = with(editorState) {
            caret.getPrevious()
        }

        return moveCaret(newPosition)
    }

    fun setCaretToSelectionOr(start: Boolean, or: SelectionManager.() -> Unit) {
        if (hasSelection) {
            caret = if (start) firstPosition else secondPosition
            clearSelection()
        } else {
            or()
        }
    }

    private fun Offset.toTextPosition(): TextPosition {
        val y = y + editorState.scrollState.value
        val x = x
        var yPos = 0
        val line = editorState.lines.indexOfFirst {
            val bottomY = yPos + it.lineHeight
            val isTheLine = yPos <= y && bottomY >= y
            yPos = bottomY
            isTheLine
        }.coerceIn(0, editorState.lines.size - 1)
        val lineState = editorState.lines[line]
        val offset = lineState.layoutResult?.getOffsetForPosition(Offset(x, 0f)) ?: 0
        return TextPosition(line, offset)
    }

    fun onDrag(uiOffset: Offset) {
        if (!active) return
        end = uiOffset.toTextPosition()
        caret = end
    }

    fun onClick(uiOffset: Offset) {
        editorState.focusRequester.requestFocus()
        start = uiOffset.toTextPosition()
        end = start
        caret = start
    }

    private var blinkJob: Job? = null
    private fun launchBlinkCursor() {
        blinkJob?.cancel()
        blinkJob = scope.launch {
            showCursor = true
            onChange()
            delay(1000)
            while (active) {
                showCursor = !showCursor
                onChange()
                delay(500)
            }
        }
    }

    fun DrawScope.drawSelection() {
        HighlightAnnotation(start.coerceAtMost(end)..end.coerceAtLeast(start), selectionColor).draw(editorState)
    }

    fun DrawScope.drawLineHighlight() {
        if (!active) return

        val lineTop = editorState.offsetAtLineTop(caret.line).toFloat()
        val lineBottom = lineTop + editorState.lines[caret.line].lineHeight

        val lineBounds = Rect(
            topLeft = Offset(0f, lineTop),
            bottomRight = Offset(size.width, lineBottom)
        )

        translate(top = -editorState.scrollState.value.toFloat()) {
            drawRect(
                AlchitryColors.current.LineHighlight,
                lineBounds.topLeft,
                lineBounds.size,
                style = Fill
            )
        }
    }

    fun DrawScope.drawCaret() {
        if (!active || !showCursor) return

        val offset = with(editorState) { caret.getTopOffset() }
        val line = editorState.lines.getOrNull(caret.line) ?: return
        val layout = line.layoutResult ?: return
        val caretOffset = caret.offset.coerceIn(0, line.text.length)
        val cursorRect = layout.getCursorRect(caretOffset)

        val margin = editorState.lineTopOffset

        translate(top = -editorState.scrollState.value.toFloat()) {
            drawRect(
                cursorColor,
                topLeft = cursorRect.topLeft
                    .copy(y = cursorRect.topLeft.y + offset.y + margin)
                    .round()
                    .toOffset(),
                size = cursorRect.size,
                style = Stroke(width = 2f)
            )
        }
    }
}