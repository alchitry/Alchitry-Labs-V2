/* 
 * Copyright 2014-2016 Key Bridge LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alchitry.labs2.hardware.usb.ftdi.enums

/**
 * Flow Control
 *
 *
 * The FT245R, FT2232C (in FIFO mode) and FT245BM chips use their own
 * handshaking as an integral part of its design, by proper use of the TXE#
 * line. The FT232R, FT2232C (in UART mode) and FT232BM chips can use RTS/CTS,
 * DTR/DSR hardware or XOn/XOff software handshaking. It is highly recommended
 * that some form of handshaking be used.
 *
 *
 * It is strongly encouraged that flow control is used because it is impossible
 * to ensure that the Ftdi driver will always be scheduled. The chip can buffer
 * up to 384 bytes of data. Windows can 'starve' the driver program of time if
 * it is doing other things. The most obvious example of this is moving an
 * application around the screen with the mouse by grabbing its task bar. This
 * will result in a lot of graphics activity and data loss will occur if
 * receiving data at 115200 baud (as an example) with no handshaking.
 *
 *
 * If the data rate is low or data loss is acceptable then flow control may be
 * omitted.
 *
 * @author Jesse Caulfield
 */
enum class FlowControl(val bytecode: Short) {
    /**
     * None - this may result in data loss at high speeds.
     */
    DISABLE_FLOW_CTRL(0x0.toShort()),

    /**
     * RTS/CTS - 2 wire handshake. The device will transmit if CTS is active and
     * will drop RTS if it cannot receive any more.
     */
    RTS_CTS_HS((0x1 shl 8).toShort()),

    /**
     * DTR/DSR - 2 wire handshake. The device will transmit if DSR is active and
     * will drop DTR if it cannot receive any more.
     */
    DTR_DSR_HS((0x2 shl 8).toShort()),

    /**
     * XON/XOFF - flow control is done by sending or receiving special characters.
     * One is XOn (transmit on) the other is XOff (transmit off). They are
     * individually programmable to any value.
     */
    XON_XOFF_HS((0x4 shl 8).toShort())

}
