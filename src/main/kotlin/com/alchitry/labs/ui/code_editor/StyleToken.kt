package com.alchitry.labs.ui.code_editor

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.alchitry.labs.parsers.errors.Notation
import com.alchitry.labs.parsers.errors.NotationType
import com.alchitry.labs.ui.theme.AlchitryColors
import org.antlr.v4.kotlinruntime.Token


data class EditorToken(
    val styleToken: StyleToken,
    val token: Token
) {
    constructor(range: ClosedRange<TextPosition>, style: SpanStyle?, token: Token) : this(
        StyleToken(range, style),
        token
    )

    val range = styleToken.range
    val style = styleToken.style
}

data class StyleToken(
    val range: ClosedRange<TextPosition>,
    val style: SpanStyle?
) {
    val isSingleLine: Boolean get() = range.start.line == range.endInclusive.line
}

data class LineStyle(
    val start: Int,
    val end: Int,
    val style: SpanStyle
)

fun Token.toEditorToken(style: SpanStyle?): EditorToken {
    // needed to avoid ambiguity see https://youtrack.jetbrains.com/issue/KT-46360
    val lineCounter = { c: Char -> if (c == '\n') 1 else 0 }
    val lineCount = text?.sumOf(lineCounter) ?: 0

    val lineOffset = (text?.lastIndexOf('\n') ?: -1) + 1

    val textLength = text?.length ?: 0
    val endOffset = if (lineCount == 0) charPositionInLine + textLength else textLength - lineOffset

    return EditorToken(
        range = TextPosition(line - 1, charPositionInLine)..
                TextPosition(line + lineCount - 1, endOffset),
        style = style,
        token = this
    )
}

fun Notation.toStyleToken(): StyleToken {
    val color = when (type) {
        NotationType.Error, NotationType.SyntaxError -> AlchitryColors.current.Error
        NotationType.Warning, NotationType.SyntaxAmbiguity -> AlchitryColors.current.Warning
        NotationType.Info -> AlchitryColors.current.Info
    }
    val style = SpanStyle(color = color, textDecoration = TextDecoration.Underline)
    return StyleToken(
        range = range,
        style = style
    )
}