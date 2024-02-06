package com.alchitry.labs2.ui.simulation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.ui.cache.CachedImage

@Composable
fun AlchitryBoard(
    board: Board,
    leds: (Int) -> Float,
    done: Float,
    modifier: Modifier = Modifier,
    onButtonChange: (Boolean) -> Unit
) {
    val boardImage = when (board) {
        Board.AlchitryAu -> "au.svg"
        Board.AlchitryAuPlus -> "au.svg"
        Board.AlchitryCu -> "cu.svg"
    }

    BoxWithConstraints(modifier.aspectRatio(65.0f / 45.0f).fillMaxSize()) {
        val darkenFilter = remember {
            ColorFilter.colorMatrix(
                ColorMatrix().apply {
                    setToScale(0.85f, 0.85f, 0.85f, 1f)
                }
            )
        }
        CachedImage(
            painterResource("boards/$boardImage"),
            Modifier.fillMaxSize(),
            colorFilter = darkenFilter
        )

        val scale = maxWidth / 65f

        PushButton(
            x = scale * 59.080f,
            y = scale * 30.125f,
            scale = scale,
            onButtonChange = onButtonChange
        )

        val ledX = scale * 61.089f
        val ledY = scale * 10.366f
        val doneX = scale * 2.089f
        val doneY = scale * 26.866f
        val ledSpacing = scale * 2f

        for (i in 0..7) {
            Led(ledX, ledY + ledSpacing * i, scale, false) { leds(i) }
        }
        Led(doneX, doneY, scale, false) { done }
    }

}