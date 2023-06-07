package com.alchitry.labs.parsers.lucidv2.signals

typealias SignalSelection = List<SignalSelector>

class SignalSelectionException(val selector: SignalSelector, message: String) : Exception(message)

sealed class SignalSelector {
    data class Bits(val range: IntRange) : SignalSelector() {
        constructor(bit: Int) : this(bit..bit)

        override fun toString(): String {
            return if (range.first == range.last) {
                "[${range.first}]"
            } else {
                "[${range.last}:${range.first}]"
            }
        }
    }

    data class Struct(val member: String) : SignalSelector() {
        override fun toString(): String {
            return member
        }
    }
}