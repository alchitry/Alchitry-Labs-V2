package com.alchitry.labs2.parsers.hdl.types

/**
 * A union of the interface SignalParent and Signal class
 */
sealed interface SignalOrParent {
    val name: String
    val parent: SignalParent?
}