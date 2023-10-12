package com.alchitry.labs.parsers.acf.types

data class ClockConstraint(
    val pinConstraint: PinConstraint,
    val frequency: Int
)
