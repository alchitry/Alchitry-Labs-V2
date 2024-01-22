package com.alchitry.labs2.hardware.pinout

interface PinConverter {
    fun AcfToFPGAPin(acfPin: String): String?
}