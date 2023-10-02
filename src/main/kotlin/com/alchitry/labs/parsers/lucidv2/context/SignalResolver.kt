package com.alchitry.labs.parsers.lucidv2.context

import com.alchitry.labs.parsers.lucidv2.types.SignalOrParent

interface SignalResolver {
    fun resolve(name: String): SignalOrParent?
}