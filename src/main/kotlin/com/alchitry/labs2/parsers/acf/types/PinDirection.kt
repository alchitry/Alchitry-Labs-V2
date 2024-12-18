package com.alchitry.labs2.parsers.acf.types

enum class PinDirection {
    INPUT,
    OUTPUT,
    INOUT;

    companion object {
        val all = entries
        val single = listOf(INPUT, OUTPUT)
    }
}