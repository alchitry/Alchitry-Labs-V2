package com.alchitry.labs2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.alchitry.labs2.Env
import com.alchitry.labs2.ui.layout.LCR
import com.alchitry.labs2.ui.theme.AlchitryColors
import java.awt.Frame
import java.awt.event.WindowEvent

@Composable
fun FrameWindowScope.WindowDecoration(state: WindowState, toolbar: (@Composable () -> Unit)? = null) {
    WindowDraggableArea(Modifier.onClick(onDoubleClick = {
        state.placement = if (state.placement == WindowPlacement.Maximized)
            WindowPlacement.Floating else WindowPlacement.Maximized
    }) {}) {
        Surface(tonalElevation = 5.dp, modifier = Modifier.height(48.dp)) {
            Decorations(
                toolbar = {
                    if (toolbar == null) {
                        Image(
                            painter = painterResource("icons/alchitry_icon.svg"),
                            contentDescription = "Alchitry Logo",
                            modifier = Modifier.padding(horizontal = 10.dp).size(35.dp)
                        )
                    } else {
                        toolbar()
                    }
                },
                title = {
                    Text(window.title, Modifier.padding(horizontal = 10.dp))
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
    toolbar: @Composable FrameWindowScope.() -> Unit,
    title: @Composable FrameWindowScope.() -> Unit,
    buttons: @Composable FrameWindowScope.() -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (Env.isMac) {
            LCR(
                left = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Row(horizontalArrangement = Arrangement.End) {
                            buttons()
                        }
                    }
                    toolbar()
                },
                center = { title() }
            )
        } else {
            LCR(
                left = { toolbar() },
                center = { title() },
                right = { buttons() }
            )
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
                indication = remember { ripple() }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun DialogWindowScope.WindowDecoration(onClose: () -> Unit) {
    WindowDraggableArea {
        Surface(tonalElevation = 5.dp, modifier = Modifier.height(48.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                LCR(
                    center = { Text(window.title, Modifier.padding(horizontal = 10.dp)) },
                    right = {
                        WindowButton(
                            onClick = onClose,
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
}