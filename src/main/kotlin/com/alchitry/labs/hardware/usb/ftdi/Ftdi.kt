package com.alchitry.labs.hardware.usb.ftdi

import com.alchitry.labs.hardware.usb.ftdi.enums.BitMode

interface Ftdi {
    fun setTimeouts(read: Int, write: Int)
    fun usbReset()
    fun setChars(eventChar: Byte, eventEnable: Boolean, errorChar: Byte, errorEnable: Boolean)
    fun setLatencyTimer(time: Byte)
    fun setBitmode(pinDirection: Byte, bitMode: BitMode)
    fun writeData(data: ByteArray): Int
    fun readData(data: ByteArray): Int
    fun readDataWithTimeout(data: ByteArray): Int
    fun usbClose(): Boolean
    fun usbPurgeBuffers()
}
