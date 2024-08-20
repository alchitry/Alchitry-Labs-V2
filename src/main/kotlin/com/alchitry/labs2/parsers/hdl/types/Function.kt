package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.parsers.grammar.LucidParser.FunctionBlockContext
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.values.SignalWidth
import com.alchitry.labs2.parsers.hdl.values.Value

sealed class Function(val label: String, val argCount: Int, val exprType: ExprType, val testOnly: Boolean = false) {
    object CLOG2 : Function("clog2", 1, ExprType.Known)
    object POWER : Function("pow", 2, ExprType.Known)
    object REVERSE : Function("reverse", 1, ExprType.Dynamic)
    object FLATTEN : Function("flatten", 1, ExprType.Dynamic)
    object BUILD : Function("build", -2, ExprType.Dynamic)
    object SIGNED : Function("signed", 1, ExprType.Dynamic)
    object UNSIGNED : Function("unsigned", 1, ExprType.Dynamic)
    object CDIV : Function("cdiv", 2, ExprType.Known)
    object RESIZE : Function("resize", 2, ExprType.Dynamic)
    object WIDTH : Function("width", 1, ExprType.Dynamic)
    object FIXEDPOINT : Function("fixedPoint", 3, ExprType.Known)
    object CFIXEDPOINT : Function("cFixedPoint", 3, ExprType.Known)
    object FFIXEDPOINT : Function("fFixedPoint", 3, ExprType.Known)
    object TICK : Function("tick", 0, ExprType.Dynamic, testOnly = true)
    object SILENTTICK : Function("silentTick", 0, ExprType.Dynamic, true)
    object ASSERT : Function("assert", 1, ExprType.Dynamic, testOnly = true)
    object PRINT : Function("print", -1, ExprType.Dynamic, testOnly = true)

    class Custom(label: String, val args: List<CustomArg>, val functionBlock: FunctionBlockContext) :
        Function(label, args.size, ExprType.Dynamic, true)

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