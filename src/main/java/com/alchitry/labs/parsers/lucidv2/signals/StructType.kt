package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

data class StructType(
    val name: String,
    private val members: Map<String, StructMember>
) : Map<String, StructMember> by members

data class StructMember(val name: String, val width: SignalWidth, val signed: Boolean)