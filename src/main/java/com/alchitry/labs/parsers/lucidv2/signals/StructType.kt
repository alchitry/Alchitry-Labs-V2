package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class StructType(
    val name: String,
    private val members: Map<String, SignalWidth>
) : Map<String, SignalWidth> by members