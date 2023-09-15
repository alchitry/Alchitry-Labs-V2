package com.alchitry.labs.ui.components

import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.alchitry.labs.ui.theme.AlchitryColors

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
    Surface(
        modifier,
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        color = AlchitryColors.current.TooltipBackground,
        contentColor = AlchitryColors.current.TooltipContent
    ) {
        Box(
            Modifier.padding(10.dp)
        ) {
            content()
        }
    }
}