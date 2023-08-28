package com.alchitry.labs.parsers.errors

import com.alchitry.labs.ui.code_editor.TextPosition

data class Notation(
    val message: String?,
    val range: ClosedRange<TextPosition>,
    val type: NotationType
) {
    override fun toString(): String {
        return "${type.label} at line ${range.start.line} offset ${range.start.offset}${message?.let { ": $it" } ?: ""}"
    }
}

enum class NotationType(val label: String) {
    Error("Error"),
    Warning("Warning"),
    Info("Info"),
    SyntaxError("Syntax error"),
    SyntaxAmbiguity("Syntax ambiguity")
}