package com.alchitry.labs2.simulation

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.hdl.types.Signal

interface SimulatorInitializer {
    /**
     * Generates a list of signals for this [Simulator] to connect to the given module.
     *
     * @param module The module for which signals need to be generated.
     * @param projectContext The project context in which the module exists.
     *
     * @return The list of signals to connect to the given module.
     */
    fun connect(module: Module, projectContext: ProjectContext): List<Signal>
}

interface Simulator {
    /**
     * Snapshots the relevant data during a simulation. Typically called on each rising edge of the clock.
     */
    fun snapshot()
}