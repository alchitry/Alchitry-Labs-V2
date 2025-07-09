package com.alchitry.labs2.parsers.notations

import androidx.compose.ui.graphics.Color
import com.alchitry.labs2.ui.code_editor.TextPosition
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

enum class NotationType(val label: String) {
    Error("Error"),
    SyntaxError("Syntax error"),
    Warning("Warning"),
    SyntaxAmbiguity("Syntax ambiguity"),
    Info("Info");

    val color: Color
        get() = when (this) {
            Error, SyntaxError -> AlchitryColors.current.Error
            Warning, SyntaxAmbiguity -> AlchitryColors.current.Warning
            Info -> AlchitryColors.current.Info
        }
}