package com.alchitry.labs.ui.code_editor

import kotlinx.collections.immutable.ImmutableList

data class EditorSnapshot(
    val lines: ImmutableList<CodeLineState>,
    val caret: TextPosition,
    val selectionStart: TextPosition,
    val selectionEnd: TextPosition
)