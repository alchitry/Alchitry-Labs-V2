package com.alchitry.labs.hardware.usb.ftdi

import com.alchitry.labs.hardware.usb.ftdi.enums.BitMode
import com.alchitry.labs.hardware.usb.ftdi.enums.MpsseCommand
import kotlinx.coroutines.delay

abstract class Mpsse(protected var ftdi: Ftdi) {
    inner class MpsseException(string: String?) : RuntimeException(string)

    open suspend fun init() {
        ftdi.usbReset()
        ftdi.setChars(0.toByte(), false, 0.toByte(), false)
        ftdi.setLatencyTimer(16.toByte())
        ftdi.setBitMode(0.toByte(), BitMode.RESET)
        ftdi.setBitMode(0.toByte(), BitMode.MPSSE)
        delay(100)
        if (!syncMpsse()) throw MpsseException("failed to sync with MPSSE")
    }

    private suspend fun syncMpsse(): Boolean {
        ftdi.writeData(byteArrayOf(0xAA.toByte()))
        delay(1000)
        val data = ByteArray(8)
        var read = 0
        while (read == 0) read = ftdi.readData(data)
        for (i in 0..<read - 1) {
            if (data[i] == 0xFA.toByte() && data[i + 1] == 0xAA.toByte()) return true
        }
        return false
    }

    fun setFreq(freq: Double) {
        val clockDivisor = (30.0 / (freq / 1000000.0) - 1.0).toInt()
        // set TCK = 60MHz /((1 + [(1 +0xValueH*256) OR 0xValueL])*2)
        ftdi.writeData(
            byteArrayOf(
                MpsseCommand.SET_CLK_DIV.command,
                (clockDivisor and 0xff).toByte(),
                (clockDivisor ushr 8 and 0xff).toByte()
            )
        )
    }
}
