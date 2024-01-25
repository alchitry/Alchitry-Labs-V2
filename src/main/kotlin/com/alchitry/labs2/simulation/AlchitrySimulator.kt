package com.alchitry.labs2.simulation

import com.alchitry.labs2.parsers.ProjectContext
import com.alchitry.labs2.parsers.lucidv2.types.Module
import com.alchitry.labs2.parsers.lucidv2.types.Signal

@Suppress("DataClassPrivateConstructor")
data class AlchitrySimulator private constructor(
    val clk: Signal,
    val rstN: Signal,
    val led: Signal
) {

    companion object {
        fun connect(module: Module, projectContext: ProjectContext): AlchitrySimulator? {
            return null
        }
    }
}