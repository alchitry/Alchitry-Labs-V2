package com.alchitry.labs2.ui.register_interface

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.painterResource
import com.alchitry.labs2.ui.components.ToggleButton
import com.alchitry.labs2.ui.graphing.RealtimeGraph
import com.alchitry.labs2.ui.graphing.RealtimeGraphState
import kotlinx.coroutines.channels.Channel
import java.awt.Cursor
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.DurationUnit

class RegisterRow(
    val address: Int,
    val parent: RegisterInterface,
    private val requestRemoval: (RegisterRow) -> Unit
) {
    var running by mutableStateOf(false)
    var watching by mutableStateOf(false)
    var valueState by mutableStateOf(
        NumberFieldState(
            value = 0,
            fractionalBits = 0,
            signed = false,
            radix = Radix.Decimal,
            valid = true
        )
    )
    var showGraph by mutableStateOf(false)
    val graphValues = RealtimeGraphState()
    val requests = Channel<RegisterRequest>(capacity = 10)

    fun onNewData(data: Int, time: Duration) {
        graphValues.add(
            data,
            time.toDouble(DurationUnit.SECONDS)
        )
        valueState = valueState.withNewValue(data)
    }

    fun resetData() {
        graphValues.clear()
    }

    @Composable
    fun Draw(dragHandleModifier: Modifier, connected: Boolean, showConfig: Boolean) {
        val dragHandleWidth = 55.dp
        Box(Modifier.background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))) {
            Box(Modifier.matchParentSize()) {
                Box(
                    modifier = dragHandleModifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .width(dragHandleWidth)
                        .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)))
                        .alpha(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource("icons/drag_indicator.svg"),
                        "Drag",
                        Modifier.size(25.dp)
                    )
                }
            }

            Column(Modifier.padding(start = dragHandleWidth)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        Modifier.padding(vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.defaultMinSize(minWidth = 275.dp)
                        ) {
                            Text("Address: ")
                            Text(address.toUInt().toString())
                            Text(" 0x${address.toUInt().toHexString(HexFormat.UpperCase)}", Modifier.alpha(0.5f))
                        }

                        NumberField(
                            valueState,
                            "Value",
                            signSelector = true,
                            fractionalBits = true,
                            readOnly = running,
                            showConfig = showConfig
                        ) { valueState = it }

                        Button({
                            running = true
                            if (requests.trySend(RegisterRequest.Read(address) { value ->
                                    if (value != null)
                                        valueState = valueState.withNewValue(value)
                                    running = false
                                }).isFailure) {
                                running = false
                            }

                        }, enabled = !running && connected) {
                            Text("Read")
                        }
                        Button({
                            running = true
                            if (requests.trySend(RegisterRequest.Write(address, valueState.value) {
                                    running = false
                                }).isFailure
                            ) {
                                running = false
                            }
                        }, enabled = !running && connected) {
                            Text("Write")
                        }

                        ToggleButton(
                            active = watching,
                            enabled = (!running || watching),
                            tooltip = { Text("Watch Register") },
                            onClick = {
                                watching = it
                                if (it) {
                                    parent.collectingValues = true
                                }
                            }
                        ) {
                            Icon(
                                painterResource("icons/glasses.svg"),
                                contentDescription = "Watch",
                                modifier = Modifier.size(width = 70.dp, height = 45.dp)
                            )
                        }


                        ToggleButton(
                            active = showGraph,
                            onClick = {
                                showGraph = it
                            },
                            tooltip = { Text("Show Graph") }
                        ) {
                            Icon(
                                painterResource("icons/chart.svg"),
                                contentDescription = "Show Graph",
                                modifier = Modifier.size(width = 70.dp, height = 45.dp)
                            )
                        }
                    }

                    Spacer(Modifier.weight(1f).pointerInput(Unit) {

                    })
                    Box(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(35.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = { requestRemoval(this@RegisterRow) },
                                role = Role.Button,
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource("icons/close.svg"),
                            "Close",
                            modifier = Modifier.matchParentSize().padding(4.dp)
                        )
                    }
                }
                AnimatedVisibility(
                    showGraph,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    RealtimeGraph(graphValues, link = parent.graphLinkState, valueFormatter = {
                        buildString {
                            if (valueState.radix != Radix.Decimal) {
                                append(valueState.radix.prefix)
                            }
                            if (valueState.signed)
                                append(it.y.roundToInt().toString(valueState.radix.radix).uppercase())
                            else
                                append(it.y.roundToInt().toUInt().toString(valueState.radix.radix).uppercase())
                        }
                    })
                }
            }
        }
    }
}