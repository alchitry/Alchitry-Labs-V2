package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.ProjectFile
import com.alchitry.labs2.project.removeFile
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DeleteFileDialog(
    file: ProjectFile?,
    onClose: () -> Unit,
) {
    AlchitryDialog(file != null, "Delete File", onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = spacedBy,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Delete ${file?.name ?: "File"}?")
            Text("This cannot be undone!")
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                Button(onClick = onClose, colors = AlchitryColors.current.cancelButton) {
                    Text("Cancel")
                }
                var loading by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            loading = true
                            scope.launch(Dispatchers.IO) {
                                try {
                                    file?.let { f ->
                                        Project.current?.removeFile(f, true)?.also { onClose() }
                                    }
                                } catch (e: IllegalStateException) {
                                    Log.showError("Failed to delete file!", e)
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        colors = AlchitryColors.current.dangerButton
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}