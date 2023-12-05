package com.alchitry.labs.ui.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/**
 * Left-Center-Right layout. Fills max width if width is bound. Center composable is centered or placed off-center
 * if the left composable doesn't provide enough space.
 *
 * @param left the composable to align on the left
 * @param center the composable to align in the center
 * @param right the composable to align on the right
 */
@Composable
fun LCR(
    modifier: Modifier = Modifier,
    left: @Composable () -> Unit = {},
    center: @Composable () -> Unit = {},
    right: @Composable () -> Unit = {}
) {
    Layout(
        modifier = modifier,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) { left() }
            Row(verticalAlignment = Alignment.CenterVertically) { center() }
            Row(verticalAlignment = Alignment.CenterVertically) { right() }
        }
    ) { measurables, constraints ->
        var remainingWidth = constraints.maxWidth
        val leftPlaceable = measurables[0].measure(
            constraints.copy(
                maxWidth = remainingWidth,
                minWidth = 0
            )
        )
        if (constraints.hasBoundedWidth)
            remainingWidth = (remainingWidth - leftPlaceable.width).coerceAtLeast(0)
        val rightPlaceable = measurables[2].measure(
            constraints.copy(
                maxWidth = remainingWidth,
                minWidth = 0
            )
        )
        if (constraints.hasBoundedWidth)
            remainingWidth = (remainingWidth - rightPlaceable.width).coerceAtLeast(0)
        val centerPlaceable = measurables[1].measure(
            constraints.copy(
                maxWidth = remainingWidth,
                minWidth = 0
            )
        )

        val placeables = listOf(leftPlaceable, centerPlaceable, rightPlaceable)

        val width = if (constraints.hasBoundedWidth) {
            constraints.maxWidth
        } else {
            placeables.sumOf { it.width }
        }

        val height = placeables.maxOf { it.height }.coerceAtMost(constraints.maxHeight)

        layout(width, height) {
            leftPlaceable.placeRelative(0, (height - leftPlaceable.height) / 2)
            rightPlaceable.placeRelative(width - rightPlaceable.width, (height - rightPlaceable.height) / 2)
            val position = (width / 2 - centerPlaceable.width / 2).coerceAtLeast(leftPlaceable.width)
            centerPlaceable.placeRelative(position, (height - centerPlaceable.height) / 2)
        }
    }
}