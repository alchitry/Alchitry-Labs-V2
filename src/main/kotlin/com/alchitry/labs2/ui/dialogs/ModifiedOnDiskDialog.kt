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
fun ModifiedOnDiskDialog(show: Boolean, fileName: String, onClose: (Boolean?) -> Unit) {
    AlchitryDialog(show, "File Modified on Disk", onClose = { onClose(null) }) {
        val spacedBy = Arrangement.spacedBy(15.dp)
        Column(
            Modifier.padding(10.dp).requiredWidthIn(min = 400.dp),
            verticalArrangement = spacedBy,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("The file \"$fileName\" was modified on disk.")
            Text("Reload the file from disk wiping any local edits or save the current version to disk?")
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        onClose(true)
                    },
                    colors = AlchitryColors.current.dangerButton
                ) {
                    Text("Reload From Disk")
                }
                Button(onClick = {
                    onClose(false)
                }) {
                    Text("Save Local Edits")
                }
            }
        }
    }
}