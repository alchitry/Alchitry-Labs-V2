package com.alchitry.labs2.parsers

interface Evaluable {
    suspend fun evaluate()
}

fun Evaluable(block: suspend ()->Unit) = object : Evaluable {
    override suspend fun evaluate() {
        block()
    }
}