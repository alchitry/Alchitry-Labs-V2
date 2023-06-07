package com.alchitry.labs.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AlchitryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AlchitryColors.colors,
        typography = AlchitryTypography.typography,
        shapes = AlchitryShapes,
        content = content
    )
}