package com.alchitry.labs.ui.tabs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alchitry.labs.parsers.lucidv2.signals.snapshot.SimParent

class SimulationResultTab(
    private val name: String,
    private val result: SimParent,
    override var parent: TabPanel
) : Tab {
    @Composable
    override fun label() {
        Text("$name Results")
    }

    @Composable
    override fun content() {

        Text("Sim goes here $name")
    }

    override fun onClose(): Boolean {
        return true
    }
}