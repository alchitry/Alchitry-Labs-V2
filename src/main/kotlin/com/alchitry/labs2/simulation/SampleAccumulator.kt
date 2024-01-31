package com.alchitry.labs2.simulation

data class SampleAccumulator(var runningSum: Long, var count: Long, var invalid: Boolean = false) {
    fun reset() {
        runningSum = 0
        count = 0
        invalid = false
    }
}