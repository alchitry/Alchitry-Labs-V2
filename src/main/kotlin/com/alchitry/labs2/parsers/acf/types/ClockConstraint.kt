package com.alchitry.labs2.parsers.acf.types

data class ClockConstraint(
    val pinConstraint: PinConstraint,
    val frequency: Int
)
