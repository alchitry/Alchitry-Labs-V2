package com.alchitry.labs2.hardware

import com.alchitry.labs2.Settings
import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.hardware.pinout.*
import com.alchitry.labs2.hardware.usb.UsbUtil
import com.alchitry.labs2.hardware.usb.ftdi.enums.PortInterfaceType
import com.alchitry.labs2.parsers.acf.AcfConverter
import com.alchitry.labs2.parsers.acf.LatticeConverter
import com.alchitry.labs2.parsers.acf.XilinxConverter
import com.alchitry.labs2.project.builders.IceCubeBuilder
import com.alchitry.labs2.project.builders.IceStormBuilder
import com.alchitry.labs2.project.builders.ProjectBuilder
import com.alchitry.labs2.project.builders.VivadoBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object BoardSerializer : KSerializer<Board> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Board", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Board) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Board {
        val name = decoder.decodeString()
        return Board.fromName(name) ?: error("Unknown board type: $name")
    }
}

@Serializable(with = BoardSerializer::class)
sealed class Board {
    companion object {
        fun fromName(name: String): Board? =
            Board::class.allSealedObjects()
                .firstOrNull { it.name.equals(name, ignoreCase = true) || it.alias.equals(name, ignoreCase = true) }

        val All: List<Board> = listOf(AlchitryAuV2, AlchitryAu, AlchitryAuPlus, AlchitryCu)
    }

    val binName: String get() = name.lowercase().replace(" ", "_")

    abstract val name: String
    abstract val alias: String
    abstract val fpgaName: String
    abstract val usbDescriptor: UsbUtil.UsbDescriptor
    abstract val serialUsbDescriptor: UsbUtil.UsbDescriptor
    abstract val pinConverters: List<PinConverter>
    abstract val acfConverter: AcfConverter
    abstract val projectBuilder: ProjectBuilder
    abstract val supportsRamLoading: Boolean

    sealed interface XilinxBoard {
        val bridgeFile: String
        val idCode: String
    }

    data object AlchitryAuV2 : Board(), XilinxBoard {
        override val name = "Alchitry Au V2"
        override val alias = "AuV2"
        override val fpgaName = "xc7a35tftg256-2"
        override val usbDescriptor =
            UsbUtil.UsbDescriptor(
                "Alchitry Au V2",
                0x0403.toShort(),
                0x6010.toShort(),
                "Alchitry Au V2",
                PortInterfaceType.INTERFACE_A
            )
        override val serialUsbDescriptor = usbDescriptor.copy(d2xxInterface = PortInterfaceType.INTERFACE_B)
        override val bridgeFile = "/bridges/au_v2.bin"
        override val idCode = "0362D093"
        override val pinConverters = listOf(AuV2Pin, AuV2toV1Pin)
        override val acfConverter = XilinxConverter
        override val projectBuilder = VivadoBuilder
        override val supportsRamLoading = true
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
        override val serialUsbDescriptor = usbDescriptor.copy(d2xxInterface = PortInterfaceType.INTERFACE_B)
        override val bridgeFile = "/bridges/au.bin"
        override val idCode = "0362D093"
        override val pinConverters = listOf(AuPin)
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
        override val serialUsbDescriptor = usbDescriptor.copy(d2xxInterface = PortInterfaceType.INTERFACE_B)
        override val bridgeFile = "/bridges/au_plus.bin"
        override val idCode = "13631093"
        override val pinConverters = listOf(AuPin)
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
        override val serialUsbDescriptor = usbDescriptor.copy(d2xxInterface = PortInterfaceType.INTERFACE_B)
        override val pinConverters = listOf(CuPin)
        override val acfConverter = LatticeConverter
        override val projectBuilder get() = if (Settings.useIceCube) IceCubeBuilder else IceStormBuilder
        override val supportsRamLoading = false
    }
}



