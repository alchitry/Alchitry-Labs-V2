package com.alchitry.labs.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alchitry.labs.ui.components.ToolbarButton
import com.alchitry.labs.ui.theme.AlchitryTheme

@Composable
fun Toolbar() {
    Row(modifier = Modifier.fillMaxWidth()) {
        ToolbarButton(
            onClick = {},
            painterResource("icons/open.svg"),
            "New file"
        )
        ToolbarButton(
            onClick = {},
            painterResource("icons/save.svg"),
            "Save"
        )
        ToolbarButton(
            onClick = {},
            painterResource("icons/save_all.svg"),
            "Save all"
        )
        ToolbarButton(
            onClick = {},
            painterResource("icons/check.svg"),
            "Check for errors"
        )
        ToolbarButton(
            onClick = {},
            painterResource("icons/open.svg"),
            "New File"
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