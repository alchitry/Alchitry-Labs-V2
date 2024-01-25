package com.alchitry.labs2.simulation

import com.alchitry.labs2.Log
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.types.ModuleInstance
import com.alchitry.labs2.parsers.lucidv2.types.Signal
import com.alchitry.labs2.parsers.lucidv2.types.SignalDirection
import com.alchitry.labs2.parsers.lucidv2.values.Bit
import com.alchitry.labs2.parsers.lucidv2.values.BitListValue
import com.alchitry.labs2.parsers.lucidv2.values.SimpleValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.Closeable
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

private data class SampleAccumulator(var runningSum: Long, var count: Long, var invalid: Boolean = false) {
    fun reset() {
        runningSum = 0
        count = 0
        invalid = false
    }
}

class ProjectSimulator(val projectContext: ProjectContext) : Closeable {
    private val resetValueFlow = MutableStateFlow(false)
    private var ledAccumulators = List(8) { SampleAccumulator(0, 0) }
    private val accumulatorLock = Mutex()
    private val mutableEvalRateFlow = MutableStateFlow(0f)
    val evalRateFlow = mutableEvalRateFlow.asStateFlow()

    suspend fun start() {
        require(projectContext.scope.isActive) { "The simulator has been closed!" }
        withContext(Dispatchers.Default) {


            val leds = Signal(
                "led",
                SignalDirection.Read,
                null,
                BitListValue(0, 8, constant = false, signed = false),
                false
            )

            val reset = Signal(
                "rst_n",
                SignalDirection.Write,
                null,
                BitListValue(1, 1, false, false),
                false
            )

            val clk = Signal(
                "clk",
                SignalDirection.Write,
                null,
                BitListValue(0, 1, false, false),
                false
            )

            val top = ModuleInstance(
                "auSimulator",
                projectContext,
                null,
                projectContext.top?.module ?: error("Missing top module!"),
                mapOf(),
                mapOf(
                    "led" to leds,
                    "rst_n" to reset,
                    "clk" to clk
                )
            )

            accumulatorLock.withLock {
                ledAccumulators.forEach { it.reset() }
            }

            val low = Bit.B0.toBitValue(constant = false, signed = false)
            val high = Bit.B1.toBitValue(constant = false, signed = false)

            clk.write(low)
            reset.write(high)

            top.initialWalk()
            top.context.initialize()

            var sampleCount = 0
            var lastRate = TimeSource.Monotonic.markNow()

            while (isActive) {
                clk.write(high)
                reset.write(if (resetValueFlow.value) low else high)

                projectContext.processQueue()
                accumulatorLock.withLock {
                    (leds.read() as SimpleValue).bits.forEachIndexed { index, bit ->
                        val accumulator = ledAccumulators[index]
                        accumulator.runningSum += when (bit) {
                            Bit.B0 -> 0
                            Bit.B1 -> 1
                            Bit.Bx -> {
                                accumulator.invalid = true
                                0
                            }

                            Bit.Bz -> 0
                        }
                        accumulator.count += 1
                    }
                }

                clk.write(low)
                reset.write(if (resetValueFlow.value) low else high)
                projectContext.processQueue()
                sampleCount += 1
                val elapsed = lastRate.elapsedNow()
                if (elapsed > 1.seconds) {
                    val hz = sampleCount.toFloat() / elapsed.toDouble(DurationUnit.SECONDS).toFloat()
                    mutableEvalRateFlow.tryEmit(hz)
                    Log.info("Running at ${hz.roundToInt()} Hz")

                    lastRate = TimeSource.Monotonic.markNow()
                    sampleCount = 0
                }
            }
        }
    }

    fun setResetButton(pressed: Boolean) {
        resetValueFlow.tryEmit(pressed)
    }

    suspend fun getLedValues(): List<Float> {
        return accumulatorLock.withLock {
            ledAccumulators.map { accumulator ->
                when {
                    accumulator.invalid -> Float.NaN
                    accumulator.count == 0L -> 0f
                    else -> accumulator.runningSum.toFloat() / accumulator.count.toFloat()
                }.also { accumulator.reset() }
            }
        }
    }

    override fun close() {
        projectContext.close()
    }
}