package com.alchitry.labs2.ui.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.alchitry.labs2.ui.menu.DesktopDropdownMenuPositionProvider
import com.alchitry.labs2.ui.menu.DropDirection

private const val InTransitionDuration = 120
private const val OutTransitionDuration = 75

@Composable
fun ExposedDropdownMenuBoxScope.ScrollableExposedDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchAnchorWidth: Boolean = true,
    shape: Shape = MenuDefaults.shape,
    containerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(1000.dp),
    tonalElevation: Dp = MenuDefaults.TonalElevation,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded

    if (expandedStates.currentState || expandedStates.targetState) {
        val density = LocalDensity.current
        val popupPositionProvider = remember(density) {
            DesktopDropdownMenuPositionProvider(DpOffset(0.dp, 0.dp), density, DropDirection.DOWN)
        }

        Popup(
            popupPositionProvider = popupPositionProvider,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true),
            onPreviewKeyEvent = { false },
            onKeyEvent = {
                if (it.key == Key.Escape) {
                    onDismissRequest()
                    true
                } else {
                    false
                }
            }
        ) {
            val transition = rememberTransition(expandedStates, "DropDownMenu")
            val alpha by transition.animateFloat(
                transitionSpec = {
                    if (false isTransitioningTo true) {
                        tween(durationMillis = InTransitionDuration)
                    } else {
                        tween(durationMillis = OutTransitionDuration)
                    }
                }
            ) {
                if (it) 1f else 0f
            }

            Surface(
                modifier = Modifier.graphicsLayer { this.alpha = alpha },
                shape = shape,
                color = containerColor,
                tonalElevation = tonalElevation,
                shadowElevation = shadowElevation,
                border = border
            ) {
                Box(
                    modifier = modifier.exposedDropdownSize(matchAnchorWidth)
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                                .padding(vertical = 8.dp),
                            content = content
                        )
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(scrollState),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}
