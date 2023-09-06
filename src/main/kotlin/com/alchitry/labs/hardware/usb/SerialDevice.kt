package com.alchitry.labs.hardware.usb

interface SerialDevice {
    fun close(): Boolean
    fun setBaudrate(baud: Int): Int
    fun setDtrRts(dtr: Boolean, rts: Boolean)
    fun setTimeouts(read: Int, write: Int)
    fun writeData(data: ByteArray): Int
    fun readDataWithTimeout(data: ByteArray): Int
    fun readData(data: ByteArray): Int
    fun flushReadBuffer()
}
