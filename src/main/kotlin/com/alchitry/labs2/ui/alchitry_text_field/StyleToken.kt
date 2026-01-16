package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.alchitry.labs2.parsers.notations.Notation
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

/**
 * Returns the [TextPosition] for the given offset inside the [Token].
 * If [offset] is null, it returns the end of the Token.
 */
fun Token.textPositionAtOffset(offset: Int?): TextPosition {
    if (offset == 0) {
        return TextPosition(line - 1, charPositionInLine)
    }
    val text = text
    val subText = text?.substring(0, offset ?: text.length)
    // needed to avoid ambiguity see https://youtrack.jetbrains.com/issue/KT-46360
    val lineCounter = { c: Char -> if (c == '\n') 1 else 0 }
    val lineCount = subText?.sumOf(lineCounter) ?: 0

    val lineOffset = (subText?.lastIndexOf('\n') ?: -1) + 1

    val textLength = subText?.length ?: 0
    val endOffset = if (lineCount == 0) charPositionInLine + textLength else textLength - lineOffset
    return TextPosition(line + lineCount - 1, endOffset)
}

fun Token.toEditorToken(style: SpanStyle?): EditorToken {
    return EditorToken(
        range = textPositionAtOffset(0)..
                textPositionAtOffset(null),
        style = style,
        token = this
    )
}

fun Notation.toStyleToken(): StyleToken {
    val style = SpanStyle(color = type.color, textDecoration = TextDecoration.Underline)
    return StyleToken(
        range = range,
        style = style
    )
}