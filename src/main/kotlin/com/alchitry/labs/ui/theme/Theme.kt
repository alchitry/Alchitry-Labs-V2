package com.alchitry.labs.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun AlchitryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AlchitryColors.current.scheme,
        typography = AlchitryTypography.typography,
        shapes = AlchitryShapes,
        content = content
    )
}