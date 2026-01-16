package com.alchitry.labs2.ui.alchitry_text_field.line_actions

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.components.TextTooltipArea
import com.alchitry.labs2.ui.theme.AlchitryColors

@Composable
fun BoxScope.LineActionButton(
    resource: String,
    description: String,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .padding(1.dp)
            .clip(RoundedCornerShape(3.dp))
            .matchParentSize()
            .clickable(onClick = onClick)
    ) {
        TextTooltipArea(
            text = description
        ) {
            Image(
                painterResource(resource),
                description,
                colorFilter = ColorFilter.tint(AlchitryColors.current.Accent),
                contentScale = ContentScale.Fit
            )
        }
    }
}