package com.alchitry.labs.parsers.lucidv2.signals

typealias SignalSelection = List<SignalSelector>

sealed class SignalSelector {
    data class Bits(val range: IntRange): SignalSelector()
    data class Struct(val member: String): SignalSelector()
}