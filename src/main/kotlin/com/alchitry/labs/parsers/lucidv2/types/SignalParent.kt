package com.alchitry.labs.parsers.lucidv2.types

/**
 * A SignalParent is something that holds signals. For example a Dff has signals "d" and "q".
 */
sealed interface SignalParent : SignalOrParent {
    /**
     * Retrieves the child signal from this parent.
     * @param name the name of the signal to retrieve
     * @return the signal or null if it couldn't be found
     */
    fun getSignal(name: String): SignalOrParent?
}