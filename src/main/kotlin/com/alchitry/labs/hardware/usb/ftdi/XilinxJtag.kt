package com.alchitry.labs.hardware.usb.ftdi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


class XilinxJtag(private val ftdi: Ftdi) {
    private val jtag: Jtag = Jtag(ftdi)

    suspend fun init() {
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
    private suspend fun loadBridge(loaderFile: String) {
        val inStream = javaClass.getResourceAsStream(loaderFile) ?: error("Failed to get loader file!")
        val os = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len: Int

        // read bytes from the input stream and store them in buffer
        while (withContext(Dispatchers.IO) {
                inStream.read(buffer)
            }.also { len = it } != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len)
        }
        loadBin(os.toByteArray())
    }

    @Throws(IOException::class)
    private suspend fun loadBin(binPath: String) {
        val binData = withContext(Dispatchers.IO) {
            Files.readAllBytes(Paths.get(binPath))
        }
        loadBin(binData)
    }

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

        // config/jprog/poll
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
    private suspend fun erase(loaderFile: String) {
        println("Loading bridge configuration...")
        loadBridge(loaderFile)
        println("Erasing...")
        jtag.setFreq(1500000.0)
        setIR(Instruction.USER1)
        jtag.shiftDR(1, byteArrayOf(0))
        delay(100)
    }

    @Throws(IOException::class)
    suspend fun eraseFlash(loaderFile: String) {
        erase(loaderFile)
        setIR(Instruction.JPROGRAM) // reset the FPGA
        jtag.resetState()
        //println("Done.", Theme.successTextColor)
    }

    @Throws(IOException::class)
    suspend fun writeBin(binFile: String, flash: Boolean, loaderFile: String) {
        if (flash) {
            erase(loaderFile) // configure the FPGA with the bridge and erase the flash
            println("Writing flash...")
            setIR(Instruction.USER2)
            val binData = withContext(Dispatchers.IO) {
                Files.readAllBytes(Paths.get(binFile))
            }
            jtag.shiftDR(binData.size * 8, binData)
            println("Resetting FPGA...")
            jtag.resetState()
            delay(100)
            setIR(Instruction.JPROGRAM)
        } else {
            println("Loading bin...")
            loadBin(binFile)
        }
        jtag.resetState()
        //println("Done.", Theme.successTextColor)
    }

    companion object {
        const val AU_LOADER_FILE = "/fpga/au_loader.bin"
        const val AU_PLUS_LOADER_FILE = "/fpga/au_plus_loader.bin"
    }
}