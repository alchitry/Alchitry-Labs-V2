package com.alchitry.labs2.hardware.usb

import com.fazecast.jSerialComm.SerialPort

class GenericSerial(private val device: SerialPort) : SerialDevice {
    private var readTimeout = 2000
    private var writeTimeout = 2000

    fun open(): Boolean {
        if (!device.openPort()) return false
        device.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, readTimeout, writeTimeout)
        return true
    }

    override fun close() {
        device.closePort()
    }

    override fun setBaudrate(baud: Int): Int {
        device.setBaudRate(baud)
        return device.baudRate
    }

    override fun setDtrRts(dtr: Boolean, rts: Boolean) {
        if (dtr) device.setDTR() else device.clearDTR()
        if (rts) device.setRTS() else device.clearRTS()
    }

    override fun setTimeouts(read: Int, write: Int) {
        readTimeout = read
        writeTimeout = write
        device.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, read, write)
    }

    override fun writeData(data: ByteArray): Int {
        return device.writeBytes(data, data.size)
    }

    override fun readDataWithTimeout(data: ByteArray): Int {
        return device.readBytes(data, data.size)
    }

    override fun readData(data: ByteArray): Int {
        device.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, readTimeout, writeTimeout)
        val count: Int = device.readBytes(data, data.size)
        device.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, readTimeout, writeTimeout)
        return count
    }

    override fun flushReadBuffer() {
        device.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, readTimeout, writeTimeout)
        val buff = ByteArray(100)
        @Suppress("ControlFlowWithEmptyBody")
        while (device.readBytes(buff, 100) > 0) {
        }
        device.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, readTimeout, writeTimeout)
    }
}
