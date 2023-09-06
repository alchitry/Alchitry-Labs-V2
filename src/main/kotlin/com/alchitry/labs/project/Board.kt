package com.alchitry.labs.project

import com.alchitry.labs.allSealedObjects
import com.alchitry.labs.hardware.usb.UsbUtil


sealed class Board {
    companion object {
        fun fromName(name: String): Board? =
            Board::class.allSealedObjects().firstOrNull { it.name.equals(name, ignoreCase = true) }

        val All: List<Board> get() = Board::class.allSealedObjects()
    }

    abstract val name: String
    abstract val fpgaName: String
    abstract val usbDescriptor: UsbUtil.UsbDescriptor

    data object AlchitryAu : Board() {
        override val name = "Alchitry Au"
        override val fpgaName = "xc7a35tftg256-1"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor("Alchitry Au", 0x0403.toShort(), 0x6010.toShort(), "Alchitry Au")
    }

    data object AlchitryAuPlus : Board() {
        override val name = "Alchitry Au+"
        override val fpgaName = "xc7a100tftg256-1"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor("Alchitry Au+", 0x0403.toShort(), 0x6010.toShort(), "Alchitry Au+")
    }

    data object AlchitryCu : Board() {
        override val name = "Alchitry Cu"
        override val fpgaName = "ICE40HX8K-CB132IC"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor("Alchitry Cu", 0x0403.toShort(), 0x6010.toShort(), "Alchitry Cu")
    }
}



