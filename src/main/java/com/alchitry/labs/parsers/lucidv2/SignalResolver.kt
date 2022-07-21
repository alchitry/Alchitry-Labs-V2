package com.alchitry.labs.parsers.lucidv2

import com.alchitry.labs.parsers.lucidv2.signals.Signal

interface SignalResolver {
    fun resolve(name: String): Signal?
}