package com.alchitry.labs.ui.code_editor

import androidx.compose.ui.text.SpanStyle
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
