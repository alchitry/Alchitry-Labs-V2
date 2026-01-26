@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.foundation.text.TextDelegate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class AlchitryLineState(
    val text: AnnotatedString,
    density: Density?,
    fontFamilyResolver: FontFamily.Resolver?,
    val style: TextStyle?
) {
    var layoutResult: TextLayoutResult? = null
    private var lastConstraints: Constraints? = null
    val lineHeight: Int? get() = layoutResult?.size?.height

    private val delegate by lazy {
        if (style != null && density != null && fontFamilyResolver != null) {
            TextDelegate(
                text = text,
                style = style,
                density = density,
                fontFamilyResolver = fontFamilyResolver
            )
        } else {
            null
        }
    }

    fun layout(constraints: Constraints): TextLayoutResult? {
        if (lastConstraints == constraints)
            layoutResult?.let { return it }
        lastConstraints = constraints

        return delegate?.layout(constraints, LayoutDirection.Ltr, layoutResult).also { layoutResult = it }
    }
}