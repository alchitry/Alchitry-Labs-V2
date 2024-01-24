package com.alchitry.labs2.ui.simulation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.alchitry.labs2.project.Board
import kotlin.math.pow

@Composable
fun AlchitryBoard(
    board: Board,
    leds: List<Float>,
    done: Float,
    modifier: Modifier = Modifier,
    onButtonChange: (Boolean) -> Unit
) {
    val boardImage = when (board) {
        Board.AlchitryAu -> "au.svg"
        Board.AlchitryAuPlus -> "au.svg"
        Board.AlchitryCu -> "cu.svg"
    }

    Box(modifier.aspectRatio(65.0f / 45.0f), contentAlignment = Alignment.Center) {
        Image(
            painterResource("boards/$boardImage"),
            "Alchitry Board",
            Modifier.fillMaxSize()
        )

        BoxWithConstraints {
            val scale = maxWidth / 65f
            var buttonPressed by remember { mutableStateOf(false) }

            Box(
                Modifier
                    .offset(scale * 59.080f, scale * 30.125f)
                    .size(scale * 4.838f, scale * 4.750f)
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            down.consume()
                            buttonPressed = true
                            onButtonChange(true)
                            waitForUpOrCancellation()
                            buttonPressed = false
                            onButtonChange(false)
                        }
                    }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .drawBehind {
                        if (buttonPressed) {
                            drawCircle(
                                Color(0xFFDC8B22),
                                size.width * 0.243386f,
                                center = Offset(size.width / 2f, size.height / 2f)
                            )
                        }
                    }
            )

            Box(Modifier.fillMaxSize().alpha(0.2f).background(Color.Black))

            val ledX = scale * 61.089f
            val ledY = scale * 10.366f
            val doneX = scale * 2.089f
            val doneY = scale * 26.866f
            val ledSpacing = scale * 2f

            leds.forEachIndexed { index, brightness ->
                Led(ledX, ledY + ledSpacing * index, scale, brightness)
            }
            Led(doneX, doneY, scale, done)
        }
    }
}

private val ledColor = Color(0xFF5FFF00)

@Composable
private fun Led(x: Dp, y: Dp, scale: Dp, brightness: Float) {
    val blur = scale * 0.2f
    // we have to add padding around the box, so there is space for it to blur into
    Box(
        Modifier
            .offset(x - blur, y - blur)
            .size(scale * 1.820f + blur * 2, scale * 1.268f + blur * 2)
            .blur(blur)
            .padding(blur)
            .alpha(brightness.pow(0.45f)) // gamma correction
            .background(ledColor, RoundedCornerShape(scale * 0.3f))
    )
}