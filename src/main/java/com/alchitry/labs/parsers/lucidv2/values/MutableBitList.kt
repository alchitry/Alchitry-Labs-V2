package com.alchitry.labs.parsers.lucidv2.values

import java.math.BigInteger
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

interface BitList : List<BitValue> {
    val signed: Boolean

    fun toBigInt(): BigInteger {
        check(isNumber()) { "The value is not a number (it contains x and z values)" }
        val bytes =
            ByteArray(ceil((size + if (signed) 0 else 1).toDouble() / 8.0).toInt()) // if not signed need extra 0 sign bit
        if (signed && msb == BitValue.B1) // sign extension
            Arrays.fill(bytes, 255.toByte()) else Arrays.fill(bytes, 0.toByte())
        repeat(size) { i ->
            val idx: Int = i / 8
            bytes[bytes.size - 1 - idx] = if (this[i] == BitValue.B1)
                bytes[bytes.size - 1 - idx] or (1 shl i % 8).toByte()
            else
                bytes[bytes.size - 1 - idx] and (1 shl i % 8).inv().toByte()
        }
        return BigInteger(bytes)
    }

    operator fun not(): BitValue {
        return isTrue().not()
    }

    fun isTrue(): BitValue {
        var hasX = false
        this.forEach {
            if (it == BitValue.B1)
                return BitValue.B1
            else if (it == BitValue.Bx || it == BitValue.Bz)
                hasX = true
        }
        return if (hasX) BitValue.Bx else BitValue.B0
    }

    fun invert(): BitList {
        return MutableBitList(this.signed, this.size) { !this[it] }
    }

    fun resize(width: Int, signExtend: Boolean = signed): BitList {
        if (width == size)
            return MutableBitList(signExtend, this)
        if (width < size)
            return MutableBitList(signExtend, subList(0, width))
        val extendBit = if (!signExtend && msb == BitValue.B1) BitValue.B0 else msb
        val newValue = toMutableBitArray()
        newValue.signed = signExtend
        repeat(width - size) { newValue.add(extendBit) }
        return newValue
    }

    private inline fun doOp(b: BitList, crossinline op: (BitValue, BitValue) -> BitValue): BitList {
        val size = size.coerceAtLeast(b.size)
        val signedOp = signed && b.signed
        val op1 = resize(size, signedOp)
        val op2 = b.resize(size, signedOp)
        return MutableBitList(signedOp, size) { i ->
            op(op1[i], op2[i])
        }
    }

    infix fun or(b: BitList): BitList {
        return doOp(b) { b1, b2 -> b1 or b2 }
    }

    infix fun and(b: BitList): BitList {
        return doOp(b) { b1, b2 -> b1 and b2 }
    }

    infix fun xor(b: BitList): BitList {
        return doOp(b) { b1, b2 -> b1 xor b2 }
    }

    infix fun nor(b: BitList): BitList {
        return doOp(b) { b1, b2 -> b1 nor b2 }
    }

    infix fun nand(b: BitList): BitList {
        return doOp(b) { b1, b2 -> b1 nand b2 }
    }

    infix fun xnor(b: BitList): BitList {
        return doOp(b) { b1, b2 -> b1 xnor b2 }
    }

    infix fun shl(n: Int): BitList {
        return MutableBitList(signed, n) { BitValue.B0 }.also { it.addAll(this) }
    }

    infix fun ushl(n: Int): BitList {
        return MutableBitList(false, n) { BitValue.B0 }.also { it.addAll(this) }
    }

    infix fun shr(n: Int): BitList {
        val value = MutableBitList(signed)
        val signBit = if (signed) msb else BitValue.B0
        value.addAll(subList(n, size))
        repeat(n) { value.add(signBit) }
        return value
    }

    infix fun ushr(n: Int): BitList {
        val value = MutableBitList(false)
        value.addAll(subList(n, size))
        repeat(n) { value.add(BitValue.B0) }
        return value
    }

    fun isNumber(): Boolean {
        this.forEach { if (it != BitValue.B0 && it != BitValue.B1) return false }
        return true
    }

    fun isNegative(): Boolean = signed && msb == BitValue.B1

    fun toMutableBitArray(): MutableBitList = MutableBitList(this)
    fun toSimpleValue(): SimpleValue = SimpleValue(this)

    override fun subList(fromIndex: Int, toIndex: Int): BitList

    /**
     * Returns the minimum number of bits needed to represent this value
     */
    fun minBits(): Int {
        if (isNegative()) {
            for (i in indices.reversed()) {
                if (get(i) != BitValue.B1)
                    return i + 2
            }
        } else {
            for (i in indices.reversed()) {
                if (get(1) != BitValue.B0)
                    return i + 1 + if (signed) 1 else 0
            }
        }
        return 1
    }
}

class MutableBitList(override var signed: Boolean = false, width: Int = 0) : ArrayList<BitValue>(width), BitList {
    constructor(signed: Boolean, size: Int, init: (Int) -> BitValue) : this(signed, size) {
        for (i in 0 until size) add(init(i))
    }

    constructor(str: String, radix: Int = 10, signed: Boolean = false) : this(signed, 0) {
        when (radix) {
            10 -> fromBigInt(BigInteger(str))
            16 -> set(str, 16, str.length * 4, signed)
            2 -> set(str, 2, str.length, signed)
            256 -> set(str, 256, str.length * 8, signed)
            else -> throw IllegalArgumentException("Radix must be 2, 10, 16, or 256")
        }
    }

    constructor(str: String, radix: Int = 10, width: Int, signed: Boolean = false) : this(signed, width) {
        set(str, radix, width, signed)
    }

    constructor(value: Long, width: Int, signed: Boolean = false) : this(signed, width) {
        repeat(width) {
            add(if ((value and (1 shr it).toLong()) != 0.toLong()) BitValue.B1 else BitValue.B0)
        }
    }

    constructor(value: BitValue, width: Int = 1, signed: Boolean = false) : this(signed, width) {
        repeat(width) { add(value) }
    }

    constructor(value: BigInteger) : this() {
        fromBigInt(value)
    }

    constructor(value: BigInteger, width: Int, signed: Boolean = value.signum() == -1) : this(width = width) {
        fromBigInt(value, width, signed)
    }

    constructor(signed: Boolean, bits: List<BitValue>) : this(signed, bits.size) {
        addAll(bits)
    }

    constructor(bitList: BitList) : this(bitList.signed, bitList)

    override fun subList(fromIndex: Int, toIndex: Int): MutableBitList {
        return MutableBitList(signed, super.subList(fromIndex, toIndex))
    }

    private fun fromBigInt(bigInt: BigInteger) {
        var w = bigInt.bitLength() // doesn't include sign bit

        if (bigInt.signum() == -1) w++
        w = w.coerceAtLeast(1)
        fromBigInt(bigInt, w)
    }

    private fun fromBigInt(bigInt: BigInteger, width: Int, signed: Boolean = bigInt.signum() == -1) {
        val bList = bigInt.toByteArray()

        for (i in 0 until width) {
            val idx = i / 8
            val offset = i % 8
            var b: Byte = 0
            if (bList.size > idx)
                b = bList[bList.size - 1 - idx]
            else if (signed)
                b = (if (bList[0].toInt() and (1 shl 7) != 0) -1 else 0).toByte() // sign extend

            if (b and (1 shl offset).toByte() != 0.toByte()) {
                add(BitValue.B1)
            } else {
                add(BitValue.B0)
            }
        }
    }

    private fun set(str: String, radix: Int, width: Int, signed: Boolean) {
        this.signed = signed
        val strlower = str.lowercase()
        when (radix) {
            10 -> fromBigInt(BigInteger(strlower), width)
            16 -> {
                var idx = 0
                while (idx < width) {
                    val charIdx = idx / 4
                    val bitIdx = idx % 4
                    var c = '0'
                    if (strlower.length > charIdx) c =
                        strlower[strlower.length - 1 - charIdx] else if (strlower[0] == 'x' || strlower[0] == 'z') c =
                        strlower[0]
                    if (c == 'x') {
                        add(BitValue.Bx)
                    } else if (c == 'z') {
                        add(BitValue.Bz)
                    } else {
                        val v = c.toString().toInt(16)
                        if (v and (1 shl bitIdx) != 0) {
                            add(BitValue.B1)
                        } else {
                            add(BitValue.B0)
                        }
                    }
                    idx++
                }
            }
            2 -> {
                var idx = 0
                while (idx < width) {
                    var c = '0'
                    if (strlower.length > idx) c =
                        strlower[strlower.length - 1 - idx] else if (strlower[0] == 'x' || strlower[0] == 'z') c =
                        strlower[0]
                    when (c) {
                        '0' -> add(BitValue.B0)
                        '1' -> add(BitValue.B1)
                        'x' -> add(BitValue.Bx)
                        'z' -> add(BitValue.Bz)
                        else -> throw NumberFormatException()
                    }
                    idx++
                }
            }
            256 -> {
                var idx = 0
                while (idx < width) {
                    val charIdx = idx / 8
                    val bitIdx = idx % 8
                    var c = 0.toChar()
                    if (str.length > charIdx) c = str[str.length - 1 - charIdx]
                    if (c.code and (1 shl bitIdx) != 0) {
                        add(BitValue.B1)
                    } else {
                        add(BitValue.B0)
                    }
                    idx++
                }
            }
            else -> throw IllegalArgumentException("Radix must be 16, 10, or 2")
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (signed)
            sb.append("signed ")
        sb.append('{')
        for (i in this.indices.reversed()) {
            val bv = this[i]
            sb.append(bv.toString().substring(1))
        }
        sb.append('}')
        return sb.toString()
    }
}

/** Least significant bit */
val List<BitValue>.lsb get() = first()

/** Most significant bit */
val List<BitValue>.msb get() = last()