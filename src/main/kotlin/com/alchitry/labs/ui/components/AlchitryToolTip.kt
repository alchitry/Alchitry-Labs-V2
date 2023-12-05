package com.alchitry.labs.ui.components

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun AlchitryToolTip(
    tooltip: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    delayMillis: Int = 500,
    tooltipPlacement: TooltipPlacement = TooltipPlacement.CursorPoint(
        offset = DpOffset(0.dp, 16.dp)
    ),
    content: @Composable () -> Unit
) {
    if (enabled) {
        TooltipArea(
            tooltip = { AlchitryToolTipContent(modifier, tooltip) },
            delayMillis = delayMillis,
            tooltipPlacement = tooltipPlacement
        ) {
            content()
        }
    } else {
        content()
    }
}

@Composable
fun AlchitryToolTipContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(modifier, tonalElevation = 4.dp, shadowElevation = 4.dp, shape = RoundedCornerShape(5.dp)) {
        Box(
            Modifier.padding(8.dp)
        ) {
            content()
        }
    }
}