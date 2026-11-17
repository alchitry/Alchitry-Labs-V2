package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorDialog(
    message: String?,
    title: String = "Error",
    onClose: () -> Unit
) {
    ErrorDialog(show = message != null, message = message ?: "", title = title, onClose = onClose)
}

@Composable
fun ErrorDialog(
    show: Boolean,
    message: String,
    title: String = "Error",
    onClose: () -> Unit
) {
    AlchitryDialog(show, title, onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)
        Column(
            Modifier.padding(10.dp).requiredWidthIn(min = 400.dp),
            verticalArrangement = spacedBy,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(message)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                Button(onClick = onClose) {
                    Text("Close")
                }
            }
        }
    }
}
