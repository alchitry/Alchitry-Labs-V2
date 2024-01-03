package com.alchitry.labs.ui.code_editor.styles.lucid

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

object LucidStyle {
    val signed = SpanStyle(Color(10, 191, 191))
    val keyword = SpanStyle(Color(10, 191, 191), fontWeight = FontWeight.Bold)
    val defaultKeyword = keyword.copy(fontWeight = null)
    val variable = SpanStyle(Color(10, 141, 191))
    val module = SpanStyle(Color(250, 172, 31), fontWeight = FontWeight.Bold)
    val operator = SpanStyle(Color(237, 67, 67))
    val comment = SpanStyle(Color(150, 150, 150))
    val string = SpanStyle(Color(191, 191, 10))
    val value = SpanStyle(Color(162, 105, 220))
    val realValue = SpanStyle(Color(178, 98, 203))
    val constant = SpanStyle(Color(212, 90, 218))
    val namespace = SpanStyle(Color(218, 90, 140))
    val function = SpanStyle(Color(27, 221, 175))
}