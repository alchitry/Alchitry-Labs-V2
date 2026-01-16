package com.alchitry.labs2.ui.alchitry_text_field.styles

import com.alchitry.labs2.parsers.notations.Notation

interface CodeErrorChecker {
    suspend fun checkText(text: String): List<Notation>
}