package com.alchitry.labs.hardware.usb.ftdi

import com.alchitry.labs.hardware.usb.SerialDevice
import com.alchitry.labs.hardware.usb.ftdi.enums.BitMode
import net.sf.yad2xx.Device
import net.sf.yad2xx.FTDIConstants
import net.sf.yad2xx.FTDIException

class FtdiD2xx(device: Device) : Ftdi, SerialDevice {
    private val device: Device
    protected var readTimeout = 0
    protected var writeTimeout = 0

    init {
        this.device = device
    }

    override fun setTimeouts(read: Int, write: Int) {
        readTimeout = read
        writeTimeout = write
        try {
            device.setTimeouts(read, write)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun usbReset() {
        try {
            device.reset()
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun setChars(eventChar: Byte, eventEnable: Boolean, errorChar: Byte, errorEnable: Boolean) {
        try {
            device.setChars(Char(eventChar.toUShort()), eventEnable, Char(errorChar.toUShort()), errorEnable)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun setLatencyTimer(time: Byte) {
        try {
            device.setLatencyTimer(time)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun setBitMode(pinDirection: Byte, bitMode: BitMode) {
        try {
            device.setBitMode(pinDirection, bitMode.fTDIMode)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun writeData(data: ByteArray): Int {
        return try {
            device.write(data)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun readData(data: ByteArray): Int {
        return try {
            val bytesToRead = device.queueStatus.coerceAtMost(data.size)
            if (bytesToRead == 0) return 0
            val buff = ByteArray(bytesToRead)
            val bytesRead: Int = device.read(buff)
            System.arraycopy(buff, 0, data, 0, bytesRead)
            bytesRead
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun readDataWithTimeout(data: ByteArray): Int {
        return try {
            device.read(data)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun close() {
        try {
            device.close()
        } catch (e: FTDIException) {
            e.printStackTrace()
        }
    }

    override fun usbPurgeBuffers() {
        try {
            device.purge(FTDIConstants.FT_PURGE_RX or FTDIConstants.FT_PURGE_TX)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun setBaudrate(baud: Int): Int {
        try {
            device.setBaudRate(baud)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
        return baud
    }

    override fun setDtrRts(dtr: Boolean, rts: Boolean) {
        try {
            device.setDtr(dtr)
            device.setRts(rts)
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }

    override fun flushReadBuffer() {
        try {
            device.purge(FTDIConstants.FT_PURGE_RX)
            while (device.getQueueStatus() > 0) {
                device.read(ByteArray(device.getQueueStatus()))
            }
        } catch (e: FTDIException) {
            throw RuntimeException(e)
        }
    }
}
