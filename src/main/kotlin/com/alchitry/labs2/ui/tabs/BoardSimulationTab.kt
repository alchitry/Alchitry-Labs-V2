package com.alchitry.labs2.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.project.Board
import com.alchitry.labs2.simulation.AuSimulator
import com.alchitry.labs2.ui.simulation.AlchitryBoard
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class BoardSimulationTab(
    override var parent: TabPanel,
    val context: ProjectContext
) : Tab {
    private val simulator = AuSimulator(context)

    @Composable
    override fun label() {
        Text("Simulation")
    }

    @Composable
    override fun content() {
        Surface {
            Box(Modifier.fillMaxSize())
            var leds by remember { mutableStateOf(List(8) { 0f }) }

            LaunchedEffect(Unit) {
                simulator.start()
            }

            LaunchedEffect(Unit) {
                while (isActive) {
                    delay(1000 / 30)
                    leds = simulator.getLedValues()
                }
            }

            AlchitryBoard(Board.AlchitryAu, leds, 1.0f) {
                simulator.setResetButton(it)
            }
        }
    }

    override fun onClose(save: Boolean): Boolean {
        simulator.close()
        return true
    }

}