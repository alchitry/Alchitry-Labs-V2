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
package com.alchitry.labs.hardware.usb.ftdi.enums

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
        return false
    }
}
