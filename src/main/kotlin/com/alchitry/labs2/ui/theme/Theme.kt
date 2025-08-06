package com.alchitry.labs2.ui.theme

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Settings


@Composable
fun AlchitryTheme(content: @Composable () -> Unit) {
    val density = LocalDensity.current
    val scrollbarStyle = remember(AlchitryColors.current) {
        ScrollbarStyle(
            minimalHeight = 16.dp,
            thickness = 8.dp,
            shape = RoundedCornerShape(4.dp),
            hoverDurationMillis = 300,
            unhoverColor = AlchitryColors.current.scheme.onSurface.copy(alpha = 0.1f),
            hoverColor = AlchitryColors.current.scheme.onSurface.copy(alpha = 0.3f)
        )
    }
    CompositionLocalProvider(
        LocalDensity provides Density(
            density.density * Settings.uiScale,
            density.fontScale
        ),
        LocalScrollbarStyle provides scrollbarStyle
    ) {
        MaterialTheme(
            colorScheme = AlchitryColors.current.scheme,
            typography = AlchitryTypography.typography,
            shapes = AlchitryShapes,
            content = content
        )
    }
}