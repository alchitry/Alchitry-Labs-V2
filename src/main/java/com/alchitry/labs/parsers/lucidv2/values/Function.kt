package com.alchitry.labs.parsers.lucidv2.values

enum class Function(val label: String, val argCount: Int, val constOnly: Boolean) {
    CLOG2("\$clog2", 1, true),
    POWER("\$pow", 2, true),
    REVERSE("\$reverse", 1, true),
    FLATTEN("\$flatten", 1, false),
    BUILD("\$build", -2, false),
    SIGNED("\$signed", 1, false),
    UNSIGNED("\$unsigned", 1, false),
    CDIV("\$cdiv", 2, true),
    RESIZE("\$resize", 2, true)
}