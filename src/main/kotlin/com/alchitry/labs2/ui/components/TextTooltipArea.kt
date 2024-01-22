package com.alchitry.labs2.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextTooltipArea(
    text: String,
    delayMillis: Int = 500,
    tooltipPlacement: TooltipPlacement = TooltipPlacement.CursorPoint(
        offset = DpOffset(0.dp, 24.dp)
    ),
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = {
            Surface(tonalElevation = 4.dp, shadowElevation = 4.dp, shape = RoundedCornerShape(5.dp)) {
                Text(text, Modifier.padding(8.dp))
            }
        },
        delayMillis = delayMillis,
        tooltipPlacement = tooltipPlacement,
        content = content
    )
}