package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.ui.theme.AlchitryColors

@Composable
fun ProjectLockedDialog(show: Boolean, onClose: (Boolean?) -> Unit) {
    AlchitryDialog(show, "Project is already open!", onClose = { onClose(null) }) {
        val spacedBy = Arrangement.spacedBy(10.dp)
        Column(
            Modifier.padding(10.dp).requiredWidthIn(min = 400.dp),
            verticalArrangement = spacedBy,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("The project appears to be open by another instance of Alchitry Labs. Opening it is not recommended.")
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        onClose(true)
                    },
                    colors = AlchitryColors.current.dangerButton
                ) {
                    Text("Accept Risk and Open")
                }
                Button(onClick = {
                    onClose(false)
                }) {
                    Text("Close")
                }
            }
        }
    }
}