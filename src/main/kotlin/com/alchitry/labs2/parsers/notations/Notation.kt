package com.alchitry.labs2.parsers.notations

import androidx.compose.ui.text.SpanStyle
import com.alchitry.labs2.ui.alchitry_text_field.TextPosition
import com.alchitry.labs2.ui.theme.AlchitryColors
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token

data class Notation(
    val message: String?,
    val range: ClosedRange<TextPosition>,
    val type: NotationType
) {
    override fun toString(): String {
        return "${type.label} at line ${range.start.line + 1} offset ${range.start.offset}${message?.let { ": $it" } ?: ""}"
    }

    companion object {
        fun from(token: Token, message: String?, type: NotationType, offset: TextPosition?): Notation {
            val line = (offset?.line ?: 0) + token.line - 1
            val lineOffset = if (token.line == 1) {
                (offset?.offset ?: 0) + token.charPositionInLine
            } else {
                token.charPositionInLine
            }
            return Notation(
                message = message,
                range = TextPosition(line, lineOffset)..
                        TextPosition(line, lineOffset + ((token.text?.length) ?: 0)),
                type = type
            )
        }

        fun from(ctx: ParserRuleContext, message: String?, type: NotationType, offset: TextPosition?): Notation? {
            val start = ctx.start ?: return null
            val stop = ctx.stop ?: return null
            val startLine = (offset?.line ?: 0) + start.line - 1
            val startOffset = if (start.line == 1) {
                (offset?.offset ?: 0) + start.charPositionInLine
            } else {
                start.charPositionInLine
            }
            val stopLine = (offset?.line ?: 0) + stop.line - 1
            val stopOffset = if (stop.line == 1) {
                (offset?.offset ?: 0) + stop.charPositionInLine
            } else {
                stop.charPositionInLine
            }
            return Notation(
                message = message,
                range = TextPosition(startLine, startOffset)..
                        TextPosition(stopLine, stopOffset + (stop.text?.length ?: 0)),
                type = type
            )
        }
    }
}

sealed class NotationType(val label: String, val style: SpanStyle) {
    data object Error : NotationType(
        "Error",
        SpanStyle(color = AlchitryColors.current.Error)
    )

    data object SyntaxError : NotationType(
        "Syntax error",
        SpanStyle(AlchitryColors.current.Error)
    )

    data object Warning : NotationType(
        "Warning",
        SpanStyle(AlchitryColors.current.Warning)
    )

    data object SyntaxAmbiguity : NotationType(
        "Syntax ambiguity",
        SpanStyle(AlchitryColors.current.Warning)
    )

    data object Info : NotationType(
        "Info",
        SpanStyle(AlchitryColors.current.Info)
    )

    class Custom(label: String, style: SpanStyle) : NotationType(label, style)

    val priority: Int
        get() = when (this) {
            is Error, is SyntaxError -> 0
            is Warning, is SyntaxAmbiguity -> 1
            is Info -> 2
            is Custom -> 3
        }
}

