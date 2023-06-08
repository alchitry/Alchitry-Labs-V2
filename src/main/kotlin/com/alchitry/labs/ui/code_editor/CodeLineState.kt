package com.alchitry.labs.ui.code_editor

import androidx.compose.foundation.text.InternalFoundationTextApi
import androidx.compose.foundation.text.TextDelegate
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
    val density: Density,
    fontFamilyResolver: FontFamily.Resolver,
    style: TextStyle
) {
    var layoutResult: TextLayoutResult? = null
    private var lastConstraints: Constraints? = null
    var lineHeight = 0
    val topMargin
        get() = ((lineHeight - (layoutResult?.size?.height ?: 0) * 1.15f) / 2f)

    private val delegate by lazy {
        TextDelegate(
            text = text,
            style = style,
            density = density,
            fontFamilyResolver = fontFamilyResolver
        )
    }

    fun layout(constraints: Constraints): TextLayoutResult {
        if (lastConstraints == constraints)
            layoutResult?.let { return it }
        lastConstraints = constraints

        return delegate.layout(constraints, LayoutDirection.Ltr, layoutResult).also { layoutResult = it }
    }
}