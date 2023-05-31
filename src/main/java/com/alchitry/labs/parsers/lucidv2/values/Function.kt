package com.alchitry.labs.parsers.lucidv2.values

enum class Function(val label: String, val argCount: Int, val constOnly: Boolean, val testOnly: Boolean = false) {
    CLOG2("clog2", 1, true),
    POWER("pow", 2, true),
    REVERSE("reverse", 1, true),
    FLATTEN("flatten", 1, false),
    BUILD("build", -2, false),
    SIGNED("signed", 1, false),
    UNSIGNED("unsigned", 1, false),
    CDIV("cdiv", 2, true),
    RESIZE("resize", 2, true),
    WIDTH("widthOf", 1, false),
    FIXEDPOINT("fixedPoint", 3, true),
    CFIXEDPOINT("cFixedPoint", 3, true),
    FFIXEDPOINT("fFixedPoint", 3, true),
    TICK("tick", 0, constOnly = false, testOnly = true),
    ASSERT("assert", 1, constOnly = false, testOnly = true),
    PRINT("print", 1, constOnly = false, testOnly = true)
}

sealed class FunctionArg {
    class ValueArg(val value: Value) : FunctionArg()
    class RealArg(val value: Double) : FunctionArg()
}