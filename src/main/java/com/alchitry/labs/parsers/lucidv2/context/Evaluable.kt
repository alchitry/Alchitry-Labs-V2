package com.alchitry.labs.parsers.lucidv2.context

interface Evaluable: Initializable {
    suspend fun evaluate()
}
