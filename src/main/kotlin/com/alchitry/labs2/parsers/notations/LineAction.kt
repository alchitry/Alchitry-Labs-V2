package com.alchitry.labs2.parsers.notations

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

data class LineAction(
    val line: Int,
    val content: @Composable BoxScope.() -> Unit
)