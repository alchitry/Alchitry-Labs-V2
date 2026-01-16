package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList

data class EditorSnapshot(
    val lines: ImmutableList<AnnotatedString>,
    val caret: TextPosition,
    val selectionStart: TextPosition,
    val selectionEnd: TextPosition
)