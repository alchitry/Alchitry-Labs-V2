package com.alchitry.labs2.ui.simulation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.alchitry.labs2.ui.cache.Cached
import com.alchitry.labs2.ui.cache.CachedImage
import com.alchitry.labs2.ui.hiddenClickable
import kotlin.math.pow


@Composable
fun IoBoard(
    leds: (Int) -> Float,
    digits: (digit: Int, segment: Int) -> Float,
    modifier: Modifier = Modifier,
    onButtonChange: (Int, Boolean) -> Unit,
    onSwitchChange: (Int) -> Unit,
    switchState: (Int) -> Boolean
) {

    BoxWithConstraints(modifier.aspectRatio(65.0f / 45.0f).fillMaxSize()) {
        val darkenFilter = remember {
            ColorFilter.colorMatrix(
                ColorMatrix().apply {
                    setToScale(0.85f, 0.85f, 0.85f, 1f)
                }
            )
        }
        CachedImage(
            painterResource("boards/io.svg"),
            Modifier.fillMaxSize(),
            colorFilter = darkenFilter
        )

        val scale = maxWidth / 65f

        PushButton(scale * 46.125f, scale * 3.581f, scale, rotate = true) { onButtonChange(0, it) }
        PushButton(scale * 46.125f, scale * 10.081f, scale, rotate = true) { onButtonChange(1, it) }
        PushButton(scale * 46.125f, scale * 16.581f, scale, rotate = true) { onButtonChange(2, it) }
        PushButton(scale * 39.581f, scale * 10.125f, scale, rotate = false) { onButtonChange(3, it) }
        PushButton(scale * 52.581f, scale * 10.125f, scale, rotate = false) { onButtonChange(4, it) }

        var ledX = scale * 54.366f
        val ledY = scale * 24.841f
        val ledSpacing = scale * -2f
        val ledGroupSpacing = scale * (36.371f - 40.366f)
        for (j in 0..2) {
            for (i in 0..7) {
                Led(ledX, ledY, scale, rotate = true) { leds(j * 8 + i) }
                ledX += ledSpacing
            }
            ledX += ledGroupSpacing - ledSpacing
        }

        val digitX = scale * 29f
        val digitY = scale * 10.5f
        val digitSpacing = scale * -7.75f
        for (i in 0..3) {
            SegmentDigit(digitX + digitSpacing * i, digitY, scale) { digits(i, it) }
        }

        var dipX = scale * 52.150f
        val dipY = scale * 33.900f
        val dipSpacing = scale * -1.3f
        val dipGroupSpacing = scale * -8.9f
        for (j in 0..2) {
            for (i in 0..7) {
                DipSwitch(dipX, dipY, scale, state = { switchState(i + j * 8) }) { onSwitchChange(i + j * 8) }
                dipX += dipSpacing
            }
            dipX += dipGroupSpacing - dipSpacing
        }
    }

}

@Composable
private fun SegmentDigit(
    x: Dp,
    y: Dp,
    scale: Dp,
    values: (Int) -> Float
) {
    Box(
        Modifier
            .offset(x, y)
            .size(scale * 7.500f, scale * 10.000f)
    ) {
        for (i in 0..7) {
            key(i) {
                Cached(Modifier.fillMaxSize().graphicsLayer { alpha = values(i).pow(0.45f) }) {
                    Image(
                        painterResource("boards/io_$i.svg"),
                        "Digit segment",
                        modifier = Modifier.fillMaxSize()
                            .blur(scale * 0.2f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DipSwitch(
    x: Dp,
    y: Dp,
    scale: Dp,
    state: () -> Boolean,
    onToggle: () -> Unit
) {
    Box(
        Modifier
            .offset(x, y)
            .size(scale * 0.8f, scale * 3.2f)
            .hiddenClickable(onClick = onToggle)
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        val animatedPosition by animateFloatAsState(targetValue = if (state()) 0.675f else 1.725f)
        Box(
            Modifier
                .offset(scale * 0.1f, scale * animatedPosition)
                .size(scale * 0.6f)
                .background(Color.White)
        )
    }
}