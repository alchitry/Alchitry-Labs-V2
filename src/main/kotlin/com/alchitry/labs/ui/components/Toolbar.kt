package com.alchitry.labs.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.project.Project
import com.alchitry.labs.ui.theme.AlchitryTheme

@Composable
fun Toolbar() {
    val scope = rememberCoroutineScope()
    Row(modifier = Modifier.fillMaxWidth()) {
        ToolbarButton(
            onClick = {
                Project.openProject()
            },
            icon = painterResource("icons/alchitry_icon.svg"),
            description = "Project",
            colorFilter = null
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/open.svg"),
            description = "New file"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/save.svg"),
            description = "Save"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/save_all.svg"),
            description = "Save all"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/check.svg"),
            description = "Check for errors"
        )
        ToolbarButton(
            onClick = {},
            icon = painterResource("icons/open.svg"),
            description = "New File"
        )
    }
}

@Preview
@Composable
fun ToolbarPreview() {
    AlchitryTheme {
        Box(Modifier.size(1000.dp).background(MaterialTheme.colors.background)) {
            Toolbar()
        }
    }
}