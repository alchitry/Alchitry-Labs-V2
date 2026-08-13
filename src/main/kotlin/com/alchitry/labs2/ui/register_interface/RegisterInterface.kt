package com.alchitry.labs2.ui.register_interface

import androidx.compose.animation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.alchitry.hardware.usb.SerialDevice
import com.alchitry.labs2.painterResource
import com.alchitry.labs2.ui.components.AlchitryToolTip
import com.alchitry.labs2.ui.components.ToggleButton
import com.alchitry.labs2.ui.components.VerticalScrollableBox
import com.alchitry.labs2.ui.drag_and_drop.*
import com.alchitry.labs2.ui.graphing.GraphLinkState
import com.alchitry.labs2.ui.tabs.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class RegisterInterface(
    override var parent: TabPanel
) : Tab {
    val state = SerialState()
    private val requests = MutableSharedFlow<RegisterRequest>(extraBufferCapacity = 256)
    private val rows = mutableStateListOf<RegisterRow>()
    val graphLinkState = GraphLinkState()
    val timeOffset = TimeSource.Monotonic.markNow()
    var collectingValues by mutableStateOf(false)
    private val verticalScrollState = ScrollState(0)
    var showConfig by mutableStateOf(true)

    private var addressState by mutableStateOf(
        NumberFieldState(
            text = "0",
            fractionalBits = 0,
            value = 0,
            signed = false,
            radix = Radix.Decimal,
            valid = true
        )
    )

    @Composable
    override fun label() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PulsingLink(state)
            Text("Register Interface")
        }
    }

    private fun <T> Channel<T>.tryReceiveAll(): List<T> {
        val items = mutableListOf<T>()
        while (true) {
            val result = tryReceive().getOrNull() ?: break
            items.add(result)
        }
        return items
    }

    private fun List<Int>.toRanges(): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var start: Int? = null
        var end = 0
        for (value in this) {
            if (start == null) {
                start = value
                end = value
            } else if (value == end + 1) {
                end = value
            } else {
                ranges.add(start..end)
                start = value
                end = value
            }
        }
        start?.let { ranges.add(it..end) }
        return ranges
    }

    private fun IntRange.chunked(maxLength: Int): List<IntRange> {
        require(maxLength > 0) { "maxLength must be positive!" }
        val ranges = mutableListOf<IntRange>()
        var start = first
        while (start <= last) {
            val end = minOf(start + maxLength - 1, last)
            ranges.add(start..end)
            start = end + 1
        }
        return ranges
    }

    private fun timeToSendFourBytes(baudRate: Int): Duration {
        require(baudRate > 0) { "baudRate must be positive!" }
        // Each byte takes 10 bits on the wire (1 start bit + 8 data bits + 1 stop bit)
        val bits = 4 * 10
        return (bits.toDouble() / baudRate).seconds
    }

    private suspend fun monitorRegisters(device: SerialDevice) = withContext(Dispatchers.IO) {
        device.flushReadBuffer()

        while (isActive) {
            val rows = rows.toList()
            val pendingRequests = rows.flatMap { it.requests.tryReceiveAll() }

            if (collectingValues) {
                rows.mapNotNull { if (it.watching) it.address else null }.sorted().toRanges().forEach { registerGroup ->
                    val values = readRegisters(device, registerGroup)
                    registerGroup.forEachIndexed { index, address ->
                        rows.firstOrNull { it.address == address }?.onNewData(values[index], timeOffset.elapsedNow())
                    }
                }
            }

            pendingRequests.forEach { it.process(device) }

            if (pendingRequests.isEmpty() && !collectingValues) {
                delay(100.milliseconds)
            }
        }
    }

    private fun readRegisterMultiple(device: SerialDevice, address: Int, times: Int): List<Int> {
        val values = mutableListOf<Int>()
        for (i in 0 until times step 64) {
            val count = min(times - i, 64)
            val buffer = ByteArray(5)
            buffer[0] = ((0 shl 7) or (0 shl 6) or (count - 1)).toByte()
            buffer[1] = (address and 0xFF).toByte()
            buffer[2] = (address shr 8 and 0xFF).toByte()
            buffer[3] = (address shr 16 and 0xFF).toByte()
            buffer[4] = (address shr 24 and 0xFF).toByte()
            check(device.writeData(buffer) == buffer.size) { "Failed to send read request!" }
            val readBuffer = ByteArray(4 * count)
            val bytesRead = device.readDataWithTimeout(readBuffer)

            check(bytesRead == readBuffer.size) {
                if (readBuffer.isEmpty())
                    "Reading ${readBuffer.size} bytes took too long!"
                else
                    "Read $bytesRead but expected ${readBuffer.size} bytes!"
            }

            ByteBuffer.wrap(readBuffer)
                .order(ByteOrder.LITTLE_ENDIAN)
                .asIntBuffer()
                .let { buffer ->
                    values.add(buffer.get())
                }
        }
        return values
    }

    private fun readRegisters(device: SerialDevice, registerGroup: IntRange): List<Int> {
        return registerGroup.chunked(64).flatMap { chunkedGroup ->
            val buffer = ByteArray(5)
            val address = chunkedGroup.first
            val count = chunkedGroup.last - chunkedGroup.first + 1
            buffer[0] = ((0 shl 7) or (1 shl 6) or (count - 1)).toByte()
            buffer[1] = (address and 0xFF).toByte()
            buffer[2] = (address shr 8 and 0xFF).toByte()
            buffer[3] = (address shr 16 and 0xFF).toByte()
            buffer[4] = (address shr 24 and 0xFF).toByte()
            check(device.writeData(buffer) == buffer.size) { "Failed to send read request!" }
            val readBuffer = ByteArray(4 * count)
            val bytesRead = device.readDataWithTimeout(readBuffer)

            check(bytesRead == readBuffer.size) {
                if (readBuffer.isEmpty())
                    "Reading ${readBuffer.size} bytes took too long!"
                else
                    "Read $bytesRead but expected ${readBuffer.size} bytes!"
            }

            ByteBuffer.wrap(readBuffer)
                .order(ByteOrder.LITTLE_ENDIAN)
                .asIntBuffer()
                .let { buffer ->
                    List(count) { buffer.get() }
                }
        }
    }

    @Composable
    override fun content() {
        Surface {
            Box(Modifier.fillMaxSize())
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SerialTerminalToolbar(state) { device ->
                        device.setTimeouts(100, 100)
                        monitorRegisters(device)
                    }

                    NumberField(
                        addressState,
                        "Address",
                        signSelector = false
                    ) {
                        addressState = it
                    }
                    val canAddAddress = rows.none { it.address == addressState.value }
                    AlchitryToolTip(
                        tooltip = { Text("Address \"${addressState.value}\" is already in use.") },
                        enabled = !canAddAddress
                    ) {
                        Button(onClick = {
                            rows.add(RegisterRow(addressState.value, this@RegisterInterface) { rows.remove(it) })
                        }, enabled = canAddAddress && addressState.valid) { Text("Add Address") }
                    }
                    ToggleButton(
                        active = showConfig,
                        tooltip = { Text("Show Configuration") },
                        onClick = { showConfig = it }
                    ) {
                        Icon(
                            painterResource("icons/gear.svg"),
                            contentDescription = "Configuration",
                            modifier = Modifier.size(45.dp).padding(5.dp)
                        )
                    }
                    AnimatedVisibility(
                        rows.any { it.watching },
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            ToggleButton(
                                active = collectingValues,
                                enabled = state.connected,
                                tooltip = { Text(if (collectingValues) "Pause" else "Start") },
                                onClick = { collectingValues = it }
                            ) {
                                Crossfade(collectingValues) { collecting ->
                                    if (collecting) {
                                        Icon(
                                            painterResource("icons/pause.svg"),
                                            contentDescription = "Pause",
                                            modifier = Modifier.size(45.dp).padding(5.dp)
                                        )
                                    } else {
                                        Icon(
                                            painterResource("icons/play.svg"),
                                            contentDescription = "Start",
                                            modifier = Modifier.size(45.dp).padding(5.dp)
                                        )
                                    }
                                }
                            }

                            ToggleButton(
                                active = false,
                                enabled = state.connected,
                                tooltip = { Text("Reset") },
                                onClick = { rows.forEach { it.resetData() } }
                            ) {
                                Icon(
                                    painterResource("icons/replay.svg"),
                                    contentDescription = "Reset",
                                    modifier = Modifier.size(45.dp).padding(5.dp)
                                )
                            }
                        }
                    }
                }
                VerticalScrollableBox(verticalScrollState = verticalScrollState) {
                    DragAndDropZone(
                        dragAnchor = DragAnchor.KeepOffset,
                        dragDirection = DragDirection.VerticalOnly
                    ) {
                        DropZone(
                            minimumSize = DpSize(1.dp, 1.dp),
                            content = { HorizontalDivider() },
                            activeModifier = Modifier.fillMaxWidth(),
                            inactiveModifier = Modifier.fillMaxWidth(),
                            hoverModifier = defaultHoverModifier.then(Modifier.fillMaxWidth())
                        ) {
                            rows.add(0, it)
                        }
                        rows.forEachIndexed { index, row ->
                            key(row) {
                                Draggable(
                                    row,
                                    onMoved = { rows.remove(row) },
                                    waitForSlop = false
                                ) { dragHandleModifier ->
                                    Column {
                                        row.Draw(dragHandleModifier, state.connected, showConfig)
                                        DropZone(
                                            minimumSize = DpSize(1.dp, 1.dp),
                                            content = { HorizontalDivider() },
                                            activeModifier = Modifier.fillMaxWidth(),
                                            inactiveModifier = Modifier.fillMaxWidth(),
                                            hoverModifier = defaultHoverModifier.then(Modifier.fillMaxWidth())
                                        ) {
                                            rows.add(rows.indexOf(row) + 1, it)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClose(save: Boolean): Boolean {
        state.scope.cancel("Tab closed.")
        return true
    }
}