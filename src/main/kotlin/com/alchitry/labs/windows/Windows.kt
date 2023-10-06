package com.alchitry.labs.windows

import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.alchitry.labs.Env
import kotlinx.coroutines.delay
import java.awt.Dimension
import javax.swing.ImageIcon

val LocalScale = compositionLocalOf { 1.0f }
val LocalComposeWindow = compositionLocalOf<ComposeWindow> { error("No ComposeWindow set!") }
lateinit var mainWindow: ComposeWindow

@Composable
fun ApplicationScope.openWindow(
    title: String,
    initialWindowState: WindowState,
    packContent: Boolean = false,
    minWidth: Int = 150,
    minHeight: Int = 150,
    onClose: (WindowState) -> Unit,
    body: @Composable () -> Unit
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
        }
    ) {
        var updateMinSize by remember { mutableStateOf(false) }

        LaunchedEffect(packContent) {
            if (!packContent) {
                window.minimumSize = Dimension(minWidth, minHeight)
            }
            delay(50) // need to wait for the window to open then resize it to the size we want

            window.size =
                Dimension(initialWindowState.size.width.value.toInt(), initialWindowState.size.height.value.toInt())
            window.iconImage =
                ImageIcon(this::class.java.getResource("/icons/icon.png")).image
            delay(100) // need to wait for the window to open then resize it to the size we want
            updateMinSize = true
        }
        SideEffect { mainWindow = this.window }

        CompositionLocalProvider(LocalComposeWindow provides this.window) {
            Layout(content = body) { measurables, constraints ->
                if (packContent) {
                    val minX = measurables.maxOf { it.minIntrinsicWidth(Int.MAX_VALUE) }
                    val minY = measurables.maxOf { it.minIntrinsicHeight(minX) }

                    val minDim = Dimension(minX.coerceAtLeast(minWidth), minY.coerceAtLeast(minHeight))
                    if (updateMinSize) {
                        updateMinSize = false
                        // some window manages seem to include the window decorations while others done
                        // the offsets adjust for window decorations
                        val offsetX = window.size.width - window.rootPane.size.width
                        val offsetY = window.size.height - window.rootPane.size.height
                        window.minimumSize = Dimension(minDim.width + offsetX, minDim.height + offsetY)
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