package com.alchitry.labs2.simulation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alchitry.labs2.hardware.pinout.AuPin
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.SignalOrSubSignal
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.ui.components.ToolbarButton
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
        val low = Bit.B0.toBitValue(constant = false, signed = false)
        val high = Bit.B1.toBitValue(constant = false, signed = false)
    }

    private val clk: SignalOrSubSignal
    private val alchitrySimulator: AlchitrySimulator
    private val top: ModuleInstance
    private val initialized = MutableStateFlow(false)
    private val targetRateFlow = MutableStateFlow(1000.0)

    init {
        val constraints = projectContext.getConstraints()
        val clkConstraint = constraints.firstOrNull { it.pin.name == AuPin.CLOCK.name }

        if (clkConstraint == null) {
            error("Design is missing a signal connected to the clock pin.")
        }

        clk = clkConstraint.port

        alchitrySimulator = AlchitrySimulator.connect(projectContext)
        alchitrySimulator.reportMissingSignals()

        top = projectContext.top ?: error("Project is missing top module!")

        projectContext.scope.launch {
            clk.write(low)
            alchitrySimulator.initialize(clk)
            top.context.initialize()
            initialized.tryEmit(true)
        }
    }

    private suspend fun run() {
        require(projectContext.scope.isActive) { "The simulator has been closed!" }
        withContext(Dispatchers.Default) {
            initialized.first { true } // wait for initialization
            var sampleCount = 0
            var lastRate = TimeSource.Monotonic.markNow()
            var duration = (1.0 / targetRateFlow.value.coerceAtLeast(1.0)).seconds
            var stepsPerDelay = (targetRateFlow.value / 100).roundToInt().coerceAtLeast(1)

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
                val currentTarget = targetRateFlow.value
                val targetDuration = (stepsPerDelay.toDouble() / currentTarget.coerceAtLeast(1.0)).seconds
                val error = (targetDuration - elapsedTime) / stepsPerDelay
                duration = (duration + error).coerceAtLeast(Duration.ZERO)
                stepsPerDelay = (currentTarget / 100).roundToInt().coerceAtLeast(1)
            }
        }
    }

    @Composable
    fun contents() {
        LaunchedEffect(Unit) {
            run()
        }
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(20.dp))
                    .padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ToolbarButton(painterResource("icons/play.svg"), "Start") {

                }
                val targetRate by targetRateFlow.collectAsState()
                TextField(
                    value = targetRate.toString(),
                    onValueChange = {
                        it.toDoubleOrNull()?.let { d -> targetRateFlow.tryEmit(d) }
                    },
                    label = { Text("Target Rate") },
                    suffix = { Text("Hz") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.weight(1f))
                Text("Running at ${evalRateFlow.collectAsState().value.roundToInt()} Hz")
            }
            alchitrySimulator.contents()
        }

    }

    override fun close() {
        projectContext.close()
    }
}