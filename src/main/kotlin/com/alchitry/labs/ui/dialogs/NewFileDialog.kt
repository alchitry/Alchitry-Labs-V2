package com.alchitry.labs.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.alchitry.labs.Log
import com.alchitry.labs.parsers.lucid.Lucid
import com.alchitry.labs.project.*
import com.alchitry.labs.ui.tabs.Workspace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LucidFileDialog(visible: Boolean, onClose: () -> Unit) {
    val project by Project.currentFlow.collectAsState()
    NewFileDialog(
        visible,
        onClose,
        "New Lucid Module",
        "Module Name",
        {
            it.matches(Regex("[a-z][a-zA-Z0-9_]*")) &&
                    !Lucid.RESERVED_WORDS.contains(it) &&
                    project?.sourceFiles?.none { f -> f.name == "$it.${Languages.Lucid.extension}" } == true
        },
        { name ->
            project?.addLucidModule(name)?.also {
                Workspace.openFile(it)
            }
        }
    )
}

@Composable
fun LucidTestBenchDialog(visible: Boolean, onClose: () -> Unit) {
    val project by Project.currentFlow.collectAsState()
    NewFileDialog(
        visible,
        onClose,
        "New Lucid Test Bench",
        "Test Bench Name",
        {
            it.matches(Regex("[a-z][a-zA-Z0-9_]*")) &&
                    !Lucid.RESERVED_WORDS.contains(it) &&
                    project?.sourceFiles?.none { f -> f.name == "$it.${Languages.Lucid.extension}" } == true
        },
        { name ->
            project?.addLucidTestBench(name)?.also {
                Workspace.openFile(it)
            }
        }
    )
}

@Composable
fun AcfFileDialog(visible: Boolean, onClose: () -> Unit) {
    val project by Project.currentFlow.collectAsState()
    NewFileDialog(
        visible,
        onClose,
        "New Alchitry Constraint",
        "Constraint Name",
        {
            it.isNotBlank() &&
                    project?.constraintFiles?.none { f -> f.name == "$it.${Languages.ACF.extension}" } == true
        },
        { name ->
            project?.addAlchitryConstraint(name)?.also {
                Workspace.openFile(it)
            }
        }
    )
}

@Composable
private fun NewFileDialog(
    visible: Boolean,
    onClose: () -> Unit,
    title: String,
    label: String,
    validator: (String) -> Boolean,
    onCreate: (String) -> Unit
) {
    AlchitryDialog(visible, title, onClose = onClose) {
        val spacedBy = Arrangement.spacedBy(10.dp)

        var fileName by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus() // focus the text box on launch
        }

        Column(Modifier.padding(10.dp), verticalArrangement = spacedBy) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                OutlinedTextField(
                    fileName,
                    onValueChange = { fileName = it },
                    isError = !validator(fileName),
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    singleLine = true,
                    label = { Text(label) },
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy) {
                Spacer(Modifier.weight(1f))
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
                                    onCreate(fileName)
                                    onClose()
                                } catch (e: IllegalStateException) {
                                    Log.showError("Failed to create new file!", e)
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        enabled = validator(fileName)
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}