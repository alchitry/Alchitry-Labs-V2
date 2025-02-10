package com.alchitry.labs2.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.alchitry.labs2.Settings

sealed interface AlchitryColors {
    val scheme: ColorScheme

    val Accent: Color
    val DarkAccent: Color
    val ProgressBar: Color
    val TooltipBackground: Color
    val TooltipContent: Color
    val MenuBarBackground: Color
    val GutterForeground: Color
    val LineHighlight: Color
    val TokenHighlight: Color
    val SelectionColor: Color
    val Success: Color
    val Warning: Color
    val Error: Color
    val Info: Color

    val dangerButton: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            DarkAlchitryColors.scheme.error,
            DarkAlchitryColors.scheme.onError
        )
    val cancelButton: ButtonColors
        @Composable get() = ButtonDefaults.buttonColors(
            DarkAlchitryColors.scheme.secondary,
            DarkAlchitryColors.scheme.onSecondary
        )

    companion object {
        val current: AlchitryColors get() = if (Settings.darkTheme) DarkAlchitryColors else LightAlchitryColors


    }
}

data object DarkAlchitryColors : AlchitryColors {
    override val Accent = Color(0xFFB98416)
    override val DarkAccent = Color(0xFF333330)

    override val ProgressBar = Color(0xFFE19A1A)

    override val TooltipBackground = Color(0xFF454545)
    override val TooltipContent = Color.White

    override val MenuBarBackground = Color(0xFF424242)

    override val GutterForeground = Color(0xFFA1A1A1)
    override val LineHighlight = Accent.copy(alpha = 0.05f)
    override val TokenHighlight = Accent.copy(alpha = 0.2f)

    override val SelectionColor = Color(0xFF6C5600)

    override val Success = Color(0xFF1BC91B)
    override val Warning = Color(0xFFCBCB18)
    override val Error = Color(0xFFCE1111)
    override val Info = Color(0xFF29A2CC)

    override val scheme = darkColorScheme(
        primary = Accent,
        onPrimary = Color.White,
        secondary = Color(0xFF4B4B41),
        onSecondary = Color.White,
        background = Color(0xFF424242),
        onBackground = Color.White,
        surface = Color(0xFF282828),
        onSurface = Color.White,
        surfaceTint = Color.Black,
        surfaceVariant = Color(0xFF333333),
        onSurfaceVariant = Color(0xFFA1A1A1),
        error = Error,
        onError = Color.White,
        primaryContainer = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFFFFF)
    )
}

data object LightAlchitryColors : AlchitryColors {
    override val Accent = Color(0xFFfaac1f)
    override val DarkAccent = Color(0xFF333330)

    override val ProgressBar = Color(0xFFE19A1A)

    override val TooltipBackground = Color(0xFF454545)
    override val TooltipContent = Color.White

    override val MenuBarBackground = Color(0xFF424242)

    override val GutterForeground = Color(0xFFA1A1A1)
    override val LineHighlight = Accent.copy(alpha = 0.1f)
    override val TokenHighlight = Accent.copy(alpha = 0.2f)

    override val SelectionColor = Accent.copy(alpha = 0.5f)

    override val Success = Color(0xFF1BC91B)
    override val Warning = Color(0xFFCBCB18)
    override val Error = Color(0xFFCE1111)
    override val Info = Color(0xFF29A2CC)

    override val scheme = lightColorScheme(
        primary = Accent,
        secondary = Color(0xFF4B4B41),
        onSecondary = Color.White,
        error = Error,
        onError = Color.White,
        background = Color(0xFFCCCCCC),
        onBackground = Color.Black,
        surface = Color.White,
        onSurface = Color.Black,
        surfaceTint = Color(0xFF333333),
        //surfaceVariant = Color(0xFF333333),
        //onSurfaceVariant = Color(0xFFA1A1A1),
    )
}

@Composable
fun toolbarButtonColors(
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    disabledContainerColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
        .compositeOver(MaterialTheme.colorScheme.background),
    disabledContentColor: Color = MaterialTheme.colorScheme.onBackground
        .copy(alpha = 0.38f)
): ButtonColors = ButtonDefaults.buttonColors(
    containerColor = containerColor,
    contentColor = contentColor,
    disabledContainerColor = disabledContainerColor,
    disabledContentColor = disabledContentColor
)