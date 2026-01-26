package com.alchitry.labs2.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.hardware.usb.SerialDevice
import com.alchitry.labs2.ui.alchitry_text_field.Console
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow

data class IntTextFieldState(
    val value: Int,
    val signed: Boolean,
    val isHex: Boolean,
    val valid: Boolean,
) {
    val radix = if (isHex) 16 else 10
    val valueString: String = formatValue(value, false)

    fun formatValue(value: Int, includePrefix: Boolean = true) = buildString {
        if (isHex && includePrefix) append("0x")
        if (signed)
            append(value.toString(radix).uppercase())
        else
            append(value.toUInt().toString(radix).uppercase())
    }
}

@Composable
fun IntTextField(
    state: IntTextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    signSelector: Boolean = true,
    enabled: Boolean = true,
    onChange: (IntTextFieldState) -> Unit
) {
    var text by remember(state.valueString) { mutableStateOf(state.valueString) }
    Column(modifier.width(IntrinsicSize.Max)) {

        TextField(
            value = text,
            onValueChange = {
                text = it.uppercase()
                val newValue = if (state.signed) {
                    it.toIntOrNull(state.radix)
                } else {
                    it.toUIntOrNull(state.radix)?.toInt()
                }
                when (newValue) {
                    null -> onChange(state.copy(valid = false))
                    else -> onChange(state.copy(value = newValue, valid = true))
                }
            },
            prefix = { if (state.isHex) Text("0x") },
            label = { Text(label) },
            enabled = enabled,
            isError = text != state.valueString,
            modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Decimal:", Modifier.padding(bottom = 5.dp))
            RadioButton(
                selected = !state.isHex,
                onClick = { onChange(state.copy(isHex = false)) },
            )
            Text("Hex:", Modifier.padding(bottom = 5.dp))
            RadioButton(
                selected = state.isHex,
                onClick = { onChange(state.copy(isHex = true)) },
            )
            if (signSelector) {
                Text("Signed:", Modifier.padding(end = 10.dp, bottom = 5.dp))
                Switch(checked = state.signed, onCheckedChange = { onChange(state.copy(signed = it)) })
            }
        }
    }
}

sealed class RegisterRequest {
    abstract fun process(device: SerialDevice): IntArray

    data class BasicWrite(val address: Int, val data: Int) : RegisterRequest() {
        override fun process(device: SerialDevice): IntArray {
            val buffer = ByteArray(9)
            buffer[0] = (1 shl 7).toByte()
            buffer[1] = (address and 0xFF).toByte()
            buffer[2] = (address shr 8 and 0xFF).toByte()
            buffer[3] = (address shr 16 and 0xFF).toByte()
            buffer[4] = (address shr 24 and 0xFF).toByte()
            buffer[5] = (data and 0xFF).toByte()
            buffer[6] = (data shr 8 and 0xFF).toByte()
            buffer[7] = (data shr 16 and 0xFF).toByte()
            buffer[8] = (data shr 24 and 0xFF).toByte()
            check(device.writeData(buffer) == buffer.size) { "Failed to write register!" }
            return intArrayOf()
        }
    }

    data class BasicRead(val address: Int) : RegisterRequest() {
        override fun process(device: SerialDevice): IntArray {
            device.flushReadBuffer()
            val buffer = ByteArray(5)
            buffer[0] = (0 shl 7).toByte()
            buffer[1] = (address and 0xFF).toByte()
            buffer[2] = (address shr 8 and 0xFF).toByte()
            buffer[3] = (address shr 16 and 0xFF).toByte()
            buffer[4] = (address shr 24 and 0xFF).toByte()
            check(device.writeData(buffer) == buffer.size) { "Failed to send read request!" }
            val readBuffer = ByteArray(4)
            val bytesRead = device.readDataWithTimeout(readBuffer)
            check(bytesRead == readBuffer.size) {
                if (readBuffer.isEmpty())
                    "Reading ${readBuffer.size} bytes took too long!"
                else
                    "Read $bytesRead but expected ${readBuffer.size} bytes!"
            }
            return intArrayOf(
                (readBuffer[0].toUByte().toInt()) or (readBuffer[1].toUByte().toInt() shl 8) or (readBuffer[2].toUByte()
                    .toInt() shl 16) or (readBuffer[3].toUByte().toInt() shl 24)
            )
        }
    }

    data class BulkWrite(val address: Int, val increment: Boolean, val data: Collection<Int>) : RegisterRequest() {
        override fun process(device: SerialDevice): IntArray {
            TODO("Not yet implemented")
        }
    }

    data class BulkRead(val address: Int, val increment: Boolean, val length: Int) : RegisterRequest() {
        override fun process(device: SerialDevice): IntArray {
            TODO("Not yet implemented")
        }
    }
}

class RegisterInterface(
    override var parent: TabPanel
) : Tab {
    val state = SerialState()
    private val console = Console()
    private val requests = MutableSharedFlow<RegisterRequest>(extraBufferCapacity = 256)

    private var addressState by mutableStateOf(
        IntTextFieldState(
            value = 0,
            signed = false,
            isHex = false,
            valid = true
        )
    )

    private var valueState by mutableStateOf(
        IntTextFieldState(
            value = 0,
            signed = false,
            isHex = false,
            valid = true
        )
    )


    @Composable
    override fun label() {
        Text("Register Interface")
    }

    @Composable
    override fun content() {
        Surface {
            Box(Modifier.fillMaxSize())
            Column {
                SerialTerminalToolbar(state) { device ->
                    device.setTimeouts(1000, 1000)
                    requests.collect { request ->
                        try {
                            val data = request.process(device)
                            val log = when (request) {
                                is RegisterRequest.BasicRead -> "Read ${valueState.formatValue(data[0])} from address ${
                                    addressState.formatValue(
                                        request.address
                                    )
                                }."

                                is RegisterRequest.BasicWrite -> "Wrote ${valueState.formatValue(request.data)} to address ${
                                    addressState.formatValue(
                                        request.address
                                    )
                                }."

                                is RegisterRequest.BulkRead -> TODO()
                                is RegisterRequest.BulkWrite -> TODO()
                            }
                            console.append("$log\n")
                        } catch (e: IllegalStateException) {
                            val log = when (request) {
                                is RegisterRequest.BasicRead -> "Read failed: ${e.message}"
                                is RegisterRequest.BasicWrite -> "Write failed: ${e.message}"
                                is RegisterRequest.BulkRead -> TODO()
                                is RegisterRequest.BulkWrite -> TODO()
                            }
                            console.append("$log\n", SpanStyle(color = AlchitryColors.current.Error))
                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IntTextField(
                        addressState,
                        "Address",
                        Modifier.padding(top = 10.dp),
                        signSelector = false
                    ) { addressState = it }
                    IntTextField(
                        valueState,
                        "Value",
                        Modifier.padding(top = 10.dp),
                        signSelector = true
                    ) { valueState = it }

                    val valid = addressState.valid && valueState.valid && state.connected
                    Button(onClick = {
                        requests.tryEmit(RegisterRequest.BasicRead(addressState.value))
                    }, enabled = valid) { Text("Read") }
                    Button(onClick = {
                        requests.tryEmit(RegisterRequest.BasicWrite(addressState.value, valueState.value))
                    }, enabled = valid) { Text("Write") }
                }
                console.show()
            }
        }
    }

    override fun onClose(save: Boolean): Boolean {
        state.scope.cancel("Tab closed.")
        return true
    }
}