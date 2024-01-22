package com.alchitry.labs2.hardware.usb.ftdi.enums

enum class MpsseCommand(/* CPUMode write extended address */
                        val command: Byte
) {
    SETB_LOW(0x80.toByte()),

    /* Set Data bits LowByte */
    READB_LOW(0x81.toByte()),

    /* Read Data bits LowByte */
    SETB_HIGH(0x82.toByte()),

    /* Set Data bits HighByte */
    READB_HIGH(0x83.toByte()),

    /* Read data bits HighByte */
    LOOPBACK_EN(0x84.toByte()),

    /* Enable loopback */
    LOOPBACK_DIS(0x85.toByte()),

    /* Disable loopback */
    SET_CLK_DIV(0x86.toByte()),

    /* Set clock divisor */
    FLUSH(0x87.toByte()),

    /* Flush buffer fifos to the PC. */
    WAIT_H(0x88.toByte()),

    /* Wait on GPIOL1 to go high. */
    WAIT_L(0x89.toByte()),

    /* Wait on GPIOL1 to go low. */
    TCK_X5(0x8A.toByte()),

    /* Disable /5 div, enables 60MHz master clock */
    TCK_D5(0x8B.toByte()),

    /* Enable /5 div, backward compat to FT2232D */
    EN_3PH_CLK(0x8C.toByte()),

    /* Enable 3 phase clk, DDR I2C */
    DIS_3PH_CLK(0x8D.toByte()),

    /* Disable 3 phase clk */
    CLK_N(0x8E.toByte()),

    /* Clock every bit, used for JTAG */
    CLK_N8(0x8F.toByte()),

    /* Clock every byte, used for JTAG */
    CLK_TO_H(0x94.toByte()),

    /* Clock until GPIOL1 goes high */
    CLK_TO_L(0x95.toByte()),

    /* Clock until GPIOL1 goes low */
    EN_ADPT_CLK(0x96.toByte()),

    /* Enable adaptive clocking */
    DIS_ADPT_CLK(0x97.toByte()),

    /* Disable adaptive clocking */
    CLK8_TO_H(0x9C.toByte()),

    /* Clock until GPIOL1 goes high, count bytes */
    CLK8_TO_L(0x9D.toByte()),

    /* Clock until GPIOL1 goes low, count bytes */
    TRI(0x9E.toByte()),

    /* Set IO to only drive on 0 and tristate on 1 */ /* CPU mode commands */
    CPU_RS(0x90.toByte()),

    /* CPUMode read short address */
    CPU_RE(0x91.toByte()),

    /* CPUMode read extended address */
    CPU_WS(0x92.toByte()),

    /* CPUMode write short address */
    CPU_WE(0x93.toByte())

}
