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
 * LineParity mode for ftdi_set_line_property()
 *
 * @author Jesse Caulfield
 */
enum class LineParity(val parity: Int) {
    /**
     * No line parity.
     */
    NONE(0),

    /**
     * The parity bit is set to one if the number of ones in a given set of bits
     * is even (making the total number of ones, including the parity bit, odd).
     */
    ODD(1),

    /**
     * The parity bit is set to one if the number of ones in a given set of bits
     * is odd (making the total number of ones, including the parity bit, even).
     */
    EVEN(2),

    /**
     * The parity bit is always 1.
     */
    MARK(3),

    /**
     * The parity bit is always 0.
     */
    SPACE(4)

}
