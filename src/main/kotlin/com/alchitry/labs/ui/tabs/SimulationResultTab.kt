package com.alchitry.labs.ui.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.ui.components.HSash
import com.alchitry.labs.ui.components.ResizePriority
import com.alchitry.labs.ui.components.rememberSashData
import com.alchitry.labs.ui.waveform.LocalWaveformDragging
import com.alchitry.labs.ui.waveform.LocalWaveformScale
import com.alchitry.labs.ui.waveform.toWaveformParent
import kotlinx.coroutines.launch

class SimulationResultTab(
    private val name: String,
    private val result: SimParent,
    override var parent: TabPanel
) : Tab {
    @Composable
    override fun label() {
        Text("$name Results")
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun content() {
        val waveform = remember { result.toWaveformParent() }
        var firstlayout by remember { mutableStateOf(true) }
        val sashData = rememberSashData(ResizePriority.SECOND)
        val verticalScroll = rememberScrollState()
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            HSash(
                sashData = sashData,
                left = {
                    Surface(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(verticalScroll)
                            .horizontalScroll(rememberScrollState())
                            .onGloballyPositioned {
                                if (firstlayout) {
                                    firstlayout = false
                                    sashData.resizeFirst(it.size.width.toFloat())
                                }
                            }) {
                        waveform.label()
                    }
                },
                right = {
                    val horizontalScroll = rememberScrollState()
                    val scope = rememberCoroutineScope()
                    var cursor by remember { mutableStateOf<Offset?>(null) }
                    var dragging by remember { mutableStateOf(false) }
                    Surface(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(verticalScroll)
                            .horizontalScroll(horizontalScroll)
                            .onPointerEvent(PointerEventType.Move) {
                                cursor = it.changes.first().position
                            }
                            .onPointerEvent(PointerEventType.Exit) { cursor = null }
                            .pointerInput(horizontalScroll, verticalScroll) {
                                detectDragGestures(
                                    onDragStart = {
                                        dragging = true
                                    },
                                    onDragEnd = {
                                        dragging = false
                                    },
                                    onDragCancel = {
                                        dragging = false
                                    }
                                ) { _, dragAmount ->
                                    scope.launch {
                                        horizontalScroll.scrollBy(-dragAmount.x)
                                    }
                                    scope.launch {
                                        verticalScroll.scrollBy(-dragAmount.y)
                                    }
                                }
                            }
                    ) {
                        CompositionLocalProvider(
                            LocalWaveformScale provides 100.dp,
                            LocalWaveformDragging provides dragging
                        ) {
                            Box {
                                waveform.waveform()
                                val lineColor = MaterialTheme.colorScheme.onSurface
                                AnimatedVisibility(
                                    !dragging,
                                    Modifier.matchParentSize(),
                                    enter = fadeIn(spring(stiffness = 3000f)),
                                    exit = fadeOut(spring(stiffness = 3000f))
                                ) {
                                    Canvas(Modifier.matchParentSize()) {
                                        cursor?.let { cursor ->
                                            drawLine(
                                                lineColor,
                                                Offset(cursor.x, 0f),
                                                Offset(cursor.x, size.height)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    override fun onClose(): Boolean {
        return true
    }
}