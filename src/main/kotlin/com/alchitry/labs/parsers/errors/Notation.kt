package com.alchitry.labs.parsers.errors

import androidx.compose.ui.graphics.Color
import com.alchitry.labs.ui.code_editor.TextPosition
import com.alchitry.labs.ui.theme.AlchitryColors

data class Notation(
    val message: String?,
    val range: ClosedRange<TextPosition>,
    val type: NotationType
) {
    override fun toString(): String {
        return "${type.label} at line ${range.start.line + 1} offset ${range.start.offset}${message?.let { ": $it" } ?: ""}"
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