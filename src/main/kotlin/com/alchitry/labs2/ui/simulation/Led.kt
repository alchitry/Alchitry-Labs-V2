package com.alchitry.labs2.ui.simulation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlin.math.pow

private val ledColor = Color(0xFF5FFF00)

@Composable
fun Led(x: Dp, y: Dp, scale: Dp, rotate: Boolean = false, brightness: () -> Float) {
    val blur = scale * 0.2f
    // we have to add padding around the box, so there is space for it to blur into
    val sizeModifier = if (rotate) {
        Modifier.size(scale * 1.268f + blur * 2, scale * 1.820f + blur * 2)
    } else {
        Modifier.size(scale * 1.820f + blur * 2, scale * 1.268f + blur * 2)
    }

    Box {
        Box(
            Modifier
                .offset(x - blur, y - blur)
                .then(sizeModifier)
                .graphicsLayer {
                    val value = brightness()
                    alpha = if (value.isNaN()) 0f else value.pow(0.45f)
                }

        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .blur(blur)
                    .padding(blur)
                    .background(ledColor, RoundedCornerShape(scale * 0.3f))
            )
        }
        Box(
            Modifier
                .offset(x - blur, y - blur)
                .then(sizeModifier)
                .graphicsLayer {
                    alpha = if (brightness().isNaN()) 1f else 0f
                }

        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .blur(blur)
                    .padding(blur)
                    .background(AlchitryColors.current.Error, RoundedCornerShape(scale * 0.3f))
            )
        }
    }
}