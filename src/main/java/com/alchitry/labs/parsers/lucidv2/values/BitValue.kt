package com.alchitry.labs.parsers.lucidv2.values

enum class BitValue {
    B0,
    B1,
    Bx,
    Bz;

    infix fun or(b: BitValue): BitValue {
        if (this == B1 || b == B1) return B1
        if (!isNumber() || !b.isNumber()) return Bx
        return B0
    }

    infix fun and(b: BitValue): BitValue {
        if (this == B1 && b == B1) return B1
        if (!isNumber() || !b.isNumber()) return Bx
        return B0
    }

    infix fun xor(b: BitValue): BitValue {
        if (!isNumber() || !b.isNumber()) return Bx
        if (this == B1 != (b == B1)) return B1
        return B0
    }

    val char: Char
        get() = when (this) {
            B0 -> '0'
            B1 -> '1'
            Bx -> 'x'
            Bz -> 'z'
        }

    infix fun nor(b: BitValue): BitValue {
        return or(b).not()
    }

    infix fun nand(b: BitValue): BitValue {
        return and(b).not()
    }

    infix fun xnor(b: BitValue): BitValue {
        return xor(b).not()
    }

    operator fun not(): BitValue {
        return invert()
    }

    fun invert(): BitValue {
        return when (this) {
            B0 -> B1
            B1 -> B0
            Bx -> Bx
            Bz -> Bx
        }
    }

    fun isNumber(): Boolean {
        return when (this) {
            B0, B1 -> true
            Bx, Bz -> false
        }
    }

    fun toBitArray(): BitList = MutableBitList(this)
    fun toSimpleValue(): SimpleValue = SimpleValue(toBitArray())
}