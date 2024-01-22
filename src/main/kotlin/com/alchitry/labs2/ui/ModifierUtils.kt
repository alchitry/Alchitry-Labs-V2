package com.alchitry.labs2.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

@Composable
fun Modifier.hiddenClickable(onClick: () -> Unit) =
    this.clickable(remember { MutableInteractionSource() }, indication = null, onClick = onClick)

/**
 * Makes the composable take-up at least it's max intrinsic size. If more space is available, it fills it.
 */
fun Modifier.fillMaxIntrinsic() = this.then(KeepSizeAndFillModifier)

private object KeepSizeAndFillModifier : LayoutModifier {
    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ) = measurable.maxIntrinsicWidth(height)

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val width = measurable.maxIntrinsicWidth(constraints.maxHeight).coerceAtLeast(constraints.maxWidth)
        val contentConstraints = constraints.copy(minWidth = width, maxWidth = width)

        val placeable = measurable.measure(
            contentConstraints
        )
        // by using the constraint's max width as our width we don't auto center our placeable
        return layout(constraints.maxWidth, placeable.height) {
            placeable.place(IntOffset.Zero)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ) = measurable.minIntrinsicHeight(width)

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ) = measurable.maxIntrinsicWidth(height)

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ) = measurable.maxIntrinsicHeight(width)
}