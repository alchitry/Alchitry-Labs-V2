package com.alchitry.labs2.parsers.lucidv2.types

import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.parsers.grammar.LucidParser.FunctionBlockContext
import com.alchitry.labs2.parsers.lucidv2.values.SignalWidth
import com.alchitry.labs2.parsers.lucidv2.values.Value

sealed class Function(val label: String, val argCount: Int, val constOnly: Boolean, val testOnly: Boolean = false) {
    object CLOG2 : Function("clog2", 1, true)
    object POWER : Function("pow", 2, true)
    object REVERSE : Function("reverse", 1, true)
    object FLATTEN : Function("flatten", 1, false)
    object BUILD : Function("build", -2, false)
    object SIGNED : Function("signed", 1, false)
    object UNSIGNED : Function("unsigned", 1, false)
    object CDIV : Function("cdiv", 2, true)
    object RESIZE : Function("resize", 2, true)
    object WIDTH : Function("width", 1, false)
    object FIXEDPOINT : Function("fixedPoint", 3, true)
    object CFIXEDPOINT : Function("cFixedPoint", 3, true)
    object FFIXEDPOINT : Function("fFixedPoint", 3, true)
    object TICK : Function("tick", 0, constOnly = false, testOnly = true)
    object SILENTTICK : Function("silentTick", 0, false, true)
    object ASSERT : Function("assert", 1, constOnly = false, testOnly = true)
    object PRINT : Function("print", -1, constOnly = false, testOnly = true)

    class Custom(label: String, val args: List<CustomArg>, val functionBlock: FunctionBlockContext) :
        Function(label, args.size, false, true)

    companion object {
        val builtIn = Function::class.allSealedObjects()
    }
}

data class CustomArg(
    val name: String,
    val width: SignalWidth,
    val signed: Boolean
)

sealed class FunctionArg {
    class ValueArg(val value: Value) : FunctionArg() {
        override fun toString() = value.toString()
    }

    class RealArg(val value: Double) : FunctionArg() {
        override fun toString() = value.toString()
    }

    abstract override fun toString(): String
}