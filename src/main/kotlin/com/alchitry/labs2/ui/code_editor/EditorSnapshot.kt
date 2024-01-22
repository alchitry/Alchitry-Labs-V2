package com.alchitry.labs2.ui.code_editor

import androidx.compose.ui.text.AnnotatedString
import kotlinx.collections.immutable.ImmutableList

data class EditorSnapshot(
    val lines: ImmutableList<AnnotatedString>,
    val caret: TextPosition,
    val selectionStart: TextPosition,
    val selectionEnd: TextPosition
)