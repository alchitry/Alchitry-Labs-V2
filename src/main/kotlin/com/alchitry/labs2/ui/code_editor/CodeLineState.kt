package com.alchitry.labs2.ui.code_editor

import androidx.compose.foundation.text.InternalFoundationTextApi
import androidx.compose.foundation.text.TextDelegate
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@OptIn(InternalFoundationTextApi::class)
class CodeLineState(
    val text: AnnotatedString,
    density: Density?,
    val highlights: MutableList<HighlightAnnotation>,
    fontFamilyResolver: FontFamily.Resolver?,
    style: TextStyle?
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

    context(DrawScope)
    fun drawHighlights() {
        highlights.forEach { it.draw(this) }
    }
}