package com.alchitry.labs.parsers.lucidv2.signals

import com.alchitry.labs.parsers.lucidv2.values.Value

data class Constant(override val name: String, val value: Value) : Named
