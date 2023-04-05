package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.signals

data class SignalSelection(private val selection: List<SignalSelector>): List<SignalSelector> by selection

sealed class SignalSelector {
    data class Bits(val range: IntRange): SignalSelector()
    data class Struct(val member: String): SignalSelector()
}