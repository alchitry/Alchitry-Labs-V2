@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.alchitry.labs.ui.code_editor

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScope
import androidx.compose.foundation.text.KeyCommand
import androidx.compose.foundation.text.isTypedEvent
import androidx.compose.foundation.text.platformDefaultKeyMapping
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.alchitry.labs.ui.gestures.detectEditorActions
import com.alchitry.labs.ui.theme.AlchitryColors
import com.alchitry.labs.ui.theme.AlchitryTypography
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log10
import kotlin.properties.Delegates

@Composable
fun rememberCodeEditorState(tokenizer: EditorTokenizer): CodeEditorState {
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val scope = rememberCoroutineScope()
    val textColor = LocalTextStyle.current.color.takeOrElse {
        LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    }
    val style = AlchitryTypography.editor.copy(color = textColor)
    return remember(tokenizer) {
        CodeEditorState(
            tokenizer,
            style,
            density,
            fontFamilyResolver,
            scope,
            textColor.copy(alpha = 0.7f),
            AlchitryColors.selectionColor
        )
    }
}

private val newLineRegex = Regex("(\\r\\n?)|\\n")

class CodeEditorState(
    private val tokenizer: EditorTokenizer,
    private val style: TextStyle,
    private val density: Density,
    private val fontFamilyResolver: FontFamily.Resolver,
    val scope: CoroutineScope,
    cursorColor: Color,
    selectionColor: Color
) {
    val focusRequester = FocusRequester()
    val lines = ArrayList<CodeLineState>()
    private var gutterDigits = 0
    val scrollState = ScrollState(0)
    var gutterWidth = 0
        private set
    var softWrap by Delegates.observable(false) { _, _, _ -> onChange() }
    private var invalidator: (() -> Unit)? = null
    private var size: IntSize = IntSize.Zero
    private var scrollTarget: Int? = null

    val selectionManager = SelectionManager(
        this,
        cursorColor,
        selectionColor,
        scope,
        ::onChange
    )
    private val undoManager = UndoManager(this, selectionManager)

    private val styler = CodeStyler(this, tokenizer)

    var clipboardManager: ClipboardManager? = null

    @Composable
    internal fun subscribe(scope: RecomposeScope) {
        DisposableEffect(scope) {
            invalidator = {
                scope.invalidate()
            }
            onDispose {
                invalidator = null
            }
        }
    }

    fun getText(): String {
        return lines.joinToString("\n")
    }

    internal fun onChange() {
        invalidator?.invoke()
    }

    fun getSnapshot() =
        EditorSnapshot(lines.toImmutableList(), selectionManager.caret, selectionManager.start, selectionManager.end)

    internal fun newLineState(text: AnnotatedString) = CodeLineState(text, density, fontFamilyResolver, style)

    /**
     * Replaces the text covered by range with newText.
     *
     * @param newText the new text to insert
     * @param range the range of text to replace
     *
     * @return the position of the end of the new text
     */
    fun replaceText(
        newText: String, range: OpenEndRange<TextPosition> = selectionManager.selectedRange,
        updateCaret: Boolean = true
    ) {
        val start = range.start.coerceInRange()
        val end = range.endExclusive.coerceInRange()

        val startLine = lines[start.line]
        val endLine = lines[end.line]

        val prefix = startLine.text.substring(0, start.offset)
        val suffix = endLine.text.substring(end.offset, endLine.text.length)

        // remove extra lines
        if (end.line != start.line)
            lines.subList(start.line + 1, end.line + 1).clear()

        val newTextLines = newText.split(newLineRegex)

        val newLines = newTextLines.mapIndexed { index, s ->
            val newLine = when (index) {
                0 -> if (newTextLines.size == 1) prefix + s + suffix else prefix + s
                newTextLines.size - 1 -> s + suffix
                else -> s
            }

            newLineState(AnnotatedString(newLine))
        }

        lines[start.line] = newLines.first()

        if (newLines.size > 1)
            lines.addAll(start.line + 1, newLines.subList(1, newLines.size))

        styler.updateStyle()

        onChange()

        undoManager.queueSnapshot()

        if (updateCaret) {
            val prefixOffset = if (newTextLines.size == 1) prefix.length else 0
            val endPosition = TextPosition(start.line + newLines.size - 1, newTextLines.last().length + prefixOffset)

            selectionManager.caret = endPosition
            selectionManager.clearSelection()
        }
    }

    fun setText(text: String) {
        val lines = text.split(newLineRegex)
        this.lines.clear()
        lines.forEach { line ->
            this.lines.add(
                newLineState(AnnotatedString(line))
            )
        }
        styler.updateStyle()
        undoManager.reset()
        onChange()
    }

    fun lineHeight(line: Int): Int {
        return lines.getOrNull(line)?.lineHeight ?: 0
    }

    fun offsetAtLineTop(line: Int): Int =
        (0 until line).sumOf { lineHeight(it) }

    fun offsetAtLineBottom(line: Int): Int =
        offsetAtLineTop(line + 1)

    fun showLine(line: Int) {
        scrollTarget = line
    }

    private fun startScrollToLine(line: Int) {
        val top = offsetAtLineTop(line)
        val bottom = top + lineHeight(line)

        val viewTop = scrollState.value
        val viewBottom = viewTop + size.height

        val destination = when {
            top < viewTop -> top
            bottom > viewBottom -> bottom - size.height
            else -> return
        }

        scope.launch {
            scrollState.animateScrollTo(destination)
        }
    }


    fun DrawScope.draw() {
        with(selectionManager) {
            drawSelection()
        }

        drawIntoCanvas { canvas ->
            canvas.save()
            var currentY = -scrollState.value
            canvas.translate(dx = 0f, dy = currentY.toFloat())
            lines.forEach { line ->
                line.layoutResult?.let { layout ->
                    val margin = line.topMargin
                    canvas.translate(dx = 0f, dy = margin)
                    val nextY = currentY + line.lineHeight
                    if (nextY > 0 || currentY < size.height) { // if visible
                        TextPainter.paint(canvas, layout)
                    }
                    currentY = nextY
                    canvas.translate(dx = 0f, dy = line.lineHeight.toFloat() - margin)
                }
            }
            canvas.restore()
        }

        with(selectionManager) {
            drawCaret()
        }
    }

    private fun Int.length() = when (this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }


    fun LazyLayoutMeasureScope.layout(constraints: Constraints) {
        size = IntSize(constraints.maxWidth, constraints.maxHeight)

        val maxDigits = lines.size.length()
        if (gutterDigits != maxDigits) {
            val placeables = measure(-maxDigits, Constraints())
            gutterWidth = placeables.maxOf { it.width }
            gutterDigits = maxDigits
        }

        val lineConstraints =
            if (softWrap)
                Constraints.fixedWidth((constraints.maxWidth - gutterWidth).coerceAtLeast(0))
            else
                Constraints()

        lines.forEach {
            it.layout(lineConstraints)
                .also { l -> it.lineHeight = l.size.height }
        }

        val totalHeight = lines.sumOf { it.lineHeight }

        @Suppress("INVISIBLE_SETTER")
        scrollState.maxValue = (totalHeight - constraints.maxHeight).coerceAtLeast(0)

        scrollTarget?.let { startScrollToLine(it) }
        scrollTarget = null
    }

    fun tapModifier() = Modifier
        .pointerInput(selectionManager) {
            detectEditorActions(
                onClick = { offset -> selectionManager.onClick(offset) },
                onDoubleClick = { offset -> println("Double click: $offset") },
                onDrag = { change ->
                    selectionManager.onDrag(change.position)
                }
            )
            detectTapGestures()
        }


    fun keyModifier() = Modifier
        .onKeyEvent {
            if (selectionManager.active) {
                return@onKeyEvent processKey(it)
            }
            return@onKeyEvent false
        }
        // must be before other focus modifiers
        .onFocusChanged {
            if (!it.hasFocus) {
                selectionManager.onFocusLost()
            }
        }
        .focusRequester(focusRequester)
        .focusable()

    /**
     * Returns a TextPosition that is the closest valid position for the
     * current text.
     */
    fun TextPosition.coerceInRange() = coerceInRange(lines = lines)

    fun TextPosition.getPrevious(): TextPosition {
        val currentOffset = offset.coerceIn(0, lines[line].text.length)
        if (currentOffset > 0) return copy(offset = currentOffset - 1)
        if (line == 0) return this
        return TextPosition(line - 1, lines[line - 1].text.length)
    }

    fun TextPosition.getNext(): TextPosition {
        val thisLine = lines[line]
        if (offset < thisLine.text.length) return copy(offset = offset + 1)
        if (line == lines.size - 1) return this
        return TextPosition(line + 1, 0)
    }

    fun copy() {
        if (!selectionManager.hasSelection) {
            selectionManager.selectCurrentLine()
        }
        clipboardManager?.setText(selectionManager.getSelectedText())
    }

    fun paste() {
        clipboardManager?.getText()?.let {
            replaceText(it.text)
        }
    }

    fun cut() {
        copy()
        deleteIfSelectedOr { }
    }

    private fun processKey(event: KeyEvent): Boolean {
        if (event.isTypedEvent) {
            val text = StringBuilder().appendCodePoint(event.utf16CodePoint).toString()
            replaceText(text)
            return true
        }

        if (event.type != KeyEventType.KeyDown) {
            return false
        }

        // compose ignores the number-pad enter key so check for it here
        val command: KeyCommand = platformDefaultKeyMapping.map(event)
            ?: if (event.key.nativeKeyCode == java.awt.event.KeyEvent.VK_ENTER) KeyCommand.NEW_LINE else return false

        var consumed = true
        //commandExecutionContext {
        when (command) {
            KeyCommand.COPY -> copy()
            KeyCommand.PASTE -> paste()
            KeyCommand.CUT -> cut()
            KeyCommand.LEFT_CHAR ->
                selectionManager.setCaretToSelectionOr(true) { moveLeft() }

            KeyCommand.RIGHT_CHAR ->
                selectionManager.setCaretToSelectionOr(false) { moveRight() }
//            KeyCommand.LEFT_WORD -> moveCursorLeftByWord()
//            KeyCommand.RIGHT_WORD -> moveCursorRightByWord()
//            KeyCommand.PREV_PARAGRAPH -> moveCursorPrevByParagraph()
//            KeyCommand.NEXT_PARAGRAPH -> moveCursorNextByParagraph()
            KeyCommand.UP ->
                selectionManager.setCaretToSelectionOr(true) { moveUp() }

            KeyCommand.DOWN ->
                selectionManager.setCaretToSelectionOr(false) { moveDown() }
//            KeyCommand.PAGE_UP -> moveCursorUpByPage()
//            KeyCommand.PAGE_DOWN -> moveCursorDownByPage()
//            KeyCommand.LINE_START -> moveCursorToLineStart()
//            KeyCommand.LINE_END -> moveCursorToLineEnd()
//            KeyCommand.LINE_LEFT -> moveCursorToLineLeftSide()
//            KeyCommand.LINE_RIGHT -> moveCursorToLineRightSide()
            KeyCommand.LINE_START -> {
                selectionManager.clearSelection()
                selectionManager.caret =
                    selectionManager.caret.copy(offset = 0) // TODO maybe make this jump to end of indent
            }

            KeyCommand.LINE_END -> {
                selectionManager.clearSelection()
                selectionManager.caret =
                    selectionManager.caret.copy(
                        offset = lines.getOrNull(selectionManager.caret.line)?.text?.length ?: 0
                    )
            }

            KeyCommand.DELETE_PREV_CHAR -> deleteIfSelectedOr {
                replaceText("", selectionManager.caret.getPrevious()..<selectionManager.caret)
            }

            KeyCommand.DELETE_NEXT_CHAR -> deleteIfSelectedOr {
                replaceText("", selectionManager.caret..<selectionManager.caret.getNext())
            }

//            KeyCommand.DELETE_PREV_WORD ->
//                deleteIfSelectedOr {
//                    getPreviousWordOffset()?.let {
//                        DeleteSurroundingTextCommand(selection.end - it, 0)
//                    }
//                }?.apply()
//
//            KeyCommand.DELETE_NEXT_WORD ->
//                deleteIfSelectedOr {
//                    getNextWordOffset()?.let {
//                        DeleteSurroundingTextCommand(0, it - selection.end)
//                    }
//                }?.apply()
//
            KeyCommand.DELETE_FROM_LINE_START -> deleteIfSelectedOr {
                val startOfLine = selectionManager.caret.copy(offset = 0)
                replaceText("", startOfLine..<selectionManager.caret)
            }

            KeyCommand.DELETE_TO_LINE_END -> deleteIfSelectedOr {
                val endOfLine = selectionManager.caret.copy(
                    offset = lines.getOrNull(selectionManager.caret.line)?.text?.length ?: 0
                )
                replaceText("", selectionManager.caret..<endOfLine)
            }

            KeyCommand.NEW_LINE -> replaceText("\n")
            KeyCommand.TAB -> replaceText("    ")
            KeyCommand.SELECT_ALL -> selectionManager.selectAll()
            KeyCommand.SELECT_LEFT_CHAR -> selectionManager.moveLeft().selectMovement()
            KeyCommand.SELECT_RIGHT_CHAR -> selectionManager.moveRight().selectMovement()
//            KeyCommand.SELECT_LEFT_WORD -> moveCursorLeftByWord().selectMovement()
//            KeyCommand.SELECT_RIGHT_WORD -> moveCursorRightByWord().selectMovement()
//            KeyCommand.SELECT_PREV_PARAGRAPH -> moveCursorPrevByParagraph().selectMovement()
//            KeyCommand.SELECT_NEXT_PARAGRAPH -> moveCursorNextByParagraph().selectMovement()
//            KeyCommand.SELECT_LINE_START -> moveCursorToLineStart().selectMovement()
//            KeyCommand.SELECT_LINE_END -> moveCursorToLineEnd().selectMovement()
//            KeyCommand.SELECT_LINE_LEFT -> moveCursorToLineLeftSide().selectMovement()
//            KeyCommand.SELECT_LINE_RIGHT -> moveCursorToLineRightSide().selectMovement()
//            KeyCommand.SELECT_UP -> moveCursorUpByLine().selectMovement()
//            KeyCommand.SELECT_DOWN -> moveCursorDownByLine().selectMovement()
//            KeyCommand.SELECT_PAGE_UP -> moveCursorUpByPage().selectMovement()
//            KeyCommand.SELECT_PAGE_DOWN -> moveCursorDownByPage().selectMovement()
//            KeyCommand.SELECT_HOME -> moveCursorToHome().selectMovement()
//            KeyCommand.SELECT_END -> moveCursorToEnd().selectMovement()
//            KeyCommand.DESELECT -> deselect()
            KeyCommand.UNDO -> undoManager.undo()
            KeyCommand.REDO -> undoManager.redo()
//
//            KeyCommand.CHARACTER_PALETTE -> {
//                showCharacterPalette()
//            }
            //}
            else -> {
                consumed = false
                println("Command $command not implemented yet!")
            }
        }
        //undoManager?.forceNextSnapshot()
        return consumed
    }

    private fun deleteIfSelectedOr(block: () -> Unit) {
        if (selectionManager.hasSelection) {
            //undoManager.takeSnapshot()
            replaceText("")
        } else {
            block()
        }
    }

}