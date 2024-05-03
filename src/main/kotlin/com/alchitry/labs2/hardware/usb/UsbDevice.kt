package com.alchitry.labs2.hardware.usb

import com.alchitry.labs2.hardware.usb.ftdi.enums.DetachMode
import com.alchitry.labs2.project.Board
import org.usb4java.*
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.IntBuffer

open class UsbDevice(
    dev: Device,
    val interfaceNumber: Int,
    val inEndPoint: Byte,
    val outEndPoint: Byte,
    detachMode: DetachMode = DetachMode.AUTO_DETACH_REATACH_SIO_MODULE
) : Closeable {
    protected val context = Context()
    protected val device: DeviceHandle
    protected val readBuffer: ByteBuffer
    private val readBufferChunksize: Int
    private val writeBufferChunksize: Int
    protected var maxPacketSize: Int

    var readTimeout = 0
    var writeTimeout = 0

    data class DeviceStrings(
        val manufacture: String?,
        val product: String?,
        val serial: String?,
    )

    init {
        if (LibUsb.init(context) != 0) throw LibUsbException("LibUsb.init() failed", -3)

        readTimeout = 5000
        writeTimeout = 5000
        writeBufferChunksize = 16384

        /*
         * We can't set readbuffer_chunksize larger than MAX_BULK_BUFFER_LENGTH, which is defined in libusb-1.0. Otherwise, each USB read request will be divided into multiple
         * URBs. This will cause issues on Linux kernel older than 2.6.32.
         */
        val chunkSize = 16384
        readBuffer = ByteBuffer.allocateDirect(chunkSize).also { it.limit(0) }
        readBufferChunksize = chunkSize

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
            if (LibUsb.detachKernelDriver(device, interfaceNumber).also { errno = it } != LibUsb.SUCCESS) detachErrno =
                errno
        } else if (DetachMode.AUTO_DETACH_REATACH_SIO_MODULE == detachMode) {
            if (LibUsb.setAutoDetachKernelDriver(device, true).also { errno = it } != LibUsb.SUCCESS) detachErrno =
                errno
            if (detachErrno == LibUsb.ERROR_NOT_SUPPORTED) if (LibUsb.detachKernelDriver(device, interfaceNumber)
                    .also { errno = it } != LibUsb.SUCCESS
            ) detachErrno = errno
        }
        if (LibUsb.getConfiguration(device, cfgBuf) < 0) throw LibUsbException("LibUsb.getConfiguration() failed", -12)
        cfg = cfgBuf[0]
        if (desc.bNumConfigurations() > 0 && cfg != cfg0) {
            if (LibUsb.setConfiguration(device, cfg0) < 0) {
                LibUsb.close(device)
                if (detachErrno == LibUsb.ERROR_ACCESS) throw LibUsbException(
                    "inappropriate permissions on device!",
                    -8
                ) else throw LibUsbException(
                    "unable to set usb configuration. Make sure the default Ftdi driver is not in use",
                    -3
                )
            }
        }
        if (LibUsb.claimInterface(device, interfaceNumber) < 0) {
            LibUsb.close(device)
            if (detachErrno == LibUsb.ERROR_ACCESS) throw LibUsbException(
                "inappropriate permissions on device!",
                -8
            ) else throw LibUsbException(
                "unable to claim usb device. Make sure the default Ftdi driver is not in use",
                -5
            )
        }
        maxPacketSize = determinMaxPacketSize(dev, 512)
        check(maxPacketSize > 0) { "maxPacketSize was not greater than 0" }
    }

    override fun close() {
        LibUsb.releaseInterface(device, interfaceNumber)
        LibUsb.close(device)
        LibUsb.exit(context)
    }

    private fun determinMaxPacketSize(dev: Device, defSize: Int): Int {
        var packetSize = defSize
        val desc = DeviceDescriptor()
        val config = ConfigDescriptor()
        if (LibUsb.getDeviceDescriptor(dev, desc) < 0) return packetSize
        if (LibUsb.getConfigDescriptor(dev, 0.toByte(), config) < 0) return packetSize
        if (desc.bNumConfigurations() > 0) {
            if (interfaceNumber < desc.bNumConfigurations()) {
                val iface: Interface = config.iface().first()
                if (iface.numAltsetting() > 0) {
                    val descriptor: InterfaceDescriptor = iface.altsetting().first()
                    if (descriptor.bNumEndpoints() > 0) {
                        packetSize = descriptor.endpoint().first().wMaxPacketSize().toInt()
                    }
                }
            }
        }
        LibUsb.freeConfigDescriptor(config)
        return packetSize
    }

    fun writeData(data: ByteArray): Int {
        var offset = 0
        val buf = ByteBuffer.allocateDirect(data.size)
        buf.put(data)
        val transferred = IntBuffer.allocate(1)
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

    open fun readData(data: ByteArray): Int {
        val readBuffer = readBuffer ?: return 0

        var offset = 0
        var ret: Int
        val actualLengthBuf = IntBuffer.allocate(1)
        var actualLength = 1

        // everything we want is still in the readbuffer?
        if (data.size <= readBuffer.remaining()) {
            readBuffer[data]
            return data.size
        }
        // something still in the readbuffer, but not enough to satisfy 'size'?
        if (readBuffer.remaining() != 0) {
            offset += readBuffer.remaining()
            readBuffer[data, 0, readBuffer.remaining()]
        }
        // do the actual USB read
        while (offset < data.size) {
            readBuffer.clear()
            /* returns how much received */
            actualLengthBuf.clear()
            ret = LibUsb.bulkTransfer(device, outEndPoint, readBuffer, actualLengthBuf, readTimeout.toLong())
            actualLength = actualLengthBuf.get()
            readBuffer.limit(actualLength)
            if (ret < 0) throw LibUsbException("usb bulk read failed", ret)
            if (actualLength <= 0) {
                // no more data to read?
                resetReadBuffer()
                return offset
            }
            if (readBuffer.remaining() > 0) {
                // data still fits in buf?
                if (offset + actualLength <= data.size) {
                    readBuffer[data, offset, actualLength]
                    offset += actualLength

                    /* Did we read exactly the right number of bytes? */if (offset == data.size) return offset
                } else {
                    // only copy part of the data or size <= readbuffer_chunksize
                    val part_size = data.size - offset
                    readBuffer[data, offset, part_size]
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
        readBuffer.clear()
        readBuffer.limit(0)
    }


    companion object {
        private fun getDeviceList(): DeviceList {
            if (LibUsb.init(null) != 0) throw LibUsbException("LibUsb.init() failed", -3)
            // Read the USB device list
            val list = DeviceList()
            val result: Int = LibUsb.getDeviceList(null, list)
            if (result < 0) throw LibUsbException("Unable to get device list", result)
            return list
        }

        fun usbFindAll(vendor: Short, product: Short, description: String?, serial: String?): List<Device> {
            val list = getDeviceList()
            val devices = try {
                list.mapNotNull { dev ->
                    val desc = DeviceDescriptor()
                    val result = LibUsb.getDeviceDescriptor(dev, desc)
                    if (result != LibUsb.SUCCESS) throw LibUsbException("Unable to read device descriptor", result)
                    if ((vendor.toInt() != 0 || product.toInt() != 0) && desc.idVendor() == vendor && desc.idProduct() == product || vendor.toInt() == 0 && product.toInt() == 0 && desc.idVendor() === 0x403.toShort() && (desc.idProduct() === 0x6001.toShort() || desc.idProduct() === 0x6010.toShort() || desc.idProduct() === 0x6011.toShort() || desc.idProduct() === 0x6014.toShort() || desc.idProduct() === 0x6015.toShort())) {
                        val device = DeviceHandle()
                        if (LibUsb.open(dev, device) < 0) throw LibUsbException("LibUsb.open() failed", -4)
                        if (description != null) {
                            val sDesc = LibUsb.getStringDescriptor(device, desc.iProduct())
                            if (sDesc == null) {
                                LibUsb.close(device)
                                throw LibUsbException("unable to fetch product description", -8)
                            }
                            if (description != sDesc) {
                                LibUsb.close(device)
                                return@mapNotNull null
                            }
                        }
                        if (serial != null) {
                            val sSer = LibUsb.getStringDescriptor(device, desc.iSerialNumber())
                            if (sSer == null) {
                                LibUsb.close(device)
                                throw LibUsbException("unable to fetch serial number", -9)
                            }
                            if (serial != sSer) {
                                LibUsb.close(device)
                                return@mapNotNull null
                            }
                        }
                        LibUsb.close(device)
                        LibUsb.refDevice(dev)
                        return@mapNotNull dev
                    }
                    return@mapNotNull null
                }
            } finally {
                // Ensure the allocated device list is freed
                LibUsb.freeDeviceList(list, true)
            }

            // Device not found
            return devices
        }

        fun usbFindAll(boards: List<Board>): List<UsbUtil.DeviceEntry> {
            if (LibUsb.init(null) != 0) throw LibUsbException("LibUsb.init() failed", -3)

            // Read the USB device list
            val list = DeviceList()
            var result: Int = LibUsb.getDeviceList(null, list)
            if (result < 0) throw LibUsbException("Unable to get device list", result)
            val devices = mutableListOf<UsbUtil.DeviceEntry>()
            try {
                // Iterate over all devices and scan for the right one
                for (dev in list) {
                    val desc = DeviceDescriptor()
                    result = LibUsb.getDeviceDescriptor(dev, desc)
                    if (result != LibUsb.SUCCESS) throw LibUsbException("Unable to read device descriptor", result)

                    if (boards.any { it.usbDescriptor.vid == desc.idVendor() && it.usbDescriptor.pid == desc.idProduct() }) {
                        val device = DeviceHandle()
                        val code: Int = LibUsb.open(dev, device)
                        if (code < 0) {
                            continue
                        }
                        val sDesc = LibUsb.getStringDescriptor(device, desc.iProduct())
                        if (sDesc == null) {
                            LibUsb.close(device)
                            throw LibUsbException("unable to fetch product description", -8)
                        }
                        val match = boards.firstOrNull {
                            it.usbDescriptor.vid == desc.idVendor() && it.usbDescriptor.pid == desc.idProduct() && (it.usbDescriptor.product == null || it.usbDescriptor.product == sDesc)
                        }

                        LibUsb.close(device)
                        if (match == null) continue
                        devices.add(UsbUtil.DeviceEntry(match, dev))
                        LibUsb.refDevice(dev)
                    }
                }
            } finally {
                // Ensure the allocated device list is freed
                LibUsb.freeDeviceList(list, true)
            }

            return devices
        }

        fun listFree(devList: List<Device>) {
            for (dev in devList) LibUsb.unrefDevice(dev)
        }

        fun entryListFree(devList: List<UsbUtil.DeviceEntry>) {
            for (dev in devList) LibUsb.unrefDevice(dev.device)
        }
    }
}
