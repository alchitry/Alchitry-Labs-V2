package com.alchitry.labs.ui.waveform

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.isIdentity
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimValue
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimValueOrParent
import com.alchitry.labs.parsers.lucidv2.values.SimpleValue
import com.alchitry.labs.parsers.lucidv2.values.Value
import com.alchitry.labs.ui.components.ExpandArrow
import com.alchitry.labs.ui.components.rememberConditionally
import com.alchitry.labs.ui.hiddenClickable
import com.alchitry.labs.ui.theme.AlchitryColors
import kotlinx.coroutines.delay

val LocalWaveformScale = compositionLocalOf { 100.dp }
val LocalWaveformDragging = compositionLocalOf { false }

sealed interface WaveformOrParent {
    val name: String

    @Composable
    fun label()

    @Composable
    fun waveform()
}

private data class DrawablePath(
    val path: Path,
    val color: Color,
    val alpha: Float = 1f,
    val style: DrawStyle
) {
    context(DrawScope)
    fun draw(matrix: Matrix = Matrix()) {
        val drawPath = if (!matrix.isIdentity()) {
            Path().apply {
                addPath(path)
                transform(matrix)
            }
        } else path
        drawPath(drawPath, color = color, alpha = alpha, style = style)
    }
}

data class Waveform(
    override val name: String,
    val values: List<Value>
) : WaveformOrParent {
    private var height by mutableStateOf(100)
    private val paths: List<DrawablePath>

    init {
        var linePath = Path()
        val paths = mutableListOf<DrawablePath>()
        this.paths = paths
        values.forEachIndexed { index, value ->
            if (value is SimpleValue) {
                val x = index.toFloat()
                val max = value.maxValue()
                val min = value.minValue()
                val v = value.toBigInt()
                if (v != null) {
                    val vd = v.toDouble()
                    val y = (vd - min) / (max - min)
                    if (linePath.isEmpty) {
                        linePath.moveTo(x, y.toFloat())
                    } else {
                        linePath.lineTo(x, y.toFloat())
                    }
                    linePath.lineTo(x + 1, y.toFloat())
                } else {
                    if (!linePath.isEmpty) {
                        paths.add(DrawablePath(linePath, AlchitryColors.current.Accent, style = Stroke(2f)))
                        linePath = Path()
                    }

                    paths.add(
                        DrawablePath(
                            Path().apply {
                                moveTo(x, max.toFloat())
                                lineTo(x + 1, max.toFloat())
                                lineTo(x + 1, min.toFloat())
                                lineTo(x, min.toFloat())
                                close()
                            },
                            AlchitryColors.current.Error,
                            alpha = 0.25f,
                            style = Fill
                        )
                    )
                }
            }
        }
        if (!linePath.isEmpty)
            paths.add(DrawablePath(linePath, AlchitryColors.current.Accent, style = Stroke(2f)))
    }

    @Composable
    override fun label() {
        Row(
            Modifier.height(100.dp).onGloballyPositioned { height = it.size.height },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun waveform() {
        var cursor by remember { mutableStateOf<Offset?>(null) }
        var canvasSize by remember { mutableStateOf(Size.Unspecified) }
        val widthScale = LocalWaveformScale.current
        val isDragging = LocalWaveformDragging.current
        with(LocalDensity.current) {
            Box(
                Modifier
                    .width(widthScale * values.size)
                    .height(height.toDp())
                    .padding(vertical = 10.dp)
                    .onPointerEvent(PointerEventType.Move, PointerEventPass.Initial) {
                        if (isDragging && cursor == null) {
                            return@onPointerEvent
                        }
                        val mouse = it.changes.first().position
                        val value = values.getOrNull((mouse.x / widthScale.toPx()).toInt()) ?: return@onPointerEvent

                        if (canvasSize.isUnspecified)
                            return@onPointerEvent

                        if (value is SimpleValue) {
                            val vd = value.toBigInt()?.toDouble() ?: return@onPointerEvent
                            val min = value.minValue()
                            val max = value.maxValue()
                            val normalized = (vd - min) / (max - min)
                            val y = normalized.toFloat() * -canvasSize.height + canvasSize.height
                            cursor = Offset(mouse.x, y)
                        }
                    }
                    .onPointerEvent(PointerEventType.Exit, PointerEventPass.Initial) { cursor = null }
            ) {
                Canvas(Modifier.matchParentSize()) {
                    canvasSize = this.size
                    val matrix = Matrix().apply {
                        translate(0f, size.height)
                        scale(widthScale.toPx(), -size.height, 1f)
                    }
                    paths.forEach { path ->
                        path.draw(matrix)
                    }
                }
                val lastCursor = rememberConditionally(cursor != null) { cursor }

                LaunchedEffect(isDragging) {
                    if (isDragging) {
                        delay(500)
                        cursor = null
                    }
                }

                AnimatedVisibility(
                    !isDragging,
                    modifier = Modifier.offset(
                        (lastCursor?.x?.toDp() ?: 0.dp) - 5.dp,
                        (lastCursor?.y?.toDp() ?: 0.dp) - 5.dp
                    ),
                    enter = fadeIn(spring(stiffness = 3000f)),
                    exit = fadeOut(spring(stiffness = 3000f))
                ) {
                    if (cursor != null)
                        Box(
                            Modifier
                                .size(10.dp)
                                .background(MaterialTheme.colorScheme.onSurface, CircleShape)
                        )
                }
            }
        }
    }
}

data class WaveformParent(
    override val name: String,
    val children: List<WaveformOrParent>
) : WaveformOrParent {
    private var expanded by mutableStateOf(true)
    private var height by mutableStateOf(0)

    @Composable
    override fun label() {
        val expandTransition = updateTransition(expanded, label = "expand")
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .hiddenClickable {
                        expanded = !expanded
                    }
                    .onGloballyPositioned { height = it.size.height }
                    .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
            ) {
                ExpandArrow(expandTransition)
                Text(name)
            }
            AnimatedVisibility(expanded) {
                Column(Modifier.padding(start = 25.dp)) {
                    children.forEach {
                        key(it) {
                            it.label()
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun waveform() {
        Column {
            with(LocalDensity.current) {
                Box(Modifier.height(height.toDp()))
            }
            AnimatedVisibility(expanded) {
                Column {
                    children.forEach { waveformOrParent ->
                        key(waveformOrParent) {
                            Row { waveformOrParent.waveform() }
                        }
                    }
                }
            }
        }
    }
}

fun SimParent.toWaveformParent(): WaveformParent =
    WaveformParent(name, children.map { it.toWaveformOrParent() })

fun SimValue.toWaveform(): Waveform =
    Waveform(name, values)

fun SimValueOrParent.toWaveformOrParent(): WaveformOrParent =
    when (this) {
        is SimParent -> toWaveformParent()
        is SimValue -> toWaveform()
    }