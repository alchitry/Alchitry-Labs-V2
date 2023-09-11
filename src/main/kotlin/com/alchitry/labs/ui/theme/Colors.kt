package com.alchitry.labs.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

object AlchitryColors {
    val Accent = Color(0xFFfaac1f)
    val DarkAccent = Color(0xFF333330)

    val colors = Colors(
        primary = Accent,
        primaryVariant = DarkAccent,
        secondary = Color(0xFF4B4B41),
        secondaryVariant = Color(0xFF545449),
        background = Color(0xFF333333),
        surface = Color(0xFF282828),
        error = Color(0xFFCC1414),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.Black,
        isLight = true
    )

    val ProgressBar = Color(0xFFE19A1A)

    val TooltipBackground = Color(0xFF454545)
    val TooltipContent = Color.White

    val MenuBarBackground = Color(0xFF424242)

    val GutterForeground = Color(0xFFA1A1A1)
    val LineHighlight = Accent.copy(alpha = 0.05f)
    val TokenHighlight = Accent.copy(alpha = 0.1f)

    val SelectionColor = Color(0xFF6C5600)

    val Success = Color(25, 200, 25)
    val Warning = Color(200, 200, 25)
    val Error = colors.error
    val Info = Color(41, 162, 204)


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