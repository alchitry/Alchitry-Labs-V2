package com.alchitry.labs.hardware.usb


import com.alchitry.labs.Env
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

object UsbUtil {
    fun getDevice(boards: List<Board>): DeviceEntry? {
        var dev: DeviceEntry? = null
        var result: DeviceEntry? = null
        val devs = UsbDevice.usbFindAll(Board.All)
        if (devs.size == 1) {
            dev = devs.first()
        } else if (devs.size > 1) {
            val devList: MutableList<String> = ArrayList()
            devs.forEachIndexed { i, it -> devList.add("${i + 1}: ${it.board.usbDescriptor.name}") }
            TODO("Select device when more than one present")
//            val dsr = DeviceSelectorRunnable(devList)
//            runBlocking(Dispatchers.SWT) { dsr.run() }
//            if (dsr.result != null) {
//                dev = devs[devList.indexOf(dsr.result)]
//                //Util.println("Selected ${dsr.result}")
//            }
        }
        if (dev != null) {
            result = DeviceEntry(dev.board, LibUsb.refDevice(dev.device))
        }
        UsbDevice.entryListFree(devs)
        return result
    }

    @Throws(FTDIException::class)
    private fun findD2xxDevice(iface: PortInterfaceType, board: List<Board>): Device? {
        val devices = FTDIInterface.getDevices()
        for (d in devices) {
            val desc = d.description
            if (desc.isNotEmpty() && iface.letterMatches(desc[(desc.length - 1).coerceAtLeast(0)])) {
                val product = desc.substring(0, desc.length - 2)
                for (b in board) if (product == b.usbDescriptor.product) {
                    return d
                }
            }
        }
        return null
    }

    fun openFtdiDevice(iface: PortInterfaceType, board: List<Board>): Ftdi? {
        if (Env.isWindows) {
            try {
                val dev = findD2xxDevice(iface, board)
                if (dev != null) {
                    dev.open()
                    return FtdiD2xx(dev)
                }
            } catch (e: FTDIException) {
                TODO("Report exceptions: $e")
            }
        }
        try {
            val dev = getDevice(board) ?: return null
            val ftdi = FtdiLibUSB(dev.device, iface)
            LibUsb.unrefDevice(dev.device)
            return ftdi
        } catch (e: LibUsbException) {
            TODO("Report exceptions: $e")
        }
    }


    @JvmStatic
    @JvmOverloads
    fun openSerial(devices: List<Board> = Board.All): SerialDevice? {
        try {
            if (Env.isWindows) {
                try {
                    val d2xx = findD2xxDevice(PortInterfaceType.INTERFACE_B, devices)
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
                    TODO("Report exceptions: $e")
                }
            }

            val dev = getDevice(devices)

            if (dev == null) {
                // Util.println("No devices found...", true)
                return null
            }

            val device: SerialDevice

            device = FtdiLibUSB(dev.device, PortInterfaceType.INTERFACE_B)
            LibUsb.unrefDevice(dev.device)
            return device
        } catch (e: LibUsbException) {
            TODO("Report exceptions: $e")
            return null
        }
    }

    data class UsbDescriptor(val name: String, val vid: Short, val pid: Short, val product: String?)
    data class DeviceEntry(val board: Board, val device: org.usb4java.Device)
}