package com.alchitry.labs2.hardware.usb.ftdi.enums

enum class FlashCommand(val command: Byte) {
    /** Write Enable */
    WE(0x06.toByte()),

    /** Volatile SR Write Enable */
    SRWE(0x50.toByte()),

    /** Write Disable */
    WD(0x04.toByte()),

    /** Release Power-Down, returns Device ID */
    RPD(0xAB.toByte()),

    /** Read Manufacturer/Device ID */
    MFGID(0x90.toByte()),

    /** Read JEDEC ID */
    JEDECID(0x9F.toByte()),

    /** Read Unique ID */
    UID(0x4B.toByte()),

    /** Read Data */
    RD(0x03.toByte()),

    /** Fast Read */
    FR(0x0B.toByte()),

    /** Page Program */
    PP(0x02.toByte()),

    /** Sector Erase 4kb */
    SE(0x20.toByte()),

    /** Block Erase 32kb */
    BE32(0x52.toByte()),

    /** Block Erase 64kb */
    BE64(0xD8.toByte()),

    /** Chip Erase */
    CE(0xC7.toByte()),

    /** Read Status Register 1 */
    RSR1(0x05.toByte()),

    /** Write Status Register 1 */
    WSR1(0x01.toByte()),

    /** Read Status Register 2 */
    RSR2(0x35.toByte()),

    /** Write Status Register 2 */
    WSR2(0x31.toByte()),

    /** Read Status Register 3 */
    RSR3(0x15.toByte()),

    /** Write Status Register 3 */
    WSR3(0x11.toByte()),

    /** Read SFDP Register */
    RSFDP(0x5A.toByte()),

    /** Erase Security Register */
    ESR(0x44.toByte()),

    /** Program Security Register */
    PSR(0x42.toByte()),

    /** Read Security Register */
    RSR(0x48.toByte()),

    /** Global Block Lock */
    GBL(0x7E.toByte()),

    /** Global Block Unlock */
    GBU(0x98.toByte()),

    /** Read Block Lock */
    RBL(0x3D.toByte()),

    /** Read Sector Protection Registers (adesto) */
    RPR(0x3C.toByte()),

    /** Individual Block Lock */
    IBL(0x36.toByte()),

    /** Individual Block Unlock */
    IBU(0x39.toByte()),

    /** Erase / Program Suspend */
    EPS(0x75.toByte()),

    /** Erase / Program Resume */
    EPR(0x7A.toByte()),

    /** Power-down */
    PD(0xB9.toByte()),

    /** Enter QPI mode */
    QPI(0x38.toByte()),

    /** Enable Reset */
    ERESET(0x66.toByte()),

    /** Reset Device */
    RESET(0x99.toByte())
}
