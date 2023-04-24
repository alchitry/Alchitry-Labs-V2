package com.alchitry.labs.parsers.lucidv2.values

import java.math.BigInteger
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

sealed class SimpleValue(
    override val constant: Boolean,
    open val signed: Boolean
) : Value() {
    abstract val size: Int
    abstract val msb: Bit
    abstract val lsb: Bit
    abstract val bits: List<Bit>
    fun asBitListValue() = if (this is BitListValue) this else BitListValue(bits, constant, signed)

    fun toBigInt(): BigInteger {
        check(isNumber()) { "The value is not a number (it contains x, z, or u values)" }
        val bytes =
            ByteArray(ceil((size + if (signed) 0 else 1).toDouble() / 8.0).toInt()) // if not signed need extra 0 sign bit
        if (signed && msb == Bit.B1) // sign extension
            Arrays.fill(bytes, 255.toByte()) else Arrays.fill(bytes, 0.toByte())
        repeat(size) { i ->
            val idx: Int = i / 8
            bytes[bytes.size - 1 - idx] = if (bits[i] == Bit.B1)
                bytes[bytes.size - 1 - idx] or (1 shl i % 8).toByte()
            else
                bytes[bytes.size - 1 - idx] and (1 shl i % 8).inv().toByte()
        }
        return BigInteger(bytes)
    }

    operator fun not(): BitValue {
        return BitValue(isTrue().bit.not(), constant, false)
    }

    override fun isTrue(): BitValue {
        var hasX = false
        bits.forEach {
            if (it == Bit.B1)
                return BitValue(Bit.B1, constant, false)
            else if (it == Bit.Bx || it == Bit.Bz)
                hasX = true
        }
        return BitValue(if (hasX) Bit.Bx else Bit.B0, constant, false)
    }

    fun isDriven(): Boolean = bits.none { it == Bit.Bu }
    override fun isNumber(): Boolean = bits.none { !it.isNumber() }

    fun isNegative(): Boolean = signed && msb == Bit.B1

    infix fun isLessThan(other: SimpleValue): BitValue {
        val longest = size.coerceAtLeast(other.size)
        val signedOp = signed && other.signed
        val constantOp = constant && other.constant
        val op1 = asBitListValue().setSign(signedOp).resize(longest)
        val op2 = other.asBitListValue().setSign(signedOp).resize(longest)

        val neg1 = signedOp && isNegative()
        val neg2 = signedOp && other.isNegative()

        if (neg1 && !neg2) // negative < positive
            return Bit.B1.toBitValue(constantOp)

        if (!neg1 && neg2) // positive !< negative
            return Bit.B0.toBitValue(constantOp)

        // negative to negative or positive to positive can be directly compared
        for (i in op1.indices.reversed()) {
            if (!op1[i].isNumber() || !op2[i].isNumber())
                return Bit.Bx.toBitValue(constantOp)

            if (op1[i] == Bit.B1 && op2[i] == Bit.B0)
                return Bit.B0.toBitValue(constantOp)

            if (op1[i] == Bit.B0 && op2[i] == Bit.B1)
                return Bit.B1.toBitValue(constantOp)
        }
        return Bit.B0.toBitValue(constantOp)
    }

    infix fun isGreaterThan(other: SimpleValue): BitValue {
        return other isLessThan this
    }

    infix fun isLessOrEqualTo(other: SimpleValue): BitValue {
        return (isLessThan(other) or isEqualTo(other))
    }

    infix fun isGreaterOrEqualTo(other: SimpleValue): BitValue {
        return (isGreaterThan(other) or isEqualTo(other))
    }

    private inline fun doOp(b: SimpleValue, crossinline op: (Bit, Bit) -> Bit): SimpleValue {
        val size = size.coerceAtLeast(b.size)
        val signedOp = signed && b.signed
        val constant = constant && b.constant
        val op1 = asBitListValue().resize(size, signedOp)
        val op2 = b.asBitListValue().resize(size, signedOp)
        return BitListValue(constant, signedOp, size) { i ->
            op(op1[i], op2[i])
        }
    }

    infix fun or(b: SimpleValue): SimpleValue {
        return doOp(b) { b1, b2 -> b1 or b2 }
    }

    infix fun and(b: SimpleValue): SimpleValue {
        return doOp(b) { b1, b2 -> b1 and b2 }
    }

    infix fun xor(b: SimpleValue): SimpleValue {
        return doOp(b) { b1, b2 -> b1 xor b2 }
    }

    infix fun nor(b: SimpleValue): SimpleValue {
        return doOp(b) { b1, b2 -> b1 nor b2 }
    }

    infix fun nand(b: SimpleValue): SimpleValue {
        return doOp(b) { b1, b2 -> b1 nand b2 }
    }

    infix fun xnor(b: SimpleValue): SimpleValue {
        return doOp(b) { b1, b2 -> b1 xnor b2 }
    }

    infix fun shl(n: Int): BitListValue {
        val bits = mutableListOf<Bit>()
        repeat(n) { bits.add(Bit.B0) }
        bits.addAll(bits)
        return BitListValue(bits, constant, signed)
    }

    infix fun ushl(n: Int): BitListValue {
        val bits = mutableListOf<Bit>()
        repeat(n) { bits.add(Bit.B0) }
        bits.addAll(bits)
        return BitListValue(bits, constant, false)
    }

    infix fun shr(n: Int): BitListValue {
        val newBits = mutableListOf<Bit>()
        newBits.addAll(bits.subList(n, size))
        val signBit = if (signed) msb else Bit.B0
        repeat(n) { newBits.add(signBit) }
        return BitListValue(newBits, constant, signed)
    }

    infix fun ushr(n: Int): BitListValue {
        val newBits = mutableListOf<Bit>()
        newBits.addAll(bits.subList(n, size))
        repeat(n) { newBits.add(Bit.B0) }
        return BitListValue(newBits, constant, signed)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (constant)
            sb.append("const ")
        if (signed)
            sb.append("signed ")
        sb.append('{')
        for (i in bits.indices.reversed()) {
            val bv = bits[i]
            sb.append(bv.toString().substring(1))
        }
        sb.append('}')
        return sb.toString()
    }
}