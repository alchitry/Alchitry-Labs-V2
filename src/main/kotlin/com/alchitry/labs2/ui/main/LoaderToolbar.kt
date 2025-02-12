package com.alchitry.labs2.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.alchitry.labs2.Settings
import com.alchitry.labs2.switchActiveWindow
import com.alchitry.labs2.ui.components.ToolbarButton
import com.alchitry.labs2.ui.menu.RadioMenuItem
import kotlin.math.roundToInt

@Composable
fun LoaderToolbar() {
    Row {
        val icon = if (Settings.darkTheme) "icons/alchitry_icon.svg" else "icons/alchitry_icon_black.svg"
        IconMenu(painterResource(icon), "Menu", colorFilter = null) {
            RadioMenuItem(
                label = { Text("UI Scale") },
                items = listOf(0.75f, 0.9f, 1f, 1.1f, 1.25f, 1.5f, 2f, 2.5f, 3f),
                labeler = { Text("${(it * 100).roundToInt()}%") },
                selected = Settings.uiScale
            ) {
                Settings.uiScale = it
            }

            RadioMenuItem(
                label = { Text("Theme") },
                items = listOf(false, true),
                labeler = { Text(if (it) "Dark" else "Light") },
                selected = Settings.darkTheme
            ) {
                Settings.darkTheme = it
            }
        }
        ToolbarButton(
            icon = painterResource("icons/swap_horiz.svg"),
            description = "Switch to Alchitry Labs"
        ) { switchActiveWindow(Settings.WindowType.Labs) }
    }
}