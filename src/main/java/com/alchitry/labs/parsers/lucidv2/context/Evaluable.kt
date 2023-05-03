package com.alchitry.labs.parsers.lucidv2.context

interface Evaluable {
    suspend fun evaluate()
    suspend fun waitCollecting()
}