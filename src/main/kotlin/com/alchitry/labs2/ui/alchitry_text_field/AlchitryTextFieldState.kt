@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScope
import androidx.compose.foundation.text.TextDelegate
import androidx.compose.foundation.text.platformDefaultKeyMapping
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.*
import com.alchitry.labs2.Log
import com.alchitry.labs2.Settings
import com.alchitry.labs2.leadingWhitespace
import com.alchitry.labs2.noNulls
import com.alchitry.labs2.parsers.findFinalNode
import com.alchitry.labs2.parsers.grammar.BracketLexer
import com.alchitry.labs2.parsers.grammar.BracketParser
import com.alchitry.labs2.parsers.grammar.LucidLexer
import com.alchitry.labs2.parsers.notations.Notation
import com.alchitry.labs2.parsers.notations.NotationType
import com.alchitry.labs2.ui.alchitry_text_field.autocomplete.Autocomplete
import com.alchitry.labs2.ui.alchitry_text_field.autocomplete.AutocompleteSource
import com.alchitry.labs2.ui.alchitry_text_field.styles.CodeFormatter
import com.alchitry.labs2.ui.alchitry_text_field.styles.CodeStyler
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextProvider
import com.alchitry.labs2.ui.alchitry_text_field.styles.TextTokenizer
import com.alchitry.labs2.ui.alchitry_text_field.tooltip.NotationTooltipProvider
import com.alchitry.labs2.ui.gestures.detectEditorActions
import com.alchitry.labs2.ui.isAwtTypedEvent
import com.alchitry.labs2.ui.theme.AlchitryColors
import com.alchitry.labs2.ui.theme.AlchitryTypography
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.tree.TerminalNode
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.properties.Delegates

@Composable
private fun rememberAlchitryTextFieldStyle(): AlchitryTextFieldStyle {
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val textColor = LocalTextStyle.current.color.takeOrElse {
        LocalContentColor.current
    }
    val selectionColor = AlchitryColors.current.SelectionColor
    return remember(density, textColor, fontFamilyResolver, selectionColor) {
        AlchitryTextFieldStyle(
            AlchitryTypography.editor.copy(
                color = textColor,
                lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)
            ), density, fontFamilyResolver, textColor.copy(alpha = 0.7f), selectionColor
        )
    }
}

private val newLineRegex = Regex("(\\r\\n?)|\\n")

data class AlchitryTextFieldStyle(
    val style: TextStyle,
    val density: Density,
    val fontFamilyResolver: FontFamily.Resolver,
    val cursorColor: Color,
    val selectionColor: Color
)

class AlchitryTextFieldState(
    val codeFormatter: CodeFormatter,
    val autocomplete: Autocomplete?,
    val tokenizer: TextTokenizer,
    val onTextChanged: () -> Unit = {},
    val onDoubleClicK: (Offset) -> Boolean = { false },
    val onReplaceText: (String, OpenEndRange<TextPosition>) -> Boolean = { _, _ -> false }
) : TextProvider, AutocompleteSource {

    var style: AlchitryTextFieldStyle? = null
        set(value) {
            if (field != value) {
                field = value
                refreshStyling()
            }
        }
    var uiScope: CoroutineScope? = null
    override val scope = CoroutineScope(Dispatchers.Main)

    var lineTopOffset: Float = 0f
    val tooltipState = NotationTooltipProvider(this)
    var notations: List<Notation>? = null
    val focusRequester = FocusRequester()
    val lines = ArrayList<AlchitryLineState>()
    private var gutterDigits = 0
    val verticalScrollState = ScrollState(0)
    val horizontalScrollState = ScrollState(0)
    var gutterWidth = 0
        private set
    var softWrap by Delegates.observable(false) { _, _, _ -> invalidate() }
    private var invalidator: (() -> Unit)? = null
    val redrawTriggerStates = mutableStateOf(false, neverEqualPolicy())
    private var layoutConstraints: Constraints = Constraints()
    var size: IntSize = IntSize.Zero
        private set
    var longestLine: Int = 0
        private set
    var scrollTarget: TextPosition? = null

    var tokens: List<EditorToken> = emptyList()
        private set

    private var lineOffsetCache: List<Int>? = null

    override val selectionManager = SelectionManager(
        this, ::invalidate
    )

    private val undoManager = UndoManager(this, selectionManager)

    val styler = CodeStyler(this)

    var clipboardManager: ClipboardManager? = null

    override fun getLine(line: Int): String? = lines.getOrNull(line)?.text?.text
    override fun getLineCount(): Int = lines.size

    fun getWindow() = Rect(
        horizontalScrollState.value.toFloat(),
        verticalScrollState.value.toFloat(),
        (horizontalScrollState.value + size.width).toFloat(),
        (verticalScrollState.value + size.height).toFloat()
    )

    fun onCaretChanged() {
        updateHighlightTokens()
        //autocomplete?.reset()
    }

    private fun updateHighlightTokens() {
        if (lines.isEmpty()) return
        lines.forEach { it.highlights.clear() }
        val token = textPositionToToken(selectionManager.caret) ?: return

        val tokens = when (token.token.type) {
            LucidLexer.Tokens.TYPE_ID, LucidLexer.Tokens.CONST_ID, LucidLexer.Tokens.SPACE_ID, LucidLexer.Tokens.FUNCTION_ID -> {
                tokens.mapNotNull { t ->
                    if (t.token.type == token.token.type && t.token.text == token.token.text) {
                        HighlightAnnotation(t.range, AlchitryColors.current.TokenHighlight)
                    } else {
                        null
                    }
                }
            }

            else -> emptyList()
        }

        tokens.forEach {
            if (it.range.start.line != it.range.endInclusive.line) {
                TODO("Multiline token highlighting not supported!")
            }
            lines[it.range.start.line].highlights.add(it)
        }

        val line = lines.getOrNull(selectionManager.caret.line)?.text?.text ?: return
        val leftChar = line.getOrNull(selectionManager.caret.offset - 1)
        val rightChar = line.getOrNull(selectionManager.caret.offset)
        if (brackets.contains(leftChar) || brackets.contains(rightChar)) {
            val caretOffset = when {
                closingBrackets.contains(leftChar) -> selectionManager.caret.offset - 1
                closingBrackets.contains(rightChar) -> selectionManager.caret.offset
                openingBrackets.contains(rightChar) -> selectionManager.caret.offset + 1
                else -> selectionManager.caret.offset
            }

            val stream =
                CommonTokenStream(BracketLexer(CharStreams.fromString(getText())).apply { removeErrorListeners() })
            val bracketParser = BracketParser(stream).apply { removeErrorListeners() }.source()
            val offset = lines.subList(0, selectionManager.caret.line).sumOf { it.text.length + 1 } + caretOffset
            var node = bracketParser.findFinalNode(stream, offset)
            if (node is TerminalNode) node = node.getParent()!!
            (node.getChild(0) as? TerminalNode)?.symbol?.let {
                lines[it.line - 1].highlights.add(
                    HighlightAnnotation(
                        it.toEditorToken(null).range, AlchitryColors.current.TokenHighlight
                    )
                )
            }
            (node.getChild(node.childCount - 1) as? TerminalNode)?.symbol?.let {
                lines[it.line - 1].highlights.add(
                    HighlightAnnotation(
                        it.toEditorToken(null).range, AlchitryColors.current.TokenHighlight
                    )
                )
            }
        }
    }

    /**
     * Converts a given [TextPosition] within the text content to a corresponding screen space [Offset].
     * This method calculates the exact screen position of the text character at the specified
     * line and offset of the [TextPosition], considering layout constraints, scrolling offsets,
     * and other editor-specific parameters.
     *
     * @param position the [TextPosition] indicating the line and offset within the text for which the screen offset is to be calculated
     * @return the screen space [Offset] corresponding to the provided [TextPosition], or null if the position is invalid or cannot be determined
     */
    override fun textPositionToScreenOffset(position: TextPosition): Offset? {
        val line = lines.getOrNull(position.line) ?: return null
        if (line.layoutResult == null) line.layout(Constraints())
        val layout = line.layoutResult ?: return null
        val rect = layout.getCursorRect(position.offset.coerceIn(0, line.text.length - 1))
        return Offset(
            rect.left - horizontalScrollState.value,
            offsetAtLineBottom(position.line).toFloat() - verticalScrollState.value
        )
    }

    private fun screenToTextOffset(offset: Offset) =
        Offset(offset.x + horizontalScrollState.value, offset.y + verticalScrollState.value)

    fun screenOffsetToTextPosition(offset: Offset): TextPosition {
        val textOffset = screenToTextOffset(offset)
        if (lines.isEmpty()) return TextPosition(0, 0)
        var lineBottom = 0
        val lineNumber = lines.indexOfFirst {
            lineBottom += it.lineHeight ?: 0
            lineBottom > textOffset.y
        }.let { if (it < 0) return TextPosition(lines.size - 1, lines.lastOrNull()?.text?.length ?: 0) else it }

        val line = lines[lineNumber]
        val charNum =
            line.layoutResult?.getOffsetForPosition(Offset(textOffset.x, textOffset.y - offsetAtLineTop(lineNumber)))
                ?: return TextPosition(0, 0)
        return TextPosition(lineNumber, charNum)
    }

    fun textPositionToToken(textPosition: TextPosition): EditorToken? {
        return tokens.firstOrNull { it.range.contains(textPosition) }
    }

    /**
     * Converts a screen space offset to the nearest token.
     */
    fun offsetToToken(offset: Offset, excludeRight: Boolean = false): EditorToken? {
        if (lines.isEmpty()) return null
        val textPosition = screenOffsetToTextPosition(offset)
        if (excludeRight) {
            val line = lines[textPosition.line]
            if (line.text.isEmpty()) return null
            if (line.text.length == textPosition.offset) {
                if ((line.layoutResult?.getBoundingBox((textPosition.offset - 1).coerceAtLeast(0))?.right
                        ?: 0f) < offset.x
                ) {
                    return null
                }
            }
        }
        return textPositionToToken(textPosition)
    }

    @Composable
    internal fun subscribe() {
        val style = rememberAlchitryTextFieldStyle()
        this.style = style
        this.uiScope = rememberCoroutineScope()

        lineTopOffset = remember(style) {
            TextDelegate(
                text = AnnotatedString("123"),
                style = style.style,
                density = style.density,
                fontFamilyResolver = style.fontFamilyResolver
            ).layout(Constraints(), LayoutDirection.Ltr, null).size.height * -0.07f
        }

        LaunchedEffect(verticalScrollState.value, horizontalScrollState.value) {
            tooltipState.hide()
            autocomplete?.reset(scope)
        }

        DisposableEffect(Unit) {
            invalidator = {
                redrawTriggerStates.value = true
            }
            onDispose {
                invalidator = null
            }
        }
    }

    override fun getText(): String {
        return lines.joinToString("\n") { it.text.text }
    }

    fun onTextChange() {
        onTextChanged()

        lineOffsetCache = null
        tokens = tokenizer.getTokens(lines.toCharStream())
        styler.updateStyle()
        updateHighlightTokens()
        invalidate()
    }

    internal fun invalidate() {
        invalidator?.invoke()
    }

    fun getSnapshot() = EditorSnapshot(
        lines.map { it.text }.toImmutableList(), selectionManager.caret, selectionManager.start, selectionManager.end
    )

    private fun TextPosition.charAt(): Char? {
        return lines.getOrNull(line)?.text?.getOrNull(offset)
    }

    internal fun newLineState(text: AnnotatedString) =
        AlchitryLineState(text, style?.density, mutableListOf(), style?.fontFamilyResolver, style?.style).also {
            it.layout(layoutConstraints)
        }

    override fun replaceText(
        newText: String,
        range: OpenEndRange<TextPosition>
    ) {
        replaceText(newText, range, true)
    }

    /**
     * Replaces the text covered by range with newText.
     *
     * @param newText the new text to insert
     * @param range the range of text to replace
     *
     * @return the position of the end of the new text
     */
    fun replaceText(
        newText: String, range: OpenEndRange<TextPosition> = selectionManager.selectedRange, updateCaret: Boolean = true
    ) {
        if (onReplaceText(newText, range)) {
            return
        }

        val start = range.start.coerceInRange()
        val end = range.endExclusive.coerceInRange()

        val startLine = lines[start.line]
        val endLine = lines[end.line]

        val prefix = startLine.text.substring(0, start.offset)
        val suffix = endLine.text.substring(end.offset, endLine.text.length)

        // remove extra lines
        if (end.line != start.line) lines.subList(start.line + 1, end.line + 1).clear()

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

        if (newLines.size > 1) lines.addAll(start.line + 1, newLines.subList(1, newLines.size))

        onTextChange()

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
        undoManager.reset()
        onTextChange()
    }

    private fun refreshStyling() {
        val lines = this.lines.toImmutableList()
        this.lines.clear()
        lines.forEach {
            this.lines.add(
                newLineState(it.text)
            )
        }
        onTextChange()
    }

    fun lineHeight(line: Int): Int? {
        return lines.getOrNull(line)?.lineHeight
    }

    fun offsetAtLineTop(line: Int): Int {
        if (line == 0) return 0
        var cache = lineOffsetCache
        if (cache == null || !cache.indices.contains(line)) {
            val lineHeights = lines.map { it.lineHeight }
            val validLineHeights =
                lineHeights.noNulls() ?: return lineHeights.subList(0, line).reduce { acc, i -> (acc ?: 0) + (i ?: 0) }
                    ?: 0
            cache = validLineHeights.runningReduce { acc, l -> acc + l }
            lineOffsetCache = cache
        }
        return cache.getOrElse(line - 1) { cache.lastOrNull() ?: 0 }
    }

    fun offsetAtLineBottom(line: Int): Int = offsetAtLineTop(line + 1)

    fun showOffset(offset: TextPosition) {
        scrollTarget = offset
    }

    private fun startScrollToPosition(textPosition: TextPosition) {
        val line = lines.getOrNull(textPosition.line) ?: return
        val layout = line.layoutResult ?: return

        val left = layout.getHorizontalPosition(textPosition.offset.coerceIn(0, line.text.length), true).roundToInt()
        val right =
            layout.getHorizontalPosition((textPosition.offset + 1).coerceIn(0, line.text.length), true).roundToInt()
        val top = offsetAtLineTop(textPosition.line)
        val bottom = top + (lineHeight(textPosition.line) ?: 0)

        val viewTop = verticalScrollState.value
        val viewBottom = viewTop + size.height
        val viewLeft = horizontalScrollState.value
        val viewRight = viewLeft + (size.width - gutterWidth)

        val verticalDestination = when {
            top < viewTop -> top
            bottom > viewBottom -> bottom - size.height
            else -> null
        }

        val horizontalDestination = when {
            left < viewLeft -> left
            right > viewRight -> right - (size.width - gutterWidth)
            else -> null
        }

        if (verticalDestination != null) {
            uiScope?.launch {
                verticalScrollState.animateScrollTo(verticalDestination)
            }
        }
        if (horizontalDestination != null) {
            uiScope?.launch {
                horizontalScrollState.animateScrollTo(horizontalDestination)
            }
        }
    }

    fun isLineVisible(line: Int): Boolean {
        if (!lines.indices.contains(line)) return false
        val top = offsetAtLineTop(line)
        val bottom = top + (lines[line].lineHeight ?: 0)
        return bottom > verticalScrollState.value || top < verticalScrollState.value + size.height
    }

    fun DrawScope.draw() {
        redrawTriggerStates.value // triggers redraws on demand
        with(selectionManager) {
            drawSelection()
        }
        drawIntoCanvas { canvas ->
            canvas.save()
            var currentY = -verticalScrollState.value
            val currentX = -horizontalScrollState.value
            canvas.translate(dx = currentX.toFloat(), dy = currentY.toFloat())
            var wasVisible = false
            for (line in lines) {
                val layout = line.layoutResult ?: continue
                val margin = lineTopOffset
                val nextY = currentY + (line.lineHeight ?: 0)
                val visible = nextY > 0 && currentY < size.height
                if (visible) {
                    wasVisible = true
                    line.drawHighlights()
                    canvas.translate(dx = 0f, dy = margin)
                    TextPainter.paint(canvas, layout)
                    canvas.translate(dx = 0f, dy = (line.lineHeight ?: 0).toFloat() - margin)
                } else {
                    canvas.translate(dx = 0f, dy = (line.lineHeight ?: 0).toFloat())
                    if (wasVisible) break
                }
                currentY = nextY
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

    private fun defaultLineHeight(): Int =
        newLineState(AnnotatedString("0")).apply { layout(Constraints()) }.lineHeight ?: 0


    fun LazyLayoutMeasureScope.layout(constraints: Constraints) {
        size = IntSize(constraints.maxWidth, constraints.maxHeight)

        val lineHeight = lines.firstOrNull()?.lineHeight.let {
            if (it == null || it == 0) defaultLineHeight() else it
        }

        val maxDigits = lines.size.length()
        val placeables = measure(-maxDigits - 1, Constraints(maxHeight = lineHeight))
        gutterWidth = placeables.maxOf { it.width }
        gutterDigits = maxDigits

        val editorHeight = (constraints.maxHeight - 20.dp.roundToPx()).coerceAtLeast(0)
        val editorWidth = (constraints.maxWidth - gutterWidth).coerceAtLeast(0)

        val lineConstraints = if (softWrap) Constraints.fixedWidth(editorWidth)
        else Constraints()

        layoutConstraints = lineConstraints

        lines.forEach {
            it.layout(lineConstraints)
        }

        val totalHeight = lines.sumOf { it.lineHeight ?: 0 }
        longestLine = (lines.maxOfOrNull { it.layoutResult?.size?.width ?: 0 } ?: 0) + 20.dp.roundToPx()

        @Suppress("INVISIBLE_SETTER")
        verticalScrollState.maxValue = (totalHeight - editorHeight).coerceAtLeast(0)
        @Suppress("INVISIBLE_SETTER")
        verticalScrollState.viewportSize = editorHeight
        @Suppress("INVISIBLE_SETTER")
        horizontalScrollState.maxValue = (longestLine - editorWidth).coerceAtLeast(0)
        @Suppress("INVISIBLE_SETTER")
        horizontalScrollState.viewportSize = editorWidth

        scrollTarget?.let { startScrollToPosition(it) }
        scrollTarget = null

    }

    fun selectWordAt(offset: TextPosition) {
        val text = lines[offset.line].text.text
        val lineOffset = offset.offset
        // Check if the offset is valid
        if (lineOffset < 0 || lineOffset >= text.length) return

        val isWordChar: (Char) -> Boolean = { it.isLetterOrDigit() || it == '_' }

        // Find the start of the word
        var start = lineOffset
        while (start > 0 && isWordChar(text[start - 1])) {
            start--
        }

        // Find the end of the word
        var end = lineOffset
        while (end < text.length && isWordChar(text[end])) {
            end++
        }
        selectionManager.selectRange(TextPosition(offset.line, start)..TextPosition(offset.line, end))
    }

    fun tapModifier() = Modifier.pointerInput(selectionManager) {
        detectEditorActions(
            onClick = { offset, modifiers ->
                selectionManager.onClick(offset, modifiers)
            },
            onDoubleClick = { offset ->
                if (onDoubleClicK(offset)) return@detectEditorActions
                val token = offsetToToken(offset) ?: return@detectEditorActions
                selectionManager.selectRange(token.range)
            },
            onTripleClick = { offset ->
                val position = screenOffsetToTextPosition(offset)
                selectionManager.selectLine(position.line)
            }, onDrag = { change ->
                selectionManager.onDrag(change.position)
            })
    }

    fun lineNotationLevel(line: Int): NotationType? {
        return notations?.filter { (it.range.start.line..it.range.endInclusive.line).contains(line) }
            ?.minByOrNull { it.type.ordinal }?.type
    }

    fun keyModifier() = Modifier.onKeyEvent {
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
        }.focusRequester(focusRequester).focusable()

    /**
     * Returns a TextPosition that is the closest valid position for the
     * current text.
     */
    private fun TextPosition.coerceInRange() = coerceInRange(lines = lines)

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

    fun adjustIndents() {
        if (lines.isNotEmpty()) replaceText( // use replaceText so this ends up on the undo/redo stack
            codeFormatter.formatAll(this),
            TextPosition(0, 0)..<TextPosition(lines.size - 1, lines.last().text.length),
            false
        )
    }

    private fun processKey(event: KeyEvent): Boolean {
        // Special key combinations
        if (event.type == KeyEventType.KeyDown) {
            val key = event.key
            val ctrl = event.isCtrlPressed
            val alt = event.isAltPressed
            val shift = event.isShiftPressed
            val meta = event.isMetaPressed

            when {
                ctrl && alt && !shift && !meta && key == Key.L -> { // Ctrl + Alt + L
                    adjustIndents()
                    return true
                }

                !ctrl && !alt && shift && !meta && key == Key.Tab -> { // Shift + Tab
                    (selectionManager.firstPosition.line..selectionManager.secondPosition.line).forEach { lineIndex ->
                        val lineText = lines.getOrNull(lineIndex)?.text?.text ?: return@forEach
                        val startOfLine = TextPosition(lineIndex, 0)
                        val whitespace = if (lineText.startsWith(CodeFormatter.INDENT_STRING)) {
                            CodeFormatter.INDENT_STRING.length
                        } else {
                            lineText.length - lineText.trimStart().length
                        }

                        replaceText(
                            "", startOfLine..<TextPosition(lineIndex, whitespace), false
                        )

                        if (selectionManager.start.line == lineIndex) {
                            selectionManager.start = selectionManager.start.copy(
                                offset = (selectionManager.start.offset - whitespace).coerceAtLeast(
                                    0
                                )
                            )
                        }
                        if (selectionManager.end.line == lineIndex) {
                            selectionManager.end = selectionManager.end.copy(
                                offset = (selectionManager.end.offset - whitespace).coerceAtLeast(
                                    0
                                )
                            )
                        }
                        if (selectionManager.caret.line == lineIndex) {
                            selectionManager.caret = selectionManager.caret.copy(
                                offset = (selectionManager.caret.offset - whitespace).coerceAtLeast(
                                    0
                                )
                            )
                        }
                    }
                    return true
                }
            }
        }

        if (event.isAwtTypedEvent) {
            val text = StringBuilder().appendCodePoint(event.utf16CodePoint).toString()

            val lineNum = selectionManager.caret.line
            val line = lines.getOrNull(lineNum)?.text?.text

            if (!selectionManager.hasSelection && (text == ")" || text == "}" || text == "]")) {
                val nextChar = selectionManager.caret.charAt()
                if (text[0] == nextChar) {
                    selectionManager.caret = selectionManager.caret.getNext()
                    return true
                }
            }

            val modifiedText = when (text) {
                "{" -> "{}"
                "(" -> "()"
                "[" -> "[]"
                else -> text
            }

            replaceText(modifiedText)
            if ((line?.isBlank() == true && modifiedText.isNotBlank()) || (text == ":" && line?.length == selectionManager.caret.offset - 1)) {
                val current = lines[lineNum].text.text
                replaceText(
                    codeFormatter.getIndentFor(this, lineNum) + current.trim(),
                    TextPosition(lineNum, 0)..<TextPosition(lineNum, current.length)
                )
            }
            repeat(modifiedText.length - text.length) {
                selectionManager.moveLeft()
            }

            autocomplete?.updateSuggestions(this)

            return true
        }

        if (event.type != KeyEventType.KeyDown) {
            return false
        }

        if (event.key == Key.Escape) {
            autocomplete?.reset(scope)
        }

        val commandName = platformDefaultKeyMapping.map(event)?.name

        // compose ignores the number-pad enter key so check for it here
        val command: KeyCommand = commandName?.let { KeyCommand.valueOf(it) } ?: when (event.key.nativeKeyCode) {
            java.awt.event.KeyEvent.VK_ENTER -> KeyCommand.NEW_LINE
            java.awt.event.KeyEvent.VK_HOME if event.isCtrlPressed -> KeyCommand.HOME
            java.awt.event.KeyEvent.VK_END if event.isCtrlPressed -> KeyCommand.END
            java.awt.event.KeyEvent.VK_BACK_SPACE if event.isShiftPressed -> KeyCommand.DELETE_PREV_CHAR
            else -> return false
        }
        var consumed = true
        var resetAutocomplete = true
        when (command) {
            KeyCommand.COPY -> copy()
            KeyCommand.PASTE -> paste()
            KeyCommand.CUT -> cut()
            KeyCommand.LEFT_CHAR -> selectionManager.setCaretToSelectionOr(true) { moveLeft() }
            KeyCommand.RIGHT_CHAR -> selectionManager.setCaretToSelectionOr(false) { moveRight() }
            KeyCommand.LEFT_WORD -> {
                selectionManager.clearSelection()
                selectionManager.moveLeftToken()
            }

            KeyCommand.RIGHT_WORD -> {
                selectionManager.clearSelection()
                selectionManager.moveRightToken()
            }

            KeyCommand.PREV_PARAGRAPH -> uiScope?.launch {
                verticalScrollState.animateScrollTo(
                    verticalScrollState.value - (lines[selectionManager.caret.line].lineHeight ?: 0)
                )
            }

            KeyCommand.NEXT_PARAGRAPH -> uiScope?.launch {
                verticalScrollState.animateScrollTo(
                    verticalScrollState.value + (lines[selectionManager.caret.line].lineHeight ?: 0)
                )
            }

            KeyCommand.UP -> {
                if (autocomplete?.selectionUp() != true) selectionManager.setCaretToSelectionOr(true) { moveUp() }
                resetAutocomplete = false
            }

            KeyCommand.DOWN -> {
                if (autocomplete?.selectionDown() != true) selectionManager.setCaretToSelectionOr(false) { moveDown() }
                resetAutocomplete = false
            }

            KeyCommand.PAGE_UP -> {
                selectionManager.clearSelection()
                selectionManager.movePageUp()
            }

            KeyCommand.PAGE_DOWN -> {
                selectionManager.clearSelection()
                selectionManager.movePageDown()
            }

            KeyCommand.HOME -> {
                selectionManager.clearSelection()
                selectionManager.moveToStart()
            }

            KeyCommand.END -> {
                selectionManager.clearSelection()
                selectionManager.moveToEnd()
            }

            KeyCommand.LINE_START, KeyCommand.LINE_LEFT -> {
                selectionManager.clearSelection()
                selectionManager.moveToStartOfLine()
            }

            KeyCommand.LINE_END, KeyCommand.LINE_RIGHT -> {
                selectionManager.clearSelection()
                selectionManager.moveToEndOfLine()
            }

            KeyCommand.DELETE_PREV_CHAR -> deleteIfSelectedOr {
                var endPosition = selectionManager.caret
                val charToRemove =
                    lines[selectionManager.caret.line].text.text.getOrNull(selectionManager.caret.offset - 1)
                if (openingBrackets.contains(charToRemove)) {
                    val nextChar = lines[selectionManager.caret.line].text.text.getOrNull(selectionManager.caret.offset)
                    if (nextChar == closingBrackets[openingBrackets.indexOf(charToRemove)]) {
                        endPosition = selectionManager.caret.getNext()
                    }
                }
                replaceText("", selectionManager.caret.getPrevious()..<endPosition)
                if (autocomplete?.active == true) autocomplete.updateSuggestions(this)
                resetAutocomplete = false
            }

            KeyCommand.DELETE_NEXT_CHAR -> deleteIfSelectedOr {
                replaceText("", selectionManager.caret..<selectionManager.caret.getNext())
                if (autocomplete?.active == true) autocomplete.updateSuggestions(this)
                resetAutocomplete = false
            }

            KeyCommand.DELETE_PREV_WORD -> deleteIfSelectedOr {
                val movement = selectionManager.moveLeftToken()
                replaceText("", movement.new..<movement.old)
                if (autocomplete?.active == true) autocomplete.updateSuggestions(this)
                resetAutocomplete = false
            }

            KeyCommand.DELETE_NEXT_WORD -> deleteIfSelectedOr {
                val movement = selectionManager.moveRightToken()
                replaceText("", movement.old..<movement.new)
                if (autocomplete?.active == true) autocomplete.updateSuggestions(this)
                resetAutocomplete = false
            }

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

            KeyCommand.NEW_LINE -> {
                resetAutocomplete = false

                autocomplete?.select(scope)?.let {
                    replaceText(it.nextText, it.range)
                    return true
                }

                when (Settings.newLineIndent) {
                    "none" -> replaceText("\n")
                    "current" -> {
                        val lineBeforeBreak = lines.getOrNull(selectionManager.caret.line)?.text?.text
                        replaceText("\n", updateCaret = false)
                        val lineNum = selectionManager.caret.line + 1
                        val current = lines[lineNum].text.text
                        replaceText(
                            (lineBeforeBreak?.takeWhile { it.isWhitespace() } ?: "") + current.trimStart(),
                            TextPosition(lineNum, 0)..<TextPosition(lineNum, current.length),
                            updateCaret = false
                        )
                        selectionManager.moveDown()
                    }

                    else -> { // auto
                        val nextChar = selectionManager.caret.charAt()
                        val prevChar = selectionManager.caret.getPrevious().charAt()
                        if ((nextChar == '}' && prevChar == '{') ||
                            (nextChar == ')' && prevChar == '(') ||
                            (nextChar == ']' && prevChar == '[')
                        ) {
                            replaceText("\n\n")
                            replaceText(codeFormatter.getIndentFor(this, selectionManager.caret.line))
                            selectionManager.moveUp()
                            replaceText(codeFormatter.getIndentFor(this, selectionManager.caret.line))
                        } else {
                            val inLeadingWhitespace =
                                lines[selectionManager.caret.line].text.text.leadingWhitespace() >= selectionManager.caret.offset
                            replaceText("\n", updateCaret = !inLeadingWhitespace)
                            val lineNum = selectionManager.caret.line + if (inLeadingWhitespace) 1 else 0
                            val current = lines[lineNum].text.text

                            val indent = codeFormatter.getIndentFor(this, lineNum)

                            replaceText(
                                indent + current.trimStart(),
                                TextPosition(lineNum, 0)..<TextPosition(lineNum, current.length),
                                updateCaret = false
                            )
                            if (inLeadingWhitespace) {
                                selectionManager.moveDown()
                            } else {
                                selectionManager.caret = selectionManager.caret.copy(offset = indent.length)
                            }
                        }
                    }
                }


            }

            KeyCommand.TAB -> {
                when {
                    selectionManager.start.line != selectionManager.end.line -> {
                        (selectionManager.firstPosition.line..selectionManager.secondPosition.line).forEach { lineIndex ->
                            val startOfLine = TextPosition(lineIndex, 0)
                            replaceText(CodeFormatter.INDENT_STRING, startOfLine..<startOfLine, false)
                        }
                        val offset = CodeFormatter.INDENT_STRING.length
                        selectionManager.start =
                            selectionManager.start.copy(offset = selectionManager.start.offset + offset)
                        selectionManager.end = selectionManager.end.copy(offset = selectionManager.end.offset + offset)
                        selectionManager.caret =
                            selectionManager.caret.copy(offset = selectionManager.caret.offset + offset)
                    }

                    lines.getOrNull(selectionManager.caret.line)?.text?.isBlank() == true -> {
                        replaceText(codeFormatter.getIndentFor(this, selectionManager.caret.line))
                    }

                    else -> {
                        replaceText(CodeFormatter.INDENT_STRING)
                    }
                }
            }

            KeyCommand.SELECT_ALL -> selectionManager.selectAll()
            KeyCommand.SELECT_LEFT_CHAR -> selectionManager.moveLeft().selectMovement()
            KeyCommand.SELECT_RIGHT_CHAR -> selectionManager.moveRight().selectMovement()
            KeyCommand.SELECT_LEFT_WORD -> selectionManager.moveLeftToken().selectMovement()
            KeyCommand.SELECT_RIGHT_WORD -> selectionManager.moveRightToken().selectMovement()
            KeyCommand.SELECT_PREV_PARAGRAPH -> {
                val currentFirstLine = selectionManager.firstPosition.line
                if (currentFirstLine > 0) {
                    val currentLastLine = selectionManager.secondPosition.line
                    val previousLine = currentFirstLine - 1
                    val currentLinesText =
                        (currentFirstLine..currentLastLine).joinToString("\n") { lines[it].text.text }
                    val previousLineText = lines[previousLine].text.text
                    replaceText(
                        "$currentLinesText\n$previousLineText", TextPosition(previousLine, 0)..<TextPosition(
                            currentLastLine, lines[currentLastLine].text.length
                        ), updateCaret = false
                    )
                    selectionManager.moveUp()
                    selectionManager.start = selectionManager.start.copy(line = selectionManager.start.line - 1)
                    selectionManager.end = selectionManager.end.copy(line = selectionManager.end.line - 1)
                }
            }

            KeyCommand.SELECT_NEXT_PARAGRAPH -> {
                val currentLastLine = selectionManager.secondPosition.line
                if (currentLastLine < lines.size - 1) {
                    val currentFirstLine = selectionManager.firstPosition.line
                    val nextLine = currentLastLine + 1
                    val currentLinesText =
                        (currentFirstLine..currentLastLine).joinToString("\n") { lines[it].text.text }
                    val nextLineText = lines[nextLine].text.text
                    replaceText(
                        "$nextLineText\n$currentLinesText", TextPosition(currentFirstLine, 0)..<TextPosition(
                            nextLine, nextLineText.length
                        ), updateCaret = false
                    )
                    selectionManager.moveDown()
                    selectionManager.start = selectionManager.start.copy(line = selectionManager.start.line + 1)
                    selectionManager.end = selectionManager.end.copy(line = selectionManager.end.line + 1)
                }
            }

            KeyCommand.SELECT_LINE_START, KeyCommand.SELECT_LINE_LEFT -> selectionManager.moveToStartOfLine()
                .selectMovement()

            KeyCommand.SELECT_LINE_END, KeyCommand.SELECT_LINE_RIGHT -> selectionManager.moveToEndOfLine()
                .selectMovement()

            KeyCommand.SELECT_UP -> selectionManager.moveUp().selectMovement()
            KeyCommand.SELECT_DOWN -> selectionManager.moveDown().selectMovement()
            KeyCommand.SELECT_PAGE_UP -> selectionManager.movePageUp().selectMovement()
            KeyCommand.SELECT_PAGE_DOWN -> selectionManager.movePageDown().selectMovement()
            KeyCommand.SELECT_HOME -> selectionManager.moveToStart().selectMovement()
            KeyCommand.SELECT_END -> selectionManager.moveToEnd().selectMovement()
            KeyCommand.DESELECT -> selectionManager.clearSelection()
            KeyCommand.UNDO -> undoManager.undo()
            KeyCommand.REDO -> undoManager.redo()

            else -> {
                consumed = false
                Log.warn("Command $command not implemented yet!")
            }
        }
        if (resetAutocomplete) autocomplete?.reset(scope)
        return consumed
    }

    private fun deleteIfSelectedOr(block: () -> Unit) {
        if (selectionManager.hasSelection) {
            replaceText("")
        } else {
            block()
        }
    }

    companion object {
        private val closingBrackets = listOf(')', ']', '}')
        private val openingBrackets = listOf('(', '[', '{')
        private val brackets = closingBrackets + openingBrackets
    }
}