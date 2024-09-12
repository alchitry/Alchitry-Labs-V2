package com.alchitry.labs2.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.isTypedEvent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.hardware.usb.UsbUtil
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.ui.components.ToolbarButton
import com.alchitry.labs2.ui.hiddenClickable
import com.alchitry.labs2.ui.main.Console
import com.alchitry.labs2.windows.LocalLabsState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

class SerialTerminal(
    override var parent: TabPanel
) : Tab {
    val scope = CoroutineScope(Dispatchers.Default)
    var connected by mutableStateOf(false)
    val console = Console()
    var selectedBoard by mutableStateOf<Pair<Board, Int>?>(null)
    var baudRate by mutableStateOf(500000)
    var connectionJob: Job? = null
    val textFlow = MutableSharedFlow<String>(extraBufferCapacity = 8192)

    @Composable
    override fun label() {
        Text("Serial Terminal")
    }

    @Composable
    private fun DeviceSelector(detectedBoards: Map<Board, Int>) {
        var expanded by remember { mutableStateOf(false) }
        val options = detectedBoards.flatMap { (board, count) ->
            if (count > 1) {
                List(count) {
                    "${board.name} (${count + 1})" to (board to it)
                }
            } else {
                listOf(board.name to (board to 0))
            }
        }.toMap()
        var selectedOption by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(options) {
            if (selectedOption == null || !options.contains(selectedOption)) {
                selectedOption = options.keys.minOrNull()
                selectedBoard = options[selectedOption]
            }
        }
        val enabled = !connected && options.isNotEmpty()
        ExposedDropdownMenuBox(expanded, onExpandedChange = { expanded = it && enabled }) {
            OutlinedTextField(
                readOnly = true,
                value = selectedOption ?: "No Devices Found",
                onValueChange = {},
                label = { Text("Device") },
                enabled = enabled,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded && enabled,
                onDismissRequest = { expanded = false },
                Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(1000.dp))
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it.key) },
                        onClick = {
                            selectedOption = it.key
                            selectedBoard = it.value
                            expanded = false
                        })
                }
            }

        }
    }

    @Composable
    override fun content() {
        val labsState = LocalLabsState.current
        val detectedBoards by labsState.detectedBoards
        var attachedToBoard by labsState.attachedToBoard

        Surface {
            Box(Modifier.fillMaxSize())
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    var rateText by remember { mutableStateOf(baudRate.toString()) }
                    val rateCorrect = rateText == baudRate.toString()
                    DeviceSelector(detectedBoards)
                    TextField(
                        value = rateText,
                        onValueChange = {
                            rateText = it
                            it.toIntOrNull()?.coerceAtLeast(1)?.let { d -> baudRate = d }
                        },
                        label = { Text("Baud Rate") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        enabled = !connected,
                        isError = !rateCorrect
                    )
                    if (!connected) {
                        ToolbarButton(
                            painterResource("icons/link.svg"),
                            "Connect",
                            size = 52.dp,
                            enabled = selectedBoard != null && rateCorrect,
                        ) {
                            connectionJob = scope.launch {
                                val selection = selectedBoard ?: return@launch
                                try {
                                    labsState.attachedToBoard.value = true
                                    connected = true

                                    UsbUtil.openSerial(selection.first, selection.second)?.use { device ->
                                        device.setBaudrate(baudRate)
                                        val buffer = ByteArray(1024)

                                        coroutineScope {
                                            launch {
                                                while (isActive) {
                                                    val bytes = device.readData(buffer)
                                                    if (bytes > 0) {
                                                        console.append(buffer.decodeToString(0, bytes))
                                                    } else {
                                                        delay(100)
                                                    }
                                                }
                                            }
                                            launch {
                                                textFlow.collect { text ->
                                                    device.writeData(text.toByteArray(Charsets.UTF_8))
                                                }
                                            }
                                        }
                                    }
                                } finally {
                                    labsState.attachedToBoard.value = false
                                    connected = false
                                }
                            }

                        }
                    } else {
                        ToolbarButton(
                            painterResource("icons/link-off.svg"),
                            "Disconnect",
                            size = 52.dp,
                        ) {
                            connectionJob?.cancel()
                        }
                    }
                }
                var hasFocus by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusable(true, remember { MutableInteractionSource() })
                        .onFocusChanged { hasFocus = it.hasFocus }
                        .hiddenClickable { }
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.isTypedEvent || (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Enter)) {
                                val text = String(intArrayOf(keyEvent.utf16CodePoint), 0, 1)
                                textFlow.tryEmit(text)
                                true
                            } else {
                                false
                            }
                        }
                ) {
                    console.show(hasFocus && connected)
                }
            }
        }
    }

    override fun onClose(save: Boolean): Boolean {
        scope.cancel("Tab closed.")
        return true
    }
}