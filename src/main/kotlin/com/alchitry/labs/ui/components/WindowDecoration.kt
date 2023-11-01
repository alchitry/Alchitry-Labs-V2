package com.alchitry.labs.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import com.alchitry.labs.Env
import com.alchitry.labs.ui.theme.AlchitryColors
import java.awt.Frame
import java.awt.event.WindowEvent

@Composable
fun FrameWindowScope.WindowDecoration() {
    WindowDraggableArea {
        Surface(tonalElevation = 5.dp, modifier = Modifier.height(48.dp)) {
            Decorations(
                content = {
                    Image(
                        painter = painterResource("icons/alchitry_icon.svg"),
                        contentDescription = "Alchitry Logo",
                        modifier = Modifier.size(45.dp).padding(start = 10.dp)
                    )
                    Text(window.title, Modifier.padding(start = 10.dp))
                },
                buttons = {
                    WindowButton(
                        onClick = { window.isMinimized = true },
                    ) {
                        Icon(
                            painterResource("icons/minimize.svg"),
                            "Close",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    WindowButton(
                        onClick = {
                            window.extendedState = if (window.extendedState == Frame.MAXIMIZED_BOTH)
                                Frame.NORMAL else Frame.MAXIMIZED_BOTH
                        },
                    ) {
                        Icon(
                            painterResource("icons/maximize.svg"),
                            "Close",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    WindowButton(
                        onClick = { window.dispatchEvent(WindowEvent(window, WindowEvent.WINDOW_CLOSING)) },
                        color = AlchitryColors.current.Error
                    ) {
                        Icon(
                            painterResource("icons/close.svg"),
                            "Close",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun FrameWindowScope.Decorations(
    content: @Composable FrameWindowScope.() -> Unit,
    buttons: @Composable FrameWindowScope.() -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (Env.isMac) {
            Layout(
                content = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Row(horizontalArrangement = Arrangement.End) {
                            buttons()
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        content()
                    }
                }
            ) { measurables, constraints ->
                val buttonPlaceable = measurables[0].measure(constraints)
                val titlePlaceable = measurables[1].measure(constraints)

                val width = if (constraints.hasBoundedWidth) {
                    constraints.maxWidth
                } else {
                    buttonPlaceable.width + titlePlaceable.width
                }

                val height = buttonPlaceable.height.coerceAtLeast(titlePlaceable.height)

                layout(width, constraints.maxHeight) {
                    buttonPlaceable.placeRelative(0, height / 2 - buttonPlaceable.height / 2)
                    val position = (width / 2 - titlePlaceable.width / 2).coerceAtLeast(buttonPlaceable.width)
                    titlePlaceable.placeRelative(position, height / 2 - titlePlaceable.height / 2)
                }
            }
        } else {
            content()
            Spacer(Modifier.weight(1f))
            buttons()
        }
    }
}

@Composable
fun WindowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .aspectRatio(1f, true)
            .clipToBounds()
            .alpha(if (interactionSource.collectIsHoveredAsState().value) 0.75f else 0.25f)
            .background(if (interactionSource.collectIsHoveredAsState().value) color else Color.Transparent)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple()
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}