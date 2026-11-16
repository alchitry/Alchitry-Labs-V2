package com.alchitry.labs2.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.project.Project
import com.alchitry.labs2.project.files.SourceFile
import com.alchitry.labs2.project.renameModule
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RenameFileDialog(
    file: SourceFile?,
    onClose: () -> Unit
) {
    AlchitryDialog(file != null, "Rename Module", onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)
        val initialName = file?.name?.substringBeforeLast('.') ?: ""
        var textFieldValue by remember(file) {
            mutableStateOf(TextFieldValue(initialName, TextRange(0, initialName.length)))
        }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(file) {
            if (file != null) {
                focusRequester.requestFocus()
            }
        }

        var loading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        val newName = textFieldValue.text
        val extension = file?.file?.extension ?: ""
        val currentNameWithoutExt = file?.name?.substringBeforeLast('.') ?: ""
        val isValidName = sourceNameValidator(newName, extension)
        val canRename = isValidName && newName != currentNameWithoutExt

        fun performRename() {
            if (!canRename) return
            loading = true
            scope.launch(Dispatchers.IO) {
                try {
                    file?.let { f ->
                        Project.current?.renameModule(f, newName)
                    }
                    onClose()
                } catch (e: Exception) {
                    Log.showError("Failed to rename module!", e)
                } finally {
                    loading = false
                }
            }
        }

        Column(Modifier.padding(10.dp), verticalArrangement = spacedBy) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    isError = !isValidName,
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    singleLine = true,
                    label = { Text("Module Name") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { performRename() })
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
                Button(onClick = onClose, colors = AlchitryColors.current.cancelButton, enabled = !loading) {
                    Text("Cancel")
                }
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { performRename() },
                        enabled = canRename
                    ) {
                        Text("Rename")
                    }
                }
            }
        }
    }
}
