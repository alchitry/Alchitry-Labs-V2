package com.alchitry.labs.hardware.usb.ftdi

import com.alchitry.labs.hardware.usb.SerialDevice
import com.alchitry.labs.hardware.usb.UsbDevice
import com.alchitry.labs.hardware.usb.ftdi.enums.*
import org.usb4java.Device
import org.usb4java.DeviceDescriptor
import org.usb4java.LibUsb
import org.usb4java.LibUsbException
import java.nio.ByteBuffer
import java.nio.IntBuffer
import kotlin.experimental.or

class FtdiLibUSB : UsbDevice(), Ftdi, SerialDevice {
    private var type: ChipType? = null
    private var bitbangEnabled = false
    private var interfaceType: PortInterfaceType? = null
    protected override fun UsbCloseInternal() {
        super.UsbCloseInternal()
        // if (eeprom != null)
        // eeprom.initialized_for_connected_device = 0;
    }

    init {
        type = ChipType.TYPE_BM
        bitbangEnabled = false
        setInterface(PortInterfaceType.INTERFACE_ANY)
        // eeprom = new FtdiEeprom();
    }

    fun setInterface(newInterface: PortInterfaceType) {
        if (device != null) {
            var checkInterface: PortInterfaceType = newInterface
            if (checkInterface == PortInterfaceType.INTERFACE_ANY) checkInterface = PortInterfaceType.INTERFACE_A
            if (interfaceType != checkInterface) throw LibUsbException(
                "Interface can not be changed on an already open device",
                -3
            )
        }
        when (newInterface) {
            PortInterfaceType.INTERFACE_ANY, PortInterfaceType.INTERFACE_A -> {
                interfaceType = PortInterfaceType.INTERFACE_A
                iface = 0
                inEndPoint = 0x02
                outEndPoint = 0x81.toByte()
            }

            PortInterfaceType.INTERFACE_B -> {
                interfaceType = PortInterfaceType.INTERFACE_B
                iface = 1
                inEndPoint = 0x04
                outEndPoint = 0x83.toByte()
            }

            PortInterfaceType.INTERFACE_C -> {
                interfaceType = PortInterfaceType.INTERFACE_C
                iface = 2
                inEndPoint = 0x06
                outEndPoint = 0x85.toByte()
            }

            PortInterfaceType.INTERFACE_D -> {
                interfaceType = PortInterfaceType.INTERFACE_D
                iface = 3
                inEndPoint = 0x08
                outEndPoint = 0x87.toByte()
            }

            else -> throw LibUsbException("Unknown interface", -1)
        }
    }

    protected override fun determinMaxPacketSize(dev: Device?, defSize: Int): Int {
        var defSize = defSize
        defSize =
            if (ChipType.TYPE_2232H == type || ChipType.TYPE_4232H == type || ChipType.TYPE_232H == type) 512 else 64
        return super.determinMaxPacketSize(dev, defSize)
    }

    override fun usbOpenDev(dev: Device?) {
        super.usbOpenDev(dev)
        val desc = DeviceDescriptor()
        if (LibUsb.getDeviceDescriptor(dev, desc) < 0) throw LibUsbException("LibUsb.getDeviceDescriptior() failed", -9)
        if (desc.bcdDevice() == 0x400.toShort() || desc.bcdDevice() == 0x200.toShort() && desc.iSerialNumber() == 0.toByte()) type =
            ChipType.TYPE_BM else if (desc.bcdDevice() == 0x200.toShort()) type =
            ChipType.TYPE_AM else if (desc.bcdDevice() == 0x500.toShort()) type =
            ChipType.TYPE_2232C else if (desc.bcdDevice() == 0x600.toShort()) type =
            ChipType.TYPE_R else if (desc.bcdDevice() == 0x700.toShort()) type =
            ChipType.TYPE_2232H else if (desc.bcdDevice() == 0x800.toShort()) type =
            ChipType.TYPE_4232H else if (desc.bcdDevice() == 0x900.toShort()) type =
            ChipType.TYPE_232H else if (desc.bcdDevice() == 0x1000.toShort()) type = ChipType.TYPE_230X
        try {
            usbReset()
        } catch (e: LibUsbException) {
            UsbCloseInternal()
            throw e
        }
        try {
            setBaudrate(1000000)
        } catch (e: LibUsbException) {
            UsbCloseInternal()
            throw LibUsbException("set baudrate failed", -7)
        }
    }

    override fun usbReset() {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        var code: Int
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_RESET_REQUEST,
                SIO_RESET_SIO,
                interfaceType?.index as Short,
                EMPTY_BUF,
                writeTimeout.toLong()
            ).also { code = it } < 0
        ) throw LibUsbException("Ftdi reset failed", code)
        resetReadBuffer()
    }

    fun usbPurgeRxBuffer() {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_RESET_REQUEST,
                SIO_RESET_PURGE_RX,
                interfaceType?.index as Short,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("Ftdi purge of RX buffer failed", -1)
        resetReadBuffer()
    }

    fun usbPurgeTxBuffer() {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_RESET_REQUEST,
                SIO_RESET_PURGE_TX,
                interfaceType?.index as Short,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("Ftdi purge of TX buffer failed", -1)
    }

    override fun usbPurgeBuffers() {
        usbPurgeRxBuffer()
        usbPurgeTxBuffer()
    }

    override fun readData(buf: ByteArray): Int {
        var offset = 0
        var ret: Int
        var i: Int
        var num_of_chunks: Int
        var chunk_remains: Int
        val packet_size: Int
        val actual_length_buf = IntBuffer.allocate(1)
        var actual_length = 1
        if (device == null) throw LibUsbException("USB device unavailable", -666)

        val readBuffer = readBuffer ?: error("readBuffer must be set before reading data!")

        // Packet size sanity check (avoid division by zero)
        packet_size = maxPacketSize
        if (packet_size == 0) throw LibUsbException("max_packet_size is bogus (zero)", -1)

        // everything we want is still in the readbuffer?
        if (buf!!.size <= readBuffer.remaining()) {
            readBuffer.get(buf)
            return buf.size
        }
        // something still in the readbuffer, but not enough to satisfy 'size'?
        if (readBuffer.remaining() != 0) {
            offset += readBuffer.remaining()
            readBuffer.get(buf, 0, readBuffer.remaining())
        }
        // do the actual USB read
        while (offset < buf.size && actual_length > 0) {
            readBuffer.clear()
            /* returns how much received */actual_length_buf.clear()
            ret = LibUsb.bulkTransfer(device, outEndPoint, readBuffer, actual_length_buf, readTimeout.toLong())
            actual_length = actual_length_buf.get()
            readBuffer.limit(actual_length)
            if (ret < 0) throw LibUsbException("usb bulk read failed", ret)
            if (actual_length > 2) {
                // skip Ftdi status bytes.
                // Maybe stored in the future to enable modem use
                num_of_chunks = actual_length / packet_size
                chunk_remains = actual_length % packet_size
                readBuffer.position(readBuffer.position() + 2)
                actual_length -= 2
                if (actual_length > packet_size - 2) {
                    val buffer = ByteArray(readBuffer.remaining())
                    readBuffer.get(buffer)
                    i = 1
                    while (i < num_of_chunks) {
                        System.arraycopy(buffer, packet_size * i, buffer, (packet_size - 2) * i, packet_size - 2)
                        i++
                    }
                    actual_length -= if (chunk_remains > 2) {
                        System.arraycopy(buffer, packet_size * i, buffer, (packet_size - 2) * i, chunk_remains - 2)
                        2 * num_of_chunks
                    } else {
                        2 * (num_of_chunks - 1) + chunk_remains
                    }
                    readBuffer.clear()
                    readBuffer.put(buffer, 0, actual_length)
                    readBuffer.limit(actual_length)
                    readBuffer.rewind()
                }
            } else if (actual_length <= 2) {
                // no more data to read?
                resetReadBuffer()
                return offset
            }
            if (readBuffer.remaining() > 0) {
                // data still fits in buf?
                if (offset + actual_length <= buf.size) {
                    readBuffer.get(buf, offset, actual_length)
                    offset += actual_length

                    /* Did we read exactly the right amount of bytes? */if (offset == buf.size) return offset
                } else {
                    // only copy part of the data or size <= readbuffer_chunksize
                    val part_size = buf.size - offset
                    readBuffer.get(buf, offset, part_size)
                    offset += part_size
                    return offset
                }
            }
        }
        // never reached
        return -127
    }

    private inner class BaudCalcResults {
        var bestBaud = 0
        var encodedDivisor: Long = 0
        var index: Short = 0
        var value: Short = 0
    }

    private fun toClkbitsAm(baudrate: Int): BaudCalcResults {
        val res = BaudCalcResults()
        val frac_code = intArrayOf(0, 3, 2, 4, 1, 5, 6, 7)
        val am_adjust_up = intArrayOf(0, 0, 0, 1, 0, 3, 2, 1)
        val am_adjust_dn = intArrayOf(0, 0, 0, 1, 0, 1, 2, 3)
        var divisor: Int
        var bestDivisor: Int
        var bestBaud: Int
        var bestBaudDiff: Int
        divisor = 24000000 / baudrate

        // Round down to supported fraction (AM only)
        divisor -= am_adjust_dn[divisor and 7]

        // Try this divisor and the one above it (because division rounds down)
        bestDivisor = 0
        bestBaud = 0
        bestBaudDiff = 0
        for (i in 0..1) {
            var try_divisor = divisor + i
            var baud_estimate: Int
            var baud_diff: Int

            // Round up to supported divisor value
            if (try_divisor <= 8) {
                // Round up to minimum supported divisor
                try_divisor = 8
            } else if (divisor < 16) {
                // AM doesn't support divisors 9 through 15 inclusive
                try_divisor = 16
            } else {
                // Round up to supported fraction (AM only)
                try_divisor += am_adjust_up[try_divisor and 7]
                if (try_divisor > 0x1FFF8) {
                    // Round down to maximum supported divisor value (for AM)
                    try_divisor = 0x1FFF8
                }
            }
            // Get estimated baud rate (to nearest integer)
            baud_estimate = (24000000 + try_divisor / 2) / try_divisor
            // Get absolute difference from requested baud rate
            baud_diff = if (baud_estimate < baudrate) {
                baudrate - baud_estimate
            } else {
                baud_estimate - baudrate
            }
            if (i == 0 || baud_diff < bestBaudDiff) {
                // Closest to requested baud rate so far
                bestDivisor = try_divisor
                bestBaud = baud_estimate
                bestBaudDiff = baud_diff
                if (baud_diff == 0) {
                    // Spot on! No point trying
                    break
                }
            }
        }
        // Encode the best divisor value
        res.encodedDivisor = (bestDivisor shr 3 or (frac_code[bestDivisor and 7] shl 14)).toLong()
        // Deal with special cases for encoded value
        if (res.encodedDivisor == 1L) {
            res.encodedDivisor = 0 // 3000000 baud
        } else if (res.encodedDivisor == 0x4001L) {
            res.encodedDivisor = 1 // 2000000 baud (BM only)
        }
        res.bestBaud = bestBaud
        return res
    }

    private fun toClkbits(baudrate: Int, clk: Int, clkDiv: Int): BaudCalcResults {
        val fracCode = intArrayOf(0, 3, 2, 4, 1, 5, 6, 7)
        var bestBaud = 0
        val divisor: Int
        var bestDivisor: Int
        val res = BaudCalcResults()
        if (baudrate >= clk / clkDiv) {
            res.encodedDivisor = 0
            bestBaud = clk / clkDiv
        } else if (baudrate >= clk / (clkDiv + clkDiv / 2)) {
            res.encodedDivisor = 1
            bestBaud = clk / (clkDiv + clkDiv / 2)
        } else if (baudrate >= clk / (2 * clkDiv)) {
            res.encodedDivisor = 2
            bestBaud = clk / (2 * clkDiv)
        } else {
            /* We divide by 16 to have 3 fractional bits and one bit for rounding */
            divisor = clk * 16 / clkDiv / baudrate
            bestDivisor = if (divisor and 1 == 1) /* Decide if to round up or down */ divisor / 2 + 1 else divisor / 2
            if (bestDivisor > 0x20000) bestDivisor = 0x1ffff
            bestBaud = clk * 16 / clkDiv / bestDivisor
            bestBaud = if (bestBaud and 1 == 1) /* Decide if to round up or down */ bestBaud / 2 + 1 else bestBaud / 2
            res.encodedDivisor = (bestDivisor shr 3 or (fracCode[bestDivisor and 0x7] shl 14)).toLong()
        }
        res.bestBaud = bestBaud
        return res
    }

    private fun convertBaudrate(baudrate: Int): BaudCalcResults? {
        val res: BaudCalcResults
        if (baudrate <= 0) {
            // Return error
            return null
        }
        val H_CLK = 120000000
        val C_CLK = 48000000
        if (ChipType.TYPE_2232H == type || ChipType.TYPE_4232H == type || ChipType.TYPE_232H == type) {
            if (baudrate * 10 > H_CLK / 0x3fff) {
                /*
				 * On H Devices, use 12 000 000 Baudrate when possible We have a 14 bit divisor, a 1 bit divisor switch (10 or 16) three fractional bits and a 120 MHz clock
				 * Assume AN_120 "Sub-integer divisors between 0 and 2 are not allowed" holds for DIV/10 CLK too, so /1, /1.5 and /2 can be handled the same
				 */
                res = toClkbits(baudrate, H_CLK, 10)
                res.encodedDivisor = res.encodedDivisor or 0x20000L /* switch on CLK/10 */
            } else res = toClkbits(baudrate, C_CLK, 16)
        } else if (ChipType.TYPE_BM == type || ChipType.TYPE_2232C == type || ChipType.TYPE_R == type || ChipType.TYPE_230X == type) {
            res = toClkbits(baudrate, C_CLK, 16)
        } else {
            res = toClkbitsAm(baudrate)
        }
        // Split into "value" and "index" values
        res.value = (res.encodedDivisor and 0xFFFFL).toShort()
        if (ChipType.TYPE_2232H == type || ChipType.TYPE_4232H == type || ChipType.TYPE_232H == type) {
            res.index = (res.encodedDivisor shr 8).toShort()
            res.index = (res.index.toInt() and 0xFF00).toShort()
            res.index = (res.index.toInt() or interfaceType!!.index.toInt()).toShort()
        } else res.index = (res.encodedDivisor shr 16).toShort()

        // Return the nearest baud rate
        return res
    }

    override fun setBaudrate(baudrate: Int): Int {
        var baudrate = baudrate
        val res: BaudCalcResults?
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        if (bitbangEnabled) {
            baudrate = baudrate * 4
        }
        res = convertBaudrate(baudrate)
        if (res!!.bestBaud <= 0) throw LibUsbException("Silly baudrate <= 0.", -1)

        // Check within tolerance (about 5%)
        if (res.bestBaud * 2 < baudrate /* Catch overflows */) throw LibUsbException("Unsupported baudrate.", -1)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_BAUDRATE_REQUEST,
                res.value,
                res.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("Setting new baudrate failed", -2)
        return res.bestBaud
    }

    fun setLineProperty(bits: LineDatabit, sbit: LineStopbit, parity: LineParity) {
        setLineProperty(bits, sbit, parity, LineBreak.BREAK_OFF)
    }

    fun setLineProperty(bits: LineDatabit, sbit: LineStopbit, parity: LineParity, breakType: LineBreak) {
        var value: Short = bits.bits.toShort()
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        when (parity) {
            LineParity.NONE -> value = (value.toInt() or (0x00 shl 8)).toShort()
            LineParity.ODD -> value = (value.toInt() or (0x01 shl 8)).toShort()
            LineParity.EVEN -> value = (value.toInt() or (0x02 shl 8)).toShort()
            LineParity.MARK -> value = (value.toInt() or (0x03 shl 8)).toShort()
            LineParity.SPACE -> value = (value.toInt() or (0x04 shl 8)).toShort()
        }
        when (sbit) {
            LineStopbit.STOP_BIT_1 -> value = (value.toInt() or (0x00 shl 11)).toShort()
            LineStopbit.STOP_BIT_15 -> value = (value.toInt() or (0x01 shl 11)).toShort()
            LineStopbit.STOP_BIT_2 -> value = (value.toInt() or (0x02 shl 11)).toShort()
        }
        when (breakType) {
            LineBreak.BREAK_OFF -> value = (value.toInt() or (0x00 shl 14)).toShort()
            LineBreak.BREAK_ON -> value = (value.toInt() or (0x01 shl 14)).toShort()
        }
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_DATA_REQUEST,
                value,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("Setting new line property failed", -1)
    }

    override fun setBitmode(mask: Byte, mode: BitMode) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val usbVal = (mask.toInt() or (mode.mask.toShort().toInt() shl 8)).toShort()
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_BITMODE_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("unable to configure bitbang mode. Perhaps not a BM type chip?", -1)
        bitbangEnabled = BitMode.RESET != mode
    }

    fun disableBitmode() {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_BITMODE_REQUEST,
                0.toShort(),
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("unable to leave bitbang mode. Perhaps not a BM type chip?", -1)
        bitbangEnabled = false
    }

    fun readPins(): Byte {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val pins = ByteBuffer.allocateDirect(1)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_IN_REQTYPE,
                SIO_READ_PINS_REQUEST,
                0.toShort(),
                interfaceType!!.index,
                pins,
                readTimeout.toLong()
            ) != 1
        ) throw LibUsbException("read pins failed", -1)
        return pins.get()
    }

    override fun setLatencyTimer(latency: Byte) {
        if (latency.toInt() == 0) throw LibUsbException("latency out of range. Only valid for 1-255", -1)
        if (device == null) throw LibUsbException("USB device unavailable", -3)
        val usbVal = (0x00FF and latency.toInt()).toShort()
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_LATENCY_TIMER_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("unable to set latency timer", -1)
    }

    fun getLatencyTimer(): Byte {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val latency = ByteBuffer.allocateDirect(1)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_IN_REQTYPE,
                SIO_GET_LATENCY_TIMER_REQUEST,
                0.toShort(),
                interfaceType!!.index,
                latency,
                readTimeout.toLong()
            ) != 1
        ) throw LibUsbException("reading latency timer failed", -1)
        return latency.get()
    }

    fun pullModemStatus(): Short {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val status = ByteBuffer.allocateDirect(2)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_IN_REQTYPE,
                SIO_POLL_MODEM_STATUS_REQUEST,
                0.toShort(),
                interfaceType!!.index,
                status,
                readTimeout.toLong()
            ) != 2
        ) throw LibUsbException("getting modemfailed", -1)
        return (status[1].toShort().toInt() shl 8 or (status[0].toShort().toInt() and 0x00FF)).toShort()
    }

    fun setFlowCtrl(flowctrl: FlowControl) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_FLOW_CTRL_REQUEST,
                0.toShort(),
                (flowctrl.bytecode.toInt() or interfaceType!!.index.toInt()).toShort(),
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("unable to set flow control", -1)
    }

    fun setFlowCtrlXonXoff(xon: Byte, xoff: Byte) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val xonxoff = (xon.toShort().toInt() and 0x00ff or (xoff.toShort().toInt() shl 8)).toShort()
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_FLOW_CTRL_REQUEST,
                xonxoff,
                (SIO_XON_XOFF_HS.toInt() or interfaceType!!.index.toInt()).toShort(),
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("unable to set flow control xon xoff", -1)
    }

    fun setDtr(state: Boolean) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val usbVal = if (state) SIO_SET_DTR_HIGH else SIO_SET_DTR_LOW
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_MODEM_CTRL_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("set dtr failed", -1)
    }

    fun setRts(state: Boolean) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        val usbVal = if (state) SIO_SET_RTS_HIGH else SIO_SET_RTS_LOW
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_MODEM_CTRL_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("set rts failed", -1)
    }

    override fun setDtrRts(dtr: Boolean, rts: Boolean) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        var usbVal = if (dtr) SIO_SET_DTR_HIGH else SIO_SET_DTR_LOW
        usbVal = (usbVal.toInt() or (if (rts) SIO_SET_RTS_HIGH else SIO_SET_RTS_LOW).toInt()).toShort()
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_MODEM_CTRL_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("set dtr and rts failed", -1)
    }

    override fun setChars(eventChar: Byte, eventEnable: Boolean, errorChar: Byte, errorEnable: Boolean) {
        if (device == null) throw LibUsbException("USB device unavailable", -2)
        var usbVal = (eventChar.toInt() and 0x00ff).toShort()
        if (eventEnable) usbVal = (usbVal.toInt() or (1 shl 8)).toShort()
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_EVENT_CHAR_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("setting event character failed", -1)
        usbVal = (errorChar.toInt() and 0x00ff).toShort()
        if (errorEnable) usbVal = (usbVal.toInt() or (1 shl 8)).toShort()
        if (LibUsb.controlTransfer(
                device,
                FTDI_DEVICE_OUT_REQTYPE,
                SIO_SET_ERROR_CHAR_REQUEST,
                usbVal,
                interfaceType!!.index,
                EMPTY_BUF,
                writeTimeout.toLong()
            ) < 0
        ) throw LibUsbException("setting error character failed", -1)
    }

    override fun close(): Boolean {
        return usbClose()
    }

    companion object {
        /* Shifting commands IN MPSSE Mode */
        const val MPSSE_WRITE_NEG = 0x01.toByte() /* Write TDI/DO on negative TCK/SK edge */
        const val MPSSE_BITMODE = 0x02.toByte() /* Write bits, not bytes */
        const val MPSSE_READ_NEG = 0x04.toByte() /* Sample TDO/DI on negative TCK/SK edge */
        const val MPSSE_LSB = 0x08.toByte() /* LSB first */
        const val MPSSE_DO_WRITE = 0x10.toByte() /* Write TDI/DO */
        const val MPSSE_DO_READ = 0x20.toByte() /* Read TDO/DI */
        const val MPSSE_WRITE_TMS = 0x40.toByte() /* Write TMS/CS */

        /* Ftdi MPSSE commands */
        const val SET_BITS_LOW = 0x80.toByte()

        /* BYTE DATA */ /* BYTE Direction */
        const val SET_BITS_HIGH = 0x82.toByte()

        /* BYTE DATA */ /* BYTE Direction */
        const val GET_BITS_LOW = 0x81.toByte()
        const val GET_BITS_HIGH = 0x83.toByte()
        const val LOOPBACK_START = 0x84.toByte()
        const val LOOPBACK_END = 0x85.toByte()
        const val TCK_DIVISOR = 0x86.toByte()

        /* H Type specific commands */
        const val DIS_DIV_5 = 0x8a.toByte()
        const val EN_DIV_re5 = 0x8b.toByte()
        const val EN_3_PHASE = 0x8c.toByte()
        const val DIS_3_PHASE = 0x8d.toByte()
        const val CLK_BITS = 0x8e.toByte()
        const val CLK_BYTES = 0x8f.toByte()
        const val CLK_WAIT_HIGH = 0x94.toByte()
        const val CLK_WAIT_LOW = 0x95.toByte()
        const val EN_ADAPTIVE = 0x96.toByte()
        const val DIS_ADAPTIVE = 0x97.toByte()
        const val CLK_BYTES_OR_HIGH = 0x9c.toByte()
        const val CLK_BYTES_OR_LOW = 0x9d.toByte()

        /* FT232H specific commands */
        const val DRIVE_OPEN_COLLECTOR = 0x9e.toByte()

        /* Value Low */ /* Value HIGH */ /* rate is 12000000/((1+value)*2) */
        fun DIV_VALUE(rate: Int): Short {
            return (if (rate > 6000000) 0 else if (6000000 / rate - 1 > 0xffff) 0xffff else 6000000 / rate - 1).toShort()
        }

        /* Commands in MPSSE and Host Emulation Mode */
        const val SEND_IMMEDIATE = 0x87.toByte()
        const val WAIT_ON_HIGH = 0x88.toByte()
        const val WAIT_ON_LOW = 0x89.toByte()

        /* Commands in Host Emulation Mode */
        const val READ_SHORT = 0x90.toByte()

        /* Address_Low */
        const val READ_EXTENDED = 0x91.toByte()

        /* Address High */ /* Address Low */
        const val WRITE_SHORT = 0x92.toByte()

        /* Address_Low */
        const val WRITE_EXTENDED = 0x93.toByte()

        /* Address High */ /* Address Low */ /* Definitions for flow control */
        const val SIO_RESET = 0.toByte() /* Reset the port */
        const val SIO_MODEM_CTRL = 1.toByte() /* Set the modem control register */
        const val SIO_SET_FLOW_CTRL = 2.toByte() /* Set flow control register */
        const val SIO_SET_BAUD_RATE = 3.toByte() /* Set baud rate */
        const val SIO_SET_DATA = 4.toByte() /* Set the data characteristics of the port */
        val FTDI_DEVICE_OUT_REQTYPE =
            (LibUsb.REQUEST_TYPE_VENDOR or LibUsb.RECIPIENT_DEVICE or LibUsb.ENDPOINT_OUT) as Byte
        val FTDI_DEVICE_IN_REQTYPE =
            (LibUsb.REQUEST_TYPE_VENDOR or LibUsb.RECIPIENT_DEVICE or LibUsb.ENDPOINT_IN) as Byte

        /* Requests */
        const val SIO_RESET_REQUEST = SIO_RESET
        const val SIO_SET_BAUDRATE_REQUEST = SIO_SET_BAUD_RATE
        const val SIO_SET_DATA_REQUEST = SIO_SET_DATA
        const val SIO_SET_FLOW_CTRL_REQUEST = SIO_SET_FLOW_CTRL
        const val SIO_SET_MODEM_CTRL_REQUEST = SIO_MODEM_CTRL
        const val SIO_POLL_MODEM_STATUS_REQUEST = 0x05.toByte()
        const val SIO_SET_EVENT_CHAR_REQUEST = 0x06.toByte()
        const val SIO_SET_ERROR_CHAR_REQUEST = 0x07.toByte()
        const val SIO_SET_LATENCY_TIMER_REQUEST = 0x09.toByte()
        const val SIO_GET_LATENCY_TIMER_REQUEST = 0x0A.toByte()
        const val SIO_SET_BITMODE_REQUEST = 0x0B.toByte()
        const val SIO_READ_PINS_REQUEST = 0x0C.toByte()
        const val SIO_READ_EEPROM_REQUEST = 0x90.toByte()
        const val SIO_WRITE_EEPROM_REQUEST = 0x91.toByte()
        const val SIO_ERASE_EEPROM_REQUEST = 0x92.toByte()
        const val SIO_RESET_SIO = 0.toShort()
        const val SIO_RESET_PURGE_RX = 1.toShort()
        const val SIO_RESET_PURGE_TX = 2.toShort()
        const val SIO_DISABLE_FLOW_CTRL = 0x0.toShort()
        const val SIO_RTS_CTS_HS = (0x1 shl 8).toShort()
        const val SIO_DTR_DSR_HS = (0x2 shl 8).toShort()
        const val SIO_XON_XOFF_HS = (0x4 shl 8).toShort()
        const val SIO_SET_DTR_MASK = 0x1.toShort()
        const val SIO_SET_DTR_HIGH = (1 or (SIO_SET_DTR_MASK.toInt() shl 8)).toShort()
        const val SIO_SET_DTR_LOW = (0 or (SIO_SET_DTR_MASK.toInt() shl 8)).toShort()
        const val SIO_SET_RTS_MASK = 0x2.toShort()
        const val SIO_SET_RTS_HIGH = (2 or (SIO_SET_RTS_MASK.toInt() shl 8)).toShort()
        const val SIO_SET_RTS_LOW = (0 or (SIO_SET_RTS_MASK.toInt() shl 8)).toShort()
        // private FtdiEeprom eeprom;
        /** Invert TXD#  */
        const val INVERT_TXD = 0x01

        /** Invert RXD#  */
        const val INVERT_RXD = 0x02

        /** Invert RTS#  */
        const val INVERT_RTS = 0x04

        /** Invert CTS#  */
        const val INVERT_CTS = 0x08

        /** Invert DTR#  */
        const val INVERT_DTR = 0x10

        /** Invert DSR#  */
        const val INVERT_DSR = 0x20

        /** Invert DCD#  */
        const val INVERT_DCD = 0x40

        /** Invert RI#  */
        const val INVERT_RI = 0x80

        /** Interface Mode.  */
        const val CHANNEL_IS_UART = 0x0
        const val CHANNEL_IS_FIFO = 0x1
        const val CHANNEL_IS_OPTO = 0x2
        const val CHANNEL_IS_CPU = 0x4
        const val CHANNEL_IS_FT1284 = 0x8
        const val CHANNEL_IS_RS485 = 0x10
        const val DRIVE_4MA = 0
        const val DRIVE_8MA = 1
        const val DRIVE_12MA = 2
        const val DRIVE_16MA = 3
        const val SLOW_SLEW = 4
        const val IS_SCHMITT = 8

        /** Driver Type.  */
        const val DRIVER_VCP = 0x08
        const val DRIVER_VCPH = 0x10 /* FT232H has moved the VCP bit */
        const val USE_USB_VERSION_BIT = 0x10
        const val SUSPEND_DBUS7_BIT = 0x80

        /** High current drive.  */
        const val HIGH_CURRENT_DRIVE = 0x10
        const val HIGH_CURRENT_DRIVE_R = 0x04
        val EMPTY_BUF = ByteBuffer.allocateDirect(0)
    }
}
