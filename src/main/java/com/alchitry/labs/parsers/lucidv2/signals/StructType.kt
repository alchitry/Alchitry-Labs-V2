package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class StructType(
    val name: String,
    private val members: MutableMap<String, SignalWidth> = mutableMapOf()
) : MutableMap<String, SignalWidth> by members