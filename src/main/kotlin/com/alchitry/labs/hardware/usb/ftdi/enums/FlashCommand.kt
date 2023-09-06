package com.alchitry.labs.hardware.usb.ftdi.enums

enum class FlashCommand(/* Reset Device */
                        val command: Byte
) {
    WE(0x06.toByte()),

    /* Write Enable */
    SRWE(0x50.toByte()),

    /* Volatile SR Write Enable */
    WD(0x04.toByte()),

    /* Write Disable */
    RPD(0xAB.toByte()),

    /* Release Power-Down, returns Device ID */
    MFGID(0x90.toByte()),

    /* Read Manufacturer/Device ID */
    JEDECID(0x9F.toByte()),

    /* Read JEDEC ID */
    UID(0x4B.toByte()),

    /* Read Unique ID */
    RD(0x03.toByte()),

    /* Read Data */
    FR(0x0B.toByte()),

    /* Fast Read */
    PP(0x02.toByte()),

    /* Page Program */
    SE(0x20.toByte()),

    /* Sector Erase 4kb */
    BE32(0x52.toByte()),

    /* Block Erase 32kb */
    BE64(0xD8.toByte()),

    /* Block Erase 64kb */
    CE(0xC7.toByte()),

    /* Chip Erase */
    RSR1(0x05.toByte()),

    /* Read Status Register 1 */
    WSR1(0x01.toByte()),

    /* Write Status Register 1 */
    RSR2(0x35.toByte()),

    /* Read Status Register 2 */
    WSR2(0x31.toByte()),

    /* Write Status Register 2 */
    RSR3(0x15.toByte()),

    /* Read Status Register 3 */
    WSR3(0x11.toByte()),

    /* Write Status Register 3 */
    RSFDP(0x5A.toByte()),

    /* Read SFDP Register */
    ESR(0x44.toByte()),

    /* Erase Security Register */
    PSR(0x42.toByte()),

    /* Program Security Register */
    RSR(0x48.toByte()),

    /* Read Security Register */
    GBL(0x7E.toByte()),

    /* Global Block Lock */
    GBU(0x98.toByte()),

    /* Global Block Unlock */
    RBL(0x3D.toByte()),

    /* Read Block Lock */
    RPR(0x3C.toByte()),

    /* Read Sector Protection Registers (adesto) */
    IBL(0x36.toByte()),

    /* Individual Block Lock */
    IBU(0x39.toByte()),

    /* Individual Block Unlock */
    EPS(0x75.toByte()),

    /* Erase / Program Suspend */
    EPR(0x7A.toByte()),

    /* Erase / Program Resume */
    PD(0xB9.toByte()),

    /* Power-down */
    QPI(0x38.toByte()),

    /* Enter QPI mode */
    ERESET(0x66.toByte()),

    /* Enable Reset */
    RESET(0x99.toByte())

}
