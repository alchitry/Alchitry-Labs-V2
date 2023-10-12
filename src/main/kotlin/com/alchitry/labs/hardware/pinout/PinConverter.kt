package com.alchitry.labs.hardware.pinout

interface PinConverter {
    fun AcfToFPGAPin(acfPin: String): String?
}