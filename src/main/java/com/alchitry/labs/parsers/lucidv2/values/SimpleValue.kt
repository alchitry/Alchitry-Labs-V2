package com.alchitry.labs.parsers.lucidv2.values

import com.alchitry.labs.parsers.lucidv2.signals.SignalSelectionException
import com.alchitry.labs.parsers.lucidv2.signals.SignalSelector
import java.math.BigInteger
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.ceil

data class SimpleValue(
    val bits: List<Bit>,
    override val constant: Boolean,
    val signed: Boolean
) : Value(), List<Bit> by bits {
    constructor(constant: Boolean, signed: Boolean, size: Int, init: (Int) -> Bit) : this(
        List(size, init),
        constant,
        signed
    )

    fun toBigInt(): BigInteger {
        check(isNumber()) { "The value is not a number (it contains x, z, or u values)" }
        val bytes =
            ByteArray(ceil((size + if (signed) 0 else 1).toDouble() / 8.0).toInt()) // if not signed need extra 0 sign bit
        if (signed && msb == Bit.B1) // sign extension
            Arrays.fill(bytes, 255.toByte()) else Arrays.fill(bytes, 0.toByte())
        repeat(size) { i ->
            val idx: Int = i / 8
            bytes[bytes.size - 1 - idx] = if (this[i] == Bit.B1)
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
        this.forEach {
            if (it == Bit.B1)
                return BitValue(Bit.B1, constant, false)
            else if (it == Bit.Bx || it == Bit.Bz)
                hasX = true
        }
        return BitValue(if (hasX) Bit.Bx else Bit.B0, constant, false)
    }

    fun isDriven(): Boolean = none { it == Bit.Bu }
    override fun isNumber(): Boolean = none { !it.isNumber() }

    override fun asMutable(): SimpleValue = copy(constant = false)

    override val signalWidth: SimpleWidth = SimpleWidth(bits.size)

    override fun invert(): SimpleValue = copy(bits = bits.map { !it })

    fun resize(width: Int, signExtend: Boolean = signed): SimpleValue {
        if (width == size)
            return copy(signed = signExtend)
        if (width < size)
            return SimpleValue(subList(0, width), signExtend, constant)
        val extendBit = if (!signExtend && msb == Bit.B1) Bit.B0 else msb
        val newBits = bits.toMutableList()
        repeat(width - size) { newBits.add(extendBit) }
        return SimpleValue(newBits, constant, signExtend)
    }

    /** Changes sign without changing bits */
    fun setSign(signed: Boolean): SimpleValue = copy(signed = signed)

    fun isNegative(): Boolean = signed && msb == Bit.B1

    infix fun isLessThan(other: SimpleValue): BitValue {
        val longest = size.coerceAtLeast(other.size)
        val signedOp = signed && other.signed
        val constantOp = constant && other.constant
        val op1 = setSign(signedOp).resize(longest)
        val op2 = other.setSign(signedOp).resize(longest)

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

    fun toBoolean() = isTrue().bit == Bit.B1

    fun select(selection: SignalSelector.Bits): Value {
        try {
            val newBits = bits.subList(selection.range.first, selection.range.last + 1)
            return copy(bits = newBits)
        } catch (e: IndexOutOfBoundsException) {
            throw SignalSelectionException(
                selection,
                "Selection $selection is outside the bounds of [${bits.size - 1}:0]"
            )
        }
    }

    private inline fun doOp(b: SimpleValue, crossinline op: (Bit, Bit) -> Bit): SimpleValue {
        val size = size.coerceAtLeast(b.size)
        val signedOp = signed && b.signed
        val constant = constant && b.constant
        val op1 = resize(size, signedOp)
        val op2 = b.resize(size, signedOp)
        return SimpleValue(constant, signedOp, size) { i ->
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

    infix fun shl(n: Int): SimpleValue {
        val bits = mutableListOf<Bit>()
        repeat(n) { bits.add(Bit.B0) }
        bits.addAll(this)
        return SimpleValue(bits, constant, signed)
    }

    infix fun ushl(n: Int): SimpleValue {
        val bits = mutableListOf<Bit>()
        repeat(n) { bits.add(Bit.B0) }
        bits.addAll(this)
        return SimpleValue(bits, constant, false)
    }

    infix fun shr(n: Int): SimpleValue {
        val bits = mutableListOf<Bit>()
        bits.addAll(subList(n, size))
        val signBit = if (signed) msb else Bit.B0
        repeat(n) { bits.add(signBit) }
        return SimpleValue(bits, constant, signed)
    }

    infix fun ushr(n: Int): SimpleValue {
        val bits = mutableListOf<Bit>()
        bits.addAll(subList(n, size))
        repeat(n) { bits.add(Bit.B0) }
        return SimpleValue(bits, constant, signed)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (constant)
            sb.append("const ")
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

    override fun subList(fromIndex: Int, toIndex: Int): SimpleValue {
        return SimpleValue(bits.subList(fromIndex, toIndex), constant, signed)
    }
}

/** Least significant bit */
val List<Bit>.lsb get() = first()

/** Most significant bit */
val List<Bit>.msb get() = last()