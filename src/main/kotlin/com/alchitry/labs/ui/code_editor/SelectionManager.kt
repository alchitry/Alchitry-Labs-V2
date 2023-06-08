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
import kotlin.math.round

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

    fun selectCurrentLine() {
        val line = caret.line.coerceIn(0, editorState.lines.size)
        val nextLine = (line + 1).coerceAtMost(editorState.lines.size)
        start = TextPosition(line, 0)
        end = if (nextLine == line)
            TextPosition(line, editorState.lines[line].text.length)
        else
            TextPosition(nextLine, 0)
        onChange()
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

    private fun TextPosition.getTopOffset(): Offset {
        val lineState = editorState.lines.getOrNull(line)
        val layout = lineState?.layoutResult
        val xOffset = round(
            layout?.getHorizontalPosition(
                offset.coerceIn(0, lineState.text.length),
                usePrimaryDirection = true
            ) ?: 0f
        )
        val yOffset = editorState.offsetAtLineTop(line).toFloat()
        return Offset(xOffset, yOffset)
    }

    private fun TextPosition.getBottomOffset(): Offset {
        return getTopOffset().let {
            it.copy(
                y = it.y + (editorState.lineHeight(line)).toFloat()
            )
        }
    }


    fun DrawScope.drawSelection() {
        if (start == end) return

        translate(top = -editorState.scrollState.value.toFloat()) {

            val firstPos = firstPosition
            val secondPos = secondPosition

            // first line (might be partial)
            val start = firstPos.getTopOffset()
            val end = if (secondPos.line == firstPos.line) {
                secondPos.getBottomOffset()
            } else {
                Offset(size.width, start.y + (editorState.lineHeight(firstPos.line)).toFloat())
            }
            val firstRect = Rect(topLeft = start, bottomRight = end)

            drawRect(
                selectionColor,
                topLeft = firstRect.topLeft,
                size = firstRect.size
            )

            // main bulk rect between partial selections
            if (secondPos.line - firstPos.line > 1) {
                val top = Offset(0f, editorState.offsetAtLineTop(firstPos.line + 1).toFloat())
                val bottom = Offset(size.width, editorState.offsetAtLineBottom(secondPos.line - 1).toFloat())
                val bulkRect = Rect(topLeft = top, bottomRight = bottom)
                drawRect(
                    selectionColor,
                    topLeft = bulkRect.topLeft,
                    size = bulkRect.size
                )
            }

            // final partial selection line
            if (secondPos.line != firstPos.line) {
                val top = Offset(0f, editorState.offsetAtLineTop(secondPos.line).toFloat())
                val bottom = secondPos.getBottomOffset()
                val finalRect = Rect(topLeft = top, bottomRight = bottom)
                drawRect(
                    selectionColor,
                    topLeft = finalRect.topLeft,
                    size = finalRect.size
                )
            }
        }
    }

    fun DrawScope.drawLineHighlight() {
        if (!active) return

        val lineBounds = Rect(
            topLeft = Offset(0f, editorState.offsetAtLineTop(caret.line).toFloat()),
            bottomRight = Offset(size.width, editorState.offsetAtLineBottom(caret.line).toFloat())
        )

        translate(top = -editorState.scrollState.value.toFloat()) {
            drawRect(
                AlchitryColors.lineHighlight,
                lineBounds.topLeft,
                lineBounds.size,
                style = Fill
            )
        }
    }

    fun DrawScope.drawCaret() {
        if (!active || !showCursor) return

        val offset = caret.getTopOffset()
        val line = editorState.lines.getOrNull(caret.line) ?: return
        val layout = line.layoutResult ?: return
        val caretOffset = caret.offset.coerceIn(0, line.text.length)
        val cursorRect = layout.getCursorRect(caretOffset)

        val margin = line.topMargin

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