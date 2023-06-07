package com.alchitry.labs.parsers.lucidv2.context

import kotlinx.coroutines.sync.Mutex

interface Evaluable {
    suspend fun evaluate()
}

interface LockableEvaluable : Evaluable {
    val lock: Mutex
}

fun Evaluable(block: suspend ()->Unit) = object : Evaluable {
    override suspend fun evaluate() {
        block()
    }
}