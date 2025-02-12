package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Settings
import com.alchitry.labs2.ui.hiddenClickable
import com.alchitry.labs2.ui.theme.AlchitryColors

@Composable
fun RebuildProjectDialog(
    show: Boolean,
    onClose: (Boolean?) -> Unit
) {
    AlchitryDialog(show, "Rebuild Project", onClose = { onClose(null) }) {
        val spacedBy = Arrangement.spacedBy(10.dp)
        var rememberDecision by remember { mutableStateOf(false) }
        Column(
            Modifier.padding(10.dp).requiredWidthIn(min = 400.dp),
            verticalArrangement = spacedBy,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("The project has been modified since last built. Rebuild before loading?")
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.hiddenClickable { rememberDecision = !rememberDecision }) {
                    Checkbox(checked = rememberDecision, onCheckedChange = { rememberDecision = it })
                    Text("Don't ask again", Modifier.padding(bottom = 5.dp, start = 3.dp))
                }
                Spacer(Modifier.weight(1f))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        Settings.askForRebuild = !rememberDecision
                        Settings.rebuildStale = false
                        onClose(false)
                    },
                    colors = AlchitryColors.current.cancelButton
                ) {
                    Text("Skip")
                }
                Button(onClick = {
                    Settings.askForRebuild = !rememberDecision
                    Settings.rebuildStale = true
                    onClose(true)
                }) {
                    Text("Rebuild")
                }
            }
        }
    }
}