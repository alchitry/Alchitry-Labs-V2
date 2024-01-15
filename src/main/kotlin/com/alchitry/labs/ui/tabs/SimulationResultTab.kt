package com.alchitry.labs.ui.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
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
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.ui.components.HSash
import com.alchitry.labs.ui.components.ResizePriority
import com.alchitry.labs.ui.components.rememberSashData
import com.alchitry.labs.ui.waveform.LocalCursorPosition
import com.alchitry.labs.ui.waveform.LocalWaveformDragging
import com.alchitry.labs.ui.waveform.LocalWaveformScale
import com.alchitry.labs.ui.waveform.toWaveformParent
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
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
        var waveScale by remember { mutableStateOf(50.dp) }

        var dragging by remember { mutableStateOf(false) }
        val tracker = remember { VelocityTracker() }
        var flingJob by remember { mutableStateOf<Job?>(null) }

        fun stopFling() {
            tracker.resetTracking()
            flingJob?.cancel()
            dragging = false
        }

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
                            .onPointerEvent(PointerEventType.Scroll) {
                                stopFling()
                            }
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

                    val decay = rememberSplineBasedDecay<Offset>()
                    Surface(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(verticalScroll, enabled = false)
                            .horizontalScroll(horizontalScroll)
                            .onPointerEvent(PointerEventType.Scroll) {
                                val scroll = it.changes.fold(Offset.Zero) { acc, c -> acc + c.scrollDelta }.y
                                if (scroll != 0f) {
                                    scope.launch {
                                        val lastScale = waveScale
                                        val newScale = (waveScale * (1f - scroll / 50)).coerceIn(5.dp, 200.dp)
                                        val cursorX = cursor?.x ?: return@launch
                                        val oldIdx = cursorX / lastScale.toPx()
                                        val scrollChange = cursorX / lastScale.toPx() * newScale.toPx() - cursorX
                                        horizontalScroll.scrollBy(scrollChange)
                                        waveScale = newScale
                                        cursor = cursor?.copy(x = oldIdx * newScale.toPx())
                                    }
                                }
                            }
                            .onPointerEvent(PointerEventType.Move) {
                                cursor = it.changes.first().position
                            }
                            .onPointerEvent(PointerEventType.Exit) { cursor = null }
                            .onPointerEvent(PointerEventType.Press) { stopFling() }
                            .pointerInput(horizontalScroll, verticalScroll) {
                                detectDragGestures(
                                    onDragStart = {
                                        stopFling()
                                        dragging = true
                                    },
                                    onDragEnd = {
                                        val oldJob = flingJob
                                        flingJob = scope.launch {
                                            oldJob?.cancelAndJoin()
                                            val velocity = tracker.calculateVelocity()
                                            tracker.resetTracking()
                                            AnimationState(
                                                typeConverter = Offset.VectorConverter,
                                                initialValue = Offset(
                                                    horizontalScroll.value.toFloat(),
                                                    verticalScroll.value.toFloat()
                                                ),
                                                initialVelocity = Offset(-velocity.x, -velocity.y)
                                            ).animateDecay(decay) {

                                                launch {
                                                    horizontalScroll.scrollTo(value.x.toInt())
                                                }
                                                launch {
                                                    verticalScroll.scrollTo(value.y.toInt())
                                                }
                                            }
                                            dragging = false
                                        }
                                    },
                                    onDragCancel = {
                                        dragging = false
                                        tracker.resetTracking()
                                    }
                                ) { event, dragAmount ->
                                    tracker.addPointerInputChange(event)
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
                            LocalWaveformScale provides waveScale,
                            LocalWaveformDragging provides dragging,
                            LocalCursorPosition provides cursor
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