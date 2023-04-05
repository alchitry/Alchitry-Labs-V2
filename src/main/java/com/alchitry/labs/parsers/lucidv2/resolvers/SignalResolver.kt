package com.alchitry.labs.com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.parsers.lucidv2.signals.Signal

interface SignalResolver {
    fun resolve(name: String): Signal?
}