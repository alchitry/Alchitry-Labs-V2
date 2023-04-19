package com.alchitry.labs.parsers.lucidv2.resolvers

import com.alchitry.labs.parsers.lucidv2.signals.SignalOrParent

interface SignalResolver {
    fun resolve(name: String): SignalOrParent?
}