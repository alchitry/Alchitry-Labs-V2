package com.alchitry.labs2.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.simulation.ProjectSimulator

class BoardSimulationTab(
    override var parent: TabPanel,
    val context: ProjectContext,
    private val simulator: ProjectSimulator = ProjectSimulator(context)
) : Tab {
    @Composable
    override fun label() {
        Text("Simulation")
    }

    @Composable
    override fun content() {
        Surface {
            Box(Modifier.fillMaxSize())
            simulator.contents()
        }
    }

    override fun onClose(save: Boolean): Boolean {
        simulator.close()
        return true
    }

}