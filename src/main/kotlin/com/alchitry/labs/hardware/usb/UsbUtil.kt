package com.alchitry.labs.hardware.usb


import com.alchitry.labs.Env
import com.alchitry.labs.Log
import com.alchitry.labs.hardware.usb.ftdi.Ftdi
import com.alchitry.labs.hardware.usb.ftdi.FtdiD2xx
import com.alchitry.labs.hardware.usb.ftdi.FtdiLibUSB
import com.alchitry.labs.hardware.usb.ftdi.enums.PortInterfaceType
import com.alchitry.labs.project.Board
import com.fazecast.jSerialComm.SerialPort
import net.sf.yad2xx.Device
import net.sf.yad2xx.FTDIException
import net.sf.yad2xx.FTDIInterface
import org.usb4java.LibUsb
import org.usb4java.LibUsbException
import java.io.Closeable

object UsbUtil {
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
        val detected = if (hasD2XX) {
            findAllD2xxDevices(Board.All).map { it.board }
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
    }

    private fun findAllD2xxDevices(board: List<Board>): List<D2xxDeviceEntry> {
        if (!hasD2XX) return emptyList()
        return FTDIInterface.getDevices().mapNotNull { d ->
            val desc = d.description
            for (b in board) {
                if (desc.isNotEmpty() && b.usbDescriptor.d2xxInterface.letterMatches(
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
    private fun findD2xxDevice(board: Board, deviceIndex: Int): Device? {
        if (!hasD2XX) return null
        return findAllD2xxDevices(listOf(board)).getOrNull(deviceIndex)?.device
    }

    fun openFtdiDevice(board: Board, deviceIndex: Int): Ftdi? {
        if (hasD2XX) {
            try {
                val dev = findD2xxDevice(board, deviceIndex)
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
            if (Env.isWindows) {
                try {
                    val d2xx = findD2xxDevice(board, deviceIndex)
                    if (d2xx != null) {
                        d2xx.open()
                        val portName = "COM" + d2xx.comPortNumber
                        d2xx.close()
                        //Util.println("Found board on $portName")
                        val port = SerialPort.getCommPort(portName)
                        if (port != null) {
                            val serial = GenericSerial(port)
                            if (serial.open()) return serial //else //Util.println(
                            //  "Failed to open serial port $portName",
                            //  true
                            // )
                        }
                    }
                } catch (e: FTDIException) {
                    Log.exception(e)
                }
            }

            val dev = getDevice(board, deviceIndex)
                ?: return null

            val device: SerialDevice

            device = FtdiLibUSB(dev.device, board.usbDescriptor.d2xxInterface)
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