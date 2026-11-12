package com.alchitry.labs2.ui.alchitry_text_field

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection

class AlchitryLineState(
    val text: AnnotatedString,
    private val textMeasurer: TextMeasurer?,
    val style: TextStyle?
) {
    var layoutResult: TextLayoutResult? = null
    private var lastConstraints: Constraints? = null
    val lineHeight: Int? get() = layoutResult?.size?.height

    fun layout(constraints: Constraints): TextLayoutResult? {
        if (lastConstraints == constraints)
            layoutResult?.let { return it }
        lastConstraints = constraints

        val textMeasurer = textMeasurer ?: return null
        val style = style ?: return null
        return textMeasurer.measure(
            text = text,
            style = style,
            constraints = constraints,
            layoutDirection = LayoutDirection.Ltr
        ).also { layoutResult = it }
    }
}