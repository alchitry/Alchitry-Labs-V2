package com.alchitry.labs2.parsers.lucidv2.types

enum class SignalDirection {
    Read,
    Write,
    Both;

    val canRead: Boolean get() = this != Write
    val canWrite: Boolean get() = this != Read

    fun flip(): SignalDirection = when (this) {
        Read -> Write
        Write -> Read
        Both -> Both
    }
}