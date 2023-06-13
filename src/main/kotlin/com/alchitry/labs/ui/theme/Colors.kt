package com.alchitry.labs.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

object AlchitryColors {
    val accent = Color(0xFFE6B800)
    val darkAccent = Color(0xFF333330)

    val colors = Colors(
        primary = accent,
        primaryVariant = darkAccent,
        secondary = Color(0xFF4B4B41),
        secondaryVariant = Color(0xFF545449),
        background = Color(0xFF333333),
        surface = Color(0xFF282828),
        error = Color(0xFFFF1919),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.Black,
        isLight = true
    )

    val tooltipBackground = Color.White
    val tooltipContent = Color.Black

    val menuBarBackground = Color(0xFF424242)

    val gutterForeground = Color(0xFFA1A1A1)
    val lineHighlight = accent.copy(alpha = 0.05f)
    val tokenHighlight = accent.copy(alpha = 0.1f)

    val selectionColor = Color(0xFF6C5600)


    @Composable
    fun toolbarButtonColors(
        backgroundColor: Color = MaterialTheme.colors.background,
        contentColor: Color = MaterialTheme.colors.onBackground,
        disabledBackgroundColor: Color = MaterialTheme.colors.onBackground.copy(alpha = 0.12f)
            .compositeOver(MaterialTheme.colors.background),
        disabledContentColor: Color = MaterialTheme.colors.onBackground
            .copy(alpha = ContentAlpha.disabled)
    ): ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledContentColor = disabledContentColor
    )
}