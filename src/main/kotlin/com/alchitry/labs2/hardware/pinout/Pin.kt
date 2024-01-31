package com.alchitry.labs2.hardware.pinout

interface PinConverter {
    fun acfToPin(name: String): Pin?
}

interface Pin {
    val name: String
    val fpgaPin: String
}
