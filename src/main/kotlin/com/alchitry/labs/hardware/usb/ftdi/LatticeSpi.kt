package com.alchitry.labs.hardware.usb.ftdi

import com.alchitry.labs.hardware.usb.ftdi.enums.FlashCommand
import com.alchitry.labs.hardware.usb.ftdi.enums.MpsseCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.min

class LatticeSpi(ftdi: Ftdi) : Mpsse(ftdi) {
    init {
        // Set up the Hi-Speed specific commands for the FTx232H
        ftdi.writeData(
            byteArrayOf(
                MpsseCommand.TCK_X5.command,
                MpsseCommand.DIS_ADPT_CLK.command,
                MpsseCommand.DIS_3PH_CLK.command
            )
        )
        // Set initial states of the MPSSE interface - low byte, both pin directions and output values
        ftdi.writeData(byteArrayOf(MpsseCommand.SETB_LOW.command, 0x00.toByte(), 0xBB.toByte()))
        // Set initial states of the MPSSE interface - high byte, both pin directions and output values
        ftdi.writeData(byteArrayOf(MpsseCommand.SETB_HIGH.command, 0x00.toByte(), 0x00.toByte()))
        // Set default frequency
        setFreq(30000000.0)
        // Disable internal loop-back
        ftdi.writeData(byteArrayOf(MpsseCommand.LOOPBACK_DIS.command))
    }

    private fun rxByte(): Byte {
        val buf = ByteArray(1)
        if (ftdi.readDataWithTimeout(buf) != buf.size) throw RuntimeException("Read of " + buf.size + " bytes timed out!")
        return buf[0]
    }

    private fun txByte(b: Byte) {
        val buf = ByteArray(1)
        buf[0] = b
        if (ftdi.writeData(buf) != 1) throw MpsseException("Failed to write single byte")
    }

    private fun sendSpi(data: ByteArray) {
        val buf = ByteArray(3)
        buf[0] = (DATA_OUT.toInt() or DATA_OCN.toInt()).toByte()
        buf[1] = (data.size - 1 and 0xff).toByte()
        buf[2] = (data.size - 1 ushr 8 and 0xff).toByte()
        ftdi.writeData(buf)
        if (ftdi.writeData(data) != data.size) throw MpsseException("Failed to write full chunk!")
    }

    private fun xferSpi(data: ByteArray) {
        val buf = ByteArray(3)
        buf[0] = (DATA_IN.toInt() or DATA_OUT.toInt() or DATA_OCN.toInt()).toByte()
        buf[1] = (data.size - 1 and 0xff).toByte()
        buf[2] = (data.size - 1 ushr 8 and 0xff).toByte()
        ftdi.writeData(buf)
        if (ftdi.writeData(data) != data.size) throw MpsseException("Failed to write full chunk!")
        if (ftdi.readDataWithTimeout(data) != data.size) throw RuntimeException("Read of " + data.size + " bytes timed out!")
    }

    private fun xferSpiBits(data: Byte, n: Int): Byte {
        if (n < 1) return 0
        val buf = ByteArray(3)
        buf[0] = (DATA_IN.toInt() or DATA_OUT.toInt() or DATA_OCN.toInt() or DATA_BITS.toInt()).toByte()
        buf[1] = (n - 1).toByte()
        buf[2] = data
        ftdi.writeData(buf)
        return rxByte()
    }

    private fun setGpio(slaveSel: Boolean, creset: Boolean) {
        var gpio: Byte = 0
        if (slaveSel) gpio = (gpio.toInt() or 0x10).toByte()
        if (creset) gpio = (gpio.toInt() or 0x80).toByte()
        val buf = ByteArray(3)
        buf[0] = MpsseCommand.SETB_LOW.command
        buf[1] = gpio
        buf[2] = 0x93.toByte()
        ftdi.writeData(buf)
    }

    val cdone: Boolean
        get() {
            txByte(MpsseCommand.READB_LOW.command)
            val data = rxByte()
            return data.toInt() and 0x40 != 0
        }

    private fun flashReleaseReset() {
        setGpio(true, true)
    }

    private fun flashChipSelect() {
        setGpio(false, false)
    }

    private fun flashChipDeselect() {
        setGpio(true, false)
    }

    fun flashReadId() {
        val buf = ByteArray(5)
        var ext: ByteArray? = null
        buf[0] = FlashCommand.JEDECID.command
        flashChipSelect()
        xferSpi(buf)
        if (buf[4] == 0xFF.toByte()) {
            println("Extended device string length is 0xff. This is likely an error. Ignoring...")
        } else if (buf[4].toInt() != 0) {
            ext = ByteArray(buf[4].toInt())
            xferSpi(ext)
        }
        flashChipDeselect()
        if (buf[1] != 0xEF.toByte() || buf[2] != 0x40.toByte() || buf[3] != 0x16.toByte() || buf[4] != 0x00.toByte()) {
            val sb = StringBuilder()
            sb.append("0x")
            for (i in 1..4) sb.append(String.format("%02X", buf[i]))
            if (ext != null) for (i in ext.indices) sb.append(String.format("%02X", ext[i]))
            throw MpsseException("Flash ID was $sb expected 0xEF401600")
        }
    }

    private fun flashReset() {
        flashChipSelect()
        xferSpiBits(0xff.toByte(), 8)
        flashChipDeselect()
        flashChipSelect()
        xferSpiBits(0xff.toByte(), 2)
        flashChipDeselect()
    }

    private fun flashPowerUp() {
        val data = byteArrayOf(FlashCommand.RPD.command)
        flashChipSelect()
        sendSpi(data)
        flashChipDeselect()
    }

    private fun flashPowerDown() {
        val data = byteArrayOf(FlashCommand.PD.command)
        flashChipSelect()
        sendSpi(data)
        flashChipDeselect()
    }

    private fun flashReadStatus(): Byte {
        val data = ByteArray(2)
        data[0] = FlashCommand.RSR1.command
        flashChipSelect()
        xferSpi(data)
        flashChipDeselect()
        return data[1]
    }

    private fun flashWriteEnable() {
        val data = byteArrayOf(FlashCommand.WE.command)
        flashChipSelect()
        sendSpi(data)
        flashChipDeselect()
    }

    private fun flashBulkErase() {
        val data = byteArrayOf(FlashCommand.CE.command)
        flashChipSelect()
        sendSpi(data)
        flashChipDeselect()
    }

    private fun flash64KbSectorErase(addr: Int) {
        val data =
            byteArrayOf(FlashCommand.BE64.command, (addr ushr 16).toByte(), (addr ushr 8).toByte(), addr.toByte())
        flashChipSelect()
        sendSpi(data)
        flashChipDeselect()
    }

    private fun flashProg(addr: Int, data: ByteArray) {
        val cmd =
            byteArrayOf(FlashCommand.PP.command, (addr ushr 16).toByte(), (addr ushr 8).toByte(), addr.toByte())
        flashChipSelect()
        sendSpi(cmd)
        sendSpi(data)
        flashChipDeselect()
    }

    @Suppress("unused")
    private fun flashRead(addr: Int, data: ByteArray) {
        val cmd =
            byteArrayOf(FlashCommand.RD.command, (addr ushr 16).toByte(), (addr ushr 8).toByte(), addr.toByte())
        flashChipSelect()
        sendSpi(cmd)
        xferSpi(data)
        flashChipDeselect()
    }

    private suspend fun flashWait() {
        var count = 0
        val data = ByteArray(2)
        while (true) {
            data[0] = FlashCommand.RSR1.command
            flashChipSelect()
            xferSpi(data)
            flashChipDeselect()
            if (data[1].toInt() and 0x01 == 0) {
                if (count < 2) {
                    count++
                } else {
                    break
                }
            } else {
                count = 0
            }
            delay(1000)
        }
    }

    @Suppress("unused")
    private suspend fun flashDisableProtection() {
        val data = byteArrayOf(FlashCommand.WSR1.command, 0x00)
        flashChipSelect()
        xferSpi(data)
        flashChipDeselect()
        flashWait()
        if (flashReadStatus().toInt() != 0) throw MpsseException(
            String.format(
                "Failed to disable write protection. SR is 0x%02X (expected 0x00)",
                data[1]
            )
        )
    }


    suspend fun eraseFlash() {
        println("Resetting...")
        flashChipDeselect()
        delay(250)
        println("Erasing...")
        flashReset()
        flashPowerUp()
        flashReadId()
        flashWriteEnable()
        flashBulkErase()
        flashWait()
        flashPowerDown()
        flashReleaseReset()
        delay(250)
        println("Done.")
    }

    @Throws(IOException::class)
    suspend fun writeBin(binFile: String) {
        val binData = withContext(Dispatchers.IO) {
            Files.readAllBytes(Paths.get(binFile))
        }
        println("Resetting...")
        flashChipDeselect()
        delay(250)
        flashReset()
        flashPowerUp()
        flashReadId()
        val begin_addr = 0
        val end_addr = binData.size + 0xffff and 0xffff.inv()
        println("Erasing...")
        var addr = begin_addr
        while (addr < end_addr) {
            flashWriteEnable()
            flash64KbSectorErase(addr)
            flashWait()
            addr += 0x10000
        }
        println("Programming...")
        var pageBuf = ByteArray(256)
        var offset = 0
        while (offset < binData.size) {
            val ct = min(256, binData.size - offset)
            if (pageBuf.size != ct) pageBuf = ByteArray(ct)
            System.arraycopy(binData, offset, pageBuf, 0, pageBuf.size)
            flashWriteEnable()
            flashProg(offset, pageBuf)
            flashWait()
            offset += pageBuf.size
        }
        println("Resetting...")
        flashPowerDown()
        flashReleaseReset()
        delay(250)
        println("Done.")
    }

    companion object {
        // private static final byte DATA_TMS = (byte) 0x40; /* When set use TMS mode */
        private const val DATA_IN = 0x20.toByte() /* When set read data (Data IN) */
        private const val DATA_OUT = 0x10.toByte() /* When set write data (Data OUT) */

        // private static final byte DATA_LSB = (byte) 0x08; /* When set input/output data LSB first. */
        // private static final byte DATA_ICN = (byte) 0x04; /* When set receive data on negative clock edge */
        private const val DATA_BITS = 0x02.toByte() /* When set count bits not bytes */
        private const val DATA_OCN = 0x01.toByte() /* When set update data on negative clock edge */
    }
}