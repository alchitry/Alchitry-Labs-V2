package com.alchitry.labs2.parsers.hdl.values

import com.alchitry.labs2.parsers.BitUtil
import java.math.BigInteger
import kotlin.experimental.and
import kotlin.math.log2
import kotlin.math.roundToInt

fun BitListValue(
    bitValues: List<BitValue>,
    signed: Boolean = bitValues.all { it.signed }
) = BitListValue(bitValues.map { it.bit }, signed)

fun BitListValue(
    str: String,
    radix: Int = 10,
    width: Int? = null,
    signed: Boolean
): BitListValue {
    if (radix == 10)
        return if (width != null)
            BitListValue(BigInteger(str), signed, width)
        else
            BitListValue(BigInteger(str), signed)

    val sigWidth = width ?: (str.length * log2(radix.toFloat()).roundToInt())

    val bits = mutableListOf<Bit>()
    val strLower = str.lowercase()
    when (radix) {
        16, 8 -> {
            var idx = 0
            val bitsPerChar = if (radix == 16) 4 else 3
            while (idx < sigWidth) {
                val charIdx = idx / bitsPerChar
                val bitIdx = idx % bitsPerChar

                val c = when {
                    strLower.length > charIdx -> strLower[strLower.length - 1 - charIdx]
                    strLower[0] == 'x' || strLower[0] == 'z' -> strLower[0]
                    else -> '0'
                }

                if (c == 'x') {
                    bits.add(Bit.Bx)
                } else if (c == 'z') {
                    bits.add(Bit.Bz)
                } else {
                    val v = c.toString().toInt(16)
                    if (v and (1 shl bitIdx) != 0) {
                        bits.add(Bit.B1)
                    } else {
                        bits.add(Bit.B0)
                    }
                }
                idx++
            }
        }

        2 -> {
            var idx = 0
            while (idx < sigWidth) {
                val c = when {
                    strLower.length > idx -> strLower[strLower.length - 1 - idx]
                    strLower[0] == 'x' || strLower[0] == 'z' -> strLower[0]
                    else -> '0'
                }
                when (c) {
                    '0' -> bits.add(Bit.B0)
                    '1' -> bits.add(Bit.B1)
                    'x' -> bits.add(Bit.Bx)
                    'z' -> bits.add(Bit.Bz)
                    else -> throw NumberFormatException()
                }
                idx++
            }
        }

        256 -> {
            var idx = 0
            while (idx < sigWidth) {
                val charIdx = idx / 8
                val bitIdx = idx % 8
                val c = if (str.length > charIdx)
                    str[str.length - 1 - charIdx]
                else
                    0.toChar()
                if (c.code and (1 shl bitIdx) != 0) {
                    bits.add(Bit.B1)
                } else {
                    bits.add(Bit.B0)
                }
                idx++
            }
        }

        else -> throw IllegalArgumentException("Radix must be 256, 16, 10, or 2")
    }
    return BitListValue(bits, signed)
}

fun BigInteger.minBits(signed: Boolean = signum() == -1): Int {
    var w = bitLength() // doesn't include sign bit
    if (signed) w++
    return w.coerceAtLeast(1)
}

fun BigInteger.toBitListValue(
    signed: Boolean = signum() == -1,
    width: Int = minBits(signed)
) = BitListValue(this, signed, width)

fun BitListValue(
    bigInt: BigInteger,
    signed: Boolean = bigInt.signum() == -1,
    width: Int = bigInt.minBits(signed)
): BitListValue {
    val bList = bigInt.toByteArray()
    val bits = mutableListOf<Bit>()

    for (i in 0 until width) {
        val idx = i / 8
        val offset = i % 8
        var b: Byte = 0
        if (bList.size > idx)
            b = bList[bList.size - 1 - idx]
        else if (signed)
            b = (if (bList[0].toInt() and (1 shl 7) != 0) -1 else 0).toByte() // sign extend

        if (b and (1 shl offset).toByte() != 0.toByte()) {
            bits.add(Bit.B1)
        } else {
            bits.add(Bit.B0)
        }
    }

    return BitListValue(bits, signed)
}

fun BitListValue(
    value: Long,
    width: Int = BitUtil.minWidthNum(value),
    signed: Boolean
): BitListValue =
    BitListValue(width, signed) {
        if ((value and (1L shl it)) != 0L) Bit.B1 else Bit.B0
    }

fun BitListValue(
    value: Int,
    width: Int = BitUtil.minWidthNum(value),
    signed: Boolean
): BitListValue =
    BitListValue(width, signed) {
        if ((value and (1 shl it)) != 0) Bit.B1 else Bit.B0
    }
