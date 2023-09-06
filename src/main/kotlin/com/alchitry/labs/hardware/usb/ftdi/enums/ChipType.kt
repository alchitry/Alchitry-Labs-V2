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
 * Enumerated list of recognized Ftdi chip types supported in this library.
 *
 * @author Jesse Caulfield
 */
enum class ChipType(val chipType: Int) {
    TYPE_AM(0),
    TYPE_BM(1),
    TYPE_2232C(2),
    TYPE_R(3),
    TYPE_2232H(4),
    TYPE_4232H(5),
    TYPE_232H(6),
    TYPE_230X(7)

}
