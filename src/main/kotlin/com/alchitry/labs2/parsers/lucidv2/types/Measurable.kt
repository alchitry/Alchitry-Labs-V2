package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.parsers.lucidv2.values.SignalWidth

/**
 * A measurable object provides a width that can be read using the $widthOf() function.
 */
interface Measurable {
    val width: SignalWidth
}