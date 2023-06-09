package com.alchitry.labs.ui.code_editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.alchitry.labs.parsers.lucidv2.Notation
import com.alchitry.labs.parsers.lucidv2.NotationType
import org.antlr.v4.runtime.Token

data class StyleToken(
    val start: TextPosition,
    val end: TextPosition,
    val style: SpanStyle?
) {
    val isSingleLine: Boolean get() = start.line == end.line
}

data class LineStyle(
    val start: Int,
    val end: Int,
    val style: SpanStyle
)

fun Token.toStyleToken(style: SpanStyle?): StyleToken {
    // needed to avoid ambiguity see https://youtrack.jetbrains.com/issue/KT-46360
    val lineCounter = { c: Char -> if (c == '\n') 1 else 0 }
    val lineCount = text.sumOf(lineCounter)

    val lineOffset = text.lastIndexOf('\n') + 1

    return StyleToken(
        start = TextPosition(line - 1, charPositionInLine),
        end = TextPosition(line + lineCount - 1, charPositionInLine + text.length - lineOffset),
        style = style
    )
}

fun Notation.toStyleToken(): StyleToken {
    val color = when (type) {
        NotationType.Error, NotationType.SyntaxError -> Color.Red
        NotationType.Warning, NotationType.SyntaxAmbiguity -> Color.Yellow
        NotationType.Info -> Color.Blue
    }
    val style = SpanStyle(color = color, textDecoration = TextDecoration.Underline)
    return StyleToken(
        start = range.start,
        end = range.endInclusive,
        style = style
    )
}