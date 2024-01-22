package com.alchitry.labs2.ui.theme

import androidx.compose.material3.Typography
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

fun Typography.defaultFontFamily(fontFamily: FontFamily): Typography {
    return this.copy(
        displayLarge = this.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = this.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = this.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = this.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = this.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = this.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = this.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = this.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = this.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = this.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = this.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = this.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = this.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = this.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = this.labelSmall.copy(fontFamily = fontFamily)
    )
}

object AlchitryTypography {
    val typography = Typography().defaultFontFamily(ubuntuFont)
    val editor = TextStyle(
        fontFamily = ubuntuMonoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 28.sp
    )
}