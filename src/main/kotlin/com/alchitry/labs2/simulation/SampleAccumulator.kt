package com.alchitry.labs2.simulation

import com.alchitry.labs2.parsers.hdl.values.Bit

data class SampleAccumulator(
    private var runningSum: Long,
    private var count: Long,
    private var invalid: Boolean = false
) {
    fun reset() {
        runningSum = 0
        count = 0
        invalid = false
    }

    fun collectRunningAverage(): Float? = when {
        invalid -> Float.NaN
        count == 0L -> null
        else -> runningSum.toFloat() / count.toFloat()
    }.also { reset() }

    operator fun plusAssign(bit: Bit) {
        count += 1
        runningSum += when (bit) {
            Bit.B0 -> 0
            Bit.B1 -> 1
            Bit.Bx -> {
                invalid = true
                0
            }

            Bit.Bz -> 0
        }
    }
}