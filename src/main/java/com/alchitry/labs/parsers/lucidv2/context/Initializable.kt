package com.alchitry.labs.parsers.lucidv2.context

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

interface Initializable {
    suspend fun waitInit()
}

class SimpleInit: Initializable {
    private val flow = MutableStateFlow(false)

    fun done() { flow.tryEmit(true) }

    override suspend fun waitInit() {
        flow.first { true }
    }
}