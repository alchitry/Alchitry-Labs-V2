package com.alchitry.labs2.simulation

import androidx.compose.runtime.*
import com.alchitry.labs2.hardware.pinout.AuPin
import com.alchitry.labs2.hardware.pinout.AuV2Pin
import com.alchitry.labs2.hardware.pinout.ConverterVersion
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.types.SignalOrSubSignal
import com.alchitry.labs2.parsers.hdl.values.Bit
import com.alchitry.labs2.parsers.hdl.values.BitValue
import com.alchitry.labs2.ui.simulation.IoBoard
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.pow

@Suppress("DataClassPrivateConstructor")
data class IoSimulator private constructor(
    val leds: List<SignalOrSubSignal>,
    val dips: List<SignalOrSubSignal>,
    val buttons: List<SignalOrSubSignal>,
    val segments: List<SignalOrSubSignal>,
    val selects: List<SignalOrSubSignal>,
    private val version: Version
) {
    private val buttonValues = List(5) { mutableStateOf(false) }
    private val dipValues = List(24) { mutableStateOf(false) }

    private var ledAccumulators = List(24) { SampleAccumulator(0, 0) }
    private var segmentAccumulators = List(4) { List(8) { SampleAccumulator(0, 0) } }
    private val accumulatorLock = Mutex()

    suspend fun initialize(clk: SignalOrSubSignal) {
        accumulatorLock.withLock {
            ledAccumulators.forEach { it.reset() }
            segmentAccumulators.forEach { it.forEach { a -> a.reset() } }
        }

        buttons.forEachIndexed { idx, btn ->
            btn.write(if (buttonValues[idx].value) ProjectSimulator.high else ProjectSimulator.low)
        }
        dips.forEachIndexed { idx, dip ->
            dip.write(if (dipValues[idx].value) ProjectSimulator.high else ProjectSimulator.low)
        }

        clk.addDependant {
            onClkChange(clk.read().isTrue().bit == Bit.B1)
        }
    }

    private suspend fun onClkChange(rising: Boolean) {
        dips.forEachIndexed { i, dip ->
            dip.write(if (dipValues[i].value) ProjectSimulator.high else ProjectSimulator.low)
        }
        buttons.forEachIndexed { i, button ->
            button.write(if (buttonValues[i].value) ProjectSimulator.high else ProjectSimulator.low)
        }
        if (rising)
            accumulatorLock.withLock {
                leds.forEachIndexed { index, led ->
                    ledAccumulators[index] += (led.read() as BitValue).bit
                }
                selects.forEachIndexed { digit, select ->
                    segments.forEachIndexed { index, segment ->
                        val selectBit = (select.read() as BitValue).bit
                        segmentAccumulators[digit][index] += when (selectBit) {
                            Bit.B1, Bit.Bz -> Bit.B0
                            Bit.B0 -> when ((segment.read() as BitValue).bit) {
                                Bit.B0 -> Bit.B1
                                Bit.B1, Bit.Bz -> Bit.Bz
                                Bit.Bx -> Bit.Bx
                            }

                            Bit.Bx -> Bit.Bx
                        }
                    }
                }
            }
    }

    private var ledStates by mutableStateOf(List(24) { 0f })
    private var segmentStates by mutableStateOf(List(4) { List(8) { 0f } })
    private suspend fun updateLedValues() {
        return accumulatorLock.withLock {
            ledStates = ledAccumulators.mapIndexed { index, accumulator ->
                accumulator.collectRunningAverage() ?: ledStates[index]
            }
            segmentStates = segmentAccumulators.mapIndexed { j, digits ->
                digits.mapIndexed { i, accumulator ->
                    accumulator.collectRunningAverage()?.pow(0.3f) ?: segmentStates[j][i]
                }
            }
        }
    }

    @Composable
    fun contents() {
        LaunchedEffect(Unit) {
            while (isActive) {
                delay(1000 / 30)
                updateLedValues()
            }
        }

        when (version) {
            Version.V1 ->
                IoBoard(
                    leds = { ledStates[it] },
                    digits = { digit, segment -> segmentStates[digit][segment] },
                    onButtonChange = { idx, clicked -> buttonValues[idx].value = clicked },
                    onSwitchChange = { idx -> dipValues[idx].value = !dipValues[idx].value },
                    switchState = { idx -> dipValues[idx].value }
                )

            Version.V2 ->
                IoBoard( // TODO: make V2
                    leds = { ledStates[it] },
                    digits = { digit, segment -> segmentStates[digit][segment] },
                    onButtonChange = { idx, clicked -> buttonValues[idx].value = clicked },
                    onSwitchChange = { idx -> dipValues[idx].value = !dipValues[idx].value },
                    switchState = { idx -> dipValues[idx].value }
                )
        }
    }

    companion object {
        private enum class Version { V1, V2 }

        fun connectV1(projectContext: ProjectContext): IoSimulator? {
            val constraints = projectContext.getConstraints(ConverterVersion.V1)
            val leds = listOf(
                AuPin.B21.name,
                AuPin.B20.name,
                AuPin.B18.name,
                AuPin.B17.name,
                AuPin.B15.name,
                AuPin.B14.name,
                AuPin.B12.name,
                AuPin.B11.name,
                AuPin.B9.name,
                AuPin.B8.name,
                AuPin.B6.name,
                AuPin.B5.name,
                AuPin.B3.name,
                AuPin.B2.name,
                AuPin.A24.name,
                AuPin.A23.name,
                AuPin.A21.name,
                AuPin.A20.name,
                AuPin.A18.name,
                AuPin.A17.name,
                AuPin.A15.name,
                AuPin.A14.name,
                AuPin.A12.name,
                AuPin.A11.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val dips = listOf(
                AuPin.B30.name,
                AuPin.B31.name,
                AuPin.B33.name,
                AuPin.B34.name,
                AuPin.B36.name,
                AuPin.B37.name,
                AuPin.B39.name,
                AuPin.B40.name,
                AuPin.B42.name,
                AuPin.B43.name,
                AuPin.B45.name,
                AuPin.B46.name,
                AuPin.B48.name,
                AuPin.B49.name,
                AuPin.A27.name,
                AuPin.A28.name,
                AuPin.A30.name,
                AuPin.A31.name,
                AuPin.A33.name,
                AuPin.A34.name,
                AuPin.A36.name,
                AuPin.A37.name,
                AuPin.A39.name,
                AuPin.A40.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val select = listOf(
                AuPin.A9.name,
                AuPin.A8.name,
                AuPin.A42.name,
                AuPin.A43.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val segments = listOf(
                AuPin.A5.name,
                AuPin.A6.name,
                AuPin.A48.name,
                AuPin.A46.name,
                AuPin.A45.name,
                AuPin.A3.name,
                AuPin.A2.name,
                AuPin.A49.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val buttons = listOf(
                AuPin.B28.name,
                AuPin.B27.name,
                AuPin.B23.name,
                AuPin.B24.name,
                AuPin.C49.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            return IoSimulator(
                leds = leds,
                dips = dips,
                buttons = buttons,
                segments = segments,
                selects = select,
                version = Version.V1
            )
        }

        fun connectV2(projectContext: ProjectContext): IoSimulator? {
            val constraints = projectContext.getConstraints(ConverterVersion.V2)
            val leds = listOf(
                AuV2Pin.A70.name,
                AuV2Pin.A72.name,
                AuV2Pin.A76.name,
                AuV2Pin.A78.name,
                AuV2Pin.A77.name,
                AuV2Pin.A75.name,
                AuV2Pin.A71.name,
                AuV2Pin.A69.name,
                AuV2Pin.A65.name,
                AuV2Pin.A63.name,
                AuV2Pin.A59.name,
                AuV2Pin.A57.name,
                AuV2Pin.A53.name,
                AuV2Pin.A51.name,
                AuV2Pin.A47.name,
                AuV2Pin.A45.name,
                AuV2Pin.A41.name,
                AuV2Pin.A39.name,
                AuV2Pin.A35.name,
                AuV2Pin.A33.name,
                AuV2Pin.A29.name,
                AuV2Pin.A27.name,
                AuV2Pin.A30.name,
                AuV2Pin.A34.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val dips = listOf(
                AuV2Pin.A66.name,
                AuV2Pin.A64.name,
                AuV2Pin.A60.name,
                AuV2Pin.A58.name,
                AuV2Pin.A54.name,
                AuV2Pin.A52.name,
                AuV2Pin.A48.name,
                AuV2Pin.A46.name,
                AuV2Pin.A42.name,
                AuV2Pin.A40.name,
                AuV2Pin.A36.name,
                AuV2Pin.B18.name,
                AuV2Pin.B16.name,
                AuV2Pin.B12.name,
                AuV2Pin.B10.name,
                AuV2Pin.B6.name,
                AuV2Pin.B4.name,
                AuV2Pin.B3.name,
                AuV2Pin.B5.name,
                AuV2Pin.B9.name,
                AuV2Pin.B11.name,
                AuV2Pin.B15.name,
                AuV2Pin.B17.name,
                AuV2Pin.B21.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val select = listOf(
                AuV2Pin.A4.name,
                AuV2Pin.A6.name,
                AuV2Pin.A12.name,
                AuV2Pin.A10.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val segments = listOf(
                AuV2Pin.A9.name,
                AuV2Pin.A3.name,
                AuV2Pin.A21.name,
                AuV2Pin.A15.name,
                AuV2Pin.A11.name,
                AuV2Pin.A5.name,
                AuV2Pin.A23.name,
                AuV2Pin.A17.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            val buttons = listOf(
                AuV2Pin.A24.name,
                AuV2Pin.A22.name,
                AuV2Pin.A18.name,
                AuV2Pin.A16.name,
                AuV2Pin.A28.name,
            ).map { name ->
                constraints.firstOrNull { it.pin.name == name }?.port ?: return null
            }
            return IoSimulator(
                leds = leds,
                dips = dips,
                buttons = buttons,
                segments = segments,
                selects = select,
                version = Version.V2
            )
        }
    }
}