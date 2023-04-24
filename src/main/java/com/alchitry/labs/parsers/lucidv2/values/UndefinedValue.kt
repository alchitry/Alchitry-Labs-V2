package com.alchitry.labs.parsers.lucidv2.values

data class UndefinedValue(
    override val constant: Boolean,
    val width: SignalWidth = UndefinedSimpleWidth
) : Value()
