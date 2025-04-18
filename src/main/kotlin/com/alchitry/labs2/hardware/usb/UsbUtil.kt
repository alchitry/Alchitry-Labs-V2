package com.alchitry.labs2.hardware.usb


import com.alchitry.labs2.Env
import com.alchitry.labs2.Log
import com.alchitry.labs2.hardware.Board
import com.alchitry.labs2.hardware.usb.ftdi.Ftdi
import com.alchitry.labs2.hardware.usb.ftdi.FtdiD2xx
import com.alchitry.labs2.hardware.usb.ftdi.FtdiLibUSB
import com.alchitry.labs2.hardware.usb.ftdi.enums.PortInterfaceType
import com.fazecast.jSerialComm.SerialPort
import kotlinx.coroutines.sync.Mutex
import net.sf.yad2xx.Device
import net.sf.yad2xx.FTDIException
import net.sf.yad2xx.FTDIInterface
import org.usb4java.LibUsb
import org.usb4java.LibUsbException
import java.io.Closeable

object UsbUtil {
    val lock = Mutex()

    val hasD2XX = try {
        FTDIInterface.getDeviceCount() // attempt to load class, fails if the D2XX driver isn't found
        true
    } catch (e: Throwable) {
        false
    }

    fun getDevice(board: Board, deviceIndex: Int): DeviceEntry? {
        val devs = UsbDevice.usbFindAll(listOf(board))
        val dev = devs.getOrNull(deviceIndex)?.let {
            LibUsb.refDevice(it.device)
            DeviceEntry(it.board, LibUsb.refDevice(it.device))
        }
        UsbDevice.entryListFree(devs)
        return dev
    }

    fun detectAttachedBoards(): Map<Board, Int> {
        try {
            val detected = if (hasD2XX) {
                findAllD2xxDevices(Board.All, false).map { it.board }
            } else {
                val devices = UsbDevice.usbFindAll(Board.All)
                val boards = devices.map { it.board }
                UsbDevice.entryListFree(devices)
                boards
            }
            val map = mutableMapOf<Board, Int>()
            detected.forEach { board ->
                map[board] = map.getOrDefault(board, 0) + 1
            }
            return map
        } catch (e: Exception) {
            Log.printlnError(e.message)
            return emptyMap()
        }
    }

    private fun findAllD2xxDevices(board: List<Board>, serialDescriptor: Boolean): List<D2xxDeviceEntry> {
        if (!hasD2XX) return emptyList()
        return FTDIInterface.getDevices().mapNotNull { d ->
            val desc = d.description
            for (b in board) {
                val usbDescriptor = if (serialDescriptor) b.serialUsbDescriptor else b.usbDescriptor
                if (desc.isNotEmpty() && usbDescriptor.d2xxInterface.letterMatches(
                        desc[(desc.length - 1).coerceAtLeast(
                            0
                        )]
                    )
                ) {
                    val product = desc.substring(0, desc.length - 2)
                    if (product == b.usbDescriptor.product) {
                        return@mapNotNull D2xxDeviceEntry(b, d)
                    }
                }
            }
            null
        }
    }

    @Throws(FTDIException::class)
    private fun findD2xxDevice(board: Board, deviceIndex: Int, serialDescriptor: Boolean): Device? {
        if (!hasD2XX) return null
        return findAllD2xxDevices(listOf(board), serialDescriptor).getOrNull(deviceIndex)?.device
    }

    fun openFtdiDevice(board: Board, deviceIndex: Int): Ftdi? {
        if (hasD2XX) {
            try {
                val dev = findD2xxDevice(board, deviceIndex, false)
                if (dev != null) {
                    dev.open()
                    return FtdiD2xx(dev)
                }
            } catch (e: FTDIException) {
                Log.exception(e)
            }
        } else {
            try {
                val dev = getDevice(board, deviceIndex) ?: return null
                val ftdi = FtdiLibUSB(dev.device, board.usbDescriptor.d2xxInterface)
                LibUsb.unrefDevice(dev.device)
                return ftdi
            } catch (e: LibUsbException) {
                Log.exception(e)
            }
        }
        return null
    }

    fun openSerial(board: Board, deviceIndex: Int): SerialDevice? {
        try {
            if (hasD2XX) {
                try {
                    val d2xx = findD2xxDevice(board, deviceIndex, true) ?: return null
                    d2xx.open()
                    if (Env.isWindows) {
                        val portName = "COM" + d2xx.comPortNumber
                        d2xx.close()
                        val port = SerialPort.getCommPort(portName)
                        if (port != null) {
                            val serial = GenericSerial(port)
                            if (serial.open()) {
                                return serial
                            } else {
                                Log.error("Failed to open generic serial port: $portName")
                            }
                        }
                    } else {
                        return FtdiD2xx(d2xx)
                    }
                } catch (e: FTDIException) {
                    Log.exception(e)
                }
                return null
            }

            val dev = getDevice(board, deviceIndex)
            if (dev == null) {
                Log.error("Failed to get device: $board[$deviceIndex]")
                return null
            }

            val device: SerialDevice = FtdiLibUSB(dev.device, board.serialUsbDescriptor.d2xxInterface)
            LibUsb.unrefDevice(dev.device)
            return device
        } catch (e: LibUsbException) {
            Log.exception(e)
            return null
        }
    }

    data class UsbDescriptor(
        val name: String,
        val vid: Short,
        val pid: Short,
        val product: String?,
        val d2xxInterface: PortInterfaceType
    )

    data class DeviceEntry(val board: Board, val device: org.usb4java.Device) : Closeable {
        override fun close() {
            LibUsb.unrefDevice(device)
        }
    }

    data class D2xxDeviceEntry(val board: Board, val device: Device)
}