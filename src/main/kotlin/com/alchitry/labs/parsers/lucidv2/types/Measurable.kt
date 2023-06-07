package com.alchitry.labs.parsers.lucidv2.types

import com.alchitry.labs.parsers.lucidv2.values.SignalWidth

/**
 * A measurable object provides a width that can be read using the $width() function.
 */
interface Measurable {
    val width: SignalWidth
}