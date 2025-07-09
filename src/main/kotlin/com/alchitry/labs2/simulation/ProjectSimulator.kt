package com.alchitry.labs2.simulation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.Log
import com.alchitry.labs2.hardware.pinout.AuPin
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.acf.types.Constraint
import com.alchitry.labs2.parsers.hdl.types.ModuleInstance
import com.alchitry.labs2.parsers.hdl.types.SignalOrSubSignal
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.ui.components.ToolbarButton
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.io.Closeable
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.TimeSource
import kotlin.time.measureTime

class ProjectSimulator(private val projectContext: ProjectContext) : Closeable {
    private val mutableEvalRateFlow = MutableStateFlow(0.0)
    val evalRateFlow = mutableEvalRateFlow.asStateFlow()

    companion object {
        val low = Bit.B0.toBitValue(signed = false)
        val high = Bit.B1.toBitValue(signed = false)
    }

    private val clk: SignalOrSubSignal
    private val alchitrySimulator: AlchitrySimulator
    private val ioSimulator: IoSimulator?
    private val top: ModuleInstance
    private val initialized = MutableStateFlow(false)
    private val targetRateFlow = MutableStateFlow(1000L)
    private var running by mutableStateOf(false)

    init {
        check(projectContext.simulating) { "Project context passed to ProjectSimulator wasn't marked as \"simulating\"." }
        val constraints = projectContext.getConstraints().filterIsInstance<Constraint.PinConstraint>()
        val clkConstraint = constraints.firstOrNull { it.pin.name == AuPin.CLOCK.name }

        if (clkConstraint == null) {
            error("Design is missing a signal connected to the clock pin.")
        }

        clk = clkConstraint.port

        alchitrySimulator = AlchitrySimulator.connect(projectContext)
        alchitrySimulator.reportMissingSignals()

        ioSimulator = IoSimulator.connectV2(projectContext) ?: IoSimulator.connectV1(projectContext)

        top = projectContext.top ?: error("Project is missing top module!")

        projectContext.scope.launch {
            reset()
            initialized.tryEmit(true)
        }
    }

    private suspend fun reset() {
        clk.write(low)
        alchitrySimulator.initialize(clk)
        ioSimulator?.initialize(clk)
        top.context.initialize()
    }

    private suspend fun run() {
        require(projectContext.scope.isActive) { "The simulator has been closed!" }
        withContext(Dispatchers.Default) {
            initialized.first { it } // wait for initialization
            var sampleCount = 0
            var lastRate = TimeSource.Monotonic.markNow()
            var duration: Duration = (1.0 / targetRateFlow.value.toDouble()).seconds
            var stepsPerDelay = (targetRateFlow.value.toDouble() / 100.0).roundToInt().coerceAtLeast(1)
            mutableEvalRateFlow.tryEmit(targetRateFlow.value.toDouble())

            try {
                while (isActive) {
                    val elapsedTime = measureTime {
                        repeat(stepsPerDelay) {
                            clk.write(high)
                            projectContext.processQueue()
                            clk.write(low)
                            projectContext.processQueue()

                            sampleCount += 1
                            val elapsed = lastRate.elapsedNow()
                            if (elapsed > 1.seconds) {
                                val hz = sampleCount / elapsed.toDouble(DurationUnit.SECONDS)
                                mutableEvalRateFlow.tryEmit(hz)

                                lastRate = TimeSource.Monotonic.markNow()
                                sampleCount = 0
                            }
                        }
                        delay(duration * stepsPerDelay)
                    }
                    val currentTarget = targetRateFlow.value.toDouble()
                    val targetDuration = (stepsPerDelay.toDouble() / currentTarget).seconds
                    val error = (targetDuration - elapsedTime) / stepsPerDelay
                    duration = (duration + error).coerceAtLeast(Duration.ZERO)
                    stepsPerDelay = (currentTarget / 100.0).roundToInt().coerceAtLeast(1)
                }
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                Log.error("Simulation failed: ${e.message}")
            }
        }
    }

    @Composable
    private fun ControllerBar() {
        var runningJob: Job? by remember { mutableStateOf(null) }
        val scope = rememberCoroutineScope()

        DisposableEffect(Unit) {
            running = true
            runningJob = scope.launch {
                run()
            }
            onDispose { running = false }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp))
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val targetRate by targetRateFlow.collectAsState()
            var rateText by remember { mutableStateOf(targetRate.toString()) }
            val rateCorrect = rateText == targetRate.toString()

            Crossfade(runningJob != null) {
                if (it) {
                    ToolbarButton(painterResource("icons/pause.svg"), "Pause", size = 44.dp) {
                        scope.launch {
                            runningJob?.cancelAndJoin()
                            runningJob = null
                            running = false
                        }
                    }
                } else {
                    ToolbarButton(painterResource("icons/play.svg"), "Start", size = 44.dp, enabled = rateCorrect) {
                        val lastJob = runningJob
                        running = true
                        runningJob = scope.launch {
                            lastJob?.cancelAndJoin()
                            run()
                        }
                    }
                }
            }

            ToolbarButton(painterResource("icons/replay.svg"), "Restart", size = 44.dp, enabled = rateCorrect) {
                val lastJob = runningJob
                runningJob = scope.launch {
                    running = true
                    lastJob?.cancelAndJoin()
                    reset()
                    run()
                }
            }

            TextField(
                value = rateText,
                onValueChange = {
                    rateText = it
                    it.toLongOrNull()?.coerceAtLeast(1)?.let { d -> targetRateFlow.tryEmit(d) }
                },
                label = { Text("Target Rate") },
                suffix = { Text("Hz") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                enabled = runningJob == null,
                isError = !rateCorrect
            )
            Spacer(Modifier.weight(1f))
            if (runningJob != null) {
                val evalRate by evalRateFlow.collectAsState()
                val evalError = evalRate / targetRate
                val color = when {
                    evalError < 0.75 -> AlchitryColors.current.Error
                    evalError < 0.9 -> AlchitryColors.current.Warning
                    else -> Color.Unspecified
                }
                Text("Running at ${evalRate.roundToInt()} Hz", color = color)
            }
        }
    }

    @Composable
    fun contents() {
        Column {
            ControllerBar()
            Box {
                Box(
                    Modifier.padding(10.dp).shadow(
                        2.dp, GenericShape { size, _ ->
                            val scale = size.width / 65f

                            moveTo(1.5f * scale, 0f * scale)
                            lineTo(63.5f * scale, 0f * scale)
                            lineTo(65f * scale, 1.5f * scale)
                            lineTo(65f * scale, 43.5f * scale)
                            lineTo(63.5f * scale, 45f * scale)
                            lineTo(1.5f * scale, 45f * scale)
                            lineTo(0f * scale, 43.5f * scale)
                            close()

                        },
                        clip = false
                    )
                ) {
                    alchitrySimulator.contents()
                    ioSimulator?.contents()
                }
                if (!running) {
                    Box(
                        Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Paused", style = MaterialTheme.typography.headlineLarge)
                    }
                }
            }
        }

    }

    override fun close() {
        projectContext.close()
    }
}