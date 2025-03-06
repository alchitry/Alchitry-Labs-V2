package com.alchitry.labs2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.alchitry.labs2.Settings


@Composable
fun AlchitryTheme(content: @Composable () -> Unit) {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(
            density.density * Settings.uiScale,
            density.fontScale
        )
    ) {
        MaterialTheme(
            colorScheme = AlchitryColors.current.scheme,
            typography = AlchitryTypography.typography,
            shapes = AlchitryShapes,
            content = content
        )
    }
}