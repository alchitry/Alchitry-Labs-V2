package com.alchitry.labs.ui.components

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ExpandArrow(
    expandTransition: Transition<Boolean>,
    modifier: Modifier = Modifier
) {
    val arrowAngle by expandTransition.animateFloat {
        if (it) 90f else 0f
    }
    val imageSize = with(LocalDensity.current) { LocalTextStyle.current.fontSize.toPx().toDp() }

    Image(
        painter = painterResource("icons/chevron-right.svg"),
        contentDescription = "Expand Arrow",
        colorFilter = ColorFilter.tint(LocalContentColor.current),
        contentScale = ContentScale.Fit,
        modifier = modifier
            .padding(horizontal = 3.dp)
            .size(imageSize)
            .rotate(arrowAngle)
    )
}