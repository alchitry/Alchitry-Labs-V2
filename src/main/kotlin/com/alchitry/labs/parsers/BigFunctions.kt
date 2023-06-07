package com.alchitry.labs.parsers

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

/*Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.*/ /**
 * Several useful BigDecimal mathematical functions.
 */
object BigFunctions {
    val LOG2 = ln(BigDecimal(2), 32)

    /**
     * Compute x^exponent to a given scale.  Uses the same
     * algorithm as class numbercruncher.mathutils.IntPower.
     * @param x the value x
     * @param exponent the exponent value
     * @param scale the desired scale of the result
     * @return the result value
     */
    fun intPower(
        x: BigDecimal, exponent: Long,
        scale: Int
    ): BigDecimal {
        // If the exponent is negative, compute 1/(x^-exponent).
        var mutableX = x
        var mutableExp = exponent
        if (mutableExp < 0) {
            return BigDecimal.valueOf(1)
                .divide(
                    intPower(mutableX, -mutableExp, scale), scale,
                    RoundingMode.HALF_EVEN
                )
        }
        var power = BigDecimal.valueOf(1)

        // Loop to compute value^exponent.
        while (mutableExp > 0) {

            // Is the rightmost bit a 1?
            if (mutableExp and 1L == 1L) {
                power = power.multiply(mutableX)
                    .setScale(scale, RoundingMode.HALF_EVEN)
            }

            // Square x and shift exponent 1 bit to the right.
            mutableX = mutableX.multiply(mutableX)
                .setScale(scale, RoundingMode.HALF_EVEN)
            mutableExp = mutableExp shr 1
            Thread.yield()
        }
        return power
    }

    /**
     * Compute the integral root of x to a given scale, x >= 0.
     * Use Newton's algorithm.
     * @param x the value of x
     * @param index the integral root value
     * @param scale the desired scale of the result
     * @return the result value
     */
    fun intRoot(
        x: BigDecimal, index: Long,
        scale: Int
    ): BigDecimal {
        // Check that x >= 0.
        var result = x
        require(result.signum() >= 0) { "x < 0" }
        val sp1 = scale + 1
        val n = result
        val i = BigDecimal.valueOf(index)
        val im1 = BigDecimal.valueOf(index - 1)
        val tolerance = BigDecimal.valueOf(5)
            .movePointLeft(sp1)
        var xPrev: BigDecimal

        // The initial approximation is x/index.
        result = result.divide(i, scale, RoundingMode.HALF_EVEN)

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            // x^(index-1)
            val xToIm1 = intPower(result, index - 1, sp1)

            // x^index
            val xToI = result.multiply(xToIm1)
                .setScale(sp1, RoundingMode.HALF_EVEN)

            // n + (index-1)*(x^index)
            val numerator = n.add(im1.multiply(xToI))
                .setScale(sp1, RoundingMode.HALF_EVEN)

            // (index*(x^(index-1))
            val denominator = i.multiply(xToIm1)
                .setScale(sp1, RoundingMode.HALF_EVEN)

            // x = (n + (index-1)*(x^index)) / (index*(x^(index-1)))
            xPrev = result
            result = numerator
                .divide(denominator, sp1, RoundingMode.DOWN)
            Thread.yield()
        } while (result.subtract(xPrev).abs().compareTo(tolerance) > 0)
        return result
    }

    /**
     * Compute e^x to a given scale.
     * Break x into its whole and fraction parts and
     * compute (e^(1 + fraction/whole))^whole using Taylor's formula.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    fun exp(x: BigDecimal, scale: Int): BigDecimal {
        // e^0 = 1
        if (x.signum() == 0) {
            return BigDecimal.valueOf(1)
        } else if (x.signum() == -1) {
            return BigDecimal.valueOf(1)
                .divide(
                    exp(x.negate(), scale), scale,
                    RoundingMode.HALF_EVEN
                )
        }

        // Compute the whole part of x.
        var xWhole = x.setScale(0, RoundingMode.HALF_EVEN)

        // If there isn't a whole part, compute and return e^x.
        if (xWhole.signum() == 0) return expTaylor(x, scale)

        // Compute the fraction part of x.
        val xFraction = x.subtract(xWhole)

        // z = 1 + fraction/whole
        val z = BigDecimal.valueOf(1)
            .add(
                xFraction.divide(
                    xWhole, scale,
                    RoundingMode.HALF_EVEN
                )
            )

        // t = e^z
        val t = expTaylor(z, scale)
        val maxLong = BigDecimal.valueOf(Long.MAX_VALUE)
        var result = BigDecimal.valueOf(1)

        // Compute and return t^whole using intPower().
        // If whole > Long.MAX_VALUE, then first compute products
        // of e^Long.MAX_VALUE.
        while (xWhole.compareTo(maxLong) >= 0) {
            result = result.multiply(
                intPower(t, Long.MAX_VALUE, scale)
            )
                .setScale(scale, RoundingMode.HALF_EVEN)
            xWhole = xWhole.subtract(maxLong)
            Thread.yield()
        }
        return result.multiply(intPower(t, xWhole.toLong(), scale))
            .setScale(scale, RoundingMode.HALF_EVEN)
    }

    /**
     * Compute e^x to a given scale by the Taylor series.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    private fun expTaylor(x: BigDecimal, scale: Int): BigDecimal {
        var factorial = BigDecimal.valueOf(1)
        var xPower = x
        var sumPrev: BigDecimal?

        // 1 + x
        var sum = x.add(BigDecimal.valueOf(1))

        // Loop until the sums converge
        // (two successive sums are equal after rounding).
        var i = 2
        do {
            // x^i
            xPower = xPower.multiply(x)
                .setScale(scale, RoundingMode.HALF_EVEN)

            // i!
            factorial = factorial.multiply(BigDecimal.valueOf(i.toLong()))

            // x^i/i!
            val term = xPower
                .divide(
                    factorial, scale,
                    RoundingMode.HALF_EVEN
                )

            // sum = sum + x^i/i!
            sumPrev = sum
            sum = sum.add(term)
            ++i
            Thread.yield()
        } while (sum.compareTo(sumPrev) != 0)
        return sum
    }

    /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     */
    fun ln(x: BigDecimal, scale: Int): BigDecimal {
        // Check that x > 0.
        require(x.signum() > 0) { "x <= 0" }

        // The number of digits to the left of the decimal point.
        val magnitude = x.toString().length - x.scale() - 1
        return if (magnitude < 3) {
            lnNewton(x, scale)
        } else {

            // x^(1/magnitude)
            val root = intRoot(x, magnitude.toLong(), scale)

            // ln(x^(1/magnitude))
            val lnRoot = lnNewton(root, scale)

            // magnitude*ln(x^(1/magnitude))
            BigDecimal.valueOf(magnitude.toLong()).multiply(lnRoot)
                .setScale(scale, RoundingMode.HALF_EVEN)
        }
    }

    /**
     * Compute the natural logarithm of x to a given scale, x > 0.
     * Use Newton's algorithm.
     */
    private fun lnNewton(x: BigDecimal, scale: Int): BigDecimal {
        var result = x
        val sp1 = scale + 1
        val n = result
        var term: BigDecimal

        // Convergence tolerance = 5*(10^-(scale+1))
        val tolerance = BigDecimal.valueOf(5)
            .movePointLeft(sp1)

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {

            // e^x
            val eToX = exp(result, sp1)

            // (e^x - n)/e^x
            term = eToX.subtract(n)
                .divide(eToX, sp1, RoundingMode.DOWN)

            // x - (e^x - n)/e^x
            result = result.subtract(term)
            Thread.yield()
        } while (term.compareTo(tolerance) > 0)
        return result.setScale(scale, RoundingMode.HALF_EVEN)
    }

    /**
     * Compute the arctangent of x to a given scale, |x| < 1
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    fun arctan(x: BigDecimal, scale: Int): BigDecimal {
        // Check that |x| < 1.
        require(x.abs().compareTo(BigDecimal.valueOf(1)) < 0) { "|x| >= 1" }

        // If x is negative, return -arctan(-x).
        return if (x.signum() == -1) {
            arctan(x.negate(), scale).negate()
        } else {
            arctanTaylor(x, scale)
        }
    }

    /**
     * Compute the arctangent of x to a given scale
     * by the Taylor series, |x| < 1
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    private fun arctanTaylor(x: BigDecimal, scale: Int): BigDecimal {
        val sp1 = scale + 1
        var i = 3
        var addFlag = false
        var power = x
        var sum = x
        var term: BigDecimal

        // Convergence tolerance = 5*(10^-(scale+1))
        val tolerance = BigDecimal.valueOf(5)
            .movePointLeft(sp1)

        // Loop until the approximations converge
        // (two successive approximations are within the tolerance).
        do {
            // x^i
            power = power.multiply(x).multiply(x)
                .setScale(sp1, RoundingMode.HALF_EVEN)

            // (x^i)/i
            term = power.divide(
                BigDecimal.valueOf(i.toLong()), sp1,
                RoundingMode.HALF_EVEN
            )

            // sum = sum +- (x^i)/i
            sum = if (addFlag) sum.add(term) else sum.subtract(term)
            i += 2
            addFlag = !addFlag
            Thread.yield()
        } while (term.compareTo(tolerance) > 0)
        return sum
    }

    /**
     * Compute the square root of x to a given scale, x >= 0.
     * Use Newton's algorithm.
     * @param x the value of x
     * @param scale the desired scale of the result
     * @return the result value
     */
    fun sqrt(x: BigDecimal, scale: Int): BigDecimal {
        // Check that x >= 0.
        require(x.signum() >= 0) { "x < 0" }

        // n = x*(10^(2*scale))
        val n = x.movePointRight(scale shl 1).toBigInteger()

        // The first approximation is the upper half of n.
        val bits = n.bitLength() + 1 shr 1
        var ix = n.shiftRight(bits)
        var ixPrev: BigInteger?

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            ixPrev = ix

            // x = (x + n/x)/2
            ix = ix.add(n.divide(ix)).shiftRight(1)
            Thread.yield()
        } while (ix.compareTo(ixPrev) != 0)
        return BigDecimal(ix, scale)
    }
}