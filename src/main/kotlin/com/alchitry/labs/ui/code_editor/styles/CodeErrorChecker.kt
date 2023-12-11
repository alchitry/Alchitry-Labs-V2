package com.alchitry.labs.ui.code_editor.styles

import com.alchitry.labs.parsers.errors.Notation

interface CodeErrorChecker {
    suspend fun checkText(text: String): List<Notation>
}