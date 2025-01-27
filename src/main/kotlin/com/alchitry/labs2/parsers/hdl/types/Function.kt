package com.alchitry.labs2.parsers.hdl.types

import com.alchitry.labs2.allSealedObjects
import com.alchitry.labs2.parsers.grammar.LucidParser.FunctionBlockContext
import com.alchitry.labs2.parsers.hdl.ExprType
import com.alchitry.labs2.parsers.hdl.values.*
import java.math.BigInteger

sealed class Function(val label: String, val argCount: Int, val exprType: ExprType, val testOnly: Boolean = false) {
    data object CLOG2 : Function("clog2", 1, ExprType.Known) {
        fun compute(value: Value): Value? {
            val bigInt = (value as? SimpleValue)?.toBigInt() ?: return null
            if (bigInt == BigInteger.ZERO) {
                return BitValue(Bit.B0, false)
            }
            if (bigInt < BigInteger.ZERO) {
                return null
            }
            return BitListValue(bigInt.subtract(BigInteger.ONE).bitLength(), signed = false)
        }
    }

    data object POWER : Function("pow", 2, ExprType.Known)
    data object REVERSE : Function("reverse", 1, ExprType.Dynamic)
    data object FLATTEN : Function("flatten", 1, ExprType.Dynamic)
    data object BUILD : Function("build", -2, ExprType.Dynamic)
    data object SIGNED : Function("signed", 1, ExprType.Dynamic)
    data object UNSIGNED : Function("unsigned", 1, ExprType.Dynamic)
    data object CDIV : Function("cdiv", 2, ExprType.Known)
    data object RESIZE : Function("resize", 2, ExprType.Dynamic)
    data object WIDTH : Function("width", -1, ExprType.Dynamic)
    data object FIXEDPOINT : Function("fixed_point", 3, ExprType.Known)
    data object CFIXEDPOINT : Function("c_fixed_point", 3, ExprType.Known)
    data object FFIXEDPOINT : Function("f_fixed_point", 3, ExprType.Known)
    data object TICK : Function("tick", 0, ExprType.Dynamic, testOnly = true)
    data object SILENTTICK : Function("silent_tick", 0, ExprType.Dynamic, true)
    data object ASSERT : Function("assert", 1, ExprType.Dynamic, testOnly = true)
    data object PRINT : Function("print", -1, ExprType.Dynamic, testOnly = true)
    data object IS_SIMULATION : Function("is_sim", 0, ExprType.Dynamic)

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