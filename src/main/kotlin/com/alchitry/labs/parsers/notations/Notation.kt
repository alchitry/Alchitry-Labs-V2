package com.alchitry.labs.parsers.notations

import androidx.compose.ui.graphics.Color
import com.alchitry.labs.ui.code_editor.TextPosition
import com.alchitry.labs.ui.theme.AlchitryColors
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
        fun from(token: Token, message: String?, type: NotationType) = Notation(
            message = message,
            range = TextPosition(token.line - 1, token.charPositionInLine)..
                    TextPosition(token.line - 1, token.charPositionInLine + ((token.text?.length) ?: 0)),
            type = type
        )

        fun from(ctx: ParserRuleContext, message: String?, type: NotationType): Notation? {
            val start = ctx.start ?: return null
            val stop = ctx.stop ?: return null
            return Notation(
                message = message,
                range = TextPosition(start.line - 1, start.charPositionInLine)..
                        TextPosition(stop.line - 1, stop.charPositionInLine + (stop.text?.length ?: 0)),
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