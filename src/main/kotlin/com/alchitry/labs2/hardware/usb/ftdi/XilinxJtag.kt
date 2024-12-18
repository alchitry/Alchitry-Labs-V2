package com.alchitry.labs2.hardware.usb.ftdi

import com.alchitry.labs2.Log
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.hardware.usb.BoardLoader
import com.alchitry.labs2.ui.theme.AlchitryColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream


class XilinxJtag private constructor(private val ftdi: Ftdi, private val board: Board.XilinxBoard) : BoardLoader {
    private val jtag: Jtag = Jtag(ftdi)

    companion object {
        suspend fun init(ftdi: Ftdi, board: Board.XilinxBoard): XilinxJtag {
            return XilinxJtag(ftdi, board).also { it.init() }
        }
    }

    private suspend fun init() {
        jtag.init()
        jtag.resetState()
    }

    enum class Instruction(val code: Byte) {
        EXTEST(0x26.toByte()), EXTEST_PULSE(0x3C.toByte()), EXTEST_TRAIN(0x3D.toByte()), SAMPLE(0x01.toByte()), USER1(
            0x02.toByte()
        ),
        USER2(0x03.toByte()), USER3(0x22.toByte()), USER4(
            0x23.toByte()
        ),
        CFG_OUT(0x04.toByte()), CFG_IN(0x05.toByte()), USERCODE(0x08.toByte()), IDCODE(0x09.toByte()), HIGHZ_IO(0x0A.toByte()), JPROGRAM(
            0x0B.toByte()
        ),
        JSTART(
            0x0C.toByte()
        ),
        JSHUTDOWN(0x0D.toByte()), XADC_DRP(0x37.toByte()), ISC_ENABLE(0x10.toByte()), ISC_PROGRAM(0x11.toByte()), XSC_PROGRAM_KEY(
            0x12.toByte()
        ),
        XSC_DNA(0x17.toByte()), FUSE_DNA(0x32.toByte()), ISC_NOOP(0x14.toByte()), ISC_DISABLE(0x16.toByte()), BYPASS(
            0x2F.toByte()
        );

    }

    fun setIR(inst: Instruction) {
        jtag.shiftIR(6, byteArrayOf(inst.code))
    }

    fun checkIDCODE(idCode: String) {
        ftdi.usbPurgeBuffers()
        setIR(Instruction.IDCODE)
        jtag.shiftDRWithCheck(32, "00000000", idCode, "0FFFFFFF")
    }

    private fun reverse(b: Byte): Byte {
        var tmp = b.toInt()
        tmp = (((tmp and 0xF0) ushr 4) or ((tmp and 0x0F) shl 4))
        tmp = (((tmp and 0xCC) ushr 2) or ((tmp and 0x33) shl 2))
        tmp = (((tmp and 0xAA) ushr 1) or ((tmp and 0x55) shl 1))
        return tmp.toUByte().toByte()
    }

    @Throws(IOException::class)
    private suspend fun loadBin(stream: InputStream) {
        withContext(Dispatchers.IO) {
            loadBin(stream.readAllBytes())
        }
    }

    @Throws(IOException::class)
    private suspend fun loadBin(binFile: File) {
        loadBin(binFile.inputStream())
    }

    // See page 166 of UG470
    @Throws(IOException::class)
    private suspend fun loadBin(binData: ByteArray) {
        for (i in binData.indices) binData[i] = reverse(binData[i])
        ftdi.usbPurgeBuffers()
        jtag.setFreq(30000000.0)
        jtag.resetState()
        jtag.navigateToState(JtagState.RUN_TEST_IDLE)
        setIR(Instruction.JPROGRAM)
        setIR(Instruction.ISC_NOOP)
        delay(100)
        jtag.sendClocks(10000)
        jtag.shiftIRWithCheck(6, "14", "11", "31")

        // config/slr
        setIR(Instruction.CFG_IN)
        jtag.shiftDR(binData.size * 8, binData)

        // config/start
        jtag.navigateToState(JtagState.RUN_TEST_IDLE)
        jtag.sendClocks(10000)
        setIR(Instruction.JSTART)
        jtag.navigateToState(JtagState.RUN_TEST_IDLE)
        jtag.sendClocks(100)
        jtag.shiftIRWithCheck(6, "09", "31", "11")
        jtag.navigateToState(JtagState.TEST_LOGIC_RESET)
    }

    @Throws(IOException::class)
    private suspend fun erase(bridgeResource: String) {
        Log.println("Loading bridge configuration...")
        val stream = this::class.java.getResourceAsStream(bridgeResource)
            ?: error("Failed to load bridge resource: $bridgeResource")
        loadBin(stream)
        Log.println("Erasing...")
        jtag.setFreq(1500000.0)
        setIR(Instruction.USER1)
        jtag.shiftDR(1, byteArrayOf(0))
        delay(100)
    }

    @Throws(IOException::class)
    override suspend fun eraseFlash() {
        erase(board.bridgeFile)
        setIR(Instruction.JPROGRAM) // reset the FPGA
        jtag.resetState()
        Log.println("Done.", AlchitryColors.current.Success)
    }

    @Throws(IOException::class)
    override suspend fun writeBin(binFile: File, flash: Boolean) {
        Log.println("Checking IDCODE...")
        checkIDCODE(board.idCode)
        if (flash) {
            erase(board.bridgeFile) // configure the FPGA with the bridge and erase the flash
            setIR(Instruction.USER2)
            val binData = withContext(Dispatchers.IO) {
                binFile.readBytes()
            }
            val bufferFull = ByteArray(binData.size)
            jtag.setFreq(1500000.0)
            Log.progressBar("Flashing", binData.size.toLong() - 1) { progressBar ->
                jtag.shiftDR(binData.size * 8, binData, bufferFull) {
                    progressBar.stepTo(it.toLong())
                }
            }
            Log.println()
            if (bufferFull.any { it != 0x00.toByte() }) {
                Log.error("Flash buffer overflowed!")
            }
            Log.println("Resetting FPGA...")
            delay(1000) // wait for flash buffer to flush
            jtag.resetState()
            delay(100)
            setIR(Instruction.JPROGRAM)
        } else {
            Log.println("Loading bin...")
            loadBin(binFile)
        }
        jtag.resetState()
        Log.println("Done.", AlchitryColors.current.Success)
    }
}
