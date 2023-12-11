package com.alchitry.labs.ui.code_editor.styles

import androidx.compose.ui.text.AnnotatedString
import com.alchitry.labs.ui.code_editor.CodeEditorState
import com.alchitry.labs.ui.code_editor.LineStyle
import com.alchitry.labs.ui.code_editor.StyleToken
import com.alchitry.labs.ui.code_editor.toStyleToken
import kotlinx.coroutines.runBlocking

class CodeStyler(
    private val editor: CodeEditorState,
    private val codeParser: CodeErrorChecker
) {

    fun updateStyle() {
        val lines = editor.lines
        val styles = MutableList(lines.size) { mutableListOf<LineStyle>() }

        val notations = runBlocking { codeParser.checkText(editor.getText()) }// TODO: do this async?
        editor.notations.clear()
        editor.notations.addAll(notations)

        val styleTokens = mutableListOf<StyleToken>().apply {
            addAll(editor.tokens.map { it.styleToken })
            addAll(notations.map { it.toStyleToken() })
        }

        styleTokens.forEach { token ->
            if (token.style == null)
                return@forEach

            val start = token.range.start.coerceInRange(lines)
            val end = token.range.endInclusive.coerceInRange(lines)

            if (token.isSingleLine) {
                styles[start.line].add(
                    LineStyle(start.offset, end.offset, token.style)
                )
                return@forEach
            }

            styles[start.line].add(
                LineStyle(start.offset, lines[start.line].text.length, token.style)
            )

            // full line length styles
            (start.line + 1..<end.line).forEach { line ->
                styles[line].add(
                    LineStyle(0, lines[line].text.length, token.style)
                )
            }

            styles[end.line].add(
                LineStyle(0, end.offset, token.style)
            )
        }

        val newLines = lines.mapIndexed { lineNum, line ->
            AnnotatedString.Builder().apply {
                append(line.text.text)
                styles[lineNum].forEach {
                    if (it.start != it.end)
                        addStyle(it.style, it.start, it.end)
                }
            }.toAnnotatedString()
        }

        with(editor)
        {
            lines.forEachIndexed { index, lineState ->
                // only update lines that changed (saves time to preventing remeasuring)
                if (lineState.text != newLines[index]) {
                    lines[index] = newLineState(newLines[index])
                }
            }
            invalidate()
        }
    }
}
