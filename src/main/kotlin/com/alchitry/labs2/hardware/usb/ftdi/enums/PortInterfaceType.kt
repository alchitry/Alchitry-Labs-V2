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
 * Port interface for chips with multiple interfaces
 *
 * @author Jesse Caulfield
 */
enum class PortInterfaceType(index: Int) {
    INTERFACE_ANY(0),
    INTERFACE_A(1),
    INTERFACE_B(2),
    INTERFACE_C(3),
    INTERFACE_D(4);

    val index: Short

    init {
        this.index = index.toShort()
    }

    fun letterMatches(letter: Char): Boolean {
        return when (this) {
            INTERFACE_ANY -> true
            INTERFACE_A -> letter == 'A'
            INTERFACE_B -> letter == 'B'
            INTERFACE_C -> letter == 'C'
            INTERFACE_D -> letter == 'D'
        }
    }

    fun interfaceNumber() = when (this) {
        INTERFACE_ANY, INTERFACE_A -> 0
        INTERFACE_B -> 1
        INTERFACE_C -> 2
        INTERFACE_D -> 3
    }

    fun inEndPoint(): Byte = when (this) {
        INTERFACE_ANY, INTERFACE_A -> 0x02
        INTERFACE_B -> 0x04
        INTERFACE_C -> 0x06
        INTERFACE_D -> 0x08
    }

    fun outEndPoint(): Byte = when (this) {
        INTERFACE_ANY, INTERFACE_A -> 0x81.toByte()
        INTERFACE_B -> 0x83.toByte()
        INTERFACE_C -> 0x84.toByte()
        INTERFACE_D -> 0x87.toByte()
    }

}
