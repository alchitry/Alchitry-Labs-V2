package com.alchitry.labs2.parsers

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.floor
import kotlin.math.ln

object BitUtil {
    /**
     * Returns the minimum number of bits needed to represent the provided value.
     * @param i the value to calculate the minimum number of bits for
     * @return the minimum number of bits needed to represent i
     */
    fun minWidthNum(i: BigInteger): Int {
        return if (i.compareTo(BigInteger.ZERO) != 0) BigFunctions.ln(BigDecimal(i), 10)
            .divide(BigFunctions.LOG2, RoundingMode.HALF_UP).setScale(0, RoundingMode.FLOOR).toInt() + 1 else 1
    }

    /**
     * Returns the minimum number of bits needed to represent the provided value.
     * @param i the value to calculate the minimum number of bits for
     * @return the minimum number of bits needed to represent i
     */
    fun minWidthNum(i: Int): Int {
        return if (i != 0) floor(ln(i.toDouble()) / ln(2.0)).toInt() + 1 else 1
    }

    /**
     * Returns the minimum number of bits needed to represent the provided value.
     * @param i the value to calculate the minimum number of bits for
     * @return the minimum number of bits needed to represent i
     */
    fun minWidthNum(i: Long): Int {
        return if (i != 0L) floor(ln(i.toDouble()) / ln(2.0)).toInt() + 1 else 1
    }

    /**
     * Returns the minimum number of bits needed to represent the provided value.
     * @param str the value to calculate the minimum number of bits for
     * @param radix the radix of str (base 2, 10, or 16)
     * @return the minimum number of bits needed to represent i
     */
    fun minWidthNum(str: String, radix: Int): Int {
        val i = str.toLong(radix)
        return minWidthNum(i)
    }
}