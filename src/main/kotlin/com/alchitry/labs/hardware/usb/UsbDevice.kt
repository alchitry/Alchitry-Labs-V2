package com.alchitry.labs.hardware.usb

import com.alchitry.labs.Env
import com.alchitry.labs.hardware.usb.ftdi.enums.DetachMode
import org.usb4java.*
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.regex.Pattern

open class UsbDevice {
    class TransferControl {
        var completed: IntBuffer = BufferUtils.allocateIntBuffer()
        var offset = 0
        var transfer: Transfer? = null
    }

    protected var device: DeviceHandle? = null
    protected var readTimeout = 0
    protected var writeTimeout = 0
    protected var readBuffer: ByteBuffer? = null
    protected var readBufferChunksize = 0
    protected var writeBufferChunksize = 0
    protected var maxPacketSize = 0
    protected var iface = 0
    protected var inEndPoint: Byte = 0
    protected var outEndPoint: Byte = 0
    private var detachMode: DetachMode? = null

    class DeviceStrings {
        var manufacture: String? = null
        var product: String? = null
        var serial: String? = null
    }

    protected open fun UsbCloseInternal() {
        UsbCloseInternal(device)
    }

    init {
        device = null
        readTimeout = 5000
        writeTimeout = 5000
        readBuffer = null
        writeBufferChunksize = 16384
        maxPacketSize = 0
        detachMode = DetachMode.AUTO_DETACH_REATACH_SIO_MODULE
        iface = 0
        readDataSetChunkSize(16384)
    }

    fun setDetachMode(mode: DetachMode) {
        if (device != null) {
            if (mode != detachMode) throw LibUsbException(
                "Detach mode can not be changed on an already open device",
                -3
            )
        }
        detachMode = mode
    }

    fun deinit() {
        UsbCloseInternal()
        if (context != null) {
            LibUsb.exit(context)
            context = null
        }
    }

    fun setUsbDev(dev: DeviceHandle?) {
        device = dev
    }

    fun usbGetStrings(dev: Device): DeviceStrings {
        val needOpen = device == null
        if (needOpen) {
            device = DeviceHandle()
            if (LibUsb.open(dev, device) < 0) throw LibUsbException("LibUsb.open() failed", -4)
        }
        val desc = DeviceDescriptor()
        if (LibUsb.getDeviceDescriptor(dev, desc) < 0) throw LibUsbException("LibUsb.getDeviceDescriptor() failed", -11)
        val strings = DeviceStrings()
        strings.product = LibUsb.getStringDescriptor(device, desc.iProduct())
        strings.manufacture = LibUsb.getStringDescriptor(device, desc.iManufacturer())
        strings.serial = LibUsb.getStringDescriptor(device, desc.iSerialNumber())
        if (needOpen) UsbCloseInternal()
        return strings
    }

    protected open fun determinMaxPacketSize(dev: Device?, defSize: Int): Int {
        var packetSize = defSize
        val desc = DeviceDescriptor()
        val config = ConfigDescriptor()
        if (dev == null) return 64
        if (LibUsb.getDeviceDescriptor(dev, desc) < 0) return packetSize
        if (LibUsb.getConfigDescriptor(dev, 0.toByte(), config) < 0) return packetSize
        if (desc.bNumConfigurations() > 0) {
            if (iface < desc.bNumConfigurations()) {
                val iface: Interface = config.iface().get(0)
                if (iface.numAltsetting() > 0) {
                    val descriptor: InterfaceDescriptor = iface.altsetting()[0]
                    if (descriptor.bNumEndpoints() > 0) {
                        packetSize = descriptor.endpoint().get(0).wMaxPacketSize().toInt()
                    }
                }
            }
        }
        LibUsb.freeConfigDescriptor(config)
        return packetSize
    }

    fun setTimeouts(read: Int, write: Int) {
        readTimeout = read
        writeTimeout = write
    }

    open fun usbOpenDev(dev: Device?) {
        val desc = DeviceDescriptor()
        val config = ConfigDescriptor()
        val cfg: Int
        var detachErrno = 0
        var errno: Int
        val cfgBuf = IntBuffer.allocate(1)
        device = DeviceHandle()
        if (LibUsb.open(dev, device) < 0) throw LibUsbException("LibUsb.open() failed", -8)
        if (LibUsb.getDeviceDescriptor(dev, desc) < 0) throw LibUsbException("LibUsb.getDeviceDescriptior() failed", -9)
        if (LibUsb.getConfigDescriptor(
                dev,
                0.toByte(),
                config
            ) < 0
        ) throw LibUsbException("LibUsb.getConfigDescriptor() failed", -10)
        val cfg0: Int = config.bConfigurationValue().toInt()
        LibUsb.freeConfigDescriptor(config)
        if (DetachMode.AUTO_DETACH_SIO_MODULE == detachMode) {
            if (LibUsb.detachKernelDriver(device, iface).also { errno = it } != LibUsb.SUCCESS) detachErrno = errno
        } else if (DetachMode.AUTO_DETACH_REATACH_SIO_MODULE == detachMode) {
            if (LibUsb.setAutoDetachKernelDriver(device, true).also { errno = it } != LibUsb.SUCCESS) detachErrno =
                errno
            if (detachErrno == LibUsb.ERROR_NOT_SUPPORTED) if (LibUsb.detachKernelDriver(device, iface)
                    .also { errno = it } != LibUsb.SUCCESS
            ) detachErrno = errno
        }
        if (LibUsb.getConfiguration(device, cfgBuf) < 0) throw LibUsbException("LibUsb.getConfiguration() failed", -12)
        cfg = cfgBuf[0]
        if (desc.bNumConfigurations() > 0 && cfg != cfg0) {
            if (LibUsb.setConfiguration(device, cfg0) < 0) {
                UsbCloseInternal()
                if (detachErrno == LibUsb.ERROR_ACCESS) throw LibUsbException(
                    "inappropriate permissions on device!",
                    -8
                ) else throw LibUsbException(
                    "unable to set usb configuration. Make sure the default Ftdi driver is not in use",
                    -3
                )
            }
        }
        if (LibUsb.claimInterface(device, iface) < 0) {
            UsbCloseInternal()
            if (detachErrno == LibUsb.ERROR_ACCESS) throw LibUsbException(
                "inappropriate permissions on device!",
                -8
            ) else throw LibUsbException(
                "unable to claim usb device. Make sure the default Ftdi driver is not in use",
                -5
            )
        }
        maxPacketSize = determinMaxPacketSize(dev, 512)
    }

    @JvmOverloads
    fun usbOpen(
        vendor: Short,
        product: Short,
        description: String? = null,
        serial: String? = null,
        index: Int = 0
    ): Boolean {
        var index = index
        val devs = DeviceList()
        if (LibUsb.getDeviceList(context, devs) < 0) throw LibUsbException("LibUsb.getDeviceList() failed", -12)
        try {
            for (dev in devs) {
                val desc = DeviceDescriptor()
                if (LibUsb.getDeviceDescriptor(
                        dev,
                        desc
                    ) < 0
                ) throw LibUsbException("LibUsb.getDeviceDescriptor() failed", -13)
                if (desc.idVendor() == vendor && desc.idProduct() == product) {
                    device = DeviceHandle()
                    if (LibUsb.open(dev, device) < 0) throw LibUsbException("LibUsb.open() failed", -4)
                    if (description != null) {
                        val sDesc = LibUsb.getStringDescriptor(device, desc.iProduct())
                        if (sDesc == null) {
                            UsbCloseInternal()
                            throw LibUsbException("unable to fetch product description", -8)
                        }
                        if (description != sDesc) {
                            UsbCloseInternal()
                            continue
                        }
                    }
                    if (serial != null) {
                        val sSer = LibUsb.getStringDescriptor(device, desc.iSerialNumber())
                        if (sSer == null) {
                            UsbCloseInternal()
                            throw LibUsbException("unable to fetch serial number", -9)
                        }
                        if (serial != sSer) {
                            UsbCloseInternal()
                            continue
                        }
                    }
                    UsbCloseInternal()
                    if (index > 0) {
                        index--
                        continue
                    }
                    usbOpenDev(dev)
                    return true
                }
            }
        } finally {
            LibUsb.freeDeviceList(devs, true)
        }
        return false
    }

    fun usbOpenBusAddr(bus: Int, addr: Int): Boolean {
        val devs = DeviceList()
        if (LibUsb.getDeviceList(context, devs) < 0) throw LibUsbException("LibUsb.getDeviceList() failed", -12)
        try {
            for (dev in devs) {
                if (LibUsb.getBusNumber(dev) === bus && LibUsb.getDeviceAddress(dev) === addr) {
                    usbOpenDev(dev)
                    return true
                }
            }
        } finally {
            LibUsb.freeDeviceList(devs, true)
        }
        return false
    }

    fun usbOpen(description: String?): Boolean {
        if (description == null || description.isEmpty() || description[1] != ':') throw LibUsbException(
            "illegal description format",
            -12
        )
        if (description[0] == 'd') {
            val devs = DeviceList()
            val p = Pattern.compile(".:(\\d+)/(\\d+)")
            val m = p.matcher(description)
            if (!m.matches() || m.groupCount() != 2) throw LibUsbException("illegal description format", -11)
            val busNumber: Int
            val deviceAddress: Int
            try {
                busNumber = m.group(1).toInt()
                deviceAddress = m.group(2).toInt()
            } catch (e: NumberFormatException) {
                throw LibUsbException("illegal description format", -11)
            }
            if (LibUsb.getDeviceList(context, devs) < 0) throw LibUsbException("LibUsb.getDeviceList() failed", -12)
            try {
                for (dev in devs) {
                    if (busNumber == LibUsb.getBusNumber(dev) && deviceAddress == LibUsb.getDeviceAddress(dev)) {
                        usbOpenDev(dev)
                        return true
                    }
                }
            } finally {
                LibUsb.freeDeviceList(devs, true)
            }
        } else if (description[0] == 'i' || description[0] == 's') {
            val p = Pattern.compile(".:([\\dx]+):([\\dx]+)(:(.+))?")
            val m = p.matcher(description)
            if (!m.matches()) throw LibUsbException("illegal description format", -11)
            val vendor: Int
            val product: Int
            var index = 0
            var serial: String? = null
            try {
                vendor = Integer.decode(m.group(1))
                product = Integer.decode(m.group(2))
            } catch (e: NumberFormatException) {
                throw LibUsbException("illegal description format", -11)
            }
            if (description[0] == 'i' && m.groupCount() == 4) {
                index = try {
                    Integer.decode(m.group(4))
                } catch (e: NumberFormatException) {
                    throw LibUsbException("illegal description format", -11)
                }
            }
            if (description[0] == 's') {
                if (m.groupCount() != 4) throw LibUsbException("illegal description format", -11)
                serial = m.group(4)
            }
            return usbOpen(vendor.toShort(), product.toShort(), null, serial, index)
        }
        throw LibUsbException("illegal description format", -11)
    }

    open fun usbClose(): Boolean {
        var rtn = true
        if (device != null) if (LibUsb.releaseInterface(device, iface) < 0) rtn = false
        UsbCloseInternal()
        return rtn
    }

    fun writeData(data: ByteArray): Int {
        var offset = 0
        val buf = ByteBuffer.allocateDirect(data.size)
        buf.put(data)
        val transferred = IntBuffer.allocate(1)
        if (device == null) throw LibUsbException("USB device unavailable", -666)
        while (offset < data.size) {
            buf.position(offset)
            var code: Int
            if (LibUsb.bulkTransfer(device, inEndPoint, buf, transferred, writeTimeout.toLong())
                    .also { code = it } < 0
            ) throw LibUsbException("usb bulk write failed", code)
            offset += transferred.get()
        }
        return offset
    }

    fun transferDataDone(tc: TransferControl): Int {
        var ret: Int
        while (tc.completed[0] == 0) {
            ret = LibUsb.handleEventsTimeoutCompleted(context, 0, tc.completed)
            if (ret < 0) {
                if (ret == LibUsb.ERROR_INTERRUPTED) continue
                LibUsb.cancelTransfer(tc.transfer)
                while (tc.completed[0] == 0) if (LibUsb.handleEventsTimeoutCompleted(
                        context,
                        0,
                        tc.completed
                    ) < 0
                ) break
                LibUsb.freeTransfer(tc.transfer)
                return ret
            }
        }
        ret = tc.offset
        tc.transfer?.let {
            if (it.status() != LibUsb.TRANSFER_COMPLETED) ret = -1
            LibUsb.freeTransfer(it)
        }
        return ret
    }

    fun transferDataCancel(tc: TransferControl, timeout: Long) {
        if (tc.completed[0] == 0 && tc.transfer != null) {
            LibUsb.cancelTransfer(tc.transfer)
            while (tc.completed[0] == 0) {
                if (LibUsb.handleEventsTimeoutCompleted(context, timeout, tc.completed) < 0) break
            }
        }
        if (tc.transfer != null) LibUsb.freeTransfer(tc.transfer)
    }

    fun readDataWithTimeout(data: ByteArray): Int {
        var buffer = ByteArray(data.size)
        var reqBytes = data.size
        var offset = 0
        val startTime = System.currentTimeMillis()
        while (reqBytes > 0) {
            if (buffer.size != reqBytes) buffer = ByteArray(reqBytes)
            val ct = readData(buffer)
            if (ct > 0) {
                System.arraycopy(buffer, 0, data, offset, ct)
                offset += ct
                reqBytes -= ct
            }
            if (System.currentTimeMillis() - startTime > readTimeout) {
                return data.size - reqBytes
            }
        }
        return data.size
    }

    open fun readData(buf: ByteArray): Int {
        var offset = 0
        var ret: Int
        val packet_size: Int
        val actual_length_buf = IntBuffer.allocate(1)
        var actual_length = 1
        if (device == null) throw LibUsbException("USB device unavailable", -666)

        // Packet size sanity check (avoid division by zero)
        packet_size = maxPacketSize
        if (packet_size == 0) throw LibUsbException("max_packet_size is bogus (zero)", -1)

        // everything we want is still in the readbuffer?
        if (buf!!.size <= readBuffer!!.remaining()) {
            readBuffer!![buf]
            return buf.size
        }
        // something still in the readbuffer, but not enough to satisfy 'size'?
        if (readBuffer!!.remaining() != 0) {
            offset += readBuffer!!.remaining()
            readBuffer!![buf, 0, readBuffer!!.remaining()]
        }
        // do the actual USB read
        while (offset < buf.size) {
            readBuffer!!.clear()
            /* returns how much received */actual_length_buf.clear()
            ret = LibUsb.bulkTransfer(device, outEndPoint, readBuffer, actual_length_buf, readTimeout.toLong())
            actual_length = actual_length_buf.get()
            readBuffer!!.limit(actual_length)
            if (ret < 0) throw LibUsbException("usb bulk read failed", ret)
            if (actual_length <= 0) {
                // no more data to read?
                resetReadBuffer()
                return offset
            }
            if (readBuffer!!.remaining() > 0) {
                // data still fits in buf?
                if (offset + actual_length <= buf.size) {
                    readBuffer!![buf, offset, actual_length]
                    offset += actual_length

                    /* Did we read exactly the right amount of bytes? */if (offset == buf.size) return offset
                } else {
                    // only copy part of the data or size <= readbuffer_chunksize
                    val part_size = buf.size - offset
                    readBuffer!![buf, offset, part_size]
                    offset += part_size
                    return offset
                }
            }
        }
        // never reached
        return -127
    }

    open fun flushReadBuffer() {
        resetReadBuffer()
        val dummyBuffer = ByteArray(maxPacketSize)
        val oldTimeout = readTimeout
        readTimeout = 100
        try {
            readData(dummyBuffer) // flush the buffer
        } catch (ignored: LibUsbException) {
        }
        readTimeout = oldTimeout
    }

    fun resetReadBuffer() {
        readBuffer!!.clear()
        readBuffer!!.limit(0)
    }

    fun readDataSetChunkSize(chunksize: Int) {
        /*
         * We can't set readbuffer_chunksize larger than MAX_BULK_BUFFER_LENGTH, which is defined in libusb-1.0. Otherwise, each USB read request will be divided into multiple
         * URBs. This will cause issues on Linux kernel older than 2.6.32.
         */
        var chunksize = chunksize
        if (Env.isLinux && chunksize > 16384) chunksize = 16384
        readBuffer = ByteBuffer.allocateDirect(chunksize).also { it.limit(0) }
        readBufferChunksize = chunksize
    }

    fun readDataGetChunkSize(): Int {
        return readBufferChunksize
    }

    companion object {
        protected var context: Context? = null
        val EMPTY_BUF = ByteBuffer.allocateDirect(0)
        protected fun UsbCloseInternal(dev: DeviceHandle?) {
            var dev: DeviceHandle? = dev
            if (dev != null) {
                LibUsb.close(dev)
                dev = null
            }
        }

        init {
            if (LibUsb.init(context) < 0) throw LibUsbException("LibUsb.init() failed", -3)
        }

        fun usbFindAll(vendor: Short, product: Short, description: String?, serial: String?): List<Device> {
            var device: DeviceHandle?
            // Read the USB device list
            val list = DeviceList()
            var result: Int = LibUsb.getDeviceList(null, list)
            if (result < 0) throw LibUsbException("Unable to get device list", result)
            val devices: ArrayList<Device> = ArrayList<Device>()
            try {
                // Iterate over all devices and scan for the right one
                for (dev in list) {
                    val desc = DeviceDescriptor()
                    result = LibUsb.getDeviceDescriptor(dev, desc)
                    if (result != LibUsb.SUCCESS) throw LibUsbException("Unable to read device descriptor", result)
                    if ((vendor.toInt() != 0 || product.toInt() != 0) && desc.idVendor() === vendor && desc.idProduct() === product || vendor.toInt() == 0 && product.toInt() == 0 && desc.idVendor() === 0x403.toShort() && (desc.idProduct() === 0x6001.toShort() || desc.idProduct() === 0x6010.toShort() || desc.idProduct() === 0x6011.toShort() || desc.idProduct() === 0x6014.toShort() || desc.idProduct() === 0x6015.toShort())) {
                        device = DeviceHandle()
                        if (LibUsb.open(dev, device) < 0) throw LibUsbException("LibUsb.open() failed", -4)
                        if (description != null) {
                            val sDesc: String = LibUsb.getStringDescriptor(device, desc.iProduct())
                            if (sDesc == null) {
                                UsbCloseInternal(device)
                                throw LibUsbException("unalbe to fetch product description", -8)
                            }
                            if (description != sDesc) {
                                UsbCloseInternal(device)
                                continue
                            }
                        }
                        if (serial != null) {
                            val sSer: String = LibUsb.getStringDescriptor(device, desc.iSerialNumber())
                            if (sSer == null) {
                                UsbCloseInternal(device)
                                throw LibUsbException("unalbe to fetch serial number", -9)
                            }
                            if (serial != sSer) {
                                UsbCloseInternal(device)
                                continue
                            }
                        }
                        UsbCloseInternal(device)
                        devices.add(dev)
                        LibUsb.refDevice(dev)
                    }
                }
            } finally {
                // Ensure the allocated device list is freed
                LibUsb.freeDeviceList(list, true)
            }

            // Device not found
            return devices
        }

        fun usbFindAll(descriptions: List<UsbUtil.UsbDescriptor>): List<UsbUtil.DeviceEntry> {
            var device: DeviceHandle?
            // Read the USB device list
            val list = DeviceList()
            var result: Int = LibUsb.getDeviceList(null, list)
            if (result < 0) throw LibUsbException("Unable to get device list", result)
            val devices: ArrayList<UsbUtil.DeviceEntry> = ArrayList()
            try {
                // Iterate over all devices and scan for the right one
                for (dev in list) {
                    val desc = DeviceDescriptor()
                    result = LibUsb.getDeviceDescriptor(dev, desc)
                    if (result != LibUsb.SUCCESS) throw LibUsbException("Unable to read device descriptor", result)
                    var vidPidMatch = false
                    for (udes in descriptions) {
                        if (udes.vid == desc.idVendor() && udes.pid == desc.idProduct()) {
                            vidPidMatch = true
                            break
                        }
                    }
                    if (vidPidMatch) {
                        device = DeviceHandle()
                        val code: Int = LibUsb.open(dev, device)
                        if (code < 0) {
                            continue
                        }
                        val sDesc: String = LibUsb.getStringDescriptor(device, desc.iProduct())
                        if (sDesc == null) {
                            UsbCloseInternal(device)
                            throw LibUsbException("unable to fetch product description", -8)
                        }
                        var match: UsbUtil.UsbDescriptor? = null
                        for (udes in descriptions) {
                            if (udes.vid == desc.idVendor() && udes.pid == desc.idProduct() && (udes.product == null || udes.product == sDesc)) {
                                match = udes
                                break
                            }
                        }
                        UsbCloseInternal(device)
                        if (match == null) continue
                        devices.add(UsbUtil.DeviceEntry(match, dev))
                        LibUsb.refDevice(dev)
                    }
                }
            } finally {
                // Ensure the allocated device list is freed
                LibUsb.freeDeviceList(list, true)
            }

            // Device not found
            return devices
        }

        fun listFree(devList: List<Device?>) {
            for (dev in devList) LibUsb.unrefDevice(dev)
        }

        fun entryListFree(devList: List<UsbUtil.DeviceEntry>) {
            for (dev in devList) LibUsb.unrefDevice(dev.device)
        }
    }
}
