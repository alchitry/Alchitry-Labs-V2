package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Settings
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AnalyticsDialog() {
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        showDialog = Settings.sendAnalytics == null
    }
    AlchitryDialog(showDialog, "Analytics Data", onClose = { showDialog = false }) {
        Column(Modifier.padding(16.dp).widthIn(500.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Allow anonymous usage data to be collected to help us understand how Alchitry Labs is used?")
            Text("Data is held privately by Aptabase and is never shared/sold to any other 3rd parties.")
            Text("Your choice can be changed at any time in the settings menu.")
            Spacer(Modifier.weight(1f))
            Row {
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    Settings.sendAnalytics = true
                    showDialog = false
                }) {
                    Text("Allow")
                }
                Spacer(Modifier.width(16.dp))
                Button(onClick = {
                    Settings.sendAnalytics = false
                    showDialog = false
                }, colors = AlchitryColors.current.dangerButton) {
                    Text("Deny")
                }
            }
        }
    }
}