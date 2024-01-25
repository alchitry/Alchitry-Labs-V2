package com.alchitry.labs2.project

import com.alchitry.labs2.Settings
import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.hardware.pinout.AuPinConverter
import com.alchitry.labs2.hardware.pinout.CuPinConverter
import com.alchitry.labs2.hardware.pinout.PinConverter
import com.alchitry.labs2.hardware.usb.UsbUtil
import com.alchitry.labs2.hardware.usb.ftdi.enums.PortInterfaceType
import com.alchitry.labs2.parsers.acf.AcfConverter
import com.alchitry.labs2.parsers.acf.LatticeConverter
import com.alchitry.labs2.parsers.acf.XilinxConverter
import com.alchitry.labs2.project.builders.IceCubeBuilder
import com.alchitry.labs2.project.builders.IceStormBuilder
import com.alchitry.labs2.project.builders.ProjectBuilder
import com.alchitry.labs2.project.builders.VivadoBuilder


sealed class Board {
    companion object {
        fun fromName(name: String): Board? =
            Board::class.allSealedObjects()
                .firstOrNull { it.name.equals(name, ignoreCase = true) || it.alias.equals(name, ignoreCase = true) }

        val All: List<Board> get() = Board::class.allSealedObjects()
    }

    val binName: String get() = name.lowercase().replace(" ", "_")

    abstract val name: String
    abstract val alias: String
    abstract val fpgaName: String
    abstract val usbDescriptor: UsbUtil.UsbDescriptor
    abstract val pinConverter: PinConverter
    abstract val acfConverter: AcfConverter
    abstract val projectBuilder: ProjectBuilder
    abstract val supportsRamLoading: Boolean

    sealed interface XilinxBoard {
        val bridgeFile: String
        val idCode: String
    }

    data object AlchitryAu : Board(), XilinxBoard {
        override val name = "Alchitry Au"
        override val alias = "Au"
        override val fpgaName = "xc7a35tftg256-1"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor(
                "Alchitry Au",
                0x0403.toShort(),
                0x6010.toShort(),
                "Alchitry Au",
                PortInterfaceType.INTERFACE_A
            )
        override val bridgeFile = "/bridges/au.bin"
        override val idCode = "0362D093"
        override val pinConverter = AuPinConverter
        override val acfConverter = XilinxConverter
        override val projectBuilder = VivadoBuilder
        override val supportsRamLoading = true
    }

    data object AlchitryAuPlus : Board(), XilinxBoard {
        override val name = "Alchitry Au+"
        override val alias = "Au+"
        override val fpgaName = "xc7a100tftg256-1"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor(
                "Alchitry Au+",
                0x0403.toShort(),
                0x6010.toShort(),
                "Alchitry Au+",
                PortInterfaceType.INTERFACE_A
            )
        override val bridgeFile = "/bridges/au_plus.bin"
        override val idCode = "13631093"
        override val pinConverter = AuPinConverter
        override val acfConverter = XilinxConverter
        override val projectBuilder = VivadoBuilder
        override val supportsRamLoading = true
    }

    data object AlchitryCu : Board() {
        override val name = "Alchitry Cu"
        override val alias = "Cu"
        override val fpgaName = "ICE40HX8K-CB132IC"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor(
                "Alchitry Cu",
                0x0403.toShort(),
                0x6010.toShort(),
                "Alchitry Cu",
                PortInterfaceType.INTERFACE_A
            )
        override val pinConverter = CuPinConverter
        override val acfConverter = LatticeConverter
        override val projectBuilder get() = if (Settings.useIceCube) IceCubeBuilder else IceStormBuilder
        override val supportsRamLoading = false
    }
}



