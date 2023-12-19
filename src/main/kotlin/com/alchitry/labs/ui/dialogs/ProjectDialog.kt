package com.alchitry.labs.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.Settings
import com.alchitry.labs.project.Project
import com.alchitry.labs.switchActiveWindow

@Composable
fun ProjectDialog(mainContent: @Composable BoxScope.() -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        mainContent()

        if (Project.currentFlow.collectAsState().value == null) {
            Box(
                Modifier.background(Color.Black.copy(alpha = 0.7f)).matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface {
                    Column {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            DialogButton(painterResource("icons/add.svg"), "Create Project") {}
                            DialogButton(painterResource("icons/open.svg"), "Open Project") { Project.openProject() }
                            DialogButton(
                                painterResource("icons/load.svg"),
                                "Alchitry Loader"
                            ) { switchActiveWindow(Settings.WindowType.Loader) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogButton(painter: Painter, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(30.dp)
    ) {
        Image(painter, label, Modifier.size(75.dp))
        Text(label)
    }
}