package com.alchitry.labs.parsers.lucidv2.signals

/**
 * A union of the interface SignalParent and Signal class
 */
sealed interface SignalOrParent {
    val name: String
    val parent: SignalParent?
}