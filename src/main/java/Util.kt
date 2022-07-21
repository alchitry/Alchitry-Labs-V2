package com.alchitry.labs

import com.alchitry.labs.parsers.BigFunctions
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

object Util {
    fun minWidthNum(i: BigInteger): Int {
        return if (i.compareTo(BigInteger.ZERO) != 0) BigFunctions.ln(BigDecimal(i), 10).divide(BigFunctions.LOG2, RoundingMode.HALF_UP).setScale(0, RoundingMode.FLOOR).toInt() + 1 else 1
    }

    fun minWidthNum(i: Long): Int {
        return if (i != 0L) floor(ln(i.toDouble()) / ln(2.0)).toInt() + 1 else 1
    }

    fun minWidthNum(str: String, base: Int): Int {
        val i = str.toLong(base)
        return minWidthNum(i)
    }

    fun widthOfMult(w1: Long, w2: Long): Int {
        return floor(ln((2.0.pow(w1.toDouble()) - 1) * (2.0.pow(w2.toDouble()) - 1)) / ln(2.0)).toInt() + 1 // max
        // value
    }

    fun widthOfMult(w1: Int, w2: Int): Int {
        return widthOfMult(w1.toLong(), w2.toLong()) // max
    }
}