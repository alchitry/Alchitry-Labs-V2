package com.alchitry.labs2.windows

import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.alchitry.labs2.Env
import com.alchitry.labs2.Settings
import com.alchitry.labs2.ui.theme.AlchitryTheme
import kotlinx.coroutines.delay
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import javax.swing.ImageIcon
import kotlin.math.roundToInt

val LocalComposeWindow = compositionLocalOf<ComposeWindow> { error("No ComposeWindow set!") }
lateinit var mainWindow: ComposeWindow

@Composable
fun ApplicationScope.openWindow(
    title: String,
    initialWindowState: WindowState,
    packContent: Boolean = false,
    minWidth: Dp = 150.dp,
    minHeight: Dp = 150.dp,
    onClose: (WindowState) -> Unit,
    body: @Composable FrameWindowScope.(state: WindowState) -> Unit
) {
    val windowState = rememberWindowState(
        placement = initialWindowState.placement,
        size = initialWindowState.size,
        position = initialWindowState.position,
        isMinimized = initialWindowState.isMinimized
    )
    Window(
        state = windowState,
        title = "$title - ${Env.version}",
        icon = painterResource("/icons/icon.png"),
        onCloseRequest = {
            onClose(windowState)
            exitApplication()
        },
        transparent = false,
        undecorated = true
    ) {
        AlchitryTheme {
            val density = LocalDensity.current
            var updateMinSize by remember { mutableStateOf(false) }

            LaunchedEffect(packContent) {
                if (!packContent) {
                    // window sizes are in Dp even though they take Ints
                    window.minimumSize = with(density) {
                        val max = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
                        Dimension(
                            minWidth.roundToPx().coerceAtMost(max.width),
                            minHeight.roundToPx().coerceAtMost(max.height)
                        )
                    }
                }
                delay(50) // need to wait for the window to open then resize it to the size we want

                window.size =
                    Dimension(
                        initialWindowState.size.width.value.roundToInt(),
                        initialWindowState.size.height.value.roundToInt()
                    )
                window.iconImage =
                    ImageIcon(this::class.java.getResource("/icons/icon.png")).image
                delay(100) // need to wait for the window to open then resize it to the size we want
                updateMinSize = true
            }
            SideEffect { mainWindow = this.window }

            LaunchedEffect(Settings.uiScale) {
                if (!packContent) {
                    // window sizes are in Dp even though they take Ints
                    window.minimumSize = with(density) {
                        val max = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
                        Dimension(
                            minWidth.roundToPx().coerceAtMost(max.width),
                            minHeight.roundToPx().coerceAtMost(max.height)
                        )
                    }
                }
            }

            CompositionLocalProvider(LocalComposeWindow provides this.window) {
                Layout(content = { body(windowState) }) { measurables, constraints ->
                    if (packContent) {
                        val minX = measurables.maxOf { it.minIntrinsicWidth(Int.MAX_VALUE) }
                        val minY = measurables.maxOf { it.minIntrinsicHeight(minX) }

                        val minDim = Dimension(
                            minX.coerceAtLeast(minWidth.roundToPx()),
                            minY.coerceAtLeast(minHeight.roundToPx())
                        )
                        if (updateMinSize) {
                            updateMinSize = false
                            // some window manages seem to include the window decorations while others don't
                            // the offsets adjust for window decorations
                            val offsetX = window.size.width - window.rootPane.size.width
                            val offsetY = window.size.height - window.rootPane.size.height
                            with(density) {
                                window.minimumSize = Dimension(
                                    ((minDim.width + offsetX).toDp().value).roundToInt(),
                                    ((minDim.height + offsetY).toDp().value).roundToInt()
                                )
                            }
                        }
                    }

                    val placeables = measurables.map { measurable ->
                        measurable.measure(constraints)
                    }

                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeables.forEach { placeable ->
                            placeable.placeRelative(x = 0, y = 0)
                        }
                    }
                }
            }
        }
    }
}