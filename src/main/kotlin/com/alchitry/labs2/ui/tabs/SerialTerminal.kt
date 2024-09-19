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
import com.alchitry.labs2.Log
import com.alchitry.labs2.hardware.usb.SerialDevice
import com.alchitry.labs2.hardware.usb.UsbUtil
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.ui.components.DeviceSelector
import com.alchitry.labs2.ui.components.ToolbarButton
import com.alchitry.labs2.ui.hiddenClickable
import com.alchitry.labs2.ui.main.Console
import com.alchitry.labs2.windows.LocalLabsState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun SerialTerminalToolbar(
    scope: CoroutineScope,
    onConnectionChanged: (Boolean) -> Unit,
    withDevice: suspend (SerialDevice) -> Unit
) {
    var selectedBoard by remember { mutableStateOf<Pair<Board, Int>?>(null) }
    var baudRate by remember { mutableStateOf(1000000) }
    var connectionJob by remember { mutableStateOf<Job?>(null) }
    var connected by remember { mutableStateOf(false) }

    val labsState = LocalLabsState.current
    val detectedBoards by labsState.detectedBoards
    var attachedToBoard by labsState.attachedToBoard

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
        DeviceSelector(detectedBoards, connected) { selectedBoard = it }
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
                enabled = selectedBoard != null && rateCorrect && !attachedToBoard,
            ) {
                connectionJob = scope.launch {
                    val selection = selectedBoard ?: return@launch
                    try {
                        attachedToBoard = true
                        connected = true
                        onConnectionChanged(true)

                        UsbUtil.openSerial(selection.first, selection.second)?.use { device ->
                            device.setBaudrate(baudRate)
                            withDevice(device)
                        }
                    } catch (_: CancellationException) {
                    } catch (e: Exception) {
                        Log.error("Connection failed: ${e.message}")
                        connectionJob?.cancel("Connection failed: ${e.message}")
                    } finally {
                        attachedToBoard = false
                        connected = false
                        onConnectionChanged(false)
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
}

class SerialTerminal(
    override var parent: TabPanel
) : Tab {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val console = Console()
    private val textFlow = MutableSharedFlow<String>(extraBufferCapacity = 8192)

    @Composable
    override fun label() {
        Text("Serial Terminal")
    }

    @Composable
    override fun content() {
        var connected by remember { mutableStateOf(false) }

        Surface {
            Box(Modifier.fillMaxSize())
            Column {

                SerialTerminalToolbar(scope, { connected = it }) { device ->
                    val buffer = ByteArray(1024)

                    coroutineScope {
                        launch {
                            while (isActive) {
                                val bytes = device.readData(buffer)
                                if (bytes > 0) {
                                    console.append(buffer.decodeToString(0, bytes).replace("\r", ""))
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

                var hasFocus by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusable(true, remember { MutableInteractionSource() })
                        .onFocusChanged { hasFocus = it.hasFocus }
                        .hiddenClickable { }
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.isTypedEvent ||
                                (keyEvent.type == KeyEventType.KeyDown &&
                                        (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter))
                            ) {
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