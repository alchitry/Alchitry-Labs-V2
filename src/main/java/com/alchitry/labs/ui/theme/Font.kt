package com.alchitry.labs.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val ubuntuFont = FontFamily(
    Font(
        resource = "Ubuntu-Bold.ttf",
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    ),
    Font(
        resource = "Ubuntu-BoldItalic.ttf",
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
    Font(
        resource = "Ubuntu-Regular.ttf",
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resource = "Ubuntu-Italic.ttf",
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resource = "Ubuntu-Light.ttf",
        weight = FontWeight.Light,
        style = FontStyle.Normal
    ),
    Font(
        resource = "Ubuntu-LightItalic.ttf",
        weight = FontWeight.Light,
        style = FontStyle.Italic
    ),
    Font(
        resource = "Ubuntu-Medium.ttf",
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    ),
    Font(
        resource = "Ubuntu-MediumItalic.ttf",
        weight = FontWeight.Medium,
        style = FontStyle.Italic
    )
)

val ubuntuMonoFont = FontFamily(
    Font(
        resource = "UbuntuMono-Bold.ttf",
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    ),
    Font(
        resource = "UbuntuMono-BoldItalic.ttf",
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
    Font(
        resource = "UbuntuMono-Regular.ttf",
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        resource = "UbuntuMono-RegularItalic.ttf",
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
)

object AlchitryTypography {
    val typography = Typography(defaultFontFamily = ubuntuFont)
    val editor = TextStyle(
        fontFamily = ubuntuMonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 28.sp
    )
}