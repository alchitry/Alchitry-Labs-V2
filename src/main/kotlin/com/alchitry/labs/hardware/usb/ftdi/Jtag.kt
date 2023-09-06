package com.alchitry.labs.hardware.usb.ftdi

import com.alchitry.labs.hardware.usb.ftdi.enums.MpsseCommand

class Jtag(ftdi: Ftdi) : Mpsse(ftdi) {
    private var currentState = JtagState.RUN_TEST_IDLE
    override suspend fun init() {
        super.init()
        configJtag()
    }

    private fun configJtag() {
        // Set up the Hi-Speed specific commands for the FTx232H
        ftdi.writeData(
            byteArrayOf(
                MpsseCommand.TCK_X5.command,
                MpsseCommand.DIS_ADPT_CLK.command,
                MpsseCommand.DIS_3PH_CLK.command,
            )
        )
        // Set initial states of the MPSSE interface - low byte, both pin directions and output values
        ftdi.writeData(byteArrayOf(MpsseCommand.SETB_LOW.command, 0x08.toByte(), 0x0B.toByte()))
        // Set initial states of the MPSSE interface - high byte, both pin directions and output values
        ftdi.writeData(byteArrayOf(MpsseCommand.SETB_HIGH.command, 0x00.toByte(), 0x00.toByte()))
        // Set default frequency
        setFreq(1000000.0)
        // Disable internal loop-back
        ftdi.writeData(byteArrayOf(MpsseCommand.LOOPBACK_DIS.command))
    }

    fun resetState() {
        currentState = JtagState.CAPTURE_DR
        navigateToState(JtagState.TEST_LOGIC_RESET)
    }

    fun navigateToState(state: JtagState) {
        val transitions: JtagState.Transistions = currentState.getTransitions(state)
        if (transitions.moves > 0) {
            if (transitions.moves < 8) {
                ftdi.writeData(
                    byteArrayOf(
                        0x4B.toByte(),
                        (transitions.moves - 1).toByte(),
                        (0x7F and transitions.tms).toByte()
                    )
                )
            } else {
                ftdi.writeData(byteArrayOf(0x4B.toByte(), 6.toByte(), (0x7F and transitions.tms).toByte()))
                ftdi.writeData(
                    byteArrayOf(
                        0x4B.toByte(),
                        (transitions.moves - 8).toByte(),
                        (0x7F and (transitions.tms shr 7)).toByte()
                    )
                )
            }
        }
        currentState = state
    }

    fun shiftData(bitCount: Int, tdi: ByteArray, tdo: ByteArray?) {
        if (bitCount == 0) return
        when (currentState) {
            JtagState.SHIFT_DR, JtagState.SHIFT_IR -> {}
            else -> throw MpsseException("jtag fsm is in state " + currentState.name + " which is not a shift state")
        }
        val reqBytes = bitCount / 8 + if (bitCount % 8 > 0) 1 else 0

        tdo?.let { ftdi.readData(it) }

        var tdoBytes = 0
        if (tdo != null && tdo.size != tdi.size) throw MpsseException("tdo length do not match tdi length")
        if (bitCount < 9) {
            if (bitCount > 1) ftdi.writeData(
                byteArrayOf(
                    (if (tdo != null) 0x3B else 0x1B).toByte(),
                    (bitCount - 2).toByte(),
                    tdi[0]
                )
            )
            val lastBit = (tdi[0].toInt() ushr (bitCount - 1) % 8 and 0x01).toByte()
            ftdi.writeData(
                byteArrayOf(
                    (if (tdo != null) 0x6E else 0x4B).toByte(),
                    0x00,
                    (0x03 or (lastBit.toInt() shl 7)).toByte()
                )
            )
            if (tdo != null) {
                val inputBuffer = ByteArray(if (bitCount > 1) 2 else 1)
                if (ftdi.readDataWithTimeout(inputBuffer) != inputBuffer.size) throw RuntimeException("Read of " + inputBuffer.size + " bytes timed out!")
                tdo[0] = (inputBuffer[inputBuffer.size - 1].toInt() ushr 8 - bitCount).toByte()
            }
        } else {
            val fullBytes = (bitCount - 1) / 8
            var remBytes = fullBytes
            var offset = 0
            var writeBuffer: ByteArray? = null
            while (remBytes > 0) {
                val bct = if (remBytes > 4096) 4096 else remBytes
                if (writeBuffer == null || writeBuffer.size != bct + 3) writeBuffer = ByteArray(bct + 3)
                writeBuffer[0] = (if (tdo != null) 0x39 else 0x19).toByte()
                writeBuffer[1] = (bct - 1 and 0xff).toByte()
                writeBuffer[2] = (bct - 1 shr 8 and 0xff).toByte()
                System.arraycopy(tdi, offset, writeBuffer, 3, bct)
                if (ftdi.writeData(writeBuffer) != writeBuffer.size) throw MpsseException("failed to write entire buffer")
                remBytes -= bct
                offset += bct
                if (tdo != null) {
                    val readBuffer = ByteArray(bct)
                    ftdi.readDataWithTimeout(readBuffer)
                    System.arraycopy(readBuffer, 0, tdo, tdoBytes, bct)
                    tdoBytes += bct
                }
            }
            val partialBits = bitCount - 1 - fullBytes * 8
            if (fullBytes * 8 + 1 != bitCount) {
                ftdi.writeData(
                    byteArrayOf(
                        (if (tdo != null) 0x3B else 0x1B).toByte(),
                        (partialBits - 1).toByte(),
                        tdi[reqBytes - 1]
                    )
                )
            }
            val lastBit = (tdi[reqBytes - 1].toInt() shr (bitCount - 1) % 8 and 0x01).toByte()
            ftdi.writeData(
                byteArrayOf(
                    (if (tdo != null) 0x6E else 0x4B).toByte(),
                    0.toByte(),
                    (0x03 or (lastBit.toInt() shl 7)).toByte()
                )
            )
            if (tdo != null) {
                val bytesToRead = if (fullBytes * 8 + 1 != bitCount) 2 else 1
                val readBuffer = ByteArray(bytesToRead)
                ftdi.readDataWithTimeout(readBuffer)
                if (bytesToRead == 2) tdo[tdoBytes] =
                    (readBuffer[1].toInt() ushr 8 - (partialBits + 1)).toByte() else tdo[tdoBytes] =
                    (readBuffer[0].toInt() ushr 8 - partialBits).toByte()
            }
        }
        when (currentState) {
            JtagState.SHIFT_DR -> currentState = JtagState.EXIT1_DR
            JtagState.SHIFT_IR -> currentState = JtagState.EXIT1_IR
            else -> {}
        }
    }

    fun shiftDataWithCheck(bitCount: Int, tdi: ByteArray, tdo: ByteArray?, mask: ByteArray?) {
        val read = tdo != null
        var tdoBuffer: ByteArray? = null
        if (read) tdoBuffer = ByteArray(tdo!!.size)
        if (read && mask!!.size != tdi.size) throw MpsseException("mask length does not match tdi length")
        shiftData(bitCount, tdi, tdoBuffer)
        if (read) {
            for (i in tdo!!.indices) if (tdoBuffer!![tdoBuffer.size - 1 - i].toInt() and mask!![i].toInt() != tdo[i].toInt() and mask[i].toInt()) {
                throw MpsseException(
                    String.format(
                        "TDO didn't match. Got %02X expected %02X with mask %02X at byte %d",
                        tdoBuffer[tdoBuffer.size - 1 - i],
                        tdo[i],
                        mask[i],
                        i
                    )
                )
            }
        }
    }

    fun sendClocks(cycles: Long) {
        val actualCycles = if (cycles / 8 > 65536) {
            sendClocks(cycles - 65536L * 8L)
            65536L
        } else {
            cycles / 8
        }

        ftdi.writeData(
            byteArrayOf(
                MpsseCommand.CLK_N8.command,
                (actualCycles - 1 and 0xffL).toByte(),
                (actualCycles - 1 shr 8 and 0xffL).toByte()
            )
        )
    }

    fun stringToByte(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    fun shiftDRWithCheck(bits: Int, write: String, read: String?, mask: String?) {
        var bRead: ByteArray? = null
        var bMask: ByteArray? = null
        val bWrite: ByteArray = stringToByte(write)
        if (read != null) bRead = stringToByte(read)
        if (mask != null) bMask = stringToByte(mask)
        shiftDRWithCheck(bits, bWrite, bRead, bMask)
    }

    fun shiftDRWithCheck(bits: Int, write: ByteArray, read: ByteArray?, mask: ByteArray?) {
        navigateToState(JtagState.SHIFT_DR)
        shiftDataWithCheck(bits, write, read, mask)
        navigateToState(JtagState.RUN_TEST_IDLE)
    }

    fun shiftIRWithCheck(bits: Int, write: String, read: String?, mask: String?) {
        var bRead: ByteArray? = null
        var bMask: ByteArray? = null
        val bWrite: ByteArray = stringToByte(write)
        if (read != null) bRead = stringToByte(read)
        if (mask != null) bMask = stringToByte(mask)
        shiftIRWithCheck(bits, bWrite, bRead, bMask)
    }

    fun shiftIRWithCheck(bits: Int, write: ByteArray, read: ByteArray?, mask: ByteArray?) {
        navigateToState(JtagState.SHIFT_IR)
        shiftDataWithCheck(bits, write, read, mask)
        navigateToState(JtagState.RUN_TEST_IDLE)
    }

    @JvmOverloads
    fun shiftIR(bits: Int, write: ByteArray, read: ByteArray? = null) {
        navigateToState(JtagState.SHIFT_IR)
        shiftData(bits, write, read)
        navigateToState(JtagState.RUN_TEST_IDLE)
    }

    @JvmOverloads
    fun shiftDR(bits: Int, write: ByteArray, read: ByteArray? = null) {
        navigateToState(JtagState.SHIFT_DR)
        shiftData(bits, write, read)
        navigateToState(JtagState.RUN_TEST_IDLE)
    }
}
