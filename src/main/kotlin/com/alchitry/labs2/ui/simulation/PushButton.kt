package com.alchitry.labs2.ui.simulation

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp

@Composable
fun PushButton(
    x: Dp, y: Dp,
    scale: Dp,
    rotate: Boolean = false,
    onButtonChange: (Boolean) -> Unit,
) {
    var buttonPressed by remember { mutableStateOf(false) }
    val sizeModifier = if (rotate) {
        Modifier.size(scale * 4.750f, scale * 4.838f)
    } else {
        Modifier.size(scale * 4.838f, scale * 4.750f)
    }
    Box(
        Modifier
            .offset(x, y)
            .then(sizeModifier)
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
                        Color(0xFFAB6E1D),
                        size.width * 0.243386f,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }
            }
    )
}